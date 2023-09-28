package org.soen387;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.soen387.Product;

public class StorefrontFacade {

    private Map<String, Product> productsBySku;
    private Map<String, Product> productsBySlug;
    private Map<String, Cart> cartsByUser;

    // Constructor
    public StorefrontFacade() {
        this.productsBySku = new HashMap<>();
        this.productsBySlug = new HashMap<>();
        this.cartsByUser = new HashMap<>();
    }


    public void createProduct(String sku, String name) {
        Product createdProduct = new Product(name, "", "", "", sku, 0.0);
        productsBySku.put(sku,createdProduct);
    }

    public void updateProduct(String sku, String name) {

    }

    public Product getProduct(String sku) {

        return null;
    }


    public Product getProductBySlug(String slug) {
        return null;
    }

    public Cart getCart(String user) {

        return null;
    }

    public void addProductToCart(String user, String sku) {

    }

    public void removeProductFromCart(String user, String sku) {

    }

    public void downloadProductCatalog() {

    }


}
