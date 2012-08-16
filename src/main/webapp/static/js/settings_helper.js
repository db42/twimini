var tm = tm || {};

var new_password;


function check_name(name){
    $('#name-error').empty();
    var ck_name = /^.{3,250}$/;
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
    $('#password-error').empty();
    return true;
}

function save_password(){
    new_password = event.target.value;
}

function validate_password(){
    var red_password = event.target.value;
    check_password(new_password, red_password);
}

function password_change(form) {
    if(check_password(form["new_password"].value, form["red_password"].value)){
        tm.auth_ajax("/update_password?userID=".concat(tm.userID), form, function (data) {callMessage("Password updated successfully."); });
    }
}

function account_change(form) {
    if (tm.check_username(form["username"].value) && tm.check_email(form["email"].value)){
        tm.auth_ajax("/update_account?userID=".concat(tm.userID), form, function (data) {callMessage("Account updated successfully."); });
    }
}

function profile_change(form) {
    if(check_name(form["name"].value) && check_description(form["description"].value)){
        tm.auth_ajax("/update_profile?userID=".concat(tm.userID), form, function (data) {callMessage("Profile updated successfully."); });
    }
}

var event_objects = {
    'username': tm.check_username,
    'email': tm.check_email,
    'name': check_name,
    'description': check_description,
    'new_password': save_password,
    'red_password': validate_password
};


function apply_validation_events(){
    var element = $("input");
    element.blur( function(){
        var elem_name = event.target['name'];
        var elem_value = event.target['value'];

        if (elem_name in event_objects){
            event_objects[elem_name](elem_value);
        }
    });
}


$(function () {
    tm.add_user_info(tm.userID);
    activatables('tab', ['account', 'profile', 'password']);
    $('fbutton').remove();
    tm.fill_topbar();

    apply_validation_events();

});
