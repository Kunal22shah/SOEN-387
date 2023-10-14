<%@ include file="navbar.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>View Product</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
  </head>
  <body>
<div class="container px-4 text-center" >
  <div class="row gx-5">
    <div class="col">
     <div class="p-3" >
        <img src="https://pngimg.com/d/box_PNG21.png" alt="productimage" style="margin:auto; margin-top:100px; height:300px;"/>
     </div>
    </div>
    <div class="col">
      <div class="p-3" style="margin:auto; margin-top:100px;">
        <h1>${product.name}</h1>
        <p style="font-weight:bold">Price: ${product.price} $</p>
        <p>
             Description: ${product.description}
        </p>
        <p>Vendor: ${product.vendor}</p>
        <button type="button" class="btn btn-primary">Add to cart</button>
    </div>
  </div>
</div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
  </body>
</html>