package com.chenbaiyu.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chenbaiyu.dao.cargo.FactoryDao;
import com.chenbaiyu.domain.cargo.Factory;
import com.chenbaiyu.domain.cargo.FactoryExample;
import com.chenbaiyu.service.cargo.FactoryService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class FactoryServiceImpl implements FactoryService {
    @Autowired
    FactoryDao factoryDao;

    @Override
    public PageInfo<Factory> findByPage(FactoryExample FactoryExample, int pageNum, int pageSize) {
        return null;
    }

    @Override
    public List<Factory> findAll(FactoryExample FactoryExample) {
        return factoryDao.selectByExample(FactoryExample);
    }

    @Override
    public Factory findByFactory(String factoryName) {
        FactoryExample factoryExample = new FactoryExample();
        FactoryExample.Criteria criteria = factoryExample.createCriteria();
        criteria.andFactoryNameEqualTo(factoryName);
        List<Factory> factories = factoryDao.selectByExample(factoryExample);
        if (factories != null){
            return factories.get(0);
        }else {
            return null;
        }
    }

    @Override
    public Factory findById(String id) {
        return null;
    }

    @Override
    public void save(Factory Factory) {

    }

    @Override
    public void update(Factory Factory) {

    }

    @Override
    public void delete(String id) {

    }
}
