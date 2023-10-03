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

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private static StorefrontFacade store = new StorefrontFacade();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle GET requests to /products
        response.setContentType("application/json");

        //For testing purposes, initially there is no products in the store
        //This is a test to see if its shows all the products if we explicitly add products in the store
        //store.createProduct("1","test");
        //store.createProduct("2","test2");
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        ArrayList<Product> allProducts = new ArrayList<Product>();
        allProducts = store.getAllProduct();
        String allProductsjson = gson.toJson(allProducts);
        out.println(allProductsjson);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle POST requests to /products/:slug
    }
}
