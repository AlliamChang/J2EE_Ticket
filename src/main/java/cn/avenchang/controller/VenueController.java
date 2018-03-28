package cn.avenchang.controller;

import cn.avenchang.config.Constant;
import cn.avenchang.domain.Venue;
import cn.avenchang.model.ResultMessage;
import cn.avenchang.service.VenueManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
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

    private Logger logger = Logger.getLogger(getClass().getName());

    @RequestMapping(value = "/my_plan", method = RequestMethod.GET)
    public ModelAndView myPlan() {
        ModelAndView view = new ModelAndView("/venue/my_plan");
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
}
