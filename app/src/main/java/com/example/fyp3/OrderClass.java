package com.example.fyp3;

public class OrderClass {



    private String username;
    private String orderAddress;
    private String orderRemark;
    private String orderType;
    private String order_status;
    private String date;
    private String time;
    private String dev_name;
    private String estimate_date;



    public OrderClass(){

    }

    public String getEstimate_date() {return estimate_date;}

    public void setEstimate_date(String estimate_date) {this.estimate_date = estimate_date;}

    public String getDev_name() {return dev_name;}

    public void setDev_name(String dev_name) {this.dev_name = dev_name;}

    public String getDate() {return date;}

    public void setDate(String date) {this.date = date;}

    public String getTime() {return time;}

    public void setTime(String time) {this.time = time;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getOrderType() {return orderType;}

    public void setOrderType(String orderType) {this.orderType = orderType;}

    public String getOrderAddress() {return orderAddress; }

    public void setOrderAddress(String orderAddress) {this.orderAddress = orderAddress;}

    public String getOrderRemark() {return orderRemark;}

    public void setOrderRemark(String orderRemark) {this.orderRemark = orderRemark; }

    public String getOrder_status() {return order_status;}

    public void setOrder_status(String order_status) {this.order_status = order_status;}


}
