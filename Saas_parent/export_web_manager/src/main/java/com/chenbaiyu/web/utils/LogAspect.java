package com.chenbaiyu.web.utils;

import com.chenbaiyu.domain.User;
import com.chenbaiyu.domain.log.Syslog;
import com.chenbaiyu.service.system.SyslogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Aspect
@Component
public class LogAspect {
    @Autowired
    SyslogService syslogService;
    @Autowired
    HttpSession session;
    @Autowired
    HttpServletRequest req;

    @Around("execution(* com.chenbaiyu.web.controller.*.*.*(..))")
    public Object wirte(ProceedingJoinPoint pjp){
        Object result = null;
        try {
            result = pjp.proceed();

            Syslog syslog = new Syslog();
            User loginuser = (User)session.getAttribute("loginuser");
            if (loginuser != null){
                syslog.setUserName(loginuser.getUserName());
                syslog.setCompanyId(loginuser.getCompanyId());
                syslog.setCompanyName(loginuser.getCompanyName());
            }
            syslog.setIp(req.getLocalAddr());
            syslog.setTime(new Date());
            String method = pjp.getSignature().getName();
            String className = pjp.getTarget().getClass().getName();
            syslog.setMethod(method);
            syslog.setAction(className);

            syslogService.addSyslog(syslog);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return result;
    }
}
