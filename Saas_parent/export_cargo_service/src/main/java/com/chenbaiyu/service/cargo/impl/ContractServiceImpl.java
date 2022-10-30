package com.chenbaiyu.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chenbaiyu.dao.cargo.ContractDao;
import com.chenbaiyu.domain.cargo.Contract;
import com.chenbaiyu.domain.cargo.ContractExample;
import com.chenbaiyu.domain.cargo.ContractProduct;
import com.chenbaiyu.domain.cargo.ContractProductExample;
import com.chenbaiyu.service.cargo.ContractProductService;
import com.chenbaiyu.service.cargo.ContractService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ContractServiceImpl implements ContractService {


    @Autowired
    ContractDao contractDao;

    @Override
    public PageInfo<Contract> findByPage(ContractExample contractExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Contract> contracts = contractDao.selectByExample(contractExample);
        return new PageInfo<>(contracts);
    }

    @Override
    public List<Contract> findAll(ContractExample contractExample) {
        return contractDao.selectByExample(contractExample);
    }

    @Override
    public Contract findById(String id) {
        return contractDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Contract contract) {
        contract.setId(UUID.randomUUID().toString());
        contract.setCreateTime(new Date());
        contract.setUpdateTime(new Date());
        contractDao.insertSelective(contract);
    }

    @Override
    public void update(Contract contract) {
        contract.setUpdateTime(new Date());
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void delete(String id) {
        contractDao.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo<Contract> findPageByDeptId(String DeptId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);

        return new PageInfo<>(contractDao.selectByParentDeptId(DeptId));
    }


}
