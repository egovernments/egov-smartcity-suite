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
	
	$('#approvalDepartment').removeAttr('required');
	$('#approvalDesignation').removeAttr('required');
	$('#approvalPosition').removeAttr('required');
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

$('#Forward').click(function() {
	
	var flag = true;

	document.getElementById("workFlowAction").value = "Forward";
	
	$('#approvalDepartment').attr('required', 'required');
	$('#approvalDesignation').attr('required', 'required');
	$('#approvalPosition').attr('required', 'required');
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

function submitForm(workFlowAction) {
	$('.loader-class').modal('show', {backdrop: 'static'});
	var id = $('#id').val();
	var url = "";
	if(id != "")
		url = "/egworks/measurementbook/save/" + id;
	else
		url = "/egworks/measurementbook/create";
	
	$.ajax({
		type: "POST",
		url: url,
		cache: true,
		dataType: "json",
		"data": getFormData(jQuery('form')),
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
						sorCount++;
					} else {
						$('#nonSorMbDetailsId_' + nonSorCount).val(value.id);
						nonSorCount++;
					}
				});
				
				$('#successMessage').html(json.message);
			} else {
				$('#measurementBookDiv').remove();
				$('#forwardMessage').html(json.message);
				$('#successPage').show();
			}
		},
		error: function (error) {
			$('#tblsor').find('input, textarea').each(function() {
				$(this).removeAttr('disabled');
			});
			
			$('#tblNonSor').find('input, textarea').each(function() {
				$(this).removeAttr('disabled');
			});
			$('#successMessage').html("");
			$('#errorMessage').html("");
			console.log(error.responseText.slice(0,-2));
			var json = $.parseJSON(error.responseText.slice(0,-2));
			
			$.each(json, function(key, value){
				$('#errorMessage').append(value + '</br>');
			});
		}
	});
	$('.loader-class').modal('hide');
}