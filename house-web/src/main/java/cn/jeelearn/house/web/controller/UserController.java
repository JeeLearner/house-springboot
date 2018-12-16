package cn.jeelearn.house.web.controller;

import cn.jeelearn.house.biz.service.UserService;
import cn.jeelearn.house.common.constants.CommonConstants;
import cn.jeelearn.house.common.model.User;
import cn.jeelearn.house.common.result.ResultMsg;
import cn.jeelearn.house.common.utils.HashUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/11
 * @Version:v1.0
 */
@Controller
@RequestMapping("/accounts")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 注册提交: 1.注册验证 2 发送邮件 3验证失败重定向到注册页面
     * 注册页获取: 根据account对象为依据判断是否注册页获取请求
     * @auther: lyd
     * @date: 2018/12/11
     */
    @RequestMapping("/register")
    public String register(User account, ModelMap modelMap){
        if (account == null || account.getName() == null){
            return "/user/accounts/register";
        }
        //用户验证
        ResultMsg resultMsg = UserHelper.validate(account);
        if (resultMsg.isSuccess() && userService.addAccount(account)){
            modelMap.put("email", account.getEmail());
            return "/user/accounts/registerSubmit";
        } else {
            return "redirect:/accounts/register?" + resultMsg.asUrlParams();
        }
    }

    /**
     * 邮箱链接激活邮件
     * @auther: lyd
     * @date: 2018/12/13
     */
    @RequestMapping("/verify")
    public String verify(String key){
        boolean result = userService.enable(key);
        if (result){
            return "redirect:/index?" + ResultMsg.successMsg("激活成功").asUrlParams();
        } else {
            return "redirect:/accounts/register?" + ResultMsg.errorMsg("激活失败,请确认链接是否过期");
        }
    }

    // ----------------------------登录流程------------------------------------

    /**
     * 登录
     * @auther: lyd
     * @date: 2018/12/14
     */
    @RequestMapping("/signin")
    public String signin(HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String target = request.getParameter("target");
        if (username == null || password == null){
            request.setAttribute("target", target);
            return "/user/accounts/signin";
        }
        User user = userService.auth(username, password);
        if (user == null){
            return "redirect:/accounts/signin?" + "target=" + target + "&username=" + username + "&"
                    + ResultMsg.errorMsg("用户名或密码错误").asUrlParams();
        } else {
            HttpSession session = request.getSession(true);
            session.setAttribute(CommonConstants.USER_ATTRIBUTE, user);
            return StringUtils.isNoneBlank(target) ? "redirect:" + target : "redirect:/index";
        }

    }

    /**
     * 登出
     * @auther: lyd
     * @date: 2018/12/14
     */
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        session.invalidate();
        return "redirect:/index";
    }

    // ---------------------个人信息页-------------------------
    /**
     * 1.能够提供页面信息
     * 2.更新用户信息
     * @auther: lyd
     * @date: 2018/12/14
     */
    @RequestMapping("/profile")
    public String profile(HttpServletRequest request, User updateUser, ModelMap modelMap){
        if (updateUser.getEmail() == null){
            return "/user/accounts/profile";
        }
        userService.updateUser(updateUser, updateUser.getEmail());
        User query = new User();
        query.setEmail(updateUser.getEmail());
        List<User> users = userService.selectUsersByQuery(query);
        request.getSession(true).setAttribute(CommonConstants.USER_ATTRIBUTE, users.get(0));
        return "redirect:/accounts/profile?" + ResultMsg.successMsg("更新成功").asUrlParams();
    }

    /**
     * 修改密码
     * @auther: lyd
     * @date: 2018/12/14
     */
    @RequestMapping("/changePassword")
    public String changePassword(String email, String password, String newPassword,String confirmPassword){
        User user = userService.auth(email, password);
        if(user == null || !confirmPassword.equals(newPassword)){
            return "redirect:/accounts/profile?" + ResultMsg.errorMsg("密码错误").asUrlParams();
        }
        User updateUser = new User();
        updateUser.setPasswd(HashUtils.encryPassword(newPassword));
        userService.updateUser(updateUser, email);
        return "redirect:/accounts/profile?" + ResultMsg.successMsg("更新成功").asUrlParams();
    }

    /**
     * 忘记密码
     * @auther: lyd
     * @date: 2018/12/14
     */
    @RequestMapping("/remember")
    public String remember(String username, ModelMap modelMap){
        if (StringUtils.isBlank(username)){
            return "redirect:/accounts/signin?" + ResultMsg.errorMsg("邮箱不能为空").asUrlParams();
        }
        userService.resetNotify(username);
        modelMap.put("email", username);
        return "/user/accounts/remember";
    }

    /**
     * 密码重置链接响应
     * @auther: lyd
     * @date: 2018/12/14
     */
    @RequestMapping("/reset")
    public String reset(String key, ModelMap modelMap){
        String email = userService.getResetEmail(key);
        if (StringUtils.isBlank(email)){
            return "redirect:/accounts/signin?" + ResultMsg.errorMsg("重置链接已过期").asUrlParams();
        }
        modelMap.put("email", email);
        modelMap.put("success_key", key);
        return "/user/accounts/reset";
    }

    /**
     * 密码重置
     * @auther: lyd
     * @date: 2018/12/14
     */
    @RequestMapping(value="/resetSubmit")
    public String resetSubmit(HttpServletRequest req,User user){
        ResultMsg resultMsg = UserHelper.validateResetPassword(user.getKey(), user.getPasswd(), user.getConfirmPasswd());
        if (!resultMsg.isSuccess()){
            String suffix = "";
            if (StringUtils.isNoneBlank(user.getKey())){
                suffix = "email=" + userService.getResetEmail(user.getKey()) + "&key=" +  user.getKey() + "&";
            }
            return "redirect:/accounts/reset?"+ suffix  + resultMsg.asUrlParams();
        }
        User updatedUser =  userService.reset(user.getKey(),user.getPasswd());
        req.getSession(true).setAttribute(CommonConstants.USER_ATTRIBUTE, updatedUser);
        return "redirect:/index?" + resultMsg.asUrlParams();
    }


    /**
     * 项目启动时对框架完整性的测试
     * @return
     */
    @RequestMapping("user")
    public List<User> queryUsers(){
        return userService.selectUsers();
    }


}

