<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="/static/css/topbar.css" type="text/css"/>
		<link rel="stylesheet" href="/static/css/profile_page.css" type="text/css"/>
		<link rel="stylesheet" href="/static/css/profile_block.css" type="text/css"/>
        <script src="/static/js/jquery.min.js"type="text/javascript"></script>
        <script src="/static/js/ejs_production.js"type="text/javascript"></script>
        <script src="/static/js/format_date.js"type="text/javascript"></script>
        <script src="/static/js/helper.js"type="text/javascript"></script>
        <script src="/static/js/follow_button.js"type="text/javascript"></script>
        <script src="/static/js/search_helper.js"type="text/javascript"></script>
        <title>Search - Twimini</title>
	</head>
	<body>
		<%@include file="topbar.jsp" %>

        <%@include file="error_msg.jsp" %>

		<div class="page-content">
			<div class="user-content">
                <div class="left-container">
                    <div class="left-part">
                        <div class="addpost-form">
                            <textarea id="tweet-text" class="addpost-input" rows="2" cols="20" placeholder="Tweet..." dir="ltr"></textarea>
                            <span id="tweet-length" class="lcolor">140</span>
                            <button id="addpost-btn">Tweet</button>
                        </div>
                    </div>
                </div>
				<div class="right-part">
					<a class="right-container" id="tweets">
						<div class="top-message">Search</div>
                        <div class="resultlist"></div>
					</a>
				</div>
			</div>
		</div>
	</body>
</html>

