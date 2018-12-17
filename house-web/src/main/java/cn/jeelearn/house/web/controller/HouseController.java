package cn.jeelearn.house.web.controller;

import cn.jeelearn.house.biz.service.AgencyService;
import cn.jeelearn.house.biz.service.CommentService;
import cn.jeelearn.house.biz.service.HouseService;
import cn.jeelearn.house.common.model.Comment;
import cn.jeelearn.house.common.model.House;
import cn.jeelearn.house.common.model.HouseUser;
import cn.jeelearn.house.common.page.PageData;
import cn.jeelearn.house.common.page.PageParams;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterators;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/16
 * @Version:v1.0
 */
@Controller
@RequestMapping("/house")
public class HouseController {

    @Autowired
    private HouseService houseService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private AgencyService agencyService;

    /**
     * 1.实现分页
     * 2.支持小区搜索、类型搜索
     * 3.支持排序
     * 4.支持展示图片、价格、标题、地址等信息
     * @auther: lyd
     * @date: 2018/12/16
     */
    @RequestMapping("/list")
    public String list(Integer pageSize, Integer pageNum, House query, ModelMap map){
        //房源列表
        PageData<House> housePageData = houseService.selectPageHouses(query, PageParams.build(pageSize, pageNum));

        map.put("ps", housePageData);
        map.put("vo", query);
        return "house/listing";
    }

    /**
     * 查看房屋详情
     * @auther: lyd
     * @date: 2018/12/17
     */
    @RequestMapping("detail")
    public String detail(long id, ModelMap map){
        //房屋详情
        House house = houseService.selectOneHouse(id);
        //评论列表
        List<Comment> comments = commentService.selectComments(id, 8);
        //房屋关联的经纪人及机构信息
        HouseUser houseUser = houseService.getHouseUser(id);
        if (houseUser.getUserId() != null && !houseUser.getUserId().equals(0)){
            map.put("agent", agencyService.getAgentDetail(houseUser.getUserId()));
        }
        map.put("house", house);
        map.put("commentList", comments);
        return "/house/detail";
    }
    

}

