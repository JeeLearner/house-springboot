package cn.jeelearn.house.common.model;

/**
 * @Description: 小区
 * @Auther: lyd
 * @Date: 2018/12/16
 * @Version:v1.0
 */
public class Community {
    private Integer id;
    private String cityCode;
    private String cityName;
    private String name;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public String getName() {
        return name;
    }
}

