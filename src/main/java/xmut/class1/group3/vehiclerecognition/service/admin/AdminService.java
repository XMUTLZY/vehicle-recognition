package xmut.class1.group3.vehiclerecognition.service.admin;

import org.springframework.data.domain.Pageable;
import xmut.class1.group3.vehiclerecognition.http.response.BaseResponse;
import xmut.class1.group3.vehiclerecognition.http.vo.admin.Admin;
import xmut.class1.group3.vehiclerecognition.http.vo.admin.AdminPrivilege;
import java.util.List;

public interface AdminService {
    Admin getAdmin(Admin admin);
    void saveAdmin(Admin admin);
    BaseResponse getAllAdmin(Pageable pageable);
    List<AdminPrivilege> getAllAdminPrivilege(String mobile);
}
