package cn.avenchang.service;

import cn.avenchang.util.ResultMessage;

/**
 * Created by 53068 on 2018/3/13 0013.
 */
public interface EmailService {

    ResultMessage sendEmail(String sendTo, String title, String content);
}
