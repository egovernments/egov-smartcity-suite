/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces, 
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any 
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines, 
 *            please contact contact@egovernments.org
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
 *
 */

$(document).ready(function()
{	
	$('.menu-item').click(function(e)
	{
		$('.citizen-screens').hide();
		$('.hr-menu li').removeClass('active');
		$(this).parent().addClass('active');
		$($(this).data('show-screen')).show();
	});
	
	$("#sortby_drop li a").click(function(){
		$("#sortby_drop > .btn > span > b").html($(this).html());
	});
	
	$('.tabs-style-topline nav li').click(function(){
		if($(this).attr('data-section') == "newrequest")
		{
			if($(this).attr('data-newreq-section') == '#section-newrequest-1')
			{
				$('.tabs-style-topline nav li').removeClass('tab-current-newreq');
				$('.content-wrap section').removeClass('content-current-newreq');
				$(this).addClass('tab-current-newreq');
				$($(this).attr('data-newreq-section')).addClass('content-current-newreq');
			}
			
		}else if($(this).attr('data-section') == "myaccount")
		{
			$('.tabs-style-topline nav li').removeClass('tab-current-myacc');
			$('.content-wrap section').removeClass('content-current-myacc');
			$(this).addClass('tab-current-myacc');
			$($(this).attr('data-myaccount-section')).addClass('content-current-myacc');
		}
	});
	
	$('.check-password').blur(function(){
		if(($('#new-pass').val()!="") && ($('#retype-pass').val()!=""))
		{
			if ($('#new-pass').val() === $('#retype-pass').val()) {
				
				}else{
				$('.password-error').show();
				$('#retype-pass').addClass('error');
			}
		}
	});
	
	$('#password-form').on('submit', function(e){
	       e.preventDefault();
	       $.ajax({
               url: '/egi/home/password/update',
               type: 'GET',
               data: {'currentPwd': $("#old-pass").val(), 'newPwd':$("#new-pass").val(),'retypeNewPwd':$("#retype-pass").val()},
               success: function(data) {
               	var msg = "";
               	if (data == "SUCCESS") {
               		msg = "Your password has been updated."
               	} else if (data == "NEWPWD_UNMATCH") {
               		msg = "New password you have entered does not match with retyped password."
               	} else if (data == "CURRPWD_UNMATCH") {
               		msg = "Old password you have entered is incorrect."
               	} 
               	bootbox.alert(msg);
               },
               error: function() {
               	bootbox.alert("Internal server error occurred, please try after sometime.");
               }, complete : function() {
               	$('.change-password, .loader-class').modal('hide');
               }
       }); 
	});
	
	
	$(".ico-menu").bind('mouseover', function () {
		$(this).addClass('open');
	});
	
	$(".ico-menu").bind('mouseout', function () { 
		$(this).removeClass('open');
	});
	
	$('a[data-open-popup]').click(function(event){
		event.preventDefault();
		popupCenter($(this).attr('href'), 'myPop1',940,600);
	});
	
	function popupCenter(url, title, w, h) {
		var left = (screen.width/2)-(w/2);
		var top = (screen.height/2)-(h/2);
		return window.open(url, title, 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width='+w+', height='+h+', top='+top+', left='+left);
	} 
	
	
});