package com.chenbaiyu.service.company;

import com.chenbaiyu.domain.company;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CompanyService {
    List<company> findAll();

    void update(company company);

    void add(company company);

    company findCompanyById(String id);

    void delete(String id);

    public PageInfo<company> findByPage(int pageNum, int pageSize);
}
