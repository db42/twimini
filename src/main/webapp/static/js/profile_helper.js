function BasicView(ejsName, listName, url, userID){
    this.ejsName = ejsName
    this.listName = listName
    this.url = url
    this.userID = userID
}

BasicView.prototype.getUrl = function(){
    return '/users/'+ this.userID+'/'+this.url;
}
BasicView.prototype.addOne = function(data){
    var entity = $(new EJS({url:'/static/ejs/'+this.ejsName}).render(data));
    $('.'+this.listName).append(entity);
}

BasicView.prototype.addAll = function(data){
    viewcontext = this
    $.each(data, function(index, value){
        viewcontext.addOne(value)
    })
}

BasicView.prototype.populate = function(){
    viewcontext = this
    $.get(this.getUrl(), function(data){
        viewcontext.addAll(data)
    })
}


function addOne(listName, ejsName, data) {
    var entity = $(new EJS({url:'/static/ejs/'+ejsName}).render(data));
    $('.'+listName).append(entity);
}

function add_tweet(form){
    $.ajax({
        url: '/users/1/posts',
        type: 'POST',
        data: $(form).serialize(),
        headers : {
            "Authorization" : window.btoa("1234"),
            "Content-Type" : "application/x-www-form-urlencoded"
        },
        success : function(data){
//    $.post('/users/1/posts',$(form).serialize(), function(data){
        addOne('tweetlist','addTweet.ejs',data)
    }
    });
}

function get_posts(){
    postview = new BasicView('addTweet.ejs', 'tweetlist', 'posts', '1');
    postview.populate();
}
$(function(){
    get_posts();
});

