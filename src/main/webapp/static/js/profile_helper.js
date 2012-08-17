var tm = tm || {};

var profile_user_id;

var get_followings = function (user_id) {
    if (typeof tm.follwingsview === 'undefined') {
        tm.follwingsview = new BasicView('addUser.ejs', 'followinglist', 'followings' + "?callerUserID=" + tm.userID, user_id);
        tm.follwingsview.populate();
    }
};


var get_followers = function (user_id) {
    if (typeof tm.followersview === 'undefined') {
        tm.followersview = new BasicView('addUser.ejs', 'followerlist', 'followers' + "?callerUserID=" + tm.userID, user_id);
        tm.followersview.populate();
    }
};

var profile_action = {
    'following':get_followings,
    'tweets':tm.get_posts,
    'followers': get_followers
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
        tm.get_posts(user_id);
        tm.scrollview = tm.postview;
    });
};

$(function () {
    var section_found=false;
    activatables('page', ['tweets', 'followers', 'following']);
    profile_user_id = tm.getProfileUserid();
    tm.add_user_info(profile_user_id);
    for (var section in profile_action){
        if (location.href.search(section) > 0) {
            console.log(section);
            profile_action[section](profile_user_id);
            section_found = true;
            break;
        }
    }
    if (!section_found){
        profile_action.tweets(profile_user_id);
    }

//    tm.get_posts(profile_user_id);
    tm.fill_topbar();
    activate_profile_links(profile_user_id);
});