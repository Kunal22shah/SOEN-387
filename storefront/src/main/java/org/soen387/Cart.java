package org.soen387;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Cart {

    private List<CartItem> cartItems;

    public Cart() {
        this.cartItems = new ArrayList<>();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void addProduct(Product p, int quantity) {
        if (p != null) {
            for (CartItem cartItem : cartItems) {
                if (cartItem.getProduct().getSku().equals(p.getSku())) {
                    cartItem.setQuantity(cartItem.getQuantity() + quantity);
                    return;
                }
            }
            cartItems.add(new CartItem(p, quantity));
        }
    }

    public void removeProductBySku(String sku) {
        cartItems = cartItems.stream()
                .filter(cartItem -> !cartItem.getProduct().getSku().equals(sku))
                .collect(Collectors.toList());
    }

    public boolean containsProduct(String sku) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getProduct().getSku().equals(sku)) {
                return true;
            }
        }
        return false;
    }

    public int getQuantityForSKU(String sku) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getProduct().getSku().equals(sku)) {
                return cartItem.getQuantity();
            }
        }
        return 0;
    }

    public static class CartItem {
        private Product product;
        private int quantity;

        public CartItem(Product product, int quantity) {
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
