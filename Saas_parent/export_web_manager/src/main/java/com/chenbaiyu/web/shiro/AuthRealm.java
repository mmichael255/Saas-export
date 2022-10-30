package com.chenbaiyu.web.shiro;

import com.chenbaiyu.domain.Module;
import com.chenbaiyu.domain.User;
import com.chenbaiyu.service.system.ModuleService;
import com.chenbaiyu.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AuthRealm extends AuthorizingRealm{
    @Autowired
    UserService userService;
    @Autowired
    ModuleService moduleService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("调用了授权方法");
        User loginuser = (User) SecurityUtils.getSubject().getPrincipal();
        List<Module> moduleByUser = moduleService.findModuleByUser(loginuser);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        if (moduleByUser != null && moduleByUser.size() > 0){
            for (Module module : moduleByUser) {
                simpleAuthorizationInfo.addStringPermission(module.getName());
            }
        }
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("调用了认证方法");

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String email = token.getUsername();
        User loginuser = userService.findByEmail(email);
        if (loginuser == null){
            return null;
        }
        return new SimpleAuthenticationInfo(loginuser,loginuser.getPassword(),"");
    }
}
