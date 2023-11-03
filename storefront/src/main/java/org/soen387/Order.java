package org.soen387;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Order {
    private static int counter = 10000;
    private int orderID;
    private String shippingAdress;
    private int trackingNumber;
    private User user;
    private List<Product> orderProducts;

    public Order(){
        this.orderID = counter++;
        this.orderProducts = new ArrayList<>();
    }

    public Order(String shippingAdress, List<Product> orderProducts, User user){
        Random randomClass = new Random();

        this.orderID = counter++;
        this.shippingAdress = shippingAdress;
        this.orderProducts = new ArrayList<>(orderProducts); 
        this.trackingNumber = randomClass.nextInt(100000);
        this.user = user; 
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public void setTrackingNumber(int trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public List<Product> getOrderProducts() {
        return orderProducts;
    }
}
