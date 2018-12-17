package cn.jeelearn.house.biz.service;

import cn.jeelearn.house.biz.mapper.CommentMapper;
import cn.jeelearn.house.common.model.Comment;
import cn.jeelearn.house.common.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 评论服务
 * @Auther: lyd
 * @Date: 2018/12/17
 * @Version:v1.0
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserService userService;
    
    /**
     * 查询某房屋的评论列表
     * @param houseId
     * @param size
     * @return
     */
    public List<Comment> selectComments(long houseId, int size){
        List<Comment> comments = commentMapper.selectComments(houseId, size);
        comments.forEach(c -> {
            User user = userService.getUserById(c.getUserId());
            System.out.println(user);
            c.setAvatar(user.getAvatar());
            c.setUserName(user.getName());
        });
        return comments;
    }
}

