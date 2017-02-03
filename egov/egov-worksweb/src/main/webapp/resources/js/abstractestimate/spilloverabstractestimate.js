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
var cuttOffDate = new Date(($('#cuttOffDate').val().split('/').reverse().join('-'))).getTime();
var currFinDate = new Date(($('#currFinDate').val().split('/').reverse().join('-'))).getTime();
$(document).ready(function(){
	$('#approverDetailHeading').hide();
	initializeDatePicker();
	$('#designation').val($('#designationValue').val());
	$('#designation').trigger('change');
	
	var estimateDate = new Date(($('#estimateDate').val().split('/').reverse().join('-'))).getTime();
	if(estimateDate < currFinDate){
		$('#billsCreatedCheckbox').show();
		$("#grossAmountBilled").val('');
    	$("#tdGrossAmount").hide();
    	$("#grossAmountBilled").removeAttr('required');
    	$('#isBillsCreated').prop('checked', false);
	} else {
		$('#billsCreatedCheckbox').hide();
		$("#grossAmountBilled").val('');
    	$("#tdGrossAmount").hide();
    	$("#grossAmountBilled").removeAttr('required');
    	$('#isBillsCreated').prop('checked', false);
	}
	
	if($('#billsCreated').val() == 'true'){
		$('#billsCreatedCheckbox').show();
	}
	
	if($("#isBillsCreatedInput").val() == 'true') {
		$("#tdGrossAmount").find('input').attr('required', 'required');
		$("#tdGrossAmount").find('input').attr('data-optional', '0');
		$("#tdGrossAmount").show();
	}
	$('#estimateDate').trigger('change');
});

$('#designation').change(function(){
	$.ajax({
		url: "/egworks/lineestimate/ajax-assignmentByDepartmentAndDesignation",     
		type: "GET",
		dataType: "json",
		data: {
			approvalDesignation : $('#designation').val(),
			approvalDepartment : $('#executingDepartment').val()    
		},
		success: function (response) {
			$('#authority').empty();
			$('#authority').append($("<option value=''>Select from below</option>"));
			var responseObj = JSON.parse(response);
			$.each(responseObj, function(index, value) {
				$('#authority').append($('<option>').text(value.name).attr('value', value.id));  
			});
			var authorityValue = $('#authorityValue').val();
			$('#authority').val(authorityValue);
		}, 
		error: function (response) {
			console.log("failed");
		}
	});
});

function validateAdminSanctionDate(obj){
	if($('#estimateDate').val() == ''){
		bootbox.alert($('#errorAbstractEstimateDate').val());
		$(obj).datepicker("setDate", new Date());
		$(obj).val('');
		$(obj).datepicker('update');
		return false;
	}
	var estimateDate = $('#estimateDate').val();
	var adminSanctionDate = $('#approvedDate').val();
	if(new Date((estimateDate.split('/').reverse().join('-'))).getTime() > new Date((adminSanctionDate.split('/').reverse().join('-'))).getTime())
	{	
		$(obj).datepicker("setDate", new Date());
		$(obj).val('');
		$(obj).datepicker('update');
		bootbox.alert($('#errorAbstractAdminSanctionDate').val());
		return false;	
	}
	
}

function validateTechinicalSanctionDate(obj){
	if($('#estimateDate').val() == ''){
		bootbox.alert($('#errorAbstractEstimateDate').val());
		$(obj).datepicker("setDate", new Date());
		$(obj).val('');
		$(obj).datepicker('update');
		return false;
	}
	var estimateDate = $('#estimateDate').val();
	var technicalSanctionDate = $('#technicalSanctionDate').val();
	if(new Date((estimateDate.split('/').reverse().join('-'))).getTime() > new Date((technicalSanctionDate.split('/').reverse().join('-'))).getTime())
	{	
		$(obj).datepicker("setDate", new Date());
		$(obj).val('');
		$(obj).datepicker('update');
		bootbox.alert($('#errorAbstractTechnicalSanctionDate').val());
		return false;	
	}
	
}

function validateAbstractEstimateDate(obj){
	var estimateDate = $('#estimateDate').data('datepicker').date;
	var technicalSanctionDate = $('#technicalSanctionDate').val();
	var adminSanctionDate = $('#approvedDate').val();
	var adminSanctionDateLE = new Date($('#adminSanctionDateLE').val());
	
	if(estimateDate < adminSanctionDateLE){
		$(obj).datepicker("setDate", new Date());
		$(obj).val('');
		$(obj).datepicker('update');
		bootbox.alert($('#errorAbstractLEAdminSanctionDate').val());
		return false;	
	}
		
	if(technicalSanctionDate != '')
		validateTechinicalSanctionDate(obj);
	if(adminSanctionDate != '' && adminSanctionDate != undefined)
		validateAdminSanctionDate(obj);
	
}

