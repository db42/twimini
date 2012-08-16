var tm = tm || {};

var event_objects = {
    'username': tm.check_username,
    'email': tm.check_email,
    'password': tm.check_password
};


function extracted(target_element, selector_prefix) {
    var elem_name, elem_value;
    target_element.blur(function () {
        elem_name = event.target.name;
        elem_value = event.target.value;

        if (elem_name in event_objects) {
            event_objects[elem_name](elem_value, selector_prefix);
        }
    });
}

function apply_validation_events() {
    var login_element, register_element;
    login_element = $(".login-box input[name='password']");
    extracted(login_element, 'login-');

    register_element = $(".register-box input");
    extracted(register_element, 'register-');
}

$(function () {
    apply_validation_events();
});
