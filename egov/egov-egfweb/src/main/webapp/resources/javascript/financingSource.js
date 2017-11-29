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
function onloadtask(){
	document.getElementsByName('subschmselectionOpt')[1].checked = true;
}

function ShowRelatedHeader(obj){
	
	if(obj.value == "1"){
		dom.get('finSrcHeaderMiddle').style.display="block";
		dom.get('shrdfinSrcHead').style.display="block";
		dom.get('finSrcHeaderLower').style.display="block";
		dom.get('finSrcHeaderUpper').style.display="none";

	}else if(obj.value == "0"){
		dom.get('finSrcHeaderUpper').style.display="block";
		dom.get('finSrcHeaderMiddle').style.display="none";
		dom.get('shrdfinSrcHead').style.display="none";
		dom.get('finSrcHeaderLower').style.display="none";
	}
   	 
     
	
}
function loadAccNum(subscheme){
		var subschemeId = subscheme.options[subscheme.selectedIndex].value;
		if(subschemeId != -1){
			populateaccountNumber({subSchemeId: subschemeId,typeOfAccount:"RECEIPTS_PAYMENTS,RECEIPTS"});
			var url = 'financingSource!getIntEstAmt.action?subSchemeId='+subschemeId;
			var transaction = YAHOO.util.Connect.asyncRequest('POST', url, callbackAmount, null);
		}
		
	}

var callbackAmount = {
success: function(o) {
		document.getElementById('initEstAmt').value = o.responseText;
		calcPercAmt();
		
    },
    failure: function(o) {
    	bootbox.alert('failure');
    }
}
function calcPercAmt(){
	if(document.getElementById('sourceAmountMiddle').value.trim() !='' && document.getElementById('sourceAmountMiddle').value.trim() !="0" && document.getElementById('sourceAmountMiddle').value.trim() !="0.00" && document.getElementById('initEstAmt').value !="0" && document.getElementById('initEstAmt').value !=""){
			document.getElementById('loanPercentage').value = ((document.getElementById('sourceAmountMiddle').value/document.getElementById	('initEstAmt').value)*100).toFixed(2);
	}else{
		document.getElementById('loanPercentage').value="0";
	}
	
}
function calcPercOwnSrc(){
	var finSrcOwnSrcId = document.getElementById("finSrcOwnSrc").value;
	if(finSrcOwnSrcId != -1){
		var url = 'financingSource!getOwnSrcAmount.action?finSrcOwnSrcId='+finSrcOwnSrcId;
		var transaction = YAHOO.util.Connect.asyncRequest('POST', url, callbackSrcAmount, null);
	}else{
		document.getElementById('ownSrcPerc').value = "0";
	}
}
var callbackSrcAmount = {
		success: function(o) {
				
				document.getElementById('initEstAmt').value = o.responseText;
				calcSrcAmtPercAmt();
				
		    },
		    failure: function(o) {
		    	bootbox.alert('failure');
		    }
}
function calcSrcAmtPercAmt(){

	if(document.getElementById('sourceAmountOwnSrc').value.trim() !='' && document.getElementById('sourceAmountOwnSrc').value.trim() !="0" && document.getElementById('sourceAmountOwnSrc').value.trim() !="0.00" && document.getElementById('initEstAmt').value !="0" && document.getElementById('initEstAmt').value !=""){

		document.getElementById('ownSrcPerc').value = ((document.getElementById('sourceAmountOwnSrc').value/document.getElementById	('initEstAmt').value)*100).toFixed(2);
	}else{
		document.getElementById('ownSrcPerc').value="0";
	}
}
function manipulateHeaderByFundingType(fundingTypeObj){
	if(fundingTypeObj.value == 'Loan'){
		document.getElementById('rateOfIntrest').readOnly=false;
		document.getElementById('loanPeriod').readOnly=false;
		document.getElementById('moratoriumPeriod').readOnly=false;
		document.getElementById('repaymentFrequency').disabled=false;
		document.getElementById('noOfInstallment').readOnly=false;
		
		// display/hide the mandatory mark
		document.getElementById('rateOfIntrestMandate').style.visibility = "visible";
		document.getElementById('prdLoanMandate').style.visibility = "visible";
		document.getElementById('mrtmPrdMandate').style.visibility = "visible";
		document.getElementById('repFrqMandate').style.visibility = "visible";
		document.getElementById('noOfInstMandate').style.visibility = "visible";
		

	}
	else if(fundingTypeObj.value == 'Interest free loan'){
		document.getElementById('rateOfIntrest').value="";
		document.getElementById('rateOfIntrest').readOnly=true;
		document.getElementById('moratoriumPeriod').value="";
		document.getElementById('moratoriumPeriod').readOnly=true;
		document.getElementById('loanPeriod').readOnly=false;
		document.getElementById('repaymentFrequency').disabled=false;
		document.getElementById('noOfInstallment').readOnly=false;

		// display/hide the mandatory mark
		document.getElementById('rateOfIntrestMandate').style.visibility = "hidden";
		document.getElementById('prdLoanMandate').style.visibility = "visible";
		document.getElementById('mrtmPrdMandate').style.visibility = "hidden";
		document.getElementById('repFrqMandate').style.visibility = "visible";
		document.getElementById('noOfInstMandate').style.visibility = "visible";
		
	}
	else if(fundingTypeObj.value == 'Grant'){
		document.getElementById('rateOfIntrest').value="";
		document.getElementById('rateOfIntrest').readOnly=true;
		document.getElementById('loanPeriod').value="";
		document.getElementById('loanPeriod').readOnly=true;
		document.getElementById('moratoriumPeriod').value="";
		document.getElementById('moratoriumPeriod').readOnly=true;
		document.getElementById('repaymentFrequency').value = -1;
		document.getElementById('repaymentFrequency').disabled=true;
		document.getElementById('noOfInstallment').value="";
		document.getElementById('noOfInstallment').readOnly=true;
		
		// display/hide the mandatory mark
		document.getElementById('rateOfIntrestMandate').style.visibility = "hidden";
		document.getElementById('prdLoanMandate').style.visibility = "hidden";
		document.getElementById('mrtmPrdMandate').style.visibility = "hidden";
		document.getElementById('repFrqMandate').style.visibility = "hidden";
		document.getElementById('noOfInstMandate').style.visibility = "hidden";
	}
	
}
function manipulateHeaderdisable(obj,value){
	
	if(obj.value == -1){
		
		var headerId =  value.split('|');
		for(var i=0;i<headerId.length;i++){
			 toggleDisabled(document.getElementById(headerId[i]),false);
		}
	}else{
		var headerId =  value.split('|');
		for(var i=0;i<headerId.length;i++){
			 toggleDisabled(document.getElementById(headerId[i]),true);
		}
		
	}
}
 function toggleDisabled(el,flag) {
		
                try {
			
		  if(el.name != undefined){
			
			if(el.name !="subSchemeId" && el.name !='isactiveChkMiddle' && el.tagName.toLowerCase() != "option"){ // avoid to disable the option tag.
				el.disabled = flag;
			        el.value = "";
			       if(el.tagName.toLowerCase() == 'select'){
				  el.value = -1;
			       }
			}  
		 }
                }
                catch(E){}
               
                if (null !=el && el.childNodes && el.childNodes.length > 0) {
                    for (var x = 0; x < el.childNodes.length; x++) {
				
				toggleDisabled(el.childNodes[x],flag);
				
			
                    }
                }
            }
