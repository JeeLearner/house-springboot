package cn.jeelearn.house.biz.service;

import cn.jeelearn.house.biz.mapper.UserMapper;
import cn.jeelearn.house.biz.service.base.FileService;
import cn.jeelearn.house.biz.service.base.MailService;
import cn.jeelearn.house.common.model.User;
import cn.jeelearn.house.common.utils.BeanHelper;
import cn.jeelearn.house.common.utils.HashUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/11
 * @Version:v1.0
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private MailService mailService;

    @Value("${file.prefix}")
    private String imgPrefix;

    public List<User> selectUsers(){
        return userMapper.selectUsers();
    }

    /**
     * 1.插入数据库，非激活;密码加盐md5;保存头像文件到本地
     * 2.生成key，绑定email
     * 3.发送邮件给用户
     *
     * @param account
     * @return
     */
    public boolean addAccount(User account){
        account.setPasswd(HashUtils.encryPassword(account.getPasswd()));
        //保存头像
        List<String> imgList = fileService.getImgPaths(Lists.newArrayList(account.getAvatarFile()));
        if(!imgList.isEmpty()){
            account.setAvatar(imgList.get(0));
        }
        BeanHelper.setDefaultProp(account, User.class);
        BeanHelper.onInsert(account);
        account.setEnable(0);
        userMapper.insert(account);
        //发送激活邮件 --异步
        mailService.registerNotify(account.getEmail());
        return true;
    }

    /**
     * 异步操作
     *  放到mailService执行，同时便于利用缓存
     * @auther: lyd
     * @date: 2018/12/13
     */
    public boolean enable(String key){
        return mailService.enable(key);
    }

    /**
     * 校验用户名密码
     * @auther: lyd
     * @date: 2018/12/14
     */
    public User auth(String username, String password){
        User user = new User();
        user.setEmail(username);
        user.setPasswd(HashUtils.encryPassword(password));
        user.setEnable(1);
        List<User> users = userMapper.selectUsersByQuery(user);
        if(users != null && !users.isEmpty()){
            return users.get(0);
        }
        return null;
    }
}

