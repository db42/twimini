<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <script type="text/javascript">
        function starter_script() {
            $.post('/posts.json',
                    null,
                    function(data){
                        var tweets = $(new EJS({
                                    url: '/static/ejs/addTweet.ejs'}).render(data));
                        $('#tweetlist').append(tweets);
                })
        }
        document.getElementById("demo").innerHTML="My First JavaScript";
    </script>
</head>

<body onload="starter_script();">
Hello ${username} <a href="/logout">Logout</a>
<form action="/newpost.json" method="post">
    Tweet: <input type="textarea" name="post"></br>
    <input type="submit" value="addpost">
</form>

<ul id="tweetlist">

</ul>

</body>