function validateHeaderBeforeAddingToGrid(){
	
   	if(document.getElementsByName('subschmselectionOpt')[0].checked){ // validation if fundsource is attached to a  Subscheme

		if( document.getElementById('finSrcOwnSrc').disabled ){
			return validateFSWithSubschemeFSNotShared();
		}else if( document.getElementById('fundingType').disabled){
			return validateFSWithSubschemeFSShared();
		}else{
			document.getElementById('lblError').innerHTML = "";
			document.getElementById('lblError').innerHTML = "Neither funding Type nor Shared financing source is selected";
			return false;
		}

	}else{ // validation  if fundsource is not attached to any subscheme
		
		return validateFSWithoutSubscheme();
	}
}

function validateFSWithoutSubscheme(){
	
	document.getElementById('lblError').innerHTML = "";
	
	if(document.getElementById('codeUpper').value.trim() == ""){
		
		document.getElementById('lblError').innerHTML = "Please enter code";
		return false;
	}
	else if(document.getElementById('nameUpper').value.trim() == ""){
		
		document.getElementById('lblError').innerHTML = "Please enter name";
		return false;
	}
	else if(document.getElementById('sourceAmountUpper').value.trim() == ""){
		
		document.getElementById('lblError').innerHTML = "Please enter source amount";
		return false;
	}	
	return true;
}
function validateFSWithSubschemeFSNotShared(){

	document.getElementById('lblError').innerHTML = "";
	var fundingType = document.getElementById('fundingType').value;
	if(document.getElementById('codeMiddle').value.trim() == ""){
		
		document.getElementById('lblError').innerHTML = "Please enter code";
		return false;
	}
	else if(document.getElementById('nameMiddle').value.trim() == ""){
		
		document.getElementById('lblError').innerHTML = "Please enter name";
		return false;
	}
	else if(document.getElementById('sourceAmountMiddle').value.trim() == ""){
		
		document.getElementById('lblError').innerHTML = "Please enter source amount";
		return false;
	}	
	
	else if(document.getElementById('subschemeid').value == -1){

		document.getElementById('lblError').innerHTML = "Please select a subscheme";
		return false;

	}
	else if(document.getElementById('finInstId').value == -1){
		
		document.getElementById('lblError').innerHTML = "Please select Name of Financial Institution / Agency";
		return false;
	}
	else if(fundingType == -1){
		
		document.getElementById('lblError').innerHTML = "Please select type of funding";
		return false;
	}
	else if(fundingType == "Loan" && document.getElementById('rateOfIntrest').value.trim() == ""){
		
		document.getElementById('lblError').innerHTML = "Please enter rate of intrest";
		return false;
	}
	else if((fundingType == "Loan" || fundingType == "Interest free loan") && document.getElementById('loanPeriod').value.trim() == ""){
		
		document.getElementById('lblError').innerHTML = "Please enter period of loan";
		return false;
	}
	else if(fundingType == "Loan" && document.getElementById('moratoriumPeriod').value.trim() == ""){
		
		document.getElementById('lblError').innerHTML = "Please enter moratorium period";
		return false;
	}
	else if((fundingType == "Loan" || fundingType == "Interest free loan") && document.getElementById('repaymentFrequency').value == -1){
		
		document.getElementById('lblError').innerHTML = "Please select re-payment frequency";
		return false;
	}
	else if((fundingType == "Loan" || fundingType == "Interest free loan") && document.getElementById('noOfInstallment').value.trim() == ""){
		
		document.getElementById('lblError').innerHTML = "Please enter total number of installments for re-payment";
		return false;
	}
	else if(document.getElementById('accountNumber').value == -1){
		
		document.getElementById('lblError').innerHTML = "Please select a bank account";
		return false;
	}
	return true;

}
function validateFSWithSubschemeFSShared(){
	document.getElementById('lblErrorOwnSrc').innerHTML = "";
	if(document.getElementById('subschemeid').value == -1){

		document.getElementById('lblErrorOwnSrc').innerHTML = "Please select a subscheme";
		return false;
	}
	else if(document.getElementById('sourceAmountOwnSrc').value == "" || document.getElementById('sourceAmountOwnSrc').value == "0.0"){
		
		document.getElementById('lblErrorOwnSrc').innerHTML = "please enter source amount";
		return false;
	}
	
	return true;
}


