package cn.jeelearn.house.biz.service.base;

import cn.jeelearn.house.biz.mapper.UserMapper;
import cn.jeelearn.house.common.model.User;
import com.google.common.base.Objects;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/12
 * @Version:v1.0
 */
@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${domain.name}")
    private String domainName;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FileService fileService;


    private final Cache<String, String> registerCache = CacheBuilder.newBuilder()
            .maximumSize(100).expireAfterAccess(15, TimeUnit.MINUTES)
            .removalListener(new RemovalListener<String, String>() {
                @Override
                public void onRemoval(RemovalNotification<String, String> notification) {
                    String email = notification.getValue();
                    User user = new User();
                    user.setEmail(email);
                    List<User> targetUser = userMapper.selectUsersByQuery(user);
                    //在删除前首先判断用户是否已经被激活，对于未激活的用户进行移除操作
                    if (!targetUser.isEmpty() && Objects.equal(targetUser.get(0).getEnable(), 0)){
                        //将上传的头像删除
                        String avatar = targetUser.get(0).getAvatar();
                        if (avatar!=null){
                            fileService.remove(avatar);
                        }
                        userMapper.delete(email);

                    }
                }
            }).build();
    private final Cache<String, String> resetCache = CacheBuilder.newBuilder()
            .maximumSize(100).expireAfterAccess(15, TimeUnit.MINUTES).build();

    @Async
    public void sendMail(String title, String content, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setSubject(title);
        message.setTo(email);
        message.setText(content);
        message.setSentDate(new Date());
        mailSender.send(message);
    }

    /**
     * 1.缓存key-email的关系
     * 2.借助spring mail 发送邮件
     * 3.借助异步框架进行异步操作
     * @param email
     */
    @Async
    public void registerNotify(String email) {
        String randomKey = RandomStringUtils.randomAlphabetic(10);
        registerCache.put(randomKey, email);
        String url = "http://" + domainName + "/accounts/verify?key=" + randomKey;
        sendMail("房产平台激活邮件", url, email);
    }

    /**
     * 激活注册用户
     * @auther: lyd
     * @date: 2018/12/13
     */
    public boolean enable(String key){
        String email = registerCache.getIfPresent(key);
        if(StringUtils.isBlank(email)){
            return false;
        }
        User updateUser = new User();
        updateUser.setEmail(email);
        updateUser.setEnable(1);
        userMapper.update(updateUser);
        registerCache.invalidate(key);
        return true;
    }
}

