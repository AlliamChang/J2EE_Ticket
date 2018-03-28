package cn.avenchang.service;

import cn.avenchang.domain.User;
import cn.avenchang.model.ResultMessage;
import cn.avenchang.model.UserInfo;
import org.apache.ibatis.annotations.Select;

/**
 * Created by 53068 on 2018/3/7 0007.
 */
public interface UserManageService {

    ResultMessage<Boolean> updateInfo(UserInfo userInfo, String token);

    ResultMessage<UserInfo> getInfo(Long id);

}
