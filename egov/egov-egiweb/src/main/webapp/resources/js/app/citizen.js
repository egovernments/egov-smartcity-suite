$(document).ready(function()
{
	
	if($('#mobInvalid').val() == "true"){
		hideSigIn();
	    $('#mobnumValid').show();
	} else if($('#emailInvalid').val() == "true"){
		hideSigIn();
	    $('#emailidValid').show();
	}else if($('#citizenActivation').val() == "true" || $('#citizenActivationFailed').val() == "true"){
		$('.signup-section').hide();
		$('.otp-section').show();
	}else if($('#activationCodeSendingFailed').val() == "true"){
		hideSigIn();
	}
	function hideSigIn()
	{
		$('#signinform').hide();
		$('#signupform').show();
		$('.sign-in').removeClass('sign-active').addClass('sign-notactive');
		$('.sign-in').removeClass('arrow_box_left');
		$('.sign-up').removeClass('sign-notactive').addClass('sign-active');
	    $('.sign-up').removeClass('arrow_box_left').addClass('arrow_box_right');
	}
	function hideSigUp()
	{
		$('#signupform').hide();
		$('#signinform').show();
		$('.sign-up').removeClass('sign-active').addClass('sign-notactive');
		$('.sign-up').removeClass('arrow_box_right');
		$('.sign-in').removeClass('sign-notactive').addClass('sign-active');
		$('.sign-in').removeClass('arrow_box_right').addClass('arrow_box_left');
	}
	$('#mobileNumber').blur(function(){
		if($('#mobileNumber').val().length>0 && $('#mobileNumber').val().length<10){
			$('#mobnumberValid').show();
			$('#mobileNumber').val("");
		}else{
			$('#mobnumberValid').hide();
		}
	});
	$('#password').blur(function(){
		if($('#password').val().length>0 && $('#password').val().length<8){
			$('#passwordValid').show();
			$('#password').val("");
		}else{
			$('#passwordValid').hide();
		}
	});
	$('#emailId').blur(function(){
		var pattern = new RegExp("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
		var email = $('#emailId').val();
		if(!pattern.test(email) && $('#emailId').val().length>0){
			$('#emailValid').show();
			$('#emailId').val("");
		}else{
			$('#emailValid').hide();
		}
	});
	
	$('.check-password').blur(function(){
		if(($('#password').val()!="") && ($('#con-password').val()!=""))
		{
			if ($('#password').val() === $('#con-password').val()) {
				$('.password-error').hide();
				$('#con-password').removeClass('error');
			}else{
				$('.password-error').show();
				$('#con-password').addClass('error');
			}
		}
	});
	
	$('.sign').click(function(){
		if($(this).attr('data-sign') == "up"){
			hideSigIn();
		}else if($(this).attr('data-sign') == "in"){
			hideSigUp();
		}
	});
	
	$('form#signupform').submit(function(){
		$('#signupform').attr('action', '/portal/citizen/register').trigger('submit');
		return false;
	});
	$('#recovrbtn').click(function(){
		if($('#emailOrMobileNum').val()===""){
			$('#emailOrMobileNoReq').show();
		}else{
		$('#forgotPasswordForm').attr('action', '/egi/login/send-pwd').trigger('submit');
		}
		return false;
	});
	$('form#citizenactivationform').submit(function(){
		$('#citizenactivationform').attr('action', '/portal/citizen/activation/'+$('#citizenId').val()).trigger('submit');
		return false;
	});
	
});