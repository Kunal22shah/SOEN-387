package org.soen387;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private static int counter = 9999;
    private int orderID;
    private String shippingAdress;
    private int trackingNumber;

    private List<Product> orderProducts;

    public Order(){
        this.orderID = counter++;
        this.orderProducts = new ArrayList<>();
    }

    public Order(String shippingAdress, int trackingNumber, List<Product> orderProducts){
        this.orderID = counter++;
        this.shippingAdress = shippingAdress;
        this.orderProducts = orderProducts;
        this.trackingNumber = trackingNumber;
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
}
