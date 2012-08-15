var tm = tm || {};

searchresult = function(searchterm){
    //todo:include spaces
    if (searchterm.search(' ')!==-1)
        callError("No spaces allowed for search");

    tm.searchview = new BasicView('addUser.ejs', 'resultlist', 'search' + "?q="+searchterm, tm.userID);
    tm.searchview.populate();
};

$(function () {
    tm.fill_topbar();

    searchresult("a");
    $('#addpost-btn').click(function(){
        tweet_text = $('#tweet-text').val().trim();
        if(tweet_text.length > 140){
            callError("Tweet too long!");
        }
        else{
            add_tweet(tweet_text);
        }
    });

    $('#tweet-text').keyup(function(){
        t_length = $('#tweet-text').val().trim().length;
        $('#tweet-length').empty().append(140-t_length);
        if (t_length > 140){
            $('#tweet-length').addClass("color-red");
            $('#tweet-length').removeClass("lcolor");
        }
        else{
            $('#tweet-length').addClass("lcolor");
            $('#tweet-length').removeClass("color-red");
        }
    })
});