package cn.avenchang.model;

import java.util.Date;

/**
 * Created by 53068 on 2018/3/28 0028.
 */
public class PlanInfo {

    private Long id;
    private String title;
    private String venueName;
    private String type;
    private Date time;
    private Double lowestPrice;
    private String poster;

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

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        if ("Dance".equals(type)) {
            poster = "/image/dance.jpg";
        }else if("Sports Game".equals(type)){
            poster = "/image/sports.jpg";
        } else if ("Drama".equals(type)) {
            poster = "/image/drama.jpg";
        } else if ("Concert".equals(type)) {
            poster = "/image/concert.jpg";
        } else{
            poster = "";
        }
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Double getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(Double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public String getPoster() {
        return poster;
    }
}
