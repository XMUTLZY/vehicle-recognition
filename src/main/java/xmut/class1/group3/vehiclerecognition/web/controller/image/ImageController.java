package xmut.class1.group3.vehiclerecognition.web.controller.image;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xmut.class1.group3.vehiclerecognition.http.response.BaseResponse;
import xmut.class1.group3.vehiclerecognition.http.vo.image.Image;
import xmut.class1.group3.vehiclerecognition.service.image.ImageService;
import java.io.*;
import java.util.Date;
import java.util.UUID;

@RequestMapping("/image")
@Controller
public class ImageController {
    private static final String ACCESS_KEY = "7ZTUd7jCIytlt2ixL1WqcQsos99Mfzr5SQMOyfvC";//访问秘钥
    private static final String SECRET_KEY = "hj5ND21XDDwKZUge0ImJ0NJp8IgWPsP3YtH0uzib";//授权秘钥
    private static final String BUCKET = "image";//存储空间名称
    private static final String DOMAIN_URL = "pwm7p4mff.bkt.clouddn.com";//外链域名 普通上传
    @Autowired
    private ImageService imageService;

    //获取所有图片
    @RequestMapping(value = "/allImage", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse getAllImage(@RequestParam("limit") Integer limit, @RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.ASC, "id");
        return imageService.getAllImage(pageable);
    }

    //将本地文件夹下的图片保存url在数据库中
    @RequestMapping(value = "/saveImage", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse saveImage() {
        return imageService.saveImage();
    }

    //标注图片
    @RequestMapping(value = "/updateImage", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse updateImage(@RequestBody Image image) {
        return imageService.updateImage(image);
    }

    //图片分类
    @RequestMapping(value = "/classifyImage", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse classifyImage(@RequestBody Image image) {
        return imageService.classifyImage(image);
    }

    //上传图片
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("type") Integer type) {
        BaseResponse response = new BaseResponse();
        //构造一个带指定Zone对象的构造器
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //获取本地图片路径
        String imagePath = getImagePathLocal(file);
        //获取图片后缀
        String imageSuffix = imagePath.substring(imagePath.lastIndexOf(".") + 1);//获取图片后缀
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = UUID.randomUUID().toString() + "." + imageSuffix;
        //验证七牛云身份是否通过
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        //生成凭证
        String token = auth.uploadToken(BUCKET);
        //返回图片服务器地址
        String imageUrl = null;
        try {
            Response response1 = uploadManager.put(imagePath, key, token);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response1.bodyString(), DefaultPutRet.class);
            imageUrl = "http://" + DOMAIN_URL + "/" + key;
            if (type == 0) {
                imageService.addImage(imageUrl);//判断图片上传来源
            }
            response.setMsg(imageUrl);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return response;
    }

    private String getImagePathLocal(MultipartFile file) {
        String localPath = "F:\\virtual-image-file-folder";//本地文件夹
        File file1 = new File(localPath);
        if (!file1.exists() && !file1.mkdirs()) {
            //创建文件夹
        }
        String imageName = file.getOriginalFilename();
        String imageSuffix = imageName.substring(imageName.lastIndexOf(".") + 1);
        String resultImageName = UUID.randomUUID().toString() + "." + imageSuffix;
        File uploadFile = new File(localPath + "/" + resultImageName);
        try {
            // 将上传的图片二进制流保存为文件
            FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(uploadFile));
            return localPath + "/" + resultImageName;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/saveToMysql", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse saveToMysql() throws IOException, InterruptedException {
        String exe = "python";
        String command = "F:\\study\\python\\getPic-02.py";
        String[] exec = new String[]{exe, command};
        Process process = Runtime.getRuntime().exec(exec);
        InputStream is = process.getInputStream();
        DataInputStream dis = new DataInputStream(is);
        String str = dis.readLine();
        process.waitFor();
        BaseResponse response = new BaseResponse();
        Date date = new Date();
        response.setMsg(date.toString());
        return response;
    }

    //算法测试
    @RequestMapping(value = "/testImage", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse testImage(@RequestBody Image image) {
        return imageService.testImage(image);
    }
}
