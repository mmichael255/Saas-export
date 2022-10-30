package com.chenbaiyu.web.controller.system;

import com.chenbaiyu.domain.Dept;
import com.chenbaiyu.service.system.DeptService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/dept")
public class DeptController {
    @Autowired
    DeptService deptService;

    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "5") int pageSize,HttpServletRequest req){
        String companyId = "1";
        PageInfo<Dept> allByPages = deptService.findAllByPages(pageNum, pageSize, companyId);
        req.setAttribute("PageInfo",allByPages);
        return "/system/dept/dept-list";
    }

    @RequestMapping("/toAdd")
    public ModelAndView toAdd(){
        String companyId = "1";
        List<Dept> deptList = deptService.findAll(companyId);
        ModelAndView mv = new ModelAndView();
        mv.addObject("deptList",deptList);
        mv.setViewName("/system/dept/dept-add");
        return mv;
    }

    @RequestMapping("/toUpdate")
    public ModelAndView toUpdate(String id){
        Dept byId = deptService.findById(id);
        String companyId = "1";
        List<Dept> all = deptService.findAll(companyId);
        ModelAndView mv = new ModelAndView();
        mv.addObject("dept",byId);
        mv.addObject("deptList",all);
        mv.setViewName("/system/dept/dept-update");
        return mv;
    }

    @RequestMapping("/edit")
    public String edit(Dept dept){
        String companyName = "传智播客教育股份有限公司";
        String companyId = "1";
        dept.setCompanyId(companyId);
        dept.setCompanyName(companyName);

        if (StringUtils.isEmpty(dept.getId())){
            deptService.add(dept);
        }else {
            deptService.updateById(dept);
        }
        return "redirect:/system/dept/list.do";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Map<String,String> dele(String id){
       Boolean flag = deptService.delete(id);
        HashMap<String, String> map = new HashMap<>();
        if (flag){
           map.put("flag","true");
           map.put("msg","删除成功");
       }else {
            map.put("flag","false");
            map.put("msg","删除失败（存在子部门）");
        }
        return map;
    }
}
