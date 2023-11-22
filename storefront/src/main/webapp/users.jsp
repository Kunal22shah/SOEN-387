<%@ include file="navbar.jsp" %>
<%@ page import="org.soen387.User" %>
<%@ page import="java.util.ArrayList" %>
 
<!DOCTYPE html>
<html>
<head>
    <title>User List</title>
</head>
<body>
    <h1>User List</h1>
    <table>
        <tr>
            <th>Password</th>
        </tr>
        <% ArrayList<User> users = (ArrayList<User>) request.getAttribute("users");
            for (User user : users) { %>
             <tr>
                <td><%= user.getPassword() %></td>
             </tr>
         <% } %>
    </table>
</body>
</html>
 
 