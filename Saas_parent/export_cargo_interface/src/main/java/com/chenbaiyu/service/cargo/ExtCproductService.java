package com.chenbaiyu.service.cargo;

import com.chenbaiyu.domain.cargo.ExtCproduct;
import com.chenbaiyu.domain.cargo.ExtCproductExample;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ExtCproductService {
    /**
     * 分页查询
     */
    PageInfo<ExtCproduct> findByPage(
            ExtCproductExample extCproductExample, int pageNum, int pageSize);

    /**
     * 查询所有
     */
    List<ExtCproduct> findAll(ExtCproductExample extCproductExample);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    ExtCproduct findById(String id);

    /**
     * 新增
     */
    void save(ExtCproduct extCproduct);

    /**
     * 修改
     */
    void update(ExtCproduct extCproduct);

    /**
     * 删除部门
     */
    void delete(String id);
}
