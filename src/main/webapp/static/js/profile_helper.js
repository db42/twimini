function getProfileUserid(){
    words = location.pathname.split('/')
    return words[words.length - 1]
}

$(function(){
    activatables('page', ['tweets', 'followers', 'following']);
    get_posts(getProfileUserid());
});