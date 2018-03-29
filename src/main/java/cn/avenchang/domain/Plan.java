package cn.avenchang.domain;

import java.util.Date;
import java.util.List;

/**
 * Created by 53068 on 2018/3/29 0029.
 */
public class Plan {

    private Long id;
    private String title;
    private Long venueId;
    private String venueName;
    private Date time;
    private String description;
    private String type;
    private Double lowestPrice;
    private List<PlanPrice> planPrices;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(Double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public List<PlanPrice> getPlanPrices() {
        return planPrices;
    }

    public void setPlanPrices(List<PlanPrice> planPrices) {
        this.planPrices = planPrices;
    }
}
