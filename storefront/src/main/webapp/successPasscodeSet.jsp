<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="navbar.jsp" %>
<html>
<head>
    <title>Passcode Set Successfully</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    <style>
        .container-custom-margin {
            margin-top: 5rem;
        }
    </style>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>

<div class="container container-custom-margin">
    <h2>Passcode Set Successfully</h2>
    <p>Your passcode has been set successfully.</p>

    <div class="mt-4">
        <a href="/storefront" class="btn btn-primary">Go to Homepage</a>
        <a href="/storefront/auth/login" class="btn btn-secondary">Login</a>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
