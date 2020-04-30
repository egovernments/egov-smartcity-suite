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
var tableContainer;
var reportdatatable;
jQuery(document).ready(function($) {

	$.fn.dataTable.moment('DD/MM/YYYY');

	$(":input").inputmask();

	$(".datepicker").datepicker({
		format : "dd/mm/yyyy"
	});
	drillDowntableContainer = $('#applicationSearchResults');

	function validRange(start, end) {
		var startDate = Date.parse(start);
		var endDate = Date.parse(end);

		// Check the date range, 86400000 is the number of
		// milliseconds in one day
		var difference = (endDate - startDate) / (86400000 * 7);
		if (difference < 0) {
			bootbox.alert("The start date must come before the end date.");
			$('#end_date').val('');
			return false;
		} else {
			return true;
		}

		return true;

	}

	$(".btn-primary").click(function(event) {
		$('#searchResultDiv').show();
		if ($('#start_date').val() != '' && $('#end_date').val() != '') {
			var start = $('#start_date').val();
			var end = $('#end_date').val();
			var stsplit = start.split("/");
			var ensplit = end.split("/");

			start = stsplit[1] + "/" + stsplit[0] + "/" + stsplit[2];
			end = ensplit[1] + "/" + ensplit[0] + "/" + ensplit[2];

			if (!validRange(start, end)) {
				return false;
			}

			else {
				document.forms[0].submit;
				callAjaxByBoundary();
				return true;
			}
		}
		if ($('#applicationType').val() == '') {
			bootbox.alert("Please select Application Type.");
			return false;
		}
		callAjaxByBoundary();
	});

});
$('#searchapp').keyup(function() {
	drillDowntableContainer.fnFilter(this.value);
});

$('#applicationSearchResults')
		.on(
				'click',
				'tbody tr',
				function(e) {
					var elementType = $(e.target).prop('nodeName');

					if (elementType != 'BUTTON') {
						drillDowntableContainer.$('tr.row_selected')
								.removeClass('row_selected');
						$(this).addClass('row_selected');
						var url = drillDowntableContainer.fnGetData(this, 8);
						$('#applicationSearchForm').attr('method', 'get');
						$('#applicationSearchForm').attr('action', url);
						window
								.open(url, 'window',
										'scrollbars=yes,resizable=yes,height=700,width=800,status=yes');
					}

				});

$(document).on("keypress", 'form', function(e) {
	var code = e.keyCode || e.which;
	if (code == 13) {
		e.preventDefault();
		return false;
	}
});

function callAjaxByBoundary() {

	reportdatatable = drillDowntableContainer
			.dataTable({
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-md-3 col-xs-6 text-right'p>>",
				"bDestroy" : true,
				"autoWidth" : false,
				searchable : true,
				"order" : [ [ 0, 'asc' ] ],
				ajax : {
					url : "/ptis/search/courtcasewriteoffapplication/result?"
							+ $("#applicationSearchForm").serialize(),
					type : 'POST',
					data : function(args) {
						return {
							"args" : JSON.stringify(args)
						};
					}
				},
				columns : [ {
					sTitle : "Application Type",
					name : "applicationType",
					data : "applicationType"
				}, {
					sTitle : "Application Number",
					name : "applicationNumber",
					data : "applicationNumber"
				}, {
					sTitle : "Application Date",
					name : "applicationDate",
					data : "applicationDate"
				}, {
					sTitle : "Applicant Name",
					name : "applicantName",
					data : "applicantName"
				}, {
					sTitle : "Channel",
					name : "source",
					data : "source"
				}, {
					sTitle : "Applicant Address",
					name : "applicationAddress",
					data : "applicationAddress"
				}, {
					sTitle : "Status",
					name : "applicationStatus",
					data : "applicationStatus"
				}, {
					sTitle : "Current Owner",
					name : "ownerName",
					data : "ownerName"
				}, {
					title : "url",
					data : "url",
					name : "url",
					"bVisible" : false
				}, ],
			});
}