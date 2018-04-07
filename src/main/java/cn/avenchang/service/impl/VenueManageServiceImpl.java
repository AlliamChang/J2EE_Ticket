package cn.avenchang.service.impl;

import cn.avenchang.dao.*;
import cn.avenchang.domain.*;
import cn.avenchang.model.ResultMessage;
import cn.avenchang.model.UserDiscount;
import cn.avenchang.model.VenueStatistic;
import cn.avenchang.service.VenueManageService;
import cn.avenchang.util.PointsUtil;
import cn.avenchang.util.TicketSynchronizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 53068 on 2018/3/28 0028.
 */
@Service
public class VenueManageServiceImpl implements VenueManageService {

    @Autowired
    private VenueDao venueDao;
    @Autowired
    private PlanDao planDao;
    @Autowired
    private SeatDao seatDao;
    @Autowired
    private TicketDao ticketDao;
    @Autowired
    private EarningDao earningDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private TicketSynchronizer ticketSynchronizer;

    @Override
    public ResultMessage<Boolean> updateInfo(Venue venue) {
        if (venueDao.updateInfo(venue) > 0) {
            return new ResultMessage<Boolean>(ResultMessage.OK, true);
        } else {
            return new ResultMessage<Boolean>(ResultMessage.FAIL, false, "上传失败，稍后再试");
        }
    }

    @Override
    public ResultMessage<Venue> getInfo(final Long id) {
        final Venue venue = venueDao.findVenueById(id);
        if (venue == null) {
            return new ResultMessage(ResultMessage.FAIL, "未找到用户信息");
        }else {
            return new ResultMessage<Venue>(ResultMessage.OK, venue, "success");
        }
    }

    @Override
    public ResultMessage<Boolean> releasePlan(Plan plan) {
        Double lowestPrice = Double.MAX_VALUE;
        for (PlanPrice planPrice : plan.getPlanPrices()) {
            if (lowestPrice > planPrice.getPrice()) {
                lowestPrice = planPrice.getPrice();
            }
        }
        plan.setLowestPrice(lowestPrice);
        if(planDao.insertPlan(plan) > 0){
            //插入价格区
            if(planDao.insertPlanPrice(plan.getId(), plan.getPlanPrices()) ==
                    plan.getPlanPrices().size()){
                //插入座位状态
                seatDao.insertSeatStates(plan.getId(), seatDao.getSeatByVenueId(plan.getVenueId()));
                //插入该计划的赚钱记录
                earningDao.addNewPlanEarning(plan.getId(), plan.getVenueId());
                return new ResultMessage<Boolean>(ResultMessage.OK, true);
            }else {
                return new ResultMessage<Boolean>(ResultMessage.FAIL, "发布失败，请重试");
            }
        }else {
            return new ResultMessage<Boolean>(ResultMessage.FAIL, "发布失败，请重试");
        }
    }

    @Override
    public String getVenueName(Long id) {
        return venueDao.getVenueName(id);
    }

    @Override
    public ResultMessage<Boolean> checkIn(Long ticketId) {
        if(ticketDao.checkIn(ticketId) > 0){
            return new ResultMessage<Boolean>(ResultMessage.OK, true);
        }else {
            return new ResultMessage<Boolean>(ResultMessage.FAIL, "票根无效");
        }
    }

    @Override
    public ResultMessage<String> buyTicketOffline(Orders orders) {
        return ticketSynchronizer.buyTicketOffline(orders);
    }

    @Override
    public ResultMessage<UserDiscount> getUserDiscount(String email) {
        User user = userDao.getTotalPointsByEmail(email);
        if (user != null) {
            UserDiscount discount = new UserDiscount();
            discount.setUserId(user.getId());
            discount.setUsername(user.getUsername());
            discount.setUserDiscount(PointsUtil.getDiscount(user.getTotalPoints()));
            return new ResultMessage<UserDiscount>(ResultMessage.OK, discount);
        }else {
            return new ResultMessage<UserDiscount>(ResultMessage.FAIL, "该会员不存在");
        }
    }

    @Override
    public ResultMessage<Double> getVenueEarning(Long id) {
        return new ResultMessage<Double>(ResultMessage.OK, venueDao.getVenueEarning(id));
    }

    @Override
    public List<VenueStatistic> getVenueStatistic(Long id) {
        return planDao.getPlanStatistic(id);
    }
}
