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

        try {
            store.addProductToCart("singleCustomer", productToAdd.getSku());
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect("/cart");
        } catch (StorefrontFacade.ProductAlreadyInCartException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product has already been added to the cart");
        }
    }

        @Override
        protected void doDelete (HttpServletRequest request, HttpServletResponse response)  throws  ServletException, IOException {
            // Handle DELETE requests to /cart/products/:slug

            String getPathInfo = request.getPathInfo();
            if (getPathInfo == null || getPathInfo.split("/").length <= 1) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            String slug = getPathInfo.split("/")[1];
            String user = "defaultUser";
            store.removeProductFromCart(user, slug);
            response.setStatus(HttpServletResponse.SC_OK);

        }
    }

