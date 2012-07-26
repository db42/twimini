<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="/static/js/jquery.min.js"type="text/javascript"></script>
    <script src="/static/js/ejs_production.js"type="text/javascript"></script>
    <script src="/static/js/profile_helper.js"type="text/javascript"></script>
</head>

<body>
Hello ${username} <a href="/logout">Logout</a>
<form action="" onsubmit="add_tweet(this); return false;">
    Tweet: <input type="text" name="post"></br>
    <input type="submit" value="addpost">
</form>

<h1>Tweets: </h1>
<div id="main-list">
</div>
<h1>Followers: </h1>
<ul id="followerslist">
</ul>
<h1>Following: </h1>
<ul id="followinglist">
</ul>


</body>
</html>