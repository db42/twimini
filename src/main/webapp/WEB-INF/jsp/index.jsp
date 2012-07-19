<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty sessionScope.userName}">
    <script type="text/javascript">
        window.location = "/todo"
    </script>
</c:if>

<c:if test="${not empty message}">
    ${message}</br>
</c:if>

Login:
<form action="/login" method="post">
    email: <input type="text" name="email"></br>
    password: <input type="password" name="password"></br>
    <input type="submit" value="login">
</form>

<form action="/register" method="post">
    Name: <input type="text" name="name"></br>
    email: <input type="text" name="email"></br>
    password: <input type="password" name="password"></br>
    <input type="submit" value="register">
</form>
