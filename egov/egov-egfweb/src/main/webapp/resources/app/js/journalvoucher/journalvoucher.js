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

var voucheramount = 0;
var debitamount = 0;
var creditamount = 0;

var subLedgerAccountCodes ;

$(document).ready(function(){
	accountDetailGlcode_initialize();
	entityName_initialize(0);
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
		   	loadSubLedgerAccountCodes();
  });
}


function entityName_initialize(rowindex) {
	 var custom = new Bloodhound({
	    datumTokenizer: function(d) { return d.tokens; },
	    queryTokenizer: Bloodhound.tokenizers.whitespace,
		   remote: {
	            url: '/EGF/common/getentitesbyaccountdetailtype?name=',
	            replace: function (url, query) {
					var accountDetailType = $('#subLedgerDetails\\['+rowindex+'\\]\\.detailTypeId').val();
					if(accountDetailType == null || accountDetailType == ""){
						bootbox.alert("Please select Type");
					}
					return url + query + '&accountDetailType=' + accountDetailType ;
				},
	            dataType: "json",
	            filter : function(data) {
					return $.map(data, function(ct) {
						return {
							code:ct.split("~")[0].split("-")[0],
							name : ct.split("~")[0].split("-")[1],
							id : ct.split("~")[1],
							codeAndName:ct
						};
					});
				}
	        }
 });

 custom.initialize();
$('.subLedgerDetailKeyName').typeahead({
 	hint : true,
		highlight : true,
		minLength : 3
		
	}, {		    
       displayKey: 'codeAndName',
       source: custom.ttAdapter()
 }).on('typeahead:selected typeahead:autocompleted', function (event, data) {
		$(this).parents("tr:first").find('.subLedgerDetailDetailKeyId ').val(data.id);
	   	$(this).parents("tr:first").find('.subLedgerName').val(data.name);
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


function addSubLedgerRow() { 
	
	$('.subLedgerDetailKeyName').typeahead('destroy');
	$('.subLedgerDetailKeyName').unbind();
	var rowcount = $("#tblsubledger tbody tr").length;
	if (rowcount < 30) {
		if (document.getElementById('subledgerrow') != null) {
			addRow('tblsubledger','subledgerrow');
			$('#tblsubledger tbody tr:eq('+rowcount+')').find('.subLedgerAccountCode').val('');
			$('#tblsubledger tbody tr:eq('+rowcount+')').find('.subLedgerAccountDetailType').val('');
			$('#tblsubledger tbody tr:eq('+rowcount+')').find('.subLedgerDetailDetailKeyId').val('');
			$('#tblsubledger tbody tr:eq('+rowcount+')').find('.subLedgerDetailKeyName').val('');
			$('#tblsubledger tbody tr:eq('+rowcount+')').find('.subLedgerAmount').val('');
			$('#tblsubledger tbody tr:eq('+rowcount+')').find('.debitAmount').val('');
			$('#tblsubledger tbody tr:eq('+rowcount+')').find('.creditAmount').val('');
			entityName_initialize(rowcount);
		}
	} else {
		  bootbox.alert('limit reached!');
	}
}

function deleteSubLedgerRow(obj) {
	var rowcount=$("#tblsubledger tbody tr").length;
    if(rowcount<=1) {
		bootbox.alert("This row can not be deleted");
		return false;
	} else {
		deleteRow(obj,'tblsubledger');
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

function loadSubLedgerAccountCodes(){
	
	subLedgerAccountCodes = new Array();
	$('#tblaccountdetails  > tbody > tr:visible[id="accountdetailsrow"]').each(function() {
		var isSubledger = $(this).find(".accountglcodeissubledger").val();
		var glcode = $(this).find(".accountglcode").val();
		var glcodeId = $(this).find(".accountglcodeid").val();
		if(isSubledger && isSubledger!="false"){
			subLedgerAccountCodes.push(glcodeId + "-" + glcode);
		}
	});
	
	$('#tblsubledger  > tbody > tr:visible[id="subledgerrow"]').each(function(index) {
		$('#subLedgerDetails\\['+index+'\\]\\.generalLedgerId\\.glcodeId').empty();
		$('#subLedgerDetails\\['+index+'\\]\\.generalLedgerId\\.glcodeId').append($("<option value=''>Select from below</option>"));
		
		var subLedgerAccountCodesLength = subLedgerAccountCodes.length;
		for (i = 0; i < subLedgerAccountCodesLength; i++) {
			$('#subLedgerDetails\\['+index+'\\]\\.generalLedgerId\\.glcodeId').append($('<option>').text(subLedgerAccountCodes[i].split('-')[1]).attr('value', subLedgerAccountCodes[i].split('-')[0]));
		}
	});
	
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

$(document).on('change','.subLedgerAccountCode', function()
{
	var index = getRow(document.getElementById($(this).prop('name'))).rowIndex;
	loadAccountDetailTypesByGlcodeId($(this).val(),index-1);
});


function loadAccountDetailTypesByGlcodeId(glcodeId,rowindex){
	if (!glcodeId) {
		$('#subLedgerDetails\\['+rowindex+'\\]\\.detailTypeId').empty();
		$('#subLedgerDetails\\['+rowindex+'\\]\\.detailTypeId').append($('<option>').text('Select from below').attr('value', ''));
		return;
	} else {
		
		$.ajax({
			method : "GET",
			url : "/EGF/common/getaccountdetailtypesbyglcodeid",
			data : {
				glcodeId : glcodeId
			},
			async : true
		}).done(
				function(response) {
					$('#subLedgerDetails\\['+rowindex+'\\]\\.detailTypeId').empty();
					$('#subLedgerDetails\\['+rowindex+'\\]\\.detailTypeId').append($("<option value=''>Select from below</option>"));
					$.each(response, function(index, value) {
						$('#subLedgerDetails\\['+rowindex+'\\]\\.detailTypeId').append($('<option>').text(value.name).attr('value', value.id));
					});
				});

	}
}