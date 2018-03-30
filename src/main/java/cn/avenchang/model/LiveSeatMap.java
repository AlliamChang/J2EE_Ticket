package cn.avenchang.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 53068 on 2018/3/30 0030.
 */
public class LiveSeatMap {

    private List<String> seatMap = new ArrayList<>();
    private List<String[]> seatLegend = new ArrayList<>();
    private List<String> seatUnavailable = new ArrayList<>();
    private List<Object[]> seatPrice = new ArrayList<>();

    public List<String> getSeatMap() {
        return seatMap;
    }

    public void setSeatMap(List<String> seatMap) {
        this.seatMap = seatMap;
    }

    public List<String[]> getSeatLegend() {
        return seatLegend;
    }

    public void setSeatLegend(List<String[]> seatLegend) {
        this.seatLegend = seatLegend;
    }

    public List<String> getSeatUnavailable() {
        return seatUnavailable;
    }

    public void setSeatUnavailable(List<String> seatUnavailable) {
        this.seatUnavailable = seatUnavailable;
    }

    public List<Object[]> getSeatPrice() {
        return seatPrice;
    }

    public void setSeatPrice(List<Object[]> seatPrice) {
        this.seatPrice = seatPrice;
    }
}
