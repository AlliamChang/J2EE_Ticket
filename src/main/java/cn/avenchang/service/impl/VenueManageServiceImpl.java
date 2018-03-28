package cn.avenchang.service.impl;

import cn.avenchang.dao.VenueDao;
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
    public void releasePlan() {

    }

    @Override
    public void updateSeat() {

    }
}
