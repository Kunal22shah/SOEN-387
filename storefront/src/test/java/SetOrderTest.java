import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.soen387.StorefrontFacade;
import org.junit.jupiter.api.Assertions.*;

import java.sql.*;

public class SetOrderTest {

    private static final String URL = "jdbc:sqlite:C:/root/soen387.db";
    private static Connection connection = null;

    private static int orderID = 0;
    private static String userEmail = "";
    private static String shippingAddress = "";

    private static boolean isShipped = false;

    @BeforeAll
    public static void setUp() throws Exception {
        //Set up db connection
        if (connection == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(URL);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        //Insert dummy data for test
        insert();
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        //Close db connection and remove dummy data
        remove();
        connection.close();
    }

    //Insert dummy data method
    public static void insert(){
        String sql = "INSERT INTO ORDERS(userEmail, shippingAddress, isShipped) VALUES (?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "guest");
            statement.setString(2, "UnitTest");
            statement.setBoolean(3, false);
            int rows = statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            throw new RuntimeException("Error creating order");
        }

        sql = "SELECT * FROM ORDERS WHERE shippingAddress=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "UnitTest");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                orderID = resultSet.getInt("orderID");
                userEmail = resultSet.getString("userEmail");
                shippingAddress = resultSet.getString("shippingAddress");
                isShipped = resultSet.getBoolean("isShipped");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving Specific Order.", e);
        }
    }

    //Remove dummy data method
    public static void remove(){
        String sql = "DELETE FROM ORDERS WHERE userEmail=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "UnitTest");
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error clearing order: " + "UnitTest", e);
        }
    }


    @Test
    public void SetOrder(){
        StorefrontFacade store = new StorefrontFacade();
        store.setOrderOwner(orderID,"UnitTest");
        String dbemail = "";
        String dbShippingAddress = "";
        boolean dbShipped = false;


        String sql = "SELECT * FROM ORDERS ORDER BY orderID DESC LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                //Get content from db
                dbemail = resultSet.getString("userEmail");
                dbShippingAddress = resultSet.getString("shippingAddress");
                dbShipped = resultSet.getBoolean("isShipped");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving Specific Order.", e);
        }
        //Make sure the order was set to a user and the other columns were not changed
        Assertions.assertEquals("UnitTest", dbemail);
        Assertions.assertEquals(shippingAddress, dbShippingAddress);
        Assertions.assertEquals(isShipped,dbShipped);

    }

}
