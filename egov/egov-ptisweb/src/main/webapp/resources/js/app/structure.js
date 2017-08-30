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
jQuery(document).ready(
		function() {
			$("#fromDate,#toDate").on(
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

			jQuery('#edit').click(
					function() {
						if (jQuery("#typeName").val() == '') {
							bootbox.alert("Building type Required");
							return false;
						} else {
							var building = jQuery("#typeName").val();
							window.open("/ptis/structureclassification/edit/"
									+ building, "_self");

						}
					});

		});
