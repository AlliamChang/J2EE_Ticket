package cn.avenchang.service.impl;

import cn.avenchang.model.PlanInfo;
import cn.avenchang.service.PlanService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 53068 on 2018/3/28 0028.
 */
@Service
public class PlanServiceImpl implements PlanService {
    @Override
    public List<PlanInfo> getAllPlans() {
        List<PlanInfo> plans = new ArrayList<>();
        PlanInfo plan1 = new PlanInfo();
        plan1.setId(new Long(1));
        plan1.setLowestPrice(100.00);
        plan1.setTime(new Date());
        plan1.setTitle("恋爱的犀牛");
        plan1.setVenueName("北京国家戏剧院");
        plan1.setType("Drama");
        PlanInfo plan2 = new PlanInfo();
        plan2.setId(new Long(2));
        plan2.setLowestPrice(200.00);
        plan2.setTime(new Date());
        plan2.setTitle("中超决赛");
        plan2.setVenueName("广州体育馆");
        plan2.setType("Sports Game");
        plans.add(plan1);
        plans.add(plan2);
        return plans;
    }
}
