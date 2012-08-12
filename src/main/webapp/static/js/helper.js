var tm = tm || {};

tm.userID = sessionStorage.getItem("userID");
tm.password = sessionStorage.getItem("password");

tm.auth_ajax = function (url, form, success_fun, type) {
    type = typeof type !== 'undefined' ? type : 'POST';
    $.ajax({
        url: url,
        type: type,
        data: $(form).serialize(),
        headers: {
            "Authorization": window.btoa(tm.password),
            "Content-Type": "application/x-www-form-urlencoded"
        },
        success: function (data) {
            if (data.status !== "failed") {
                success_fun(data);
            }
        }
    });
};

function BasicView(ejsName, listName, url, userID) {
    this.ejsName = ejsName;
    this.listName = listName;
    this.url = url;
    this.userID = userID;
    this.since_id = "0";
    this.polled_data = [];
}

BasicView.prototype.getUrl = function () {
    return '/users/' + this.userID + '/' + this.url;
};

BasicView.prototype.addOne = function (data, append) {
    console.log("addOne");
    console.log(this.ejsName);
    console.log(this.listName);
    console.log(this.url);
    console.log(this.getUrl());
    console.log(data);
    if (data.timestamp !== undefined) {
        data.timestamp = tm.humaneDate(tm.parseISO8601(data.timestamp));
        if (this.since_id < data.id) {
            this.since_id = data.id; //SET since_id to the id of the latest tweet.
        }
    }

    if (this.max_id > data.id || typeof this.max_id === 'undefined') {
        this.max_id = data.id; //SET since_id to the id of the oldest tweet.
    }

    var entity = $(new EJS({url: '/static/ejs/' + this.ejsName}).render(data));
    var tweet_id = data.id;

    if (typeof append === 'undefined') {
        $('.' + this.listName).prepend(entity);
    } else {
        $('.' + this.listName).append(entity);
    }


    if (typeof data.post !== 'undefined') {
        $.get('/users/' + data.user_id, function (data) {
            var entity = $(new EJS({url: '/static/ejs/tweet_user_info.ejs'}).render(data));
            $('#tweet_id_' + tweet_id).prepend(entity);
        });
    }

};

BasicView.prototype.addAll = function (data) {
    var viewcontext = this;
    $.each(data, function (index, value) {
        viewcontext.addOne(value);
    });
};

BasicView.prototype.populate = function () {
    var viewcontext = this;
    $("#" + this.listName).empty();
    $.get(this.getUrl(), function (data) {
        console.log(viewcontext.getUrl());
        viewcontext.addAll(data.reverse());
    });
};


BasicView.prototype.callMessage = function (Message) {
    $('#container-message').empty().append(Message);
    $('#container-message').slideDown("slow");
    //todo: add click operation
    var viewcontext = this;
    $('#container-message').click(function () {
        $("#container-message").slideUp("slow");
        viewcontext.addAll(viewcontext.polled_data);
        $.each(viewcontext.polled_data, function (index, value) {
            viewcontext.polled_data.pop(value);
        });
    });
};

BasicView.prototype.load_new_data = function () {
    var viewcontext, url;
    if (typeof this.max_id === 'undefined') {
        return;
    }
    url = this.getUrl() + "?max_id=" + this.max_id;
    viewcontext = this;

    $.get(url, function (data) {
        $.each(data, function (index, value) {
            viewcontext.addOne(value, 'append');
        });
    });
};


function PostView(ejsName, listName, url, userID) {
    BasicView.call(this, ejsName, listName, url, userID);
}

PostView.prototype = new BasicView();
PostView.prototype.constructor = PostView;


PostView.prototype.poll = function () {
    var viewcontext, url, poll_success;
    viewcontext = this;
    url = this.getUrl() + "?since_id=" + this.since_id;
    poll_success = function (data) {
        $.each(data.reverse(), function (index, value) {
            viewcontext.polled_data.push(value);
            if (viewcontext.since_id < value.id) {
                viewcontext.since_id = value.id;
            }
        });
        var num_msgs = viewcontext.polled_data.length;
        if (num_msgs > 0) {
            viewcontext.callMessage(num_msgs + " new tweets");
        }
    };

    tm.auth_ajax(url, null, poll_success, 'GET');
};

function FeedView(ejsName, listName, url, userID) {
    PostView.call(this, ejsName, listName, url, userID);
}

FeedView.prototype = new PostView();
FeedView.prototype.constructor = FeedView;

