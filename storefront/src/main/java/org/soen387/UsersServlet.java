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
        request.getRequestDispatcher("/path/to/users.jsp").forward(request, response);
    }
}
 