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
jQuery(document).ready(function($) {
	$('#genericSubReportResult-header').hide();
	$('#reportgeneration-header').hide();

	jQuery('#genericSubReportSearch').click(function(e) {
		submitForm();

	});
});

function submitForm() {
	if ($('form').valid()) {

		var today = getdate();

		oTable = $('#genericSubReport-table');
		$('#genericSubReport-header').show();
		$('#reportgeneration-header').show();
		var oDataTable = oTable
				.dataTable({
					"sPaginationType" : "bootstrap",
					"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
					"aLengthMenu" : [ [ 10, 25, 50, 100 ], [ 10, 25, 50, 100 ] ],
					"autoWidth" : false,
					"bDestroy" : true,
					"processing" : true,
					"oTableTools" : {
						"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf"

					},
					ajax : {

						url : "/lcms/reports/genericSubResult",
						data : {
							'aggregatedBy' : $("#aggregatedBy").val(),
							'caseCategory' : $("#caseCategory").val(),
							'standingCounsel' : $("#standingCounsel").val(),
							'courtType' : $("#courtType").val(),
							'courtName' : $("#courtName").val(),
							'judgmentType' : $("#judgmentType").val(),
							'petitionType' : $("#petitiontype").val(),
							'caseStatus' : $("#casestatus").val(),
							'officerIncharge' : $("#officerIncharge").val(),
							'fromDate' : $("#fromDate").val(),
							'toDate' : $("#toDate").val()

						}
					},
					"columns" : [ {
						"title" : "S. No",
						"sClass" : "text-center"
					}, {
						"data" : "aggregatedBy",
						"title" : "Aggregated By",
						"sClass" : "text-center"
					}, {
						"data" : "noOfCase",
						"title" : "Number Of Cases",
						"sClass" : "text-center"
					}

					],
					"fnRowCallback" : function(row, data, index) {
						$('td:eq(0)', row).html(index + 1);
						return row;
					},
				});
	}
}

function getdate() {
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth() + 1; // January is 0!

	var yyyy = today.getFullYear();
	if (dd < 10) {
		dd = '0' + dd
	}
	if (mm < 10) {
		mm = '0' + mm
	}
	var today = dd + '/' + mm + '/' + yyyy;
	return today;
}
