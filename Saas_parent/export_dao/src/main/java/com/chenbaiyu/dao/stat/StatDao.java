package com.chenbaiyu.dao.stat;

import java.util.List;
import java.util.Map;

public interface StatDao {
    List<Map<String,Object>> findSellStat(String companyId);

    List<Map<String, Object>> findProductSellStat(String companyId);

    List<Map<String, Object>> findonlineData(String companyId);
}
