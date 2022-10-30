package com.chenbaiyu.web.controller.system;

import com.chenbaiyu.domain.Module;
import com.chenbaiyu.domain.Role;
import com.chenbaiyu.service.system.ModuleService;
import com.chenbaiyu.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/module")
public class ModuleController extends BaseController {
    @Autowired
    ModuleService moduleService;

    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "5") int pageSize){
        PageInfo allByPages = moduleService.findAllByPages(pageNum, pageSize);
        req.setAttribute("PageInfo",allByPages);
        return "/system/module/module-list";
    }
    @RequestMapping("/toAdd")
    public String toAdd(){
        List<Module> all = moduleService.findAll();
        req.setAttribute("menus",all);
        return "/system/module/module-add";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        Module byId = moduleService.findById(id);
        List<Module> all = moduleService.findAll();
//        System.out.println(all);
        req.setAttribute("module",byId);
        req.setAttribute("menus",all);
        return "/system/module/module-update";
    }

    @RequestMapping("/edit")
    public String edit(Module module){
        if (StringUtils.isEmpty(module.getId())){
            moduleService.addModule(module);
        }else {
            moduleService.updateModule(module);
        }
        return "redirect:/system/module/list.do";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Map<String,String> delete(String id){
        HashMap<String, String> map = new HashMap<>();
        boolean delete = moduleService.delete(id);
        if (delete){
            map.put("flag","true");
            map.put("msg","删除成功");
        }else {
            map.put("flag","false");
            map.put("msg","删除失败，存在绑定了角色");
        }
        return map;
    }
}
