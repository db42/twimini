<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="/static/css/topbar.css" type="text/css"/>
		<link rel="stylesheet" href="/static/css/profile_page.css" type="text/css"/>
        <link rel="stylesheet" href="/static/css/tabs.css" type="text/css"/>
        <link rel="stylesheet" href="/static/css/edit_profile.css" type="text/css"/>
        <script src="/static/js/jquery.min.js"type="text/javascript"></script>
        <script src="/static/js/ejs_production.js"type="text/javascript"></script>
        <title>Settings - Twimini</title>
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
                    <li class="topbar-button new-tweet-button">
                        ne
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
				<div class="left-pull">
					<div class="profile-pic"> </div>
					<div class="username">Hello, ${username} <a href="/logout">Logout</a></div>
				</div>
				<div class="right-pull">
					<div class="profile-details">details</div>
				</div>
			</div>

			<div class="user-content">
                <div class="left-container">
                <ol class="left-part">
					<li class="link-to-tab"> <a href="#account"> Account -></a></li>
					<li class="link-to-tab"> <a href="#profile"> Profile -> </a></li>
					<li class="link-to-tab"> <a href="#password"> Password -> </a></li>
                </ol>
                </div>
				<div class="right-part">
					<a class="right-container" id="account">
						<div class="top-message"> Account </div>
						<form action="" onsubmit="account_change(this); return false;">
	                        <div class="form-block">
	                        	<div class="in-text"> User Name:</div>
	                        	<input type="text" class="addpost-input right-pull pr-input" name="username" placeholder="UserName" autocomplete="off" dir="ltr">
                        	</div>
	                        <div class="form-block">
	                        	<div class="in-text">Email: </div>
	                        	<input type="text" class="addpost-input right-pull pr-input" name="email" placeholder="Email" autocomplete="off" dir="ltr">
                        	</div>
	                        <input type="submit" value="Save Changes" class="addpost-btn">
	                    </form>
					</a>
					<a class="right-container" id="profile">
						<div class="top-message"> Profile </div>
						<form action="" onsubmit="profile_change(this); return false;">
							<div class="form-block">
	                    	    <div class="in-text"> Name: </div>
		                        <input type="text" class="addpost-input right-pull pr-input" name="name" placeholder="Name" autocomplete="off" dir="ltr"></br>
	                        </div>
		                    <div class="form-block">
		                        <div class="in-text"> Description: </div>
		                        <input type="text" class="addpost-input right-pull pr-input" name="description" placeholder="Description" autocomplete="off" dir="ltr"></br>
	                        </div>
	                        <input type="submit" value="Save Changes" class="addpost-btn">
	                    </form>
					</a>
					<a class="right-container" id="password">
						<div class="top-message"> Password </div>
						<form action="" onsubmit="password_change(this); return false;">
							<div class="form-block">
	                    	    <div class="in-text"> Old Password: </div>
		                        <input type="password" class="addpost-input right-pull pr-input" name="old-pass" placeholder="Old" autocomplete="off" dir="ltr"></br>
	                        </div>
		                    <div class="form-block">
		                        <div class="in-text"> New Password: </div>
		                        <input type="password" class="addpost-input right-pull pr-input" name="new-pass" placeholder="New" autocomplete="off" dir="ltr"></br>
	                        </div>
	                        <div class="form-block">
		                        <div class="in-text"> Retype Password: </div>
		                        <input type="password" class="addpost-input right-pull pr-input" name="red-pass" placeholder="Re-type" autocomplete="off" dir="ltr"></br>
	                        </div>
	                        <input type="submit" value="Change Password" class="addpost-btn">
	                    </form>
					</a>
				</div>
			</div>

		</div>
        <script src="/static/js/activatetable.js" type="text/javascript"></script>
        <script src="/static/js/settings_helper.js" type="text/javascript"></script>
	</body>
</html>

