package com.chenbaiyu.service.system.Impl;

import com.chenbaiyu.dao.system.UserDao;
import com.chenbaiyu.domain.User;
import com.chenbaiyu.service.system.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Override
    public List<User> findAll(String companyId) {
        return userDao.findAll(companyId);
    }

    @Override
    public User findById(String id) {
        return userDao.findById(id);
    }

    @Override
    public PageInfo<User> findAllByPages(int pageNum,int pageSize,String companyId) {
        PageHelper.startPage(pageNum,pageSize);
        List<User> userList = userDao.findAll(companyId);
        return new PageInfo<>(userList);
    }

    @Override
    public void addUser(User user) {
        user.setId(UUID.randomUUID().toString());
        userDao.addUser(user);
    }

    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    @Override
    public Boolean delete(String id) {
        long count = userDao.findUserInRole(id);
        if (count > 0){
            return false;
        }else{
            userDao.deleteUser(id);
            return true;
        }
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }
}
