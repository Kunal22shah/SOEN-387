package org.soen387;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserUtility {

    private final Path FILE_PATH;

    public UserUtility() {
        String fileName = "credentials.json";
        String dataDirectory = "data";


        String projectRootPath = System.getProperty("user.dir");

        FILE_PATH = Paths.get(projectRootPath, dataDirectory, fileName);
        System.out.println("Credentials file path is: " + FILE_PATH);

        try {
            if (Files.notExists(FILE_PATH)) {
                Files.createDirectories(FILE_PATH.getParent());
                Files.createFile(FILE_PATH);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error creating or accessing the credentials file", e);
        }
    }

    public User getUserByEmail(String email) {
        Connection connection = DatabaseConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users WHERE email = ?");
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addUser(User user) {
        Connection connection = DatabaseConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Users (username, email, password,role) VALUES (?, ?, ?,?)");
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isPasscodeTaken(String passcode) {
        // Check if the passcode is already used by any user
        Connection connection = DatabaseConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM Users WHERE password = ?");
            statement.setString(1, passcode);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setPasscode(String oldPassword, String newPassword) throws Exception {
        User user = getUserByPassword(oldPassword);
        if (user == null) {
            throw new Exception("Incorrect old password.");
        }
        if(isPasscodeTaken(newPassword)){
            throw new Exception("Passcode already taken");
        }

        Connection connection = DatabaseConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE Users SET password = ? WHERE password = ?");
            statement.setString(1, newPassword);
            statement.setString(2, oldPassword);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new Exception("Password update failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Database error.");
        }
    }


    public User getUserByPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }

        Connection connection = DatabaseConnection.getConnection();
        if (connection == null) {
            throw new IllegalStateException("Database connection is not available.");
        }

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users WHERE password = ?");
            statement.setString(1, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(User.Role.valueOf(resultSet.getString("Role").toUpperCase()));
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving user by password.", e);
        }
    }

}
