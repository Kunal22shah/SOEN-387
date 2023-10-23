package org.soen387;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.http.HttpSession;

@WebServlet("/staffAuth/*")
public class AuthServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher("/staffAuth.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String passcode = request.getParameter("passcode");

        if ("secret".equals(passcode)) {
            HttpSession session = request.getSession();
            session.setAttribute("isStaff", true);
            response.sendRedirect("/storefront/products");
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid passcode");
        }
    }
}
