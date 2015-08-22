/*#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------*/

$(document).ready(function()
{	
	$.fn.dataTable.moment( 'DD/MM/YYYY h:mm a' );
	
	$('.page-container.horizontal-menu header.navbar .navbar-right > li, .page-container.horizontal-menu header.navbar .navbar-right > li ul li').hover(
			function() {
				$(this).children('ul').show();
			},
			function() {
				$(this).children('ul').removeAttr('style');
				$(this).children('ul').hide();
	});
	
	//TODO not yet implemented at backend
	$('#feedback-form').on('submit', function(e){
        e.preventDefault();
        $.ajax({
                url: 'home/feedback/sent',
                type: 'GET',
                data: {'subject':$("#subject").val(),'message':$("#comment").val()},
                success: function(data) {
                	bootbox.alert("Your feedback successfully submitted.");
                },
                error: function() {
                        
                }, complete : function() {
                	$('.add-feedback').modal('hide');
                }
        });
        
	});

	$('#password-form').on('submit', function(e){
	       e.preventDefault();
	       $.ajax({
	                url: 'home/password/update',
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
	                	} else  if (data == "NEWPWD_INVALID") {
	                		msg = "Password must be at least 8 to 32 characters long and must have one or more :- upper case and lower case alphabet,number and special character except [& < > # % \" ' / \ and space]"
	                	}
	                	bootbox.alert(msg);
	                },
	                error: function() {
	                	bootbox.alert("Internal server error occurred, please try after sometime.");
	                }, complete : function() {
	                	$('.change-password').modal('hide');
	                }
	        });
	        
	});
	
	worklist();
	
	$("#official_inbox").on('click','tbody tr td i',function(e) {
		$('.history-inbox').modal('show');
		historyTableContainer = $("#historyTable"); 
		historyTableContainer.dataTable({
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"autoWidth": false,
			"paging": false,
			"destroy":true,
			"oLanguage": {
				"sInfo": ""
			},
			"ajax": "inbox/history?stateId="+tableContainer1.fnGetData($(this).parent().parent(),6),
			"columns": [
						{ "data": "date","width": "20%" },
						{ "data": "sender","width": "15%" },
						{ "data": "task","width": "20%" },
						{ "data": "status","width": "20%" },
						{ "data": "details","width": "20%" },
						{ "data": "id","visible": false, "searchable": false },
						{ "data": "link","visible": false, "searchable": false }
						
					],
					"aaSorting": [[0, 'desc']]
		});
		
		e.stopPropagation();
	});
	
	$('.workspace').click(function(){
		$('.main-space').hide();
		$('.workspace').removeClass('active');
		$(this).addClass('active');
		if($(this).attr('data-work') == 'worklist' ){
			focussedmenu = "worklist";
			worklist();
		}else if($(this).attr('data-work') == 'drafts' ){
			focussedmenu = "drafts";
			drafts();
		}else if($(this).attr('data-work') == 'notifications' ){
			focussedmenu = "notifications";
			notifications();
		}
		$('#'+$(this).attr('data-work')).show();
	});
	
	
	$('#inboxsearch').keyup(function(){
		tableContainer1.fnFilter(this.value);
	});
	
	$('#draftsearch').keyup(function(){
		tableContainer1.fnFilter(this.value);
	});
	
	$('#notifysearch').keyup(function(){
		tableContainer1.fnFilter(this.value);
	});
	
	$("#official_inbox").on('click','tbody tr',function(event) {
		if (tableContainer1.fnGetData(this,7) != undefined) {
			var windowObjectReference = window.open(tableContainer1.fnGetData(this,7), ''+tableContainer1.fnGetData(this,6)+'', 'width=900, height=700, top=300, left=150,scrollbars=yes'); 
			openedWindows.push(windowObjectReference);
			windowObjectReference.focus();
		}
	});
	
	$("#official_drafts").on('click','tbody tr',function(event) {
		if (tableContainer1.fnGetData(this,6) != undefined) {
			var windowObjectReference = window.open(tableContainer1.fnGetData(this,6), ''+tableContainer1.fnGetData(this,5)+'', 'width=900, height=700, top=300, left=150,scrollbars=yes'); 
			openedWindows.push(windowObjectReference);
			windowObjectReference.focus();
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
	
});

//common ajax functions for worklist, drafts and notifications 
function worklist(){
	tableContainer1 = $("#official_inbox"); 
	tableContainer1.dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"bDestroy": true,
		"autoWidth": false,
		"ajax": "inbox",
			"columns": [
			{ "data": "date","width": "20%" },
			{ "data": "sender","width": "15%" },
			{ "data": "task","width": "20%" },
			{ "data": "status","width": "20%" },
			{ "data": "details","width": "20%" },
			{ "data" : null, "target":-1,"defaultContent": '<i class="fa fa-history history-size" class="tooltip-secondary" data-toggle="tooltip" title="History"></i>'},
			{ "data": "id","visible": false, "searchable": false },
			{ "data": "link","visible": false, "searchable": false }
		] ,
		"aaSorting": [[0, 'desc']]
	});
}

function drafts(){
	tableContainer1 = $("#official_drafts"); 
	tableContainer1.dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"bDestroy": true,
		"autoWidth": false,
		"ajax": "inbox/draft",
		"columns": [
		{ "data": "date","width": "20%" },
		{ "data": "sender","width": "15%" },
		{ "data": "task","width": "20%" },
		{ "data": "status","width": "20%" },
		{ "data": "details","width": "20%" },
		{ "data": "id","visible": false, "searchable": false },
		{ "data": "link","visible": false, "searchable": false }
	] ,
	"aaSorting": [[0, 'desc']]
});
}

function notifications(){
	tableContainer1 = $("#official_notify");
	tableContainer1.dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"bDestroy": true,
		"autoWidth": false,
		"aaSorting": [[0, 'desc']]
	});
}

function inboxloadmethod(){
	//alert('came to my parent'+focussedmenu);
	if(focussedmenu == 'worklist'){
		worklist();
	}else if(focussedmenu == 'drafts'){
		drafts();
	}else if(focussedmenu == 'notifications'){
		notifications();
	}
}