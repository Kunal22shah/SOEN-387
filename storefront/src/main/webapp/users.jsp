<%@ include file="navbar.jsp" %>
<%@ page import="org.soen387.User" %>
<%@ page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html>
<head>
    <title>User List</title>
    <style>
        body {
            margin: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid black;
            padding: 8px;
            text-align: left;
        }
    </style>
</head>
<body>
    <h1>User List</h1>
    <table>
        <tr>
            <th>Password</th>
            <th>Role</th>
            <th>Change Role</th>
        </tr>
        <% ArrayList<User> users = (ArrayList<User>) request.getAttribute("users");
            for (User user : users) { %>
             <tr>
                <td><%= user.getPassword() %></td>
                <td><%= user.getRole()%></td>
                <td>
                    <form action="/storefront/users" method="post">
                        <input type="hidden" name="userId" value="<%= user.getEmail() %>" />
                        <select name="newRole">
                            <option value="CUSTOMER">Customer</option>
                            <option value="STAFF">Staff</option>
                        </select>
                        <input type="submit" value="Change Role" />
                    </form>
                </td>
             </tr>
         <% } %>
    </table>
</body>
</html>