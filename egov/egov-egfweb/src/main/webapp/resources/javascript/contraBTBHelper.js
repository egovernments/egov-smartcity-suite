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
function loadBank(fund) {
	// bootbox.alert(fund.options[fund.selectedIndex].value);
	//loadFromDepartment();
	populatefromBankId({
		fundId : fund.options[fund.selectedIndex].value,
		typeOfAccount : "RECEIPTS_PAYMENTS,RECEIPTS"
	})
	checkInterFund();
}

function loadFromDepartment() {
	var selectedFund = jQuery("#fundId option:selected").text();
	if (selectedFund == '01-Municipal Fund'
			|| selectedFund == '02-Capital Fund') {
		document.getElementById("vouchermis.departmentid").disabled = "";
		document.getElementById("vouchermis.departmentid").value = 1;
	} else if (selectedFund == '03-Elementary Education Fund') {
		document.getElementById("vouchermis.departmentid").value = 31;
		document.getElementById("vouchermis.departmentid").disabled = "true";
	} else {
		document.getElementById("vouchermis.departmentid").disabled = "";
		document.getElementById("vouchermis.departmentid").value = "";
	}
}
function loadToDepartment() {
	var selectedFund = jQuery("#toFundId option:selected").text();
	if (selectedFund == '01-Municipal Fund'
			|| selectedFund == '02-Capital Fund') {
		document.getElementById("contraBean.toDepartment").disabled = "";
		document.getElementById("contraBean.toDepartment").value = 1;
	} else if (selectedFund == '03-Elementary Education Fund') {
		document.getElementById("contraBean.toDepartment").value = 31;
		document.getElementById("contraBean.toDepartment").disabled = "true";
	} else {
		document.getElementById("contraBean.toDepartment").value = "";
		document.getElementById("contraBean.toDepartment").disabled = "";
	}
}
function loadToBank(fund) {
	//loadToDepartment();
	populatetoBankId({
		fundId : fund.options[fund.selectedIndex].value,
		typeOfAccount : "RECEIPTS_PAYMENTS,PAYMENTS"
	})
}

function loadFromAccNum(branch) {

	var fundObj = document.getElementById('fundId');
	var bankbranchId = branch.options[branch.selectedIndex].value;
	var index = bankbranchId.indexOf("-");
	var bankId = bankbranchId.substring(0, index);
	var brId = bankbranchId.substring(index + 1, bankbranchId.length);
	populatefromAccountNumber({
		fundId : fundObj.options[fundObj.selectedIndex].value,
		branchId : brId,
		typeOfAccount : "RECEIPTS_PAYMENTS,RECEIPTS"
	})
}
function loadToAccNum(branch) {
	var fundObj = document.getElementById('toFundId');
	var bankbranchId = branch.options[branch.selectedIndex].value;
	var index = bankbranchId.indexOf("-");
	var bankId = bankbranchId.substring(0, index);
	var brId = bankbranchId.substring(index + 1, bankbranchId.length);
	populatetoAccountNumber({
		fundId : fundObj.options[fundObj.selectedIndex].value,
		branchId : brId,
		typeOfAccount : "RECEIPTS_PAYMENTS,PAYMENTS"
	})
}

function populatefromNarration(accnumObj) {

	var accnum = accnumObj.options[accnumObj.selectedIndex].value;
	var bankbranchObj = document.getElementById('fromBankId');
	var bankbranchId = bankbranchObj.options[bankbranchObj.selectedIndex].value;
	var index = bankbranchId.indexOf("-");
	var branchId = bankbranchId.substring(index + 1, bankbranchId.length);
	var url = '../voucher/common-loadAccNumNarration.action?accnum=' + accnum
			+ '&branchId=' + branchId;
	YAHOO.util.Connect.asyncRequest('POST', url, postTypeFrom, null);
}
function populatetoNarration(accnumObj) {

	var accnum = accnumObj.options[accnumObj.selectedIndex].value;
	var bankbranchObj = document.getElementById('toBankId');
	var bankbranchId = bankbranchObj.options[bankbranchObj.selectedIndex].value;
	var index = bankbranchId.indexOf("-");
	var branchId = bankbranchId.substring(index + 1, bankbranchId.length);
	var url = '../voucher/common-loadAccNumNarration.action?accnum=' + accnum
			+ '&branchId=' + branchId;
	YAHOO.util.Connect.asyncRequest('POST', url, postTypeTo, null);

}

