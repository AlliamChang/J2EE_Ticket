package cn.avenchang.controller;

import cn.avenchang.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by 53068 on 2018/3/28 0028.
 */
@Controller
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @RequestMapping(value = "/approve_list", method = RequestMethod.GET)
    public ModelAndView approveList() {
        ModelAndView view = new ModelAndView("/manager/approve_list");
        return view;
    }
}
