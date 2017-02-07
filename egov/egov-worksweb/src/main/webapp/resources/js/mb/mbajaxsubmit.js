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
$('#Save').click(function() {
	
	var flag = true;

	document.getElementById("workFlowAction").value = "Save";
	
	removeApprovalMandatoryAttribute();
	$('#approvalComent').removeAttr('required');
	
	if($('#mbHeader').valid()) {
		flag = validateFormData();
		if(!flag)
			return false;
	}
	
	validateSORDetails();
	
	if($('#mbHeader').valid() && flag) {
		submitForm("Save");
	}
	else
		return false;
	
	return false;
});

$('#CreateAndApprove').click(function() {
	
	var flag = true;

	document.getElementById("workFlowAction").value = "";
	
	removeApprovalMandatoryAttribute();
	$('#approvalComent').removeAttr('required');
	
	if($('#mbHeader').valid()) {
		flag = validateFormData();
		if(!flag)
			return false;
	}

	var mbDate = $('#mbDate').data('datepicker').date;
	var cutOffDate = $('#cutOffDate').val(); 
	var cutOffDateDisplay = $('#cutOffDateDisplay').val();
	if(cutOffDate != '' && $('#mbDate').val() != '') {
	if(mbDate.getTime() > new Date(cutOffDate).getTime()) {
		bootbox.alert($('#cuttoffdateerrormsg1').val() + ' ' +cutOffDateDisplay+ '.'+'</br>'+$('#cuttoffdateerrormsg2').val());
		$('#mbDate').val('');
		return false;
	 }
	}
	
	validateSORDetails();
	
	if($('#mbHeader').valid() && flag) {
		submitForm("");
	}
	else
		return false;
	
	return false;
});

$('#Forward').click(function() {
	
	var flag = true;

	document.getElementById("workFlowAction").value = "Forward";
	
	flag = showHideApprovalDetails("Forward");
	if(!flag) {
		return false;
	}
	
	addApprovalMandatoryAttribute();
	$('#approvalComent').removeAttr('required');
	
	if($('#mbHeader').valid()) {
		flag = validateFormData();
		if(!flag)
			return false;
	}
	
	validateSORDetails();
	
	if($('#mbHeader').valid() && flag) {
		submitForm("Forward");
	}
	else
		return false;
	
	return false;
});


$('#Cancel').click(function() {
	
	var flag = true;

	document.getElementById("workFlowAction").value = "Cancel";
	
	removeApprovalMandatoryAttribute();
	$('#approvalComent').attr('required', 'required');
	
	if($('#mbHeader').valid()) {
		flag = validateFormData();
		if(!flag)
			return false;
	}

	
	if($('#mbHeader').valid() && flag) {
		bootbox.confirm($('#cancelConfirm').val(), function(result) {
			if(!result) {
				bootbox.hideAll();
				return false;
			} else {
				submitForm("Cancel");
			}
		});
		
	}
	else
		return false;
	
	return false;
});

$('#Create\\ And\\ Approve').click(function() {
	
	var flag = true;

	document.getElementById("workFlowAction").value = "Create And Approve";
	
	flag = showHideApprovalDetails("Create And Approve");
	if(!flag) {
		return false;
	}
	removeApprovalMandatoryAttribute();
	$('#approvalComent').removeAttr('required');
	
	if($('#mbHeader').valid()) {
		flag = validateFormData();
		if(!flag)
			return false;
	}

	validateSORDetails();
	
	if($('#mbHeader').valid() && flag) {
		submitForm("Create And Approve");
	}
	else
		return false;
	
	return false;
});


function submitForm(workFlowAction) {
	var id = $('#id').val();
	var url = "";
	if (id != "")
		url = "/egworks/measurementbook/save/" + id;
	else
		url = "/egworks/measurementbook/create";
	$("#mbHeaderTab").find('input,button,textarea').prop('disabled',false);
	oData = new FormData(document.forms.namedItem("mbHeader"));
	$.ajax({
		type: "POST",
		url: url,
		async: false,
	    cache: false,
	    contentType: false,
	    processData: false,
		data: oData,
		beforeSend : function(){
			$('.loader-class').modal('show', {
				backdrop : 'static'
			});
		},
		success: function (message) {
			var json = $.parseJSON(message);
			if(workFlowAction == "Save") {
				$('#removedDetailIds').val("");
				$('#tblsor').find('input, textarea').each(function() {
					$(this).removeAttr('disabled');
				});
				
				$('#tblNonSor').find('input, textarea').each(function() {
					$(this).removeAttr('disabled');
				});
				
				$('#tblNonTendered').find('input, textarea').each(function() {
					$(this).removeAttr('disabled');
				});
				
				$('#tblLumpSum').find('input, textarea').each(function() {
					$(this).removeAttr('disabled');
				});
				
				$('.clearthis').each(function() {
					$(this).attr('value', '');
				});
				
				var sorCount = 0;
				var nonSorCount = 0;
				
				$('#id').val(json.id);
				$('#stateType').val(json.stateType);
				$('#currentState').val(json.currentState);
				$('#nextAction').val(json.nextAction);
				
				$.each(json.detailIds, function(key, value){
					$('#successMessage').html("");
					$('#errorMessage').html("");
					if(value.sorType == "SOR") {
						$('#sorMbDetailsId_' + sorCount).val(value.id);
						$.each(value.msIds, function(idx, val){
							$('#sorMbDetails_' + sorCount + '_measurementSheets_' + idx + '_id').attr('value', val.id);
						});
						sorCount++;
					} else {
						$('#nonSorMbDetailsId_' + nonSorCount).val(value.id);
						$.each(value.msIds, function(idx, val){
							$('#nonSorMbDetails_' + nonSorCount + '_measurementSheets_' + idx + '_id').attr('value', val.id);
						});
						nonSorCount++;
					}
				});
				
				$('#successMessage').html(json.message);
				$('#Cancel').prop('type',"submit");
				$('#CreateAndApprove').prop('type',"hidden");
			} else {
				$('#measurementBookDiv').remove();
				$('#forwardMessage').html(json.message);
				$('#successPage').show();
			}
			window.scrollTo(0, 0);
		},
		error: function (error) {
			$('#tblsor').find('input, textarea').each(function() {
				$(this).removeAttr('disabled');
			});
			
			$('#tblNonSor').find('input, textarea').each(function() {
				$(this).removeAttr('disabled');
			});
			
			$('#tblNonTendered').find('input, textarea').each(function() {
				$(this).removeAttr('disabled');
			});
			
			$('#tblLumpSum').find('input, textarea').each(function() {
				$(this).removeAttr('disabled');
			});
			$('#successMessage').html("");
			$('#errorMessage').html("");
			console.log(error.responseText.slice(0,-2));
			var json = $.parseJSON(error.responseText.slice(0,-2));
			
			$.each(json, function(key, value){
				$('#errorMessage').append(value + '</br>');
			});
			window.scrollTo(0, 0);
		},
		complete : function(){
			$('.loader-class').modal('hide');
		}
	});
}