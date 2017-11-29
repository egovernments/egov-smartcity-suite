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
/*Summary of Validations undertaken in Account Cheque Master.
 Below Validations are particular to a Bank Account. (Not accross bankaccount)

 1. If new serial no. is given, No validation for cheque range or department.
 2. Under a serial no. if cheque ranges are overlaping across department, then 'Invalid cheque range' is thrown.
 (eg for overlaping: existing cheque range: 000001-000100 new cheque range: 000050-000150)
 3. Combination of ChequeRange, Dept & SerialNo if already exist, then throws 'already exist'.
 4. Under a serial no. if same cheque range is given to different dept, This is accepted.

 Use case other than above 4 'll throw 'Invalid Cheque Range'
 */
function updateGridData() {

	document.getElementById("lblError").innerHTML = "";

	if (document.getElementById("fromChqNo").value.trim() == "") {
		document.getElementById("lblError").innerHTML = "Please enter from cheque number";
		return false;
	}
	if (document.getElementById("toChqNo").value.trim() == "") {
		document.getElementById("lblError").innerHTML = "Please enter to cheque number";
		return false;
	}
	if (document.getElementById("fromChqNo").value.trim().length != 6) {
		document.getElementById("lblError").innerHTML = "From Cheque number should be 6 digits";
		return false;
	}
	if (document.getElementById("toChqNo").value.trim().length != 6) {
		document.getElementById("lblError").innerHTML = "To Cheque number should be 6 digits";
		return false;
	}
	if (document.getElementById("fromChqNo").value.trim().length != document
			.getElementById("toChqNo").value.trim().length) {

		document.getElementById("lblError").innerHTML = "from cheque number and to cheque number length should be same";
		return false;
	}

	if (document.getElementById("receivedDate").value.trim() == "") {
		document.getElementById("lblError").innerHTML = "Please enter received date";
		return false;
	}
	var deptSelectedValue = new Array();
	var deptSelectedText = new Array();
	var deptObj = document.getElementById("departmentList");

	var serialNoSelectedValue = new Array();
	var serialNoSelectedText = new Array();
	var serialNoObj = document.getElementById("serialNo");

	
	for (var i = 0; i < deptObj.length; i++) {
		if (jQuery("#departmentList option")[i]['selected'] == true) {
			deptSelectedValue.push(deptObj.options[i].value);
			deptSelectedText.push(deptObj.options[i].text);
		}
	}
	
	for (var i = 0; i < serialNoObj.length; i++) {
		if (jQuery("#serialNo option")[i]['selected'] == true) {
			serialNoSelectedValue.push(serialNoObj.options[i].value);
			serialNoSelectedText.push(serialNoObj.options[i].text);
		}
	}
	
	if (deptSelectedValue == "") {
		document.getElementById("lblError").innerHTML = "Please select a department";
		return false;
	}
	if (document.getElementById("serialNo").value == "-1") {
		document.getElementById("lblError").innerHTML = "Please enter financial year";
		return false;
	}
	// validate invalid cheque range.
	var fromchqNum = parseInt(document.getElementById("fromChqNo").value.trim() * 1);
	var tochqNum = parseInt(document.getElementById("toChqNo").value.trim() * 1);
	if (fromchqNum >= tochqNum) {
		document.getElementById("lblError").innerHTML = "from cheque number should be less than to cheque number";
		return false;
	}
	for (var i = 0; i < deptSelectedValue.length; i++) {

		for (var j = 0; j < chequeRangeArray.length; j++) {
			var tokens = chequeRangeArray[j].split("-");
			 if (fromchqNum == parseInt(tokens[0] * 1)
					&& tochqNum == parseInt(tokens[1] * 1)
					&& deptSelectedValue[i] == tokens[2]
					&& serialNoSelectedValue[0] == tokens[3]) {

				document.getElementById("lblError").innerHTML = "Cheque Range is already assigned for department :"
						+ deptSelectedText[i] + " & SerialNo :" + serialNoSelectedText[0];
				return false;
			} else if (deptSelectedValue[i] != tokens[2]) {
				continue;
			} else if (serialNo != tokens[3]) {
				continue;
			} else if (fromchqNum > parseInt(tokens[0] * 1)
					&& fromchqNum > parseInt(tokens[1] * 1)
					&& tochqNum > parseInt(tokens[0] * 1)
					&& tochqNum > parseInt(tokens[1] * 1)
					&& deptSelectedValue[i] == tokens[2]
					&& serialNo == tokens[3]
					) {
				continue;
			} else {
				document.getElementById("lblError").innerHTML = "Invalid cheque range";
				return false;
			}

		}

	}

	for (var i = 0; i < deptSelectedValue.length; i++) {
		chequeDetailsGridTable.addRow({
			SlNo : chequeDetailsGridTable.getRecordSet().getLength() + 1
		});
		document.getElementById(CHQDETAILSLIST + '[' + chqDetailsIndex
				+ '].fromChqNo').value = document.getElementById("fromChqNo").value
				.trim();
		document.getElementById(CHQDETAILSLIST + '[' + chqDetailsIndex
				+ '].toChqNo').value = document.getElementById("toChqNo").value
				.trim();
		document.getElementById(CHQDETAILSLIST + '[' + chqDetailsIndex
				+ '].deptName').innerHTML = deptSelectedText[i];
		document.getElementById(CHQDETAILSLIST + '[' + chqDetailsIndex
				+ '].receivedDateL').innerHTML = document
				.getElementById("receivedDate").value;
		document.getElementById(CHQDETAILSLIST + '[' + chqDetailsIndex
				+ '].receivedDate').value = document
				.getElementById("receivedDate").value;
		document.getElementById(CHQDETAILSLIST + '[' + chqDetailsIndex
				+ '].serialNoL').innerHTML =serialNoSelectedText[0];
		document.getElementById(CHQDETAILSLIST + '[' + chqDetailsIndex
				+ '].serialNo').value = serialNoSelectedValue[0];
		document.getElementById(CHQDETAILSLIST + '[' + chqDetailsIndex
				+ '].nextChqPresent').value = "No";
		document.getElementById(CHQDETAILSLIST + '[' + chqDetailsIndex
				+ '].isExhusted').value = "No";
		document.getElementById(CHQDETAILSLIST + '[' + chqDetailsIndex
				+ '].isExhustedL').value = "No";
		document.getElementById(CHQDETAILSLIST + '[' + chqDetailsIndex
				+ '].isExhustedL').innerHTML = "No";
		document.getElementById(CHQDETAILSLIST + '[' + chqDetailsIndex
				+ '].deptId').value = deptSelectedValue[i];
		chqDetailsIndex = chqDetailsIndex + 1;
		chequeRangeArray.push(document.getElementById("fromChqNo").value.trim()
				+ "-" + document.getElementById("toChqNo").value.trim() + "-"
				+ deptSelectedValue[i] + "-"
				+ serialNoSelectedValue[0]);
	}
	clearHeaderData();
	return true;
}

