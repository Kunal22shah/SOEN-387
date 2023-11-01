package org.soen387;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class UserUtility {

    private static final String USER_FILE = "users.json";
    private static final Gson gson = new Gson();
    private final Map<String, User> users;

    public UserUtility() {
        this.users = UserUtility.loadUsers();
    }

    public static Map<String, User> loadUsers() {
        try (FileReader reader = new FileReader(USER_FILE)) {
            Type type = new TypeToken<HashMap<String, User>>() {
            }.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public static void saveUsers(Map<String, User> users) {
        try (FileWriter writer = new FileWriter(USER_FILE)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getUserByUsername(String username) {
        return users.get(username);
    }

    public User getUserByEmail(String email) {
        return users.values().stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst()
                .orElse(null);
    }

    public void addUser(User user) {
        users.put(user.getUsername(), user);
        UserUtility.saveUsers(users);
    }
}
