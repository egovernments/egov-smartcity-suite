var voucheramount = 0;
var debitamount = 0;
var creditamount = 0;
$(document).ready(function(){
	
});

$('#voucherSubType').change(function () {
	makePartyNameMandatory($('#voucherSubType').val());
});

function makePartyNameMandatory(voucherSubType){
	
	if(voucherSubType){
		if(voucherSubType != 'JVGeneral'){
			$('#partyNameLabelId').removeClass('hide');
			$('#partyName').prop("required","required");
		}else{
			$('#partyNameLabelId').addClass('hide');
			$('#partyName').removeAttr("required");
		}
	}
}