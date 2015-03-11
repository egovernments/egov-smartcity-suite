$(document).ready(function()
{
	/* function getUrlVars()
	{
		var vars = [], hash;
		var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
		for(var i = 0; i < hashes.length; i++)
		{
			hash = hashes[i].split('=');
			vars.push(hash[0]);
			vars[hash[0]] = hash[1];
		}
		return vars;
	}
	
	if(getUrlVars()["form"] == 'signup'){
		//$('.login-toggle').removeClass('arrow_box_left').addClass('arrow_box_right');
		$('#signinform').hide();
		$('#signupform').show();
	}else if(getUrlVars()["form"] == 'signin'){
		//$('.login-toggle').removeClass('arrow_box_right').addClass('arrow_box_left');
		$('#signupform').hide();
		$('#signinform').show();
	} */
	
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
			$('.sign-in').removeClass('sign-active').addClass('sign-notactive');
			$(this).removeClass('sign-notactive').addClass('sign-active');
		   // $('.login-toggle').removeClass('arrow_box_left').addClass('arrow_box_right');
		}else if($(this).attr('data-sign') == "in"){
			$('#signupform').hide();
			$('#signinform').show();
			$('.sign-up').removeClass('sign-active').addClass('sign-notactive');
			$(this).removeClass('sign-notactive').addClass('sign-active');
			//$('.login-toggle').removeClass('arrow_box_right').addClass('arrow_box_left');
		}
	});
	
	$('form#signupform').submit(function(){
		$('.signup-section').hide();
		$('.otp-section').show();
		return false;
	});
	
});