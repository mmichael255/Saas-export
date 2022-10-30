package com.chenbaiyu.dao.system;

import com.chenbaiyu.domain.Role;
import org.apache.ibatis.annotations.Param;


import java.util.List;

public interface RoleDao {
    List<Role> findAll(String companyId);

    Role findById(String id);

    void addRole(Role role);

    void updateRole(Role role);

    Boolean deleteRole(String id);

    void deleteRoleModule(String roleid);

    void addRoleModule(@Param("roleid") String roleid, @Param("moduleId") String moduleId);

    List<Role> findUserRoleByUserId(String id);

    void deleteUserRoleById(String userid);

    void addUserRole(@Param("userid") String userid,@Param("roleId") String roleId);
}
