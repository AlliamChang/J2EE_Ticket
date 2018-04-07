package cn.avenchang.service;

import cn.avenchang.domain.Orders;
import cn.avenchang.domain.Plan;
import cn.avenchang.domain.SeatState;
import cn.avenchang.domain.Venue;
import cn.avenchang.model.ResultMessage;
import cn.avenchang.model.UserDiscount;
import cn.avenchang.model.VenueStatistic;

import java.util.List;

/**
 * Created by 53068 on 2018/3/14 0014.
 */
public interface VenueManageService {

    ResultMessage<Boolean> updateInfo(Venue venue);

    ResultMessage<Venue> getInfo(Long id);

    ResultMessage<Boolean> releasePlan(Plan plan);

    ResultMessage<Boolean> checkIn(Long ticketId);

    String getVenueName(Long id);

    ResultMessage<String> buyTicketOffline(Orders orders);

    ResultMessage<UserDiscount> getUserDiscount(String email);

    ResultMessage<Double> getVenueEarning(Long id);

    List<VenueStatistic> getVenueStatistic(Long id);

}
