package cn.avenchang.controller;

import cn.avenchang.model.ResultMessage;
import cn.avenchang.service.ManagerService;
import cn.avenchang.service.SeatService;
import cn.avenchang.util.SeatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Created by 53068 on 2018/3/28 0028.
 */
@Controller
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;
    @Autowired
    private SeatService seatService;

    @RequestMapping(value = "/approve_list", method = RequestMethod.GET)
    public ModelAndView approveList() {
        ModelAndView view = new ModelAndView("/manager/approve_list");
        view.addObject("registerVenues", managerService.getToApproveRegisterList());
        view.addObject("updateVenues", managerService.getToApproveUpdateList());
        return view;
    }

    @RequestMapping(value = "/approve_register", method = RequestMethod.PUT)
    public ModelAndView approveRegister(@RequestParam Long venueId,
                                        RedirectAttributes redirectAttributes) {
        ModelAndView view = new ModelAndView("redirect:/manager/approve_list");
        ResultMessage<Boolean> resultMessage = managerService.approveRegister(venueId);
        if (resultMessage.status == ResultMessage.FAIL) {
            redirectAttributes.addFlashAttribute("msg", resultMessage.message);
        } else if (resultMessage.status == ResultMessage.OK) {
            redirectAttributes.addFlashAttribute("status", ResultMessage.OK);
        }
        return view;
    }

    @RequestMapping(value = "/approve_update", method = RequestMethod.PUT)
    public ModelAndView approveUpdate(@RequestParam Long venueId,
                                      RedirectAttributes redirectAttributes) {
        ModelAndView view = new ModelAndView("redirect:/manager/approve_list");
        ResultMessage<Boolean> resultMessage = managerService.approveUpdateInfo(venueId);
        if (resultMessage.status == ResultMessage.FAIL) {
            redirectAttributes.addFlashAttribute("msg", resultMessage.message);
        } else if (resultMessage.status == ResultMessage.OK) {
            redirectAttributes.addFlashAttribute("status", ResultMessage.OK);
        }
        return view;
    }

    @RequestMapping(value = "refuse_register", method = RequestMethod.PUT)
    public ModelAndView refuseRegister(@RequestParam Long venueId,
                                       RedirectAttributes redirectAttributes) {
        ModelAndView view = new ModelAndView("redirect:/manager/approve_list");
        ResultMessage<Boolean> resultMessage = managerService.refuseRegister(venueId);
        if (resultMessage.status == ResultMessage.FAIL) {
            redirectAttributes.addFlashAttribute("msg", resultMessage.message);
        } else if (resultMessage.status == ResultMessage.OK) {
            redirectAttributes.addFlashAttribute("status", ResultMessage.OK);
        }
        return view;
    }

    @RequestMapping(value = "refuse_update", method = RequestMethod.PUT)
    public ModelAndView refuseUpdate(@RequestParam Long venueId,
                                     RedirectAttributes redirectAttributes) {
        ModelAndView view = new ModelAndView("redirect:/manager/approve_list");
        ResultMessage<Boolean> resultMessage = managerService.refuseUpdateInfo(venueId);
        if (resultMessage.status == ResultMessage.FAIL) {
            redirectAttributes.addFlashAttribute("msg", resultMessage.message);
        } else if (resultMessage.status == ResultMessage.OK) {
            redirectAttributes.addFlashAttribute("status", ResultMessage.OK);
        }
        return view;
    }

    @RequestMapping(value = "/seat_map/{venueId}", method = RequestMethod.GET)
    public ModelAndView seatMap(@PathVariable Long venueId) {
        ModelAndView view = new ModelAndView("/seat_preview");
        final List<String> map = SeatUtil.getSeatMap(seatService.getSeatMap(venueId));
        view.addObject("seatMap", map);
        return view;
    }
}
