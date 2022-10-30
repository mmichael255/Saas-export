package com.chenbaiyu.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chenbaiyu.dao.cargo.ContractDao;
import com.chenbaiyu.dao.cargo.ContractProductDao;
import com.chenbaiyu.dao.cargo.ExtCproductDao;
import com.chenbaiyu.domain.cargo.*;
import com.chenbaiyu.service.cargo.ContractProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ContractProductServiceImpl implements ContractProductService {
    @Autowired
    ContractProductDao contractProductDao;

    @Autowired
    ContractDao contractDao;

    @Autowired
    ExtCproductDao extCproductDao;

    @Override
    public PageInfo<ContractProduct> findByPage(ContractProductExample ContractProductExample, int pageNum, int pageSize) {
        List<ContractProduct> contractProducts = contractProductDao.selectByExample(ContractProductExample);
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<>(contractProducts);
    }

    @Override
    public List<ContractProduct> findAll(ContractProductExample ContractProductExample) {
        return contractProductDao.selectByExample(ContractProductExample);
    }

    @Override
    public ContractProduct findById(String id) {
        return contractProductDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(ContractProduct contractProduct) {
        contractProduct.setId(UUID.randomUUID().toString());
        contractProduct.setCreateTime(new Date());
        contractProduct.setUpdateTime(new Date());
        Double amount = 0d;
        if (contractProduct.getCnumber() != null && contractProduct.getPrice() != null){
            amount = contractProduct.getCnumber()*contractProduct.getPrice();
        }
        contractProduct.setAmount(amount);
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        if (contract.getTotalAmount() != null){
            contract.setTotalAmount(contract.getTotalAmount()+amount);
        }else {
            contract.setTotalAmount(amount);
        }
        if (contract.getProNum() != null){
            contract.setProNum(contract.getProNum()+1);
        }else {
            contract.setProNum(1);
        }
        contractDao.updateByPrimaryKeySelective(contract);
        contractProductDao.insertSelective(contractProduct);

    }

    @Override
    public void update(ContractProduct contractProduct) {
        ContractProduct oldCP = contractProductDao.selectByPrimaryKey(contractProduct.getId());
        Double oldAmount = oldCP.getAmount();
        contractProduct.setUpdateTime(new Date());
        Double amount = 0d;
        if (contractProduct.getCnumber() != null && contractProduct.getPrice() != null){
            amount = contractProduct.getCnumber()*contractProduct.getPrice();
        }
        contractProduct.setAmount(amount);
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        if (contract != null){
            contract.setTotalAmount(contract.getTotalAmount()-oldAmount+amount);
        }
        contractProductDao.updateByPrimaryKeySelective(contractProduct);
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void delete(String id) {

        ContractProduct contractProduct = contractProductDao.selectByPrimaryKey(id);
        contractProductDao.deleteByPrimaryKey(id);
        ExtCproductExample extCproductExample = new ExtCproductExample();
        ExtCproductExample.Criteria extCproductExampleCriteria = extCproductExample.createCriteria();
        extCproductExampleCriteria.andContractProductIdEqualTo(id);
        List<ExtCproduct> extCproducts = extCproductDao.selectByExample(extCproductExample);
        Double amount = 0d;
        if (extCproducts != null){
            for (ExtCproduct extCproduct : extCproducts) {
                amount += extCproduct.getAmount();
                extCproductDao.deleteByPrimaryKey(extCproduct.getId());
            }
        }
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        if (contract != null){
            contract.setTotalAmount(contract.getTotalAmount() - amount - contractProduct.getAmount());
        }
        contract.setProNum(contract.getProNum()-1);

        if (extCproducts != null){
            contract.setExtNum(contract.getExtNum() - extCproducts.size());
        }
        contractDao.updateByPrimaryKeySelective(contract);
    }
}
