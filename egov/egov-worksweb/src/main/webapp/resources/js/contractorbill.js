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
$(document).ready(function(){
	
	creditGlcode_initialize();	
	replaceBillTypeChar();
	calculateNetPayableAmount();
	
	// TODO: remove this condition
	if($('#hiddenbilldate').val() != '') {
		$('#billdate').datepicker('setDate',new Date($('#hiddenbilldate').val()));	
	}
	if($('#workCompletionDate').val() != '' || $('#billtype').val() == 'Final Bill') {
		$(".workcompletion").show();
	} else 
		$(".workcompletion").hide();
	
	var currentState = $('#currentState').val();
	if(currentState == 'Created') {
		$('#approverDetailHeading').hide();
		
		$('#approvalDepartment').removeAttr('required');
		$('#approvalDesignation').removeAttr('required');
		$('#approvalPosition').removeAttr('required');
	}
		
	function replaceBillTypeChar() { 
		$('#billtype option').each(function() {
		   var $this = $(this);
		   if($this.val() != '') {
			   var billType = $this.text().replace(/_/g, ' ');
			   $this.text(billType);
			   $this.val(billType);
		   }
		});
	}
				
	$('.btn-primary').click(function(){
		var button = $(this).attr('id');
		if (button != null && button == 'Forward') {
			//TODO: remove code till billdate < workOrderDate condition check
			var billType = $('#billtype').val();
			var workOrderEstimateId = $('#workOrderEstimateId').val();
			var billDate = $('#billdate').data('datepicker').date;
			var workOrderDate = $('#workOrderDate').data('datepicker').date;
			var workCompletionDate = $('#workCompletionDate').data('datepicker').date;
			var currentDate = new Date();
			var currentFinYearDate;
			var cutOffDate;
			if(currentDate.getMonth() == 0 || currentDate.getMonth() == 1 || currentDate.getMonth() == 2) {
				currentDate = new Date(currentDate.getFullYear() - 1, 3, 1);
			}
			if($("#isSpillOver").val() =="true" && $("#defaultCutOffDate").val() != null && $("#defaultCutOffDate").val().length != 0){
				var cutOffDateString = $("#defaultCutOffDate").val().split("-");
				cutOffDate = new Date(cutOffDateString[2],cutOffDateString[1]-1,cutOffDateString[0]);
			}
			else
				currentFinYearDate = new Date(currentDate.getFullYear(), 3, 1);
			
			if(billDate < cutOffDate) {
				bootbox.alert($('#errorBillCutOffDate').val() +" " +$("#defaultCutOffDate").val());
				$('#billdate').val(""); 
				return false;
			}
			
			if(billDate < currentFinYearDate) {
				bootbox.alert($('#errorBillDateFinYear').val());
				$('#billdate').val(""); 
				return false;
			}
			
			if(billDate < workOrderDate) {
				bootbox.alert($('#errorBillDateWorkOrder').val());
				$('#billdate').val(""); 
				return false;
			}
			if(billType == 'Final Bill') {
				$('#workCompletionDate').attr('required', 'required');
				
				if($('#workCompletionDate').val() != '') {
				if(workCompletionDate > billDate) {
					bootbox.alert($('#errorWorkCompletionDateGreaterThanBillDate').val());
					$('#workCompletionDate').val(""); 
					return false;
				}
				
				if(workCompletionDate < workOrderDate) {
					bootbox.alert($('#errorWorkCompletionDateGreaterThanWorkOrderDate').val());
					$('#workCompletionDate').val(""); 
					return false;
				}
				
				if(workCompletionDate > new Date()) {
					bootbox.alert($('#errorWorkCompletionDateFutureDate').val());
					$('#workCompletionDate').val(""); 
					return false;
				}
				}
				
			}
			
			var debitamount = 0;
			$( "input[name$='debitamount']" ).each(function(){
				debitamount = debitamount + parseFloat(($(this).val()?$(this).val():"0"));
			});
			$('#billamount').val(debitamount);
			if($('#partyBillDate').val() != '') { 
				var workOrderDate = $('#workOrderDate').data('datepicker').date;
				var partyBillDate = $('#partyBillDate').data('datepicker').date;
				if(workOrderDate > partyBillDate) {
					bootbox.alert($('#errorPartyBillDate').val());
					$('#partyBillDate').val(""); 
					return false;
				}
				//TODO: remove this condition
				if(partyBillDate > billDate) {
					bootbox.alert($('#errorPartyBillDateBillDate').val());
					$('#partyBillDate').val(""); 
					return false;
				}
			}	
			if($('#mbDate').val() != '') { 
				var workOrderDate = $('#workOrderDate').data('datepicker').date;
				var mbDate = $('#mbDate').data('datepicker').date;
				if(workOrderDate > mbDate) {
					bootbox.alert($('#errorMBDate').val());
					$('#mbDate').val("");
					return false;
				}
			}	
			if(!validateMBPageNumbers())
				return false;
			if(!validateNetPayableAmount())
				return false;
			if(!validateCreditAndDebitAmount())
				return false;
			return validateWorkFlowApprover(button);
		}
		return validateWorkFlowApprover(button);
	});
	
	var netPayableAccountCodeId = $('#netPayableAccountCodeId').val();
	$("#netPayableAccountCode").each(function() {
		if($(this).children('option').length == 2) {
		 	$(this).find('option').eq(1).prop('selected', true);
		} else {
			$(this).val(netPayableAccountCodeId);
		}
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
		$('.creditGlcode').removeAttr('required');
		$('.creditAmount').removeAttr('required');
		//document.forms[0].submit;
		return true;
	}
	
	
	$("#tblrefunddetails tbody tr").each(function() {
		hiddenElem=$(this).find("input:hidden").val();
		$(this).find("select").val(hiddenElem);
	});
	
});

function addDeductionRow() { 
	
	$('.creditGlcode').typeahead('destroy');
	
	var rowcount = $("#tblcreditdetails tbody tr").length;
	if (rowcount < 30) {
		if (document.getElementById('deductionRow') != null) {
			// get Next Row Index to Generate
			var nextIdx = 0;
			nextIdx = rowcount + 1;
			// validate status variable for exiting function
			var isValid = 1;// for default have success value 0

			// validate existing rows in table
			$("#tblcreditdetails tbody tr").find('input').each(
					function() {
						if (($(this).data('optional') === 0)
								&& (!$(this).val()) && !($(this).attr('name')===undefined)) { 
							$(this).focus();
							bootbox.alert($(this).data('errormsg'));
							isValid = 0;// set validation failure
							return false;
						}
			});
			if (isValid === 0) {
				return false;
			}
			
			// Generate all textboxes Id and name with new index
			$("#deductionRow").clone().find("input, errors").each(
			function() {	
				if ($(this).data('server')) {
					$(this).removeAttr('data-server');
				}
				
				$(this).attr(
						{
							'name' : function(_, name) {
								if(!($(this).attr('name')===undefined))
									return name.replace(/\d+/, nextIdx); 
							},
							'id' : function(_, id) {
								if(!($(this).attr('id')===undefined))
									return id.replace(/\d+/, nextIdx); 
							},
							'data-idx' : function(_,dataIdx)
							{
								return nextIdx;
							}
						});
	
					// if element is static attribute hold values for next row, otherwise it will be reset
					if (!$(this).data('static')) {
						$(this).val('');
					}
	
			}).end().appendTo("#tblcreditdetails tbody");		
			
			creditGlcode_initialize();
			
		}
	} else {
		  bootbox.alert('limit reached!');
	}
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

function deleteDeductionRow(obj) {
    var rIndex = getRow(obj).rowIndex;
    
    var id = $(getRow(obj)).children('td:first').children('input:first').val();
    //To get all the deleted rows id
    var aIndex = rIndex - 1;
	var tbl=document.getElementById('tblcreditdetails');	
	var rowcount=$("#tblcreditdetails tbody tr").length;
    if(rowcount<=1) {
		bootbox.alert("This row can not be deleted");
		return false;
	} else {
		tbl.deleteRow(rIndex);
		//starting index for table fields
		var idx=1;
		//regenerate index existing inputs in table row
		$("#tblcreditdetails tbody tr").each(function() {
				$(this).find("input, errors").each(function() {
					   $(this).attr({
					      'name': function(_, name) {
					    	  if(!($(this).attr('name')===undefined))
					    		  return name.replace(/\[.\]/g, '['+ idx +']'); 
					      },
					      'id': function(_, id) {
					    	  if(!($(this).attr('id')===undefined))
					    		  return id.replace(/\[.\]/g, '['+ idx +']'); 
					      },
						  'data-idx' : function(_,dataIdx)
						  {
							  return idx;
						  }
					   });
			    });
				idx++;
			});
		calculateNetPayableAmount();
		return true;
	}	
}

function calculateNetPayableAmount(){
	var debitAmount = 0;
	var totalDeductionAmount = 0;
	$( "input[name$='creditamount']" ).each(function(){
		totalDeductionAmount = totalDeductionAmount + parseFloat(($(this).val()?$(this).val():"0"));
	});
	$( "input[name$='debitamount']" ).each(function(){
		debitAmount = debitAmount + parseFloat(($(this).val()?$(this).val():"0"));
	});
	$('#netPayableAmount').val(roundTo(debitAmount-totalDeductionAmount));
	validateNetPayableAmount();
}	

function validateMBPageNumbers() { 
	if(($('#fromPageNo').val() != '' && $('#fromPageNo').val() == 0) || ($('#toPageNo').val() != '' && $('#toPageNo').val() == 0)) {
		bootbox.alert("MB Page Numbers should be greater than Zero!");
		return false;
	}
	if($('#fromPageNo').val() != '' && $('#toPageNo').val() != '' && eval($('#fromPageNo').val()) > eval($('#toPageNo').val())) {
		bootbox.alert("MB From Page Number cannot be greater than MB To Page Number!");
		return false;
	}
	return true;
}

function validateNetPayableAmount() {
	if($('#debitamount').val() != '' && $('#netPayableAmount').val() <= 0) {
		bootbox.alert("Net Payable amount should be greater than zero!");
		return false;
	}
	return true;
}

$(document).on('keyup','.validateZero', function(){
  var valid = /^[1-9](\d{0,9})(\.\d{0,2})?$/.test(this.value),
  val = this.value;
  
  if(!valid){
    console.log("Invalid input!");
    this.value = val.substring(0, val.length - 1);
   }
});


function roundTo(value, decimals, decimal_padding) {
	if (!decimals)
		decimals = 2;
	if (!decimal_padding)
		decimal_padding = '0';
	if (isNaN(value))
		value = 0;
	value = Math.round(value * Math.pow(10, decimals));
	var stringValue = (value / Math.pow(10, decimals)).toString();
	var padding = 0;
	var parts = stringValue.split(".");
	if (parts.length == 1) {
		padding = decimals;
		stringValue += ".";
	} else
		padding = decimals - parts[1].length;
	for (var i = 0; i < padding; i++) {
		stringValue += decimal_padding;
	}
	return stringValue;
}


function creditGlcode_initialize() {
	 var custom = new Bloodhound({
	    datumTokenizer: function(d) { return d.tokens; },
	    queryTokenizer: Bloodhound.tokenizers.whitespace,
		   remote: {
	            url: '/egworks/contractorbill/ajaxdeduction-coa?searchQuery=%QUERY',
	            filter: function (data) {
	                return $.map(data, function (ct) {
	                    return {
	                        id: ct.id,
	                        name: ct.name,
	                        glcode: ct.glcode,
	                        glcodesearch: ct.glcode+' ~ '+ct.name
	                    };
	                });
	            }
	        }
    });

    custom.initialize();

    $('.creditGlcode').typeahead({
    	hint : true,
		highlight : true,
		minLength : 3
		
	}, {		    
          displayKey: 'glcodesearch',
          source: custom.ttAdapter()
    }).on('typeahead:selected typeahead:autocompleted', function (event, data) {
    	$(this).parents("tr:first").find('.creditaccountheadname').val(data.name);
    	$(this).parents("tr:first").find('.creditglcodeid').val(data.id);    
    });
}

function resetCreditAccountDetails(obj){
	if(obj.value=='') {
		$(obj).parents("tr:first").find('.creditglcodeid').val('');
		$(obj).parents("tr:first").find('.creditaccountheadname').val('');
	}
}

function renderPDF(){
	var id = $('#id').val();
	window.open("/egworks/contractorbill/contractorbillPDF/" + id, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

$('#billtype').change(function() {
	var billType = $('#billtype').val();
	if(billType == 'Final Bill') {
		$(".workcompletion").show();
	} else {
		$('#workCompletionDate').val('');
		$(".workcompletion").hide();
	}
});


function addRefundRow() {
	var rowcount = $("#tblrefunddetails tbody tr").length;
	
	if (document.getElementById('refundRow') != null) {
		// get Next Row Index to Generate
		var nextIdx = rowcount;
		// validate status variable for exiting function
		var isValid = 1;// for default have success value 0

		// validate existing rows in table
		$("#tblrefunddetails tbody tr").find('input,select').each(
				function() {
					if (($(this).data('optional') === 0)
							&& (!$(this).val()) && !($(this).attr('name')===undefined)) { 
						$(this).focus();
						bootbox.alert($(this).data('errormsg'));
						isValid = 0;// set validation failure
						return false;
					}
		});
		if (isValid === 0) {
			return false;
		}
		
		$("#refundRow").clone().find("input, errors, select").each(
				function() {	
					$(this).attr(
							{
								'name' : function(_, name) {
									if(!($(this).attr('name')===undefined))
										return name.replace(/\d+/, nextIdx); 
								},
								'id' : function(_, id) {
									if(!($(this).attr('id')===undefined))
										return id.replace(/\d+/, nextIdx); 
								},
								'data-idx' : function(_,dataIdx)
								{
									return nextIdx;
								}
							});
		
						// if element is static attribute hold values for next row, otherwise it will be reset
						if (!$(this).data('static')) {
							$(this).val('');
						}
		
				}).end().appendTo("#tblrefunddetails tbody");	
}
}

function deleteRefundRow(obj) {
	var rIndex = getRow(obj).rowIndex;
	var tbl=document.getElementById('tblrefunddetails');	
	var rowo=tbl.rows.length;
	
	 if(rowo<=2)
		{
			bootbox.alert("This row cannot be deleted");
			return false;
		}
	 else {
		 tbl.deleteRow(rIndex);
		//starting index for table fields
			var idx=0;
			//regenerate index existing inputs in table row
			$("#tblrefunddetails tbody tr").each(function() {
			
					$(this).find("input, errors, select").each(function() {
						   $(this).attr({
						      'name': function(_, name) {
						    	  if(!($(this).attr('name')===undefined))
						    		  return name.replace(/\[.\]/g, '['+ idx +']'); 
						      },
						      'id': function(_, id) {
						    	  if(!($(this).attr('id')===undefined))
						    		  return id.replace(/\[.\]/g, '['+ idx +']'); 
						      },
							  'data-idx' : function(_,dataIdx)
							  {
								  return idx;
							  }
						   });
				    });
					idx++;
			});
			calculateNetPayableAmount();
			return true;
	 }
}

$(document).on('blur','.refundAmount',function(e) {
	var index = $(this).attr("data-idx"); 
	var accountCode = document.getElementById("refundBillDetails["+index+"].refundAccountCode");
	var accountCodeName = accountCode.options[accountCode.selectedIndex].text;
	var withheldAmount = $('input[name="refundBillDetails['+ index +'].withHeldAmount"]').val();
	var refundedAmount = $('input[name="refundBillDetails['+ index +'].refundedAmount"]').val();
	var refundAmount = $('input[name="refundBillDetails['+ index +'].debitamount"]').val();
	if(withheldAmount == "")
		withheldAmount = "0";
	
	if(refundedAmount == "") 
		refundedAmount = "0";
	
	if(refundAmount == "")
		refundAmount = "0";
	
	var totalRefund =  parseFloat(refundedAmount) + parseFloat(refundAmount);
	var diffAmount = totalRefund - parseFloat(withheldAmount);
	var flag = true;
	if(totalRefund > parseFloat(withheldAmount) && refundAmount != 0) {
		var spillOverFlag = $("#isSpillOver").val();
		if(spillOverFlag == 'true') {
			var alertMessage = '';
			if(withheldAmount == 0) {
				alertMessage = $("#errorSpilloverNoRefund").val();
			} else {
				alertMessage = "Sum of Refund amount for the COA "+ accountCodeName +" is exceeding the withheld amount by Rs."+ diffAmount +". Do you want to proceed?;"
			}
			bootbox.confirm(alertMessage, function(result) {
				if(!result) {
					bootbox.hideAll();
					$('input[name="refundBillDetails['+ index +'].debitamount"]').val('');
					calculateNetPayableAmount();
					return false;
				} else {
					return true;
				}
			});
		} else {
			 if(withheldAmount == 0) {
				 bootbox.alert($("#errorNonSpilloverNoRefund").val());
			 } else 
		        bootbox.alert("Sum of Refund amount for the COA "+ accountCodeName +" is exceeding the withheld amount by Rs."+ diffAmount +". Please enter proper value");
			 
			 $('input[name="refundBillDetails['+ index +'].debitamount"]').val('');
		     return false;
		}
	}
});

$(document).on('change','.refundpurpose',function(e) {
	var index = $(this).attr("data-idx"); 
	var accountCode = $(this).find("option:selected").text();
	var accountHead = accountCode.split('-').slice(1).join('-');
	var accountHeadName = "";
	var glCodeId = $(this).val();
	var woeId = $("#workOrderEstimateId").val();
	var spillOverFlag = $("#isSpillOver").val();
	var contractorBillId = $("#contractorBillId").val()
	if(contractorBillId == '')
		contractorBillId = -1;
		
	if(glCodeId != "") {
	$.ajax({
		url: "/egworks/contractorbill/gettotalcreditanddebitamount?workOrderEstimateId="+woeId+"&glCodeId="+$(this).val()+"&contractorBillId="+contractorBillId,     
		type: "GET",
		dataType: "json",
		success: function (data) {
			if(data != '') {
			var amounts = data.split(",");
			var creditAmount = amounts[0];
			var debitAmount = amounts[1];
			$('input[name="refundBillDetails['+ index +'].refundAccountHead"]').val(accountHead);
			$('input[name="refundBillDetails['+ index +'].glcodeid"]').val(glCodeId);
		    $('input[name="refundBillDetails['+ index +'].withHeldAmount"]').val(creditAmount);
		    $('input[name="refundBillDetails['+ index +'].refundedAmount"]').val(debitAmount);
		    $('input[name="refundBillDetails['+ index +'].debitamount"]').val('');
		    calculateNetPayableAmount();
		  }
		}, 
	   error: function (response) {
		 console.log("failed");
	   }
	});
} else {
	$('input[name="refundBillDetails['+ index +'].refundAccountHead"]').val('');
	$('input[name="refundBillDetails['+ index +'].glcodeid"]').val('');
    $('input[name="refundBillDetails['+ index +'].withHeldAmount"]').val('');
    $('input[name="refundBillDetails['+ index +'].refundedAmount"]').val('');
    $('input[name="refundBillDetails['+ index +'].debitamount"]').val('');
    calculateNetPayableAmount();
}
});

function validateCreditAndDebitAmount() {
	var creaditdetailsLength = $('#tblcreditdetails tr').length - 1;
	var index;
	for (var i = 1; i <= creaditdetailsLength; i++) {
		index = i;
		var accountCode = document.getElementById('billDetailes[' + index + '].glcodeid').value;
		var amount = document.getElementById('billDetailes[' + index + '].creditamount').value;
		if(accountCode != '' && amount == '') {
			bootbox.alert("Credit Amount is mandatory for deduction row: " +i);
			return false;
		}
		
		if(accountCode == '' && amount != '') {
			bootbox.alert("Credit Account code is mandatory for deduction row: " +i );
			return false;
		}
	}
	
	var refunddetailsLength = $('#tblrefunddetails tr').length - 1;
	for (var i = 1; i <= refunddetailsLength; i++) {
		index = i-1;
		var accountCode = document.getElementById('refundBillDetails[' + index + '].glcodeid').value;
		var amount = document.getElementById('refundBillDetails[' + index + '].debitamount').value;
		if(accountCode != '' && amount == '') {
			bootbox.alert("Debit Amount is mandatory for Refund row: " +i ); 
			return false;
		}
		if(accountCode == '' && amount != '') {
			bootbox.alert("Debit Account code is mandatory for Refund row: " +i );
			return false;
		}
	}
	
	return true;
}
