package cn.avenchang.domain;

/**
 * Created by 53068 on 2018/3/29 0029.
 */
public class PlanPrice {

    private Long planId;
    private String name;
    private int area;
    private Double price;

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
