package org.soen387;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.http.HttpSession;

@WebServlet("/manageProduct")
public class ManageProductServlet extends HttpServlet {

    protected static final StorefrontFacade store = new StorefrontFacade();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Boolean isStaff = (session != null) ? (Boolean) session.getAttribute("isStaff") : Boolean.FALSE;

        if (!Boolean.TRUE.equals(isStaff)) {
            response.sendRedirect("/staffAuth");
            return;
        }

        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            String sku = request.getParameter("sku");
            Product product = store.getProductBySku(sku); // fetch product by SKU
            request.setAttribute("product", product); // for pre-filling the form
        }
        // Forward to the form page
        request.getRequestDispatcher("/manageProduct.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Boolean isStaff = (session != null) ? (Boolean) session.getAttribute("isStaff") : Boolean.FALSE;

        if (!Boolean.TRUE.equals(isStaff)) {
            // If the user is not authenticated, redirect to the login page
            response.sendRedirect("/staffAuth");
            return;
        }
        
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String vendor = request.getParameter("vendor");
        String urlSlug = request.getParameter("urlSlug");
        String sku = request.getParameter("sku");
        double price;
        try {
            price = Double.parseDouble(request.getParameter("price"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid price format");
            return;
        }

        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            store.updateProduct(sku, name, description, vendor, urlSlug, price); 
            response.sendRedirect("/products/" + urlSlug); 
        } else {
            try {
            Product newProduct = new Product(name, description, vendor, urlSlug, sku, price);
            store.createProduct(newProduct.getSku(), newProduct.getName());
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect("/products/" + urlSlug);
            } catch (RuntimeException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }
        }
    }
}