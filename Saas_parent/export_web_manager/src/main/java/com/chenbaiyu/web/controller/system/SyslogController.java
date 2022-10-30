package com.chenbaiyu.web.controller.system;

import com.chenbaiyu.service.system.SyslogService;
import com.chenbaiyu.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/system/log")
public class SyslogController extends BaseController {
    @Autowired
    SyslogService syslogService;

    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize){
        PageInfo all = syslogService.findAll(pageNum, pageSize,getLogInCompanyId());
        req.setAttribute("PageInfo",all);
        return "/system/log/log-list";
    }
}
