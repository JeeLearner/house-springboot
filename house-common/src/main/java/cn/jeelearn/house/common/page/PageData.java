package cn.jeelearn.house.common.page;

import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/16
 * @Version:v1.0
 */
public class PageData<T> {
    private List<T> list;
    private Pagination pagination;

    public PageData(Pagination pagination, List<T> list){
        this.pagination = pagination;
        this.list = list;
    }

    public static <T> PageData<T> buildPage(List<T> list,Long count,Integer pageSize,Integer pageNum){
        Pagination _pagination = new Pagination(pageSize, pageNum, count);
        return new PageData<>(_pagination, list);

    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}

