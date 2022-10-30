package com.chenbaiyu.service.cargo.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.chenbaiyu.dao.cargo.ContractProductVoDao;
import com.chenbaiyu.vo.ContractProductVo;
import com.chenbaiyu.service.cargo.ContractProductVoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class ContractProductVoServiceImpl implements ContractProductVoService {
    @Autowired
    ContractProductVoDao contractProductVoDao;
    @Override
    public List<ContractProductVo> findByShipTime(String shipTime, String companyId) {
        return contractProductVoDao.findByShipTime(shipTime,companyId);
    }
}
