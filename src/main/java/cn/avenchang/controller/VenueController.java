package cn.avenchang.controller;

import cn.avenchang.config.Constant;
import cn.avenchang.domain.Plan;
import cn.avenchang.domain.Venue;
import cn.avenchang.model.ResultMessage;
import cn.avenchang.service.PlanService;
import cn.avenchang.service.SeatService;
import cn.avenchang.service.VenueManageService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by 53068 on 2018/3/28 0028.
 */
@Controller
@RequestMapping("/venue")
public class VenueController {
    @Autowired
    private VenueManageService venueManageService;
    @Autowired
    private SeatService seatService;
    @Autowired
    private PlanService planService;

    private Logger logger = Logger.getLogger(getClass().getName());

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping(value = "/my_plan", method = RequestMethod.GET)
    public ModelAndView myPlan(HttpSession session) {
        ModelAndView view = new ModelAndView("/venue/my_plan");
        Long id = (Long) session.getAttribute(Constant.ID_ATTR);
        view.addObject("plans", planService.getVenuePlan(id));
        return view;
    }

    @RequestMapping(value = "/my_plan", method = RequestMethod.POST)
    public ModelAndView newPlan(HttpSession session,
                                RedirectAttributes redirectAttributes,
                                Plan plan) {
        ModelAndView view = new ModelAndView("redirect:/venue/my_plan");

//        if(result.hasErrors()){
//            List<ObjectError> ls = result.getAllErrors();
//            for (int i = 0; i < ls.size(); i++) {
//                System.out.println("error:"+ls.get(i));
//            }
//        }
        Long venueId = (Long) session.getAttribute(Constant.ID_ATTR);
        plan.setVenueId(venueId);
        plan.setVenueName(venueManageService.getVenueName(venueId));
        ResultMessage<Boolean> resultMessage = venueManageService.releasePlan(plan);
        redirectAttributes.addFlashAttribute("status", resultMessage.status);
        redirectAttributes.addFlashAttribute("msg", resultMessage.message);
        return view;
    }

    @RequestMapping(value = "/new_plan", method = RequestMethod.GET)
    public ModelAndView newPlanPage(HttpSession session) {
        ModelAndView view = new ModelAndView("/venue/new_plan");
        Long id = (Long) session.getAttribute(Constant.ID_ATTR);
        view.addObject("areas", seatService.getSeatMap(id));
        return view;
    }

    @RequestMapping(value = "/venue_info", method = RequestMethod.GET)
    public ModelAndView getInfo(HttpSession session){
        ModelAndView view = new ModelAndView("/venue/venue_info");
        Long id = (Long) session.getAttribute(Constant.ID_ATTR);
        ResultMessage<Venue> result = venueManageService.getInfo(id);
        if (result.status == ResultMessage.OK) {
            view.addObject("venue", result.result);
        } else {
            view.addObject("msg", result.message);
        }
        return view;
    }

    @ResponseBody
    @RequestMapping(value = "/venue_info", method = RequestMethod.PUT)
    public ResultMessage<Boolean> saveInfo(HttpSession session,
                                           @RequestParam("id") final Long id,
                                           @RequestParam("name") String name,
                                           @RequestParam("location") String location){
        logger.log(Level.INFO, name.toString());
        final Venue venue = new Venue();
        venue.setId(id);
        venue.setName(name);
        venue.setLocation(location);
        return venueManageService.updateInfo(venue);
    }

    @RequestMapping(value = "/venue_statistic", method = RequestMethod.GET)
    public ModelAndView getVenueStatistic() {
        ModelAndView view = new ModelAndView("/venue/venue_statistic");
        return view;
    }

    @RequestMapping(value = "/check_in", method = RequestMethod.GET)
    public String checkInPage() {
        return "/venue/check_in";
    }

    @RequestMapping(value = "/check_in", method = RequestMethod.PUT)
    public ModelAndView checkIn(@RequestParam("ticketId") Long ticketId) {
        ModelAndView view = new ModelAndView("/venue/check_in");
        return view;
    }
}
