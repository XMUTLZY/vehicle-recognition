package xmut.class1.group3.vehiclerecognition.domain.admin;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "admin_privilege_role")
public class AdminPrivilegeRoleEntity implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    @Column(name = "role_id")
    private Integer roleId;
    @Column(name = "privilege_id")
    private Integer privilegeId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(Integer privilegeId) {
        this.privilegeId = privilegeId;
    }
}
