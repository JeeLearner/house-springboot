package cn.jeelearn.house.biz.mapper;

import cn.jeelearn.house.common.model.Community;
import cn.jeelearn.house.common.model.House;
import cn.jeelearn.house.common.model.HouseUser;
import cn.jeelearn.house.common.model.UserMsg;
import cn.jeelearn.house.common.page.PageParams;
import com.google.common.collect.Lists;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HouseMapper {

    List<House> selectPageHouses(@Param("house") House house, @Param("pageParams") PageParams params);

    Long selectPageCount(@Param("house") House query);

    List<Community> selectCommunity(Community community);

    public HouseUser selectSaleHouseUser(@Param("houseId") Long houseId);

    int insertUserMsg(UserMsg userMsg);

    int insert(House house);

	public HouseUser selectHouseUser(@Param("userId")Long userId,@Param("id") Long houseId,@Param("type") Integer integer);

	int insertHouseUser(HouseUser houseUser);

	int downHouse(Long id);

	int deleteHouseUser(@Param("id")Long id,@Param("userId") Long userId,@Param("type") Integer value);

	int updateHouse(House updateHouse);
	
	
}
