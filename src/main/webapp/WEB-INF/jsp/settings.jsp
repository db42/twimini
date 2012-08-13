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
		<%@include file="topbar.jsp" %>

        <%@include file="error_msg.jsp" %>

		<div class="page-content">
			<%@include file="profile_block.jsp" %>

			<div class="user-content">
                <div class="left-container">
                <ol class="left-part">
					<li class="link-to-tab"> <a href="#account"> Account </a></li>
					<li class="link-to-tab"> <a href="#profile"> Profile  </a></li>
					<li class="link-to-tab"> <a href="#password"> Password  </a></li>
                </ol>
                </div>
				<div class="right-part">
					<a class="tab-container" id="account">
						<div class="top-message"> Account </div>
						<form class="top-balance" onsubmit="account_change(this); return false;">
	                        <div class="form-block">
	                        	<div class="in-text"> User Name:</div>
	                        	<div class = "right-top">
                                    <input type="text" class="right-pull pr-input" name="username" placeholder="UserName" autocomplete="off" dir="ltr">
                                    <br/>
                                    <div class="error-settings" id="username-error"> User Name is incorrect! </div>
                                </div>
                        	</div>
	                        <div class="form-block">
	                        	<div class="in-text">Email: </div>
                                <div class = "right-top">
                                    <input type="text" class="right-pull pr-input" name="email" placeholder="Email" autocomplete="off" dir="ltr">
                                    <br/>
                                    <div class="error-settings" id="email-error"> Email is incorrect! </div>
                                </div>
                        	</div>
	                        <input type="submit" value="Save Changes" class="setting-btn">
	                    </form>
					</a>
					<a class="tab-container" id="profile">
						<div class="top-message"> Profile </div>
						<form class="top-balance" onsubmit="profile_change(this); return false;">
							<div class="form-block">
	                    	    <div class="in-text"> Name: </div>
                                <div class = "right-top">
                                    <input type="text" class="right-pull pr-input" name="name" placeholder="Name" autocomplete="off" dir="ltr">
                                    <br/>
                                    <div class="error-settings" id="name-error"> Name exceeds (limit=250)! </div>
                                </div>
	                        </div>
		                    <div class="form-block">
		                        <div class="in-text"> Description: </div>
                                <div class = "right-top">
                                    <input type="text" class="right-pull pr-input" name="description" placeholder="Description" autocomplete="off" dir="ltr">
                                    <br/>
                                    <div class="error-settings" id="description-error"> description exceeds (limit=250)! </div>
                                </div>
	                        </div>
	                        <input type="submit" value="Save Changes" class="setting-btn">
	                    </form>
					</a>
					<a class="tab-container" id="password">
						<div class="top-message"> Password </div>
						<form action="" onsubmit="password_change(this); return false;">
							<div class="form-block">
	                    	    <div class="in-text"> Old Password: </div>
		                        <input type="password" class="right-pull pr-input" name="old_password" placeholder="Old" autocomplete="off" dir="ltr">
	                        </div>
		                    <div class="form-block">
		                        <div class="in-text"> New Password: </div>
		                        <input type="password" class="right-pull pr-input" name="new_password" placeholder="New" autocomplete="off" dir="ltr">
	                        </div>
	                        <div class="form-block">
		                        <div class="in-text"> Retype Password: </div>
		                        <div class = "right-top">
                                    <input type="password" class="right-pull pr-input" name="red_password" placeholder="Re-type" autocomplete="off" dir="ltr">
                                    <br/>
                                    <div class="error-settings" id="password-error"> Password doesn't match </div>
                                </div>
	                        </div>
	                        <input type="submit" value="Change Password" class="setting-btn">
	                    </form>
					</a>
				</div>
			</div>

		</div>
        <script src="/static/js/activatetable.js" type="text/javascript"></script>
        <script src="/static/js/helper.js" type="text/javascript"></script>
        <script src="/static/js/settings_helper.js" type="text/javascript"></script>
	</body>
</html>

