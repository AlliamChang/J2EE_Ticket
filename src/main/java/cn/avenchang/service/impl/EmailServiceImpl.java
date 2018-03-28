package cn.avenchang.service.impl;

import cn.avenchang.config.EmailConfig;
import cn.avenchang.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Created by 53068 on 2018/3/13 0013.
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailConfig emailConfig;
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public Boolean sendEmail(String sendTo, String title, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailConfig.getEmailFrom());
        message.setTo(sendTo);
        message.setSubject(title);
        message.setText(content);
        mailSender.send(message);
        return true;
    }
}
