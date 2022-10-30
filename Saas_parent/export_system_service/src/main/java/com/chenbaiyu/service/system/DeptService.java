package com.chenbaiyu.service.system;

import com.chenbaiyu.domain.Dept;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface DeptService {

    List<Dept> findAll(String companyId);

    PageInfo<Dept> findAllByPages(int pageNum, int pageSize, String companyId);

    Dept findById(String id);

    void add(Dept dept);

    void updateById(Dept dept);

    Boolean delete(String id);
}
