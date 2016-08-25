var phoneError = '';
var successSt = "success";
var incorrect = "is-invalid";

function checkValid(elem) {
    return !$(elem).hasClass(incorrect);
}

function showloader() {
    $('#loader').css({display: 'block'});
}

function hideloader() {
    $('#loader').css({display: 'none'});
}

function checkPhone() {
    showloader();
    $.ajax({
        dataType: "json",
        url: 'phoneCheck?phone=' + $('#phoneInput').val(),
        data: {},
        success: function (data) {
            hideloader();
            if (data.status !== successSt) {
                $('#phoneDiv').addClass(incorrect);
            } else {
                $('#getcards-step').addClass('is-visible');
                $('#phone-step').css({display:'none'});
                $('#step-counter').html('Step (2/3)');
            }
        }
    });
}

function checkCard() {
    showloader();
    $.ajax({
        dataType: "json",
        url: 'cardCheck?card=' + $('#cardInput').val(),
        data: {},
        success: function (data) {
            hideloader();
            if (data.status !== successSt) {
                $('#cardDiv').addClass(incorrect);
            } else {
                $('#getcards-step').css({display:'none'});
                $('#getotp-step').addClass('is-visible');
                $('#step-counter').html('Step (3/3)');
            }
        }
    });
}

function checkOtp() {
    showloader();
    $.ajax({
        dataType: "json",
        url: 'otpCheck?otp=' + $('#otpInput').val() + '&token=' + $('#token').val(),
        data: {},
        success: function (data) {
            hideloader();
            if (data.status !== successSt) {
                $('#otpDiv').addClass(incorrect);
            } else {
                $('#getotp-step').css({display:'none'});
                $('#end-step').addClass('is-visible');
            }
        }
    });
}

$(document).ready(function () {
    $('#otpInput').on("keypress", function (e) {
        /* ENTER PRESSED*/
        if (e.keyCode === 13) {
            checkOtp();
            return false;
        }
    });
    $('#cardInput').on("keypress", function (e) {
        /* ENTER PRESSED*/
        if (e.keyCode === 13) {
            checkOtp();
            return false;
        }
    });
    $('#cardInput').on("keypress", function (e) {
        /* ENTER PRESSED*/
        if (e.keyCode === 13) {
            checkCard();
            return false;
        }
    });
    $('#phoneInput').on("keypress", function (e) {
        /* ENTER PRESSED*/
        if (e.keyCode === 13) {
            checkPhone();
            return false;
        }
    });
});