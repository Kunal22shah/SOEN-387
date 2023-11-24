package org.soen387;
 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
 
import static org.soen387.ProductServlet.store;
 
@WebServlet("/users")
public class UsersServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<User> users = store.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserUtility userUtility = new UserUtility();
        String userEmail = request.getParameter("userEmail");
        String role = request.getParameter("role");
        User user = userUtility.getUserByEmail(userEmail);
        if (userEmail != null && !userEmail.isEmpty() && role != null && !role.isEmpty()) {
            try {
                store.ChangePermission(user, User.Role.valueOf(role.toUpperCase()));
                response.sendRedirect("/users");
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
}
 