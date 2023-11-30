package org.soen387;

import org.json.JSONException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.*;


@WebServlet("/auth/*")
public class AuthServlet extends HttpServlet {

    private final UserUtility userUtility = new UserUtility();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if ("/login".equals(pathInfo)) {
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else if ("/register".equals(pathInfo)) {
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } else if ("/changePass".equals(pathInfo)) {
            request.getRequestDispatcher("/changePass.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/staffAuth.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        switch (pathInfo) {
            case "/login":
                handleUserLogin(request, response);
                break;
            case "/register":
                try {
                    handleUserRegistration(request, response);
                } catch (Exception e) {
                    displayError(response,HttpServletResponse.SC_BAD_REQUEST,e.getMessage());
                }
                break;
            case "/staffAuth":
                handleStaffAuthentication(request, response);
                break;
            case "/changePass":
                handleChangePassword(request, response);
                break;
            default:
                displayError(response, HttpServletResponse.SC_NOT_FOUND, "Not Found");
                break;
        }
    }

    private void handleUserLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String password = request.getParameter("password");

        User user = userUtility.getUserByPassword(password);
        if (user != null && Objects.equals(password, user.getPassword())) {
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", user);
            session.setAttribute("loggedInUserEmail", user.getEmail());
            if (User.Role.STAFF.equals(user.getRole())) {
                session.setAttribute("isStaff", true);
            } else {
                session.setAttribute("isStaff", false);
            }
            response.sendRedirect("/storefront/products");
        } else {
            displayError(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid email or password");
        }

    }

    private void handleUserRegistration(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String password = request.getParameter("password");

        if (password == null || password.isEmpty()) {
            displayError(response, HttpServletResponse.SC_BAD_REQUEST, "Password cannot be empty");
            return;
        }
        if(userUtility.isPasscodeTaken(password)){
            throw new Exception("Passcode already taken");
        }

        User newUser = new User();
        newUser.setPassword(password);
        newUser.setUsername(User.generateRandomUsername());
        newUser.setEmail(User.generateRandomEmail());

        // Add user to the database
        userUtility.addUser(newUser);

        response.sendRedirect("/storefront/auth/login");
    }


    private void handleStaffAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String passcode = request.getParameter("passcode");

        if (passcode == null || passcode.trim().isEmpty()) {
            displayError(response, HttpServletResponse.SC_BAD_REQUEST, "Passcode cannot be null or empty.");
            return;
        }

        User user = userUtility.getUserByPassword(passcode);
        if (user != null && User.Role.STAFF.equals(user.getRole())) {
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", user);
            session.setAttribute("loggedInUserEmail", user.getEmail());
            session.setAttribute("isStaff", true);
            response.sendRedirect("/storefront/products");
        } else {
            displayError(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid passcode or not authorized as staff");
        }
    }



    private void handleChangePassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmNewPassword = request.getParameter("confirmNewPassword");

        if (!newPassword.equals(confirmNewPassword)) {
            displayError(response, HttpServletResponse.SC_BAD_REQUEST, "New passwords do not match.");
            return;
        }

        try {
            userUtility.setPasscode(oldPassword, newPassword);
            response.sendRedirect("/storefront/successPasscodeSet.jsp");
        } catch (Exception e) {
            displayError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    private void displayError(HttpServletResponse response, int statusCode, String errorMessage) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css' rel='stylesheet'>");
        out.println("<title>Error</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container mt-5'>");
        out.println("<div class='alert alert-danger' role='alert'>");
        out.println("<h4 class='alert-heading'>Error " + statusCode + "</h4>");
        out.println("<p>" + errorMessage + "</p>");
        out.println("</div>");
        out.println("<div class='text-center'>");
        out.println("<a href='/storefront' class='btn btn-primary'>Home</a>");
        out.println("<a href='/storefront/auth/login' class='btn btn-secondary'>Return to Authentication</a>");
        out.println("</div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}
