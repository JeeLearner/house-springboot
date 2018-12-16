package cn.jeelearn.house.web.interceptor;

import cn.jeelearn.house.common.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/16
 * @Version:v1.0
 */
@Component
public class AuthActionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        User user = UserContext.getUser();
        if (user == null){
            String msg = URLEncoder.encode("请先登录", "utf-8");
            //将目前的url记录下来
            String target = URLEncoder.encode(httpServletRequest.getRequestURL().toString(), "utf-8");
            //修复bug,未登录要返回false
            if("GET".equalsIgnoreCase(httpServletRequest.getMethod())){
                httpServletResponse.sendRedirect("/accounts/signin?errorMsg=" + msg + "&target="+target);
                return false;
            } else {
                httpServletResponse.sendRedirect("/accounts/signin?errorMsg="+msg);
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}

