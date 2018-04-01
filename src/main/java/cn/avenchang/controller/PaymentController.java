package cn.avenchang.controller;

import cn.avenchang.config.Constant;
import cn.avenchang.model.ResultMessage;
import cn.avenchang.service.PaymentService;
import cn.avenchang.service.UserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by 53068 on 2018/3/29 0029.
 */
@Controller
@RequestMapping("/alipay")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserManageService userManageService;

    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public ModelAndView readyPay(HttpSession session,
                                 @RequestParam("return") String returnUrl,
                                 @RequestParam("orderId") Long orderId,
                                 @RequestParam("callback") String callback,
                                 @RequestParam("amount") Double amount,
                                 @RequestParam(value = "useProfit", required = false) boolean useProfit) {
        ModelAndView view = new ModelAndView("/user/pay");
        session.setAttribute(Constant.ORDER_ID, orderId);
        session.setAttribute(Constant.USE_PROFIT, useProfit);
        session.setAttribute(Constant.CALLBACK, callback);
        session.setAttribute("amount", amount);
        session.setAttribute("return", returnUrl);
        return view;
    }

    @RequestMapping(value = "/pay", method = RequestMethod.PUT)
    public ModelAndView pay(HttpSession session,
                            @RequestParam("id") Long id,
                            @RequestParam("password") String password){
        ModelAndView view = new ModelAndView("/pay_hint");
        Double amount = (Double) session.getAttribute("amount");
        ResultMessage<Boolean> resultMessage = paymentService.pay(id, password, amount);
        if (resultMessage.status == ResultMessage.OK) {
            String callback = (String) session.getAttribute(Constant.CALLBACK);
            boolean useProfit = (boolean) session.getAttribute(Constant.USE_PROFIT);
            Long orderId = (Long) session.getAttribute(Constant.ORDER_ID);
            userManageService.paid(orderId, id, useProfit);
//            RestTemplate template = new RestTemplate();
//            HttpHeaders httpHeaders = new HttpHeaders();
//            httpHeaders.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
//            httpHeaders.add("Accept", MediaType.APPLICATION_JSON.toString());
//            MultiValueMap<String, String> variable = new LinkedMultiValueMap<>();
//            variable.set("accountId", String.valueOf(id));
//            variable.set(Constant.ORDER_ID, String.valueOf(orderId));
//            variable.set(Constant.USE_PROFIT, String.valueOf(useProfit));
//            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(variable, httpHeaders);
//            System.out.println(template.postForEntity(callback, entity, String.class).toString());
//            view.addObject("callback", callback);
            view.addObject("return", session.getAttribute("return"));
            view.addObject("msg", "支付成功");
        }else {
            view.addObject("status", resultMessage.status);
            view.addObject("msg", resultMessage.message);
        }
        return view;
    }
}
