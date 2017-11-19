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

var billamount = 0;
var debitamount = 0;
var creditamount = 0;
$(document).ready(function(){
	calculateBillAmount();
	});


$('.btn-primary').click(function(){
	var button = $(this).attr('id');
	if (button != null && button == 'Forward') {
		if(!$("form").valid())
			return false;
		if(validateWorkFlowApprover(button))
			return true;
		else
			return false;
	} else if(validateWorkFlowApprover(button))
		return true;
	else
		return false;
});


$('.btn-primary').click(function(){
	var button = $(this).attr('id');
	if(!validateWorkFlowApprover(button))
		return false;
	if($("form").valid()){
		return true;
	}else
		return false;
});


function calculateBillAmount(){
	billamount = 0;
	debitamount = 0;
	creditamount = 0;
	$('#tblaccountdetails  > tbody > tr:visible[id="accountdetailsrow"]').each(function() {
		billamount = parseFloat(Number(billamount) + Number($(this).find(".accountDetailsDebitAmount").html())).toFixed();
		debitamount = parseFloat(Number(debitamount) + Number($(this).find(".accountDetailsDebitAmount").html())).toFixed();
		creditamount = parseFloat(Number(creditamount) + Number($(this).find(".accountDetailsCreditAmount").html())).toFixed();
	});
	$("#expenseNetPayableAmount").html($("#netPayableAmount").val());
	$("#expenseBillTotalDebitAmount").html(debitamount);
	$("#expenseBillTotalCreditAmount").html(Number(Number(creditamount)-Number($("#netPayableAmount").val())));
	$("#billamount").val(billamount);
}

function validateWorkFlowApprover(name) {
	document.getElementById("workFlowAction").value = name;
	var approverPosId = document.getElementById("approvalPosition");
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
	return true;
}