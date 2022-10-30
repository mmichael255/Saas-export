package com.chenbaiyu.dao.company;

import com.chenbaiyu.domain.company;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CompanyDao {

    List<company> findAll();

    void update(company company);

    void add(company company);

    company findCompanyById(String id);

    void delete(String id);
}