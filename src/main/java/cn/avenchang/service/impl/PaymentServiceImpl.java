package cn.avenchang.service.impl;

import cn.avenchang.dao.PaymentDao;
import cn.avenchang.model.ResultMessage;
import cn.avenchang.service.PaymentService;
import cn.avenchang.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 53068 on 2018/4/2 0002.
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentDao paymentDao;

    @Override
    public ResultMessage<Boolean> pay(Long cardId, String password, Double amount) {
        String encrypt = MD5.encrypt(password);
        if(paymentDao.pay(cardId, encrypt, amount) > 0){
            return new ResultMessage<Boolean>(ResultMessage.OK, true);
        }else {
            return new ResultMessage<Boolean>(ResultMessage.FAIL, "余额不足");
        }
    }
}
