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
    var entity = $(new EJS({url:'/static/ejs/'+this.ejsName}).render(data));
    $('.'+this.listName).append(entity);

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
        viewcontext.addAll(data)
    })
}

BasicView.prototype.poll = function() {
    var viewcontext = this
    $.get(this.getUrl()+"?since_id="+this.since_id, function(data){
        $.each(data, function(index, value){
            viewcontext.addOne(value)
        });
    })
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
}

function get_feed(){
    postview = new BasicView('addTweet.ejs', 'tweetlist', 'posts/feed', userID);
    postview.populate();

    setInterval(function(){
            postview.poll();
            }, 20000);
}

function getProfileUserid(){
    words = location.pathname.split('/')
    return words[words.length - 1]
}

function add_user_info(userID){
    $.get('/users/'+userID, function(data){
        var entity = $(new EJS({url:'/static/ejs/UserInfo.ejs'}).render(data));
        $('.profile-block').append(entity);
    });
}

function callError  (errorMessage){
    $('#error-message').empty();
    $('#error-message').append(errorMessage);
    $('#error-wrapper').fadeIn("slow");
    setTimeout('$("#error-wrapper").fadeOut("slow");', 5000);
}

function callNormal  (Message){
    $('#normal-message').empty();
    $('#normal-message').append(Message);
    $('#normal-wrapper').fadeIn("slow");
    setTimeout('$("#normal-wrapper").fadeOut("slow");', 5000);
    // todo: check options for animation
    /*$('#normal-wrapper').slideDown("slow");
    setTimeout('$("#normal-wrapper").slideUp("slow");', 5000);*/
}



