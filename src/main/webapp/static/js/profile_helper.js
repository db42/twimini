var tm = tm || {};

var get_followings = function (user_id) {
        if (typeof tm.follwingsview === 'undefined') {
            tm.follwingsview = new BasicView('addUser.ejs', 'followinglist', 'followings', user_id);
            tm.follwingsview.populate();
        }
};


var get_followers = function (user_id) {
        if (typeof tm.followersview === 'undefined') {
            tm.followersview = new BasicView('addUser.ejs', 'followerlist', 'followers', user_id);
            tm.followersview.populate();
        }
};

var activate_profile_links = function (user_id) {
    $('a[href="#followers"]').click(function () {
        get_followers(user_id);
        tm.scrollview = tm.followersview;
    });

    $('a[href="#following"]').click(function () {
        get_followings(user_id);
        tm.scrollview = tm.follwingsview;
    });

    $('a[href="#tweets"]').click(function () {
        tm.scrollview = tm.postview;
    });
};

$(function () {
    activatables('page', ['tweets', 'followers', 'following']);
    var user_id = tm.getProfileUserid();
    tm.add_user_info(user_id);
    tm.get_posts(user_id);
    tm.fill_topbar();
    activate_profile_links(user_id);
});