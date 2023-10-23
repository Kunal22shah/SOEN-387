<%@ include file="navbar.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>View Products</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
  </head>
  <body>
    <h1 style="text-align:center;">View All products in StoreFront</h1>
    <br/>
    <c:if test="${sessionScope.isStaff != null && sessionScope.isStaff}">
      <div style="text-align:center; margin-bottom:20px;">
        <a href="/storefront/manageProduct" class="btn btn-success">+ Create New Product</a>
      </div>
    </c:if>
    <div style="display:flex; flex-wrap:wrap; margin-left:50px; gap:80px">
    <c:forEach items="${products}" var="product">
        <div class="card" style="width: 18rem;">
          <img src="https://pngimg.com/d/box_PNG21.png" class="card-img-top" alt="productimage"   >
          <div class="card-body">
            <h5 class="card-title">${product.name}</h5>
            <p class="card-text" style="text-align:center">Seller: ${product.vendor}</p>
            <p class="card-text" style="text-align:center; font-weight:bold">Price: ${product.price} $</p>
            <div style="display:flex; justify-content:center;">
                <a href="/storefront/products/${product.urlSlug}" class="btn btn-primary">View Details</a>
                <c:if test="${sessionScope.isStaff != null && sessionScope.isStaff}">
                  <a href="/storefront/manageProduct?sku=${product.sku}" class="btn btn-secondary">Edit</a>
                </c:if>
            </div>
          </div>
        </div
      </div>
    </c:forEach>
      </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
  </body>
</html>