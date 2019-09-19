package xmut.class1.group3.vehiclerecognition.repository.admin;


import org.springframework.data.jpa.repository.JpaRepository;
import xmut.class1.group3.vehiclerecognition.domain.admin.AdminEntity;

public interface AdminRepository extends JpaRepository<AdminEntity, Integer> {
    AdminEntity findByMobile(String mobile);
}
