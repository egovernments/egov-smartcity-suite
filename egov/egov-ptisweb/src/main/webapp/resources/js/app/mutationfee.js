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

jQuery(document).ready(function() {
	jQuery('#flatAmount').change(function() {
		if (jQuery(this).val() != "" || jQuery(this).val().length > 0)
			jQuery('#percentage').attr('disabled', 'disabled');
		else
			jQuery('#percentage').removeAttr('disabled');
	});

	jQuery('#percentage').change(function() {
		if (this.value != "" || this.value.length > 0)
			jQuery('#flatAmount').attr('disabled', 'disabled');
		else
			jQuery('#flatAmount').removeAttr('disabled');
	});

	jQuery('#highLimit').change(function() {
		if (this.value != "" || this.value.length > 0) {
			jQuery('#recursiveFactor').val("").attr('disabled', 'disabled');
			jQuery('#recursiveAmount').val("").attr('disabled', 'disabled');
		} else {
			jQuery('#recursiveFactor,#recursiveAmount').removeAttr('disabled');
		}
	});

	$("#fromDate,#toDate").datepicker({
		format : "dd/mm/yyyy",
		autoclose : true
	}).on('changeDate', function(ev) {
		var fromDate = jQuery('#fromDate').val();
		var toDate = jQuery('#toDate').val();
		if (fromDate != "" && toDate != "") {
			var stsplit = fromDate.split("/");
			var ensplit = toDate.split("/");
			fromDate = stsplit[1] + "/" + stsplit[0] + "/" + stsplit[2];
			toDate = ensplit[1] + "/" + ensplit[0] + "/" + ensplit[2];

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

	jQuery('#recursiveFactor').change(function() {
		if (this.value != "" || this.value.length > 0) {
			jQuery('#highLimit').val("").attr('disabled', 'disabled');

		} else {
			jQuery('#highLimit').removeAttr('disabled');
		}
	});
	jQuery('#recursiveAmount').change(function() {
		if (this.value != "" || this.value.length > 0) {
			jQuery('#highLimit').val("").attr('disabled', 'disabled');

		} else {
			jQuery('#highLimit').removeAttr('disabled');

		}
	});

});
function validate() {
	var low = jQuery('#lowLimit').val();
	var high = jQuery('#highLimit').val();
	if (Number(high) <= Number(low)) {
		jQuery('#highlimiterror').show();	
		jQuery('#highLimit').val("");
	} else {
		jQuery('#highlimiterror').hide();
	}

}