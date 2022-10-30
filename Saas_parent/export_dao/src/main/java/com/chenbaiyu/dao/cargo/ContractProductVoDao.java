package com.chenbaiyu.dao.cargo;

import com.chenbaiyu.vo.ContractProductVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContractProductVoDao {
    List<ContractProductVo> findByShipTime(@Param("shipTime") String shipTime, @Param("companyId") String companyId);
}
