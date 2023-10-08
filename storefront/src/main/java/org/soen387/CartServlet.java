package org.soen387;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CartServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle GET requests to /cart
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle POST requests to /cart/products/:slug

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || !pathInfo.startsWith("/products/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product slug");
            return;
        }

        String slug = pathInfo.split("/")[2];
        Product productToAdd = store.getProductBySlug(slug);
        if (productToAdd == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
            return;
        }

        store.addProductToCart("singleCustomer", productToAdd.getSku());
        response.sendRedirect("/cart");
    }
}
