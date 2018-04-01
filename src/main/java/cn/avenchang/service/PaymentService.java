package cn.avenchang.service;

/**
 * Created by 53068 on 2018/3/14 0014.
 */

import cn.avenchang.model.ResultMessage;

/**
 * 模拟第三方支付
 */
public interface PaymentService {

    ResultMessage<Boolean> pay(Long cardId, String password, Double amount);

}
