package cn.avenchang.util;

import cn.avenchang.config.DefaultConfig;

import java.util.Date;

/**
 * Created by 53068 on 2018/4/7 0007.
 */
public class TimeUtil {
    private static DefaultConfig defaultConfig = new DefaultConfig();

    public static Long getTimeDistance(Date before, Date after) {
        return after.getTime() - before.getTime();
    }

    public static Double getRefundPercent(Date now, Date planTime) {
        int timeCase = 0;
        Long timeDistance = getTimeDistance(now, planTime);
//        System.out.println(timeDistance);
        for (Long aLong : defaultConfig.getRefundTime()) {
            if (timeDistance > aLong) {
                break;
            }
            timeCase ++;
        }
        return defaultConfig.getRefundPercent().get(timeCase);
    }
}
