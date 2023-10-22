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
import java.util.ArrayList;

@WebServlet("/products/*")
public class ProductServlet extends HttpServlet {

    protected static final StorefrontFacade store = new StorefrontFacade();

    //Initialization method used for testing purposes
    //This will create products in the store to test the GET request
    //This method will only be executed once
    public void init() throws ServletException{
        super.init();
        store.createProduct("1","test");
        store.updateProduct("1",  "Airpods Pro 2nd Generation", "Airpods that was barely used. Was worn only twice", "Micheal Smith", "airpods-pro-2ndgen", 249.99);
        store.createProduct("2","test2");
        store.updateProduct("2",  "2021 Macbook Air M1", "Used Macbook Air in good condition. Comes with charger and case", "Steven Bala", "macbook-air-2021", 999.99);
        store.createProduct("3","test2");
        store.updateProduct("3",  "2020 Day One edition PS5", "PS5 that was bought on launch day", "Rodrigo Guy", "ps5-dayone", 499.99);
        store.createProduct("4","test2");
        store.updateProduct("4",  "Gucci Shoes", "Gucci shoes. Good condition", "Rob Wayne", "gucci-shoes", 299.99);
        store.createProduct("5","test2");
        store.updateProduct("5",  "Gaming chair", "Gaming chair. Not in good condition but selling it for cheap. Negotiable", "Max Bobby", "gaming-chair", 30.00);
        store.createProduct("6","test2");
        store.updateProduct("6",  "Gaming PC", "Gaming PC I built back in 2020. Parts are still good and the PC is very customizable", "Jeffrey Kai", "gaming-pc", 550.00);
        store.createProduct("7","test2");
        store.updateProduct("7",  "IPhone 14 Pro Max", "IPhone 14 Pro Max Unlocked. Everything works fine and in good condition. Provided with charging outlet", "Angelo Mo", "iphone14-promax", 1200.00);
        store.createProduct("8","test2");
        store.updateProduct("8",  "Samsung Smart Watch", "Used Samsung Smart Watch i bought in 2021. In very good condition", "Rod Mike", "samsung-watch-2021", 100.00);
    }

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
            ArrayList<Product> allProducts;
            allProducts = store.getAllProduct();
            request.setAttribute("products",allProducts);
            RequestDispatcher rd = request.getRequestDispatcher("products.jsp");
            rd.forward(request, response );
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        //Handle the GET /products/:slug request
        //Extract slug form "/:slug"
        String getRequestSlug = getPathInfo.split("/")[1];

        try {
            Product singleProduct = store.getProductBySlug(getRequestSlug);
            request.setAttribute("product",singleProduct);
            RequestDispatcher rd = request.getRequestDispatcher("/product.jsp");
            rd.forward(request, response );
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        catch (RuntimeException e){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<h1 style='text-align:center;'>Error Status Code 404: NOT FOUND!! </h1>");
            out.println("<div style='text-align:center;'><a href='/storefront'><button>Home</button></a></div>");
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
    
}
