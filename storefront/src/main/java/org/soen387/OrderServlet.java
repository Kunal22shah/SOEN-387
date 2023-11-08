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

        if (userEmail != null && !userEmail.isEmpty() && getPathInfo == null){
            try{
                ArrayList<Order> userOrders = new ArrayList<>();
                if (isStaff != null && isStaff.equals(true)) {
                    userOrders = store.getAllOrders(); // Assuming you have a getAllOrders method
                } else {
                    userOrders = store.getOrders(userEmail);
                }
                request.setAttribute("orders",userOrders);
                request.getRequestDispatcher("orders.jsp").forward(request,response);
                response.setStatus(HttpServletResponse.SC_OK);
                return;

            }
            catch (RuntimeException e){
                displayError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching orders");

            }
        }
        else if (userEmail != null && !userEmail.isEmpty() && getPathInfo != null){
            String getOrderID = getPathInfo.split("/")[1];
            Order userOrder;
            userOrder = store.getOrder(userEmail, Integer.parseInt(getOrderID));
            request.setAttribute("order", userOrder);
            request.getRequestDispatcher("/order.jsp").forward(request,response);
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        else {
            displayError(response, HttpServletResponse.SC_UNAUTHORIZED, "You are not autenticated");
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = request.getParameter("user");
        String shippingAddress = request.getParameter("shippingAddress");
        String pathInfo = request.getPathInfo();

        store.createOrder(user, shippingAddress);

        if ("/shipOrder".equals(pathInfo)) {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        store.shipOrder(orderId);
        response.sendRedirect("/storefront/orders");
        }
    }
}
