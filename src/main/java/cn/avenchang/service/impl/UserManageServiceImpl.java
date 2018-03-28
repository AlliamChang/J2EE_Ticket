package cn.avenchang.service.impl;

import cn.avenchang.config.DefaultConfig;
import cn.avenchang.dao.UserDao;
import cn.avenchang.domain.User;
import cn.avenchang.model.ResultMessage;
import cn.avenchang.model.UserInfo;
import cn.avenchang.util.PointsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.avenchang.service.UserManageService;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by 53068 on 2018/3/7 0007.
 */
@Service
public class UserManageServiceImpl implements UserManageService{

    @Autowired
    private UserDao userDao;
    private final Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public ResultMessage<Boolean> updateInfo(UserInfo userInfo, String token) {
        final User user = new User();
        user.setEmail(userInfo.getEmail());
        user.setRealName(userInfo.getRealName());
        user.setUsername(userInfo.getUsername());
        user.setToken(token);
        if (userDao.updateInfo(user) > 0) {
            return new ResultMessage<Boolean>(ResultMessage.OK, true);
        }

        return new ResultMessage<Boolean>(ResultMessage.FAIL, false,"签名过期，重新登录");
    }

    @Override
    public ResultMessage<UserInfo> getInfo(final Long id) {
        final User user = userDao.findUserByID(id);
        if (user == null) {
            logger.log(Level.WARNING, "未找到用户信息");
            return new ResultMessage<UserInfo>(ResultMessage.FAIL, "未找到用户信息");
        }
        final UserInfo userInfo = new UserInfo();
        userInfo.setEmail(user.getEmail());
        userInfo.setRealName(user.getRealName());
        userInfo.setUsername(user.getUsername());
        userInfo.setRestPoints(user.getRestPoints());
        userInfo.setRegistTime(user.getRegistTime());
        userInfo.setLevel(PointsUtil.getLevel(user.getTotalPoints()));
        return new ResultMessage<UserInfo>(ResultMessage.OK, userInfo);
    }
}
