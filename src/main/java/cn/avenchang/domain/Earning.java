package cn.avenchang.domain;

/**
 * Created by 53068 on 2018/3/30 0030.
 */
public class Earning {

    private Long venueId;
    private String venueName;
    private Long planId;
    private Double earning;
    private boolean isSettle;

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Double getEarning() {
        return earning;
    }

    public void setEarning(Double earning) {
        this.earning = earning;
    }

    public boolean isSettle() {
        return isSettle;
    }

    public void setSettle(boolean settle) {
        isSettle = settle;
    }
}
