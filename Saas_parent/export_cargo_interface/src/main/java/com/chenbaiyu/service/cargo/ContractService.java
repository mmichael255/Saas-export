package com.chenbaiyu.service.cargo;

import com.chenbaiyu.domain.cargo.Contract;
import com.chenbaiyu.domain.cargo.ContractExample;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ContractService {
    /**
     * 分页查询
     * @param contractExample 分页查询的参数
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @return
     */
    PageInfo<Contract> findByPage(ContractExample contractExample, int pageNum, int pageSize);

    /**
     * 查询所有
     */
    List<Contract> findAll(ContractExample contractExample);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Contract findById(String id);

    /**
     * 新增
     */
    void save(Contract contract);

    /**
     * 修改
     */
    void update(Contract contract);

    /**
     * 删除部门
     */
    void delete(String id);


    PageInfo<Contract> findPageByDeptId(String DeptId, int pageNum, int pageSize);
}
