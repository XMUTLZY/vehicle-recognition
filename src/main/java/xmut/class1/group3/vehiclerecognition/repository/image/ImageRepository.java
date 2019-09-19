package xmut.class1.group3.vehiclerecognition.repository.image;

import org.springframework.data.jpa.repository.JpaRepository;
import xmut.class1.group3.vehiclerecognition.domain.image.ImageEntity;

public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {
}
