package cn.jeelearn.house.web.controller;

import cn.jeelearn.house.biz.service.HouseService;
import cn.jeelearn.house.common.model.House;
import cn.jeelearn.house.common.page.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Description: 首页Controller
 * @Auther: lyd
 * @Date: 2018/12/13
 * @Version:v1.0
 */
@Controller
public class IndexController {

    @Autowired
    private HouseService houseService;

    @RequestMapping("")
    public String index(ModelMap modelMap){
        return "redirect:/index";
    }

    /**
     * 1.首页展示
     * 2.新上房源
     * @auther: lyd
     * @date: 2018/12/18
     */
    @RequestMapping("index")
    public String accountsRegister(ModelMap modelMap){
        House house = new House();
        house.setSort("create_time");
        List<House> houses = houseService.queryAndSetImg(house, PageParams.build(8, 1));
        modelMap.put("recomHouses", houses);
        return "/homepage/index";
    }
}

