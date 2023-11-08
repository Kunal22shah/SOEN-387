<%@ include file="navbar.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Cart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
</head>
<body>
<br/>
<br/>
<br/>
<br/>
<div class="container">
    <h2>Your Orders</h2>
    <table class="table">
        <thead>
        <tr>
            <th>Order ID</th>
            <th>Shipping Address</th>
            <th>Tracking Number</th>
            <th>Shipped</th>
            <th>Details</th>
            <c:if test="${sessionScope.isStaff != null && sessionScope.isStaff}">
                <th>Ship Order</th>
            </c:if>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="order" items="${orders}">
              <tr>
                  <td>${order.orderID}</td>
                  <td>${order.shippingAddress}</td>
                  <td>${order.trackingNumber}</td>
                  <c:choose>
                       <c:when test="${order.isShipped()}">
                            <td>Product has been shipped</td>
                       </c:when>
                  <c:otherwise>
                       <td>Product has not been shipped</td>
                  </c:otherwise>
                  </c:choose>
                  <td><a href="/storefront/orders/${order.orderID}" class="btn btn-primary">View Details</a></td>
                    <c:if test="${sessionScope.isStaff != null && sessionScope.isStaff}">
                        <td>
                            <form action="/storefront/OrderServlet" method="post">
                                <input type="hidden" name="orderId" value="${order.orderID}" />
                                <button type="submit" class="btn btn-primary">Ship Order</button>
                            </form>
                        </td>
                </c:if>
              </tr>
        </tbody>

        </c:forEach>
    </table>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
</body>
</html>
