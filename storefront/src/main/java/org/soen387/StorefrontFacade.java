package org.soen387;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.sql.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.gson.Gson;

public class StorefrontFacade {
    private final Connection connection;
    private final Map<String, Product> productsBySlug;
    private final Map<String, Cart> cartsByUser;
    protected Map<String, Product> productsBySku;
    //Store a list of orders for each user
    private Map<String, ArrayList<Order>> allOrderByUser;

    //Store all Orders made by everyone
    private ArrayList<Order> allOrdersInStore;

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
        String sql = "INSERT INTO PRODUCTS(sku, name, description, vendor, urlSlug, price) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement insertStmt = connection.prepareStatement(sql)) {
            insertStmt.setString(1, sku);
            insertStmt.setString(2, name);
            insertStmt.setString(3, description);
            insertStmt.setString(4, vendor);
            insertStmt.setString(5, urlSlug);
            insertStmt.setDouble(6, price);
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
        String sql = "SELECT urlSlug, sku FROM PRODUCTS WHERE urlSlug = ?";
        String getUrlSlug = "";
        String productSku = "";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, urlSlug);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                getUrlSlug = resultSet.getString("urlSlug");
                productSku = resultSet.getString("sku");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (getUrlSlug.equals(urlSlug) && !productSku.equals(sku) ){
            throw new RuntimeException("Url slug in use");
        }
        String sqlupdate = "UPDATE PRODUCTS SET name=?, description=?, vendor=?, urlSlug=?, price=? WHERE sku = ?";
        try (PreparedStatement updateStmt = connection.prepareStatement(sqlupdate)) {
            updateStmt.setString(1, name);
            updateStmt.setString(2, description);
            updateStmt.setString(3, vendor);
            updateStmt.setString(4, urlSlug);
            updateStmt.setDouble(5, price);
            updateStmt.setString(6, sku);
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
        try {
            Statement stmt = connection.createStatement();
            String Sql = "SELECT * FROM Products";
            ResultSet rs = stmt.executeQuery(Sql);
            while(rs.next()){
                String name = rs.getString("name");
                String vendor = rs.getString("vendor");
                String urlSlug = rs.getString("urlSlug");
                String sku = rs.getString("sku");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                allProducts.add(new Product(name,description,vendor,urlSlug,sku,price));
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
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
                setProductQuantityInCart(userEmail, sku, quantity + 1);
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

    public void setProductQuantityInCart(String userEmail, String sku, int quantity) {

        String sqlCheck = "SELECT quantity FROM Carts WHERE userEmail=? AND sku=?";
        try (PreparedStatement checkStmt = connection.prepareStatement(sqlCheck)) {
            checkStmt.setString(1, userEmail);
            checkStmt.setString(2, sku);
            ResultSet resultSet = checkStmt.executeQuery();

            if (resultSet.next() && quantity > 0) {

                String sqlUpdate = "UPDATE Carts SET quantity=? WHERE userEmail=? AND sku=?";
                try (PreparedStatement updateStmt = connection.prepareStatement(sqlUpdate)) {
                    updateStmt.setInt(1, quantity);
                    updateStmt.setString(2, userEmail);
                    updateStmt.setString(3, sku);
                    updateStmt.executeUpdate();
                }
            } else if (!resultSet.next() && quantity > 0) {

                String sqlInsert = "INSERT INTO Carts(userEmail, sku, quantity) VALUES (?, ?, ?)";
                try (PreparedStatement insertStmt = connection.prepareStatement(sqlInsert)) {
                    insertStmt.setString(1, userEmail);
                    insertStmt.setString(2, sku);
                    insertStmt.setInt(3, quantity);
                    insertStmt.executeUpdate();
                }
            } else if (quantity == 0) {

                removeProductCompletelyFromCart(userEmail, sku);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error setting product quantity in cart.", e);
        }
    }


    public void decreaseProductQuantityInCart(String userEmail, String sku) {
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
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error decreasing product quantity in cart.", e);
        }
    }


    public void removeProductCompletelyFromCart(String userEmail, String sku) {
        if (userEmail == null || userEmail.isEmpty()) {
            throw new IllegalArgumentException("User email must not be null or empty");
        }
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("SKU must not be null or empty");
        }

        String sqlDelete = "DELETE FROM Carts WHERE userEmail=? AND sku=?";
        try (PreparedStatement deleteStmt = connection.prepareStatement(sqlDelete)) {
            deleteStmt.setString(1, userEmail);
            deleteStmt.setString(2, sku);
            int rowsAffected = deleteStmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No product found in cart to remove.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error removing product from cart completely.", e);
        }
    }


    public void downloadProductCatalog() {
        ArrayList<Product> productList = new ArrayList<>();
        String jsonCatalog = "";

        String sqlQuery = "SELECT * FROM Products";

        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery);
             ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("vendor"),
                        resultSet.getString("urlSlug"),
                        resultSet.getString("sku"),
                        resultSet.getDouble("price")
                );
                productList.add(product);
            }

            Gson gson = new Gson();
            jsonCatalog = gson.toJson(productList);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load the product catalog from database", e);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("ProductCatalog.json"))) {
            writer.write(jsonCatalog);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to write the product catalog to file", e);
        }
    }

    public ArrayList<Order> getOrders(String user) {
        if (user == null || user.isEmpty()) {
            throw new IllegalArgumentException("User must not be null or empty");
        }
        String sql = "SELECT * FROM ORDERS WHERE userEmail=?";
        ArrayList<Order> userOrders = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int orderID = resultSet.getInt("orderID");
                String shippingAddress = resultSet.getString("shippingAddress");
                int trackingNumber = resultSet.getInt("trackingNumber");
                boolean isShipped = resultSet.getBoolean("isShipped");
                userOrders.add(new Order(shippingAddress, null, user, orderID, trackingNumber, isShipped));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving Order.", e);
        }
        allOrderByUser.put(user, userOrders);
        return allOrderByUser.get(user);
    }

    public Order getOrder(String user, int id) {
        int orderID = id;
        String shippingAddress = "";
        if (user == null) {
            String sql = "SELECT orderID, shippingAddress FROM ORDERS WHERE orderID=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    orderID = resultSet.getInt("orderID");
                    shippingAddress = resultSet.getString("shippingAddress");
                }
            } catch (Exception e) {
                throw new RuntimeException("Error retrieving Specific Order.", e);
            }
            ArrayList<Order.OrderProductItem> userOrder = new ArrayList<>();
            sql = "SELECT p.*, op.quantity FROM OrderProducts op JOIN Products p ON op.sku = p.sku WHERE op.orderID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Product p = new Product(
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getString("vendor"),
                            resultSet.getString("urlSlug"),
                            resultSet.getString("sku"),
                            resultSet.getDouble("price")
                    );
                    int quantity = resultSet.getInt("quantity");
                    userOrder.add(new Order.OrderProductItem(p, quantity));
                }
            } catch (Exception e) {
                throw new RuntimeException("Error retrieving Specific Order.", e);
            }
        }
        String sql = "SELECT orderID, shippingAddress FROM ORDERS WHERE orderID=? and userEmail=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setString(2, user);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                orderID = resultSet.getInt("orderID");
                shippingAddress = resultSet.getString("shippingAddress");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving Specific Order.", e);
        }
        ArrayList<Order.OrderProductItem> userOrder = new ArrayList<>();
        sql = "SELECT p.*, op.quantity FROM OrderProducts op JOIN Products p ON op.sku = p.sku WHERE op.orderID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Product p = new Product(
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("vendor"),
                        resultSet.getString("urlSlug"),
                        resultSet.getString("sku"),
                        resultSet.getDouble("price")
                );
                int quantity = resultSet.getInt("quantity");
                userOrder.add(new Order.OrderProductItem(p, quantity));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving Specific Order.", e);
        }
        return new Order(shippingAddress, userOrder, user, orderID, 0, false);
    }

    public ArrayList<Order> getAllOrders(){
        String sql = "SELECT * FROM ORDERS";
        ArrayList<Order> allOrders = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int orderID = resultSet.getInt("orderID");
                String shippingAddress = resultSet.getString("shippingAddress");
                String user = resultSet.getString("userEmail");
                int trackingNumber = resultSet.getInt("trackingNumber");
                boolean isShipped = resultSet.getBoolean("isShipped");
                allOrders.add(new Order(shippingAddress, null, user, orderID, trackingNumber, isShipped));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving Order.", e);
        }
        return allOrders;
    }

    public Order createOrder(String userEmail, String shippingAddress) {
        Cart userCart = getCart(userEmail);
        if (userCart == null || userCart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty or does not exist for user: ");
        }
    
        ArrayList<Order.OrderProductItem> orderProductItems = new ArrayList<>();
        for (Cart.CartItem cartItem : userCart.getCartItems()) {
            orderProductItems.add(new Order.OrderProductItem(cartItem.getProduct(), cartItem.getQuantity()));
        }

        Order newOrder = new Order(shippingAddress, orderProductItems, userEmail);
        String sql = "INSERT INTO ORDERS(userEmail, shippingAddress, isShipped) VALUES (?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setInt(1, newOrder.getOrderID());
            statement.setString(1, userEmail);
            statement.setString(2, shippingAddress);
            statement.setBoolean(3, false);
            int rows = statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            throw new RuntimeException("Error creating order");
        }
        int orderID = newOrder.getOrderID();
        String sqlselect = "SELECT orderID FROM ORDERS WHERE userEmail=?";
        try (PreparedStatement statement = connection.prepareStatement(sqlselect)) {
            statement.setString(1, userEmail);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orderID = resultSet.getInt("orderID");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating order for user");
        }

        String sqlOrderProduct = "INSERT INTO OrderProducts (orderID, sku, quantity) VALUES (?, ?, ?)";
            for (Order.OrderProductItem item : orderProductItems) {
                try (PreparedStatement statementOrderProduct = connection.prepareStatement(sqlOrderProduct)) {
                    statementOrderProduct.setInt(1, orderID);
                    statementOrderProduct.setString(2, item.getProduct().getSku());
                    statementOrderProduct.setInt(3, item.getQuantity());
                    statementOrderProduct.executeUpdate();
                } catch (Exception e) {
                    throw new RuntimeException("Error creating order for user");
                }
            }




    
        clearCart(userEmail);
    
