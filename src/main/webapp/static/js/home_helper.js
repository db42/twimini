var tm = tm || {};

$(function () {
    tm.get_feed();
    tm.fill_topbar();
    $('#addpost-btn').click(function(){
        tweet_text = $('#tweet-text').val().trim();
        if(tweet_text.length > 140){
            callError("Tweet too long!");
        }
        else{
            add_tweet(tweet_text);
        }
    })
});