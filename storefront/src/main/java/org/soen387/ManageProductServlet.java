package org.soen387;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

import static org.soen387.ProductServlet.store;

@WebServlet("/manageProduct/*")
public class ManageProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Check if the user is logged in as staff
        Boolean isStaff = (Boolean) session.getAttribute("isStaff");
        if (isStaff == null || !isStaff) {
            displayError(response, HttpServletResponse.SC_FORBIDDEN, "You are not authorized to access this page.");
            return;
        }
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
            displayError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid price format");
            return;
        }

        String action = request.getParameter("action");

        if ("create".equals(action)) {
            try {
                Product newProduct = new Product(name, description, vendor, urlSlug, sku, price);
                store.createProduct(newProduct.getSku(), newProduct.getName(), newProduct.getDescription(), newProduct.getVendor(), newProduct.getUrlSlug(), newProduct.getPrice());
                response.sendRedirect("/storefront/products/" + newProduct.getUrlSlug());
            } catch (RuntimeException e) {
                displayError(response, HttpServletResponse.SC_BAD_REQUEST, "Error adding product");
            }
        } else if ("edit".equals(action)) {
            try {
                store.updateProduct(sku, name, description, vendor, urlSlug, price);
                response.sendRedirect("/storefront/products/" + urlSlug);
            } catch (RuntimeException e) {
                displayError(response, HttpServletResponse.SC_BAD_REQUEST, "Error updating product");
            }
        }
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
        out.println("</div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}