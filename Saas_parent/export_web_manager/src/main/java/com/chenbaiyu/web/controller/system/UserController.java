package com.chenbaiyu.web.controller.system;

import com.chenbaiyu.domain.Dept;
import com.chenbaiyu.domain.Role;
import com.chenbaiyu.domain.User;
import com.chenbaiyu.service.system.DeptService;
import com.chenbaiyu.service.system.RoleService;
import com.chenbaiyu.service.system.UserService;
import com.chenbaiyu.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {
    @Autowired
    UserService userService;
    @Autowired
    DeptService deptService;
    @Autowired
    RoleService roleService;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @RequestMapping("/list")
    public ModelAndView list(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "5") int pageSize, String companyId){
//        Subject subject = SecurityUtils.getSubject();
//        subject.checkPermission("用户管理");
        PageInfo byPage = userService.findAllByPages(pageNum,pageSize,companyId);
        ModelAndView mv = new ModelAndView();
        mv.addObject("PageInfo",byPage);
        mv.setViewName("/system/user/user-list");
        return mv;
    }

    @RequestMapping("/toAdd")
    public ModelAndView toAdd(){
        String company = "1";
        List<Dept> all = deptService.findAll(company);
        ModelAndView mv = new ModelAndView();
        mv.addObject("deptList",all);
        mv.setViewName("/system/user/user-add");
        return mv;
    }

    @RequestMapping("/toUpdate")
    public ModelAndView toUpdate(String id){
        User byId = userService.findById(id);
        String company = "1";
        List<Dept> all = deptService.findAll(company);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user",byId);
        modelAndView.addObject("deptList",all);
        modelAndView.setViewName("/system/user/user-update");
        return modelAndView;
    }

    @RequestMapping("/edit")
    public String edit(User user){
        user.setCompanyName(getLogInCompanyName());
        user.setCompanyId(getLogInCompanyId());
        if (StringUtils.isEmpty(user.getId())){
            Map map = new HashMap<>();
            map.put("name",user.getUserName());
            map.put("deptName",user.getDeptName());
            map.put("email",user.getEmail());
            rabbitTemplate.convertAndSend("spring_topEx","email.send",map);
            userService.addUser(user);
        }else {
            userService.updateUser(user);
        }
        return "redirect:/system/user/list.do";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Map<String,String> delete(String id){
        Boolean delete = userService.delete(id);
        HashMap<String, String> map = new HashMap<>();
        if (delete){
            map.put("flag","true");
            map.put("msg","删除成功");
        }else {
            map.put("flag","false");
            map.put("msg","删除失败，存在绑定了角色");
        }
        return map;
    }

    @RequestMapping("/roleList")
    public String roleList(String id){
        List<Role> roleList = roleService.findAll(getLogInCompanyId());
        List<Role> userRoleList = roleService.findUserRoleByUserId(id);
        User byId = userService.findById(id);
        req.setAttribute("user",byId);
        req.setAttribute("roleList",roleList);
        req.setAttribute("userRoleStr",userRoleList);
        return "/system/user/user-role";
    }

    @RequestMapping("/changeRole")
    public String changeRole(String userid,String[] roleIds){
        roleService.changeRole(userid,roleIds);
        return "redirect:/system/user/list.do";
    }
}
