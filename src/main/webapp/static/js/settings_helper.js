var tm = tm || {};

function check_username(username){
    $('#username-error').empty();
    if(username.length === 0){
        $('#username-error').append("User Name Empty").slideDown("slow");
        return false;
    }
    if(username.search(' ') !== -1 || username.search('@') !== -1) {
        $('#username-error').append("User Name can't have spaces or '@'").slideDown("slow");
        return false;
    }
    if(username.length > 125){
        $('#username-error').append("User Name Too Large").slideDown("slow");
        return false;
    }
    return true;
}
function check_email(email){
    $('#email-error').empty();
    var re_email = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i
    if(!re_email.test(email)){
        $('#email-error').append("Email incorrect!").slideDown("slow");
        return false;
    }
    if(email.length > 125){
        $('#email-error').append("Email Too Large").slideDown("slow");
        return false;
    }
    return true;
}

function check_name(name){
    $('#name-error').empty();
    var ck_name = /^[A-Za-z]{3,250}$/;
    if(!ck_name.test(name)){
        $('#name-error').append("Name can have only Characters<br/> Min-Length : 3").slideDown("slow");
        return false;
    }
    return true;
}
function check_description(description){
    if(description.length > 250){
        $('#description-error').empty().append("Description too large!").slideDown("slow");
        return false;
    }
    return true;
}

function check_password(newpassword, retype){
    if(newpassword !== retype){
        $('#password-error').empty().append("The retyped Password doesn't match!").slideDown("slow");
        return false;
    }
    return true;
}

function password_change(form) {
    if(check_password(form["new_password"].value, form["red_password"].value)){
        tm.auth_ajax("/update_password?userID=".concat(tm.userID), form, function (data) {tm.callNormal("Password updated successfully."); });
    }
}

function account_change(form) {
    if (check_username(form["username"].value) && check_email(form["email"].value)){
        tm.auth_ajax("/update_account?userID=".concat(tm.userID), form, function (data) {tm.callNormal("Account updated successfully."); });
    }
}

function profile_change(form) {
    if(check_name(form["name"].value) && check_description(form["description"].value)){
        tm.auth_ajax("/update_profile?userID=".concat(tm.userID), form, function (data) {tm.callNormal("Profile updated successfully."); });
    }
}

$(function () {
    tm.add_user_info(tm.userID);
    activatables('tab', ['account', 'profile', 'password']);
});
