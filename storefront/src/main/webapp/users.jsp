<%@ include file="navbar.jsp" %>
<%@ page import="org.soen387.User" %>
<%@ page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html>
<head>
    <title>User List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .container-custom-margin {
            margin-top: 5rem;
        }
    </style>
</head>
<body>
<div class="container container-custom-margin">
    <h1>User List</h1>
    <table class="table">
        <thead>
            <tr>
                <th>Password</th>
                <th>Role</th>
                <th>Change Role</th>
            </tr>
        </thead>
        <tbody>
            <% ArrayList<User> users = (ArrayList<User>) request.getAttribute("users");
                for (User user : users) { %>
                    <tr>
                        <td><%= user.getPassword() %></td>
                        <td><%= user.getRole() %></td>
                        <td>
                            <form action="/storefront/users" method="post">
                                <input type="hidden" name="password" value="<%= user.getPassword() %>" />
                                <select name="newRole" class="form-select">
                                    <option value="CUSTOMER">Customer</option>
                                    <option value="STAFF">Staff</option>
                                </select>
                                <button type="submit" class="btn btn-primary mt-2">Change Role</button>
                            </form>
                        </td>
                    </tr>
            <% } %>
        </tbody>
    </table>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
</body>
</html>