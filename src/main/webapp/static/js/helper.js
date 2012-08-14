var tm = tm || {};

tm.userID = sessionStorage.getItem("userID");
tm.auth_key = sessionStorage.getItem("auth_key");

tm.auth_ajax = function (url, form, success_fun, type) {
    if (tm.userID === null) {
        callError("This action requires user log-in. Redirecting to login-page..");
        setTimeout(function () {
            document.location.href = "/";
        }, 3000);
        return;
    }

    type = typeof type !== 'undefined' ? type : 'POST';
    $.ajax({
        url: url,
        type: type,
        data: $(form).serialize(),
        headers: {
            "Authorization": window.btoa(tm.auth_key),
            "Content-Type": "application/x-www-form-urlencoded"
        },
        success: function (data) {
            if (data.status !== "failed") {
                success_fun(data);
            }
        },
        error: function (){
            callError('Not Connected!');
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

        //Retweet button
        if (data.reposted === false && tm.userID != data.user_id && tm.userID != data.author_id) {
            var retweet_button = $("<div class=\"retweet\" onclick=\"repost(" + data.id + ")\">Re-Tweet</div>");
            $('#tweet_id_' + tweet_id + ' .post-data').append(retweet_button);
            // <img src='/static/images/retweet.png' />
        }

        //Retweet by
        if (data.author_id !== 0) {
            $.get('/users/' + data.user_id, function (user_data) {
                console.log("extra " + user_data);
                $('#tweet_id_' + tweet_id + ' .author_name').html(user_data.name);
            });
            data.user_id = data.author_id;
        }

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
    $.get(url, function (data) {
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
    });
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

FeedView.prototype.poll = function () {
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

function add_tweet(tweet) {
    var form=$("<form><input type=\"text\" name=\"post\" value=\""+tweet+"\"></form>");
    callError("Tweeting...");
    var url, successfun;
    successfun = function (data) {
        callError("Tweet posted successfully.");
    };
    url = "/users/" + tm.userID + "/posts";
    tm.auth_ajax(url, form, successfun, 'POST');
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
            sessionStorage.setItem("auth_key", data.auth_key);
            window.location.replace("http://localhost:8080/twimini/home");
        } else {
            callError("Username or Password is not correct.");
        }
    });
}

function user_register(form) {
    $.post('/register', $(form).serialize(), function (data) {
        if (data.status === "success") {
            callError("User successfully registered. Redirecting... ");
            setTimeout(function () {
                user_login(form);
            }, 2000);
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



var logout = function() {
    sessionStorage.clear();
    $.get('/logout', function(data){
        document.location.href = '/';
    })

}
tm.fill_topbar = function () {
    if (typeof tm.image_url === 'undefined') {
        $.get('/users/' + tm.userID, function (data) {
            tm.image_url = data.image_url;
            $('#profile-image').append('<img src=' + tm.image_url + '?s=30></img>');
        });
    } else {
        $('#profile-image').append('<img src=' + tm.image_url + '?s=30></img>');
    }

    $("#settings-block").click(function(){
        document.location.href='/twimini/settings';
    });
    $("#logout-block").click(function(){
        document.location.href='/logout';
    });
};

var repost = function (postID) {
    var url, repost_success;
    url = '/users/' + tm.userID + '/posts/repost/' + postID;
    var element = event.target;
    repost_success = function (data) {
        callError("Retweet posted successfully.");
        $(element).hide();
    };
    tm.auth_ajax(url, null, repost_success, 'POST');
};

var canLoad = true;
$(window).scroll(function () {
    if (1.06 * $(window).scrollTop() >= $(document).height() - $(window).height() && canLoad) {
        setTimeout(function () {
        tm.scrollview.load_new_data();
        }, 1000);
    }
});

searchresult = function(form){
    searchterm = form["q"].value.trim();
    //todo:include spaces
    if (searchterm.search(' ')!==-1)
        callError("No spaces allowed for search");

    tm.searchview = new BasicView('addUser.ejs', 'resultlist', 'search' + "?q="+searchterm, tm.userID);
    tm.searchview.populate();
};


