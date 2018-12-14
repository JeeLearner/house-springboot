package cn.jeelearn.house.web;

import cn.jeelearn.house.biz.service.UserService;
import cn.jeelearn.house.common.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/14
 * @Version:v1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthTests {

    @Autowired
    private UserService userService;

    @Test
    public void testAuth(){
        User auth = userService.auth("512013674@qq.com", "111111");
        assert auth != null;
        System.out.println(auth.getEmail());
    }




}

