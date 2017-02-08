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

var $bankAccountId = 0;
$(document).ready(
		function() {
			$bankAccountId = $('#bankaccount').val();
			var value = "cheque";
			$("input[name=modeOfPayment][value=" + value + "]").attr('checked',
					'checked');
			
			$('#bankbranch').change(
					function() {
						$bankAccountId = "";
						$('#bankaccount').empty();
						$('#bankaccount').append(
								$('<option>').text('Select from below').attr('value', ''));
						if ($('#bankbranch').val() != '')
							loadBankAccounts($('#bankbranch').val(), $('#fundId').val());
					});
			$('#bankaccount').change(function() {
				$bankAccountId = "";
				if ($('#bankaccount').val() != '' && $('#bankaccount').val() != null)
					loadBankBalance($('#bankaccount').val(), $('#voucherDate').val());
			});

			$('.btn-wf-primary').click(function() {
				var button = $(this).attr('id');
				if (!validate()) {
					return false;
				} else if (button != null && (button == 'Forward')) {
					if (!validateWorkFlowApprover(button))
						return false;
					if (!$("form").valid())
						return false;

				} else if (button != null && (button == 'Create And Approve')) {
					$('#approvalDepartment').removeAttr('required');
					$('#approvalDesignation').removeAttr('required');
					$('#approvalPosition').removeAttr('required');
					$('#approvalComent').removeAttr('required');
					if (!validateWorkFlowApprover(button))
						return false;
					return true
				}
		});
			
});


function loadBankAccounts(bankBranchId, fundId) {
	$.ajax({
		method : "GET",
		url : "/EGF/common/getaccountnobybranchidforpayment",
		data : {
			bankBranchId : bankBranchId,
			fundId : fundId
		},
		async : true
	}).done(
			function(response) {
				$('#bankaccount').empty();
				$('#bankaccount').append(
						$("<option value=''>Select from below</option>"));
				$.each(response, function(index, value) {
					var selected = "";
					if ($bankAccountId && $bankAccountId == value.id) {
						selected = "selected";
					}
					$('#bankaccount').append(
							$('<option ' + selected + '>').text(
									value.accountnumber + "-"
											+ value.bankbranch.bank.name).attr(
									'value', value.id));
				});
			});
}

function loadBankBalance(bankAccountId, voucherDate) {
	$.ajax({
		method : "GET",
		url : "/EGF/common/getaccountbalance",
		data : {
			bankAccountId : bankAccountId,
			voucherDate : voucherDate
		},
		async : false
	}).done(function(response) {
		$('#paymentbalance').val(response);
	});
}

function viewBillVoucher(billVoucherId) {
	window.open('/EGF/voucher/preApprovedVoucher-loadvoucherview.action?vhid='
			+ billVoucherId, '',
			'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}
function viewBill(billSourcePath) {
	window.open(billSourcePath, '',
			'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

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
	}
	if (button != null && button == 'Forward') {
		$('#approvalDepartment').attr('required', 'required');
		$('#approvalDesignation').attr('required', 'required');
		$('#approvalPosition').attr('required', 'required');
		$('#approvalComent').removeAttr('required');
	}
	if (button != null && button == 'Approve') {
		$('#approvalComent').removeAttr('required');
	}
	if (button != null && button == 'Create And Approve') {
		return validateCutOff();
	} else
		return true;
}
function validate() {
	if ($('#bankaccount').val() == '') {
		bootbox.alert($('#errorSelectBank').val());
		return false;
	}
	return true;
}

function validateCutOff() {
	var cutofdate = $("#cutOffDate").val();
	var voucherdate = $("#voucherDate").val();
	var cutOffDateArray = cutofdate.split("/");
	var billDateArray = voucherdate.split("/");
	var cutOffDate = new Date(cutOffDateArray[1] + "/" + cutOffDateArray[0]
			+ "/" + cutOffDateArray[2]);
	var voucherDate = new Date(billDateArray[1] + "/" + billDateArray[0] + "/"
			+ billDateArray[2]);
	if (voucherDate <= cutOffDate) {
		return true;
	} else
		bootbox.alert("Vouchers created after " + cutofdate
				+ " cannot be approved on create. Use the Forward option.");
	return false;
}
