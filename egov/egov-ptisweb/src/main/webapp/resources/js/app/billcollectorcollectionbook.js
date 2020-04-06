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
jQuery(document).ready(function() {
	$("#searchResultDiv").hide();
	var fromDate = '';
	var toDate = '';
	$(".datepicker").datepicker({
		dateFormat : 'dd/mm/yyyy'
	});
	changeFromToDates();
	drillDowntableContainer = jQuery("#bcCollBookReport-table");
	jQuery('#btnsearch').click(function(e) {
		$("#searchResultDiv").show();
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
	var year = jQuery('#year').val();
	var bcId = jQuery('#collectionOperator').val();
	jQuery('.report-section').removeClass('display-hide');
	jQuery('#report-footer').show();
	jQuery('#reportDates').html(
			"Bill Collectors Collection Book Report from " + fromDate + " to "
					+ toDate);
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
					"aButtons" : [
							{
								"sExtends" : "pdf",
								"sButtonText" : "Pdf",
								"sTitle" : jQuery('#pdfTitle').val(),
								"sPdfMessage" : "Bill Collectors Collection Book Report",
								"sPdfOrientation" : "landscape"
							},
							{
								"sExtends" : "xls",
								"sButtonText" : "Excel",
								"sPdfMessage" : "Bill Collectors Collection Book Report",
								"sTitle" : jQuery('#pdfTitle').val(),
							},
							{
								"sExtends" : "print",
								"sPdfMessage" : "Bill Collectors Collection Book Report",
								"sTitle" : jQuery('#pdfTitle').val(),
							} ]
				},
				ajax : {
					url : "/ptis/report/bccollectionbook/result",
					type : "GET",
					data : function(args) {
						return {
							"args" : JSON.stringify(args),
							"fromDate" : fromDate,
							"toDate" : toDate,
							"userId" : bcId,
							"financilaYear" : year,
							"filterName" : $("#filter").val()
						}
					},
				},
				"fnRowCallback" : function(row, data, index) {
				},
				aaSorting : [],
				columns : [ {
					"data" : "collectionDate",
					"sTitle" : "Date of Collection"
				}, {
					"data" : "name",
					"sTitle" : "Name of the Bill Collector"
				}, {
					"data" : "ward",
					"sTitle" : "Revenue Ward"
				}, {
					"data" : "assessmentNo",
					"sTitle" : " Assessment No."
				}, {
					"data" : "fromInstallment",
					"sTitle" : "From"
				}, {
					"data" : "toInstallment",
					"sTitle" : "Upto"
				}, {
					"data" : "arrearPropertyTax",
					"sTitle" : "Arrear"
				}, {
					"data" : "currentPropertyTax",
					"sTitle" : "Current"
				}, {
					"data" : "arrearEduTax",
					"sTitle" : "Arrear"
				}, {
					"data" : "currentEduTax",
					"sTitle" : "Current"
				}, {
					"data" : "arrearLibCess",
					"sTitle" : "Arrear"
				}, {
					"data" : "currentLibCess",
					"sTitle" : "Current"
				}, {
					"data" : "arrearTotal",
					"sTitle" : "Arrear"
				}, {
					"data" : "currentTotal",
					"sTitle" : "Current"
				} ]
			});

}

function changeFromToDates() {
	var endDateValue;
	var todayDate = new Date();
	var staringDate = jQuery('#year').val();
	var toValue = new Date(staringDate);
	$("#fromDate").datepicker("update", toValue).trigger('changeDate');
	$('.datepicker').datepicker('setStartDate', new Date(toValue));
	toValue.setDate(toValue.getDate() + 364);
	var toDate = new Date(staringDate);
	if (todayDate.getFullYear() == new Date(toValue).getFullYear()
			&& todayDate.getMonth() < 3) {
		$('#toDate').datepicker("update", new Date()).trigger('changeDate');
		$('.datepicker').datepicker('setEndDate', new Date());
	} else {
		var getLeapYear = isNotLeap(toValue.getFullYear());
		if (getLeapYear)
			endDateValue = toDate.setDate(toDate.getDate() + 365);
		else
			endDateValue = toDate.setDate(toDate.getDate() + 364);
		$('.datepicker').datepicker('setEndDate', new Date(endDateValue));
		$('#toDate').datepicker("update", new Date(endDateValue)).trigger(
				'changeDate');
	}
}

function isNotLeap(year) {
	return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
}