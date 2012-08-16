var tm = tm || {};

var addEventOnPostButton = function () {
    var tweet_text;
    $('#addpost-btn').click(function () {
        tweet_text = $('#tweet-text').val().trim();
        if (tweet_text.length > 140) {
            tm.callGlobalMessage("Tweet too long!");
        } else {
            add_tweet(tweet_text);
        }
    });
};

var addEventOnTweetBox = function () {
    var tweet_length;
    $('#tweet-text').keyup(function () {
        tweet_length = $('#tweet-text').val().trim().length;
        $('#tweet-length').empty().append(140 - tweet_length);
        if (tweet_length > 140) {
            $('#tweet-length').addClass("color-red");
            $('#tweet-length').removeClass("lcolor");
        } else {
            $('#tweet-length').addClass("lcolor");
            $('#tweet-length').removeClass("color-red");
        }
    });
};

$(function () {
    tm.get_feed();
    tm.fill_topbar();
    addEventOnPostButton();
    addEventOnTweetBox();
});