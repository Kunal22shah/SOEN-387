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
<div class="container mt-5">
    <h2>Your Cart</h2>
    <table class="table">
        <thead>
        <tr>
            <th>Product Name</th>
            <th>Price</th>
            <th>Remove</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="product" items="${cart.products}">
            <tr>
                <td><a href="/storefront/products/${product.urlSlug}">${product.name}</a></td>
                <td>${product.price}</td>
                <td>
                    <form action="/storefront/cart/products/${product.urlSlug}" method="post">
                        <input type="hidden" name="_method" value="delete">
                        <button type="submit" class="btn btn-danger">Remove</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
</body>
</html>
