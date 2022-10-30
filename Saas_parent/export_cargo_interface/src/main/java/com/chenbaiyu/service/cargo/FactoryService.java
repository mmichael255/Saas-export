package com.chenbaiyu.service.cargo;

import com.chenbaiyu.domain.cargo.Factory;
import com.chenbaiyu.domain.cargo.FactoryExample;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface FactoryService {
    /**
     * 分页查询
     */
    PageInfo<Factory> findByPage(
            FactoryExample FactoryExample, int pageNum, int pageSize);

    /**
     * 查询所有
     */
    List<Factory> findAll(FactoryExample FactoryExample);

    Factory findByFactory(String factoryName);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    Factory findById(String id);

    /**
     * 新增
     */
    void save(Factory Factory);

    /**
     * 修改
     */
    void update(Factory Factory);

    /**
     * 删除部门
     */
    void delete(String id);
}
