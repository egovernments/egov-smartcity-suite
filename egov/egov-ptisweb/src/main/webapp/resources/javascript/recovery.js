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

function showPropertyHeaderTab() {
	document.getElementById('property_header').style.display = '';
	setCSSClasses('propertyHeaderTab', 'First Active');
	setCSSClasses('recoveryDetailTab', '');
	setCSSClasses('approvalTab', 'Last');
	hiderecoveryHeaderTab();
	hideApprovalHeaderTab();

}
function showRecoveryHeaderTab() {
	document.getElementById('recovery_header').style.display = '';
	setCSSClasses('propertyHeaderTab', 'First BeforeActive');
	setCSSClasses('recoveryDetailTab', 'Active');
	setCSSClasses('approvalTab', 'Last');
	hidepropertyHeaderTab();
	hideApprovalHeaderTab();

}

function showApprovalTab() {
	document.getElementById('approval_header').style.display = '';
	setCSSClasses('propertyHeaderTab', 'First');
	setCSSClasses('recoveryDetailTab', 'BeforeActive');
	setCSSClasses('approvalTab', 'Last Active ActiveLast');
	hidepropertyHeaderTab();
	hiderecoveryHeaderTab();

}

function setCSSClasses(id, classes) {
	document.getElementById(id).setAttribute('class', classes);
	document.getElementById(id).setAttribute('className', classes);
}
function hidepropertyHeaderTab() {
	document.getElementById('property_header').style.display = 'none';
}
function hiderecoveryHeaderTab() {
	document.getElementById('recovery_header').style.display = 'none';
}
function hideApprovalHeaderTab() {
	document.getElementById('approval_header').style.display = 'none';

}

function validateIntimateRecovery(obj) {

	document.getElementById("lblError").style.display = 'none';
	if (dom.get('paymentDueDate').value == '') {
		document.getElementById("lblError").style.display = 'block';
		document.getElementById("lblError").innerHTML = "Please Enter Payment Due Date";
		return false;
	} else {
		return validateApproval(obj);
	}

}
function validateApproval(obj) {
	document.getElementById("workflowBean.actionName").value = obj.value;

	if (obj.value.toUpperCase() == "REJECT"
			|| obj.value.toUpperCase() == "SAVE"
			|| obj.value.toUpperCase() == "APPROVE") {
		return true;
	} else if (document.getElementById("approverUserId")
			&& document.getElementById("approverUserId").value == "-1") {
		document.getElementById("lblError").style.display = 'block';
		document.getElementById("lblError").innerHTML = 'Please select Employee ';
		return false;
	} else {
		return true;
	}

}
function checkLength1024(obj) {
	if (obj.value.length > 1024) {
		bootbox.alert('Max 1024 characters are allowed. Remaining characters are truncated.')
		obj.value = obj.value.substring(1, 1024);
	}
}

function validateWarrantApplication(obj) {
	document.getElementById("lblError").style.display = "none";
	document.getElementById("lblError").innerHTML ="";
	var warrentFee = document.getElementById('warrentFee').value;
	var noticeFee = document.getElementById('noticeFee').value;
	
		if (warrentFee == '' || warrentFee =="0" || warrentFee =="0.00" ) {
			document.getElementById("lblError").style.display = 'block';
			document.getElementById("lblError").innerHTML = "Please Enter Warrant Fee";
			return false;
		}
		/*else  if (courtFee == '') {
			document.getElementById("lblError").style.display = 'block';
			if(document.getElementById("lblError").innerHTML == ''){
				document.getElementById("lblError").innerHTML = "Please Enter Court Fee";
			}else{			
			document.getElementById("lblError").innerHTML = document.getElementById("lblError").innerHTML+ ", Court Fee";
			}
		}*/
		else if (noticeFee == ''|| noticeFee =="0" || noticeFee =="0.00") {
			document.getElementById("lblError").style.display = 'block';
			document.getElementById("lblError").innerHTML = "Please Enter Notice Fee";
			return false;
		}else{
			document.getElementById('courtFee').value=0.00;
			return validateApproval(obj);
		}
		
	
}

function totalFees(obj) {
	var warrentFee = document.getElementById('warrentFee').value != ""?document.getElementById('warrentFee').value:0.00;
	var courtFee = document.getElementById('courtFee').value != ""?document.getElementById('courtFee').value:0.00;
	var noticeFee = document.getElementById('noticeFee').value != ""?document.getElementById('noticeFee').value:0.00;
	var totalFee = parseFloat(noticeFee) + parseFloat(courtFee)
			+ parseFloat(warrentFee);
	  var roundedOffTotalFee = roundoff(totalFee);
	document.getElementById('ttlFee').innerHTML = roundedOffTotalFee;
		}

function validateWarrantNotice(obj){
	document.getElementById("lblError").style.display='none';
	if(dom.get('warrantReturnByDate').value==''){
		document.getElementById("lblError").style.display='block';
		document.getElementById("lblError").innerHTML  = "Please Enter Warrant Retuen By Date";
		return false;
	}else{
		return validateApproval(obj);
	} 
}
function validateCeaseNotice(obj){
		document.getElementById("lblError").style.display='none';
		if(dom.get('executionDate').value==''){
			document.getElementById("lblError").style.display='block';
			document.getElementById("lblError").innerHTML  = "Please Enter Execution Date";
			return false;
		}else{
			return validateApproval(obj);
		} 
}
function displayNotice(noticeNumber) {
		var sUrl = "/egi/docmgmt/ajaxFileDownload.action?moduleName=PT&docNumber="+noticeNumber+"&fileName="+noticeNumber+".pdf";
		window.open(sUrl,"window",'scrollbars=yes,resizable=no,height=200,width=400,status=yes');
}


