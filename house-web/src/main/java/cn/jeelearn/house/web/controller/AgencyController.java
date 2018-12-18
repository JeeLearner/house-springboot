package cn.jeelearn.house.web.controller;

import cn.jeelearn.house.biz.service.AgencyService;
import cn.jeelearn.house.biz.service.HouseService;
import cn.jeelearn.house.biz.service.RecommendService;
import cn.jeelearn.house.biz.service.base.MailService;
import cn.jeelearn.house.common.constants.CommonConstants;
import cn.jeelearn.house.common.model.House;
import cn.jeelearn.house.common.model.User;
import cn.jeelearn.house.common.page.PageData;
import cn.jeelearn.house.common.page.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/18
 * @Version:v1.0
 */
@Controller
@RequestMapping("/agency")
public class AgencyController {

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private MailService mailService;

    @Autowired
    private RecommendService recommendService;

    /**
     * 经纪人列表
     * @auther: lyd
     * @date: 2018/12/18
     */
    @RequestMapping("/agentList")
    public String agentList(Integer pageSize, Integer pageNum, ModelMap modelMap){
        if (pageSize == null) {
            pageSize = 6;
        }
        //热门房产
        List<House> houses =  recommendService.getHotHouse(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses", houses);
        PageData<User> ps = agencyService.selectAllAgent(PageParams.build(pageSize, pageNum));
        modelMap.put("ps", ps);
        return "/user/agent/agentList";
    }

    /**
     *  经纪人详情页
     * @auther: lyd
     * @date: 2018/12/18
     */
    @RequestMapping("/agentDetail")
    public String agentDetail(Long id,ModelMap modelMap){
        //查经纪人及机构
        User agent =  agencyService.getAgentDetail(id);
        //查所关联房产
        House query = new House();
        query.setUserId(id);
        query.setBookmarked(false);
        PageData<House> bindHouse = houseService.selectPageHouses(query, new PageParams(3,1));
        if (bindHouse != null) {
            modelMap.put("bindHouses", bindHouse.getList()) ;
        }
        //查热门房产
        List<House> houses =  recommendService.getHotHouse(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses", houses);

        modelMap.put("agent", agent);
        modelMap.put("agencyName", agent.getAgencyName());
        return "/user/agent/agentDetail";
    }
}

