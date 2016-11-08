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

$(document)
		.ready(
				function() {

					$("#add-row").click(
							function(event) {
								var rowCount = $('#result tr').length;
								if (!checkforNonEmptyPrevRow())
									return false;
								var prevUOMFromVal = getPrevUOMFromData();
								var content = $('#resultrow0').html();
								resultContent = content.replace(/0/g,
										rowCount - 1);
								$(resultContent).find("input").val("");
								$('#result > tbody:last').append(
										"<tr>" + resultContent + "</tr>");
								$('#result tr:last').find("input").val("");
								$('.fromRange').attr("readonly", true);
								intiUOMFromData(prevUOMFromVal);
								patternvalidation();
							});

					$("#search")
							.click(
									function(event) {
										$('#resultdiv').empty();
										var valid = $('#penaltyform')
												.validate().form();
										if (!valid) {
											bootbox
													.alert("Please fill mandatory fields");
											return false;
										}
										var param = "licenseAppType=";
										param = param
												+ $('#licenseAppType').val();
										$.ajax({
													url : "/tl/penaltyRates/search?"
															+ param,
													type : "GET",
													// dataType: "json",
													success : function(response) {
														$('#resultdiv').html(
																response);
														$("#penalty").hide();
														if (jQuery('#result tbody tr').length == 1) {
															jQuery(
																	'input[name="penaltyRatesList[0].fromRange"]')
																	.attr(
																			"readonly",
																			false);
														}
													},
													error : function(response) {
														console.log("failed");
													}
												});

									});
				});

function checkforNonEmptyPrevRow() {
	var tbl = document.getElementById("result");
	var lastRow = (tbl.rows.length) - 1;
	var fromRange = getControlInBranch(tbl.rows[lastRow], 'fromRange').value;
	var toRange = getControlInBranch(tbl.rows[lastRow], 'toRange').value;
	var rate = getControlInBranch(tbl.rows[lastRow], 'rate').value;
	if (fromRange == '' || toRange == '' || rate == '') {
		bootbox.alert("Enter all values for existing rows before adding.");
		return false;
	}
	return true;
}
function getPrevUOMFromData() {
	var tbl = document.getElementById("result");
	var lastRow = (tbl.rows.length) - 1;
	return getControlInBranch(tbl.rows[lastRow], 'toRange').value;
}

function intiUOMFromData(obj) {
	var tbl = document.getElementById("result");
	var lastRow = (tbl.rows.length) - 1;
	getControlInBranch(tbl.rows[lastRow], 'fromRange').value = obj;
}

function checkValue(obj) {
	var rowobj = getRow(obj);
	var tbl = document.getElementById('result');
	var toRange = getControlInBranch(tbl.rows[rowobj.rowIndex], 'toRange').value;
	var fromRange = getControlInBranch(tbl.rows[rowobj.rowIndex], 'fromRange').value;
	if (fromRange != '' && toRange != '' && (eval(fromRange) >= eval(toRange))) {
		bootbox.alert("\"To Range\" should be greater than \"From Range\".");
		getControlInBranch(tbl.rows[rowobj.rowIndex], 'toRange').value = "";
		return false;
	}
	$(obj).closest('tr').next('tr').find('td:eq(0) input').val(toRange);
}

function deleteThisRow(obj) {
	var tbl = document.getElementById("result");
	var lastRow = (tbl.rows.length) - 1;
	var curRow = getRow(obj).rowIndex;
	var counts = lastRow - 1;
	if (curRow == 1) {
		bootbox.alert('Cannot delete first row');
		return false;
	} else if (curRow != lastRow) {
		bootbox.alert('Cannot delete in between. Delete from last.');
		return false;
	} else {
		if (getControlInBranch(tbl.rows[lastRow], 'penaltyId').value == '') {
			tbl.deleteRow(curRow);
			return true;
		} else if (getControlInBranch(tbl.rows[lastRow], 'penaltyId').value != '') {
			bootbox.confirm("This will delete the row permanently. Press OK to Continue. ",
				function(r) {
					if (r) {
						$.ajax({
							url : "/tl/domain/commonAjax-deleteRow.action?penaltyRateId="
									+ getControlInBranch(
											tbl.rows[lastRow],
											'penaltyId').value
									+ "",
							type : "GET",
							dataType : "json",
							success : function(response) {
								tbl.deleteRow(curRow);
							},
							error : function(response) {
								bootbox
										.alert("Unable to delete this row.");
								console.log("failed");
							}
						});
					}
				});
		}
	}
}

function validateDetailsBeforeSubmit() {
	var tbl = document.getElementById("result");
	var tabLength = (tbl.rows.length) - 1;
	var fromRange, toRange, rate;
	for (var i = 1; i <= tabLength; i++) {
		fromRange = getControlInBranch(tbl.rows[i], 'fromRange').value;
		toRange = getControlInBranch(tbl.rows[i], 'toRange').value;
		rate = getControlInBranch(tbl.rows[i], 'rate').value;
		if (jQuery.isNumeric(fromRange) && jQuery.isNumeric(toRange)) {
			if (fromRange != '' && toRange != ''
					&& (eval(fromRange) >= eval(toRange))) {
				bootbox
						.alert("\"To Range\" should be greater than \"From Range\" for row "
								+ (i) + ".");
				getControlInBranch(tbl.rows[i], 'toRange').value = "";
				getControlInBranch(tbl.rows[i], 'toRange').focus();
				return false;
			}
			if (!toRange) {
				bootbox.alert("Please enter \"To(days)\" for row " + (i) + ".");
				getControlInBranch(tbl.rows[i], 'toRange').value = "";
				getControlInBranch(tbl.rows[i], 'toRange').focus();
				return false;
			}
			if (!rate) {
				bootbox.alert("Please enter \"Penalty Rate(In Perc)\" for row "
						+ (i) + ".");
				getControlInBranch(tbl.rows[i], 'rate').value = "";
				getControlInBranch(tbl.rows[i], 'rate').focus();
				return false;
			}
		} else {
			bootbox.alert("Not a valid number for row " + (i) + ".");
			return false;
		}
	}
	return true;
}
