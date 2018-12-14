package cn.jeelearn.house.web.controller;

import cn.jeelearn.house.common.model.User;
import cn.jeelearn.house.common.result.ResultMsg;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/12
 * @Version:v1.0
 */
public class UserHelper {

    /**
     * 账号信息校验
     *      这里是简单实现
     * @param account
     * @return
     */
    public static ResultMsg validate(User account){
        if (StringUtils.isBlank(account.getEmail())){
            return ResultMsg.errorMsg(("Email 为空"));
        }
        if (StringUtils.isBlank(account.getConfirmPasswd()) || StringUtils.isBlank(account.getPasswd())
                || !account.getPasswd().equals(account.getConfirmPasswd())) {
            return ResultMsg.errorMsg("密码 有误");
        }
        if (account.getPasswd().length() < 6) {
            return ResultMsg.errorMsg("密码大于6位");
        }
        return ResultMsg.successMsg("");
    }

}