function clearHeaderData() {

	document.getElementById("fromChqNo").value = "";
	document.getElementById("toChqNo").value = "";
	document.getElementById("receivedDate").value = "";
	document.getElementById("serialNo").value = "-1";
	var deptObj = document.getElementById("departmentList");
	while (deptObj.selectedIndex != -1) {
		deptObj.options[deptObj.selectedIndex].selected = false;

	}
}
// used to check the cheque range overlapping on blur of from cheque and to
// cheque number in the grid.
function validateCheque(obj) {
	document.getElementById("save").disabled = true;
	var count = 0;
	document.getElementById("lblErrorGrid").innerHTML = "";
	if (!obj.readOnly) {
		var index = obj.id.substring(18, 19);// to get index e.g"0" from the
		// string chequeDetailsList[0]
		if (document.getElementById(CHQDETAILSLIST + '[' + index
				+ '].fromChqNo').value.length != document
				.getElementById(CHQDETAILSLIST + '[' + index + '].toChqNo').value.length) {
			document.getElementById("lblErrorGrid").innerHTML = "From Cheque No. and To Cheque No. length should be same";
			return false;

		}
		var fromchqNum = document.getElementById(CHQDETAILSLIST + '[' + index
				+ '].fromChqNo').value * 1;
		var tochqNum = document.getElementById(CHQDETAILSLIST + '[' + index
				+ '].toChqNo').value * 1;
		var deptId = document.getElementById(CHQDETAILSLIST + '[' + index
				+ '].deptId').value;
		var deptName = document.getElementById(CHQDETAILSLIST + '[' + index
				+ '].deptName').innerHTML;
		var serialNo1 = document.getElementById(CHQDETAILSLIST + '[' + index
				+ '].serialNo');
		var serialNo =serialNo1.options[serialNo1.selectedIndex].value; 
		chequeRangeArray.splice(index, 1, fromchqNum + "-" + tochqNum + "-"
				+ deptId + "-" + serialNo);

		if (parseInt(fromchqNum) >= parseInt(tochqNum)) {
			document.getElementById("lblErrorGrid").innerHTML = "from cheque number should be less than to cheque number";
			return false;
		}

		for (var j = 0; j < chequeRangeArray.length; j++) {
			var tokens = chequeRangeArray[j].split("-");
			if ((fromchqNum < parseInt(tokens[0] * 1) && tochqNum < parseInt(tokens[0] * 1))
					|| (fromchqNum > parseInt(tokens[0] * 1) && fromchqNum > parseInt(tokens[1] * 1))) {
				continue;
			} else if (fromchqNum == parseInt(tokens[0] * 1)
					&& tochqNum == parseInt(tokens[1] * 1)
					&& deptId == tokens[2] * 1 && serialNo == tokens[3]) {

				count = count + 1;
				if (count > 1) {
					document.getElementById("lblErrorGrid").innerHTML = "Cheque Range is already assigned for department :"
							+ deptName + " & SerialNo :" + serialNo;
					return false;
				}
				continue;
			} else if (fromchqNum == parseInt(tokens[0] * 1)
					&& tochqNum == parseInt(tokens[1] * 1)) {
				continue;
			} else if (serialNo != tokens[3]) {
				continue;
			} else {
				document.getElementById("lblErrorGrid").innerHTML = "Invalid cheque range";
				return false;
			}

		}

	}
	document.getElementById("save").disabled = false;

}
if (!Array.indexOf) {
	Array.prototype.indexOf = function(obj) {
		for (var i = 0; i < this.length; i++) {
			if (this[i] == obj) {
				return i;
			}
		}
		return -1;
	}
}

String.prototype.trim = function() {
	return this.replace(/^\s*/, "").replace(/\s*$/, "");
}