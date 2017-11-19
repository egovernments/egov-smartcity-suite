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
function setDropDownsAlignment(){
	var elements = jQuery("#contractorTable").find("tbody.yui-dt-data").find("tr");
		elements.find("td.yui-dt-col-departmentName").css('width', '15%');
		elements.find("td.yui-dt-col-categoryName").css('width', '15%');
		elements.find("td.yui-dt-col-gradeName").css('width', '15%');
		elements.find("td.yui-dt-col-statusDesc").css('width', '15%');
		elements.find("td.yui-dt-col-departmentName").find("select").css('width', '100%');
		elements.find("td.yui-dt-col-categoryName").find("select").css('width', '100%');
		elements.find("td.yui-dt-col-gradeName").find("select").css('width', '100%');
		elements.find("td.yui-dt-col-statusDesc").find("select").css('width', '100%');
}

function setTextFieldsAlignment(){
	var elements = jQuery("#contractorTable").find("tbody.yui-dt-data").find("tr")
		elements.find("td.yui-dt-col-registrationNumber").css('width', '15%');
		elements.find("td.yui-dt-col-startDate").css('width', '15%');
		elements.find("td.yui-dt-col-endDate").css('width', '15%');
		elements.find("td.yui-dt-col-registrationNumber").find("input").css('width', '100%');
		elements.find("td.yui-dt-col-startDate").find("input").css('width', '100%');
		elements.find("td.yui-dt-col-endDate").find("input").css('width', '100%');
}

function setDefaultDepartment() {
	var elements = jQuery("#contractorTable").find("tbody.yui-dt-data").find("tr")
	for(var i=0;i<elements.length;i++){
		var departmentElement = jQuery(jQuery(elements[i]).find("select")[0])
		departmentElement.val(jQuery('#defaultDepartmentId').val());
	}
}

