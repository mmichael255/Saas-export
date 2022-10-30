package com.chenbaiyu.service.cargo;

import com.chenbaiyu.vo.ContractProductVo;

import java.util.List;

public interface ContractProductVoService {
    List<ContractProductVo> findByShipTime(String shipTime,String companyId);
}