function uniqueCheckCodeUpper(){
       if(dom.get("codeUpper").value.trim() !=""){
		populatefinSrcCodeUpperunique({code:dom.get('codeUpper').value});
      }
	
}
function clearCodeUpperIfExists(){
	 if(dom.get("finSrcCodeUpperunique").style.display =="" ){
		 document.getElementById('codeUpper').value="";
	 }	
}
function uniqueCheckNameUpper(){
	 if(dom.get("nameUpper").value.trim() !=""){
		populatefinSrcNameUpperunique({name:dom.get('nameUpper').value});
	 }
}
function clearNameUpperIfExists(){
	 if(dom.get("finSrcNameUpperunique").style.display =="" ){
		 document.getElementById('nameUpper').value="";
	 }
}
function uniqueCheckCodeMiddle(){
       if(dom.get("codeMiddle").value.trim() !=""){
		populatefinSrcCodeUpperunique({code:dom.get('codeMiddle').value});
      }
	
}
function clearCodeMiddleIfExists(){
	 if(dom.get("finSrcCodeMiddleunique").style.display =="" ){
		 document.getElementById('codeMiddle').value="";
	 }	
}
function uniqueCheckNameMiddle(){
	 if(dom.get("nameMiddle").value.trim() !=""){
		populatefinSrcNameMiddleunique({name:dom.get('nameMiddle').value});
	 }
}
function clearNameMiddleIfExists(){
	 if(dom.get("finSrcNameMiddleunique").style.display =="" ){
		 document.getElementById('nameMiddle').value="";
	 }
}
$.fn.clearForm = function() {
        return this.each(function() {
          var type = this.type, tag = this.tagName.toLowerCase();
          if (tag == 'form'){
           return $(':input',this).clearForm();
	  }
          if (type == 'text' || type == 'password' || tag == 'textarea'){
		 this.disabled=false;
           	  this.value = '';
	  }
	   else if (tag == 'select'){
	    this.disabled=false;
	    this.value = -1;
	  }
         // else if (type == 'checkbox' || type == 'radio'){
           // this.checked = false;
	 // }
         
            
        });
  
      };
String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
}
