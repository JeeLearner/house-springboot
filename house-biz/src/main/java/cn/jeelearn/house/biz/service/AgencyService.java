package cn.jeelearn.house.biz.service;

import cn.jeelearn.house.biz.mapper.AgencyMapper;
import cn.jeelearn.house.common.model.Agency;
import cn.jeelearn.house.common.model.User;
import cn.jeelearn.house.common.page.PageData;
import cn.jeelearn.house.common.page.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 组织机构
 * @Auther: lyd
 * @Date: 2018/12/17
 * @Version:v1.0
 */
@Service
public class AgencyService {

    @Autowired
    private AgencyMapper agencyMapper;
    @Autowired
    private UserService userService;

    @Value("${file.prefix}")
    private String imgPrefix;


    public PageData<User> selectAllAgent(PageParams params) {
        List<User> agents = agencyMapper.selectAgent(new User(), params);
        setImg(agents);
        long count = agencyMapper.selectAgentCount(new User());
        return PageData.buildPage(agents, count, params.getPageSize(), params.getPageNum());
    }

    /**
     * 访问user表获取详情 添加用户头像
     * @auther: lyd
     * @date: 2018/12/17
     */
    public User getAgentDetail(Long userId) {
        User user = new User();
        user.setType(2);
        user.setId(userId);
        //List<User> users = userService.selectUsersByQuery(user);
        List<User> list = agencyMapper.selectAgent(user, PageParams.build(1, 1));
        setImg(list);
        if (!list.isEmpty()){
            //经纪人
            User agent = list.get(0);
            //经纪机构
            Agency agency = new Agency();
            agency.setId(agent.getAgencyId().intValue());
            List<Agency> agencies = agencyMapper.select(agency);
            if (!agencies.isEmpty()){
                agent.setAgencyName(agencies.get(0).getName());
            }
            return agent;
        }
        return null;
    }

    private void setImg(List<User> list) {
        list.forEach(i -> {
            i.setAvatar(imgPrefix + i.getAvatar());
        });

    }

}

