<%@ include file="navbar.jsp" %>

<!DOCTYPE HTML>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Bootstrap demo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
  </head>
  <body>
    <h1 style="text-align:center;">All products in Storefront</h1>
    <br/>
    <div style="display:flex; flex-wrap:wrap; margin-left:50px; gap:80px;">
        <div class="card" style="width: 18rem;">
          <img src="https://imageio.forbes.com/specials-images/imageserve/65034bdac9e8c78c1728ff0b/Apple-iPhone-15-Pro-lineup-natural-titanium-geo/960x0.jpg?format=jpg&width=960" class="card-img-top" alt="productimage"   >
          <div class="card-body">
            <h5 class="card-title">IPhone 15 Pro Max 128 GB</h5>
            <p class="card-text">The brand new 2023 Smartphone from Apple.</p>
            <p class="card-text" style="text-align:center">1199.99$</p>
            <div style="display:flex; justify-content:center;">
                <a href="#" class="btn btn-primary">View Details</a>
            </div>
          </div>
        </div
      </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
  </body>
</html>