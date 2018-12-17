package cn.jeelearn.house.biz.mapper;

import cn.jeelearn.house.common.model.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {

    /**
     * 查询某房屋的评论列表
     * @param houseId
     * @param size
     * @return
     */
    List<Comment> selectComments(@Param("houseId")long houseId, @Param("size")int size);
}
