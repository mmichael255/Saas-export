package com.chenbaiyu.web.controller.system;

import com.chenbaiyu.domain.Module;
import com.chenbaiyu.domain.Role;
import com.chenbaiyu.service.system.ModuleService;
import com.chenbaiyu.service.system.RoleService;
import com.chenbaiyu.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController {
    @Autowired
    RoleService roleService;

    @Autowired
    ModuleService moduleService;

    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "5") int pageSize){
        PageInfo allByPages = roleService.findAllByPages(pageNum, pageSize, getLogInCompanyId());
        req.setAttribute("PageInfo",allByPages);
        return "/system/role/role-list";
    }

    @RequestMapping("/toAdd")
    public String toAdd(){
        return "/system/role/role-add";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        Role byId = roleService.findById(id);
        req.setAttribute("role",byId);
        return "/system/role/role-update";
    }

    @RequestMapping("/edit")
    public String edit(Role role){
        role.setCompanyId(getLogInCompanyId());
        role.setCompanyName(getLogInCompanyName());

        if (StringUtils.isEmpty(role.getId())){
            roleService.addRole(role);
        }else {
            roleService.updateRole(role);
        }
        return "redirect:/system/role/list.do";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Map<String,String> delete(String id){
        HashMap<String, String> map = new HashMap<>();
        boolean delete = roleService.delete(id);
        if (delete){
            map.put("flag","true");
            map.put("msg","删除成功");
        }else {
            map.put("flag","false");
            map.put("msg","删除失败，存在绑定了角色");
        }
        return map;
    }

    @RequestMapping("/roleModule")
    public String roleModule(String roleid){
        Role byId = roleService.findById(roleid);
        req.setAttribute("role",byId);
        return "/system/role/role-module";
    }

    @RequestMapping("/getZtreeNodes")
    @ResponseBody
    public List<Map<String,Object>> getZtreeNodes(String roleid){
        ArrayList<Map<String,Object>> mapList = new ArrayList<>();
        List<Module> moduleList = moduleService.findAll();
        List<Module> roleModuleList =moduleService.findRoleModuleByRoleId(roleid);
        for (Module module : moduleList) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id",module.getId());
            map.put("pId",module.getParentId());
            map.put("name",module.getName());
            map.put("open",true);
            if (roleModuleList.contains(module)){
                map.put("checked",true);
            }
            mapList.add(map);
        }
        return mapList;
    }

    @RequestMapping("/updateRoleModule")
    public String updateRoleModule(String roleid,String[] moduleIds){
        roleService.updateRoleModule(roleid,moduleIds);
        return "redirect:/system/role/list.do";
    }
}
