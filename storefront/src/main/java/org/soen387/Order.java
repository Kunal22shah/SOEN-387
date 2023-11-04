package org.soen387;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Order {
    private static int counter = 10000;
    private int orderID;
    private String shippingAdress;
    private int trackingNumber;
    private String user;

    private boolean isShipped;
    private List<Product> orderProducts;



    public Order(){
        this.orderID = counter++;
        this.orderProducts = new ArrayList<>();
    }
    public Order(String shippingAdress, List<Product> orderProducts, String user, int orderID, int trackingNumber, boolean isShipped){
        this.orderID = orderID;
        this.shippingAdress = shippingAdress;
        this.orderProducts = orderProducts;
        this.trackingNumber = trackingNumber;
        this.user = user;
        this.isShipped = isShipped;
    }
    public String getUser() {
        return user;
    }
    public int getOrderID() {
        return orderID;
    }

    public String getShippingAdress() {
        return shippingAdress;
    }

    public int getTrackingNumber() {
        return trackingNumber;
    }

    public List<Product> getOrderProducts() {
        return orderProducts;
    }

    public boolean isShipped() {
        return isShipped;
    }
}
