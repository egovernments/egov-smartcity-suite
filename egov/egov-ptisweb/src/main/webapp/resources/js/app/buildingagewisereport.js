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

var reportdatatable;
jQuery(document)
		.ready(
				function() {
					$("#searchResultDiv").hide();
					$(".datepicker").datepicker({
						dateFormat : 'dd/mm/yyyy'
					});
					drillDowntableContainer = jQuery("#buildAgewiselReport-table");
					jQuery('#btnsearch')
							.click(
									function(e) {
										$("#searchResultDiv").show();

										if (jQuery('#buildingfromage').val() == ""
												&& jQuery('#buildingtoage')
														.val() == "") {
											bootbox
													.alert(' Please select From Age and To Age');
											return false;
										} else if (jQuery('#buildingfromage')
												.val() == "") {
											bootbox
													.alert(' Please select From Age');
											return false;
										} else if (jQuery('#buildingtoage')
												.val() == "") {
											bootbox
													.alert(' Please select To Age');
											return false;
										} else if (jQuery('#buildingfromage')
												.val() > jQuery(
												'#buildingtoage').val()) {
											bootbox
													.alert(' To Age should be greater than  From Age');
											return false;
										}
										callAjaxByBoundary();
									});

				});

function callAjaxByBoundary() {
	var propertyTypeMaster = jQuery('#ownership').val();
	var fromAge = jQuery('#buildingfromage').val();
	var toAge = jQuery('#buildingtoage').val();
	jQuery('.report-section').removeClass('display-hide');
	jQuery('#report-footer').show();
	jQuery('#footeTitle')
			.html("[P] ->Part of the building was constructed during the criteria selected. ");

	reportdatatable = drillDowntableContainer
			.dataTable({
				destroy : true,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"autoWidth" : false,
				"bInfo" : false,
				processing : true,
				serverSide : true,
				sort : true,
				filter : true,
				"searching" : false,
				"order" : [ [ 0, 'asc' ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ {
						"sExtends" : "pdf",
						"sButtonText" : "Pdf",
						"sTitle" : jQuery('#pdfTitle').val(),
						"sPdfMessage" : "Building Age Wise Assessment Report",
						"sPdfOrientation" : "landscape"
					}, {
						"sExtends" : "xls",
						"sButtonText" : "Excel",
						"sPdfMessage" : "Building Age Wise Assessment Report",
						"sTitle" : jQuery('#pdfTitle').val(),
					}, {
						"sExtends" : "print",
						"sPdfMessage" : "Building Age Wise Assessment Report",
						"sTitle" : jQuery('#pdfTitle').val(),
					} ]
				},
				ajax : {
					url : "/ptis/report/agewiseassessmentreport/result",
					type : "GET",
					data : function(args) {
						return {
							"args" : JSON.stringify(args),
							"fromAge" : fromAge,
							"toAge" : toAge,
							"propertyTypeMaster" : propertyTypeMaster,
							"filterName" : $("#filter").val()
						}
					},
				},
				"fnRowCallback" : function(row, data, index) {
				},
				aaSorting : [],
				columns : [ {
					"data" : "assessmentNo",
					"sTitle" : "Assessment No."
				}, {
					"data" : "doorNo",
					"sTitle" : "Door No."
				}, {
					"data" : "name",
					"sTitle" : "Owner Name"
				}, {
					"data" : "zone",
					"sTitle" : "Revenue Zone"
				}, {
					"data" : "ward",
					"sTitle" : "Revenue Ward"
				}, {
					"data" : "block",
					"sTitle" : "Revenue Block"
				}, {
					"data" : "locality",
					"sTitle" : "Locality"
				}, {
					"data" : "propertyTax",
					"sTitle" : "Property Tax"
				}, {
					"data" : "libCess",
					"sTitle" : "Library Cess"
				}, {
					"data" : "eduTax",
					"sTitle" : "Education Tax"
				}, {
					"data" : "ucPenalty",
					"sTitle" : "UC Penalty"
				}, {
					"data" : "total",
					"sTitle" : "Total"
				} ]
			});

}