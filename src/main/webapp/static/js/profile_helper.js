var tm = tm || {};

var profile_user_id;
function follow_user(user_id, caller_user_id) {
    var url = '/users/' + caller_user_id + '/followings/' + user_id;
    tm.auth_ajax(url, null, function (data) {
        callError("Successfully followed.");
    }, 'PUT');
}

function unfollow_user(user_id, caller_user_id) {
    var url = '/users/' + caller_user_id + '/followings/' + user_id;
    tm.auth_ajax(url, null, function (data) {
        callError("Successfully unfollowed.");
    }, 'DELETE');
}

var mouse_pressed = false;

function activate_follow_button(user_id) {
    var caller_user_id = tm.userID;
    $('#fbutton').mouseenter(
        function () {
            if ($('#fbutton').hasClass('follow')) {
                $('#fbutton').empty().append('Follow');
            } else if ($('#fbutton').html().trim() === 'Following') {
                $('#fbutton').empty().append('Unfollow');
                $('#fbutton').addClass('unfollow');
            }
            mouse_pressed = false;
        }
    );
    $('#fbutton').mousedown(
        function () {
            if ($('#fbutton').html().trim() === 'Unfollow') {
                unfollow_user(user_id, caller_user_id);
                $('#fbutton').removeClass('unfollow').addClass('follow');
                $('#fbutton').empty().append('Follow');
            } else if ($('#fbutton').html().trim() === 'Following') {
                unfollow_user(user_id, caller_user_id);
                $('#fbutton').addClass('follow');
                $('#fbutton').empty().append('Follow');
            } else {
                follow_user(user_id, caller_user_id);
                $('#fbutton').removeClass('follow');
                $('#fbutton').empty().append('Following');
            }
            mouse_pressed = true;
        }
    );
    $('#fbutton').mouseleave(
        function () {
            if (!mouse_pressed) {
                if ($('#fbutton').html().trim() === 'Unfollow') {
                    $('#fbutton').removeClass('follow').removeClass('unfollow');
                    $('#fbutton').empty().append('Following');
                }
            }
        }
    );
}

tm.add_user_info = function (user_id) {
    $.get('/users/' + user_id + "?callerUserID=" + tm.userID, function (data) {
        var entity = $(new EJS({url: '/static/ejs/UserInfo.ejs'}).render(data));
        $('.profile-block').append(entity);
        if (user_id == tm.userID){
            $('#fbutton').remove();
        }
        else{
            activate_follow_button(user_id);
        }
    });
};

function follow_user_button(user_block){
    var caller_user_id = tm.userID;
    var user_id = user_block.getAttribute('userid');

    if(user_block.innerHTML.trim() === "Follow"){
        follow_user(user_id, caller_user_id);
        user_block.innerHTML = 'Following';
        user_block.className = 'fubutton following';
/*        //add to followings
        var entity = $(new EJS({url: '/static/ejs/addUser.ejs'}).render(data));
        $('.' + this.listName).append(entity);
        var followingsview = new BasicView('addUser.ejs', 'followinglist', 'followings', userID);
        followingsview.addOne();*/
    }
    else if(user_block.innerHTML.trim() === "Unfollow"){
        unfollow_user(user_id, caller_user_id);
        if(user_block.parentElement.parentElement.parentElement.parentElement.parentElement.id === "followers"){
            user_block.innerHTML = 'Follow';
            user_block.className = 'fubutton following follow';
        }
        else{
            if (tm.userID === profile_user_id) {
                var parentx = user_block.parentNode.parentNode.parentNode;
                parentx.parentNode.removeChild(parentx);
            }
            else {
                user_block.innerHTML = 'Follow';
                user_block.className = 'fubutton following follow';
            }
        }
    }
}
function follow_user_button_hover(user_block){
    if(user_block.innerHTML.trim() === "Following"){
        user_block.innerHTML = 'Unfollow';
        user_block.className = 'fubutton unfollow';
    }
}
function follow_user_button_out(user_block){
    if(user_block.innerHTML.trim() === "Unfollow"){
        user_block.innerHTML = 'Following';
        user_block.className = 'fubutton following';
    }
}

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
    profile_user_id = tm.getProfileUserid();
    tm.add_user_info(profile_user_id);
    tm.get_posts(profile_user_id);
    tm.fill_topbar();
    activate_profile_links(profile_user_id);
});