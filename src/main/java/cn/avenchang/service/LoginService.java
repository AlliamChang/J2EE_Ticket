package cn.avenchang.service;

import cn.avenchang.model.ResultMessage;
import cn.avenchang.model.ValidInfo;
import cn.avenchang.model.VenueInfo;
import cn.avenchang.state.Role;

/**
 * Created by 53068 on 2018/3/14 0014.
 */
public interface LoginService {

    ResultMessage<String> userRegister(String email, String password, String username, String realName);

    Boolean userVerify(Long id, String verification);

    ResultMessage<ValidInfo> userFirstLogin(String email, String password);

    ResultMessage<Boolean> autoLogin(Role role, Long id, String token);

    ResultMessage<Boolean> logout(Role role, Long id, String token);

    ResultMessage<String> delete(Long id, String token);

    ResultMessage<String> confirmDelete(Long id, String verification, String token);

    ResultMessage<String> venueRegister(VenueInfo venueInfo);

    ResultMessage<ValidInfo> venueFirstLogin(Long id, String password);

    ResultMessage<ValidInfo> managerFirstLogin(String username, String password);
}
