package cn.jeelearn.house.web.controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/11
 * @Version:v1.0
 */
@RestController
public class HelloController {

    @RequestMapping("hello")
    public String  hello(ModelMap modelMap){
        return "hello";
    }
}

