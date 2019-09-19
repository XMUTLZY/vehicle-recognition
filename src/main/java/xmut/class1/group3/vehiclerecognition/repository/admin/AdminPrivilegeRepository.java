package xmut.class1.group3.vehiclerecognition.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xmut.class1.group3.vehiclerecognition.domain.admin.AdminPrivilegeEntity;

import java.util.List;

public interface AdminPrivilegeRepository extends JpaRepository<AdminPrivilegeEntity,Integer> {
    @Query(value = "select * from admin_privilege where id in(:list)", nativeQuery = true)
    List<AdminPrivilegeEntity> getPrivilegeList(@Param("list") List<Integer> list);
}
