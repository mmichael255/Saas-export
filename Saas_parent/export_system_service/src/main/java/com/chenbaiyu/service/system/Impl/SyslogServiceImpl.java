package com.chenbaiyu.service.system.Impl;

import com.chenbaiyu.dao.system.SyslogDao;
import com.chenbaiyu.domain.log.Syslog;
import com.chenbaiyu.service.system.SyslogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SyslogServiceImpl implements SyslogService {
    @Autowired
    SyslogDao syslogDao;
    @Override
    public PageInfo<Syslog> findAll(int pageNum, int pageSize,String companyId) {
        PageHelper.startPage(pageNum,pageSize);
        List<Syslog> all = syslogDao.findAll(companyId);
        return new PageInfo<>(all);
    }

    @Override
    public void addSyslog(Syslog syslog) {
        syslog.setId(UUID.randomUUID().toString());
        syslogDao.addLog(syslog);
    }
}
