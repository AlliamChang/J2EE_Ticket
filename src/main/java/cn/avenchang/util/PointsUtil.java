package cn.avenchang.util;

/**
 * Created by 53068 on 2018/3/27 0027.
 */

import cn.avenchang.config.DefaultConfig;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 积分变换工具类
 */
public class PointsUtil {

    private static DefaultConfig defaultConfig = new DefaultConfig();

    public static int getLevel(int points) {
        int level = 0;
        for (Integer i : defaultConfig.getUserLevel()) {
            if(level < i){
                break;
            }
            level ++;
        }
        return level;
    }

    public static double getDiscount(int points) {
        return defaultConfig.getUserDiscount().get(getLevel(points));
    }
}
