var tm = tm || {};

var follow_user = function (user_id, caller_user_id) {
    var url = '/users/' + caller_user_id + '/followings/' + user_id;
    tm.auth_ajax(url, null, function (data) {
        callError("Successfully followed.");
    }, 'PUT');
};

var unfollow_user = function (user_id, caller_user_id) {
    var url = '/users/' + caller_user_id + '/followings/' + user_id;
    tm.auth_ajax(url, null, function (data) {
        callError("Successfully unfollowed.");
    }, 'DELETE');
};

var mouse_pressed = false;

tm.activate_follow_button = function (user_id) {
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
};

tm.follow_user_button = function (user_block) {
    var user_id, caller_user_id, parent;

    caller_user_id = tm.userID;
    user_id = user_block.getAttribute('userid');

    if (user_block.innerHTML.trim() === "Follow") {
        follow_user(user_id, caller_user_id);
        user_block.innerHTML = 'Following';
        user_block.className = 'fubutton following';
        /*        //add to followings
         var entity = $(new EJS({url: '/static/ejs/addUser.ejs'}).render(data));
         $('.' + this.listName).append(entity);
         var followingsview = new BasicView('addUser.ejs', 'followinglist', 'followings', userID);
         followingsview.addOne();*/
    } else if (user_block.innerHTML.trim() === "Unfollow") {
        unfollow_user(user_id, caller_user_id);
        if (user_block.parentElement.parentElement.parentElement.parentElement.parentElement.id === "followers") {
            user_block.innerHTML = 'Follow';
            user_block.className = 'fubutton following follow';
        } else {
            if (typeof profile_user_id !== 'undefined' && tm.userID === profile_user_id) {
                parent = user_block.parentNode.parentNode.parentNode;
                parent.parentNode.removeChild(parent);
            } else {
                user_block.innerHTML = 'Follow';
                user_block.className = 'fubutton following follow';
            }
        }
    }
};

tm.follow_user_button_hover = function (user_block) {
    if (user_block.innerHTML.trim() === "Following") {
        user_block.innerHTML = 'Unfollow';
        user_block.className = 'fubutton unfollow';
    }
};

tm.follow_user_button_out = function (user_block) {
    if (user_block.innerHTML.trim() === "Unfollow") {
        user_block.innerHTML = 'Following';
        user_block.className = 'fubutton following';
    }
};
