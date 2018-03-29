package cn.avenchang.service;

import cn.avenchang.domain.Plan;
import cn.avenchang.model.PlanInfo;

import java.util.List;

/**
 * Created by 53068 on 2018/3/28 0028.
 */
public interface PlanService {

    PlanInfo getPlanDetail(Long id);

    List<PlanInfo> getAllPlans();

    List<PlanInfo> getVenuePlan(Long venueId);
}
