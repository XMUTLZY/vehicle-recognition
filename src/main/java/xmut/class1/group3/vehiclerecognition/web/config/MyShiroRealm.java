package xmut.class1.group3.vehiclerecognition.web.config;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import xmut.class1.group3.vehiclerecognition.http.vo.admin.Admin;
import xmut.class1.group3.vehiclerecognition.http.vo.admin.AdminPrivilege;
import xmut.class1.group3.vehiclerecognition.service.admin.AdminService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyShiroRealm extends AuthorizingRealm {
    @Autowired
    AdminService adminService;
    //获取权限信息
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取管理员
        Admin admin = (Admin) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //获取该管理员权限
        List<AdminPrivilege> adminPrivilegeList = adminService.getAllAdminPrivilege(admin.getMobile());
        List<String> perUrlList = new ArrayList<>();
        if (!adminPrivilegeList.isEmpty() && adminPrivilegeList != null) {
            for (AdminPrivilege adminPrivilege : adminPrivilegeList) {
                perUrlList.add(adminPrivilege.getPrivilege());
            }
            Set<String> setPerUrl = new HashSet<>();
            for (String  s : perUrlList) {
                setPerUrl.add(s);
            }
            info.setStringPermissions(setPerUrl);
        }
        return info;
    }

    //验证管理员身份
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //从数据库获取该管理员信息
        Admin admin = new Admin();
        admin.setMobile(token.getUsername());
        Admin admin1 = adminService.getAdmin(admin);
        String password = new String((char[]) token.getCredentials());
        if (!password.equals(admin1.getPassword())) {
            throw new AccountException("查询不到该管理员");
        }
        return new SimpleAuthenticationInfo(admin1, password, getName());
    }
}
