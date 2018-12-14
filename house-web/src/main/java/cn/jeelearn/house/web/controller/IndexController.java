package cn.jeelearn.house.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description: 首页Controller
 * @Auther: lyd
 * @Date: 2018/12/13
 * @Version:v1.0
 */
@Controller
public class IndexController {

    @RequestMapping("")
    public String index(ModelMap modelMap){
        return "redirect:/index";
    }

    @RequestMapping("index")
    public String accountsRegister(ModelMap modelMap){

        return "/homepage/index";
    }
}

