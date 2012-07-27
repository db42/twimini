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
        <script src="/static/js/profile_helper.js"type="text/javascript"></script>
		<title>Profile - Twimini</title>
	</head>
	<body>
		<div class="topbar-permanant">
			<div class="container">
				<div class = "left-pull">
					<div class="topbar-button home-button">
						Home
					</div>
				</div>

				<div class="icon-position">
					<i class="twitter-icon-embossed"></i>
				</div>

				<div class = "right-pull">
					<div class="topbar-button new-tweet-button">
						ne
					</div>
					<div class="topbar-button profile-button">
						pr
					</div>
					<form class="search-box" action="/search">
						<span class="search-icon">
						</span>
						<input class="search-input" type="text" name="q" placeholder=" Search..." autocomplete="off" dir="ltr"/>
					</form>
				</div>
			</div>
		</div>

		<div class="page-content">
			<div class="profile-block">
				<div class="left-pull">
					<div class="profile-pic"> </div>
					<div class="username">Hello, ${username} <a href="/logout">Logout</a></div>
				</div>
				<div class="right-pull">
					<div class="profile-details">details</div>
				</div>
			</div>

			<div class="user-content">
                <%--<div class="left-part">
                    <form action="" onsubmit="add_tweet(this); return false;">
                        <input type="text" class="addpost-input" name="post" placeholder="Tweet" autocomplete="off" dir="ltr"></br>
                        <input type="submit" value="addpost" class="addpost-btn">
                    </form>
                </div>--%>
				<div class="left-part">
					<div class="link-to-tab"> <a href="#tweets"> Tweets -></a></div>
					<div class="link-to-tab"> <a href="#followers"> Followers -> </a></div>
					<div class="link-to-tab"> <a href="#following"> Following -> </a></div>
                </div>
				<div class="right-part">
					<a class="tweet-container" id= "tweets">
						<div class="top-message"> Tweets </div>
                        <div class="tweetlist">
                        </div>
					</a>
					<a class="followers-container" id="followers">
						<div class="top-message"> Followers </div>
						<div class="followerslist">
						</div>
					</a>
					<a class="following-container" id="following">
						<div class="top-message"> Following </div>
						<div class="followinglist">
					    </div>
					</a>
				</div>
			</div>

		</div>

	</body>
</html>

