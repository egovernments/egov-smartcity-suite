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