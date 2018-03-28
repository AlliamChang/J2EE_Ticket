package cn.avenchang.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 53068 on 2018/3/25 0025.
 */
@Component
public class DefaultConfig {

    @Value("${my.host}")
    private String host;

    private static List<String> planTypes = Arrays.asList("Concert", "Dance", "Drama", "Sports Game");

    private static List<Integer> userLevel = Arrays.asList(500, 1000, 2000, 4000, 8000);

    public String getHost() {
        return host;
    }

    public List<String> getPlanTypes() {
        return planTypes;
    }

    public List<Integer> getUserLevel() {
        return userLevel;
    }
}