package cn.avenchang.util;

import cn.avenchang.dao.*;
import cn.avenchang.domain.Orders;
import cn.avenchang.domain.PlanPrice;
import cn.avenchang.domain.SeatState;
import cn.avenchang.domain.Ticket;
import cn.avenchang.model.ResultMessage;
import cn.avenchang.model.SeatInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 53068 on 2018/4/6 0006.
 */
@Component
public class TicketSynchronizer {

    @Autowired
    private SeatDao seatDao;
    @Autowired
    private PlanDao planDao;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private TicketDao ticketDao;

//    public boolean buyTicketWithoutSelect(Orders orders) {
//        synchronized (this) {
//            List<SeatState> selectedSeat = seatDao.getAvailableSeat(orders.getPlanId(), "area",orders.getTicketNum());
//            if (selectedSeat.size() == orders.getTicketNum()) {
//
//            }else {
//
//            }
//        }
//    }

    public ResultMessage<Long> buyTicketBySelect(List<SeatState> selectedSeat, Orders orders) {
        synchronized (this) {
            boolean isAvailable = true;
            //检测座位是否都能选
            for (int i = 0; i < selectedSeat.size(); i++) {
                SeatState seatState = selectedSeat.get(i);
                if (seatState.getRow() == 0) {
                    selectedSeat.remove(i);
                    continue;
                }
                seatState.setPlanId(orders.getPlanId());
                if (seatDao.isAvailable(seatState) <= 0) {
                    isAvailable = false;
                    break;
                }
            }

            if (isAvailable) {
                //为每个座位创建票根
                Long venueId = planDao.getVenueId(orders.getPlanId());
                orders.setVenueId(venueId);
                List<Ticket> tickets = new ArrayList<>();
                Date now = new Date();
                selectedSeat.forEach(seatState -> {
                    Ticket ticket = new Ticket();
                    ticket.setTime(now);
                    ticket.setVenueId(venueId);
                    ticket.setUserId(orders.getUserId());
                    ticket.setArea(seatState.getArea());
                    ticket.setRow(seatState.getRow());
                    ticket.setCol(seatState.getCol());
                    ticket.setPlanId(orders.getPlanId());
                    ticket.setOnline(true);
                    tickets.add(ticket);
                    seatDao.bookSeat(seatState);
                });
                //完善订单内容
                orders.setTime(now);

                //得到会员价
                double discount = PointsUtil.getDiscount(userDao.getTotalPoints(orders.getUserId()));
                orders.setActualPrice(orders.getOriginalPrice() * discount);
                ordersDao.newOrders(orders);
                if (orders.getId() > 0) {
                    ticketDao.insertTickets(orders.getId(), tickets);
                    new Thread(
                            new TicketSynchronizer.OrderPayCountdown(orders.getId(), true)
                    ).start();
                    return new ResultMessage<Long>(ResultMessage.OK, orders.getId());
                }
            }else {
                return new ResultMessage<Long>(ResultMessage.FAIL, "所选座位已被预订");
            }

            return new ResultMessage<Long>(ResultMessage.UNKNOWN, "订单生成失败");
        }
    }

    public ResultMessage<String> buyTicketOffline(Orders orders) {
        synchronized (this) {
            boolean isAvailable = true;
            List<SeatState> selectedSeat = orders.getSeatStates();
            //检测座位是否都能选
            for (int i = 0; i < selectedSeat.size(); i++) {
                SeatState seatState = selectedSeat.get(i);
                if (seatState.getRow() == 0) {
                    selectedSeat.remove(i);
                    continue;
                }
                seatState.setPlanId(orders.getPlanId());
                if (seatDao.isAvailable(seatState) <= 0) {
                    isAvailable = false;
                    break;
                }
            }

            if (isAvailable) {

                //为每个座位创建票根
                Long venueId = planDao.getVenueId(orders.getPlanId());
                List<Ticket> tickets = new ArrayList<>();
                Date now = new Date();
                selectedSeat.forEach(seatState -> {
                    Ticket ticket = new Ticket();
                    ticket.setTime(now);
                    ticket.setVenueId(venueId);
                    ticket.setUserId(orders.getUserId());
                    ticket.setArea(seatState.getArea());
                    ticket.setRow(seatState.getRow());
                    ticket.setCol(seatState.getCol());
                    ticket.setPlanId(orders.getPlanId());
                    ticket.setOnline(false);
                    tickets.add(ticket);
                    seatDao.bookSeat(seatState);
                });

                ticketDao.insertTickets( 0L, tickets);
                if(orders.getUserId() > 0) {
                    userDao.addPointsByConsumingOffline(orders.getUserId(), orders.getOriginalPrice().intValue() / 2);
                }
                return new ResultMessage<String>(ResultMessage.OK, "购票成功", "");
            }else {
                return new ResultMessage<String>(ResultMessage.FAIL,"", "所选座位已被预订");
            }
        }
    }

