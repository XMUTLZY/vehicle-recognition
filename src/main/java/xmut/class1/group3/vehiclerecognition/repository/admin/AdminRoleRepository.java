package xmut.class1.group3.vehiclerecognition.repository.admin;


import org.springframework.data.jpa.repository.JpaRepository;
import xmut.class1.group3.vehiclerecognition.domain.admin.AdminRoleEntity;

public interface AdminRoleRepository extends JpaRepository<AdminRoleEntity, Integer> {
    AdminRoleEntity findAllById(Integer id);
}
