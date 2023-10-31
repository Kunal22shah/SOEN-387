package org.soen387;

import com.google.gson.Gson;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

@WebServlet("/products/*")
public class ProductServlet extends HttpServlet {

    protected static final StorefrontFacade store = new StorefrontFacade();
    public static String DB_URL = "jdbc:mysql://localhost:3306/storefront";
    public static String USER = "root";
    public static String PASS = "dbuser";
    Connection con = null;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //GET path info from request (anything after "/products")
        String getPathInfo = request.getPathInfo();

        if("/download".equals((getPathInfo))){
            try {
                store.downloadProductCatalog();

                response.setContentType("application/json");
                response.setHeader("Content-Disposition", "attachment; filename=ProductCatalog.json");

                try (ServletOutputStream outputStream = response.getOutputStream();
                     FileInputStream fileInputStream = new FileInputStream("ProductCatalog.json")) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Download successful");
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error occurred while downloading the product catalog: " + e.getMessage());
            }
            return;
        }
        // Handle GET requests to /products/*

        /* Handle the GET /products request */
        if (getPathInfo == null){
            ArrayList<Product> Products = new ArrayList<>();
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                Statement stmt = conn.createStatement();
                String Sql = "SELECT * FROM Products";
                ResultSet rs = stmt.executeQuery(Sql);
                while(rs.next()){
                    String name = rs.getString("name");
                    String vendor = rs.getString("vendor");
                    String urlSlug = rs.getString("urlSlug");
                    String sku = rs.getString("sku");
                    String description = rs.getString("description");
                    double price = rs.getDouble("price");
                    Products.add(new Product(name,description,vendor,urlSlug,sku,price));
                }
            }
            catch(SQLException e) {
                System.out.println(e.getMessage());
            }
            catch(ClassNotFoundException c){
                c.printStackTrace();
            }
            request.setAttribute("products",Products);
            RequestDispatcher rd = request.getRequestDispatcher("products.jsp");
            rd.forward(request, response );
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        //Handle the GET /products/:slug request
        //Extract slug form "/:slug"
        String getRequestSlug = getPathInfo.split("/")[1];

        try {
            Product singleProduct = new Product();
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM PRODUCTS WHERE urlSlug = ?");
                stmt.setString(1,getRequestSlug);
                ResultSet rs = stmt.executeQuery();
                while(rs.next()){
                    String name = rs.getString("name");
                    String vendor = rs.getString("vendor");
                    String urlSlug = rs.getString("urlSlug");
                    String sku = rs.getString("sku");
                    String description = rs.getString("description");
                    double price = rs.getDouble("price");
                    singleProduct = new Product(name,description,vendor,urlSlug,sku,price);
                }
            }
            catch(SQLException e) {
                System.out.println(e.getMessage());
            }
            catch(ClassNotFoundException c){
                c.printStackTrace();
            }
            request.setAttribute("product",singleProduct);
            RequestDispatcher rd = request.getRequestDispatcher("/product.jsp");
            rd.forward(request, response );
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        catch (RuntimeException e) {
            displayError(response, HttpServletResponse.SC_NOT_FOUND, "Product not found");
        }

    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle POST requests to /products/:slug

        // Extract slug from the path
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product slug");
            return;
        }

        String slug = pathInfo.split("/")[1];
        Product existingProduct = store.getProductBySlug(slug);
        if (existingProduct == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
            return;
        }

        // Extract product details from the request
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String vendor = request.getParameter("vendor");
        String urlSlug = request.getParameter("urlSlug");
        double price;
        try {
            price = Double.parseDouble(request.getParameter("price"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid price format");
            return;
        }

        try {
        // Update the product
        store.updateProduct(existingProduct.getSku(), name, description, vendor, urlSlug, price);
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect("/products/" + urlSlug);
        } catch (RuntimeException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
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
        out.println("<a href='/storefront/products' class='btn btn-secondary'>Return to Products</a>");
        out.println("</div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
    
}
