package com.chenbaiyu.dao.system;

import com.chenbaiyu.domain.User;

import java.util.List;

public interface UserDao {

    List<User> findAll(String companyId);

    User findById(String id);

    void addUser(User user);

    void updateUser(User user);

    Boolean deleteUser(String id);

    long findUserInRole(String id);

    User findByEmail(String email);
}
