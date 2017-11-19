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

$(document)
		.ready(
				function() {

					//toggle between multiple tab
					jQuery('form').validate({
						ignore: ".ignore",
						invalidHandler: function(e, validator) {
							if(validator.errorList.length)
							$('#settingstab a[href="#' + jQuery(validator.errorList[0].element).closest(".tab-pane").attr('id') + '"]').tab('show');
						}
					});

					var validator = $("#updateMarriageRegistrationForm")
							.validate({
								highlight : function(element, errorClass) {
									$(element).fadeOut(function() {
										$(element).fadeIn();
									});
								}
							});

					$(".btn-primary")
							.click(
									function() {
										if ($('#allowDaysValidation').val() == "YES") {
											var noOfDays = validateApplicationDate();
											if (noOfDays > 90) {
												bootbox
														.alert("Application will not be accepted beyond 90 days from the date of marriage "
																+ $(
																		'#txt-dateOfMarriage')
																		.val());
												return false;
											}
										}

										var action = $('#workFlowAction').val();
										if (action == 'Forward') {
											return validateForm(validator);
										} else if (action == 'Reject') {
											$('#Reject').attr('formnovalidate',
													'true');
											var r = confirm("Do You Really Want to Reject The Registration!");
											if (r) {
												var approvalComent = $(
														'#approvalComent')
														.val();
												if (approvalComent == "") {
													bootbox
															.alert("Please enter rejection comments!");
													$('#approvalComent')
															.focus();
													return false;
												} else {
													validateWorkFlowApprover(action);
													document.forms[0].submit();
												}
											} else {
												return false;
											}
										} else if (action == 'Cancel Registration') {
											$('#Cancel Registration').attr(
													'formnovalidate', 'true');
											var res = confirm("Do You Really Want to Cancel The Registration!");
											if (res) {
												var approvalComment = $(
														'#approvalComent')
														.val();
												if (approvalComment == "") {
													bootbox
															.alert("Please enter cancellation comments!");
													$('#approvalComent')
															.focus();
													return false;
												} else {
													validateWorkFlowApprover(action);
													document.forms[0].submit();
												}
											} else {
												return false;
											}
										} else if (action == 'Print Certificate') {
											validateForm(validator);
										} else if (action == 'Approve') {
											validateMandatoryOnApprove(action);
											return validateSerialNumber(validator);
										}
									});

				});

function validateMandatoryOnApprove(action) {
	if (action == 'Approve') {
		$('.validate-madatory').find("span").addClass("mandatory");
		$('.addremovemandatory').attr("required", "true");
	} else {
		$('.validate-madatory').find("span").removeClass("mandatory");
		$('.addremovemandatory').removeattr("required", "true");
	}
}

function validateForm(validator) {
	if ($('form').valid()) {
		return true;
	} else {
		$.each(validator.invalidElements(), function(index, elem) {
			console.log(elem);
			if (!$(elem).is(":visible")
					&& !$(elem).closest('div.panel-body').is(":visible")) {
				$(elem).closest('div.panel-body').show();
			}
		});
		validator.focusInvalid();
		return false;
	}
}

function validateSerialNumber(validator) {
	var serialNo = $('#txt-serialNo').val();
	if (serialNo != '') {
		$
				.ajax({
					url : "/mrs/registration/checkunique-serialno",
					type : "GET",
					data : {
						"serialNo" : serialNo
					},
					dataType : "json",
					success : function(response) {
						if (response) {
							$('#txt-serialNo').val('');
							bootbox
									.alert("Entered serial number is already exists, please check and enter valid value and serial number must be unique.");
							return false;
						}
						return validateForm(validator);
					},
					error : function(response) {
						$('#txt-serialNo').val('');
						return false;
					}
				});
	} else {
		return validateForm(validator);
	}
}
