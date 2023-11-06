<%@ include file="navbar.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Cart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <style>
        .container-custom-margin {
            margin-top: 5rem;
        }
    </style>
</head>
<body>
<div class="container container-custom-margin">
    <h2>Your Cart</h2>
    <table class="table">
        <thead>
        <tr>
            <th>Product Name</th>
            <th>Quantity</th>
            <th>Price</th>
            <th>Total Price for Product</th>
            <th>Remove</th>
        </tr>
        </thead>
        <tbody>
        <c:set var="totalPrice" value="0" />
        <c:forEach var="cartItem" items="${cart.cartItems}">
            <tr>
                <td><a href="/storefront/products/${cartItem.product.urlSlug}">${cartItem.product.name}</a></td>
                <td>${cartItem.quantity}</td>
                <td>${cartItem.product.price}</td>
                <td>${cartItem.product.price * cartItem.quantity}</td>
                <c:set var="totalPrice" value="${totalPrice + (cartItem.product.price * cartItem.quantity)}" />
                <td>
                    <form action="/storefront/cart/products/${cartItem.product.urlSlug}" method="post">
                        <input type="hidden" name="_method" value="delete">
                        <button type="submit" class="btn btn-danger">Remove</button>
                    </form>
                    <form action="OrderServlet" method="post">
                        <button type="submit" class="btn btn-primary">Create Order</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
        <tfoot>
        <tr>
            <td colspan="3" class="text-end"><strong>Total:</strong></td>
            <td>${totalPrice}</td>
            <td></td>
        </tr>
        </tfoot>
    </table>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
</body>
</html>
