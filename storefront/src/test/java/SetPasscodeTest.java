import org.junit.jupiter.api.*;
import org.soen387.DatabaseConnection;
import org.soen387.User;
import org.soen387.UserUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SetPasscodeTest {

    private UserUtility userUtility;
    private Connection connection;
    private String testUsername = "testUser";
    private String testEmail = "SetPasscodeTest@storefront.com";
    private String testPassword = "testPassword123";
    private User.Role testRole = User.Role.CUSTOMER;
    private String newPassword = "newPassword123";

    @BeforeAll
    public void setUpClass() {
        userUtility = new UserUtility();
        connection = DatabaseConnection.getConnection();
        try {
            // Add user to the database for testing
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO Users (username, email, password, role) VALUES (?, ?, ?, ?)");
            statement.setString(1, testUsername);
            statement.setString(2, testEmail);
            statement.setString(3, testPassword);
            statement.setString(4, testRole.toString());
            statement.executeUpdate();

            if (!connection.getAutoCommit()) {
                connection.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error setting up the database for tests.");
        }
    }

    @AfterAll
    public void tearDownClass() {
        // Remove the user from the database after tests are complete
        removeUserFromDatabase(newPassword);
    }

    @Test
    public void testSetPasscodeWithCorrectOldPassword() {
        // Attempt to set a new password using the correct old password
        Assertions.assertDoesNotThrow(() -> userUtility.setPasscode(testPassword, newPassword));
    }

    @Test
    public void testSetPasscodeWithIncorrectOldPassword() {
        // Attempt to set a new password using an incorrect old password
        Exception exception = Assertions.assertThrows(Exception.class, () -> userUtility.setPasscode("wrongPassword", newPassword));
        Assertions.assertTrue(exception.getMessage().contains("Incorrect old password"));
    }

    private void removeUserFromDatabase(String newPassword) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM Users WHERE password = ?");
            statement.setString(1, newPassword);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
