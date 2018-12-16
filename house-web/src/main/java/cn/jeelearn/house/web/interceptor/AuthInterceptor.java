package cn.jeelearn.house.web.interceptor;

import cn.jeelearn.house.common.constants.CommonConstants;
import cn.jeelearn.house.common.model.User;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/16
 * @Version:v1.0
 */
@Component
public class AuthInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        Map<String, String[]> map = httpServletRequest.getParameterMap();
        map.forEach((k, v) -> {
            if (k.equals("errorMsg") || k.equals("successMsg") || k.equals("target")) {
                httpServletRequest.setAttribute(k, Joiner.on(",").join(v));
            }
        });
        String requestURI = httpServletRequest.getRequestURI();
        if (requestURI.startsWith("/static") || requestURI.startsWith("/error") ) {
            return true;
        }
        HttpSession session = httpServletRequest.getSession(true);
        User user =  (User)session.getAttribute(CommonConstants.USER_ATTRIBUTE);
        if (user != null) {
            UserContext.setUser(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        UserContext.remove();
    }
}

