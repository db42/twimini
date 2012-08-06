function password_change(form){
    $.ajax({
        url: '/update_password?userID='+userID,
        type: 'POST',
        data: $(form).serialize(),
        headers : {
            "Authorization" : window.btoa(password),
            "Content-Type" : "application/x-www-form-urlencoded"
        },
        success : function(data){
        if (data.status == "success")
            callNormal("Password updated successfully.")
    }
    });
}

function account_change(form){
    $.ajax({
        url: '/update_account?userID='+userID,
        type: 'POST',
        data: $(form).serialize(),
        headers : {
            "Authorization" : window.btoa(password),
            "Content-Type" : "application/x-www-form-urlencoded"
        },
        success : function(data){
        if (data.status == "success")
            callNormal("Account updated successfully.")
    }
    });
}

function profile_change(form){
    $.ajax({
        url: '/update_profile?userID='+userID,
        type: 'POST',
        data: $(form).serialize(),
        headers : {
            "Authorization" : window.btoa(password),
            "Content-Type" : "application/x-www-form-urlencoded"
        },
        success : function(data){
        if (data.status == "success")
            callNormal("Profile updated successfully.")
    }
    });
}

$(function(){
add_user_info(userID);
activatables('tab', ['account', 'profile', 'password']);
})
