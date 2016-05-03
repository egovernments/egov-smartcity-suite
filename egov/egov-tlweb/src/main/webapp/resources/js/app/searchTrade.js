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
function setupAjaxDivision(elem) {
	zone_id = elem.options[elem.selectedIndex].value;
	populatedivision( {
		zoneId : zone_id
	});
}

function numbersonly(myfield, e) {
	var key;
	var keychar;

	if (window.event)
		key = window.event.keyCode;
	else if (e)
		key = e.which;
	else
		return true;
	keychar = String.fromCharCode(key);

	// control keys
	if ((key == null) || (key == 0) || (key == 8) || (key == 9) || (key == 13)
			|| (key == 27))
		return true;

	// numbers
	else if ((("0123456789").indexOf(keychar) > -1))
		return true;
	else
		return false;
}

function chkNum(obj, checkAssessee) {
	obj.value = obj.value.replace(/[^0-9]/g, '') // numbers only

}

function trim(stringToTrim) {
	return stringToTrim.replace(/^\s+|\s+$/g, "");
}

function init() {
	waterMarkInitialize('applicationFromDate', 'dd/mm/yyyy');
	waterMarkInitialize('applicationToDate', 'dd/mm/yyyy');
	waterMarkInitialize('noticeFromDate', 'dd/mm/yyyy');
	waterMarkInitialize('noticeToDate', 'dd/mm/yyyy');
}
function clearWaterMark() {

	if (document.getElementById('applicationFromDate').value == 'dd/mm/yyyy') {
		document.getElementById('applicationFromDate').value = '';
	}

	if (document.getElementById('applicationToDate').value == 'dd/mm/yyyy') {
		document.getElementById('applicationToDate').value = '';
	}

	if (document.getElementById('noticeFromDate').value == 'dd/mm/yyyy') {
		document.getElementById('noticeFromDate').value = '';
	}
	if (document.getElementById('noticeToDate').value == 'dd/mm/yyyy') {
		document.getElementById('noticeToDate').value = '';
	}
}

function waterMarkTextIn(styleId, value) {
	if (document.getElementById(styleId) == null)
		return;
	var txt = document.getElementById(styleId).value;
	if (txt == value) {
		document.getElementById(styleId).value = '';
		document.getElementById(styleId).style.color = '';
	}
}

function waterMarkTextOut(styleId, value) {

	if (document.getElementById(styleId) == null)
		return;
	var txt = document.getElementById(styleId).value;
	if (txt == '') {
		document.getElementById(styleId).value = value;
		document.getElementById(styleId).style.color = 'DarkGray';
	}
}

function waterMarkInitialize(styleId, value) {
	if (document.getElementById(styleId) == null)
		return;
	if (document.getElementById(styleId).value == '') {
		document.getElementById(styleId).value = value;
		document.getElementById(styleId).style.color = 'DarkGray';
	}
}

function validateFromAndTo(from, to, strMsg) {
	
	if (document.getElementById(from).value == '' && document.getElementById(to).value == '') {
		return true;
	}

	var fromval = document.getElementById(from).value;
	var toval = document.getElementById(to).value;

	if (fromval == '' && toval != '') {
		document.getElementById("error").innerHTML = '<ul><li><span class="errorMessage" style="color: #FF0000">Please enter From value for ' + strMsg + '</li></ul>';
		return false;
	}

	if (fromval != '' && toval == '') {
		document.getElementById("error").innerHTML = '<ul><li><span class="errorMessage" style="color: #FF0000">Please enter To value for ' + strMsg + '</li></ul>';
		return false;
	}

	var iFromVal = parseInt(fromval);
	var iToVal = parseInt(toval);

	// check whether 'from' value is greater than 'to' value
	if (iFromVal > iToVal) {
		document.getElementById("error").innerHTML = '<ul><li><span class="errorMessage" style="color: #FF0000">From value of ' + strMsg + ' should be less than To value</li></ul>';
		return false;
	}
	return true;
}

function validateForm() {
	clearWaterMark();
	if (document.getElementById("applNumber").value == ''
			&& document.getElementById("applicantName").value == ''
			&& document.getElementById("applicationFromDate").value == ''
			&& document.getElementById("applicationToDate").value == ''
			&& document.getElementById("licenseNumber").value == ''
			&& document.getElementById("oldLicenseNum").value == ''
			&& document.getElementById("zone").value == '-1'
			&& document.getElementById("division").value == '-1'
			&& document.getElementById("tradeName").value == '-1'
			&& document.getElementById("establishmentName").value == ''
			&& document.getElementById("licenseFeeFrom").value == ''
			&& document.getElementById("licenseFeeTo").value == ''
			&& document.getElementById("noticeNumber").value == ''
			&& document.getElementById("noticeType").value == '-1'
			&& document.getElementById("noticeFromDate").value == ''
			&& document.getElementById("noticeToDate").value == ''
			&& document.getElementById("docNumber").value == ''
			&& !document.getElementById("motorInstalled").checked
			&& !document.getElementById("licenseexpired").checked
			&& !document.getElementById("licenseCancelled").checked
			&& !document.getElementById("licenseSuspended").checked
			&& !document.getElementById("licenseObjected").checked
			&& !document.getElementById("licenseRejected").checked)
		{
		document.getElementById("error").innerHTML = '<ul><li><span class="errorMessage" style="color: #FF0000">Please select at least one search criteria</li></ul>';
		return false;
	}
	return true;

}
function flipAdvanceOptions() {

	if (document.getElementById('advSearchOpt').style.display == 'none'
			|| document.getElementById('advSearchOpt').style.display == '') {
		document.getElementById('advSearchOpt').style.display = 'block';
	} else {
		document.getElementById('advSearchOpt').style.display = 'none';

	}
}
