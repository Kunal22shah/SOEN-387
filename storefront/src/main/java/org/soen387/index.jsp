<!DOCTYPE HTML>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Bootstrap demo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
  </head>
  <body>
    <nav class="navbar navbar-expand-lg bg-body-tertiary">
      <div class="container-fluid">
        <a class="navbar-brand" href="#">Storefront</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item">
              <a class="nav-link active" aria-current="page" href="#"><button type="button" class="btn btn-outline-primary">Products</button></a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#"><button type="button" class="btn btn-outline-primary">Cart</button></a>
            </li>
          </ul>
          <span class="navbar-text">
               <button type="button" class="btn btn-primary">Autenticate as Staff</button>
          </span>
        </div>
      </div>
    </nav>
    <div id="carouselExampleCaptions" class="carousel slide">
      <div class="carousel-indicators">
        <button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
      </div>
      <div class="carousel-inner">
        <div class="carousel-item active">
          <img src="https://9to5mac.com/wp-content/uploads/sites/6/2021/04/iPhone-12-purple-wallpaper.jpg?quality=82&strip=all" class="d-block w-100" alt="techproducts" style="height:650px;">
          <div class="carousel-caption d-none d-md-block">
            <h5>Shop tech products</h5>
            <p>The latest tech products here at Storefront!</p>
          </div>
        </div>

    </div>
    <br/>
    <h1 style="text-align:center;"> Products coming soon to Storefront</h1>
    <br/>
    <div style="display:flex; flex-wrap:wrap; margin-left:50px; gap:80px;">
        <div class="card" style="width: 18rem;">
          <img src="https://imageio.forbes.com/specials-images/imageserve/65034bdac9e8c78c1728ff0b/Apple-iPhone-15-Pro-lineup-natural-titanium-geo/960x0.jpg?format=jpg&width=960" class="card-img-top" alt="productimage"   >
          <div class="card-body">
            <h5 class="card-title">IPhone 15 Pro Max 128 GB</h5>
            <p class="card-text">The brand new 2023 Smartphone from Apple.</p>
            <div style="display:flex; justify-content:center;">
                <a href="#" class="btn btn-primary">Available Soon</a>
            </div>
          </div>
        </div>
        <div class="card" style="width: 18rem;">
          <img src="https://assets.xboxservices.com/assets/ff/0f/ff0f7631-df48-4ff6-868a-45cd0dd47edd.png?n=247813_Image-0_Buy-Box-4_780x615.png" class="card-img-top" alt="xboximage" >
          <div class="card-body">
            <h5 class="card-title">XBOX Series S 1 TB</h5>
            <p class="card-text">The brand new XBOX Series S. Game like never before with one of the best consoles in the market with XBOX Game Pass Ultimate. Access upto 500 games</p>
            <div style="display:flex; justify-content:center;">
                 <a href="#" class="btn btn-primary">Available Soon</a>
            </div>
           </div>
        </div>
        <div class="card" style="width: 18rem;">
             <img src="https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/MQTR3?wid=1377&hei=2057&fmt=jpeg&qlt=95&.v=1687660671097" class="card-img-top" alt="beatsimage" style="height:250px;">
             <div class="card-body">
               <h5 class="card-title">2023 Beats Studio Pro</h5>
               <p class="card-text">The new 2023 Beats Studio Pro has released. Listen to your music with comfort and one of the best sound quality in the market</p>
               <div style="display:flex; justify-content:center;">
                   <a href="#" class="btn btn-primary">Available Soon</a>
               </div>
             </div>
           </div>
           <div class="card" style="width: 18rem;">
             <img src="https://imagineonline.store/cdn/shop/files/iPhone_15_Plus_Blue_PDP_Image_Position-1__en-IN_9dc80be8-e874-4f4b-b74c-fe5a6f40696c.jpg?v=1694607660" class="card-img-top" alt="macimage" >
             <div class="card-body">
               <h5 class="card-title">IPhone 15 Plus 128 GB</h5>
               <p class="card-text">The brand new 2023 Smartphone from Apple.</p>
               <div style="display:flex; justify-content:center;">
                    <a href="#" class="btn btn-primary">Available Soon</a>
                </div>
           </div>
         </div>
        </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
  </body>
</html>