<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>StoreFront</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
</head>
<body>
<nav class="navbar navbar-expand-lg bg-primary fixed-top" >
    <div class="container-fluid">
        <a class="navbar-brand" href="/storefront" style="font-weight:bold; color: white;">StoreFront</a>
        <c:choose>

            <c:when test="${sessionScope.isStaff != null && sessionScope.isStaff}">

                <a class="nav-link text-white" aria-current="page" href="/storefront/users">All Users</a>
            </c:when>
            <c:otherwise>
                 <div></div>
            </c:otherwise>
        </c:choose>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item" id="productsLink">
                    <a class="nav-link text-white" aria-current="page" href="/storefront/products">Products</a>
                </li>
                <c:choose>

                    <c:when test="${not (sessionScope.isStaff != null && sessionScope.isStaff)}">

                            <a class="nav-link text-white" href="/storefront/cart">Cart</a>
                    </c:when>
                    <c:otherwise>
                        <div></div>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                     <c:when test="${not empty sessionScope.loggedInUser || sessionScope.isStaff}">
                          <a class="nav-link text-white" aria-current="page" href="/storefront/orders">View Orders</a>
                          <a class="nav-link text-white" aria-current="page" href="/storefront/unclaimedOrders">View Unclaimed Orders</a>
                     </c:when>
                     <c:otherwise>
                          <div></div>
                     </c:otherwise>
                </c:choose>
            </ul>
            <span class="navbar-text">
                <!-- Combined Authentication Links -->
                <c:choose>
                    <c:when test="${not empty sessionScope.loggedInUser}">
                        <!-- User is logged in -->
                        <a class="btn btn-danger" href="/storefront/logout">Log Out (${sessionScope.loggedInUser.password})</a>
                    </c:when>
                    <c:when test="${sessionScope.isStaff}">
                        <!-- Staff is logged in -->
                        <a class="btn btn-danger" href="/storefront/logout">Staff Log Out</a>
                    </c:when>
                    <c:otherwise>
                        <!-- No one is logged in -->
                        <a class="btn btn-light" href="/storefront/auth/login">Login</a>
                        <a class="btn btn-light" href="/storefront/auth/register">Register</a>
                        <!-- <a class="btn btn-light" href="/storefront/auth/staffAuth">Authenticate as Staff</a> -->
                    </c:otherwise>
                </c:choose>
            </span>
        </div>
    </div>
</nav>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
</body>
</html>
