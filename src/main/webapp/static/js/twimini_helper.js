function starter_script() {
    $.get('/posts',
        function(data){
            jQuery.each(data, function(){
                var tweets = $(new EJS({
                    url: '/static/ejs/addTweet.ejs'}).render(this));
            $('#tweetlist').append(tweets);
            })

    })
    $.get('/followers',
        function(data){
            jQuery.each(data, function(){
                var follower = $(new EJS({
                    url: '/static/ejs/addUser.ejs'}).render(this));
            $('#followerslist').append(follower);
            })

    })
    $.get('/followings',
        function(data){
            jQuery.each(data, function(){
                var following = $(new EJS({
                    url: '/static/ejs/addUser.ejs'}).render(this));
            $('#followinglist').append(following);
            })

    })
}

function add_tweet(form){
    $.post('/posts',$(form).serialize(), function(data){
        var tweet = $(new EJS({
            url: '/static/ejs/addTweet.ejs'}).render(data));
        $('#tweetlist').append(tweet);
    });
}

$(window).load(starter_script);