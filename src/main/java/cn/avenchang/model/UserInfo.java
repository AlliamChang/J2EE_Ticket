package cn.avenchang.model;

import java.util.Date;

/**
 * Created by 53068 on 2018/3/27 0027.
 */
public class UserInfo {

    private String email;

    private String realName;

    private String username;

    private int restPoints;

    private Date registTime;

    private int level;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRestPoints() {
        return restPoints;
    }

    public void setRestPoints(int restPoints) {
        this.restPoints = restPoints;
    }

    public Date getRegistTime() {
        return registTime;
    }

    public void setRegistTime(Date registTime) {
        this.registTime = registTime;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
