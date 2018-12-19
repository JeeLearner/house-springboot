package cn.jeelearn.house.biz.service;

import cn.jeelearn.house.biz.mapper.HouseMapper;
import cn.jeelearn.house.biz.service.base.FileService;
import cn.jeelearn.house.biz.service.base.MailService;
import cn.jeelearn.house.common.constants.HouseUserType;
import cn.jeelearn.house.common.model.*;
import cn.jeelearn.house.common.page.PageData;
import cn.jeelearn.house.common.page.PageParams;
import cn.jeelearn.house.common.utils.BeanHelper;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/16
 * @Version:v1.0
 */
@Service
public class HouseService {

    @Autowired
    private HouseMapper houseMapper;
    @Autowired
    private AgencyService agencyService;
    @Autowired
    private MailService mailService;
    @Autowired
    private FileService fileService;

    @Value("${file.prefix}")
    private String imgPrefix;

    /**
     * 1.查询小区
     * 2.添加图片服务器地址前缀
     * 3.构建分页结果
     * @auther: lyd
     * @date: 2018/12/16
     */
    public PageData<House> selectPageHouses(House query, PageParams pageParams){
        List<House> houses = Lists.newArrayList();
        if (StringUtils.isNotBlank(query.getName())){
            Community community = new Community();
            community.setName(query.getName());
            List<Community> communities = houseMapper.selectCommunity(community);
            if(!communities.isEmpty()){
                query.setCommunityId(communities.get(0).getId());
            }
        }
        houses = queryAndSetImg(query, pageParams);
        Long count = houseMapper.selectPageCount(query);
        return PageData.buildPage(houses, count, pageParams.getPageSize(), pageParams.getPageNum());
    }

    public List<House> queryAndSetImg(House query, PageParams pageParams){
        List<House> houses = houseMapper.selectPageHouses(query, pageParams);
        houses.forEach(h -> {
            h.setFirstImg(imgPrefix + h.getFirstImg());
            h.setImageList(h.getImageList().stream().map(img -> imgPrefix + img).collect(Collectors.toList()));
            h.setFloorPlanList(h.getFloorPlanList().stream().map(img -> imgPrefix + img).collect(Collectors.toList()));
        });
        return houses;
    }

    /**
     * 通过houseId查询房产
     * @param id
     * @return
     */
    public House selectOneHouse(long id){
        House house = new House();
        house.setId(id);
        List<House> houses = queryAndSetImg(house, PageParams.build(1, 1));
        if(!houses.isEmpty()){
            return houses.get(0);
        }
        return null;
    }

    public HouseUser getHouseUser(Long houseId){
        HouseUser houseUser =  houseMapper.selectSaleHouseUser(houseId);
        return houseUser;
    }

    /**
     * 留言
     * @param userMsg
     */
    public void addUserMsg(UserMsg userMsg) {
        BeanHelper.onInsert(userMsg);
        houseMapper.insertUserMsg(userMsg);
        User agent = agencyService.getAgentDetail(userMsg.getAgentId());
        mailService.sendMail("来自用户"+userMsg.getEmail()+"的留言", userMsg.getMsg(), agent.getEmail());
    }

    /**
     * 查询所有小区
     * @return
     */
	public List<Community> getAllCommunitys() {
		Community community = new Community();
		return houseMapper.selectCommunity(community);
	}

	/**添加房产
	 *   添加房屋图片
	 *   添加户型图片
	 *   插入房产信息
	 *   绑定用户到房产的关系
	 * @param house
	 * @param user
	 */
	public void addHouse(House house, User user) {
		if(CollectionUtils.isNotEmpty(house.getHouseFiles())){
			String images = Joiner.on(",").join(fileService.getImgPaths(house.getHouseFiles()));
			house.setImages(images);
		}
		if (CollectionUtils.isNotEmpty(house.getFloorPlanFiles())) {
			String images = Joiner.on(",").join(fileService.getImgPaths(house.getFloorPlanFiles()));
		    house.setFloorPlan(images);
		}
		BeanHelper.onInsert(house);
		houseMapper.insert(house);
		bindUser2House(house.getId(),user.getId(),false);
	}

	public void bindUser2House(Long houseId, Long userId, boolean collect) {
		HouseUser existHouseUser = houseMapper.selectHouseUser(userId, houseId, collect ? HouseUserType.BOOKMARK.value : HouseUserType.SALE.value);
		if(existHouseUser != null ) {
			return;
		}
		HouseUser houseUser = new HouseUser();
		houseUser.setHouseId(houseId);
		houseUser.setUserId(userId);
		houseUser.setType(collect ? HouseUserType.BOOKMARK.value : HouseUserType.SALE.value);
		BeanHelper.setDefaultProp(houseUser, HouseUser.class);
		BeanHelper.onInsert(houseUser);
		houseMapper.insertHouseUser(houseUser);
	}

	/**
	 * 删除房产
	 * 		出售的房产－－》下架
	 * 		收藏的房产－－》删除房产和用户的关系
	 * @param id
	 * @param userId
	 * @param type
	 */
	public void unbindUser2House(Long id, Long userId, Integer type) {
		  if (type.equals(HouseUserType.SALE.value)) {
		      houseMapper.downHouse(id);
		  }else {
		      houseMapper.deleteHouseUser(id, userId, type);
		  }
		    
	}

	public void updateRating(Long id, Double rating) {
		House house = selectOneHouse(id);
		Double oldRating = house.getRating();
		Double newRating  = oldRating.equals(0D)? rating : Math.min((oldRating+rating)/2, 5);
		House updateHouse = new House();
		updateHouse.setId(id);
		updateHouse.setRating(newRating);
		BeanHelper.onUpdate(updateHouse);
		houseMapper.updateHouse(updateHouse);
	}
}

