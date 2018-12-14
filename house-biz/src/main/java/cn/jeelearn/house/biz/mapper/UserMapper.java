package cn.jeelearn.house.biz.mapper;

import cn.jeelearn.house.common.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/11
 * @Version:v1.0
 */
@Mapper
public interface UserMapper {
    List<User> selectUsers();

    int insert(User account);
    int update(User account);
    int delete(String email);
    List<User> selectUsersByQuery(User user);

}

