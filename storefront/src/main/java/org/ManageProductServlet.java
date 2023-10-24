package org.soen387;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.http.HttpSession;

import static org.soen387.ProductServlet.store;


@WebServlet("/manageProduct/*")
public class ManageProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        // Check if pathInfo is not null and has more than one segment
        if (pathInfo != null && pathInfo.split("/").length > 1) {
            String sku = pathInfo.split("/")[1];

            // If SKU is provided, fetch the product by SKU
            if (sku != null && !sku.isEmpty()) {
                Product product = store.getProduct(sku);
                request.setAttribute("product", product); // for pre-filling the form
            }
        }
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

        String action = request.getParameter("action");
        System.out.println(action);

        if ("create".equals(action)) {
            Product newProduct = null;
            try {
                newProduct = new Product(name, description, vendor, urlSlug, sku, price);
                store.createProduct(newProduct.getSku(), newProduct.getName(), newProduct.getDescription(), newProduct.getVendor(), newProduct.getUrlSlug(), newProduct.getPrice());
                System.out.println("New Product added: " + newProduct.getSku() + newProduct.getName());
                response.sendRedirect("/storefront/products/" + newProduct.getUrlSlug());
            } catch (RuntimeException e) {
                request.setAttribute("error", e.getMessage());
                request.getRequestDispatcher("/manageProduct.jsp").forward(request, response);
            }
        } else if ("edit".equals(action)) {
            try {
                store.updateProduct(sku, name, description, vendor, urlSlug, price);
                // Redirect after successful update.
                response.sendRedirect("/storefront/products/" + urlSlug);
            } catch (RuntimeException e) {
                // Handle update errors.
                request.setAttribute("error", e.getMessage());
                request.getRequestDispatcher("/manageProduct.jsp").forward(request, response);
            }
        }
    }
}