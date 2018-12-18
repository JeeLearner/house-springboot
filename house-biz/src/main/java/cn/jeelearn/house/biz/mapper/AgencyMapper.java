package cn.jeelearn.house.biz.mapper;

import cn.jeelearn.house.common.model.Agency;
import cn.jeelearn.house.common.model.User;
import cn.jeelearn.house.common.page.PageParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/17
 * @Version:v1.0
 */
@Mapper
public interface AgencyMapper {

    List<User>	selectAgent(@Param("user") User user, @Param("pageParams") PageParams pageParams);

    List<Agency> select(Agency agency);

    long selectAgentCount(@Param("user") User user);
}

