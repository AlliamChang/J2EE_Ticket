package cn.avenchang.service.impl;

import cn.avenchang.dao.PlanDao;
import cn.avenchang.dao.SeatDao;
import cn.avenchang.domain.PlanPrice;
import cn.avenchang.domain.Seat;
import cn.avenchang.domain.SeatState;
import cn.avenchang.model.LiveSeatMap;
import cn.avenchang.service.SeatService;
import cn.avenchang.util.SeatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 53068 on 2018/3/29 0029.
 */
@Service
public class SeatServiceImpl implements SeatService{
    @Autowired
    private SeatDao seatDao;
    @Autowired
    private PlanDao planDao;

    @Override
    public List<Seat> getSeatMap(Long venueId) {
        return seatDao.getSeatByVenueId(venueId);
    }

    @Override
    public LiveSeatMap getLiveSeatMap(Long planId) {
        List<SeatState> unavailableSeats = seatDao.getUnavailableSeats(planId);
        List<Seat> areas = seatDao.getSeatByPlanId(planId);
        List<PlanPrice> planPrice = planDao.getPlanPrice(planId);

        return SeatUtil.getLiveSeatMap(areas, planPrice, unavailableSeats);
    }
}
