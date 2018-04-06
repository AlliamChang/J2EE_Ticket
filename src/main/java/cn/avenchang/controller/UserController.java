package cn.avenchang.controller;

import cn.avenchang.config.Constant;
import cn.avenchang.domain.Orders;
import cn.avenchang.domain.Plan;
import cn.avenchang.domain.SeatState;
import cn.avenchang.model.OrderInfo;
import cn.avenchang.model.ResultMessage;
import cn.avenchang.model.UserInfo;
import cn.avenchang.service.PlanService;
import cn.avenchang.service.SeatService;
import cn.avenchang.service.UserManageService;
import cn.avenchang.util.TicketSynchronizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @Autowired
    private SeatService seatService;

    @RequestMapping(value = "/plan_list", method = RequestMethod.GET)
    public ModelAndView planList() {
        ModelAndView view = new ModelAndView("user/plan_list");
        view.addObject("plans", planService.getAllPlans());
        return view;
    }

    @RequestMapping(value = "/plan/{id}", method = RequestMethod.GET)
    public ModelAndView planDetail(@PathVariable Long id) {
        ModelAndView view = new ModelAndView("/user/plan_detail");
        view.addObject("plan", planService.getPlanDetail(id));
        view.addObject("id", id);
        return view;
    }

    @RequestMapping(value = "/seat_info/{planId}", method = RequestMethod.GET)
    public ModelAndView getSeatInfo(@PathVariable Long planId,
                           RedirectAttributes redirectAttributes) {
        ModelAndView view = new ModelAndView("redirect:/user/plan/"+planId);
        redirectAttributes.addFlashAttribute("seatInfo", userManageService.getSeatAreaInfo(planId));
        return view;
    }

    @RequestMapping(value = "/my_order", method = RequestMethod.POST)
    public ModelAndView selectSeat(HttpSession session,
                                   RedirectAttributes redirectAttributes,
                                   Orders order) {
        ModelAndView view = new ModelAndView();
//        System.out.println(order.getSeatStates().size());
        Long userId = (Long) session.getAttribute(Constant.ID_ATTR);
        order.setUserId(userId);
        ResultMessage<Long> resultMessage;
        if (order.getTicketNum() > 0) {
            resultMessage = userManageService.buyTicketWithoutSelect(order);
        }else {
            resultMessage = userManageService.buyTicketBySelect(order.getSeatStates(), order);
        }
        if (resultMessage.status == ResultMessage.OK) {
            view.setViewName("redirect:/user/order_unpaid/" + resultMessage.result);
        } else {
            view.setViewName("redirect:/user/plan/" + order.getPlanId());
            redirectAttributes.addFlashAttribute("msg", resultMessage.message);
        }
        return view;
    }

    @RequestMapping(value = "/choose_seat/{planId}", method = RequestMethod.GET)
    public ModelAndView chooseSeatPage(@PathVariable Long planId) {
        ModelAndView view = new ModelAndView("/user/choose_seat");
        view.addObject("seat", seatService.getLiveSeatMap(planId));
        view.addObject("planId", planId);
        return view;
    }

    @RequestMapping(value = "/my_order", method = RequestMethod.GET)
    public ModelAndView myOrder(HttpSession session) {
        ModelAndView view = new ModelAndView("/user/my_order");
        Long id = (Long) session.getAttribute(Constant.ID_ATTR);
        view.addObject("orders", userManageService.myOrders(id));
        return view;
    }

    @RequestMapping(value = "/my_order", method = RequestMethod.DELETE)
    public ModelAndView refundOrder(@RequestParam("orderId") Long orderId,
                                    RedirectAttributes redirectAttributes) {
        ModelAndView view = new ModelAndView("redirect:/user/my_order");
        ResultMessage<Boolean> resultMessage = userManageService.refundOrder(orderId);
        if (resultMessage.status == ResultMessage.OK) {
            redirectAttributes.addFlashAttribute("status", resultMessage.status);
        } else {
            redirectAttributes.addFlashAttribute("msg", resultMessage.message);
        }
        return view;
    }

    @ResponseBody
    @RequestMapping(value = "/refund_percent", method = RequestMethod.GET)
    public Map refundPercent(@RequestParam("orderId") Long orderId) {
        Map result = new HashMap();
        result.put("percent", userManageService.getRefundPercent(orderId));
        return result;
    }

    @RequestMapping(value = "/order_unpaid/{orderId}", method = RequestMethod.GET)
    public ModelAndView orderUnpaid(@PathVariable Long orderId,
                                    HttpSession session) {
        ModelAndView view = new ModelAndView("/user/order_unpaid");
        ResultMessage<OrderInfo> resultMessage = userManageService.unpaidOrder(orderId);
        if (resultMessage.status == ResultMessage.OK) {
            Long id = (Long) session.getAttribute(Constant.ID_ATTR);
            view.addObject("isEnough", userManageService.isEnoughToProfit(id));
            view.addObject("orderInfo", resultMessage.result);
        } else {
            view.setViewName("/user/my_order");
            view.addObject("msg", resultMessage.message);
        }
        return view;
    }

    @ResponseBody
    @RequestMapping(value = "/paid", method = RequestMethod.POST)
    public String pay(@RequestParam("orderId") Long orderId,
                      @RequestParam("accountId") Long accountId,
                      @RequestParam("useProfit") boolean useProfit) {

        System.out.println("done");
        userManageService.paid(orderId, accountId, useProfit);

        return "done";
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
        return userManageService.updateInfo(userInfo, (String) session.getAttribute(Constant.TOKEN_ATTR));
    }

}
