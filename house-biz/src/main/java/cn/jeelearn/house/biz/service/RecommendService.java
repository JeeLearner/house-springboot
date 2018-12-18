package cn.jeelearn.house.biz.service;

import cn.jeelearn.house.common.model.House;
import cn.jeelearn.house.common.page.PageParams;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: 热门房产
 * @Auther: lyd
 * @Date: 2018/12/18
 * @Version:v1.0
 */
@Service
public class RecommendService {

    private static final String HOT_HOUSE_KEY = "hot_house";

    private static final Logger logger = LoggerFactory.getLogger(RecommendService.class);

    @Value("${spring.redis.host}")
    private String jedisHost;
    @Value("${spring.redis.password}")
    private String jedisAuth;


    @Autowired
    private HouseService houseService;

    public void increase(Long id){
        try {
            Jedis jedis = new Jedis(jedisHost);
            if (!jedisAuth.isEmpty()){
                jedis.auth(jedisAuth);
            }
            jedis.zincrby(HOT_HOUSE_KEY, 1.0D, id + "");
            // 0代表第一个元素,-1代表最后一个元素，保留热度由低到高末尾10个房产
            jedis.zremrangeByRank(HOT_HOUSE_KEY, 0, -11);
            jedis.close();
        } catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    public List<Long> getHot() {
        try {
            Jedis jedis = new Jedis(jedisHost);
            if (!jedisAuth.isEmpty()){
                jedis.auth(jedisAuth);
            }
            Set<String> idSet = jedis.zrevrange(HOT_HOUSE_KEY, 0, -1);
            jedis.close();
            List<Long> ids = idSet.stream().map(Long::parseLong).collect(Collectors.toList());
            return ids;
        }catch (Exception e) {
            logger.error(e.getMessage(), e);//有同学反应在未安装redis时会报500,在这里做下兼容,
            return Lists.newArrayList();
        }
    }

    public List<House> getHotHouse(Integer size){
        House query = new House();
        List<Long> list = getHot();
        list = list.subList(0, Math.min(list.size(), size));
        if (list.isEmpty()) {
            return Lists.newArrayList();
        }
        query.setIds(list);
        final List<Long> order = list;
        List<House> houses = houseService.queryAndSetImg(query, PageParams.build(size, 1));
        Ordering<House> houseSort = Ordering.natural().onResultOf(hs -> {
            return order.indexOf(hs.getId());
        });
        return houseSort.sortedCopy(houses);
    }
}

