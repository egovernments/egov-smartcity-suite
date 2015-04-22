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
	$('#feedback-form').on('submit', function(e){
        e.preventDefault();
        /*$.ajax({
                url: 'home/feedback/sent',
                type: 'POST',
                dataType: 'json',
                data: {
                        action: 'json'
                },
                success: function(data) {
                        
                },
                error: function() {
                        
                }
        });*/
        $('.loader-class').modal('hide')
	});

	$('#password-form').on('submit', function(e){
	       e.preventDefault();
	       /*$.ajax({
	                url: 'home/password/update',
	                type: 'POST',
	                dataType: 'json',
	                data: {
	                        action: 'json'
	                },
	                success: function(data) {
	                        
	                },
	                error: function() {
	                        
	                }
	        });*/
	        $('.loader-class').modal('hide')
	});
	tableContainer1 = $("#official_inbox"); 
	
	tableContainer1.dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"autoWidth": false,
		"bDestroy": true,
		"ajax": "inbox",
			"columns": [
			{ "data": "date","width": "20%" },
			{ "data": "sender","width": "15%" },
			{ "data": "task","width": "20%" },
			{ "data": "status","width": "20%" },
			{ "data": "details","width": "20%" },
			{ "data": "id","visible": false, "searchable": false },
			{ "data": "link","visible": false, "searchable": false }
		]
	});
	
	$('.workspace').click(function(){
		$('.main-space').hide();
		if($(this).attr('data-work') == 'worklist' ){
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
					{ "data": "id","visible": false, "searchable": false },
					{ "data": "link","visible": false, "searchable": false }
				] 
			});
			}else if($(this).attr('data-work') == 'drafts' ){
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
				] 
			});
			}else if($(this).attr('data-work') == 'notifications' ){
			tableContainer1 = $("#official_notify");
			tableContainer1.dataTable({
				"sPaginationType": "bootstrap",
				"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
				"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
				"bDestroy": true,
				"autoWidth": false
			});
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
		var windowObjectReference = window.open(tableContainer1.fnGetData(this,6), ''+tableContainer1.fnGetData(this,5)+'', 'width=900, height=700, top=300, left=150,scrollbars=yes'); 
		openedWindows.push(windowObjectReference);
	});
	
	$("#official_drafts").on('click','tbody tr',function(event) {
		var windowObjectReference = window.open(tableContainer1.fnGetData(this,6), ''+tableContainer1.fnGetData(this,5)+'', 'width=900, height=700, top=300, left=150,scrollbars=yes'); 
		openedWindows.push(windowObjectReference);
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
	
	// MASK SCREEN IMPORTANT
	//$('.loader-class').modal('show', {backdrop: 'static'});
	//$('.loader-class').modal('hide');
	
});
/*
	* { "data":[{"id":"30#1","sender":"Unknown / Admin","date":"09/04/2015 05:22 PM","task":"Complaint","status":"Registered","details":"CRN : CRN-tj3h6","link":"/pgr/complaint-update?id\u003d48"},{"id":"23#1","sender":"Unknown / Admin","date":"31/03/2015 08:50 PM","task":"Complaint","status":"Registered","details":"CRN : CRN-Y0QBo","link":"/pgr/complaint-update?id\u003d41"},{"id":"22#1","sender":"Unknown / Admin","date":"31/03/2015 07:31 PM","task":"Complaint","status":"Registered","details":"CRN : CRN-yvyjm","link":"/pgr/complaint-update?id\u003d40"},{"id":"21#1","sender":"Unknown / Admin","date":"31/03/2015 07:24 PM","task":"Complaint","status":"Registered","details":"CRN : CRN-57xOq","link":"/pgr/complaint-update?id\u003d39"},{"id":"20#1","sender":"Unknown / Admin","date":"31/03/2015 07:17 PM","task":"Complaint","status":"Registered","details":"CRN : CRN-3zZYS","link":"/pgr/complaint-update?id\u003d38"},{"id":"19#1","sender":"Unknown / Admin","date":"31/03/2015 11:03 AM","task":"Complaint","status":"Registered","details":"CRN : CRN-6RNTy","link":"/pgr/complaint-update?id\u003d37"},{"id":"18#1","sender":"Unknown / Admin","date":"31/03/2015 10:31 AM","task":"Complaint","status":"Registered","details":"CRN : CRN-bGOMD","link":"/pgr/complaint-update?id\u003d36"},{"id":"17#1","sender":"Unknown / Admin","date":"31/03/2015 10:18 AM","task":"Complaint","status":"Registered","details":"CRN : CRN-OlTX4","link":"/pgr/complaint-update?id\u003d33"},{"id":"15#1","sender":"Unknown / Admin","date":"30/03/2015 07:34 PM","task":"Complaint","status":"Registered","details":"CRN : CRN-K5O10","link":"/pgr/complaint-update?id\u003d30"},{"id":"13#1","sender":"Unknown / Admin","date":"30/03/2015 07:34 PM","task":"Complaint","status":"Registered","details":"CRN : CRN-LmxHD","link":"/pgr/complaint-update?id\u003d26"},{"id":"14#1","sender":"Unknown / Admin","date":"30/03/2015 07:34 PM","task":"Complaint","status":"Registered","details":"CRN : CRN-XpTSH","link":"/pgr/complaint-update?id\u003d27"}]}
*/
