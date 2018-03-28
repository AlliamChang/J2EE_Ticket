package cn.avenchang.model;

/**
 * Created by 53068 on 2018/3/28 0028.
 */
public class VenueUpdate {

    private Long venueId;
    private String oldName;
    private String newName;
    private String oldLocation;
    private String newLocation;

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getOldLocation() {
        return oldLocation;
    }

    public void setOldLocation(String oldLocation) {
        this.oldLocation = oldLocation;
    }

    public String getNewLocation() {
        return newLocation;
    }

    public void setNewLocation(String newLocation) {
        this.newLocation = newLocation;
    }
}
