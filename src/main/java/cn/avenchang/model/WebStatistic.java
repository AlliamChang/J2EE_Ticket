package cn.avenchang.model;

/**
 * Created by 53068 on 2018/4/7 0007.
 */
public class WebStatistic {

    private Double income;
    private int userNum;
    private int orderNum;
    private Double orderValue;
    private int venueNum;
    private int planNum;

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public int getUserNum() {
        return userNum;
    }

    public void setUserNum(int userNum) {
        this.userNum = userNum;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public Double getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(Double orderValue) {
        this.orderValue = orderValue;
    }

    public int getVenueNum() {
        return venueNum;
    }

    public void setVenueNum(int venueNum) {
        this.venueNum = venueNum;
    }

    public int getPlanNum() {
        return planNum;
    }

    public void setPlanNum(int planNum) {
        this.planNum = planNum;
    }
}
