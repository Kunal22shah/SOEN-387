package org.soen387;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.soen387.ProductServlet.store;

@WebServlet("/cart/*")
public class CartServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle GET requests to /cart
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle POST requests to /cart/products/:slug
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        // Handle DELETE requests to /cart/products/:slug

        String getPathInfo = request.getPathInfo();
        if (getPathInfo == null || getPathInfo.split("/").length <= 1) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String slug = getPathInfo.split("/")[1];
        String user = "defaultUser";
        store.removeProductFromCart(user, slug);
//        response.setStatus(HttpServletResponse.SC_OK);

    }
}
