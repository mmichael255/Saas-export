package com.chenbaiyu.web.controller.company;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chenbaiyu.domain.company;
import com.chenbaiyu.service.company.CompanyService;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/company")
public class CompanyController {

    @Reference(timeout = 100000,retries = 0)
    CompanyService companyServiceImpl;

    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "3") int pageSize, HttpServletRequest req, HttpServletResponse resp){
        PageInfo byPage = companyServiceImpl.findByPage(pageNum, pageSize);
        req.setAttribute("PageInfo",byPage);
        return "/company/company-list";
    }

    @RequestMapping("/toAdd")
    public String toAdd(){
        return "/company/company-add";
    }

    @RequestMapping("/toUpdate")
    public String toupdate(String id,HttpServletRequest req){
        company companyById = companyServiceImpl.findCompanyById(id);
        req.setAttribute("company",companyById);
        return "/company/company-update";
    }

    @RequestMapping("/edit")
    public String edit(company company){
        if (StringUtil.isEmpty(company.getId())){
            companyServiceImpl.add(company);
        }else {
            companyServiceImpl.update(company);
        }
        return "redirect:/company/list.do";
    }

    @RequestMapping("/delete")
    public String delete(String id){
        companyServiceImpl.delete(id);
        return "redirect:/company/list.do";
    }
}
