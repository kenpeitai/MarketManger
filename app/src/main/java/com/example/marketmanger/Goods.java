package com.example.marketmanger;

public class Goods {
    public Goods(){};
    public Goods(String name, double price, int count) {
        this.name = name;
        this.price = price;
        this.count = count;
    }

    private String name;
    private double price;
    private int count;
    private boolean isShow = true;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
