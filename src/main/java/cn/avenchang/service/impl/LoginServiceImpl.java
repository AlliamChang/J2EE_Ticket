package cn.avenchang.service.impl;

import cn.avenchang.config.DefaultConfig;
import cn.avenchang.dao.ManagerDao;
import cn.avenchang.dao.SeatDao;
import cn.avenchang.dao.UserDao;
import cn.avenchang.dao.VenueDao;
import cn.avenchang.domain.Manager;
import cn.avenchang.domain.User;
import cn.avenchang.domain.Venue;
import cn.avenchang.model.ValidInfo;
import cn.avenchang.model.VenueInfo;
import cn.avenchang.service.EmailService;
import cn.avenchang.service.LoginService;
import cn.avenchang.model.ResultMessage;
import cn.avenchang.state.Role;
import cn.avenchang.state.UserState;
import cn.avenchang.state.VenueState;
import cn.avenchang.util.MD5;
import org.apache.ibatis.executor.ExecutorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by 53068 on 2018/3/25 0025.
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private ManagerDao managerDao;
    @Autowired
    private VenueDao venueDao;
    @Autowired
    private SeatDao seatDao;
    @Autowired
    private EmailService emailService;
    @Autowired
    private DefaultConfig config;

    @Override
    public ResultMessage<String> userRegister(final String email, final String password, final String username, final String realName) {
        final User user = userDao.findUser(email);
        if (user != null) {
            return new ResultMessage<>(ResultMessage.FAIL, "该邮箱已被注册", "该邮箱已被注册");
        }
        String token = MD5.encrypt(email + password + new Date().toString());
        String passwdEncrypt = MD5.encrypt(password);
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(passwdEncrypt);
        newUser.setRealName(realName);
        newUser.setUsername(username);
        newUser.setVerification(token);
        userDao.register(newUser);
        if ( newUser.getId() > 0) {
//            System.out.println("Success");
            final String content = "请将下列地址复制到地址栏中: " + config.getHost() + "/login/" + newUser.getId() + "/" + token;
            new Thread(
                    () -> emailService.sendEmail(email, "邮箱验证", content)
            ).start();
            return new ResultMessage<>(ResultMessage.OK, "注册成功，验证邮件已发到您的邮箱", "");

        }

        return new ResultMessage<>(ResultMessage.FAIL, "注册失败，请稍后再试", "注册失败");
    }

    @Override
    public ResultMessage<ValidInfo> userFirstLogin(String email, String password) {
        User validUser = userDao.findUser(email);
        if (validUser != null && UserState.UNVERIFIED.ordinal() == validUser.getFlag()) {
            return new ResultMessage<ValidInfo>(ResultMessage.FAIL, "未验证用户");
        }
        final String token = MD5.encrypt(password + new Date().toString());
        User user = new User();
        user.setEmail(email);
        user.setPassword(MD5.encrypt(password));
        user.setToken(token);
        int result = 0;
        try {
            result = userDao.updateToken(user);
        } catch (ExecutorException e) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, e.toString());
        }
        if(result > 0){
            return new ResultMessage(ResultMessage.OK, new ValidInfo(user.getId(), token));
        }else {
            return new ResultMessage(ResultMessage.FAIL, "邮箱不存在或者密码错误");
        }
    }

    @Override
    public Boolean userVerify(Long id, String verification) {
        return userDao.verify(id, verification) > 0;
    }

    @Override
    public ResultMessage<Boolean> autoLogin(Role role, Long id, String token) {
        switch (role) {
            case USER:
                User user = userDao.validToken(id, token);
                if (user != null) {
                    if(UserState.UNVERIFIED.ordinal() == user.getFlag()){
                        return new ResultMessage<Boolean>(ResultMessage.FAIL, false, "未验证用户");
                    }else if(UserState.DELETED.ordinal() == user.getFlag()){
                        return new ResultMessage<Boolean>(ResultMessage.FAIL, false, "不存在用户");
                    } else{
                        return new ResultMessage<>(ResultMessage.OK, true);
                    }
                }
                break;
            case VENUE:
                Venue venue = venueDao.validToken(id, token);
                if (venue != null) {
                    if (VenueState.REGISTER_TO_AUDIT.ordinal() == venue.getFlag()) {
                        return new ResultMessage<Boolean>(ResultMessage.FAIL, false, "该场馆仍在审核中");
                    } else if (VenueState.UNAUDITED.ordinal() == venue.getFlag()) {
                        return new ResultMessage<Boolean>(ResultMessage.FAIL, false, "该场馆不通过审核");
                    }else {
                        return new ResultMessage<Boolean>(ResultMessage.OK, true);
                    }
                }
                break;
            case MANAGER:
                Integer manager = managerDao.validToken(id, token);
                if (manager > 0) {
                    return new ResultMessage<Boolean>(ResultMessage.OK, true);
                }
                break;
            default:
                System.out.println("未知错误");
        }
        return new ResultMessage<Boolean>(ResultMessage.FAIL, false, "签名已过期");
    }

    @Override
    public ResultMessage<Boolean> logout(Role role, Long id, String token) {
        switch (role) {
            case USER:
                userDao.logout(id, token);
                break;
            case VENUE:
                venueDao.logout(id, token);
                break;
            case MANAGER:
                managerDao.logout(id, token);
                break;
            default:

        }
        return new ResultMessage<Boolean>(ResultMessage.OK, true);
    }

    @Override
    public ResultMessage<String> delete(final Long id, String token) {
        final String verification = MD5.encrypt(id + token + new Date().toString());
        final User user = userDao.findUserByID(id);
        if(user == null){
            return new ResultMessage<String>(ResultMessage.UNKNOWN, "", "错误的id");
        }
        final String email = user.getEmail();
        if( userDao.delete(id, token, verification) > 0){
            final String content = "如果确认注销账号，请将下列地址复制到地址栏中: " +
                    config.getHost() + "/login/delete" + id + "/" + verification;
            new Thread(
                    () -> {
                      emailService.sendEmail(email, "再次确认操作", content);
                    }
            ).start();
            return new ResultMessage<>(ResultMessage.OK, "验证邮件已发到您的邮箱", "");

        }

        return new ResultMessage<>(ResultMessage.FAIL, "", "发送失败");
    }

    @Override
    public ResultMessage<String> confirmDelete(Long id, String verification, String token) {
        if(userDao.confirmDelete(id, token, verification) > 0){
            return new ResultMessage<String>(ResultMessage.OK, "Success", "");
        }else {
            return new ResultMessage<String>(ResultMessage.FAIL, "", "Fail");
        }
    }

    @Override
    public ResultMessage<String> venueRegister(VenueInfo venueInfo) {
        String token = MD5.encrypt(venueInfo.getEmail() + venueInfo.getPassword() + new Date().toString());
        String passwdEncrypt = MD5.encrypt(venueInfo.getPassword());
        Venue newVenue = new Venue();
        newVenue.setEmail(venueInfo.getEmail());
        newVenue.setPassword(passwdEncrypt);
        newVenue.setName(venueInfo.getVenueName());
        newVenue.setLocation(venueInfo.getLocation());
        venueDao.register(newVenue);
        if ( newVenue.getId() > 0) {
            System.out.println(venueInfo.getArea().size());
//            venueInfo.getArea().get(0).setVenueId(newVenue.getId());
            int result = seatDao.insertSeats(newVenue.getId(), venueInfo.getArea());
            final String content = "您已注册成功，现等待网站管理员进行审核，审核过后就会通知您";
            new Thread(
                    () -> emailService.sendEmail(venueInfo.getEmail(), "场馆信息注册成功！", content)
            ).start();
            return new ResultMessage<>(ResultMessage.OK, "注册成功，提示邮件已发到您的邮箱", "");

        }

        return new ResultMessage<>(ResultMessage.FAIL, "注册失败，请稍后再试", "注册失败");

    }

    @Override
    public ResultMessage<ValidInfo> venueFirstLogin(final Long id, final String password) {
        Venue validVenue = venueDao.findVenueById(id);
        if (validVenue != null && VenueState.UNAUDITED.ordinal() == validVenue.getFlag()) {
            return new ResultMessage<ValidInfo>(ResultMessage.FAIL, "该场馆不通过审核");
        }else if (validVenue != null && VenueState.REGISTER_TO_AUDIT.ordinal() == validVenue.getFlag()) {
            return new ResultMessage<ValidInfo>(ResultMessage.FAIL, "该场馆正在审核中");
        }
        final String token = MD5.encrypt(password + new Date().toString());
        Venue venue = new Venue();
        venue.setId(id);
        venue.setPassword(MD5.encrypt(password));
        venue.setToken(token);
        int result = venueDao.updateToken(venue);
        if(result > 0){
            return new ResultMessage(ResultMessage.OK, new ValidInfo(id, token));
        }else {
            return new ResultMessage(ResultMessage.FAIL, "场馆ID不存在或者密码错误");
        }
    }

    @Override
    public ResultMessage<ValidInfo> managerFirstLogin(String username, String password) {
        final String token = MD5.encrypt(password + new Date().toString());
        Manager manager = new Manager();
        manager.setUsername(username);
        manager.setPassword(MD5.encrypt(password));
        manager.setToken(token);
        int result = managerDao.updateToken(manager);
        if(result > 0){
            return new ResultMessage(ResultMessage.OK, new ValidInfo(manager.getId(), token));
        }else {
            return new ResultMessage(ResultMessage.FAIL, "管理员账号不存在或者密码错误");
        }
    }
}
