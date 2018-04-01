package cn.avenchang.service;

import cn.avenchang.domain.Orders;
import cn.avenchang.domain.SeatState;
import cn.avenchang.domain.User;
import cn.avenchang.model.OrderInfo;
import cn.avenchang.model.ResultMessage;
import cn.avenchang.model.UserInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by 53068 on 2018/3/7 0007.
 */
public interface UserManageService {

    ResultMessage<Boolean> updateInfo(UserInfo userInfo, String token);

    ResultMessage<UserInfo> getInfo(Long id);

    List<OrderInfo> myOrders(Long id);

    ResultMessage<OrderInfo> unpaidOrder(Long orderId);

    /**
     * 返回order id
     * @param selectedSeat
     * @return
     */
    ResultMessage<Long> buyTicketBySelect(List<SeatState> selectedSeat, Orders orders);

    /**
     * 返回order id
     * @param orders
     * @return
     */
    ResultMessage<Long> buyTicketWithoutSelect(Orders orders);

    ResultMessage<Boolean> refundOrder(Long orderId);

    boolean isEnoughToProfit(Long id);

    ResultMessage<String> paid(Long orderId, Long accountId, boolean useProfit);

}