var postTypeFrom = {
	success : function(o) {
		var fromNarration = o.responseText;
		// var index=fromNarration.indexOf("-");
		document.getElementById('fromAccnumnar').value = fromNarration;
	},
	failure : function(o) {
		bootbox.alert('failure');
	}
}

var postTypeTo = {
	success : function(o) {
		var toNarration = o.responseText;
		// var index=fromNarration.indexOf("-");
		document.getElementById('toAccnumnar').value = toNarration;
	},
	failure : function(o) {
		bootbox.alert('failure');
	}
}

function nextChqNo() {
	var obj = document.getElementById("fromAccountNumber");
	var bankBr = document.getElementById("fromBankId");
	if (bankBr.selectedIndex == -1) {
		bootbox.alert("Select Bank Branch and Account No!!");
		return;
	}

	if (obj.selectedIndex == -1) {
		bootbox.alert("Select Account No!!");
		return;
	}
	var accNo = obj.options[obj.selectedIndex].text;
	var accNoId = obj.options[obj.selectedIndex].value;
	var sRtn = showModalDialog("../HTML/SearchNextChqNo.html?accntNo=" + accNo
			+ "&accntNoId=" + accNoId, "",
			"dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
	if (sRtn != undefined)
		document.getElementById("chequeNum").value = sRtn;
}
function checkInterFund() {
	var fromFund = document.getElementById('fundId').value;
	var toFund = document.getElementById('toFundId').value;
	var splitStr = new Array();
	var temp;
	// bootbox.alert("hi"+toFund);
	// bootbox.alert("from fund"+fromFund);
	if (fromFund != "" && toFund != "") {
		if (fromFund != toFund) {
			for (i = 0, j = 0; i < fund_map.length; i++) {
				splitStr[j] = fund_map[i].split("_");
				if (splitStr[j].length == 2) {

					setDefaultValues(splitStr[j][0], splitStr[j][1]);
				}
				j++;
			}
			document.getElementById('interFundRow1').style.visibility = "visible";
			document.getElementById('interFundRow2').style.visibility = "visible";
			document.getElementById('interFundRow3').style.visibility = "visible";
		} else {
			// bootbox.alert("from fund="+fromFund+"toFund="+toFund);
			document.getElementById('interFundRow1').style.visibility = "hidden";
			document.getElementById('interFundRow2').style.visibility = "hidden";
			document.getElementById('interFundRow3').style.visibility = "hidden";
		}

	} else if (fromFund == "" || toFund == "") {
		// bootbox.alert("min else if");
		document.getElementById('interFundRow1').style.visibility = "hidden";
		document.getElementById('interFundRow2').style.visibility = "hidden";
		document.getElementById('interFundRow3').style.visibility = "hidden";
	}
}

function loadFromBalance(obj) {
	if (document.getElementById('voucherDate').value == '') {
		bootbox.alert("Please Select the Voucher Date!!");
		obj.options.value = -1;
		return;
	}
	if (obj.options[obj.selectedIndex].value == -1)
		document.getElementById('fromBankBalance').value = '';
	else {

		populatefromBankBalance({
			bankaccount : obj.options[obj.selectedIndex].value,
			voucherDate : document.getElementById('voucherDate').value
					+ '&date=' + new Date()
		});

	}
}

function loadToBalance(obj) {
	if (document.getElementById('voucherDate').value == '') {
		bootbox.alert("Please Select the Voucher Date!!");
		obj.options.value = -1;
		return;
	}
	if (obj.options[obj.selectedIndex].value == -1)
		document.getElementById('toBankBalance').value = '';
	else
		populatetoBankBalance({
			bankaccount : obj.options[obj.selectedIndex].value,
			voucherDate : document.getElementById('voucherDate').value
					+ '&date=' + new Date()
		});
}
function disableControls(frmIndex, isDisable) {
	for (var i = 0; i < document.forms[frmIndex].length; i++)
		document.forms[frmIndex].elements[i].disabled = isDisable;
}

function enableAll() {
	for (var i = 0; i < document.forms[0].length; i++)
		document.forms[0].elements[i].disabled = false;
}

function validate() {
	var insuffientAlert = 'There is no sufficient bank balance. ';
	var continueAlert = 'Do you want to continue ? ';
	var fundFlowNotGeneratedAlert = '';
	var voucher_date = document.getElementById('voucherDate').value;
	var fundFlowDateChkStr = document
			.getElementById('startDateForBalanceCheckStr').value;
	var vh_split = voucher_date.split('/');
	var sp_date = fundFlowDateChkStr.split('-');
	var month = getMonthNo(sp_date[1]);
	var app_config_Date_value = new Date(sp_date[2], month - 1, sp_date[0]);
	var voucherDateChk = new Date(vh_split[2], vh_split[1] - 1, vh_split[0]);
	// bootbox.alert(voucher_date);
	// bootbox.alert("Check app date"+app_config_Date_value);
	// bootbox.alert("Check app date"+voucherDateChk);

	if (parseFloat(document.getElementById('fromBankBalance').value) == -1
			|| parseFloat(document.getElementById('toBankBalance').value) == -1) {
		fundFlowNotGeneratedAlert = "FundFlowReport is not generated for the for the day.Please create fund flow first";
	}
	if (parseFloat(document.getElementById('amount').value) > parseFloat(document
			.getElementById('fromBankBalance').value)) {
		if (voucherDateChk >= app_config_Date_value) {
			if (document.getElementById('bankBalanceMandatory').value == 'true') {

				if (fundFlowNotGeneratedAlert != '') {
					bootbox.alert(fundFlowNotGeneratedAlert);
					return false;
				} else {
					if (document.getElementById('bankBalanceMandatory').value)
						bootbox.alert(insuffientAlert);
					return false;
				}
			} else {
				if (confirm(fundFlowNotGeneratedAlert + insuffientAlert
						+ continueAlert)) {
					document.getElementById("fromFundId").disabled = false;
					return true;
				} else {
					return false;
				}
			}
		}
	}
	var Amount = document.getElementById("amount").value;
	if (Amount <= 0) {
		bootbox.alert("Amount should be greater than zero ");
		return false;
	}
	document.cbtbform.action='/EGF/contra/contraBTB-create.action';
	document.cbtbform.submit();
	return true;
}

function validateReverse() {
	return true;
}
function setDefaultValues(fnd, objinterfnd) {
	var srcFund = document.getElementById('fundId').value;
	var desFund = document.getElementById('toFundId').value;
	if (srcFund == fnd) {
		if (objinterfnd == '') {
			document.getElementById('sourceGlcode').value = "-1";

		} else
			document.getElementById('sourceGlcode').value = objinterfnd;
	} else if (desFund == fnd) {
		if (objinterfnd == '') {
			document.getElementById('destinationGlcode').value = "-1";
		} else
			document.getElementById('destinationGlcode').value = objinterfnd;
	}
}
function getMonthNo(month) {
	switch (month) {
	case "Jan":
		month = '01';
		break;
	case "Feb":
		month = '02';
		break;
	case "Mar":
		month = '03';
		break;
	case "Apr":
		month = '04';
		break;
	case "May":
		month = '05';
		break;
	case "Jun":
		month = '06';
		break;
	case "Jul":
		month = '07';
		break;
	case "Aug":
		month = '08';
		break;
	case "Sep":
		month = '09';
		break;
	case "Oct":
		month = '10';
		break;
	case "Nov":
		month = '11';
		break;
	case "Dec":
		month = '12';
		break;
	}
	return month;
}
