package cn.avenchang.domain;

import java.util.Date;

/**
 * Created by 53068 on 2018/3/27 0027.
 */
public class Venue {

    private Long id;
    private String email;
    private String password;
    private String token;
    private String name;
    private int flag;
    private Date registTime;
    private String location;
    private Double account;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getAccount() {
        return account;
    }

    public void setAccount(Double account) {
        this.account = account;
    }
}
