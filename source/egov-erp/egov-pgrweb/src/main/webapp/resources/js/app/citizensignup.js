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
	
	$('form').submit(function(){
		$('.signup-section').hide();
		$('.otp-section').show();
		return false;
	});
	
});