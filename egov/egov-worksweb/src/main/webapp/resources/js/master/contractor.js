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
function setExemptionFormValues(){
	var exemptionFormOptions = $("#exemptionForm").find("option");
	$.each(exemptionFormOptions,function(){
		var option = $(this).val();
		if(option != "")
			$(this).html(option.replace(/_/g," "));
	});
}

$(document).ready(function(){
	flag = false;
	var mode = $("#mode").val();
	if($("#mandatory").val() != null){
		var loadMandatoryFields = $("#mandatory").val().replace(/[\[\]']+/g,'').split(",");
		$.each(loadMandatoryFields,function(){
			var fieldName = this.toString().trim();
			var label = $("label[for='"+fieldName+"']");
			label.append("<span class = 'mandatory'></span>");
		});
	}
	if($("#hide").val() != null){
		var loadHiddenFields = $("#hide").val().replace(/[\[\]']+/g,'').split(",");
		$.each(loadHiddenFields,function(){
			var fieldName = this.toString().trim();
			var label = $("label[for='"+fieldName+"']");
			label.hide();
			$("#"+fieldName).hide();
			$("#"+fieldName+"-value").hide();
			$("#"+fieldName+"Label").hide();
			$("#"+fieldName).parent().hide();
			var sibling = label.siblings("label");
			if(sibling.hasClass('col-sm-2')){
				sibling.removeClass('col-sm-2');
				sibling.addClass('col-sm-3');
			}
		});
	}
	
	if($("#mandatory").val() != null && $("#hide").val() != null){
		var mandatoryFields = $("#mandatory").val().replace(/[\[\]']+/g,'').split(",");
		var hiddenFields = $("#hide").val().replace(/[\[\]']+/g,'').split(",");
		var contractorMandatoryFields = mandatoryFields.filter(function(val) {
		  return hiddenFields.indexOf(val) == -1;
		});
		$.each(contractorMandatoryFields.reverse(),function(){
			var fieldName = this.toString().trim();
			$("#"+fieldName).attr("required","required");
			if(fieldName == "registrationNumber"){
				$(".registrationNumber").attr("required","required");
			}
			if(fieldName == "category")
				$(".category").attr("required","required");
			if(fieldName == "grade")
				$(".grade").attr("required","required");
		});
	}
	
	$("#mobileNumber").on("input", function(){
		var valid = /\d+$/.test(this.value),
	       val = this.value;
	   
	   if(!valid){
	       console.log("Invalid input!");
	       this.value = val.substring(0, val.length - 1);
	   }
		});

	if($("#codeautogeneration").val() == "Yes" && mode != "edit"){
		$("label[for='code']").remove();
		$("#contractor_code").parent().remove();
		$("label[for='name']").removeClass('col-sm-2');
		$("label[for='name']").addClass('col-sm-3');
	}
	
	if(mode != "edit"){
		var defaultDepartmentId = $("#defaultDepartmentId").val();
		if(defaultDepartmentId != ""){
			$("#department").val(defaultDepartmentId);
		}
	}
	
	$('.textfieldsvalidate').on('input',function(){
		var regexp_textfields = /[^0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]/g;
		if($(this).val().match(regexp_textfields)){
			$(this).val( $(this).val().replace(regexp_textfields,'') );
		}
	})
	
	setExemptionFormValues();
	
	var exemptionFormValue = $("#exemptionForm-value").html();
	if(exemptionFormValue != undefined)
		$("#exemptionForm-value").html(exemptionFormValue.replace(/_/g," "));
	
});

function checkMobileNumber(){
	var regx = /\d{10}$/;
	var mobileNumberVal = $("#mobileNumber").val();
	if (mobileNumberVal != "" && mobileNumberVal.length != 10 && !regx.test(mobileNumberVal)) {
		  bootbox.alert( $("#mobileNumberErrorMsg").val());
		  flag = true;
		  return false;
	  }
	  return true;
}
function checkPanNumber(){
  var regx = /^[A-Za-z]{5}\d{4}[A-za-z]{1}$/;
  if ($("#panNumber").val() != "" && !regx.test($("#panNumber").val())) {
	  bootbox.alert( $("#panNumberMessage").val());
	  flag = true;
	  return false;
  }
  return true;
}

function checkTinNumber(){
  var regx = /^[A-Za-z0-9]+$/;
  if ($("#tinNumber").val() != "" && !regx.test($("#tinNumber").val())) {
	  bootbox.alert( $("#tinNumberAlphaNeumericErrorMsg").val());
	  flag = true;
	  return false;
  }
  return true;
}

function checkContactPerson(){
	var regx = /^[A-Za-z0-9 ]+$/;
	  if ($("#contactPerson").val() != "" && !regx.test($("#contactPerson").val())) {
		  bootbox.alert( $("#contactPersonAlphaNeumericErrorMsg").val());
		  flag = true;
		  return false;
	  }
	return true;
}

function checkIfscCode(){
  var regx = /^[A-Za-z0-9]+$/;
  if ($("#ifscCode").val() != "" && !regx.test($("#ifscCode").val())) {
	  bootbox.alert( $("#ifscCodeAlphaNeumericErrorMsg").val());
	  flag = true;
	  return false;
  }
  return true;
}

function checkBankAccountNumber(){
  var regx = /^[A-Za-z0-9]+$/;
  if ($("#bankAccount").val() != "" && !regx.test($("#bankAccount").val())) {
	  bootbox.alert( $("#bankAccountAlphaNeumericErrorMsg").val());
	  flag = true;
	  return false;
  }
  return true;
}

function checkEmail(){
	var regx = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	if ($("#email").val() != "" && !regx.test($("#email").val())) {
	  bootbox.alert( $("#emailInvalidErrorMsg").val());
	  flag = true;
	  return false;
	}
	return true;
}

function checkCodeAndName(){  
	var regxCode = /^[A-Za-z0-9\-\/]+$/
	var regxName = /^[A-Za-z0-9\-\& \:\,\/\.()\@]+$/;
	if(!($("#codeautogeneration").val() == "Yes")){
		  
		  if (!regxCode.test($("#code").val())) {
			  bootbox.alert( $("#codeAlphaNeumericErrorMsg").val());
			  return false;
		  }

		  if (!regxName.test($("#name").val())) {
			  bootbox.alert( $("#nameAlphaNeumericErrorMsg").val());
			  return false;
		  }
	}else{

		  if (!regxName.test($("#name").val())) {
			  bootbox.alert( $("#nameAlphaNeumericErrorMsg").val());
			  return false;
		  }
			  
		 if($("#name").val().length < 4){
				bootbox.alert( $("#nameErrorLengthMsg").val());
				return false;
		 }
	}
  
  return true;
}


$('#btnsearch').click(function(e) {
	drillDowntableContainer = $("#resultTable");
	$('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/egworks/masters/contractor-searchdetails",
					type : "POST",
					"data" : getFormData($('form'))
				},
				"bDestroy" : true,
				'bAutoWidth': false,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : []
				},
				"fnRowCallback" : function(row, data, index) {
					$('td:eq(0)', row).html(index + 1);
					$('td:eq(5)', row).html(
							'<a href="javascript:void(0);" onclick="modifyContractor(\''
									+ data.contractorId + '\')">Modify</a>');
					return row;
				},
				aaSorting : [],
				columns : [{
					"data" : "",
					"sClass" : "text-center","width": "2%"
				}, {
					"data" : "name",
				},{
					"data" : "code",
				}, {
					"data" : "class",
				},{
					"data" : "status",
				},{
					"data" : "",
					"sClass" : "text-center"
				}]
			});
});

function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}

function addContractorDetails() {
	var rowcount = $("#tblContractorDetails tbody tr").length;
	if (rowcount < 30) {
		if ($('contractorDetailRow') != null) {
			// get Next Row Index to Generate
			var nextIdx = 0;
			nextIdx = $("#tblContractorDetails tbody tr").length;
			
			// validate status variable for exiting function
			var isValid = 1;// for default have success value 0

			// validate existing rows in table
			$("#tblContractorDetails tbody tr").find('input, select').each(
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
			$("#tblContractorDetails tbody tr").last().clone().find("input, select").each(
					function() {

						if ($(this).data('server')) {
							$(this).removeAttr('data-server');
						}
						
							$(this).attr(
									{
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
							
							$(this).removeAttr('disabled');
							$(this).prop('checked', false);

					}).end().appendTo("#tblContractorDetails tbody");
			
			generateSno();
			initializeDatePicker();
		}
	} else {
		  bootbox.alert('limit reached!');
	}
}

function generateSno()
{
	var idx=1;
	$(".spansno").each(function(){
		$(this).text(idx);
		idx++;
	});
}

function checkStartDateAndEndDate() {
	var fromDateElements = $(".fromdate");
	var toDateElements = $(".todate");
	var checkDateFlag = true;
	$.each(toDateElements, function(index) {
		var fromDateValue = $(fromDateElements[index]).val().split("/");
		var toDateValue = $(this).val().split("/");
		if ($(this).val() != ""
				&& (new Date(fromDateValue[2], fromDateValue[1] - 1,
						fromDateValue[0]) > new Date(toDateValue[2],
						toDateValue[1] - 1, toDateValue[0]))) {
			bootbox.alert($("#fromDateGreaterThanEndDateError").val());
			checkDateFlag = false;
			return false;
		}
	});
	if (checkDateFlag)
		return true;
	else
		return false;
}

$('#submitBtn').click(function() {
	if ($('#contractorForm').valid()) {
		if (!checkCodeAndName()){
			return false;
		}
		
		if(!checkStartDateAndEndDate()){
			return false;
		}
		
		if(!checkPanNumber()){
			return false;
		}
		
		if (!checkTinNumber()){
			return false;
		}
		
		if(!checkEmail()){
			return false;
		}
		
		if(!checkMobileNumber()){
			return false;
		}
		
		if(!checkBankAccountNumber()){
			return false;
		}
		
		if(!checkIfscCode()){
			return false;
		}
		
		if(!checkContactPerson()){
			return false;
		}
		
		return true;
	}
	return false;
});

function deleteContractorDetail(obj) {
	var rIndex = getRow(obj).rowIndex;
	
	var tbl=document.getElementById('tblContractorDetails');	
	var rowcount=$("#tblContractorDetails tbody tr").length;
	
	if(rowcount<=1) {
		bootbox.alert($("#rowDeleteErrorMessage").val());
		return false;
	} else {
		tbl.deleteRow(rIndex);
		//starting index for table fields
		var idx= 0;
		var sno = 1;
		//regenerate index existing inputs in table row
		$("#tblContractorDetails tbody tr").each(function() {
		
				$(this).find("input, select, textarea, errors, span, input:hidden").each(function() {
					var classval = $(this).attr('class');
					if(classval == 'spansno') {
						$(this).text(sno);
						sno++;
					} else {
					$(this).attr({
					      'name': function(_, name) {
					    	  if(!($(this).attr('name')===undefined))
					    		  return name.replace(/\[.\]/g, '['+ idx +']'); 
					      },
					      'id': function(_, id) {
					    	  if(!($(this).attr('id')===undefined))
					    		  return id.replace(/\[.\]/g, '['+ idx +']'); 
					      },
					      'class' : function(_, name) {
								if(!($(this).attr('class')===undefined))
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
		generateSno();
		initializeDatePicker();
		return true;
	}	
}

function getRow(obj) {
	if(!obj)
		return null;
	tag = obj.nodeName.toUpperCase();
	while(tag != 'BODY'){
		if (tag == 'TR') 
			return obj;
		obj=obj.parentNode ;
		tag = obj.nodeName.toUpperCase();
	}
	return null;
}

function initializeDatePicker()
{
	$(".datepicker").datepicker({
		format: "dd/mm/yyyy",
		autoclose:true
	});
	try {
		$(".datepicker").inputmask();
		} catch (e) {
	}	
}

function createNewContractor() {
	window.location = "contractor-newform";
}

function modifyContractor(contractorId) {
	window.location = '/egworks/masters/contractor-update/'+contractorId;
}
