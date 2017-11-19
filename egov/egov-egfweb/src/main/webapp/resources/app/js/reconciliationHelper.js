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

function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}

function callAjaxSearch() {
	doLoadingMask();
	var fd = jQuery('#mrform').serialize();
	jQuery.ajax({
		url : "/EGF/brs/manualReconciliation-ajaxSearch.action",
		type : "POST",
		data : fd,
		// dataType: "text",
		success : function(response) {
			// console.log("success"+response );
			jQuery('#resultDiv').html(response);
			jQuery('#reconcileDiv').show();
			jQuery(".datepicker").datepicker({
				format : "dd/mm/yyyy",
				autoclose : true
			});

			undoLoadingMask();
		},
		error : function(response) {
			console.log("failed");

			bootbox.alert("Failed to search Details");
			undoLoadingMask();
		}
	});

}

function validateReconcile() {

	var toDate = document.getElementById("toDate").value;
	var len = jQuery('#resultTable tr').length;
	var row = "line ";
	var rows;
	var numOfrows = '';
	var value = false;
	for (i = 0; i <= len - 2; i++) {
		if (document.getElementById('reconDates' + i).value > toDate) {
			var a = i + 1;
			rows = row.concat(a);
			numOfrows = numOfrows + rows + ',';
			value = true;
		}

	}

	if (value == true) {

		bootbox
				.alert("Reconciliation Date should be less than or equal to Bank statement To Date : "
						+ numOfrows.replace(/\,$/, ''));
		return false;
	}

	if (!validateReconDate()) {
		bootbox.alert("Add atleast one Reconciliation Date");
		return false;
	}

	doLoadingMask();
	var fd = jQuery('#mrform').serialize();
	jQuery.ajax({
		url : "/EGF/brs/manualReconciliation-update.action",
		type : "POST",
		data : fd,
		// dataType: "text",
		success : function(response) {
			// console.log("success"+response );
			undoLoadingMask();
			jQuery('#resultDiv').html(response);
			// bootbox.alert("Passed to Reconcile Details");

		},
		error : function(response) {
			console.log("failed");
			undoLoadingMask();
			bootbox.alert("Failed to Reconcile Details");

		}
	});

}

function showBalance() {
	// alert("returned "+validateReconDate())
	if (!validate()) {
		return false;
	}

	doLoadingMask();
	var fd = jQuery('#mrform').serialize();
	jQuery.ajax({
		url : "/EGF/brs/manualReconciliation-ajaxBalance.action",
		type : "POST",
		data : fd,
		// dataType: "text",
		success : function(response) {
			// console.log("success"+response );
			undoLoadingMask();
			jQuery('#balanceDiv').html(response);
			// bootbox.alert("Passed to Reconcile Details");

		},
		error : function(response) {
			console.log("failed");
			undoLoadingMask();
			bootbox.alert("Failed to Show balance Details");

		}
	});

}

function validateReconDate() {
	// alert("Validating Reconciliation Date"+jQuery('#resultTable tr').length);
	var len = jQuery('#resultTable tr').length;

	for (i = 0; i <= len - 2; i++) {
		// alert("'"+document.getElementById('reconDates'+i).value+"'");

		// alert("Compare----"+
		// document.getElementById('reconDates'+i).value!='');

		if (document.getElementById('reconDates' + i).value != '')
			return true;
	}

	return false;
}

function validate() {
	if (document.getElementById("bankId").value == "") {
		bootbox.alert("Select Bank");
		return false;
	}
	if (document.getElementById("branchId").value == "") {
		bootbox.alert("Select Branch");
		return false;
	}
	if (document.getElementById("accountId").value == "") {
		bootbox.alert("Select Account");
		return false;
	}
	var toDateStr = document.getElementById("toDate").value;
	var fromDatestr = document.getElementById("fromDate").value;
	var reconDateStr = document.getElementById("reconciliationDate").value;
	var toDateParts = toDateStr.split("/");
	var fromDateParts = fromDatestr.split("/");
	if (reconDateStr == "") {
		bootbox.alert("Select Reconciliation Date");
		return false;
	}
	if (fromDatestr == "") {
		bootbox.alert("Select Bank Statement From Date");
		return false;
	}
	if (toDateStr == "") {
		bootbox.alert("Select Bank Statement To Date");
		return false;
	}

	var fromDate = new Date(fromDateParts[1] + "/" + fromDateParts[0] + "/"
			+ fromDateParts[2]);
	var toDates = new Date(toDateParts[1] + "/" + toDateParts[0] + "/"
			+ toDateParts[2]);
	if (fromDate > toDates) {
		bootbox
				.alert("Bank Statement From Date must be less than or equal to Bank Statement To Date");
		return false;
	}
	if (toDateStr != null && reconDateStr != null) {

		if (toDateParts.length != 3) {
			bootbox.alert("Enter date is 'DD/MM/YYYY' format only");
			return false;
		}
		var toDate = new Date(toDateParts[1] + "/" + toDateParts[0] + "/"
				+ toDateParts[2]);
		var reconDateParts = reconDateStr.split("/");

		if (reconDateParts.length != 3) {
			bootbox.alert("Enter date is 'DD/MM/YYYY' format only");
			return false;
		}
		var reconDate = new Date(reconDateParts[1] + "/" + reconDateParts[0]
				+ "/" + reconDateParts[2]);
		// bootbox.alert(reconDate.toString('MM-dd-yyyy'));
		if (reconDate < toDate) {
			bootbox
					.alert("Reconciliation Date must be greater than or equal to Bank Statement To Date");
			return false;
		}
	}
	return true;
}
