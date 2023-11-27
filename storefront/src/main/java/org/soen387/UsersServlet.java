package org.soen387;
 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.ArrayList;
 
import static org.soen387.ProductServlet.store;
 
@WebServlet("/users")
public class UsersServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        // Check if the user is logged in as staff
        Boolean isStaff = (Boolean) session.getAttribute("isStaff");
        if (isStaff == null || !isStaff) {
            displayError(response, HttpServletResponse.SC_FORBIDDEN, "You are not authorized to access this page.");
            return;
        }
        ArrayList<User> users = store.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserUtility userUtility = new UserUtility();
        // String userEmail = request.getParameter("userEmail");
        // String role = request.getParameter("role");
        // User user = userUtility.getUserByEmail(userEmail);
        String password = request.getParameter("password");
        String role = request.getParameter("newRole");
        User user = userUtility.getUserByPassword(password);
        if (password != null && !password.isEmpty() && role != null && !role.isEmpty()) {
            try {
                store.ChangePermission(user, User.Role.valueOf(role.toUpperCase()));
                response.sendRedirect("/storefront/users");
            } catch (IllegalArgumentException e) {
                request.setAttribute("error", "Invalid role: " + role);
                doGet(request, response);
            } catch (RuntimeException e) {
                request.setAttribute("error", "Error updating user role");
                doGet(request, response);
            }
        } else {
            request.setAttribute("error", "User email and role are required");
            doGet(request, response);
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
        out.println("</div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}
 