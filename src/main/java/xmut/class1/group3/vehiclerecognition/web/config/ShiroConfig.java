package xmut.class1.group3.vehiclerecognition.web.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xmut.class1.group3.vehiclerecognition.domain.admin.AdminPrivilegeEntity;
import xmut.class1.group3.vehiclerecognition.repository.admin.AdminPrivilegeRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Autowired
    private AdminPrivilegeRepository adminPrivilegeRepository;

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //拦截器
        Map<String, String> filterMap = new HashMap<>();
        List<AdminPrivilegeEntity> adminPrivilegeEntityList = adminPrivilegeRepository.findAll();//动态设置所有权限
        for (AdminPrivilegeEntity adminPrivilegeEntity : adminPrivilegeEntityList) {
            filterMap.put(adminPrivilegeEntity.getPrivilegeUrl(), "perms[\"" + adminPrivilegeEntity.getPrivilege() + "\"]");
        }
        //设置值不拦截url
        filterMap.put("/**","anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }

    //注入SecurityManager
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置realm
        securityManager.setRealm(myShiroRealm());
        return securityManager;
    }

    /*
    * 自定义身份认证realm
    * 必须加上这个类，并加上@Bean注解，目的是注入CustomRealm
    * 否则会影响CustomRealm类中其他类的依赖注入
    * */
    @Bean
    public MyShiroRealm myShiroRealm() {
        return new MyShiroRealm();
    }

    //扩展标签控制权限
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
}
