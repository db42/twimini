var tm = tm || {};

$(function () {
    tm.get_feed();
    tm.fill_topbar();
    $('#addpost-btn').click(function(){
        tweet_text = $('#tweet-text').val().trim();
        if(tweet_text.length > 140){
            callMessage("Tweet too long!");
        }
        else{
            add_tweet(tweet_text);
        }
    });

    $('#tweet-text').keyup(function(){
        t_length = $('#tweet-text').val().trim().length
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