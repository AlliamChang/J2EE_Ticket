package cn.avenchang.service;

import cn.avenchang.domain.Venue;
import cn.avenchang.model.ResultMessage;
import cn.avenchang.model.VenueEarning;
import cn.avenchang.model.VenueUpdate;

import java.util.List;

/**
 * Created by 53068 on 2018/3/14 0014.
 */
public interface ManagerService {

    /**
     * 审批注册
     */
    ResultMessage<Boolean> approveRegister(Long venueId);

    /**
     * 驳回注册
     * @param venueId
     * @return
     */
    ResultMessage<Boolean> refuseRegister(Long venueId);

    /**
     * 审批修改信息
     * @param venueId
     * @return
     */
    ResultMessage<Boolean> approveUpdateInfo(Long venueId);

    /**
     * 驳回修改
     * @param venueId
     * @return
     */
    ResultMessage<Boolean> refuseUpdateInfo(Long venueId);

    /**
     * 获取待审批注册的场馆信息
     * @return
     */
     List<Venue> getToApproveRegisterList();

    /**
     * 获取待审批修改的场馆信息
     * @return
     */
    List<VenueUpdate> getToApproveUpdateList();

    /**
     * 得到未结算盈利列表
     * @return
     */
    List<VenueEarning> getUnsettle();

    /**
     * 结算场馆所得
     */
    ResultMessage<String> settleEarning(Long venueId);

    void charts();
}
