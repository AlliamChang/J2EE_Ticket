package cn.avenchang.controller;

import cn.avenchang.config.Constant;
import cn.avenchang.model.ResultMessage;
import cn.avenchang.model.UserInfo;
import cn.avenchang.service.PlanService;
import cn.avenchang.service.UserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by 53068 on 2018/3/27 0027.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = Logger.getLogger(getClass().getName());
    @Autowired
    private UserManageService userManageService;
    @Autowired
    private PlanService planService;

    @RequestMapping(value = "/plan_list", method = RequestMethod.GET)
    public ModelAndView planList() {
        ModelAndView view = new ModelAndView("user/plan_list");
        view.addObject("plans", planService.getAllPlans());
        return view;
    }

    @RequestMapping(value = "/plan/{id}", method = RequestMethod.GET)
    public ModelAndView planDetail(@PathVariable Long id) {
        ModelAndView view = new ModelAndView("/user/plan_detail");
        return view;
    }

    @RequestMapping(value = "/my_order", method = RequestMethod.GET)
    public ModelAndView myOrder() {
        ModelAndView view = new ModelAndView("/user/my_order");
        return view;
    }

    @RequestMapping(value = "/my_info", method = RequestMethod.GET)
    public ModelAndView myInfo(HttpSession session) {
        ModelAndView view = new ModelAndView("/user/my_info");
        final Long id = (Long) session.getAttribute(Constant.ID_ATTR);
        ResultMessage<UserInfo> result = userManageService.getInfo(id);
        if (result.status == ResultMessage.OK) {
            view.addObject("user", result.result);
        } else {
            view.addObject("msg", result.message);
        }
        return view;
    }

    @ResponseBody
    @RequestMapping(value = "/my_info", method = RequestMethod.POST)
    public ResultMessage<Boolean> saveInfo(HttpSession session,
                                           @RequestParam("email") String email,
                                           @RequestParam("realName") String realName,
                                           @RequestParam("username") String username) {
        logger.log(Level.INFO, realName);
        logger.log(Level.INFO, username);
        final UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);
        userInfo.setUsername(username);
        userInfo.setRealName(realName);
        return userManageService.updateInfo(userInfo, (String)session.getAttribute(Constant.TOKEN_ATTR));
    }

}
