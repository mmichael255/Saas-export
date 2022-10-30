package com.chenbaiyu.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chenbaiyu.domain.company;
import com.chenbaiyu.service.company.CompanyService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CompanyController {

    @Reference(timeout = 1000000,retries = 0)
    private CompanyService companyService;

    @RequestMapping("/apply")
    @ResponseBody
    public String addCompany(company company){
        try {
            company.setState(0);
            companyService.add(company);
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }
}
