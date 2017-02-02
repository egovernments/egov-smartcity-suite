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
function renderPDF(){
	var id = $('#id').val();
	window.open("/egworks/letterofacceptance/letterOfAcceptancePDF/" + id, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

$(document).ready(function() {
	var percentageSign = $('#percentageSign').val();
	if(percentageSign == '')
		$('#percentageSign').val('+');
	else
		$('.number-sign span.sign-text').html(percentageSign);
	
	$('.number-sign li a').click(function(e){
		
		$('.number-sign span.sign-text').html($(this).html());
		$('#percentageSign').val($(this).html());
		calculateAgreementAmount();
		
	});
	
	$('#revisedValue').blur(function(){
		calculateAgreementAmount();		
	});
	
	function calculateAgreementAmount() {
    	var revisedValue = $('#revisedValue').val();
		if(revisedValue != ''){
			revisedValue = assignSignForrevisedValue(revisedValue);
	    	var agreementAmount = eval(parseFloat($('#agreementAmount').html().trim())+parseFloat(revisedValue));
		    $('#workOrderAmount').val(roundTo(agreementAmount));
		}
		else 
				$('#workOrderAmount').val('');
	}

	function assignSignForrevisedValue(revisedValue){
		var percentageSign = $('#percentageSign').val();
		if(percentageSign=='-'){
		      return -revisedValue;
		  }else{
		     return revisedValue;
		  }
	}
	
	function roundTo(value,decimals,decimal_padding){
		  if(!decimals) decimals=2;
		  if(!decimal_padding) decimal_padding='0';
		  if(isNaN(value)) value=0;
		  value=Math.round(value*Math.pow(10,decimals));
		  var stringValue= (value/Math.pow(10,decimals)).toString();
		  var padding=0;
		  var parts=stringValue.split(".");
		  if(parts.length==1) {
		  	padding=decimals;
		  	stringValue+=".";
		  } 
		  else 
			 padding=decimals-parts[1].length;
		  for(var i=0;i<padding;i++)
		  {
			  stringValue+=decimal_padding;
		  }
		  return stringValue;
		}
	
	$('#modify').click(function() {
		var flag = true;
		$("#loaViewForm").find('input').each(function() {
			if($(this).attr('required') == 'required' && $(this).val() == '') {
				flag = false;
			}
		});
		if($('#workOrderAmount').val() <= 0) {
			bootbox.alert("Revised agreement amount should be greater than 0. Please enter valid amount");
			flag = false;
		}
		return flag;
	});
	
	if($('#revisedValue').val() != '')
		$('#revisedValue').trigger('blur');

	var defaultDepartmentId = $("#defaultDepartmentId").val();
	if(defaultDepartmentId != "")
		$("#approvalDepartment").val(defaultDepartmentId);
});


function validateWorkFlowApprover(name) {
	document.getElementById("workFlowAction").value = name;
	var approverPosId = document.getElementById("approvalPosition");
	var button = document.getElementById("workFlowAction").value;
	
	var flag = true;
	
	if (button != null && button == 'Approve') {
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
			bootbox.confirm($('#confirm').val(), function(result) {
				if(!result) {
					bootbox.hideAll();
					return false;
				} else {
					document.forms[0].submit();
				}
			});
		}
		return false;
	}
	if (button != null && button == 'Forward') {
		$('#approvalDepartment').attr('required', 'required');
		$('#approvalDesignation').attr('required', 'required');
		$('#approvalPosition').attr('required', 'required');
		$('#approvalComent').removeAttr('required');
		
		if(!$("form").valid())
		{
			return false;
		}
	}
	
	if(flag) {
		document.forms[0].submit;
		return true;
	} else
		return false;
}