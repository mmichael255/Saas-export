package com.chenbaiyu.web.controller;

import com.chenbaiyu.domain.User;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class BaseController {

    @Autowired
    protected HttpServletRequest req;

    @Autowired
    protected HttpServletResponse response;

    @Autowired
    protected HttpSession session;

    protected String getLogInCompanyId(){
        return getLoginUser().getCompanyId();
    }

    protected String getLogInCompanyName(){

        return getLoginUser().getCompanyName();
    }

    protected User getLoginUser(){
       return (User)session.getAttribute("loginuser");
    }
}
