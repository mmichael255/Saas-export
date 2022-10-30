package com.chenbaiyu.service.system.Impl;

import com.chenbaiyu.dao.system.ModuleDao;
import com.chenbaiyu.domain.Module;
import com.chenbaiyu.domain.User;
import com.chenbaiyu.service.system.ModuleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ModuleServiceImpl implements ModuleService {
    @Autowired
    ModuleDao moduleDao;

    @Override
    public List<Module> findAll() {
        return moduleDao.findAll();
    }

    @Override
    public Module findById(String id) {
        return moduleDao.findById(id);
    }

    @Override
    public PageInfo<Module> findAllByPages(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Module> all = moduleDao.findAll();
        return new PageInfo<>(all);
    }

    @Override
    public void addModule(Module module) {
        module.setId(UUID.randomUUID().toString());
        moduleDao.addModule(module);
    }

    @Override
    public void updateModule(Module module) {
        moduleDao.updateModule(module);
    }

    @Override
    public boolean delete(String id) {
        moduleDao.deleteModule(id);
        return true;
    }

    @Override
    public List<Module> findRoleModuleByRoleId(String roleid) {
        return moduleDao.findRoleModuleByRoleId(roleid);
    }


    @Override
    public List<Module> findModuleByUser(User user) {
        Integer degree = user.getDegree();
        if (degree == 0){
            return  moduleDao.findModuleByDegree(0);
        }else if (degree == 1){
            return moduleDao.findModuleByDegree(1);
        }else {
            return moduleDao.findModuleByUserId(user.getId());
        }
    }
}
