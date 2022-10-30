package com.chenbaiyu.service.system;


import com.chenbaiyu.domain.Role;
import com.chenbaiyu.domain.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface UserService {
    List<User> findAll(String companyId);

    User findById(String id);

    PageInfo findAllByPages(int pageNum, int pageSize, String companyName);

    void addUser(User user);

    void updateUser(User user);

    Boolean delete(String id);

    User findByEmail(String email);
}
