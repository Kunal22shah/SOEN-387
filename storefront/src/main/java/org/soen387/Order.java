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
    private List<OrderProductItem> orderProducts;

    public Order(){
        this.orderID = counter++;
        this.orderProducts = new ArrayList<>();
    }
    public Order(String shippingAdress, List<OrderProductItem> orderProducts, String user, int orderID, int trackingNumber, boolean isShipped){
        this.orderID = orderID;
        this.shippingAdress = shippingAdress;
        this.orderProducts = orderProducts;
        this.trackingNumber = trackingNumber;
        this.user = user;
        this.isShipped = isShipped;
    }

    public Order(String shippingAddress, ArrayList<OrderProductItem> orderProductItems, String userEmail){
        this.shippingAddress = shippingAddress;
        this.orderProducts = orderProductItems;
        this.user = userEmail;
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

    public List<OrderProductItem> getOrderProducts() {
        return orderProducts;
    }

    public boolean isShipped() {
        return isShipped;
    }

    //Class to store the products in a order
    public static class OrderProductItem {
        private Product product;
        private int quantity;


        public OrderProductItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;

        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
