package cn.avenchang.model;

/**
 * Created by 53068 on 2018/4/3 0003.
 */
public class VenueEarning {

    private Long venueId;
    private String venueName;
    private Double earning;

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

    public Double getEarning() {
        return earning;
    }

    public void setEarning(Double earning) {
        this.earning = earning;
    }
}
