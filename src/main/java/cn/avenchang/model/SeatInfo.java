package cn.avenchang.model;

/**
 * Created by 53068 on 2018/4/1 0001.
 */
public class SeatInfo {

    private int area;
    private int row;
    private int col;
    private String name;
    private double price;
    private int rest;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public boolean isOut() {
        return (rest > 20);
    }
}
