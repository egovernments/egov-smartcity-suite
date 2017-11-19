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
function onSubmit(obj) {
	document.forms[0].action = obj;
	document.forms[0].submit;
	return true;
}

function showPropertyHeaderTab() {
	document.getElementById('property_header').style.display = '';
	setCSSClasses('propertyHeaderTab', 'First Active');
	setCSSClasses('objectionDetailTab', '');
	hideObjectionHeaderTab();

}
function showObjectionHeaderTab() {
	document.getElementById('objection_header').style.display = '';
	setCSSClasses('propertyHeaderTab', 'First BeforeActive');
	setCSSClasses('objectionDetailTab', 'Last Active ActiveLast');
	hidepropertyHeaderTab();

}

function setCSSClasses(id, classes) {
	document.getElementById(id).setAttribute('class', classes);
	document.getElementById(id).setAttribute('className', classes);
}
function hidepropertyHeaderTab() {
	document.getElementById('property_header').style.display = 'none';
}
function hideObjectionHeaderTab() {
	document.getElementById('objection_header').style.display = 'none';
}
function hideApprovalHeaderTab() {
	document.getElementById('approval_header').style.display = 'none';

}
function checkLength(obj) {
	if (obj.value.length > 1024) {
		bootbox.alert('Max 1024 characters are allowed for comments. Remaining characters are truncated.')
		obj.value = obj.value.substring(1, 1024);
	}
}

function validateRecordObjections() {
	document.getElementById("lblError").style.display = 'none';

	if (dom.get('details').value == '') {
		bootbox.alert('Please enter Revision Petition Details');
		return false;
	}
	return true;
}

function validateHearingDate() {

	if (dom.get('plannedHearingDtId').value == "") {
		bootbox.alert('Please enter Hearing Date');
		return false;
	} else if (dom.get('hearingTime').value == "") {
		bootbox.alert('Please enter Hearing time');
		return false;
	} else if (dom.get('hearingVenue').value == "") {
		bootbox.alert('Please enter Hearing venue');
		return false;
	}
	return true;
}

function printHearingNotice() {
	var url;
	url = "/ptis/revPetition/revPetition-printHearingNotice.action?objectionId="
			+ document.getElementById("model.id").value;
	document.getElementById("lblError").style.display = 'none';

	window.open(url, 'printHearingNotice', 'width=1000,height=400');

}

function printEnodresementNotice(obj) {
	var url;
	url = "/ptis/revPetition/revPetition-printEnodresementNotice.action?objectionId="
			+ document.getElementById("model.id").value;
	document.getElementById("lblError").style.display = 'none';
	document.getElementById("workflowBean.actionName").value = obj.value;

	window.open(url, 'printEnodresementNotice', 'width=1000,height=400');

}

function validateRecordHearing() {

	if (dom.get('hearingDetails').value == "") {
		bootbox.alert('Please enter Hearing Details');
		return false;
	} else if (dom.get('inspectionRequiredtrue').checked == false
			&& dom.get('inspectionRequiredfalse').checked == false) {
		bootbox.alert('Please choose if Inspection Required or not');
		return false;
	}

	return true;
}

function validateRecordInspection() {

	/*
	 if (dom.get('generateSpecialNotice').checked == false) {
			bootbox.alert('Please choose whether special Notice to be generate after final approval');
			return false;
		}
	*/	
	if (dom.get('inspectionRemarks').value == "") {
		bootbox.alert('Please enter Inspection Remark');
		return false;
	}
	 /*if (dom.get('generateSpecialNotice').checked == false) {
		bootbox.alert('Please choose whether special Notice to be generate after final approval');
		return false;
	}*/
	return true;
}

function validateObjectionOutcome(obj) {
	
	if (dom.get('dateOfOutcome').value == '') {
		bootbox.alert('Please enter Outcome Date');
		return false;
	} else if (dom.get('outcomeRemarks').value == '') {
		bootbox.alert('Please enter Outcome Remarks');
		return false;
	}

	return true;
}

function validateApproval(obj) {
	if (obj.value.toUpperCase() == "REJECT"
			|| obj.value.toUpperCase() == "SAVE"
			|| obj.value.toUpperCase() == "APPROVE"
			|| obj.value.toUpperCase() == "Generate Notice") {
		return true;
	} else if (document.getElementById("approverUserId")
			&& document.getElementById("approverUserId").value == "-1") {
		document.getElementById("lblError").style.display = 'block';
		document.getElementById("lblError").innerHTML = 'Please select Employee';
		return false;
	} else {
		return true;
	}

}

function showDocumentManager() {
	var docNum = document.getElementById("docNumber").value;
	var url;
	if (docNum == null || docNum == '' || docNum == 'To be assigned') {
		url = "/egi/docmgmt/basicDocumentManager.action?moduleName=ptis";
	} else {
		url = "/egi/docmgmt/basicDocumentManager!editDocument.action?docNumber="
				+ docNum + "&moduleName=ptis";
	}
	window.open(url, 'docupload', 'width=1000,height=400');
}
