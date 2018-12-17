package cn.jeelearn.house.biz.service;

import cn.jeelearn.house.biz.mapper.AgencyMapper;
import cn.jeelearn.house.common.model.Agency;
import cn.jeelearn.house.common.model.User;
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

    /**
     * 访问user表获取详情 添加用户头像
     * @auther: lyd
     * @date: 2018/12/17
     */
    public User getAgentDetail(Long userId) {
        User user = new User();
        user.setType(2);
        user.setId(userId);
        List<User> users = userService.selectUsersByQuery(user);
        if (!users.isEmpty()){
            //经纪人
            User agent = users.get(0);
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
}

