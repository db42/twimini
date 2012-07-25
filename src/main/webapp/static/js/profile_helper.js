function starter_script() {
    $.get('/posts',
        function(data){
            jQuery.each(data, function(){
                append_entity('tweetlist','addTweet.ejs',this);
            })

    })
    $.get('/followers',
        function(data){
            jQuery.each(data, function(){
                append_entity('followerslist','addUser.ejs',this);
            })

    })
    $.get('/followings',
        function(data){
            jQuery.each(data, function(){
                append_entity('followinglist','addUser.ejs',this);
            })
    })
}

function append_entity(listName, ejsName, data) {
    var entity = $(new EJS({
        url:'/static/ejs/'+ejsName}).render(data));
    $('#'+listName).append(entity);
}

function add_tweet(form){
    $.post('/posts',$(form).serialize(), function(data){
        append_entity('tweetlist','addTweet.ejs',data);
    });
}

$(window).load(starter_script);