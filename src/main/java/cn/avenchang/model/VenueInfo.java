package cn.avenchang.model;

import cn.avenchang.domain.Seat;

import java.util.List;

/**
 * Created by 53068 on 2018/3/28 0028.
 */
public class VenueInfo {

    private String email;
    private String venueName;
    private String location;
    private String password;
    private List<Seat> area;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Seat> getArea() {
        return area;
    }

    public void setArea(List<Seat> area) {
        this.area = area;
    }
}
