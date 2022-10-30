import com.chenbaiyu.dao.stat.StatDao;
import com.chenbaiyu.dao.system.UserDao;
import com.chenbaiyu.domain.User;
import com.chenbaiyu.service.system.DeptService;
import com.chenbaiyu.service.system.RoleService;
import com.chenbaiyu.service.system.UserService;
import com.github.pagehelper.PageInfo;


import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-*.xml")
public class Test01 {
    @Autowired
    DeptService deptService;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    UserDao userDao;

    @Autowired
    StatDao statDao;


    @Test
    public void testPageInfo(){
//        PageInfo all = deptService.findAll(1, 3, "1");
//        System.out.println("总记录数"+all.getTotal());
//        System.out.println("总页数"+all.getPages());
//        System.out.println("数据列表"+all.getList());
//        System.out.println("上一页"+all.getPrePage());
//        System.out.println("当前页"+all.getPageNum());
    }

    @Test
    public void TestTX(){
        String id = "5b3c6d42-6e3e-40a2-b1bb-60d9b7191316";
        Boolean delete = deptService.delete(id);
        System.out.println(delete);
    }

    @Test
    public void testFind1User(){
        User byId = userService.findById("3d00290a-1af0-4c28-853e-29fbf96a2722");
        System.out.println(byId);
    }

    @Test
    public void testFindInRole(){
        long userInRole = userDao.findUserInRole("10d29bc9-78c1-4831-a21a-4fcfea7d87ce");
        System.out.println(userInRole);
    }

    @Test
    public void testFindRole(){
        PageInfo allByPages = roleService.findAllByPages(1, 5, "1");
        System.out.println(allByPages.getList());
    }

    @Test
    public void encodePWD(){
        String username = "OOP@export.com";
        String pwd = "123";
        Md5Hash md5Hash = new Md5Hash(pwd, username);
        String newPwd = md5Hash.toString();
        System.out.println(newPwd);
    }

    @Test
    public void testStat(){
        List<Map<String, Object>> sellStat = statDao.findSellStat("1");
        for (Map<String, Object> stringObjectMap : sellStat) {
            System.out.println(stringObjectMap.get("name")+":"+stringObjectMap.get("value"));
        }
    }
}

