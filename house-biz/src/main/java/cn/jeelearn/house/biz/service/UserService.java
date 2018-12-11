package cn.jeelearn.house.biz.service;

import cn.jeelearn.house.biz.mapper.UserMapper;
import cn.jeelearn.house.common.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/11
 * @Version:v1.0
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public List<User> queryUsers(){
        return userMapper.queryUsers();
    }
}