FeedView.prototype.populate = function () {
    var viewcontext = this;
    tm.auth_ajax(this.getUrl(), null, function (data) {
        console.log(viewcontext.getUrl());
        viewcontext.addAll(data.reverse());
    }, 'GET');
};

FeedView.prototype.load_new_data = function () {
    var viewcontext, url, success_load;
    url = this.getUrl() + "?max_id=" + this.max_id;
    viewcontext = this;
    success_load = function (data) {
        $.each(data, function (index, value) {
            viewcontext.addOne(value, 'append');
        });
    };

    tm.auth_ajax(url, null, success_load, 'GET');
};

function add_tweet(form) {
    var successfun = function (data) {
        tm.callNormal("Tweet posted successfully.");
    };
    tm.auth_ajax("/users/".concat(tm.userID).concat("/posts"), form, successfun);
}

function callError(errorMessage) {
    $('#error-message').empty().append(errorMessage);
    $('#message-wrapper').fadeIn("slow");
    setTimeout('$("#message-wrapper").fadeOut("slow");', 5000);
}

function user_login(form) {
    $.post('/login', $(form).serialize(), function (data) {
        if (data.status === "success") {
            sessionStorage.setItem("userID", data.userID);
            //TODO get auth_key from server instead of password.
            sessionStorage.setItem("password", data.password);
            window.location.replace("http://localhost:8080/twimini/home");
        } else {
            callError("Username or Password is not correct.");
        }
    });
}

function user_register(form) {
    $.post('/register', $(form).serialize(), function (data) {
        if (data.status === "success") {
            callError("User successfully registered.");
        } else {
            callError("User registration failed.");
        }
    });
}

tm.get_posts = function (userID) {
    tm.postview = new PostView('addTweet.ejs', 'tweetlist', 'posts', userID);
    tm.postview.populate();

    setInterval(tm.postview.poll.bind(tm.postview), 20000);
    tm.scrollview = tm.postview;
};

tm.get_feed = function () {
    tm.feedview = new FeedView('addTweet.ejs', 'tweetlist', 'posts/feed', tm.userID);
    tm.feedview.populate();

    setInterval(tm.feedview.poll.bind(tm.feedview), 20000);
    tm.scrollview = tm.feedview;
};

tm.getProfileUserid = function () {
    var i, user_id, words = location.pathname.split('/');
    for (i = 0; i < words.length; i++) {
        if (words[i].trim() === 'profile') {
            user_id = words[i + 1];
        }
    }
    if (typeof user_id === 'undefined') {
        return tm.userID;
    } else {
        return words[words.length - 1];
    }
};

function follow_user(user_id, caller_user_id) {
    var url = '/users/' + caller_user_id + '/followings/' + user_id;
    tm.auth_ajax(url, null, function (data) {
        tm.callNormal("Successfully followed.");
    }, 'PUT');
}

function unfollow_user(user_id, caller_user_id) {
    var url = '/users/' + caller_user_id + '/followings/' + user_id;
    tm.auth_ajax(url, null, function (data) {
        tm.callNormal("Successfully unfollowed.");
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
            var parentx = user_block.parentNode.parentNode.parentNode;
            parentx.parentNode.removeChild(parentx);
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

tm.add_user_info = function (user_id) {
    $.get('/users/' + user_id + "?callerUserID=" + tm.userID, function (data) {
        var entity = $(new EJS({url: '/static/ejs/UserInfo.ejs'}).render(data));
        $('.profile-block').append(entity);
        if (user_id == tm. userID){
            $('#fbutton').remove();
        }
        activate_follow_button(user_id);
    });
};

tm.callNormal = function (Message) {
    $('#normal-message').empty().append(Message);
    $('#normal-wrapper').fadeIn("slow");
    setTimeout('$("#normal-wrapper").fadeOut("slow");', 5000);
    // todo: check options for animation
    /*$('#normal-wrapper').slideDown ("slow");
    setTimeout('$("#normal-wrapper").slideUp("slow");', 5000);*/
};

tm.fill_topbar = function () {
    if (typeof tm.image_url === 'undefined') {
        $.get('/users/' + tm.userID, function (data) {
            tm.image_url = data.image_url;
            $('#profile-image').append('<img src=' + tm.image_url + '?s=30></img>');
        });
    } else {
        $('#profile-image').append('<img src=' + tm.image_url + '?s=30></img>');
    }
};

var canLoad = true;
$(window).scroll(function () {
    if (1.06 * $(window).scrollTop() >= $(document).height() - $(window).height() && canLoad) {
        tm.scrollview.load_new_data();
    }
});
