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
        if (sku.isEmpty()){
            throw new RuntimeException("Please add a valid sku");
        }
        else if (name.isEmpty()) {
            throw new RuntimeException("Please add a valid name");
        }
        if (productsBySku.containsKey(sku)){
            throw new RuntimeException("Sku is already in use. Please select another sku identifier");
        }
        Product createdProduct = new Product(name, "", "", "", sku, 0.0);
        productsBySku.put(sku,createdProduct);
    }

    public void updateProduct(String sku, String name, String description, String vendor, String urlSlug, double price) {
        if (sku.isEmpty()){
            throw new RuntimeException("Please add a valid sku");
        }
        else if (name.isEmpty()) {
            throw new RuntimeException("Please add a valid name");
        }
        else if (description.isEmpty() || vendor.isEmpty() || urlSlug.isEmpty() || Double.isNaN(price)) {
            throw new RuntimeException("Please add all fields");
        }
        if (urlSlug.length() > 100 || !urlSlug.matches("^[0-9a-z-]+$")) {
            throw new RuntimeException("Please add a valid url slug");
        }
        if (!productsBySku.containsKey(sku)){
            throw new RuntimeException("Product does not exist. Please add product before updating it");
        }
        Product getUpdatedProduct = productsBySku.get(sku);
        getUpdatedProduct.setName(name);
        getUpdatedProduct.setDescription(description);
        getUpdatedProduct.setVendor(vendor);
        getUpdatedProduct.setUrlSlug(urlSlug);
        getUpdatedProduct.setPrice(price);
        productsBySku.replace(sku,getUpdatedProduct);
        if (!productsBySlug.containsKey(urlSlug)) {
            productsBySlug.put(urlSlug, getUpdatedProduct);
        } else {
            productsBySlug.replace(urlSlug,getUpdatedProduct);
        }
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
