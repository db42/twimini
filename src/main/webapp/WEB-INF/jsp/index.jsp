<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html xmlns="http://www.w3.org/1999/xhtml">
    <c:if test="${not empty sessionScope.userName}">
        <script type="text/javascript">
            window.location = "/todo"
        </script>
    </c:if>

    <c:if test="${not empty status}">
        ${status}</br>
    </c:if>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="/static/css/topbar.css" type="text/css"/>
		<link rel="stylesheet" href="/static/css/login_page.css" type="text/css"/>
        <script src="/static/js/jquery.min.js"type="text/javascript"></script>
		<script src="/static/js/helper.js" type="text/javascript"></script>
		<title>Login - Twimini</title>
	</head>
	<body>
		<div class="topbar-permanant">
			<div class="container">
				<i class="twitter-icon-embossed"></i>
			</div>
		</div>

        <%@include file="error_msg.jsp" %>

		<div class="front-box">
			<div class="tweet-img">

			</div>
			<div class="ask-box login-box">
				<form class="login-form" action="" onsubmit="user_login(this); return false;" method="post">
				    <input class="form-input" type="text" name="email" autocomplete="on" title="Email" placeholder="Username or Email"> <br/>
				    <input class="form-input" type="password" name="password" placeholder="Password"><br/>
				    <input class="form-submit-btn login-btn" type="submit" value="Login">
				</form>
			</div>
			<div class="ask-box register-box">
				<div class="register-message"><strong>New to Twitter?</strong> Register</div>
				<form class="register-form" action="" onsubmit="user_register(this); return false;" method="post">
				    <input class="form-input" type="text" maxlength="20" name="name" placeholder="Username"><br/>
				    <input class="form-input" type="text" name="email" autocomplete="on" placeholder="Email"><br/>
				    <input class="form-input" type="password" name="password" placeholder="Password"><br/>
				    <input class="form-submit-btn sign-up" type="submit" value="Register">
				</form>
			</div>
		</div>
	</body>
</html>

