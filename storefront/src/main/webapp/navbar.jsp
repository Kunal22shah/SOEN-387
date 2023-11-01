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
<nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container-fluid">
        <a class="navbar-brand" href="/storefront" style="font-weight:bold;">StoreFront</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link active" aria-current="page" href="/storefront/products"><button type="button" class="btn btn-outline-primary">Products</button></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/storefront/cart"><button type="button" class="btn btn-outline-primary">Cart</button></a>
                </li>
            </ul>
            <span class="navbar-text">
                <c:choose>
                    <c:when test="${not empty sessionScope.currentUser}">
                        <a class="nav-link" href="/storefront/auth/logout">
                            <button type="button" class="btn btn-danger">Log Out (${sessionScope.currentUser.username})</button>
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a class="nav-link" href="/storefront/auth/login">
                            <button type="button" class="btn btn-primary">Login</button>
                        </a>
                        <a class="nav-link" href="/storefront/auth/register">
                            <button type="button" class="btn btn-secondary">Register</button>
                        </a>
                    </c:otherwise>
                </c:choose>
                <c:if test="${sessionScope.isStaff}">
                    <a class="nav-link" href="/storefront/auth/logout">
                        <button type="button" class="btn btn-danger">Staff Log Out</button>
                    </a>
                </c:if>
                <c:if test="${not sessionScope.isStaff}">
                    <a class="nav-link" href="/storefront/auth/staffAuth">
                        <button type="button" class="btn btn-primary">Authenticate as Staff</button>
                    </a>
                </c:if>
            </span>
        </div>
    </div>
</nav>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
</body>
</html>
