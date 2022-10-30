package com.chenbaiyu.service.system;

import com.chenbaiyu.domain.Role;
import com.chenbaiyu.domain.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface RoleService {
    List<Role> findAll(String companyId);

    Role findById(String id);

    PageInfo findAllByPages(int pageNum, int pageSize, String companyId);

    void addRole(Role role);

    void updateRole(Role role);

    boolean delete(String id);

    void updateRoleModule(String roleid, String[] moduleIds);

    List<Role> findUserRoleByUserId(String id);

    void changeRole(String userid, String[] roleIds);
}
