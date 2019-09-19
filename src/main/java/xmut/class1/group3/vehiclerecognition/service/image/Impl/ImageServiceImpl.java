package xmut.class1.group3.vehiclerecognition.service.image.Impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import xmut.class1.group3.vehiclerecognition.constants.ImageConstant;
import xmut.class1.group3.vehiclerecognition.domain.image.ImageEntity;
import xmut.class1.group3.vehiclerecognition.http.response.BaseResponse;
import xmut.class1.group3.vehiclerecognition.http.response.admin.ClassifyResultResponse;
import xmut.class1.group3.vehiclerecognition.http.response.admin.TestResultResponse;
import xmut.class1.group3.vehiclerecognition.http.vo.image.Image;
import xmut.class1.group3.vehiclerecognition.repository.image.ImageRepository;
import xmut.class1.group3.vehiclerecognition.service.image.ImageService;
import xmut.class1.group3.vehiclerecognition.web.config.ImageTestRunnable;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Override
    public BaseResponse getAllImage(Pageable pageable) {
        List<Image> imageList = converToImageList(imageRepository.findAll(pageable));
        List<ImageEntity> imageEntityList = imageRepository.findAll();
        BaseResponse response = new BaseResponse();
        response.setCount(imageEntityList.size());
        response.setData(imageList);
        return response;
    }

    @Override
    public BaseResponse addImage(String imageUrl) {
        ImageEntity imageEntity = new ImageEntity();
        BaseResponse response = new BaseResponse();
        imageEntity.setImage(imageUrl);
        Date date = new Date();
        imageEntity.setCreateTime(date);
        imageEntity.setUpdateTime(date);
        imageRepository.save(imageEntity);
        return response;
    }

    @Override
    public BaseResponse saveImage() {
        String filePath = "F:\\vehicle-recognition\\car";
        String filePath2 = "F:\\vehicle-recognition\\no-car";
        List<String> fileNames = new ArrayList<>();
        List<String> fileNames2 = new ArrayList<>();
        File file = new File(filePath);
        File file2 = new File(filePath2);
        File[] files = file.listFiles();
        File[] files2 = file2.listFiles();
        for (File file1 : files) {
            if (file1.isFile()) {
                fileNames.add("/car/" + file1.getName());
            }
        }
        for (File file1 : files2) {
            if (file1.isFile()) {
                fileNames2.add("/no-car/" + file1.getName());
            }
        }
        for (String imageUrl : fileNames) {
            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setImage(imageUrl);
            imageEntity.setIsSign(ImageConstant.IS_SIGN_TRUE);
            Date date = new Date();
            imageEntity.setCreateTime(date);
            imageEntity.setUpdateTime(date);
            imageRepository.save(imageEntity);
        }
        for (String imageUrl : fileNames2) {
            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setImage(imageUrl);
            Date date = new Date();
            imageEntity.setCreateTime(date);
            imageEntity.setUpdateTime(date);
            imageRepository.save(imageEntity);
        }
        return new BaseResponse();
    }

    @Override
    public BaseResponse updateImage(Image image) {
        ImageEntity imageEntity = imageRepository.findById(image.getId()).get();
        imageEntity.setIsSign(image.getIsSign());
        imageEntity.setUpdateTime(new Date());
        imageRepository.save(imageEntity);
        return new BaseResponse();
    }

    @Override
    public BaseResponse classifyImage(Image image) {
        ClassifyResultResponse response = new ClassifyResultResponse();
        String exe = "python";
        String command = "F:\\study\\WebCrawler\\test.py";
        String[] exec = new String[]{exe, command, "--image", image.getImage()};
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(exec);
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream is = process.getInputStream();
        DataInputStream dis = new DataInputStream(is);
        String str = null;
        try {
            str = dis.readLine();
            System.out.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (str.indexOf("not") == -1) {
            response.setMsg(image.getImage() + "?" + ImageConstant.IMAGE_STYLE_1);//有车
        } else {
            response.setMsg(image.getImage() + "?" + ImageConstant.IMAGE_STYLE_2);//无车
        }
        //提取精确度
        List<String> accuracy = new ArrayList<String>();
        for(String sss:str.replaceAll("[^0-9]", ",").split(",")){
            if (sss.length()>0)
                accuracy.add(sss);
        }
        response.setAccuracy(accuracy.toString());
        return response;
    }

    @Override
    public BaseResponse testImage(Image image) {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        String pythonProject1 = "F:\\study\\WebCrawler\\test.py";
        String pythonProject2 = "F:\\study\\WebCrawler\\uselenetmodel.py";
        String pythonProject3 = "F:\\study\\WebCrawler\\usesvmmodel.py";
        ImageTestRunnable imageTestRunnable1 = new ImageTestRunnable(pythonProject1, image.getImage(), countDownLatch, "project1");
        imageTestRunnable1.newMap();
        imageTestRunnable1.start();
        ImageTestRunnable imageTestRunnable2 = new ImageTestRunnable(pythonProject2, image.getImage(), countDownLatch, "project2");
        imageTestRunnable2.start();
        ImageTestRunnable imageTestRunnable3 = new ImageTestRunnable(pythonProject3, image.getImage(), countDownLatch, "project3");
        imageTestRunnable3.start();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ImageTestRunnable imageTestRunnable = new ImageTestRunnable();
        return imageTestRunnable.getTestResultResponse();
    }

    private List<Image> converToImageList(Page<ImageEntity> list) {
        if (list == null && list.isEmpty()) {
            return null;
        }
        List<Image> imageList = new ArrayList<>();
        for (ImageEntity imageEntity : list) {
            Image image = new Image();
            BeanUtils.copyProperties(imageEntity, image);
            imageList.add(image);
        }
        return imageList;
    }
}
