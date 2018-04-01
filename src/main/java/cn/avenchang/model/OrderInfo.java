package cn.avenchang.model;

import cn.avenchang.domain.PlanPrice;

import java.util.Date;
import java.util.List;

/**
 * Created by 53068 on 2018/4/1 0001.
 */
public class OrderInfo {

    private Long id;
    private Long accountId;
    private Long venueId;
    private Long planId;
    private Double originalPrice;
    private Double actualPrice;
    private String planTitle;
    private Date time;
    private int state;
    private Long distance;
    private List<SeatInfo> seatInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(Double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public List<SeatInfo> getSeatInfo() {
        return seatInfo;
    }

    public void setSeatInfo(List<SeatInfo> seatInfo) {
        this.seatInfo = seatInfo;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }
}
