var phoneError='';
var successSt="success";
var incorrect="is-invalid";

function checkValid(elem){
    return !$(elem).hasClass(incorrect);
}

function showloader(){
    $('#loader').css({display: 'block'});
}

function hideloader(){
    $('#loader').css({display: 'none'});
}

function checkPhone(){
    showloader();
    $.ajax({
        dataType: "json",
        url: 'phoneCheck?phone='+$('#phoneInput').val(),
        data: {},
        success: function(data){
            hideloader();
            if (data.status!==successSt){                
                $('#phoneDiv').addClass(incorrect);                
            } else {
                $('#getcards-step').addClass('is-visible');
            }
        }
    });
}

function checkCard(){
    showloader();
    $.ajax({
        dataType: "json",
        url: 'cardCheck?card='+$('#cardInput').val(),
        data: {},
        success: function(data){
            hideloader();
            if (data.status!==successSt){                
                $('#cardDiv').addClass(incorrect);                
            } else {
                $('#getotp-step').addClass('is-visible');
            }
        }
    });
}

function checkOtp(){
    showloader();
    $.ajax({
        dataType: "json",
        url: 'otpCheck?otp='+$('#otpInput').val()+'&token='+$('#token').val(),
        data: {},
        success: function(data){
            hideloader();
            if (data.status!==successSt){                
                $('#otpDiv').addClass(incorrect);                
            } else {
                $('#end-step').addClass('is-visible');
            }
        }
    });
}