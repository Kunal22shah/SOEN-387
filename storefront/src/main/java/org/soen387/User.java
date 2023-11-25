package org.soen387;

import java.util.Random;
import java.util.UUID;

public class User {


    public enum Role {
        STAFF, CUSTOMER
    }

    private String username;
    private String password;
    private String email;
    private Role role;

    public User() {
        this.role = Role.CUSTOMER;
    }

    public static String generateRandomUsername() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String combinedChars = letters + numbers;
        Random random = new Random();
        StringBuilder sb = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            sb.append(combinedChars.charAt(random.nextInt(combinedChars.length())));
        }

        return sb.toString();
    }

    public static String generateRandomEmail() {
        return "user" + UUID.randomUUID() + "@storefront.com";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
