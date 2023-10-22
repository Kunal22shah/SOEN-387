package org.soen387;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;

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
        if (sku.isEmpty()) {
            throw new RuntimeException("Please add a valid sku");
        } else if (name.isEmpty()) {
            throw new RuntimeException("Please add a valid name");
        }
        if (productsBySku.containsKey(sku)) {
            throw new RuntimeException("Sku is already in use. Please select another sku identifier");
        }
        Product createdProduct = new Product(name, "", "", "", sku, 0.0);
        productsBySku.put(sku, createdProduct);
        System.out.println("Product with sku " + productsBySku.get(sku).getSku() + " has been added");
    }

    public void updateProduct(String sku, String name, String description, String vendor, String urlSlug, double price) {
        if (sku.isEmpty()) {
            throw new RuntimeException("Please add a valid sku");
        } else if (name.isEmpty()) {
            throw new RuntimeException("Please add a valid name");
        } else if (description.isEmpty() || vendor.isEmpty() || urlSlug.isEmpty() || Double.isNaN(price)) {
            throw new RuntimeException("Please add all fields correctly");
        }
        if (urlSlug.length() > 100 || !urlSlug.matches("^[0-9a-z-]+$")) {
            throw new RuntimeException("Please add a valid url slug");
        }
        if (!productsBySku.containsKey(sku)) {
            throw new RuntimeException("Product does not exist. Please add product before updating it");
        }
        Product productWithSameSlug = productsBySlug.get(urlSlug);
        if (productWithSameSlug != null && !productWithSameSlug.getSku().equals(sku)) {
            throw new RuntimeException("URL slug is already in use. Please select another slug.");
        }
        Product getUpdatedProduct = productsBySku.get(sku);
        getUpdatedProduct.setName(name);
        getUpdatedProduct.setDescription(description);
        getUpdatedProduct.setVendor(vendor);
        getUpdatedProduct.setUrlSlug(urlSlug);
        getUpdatedProduct.setPrice(price);
        productsBySku.replace(sku, getUpdatedProduct);
        if (!productsBySlug.containsKey(urlSlug)) {
            productsBySlug.put(urlSlug, getUpdatedProduct);
        } else {
            productsBySlug.replace(urlSlug, getUpdatedProduct);
        }
        System.out.println("Product with sku " + productsBySku.get(sku).getSku() + " has been updated");
    }

    public Product getProduct(String sku) {
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("SKU must not be null or empty");
        }
        Product product = productsBySku.get(sku);
        if (product == null) {
            throw new RuntimeException("Product with SKU " + sku + " does not exist");
        }
        return product;
    }

    public ArrayList<Product> getAllProduct(){
        ArrayList<Product> allProducts = new ArrayList<Product>();
        for (Product product : productsBySku.values()){
            Product oneProduct = new Product (product.getName(), product.getDescription(), product.getVendor(), product.getUrlSlug(), product.getSku(), product.getPrice());
            allProducts.add(oneProduct);
        }
        return allProducts;
    }

    public Product getProductBySlug(String slug) {
        if (slug == null || slug.isEmpty()) {
            throw new IllegalArgumentException("Slug must not be null or empty");
        }
        Product product = productsBySlug.get(slug);
        if (product == null) {
            throw new RuntimeException("Product with slug " + slug + " does not exist");
        }
        return product;
    }

    public Cart getCart(String user) {
        if (user == null || user.isEmpty()) {
            throw new IllegalArgumentException("User must not be null or empty");
        }
        Cart cart = cartsByUser.getOrDefault(user, new Cart());
        return cart;
    }

    public void addProductToCart(String user, String sku) {
        if (user == null || user.isEmpty()) {
            throw new IllegalArgumentException("User must not be null or empty");
        }
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("SKU must not be null or empty");
        }
        Product product = getProduct(sku); // Reuse the getProduct method to ensure the product exists
        Cart cart = cartsByUser.get(user);
        if (cart.containsProduct(sku)) {
            throw new ProductAlreadyInCartException("Product with SKU " + sku + " is already in the cart");
    }
        if (cart == null) {
            cart = new Cart();
            cartsByUser.put(user, cart);
        }
        cart.addProduct(product);
    }

    public void removeProductFromCart(String user, String sku) {
        if (user == null || user.isEmpty()) {
            throw new IllegalArgumentException("User must not be null or empty");
        }
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("SKU must not be null or empty");
        }
        Cart cart = cartsByUser.get(user);
        if (cart == null) {
            // No cart is associated with the user, so no operation is performed.
            return;
        }
        cart.removeProductBySku(sku);
    }

    public void downloadProductCatalog() {
        Gson gson = new Gson();
        String jsonCatalog = gson.toJson(productsBySku.values());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("ProductCatalog.json"))) {
            writer.write(jsonCatalog);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to write the product catalog to file");
        }
    }

    public static class ProductAlreadyInCartException extends RuntimeException {
        public ProductAlreadyInCartException(String message) {
            super(message);
        }
    }
}
