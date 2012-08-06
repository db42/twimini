<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="/static/css/topbar.css" type="text/css"/>
		<link rel="stylesheet" href="/static/css/profile_page.css" type="text/css"/>
        <script src="/static/js/jquery.min.js"type="text/javascript"></script>
        <script src="/static/js/ejs_production.js"type="text/javascript"></script>
        <script src="/static/js/helper.js"type="text/javascript"></script>
        <script src="/static/js/home_helper.js"type="text/javascript"></script>
        <title>Profile - Twimini</title>
	</head>
	<body>
		<%@include file="topbar.jsp" %>

        <%@include file="error_msg.jsp" %>

		<div class="page-content">
			<div class="user-content">
                <div class="left-container">
                    <div class="left-part">
                        <form action="" onsubmit="add_tweet(this); return false;" class="addpost-form">
                            <input type="text" class="addpost-input" name="post" placeholder="Tweet" autocomplete="off" dir="ltr"></br>
                            <input type="submit" value="Tweet" class="addpost-btn">
                        </form>
                    </div>
                </div>
				<div class="right-part">
					<a class="right-container" id="tweets">
						<div class="top-message"> Feed </div>
                        <%@include file="container_message.jsp" %>
                        <div class="tweetlist">
                        </div>
					</a>
				</div>
			</div>
		</div>
	</body>
</html>

