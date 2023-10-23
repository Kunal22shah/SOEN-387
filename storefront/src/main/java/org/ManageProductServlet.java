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
        String sku = request.getParameter("sku");
        if (sku != null && !sku.isEmpty()) {
            String sku = request.getParameter("sku");
            Product product = store.getProductBySku(sku); // fetch product by SKU
            request.setAttribute("product", product); // for pre-filling the form
        }
        // For a new product, no attribute is set, so 'product' will be empty in the JSP
        request.getRequestDispatcher("/manageProduct.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
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

        if (sku == null || sku.isEmpty()) {
            try {
                Product newProduct = new Product(name, description, vendor, urlSlug, sku, price);
                store.createProduct(newProduct.getSku(), newProduct.getName());
                response.setStatus(HttpServletResponse.SC_OK);
                // Redirect or forward after successful creation (this is just an example).
                response.sendRedirect("/products/" + urlSlug);
            } catch (RuntimeException e) {
                // Handle creation errors. For example, set error messages in request scope and forward back to form.
                request.setAttribute("error", e.getMessage());
                request.getRequestDispatcher("/manageProduct.jsp").forward(request, response);
            }
        } else {
            try {
                store.updateProduct(sku, name, description, vendor, urlSlug, price);
                // Redirect after successful update.
                response.sendRedirect("/products/" + urlSlug);
            } catch (RuntimeException e) {
                // Handle update errors.
                request.setAttribute("error", e.getMessage());
                request.getRequestDispatcher("/manageProduct.jsp").forward(request, response);
            }
        }
    }
}
