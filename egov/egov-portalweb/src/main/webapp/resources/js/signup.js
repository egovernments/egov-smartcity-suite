$(document).ready(function(){
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
	
	$('#activationCode').focus(function() {
		$('#activationCode').val("");
		return false;
	});
	
	$('form#signupform').submit(function(){
		$('#signupform').attr('action', '/portal/citizen/register').trigger('submit');
		return false;
	});
	
	$('form#citizenactivationform').submit(function(){
		if($('#citizenId').val()===""){
			$('#citizenactivationform').attr('action', '/portal/citizen/activation').trigger('submit');
		}else{
			$('#citizenactivationform').attr('action', '/portal/citizen/activation/'+$('#citizenId').val()).trigger('submit');
		}
		return false;
	});
});