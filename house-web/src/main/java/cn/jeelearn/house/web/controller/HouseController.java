package cn.jeelearn.house.web.controller;

import cn.jeelearn.house.biz.service.AgencyService;
import cn.jeelearn.house.biz.service.CityService;
import cn.jeelearn.house.biz.service.CommentService;
import cn.jeelearn.house.biz.service.HouseService;
import cn.jeelearn.house.biz.service.RecommendService;
import cn.jeelearn.house.common.constants.CommonConstants;
import cn.jeelearn.house.common.constants.HouseUserType;
import cn.jeelearn.house.common.model.Comment;
import cn.jeelearn.house.common.model.House;
import cn.jeelearn.house.common.model.HouseUser;
import cn.jeelearn.house.common.model.User;
import cn.jeelearn.house.common.model.UserMsg;
import cn.jeelearn.house.common.page.PageData;
import cn.jeelearn.house.common.page.PageParams;
import cn.jeelearn.house.common.result.ResultMsg;
import cn.jeelearn.house.web.interceptor.UserContext;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @Autowired
    private RecommendService recommendService;
    @Autowired
    private CityService cityService;

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
        //热门房源
        List<House> hotHouses =  recommendService.getHotHouse(CommonConstants.RECOM_SIZE);
        map.put("recomHouses", hotHouses);
        map.put("ps", housePageData);
        map.put("vo", query);
        return "house/listing";
    }

    /**
     * 查看房屋详情
     * @auther: lyd
     * @date: 2018/12/17
     */
    @RequestMapping("/detail")
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
        //热门房产
        List<House> rcHouses =  recommendService.getHotHouse(CommonConstants.RECOM_SIZE);
        map.put("recomHouses", rcHouses);
        //*****点击一次加1
        recommendService.increase(id);
        map.put("house", house);
        map.put("commentList", comments);
        return "/house/detail";
    }

    /**
     * 留言
     * @auther: lyd
     * @date: 2018/12/18
     */
    @RequestMapping("/leaveMsg")
    public String houseMsg(UserMsg userMsg){
        houseService.addUserMsg(userMsg);
        return "redirect:/house/detail?id=" + userMsg.getHouseId() + "&" + ResultMsg.successMsg("留言成功").asUrlParams();
    }
    
    @RequestMapping("/toAdd")
	public String toAdd(ModelMap modelMap) {
		modelMap.put("citys", cityService.getAllCitys());
		modelMap.put("communitys", houseService.getAllCommunitys());
		return "/house/add";
	}
    
    /**
     * 添加房产
     * @param house
     * @return
     */
    @RequestMapping("/add")
	public String doAdd(House house){
		User user = UserContext.getUser();
		house.setState(CommonConstants.HOUSE_STATE_UP);
		houseService.addHouse(house,user);
		return "redirect:/house/ownlist";
	}
    
    /**
     * 展示个人房产列表
     * @param house
     * @param pageNum
     * @param pageSize
     * @param modelMap
     * @return
     */
	@RequestMapping("/ownlist")
	public String ownlist(House house,Integer pageNum,Integer pageSize,ModelMap modelMap){
		User user = UserContext.getUser();
		house.setUserId(user.getId());
		house.setBookmarked(false);
		modelMap.put("ps", houseService.selectPageHouses(house, PageParams.build(pageSize, pageNum)));
		//pageType=own表示我的房产列表，另外还有房产收藏
		modelMap.put("pageType", "own");
		return "/house/ownlist";
	}
	
	/**
	 * 删除房产
	 * @param id
	 * @param pageType
	 * @return
	 */
	@RequestMapping(value="/del")
	public String delsale(Long id,String pageType){
	   User user = UserContext.getUser();
	   houseService.unbindUser2House(id,user.getId(),pageType.equals("own")?HouseUserType.SALE.value:HouseUserType.BOOKMARK.value);
	   return "redirect:/house/ownlist";
	}
	
	/**
	 * 评分
	 * @param rating
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/rating")
	public ResultMsg houseRate(Double rating,Long id){
		houseService.updateRating(id,rating);
		return ResultMsg.successMsg("ok");
	}
	
	/**
	 * 收藏
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/bookmark")
	public ResultMsg bookmark(Long id){
	  User user =	UserContext.getUser();
	  houseService.bindUser2House(id, user.getId(), true);
	  return ResultMsg.successMsg("ok");
	}
	
	/**
	 * 取消收藏
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/unbookmark")
	public ResultMsg unbookmark(Long id){
	  User user =	UserContext.getUser();
	  houseService.unbindUser2House(id,user.getId(),HouseUserType.BOOKMARK.value);
	  return ResultMsg.successMsg("ok");
	}
	
	/**
	 * 收藏列表
	 * @param id
	 * @return
	 */
	@RequestMapping("/bookmarked")
	public String bookmarked(House house,Integer pageNum,Integer pageSize,ModelMap modelMap){
		User user = UserContext.getUser();
		house.setBookmarked(true);
		house.setUserId(user.getId());
		modelMap.put("ps", houseService.selectPageHouses(house, PageParams.build(pageSize, pageNum)));
		modelMap.put("pageType", "book");
		return "/house/ownlist";
	}

}

