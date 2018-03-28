package cn.avenchang.service.impl;

import cn.avenchang.dao.VenueDao;
import cn.avenchang.domain.Venue;
import cn.avenchang.model.ResultMessage;
import cn.avenchang.model.VenueUpdate;
import cn.avenchang.service.EmailService;
import cn.avenchang.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 53068 on 2018/3/28 0028.
 */
@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    private VenueDao venueDao;
    @Autowired
    private EmailService emailService;

    @Override
    public ResultMessage<Boolean> approveRegister(final Long venueId) {
        if(venueDao.approveRegister(venueId) > 0){
            final String email = venueDao.findVenueEmail(venueId);
            new Thread(
                    () -> {
                        emailService.sendEmail(email, "审批通过", "您好，您的场地注册申请已经通过审核！您的登录ID为"+venueId);
                    }
            ).start();
            return new ResultMessage<Boolean>(ResultMessage.OK, true);
        }else {
            return new ResultMessage<Boolean>(ResultMessage.FAIL, false, "审批失败，稍后再试");
        }
    }

    @Override
    public ResultMessage<Boolean> refuseRegister(Long venueId) {
        if (venueDao.refuseRegister(venueId) > 0) {
            final String email = venueDao.findVenueEmail(venueId);
            new Thread(
                    () -> {
                        emailService.sendEmail(email, "审核不通过", "您好，您的场馆注册申请未能通过审核");
                    }
            ).start();
            return new ResultMessage<Boolean>(ResultMessage.OK, true);
        }else {
            return new ResultMessage<Boolean>(ResultMessage.FAIL, false, "审批失败，稍后再试");
        }
    }

    @Override
    public ResultMessage<Boolean> approveUpdateInfo(Long venueId) {
        if(venueDao.approveUpdateInfo(venueId) > 0){
            final String email = venueDao.findVenueEmail(venueId);
            new Thread(
                    () -> {
                        emailService.sendEmail(email, "修改申请审核通过", "您好，您的场馆修改申请已通过审核");
                    }
            ).start();
            return new ResultMessage<Boolean>(ResultMessage.OK, true);
        }else {
            return new ResultMessage<Boolean>(ResultMessage.FAIL, false, "审批失败，稍后再试");
        }
    }

    @Override
    public ResultMessage<Boolean> refuseUpdateInfo(Long venueId) {
        if (venueDao.refuseUpdateInfo(venueId) > 0) {
            final String email = venueDao.findVenueEmail(venueId);
            new Thread(
                    () -> {
                        emailService.sendEmail(email, "修改申请审批未通过", "您好，您的场馆修改申请未能通过审核");
                    }
            ).start();
            return new ResultMessage<Boolean>(ResultMessage.OK, true);
        }else {
            return new ResultMessage<Boolean>(ResultMessage.FAIL, false, "审批失败，稍后再试");
        }
    }

    @Override
    public List<Venue> getToApproveRegisterList() {
        return venueDao.getRegisterUnapprovedVenue();
    }

    @Override
    public List<VenueUpdate> getToApproveUpdateList() {
        return venueDao.getUpdateUnapprovedVenue();
    }

    @Override
    public void settle() {

    }

    @Override
    public void charts() {

    }
}
