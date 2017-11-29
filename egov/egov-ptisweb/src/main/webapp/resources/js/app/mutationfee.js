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

jQuery(document).ready(
		function() {
			var todtid;
			jQuery('#flatAmount').blur(function() {
				if (jQuery(this).val() != "" || jQuery(this).val().length > 0)
					jQuery('#percentage').val("").attr('disabled', 'disabled');
				else
					jQuery('#percentage').removeAttr('disabled');
			});

			jQuery('#percentage').blur(function() {
				if (this.value != "" || this.value.length > 0)
					jQuery('#flatAmount').val("").attr('disabled', 'disabled');
				else
					jQuery('#flatAmount').removeAttr('disabled');
			});

			$("#fromDate,#toDate").datepicker({
				format : "dd/mm/yyyy",
				autoclose : true
			}).on(
					'changeDate',
					function(ev) {
						var fromDate = jQuery('#fromDate').val();
						var toDate = jQuery('#toDate').val();
						if (fromDate != "" && toDate != "") {
							var stsplit = fromDate.split("/");
							var ensplit = toDate.split("/");
							fromDate = stsplit[1] + "/" + stsplit[0] + "/"
									+ stsplit[2];
							toDate = ensplit[1] + "/" + ensplit[0] + "/"
									+ ensplit[2];

							var start = Date.parse(fromDate);
							var end = Date.parse(toDate);
							var difference = (end - start) / (86400000 * 7);
							if (difference <= 0) {
								jQuery('#todateerror').show();
								jQuery('#toDate').val("");
							} else {
								jQuery('#todateerror').hide();
							}
						}
					});
			jQuery('#highLimit').blur(
					function() {
						if (this.value != "" || this.value.length > 0) {
							jQuery('input:radio[name=isRecursive]').val("")
									.attr('disabled', 'disabled');
						} else {
							jQuery('input:radio[name=isRecursive]').removeAttr(
									'disabled');
						}
					});

			jQuery('#recursiveFactor').blur(function() {
				if (this.value != "" || this.value.length > 0) {
					jQuery('#highLimit').val("").attr('disabled', 'disabled');

				} else {
					jQuery('#highLimit').removeAttr('disabled');
				}
			});
			jQuery('#recursiveAmount').blur(function() {
				if (this.value != "" || this.value.length > 0) {
					jQuery('#highLimit').val("").attr('disabled', 'disabled');

				} else {
					jQuery('#highLimit').removeAttr('disabled');

				}
			});
			jQuery('.effToDate').change(function() {
				var toDt = jQuery('.effToDate').val();
				if (toDt != "" || toDt.length > 0)
					jQuery('.btnsave').removeAttr('disabled');
			});

			jQuery('.btnedit').click(
					function(e) {
						todtid = $(this).closest('tr').find('#key').val();
						var effToDateTextBox = $(
								'#tblViewMutation tbody tr:eq('
										+ jQuery(this).attr('data-idx') + ')')
								.find('.effToDate');

						var effToDateValue = effToDateTextBox.val();
						var todaysDate = new Date();
						var currentDate = todaysDate.getDate() + "/"
								+ (todaysDate.getMonth() + 1) + "/"
								+ todaysDate.getFullYear();
						var stsplit = effToDateValue.split("/");
						var ensplit = currentDate.split("/");
						effToDateValue = stsplit[1] + "/" + stsplit[0] + "/"
								+ stsplit[2];
						currentDate = ensplit[1] + "/" + ensplit[0] + "/"
								+ ensplit[2];

						var start = Date.parse(effToDateValue);
						var end = Date.parse(currentDate);
						var difference = (end - start) / (86400000 * 7);
						if (difference < 0) {

							effToDateTextBox.removeAttr('disabled');
							effToDateTextBox.addClass('datepicker');
							changeDatePicker();
						}
						return false;
					});

			$('#view').click(
					function() {
						$('#mutationFeeForm').attr('method', 'get');
						$('#mutationFeeForm').attr(
								'action',
								'/ptis/mutationfee/modify/'
										+ $('#slabName').val());
					});
			$('.btnsave').click(
					function() {
						$('#mutationFeeForm').attr('method', 'post');
						$('#mutationFeeForm').attr('action',
								'/ptis/mutationfee/modify/' + todtid);
					});

			$('#addnewslab').click(
					function() {
						$('#mutationFeeForm').attr('method', 'get');
						$('#mutationFeeForm').attr('action',
								'/ptis/mutationfee/create');
					});

			function changeDatePicker() {
				$(".effToDate").datepicker({
					format : "dd/mm/yyyy",
					autoclose : true
				}).on('changeDate', function(ev) {
					todtid = $(this).closest('tr').find('#key').val();
				});
			}
		});

function validate() {
	var low = jQuery('#lowLimit').val();
	var high = jQuery('#highLimit').val();
	if ((high.length > 0) && (Number(high) > Number(low))) {
		jQuery('#highlimiterror').hide();
		jQuery('#recursiveFactor').val("").attr('disabled', 'disabled');
		jQuery('#recursiveAmount').val("").attr('disabled', 'disabled');
	} else {
		jQuery('#highLimit').val("");
		jQuery('#highlimiterror').show();
		jQuery('#recursiveFactor,#recursiveAmount').removeAttr('disabled');
	}
}