var estimateDateArray=[];
function initializeDatePicker(){
	
	$('#approvedDate').datepicker().off('changeDate');
	jQuery( "#approvedDate" ).datepicker({ 
		format: 'dd/mm/yyyy',
		autoclose:true,
		onRender: function(date) {
			return date.valueOf() < now.valueOf() ? 'disabled' : '';
		}
		}).on('changeDate', function(ev) {
		
			var string=jQuery(this).val();
			if(!(string.indexOf("_") > -1)){
			isDatepickerOpened=false; 
			validateAdminSanctionDate(this);
			$('#approvedDate').datepicker('hide');
			}

		}).data('datepicker');
	
	$('#technicalSanctionDate').datepicker().off('changeDate');
	jQuery( "#technicalSanctionDate" ).datepicker({ 
		format: 'dd/mm/yyyy',
		autoclose:true,
		onRender: function(date) {
			return date.valueOf() < now.valueOf() ? 'disabled' : '';
		}
		}).on('changeDate', function(ev) {
		
			var string=jQuery(this).val();
			if(!(string.indexOf("_") > -1)){
			isDatepickerOpened=false; 
			validateTechinicalSanctionDate(this);
			$('#technicalSanctionDate').datepicker('hide');
			}

		}).data('datepicker');
	
	$('#estimateDate').datepicker().off('changeDate');
	jQuery( "#estimateDate" ).datepicker({ 
		format: 'dd/mm/yyyy',
		autoclose:true,
		onRender: function(date) {
			return date.valueOf() < now.valueOf() ? 'disabled' : '';
		}
		}).on('changeDate', function(ev) {
			estimateDateArray.push($(this).val());
			var estimateDate = new Date(($('#estimateDate').val().split('/').reverse().join('-'))).getTime();
			var string=jQuery(this).val();
			if(!(string.indexOf("_") > -1)){
				var flag = validateAbstractEstimateDate(this);
				if(flag == false) {
					estimateDateArray.pop();
				}
				var hiddenRowCount = $("#tblsor tbody tr[sorinvisible='true']").length;
				var nonSorHiddenRowCount = $("#tblNonSor tbody tr[nonsorinvisible='true']").length;
				var overheadData = document.getElementById('tempOverheadValues[0].overhead.id').value;
				if(flag == undefined && (hiddenRowCount != 1 || nonSorHiddenRowCount!= 1 || overheadData != "")) {
					bootbox.confirm({
					    message: $('#msgEstimateDateChange').val(),
					    buttons: {
					        'cancel': {
					            label: 'No',
					            className: 'btn-default pull-right'
					        },
					        'confirm': {
					            label: 'Yes',
					            className: 'btn-danger pull-right'
					        }
					    },
					    callback: function(result) {
					        if (result) {
					        	$("#workdetails").addClass("active");
								clearActivities();
								clearOverHeads();
								resetAddedOverheads();
								if(estimateDate < currFinDate){
					        		$('#billsCreatedCheckbox').show();
					        		$("#grossAmountBilled").val('');
						        	$("#tdGrossAmount").hide();
						        	$("#grossAmountBilled").removeAttr('required');
						        	$('#isBillsCreated').prop('checked', false);
					        	} else {
				        			$('#billsCreatedCheckbox').hide();
				        			$("#grossAmountBilled").val('');
				        	    	$("#tdGrossAmount").hide();
				        	    	$("#grossAmountBilled").removeAttr('required');
				        	    	$('#isBillsCreated').prop('checked', false);
				        		}
					        }else{
					        	if(estimateDateArray.length > 1)
					        		$('#estimateDate').datepicker('setDate',new Date((estimateDateArray[(estimateDateArray.length) - 2].split('/').reverse().join('-'))));
					        	else {
					        		$('#estimateDate').datepicker("setDate", new Date());
					        		$('#estimateDate').val('');
					        		$('#estimateDate').datepicker("update", $('#estimateDate').val(''));
					        	}
			    				estimateDateArray.pop();
					        	bootbox.hideAll();
								return false;
			    			}
					    }
					});
				} else{
					if(cuttOffDate != '' && estimateDate > currFinDate && estimateDate < cuttOffDate){
	        			$('#billsCreatedCheckbox').hide();
	        			$("#grossAmountBilled").val('');
	        	    	$("#tdGrossAmount").hide();
	        	    	$("#grossAmountBilled").removeAttr('required');
	        	    	$('#isBillsCreated').prop('checked', false);
	        			return false
	        		} else if(estimateDate < currFinDate){
	        			$('#billsCreatedCheckbox').show();
	        		}
				}
				$('#estimateDate').datepicker('hide');
			}
		}).data('datepicker');
	$('#estimateDate').datepicker('update');
	
	$('#technicalSanctionDate').datepicker('update');

}

$('#saveSpillAbstractEstimate').click(function() {
	if ($('#location').val() == '' || $('#description').val() == ''
			|| $('#technicalSanctionNumber').val() == '' || $('#technicalSanctionDate').val() == ''
			|| $('#designation').val() == '') {
		bootbox.alert($('#mandatoryError').val());
		return false;
	}
	
	if($('#abstractEstimate').valid()) {
		var flag = validateSORDetails();
		if(!flag)
			return false;
		$('.disablefield').removeAttr("disabled");
		var resultLengthForDeductionTable = jQuery('#deductionTable tr').length - 1;
		var deleteDeductionTable=$('#deductionTable tr:last');
		var accountCode = document.getElementById('tempDeductionValues[0].accountCode').value;
		if(resultLengthForDeductionTable<=1 && accountCode == "") {
			var i=0;
			deleteDeductionTable.remove();
		}
		return true;
	} else
	return false;
});

function resetFormOnSubmit(){
	if($('#estimateNumber').val() != '') {
		   $('.disablefield').attr('disabled', 'disabled');
		}
	initializeDatePicker();
	if($("#description").val() == '')
		$('#estimateDate').val('');
	$('#designation').val($('#designationValue').val());
	$('#designation').trigger('change');
		
}

$('#isBillsCreated').click(function() {
	if($(this).prop("checked") == true) {
		$('#footNoteMsg').show();
		$("#tdGrossAmount").find('input').attr('required', 'required');
		$("#tdGrossAmount").find('input').attr('data-optional', '0');
		$("#tdGrossAmount").show();
	} else {
		$('#footNoteMsg').hide();
		$("#tdGrossAmount").find('input').val("");
		$("#tdGrossAmount").find('input').removeAttr('required');
		$("#tdGrossAmount").find('input').attr('data-optional', '1');
		$("#tdGrossAmount").hide();
	}
});