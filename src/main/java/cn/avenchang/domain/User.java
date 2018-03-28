package cn.avenchang.domain;

import java.util.Date;

/**
 * Created by 53068 on 2018/3/7 0007.
 */
public class User {

    private Long id;

    private String email;

    private String password;

    private String token;

    private String verification;

    private String realName;

    private String username;

    private int totalPoints;

    private int restPoints;

    private int flag;

    private Date registTime;

    private Date latestTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getRestPoints() {
        return restPoints;
    }

    public void setRestPoints(int restPoints) {
        this.restPoints = restPoints;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Date getRegistTime() {
        return registTime;
    }

    public void setRegistTime(Date registTime) {
        this.registTime = registTime;
    }

    public Date getLatestTime() {
        return latestTime;
    }

    public void setLatestTime(Date latestTime) {
        this.latestTime = latestTime;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }
}
