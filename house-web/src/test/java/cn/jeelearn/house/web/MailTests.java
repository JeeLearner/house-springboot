package cn.jeelearn.house.web;

import cn.jeelearn.house.biz.service.base.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/14
 * @Version:v1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MailTests {

    @Autowired
    private MailSender mailSender;
    @Autowired
    private MailService mailService;

    @Test
    public void sendMail(){
        mailService.sendMail("房产平台激活邮件", "内容", "512013674@qq.com");
    }
}

