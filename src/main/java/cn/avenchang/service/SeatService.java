package cn.avenchang.service;

import cn.avenchang.domain.Seat;
import cn.avenchang.model.LiveSeatMap;

import java.util.List;

/**
 * Created by 53068 on 2018/3/29 0029.
 */
public interface SeatService {

    List<Seat> getSeatMap(Long venueId);

    LiveSeatMap getLiveSeatMap(Long planId);
}
