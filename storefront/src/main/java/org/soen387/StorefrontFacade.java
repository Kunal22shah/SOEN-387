package org.soen387;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import org.soen387.Product;

public class StorefrontFacade {
    private final Connection connection;

    protected Map<String, Product> productsBySku;
    private final Map<String, Product> productsBySlug;
    private final Map<String, Cart> cartsByUser;
    private List<Order> allOrdersInStore;
    private Map<User, List<Order>> allOrderByUser;

    //Store a list of orders for each user
    private Map<String, ArrayList<Order>> allOrderByUser;

    //Store all Orders made by everyone
    private ArrayList<Order> allOrdersInStore;

    // Constructor
//    public StorefrontFacade() {
//        this.productsBySku = new HashMap<>();
//        this.productsBySlug = new HashMap<>();
//        this.cartsByUser = new HashMap<>();
//    }
    public StorefrontFacade() {

        this.connection = DatabaseConnection.getConnection();

        this.productsBySku = new HashMap<>();
        this.productsBySlug = new HashMap<>();
        this.cartsByUser = new HashMap<>();
        this.allOrderByUser = new HashMap<>();
        this.allOrdersInStore = new ArrayList<>();

    }

    public void createProduct(String sku, String name, String description, String vendor, String urlSlug, double price) {
        if (sku.isEmpty()) {
            throw new RuntimeException("Please add a valid sku");
        } else if (name.isEmpty()) {
            throw new RuntimeException("Please add a valid name");
        }
        if (productsBySku.containsKey(sku)) {
            throw new RuntimeException("Sku is already in use. Please select another sku identifier");
        }
        Product createdProduct = new Product(name, description, vendor, urlSlug, sku, price);
        productsBySku.put(sku, createdProduct);
        productsBySlug.put(urlSlug, createdProduct); // Also add to productsBySlug map

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
        // Validate input
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("SKU must not be null or empty");
        }

        Product product = null;

