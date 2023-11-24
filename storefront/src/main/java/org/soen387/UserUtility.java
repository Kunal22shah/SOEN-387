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
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Users (username, email, password) VALUES (?, ?, ?)");
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
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
        if(oldPassword.equals("secret")){
            throw new Exception("Staff Passcode cannot be changed");
        }
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
        Connection connection = DatabaseConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users WHERE password = ?");
            statement.setString(1, password);
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
}