    public ResultMessage<Long> buyTicketWithoutSelect(Orders orders) {
        Long venueId = planDao.getVenueId(orders.getPlanId());
        orders.setVenueId(venueId);
        Date now = new Date();
        orders.setTime(now);

        PlanPrice planPrice = planDao.getAreaPrice(orders.getPlanId(), orders.getArea());
        orders.setOriginalPrice(planPrice.getPrice() * orders.getTicketNum());
        orders.setAreaName(planPrice.getName());
        //得到会员价
        double discount = PointsUtil.getDiscount(userDao.getTotalPoints(orders.getUserId()));
        orders.setActualPrice(orders.getOriginalPrice() * discount);
        ordersDao.newOrders(orders);
        if(orders.getId() > 0) {
            new Thread(
                    new TicketSynchronizer.OrderPayCountdown(orders.getId(),false)
            ).start();
            return new ResultMessage<Long>(ResultMessage.OK, orders.getId());
        }else {
            return new ResultMessage<Long>(ResultMessage.FAIL, "座位数量不足");
        }
    }

    public ResultMessage<String> allocateTicket(Orders orders) {
        synchronized (this) {
            int available = seatDao.getAvailableSeatNumByArea(orders.getPlanId(), orders.getArea());
            if (orders.getTicketNum() > available) {
                ordersDao.refund(orders.getId());
                ordersDao.refundMoney(orders.getId(), 1);
                return new ResultMessage<String>(ResultMessage.FAIL, "", "余座不足");
            }else {
                List<SeatState> selectedSeat = seatDao.getAvailableSeat(orders.getPlanId(), orders.getArea(), orders.getTicketNum());
                System.out.println(selectedSeat.size());
                System.out.println(orders.getPlanId() + " " + orders.getArea() + " " + orders.getTicketNum());
                List<Ticket> tickets = new ArrayList<>();
                selectedSeat.forEach(seatState -> {
                    Ticket ticket = new Ticket();
                    ticket.setTime(orders.getTime());
                    ticket.setVenueId(orders.getVenueId());
                    ticket.setUserId(orders.getUserId());
                    ticket.setArea(seatState.getArea());
                    ticket.setRow(seatState.getRow());
                    ticket.setCol(seatState.getCol());
                    ticket.setPlanId(orders.getPlanId());
                    ticket.setOnline(false);
                    tickets.add(ticket);
                    seatState.setPlanId(orders.getPlanId());
                    seatDao.bookSeat(seatState);
                });
                ticketDao.insertTickets(orders.getId(), tickets);
                ordersDao.allocateFinish(orders.getId());
                return new ResultMessage<String>(ResultMessage.OK, "配票完成", "");
            }
        }
    }

    class OrderPayCountdown implements Runnable{

        private final Long orderId;
        private final boolean isSelected;

        public OrderPayCountdown(Long orderId, boolean isSelected)
        {
            this.isSelected = isSelected;
            this.orderId = orderId;
        }

        @Override
        public void run() {
            try {
                // 5分钟内不支付则取消订单
                Thread.sleep(1 * 60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("取消订单: "+orderId);
            if(isSelected) {
                ordersDao.overdue(orderId);
            }else {
                ordersDao.overdueWithoutSelect(orderId);
            }
        }
    }
}
