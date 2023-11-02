package org.soen387;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class UserUtility {

    private final Path FILE_PATH;

    public UserUtility() {
        try {
            FILE_PATH = Paths.get(getClass().getClassLoader().getResource("credentials.json").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Error initializing path to credentials.json", e);
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

    // Write user credentials to the JSON file
    public void writeToFile(String email, String password) throws JSONException {
        Map<String, String> credentialsMap = readFromFile();
        credentialsMap.put(email, password);

        // Convert Map to JSONObject
        JSONObject credentials = new JSONObject(credentialsMap);

        try (FileWriter file = new FileWriter(FILE_PATH.toFile())) {
            System.out.println(FILE_PATH.toAbsolutePath());
            file.write(credentials.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read all user credentials from the JSON file and return as a map
    public Map<String, String> readFromFile() {
        Map<String, String> credentialsMap = new HashMap<>();

        try {
            String content = new String(Files.readAllBytes((FILE_PATH)));
            System.out.println((FILE_PATH).toAbsolutePath());
            JSONObject credentials = new JSONObject(new JSONTokener(content));

            Iterator<String> keys = credentials.keys();
            while (keys.hasNext()) {
                String email = keys.next();
                credentialsMap.put(email, credentials.getString(email));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return credentialsMap;
    }
}
