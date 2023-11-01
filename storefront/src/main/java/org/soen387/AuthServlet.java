package org.soen387;

import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

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
        } else {
            request.getRequestDispatcher("/staffAuth.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        System.out.println(pathInfo);

        if ("/login".equals(pathInfo)) {
            handleUserLogin(request, response);
        } else if ("/register".equals(pathInfo)) {
            handleUserRegistration(request, response);
        } else {
            handleStaffAuthentication(request, response);
        }
    }

    private void handleUserLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = userUtility.getUserByEmail(email);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", user);
            response.sendRedirect("/storefront/products");
        } else {
            displayError(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid email or password");
        }
    }
    private void handleUserRegistration(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        if (username == null || password == null || email == null || username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            displayError(response, HttpServletResponse.SC_BAD_REQUEST, "Username, password, and email cannot be empty");
            return;
        }

        User existingUserByEmail = userUtility.getUserByEmail(email);
        if (existingUserByEmail != null) {
            displayError(response, HttpServletResponse.SC_CONFLICT, "A user with this email already exists");
            return;
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        newUser.setEmail(email);

        userUtility.addUser(newUser);

        response.sendRedirect("/storefront/auth/login");
    }

    private void handleStaffAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String passcode = request.getParameter("passcode");

        if ("secret".equals(passcode)) {
            HttpSession session = request.getSession();
            session.setAttribute("isStaff", true);
            response.sendRedirect("/storefront/products");
        } else {
            displayError(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid passcode");
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
        out.println("<a href='/storefront/staffAuth' class='btn btn-secondary'>Return to Authentication</a>");
        out.println("</div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}
