package com.chenbaiyu.service.stat.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chenbaiyu.dao.stat.StatDao;
import com.chenbaiyu.service.stat.StatService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
@Service
public class StatServiceImpl implements StatService {
    @Autowired
    StatDao statDao;

    @Override
    public List<Map<String, Object>> findSellStat(String companyId) {
        return statDao.findSellStat(companyId);
    }

    @Override
    public List<Map<String, Object>> findProductSellStat(String companyId) {
        return statDao.findProductSellStat(companyId);
    }

    @Override
    public List<Map<String, Object>> findonlineData(String companyId) {
        return statDao.findonlineData(companyId);
    }
}
