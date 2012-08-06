
$(function(){
    activatables('page', ['tweets', 'followers', 'following']);
    user_id = getProfileUserid();
    add_user_info(user_id);
    get_posts(user_id);
});