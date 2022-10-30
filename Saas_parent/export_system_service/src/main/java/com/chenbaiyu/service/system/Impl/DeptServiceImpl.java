package com.chenbaiyu.service.system.Impl;

import com.chenbaiyu.dao.system.DeptDao;
import com.chenbaiyu.domain.Dept;
import com.chenbaiyu.service.system.DeptService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    DeptDao deptDao;

    @Override
    public List<Dept> findAll(String companyId) {

        return deptDao.findAll(companyId);
    }

    @Override
    public PageInfo<Dept> findAllByPages(int pageNum, int pageSize, String companyId) {
        PageHelper.startPage(pageNum,pageSize);
        List<Dept> all = deptDao.findAll(companyId);
        return new PageInfo<>(all);
    }

    @Override
    public Dept findById(String id) {
        return deptDao.findById(id);
    }

    @Override
    public void add(Dept dept) {
        dept.setId(UUID.randomUUID().toString());
        deptDao.add(dept);
    }

    @Override
    public void updateById(Dept dept) {
        deptDao.updateById(dept);
    }

    @Override
    public Boolean delete(String id) {
        long count = deptDao.findChildById(id);
        if (count > 0){
            return false;
        }else {
            deptDao.deleteById(id);
            return true;
        }
    }
}
