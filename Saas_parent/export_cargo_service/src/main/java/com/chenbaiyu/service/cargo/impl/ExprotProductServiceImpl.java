package com.chenbaiyu.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chenbaiyu.dao.cargo.ExportProductDao;
import com.chenbaiyu.domain.cargo.ExportProduct;
import com.chenbaiyu.domain.cargo.ExportProductExample;
import com.chenbaiyu.service.cargo.ExportProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ExprotProductServiceImpl implements ExportProductService {
    @Autowired
    ExportProductDao exportProductDao;

    @Override
    public ExportProduct findById(String id) {
        return exportProductDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(ExportProduct exportProduct) {
        exportProduct.setId(UUID.randomUUID().toString());
        exportProduct.setCreateTime(new Date());
        exportProductDao.insertSelective(exportProduct);
    }

    @Override
    public void update(ExportProduct exportProduct) {
        exportProduct.setUpdateTime(new Date());
        exportProductDao.updateByPrimaryKeySelective(exportProduct);
    }

    @Override
    public void delete(String id) {
        exportProductDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<ExportProduct> findAll(ExportProductExample exportProductExample) {
        return exportProductDao.selectByExample(exportProductExample);
    }

    @Override
    public PageInfo<ExportProduct> findByPage(ExportProductExample exportProductExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);

        return new PageInfo<>(exportProductDao.selectByExample(exportProductExample));
    }
}
