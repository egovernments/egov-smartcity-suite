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

jQuery(document)
		.ready(
				function() {

					jQuery('#submitBtn')
							.click(
									function() {
										if (jQuery("#basicproperty").val() == '') {
											bootbox
													.alert("Assessment number is mandatory");
											return false;
										} else if (jQuery("#reasonMaster")
												.val() == '') {
											bootbox
													.alert("Please select deactivation reason");
											return false;
										} else if (jQuery("#originalAssessment")
												.val() == ''
												&& jQuery("#reasonMaster")
														.val() != 'Bogus Property') {
											bootbox
													.alert("Original Assessment number required");
											return false;
										} else {
											var basicproperty = jQuery(
													"#basicproperty").val();
											jQuery('#deactivation-form').attr(
													'method', 'post');
											jQuery('#deactivation-form').attr(
													'action',
													'/ptis/deactivation/search/'
															+ basicproperty);
											jQuery('#deactivation-form')
													.submit();
										}
									});
					jQuery('#deactBtn')
							.click(
									function() {
										var index = jQuery(this).data('idx');
										if (!addFileInputField()) {
											if (jQuery("#basicproperty").val() == '') {
												bootbox
														.alert("Assessment number is mandatory");
												return false;
											} else if (jQuery("#reasonMaster")
													.val() == '') {
												bootbox
														.alert("Please select deactivation reason");
												return false;
											} else if (jQuery(
													"#originalAssessment")
													.val() == ''
													&& jQuery("#reasonMaster")
															.val() != 'Bogus Property') {
												bootbox
														.alert("Original Assessment number required");
												return false;
											} else if (jQuery("#councilno")
													.val() == '') {
												bootbox
														.alert("council number is required");
												return false;
											} else if (jQuery("#councilDate")
													.val() == '') {
												bootbox
														.alert("select the council date");
												return false;
											}

											else {
												var basicproperty = jQuery(
														"#basicproperty").val();
												jQuery('#deactivation-form')
														.attr('method', 'post');
												jQuery('#deactivation-form')
														.attr(
																'action',
																'/ptis/deactivation/update/'
																		+ basicproperty);
												jQuery('#deactivation-form')
														.submit();
											}
										}
									});

					jQuery('select').on('change', function() {
						hideOnReason(this.value);
					});

					jQuery(window).load(function() {
						var reason = jQuery("#reasonMaster").val();

						if (reason) {
							jQuery("#reasonMaster option").filter(function() {
								return this.text == reason;
							}).attr('selected', true);
							hideOnReason(reason);
						}

					});

					function hideOnReason(reason) {
						if (reason === 'Bogus Property') {
							jQuery('#originalAssessment').val('');
							jQuery('#orgnlAssmnt').addClass('display-hide');

						} else {
							jQuery('#orgnlAssmnt').removeClass('display-hide');
						}

					}

					jQuery(".dateval")
							.datepicker(
									{
										format : 'dd/mm/yyyy',
										autoclose : true,
										onRender : function(date) {
											return date.valueOf() < now
													.valueOf() ? 'disabled'
													: '';
										}

									}).data('datepicker');

				});
function addFileInputField() {
	var tempTrNo = $("#uploadertbl tbody tr").length;
	var flag = false;
	for (var i = 0; i < tempTrNo; i++) {
		var checkboxValue = jQuery("#check" + i).is(':checked');
		var curFieldValue = jQuery("#file" + i).val();
		if (curFieldValue == "" && checkboxValue) {
			bootbox.alert("Please attach the File");
			flag = true;
			return flag;
		}
	}
	return flag;
}