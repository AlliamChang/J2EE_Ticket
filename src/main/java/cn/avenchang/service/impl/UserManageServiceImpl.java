package cn.avenchang.service.impl;

import cn.avenchang.dao.UserDao;
import cn.avenchang.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.avenchang.service.UserManageService;

/**
 * Created by 53068 on 2018/3/7 0007.
 */
@Service
public class UserManageServiceImpl implements UserManageService{

    @Autowired
    private UserDao userDao;

    @Override
    public User findUser(Long id) {
        return userDao.findUser(id);
    }
}
