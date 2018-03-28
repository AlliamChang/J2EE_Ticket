package cn.avenchang.config;

import cn.avenchang.service.LoginService;
import cn.avenchang.state.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by 53068 on 2018/3/27 0027.
 */
@Component
public class TokenVerifyInterceptor extends HandlerInterceptorAdapter{
    @Autowired
    private LoginService loginService;

    private Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean flag = false;
        HttpSession session = request.getSession();
        Role role = (Role) session.getAttribute(Constant.ROLE_ATTR);
        Long id = (Long) session.getAttribute(Constant.ID_ATTR);
        String token = (String) session.getAttribute(Constant.TOKEN_ATTR);
        if (role != null && id != null && token != null) {
            logger.log(Level.INFO, role.name());
            logger.log(Level.INFO, id.toString());
            logger.log(Level.INFO, token);
            flag = loginService.autoLogin(role, id, token).result;
        }
        if (!flag) {
            logger.log(Level.WARNING, "unauthorized");
            session.removeAttribute(Constant.ROLE_ATTR);
            session.removeAttribute(Constant.ID_ATTR);
            session.removeAttribute(Constant.TOKEN_ATTR);
            response.sendRedirect("/");
        }else {
            logger.log(Level.INFO, request.getServletPath());
            switch (role) {
                case USER:
                    if (!request.getServletPath().startsWith("/user")) {
                        response.sendRedirect("/user");
                    }
                    break;
                case VENUE:
                    if(!request.getServletPath().startsWith("/venue")) {
                        response.sendRedirect("/venue");
                    }
                    break;
                case MANAGER:
                    if (!request.getServletPath().startsWith("/manager")) {
                        response.sendRedirect("/manager");
                    }
                    break;
                default:
                    response.sendRedirect("/");
            }
        }
        return flag;
    }
}
