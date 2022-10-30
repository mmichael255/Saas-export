package com.chenbaiyu.service.company.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chenbaiyu.dao.company.CompanyDao;
import com.chenbaiyu.domain.company;
import com.chenbaiyu.service.company.CompanyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;
import java.util.UUID;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    CompanyDao companyDao;

    @Override
    public List<company> findAll() {
        return companyDao.findAll();
    }

    @Override
    public void update(company company) {
        companyDao.update(company);
    }

    @Override
    public void add(company company) {
        company.setId(UUID.randomUUID().toString());
        companyDao.add(company);
    }

    @Override
    public company findCompanyById(String id) {
        return companyDao.findCompanyById(id);
    }

    @Override
    public void delete(String id) {
        companyDao.delete(id);
    }

    @Override
    public PageInfo<company> findByPage(int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<company> all = companyDao.findAll();
        return new PageInfo<>(all);
    }
}
