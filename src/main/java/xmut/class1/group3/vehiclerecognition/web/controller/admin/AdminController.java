package xmut.class1.group3.vehiclerecognition.web.controller.admin;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import xmut.class1.group3.vehiclerecognition.constants.AdminConstant;
import xmut.class1.group3.vehiclerecognition.http.response.BaseResponse;
import xmut.class1.group3.vehiclerecognition.http.vo.admin.Admin;
import xmut.class1.group3.vehiclerecognition.service.admin.AdminService;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;
    private Logger logger = Logger.getLogger("");

    //登录逻辑 shiro管理
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse login(@RequestBody Admin admin, HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();//获得当前subject 即用户信息
        BaseResponse response = new BaseResponse();
        Admin admin1 = adminService.getAdmin(admin);
        if (admin1 == null) {
            response.setMsg("账号不存在");
            return response;
        }
        String encrypt = admin1.getEncrypt();//获取盐
        String encodePassword = new SimpleHash(AdminConstant.ENCRYPTION_TYPE, admin.getPassword(), encrypt, AdminConstant.ENCRYPTION_TIMES).toString();
        UsernamePasswordToken token = new UsernamePasswordToken(admin.getMobile(), encodePassword);//认证
        //执行登录
        try {
            subject.login(token);
            token.setRememberMe(true);
            request.getSession().setAttribute("admin", adminService.getAdmin(admin));//登录成功，在缓存中设置session
            response.setMsg("SUCCESS");
        } catch (IncorrectCredentialsException e) {
            response.setMsg("登录密码错误");
        } catch (ExcessiveAttemptsException e) {
            response.setMsg("登录失败次数过多");
        } catch (LockedAccountException e) {
            response.setMsg("账号已被锁定");
        } catch (DisabledAccountException e) {
            response.setMsg("账号已被禁用");
        } catch (ExpiredCredentialsException e) {
            response.setMsg("账号已过期");
        } catch (UnknownAccountException e) {
            response.setMsg("账号不存在");
        } catch (UnauthorizedException e) {
            response.setMsg("您没有得到相应的授权");
        } catch (Exception e) {
            response.setMsg("用户名或密码错误");
        }
        return response;
    }

    //注册
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse registerAdmin(@RequestBody Admin admin, HttpServletRequest request) {
        BaseResponse response = new BaseResponse();
        String encrypt = new SecureRandomNumberGenerator().nextBytes().toString();//生成盐
        String encodePassword = new SimpleHash(AdminConstant.ENCRYPTION_TYPE, admin.getPassword(), encrypt, AdminConstant.ENCRYPTION_TIMES).toString();
        admin.setPassword(encodePassword);
        admin.setEncrypt(encrypt);
        adminService.saveAdmin(admin);
        response.setMsg("注册成功");
        return response;
    }
}
