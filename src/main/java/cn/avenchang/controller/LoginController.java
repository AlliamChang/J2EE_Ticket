package cn.avenchang.controller;

import cn.avenchang.domain.User;
import cn.avenchang.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cn.avenchang.service.UserManageService;

/**
 * Created by 53068 on 2018/3/7 0007.
 */
@RestController
public class LoginController {

    @Autowired
    private UserManageService userManageService;
    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/hello")
    public String hello() {
        emailService.sendEmail("530685625@qq.com", "test", "test");
        return "hello!";
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public User findOneUser(@RequestParam(value = "id", required = true) Long id) {
        return userManageService.findUser(id);
    }
}
