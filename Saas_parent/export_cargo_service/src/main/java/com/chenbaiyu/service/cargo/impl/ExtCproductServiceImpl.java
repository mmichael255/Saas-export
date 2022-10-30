package com.chenbaiyu.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chenbaiyu.dao.cargo.ContractDao;
import com.chenbaiyu.dao.cargo.ContractProductDao;
import com.chenbaiyu.dao.cargo.ExtCproductDao;
import com.chenbaiyu.domain.cargo.Contract;
import com.chenbaiyu.domain.cargo.ContractProduct;
import com.chenbaiyu.domain.cargo.ExtCproduct;
import com.chenbaiyu.domain.cargo.ExtCproductExample;
import com.chenbaiyu.service.cargo.ExtCproductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ExtCproductServiceImpl implements ExtCproductService {
    @Autowired
    ExtCproductDao extCproductDao;

    @Autowired
    ContractProductDao contractProductDao;

    @Autowired
    ContractDao contractDao;

    @Override
    public PageInfo<ExtCproduct> findByPage(ExtCproductExample extCproductExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<>(extCproductDao.selectByExample(extCproductExample));
    }

    @Override
    public List<ExtCproduct> findAll(ExtCproductExample extCproductExample) {
        return extCproductDao.selectByExample(extCproductExample);
    }

    @Override
    public ExtCproduct findById(String id) {
        return extCproductDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(ExtCproduct extCproduct) {
        extCproduct.setId(UUID.randomUUID().toString());
        extCproduct.setCreateTime(new Date());
        double amount = 0d;
        if (extCproduct.getCnumber() !=null && extCproduct.getPrice() != null){
            amount = extCproduct.getCnumber()*extCproduct.getPrice();
        }
        extCproduct.setAmount(amount);
        extCproductDao.insertSelective(extCproduct);
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        if (contract != null){
            contract.setTotalAmount(contract.getTotalAmount()+amount);
        }else{
            contract.setTotalAmount(amount);
        }
        contract.setExtNum(contract.getExtNum()+1);
        contractDao.updateByPrimaryKeySelective(contract);

    }

    @Override
    public void update(ExtCproduct extCproduct) {
        extCproduct.setUpdateTime(new Date());
        ExtCproduct oldExt = extCproductDao.selectByPrimaryKey(extCproduct.getId());
        double oldamount = oldExt.getAmount();
        double amout = 0d;
        if (extCproduct.getPrice() != null && extCproduct.getCnumber() != null){
            amout = extCproduct.getPrice()*extCproduct.getCnumber();
        }
        extCproduct.setAmount(amout);
        extCproductDao.updateByPrimaryKeySelective(extCproduct);
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        if (contract != null){
            contract.setTotalAmount(contract.getTotalAmount() - oldamount + amout);
        }
        contractDao.updateByPrimaryKeySelective(contract);

    }

    @Override
    public void delete(String id) {
        ExtCproduct extCproduct = extCproductDao.selectByPrimaryKey(id);
        extCproductDao.deleteByPrimaryKey(id);

        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        if(contract!=null) {
            contract.setTotalAmount(contract.getTotalAmount() - extCproduct.getAmount());
            contract.setExtNum(contract.getExtNum() - 1);
            contractDao.updateByPrimaryKeySelective(contract);
        }
    }
}
