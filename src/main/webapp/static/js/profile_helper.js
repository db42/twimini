

$(function(){
    activatables('page', ['tweets', 'followers', 'following']);
    userID = getProfileUserid();
    add_user_info(userID);
    get_posts(userID);
});