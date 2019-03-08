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
function validate() {
	if (!validateForm_directBankPayment()) {
		undoLoadingMask();
		return false;
	}
	enableAll();
	if (!updateAndCheckAmount()) {
		undoLoadingMask();
		return false;
	}
	if(!balanceCheck()){
		bootbox.alert("Insuffiecient Bank Balance. Do you want to process", function() { 
			return true;
		});
	}
	return true;
}
function updateAndCheckAmount() {
	var debit = document.getElementById('totaldbamount').value;
	var credit = document.getElementById('totalcramount').value;
	var dbcr = debit - credit;
	var amt = document.getElementById('amount').value;
	if (isNaN(amt)) {
		bootbox.alert(amountshouldbenumeric);
		return false;
	}
	// document.getElementById('amount').value=debit-credit;
	if (amt != dbcr) {
		bootbox.alert(totalsnotmatchingamount);
		return false;
	}else {
		return true;
	}

}

function populateNarration(accnumObj) {

	var accnum = accnumObj.options[accnumObj.selectedIndex].value;
	var bankbranchObj = document.getElementById('bankId');
	var bankbranchId = bankbranchObj.options[bankbranchObj.selectedIndex].value;
	var index = bankbranchId.indexOf("-");
	var branchId = bankbranchId.substring(index + 1, bankbranchId.length);
    var csrfToken = document.getElementById('csrfTokenValue').value;
	var url = '../voucher/common-loadAccNumNarration.action?accnum=' + accnum+'&_csrf='+csrfToken
			+ '&branchId=' + branchId;
	YAHOO.util.Connect.asyncRequest('POST', url, postTypeFrom, null);
}

function loadDocumentNoAndDate(billVhId) {
	var url = '../voucher/common-ajaxLoadDocumentNoAndDate.action?billVhId='
			+ billVhId;
	YAHOO.util.Connect.asyncRequest('POST', url, documentNoAndDateTypeFrom,
			null);
}

/*
 * function loadBalance(obj) { if(dom.get('voucherdate').value=='') {
 * bootbox.alert("Please Select the Voucher Date!!"); obj.options.value=-1;
 * return; } if(obj.options[obj.selectedIndex].value==-1)
 * dom.get('balance').value=''; else
 * populatebalance({bankaccount:obj.options[obj.selectedIndex].value,voucherDate:dom.get('voucherdate').value+'&date='+new
 * Date()}); }
 */

function populateAvailableBalance(accnumObj) {
	if (document.getElementById('voucherDate').value == '') {
		bootbox.alert("Please Select the Voucher Date!!");
		accnumObj.options.value = -1;
		return;
	}
	if (accnumObj.options[accnumObj.selectedIndex].value == -1)
		document.getElementById('availableBalance').value = '';
	else
		populateavailableBalance({
			bankaccount : accnumObj.options[accnumObj.selectedIndex].value,
			voucherDate : document.getElementById('voucherDate').value
					+ '&date=' + new Date()
		});

}

var callback = {
	success : function(o) {
		console.log("success");
		document.getElementById('availableBalance').value = o.responseText;
	},
	failure : function(o) {
		console.log("failed");
	}
}

var postTypeFrom = {
	success : function(o) {
		document.getElementById('accnumnar').value = o.responseText;
	},
	failure : function(o) {
		bootbox.alert('failure');
	}
}

var documentNoAndDateTypeFrom = {
	success : function(o) {
		if (o.responseText != "") {
			var docs = o.responseText.split("$");
			document.getElementById('commonBean.documentNumber').value = docs[0];
			document.getElementById('documentDate').value = docs[1];
		}
	},
	failure : function(o) {
		bootbox.alert('Cannot fetch Document Number and Date');
	}
}

function loadBank(fund) {
	var vTypeOfAccount = document.getElementById('typeOfAccount').value;
	populatebankId({
		fundId : fund.options[fund.selectedIndex].value,
		typeOfAccount : vTypeOfAccount
	})
}

function enableAll() {
	for (var i = 0; i < document.forms[0].length; i++)
		document.forms[0].elements[i].disabled = false;
}

function disableControls(frmIndex, isDisable) {
	for (var i = 0; i < document.forms[frmIndex].length; i++)
		document.forms[frmIndex].elements[i].disabled = isDisable;
}

function balanceCheck() {

	if (document.getElementById('balanceAvl')) {
		console.log("ins did");
		if (parseFloat(document.getElementById('amount').value) > parseFloat(document
				.getElementById('availableBalance').value)) {
			return false;
		}
	}
	return true;
}
function updateVoucherNumber() {
	document.getElementById('voucherNumber').value = document
			.getElementById('voucherNumberRest').value;
}

function disableForNonBillPayment() {
	var frmIndex = 0;
	for (var i = 0; i < document.forms[frmIndex].length; i++)
		document.forms[frmIndex].elements[i].disabled = true;
	document.getElementById("closeButton").disabled = false;
	if (document.getElementById("closeButtonNew"))
		document.getElementById("closeButtonNew").disabled = false;
	if (document.getElementById("comments"))
		document.getElementById("comments").disabled = false;
	document.getElementById("bankId").disabled = false;
	document.getElementById("accountNumber").disabled = false;
	document.getElementById("modeOfPaymentcheque").disabled = false;
	document.getElementById('commonBean.documentNumber').disabled = false;
	document.getElementById("documentDate").disabled = false;
	if (document.getElementById("voucherNumber")) {
		document.getElementById("voucherNumber").disabled = false;
		document.getElementById("voucherNumber").value = "";
	}
	document.getElementById('paidTo').disabled = false;
	document.getElementById("description").disabled = false;
	if (document.getElementById('wfBtn0'))
		document.getElementById('wfBtn0').disabled = false;
	if (document.getElementById('wfBtn1'))
		document.getElementById('wfBtn1').disabled = false;
	if (document.getElementById('wfBtn2'))
		document.getElementById('wfBtn2').disabled = false;
	if (document.getElementById('designationId'))
		document.getElementById('designationId').disabled = false;
	if (document.getElementById('departmentid'))
		document.getElementById('departmentid').disabled = false;
	if (document.getElementById('approverUserId'))
		document.getElementById('approverUserId').disabled = false;
}

function openViewVouchers() {
	var url = '../voucher/voucherSearch-beforesearch.action?showMode=sourceLink';
	var val = window.showModalDialog(url, "SearchBillVouchers",
			"dialogwidth: 800; dialogheight: 600;");
	if (val != undefined && val != null && val != ""
			&& val.split("$").length > 0) {
		var data = val.split("$");
		document.getElementById('commonBean.linkReferenceNumber').value = data[0];
		document.getElementById('commonBean.documentId').value = data[2];
		loadDocumentNoAndDate(data[2]);

	}
}
