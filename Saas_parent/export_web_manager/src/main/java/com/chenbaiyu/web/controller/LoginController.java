package com.chenbaiyu.web.controller;

import com.chenbaiyu.domain.Module;
import com.chenbaiyu.domain.User;
import com.chenbaiyu.service.system.Impl.UserServiceImpl;
import com.chenbaiyu.service.system.ModuleService;
import com.chenbaiyu.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.List;

@Controller
public class LoginController extends BaseController{
    @Autowired
    UserService userService;

    @Autowired
    ModuleService moduleService;
    /**
     * 路径：
     * @return
     */
    @RequestMapping("login")
    public String trans(){
        return "redirect:/login.jsp";
    }

    @RequestMapping("home")
    public String home(){
        return "/home/home";
    }

    private void removeDuplicate(List<Module> moduleList){
        HashSet<Module> modules = new HashSet<>(moduleList);
        moduleList.clear();
        moduleList.addAll(modules);
    }

    @RequestMapping("/userlogin")
    public String userlogin(String email,String password){

        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)){
            return "redirect:/login.jsp";
        }
//        User user = userService.findByEmail(email);
//        if (user == null || !(user.getPassword().equals(password))){
//            req.setAttribute("error","密码或账号有误");
//            return "forward:/login.jsp";
//        }

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(email, password);

        try{
            subject.login(token);
            User user = (User) subject.getPrincipal();
            session.setAttribute("loginuser",user);
            List<Module> moduleList = moduleService.findModuleByUser(user);
            removeDuplicate(moduleList);
            session.setAttribute("modules",moduleList);
            return "/home/main";

        }catch (UnknownAccountException u){
            req.setAttribute("error","不存在的用户");
            return "forward:/login.jsp";
        }catch (IncorrectCredentialsException i){
            req.setAttribute("error","密码错误");
            return "forward:/login.jsp";
        }
    }

    @RequestMapping("/logout")
    public String logout(){
        req.getSession().removeAttribute("loginuser");
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/login.do";
    }
}