jQuery(document).ready(function(){
	flag = false;
	if(jQuery("#mandatory").val() != null){
		var loadMandatoryFields = jQuery("#mandatory").val().replace(/[\[\]']+/g,'').split(",");
		jQuery.each(loadMandatoryFields,function(){
			var fieldName = this.toString().trim();
			var label = jQuery("label[for='"+fieldName+"']");
			label.append("<span class = 'mandatory'></span>");
		})
	}
	if(jQuery("#hide").val() != null){
		var loadHiddenFields = jQuery("#hide").val().replace(/[\[\]']+/g,'').split(",");
		jQuery.each(loadHiddenFields,function(){
			var fieldName = this.toString().trim();
			var label = jQuery("label[for='"+fieldName+"']");
			label.hide();
			jQuery("#"+fieldName).hide();
			jQuery("#"+fieldName+"-value").hide();
			jQuery("#"+fieldName).parent().hide();
		})
	}
	
	jQuery("#mobileNumber").on("input", function(){
		var valid = /\d+$/.test(this.value),
	       val = this.value;
	   
	   if(!valid){
	       console.log("Invalid input!");
	       this.value = val.substring(0, val.length - 1);
	   }
		});
	setDropDownsAlignment();
	setTextFieldsAlignment();

	if(jQuery("#codeautogeneration").val() == "true" && jQuery("#mode").val() != "edit"){
		jQuery("label[for='code']").remove();
		jQuery("#contractor-save_code").parent().remove();
	}
	if(jQuery("#mode").val() != "edit"){
		setDefaultDepartment();
	}
	jQuery("#addContractorDetails").on("click",function(){
		setDropDownsAlignment();
		setTextFieldsAlignment();
	});
	

	jQuery('.textfieldsvalidate').on('input',function(){
		var regexp_textfields = /[^0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]/g;
		if(jQuery(this).val().match(regexp_textfields)){
			jQuery(this).val( jQuery(this).val().replace(regexp_textfields,'') );
		}
	})
});

function checkMobileNumber(){
	if(jQuery("#mobileNumber").val().length == 0){
		showMessage('contractor_error', jQuery("#mobileNumberErrorMsg").val());
		flag = true;
		return false;
		}
	var regx = /\d{10}$/;
	if (jQuery("#mobileNumber").val().length < 10 || !regx.test(jQuery("#mobileNumber").val())) {
		  showMessage('contractor_error', jQuery("#mobileNumberErrorMsg").val());
		  flag = true;
		  return false;
	  }
	  return true;
}
function checkPanNumber(){
	if(jQuery("#panNumber").val().length == 0){
		showMessage('contractor_error', jQuery("#panNumberErrorMsg").val());
		flag = true;
		return false;
		}
  var regx = /^[A-Za-z]{5}\d{4}[A-za-z]{1}$/;
  if (!regx.test(jQuery("#panNumber").val())) {
	  showMessage('contractor_error', jQuery("#panNumberMessage").val());
	  flag = true;
	  return false;
  }
  return true;
}

function checkTinNumber(){
	if(jQuery("#tinNumber").val().length == 0){
		showMessage('contractor_error', jQuery("#tinNumberErrorMsg").val());
		flag = true;
		return false;
		}
  var regx = /^[A-Za-z0-9]+$/;
  if (!regx.test(jQuery("#tinNumber").val())) {
	  showMessage('contractor_error', jQuery("#tinNumberAlphaNeumericErrorMsg").val());
	  flag = true;
	  return false;
  }
  return true;
}

function checkContactPerson(){
	if(jQuery("#contactPerson").val().length == 0){
		showMessage('contractor_error', jQuery("#contactPersonErrorMsg").val());
		flag = true;
		return false;
	}
	var regx = /^[A-Za-z0-9 ]+$/;
	  if (!regx.test(jQuery("#contactPerson").val())) {
		  showMessage('contractor_error', jQuery("#contactPersonAlphaNeumericErrorMsg").val());
		  flag = true;
		  return false;
	  }
	return true;
}

function checkIfscCode(){
	if(jQuery("#ifscCode").val().length == 0){
		showMessage('contractor_error', jQuery("#ifscCodeErrorMsg").val());
		flag = true;
		return false;
		}
  var regx = /^[A-Za-z0-9]+$/;
  if (!regx.test(jQuery("#ifscCode").val())) {
	  showMessage('contractor_error', jQuery("#ifscCodeAlphaNeumericErrorMsg").val());
	  flag = true;
	  return false;
  }
  return true;
}

function checkCorrespondenceAddress(){
	
	if(jQuery("#correspondenceAddress").val().length == 0){
		showMessage('contractor_error', jQuery("#correspondenceAddressErrorMsg").val());
		flag = true;
		return false;
		}
  return true;
}

function checkPaymentAddress(){
	if(jQuery("#paymentAddress").val().length == 0){
		showMessage('contractor_error', jQuery("#paymentAddressErrorMsg").val());
		flag = true;
		return false;
		}
  return true;
}

function checkBankAccountNumber(){
	if(jQuery("#bankAccount").val().length == 0){
		showMessage('contractor_error', jQuery("#bankAccountErrorMsg").val());
		flag = true;
		return false;
		}
  var regx = /^[A-Za-z0-9]+$/;
  if (!regx.test(jQuery("#bankAccount").val())) {
	  showMessage('contractor_error', jQuery("#bankAccountAlphaNeumericErrorMsg").val());
	  flag = true;
	  return false;
  }
  return true;
}

function checkBank(){
	if(jQuery("#bank").val() == -1){
	 showMessage('contractor_error', jQuery("#bankErrorMsg").val());
	 flag = true;
	 return false;
	}
	return true;
}

function checkEmail(){
	if(jQuery("#email").val().length == 0){
		showMessage('contractor_error', jQuery("#emailErrorMsg").val());
		flag = true;
		return false;
		}
	var regx = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	if (!regx.test(jQuery("#email").val())) {
	  showMessage('contractor_error', jQuery("#emailInvalidErrorMsg").val());
	  flag = true;
	  return false;
	}
	return true;
}

function checkNarration(){
	if(jQuery("#narration").val().length == 0){
		showMessage('contractor_error', jQuery("#narrationErrorMsg").val());
		flag = true;
		return false;
		}
	return true;
}

function checkPwdApprovalCode(){
	if(jQuery("#pwdApprovalCode").val().length == 0){
		showMessage('contractor_error', jQuery("#pwdApprovalCodeErrorMsg").val());
		flag = true;
		return false;
		}
	var regx = /^[A-Za-z0-9\-\/]+$/;
	if (!regx.test(jQuery("#pwdApprovalCode").val())) {
		  showMessage('contractor_error', jQuery("#pwdApprovalCodeAlphaNeumericErrorMsg").val());
		  flag = true;
		  return false;
	  }
	return true;
}

function checkExemptionForm(){
	if(jQuery("#exemptionForm").val().length == 0){
		showMessage('contractor_error', jQuery("#exemptionFormErrorMsg").val());
		flag = true;
		return false;
		}
	return true;
}

function checkRegistrationNumber(){
	var elements = jQuery(".selectmultilinewk");
	for(var i = 0;i< elements.length;i++){
		if(jQuery("#registrationNumberyui-rec"+i).val() == ''){
			showMessage('contractor_error', jQuery("#registrationNumberErrorMsg").val());
			flag = true;
			return false;
			}
		var regx = /^[A-Za-z0-9\-\/]+$/;
		  if (!regx.test(jQuery("#registrationNumberyui-rec"+i).val())) {
			  showMessage('contractor_error', jQuery("#registrationNumberAlphaNeumericErrorMsg").val());
			  flag = true;
			  return false;
		  }
		}
	return true;
}

function checkContractorClass(){
	var elements = jQuery(".yui-dt-dropdown");
	for(var i=0;i < elements.length;i++){
		if(jQuery("#gradeNameyui-rec"+i).val() == 0){
			showMessage('contractor_error', jQuery("#contractorClassErrorMsg").val());
			flag = true;
			return false;
			}
	}
	return true;
}

function checkStartDate(){
	var elements = jQuery(".selectmultilinewk");
	for(var i = 0;i< elements.length;i++){
		if(jQuery("#startDateyui-rec"+i).val() == ''){
			showMessage('contractor_error', jQuery("#startDateErrorMsg").val());
			flag = true;
			return false;
			}
		}
	return true;
}

function checkDepartment(){
	var elements = jQuery("#contractorTable").find("tbody.yui-dt-data").find("tr");
	for(var i=0 ; i < elements.length ; i++){
		var departmentElement = jQuery(jQuery(elements[i]).find("select")[0])
		if(departmentElement.val() == 0){
			showMessage('contractor_error', jQuery("#departmentErrorMsg").val());
			return false;
		}
	}
	return true;
}

function checkCodeAndName(){  
	var regxCode = /^[A-Za-z0-9\-\/]+$/
	var regxName = /^[A-Za-z0-9\-\& \:\,\/\.()\@]+$/;
	if(!(jQuery("#codeautogeneration").val() == "true")){
		if(jQuery("#contractor-save_code").val().length == 0){
			showMessage('contractor_error', jQuery("#codeErrorMsg").val());
			return false;
		}
		  
		  if (!regxCode.test(jQuery("#contractor-save_code").val())) {
			  showMessage('contractor_error', jQuery("#codeAlphaNeumericErrorMsg").val());
			  return false;
		  }

		  if(jQuery("#name").val().length == 0){
				showMessage('contractor_error', jQuery("#nameErrorMsg").val());
				return false;
		  }
		  
		  if (!regxName.test(jQuery("#name").val())) {
			  showMessage('contractor_error', jQuery("#nameAlphaNeumericErrorMsg").val());
			  return false;
		  }
	}else{

		  if(jQuery("#name").val().length == 0){
				showMessage('contractor_error', jQuery("#nameErrorMsg").val());
				return false;
			}
		  
		  if (!regxName.test(jQuery("#name").val())) {
			  showMessage('contractor_error', jQuery("#nameAlphaNeumericErrorMsg").val());
			  return false;
		  }
			  
		 if(jQuery("#name").val().length < 4){
				showMessage('contractor_error', jQuery("#nameErrorLengthMsg").val());
				return false;
		 }
	}
  
  return true;
}

function checkContractorCategory(){
	var elements = jQuery("#contractorTable").find("tbody.yui-dt-data").find("tr")
	for(var i=0;i < elements.length;i++){
		if(jQuery(elements[i]).find("td:nth-child(8)").find("select").val() == 0){
			showMessage('contractor_error', jQuery("#contractorCategoryErrorMsg").val());
			return false;
		}
	}
	return true;
}

function checkContractorDetailsMandatoryFieldsErrors(fieldName){
	if (fieldName == "registrationNumber")
		checkRegistrationNumber();
	else if (fieldName == "grade")
		checkContractorClass();
	else if (fieldName == "category")
		checkContractorCategory();
}

function checkContractorMasterMandatoryFieldsErrors(fieldName){
	if (fieldName == "panNumber") 
		checkPanNumber();
	else if (fieldName == "tinNumber")
		checkTinNumber();
	else if (fieldName == "contactPerson")
		checkContactPerson();
	else if (fieldName == "email")
		checkEmail();
	else if (fieldName == "mobileNumber")
		checkMobileNumber();
	else if (fieldName == "bank")
		checkBank();
	else if (fieldName == "bankAccount")
		checkBankAccountNumber();
	else if (fieldName == "correspondenceAddress")
		checkCorrespondenceAddress();
	else if (fieldName == "ifscCode")
		checkIfscCode();
	else if (fieldName == "paymentAddress")
		checkPaymentAddress();
	else if (fieldName == "narration")
		checkNarration();
	else if (fieldName == "exemptionForm")
		checkExemptionForm();
	else if (fieldName == "pwdApprovalCode")
		checkPwdApprovalCode();
	checkContractorDetailsMandatoryFieldsErrors(fieldName);
}

function checkForMandatoryValues(){
	if(jQuery("#mandatory").val() != null && jQuery("#hide").val() != null){
			var mandatoryFields = jQuery("#mandatory").val().replace(/[\[\]']+/g,'').replace(/, /g, ",").split(",");
			var hiddenFields = jQuery("#hide").val().replace(/[\[\]']+/g,'').replace(/, /g, ",").split(",");
			var contractorMasterMandatoryFields = mandatoryFields.filter(function(val) {
				  return hiddenFields.indexOf(val) == -1;
				});
			jQuery.each(contractorMasterMandatoryFields.reverse(),function(){
				var fieldName = this.toString().trim();
				checkContractorMasterMandatoryFieldsErrors(fieldName);
			});
			window.scroll(0,0);
			return flag;
	}
}

function validateContractorFormAndSubmit() {
	clearMessage('contractor_error');
	if(jQuery("#contractorTable").find("tbody.yui-dt-data").find("tr").length < 1) {
		showMessage('contractor_error', jQuery("#contractorDetailErrorMsg").val());
		window.scroll(0,0);
		return false;
	}
		
	if (!checkCodeAndName()){
		window.scroll(0,0);
		return false;
	}
	if (checkForMandatoryValues()){
		flag = false;
		return false; 
	}	
	if (!checkDepartment()){
		window.scroll(0,0);
		return false;
	}
	if (!checkStartDate()){
		window.scroll(0,0);
		return false;
	}
	
	links=document.contractor.getElementsByTagName("span");
	for(i=0;i<links.length;i++) {
		if(links[i].innerHTML=='&nbsp;x' && links[i].style.display!='none'){
			showMessage('contractor_error', jQuery("#vaidateError").val());
			window.scroll(0,0);
			return false;
		}
	}
}

function replaceExemptionFormChar() {
	var exemption = document.getElementById('exemptionForm-value').innerHTML;
	exemption = exemption.replace(/_/g, " ");
    document.getElementById("exemptionForm-value").innerHTML = exemption;
}


function createNewContractor() {
	window.location = "contractor-newform.action";
}

function modifyContractorData() {
	var model = document.getElementById('model').value;
	window.location = 'contractor-edit.action?mode=edit&id='+model;
}