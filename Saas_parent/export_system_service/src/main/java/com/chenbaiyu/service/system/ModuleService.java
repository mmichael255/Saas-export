package com.chenbaiyu.service.system;

import com.chenbaiyu.domain.Module;
import com.chenbaiyu.domain.Role;
import com.chenbaiyu.domain.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ModuleService {
    List<Module> findAll();

    Module findById(String id);

    PageInfo findAllByPages(int pageNum, int pageSize);

    void addModule(Module module);

    void updateModule(Module module);

    boolean delete(String id);

    List<Module> findRoleModuleByRoleId(String roleid);

    List<Module> findModuleByUser(User user);
}
