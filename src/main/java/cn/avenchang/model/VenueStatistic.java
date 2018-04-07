package cn.avenchang.model;

/**
 * Created by 53068 on 2018/4/7 0007.
 */
public class VenueStatistic {

    private String title;
    private int paidNum;
    private int refundNum;
    private Double earning;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPaidNum() {
        return paidNum;
    }

    public void setPaidNum(int paidNum) {
        this.paidNum = paidNum;
    }

    public int getRefundNum() {
        return refundNum;
    }

    public void setRefundNum(int refundNum) {
        this.refundNum = refundNum;
    }

    public Double getEarning() {
        return earning;
    }

    public void setEarning(Double earning) {
        this.earning = earning;
    }
}
