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

/*
 * Note : Property "selectedModeBndry" used to traverse forward and backward. 
 * 1.ondrilldown at each level(ie zone/ward/block/property) this property value gets updated with the concatenated values of mode and boundary.
 * 2. Value format : mode~boundaryId. 
 * 3. Ex: zone~1 (At 1st level), zone~1-ward~6-block~8-property~10 (At last level). 
 * 4. property~10 means show all properties under block with id 10 / block~8 means show all blocks under ward id 8 and so on.  		
 */

var reportdatatable;
var revenueWardName;

jQuery(document).ready(function() {
	$("#reportTitle").hide();
	var fromDate = '';
	var toDate = '';
	drillDowntableContainer = jQuery("#tblrevisionpetitiondrilldown");
	jQuery('#btnsearch').click(function(e) {
		$("#reportTitle").show();
		if ($('#fromDate').val() == "" && $('#toDate').val() == "") {
			bootbox.alert("Please enter From Date and To Date");
			$("#reportTitle").hide();
			return false;
		}
		if ($('#fromDate').val() == "" && $('#toDate').val() != "") {
			bootbox.alert('Please enter the From Date');
			$("#reportTitle").hide();
			return false;
		}
		if ($('#fromDate').val() != "" && $('#toDate').val() == "") {
			bootbox.alert('Please enter the To Date');
			$("#reportTitle").hide();
			return false;
		}
		if ($('#fromDate').val() != "") {
			fromDate = $('#fromDate').data('datepicker').date;
		}
		if ($('#toDate').val() != "") {
			toDate = $('#toDate').data('datepicker').date;
		}
		if (toDate != '' && fromDate != '') {
			if (fromDate > toDate) {
				bootbox.alert(' To Date should be greater than  From Date');
				return false;
			}
		}
		callAjaxByBoundary();
	});

});

function callAjaxByBoundary() {
	var fromDate = jQuery('#fromDate').val();
	var toDate = jQuery('#toDate').val();
	var currDate = jQuery('#currentDate').val();
	jQuery('.report-section').removeClass('display-hide');
	jQuery('#report-footer').show();
	jQuery('#reportDates').html(
			"From RP Filed " + fromDate + " to RP Filed " + toDate
					+ " Generated on " + currDate);
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/ptis/report/revisionpetition/results",
					"data" : {
						fromDate : fromDate,
						toDate : toDate
					},
				},
				"orderCellsTop" : true,
				"bDestroy" : true,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ {
						"sExtends" : "pdf",
						"sTitle" : jQuery('#pdfTitle').val(),
						"sPdfMessage" : jQuery('#reportTitle').html()
					}, {
						"sExtends" : "xls",
						"sTitle" : jQuery('#pdfTitle').val(),
						"sPdfMessage" : jQuery('#reportTitle').html()
					}, {
						"sExtends" : "print",
						"sTitle" : jQuery('#pdfTitle').val(),
						"sPdfMessage" : jQuery('#reportTitle').html()
					} ]
				},
				"fnRowCallback" : function(row, data, index) {
				},
				aaSorting : [],
				columns : [ {
					"data" : "slNo",
					"sTitle" : "Sl.No"
				}, {
					"data" : "ownerName",
					"sTitle" : "Name of the party"
				}, {
					"data" : "assessmentNo",
					"sTitle" : "Assessment No"
				}, {
					"data" : "PropertyType",
					"sTitle" : "Property Type"
				}, {
					"data" : "receiptDate",
					"sTitle" : "Date of Receipt of Revision Petition"
				}, {
					"data" : "noticeDate",
					"sTitle" : "Date of Service of Notice"
				}, {
					"data" : "prevGenTax",
					"sTitle" : "General Tax / Vacant Land Tax"
				}, {
					"data" : "prevWaterTax",
					"sTitle" : "Water Tax"
				}, {
					"data" : "prevDrainageTax",
					"sTitle" : "Drainage Tax"
				}, {
					"data" : "prevLightTax",
					"sTitle" : "Lighting Tax"
				}, {
					"data" : "prevScavageTax",
					"sTitle" : "Scavenging / Conservancy Tax"
				}, {
					"data" : "prevEduTax",
					"sTitle" : "Education Tax"
				}, {
					"data" : "prevLibTax",
					"sTitle" : "Library Cess"
				}, {
					"data" : "prevUnauthPenaltyTax",
					"sTitle" : "Unauthorized Penalty"
				}, {
					"data" : "prevTotalTax",
					"sTitle" : "Total"
				}, {
					"data" : "currentGenTax",
					"sTitle" : "General Tax / Vacant Land Tax"
				}, {
					"data" : "currentWaterTax",
					"sTitle" : "Water Tax"
				}, {
					"data" : "currentDrainageTax",
					"sTitle" : "Drainage Tax"
				}, {
					"data" : "currentLightTax",
					"sTitle" : "Lighting Tax"
				}, {
					"data" : "currentScavageTax",
					"sTitle" : "Scavenging / Conservancy Tax"
				}, {
					"data" : "currentEduTax",
					"sTitle" : "Education Tax"
				}, {
					"data" : "currentLibTax",
					"sTitle" : "Library Cess"
				}, {
					"data" : "currentUnauthPenaltyTax",
					"sTitle" : "Unauthorized Penalty"
				}, {
					"data" : "currentTotalTax",
					"sTitle" : "Total"
				}, {
					"data" : "remarks",
					"sTitle" : "Orders of the Commissioner"
				} ]
			});

}