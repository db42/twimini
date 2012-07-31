function getProfileUserid(){
    words = location.pathname.split('/')
    return words[words.length - 1]
}

function add_user_info(userID){
    $.get('/users/'+userID, function(data){
        var entity = $(new EJS({url:'/static/ejs/UserInfo.ejs'}).render(data));
        $('.profile-block').append(entity);
    });
}

$(function(){
    activatables('page', ['tweets', 'followers', 'following']);
    userID = getProfileUserid();
    add_user_info(userID);
    get_posts(userID);
});