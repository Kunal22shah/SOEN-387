package org.soen387;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/products/*")
public class ProductServlet extends HttpServlet {

    private static StorefrontFacade store = new StorefrontFacade();

    //Initialization method used for testing purposes
    //This will create products in the store to test the GET request
    //This method will only be executed once
    public void init() throws ServletException{
        super.init();
        store.createProduct("1","test");
        store.createProduct("2","test2");
        store.updateProduct("2",  "test2", "test description", "test", "test-product", 25.99);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle GET requests to /products/*
        response.setContentType("application/json");

        //GET path info from request (anything after "/products")
        String getPathInfo = request.getPathInfo();

        Gson gson = new Gson();
        PrintWriter out = response.getWriter();

        //Handle the GET /products request
        if (getPathInfo == null){
            ArrayList<Product> allProducts = new ArrayList<Product>();
            allProducts = store.getAllProduct();
            String allProductsjson = gson.toJson(allProducts);
            out.println(allProductsjson);
            return;
        }

        //Handle the GET /products/:slug request
        //Extract slug form "/:slug"
        String getRequestSlug = getPathInfo.split("/")[1];

        String singleProduct = gson.toJson(store.getProductBySlug(getRequestSlug));
        out.println(singleProduct);

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
