package cn.avenchang.service.impl;

import cn.avenchang.dao.PlanDao;
import cn.avenchang.domain.Plan;
import cn.avenchang.model.PlanInfo;
import cn.avenchang.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 53068 on 2018/3/28 0028.
 */
@Service
public class PlanServiceImpl implements PlanService {
    @Autowired
    private PlanDao planDao;

    @Override
    public PlanInfo getPlanDetail(Long id) {
        return planDao.getPlanById(id);
    }

    @Override
    public List<PlanInfo> getAllPlans() {
        List<Plan> planPO = planDao.getAllPlan();
        List<PlanInfo> plans = new ArrayList<>();
        planPO.forEach(plan -> {
            PlanInfo plan1 = new PlanInfo();
            plan1.setId(plan.getId());
            plan1.setLowestPrice(plan.getLowestPrice());
            plan1.setTime(plan.getTime());
            plan1.setTitle(plan.getTitle());
            plan1.setVenueName(plan.getVenueName());
            plan1.setType(plan.getType());
            plans.add(plan1);
        });

        return plans;
    }

    @Override
    public List<PlanInfo> getVenuePlan(Long venueId) {
        List<Plan> planPO = planDao.getVenuePlan(venueId);
        List<PlanInfo> plans = new ArrayList<>();
        planPO.forEach(plan -> {
            PlanInfo plan1 = new PlanInfo();
            plan1.setId(plan.getId());
            plan1.setTime(plan.getTime());
            plan1.setTitle(plan.getTitle());
            plan1.setType(plan.getType());
            plans.add(plan1);
        });

        return plans;
    }
}
