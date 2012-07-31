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
		<div class="topbar-permanant">
            <div class="container">
                <ul class = "left-pull">
                    <li class="topbar-button home-button">
                        <a href="/twimini/home"> Home </a>
                    </li>
                </ul>

                <div class="icon-position">
                    <i class="twitter-icon-embossed"></i>
                </div>

                <ul class = "right-pull">
                    <li class="topbar-button">
                        <a onclick="focus_new_tweet();" class="new-tweet-button"></a>
                    </li>
                    <li class="topbar-button" id="nav">
                        <ul >
                            <li class="profile-button">
                                <ul>
                                    <li>
                                        <a href="/twimini/profile">Profile</a>
                                    </li>
                                    <li>
                                        <a href="/twimini/settings">Settings</a>
                                    </li>
                                    <li>
                                        <a href="/logout">Logout</a>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </li>
                    <form class="search-box" action="/search">
                        <span class="search-icon">
                        </span>
                        <input class="search-input" type="text" name="q" placeholder=" Search..." autocomplete="off" dir="ltr"/>
                    </form>
                </ul>
            </div>
		</div>

		<div class="page-content">
			<div class="profile-block">

			</div>

			<div class="user-content">
                <div class="left-container">
                <div class="left-part">
                    <form action="" onsubmit="add_tweet(this); return false;" class="addpost-form">
                        <input type="text" class="addpost-input" name="post" placeholder="Tweet" autocomplete="off" dir="ltr"></br>
                        <input type="submit" value="Tweet" class="addpost-btn">
                    </form>
                </div>
				<ol class="left-part">
					<li class="link-to-tab"> <a href="#tweets"> Tweets -></a></li>
					<li class="link-to-tab"> <a href="#followers"> Followers -> </a></li>
					<li class="link-to-tab"> <a href="#following"> Following -> </a></li>
                </ol>
                </div>
				<div class="right-part">
					<a class="right-container" id="tweets">
						<div class="top-message"> Tweets </div>
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
        <script src="/static/js/helper.js" type="text/javascript"></script>
        <script src="/static/js/profile_helper.js" type="text/javascript"></script>
	</body>
</html>

