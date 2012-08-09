var tm = tm || {};

$(function () {
    activatables('page', ['tweets', 'followers', 'following']);
    var user_id = tm.getProfileUserid();
    tm.add_user_info(user_id);
    tm.get_posts(user_id);
    tm.fill_topbar();
});