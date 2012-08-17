var tm = tm || {};

var fetchSearchResult = function (search_term) {
    tm.searchview = new BasicView('addUser.ejs', 'resultlist', 'search' + "?q=" + search_term, tm.userID);
    tm.searchview.populate();
};

var getFromUrl = function (request_param) {
    var i, queryparams, query, queries;
    queries = document.location.href.split('?');
    for (i = 0; i < queries.length; i++) {
        query = queries[i];
        console.log(query);
        queryparams = query.split('=');
        console.log(queryparams);
        if (queryparams[0] === request_param) {
            return queryparams[1];
        }
    }
    return null;
};

$(function () {
    var search_term;
    tm.fill_topbar();
    search_term = getFromUrl("q");

    //todo:include spaces
    if (search_term.search(' ') !== -1)
        tm.callGlobalMessage("No spaces allowed for search");

    if (search_term !== null)
        fetchSearchResult(search_term);

    $('#addpost-btn').click(function(){
        tweet_text = $('#tweet-text').val().trim();
        if(tweet_text.length > 140){
            tm.callGlobalMessage("Tweet too long!");
        }
        else{
            add_tweet(tweet_text);
        }
    });

    $('#tweet-text').keyup(function(){
        tweet_length = $('#tweet-text').val().trim().length;
        $('#tweet-length').empty().append(140-tweet_length);
        if (tweet_length > 140){
            $('#tweet-length').addClass("color-red");
            $('#tweet-length').removeClass("lcolor");
        }
        else{
            $('#tweet-length').addClass("lcolor");
            $('#tweet-length').removeClass("color-red");
        }
    })
});