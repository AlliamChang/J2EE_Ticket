package cn.avenchang.controller;

import cn.avenchang.config.Constant;
import cn.avenchang.model.VenueInfo;
import cn.avenchang.state.Role;
import cn.avenchang.util.SeatUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by 53068 on 2018/3/25 0025.
 */
@Controller
public class MainController {

    @RequestMapping("/")
    public String index(HttpSession httpSession,
                        @ModelAttribute String msg){
        Role role = (Role) httpSession.getAttribute(Constant.ROLE_ATTR);
        if ( role != null) {
            switch (role) {
                case MANAGER:
                    return "redirect:".concat("/manager");
                case USER:
                    return "redirect:".concat("/user");
                case VENUE:
                    return "redirect:".concat("/venue");
            }
        }
        return "index";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(@RequestParam("role") String role) {
        if(role.equals("venue")){
            return "venue_register";
        }
        return "register";
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String userIndex() {
        return "forward:/user/plan_list";
    }

    @RequestMapping(value = "/venue", method = RequestMethod.GET)
    public String venueIndex() {
        return "forward:/venue/my_plan";
    }

    @RequestMapping(value = "/manager", method = RequestMethod.GET)
    public String managerIndex() {
        return "forward:/manager/approve_list";
    }

    @RequestMapping(value = "/seat-preview", method = RequestMethod.PUT)
    public ModelAndView seatPreview(VenueInfo venueInfo) {
        ModelAndView view = new ModelAndView("seat_preview");
        Logger.getLogger(getClass().getName()).log(Level.INFO, ""+venueInfo.getArea().size());
        view.addObject("seatMap", SeatUtil.getSeatMap(venueInfo.getArea()));
        return view;
    }

}
