/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

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
				$('.check-password').removeClass('error');
			}else{
				$('.password-error').show();
				$('.check-password').addClass('error');
			}
		}
	});
	
	$('#name').blur(function(){
		var arr = $(this).val().split(' ');
	    var result = "";
	    for (var x=0; x<arr.length; x++)
        result+=arr[x].substring(0,1).toUpperCase()+arr[x].substring(1)+' ';
	    $(this).val(result.substring(0, result.length-1));
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
	
	$('#password, #username').popover({ trigger: "focus",placement: "bottom"})
	
});