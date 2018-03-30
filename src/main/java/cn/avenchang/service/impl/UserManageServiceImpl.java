package cn.avenchang.service.impl;

import cn.avenchang.dao.OrdersDao;
import cn.avenchang.dao.SeatDao;
import cn.avenchang.dao.TicketDao;
import cn.avenchang.dao.UserDao;
import cn.avenchang.domain.Orders;
import cn.avenchang.domain.SeatState;
import cn.avenchang.domain.Ticket;
import cn.avenchang.domain.User;
import cn.avenchang.model.Profit;
import cn.avenchang.model.ResultMessage;
import cn.avenchang.model.UserInfo;
import cn.avenchang.util.PointsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.avenchang.service.UserManageService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by 53068 on 2018/3/7 0007.
 */
@Service
public class UserManageServiceImpl implements UserManageService{

    @Autowired
    private UserDao userDao;
    @Autowired
    private SeatDao seatDao;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private TicketDao ticketDao;

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public ResultMessage<Boolean> updateInfo(UserInfo userInfo, String token) {
        final User user = new User();
        user.setEmail(userInfo.getEmail());
        user.setRealName(userInfo.getRealName());
        user.setUsername(userInfo.getUsername());
        user.setToken(token);
        if (userDao.updateInfo(user) > 0) {
            return new ResultMessage<Boolean>(ResultMessage.OK, true);
        }

        return new ResultMessage<Boolean>(ResultMessage.FAIL, false,"签名过期，重新登录");
    }

    @Override
    public ResultMessage<UserInfo> getInfo(final Long id) {
        final User user = userDao.findUserByID(id);
        if (user == null) {
            logger.log(Level.WARNING, "未找到用户信息");
            return new ResultMessage<UserInfo>(ResultMessage.FAIL, "未找到用户信息");
        }
        final UserInfo userInfo = new UserInfo();
        userInfo.setEmail(user.getEmail());
        userInfo.setRealName(user.getRealName());
        userInfo.setUsername(user.getUsername());
        userInfo.setRestPoints(user.getRestPoints());
        userInfo.setRegistTime(user.getRegistTime());
        userInfo.setLevel(PointsUtil.getLevel(user.getTotalPoints()));
        return new ResultMessage<UserInfo>(ResultMessage.OK, userInfo);
    }

    @Override
    public synchronized ResultMessage<Long> buyTicketBySelect(List<SeatState> selectedSeat, Orders orders) {
        boolean isAvailable = true;
        //检测座位是否都能选
        for (SeatState seatState : selectedSeat) {
            if (seatDao.isAvailable(seatState) <= 0) {
                isAvailable = false;
                break;
            }
        }
        if (isAvailable) {
            //为每个座位创建票根
            List<Ticket> tickets = new ArrayList<>();
            Date now = new Date();
            selectedSeat.forEach(seatState -> {
                Ticket ticket = new Ticket();
                ticket.setTime(now);
                ticket.setVenueId(orders.getVenueId());
                ticket.setUserId(orders.getUserId());
                ticket.setArea(seatState.getArea());
                ticket.setRow(seatState.getRow());
                ticket.setCol(seatState.getCol());
                ticket.setPlanId(orders.getPlanId());
                tickets.add(ticket);
                seatDao.bookSeat(seatState);
            });
            //完善订单内容
            orders.setTime(now);

            double discount = PointsUtil.getDiscount(userDao.getRestPoints(orders.getUserId()));
            orders.setActualPrice(orders.getOriginalPrice() * discount);
            ordersDao.newOrders(orders);
            if (orders.getId() > 0) {
                ticketDao.insertTickets(orders.getId(), tickets);
                new Thread(
                        new OrderPayCountdown(orders.getId())
                ).start();
                return new ResultMessage<Long>(ResultMessage.OK, orders.getId());
            }
        }else {
            return new ResultMessage<Long>(ResultMessage.FAIL, "所选座位已被预订");
        }

        return new ResultMessage<Long>(ResultMessage.UNKNOWN, "订单生成失败");
    }

    @Override
    public List<Orders> myOrders(Long id) {
        return ordersDao.getOrders(id);
    }

    @Override
    public ResultMessage<Long> buyTicketWithoutSelect(Orders orders) {
        return null;
    }

    @Override
    public ResultMessage<Boolean> refundOrder(Long orderId) {
        return null;
    }

    @Override
    public boolean isEnoughToProfit(Long id) {
        return userDao.getRestPoints(id) >= Profit.getCostPoints();
    }

    @Override
    public ResultMessage<Boolean> userProfits(Long orderId) {
        return null;
    }

    class OrderPayCountdown implements Runnable{

        private final Long orderId;

        public OrderPayCountdown(Long orderId) {
            this.orderId = orderId;
        }

        @Override
        public void run() {
            try {
                // 5分钟内不支付则取消订单
                Thread.sleep(5 * 60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ordersDao.overdue(orderId);
        }
    }
}
