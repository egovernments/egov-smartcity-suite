$(document).ready(function()
{
	$('.check-password').blur(function(){
		if(($('#password').val()!="") && ($('#con-password').val()!=""))
		{
			if ($('#password').val() === $('#con-password').val()) {
				
			}else{
				$('.password-error').show();
				$('#con-password').addClass('error');
			}
		}
	});
	
	$('.sign').click(function(){
		if($(this).attr('data-sign') == "up"){
			$('#signinform').hide();
			$('#signupform').show();
			$('.sign-in').removeClass('arrow_box');
			$(this).addClass('arrow_box');
		}else if($(this).attr('data-sign') == "in"){
			$('#signupform').hide();
			$('#signinform').show();
			$('.sign-up').removeClass('arrow_box');
			$(this).addClass('arrow_box');
		}
	});
	
	$('form#signupform').submit(function(){
		$('.signup-section').hide();
		$('.otp-section').show();
		return false;
	});
	
});