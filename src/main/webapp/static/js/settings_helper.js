var tm = tm || {};

function password_change(form) {
    $.ajax({
        url: '/update_password?userID=' + tm.userID,
        type: 'POST',
        data: $(form).serialize(),
        headers : {
            "Authorization" : window.btoa(tm.password),
            "Content-Type" : "application/x-www-form-urlencoded"
        },
        success : function (data) {
            if (data.status === "success") {
                tm.callNormal("Password updated successfully.");
            }
        }
    });
}

function account_change(form) {
    $.ajax({
        url: '/update_account?userID=' + tm.userID,
        type: 'POST',
        data: $(form).serialize(),
        headers : {
            "Authorization" : window.btoa(tm.password),
            "Content-Type" : "application/x-www-form-urlencoded"
        },
        success : function (data) {
            if (data.status === "success") {
                tm.callNormal("Account updated successfully.");
            }
    }
    });
}

function profile_change(form) {
    $.ajax({
        url: '/update_profile?userID=' + tm.userID,
        type: 'POST',
        data: $(form).serialize(),
        headers : {
            "Authorization" : window.btoa(tm.password),
            "Content-Type" : "application/x-www-form-urlencoded"
        },
        success : function (data) {
            if (data.status === "success") {
                tm.callNormal("Profile updated successfully.");
            }
        }
    });
}

$(function () {
    tm.add_user_info(tm.userID);
    activatables('tab', ['account', 'profile', 'password']);
});
