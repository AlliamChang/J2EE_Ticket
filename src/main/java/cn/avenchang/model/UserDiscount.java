package cn.avenchang.model;

/**
 * Created by 53068 on 2018/4/4 0004.
 */
public class UserDiscount {

    private Long userId;
    private String username;
    private Double userDiscount;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getUserDiscount() {
        return userDiscount;
    }

    public void setUserDiscount(Double userDiscount) {
        this.userDiscount = userDiscount;
    }
}
