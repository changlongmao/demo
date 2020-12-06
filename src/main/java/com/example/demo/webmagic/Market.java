package com.example.demo.webmagic;

/**
 * @author cai
 */
public class Market {

    //指数名称
    private String name;
    //指数价格
    private String price;
    //指数涨幅
    private String updown;
    //涨幅百分比
    private String percent;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUpdown() {
        return updown;
    }

    public void setUpdown(String updown) {
        this.updown = updown;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "Market{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", updown='" + updown + '\'' +
                ", percent='" + percent + '\'' +
                '}';
    }
}
