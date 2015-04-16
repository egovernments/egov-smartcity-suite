
function validatePwdReminder() {
	if (document.getElementById('pwd').value != document.getElementById('retypePwd').value) {
		alert("Password and Repeat Password values should be same");
		document.getElementById('pwd').value = "";
		document.getElementById('retypePwd').value = "";
		document.getElementById('pwd').focus();
		return false;
	} else {
		return true;
	}
}
function validate(obj) {
	var rowObj = getRow(obj);
	var table = document.getElementById("reason_table");
	document.getElementById("roleId").readOnly = false;
	var len = table.rows.length;
	if (len > 0) {
		for (var i = 0; i < len; i++) {
			if (getControlInBranch(table.rows[rowObj.rowIndex], "modifyRole").checked) {
				getControlInBranch(table.rows[rowObj.rowIndex], "fromDate1").disabled = false;
				getControlInBranch(table.rows[rowObj.rowIndex], "toDate1").disabled = false;
			} else {
				getControlInBranch(table.rows[rowObj.rowIndex], "fromDate1").disabled = true;
				getControlInBranch(table.rows[rowObj.rowIndex], "toDate1").disabled = true;
			}
		}
	} else {
		if (document.getElementById('modifyRole').checked) {
			document.getElementById('fromDate1').disabled = false;
			document.getElementById('toDate1').disabled = false;
		} else {
			document.getElementById('fromDate1').disabled = true;
			document.getElementById('toDate1').disabled = true;
		}
	}
}
function callAction() {
	alert("You cant change the User Name");
	document.getElementById('userName').value = name;
	return false;
}
function validateisActive(obj) {
	if (document.getElementById("isActive").checked) {
		document.getElementById('isActive').value = 1;
		return false;
	} else {
		document.getElementById('isActive').value = 0;
		return false;
	}
}
//to set the hidden role variable
function setSelRoleID(obj) {
	var tbl = document.getElementById("reason_table");
	var rowNumber = getRow(obj).rowIndex;
	var roleid = getControlInBranch(tbl.rows[rowNumber], "roleId").value;
	if (rowNumber == 0) {
		document.getElementById('selRoleID').value = roleid;
	} else {
		document.getElementsByName('selRoleID')[rowNumber].value = roleid;
	}
}
function deleteRole(obj) {
	var tbl = document.getElementById("reason_table");
	var rowNumber = getRow(obj).rowIndex;
	var rowo = tbl.rows.length;
	if (rowo <= 1) {
		alert("This row can not be deleted");
		return false;
	} else {
		userRoleId = getControlInBranch(tbl.rows[rowNumber], "userRoleId").value;
		populateDeleteSet("delUserRoles", userRoleId);
		tbl.deleteRow(rowNumber);
		return true;
	}
}
function populateDeleteSet(setName, delId) {
	var http = initiateRequest();
	var url = "/egi/commons/updateDelSets.jsp?type=" + setName + "&id=" + delId;
	http.open("GET", url, true);
	http.onreadystatechange = function () {
		if (http.readyState == 4) {
			if (http.status == 200) {
				var statusString = http.responseText.split("^");
			}
		}
	};
	http.send(null);
}
function checkData() {
	document.getElementById('fromDate1').disabled = false;
	document.getElementById('toDate1').disabled = false;
	document.getElementById('roleId').readOnly = false;
	var row = document.getElementById("reason_table").rows.length;
	var len = document.getElementsByName('fromDate1').length;
	if (len > 0) {
		for (var i = 0; i < len; i++) {
			for (var j = i + 1; j < len; j++) {
				if (i != j) {
					if (document.getElementsByName('roleId')[i].value == document.getElementsByName('roleId')[j].value) {
						alert("Role is already selected");
						document.getElementsByName('roleId')[j].focus();
						return false;
					}
				}
			}
		}
	}
	if (document.getElementById('fromDate').value == "") {
		alert("Please enter a From Date");
		document.getElementById('fromDate').focus();
		return false;
	}
	if (document.getElementById('fromDate').value != "" && document.getElementById('toDate').value != "") {
		rTF = checkFdateTdate(document.getElementById('fromDate').value, document.getElementById('toDate').value);
		if (rTF == false) {
			alert("From Date should be less than or equal to To Date");
			document.getElementById('fromDate').value = "";
			return false;
		}
	}
	if (row == 1) {
		if (document.getElementById('roleId').value != "0") {
			if (document.getElementById('fromDate1').value == "") {
				alert("Please enter Role From Date");
				document.getElementById('fromDate1').focus();
				return false;
			}
		}
		if (document.getElementById('fromDate1').value != "" && document.getElementById('toDate1').value != "") {
			rTF = checkFdateTdate(document.getElementById('fromDate1').value, document.getElementById('toDate1').value);
			if (rTF == false) {
				alert("Role From date should be less than or equal to Role To Date");
				document.getElementById('fromDate1').value = "";
				return false;
			}
		}
		if (document.getElementById("isActiveValue").checked) {
			document.getElementById('isActive').value = 1;
		} else {
			document.getElementById('isActive').value = 0;
		}
		if (document.getElementById('modifyRole').checked) {
			document.getElementById('selCheck').value = "yes";
		} else {
			document.getElementById('selCheck').value = "no";
		}
	} else {
		if (row > 1) {
			if (document.getElementsByName('roleId')[row - 1].value == "0") {
				alert("Please select Role");
				document.getElementsByName('roleId')[row - 1].focus();
				return false;
			}
			if (document.getElementsByName('fromDate1')[row - 1].value == "") {
				alert("Please enter Role From Date");
				document.getElementsByName('fromDate1')[row - 1].focus();
				return false;
			}
			if (document.getElementsByName('fromDate1')[row - 1].value != "" && document.getElementsByName('toDate1')[row - 1].value != "") {
				rTF = checkFdateTdate(document.getElementsByName('fromDate1')[row - 1].value, document.getElementsByName('toDate1')[row - 1].value);
				if (rTF == false) {
					alert("Role From date should be less than or equal to Role To Date");
					document.getElementsByName('fromDate1')[row - 1].value = "";
					return false;
				}
			}
			var table = document.getElementById("reason_table");
			for (var i = 0; i < table.rows.length; i++) {
				if (getControlInBranch(table.rows[i], "modifyRole").checked) {
					getControlInBranch(table.rows[i], "selCheck").value = "yes";
				}
				if (!getControlInBranch(table.rows[i], "modifyRole").checked) {
					getControlInBranch(table.rows[i], "selCheck").value = "no";
				}
			}
		}
	}
	return true;
}
function validation() {
	document.getElementById('fromDate1').disabled = false;
	document.getElementById('toDate1').disabled = false;
	document.getElementById('roleId').readOnly = false;
	var row = document.getElementById("reason_table").rows.length;
	var len = document.getElementsByName('fromDate1').length;
	if (len > 0) {
		for (var i = 0; i < len; i++) {
			document.getElementsByName('fromDate1')[i].disabled = false;
			document.getElementsByName('toDate1')[i].disabled = false;
			for (var j = i + 1; j < len; j++) {
				if (i != j) {
					if (document.getElementsByName('roleId')[i].value == document.getElementsByName('roleId')[j].value) {
						alert("Role already selected");
						document.getElementsByName('roleId')[j].focus();
						return false;
					}
				}
			}
		}
	}
	if (document.userForm.fromDate.value == "") {
		alert("Please enter From Date");
		document.getElementById('fromDate').focus();
		return false;
	}
	if (document.getElementById('fromDate').value != "" && document.getElementById('toDate').value != "") {
		rTF = checkFdateTdate(document.getElementById('fromDate').value, document.getElementById('toDate').value);
		if (rTF == false) {
			alert("From Date should be less than or equal to To Date");
			document.getElementById('fromDate').value = "";
			return false;
		}
	}
	if (row == 1) {
		if (document.getElementById('roleId').value != "0") {
			if (document.getElementById('fromDate1').value == "") {
				alert("Please enter Role From Date");
				document.getElementById('fromDate1').focus();
				return false;
			}
		}
		if (document.getElementById('fromDate1').value != "" && document.getElementById('toDate1').value != "") {
			rTF = checkFdateTdate(document.getElementById('fromDate1').value, document.getElementById('toDate1').value);
			if (rTF == false) {
				alert("Role From date should be less than or equal to Role To Date");
				document.getElementById('fromDate1').value = "";
				return false;
			}
		}
		if (document.getElementById('modifyRole').checked) {
			document.getElementById('selCheck').value = "yes";
		} else {
			document.getElementById('selCheck').value = "no";
		}
	} else {
		if (row > 1) {
			if (document.getElementsByName('roleId')[row - 1].value == "0") {
				alert("Please select a Role");
				document.getElementsByName('roleId')[row - 1].focus();
				return false;
			}
			if (document.getElementsByName('fromDate1')[row - 1].value == "") {
				alert("Please enter Role From Date");
				document.getElementsByName('fromDate1')[row - 1].focus();
				return false;
			}
			if (document.getElementsByName('fromDate1')[row - 1].value != "" && document.getElementsByName('toDate1')[row - 1].value != "") {
				rTF = checkFdateTdate(document.getElementsByName('fromDate1')[row - 1].value, document.getElementsByName('toDate1')[row - 1].value);
				if (rTF == false) {
					alert("Role From Date should be less than or equal to Role To Date");
					document.getElementsByName('fromDate1')[row - 1].value = "";
					return false;
				}
			}
			var table = document.getElementById("reason_table");
			for (var i = 0; i < table.rows.length; i++) {
				if (getControlInBranch(table.rows[i], "modifyRole").checked) {
					getControlInBranch(table.rows[i], "selCheck").value = "yes";
				}
				if (!getControlInBranch(table.rows[i], "modifyRole").checked) {
					getControlInBranch(table.rows[i], "selCheck").value = "no";
				}
			}
		}
	}
	if (document.getElementById("isActiveValue").checked) {
		document.getElementById("isActive").value = 1;
	} else {
		document.getElementById("isActive").value = 0;
	}
	return true;
}
function addRole() {
	var correctData = checkData();
	if (document.getElementById("roleId").value == "0") {
		alert("Please select Role");
		return false;
	}
	if (correctData) {
		var tbl = document.getElementById("reason_table");
		var tbody = tbl.tBodies[0];
		var lastRow = tbl.rows.length;
		var rowObj = document.getElementById("reasonrow").cloneNode(true);
		tbody.appendChild(rowObj);
		document.getElementsByName('roleId')[lastRow].value = "0";
		document.getElementsByName('roleId')[lastRow].disabled = false;
		document.getElementsByName('userRoleId')[lastRow].value = "0";
		document.getElementsByName('fromDate1')[lastRow].value = "";
		document.getElementsByName('fromDate1')[lastRow].disabled = false;
		document.getElementsByName('toDate1')[lastRow].disabled = false;
		document.getElementsByName('toDate1')[lastRow].value = "";
		document.getElementsByName('modifyRole')[lastRow].checked = false;
		
	}
}
function trimText1(obj, value) {
	value = value;
	if (value != undefined) {
		while (value.charAt(value.length - 1) == " ") {
			value = value.substring(0, value.length - 1);
		}
		while (value.substring(0, 1) == " ") {
			value = value.substring(1, value.length);
		}
		obj.value = value;
	}
	return value;
}

