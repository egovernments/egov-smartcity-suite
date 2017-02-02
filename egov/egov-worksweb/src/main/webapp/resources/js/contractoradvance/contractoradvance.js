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
	$('#debitGlcodeId').change(function() {
		if($(this).val() == '')
			$('#debitAccountHead').val('');
		else {
			var accountHead = $("#debitGlcodeId option:selected").text().split('-').slice(1).join('-');
			$('#debitAccountHead').val(accountHead);
		}
	});

	$('#creditGlcodeId').change(function() {
		if($(this).val() == '')
			$('#creditAccountHead').val('');
		else {
			var accountHead = $("#creditGlcodeId option:selected").text().split('-').slice(1).join('-');
			$('#creditAccountHead').val(accountHead);
		}
	});

	$('#debitamount').change(function() {
		if ($(this).val() > 0) {
			$('#creditamount').val(parseFloat($(this).val()).toFixed(2));
			$('#advanceRequisitionAmount').val(parseFloat($(this).val()).toFixed(2));
			$('#advanceAmount').html(parseFloat($(this).val()).toFixed(2));
		} else {
			$('#creditamount').val('');
			$('#advanceRequisitionAmount').val('');
			$('#advanceAmount').html('');
		}
	});
	
	var debitGlcodeId = $('#debitGlcodeId').val();
	$("#debitGlcodeId").each(function() {
		if($(this).children('option').length == 2) {
		 	$(this).find('option').eq(1).prop('selected', true);
		} else {
			$(this).val(debitGlcodeId);
		}
	});
	
	var creditGlcodeId = $('#creditGlcodeId').val();
	$("#creditGlcodeId").each(function() {
		if($(this).children('option').length == 2) {
		 	$(this).find('option').eq(1).prop('selected', true);
		} else {
			$(this).val(creditGlcodeId);
		}
	});
	
	$('#debitGlcodeId').trigger('change');
	$('#creditGlcodeId').trigger('change');
	var defaultDepartmentId = $("#defaultDepartmentId").val();
	if(defaultDepartmentId != "")
		$("#approvalDepartment").val(defaultDepartmentId);
});

function validateWorkFlowApprover(name) {
	document.getElementById("workFlowAction").value = name;
	var button = document.getElementById("workFlowAction").value;
	if (button != null && button == 'Submit') {
		$('#approvalDepartment').attr('required', 'required');
		$('#approvalDesignation').attr('required', 'required');
		$('#approvalPosition').attr('required', 'required');
		$('#approvalComent').removeAttr('required');
	}
	if (button != null && button == 'Reject') {
		$('#approvalDepartment').removeAttr('required');
		$('#approvalDesignation').removeAttr('required');
		$('#approvalPosition').removeAttr('required');
		$('#approvalComent').attr('required', 'required');
	}
	if (button != null && button == 'Cancel') {
		$('#approvalDepartment').removeAttr('required');
		$('#approvalDesignation').removeAttr('required');
		$('#approvalPosition').removeAttr('required');
		$('#approvalComent').attr('required', 'required');
		if($("form").valid())
		{
			bootbox.confirm($('#cancelConfirm').val(), function(result) {
				if(!result) {
					bootbox.hideAll();
					return false;
				} else {
					document.forms[0].submit();
				}
			});
		}
		window.scrollTo(0, 0);
		return false;
	}
	if (button != null && button == 'Forward') {
		$('#approvalDepartment').attr('required', 'required');
		$('#approvalDesignation').attr('required', 'required');
		$('#approvalPosition').attr('required', 'required');
		$('#approvalComent').removeAttr('required');
		if(!$("form").valid())
		{
			window.scrollTo(0, 0);
			return false;
		}
	}
	if (button != null && button == 'Approve') {
		$('#approvalComent').removeAttr('required');
	}
	if(validateContractorAdvance()) {
		document.forms[0].submit;
		return true;
	} else {
		window.scrollTo(0, 0);
		return false;
	}
}

function validateContractorAdvance() {
	var advancePaidTillNow = $('#advancePaidTillNow').val() == "" ? 0 : parseFloat($('#advancePaidTillNow').val());
	var workOrderAmount = $('#workOrderAmount').val() == "" ? 0 : parseFloat($('#workOrderAmount').val());
	var totalPartBillsAmount = $('#totalPartBillsAmount').val() == "" ? 0 : parseFloat($('#totalPartBillsAmount').val());
	var advanceRequisitionAmount = $('#advanceRequisitionAmount').val() == "" ? 0 : parseFloat($('#advanceRequisitionAmount').val());
	
	if (advanceRequisitionAmount <= 0) {
		bootbox.alert($('#errorAdvanceZero').val());
		return false;
	}
	if (workOrderAmount < (advancePaidTillNow + advanceRequisitionAmount + totalPartBillsAmount)) {
		var message = $('#errorAdvanceExceeded').val();
		message = message.replace('{0}', parseFloat(advancePaidTillNow + advanceRequisitionAmount + totalPartBillsAmount - workOrderAmount).toFixed(2))
		bootbox.alert(message);
		return false;
	}
	return true;
}

function viewLOA(workOrderId) {
	window.open("/egworks/letterofacceptance/view/"+ workOrderId , "", "height=650,width=980,scrollbars=yes,left=0,top=0,status=yes");
}

function viewLineEstimate(lineEstimateID) {
	window.open("/egworks/lineestimate/view/"+ lineEstimateID , "", "height=650,width=980,scrollbars=yes,left=0,top=0,status=yes");
}

function viewAbstractEstimate(abstractEstimateID) {
	window.open("/egworks/abstractestimate/view/"+ abstractEstimateID , "", "height=650,width=980,scrollbars=yes,left=0,top=0,status=yes");
}

$('#viewAdvanceBill').click(function(){
	var sourcePath = $('#sourcePath').val();
	window.open(sourcePath , "", "height=650,width=980,scrollbars=yes,left=0,top=0,status=yes");
});