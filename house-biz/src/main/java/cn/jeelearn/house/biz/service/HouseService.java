package cn.jeelearn.house.biz.service;

import cn.jeelearn.house.biz.mapper.HouseMapper;
import cn.jeelearn.house.common.model.Community;
import cn.jeelearn.house.common.model.House;
import cn.jeelearn.house.common.model.HouseUser;
import cn.jeelearn.house.common.page.PageData;
import cn.jeelearn.house.common.page.PageParams;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
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

    private List<House> queryAndSetImg(House query, PageParams pageParams){
        List<House> houses = houseMapper.selectPageHouses(query, pageParams);
        houses.forEach(h -> {
            h.setFirstImg(imgPrefix + h.getFirstImg());
            h.setImageList(h.getImageList().stream().map(img -> imgPrefix + img).collect(Collectors.toList()));
            h.setFloorPlanList(h.getFloorPlanList().stream().map(img -> imgPrefix + img).collect(Collectors.toList()));
        });
        return houses;
    }

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

}

