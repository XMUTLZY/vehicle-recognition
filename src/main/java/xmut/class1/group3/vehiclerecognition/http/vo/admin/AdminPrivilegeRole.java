package xmut.class1.group3.vehiclerecognition.http.vo.admin;

import java.io.Serializable;

public class AdminPrivilegeRole implements Serializable {
    private Integer id;
    private Integer RoleId;
    private Integer privilegeId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return RoleId;
    }

    public void setRoleId(Integer roleId) {
        RoleId = roleId;
    }

    public Integer getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(Integer privilegeId) {
        this.privilegeId = privilegeId;
    }
}