        String sql = "SELECT * FROM Products WHERE sku=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, sku);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                product = new Product(
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("vendor"),
                        resultSet.getString("urlSlug"),
                        resultSet.getString("sku"),
                        resultSet.getDouble("price")
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving product with SKU " + sku, e);
        }

        if (product == null) {
            throw new RuntimeException("Product with SKU " + sku + " does not exist");
        }

        return product;
    }


    public ArrayList<Product> getAllProduct() {
        ArrayList<Product> allProducts = new ArrayList<Product>();
        for (Product product : productsBySku.values()) {
            Product oneProduct = new Product(product.getName(), product.getDescription(), product.getVendor(), product.getUrlSlug(), product.getSku(), product.getPrice());
            allProducts.add(oneProduct);
        }
        return allProducts;
    }

    public Product getProductBySlug(String slug) {
        if (slug == null || slug.isEmpty()) {
            throw new IllegalArgumentException("Slug must not be null or empty");
        }

        Product product = null;

        String sql = "SELECT * FROM Products WHERE urlSlug=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, slug);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                product = new Product(
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("vendor"),
                        resultSet.getString("urlSlug"),
                        resultSet.getString("sku"),
                        resultSet.getDouble("price")
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving product by slug.", e);
        }

        if (product == null) {
            throw new RuntimeException("Product with slug " + slug + " does not exist");
        }
        return product;
    }

    public Cart getCart(String userEmail) {
        if (userEmail == null || userEmail.isEmpty()) {
            throw new IllegalArgumentException("User email must not be null or empty");
        }

        Cart cart = new Cart();
        String sql = "SELECT * FROM Carts WHERE userEmail=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userEmail);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Product product = getProduct(resultSet.getString("sku"));
                int quantity = resultSet.getInt("quantity");
                cart.addProduct(product, quantity);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving cart.", e);
        }

        return cart;
    }


    public void addProductToCart(String userEmail, String sku) {
        if (userEmail == null || userEmail.isEmpty()) {
            throw new IllegalArgumentException("User email must not be null or empty");
        }
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("SKU must not be null or empty");
        }

        String sqlCheck = "SELECT quantity FROM Carts WHERE userEmail=? AND sku=?";
        try (PreparedStatement checkStmt = connection.prepareStatement(sqlCheck)) {
            checkStmt.setString(1, userEmail);
            checkStmt.setString(2, sku);
            ResultSet resultSet = checkStmt.executeQuery();
            if (resultSet.next()) {
                int quantity = resultSet.getInt("quantity");
                String sqlUpdate = "UPDATE Carts SET quantity=? WHERE userEmail=? AND sku=?";
                try (PreparedStatement updateStmt = connection.prepareStatement(sqlUpdate)) {
                    updateStmt.setInt(1, quantity + 1);
                    updateStmt.setString(2, userEmail);
                    updateStmt.setString(3, sku);
                    updateStmt.executeUpdate();
                }
            } else {
                String sqlInsert = "INSERT INTO Carts(userEmail, sku, quantity) VALUES (?, ?, 1)";
                try (PreparedStatement insertStmt = connection.prepareStatement(sqlInsert)) {
                    insertStmt.setString(1, userEmail);
                    insertStmt.setString(2, sku);
                    insertStmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error adding product to cart.", e);
        }
    }

    public void removeProductFromCart(String userEmail, String sku) {
        if (userEmail == null || userEmail.isEmpty()) {
            throw new IllegalArgumentException("User email must not be null or empty");
        }
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("SKU must not be null or empty");
        }

        String sqlCheck = "SELECT quantity FROM Carts WHERE userEmail=? AND sku=?";
        try (PreparedStatement checkStmt = connection.prepareStatement(sqlCheck)) {
            checkStmt.setString(1, userEmail);
            checkStmt.setString(2, sku);
            ResultSet resultSet = checkStmt.executeQuery();
            if (resultSet.next()) {
                int quantity = resultSet.getInt("quantity");
                if (quantity > 1) {
                    String sqlUpdate = "UPDATE Carts SET quantity=? WHERE userEmail=? AND sku=?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(sqlUpdate)) {
                        updateStmt.setInt(1, quantity - 1);
                        updateStmt.setString(2, userEmail);
                        updateStmt.setString(3, sku);
                        updateStmt.executeUpdate();
                    }
                } else {
                    String sqlDelete = "DELETE FROM Carts WHERE userEmail=? AND sku=?";
                    try (PreparedStatement deleteStmt = connection.prepareStatement(sqlDelete)) {
                        deleteStmt.setString(1, userEmail);
                        deleteStmt.setString(2, sku);
                        deleteStmt.executeUpdate();
                    }
                }
            } else {
                return;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error removing product from cart.", e);
        }
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
    public ArrayList<Order> getOrders(String user){
        if (user == null || user.isEmpty()){
            throw new IllegalArgumentException("User must not be null or empty");
        }
        return allOrderByUser.get(user);
    }

    public Order getOrder(String user, int id){
        if (user == null){
            for (Order order : allOrdersInStore){
                int orderID = order.getOrderID();
                if (orderID == id){
                    return order;
                }
            }
        }
        ArrayList<Order> userOrders = allOrderByUser.get(user);
        for (Order order : userOrders){
            int orderID = order.getOrderID();
            String userEmail = order.getUser();
            if (orderID == id && user != null && user.equals(userEmail)){
                return order;
            }
        }
        return null;
    }

    public static class ProductAlreadyInCartException extends RuntimeException {
        public ProductAlreadyInCartException(String message) {
            super(message);
        }
    }

    public Order createOrder(String email, String shippingAddress) {
        User userObj = userUtility.getUserByEmail(email);
        if (userObj == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        Cart cart = cartsByUser.get(userObj.getUsername());
        if (cart == null || cart.getProducts().isEmpty()) {
            throw new RuntimeException("The cart is empty or does not exist for user: " + userObj.getUsername());
        }

        Order newOrder = new Order(shippingAddress, new ArrayList<>(cart.getProducts()), userObj);

        allOrdersInStore.add(newOrder);
        allOrderByUser.computeIfAbsent(userObj, k -> new ArrayList<>()).add(newOrder);

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO orders (order_id, shipping_address, tracking_number, user_id) VALUES (?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, newOrder.getOrderID());
                pstmt.setString(2, newOrder.getShippingAddress());
                pstmt.setInt(3, newOrder.getTrackingNumber());
                pstmt.setInt(4, userObj.getUserId()); // Assuming User class has getUserId()

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Creating order failed, no rows affected.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database operation failed", e);
        }

        return newOrder;
    }

    public boolean shipOrder(int id, int trackingNumber) {
        for (Order order : allOrdersInStore) {
            if (order.getOrderID() == id) {
                order.setTrackingNumber(trackingNumber);
                return true;
            }
        }
        return false;
    }
}
