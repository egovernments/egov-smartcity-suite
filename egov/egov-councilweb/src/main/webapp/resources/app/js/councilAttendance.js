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

$(document).ready(function() {
	
	
	$(".councilcommitmem").change(function(){  
    	var $hiddenName=$(this).data('change-to');
    	if($(this).is(':checked')){
    		$('input[name="'+$hiddenName+'"]').val(true);
    	}else{
    		$('input[name="'+$hiddenName+'"]').val(false);
    	}
    });

	$("#committeechk").change(function(){  
		if($(this).is(':checked')){
			$('#councilcommittee')
	        .find('> tbody > tr > td:first-child > input[type="checkbox"]')
	        .prop('checked', true);
			setHiddenValue(true);
		}else{
			$('#councilcommittee')
	        .find('> tbody > tr > td:first-child > input[type="checkbox"]')
	        .prop('checked', false);
			setHiddenValue(false);
		}
	});
	

	$("#btnsubmit").click(function(e){ 
		
			var chkbxLength = $('.councilcommitmem:checked').length;
			if(chkbxLength <= 0){
				bootbox.alert('Please enter attendance details atleast for one member');
				return false;
			}
			return true;
	});  
	

	jQuery('#btnsearch').click(function(e) {
			
			callAjaxSearch();
		});

});

function setHiddenValue(flag)
{
	
	$('.councilcommitmem').each(function(){
		var $hiddenName=$(this).data('change-to');
		$('input[name="'+$hiddenName+'"]').val(flag);
	});
	
}

function getFormData($form){
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};
    $.map(unindexed_array, function(n, i){
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}

var viewurl='/council/councilmeeting/attendance/search/view/';
var editurl='/council/councilmeeting/attend/search/edit/';
function callAjaxSearch() {
	
	jQuery('.report-section').removeClass('display-hide');
	jQuery("#resultTable").dataTable({
				ajax : {
					url : "/council/councilmeeting/ajaxsearch/"+$('#mode').val(),      
					type: "POST",
					beforeSend : function() {
						$('.loader-class').modal('show', {
							backdrop : 'static'
						});
					},
					"data" : getFormData(jQuery('form')),
					complete : function() {
						$('.loader-class').modal('hide');
					}
				},
				"destroy": true,
				"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
				"aLengthMenu" : [[10,25,50,-1 ],[10,25,50,"All" ] ],
				"autoWidth" : false,
				"oTableTools" : {
					"sSwfPath" : "../../../>${action}../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ 
					               {
						             "sExtends": "pdf",
				                     "sPdfMessage": "",
				                     "sTitle": "Council Meeting Attendance Report",
				                     "sPdfOrientation": "landscape"
					                },
					                {
							             "sExtends": "xls",
				                         "sPdfMessage": "Council Meeting Attendance Report",
				                         "sTitle": "Council Meeting Attendance Report"
						             },
						             {
							             "sExtends": "print",
				                         "sTitle": "Council Meeting Attendance Report"
						             }],
			},
				aaSorting: [],				
				columns : [ { 
					"data" : "meetingDate", "sClass" : "text-left"},{ 
					"data" : "committeeType", "sClass" : "text-left",
					"render": function ( data, type, row, meta ) {
						return '<a target="_new" onclick="openPopup(\'/council/councilmeeting/attendance/search/view/'+ row.id +'\')">'+data+'</a>' 
					  }
					},
					{"data" : "totCommitteMemCount", "sClass" : "text-center"},
					{"data" : "noOfMembersPresent", "sClass" : "text-center"},
					{"data" : "noOfMembersAbsent", "sClass" : "text-center"},
					{"data" : "meetingStatus", "sClass" : "text-center"},
					{ "data" : null, "target":-1,
					    sortable: false,
					    "render": function ( data, type, row, meta ) {   
					    	if(row.meetingStatus == 'ATTENDANCE FINALIZED' || row.meetingStatus == 'MOM CREATED' || row.meetingStatus == 'MOM FINALISED'){
					    		return ('<select class="dropchange"><option value="">Select from Below</option><option  value='+viewurl+row.id +'>View</option>');
					    	}else{
					    		return ('<select class="dropchange"><option value="">Select from Below</option><option  value='+viewurl+row.id +'>View</option><option  value='+editurl+row.id +'>Edit</option></select>');
					    	}
					    }
					}
					,{ "data": "id", "visible":false },
					 { "data": "meetingStatus", "visible":false }
					]				
			});
}

$(document).on('change','.dropchange',function(){
    var url = $(this).val();
    if(url){
    	openPopup(url);
    }
    
});

function openPopup(url)
{
	window.open(url,'window','scrollbars=yes,resizable=yes,height=700,width=800,status=yes');
}

var isSubmit=false;

$('#finalizeAttendanceBtn').click(
       function(e) {

                   if(isSubmit)
                   {
                     return true;
                   }

                   if ($('form').valid()) {
                       var chkbxLength = $('.councilcommitmem:checked').length;
                       if(chkbxLength <= 0){
                           bootbox.alert('Please enter attendance details');
                       }else{
                           bootbox
                           .confirm({
                               message : 'Information entered in this screen will not be modified once submitted,Please confirm yes to save',
                               buttons : {
                                   'cancel' : {
                                       label : 'No',
                                       className : 'btn-danger pull-right'
                                   },
                                   'confirm' : {
                                       label : 'Yes',
                                       className : 'btn-danger pull-right'
                                   }
                               },
                               callback : function(result) {
                                   if (result) {
                                        var action = '/council/councilmeeting/attendance/finalizeattendance';
                                            $('#councilMeetingform').attr('method', 'post');
                                            $('#councilMeetingform').attr('action', action);
                                            isSubmit=true;
                                            $('#finalizeAttendanceBtn').trigger('click');
                                   } else {
                                       e.preventDefault();
                                   }
                               }
                           });
                       }
           }

           return false;

});

