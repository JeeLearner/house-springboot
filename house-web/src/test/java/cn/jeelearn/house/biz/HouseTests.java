package cn.jeelearn.house.biz;

import cn.jeelearn.house.biz.mapper.HouseMapper;
import cn.jeelearn.house.common.model.House;
import cn.jeelearn.house.common.page.PageParams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/17
 * @Version:v1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HouseTests {

    @Autowired
    private HouseMapper houseMapper;

    @Test
    public void testPageHouses(){
        House house = new House();
        PageParams pageParams = new PageParams();
        List<House> houses = houseMapper.selectPageHouses(house, pageParams);
        System.out.println(houses.stream().findFirst());
    }


}

