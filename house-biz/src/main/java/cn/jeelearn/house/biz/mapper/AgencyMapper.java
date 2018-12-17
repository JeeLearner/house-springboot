package cn.jeelearn.house.biz.mapper;

import cn.jeelearn.house.common.model.Agency;
import cn.jeelearn.house.common.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/17
 * @Version:v1.0
 */
@Mapper
public interface AgencyMapper {

    List<Agency> select(Agency agency);
}

