package org.soen387;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.soen387.ProductServlet.store;

@WebServlet("/cart/*")
public class CartServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle GET requests to /cart
        String userEmail = (String) request.getSession().getAttribute("loggedInUserEmail");
        if (userEmail == null) {
            Cart userCart = store.getCart("guest");
            request.setAttribute("cart", userCart);
            request.getRequestDispatcher("/cart.jsp").forward(request, response);
            return;
        }
        Cart userCart = store.getCart(userEmail);
        request.setAttribute("cart", userCart);
        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle POST requests to /cart/products/:slug
        String userEmail = (String) request.getSession().getAttribute("loggedInUserEmail");

        if (userEmail == null) {
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

            String methodOverride = request.getParameter("_method");
            if ("delete".equalsIgnoreCase(methodOverride)) {
                doDelete(request, response);
                response.sendRedirect("/storefront/cart");
                return;
            }
            try {
                store.addProductToCart("guest", productToAdd.getSku());
                response.setStatus(HttpServletResponse.SC_OK);
                response.sendRedirect("/storefront/cart");
            } catch (StorefrontFacade.ProductAlreadyInCartException e) {
                displayError(response, HttpServletResponse.SC_BAD_REQUEST, "Product has already been added to the cart");
            } catch (Exception e) {
                displayError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while adding the product to the cart");
            }
            return;
        }

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

        String methodOverride = request.getParameter("_method");
        if ("delete".equalsIgnoreCase(methodOverride)) {
            doDelete(request, response);
            response.sendRedirect("/storefront/cart");
            return;
        }
        try {
            store.addProductToCart(userEmail, productToAdd.getSku());
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect("/storefront/cart");
        } catch (StorefrontFacade.ProductAlreadyInCartException e) {
            displayError(response, HttpServletResponse.SC_BAD_REQUEST, "Product has already been added to the cart");
        } catch (Exception e) {
            displayError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while adding the product to the cart");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle DELETE requests to /cart/products/:slug
        String userEmail = (String) request.getSession().getAttribute("loggedInUserEmail");

        if (userEmail == null) {
            String getPathInfo = request.getPathInfo();
            if (getPathInfo == null || getPathInfo.split("/").length <= 1) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            String slug = getPathInfo.split("/")[2];
            Product productToRemove = store.getProductBySlug(slug);
            if (productToRemove == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                return;
            }
            String removeCompletelyParam = request.getParameter("remove");
            boolean removeCompletely = "true".equalsIgnoreCase(removeCompletelyParam);

            if (removeCompletely) {

                store.removeProductCompletelyFromCart("guest", productToRemove.getSku());
            } else {

                store.decreaseProductQuantityInCart("guest", productToRemove.getSku());
            }
            return;
        }

        String getPathInfo = request.getPathInfo();
        if (getPathInfo == null || getPathInfo.split("/").length <= 1) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String slug = getPathInfo.split("/")[2];
        Product productToRemove = store.getProductBySlug(slug);
        if (productToRemove == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
            return;
        }
        String removeCompletelyParam = request.getParameter("remove");
        boolean removeCompletely = "true".equalsIgnoreCase(removeCompletelyParam);

        if (removeCompletely) {

            store.removeProductCompletelyFromCart(userEmail, productToRemove.getSku());
        } else {

            store.decreaseProductQuantityInCart(userEmail, productToRemove.getSku());
        }
    }
    // In CartServlet
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userEmail = (String) request.getSession().getAttribute("loggedInUserEmail");
        if (userEmail == null) {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.split("/").length <= 2) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            String slug = pathInfo.split("/")[3];
            Product product = store.getProductBySlug(slug);
            if (product == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                return;
            }

            String action = request.getParameter("action");
            if ("increase".equals(action)) {
                store.setProductQuantityInCart("guest", product.getSku(), store.getCart("guest").getQuantityForSKU(product.getSku()) + 1);
            } else {
                store.decreaseProductQuantityInCart("guest",product.getSku());
            }

            response.sendRedirect("/storefront/cart");
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.split("/").length <= 2) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String slug = pathInfo.split("/")[3];
        Product product = store.getProductBySlug(slug);
        if (product == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
            return;
        }

        String action = request.getParameter("action");
        if ("increase".equals(action)) {
            store.setProductQuantityInCart(userEmail, product.getSku(), store.getCart(userEmail).getQuantityForSKU(product.getSku()) + 1);
        } else {
            store.decreaseProductQuantityInCart(userEmail,product.getSku());
        }

        response.sendRedirect("/storefront/cart");
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
        out.println("<a href='/storefront/cart' class='btn btn-secondary'>Return to Cart</a>");
        out.println("</div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}
