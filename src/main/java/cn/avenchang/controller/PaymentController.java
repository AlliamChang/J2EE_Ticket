package cn.avenchang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by 53068 on 2018/3/29 0029.
 */
@Controller
@RequestMapping("/pay")
public class PaymentController {

    @RequestMapping(value = "/",method = RequestMethod.POST)
    public void readyPay() {

    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public void pay() {

    }
}
