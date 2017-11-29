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
$deletedAmt = 0;
$locationId = 0;
$subTypeOfWorkId = 0;
$subSchemeId = 0;
$functionId = 0;
$detailsRowCount = $('#detailsSize').val();
$budgetHeadId=0;
$(document).ready(function(){
	
	var lineEstimateStatus = $('#lineEstimateStatus').val();
	if(lineEstimateStatus == 'ADMINISTRATIVE_SANCTIONED' || lineEstimateStatus == 'TECHNICAL_SANCTIONED') {
		$('#actionButtons').prepend("<a href='javascript:void(0)' class='btn btn-primary' onclick='renderPdf()'>View Proceedings</a>");
	}
	
	getLineEstimateDate();
	$locationId = $('#locationValue').val();
	$subTypeOfWorkId = $('#subTypeOfWorkValue').val();
	$subSchemeId = $('#subSchemeValue').val();
	$functionId = $('#functionId').val();
	$budgetHeadId = $('#budgetHeadValue').val();
	getFunctionsByFundAndDepartment();
	getBudgetHeads();
	$( "input[name$='estimateAmount']" ).each(function(){
		var value = parseFloat(roundTo($(this).val()));
		if(value != 0)
			$(this).val(roundTo($(this).val()));
	});
	
	var boundaryType = $('#boundaryType').val();
	if(boundaryType != undefined && boundaryType.toUpperCase() == 'CITY')  {
		$('#wardInput').val($("#boundaryName").val());
	}
	else
		$('#wardInput').val($("#boundaryNumber").val());
	
	$( "input[name$='actualEstimateAmount']" ).each(function(){
		var value = parseFloat(roundTo($(this).val()));
		if(value != 0)
			$(this).val(roundTo($(this).val()));
	});
	
	$('#estimateTotal').text(roundTo($('#estimateTotal').text()));

	var functionId = $('#functionId').val();
	if (functionId != "") {
		$('#function option').each(function() {
			if ($(this).val() == functionId)
				$(this).attr('selected', 'selected');
		});
	}
	
	//TODO : Need to remove trigger
	$('#typeofwork').trigger('blur');
	$('#scheme').trigger('change');
	$('#function').trigger('change');
	
	replaceWorkCategoryChar();
	replaceBeneficiaryChar();
	
	var hiddenFields = $("#hiddenfields").val().replace(/[\[\]']+/g,'').replace(/, /g, ",").split(",");
	$.each(hiddenFields,function(){
		var fieldName = this.toString().trim();
		var label = $("label[for='"+fieldName+"']");
		label.hide();
		$("#"+fieldName).hide();
		$("#"+fieldName+"-value").hide();
		$("#subjectDescriptionHide").hide();
		$('#'+fieldName).removeAttr('required')
	});

});

function renderPdf() {
	var id = $('#lineEstimateId').val();
	window.open("/egworks/lineestimate/lineEstimatePDF/" + id, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

$('.btn-primary').click(function(){
	var button = $(this).attr('id');
	if (button != null && button == 'Approve') {
		var flag = true;
		$('#approvalComent').removeAttr('required');
		
		var lineEstimateStatus = $('#lineEstimateStatus').val();
		if(lineEstimateStatus == 'ADMINISTRATIVE_SANCTIONED') {
			var adminSanctionDate = $('#adminSanctionDate').data('datepicker').date;
			var technicalSanctionDate = $('#technicalSanctionDate').data('datepicker').date;
			var technicalSanctionNumber = $('#technicalSanctionNumber').val();
			
			if(adminSanctionDate > technicalSanctionDate && technicalSanctionDate != '') {
				bootbox.alert($('#errorTechDate').val());
				$('#technicalSanctionDate').val('').datepicker('update');
				return false;
			}

			var message = $('#errorActualAmount').val() + " ";

			if(technicalSanctionDate != '' && technicalSanctionNumber != '') {
				$("input[name$='actualEstimateAmount']")
				.each(
						function() {
							var index = getRow(this).rowIndex - 1;
							var estimateAmount = $(
									'#estimateAmount' + index).html();
							var actualAmount = $(
									'#actualEstimateAmount' + index).val();
							if (parseFloat(estimateAmount.trim()) < parseFloat(actualAmount)) {
								var estimateNumber = $(
										'#estimateNumber' + index).val();
								message += estimateNumber + ", ";
								flag = false;
							}
						});
				message = message.replace(/,\s*$/, ". ");
				message += $('#errorActualAmountContinued').val();
				if (!flag) {
					bootbox.alert(message);
					return false;
				}
			}
		}
		
		if(!flag)
			return false;
	}
	else {
		return validateWorkFlowApprover(button);
	}
});

function getSubSchemsBySchemeId(schemeId) {
	if ($('#scheme').val() === '') {
		   $('#subScheme').empty();
		   $('#subScheme').append($('<option>').text('Select from below').attr('value', ''));
			return;
			} else {
				$.ajax({
					url: "/egworks/lineestimate/getsubschemesbyschemeid/"+schemeId,     
					type: "GET",
					dataType: "json",
					success: function (response) {
						$('#subScheme').empty();
						$('#subScheme').append($("<option value=''>Select from below</option>"));
						var responseObj = JSON.parse(response);
						$.each(responseObj, function(index, value) {
							$('#subScheme').append($('<option>').text(responseObj[index].name).attr('value', responseObj[index].id));
							$('#subScheme').val($subSchemeId);
						});
					}, 
					error: function (response) {
						console.log("failed");
					}
				});
			}
}

function addLineEstimate() {
	var rowcount = $("#tblestimate tbody tr").length;
	if (rowcount < 30) {
		if (document.getElementById('estimateRow') != null) {
			// get Next Row Index to Generate
			var nextIdx = $("#tblestimate tbody tr").length;;
			
            var estimateNo = (new Date()).valueOf();
			// validate status variable for exiting function
			var isValid = 1;// for default have success value 0

			// validate existing rows in table
			$("#tblestimate tbody tr").find('input, select, textarea').each(
					function() {
						if (($(this).data('optional') === 0)
								&& (!$(this).val())) {
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
			$("#estimateRow").clone().find("input, errors, textarea, select").each(
					function() {

						if ($(this).data('server')) {
							$(this).removeAttr('data-server');
						}
						
							$(this).attr(
									{
										/*'id' : function(_, id) {
											return id.replace('[0]', '['
													+ nextIdx + ']');
										},*/
										'name' : function(_, name) {
											return name.replace(/\d+/, nextIdx);
										},
										'data-idx' : function(_,dataIdx)
										{
											return nextIdx;
										}
									});

							// if element is static attribute hold values for
							// next row, otherwise it will be reset
							if (!$(this).data('static')) {
								$(this).val('');
								// set default selection for dropdown
								if ($(this).is("select")) {
									$(this).prop('selectedIndex', 0);
								}
							}
							
							$(this).attr('readonly', false);
							$(this).removeAttr('disabled');
							$(this).prop('checked', false);

					}).end().appendTo("#tblestimate tbody");
			
			generateSno();
			
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

function generateSno()
{
	var idx=1;
	$(".spansno").each(function(){
		$(this).text(idx);
		idx++;
	});
}


function deleteLineEstimate(obj) {
    var rIndex = getRow(obj).rowIndex;
    
    var id = $(getRow(obj)).children('td:first').children('input:first').val();
    //To get all the deleted rows id
    var aIndex = rIndex - 1;
    if(!$("#removedLineEstimateDetailsIds").val()==""){
		$("#removedLineEstimateDetailsIds").val($("#removedLineEstimateDetailsIds").val()+",");
	}
    $("#removedLineEstimateDetailsIds").val($("#removedLineEstimateDetailsIds").val()+id);

	var tbl=document.getElementById('tblestimate');	
	var rowcount=$("#tblestimate tbody tr").length;

    if(rowcount<=1) {
		bootbox.alert("This row can not be deleted");
		return false;
	} else {
		tbl.deleteRow(rIndex);
		//starting index for table fields
		var idx= 0;
		var sno = 1;
		//regenerate index existing inputs in table row
		jQuery("#tblestimate tbody tr").each(function() {
		
				jQuery(this).find("input, select, textarea, errors, span, input:hidden").each(function() {
					var classval = jQuery(this).attr('class');
					if(classval == 'spansno') {
						jQuery(this).text(sno);
						sno++;
					} else {
					jQuery(this).attr({
					      'name': function(_, name) {
					    	  if(!(jQuery(this).attr('name')===undefined))
					    		  return name.replace(/\[.\]/g, '['+ idx +']'); 
					      },
					      'id': function(_, id) {
					    	  if(!(jQuery(this).attr('id')===undefined))
					    		  return id.replace(/\[.\]/g, '['+ idx +']'); 
					      },
					      'class' : function(_, name) {
								if(!(jQuery(this).attr('class')===undefined))
									return name.replace(/\[.\]/g, '['+ idx +']'); 
							},
						  'data-idx' : function(_,dataIdx)
						  {
							  return idx;
						  }
					   });
					}
			    });
				
				idx++;
		});
		calculateEstimatedAmountTotal();
		return true;
	}
		
}

function calculateEstimatedAmountTotal(){
	validateEstimateAmount();
	var estimateTotal=0;
	$( "input[name$='estimateAmount']" ).each(function(){
		estimateTotal = estimateTotal + parseFloat(($(this).val()?$(this).val():"0"));
		if(parseFloat($(this).val()) == 0)
			$(this).val("");
	});
	$('#estimateTotal').html(estimateTotal);
}

function calculateActualEstimatedAmountTotal() {
	validateActualEstimateAmount();
	var actualEstimateTotal = 0;
	$( "input[name$='actualEstimateAmount']" ).each(function(){
		actualEstimateTotal = actualEstimateTotal + parseFloat(($(this).val()?$(this).val():"0"));
		if(parseFloat($(this).val()) == 0)
			$(this).val("");
	});
	$('#actualEstimateTotal').html(actualEstimateTotal);
}

function getLineEstimateDate() {
	var dt = new Date(); 
	var dd = dt.getDate() < 10 ? "0" + dt.getDate() : dt.getDate(); 
	var mm = dt.getMonth() + 1;
	var mm = mm < 10 ? "0" + mm : mm;
	var yy = 1900 + dt.getYear();
	$('[name="lineEstimateDate"]').val(dd+"/"+mm+"/"+yy);
}

function validateEstimateAmount() {
	$( "input[name$='estimateAmount']" ).on("keyup", function(){
	    var valid = /^[1-9](\d{0,9})(\.\d{0,2})?$/.test(this.value),
	        val = this.value;
	    
	    if(!valid){
	        console.log("Invalid input!");
	        this.value = val.substring(0, val.length - 1);
	    }
	});
}

function validateActualEstimateAmount() {
	$( "input[name$='actualEstimateAmount']" ).on("keyup", function(){
	    var valid = /^[1-9](\d{0,9})(\.\d{0,2})?$/.test(this.value),
	        val = this.value;
	    
	    if(!valid){
	        console.log("Invalid input!");
	        this.value = val.substring(0, val.length - 1);
	    }
	});
}

function replaceWorkCategoryChar() {
	$('#workCategory option').each(function() {
	   var $this = $(this);
	   $this.text($this.text().replace(/_/g, ' '));
	});
}

function replaceBeneficiaryChar() {
	$('#beneficiary option').each(function() {
	   var $this = $(this);
	   $this.text($this.text().replace(/_C/g, '/C').replace(/_/g, ' '));
	});
}

$('#typeofwork').blur(function(){
	 if ($('#typeofwork').val() === '') {
		   $('#subTypeOfWork').empty();
		   $('#subTypeOfWork').append($('<option>').text('Select from below').attr('value', ''));
			return;
			} else {
			$.ajax({
				type: "GET",
				url: "/egworks/lineestimate/getsubtypeofwork",
				cache: true,
				dataType: "json",
				data:{'id' : $('#typeofwork').val()}
			}).done(function(value) {
				console.log(value);
				$('#subTypeOfWork').empty();
				$('#subTypeOfWork').append($("<option value=''>Select from below</option>"));
				$.each(value, function(index, val) {
					var selected="";
					if($subTypeOfWorkId)
					{
						if($subTypeOfWorkId==val.id)
						{
							selected="selected";
						}
					}
				     $('#subTypeOfWork').append($('<option '+ selected +'>').text(val.description).attr('value', val.id));
				});
			});
		}
	});

$(document).ready(function(){
    var ward = new Bloodhound({
        datumTokenizer: function (datum) {
            return Bloodhound.tokenizers.whitespace(datum.value);
        },
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        remote: {
            url: '/egworks/lineestimate/ajax-getward?name=%QUERY',
            filter: function (data) {
                return $.map(data, function (ct) {
                    return {
                        name: ct.boundaryType.name.toUpperCase() == 'CITY' ? ct.name : ct.boundaryNum + '' ,
                        value: ct.id
                    };
                });
            }
        }
    });
    
    ward.initialize();
	var ward_typeahead = $('#wardInput').typeahead({
		hint : false,
		highlight : false,
		minLength : 1
	}, {
		displayKey : 'name',
		source : ward.ttAdapter(),
	});
	
	typeaheadWithEventsHandling(ward_typeahead,
	'#ward');
});

function validateadminSanctionNumber() {
	$( "input[name$='adminSanctionNumber']" ).on("keyup", function(){
		var valid = /^[a-zA-Z0-9\\/-]*$/.test(this.value),
	        val = this.value;
	    if(!valid){
	        console.log("Invalid input!");
	        this.value = val.substring(0, val.length - 1);
	    }
	});
}

function validatecouncilResolutionNumber() {
	$( "input[name$='councilResolutionNumber']" ).on("keyup", function(){
		var valid = /^[a-zA-Z0-9\\/-]*$/.test(this.value),
	        val = this.value;
	    if(!valid){
	        console.log("Invalid input!");
	        this.value = val.substring(0, val.length - 1);
	    }
	});
}

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

function validateWorkFlowApprover(name) {
	document.getElementById("workFlowAction").value = name;
	var approverPosId = document.getElementById("approvalPosition");
	var button = document.getElementById("workFlowAction").value;
	if (button != null && button == 'Submit') {
		$('#approvalDepartment').attr('required', 'required');
		$('#approvalDesignation').attr('required', 'required');
		$('#approvalPosition').attr('required', 'required');
		$('#approvalComent').removeAttr('required');
		
		var lineEstimateStatus = $('#lineEstimateStatus').val();
		if(lineEstimateStatus == 'BUDGET_SANCTIONED') {
			var lineEstimateDate = new Date($('#lineEstimateDate').val());
			var councilResolutionDate = $('#councilResolutionDate').data('datepicker').date;	
			if (councilResolutionDate != '' && councilResolutionDate < lineEstimateDate) {
					bootbox.alert($('#errorCouncilResolutionDate').val());
					$('#councilResolutionDate').val('').datepicker('update');
					return false;
			}
		}
	}
	if (button != null && button == 'Reject') {
		$('#approvalDepartment').removeAttr('required');
		$('#approvalDesignation').removeAttr('required');
		$('#approvalPosition').removeAttr('required');
		$('#approvalComent').attr('required', 'required');
		$('#adminSanctionNumber').removeAttr('required');
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
	}

	document.forms[0].submit;
	return true;
}
function getBudgetHeads() {
	 if ($('#fund').val() === '' || $('#executingDepartments').val() === '' || ($('#function').val() === '' && $functionId == 0) || $('#natureOfWork').val() === '') {
		   $('#budgetHead').empty();
		   $('#budgetHead').append($('<option>').text('Select from below').attr('value', ''));
			return;
			} else {
			$.ajax({
				type: "GET",
				url: "/egworks/lineestimate/getbudgethead",
				cache: true,
				dataType: "json",
				data:{
					'fundId' : $('#fund').val(),
					'functionId' : $('#function').val(),
					'departmentId' : $('#executingDepartments').val(),
					'natureOfWorkId' : $('#natureOfWork').val()
					
					}	
			}).done(function(value) {
				console.log(value);
				$('#budgetHead').empty();
				$('#budgetHead').append($("<option value=''>Select from below</option>"));
				$.each(value, function(index, val) {
					var selected="";
					if($budgetHeadId)
					{
						if($budgetHeadId==val.id)
						{
							selected="selected";
						}
					}
				     $('#budgetHead').append($('<option '+ selected +'>').text(val.name).attr('value', val.id));
				});
			});
		}
}
function getFunctionsByFundAndDepartment() {
	if ($('#fund').val() === '' || $('#executingDepartments').val() === '') {
		   $('#function').empty();
		   $('#function').append($('<option>').text('Select from below').attr('value', ''));
			return;
			} else {
				$.ajax({
					method : "GET",
					url : "/egworks/lineestimate/getfunctionsbyfundidanddepartmentid",
					data : {
						fundId : $('#fund').val(),
						departmentId : $('#executingDepartments').val()
					},
					async : true
				}).done(
						function(response) {
							$('#function').empty();
							$('#function').append($("<option value=''>Select from below</option>"));
							var output = '<option value="">Select from below</option>';
							$.each(response, function(index, value) {
								var selected="";
								if($functionId)
								{
									if($functionId==value.id)
									{
										selected="selected";
									}
								}
								$('#function').append($('<option '+ selected +'>').text(value.code + ' - ' + value.name).attr('value', value.id));
							});
				});
			}
}