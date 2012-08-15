var event_objects = {
    'username': tm.check_username,
    'email': tm.check_email,
    'password': tm.check_password
};


function apply_validation_events(){
    var login_element = $(".login-box input[name='password']");
    login_element.blur( function(){
        var elem_name = event.target['name'];
        var elem_value = event.target['value'];

        if (elem_name in event_objects){
            event_objects[elem_name](elem_value, 'login-');
        }
    });

    var register_element = $(".register-box input");
    register_element.blur( function(){
        var elem_name = event.target['name'];
        var elem_value = event.target['value'];

        if (elem_name in event_objects){
            event_objects[elem_name](elem_value, 'register-');
        }
    });

}

$(function(){
    apply_validation_events();
});
