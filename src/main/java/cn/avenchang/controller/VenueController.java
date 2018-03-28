package cn.avenchang.controller;

import cn.avenchang.service.VenueManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by 53068 on 2018/3/28 0028.
 */
@Controller
@RequestMapping("/venue")
public class VenueController {
    @Autowired
    private VenueManageService venueManageService;

    @RequestMapping(value = "/my_plan", method = RequestMethod.GET)
    public ModelAndView myPlan() {
        ModelAndView view = new ModelAndView("/venue/my_plan");
        return view;
    }
}
