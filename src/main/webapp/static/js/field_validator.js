var tm = tm || {};

tm.check_username = function (username, selector_prefix) {
    if (typeof selector_prefix === 'undefined') {
        selector_prefix = '#';
    } else {
        selector_prefix = '#' + selector_prefix;
    }
    $(selector_prefix + 'username-error').empty();
    if (username.length === 0) {
        $(selector_prefix + 'username-error').append("User Name Empty").slideDown("slow");
        return false;
    }
    if (username.search(' ') !== -1 || username.search('@') !== -1) {
        $(selector_prefix + 'username-error').append("User Name can't have spaces or '@'").slideDown("slow");
        return false;
    }
    if (username.length > 125) {
        $(selector_prefix + 'username-error').append("User Name Too Large").slideDown("slow");
        return false;
    }
    $(selector_prefix + 'username-error').empty();
    return true;
};

tm.check_email = function (email, selector_prefix) {
    var re_email;
    if (typeof selector_prefix === 'undefined') {
        selector_prefix = '#';
    } else {
        selector_prefix = '#' + selector_prefix;
    }
    $(selector_prefix + 'email-error').empty();
    re_email = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i
    if (!re_email.test(email)) {
        $(selector_prefix + 'email-error').append("Invalid e-mail address").slideDown("slow");
        return false;
    }
    if (email.length > 125) {
        $(selector_prefix + 'email-error').append("E-mail Too Large").slideDown("slow");
        return false;
    }
    $(selector_prefix + 'email-error').empty();
    return true;
};

tm.check_password = function (password, selector_prefix){
    if (typeof selector_prefix === 'undefined') {
        selector_prefix = '#';
    } else {
        selector_prefix = '#' + selector_prefix;
    }
    $(selector_prefix + 'password-error').empty();
    if (password.length < 6) {
        $(selector_prefix + 'password-error').append("At least 6 characters long").slideDown("slow");
        return false;
    }
    $(selector_prefix + 'password-error').empty();
    return true;
};

