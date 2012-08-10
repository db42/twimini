<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="/static/css/topbar.css" type="text/css"/>
		<link rel="stylesheet" href="/static/css/profile_page.css" type="text/css"/>
        <link rel="stylesheet" href="/static/css/tabs.css" type="text/css"/>
        <script src="/static/js/jquery.min.js"type="text/javascript"></script>
        <script src="/static/js/ejs_production.js"type="text/javascript"></script>
        <title>Profile - Twimini</title>
	</head>
	<body>
		<%@include file="topbar.jsp" %>

        <%@include file="error_msg.jsp" %>
		<div class="page-content">
			<%@include file="profile_block.jsp" %>

			<div class="user-content">
                <div class="left-container">
                    <ol class="left-part">
                        <li class="link-to-tab"> <a href="#tweets"> Tweets </a></li>
                        <li class="link-to-tab"> <a href="#followers"> Followers </a></li>
                        <li class="link-to-tab"> <a href="#following"> Following </a></li>
                    </ol>
                </div>
				<div class="right-part">
					<a class="right-container" id="tweets">
						<div class="top-message"> Tweets </div>
                        <%@include file="container_message.jsp" %>
                        <div class="tweetlist">
                        </div>
					</a>
					<a class="right-container" id="followers">
						<div class="top-message"> Followers </div>
						<div class="followerlist">
						</div>
					</a>
					<a class="right-container" id="following">
						<div class="top-message"> Following </div>
						<div class="followinglist">
					    </div>
					</a>
				</div>
			</div>

		</div>
        <script src="/static/js/activatetable.js" type="text/javascript"></script>
        <script src="/static/js/format_date.js"type="text/javascript"></script>
        <script src="/static/js/helper.js" type="text/javascript"></script>
        <script src="/static/js/profile_helper.js" type="text/javascript"></script>
	</body>
</html>

