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
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
            displayError(response,HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
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
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
            displayError(response,HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
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
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
            displayError(response,HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
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

        store.removeProductFromCart(userEmail, productToRemove.getSku());
        System.out.println("Processing DELETE request for product slug: " + slug);
        response.setStatus(HttpServletResponse.SC_OK);
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

