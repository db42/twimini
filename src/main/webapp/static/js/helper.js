var userID;
var password;
userID = sessionStorage.getItem("userID");
password = sessionStorage.getItem("password");

function BasicView(ejsName, listName, url, userID){
    this.ejsName = ejsName
    this.listName = listName
    this.url = url
    this.userID = userID
    this.since_id = "0"
    this.polled_data = []
}

BasicView.prototype.getUrl = function(){
    return '/users/'+ this.userID+'/'+this.url;
}
BasicView.prototype.addOne = function(data){
    console.log("addOne")
    console.log(this.ejsName)
    console.log(this.listName)
    console.log(this.url)
    console.log(this.getUrl())
    console.log(data)
    if (data['timestamp']!=null)
        data['timestamp'] = humaneDate(parseISO8601(data['timestamp']));
    var entity = $(new EJS({url:'/static/ejs/'+this.ejsName}).render(data));
    $('.'+this.listName).prepend(entity);

    if (this.since_id < data.id)
        this.since_id = data.id //SET since_id to the id of the latest tweet.
}

BasicView.prototype.addAll = function(data){
    var viewcontext = this
    $.each(data, function(index, value){
        viewcontext.addOne(value)
    })
}

BasicView.prototype.populate = function(){
    var viewcontext = this
    $.get(this.getUrl(), function(data){
        console.log(viewcontext.getUrl())
        viewcontext.addAll(data.reverse())
    })
}

BasicView.prototype.poll = function() {
    var viewcontext = this
    $.get(this.getUrl()+"?since_id="+this.since_id, function(data){
        $.each(data.reverse(), function(index, value){
            viewcontext.polled_data.push(value)
            if (viewcontext.since_id < value.id)
                viewcontext.since_id = value.id //SET since_id to the id of the latest tweet.
        });
        num_msgs = viewcontext.polled_data.length;
        if (num_msgs >0)
            viewcontext.callMessage(num_msgs+" new tweets");
//        viewcontext.addAll(data.reverse())
    })
}

BasicView.prototype.callMessage = function( Message){
    $('#container-message').empty().append(Message);
    $('#container-message').slideDown("slow");
    //todo: add click operation
    var viewcontext = this
    $('#container-message').click(function(){
        $("#container-message").slideUp("slow");
        viewcontext.addAll(viewcontext.polled_data)
        $.each(viewcontext.polled_data, function(index, value){
            viewcontext.polled_data.pop(value)
        });
    });
}
function addOne(listName, ejsName, data) {
    var entity = $(new EJS({url:'/static/ejs/'+ejsName}).render(data));
    $('.'+listName).prepend(entity);
}

function add_tweet(form){
    $.ajax({
        url: '/users/'+userID+'/posts',
        type: 'POST',
        data: $(form).serialize(),
        headers : {
            "Authorization" : window.btoa(password),
            "Content-Type" : "application/x-www-form-urlencoded"
        },
        success : function(data){
            dattt=data;
            console.log(data);
            data['timestamp'] = humaneDate(parseISO8601(data['timestamp']));
//    $.post('/users/1/posts',$(form).serialize(), function(data){
        addOne('tweetlist','addTweet.ejs',data)
    }
    });
}

function user_login(form){
    $.post('/login', $(form).serialize(), function(data){
        if (data.status == "success"){
            sessionStorage.setItem("userID",data.userID);
            //TODO get auth_key from server instead of password.
            sessionStorage.setItem("password",data.password);
            window.location.replace("http://localhost:8080/twimini/home");
        }
        else {
            callError("Username or Password is not correct.");
        }
    });
}

function user_register(form){
    $.post('/register', $(form).serialize(), function(data){
        if (data.status == "success"){
            callError("User successfully registered.");
        }
        else {
            callError("User registration failed.");
        }
    });
}

function get_posts(userID){
    postview = new BasicView('addTweet.ejs', 'tweetlist', 'posts', userID);
    postview.populate();
    followersview = new BasicView('addUser.ejs', 'followerlist', 'followers', userID);
    followersview.populate();
    followersview = new BasicView('addUser.ejs', 'followinglist', 'followers', userID);
    followersview.populate();

    setInterval(postview.poll.bind(postview), 20000);
}

function get_feed(){
    postview = new BasicView('addTweet.ejs', 'tweetlist', 'posts/feed', userID);
    postview.populate();

    setInterval(postview.poll.bind(postview), 20000);
//    setInterval(function(){
//            postview.poll();
//            }, 20000);
}

function getProfileUserid(){
    words = location.pathname.split('/')
    return words[words.length - 1]
}
var mouse_pressed=false;
function activate_follow_button(user_id){
    caller_user_id = userID;
    $('#fbutton').mouseenter(
        function(){
            if($('#fbutton').hasClass('follow')){
              $('#fbutton').empty().append('Follow');
            }
            else if($('#fbutton').html() == 'Following'){
                $('#fbutton').empty().append('Unfollow');
                $('#fbutton').addClass('unfollow');
            }
            mouse_pressed=false;
        }
    );
    $('#fbutton').mousedown(
        function(){
            if($('#fbutton').html() == 'Unfollow'){
                unfollow_user(user_id, caller_user_id);
                $('#fbutton').removeClass('unfollow').addClass('follow');
                $('#fbutton').empty().append('Follow');
            }
            else if($('#fbutton').html() == 'Following'){
                unfollow_user(user_id, caller_user_id);
                $('#fbutton').addClass('follow');
                $('#fbutton').empty().append('Follow');
            }
            else{
                follow_user(user_id, caller_user_id);
                $('#fbutton').removeClass('follow');
                $('#fbutton').empty().append('Following');
            }
            mouse_pressed=true;
        }
    );
    $('#fbutton').mouseleave(
        function(){
            if(!mouse_pressed){
                if($('#fbutton').html()=='Unfollow'){
                    $('#fbutton').removeClass('follow').removeClass('unfollow');
                    $('#fbutton').empty().append('Following');
                }
            }
        }
    );
}

function follow_user(user_id, caller_user_id){
     $.ajax({
         url: '/users/'+caller_user_id+'/followings/'+user_id,
         type: 'PUT',
         headers : {
             "Authorization" : window.btoa(password),
             "Content-Type" : "application/x-www-form-urlencoded"
         },
         success : function(data){
         //action
     }
     });
}

function unfollow_user(user_id, caller_user_id){
     $.ajax({
         url: '/users/'+caller_user_id+'/followings/'+user_id,
         type: 'DELETE',
         headers : {
             "Authorization" : window.btoa(password),
             "Content-Type" : "application/x-www-form-urlencoded"
         },
         success : function(data){
         //action
     }
     });
}

function add_user_info(user_id){
    $.get('/users/'+user_id+"?callerUserID="+this.userID, function(data){
        var entity = $(new EJS({url:'/static/ejs/UserInfo.ejs'}).render(data));
        $('.profile-block').append(entity);
        activate_follow_button(user_id);
    });
}

function callError  (errorMessage){
    $('#error-message').empty().append(errorMessage);
    $('#error-wrapper').fadeIn("slow");
    setTimeout('$("#error-wrapper").fadeOut("slow");', 5000);
}

function callNormal  (Message){
    $('#normal-message').empty().append(Message);
    $('#normal-wrapper').fadeIn("slow");
    setTimeout('$("#normal-wrapper").fadeOut("slow");', 5000);
    // todo: check options for animation
    /*$('#normal-wrapper').slideDown("slow");
    setTimeout('$("#normal-wrapper").slideUp("slow");', 5000);*/
}




