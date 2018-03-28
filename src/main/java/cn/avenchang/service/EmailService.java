package cn.avenchang.service;

/**
 * Created by 53068 on 2018/3/13 0013.
 */
public interface EmailService {

    Boolean sendEmail(String sendTo, String title, String content);
}
