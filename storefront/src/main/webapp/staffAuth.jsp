<%@ include file="navbar.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Staff Authentication</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
</head>
<body>

<!-- Staff Authentication Modal -->
<div class="modal fade show d-block" id="staffAuthModal" tabindex="-1" aria-labelledby="staffAuthModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title" id="staffAuthModalLabel">Authenticate as Staff</h5>
            </div>
            <div class="modal-body">
                <form action="/storefront/auth/staffAuth" method="post">
                    <div class="mb-3">
                        <label for="passcode" class="form-label">Enter Passcode</label>
                        <input type="password" class="form-control" id="passcode" name="passcode" required placeholder="Enter your passcode">
                    </div>
                    <div class="modal-footer">

                        <button type="submit" class="btn btn-primary">Authenticate</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
</body>
</html>
