var tm = tm || {};

function password_change(form) {
    tm.auth_ajax("/update_password?userID=".concat(tm.userID), form, function (data) {tm.callNormal("Password updated successfully."); });
}

function account_change(form) {
    tm.auth_ajax("/update_account?userID=".concat(tm.userID), form, function (data) {tm.callNormal("Account updated successfully."); });
}

function profile_change(form) {
    tm.auth_ajax("/update_profile?userID=".concat(tm.userID), form, function (data) {tm.callNormal("Profile updated successfully."); });
}

$(function () {
    tm.add_user_info(tm.userID);
    activatables('tab', ['account', 'profile', 'password']);
});
