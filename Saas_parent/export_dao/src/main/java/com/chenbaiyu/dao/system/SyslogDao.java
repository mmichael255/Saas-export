package com.chenbaiyu.dao.system;

import com.chenbaiyu.domain.log.Syslog;

import java.util.List;

public interface SyslogDao {

    List<Syslog> findAll(String companyId);

    void addLog(Syslog syslog);
}
