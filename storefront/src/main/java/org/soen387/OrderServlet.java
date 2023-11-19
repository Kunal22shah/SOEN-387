package org.soen387;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

@WebServlet("/orders/*")
public class OrderServlet extends HttpServlet {

    protected static final StorefrontFacade store = new StorefrontFacade();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String getPathInfo = request.getPathInfo();
        HttpSession session = request.getSession();

        // Check if the user is logged in
        String userEmail = (String) session.getAttribute("loggedInUserEmail");
        Boolean isStaff = (Boolean) session.getAttribute("isStaff");
        if (isStaff != null && isStaff.equals(true) && getPathInfo==null) {
            try {
                ArrayList<Order> userOrders = new ArrayList<>();
                userOrders = store.getAllOrders();
                request.setAttribute("orders", userOrders);
                request.getRequestDispatcher("orders.jsp").forward(request, response);
                response.setStatus(HttpServletResponse.SC_OK);
                return;

            } catch (RuntimeException e) {
                displayError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching orders");

            }
        }

        if (userEmail != null && !userEmail.isEmpty() && getPathInfo == null) {
            try {
                ArrayList<Order> userOrders = new ArrayList<>();
                    userOrders = store.getOrders(userEmail);
                request.setAttribute("orders", userOrders);
                request.getRequestDispatcher("orders.jsp").forward(request, response);
                response.setStatus(HttpServletResponse.SC_OK);
                return;

            } catch (RuntimeException e) {
                displayError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching orders");

            }
        } else if (userEmail != null && !userEmail.isEmpty() && getPathInfo != null) {
            getUserOrder(request, response, getPathInfo, userEmail);
            return;
        }else if(userEmail == null && isStaff!=null && isStaff.equals(true)){
            getUserOrder(request, response, getPathInfo, userEmail);
        }
        else {
            displayError(response, HttpServletResponse.SC_UNAUTHORIZED, "You are not autenticated");
        }
    }
    private void getUserOrder(HttpServletRequest request, HttpServletResponse response, String getPathInfo, String userEmail) throws ServletException, IOException {
        String getOrderID = getPathInfo.split("/")[1];
        System.out.println(getOrderID);
        Order userOrder;
        userOrder = store.getOrder(userEmail, Integer.parseInt(getOrderID));
        request.setAttribute("order", userOrder);
        request.getRequestDispatcher("/order.jsp").forward(request, response);
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
        out.println("</div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        System.out.println("Path Info: " + pathInfo);

        // Ship order operation
        if ("/shipOrder".equals(pathInfo)) {
            String orderIdString = request.getParameter("orderId");
            int trackingNumber = Integer.parseInt(request.getParameter("trackingNumber"));
            System.out.println(trackingNumber);
            if (orderIdString != null && !orderIdString.isEmpty()) {
                try {
                    int orderId = Integer.parseInt(orderIdString);

                    store.shipOrder(orderId,trackingNumber);
                    response.sendRedirect("/storefront/orders");
                } catch (NumberFormatException e) {
                    displayError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid order ID");
                } catch (RuntimeException e) {
                    displayError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error shipping order");
                }
            } else {
                displayError(response, HttpServletResponse.SC_BAD_REQUEST, "Order ID is required");
            }
        }
        // Create order operation
        else if ("/createOrder".equals(pathInfo)) { // Or some other path you designate for creating orders
            String userEmail = (String) request.getSession().getAttribute("loggedInUserEmail");
            String shippingAddress = request.getParameter("shippingAddress");

            if (userEmail == null || userEmail.isEmpty()) {
                // Attempt to create a new order
                try {
                    store.createOrder("guest", shippingAddress);
                    response.sendRedirect("/storefront/cart");
                } catch (RuntimeException e) {
                    displayError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error creating order");
                }
                return;
            }

            // Attempt to create a new order
            try {
                store.createOrder(userEmail, shippingAddress);
                response.sendRedirect("/storefront/orders");
            } catch (RuntimeException e) {
                displayError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error creating order");
            }
        }

        else {
            displayError(response, HttpServletResponse.SC_NOT_FOUND, "Not Found");
        }
    }

}
