package cn.avenchang.service;

import cn.avenchang.domain.Venue;
import cn.avenchang.model.ResultMessage;

/**
 * Created by 53068 on 2018/3/14 0014.
 */
public interface VenueManageService {

    ResultMessage<Boolean> updateInfo(Venue venue);

    ResultMessage<Venue> getInfo(Long id);

    void releasePlan();

    void updateSeat();


}