//        allOrdersInStore.add(newOrder);
//        allOrderByUser.computeIfAbsent(userEmail, k -> new ArrayList<>()).add(newOrder);
    
        return newOrder;
}   

    private void clearCart(String userEmail) {
        String sql = "DELETE FROM carts WHERE userEmail=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userEmail);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error clearing cart for user: " + userEmail, e);
        }
    }

    public void shipOrder(int orderID, int trackingNumber) {
        String sql = "UPDATE orders SET trackingNumber=?, isShipped=? WHERE orderID=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, trackingNumber);
            statement.setBoolean(2, true);
            statement.setInt(3, orderID);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error shipping order with ID: " + orderID, e);
        }
    }

    public static class ProductAlreadyInCartException extends RuntimeException {
        public ProductAlreadyInCartException(String message) {
            super(message);
        }
    }

    public ArrayList<User> getAllUsers() {
    ArrayList<User> users = new ArrayList<>();
    String sql = "SELECT password, role FROM users";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            User user = new User();
            String password = user.getPassword();
            if (!"guest".equals(password)) {
                user.setPassword(resultSet.getString("password"));
                user.setRole(User.Role.valueOf(resultSet.getString("role")));   
                users.add(user);
            }
        }
    } catch (SQLException e) {
        throw new RuntimeException("Error retrieving users.", e);
    }
    return users;
}

public void ChangePermission(User user, User.Role role) {
    String password = user.getPassword();
    String sql = "UPDATE Users SET Role = ? WHERE password = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setString(1, role.toString());
        statement.setString(2, user.getPassword());
        statement.executeUpdate();
    } catch (SQLException e) {
        throw new RuntimeException("Error updating user role.", e);
    }
}

}