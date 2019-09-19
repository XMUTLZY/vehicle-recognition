package xmut.class1.group3.vehiclerecognition.service.image;

import org.springframework.data.domain.Pageable;
import xmut.class1.group3.vehiclerecognition.http.response.BaseResponse;
import xmut.class1.group3.vehiclerecognition.http.vo.image.Image;

public interface ImageService {
    BaseResponse getAllImage(Pageable pageable);
    BaseResponse addImage(String imageUrl);
    BaseResponse saveImage();
    BaseResponse updateImage(Image image);
    BaseResponse classifyImage(Image image);
    BaseResponse testImage(Image image);
}
