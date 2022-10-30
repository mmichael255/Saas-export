package com.chenbaiyu.dao.system;

import com.chenbaiyu.domain.Dept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeptDao {

    List<Dept> findAll(String companyId);

    Dept findById(String id);

    void add(Dept dept);

    void updateById(Dept dept);

    long findChildById(@Param("deptId") String id);

    void deleteById(String id);
}
