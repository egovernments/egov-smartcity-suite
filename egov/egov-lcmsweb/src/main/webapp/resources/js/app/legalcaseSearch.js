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
var tableContainer;
var reportdatatable;
jQuery(document).ready(
		function($) {

			tableContainer = $('#legalCaseResults');
			document.onkeydown = function(evt) {
				var keyCode = evt ? (evt.which ? evt.which : evt.keyCode)
						: event.keyCode;
				if (keyCode == 13) {
					submitForm();
				}
			}
			$('#legalcaseReportSearch').click(function() {
				submitForm();
			});

		});
function submitForm() {

	var caseNumber = $("#caseNumber").val();
	var lcNumber = $("#lcNumber").val();
	$('.report-section').removeClass('display-hide');
	$('#report-footer').show();
	reportdatatable = tableContainer
			.dataTable({
				ajax : {
					url : "/lcms/search/legalsearchResult",
					data : {
						'caseNumber' : caseNumber,
						'lcNumber' : lcNumber
					}
				},
				"sPaginationType" : "bootstrap",
				"autoWidth" : false,
				"bDestroy" : true,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ "xls", "pdf", "print" ]
				},
				columns : [
						{
							"data" : "casenumber",

							"sTitle" : "Case Number"
						},
						{
							"data" : "legalcaseno",
							"sTitle" : "Legal Case Number",
							"className" : "text-right"
						},
						{
							title : 'Actions',
							"className" : "text-right",
							render : function(data, type, full) {

								return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="1">Judgement</option><option value="2">Create Hearing</option><option value="3">Edit legalCase</option><option value="4">View legalCase</option></select>');
							}
						} /*
							 * , { "data" : "casenumber2", "sTitle" : "case2",
							 * "className": "text-right" }, { "data" :
							 * "legalcaseno1", "sTitle" : "Legal1", "className":
							 * "text-right"
							 * 
							 * },{ "data" : "legalcaseno2", "sTitle" : "legal2",
							 * "className": "text-right" }, { "data" : "total",
							 * "sTitle" : "Total", "className": "text-right"
							 *  }
							 */],
				"footerCallback" : function(row, data, start, end, display) {
					var api = this.api(), data;
					if (data.length == 0) {
						$('#report-footer').hide();
					} else {
						$('#report-footer').show();
					}

				},

			});
	$('.loader-class').modal('hide');
}
