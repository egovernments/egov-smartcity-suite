var voucheramount = 0;
var debitamount = 0;
var creditamount = 0;
$(document).ready(function(){
	accountDetailGlcode_initialize();
});

$('#voucherSubType').change(function () {
	makePartyNameMandatory($('#voucherSubType').val());
});

function makePartyNameMandatory(voucherSubType){
	
	if(voucherSubType){
		if(voucherSubType != 'JVGeneral'){
			$('#partyNameLabelId').removeClass('hide');
			$('#partyName').prop("required","required");
			$('#partyName').removeAttr("disabled");
			$('#partyBillNumber').removeAttr("disabled");
			$('#partyBillDate').removeAttr("disabled");
			$('#billNumber').removeAttr("disabled");
			$('#billDate').removeAttr("disabled");
		}else{
			$('#partyNameLabelId').addClass('hide');
			$('#partyName').removeAttr("required");
			$('#partyName').prop("disabled","disabled");
			$('#partyBillNumber').prop("disabled","disabled");
			$('#partyBillDate').prop("disabled","disabled");
			$('#billNumber').prop("disabled","disabled");
			$('#billDate').prop("disabled","disabled");
		}
	}else{
		$('#partyNameLabelId').addClass('hide');
		$('#partyName').removeAttr("required");
		$('#partyName').removeAttr("disabled");
		$('#partyBillNumber').removeAttr("disabled");
		$('#partyBillDate').removeAttr("disabled");
		$('#billNumber').removeAttr("disabled");
		$('#billDate').removeAttr("disabled");
	}
}

function accountDetailGlcode_initialize() {
	 var custom = new Bloodhound({
	    datumTokenizer: function(d) { return d.tokens; },
	    queryTokenizer: Bloodhound.tokenizers.whitespace,
		   remote: {
	            url: '/EGF/common/getallaccountcodes?glcode=',
	            replace: function (url, query) {
					return url + query ;
				},
	            dataType: "json",
	            filter: function (data) {
	            	var responseObj = JSON.parse(data);
	                return $.map(responseObj, function (ct) {
	                    return {
	                        id: ct.id,
	                        name: ct.name,
	                        glcode: ct.glcode,
	                        issubledger: ct.isSubLedger,
	                        glcodesearch: ct.glcode+' ~ '+ct.name
	                    };
	                });
	            }
	        }
  });

  custom.initialize();
 $('.accountDetailGlcode').typeahead({
  	hint : true,
		highlight : true,
		minLength : 3
		
	}, {		    
        displayKey: 'glcodesearch',
        source: custom.ttAdapter()
  }).on('typeahead:selected typeahead:autocompleted', function (event, data) {
			$(this).parents("tr:first").find('.accountglname').val(data.name);
		   	$(this).parents("tr:first").find('.accountglcode').val(data.glcode);
		   	$(this).parents("tr:first").find('.accountglcodeid').val(data.id);
		   	$(this).parents("tr:first").find('.accountglcodeissubledger').val(data.issubledger);
  });
}

function addAccountDetailsRow() { 
	
	$('.accountDetailGlcode').typeahead('destroy');
	$('.accountDetailGlcode').unbind();
	var rowcount = $("#tblaccountdetails tbody tr").length;
	if (rowcount < 30) {
		if (document.getElementById('accountdetailsrow') != null) {
			addRow('tblaccountdetails','accountdetailsrow');
			$('#tblaccountdetails tbody tr:eq('+rowcount+')').find('.accountDetailGlcode').val('');
			$('#tblaccountdetails tbody tr:eq('+rowcount+')').find('.accountglname').val('');
			$('#tblaccountdetails tbody tr:eq('+rowcount+')').find('.accountglcode').val('');
			$('#tblaccountdetails tbody tr:eq('+rowcount+')').find('.accountglcodeid').val('');
			$('#tblaccountdetails tbody tr:eq('+rowcount+')').find('.accountglcodeissubledger').val('');
			$('#tblaccountdetails tbody tr:eq('+rowcount+')').find('.debitAmount').val('');
			$('#tblaccountdetails tbody tr:eq('+rowcount+')').find('.creditAmount').val('');
			accountDetailGlcode_initialize();
		}
	} else {
		  bootbox.alert('limit reached!');
	}
}

function deleteAccountDetailsRow(obj) {
	var rowcount=$("#tblaccountdetails tbody tr").length;
    if(rowcount<=1) {
		bootbox.alert("This row can not be deleted");
		return false;
	} else {
		deleteRow(obj,'tblaccountdetails');
		return true;
	}
}


function calculateDebitAmount(obj){
	debitamount = parseFloat(Number(debitamount) + Number(obj.value)).toFixed();
	voucheramount = parseFloat(Number(voucheramount) + Number(obj.value)).toFixed();
	$("#journalVoucherAmount").html(voucheramount);
	$("#journalVoucherTotalDebitAmount").html(debitamount);
	$("#voucheramount").val(voucheramount);
}

function calculateCreditAmount(obj){
	creditamount = parseFloat(Number(creditamount) + Number(obj.value)).toFixed();
	$("#journalVoucherTotalCreditAmount").html(creditamount);
}

$('.btn-wf-primary').click(function(){
	var button = $(this).attr('id');
	if (button != null && (button == 'Create And Approve')) {
		$('#approvalDepartment').removeAttr('required');
		$('#approvalDesignation').removeAttr('required');
		$('#approvalPosition').removeAttr('required');
		$('#approvalComent').removeAttr('required');
		if(!validateWorkFlowApprover(button))
			return false;
		if(!$("form").valid())
			return false;
		else
			return false;
	} else{
		if(!validateWorkFlowApprover(button))
			return false;
		if($("form").valid()){
			return true;
		}else
			return false;
	}
	return false;
});

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
	if (button != null && button == 'Create And Approve') {
		return validateCutOff();
	}else
		return true;
	
	return true;
}
function validateCutOff()
{
	var cutofdate = $("#cutOffDate").val();
	var voucherdate = $("#voucherDate").val();
	var cutOffDateArray=cutofdate.split("/");
	var voucherDateArray=voucherdate.split("/");
	var cutOffDate = new Date(cutOffDateArray[1] + "/" + cutOffDateArray[0] + "/"
			+ cutOffDateArray[2]);
	var billDate = new Date(voucherDateArray[1] + "/" + voucherDateArray[0] + "/"
			+ voucherDateArray[2]);
	if(billDate<=cutOffDate)
	{
		return true;
	}
	else
	{
		bootbox.alert("Vouchers created after "+cutofdate+" cannot be approved on create. Use the Forward option.");
		return false;
	}
	return false;
}