var tm = tm || {};

$(function () {
    tm.get_feed();
    tm.fill_topbar();
    $('#addpost-btn').click(function(){
        tweet_text = $('#tweet-text').val().trim();
        alert(tweet_text);
        add_tweet(tweet_text);
    })
});