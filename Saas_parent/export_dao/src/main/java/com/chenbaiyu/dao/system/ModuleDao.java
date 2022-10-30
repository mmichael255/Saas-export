package com.chenbaiyu.dao.system;

import com.chenbaiyu.domain.Module;
import org.springframework.util.StringUtils;


import java.util.List;

public interface ModuleDao {
    List<Module> findAll();

    Module findById(String id);

    void addModule(Module module);

    void updateModule(Module module);

    Boolean deleteModule(String id);

    List<Module> findRoleModuleByRoleId(String roleid);

    List<Module> findModuleByUserId(String id);

    List<Module> findModuleByDegree(int i);
}
