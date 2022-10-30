package com.chenbaiyu.service.system.Impl;

import com.chenbaiyu.dao.system.RoleDao;
import com.chenbaiyu.domain.Role;
import com.chenbaiyu.service.system.RoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleDao roleDao;

    @Override
    public List<Role> findAll(String companyId) {
        return roleDao.findAll(companyId);
    }

    @Override
    public Role findById(String id) {
        return roleDao.findById(id);
    }

    @Override
    public PageInfo<Role> findAllByPages(int pageNum, int pageSize, String companyId) {
        PageHelper.startPage(pageNum,pageSize);
        List<Role> all = roleDao.findAll(companyId);
        return new PageInfo<>(all);
    }

    @Override
    public void addRole(Role role) {
        role.setId(UUID.randomUUID().toString());
        roleDao.addRole(role);
    }

    @Override
    public void updateRole(Role role) {
        roleDao.updateRole(role);
    }

    @Override
    public boolean delete(String id) {
        roleDao.deleteRole(id);
        return true;
    }

    @Override
    public void updateRoleModule(String roleid, String[] moduleIds) {
        roleDao.deleteRoleModule(roleid);
        if (moduleIds != null && moduleIds.length > 0){
            for (String moduleId : moduleIds) {
                roleDao.addRoleModule(roleid,moduleId);
            }
        }
    }

    @Override
    public List<Role> findUserRoleByUserId(String id) {
        return roleDao.findUserRoleByUserId(id);
    }

    @Override
    public void changeRole(String userid, String[] roleIds) {
        roleDao.deleteUserRoleById(userid);
        if (roleIds != null && roleIds.length > 0){
            for (String roleId : roleIds) {
                roleDao.addUserRole(userid,roleId);
            }
        }
    }
}
