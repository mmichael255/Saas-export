package com.chenbaiyu.service.system;

import com.chenbaiyu.dao.system.SyslogDao;
import com.chenbaiyu.domain.log.Syslog;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface SyslogService {

    PageInfo findAll(int pageNum, int pageSize,String companyId);

    void addSyslog(Syslog syslog);
}
