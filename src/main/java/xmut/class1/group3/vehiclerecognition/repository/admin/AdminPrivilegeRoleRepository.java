package xmut.class1.group3.vehiclerecognition.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xmut.class1.group3.vehiclerecognition.domain.admin.AdminPrivilegeRoleEntity;

import java.util.List;

public interface AdminPrivilegeRoleRepository extends JpaRepository<AdminPrivilegeRoleEntity, Integer> {
    @Query(value = "select privilege_id from admin_privilege_role where role_id = :roleId", nativeQuery = true)
    List<Integer> findPrivilegeIdList(@Param("roleId") Integer roleId);
}
