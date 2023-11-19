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
    <h2>Unclaimed Orders in StoreFront</h2>
    <p>If you have made a purchase as a guest or in store. Please enter Order ID to claim your order.</p>
    <table class="table">
        <thead>
        <tr>
            <th>Shipping Address</th>
            <th>Tracking Number</th>
            <th>Shipped</th>
            <th>Details</th>
            <th>Claim Order</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="unclaimedOrders" items="${unclaimedOrders}">
              <tr>
                  <td>${unclaimedOrders.shippingAddress}</td>
                  <td>${unclaimedOrders.trackingNumber}</td>
                  <c:choose>
                       <c:when test="${unclaimedOrders.isShipped()}">
                            <td>Product has been shipped</td>
                       </c:when>
                  <c:otherwise>
                       <td>Product has not been shipped</td>
                  </c:otherwise>
                  </c:choose>
                  <td><a href="/storefront/orders/${unclaimedOrders.orderID}" class="btn btn-primary">View Details</a></td>
                      <td>
                              <form action="/storefront/unclaimedOrders" method="post">
                                  <input type="hidden" name="orderId" value="${unclaimedOrders.orderID}" />
                                  <input type="number" name="userOrderId" placeholder="Enter Order ID" required="required"/>
                                  <button type="submit" class="btn btn-primary">Claim Order Order</button>
                              </form>

                      </td>

              </tr>
        </tbody>
        </c:forEach>
    </table>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
</body>
</html>
