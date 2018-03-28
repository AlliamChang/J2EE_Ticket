package cn.avenchang.service.impl;

import cn.avenchang.dao.SeatDao;
import cn.avenchang.domain.Seat;
import cn.avenchang.service.SeatService;
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

    @Override
    public List<Seat> getSeatMap(Long venueId) {
        return seatDao.getSeatByVenueId(venueId);
    }
}
