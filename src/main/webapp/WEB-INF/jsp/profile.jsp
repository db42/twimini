<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="/static/js/jquery.min.js"type="text/javascript"></script>
    <script src="/static/js/ejs_production.js"type="text/javascript"></script>
    <script type="text/javascript">
        function starter_script() {
            $.get('/twimini/posts.json',
                function(data){
                    jQuery.each(data, function(){
                        var tweets = $(new EJS({
                            url: '/static/ejs/addTweet.ejs'}).render(this));
                    $('#tweetlist').append(tweets);
                    })

            })
            $.get('/twimini/followers.json',
                function(data){
                    jQuery.each(data, function(){
                        var follower = $(new EJS({
                            url: '/static/ejs/addUser.ejs'}).render(this));
                    $('#followerslist').append(follower);
                    })

            })
            $.get('/twimini/followings.json',
                function(data){
                    jQuery.each(data, function(){
                        var following = $(new EJS({
                            url: '/static/ejs/addUser.ejs'}).render(this));
                    $('#followinglist').append(following);
                    })

            })
        }

        function add_tweet(form){
            $.post('/twimini/newpost.json',$(form).serialize(), function(data){
                var tweet = $(new EJS({
                    url: '/static/ejs/addTweet.ejs'}).render(data));
                $('#tweetlist').append(tweet);
            });
        }
    </script>
</head>

<body onload="starter_script();">
Hello ${username} <a href="/logout">Logout</a>
<form action="/twimini/newpost.json" onsubmit="add_tweet(this); return false;">
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