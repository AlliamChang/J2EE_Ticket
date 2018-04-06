package cn.avenchang.service.impl;

import cn.avenchang.dao.*;
import cn.avenchang.domain.*;
import cn.avenchang.model.*;
import cn.avenchang.util.PointsUtil;
import cn.avenchang.util.TicketSynchronizer;
import cn.avenchang.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.avenchang.service.UserManageService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by 53068 on 2018/3/7 0007.
 */
@Service
public class UserManageServiceImpl implements UserManageService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private SeatDao seatDao;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private TicketDao ticketDao;
    @Autowired
    private PlanDao planDao;
    @Autowired
    private EarningDao earningDao;
    @Autowired
    private TicketSynchronizer ticketSynchronizer;

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

        return new ResultMessage<Boolean>(ResultMessage.FAIL, false, "签名过期，重新登录");
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
        return ticketSynchronizer.buyTicketBySelect(selectedSeat, orders);

        //        boolean isAvailable = true;
//        //检测座位是否都能选
//        for (int i = 0; i < selectedSeat.size(); i++) {
//            SeatState seatState = selectedSeat.get(i);
//            if (seatState.getRow() == 0) {
//                selectedSeat.remove(i);
//                continue;
//            }
//            seatState.setPlanId(orders.getPlanId());
//            if (seatDao.isAvailable(seatState) <= 0) {
//                isAvailable = false;
//                break;
//            }
//        }
//
//        if (isAvailable) {
//            //为每个座位创建票根
//            Long venueId = planDao.getVenueId(orders.getPlanId());
//            orders.setVenueId(venueId);
//            List<Ticket> tickets = new ArrayList<>();
//            Date now = new Date();
//            selectedSeat.forEach(seatState -> {
//                Ticket ticket = new Ticket();
//                ticket.setTime(now);
//                ticket.setVenueId(venueId);
//                ticket.setUserId(orders.getUserId());
//                ticket.setArea(seatState.getArea());
//                ticket.setRow(seatState.getRow());
//                ticket.setCol(seatState.getCol());
//                ticket.setPlanId(orders.getPlanId());
//                ticket.setOnline(true);
//                tickets.add(ticket);
//                seatDao.bookSeat(seatState);
//            });
//            //完善订单内容
//            orders.setTime(now);
//
//            //得到会员价
//            double discount = PointsUtil.getDiscount(userDao.getTotalPoints(orders.getUserId()));
//            orders.setActualPrice(orders.getOriginalPrice() * discount);
//            ordersDao.newOrders(orders);
//            if (orders.getId() > 0) {
//                ticketDao.insertTickets(orders.getId(), tickets);
//                new Thread(
//                        new OrderPayCountdown(orders.getId())
//                ).start();
//                return new ResultMessage<Long>(ResultMessage.OK, orders.getId());
//            }
//        }else {
//            return new ResultMessage<Long>(ResultMessage.FAIL, "所选座位已被预订");
//        }
//
//        return new ResultMessage<Long>(ResultMessage.UNKNOWN, "订单生成失败");
    }

    @Override
    public List<OrderInfo> myOrders(Long id) {
        List<OrderInfo> orderInfo = ordersDao.getOrders(id);
        Long now = new Date().getTime();
        for (int i = 0; i < orderInfo.size(); i++) {
            OrderInfo info = orderInfo.get(i);
            orderInfo.get(i).setSeatInfo(ticketDao.getSeatInfo(info.getId()));
            if (info.getState() == 0 && (300 - (now - info.getTime().getTime()) / 1000) > 1) {
                orderInfo.get(i).setDistance(300 - (now - info.getTime().getTime()) / 1000);
            } else if (info.getState() == 1) {

            }
        }
        return orderInfo;
    }

    @Override
    public ResultMessage<OrderInfo> unpaidOrder(Long orderId) {
        OrderInfo orderInfo = ordersDao.getUnpaidOrderDetail(orderId);
        if (orderInfo != null) {
            if(orderInfo.getTicketNum() > 0){

            }else {
                orderInfo.setSeatInfo(ticketDao.getSeatInfo(orderId));
            }
            return new ResultMessage<OrderInfo>(ResultMessage.OK, orderInfo);
        } else {
            return new ResultMessage<OrderInfo>(ResultMessage.FAIL, "该订单已过期");
        }
    }

    @Override
    public ResultMessage<Long> buyTicketWithoutSelect(Orders orders) {
        return ticketSynchronizer.buyTicketWithoutSelect(orders);
    }

    @Override
    public List<SeatInfo> getSeatAreaInfo(Long planId) {
        return seatDao.getAreaSeatInfo(planId);
    }

    @Override
    public Double getRefundPercent(Long orderId) {
        Date planTime = planDao.getPlanTime(orderId);
        Date now = new Date();
        Double refundPercent = TimeUtil.getRefundPercent(now, planTime);
        return refundPercent;
    }

    @Override
    public ResultMessage<Boolean> refundOrder(Long orderId) {
        Double refundPercent = getRefundPercent(orderId);
        if (ordersDao.refund(orderId) > 0) {
            ordersDao.refundMoney(orderId, refundPercent);
            return new ResultMessage<Boolean>(ResultMessage.OK, true);
        }else {
            return new ResultMessage<Boolean>(ResultMessage.FAIL, "该订单已超过退订时限");
        }
    }

    @Override
    public boolean isEnoughToProfit(Long id) {
        return userDao.getRestPoints(id) >= Profit.getCostPoints();
    }

    @Override
    public ResultMessage<String> paid(Long orderId, Long accountId, boolean useProfit) {
        int result = 0;
        if (useProfit) {
            result = ordersDao.paidByUseProfit(orderId, accountId);
        } else {
            result = ordersDao.paid(orderId, accountId);
        }
        if (result > 0) {
            earningDao.income(orderId);
            return new ResultMessage<String>(ResultMessage.OK, "", "");
        } else {
            return new ResultMessage<String>(ResultMessage.FAIL, "", "支付失败");
        }
    }


}
