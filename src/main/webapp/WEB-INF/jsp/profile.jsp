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
        }
    </script>
</head>

<body onload="starter_script();">
Hello ${username} <a href="/logout">Logout</a>
<form action="/twimini/newpost.json" method="post">
    Tweet: <input type="textarea" name="post"></br>
    <input type="submit" value="addpost">
</form>

<ul id="tweetlist">

</ul>

</body>
</html>