package cn.avenchang.controller;

import cn.avenchang.config.Constant;
import cn.avenchang.model.ResultMessage;
import cn.avenchang.model.ValidInfo;
import cn.avenchang.service.LoginService;
import cn.avenchang.state.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * Created by 53068 on 2018/3/7 0007.
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

//    @RequestMapping(value = "/hello")
//    public String hello() {
//        emailService.sendEmail("530685625@qq.com", "test", "test");
//        return "hello!";
//    }

    @RequestMapping(value = "/register", method = RequestMethod.PUT)
    public String register(ModelMap map,
                           @RequestParam(value = "email") String email,
                           @RequestParam(value = "password") String password,
                           @RequestParam(value = "username") String username,
                           @RequestParam(value = "real_name") String realName){
        final ResultMessage<String> resultMessage = loginService.userRegister(email, password, username, realName);
        map.addAttribute("result", resultMessage.result);
        map.addAttribute("status", resultMessage.status);
        return "register_hint";
    }

    @RequestMapping(value = "/venue_register", method = RequestMethod.PUT)
    public String venueRegister(ModelMap map,
                                @RequestParam(value = "email") String email,
                                @RequestParam(value = "password") String password,
                                @RequestParam(value = "venueName") String venueName,
                                @RequestParam(value = "location") String location){
        final ResultMessage<String> resultMessage = loginService.venueRegister(
                email,
                password,
                venueName,
                location
        );
        map.addAttribute("result", resultMessage.result);
        map.addAttribute("status", resultMessage.status);
        return "register_hint";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout(HttpSession session){
        Role role = (Role) session.getAttribute(Constant.ROLE_ATTR);
        Long id = (Long) session.getAttribute(Constant.ID_ATTR);
        String token = (String) session.getAttribute(Constant.TOKEN_ATTR);

        session.removeAttribute(Constant.ROLE_ATTR);
        session.removeAttribute(Constant.ID_ATTR);
        session.removeAttribute(Constant.TOKEN_ATTR);

        if (role != null && id != null && token != null) {
            loginService.logout(role, id, token);
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ModelAndView userFirstLogin(HttpSession session,
                                   RedirectAttributes redirectAttributes,
                             @RequestParam(value = "email") String email,
                             @RequestParam(value = "password") String password) {
        ModelAndView view = new ModelAndView();
        ResultMessage<ValidInfo> resultMessage = loginService.userFirstLogin( email, password);
        if(resultMessage.status == ResultMessage.OK){
            session.setAttribute(Constant.ROLE_ATTR, Role.USER);
            session.setAttribute(Constant.ID_ATTR, resultMessage.result.getId());
            session.setAttribute(Constant.TOKEN_ATTR, resultMessage.result.getToken());
            view.setViewName("redirect:".concat("/user"));
            return view;
        }else {
            redirectAttributes.addFlashAttribute("msg", resultMessage.message);
            view.setViewName("redirect:".concat("/"));
            return view;
//            return "index";
        }
    }

    @RequestMapping(value = "/venue", method = RequestMethod.POST)
    public ModelAndView venueFirstLogin(HttpSession session,
                                        RedirectAttributes redirectAttributes,
                                        @RequestParam(value = "id") Long id,
                                        @RequestParam(value = "password") String password) {
        ModelAndView view = new ModelAndView();
        ResultMessage<ValidInfo> resultMessage = loginService.venueFirstLogin(id, password);
        if (resultMessage.status == ResultMessage.OK) {
            session.setAttribute(Constant.ROLE_ATTR, Role.VENUE);
            session.setAttribute(Constant.ID_ATTR, resultMessage.result.getId());
            session.setAttribute(Constant.TOKEN_ATTR, resultMessage.result.getToken());
            view.setViewName("redirect:".concat("/venue"));
        }else {
            redirectAttributes.addFlashAttribute("msg", resultMessage.message);
            view.setViewName("redirect:".concat("/"));
        }
        return view;
    }

    @RequestMapping(value = "/manager", method = RequestMethod.POST)
    public ModelAndView managerFirstLogin(HttpSession session,
                                          RedirectAttributes redirectAttributes,
                                          @RequestParam(value = "username") String username,
                                          @RequestParam(value = "password") String password){
        ModelAndView view = new ModelAndView();
        ResultMessage<ValidInfo> resultMessage = loginService.managerFirstLogin(username, password);
        if (resultMessage.status == ResultMessage.OK) {
            session.setAttribute(Constant.ROLE_ATTR, Role.MANAGER);
            session.setAttribute(Constant.ID_ATTR, resultMessage.result.getId());
            session.setAttribute(Constant.TOKEN_ATTR, resultMessage.result.getToken());
            view.setViewName("redirect:".concat("/manager"));
        }else {
            redirectAttributes.addFlashAttribute("msg", resultMessage.message);
            view.setViewName("redirect:".concat("/"));
        }
        return view;
    }

    @ResponseBody
    @RequestMapping(value = "/{id}/{verification}", method = RequestMethod.GET)
    public String verify(@PathVariable Long id,@PathVariable String verification){
        loginService.userVerify(id, verification);
        return "Success";
    }

    @ResponseBody
    @RequestMapping(value = "/user", method = RequestMethod.DELETE)
    public String delete(HttpSession session) {
        Long id = (Long) session.getAttribute(Constant.ID_ATTR);
        String token = (String) session.getAttribute(Constant.TOKEN_ATTR);
        if (id != null && token != null) {
            ResultMessage<String> resultMessage = loginService.delete(id, token);
            if (resultMessage.status == ResultMessage.OK) {
                return resultMessage.result;
            }else {
                return resultMessage.message;
            }
        }
        return "未授权操作";
    }

    @ResponseBody
    @RequestMapping(value = "/delete{id}/{verification}", method = RequestMethod.GET)
    public String confirmDelete(HttpSession session, @PathVariable Long id, @PathVariable String verification) {
        Long nowId = (Long) session.getAttribute(Constant.ID_ATTR);
        String token = (String) session.getAttribute(Constant.TOKEN_ATTR);
        if(!id.equals(nowId)){
            return "请登录本人账号操作";
        }
        ResultMessage<String> resultMessage = loginService.confirmDelete(id, verification, token);
        if (resultMessage.status == ResultMessage.OK) {
            session.removeAttribute(Constant.ROLE_ATTR);
            session.removeAttribute(Constant.ID_ATTR);
            session.removeAttribute(Constant.TOKEN_ATTR);
            return resultMessage.result;
        }else {
            return resultMessage.message;
        }
    }
}
