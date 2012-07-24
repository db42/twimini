<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="/static/js/jquery.min.js"type="text/javascript"></script>
    <script src="/static/js/ejs_production.js"type="text/javascript"></script>
    <script src="/static/js/twimini_helper.js"type="text/javascript"></script>
</head>

<body>
Hello ${username} <a href="/logout">Logout</a>
<form action="/posts.json" onsubmit="add_tweet(this); return false;">
    Tweet: <input type="textarea" name="post"></br>
    <input type="submit" value="addpost">
</form>

<h1>Tweets: </h1>
<ul id="tweetlist">
</ul>
<h1>Followers: </h1>
<ul id="followerslist">
</ul>
<h1>Following: </h1>
<ul id="followinglist">
</ul>


</body>
</html>