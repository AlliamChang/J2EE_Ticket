package cn.avenchang.service.impl;

import cn.avenchang.dao.PlanDao;
import cn.avenchang.dao.SeatDao;
import cn.avenchang.dao.VenueDao;
import cn.avenchang.domain.Plan;
import cn.avenchang.domain.PlanPrice;
import cn.avenchang.domain.Venue;
import cn.avenchang.model.ResultMessage;
import cn.avenchang.service.VenueManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return null;
    }

    @Override
    public void buyTicketOffline() {

    }
}
