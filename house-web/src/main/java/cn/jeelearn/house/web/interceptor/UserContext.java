package cn.jeelearn.house.web.interceptor;

import cn.jeelearn.house.common.model.User;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/16
 * @Version:v1.0
 */
public class UserContext {
    private static final ThreadLocal<User> USER_HODLER = new ThreadLocal<>();

    public static void setUser(User user){
        USER_HODLER.set(user);
    }

    public static void remove(){
        USER_HODLER.remove();
    }

    public static User getUser(){
        return USER_HODLER.get();
    }
}

