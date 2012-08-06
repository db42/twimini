var userID;
var password;
userID = sessionStorage.getItem("userID");
password = sessionStorage.getItem("password");

function humane_date(date_str){
	var time_formats = [
		[60, 'Just Now'],
		[90, '1 Minute'], // 60*1.5
		[3600, 'Minutes', 60], // 60*60, 60
		[5400, '1 Hour'], // 60*60*1.5
		[86400, 'Hours', 3600], // 60*60*24, 60*60
		[129600, '1 Day'], // 60*60*24*1.5
		[604800, 'Days', 86400], // 60*60*24*7, 60*60*24
		[907200, '1 Week'], // 60*60*24*7*1.5
		[2628000, 'Weeks', 604800], // 60*60*24*(365/12), 60*60*24*7
		[3942000, '1 Month'], // 60*60*24*(365/12)*1.5
		[31536000, 'Months', 2628000], // 60*60*24*365, 60*60*24*(365/12)
		[47304000, '1 Year'], // 60*60*24*365*1.5
		[3153600000, 'Years', 31536000], // 60*60*24*365*100, 60*60*24*365
		[4730400000, '1 Century'], // 60*60*24*365*100*1.5
	];

	var time = ('' + date_str).replace(/-/g,"/").replace(/[TZ]/g," "),
		dt = new Date,
		seconds = ((dt - new Date(time) + (dt.getTimezoneOffset() * 60000)) / 1000),
		token = ' Ago',
		i = 0,
		format;

	if (seconds < 0) {
		seconds = Math.abs(seconds);
		token = '';
	}

	while (format = time_formats[i++]) {
		if (seconds < format[0]) {
			if (format.length == 2) {
				return format[1] + (i > 1 ? token : ''); // Conditional so we don't return Just Now Ago
			} else {
				return Math.round(seconds / format[2]) + ' ' + format[1] + (i > 1 ? token : '');
			}
		}
	}

	// overflow for centuries
	if(seconds > 4730400000)
		return Math.round(seconds / 4730400000) + ' Centuries' + token;

	return date_str;
};

if(typeof jQuery != 'undefined') {
	jQuery.fn.humane_dates = function(){
		return this.each(function(){
			var date = humane_date(this.title);
			if(date && jQuery(this).text() != date) // don't modify the dom if we don't have to
				jQuery(this).text(date);
		});
	};
}


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
        data['timestamp'] = humane_date(data['timestamp']);
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
        setTimeout('$("#container-message").slideUp("slow");', 5000);
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
            data['timestamp'] = humane_date(data['timestamp']);
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




