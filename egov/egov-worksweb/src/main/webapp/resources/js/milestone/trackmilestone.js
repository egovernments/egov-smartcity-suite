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

$detailsRowCount = $('#detailsSize').val();

function validateCompletedPercentage(percentage) {
    var valid = /^[1-9](\d{0,9})?$/.test(percentage.value);
    var val = percentage.value;
    if(!valid){
    	percentage.value = val.substring(0, val.length - 1);
    }
}

function validatePercentage(completedPercentage) {
	if(completedPercentage.value > 100) {
		bootbox.alert('Completion Percentage can not be greater than 100');
		completedPercentage.value = '';
	} else {
		rowcount = getRow(completedPercentage).rowIndex - 1;
		var percentage = $('.percentage_' + rowcount).html().trim();
		$('#actualPercentage_' + rowcount).val((completedPercentage.value / 100) * percentage);
	}
	var total = 0;
	$('.actualPercentage').each(function() {
		if($(this).val() != '')
			total = parseFloat(total) + parseFloat($(this).val());
	});
	$('#totalActualPercentage').html(total);
}

function getRow(obj) {
	if(!obj)return null;
	tag = obj.nodeName.toUpperCase();
	while(tag != 'BODY'){
		if (tag == 'TR') return obj;
		obj=obj.parentNode ;
		tag = obj.nodeName.toUpperCase();
	}
	return null;
}

function openLetterOfAcceptance() {
	var workOrderId = $('#workOrderId').val();
	window.open("/egworks/letterofacceptance/view/" + workOrderId, '','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}

$('#save').click(function() {
	var totalRows = $("#tblmilestone tbody tr").length;
	var flag = false;
	for(var i = 0; i < totalRows; i++) {
		var scheduleStartDate = $('#hiddenScheduleStartDate_' + i).val();
		var completionDate = $('#completionDate_' + i).data('datepicker').date;
		if($('#completionDate_' + i).val() != "") {
			if(completionDate.getTime() < new Date(scheduleStartDate).getTime()) {
				$('#completionDate_' + i).val("");
				flag = true;
			}
		}
	}
	if(flag)
		bootbox.alert('Completion Date should be greater than Schedule Start Date');
	else {
		if($('#trackMilestoneForm').valid()) {
			var milestoneId = $('#id').val();
			$.ajax({
				type: "POST",
				url: "/egworks/milestone/track/" + milestoneId,
				cache: true,
				dataType: "json",
				"data": getFormData(jQuery('form')),
				beforeSend : function(){
					$('.loader-class').modal('show', {backdrop: 'static'});
				},
				success: function (message) {
					$('#trackMilestoneDiv').remove();
					$('#successMessage').html(message);
					$('#successPage').show();
				},
				error: function (error) {
					console.log(error.responseText.slice(0,-2));
					var json = $.parseJSON(error.responseText.slice(0,-2));
					
					$.each(json, function(key, value){
						$('#errorMessage').append(value + '</br>');
					});
					$('#errorMessage').show();
				},
				complete : function(){
					$('.loader-class').modal('hide');
				}
			});
		}
		else
			return false;
	}
	
	return false;
});

function makeCompletionMandatory(currentStatus) {
	rowcount = getRow(currentStatus).rowIndex - 1;
	if($(currentStatus).val() == 'COMPLETED') {
		$('#completionDate_' + rowcount).attr('required', 'required');
		$('#completedPercentage_' + rowcount).attr('readonly', 'true');
		$('#completedPercentage_' + rowcount).val(100);
		$('#completedPercentage_' + rowcount).removeAttr('required');
	} else if($(currentStatus).val() == 'NOT_YET_STARTED') {
		$('#completedPercentage_' + rowcount).val(0);
		$('#completedPercentage_' + rowcount).attr('readonly', 'true');
		$('#completedPercentage_' + rowcount).removeAttr('required');
		$('#completionDate_' + rowcount).val("");
		$('#reasonForDelay_' + rowcount).removeAttr('required');
		$('#reasonForDelay_' + rowcount).val("");
		$('#reasonForDelay_' + rowcount).attr('readonly', 'true');
		$('#completionDate_' + rowcount).removeAttr('required');
	} else {
		$('#completedPercentage_' + rowcount).removeAttr('readonly');
		$('#completionDate_' + rowcount).val("");
		$('#reasonForDelay_' + rowcount).removeAttr('required');
		$('#reasonForDelay_' + rowcount).val("");
		$('#reasonForDelay_' + rowcount).attr('readonly', 'true');
		$('#completionDate_' + rowcount).removeAttr('required');
		$('#completedPercentage_' + rowcount).attr('required', 'required');
	}
	$('#completedPercentage_' + rowcount).trigger('blur');
}

function makeReasonMandatory(completionDate) {
	rowcount = getRow(completionDate).rowIndex - 1;
	var completionDate = $(completionDate).data('datepicker').date;
	var scheduleEndDate = $('#hiddenScheduleEndDate_' + rowcount).val();
	if(completionDate.getTime() > new Date(scheduleEndDate).getTime()) {
		$('#reasonForDelay_' + rowcount).removeAttr('readonly');
		$('#reasonForDelay_' + rowcount).attr('required', 'required');
	} else {
		$('#reasonForDelay_' + rowcount).removeAttr('required');
		$('#reasonForDelay_' + rowcount).val("");
		$('#reasonForDelay_' + rowcount).attr('readonly', 'true');
	}
}