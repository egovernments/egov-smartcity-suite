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

$(document).ready(function(){
	$('.password-error').hide();
	$('.totalServicesAppliedHide').hide();
	$('.totalServicesCompletedHide').hide();
	$('#servicesCmpletedDiv').attr('style', 'opacity: 0.7;cursor: pointer');
	$('#totalServicesAppliedDiv').attr('style', 'opacity: 0.7;cursor: pointer');
	$('#totalServicesAppliedDiv').click(function() {
		$('.servicesUnderScrutinyHide').hide();
		$('.totalServicesCompletedHide').hide();
		$('.totalServicesAppliedHide').show();
		$('#totalServicesAppliedDiv').attr('style', 'opacity: 1;cursor: pointer');
		$('#servicesUnderScrutinyDiv').attr('style', 'opacity: 0.7;cursor: pointer');
		$('#servicesCmpletedDiv').attr('style', 'opacity: 0.7;cursor: pointer');
	});
	
	$('#servicesUnderScrutinyDiv').click(function() {
		$('.totalServicesAppliedHide').hide();
		$('.totalServicesCompletedHide').hide();
		$('.servicesUnderScrutinyHide').show();
		$('#servicesUnderScrutinyDiv').attr('style', 'opacity: 1;cursor: pointer');
		$('#servicesCmpletedDiv').attr('style', 'opacity: 0.7;cursor: pointer');
		$('#totalServicesAppliedDiv').attr('style', 'opacity: 0.7;cursor: pointer');
	});
	
	$('#servicesCmpletedDiv').click(function() {
		$('.totalServicesAppliedHide').hide();
		$('.servicesUnderScrutinyHide').hide();
		$('.totalServicesCompletedHide').show();
		$('#servicesCmpletedDiv').attr('style', 'opacity: 1;cursor: pointer');
		$('#totalServicesAppliedDiv').attr('style', 'opacity: 0.7;cursor: pointer');
		$('#servicesUnderScrutinyDiv').attr('style', 'opacity: 0.7;cursor: pointer');

	});
	
  var module;

  $('.services .content').matchHeight();
  
  leftmenuheight();
  rightcontentheight();

  //alert(matchRuleShort("bird123", "bird*"))

  $('#search').keyup(function(e){
    var rule = '*'+$(this).val()+'*';
    if(e.keyCode == 8){
      $('[data-services="'+module+'"]').show();
    }
    $(".services-item .services:visible").each(function(){
      var testStr = $(this).find('.content').html().toLowerCase();
      console.log(testStr, rule, matchRuleShort(testStr, rule))
      if(matchRuleShort(testStr, rule))
        $(this).show();
      else
        $(this).hide();
    });
  });

  $('.modules-li').click(function(){
    $('#search').val('');
    $('.modules-li').removeClass('active');
    $(this).addClass('active');
    module = $(this).data('module');
    $('.services-item .services').hide();
    $('#showServiceGroup').hide();
    if(module == 'home'){
      $('.inbox-modules #showServiceGroup').show();
      $('.action-bar').hide();
      $('.linkedApplications').hide();
      inboxloadmethod();
    }
    else if(module == 'My Services'){
        $('.inbox-modules').hide();
        $('[data-services="'+module+'"], .action-bar, .linkedApplications').show();
    }	
    else{
      $('.inbox-modules, .linkedApplications').hide();
      $('[data-services="'+module+'"], .action-bar').show();
    }
  })

  $( window ).resize(function() {
    leftmenuheight();
    rightcontentheight();
  });

  $('table tbody tr').click(function(){
    $('#myModal').modal('show');
  });
  

  $('.checkpassword').blur(function(){
  	if(($('#new-pass').val()!="") && ($('#retype-pass').val()!=""))
  	{
  		if ($('#new-pass').val() === $('#retype-pass').val()) {
  			
  			}else{
  			$('.password-error').show();
  			$('#retype-pass').addClass('error');
  		}
  	}
  });

  $('#btnChangePwd').click(function(e){
         e.preventDefault();
         $.ajax({
             url: '/egi/home/password/update',
             type: 'GET',
             data: {'currentPwd': $("#old-pass").val(), 'newPwd':$("#new-pass").val(),'retypeNewPwd':$("#retype-pass").val()},
             success: function(data) {
             	var msg = "";
             	if (data == "SUCCESS") {
             		msg = "Your password has been updated.";
             	} else if (data == "NEWPWD_UNMATCH") {
             		msg = "New password you have entered does not match with retyped password.";
             	} else if (data == "CURRPWD_UNMATCH") {
             		msg = "Old password you have entered is incorrect.";
             	}  else if (data = "NEWPWD_INVALID") {
             		msg = $('#errorPwdInvalid').val() + "\"" + " ' / \ and space]";
             	}
             	bootbox.alert(msg);
             },
             error: function() {
             	bootbox.alert("Internal server error occurred, please try after sometime.");
             }, complete : function() {
             	$('.change-password, .loader-class').modal('hide');
             	resetValues();
             }
     }); 
  });
  
  $('#serviceGroup').change(function(){
	  var selected = $(this).val();
	  var total = $( "#totalServicesAppliedSize" ).html().trim();
	  var length = document.getElementsByClassName($(this).val()).length / 2;
	  if($(this).val() == "") {
		  $('.showAll').show();
		  $( "#totalServicesAppliedSize" ).html($( "#tabelPortal tbody.totalServicesAppliedHide tr.showAll" ).length);
		  $( "#totalServicesCompletedSize" ).html($( "#tabelPortal tbody.totalServicesCompletedHide tr.showAll" ).length);
		  $( "#totalServicesPendingSize" ).html($( "#tabelPortal tbody.servicesUnderScrutinyHide tr.showAll" ).length);
		  var showAllClass ="#tabelPortal tbody.servicesUnderScrutinyHide tr.showAll td:first-child";
		  generateSno(showAllClass);

	  } else {
		  $('.showAll').hide();
		  $('.'+$(this).val()).show();
		  $( "#totalServicesAppliedSize" ).html($( "#tabelPortal tbody.totalServicesAppliedHide tr."+$(this).val() ).length);
		  $( "#totalServicesCompletedSize" ).html($( "#tabelPortal tbody.totalServicesCompletedHide tr."+$(this).val() ).length);
		  $( "#totalServicesPendingSize" ).html($( "#tabelPortal tbody.servicesUnderScrutinyHide tr."+$(this).val() ).length);
		  
		  var servicesUnderScrutinyHideClass ="#tabelPortal tbody.servicesUnderScrutinyHide tr."+ selected + " td:first-child";
		  var totalServicesAppliedHideClass="#tabelPortal tbody.totalServicesAppliedHide tr."+ selected + " td:first-child";
		  var totalServicesCompletedHideClass="#tabelPortal tbody.totalServicesCompletedHide tr."+ selected + " td:first-child";
		  generateSno(servicesUnderScrutinyHideClass);
		  generateSno(totalServicesAppliedHideClass);
		  generateSno(totalServicesCompletedHideClass);
	  }
  });
  
  $('.linkedApplications, .action-bar').hide();
  

});

function onlinePayTaxForm(url){
	window.open(url,'window','scrollbars=yes,resizable=yes,height=700,width=800,status=yes');
}

function leftmenuheight(){
  //console.log($( window ).height(), $('.modules-ul').height());
  $('.left-menu,.modules-ul').css({
    height:$( window ).height(),
    overflow : 'auto'
  });
}

function rightcontentheight(){
  //console.log($( window ).height(), $('.right-content').height());
  $('.right-content').css({
    height:$( window ).height(),
    overflow : 'auto'
  })
}

//Short code
function matchRuleShort(str, rule) {
  return new RegExp("^" + rule.toLowerCase().split("*").join(".*") + "$").test(str.toLowerCase());
}

var url;
function openPopUp(url) {
	window.open(url, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function generateSno(className)
{
	var idx=1;
	$(className).each(function(){
		$(this).text(idx);
		idx++;
	});
}

function resetValues() {
		$('#retype-pass').val('');
		$('#new-pass').val('');
		$('#old-pass').val('');
		$('.password-error').hide();
}

function inboxloadmethod() {
	location.reload();
}