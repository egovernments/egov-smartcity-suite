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

var reportdatatable;
jQuery(document).ready(function($) {

	tableContainer1 = $("#ageingReport-table");
	drillDowntableContainer = $("#drilldownReport-table");
	$('#report-backbutton').hide();

	$('#backButton').click(function(e) {
		
		if ($('#selecteduserid').val()) {
			console.log('true!')
			$('#selecteduserid').val("");
			callAjaxByUserNameType();
		} else if ($('#complainttypeid').val()) {
			console.log('true!')
			$('#complainttypeid').val("");
			callAjaxByComplaintType();
		} else if ($('#deptid').val()) {
			console.log('true!')
			$('#deptid').val("");
			callAjaxByDepartment();
			if ($('#mode').val() != 'ByBoundary') {
				$('#report-backbutton').hide();
			}
		} else if ($('#locality').val()) {
			$('#locality').val("");
			callAjaxByLocality();
		}
		else if ($('#boundary').val()) {
			console.log('true!')
			$('#boundary').val("");
			callajaxdatatableForDrilDownReport();

		}

	});

	$('#drilldownReportSearch').click(function(e) {
		console.log('calling inside ajax');
		
		$('#locality').val();
		$('#deptid').val("");
		$('#complainttypeid').val("");
		$('#selecteduserid').val("");
		$('#boundary').val("");
		$('#type').val("");

		if ($('#mode').val() == 'ByBoundary') {
			callajaxdatatableForDrilDownReport();
		} else {
			callAjaxByDepartment();
			$('#report-backbutton').hide();
		}
	});
});

function callajaxdatatableForDrilDownReport() {

	var startDate = "";
	var endDate = "";
	var modeVal = "";
	var when_dateVal = "";
	startDate = $('#start_date').val();
	endDate = $('#end_date').val();
	modeVal = $('#mode').val();
	when_dateVal = $('#when_date').val();

	var groupByobj = "";
	groupByobj = $('input[name="groupBy"]:checked').val();
	if ($('#start_date').val() == "")
		startDate = "";

	if ($('#end_date').val() == "")
		endDate = "";

	$('.report-section').removeClass('display-hide');
	$('#report-footer').show();
	$('#report-backbutton').hide();
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "drillDown/resultList-update",
					data : {
						fromDate : startDate,
						toDate : endDate,
						groupBy : modeVal,
						deptid : $('#deptid').val(),
						boundary : $('#boundary').val(),
						locality : $('#locality').val(),
						complainttypeid : $('#complainttypeid').val(),
						selecteduserid : $('#selecteduserid').val(),
						type : $('#type').val(),
						complaintDateType : when_dateVal
					}
				},
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
							"data" : "name",
							"render" : function(data, type, row) {
								return '<a href="javascript:void(0);" onclick="setHiddenValueByLink(this,\'Boundarywise\');" data-hiddenele="boundary" data-eleval="'
										+ data + '">' + data + '</a>';
							},
							"sTitle" : "Boundary Name"
						}, {
							"mData" : "registered",
							"sTitle" : "Registered",
							"sClass" : "text-right"

						}, {
							"data" : "inprocess",
							"sTitle" : "Inprocess",
							"sClass" : "text-right"
						}, {
							"data" : "completed",
							"sTitle" : "Disposed",
							"sClass" : "text-right"
						}, {
							"data" : "rejected",
							"sTitle" : "Rejected",
							"sClass" : "text-right"

						},{
							"data" : "withinsla",
							"sTitle" : "Within SLA",
							"sClass" : "text-right"
						},{
							"data" : "beyondsla",
							"sTitle" : "Beyond SLA",
							"sClass" : "text-right"
						},{
							"data" : "total",
							"sTitle" : "Total",
							"sClass" : "text-right"

						} ],
				"footerCallback" : function(row, data, start, end, display) {
					var api = this.api(), data;
					if (data.length == 0) {
						$('#report-footer').hide();
					} else {
						$('#report-footer').show();
					}
					if (data.length > 0) {
						updateTotalFooter(1, api);
						updateTotalFooter(2, api);
						updateTotalFooter(3, api);
						updateTotalFooter(4, api);
						updateTotalFooter(5, api);
						updateTotalFooter(6, api);
						updateTotalFooter(7, api);

					}
				},
				"aoColumnDefs" : [ {
					"aTargets" : [ 1, 2, 3, 4, 5 ],
					"mRender" : function(data, type, full) {
						return formatNumberInr(data);
					}
				} ]

			});

}

function updateTotalFooter(colidx, api) {
	// Remove the formatting to get integer data for summation
	var intVal = function(i) {
		return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1
				: typeof i === 'number' ? i : 0;
	};

	// Total over all pages
	total = api.column(colidx).data().reduce(function(a, b) {
		return intVal(a) + intVal(b);
	});

	// Total over this page
	pageTotal = api.column(colidx, {
		page : 'current'
	}).data().reduce(function(a, b) {
		return intVal(a) + intVal(b);
	}, 0);

	// Update footer
	$(api.column(colidx).footer()).html(
			'<b>' + formatNumberInr(pageTotal) + ' (' + formatNumberInr(total)
					+ ')</b>');
}

//inr formatting number
function formatNumberInr(x) {
	if (x) {
		x = x.toString();
		var afterPoint = '';
		if (x.indexOf('.') > 0)
			afterPoint = x.substring(x.indexOf('.'), x.length);
		x = Math.floor(x);
		x = x.toString();
		var lastThree = x.substring(x.length - 3);
		var otherNumbers = x.substring(0, x.length - 3);
		if (otherNumbers != '')
			lastThree = ',' + lastThree;
		var res = otherNumbers.replace(/\B(?=(\d{2})+(?!\d))/g, ",")
				+ lastThree + afterPoint;
		return res;
	}
	return x;
}

function callAjaxByDepartment() {

	var startDate = "";
	var endDate = "";
	var modeVal = "";
	var when_dateVal = "";
	startDate = $('#start_date').val();
	endDate = $('#end_date').val();
	modeVal = $('#mode').val();
	when_dateVal = $('#when_date').val();

	var groupByobj = "";
	groupByobj = $('input[name="groupBy"]:checked').val();
	if ($('#start_date').val() == "")
		startDate = "";

	if ($('#end_date').val() == "")
		endDate = "";

	$('.report-section').removeClass('display-hide');
	$('#report-footer').show();
	$('#report-backbutton').show();

	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "drillDown/resultList-update",
					data : {
						fromDate : startDate,
						toDate : endDate,
						groupBy : modeVal,
						deptid : $('#deptid').val(),
						boundary : $('#boundary').val(),
						locality : $('#locality').val(),
						complainttypeid : $('#complainttypeid').val(),
						selecteduserid : $('#selecteduserid').val(),
						type : $('#type').val(),
						complaintDateType : when_dateVal
					}
				},
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
							"data" : "name",
							"render" : function(data, type, row) {
								return '<a href="javascript:void(0);" onclick="setHiddenValueByLink(this,\'Departmentwise\');" data-hiddenele="deptid" data-eleval="'
										+ data + '">' + data + '</a>';
							},
							"sTitle" : "Department Name"
						}, {
							"mData" : "registered",
							"sTitle" : "Registered",
							"sClass" : "text-right"

						}, {
							"data" : "inprocess",
							"sTitle" : "Inprocess",
							"sClass" : "text-right"
						}, {
							"data" : "completed",
							"sTitle" : "Disposed",
							"sClass" : "text-right"
						}, {
							"data" : "rejected",
							"sTitle" : "Rejected",
							"sClass" : "text-right"

						}, {
							"data" : "withinsla",
							"sTitle" : "Within SLA",
							"sClass" : "text-right"

						}, {
							"data" : "beyondsla",
							"sTitle" : "Beyond SLA",
							"sClass" : "text-right"
						},{
							"data" : "total",
							"sTitle" : "Total",
							"sClass" : "text-right"

						}],
				"footerCallback" : function(row, data, start, end, display) {
					var api = this.api(), data;
					if (data.length == 0) {
						$('#report-footer').hide();
					} else {
						$('#report-footer').show();
					}
					if (data.length > 0) {
						updateTotalFooter(1, api);
						updateTotalFooter(2, api);
						updateTotalFooter(3, api);
						updateTotalFooter(4, api);
						updateTotalFooter(5, api);
						updateTotalFooter(6, api);
						updateTotalFooter(7, api);

					}
				},
				"aoColumnDefs" : [ {
					"aTargets" : [ 1, 2, 3, 4, 5 ],
					"mRender" : function(data, type, full) {
						return formatNumberInr(data);
					}
				} ]

			});
	//e.stopPropagation();

}
function callAjaxByLocality(){

	var startDate = "";
	var endDate = "";
	var modeVal = "";
	var when_dateVal = "";
	startDate = $('#start_date').val();
	endDate = $('#end_date').val();
	modeVal = $('#mode').val();
	when_dateVal = $('#when_date').val();

	var groupByobj = "";
	groupByobj = $('input[name="groupBy"]:checked').val();
	if ($('#start_date').val() == "")
		startDate = "";

	if ($('#end_date').val() == "")
		endDate = "";

	$('.report-section').removeClass('display-hide');
	$('#report-footer').show();
	$('#report-backbutton').show();
	
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "drillDown/resultList-update",
					data : {
						fromDate : startDate,
						toDate : endDate,
						groupBy : modeVal,
						deptid : $('#deptid').val(),
						boundary : $('#boundary').val(),
						locality : $('#locality').val(),
						complainttypeid : $('#complainttypeid').val(),
						selecteduserid : $('#selecteduserid').val(),
						type : $('#type').val(),
						complaintDateType : when_dateVal
					}
				},
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
							"data" : "name",
							"render" : function(data, type, row) {
								return '<a href="javascript:void(0);" onclick="setHiddenValueByLink(this,\'LocalityWise\');" data-hiddenele="locality" data-eleval="'
										+ data + '">' + data + '</a>';
							},
							"sTitle" : "Locality"
						}, {
							"mData" : "registered",
							"sTitle" : "Registered",
							"sClass" : "text-right"

						}, {
							"data" : "inprocess",
							"sTitle" : "Inprocess",
							"sClass" : "text-right"
						}, {
							"data" : "completed",
							"sTitle" : "Disposed",
							"sClass" : "text-right"
						}, {
							"data" : "rejected",
							"sTitle" : "Rejected",
							"sClass" : "text-right"

						},{
							"data" : "withinsla",
							"sTitle" : "Within SLA",
							"sClass" : "text-right"
						},{
							"data" : "beyondsla",
							"sTitle" : "Beyond SLA",
							"sClass" : "text-right"
						},{
							"data" : "total",
							"sTitle" : "Total",
							"sClass" : "text-right"

						}],
				"footerCallback" : function(row, data, start, end, display) {
					var api = this.api(), data;
					if (data.length == 0) {
						$('#report-footer').hide();
					} else {
						$('#report-footer').show();
					}
					if (data.length > 0) {
						updateTotalFooter(1, api);
						updateTotalFooter(2, api);
						updateTotalFooter(3, api);
						updateTotalFooter(4, api);
						updateTotalFooter(5, api);
						updateTotalFooter(6, api);
						updateTotalFooter(7, api);

					}
				},
				"aoColumnDefs" : [ {
					"aTargets" : [ 1, 2, 3, 4, 5 ],
					"mRender" : function(data, type, full) {
						return formatNumberInr(data);
					}
				} ]

			});
}
function callAjaxByComplaintType() {

	var startDate = "";
	var endDate = "";
	var modeVal = "";
	var when_dateVal = "";
	startDate = $('#start_date').val();
	endDate = $('#end_date').val();
	modeVal = $('#mode').val();
	when_dateVal = $('#when_date').val();

	var groupByobj = "";
	groupByobj = $('input[name="groupBy"]:checked').val();
	if ($('#start_date').val() == "")
		startDate = "";

	if ($('#end_date').val() == "")
		endDate = "";

	$('.report-section').removeClass('display-hide');
	$('#report-footer').show();
	$('#report-backbutton').show();
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "drillDown/resultList-update",
					data : {
						fromDate : startDate,
						toDate : endDate,
						groupBy : modeVal,
						deptid : $('#deptid').val(),
						boundary : $('#boundary').val(),
						locality : $('#locality').val(),
						complainttypeid : $('#complainttypeid').val(),
						selecteduserid : $('#selecteduserid').val(),
						type : $('#type').val(),
						complaintDateType : when_dateVal
					}
				},
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
							"data" : "name",
							"render" : function(data, type, row) {
								return '<a href="javascript:void(0);" onclick="setHiddenValueByLink(this,\'ComplaintTypeWise\');" data-hiddenele="complainttypeid" data-eleval="'
										+ data + '">' + data + '</a>';
							},
							"sTitle" : "Grievance Type Name"
						}, {
							"mData" : "registered",
							"sTitle" : "Registered",
							"sClass" : "text-right"

						}, {
							"data" : "inprocess",
							"sTitle" : "Inprocess",
							"sClass" : "text-right"
						}, {
							"data" : "completed",
							"sTitle" : "Disposed",
							"sClass" : "text-right"
						}, {
							"data" : "rejected",
							"sTitle" : "Rejected",
							"sClass" : "text-right"

						},{
							"data" : "withinsla",
							"sTitle" : "Within SLA",
							"sClass" : "text-right"

						},{
							"data" : "beyondsla",
							"sTitle" : "Beyond SLA",
							"sClass" : "text-right"
						}, {
							"data" : "total",
							"sTitle" : "Total",
							"sClass" : "text-right"

						}],
				"footerCallback" : function(row, data, start, end, display) {
					var api = this.api(), data;
					if (data.length == 0) {
						$('#report-footer').hide();
					} else {
						$('#report-footer').show();
					}
					if (data.length > 0) {
						updateTotalFooter(1, api);
						updateTotalFooter(2, api);
						updateTotalFooter(3, api);
						updateTotalFooter(4, api);
						updateTotalFooter(5, api);
						updateTotalFooter(6, api);
						updateTotalFooter(7, api);

					}
				},
				"aoColumnDefs" : [ {
					"aTargets" : [ 1, 2, 3, 4, 5 ],
					"mRender" : function(data, type, full) {
						return formatNumberInr(data);
					}
				} ]

			});
}
function callAjaxByUserNameType() {

	var startDate = "";
	var endDate = "";
	var modeVal = "";
	var when_dateVal = "";
	startDate = $('#start_date').val();
	endDate = $('#end_date').val();
	modeVal = $('#mode').val();
	when_dateVal = $('#when_date').val();

	var groupByobj = "";
	groupByobj = $('input[name="groupBy"]:checked').val();
	if ($('#start_date').val() == "")
		startDate = "";

	if ($('#end_date').val() == "")
		endDate = "";

	$('.report-section').removeClass('display-hide');
	$('#report-footer').show();

	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "drillDown/resultList-update",
					data : {
						fromDate : startDate,
						toDate : endDate,
						groupBy : modeVal,
						deptid : $('#deptid').val(),
						boundary : $('#boundary').val(),
						locality : $('#locality').val(),
						complainttypeid : $('#complainttypeid').val(),
						selecteduserid : $('#selecteduserid').val(),
						type : $('#type').val(),
						complaintDateType : when_dateVal
					}
				},
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
							"data" : "name",
							"render" : function(data, type, row) {
								return '<a href="javascript:void(0);" onclick="setHiddenValueByLink(this,\'UserNameWise\');" data-hiddenele="selecteduserid" data-eleval="'
										+ data + '">' + data + '</a>';
							},
							"sTitle" : "User name-Position Name"
						}, {
							"mData" : "registered",
							"sTitle" : "Registered",
							"sClass" : "text-right"

						}, {
							"data" : "inprocess",
							"sTitle" : "Inprocess",
							"sClass" : "text-right"
						}, {
							"data" : "completed",
							"sTitle" : "Disposed",
							"sClass" : "text-right"
						}, {
							"data" : "rejected",
							"sTitle" : "Rejected",
							"sClass" : "text-right"

						},{
							"data" : "withinsla",
							"sTitle" : "Within SLA",
							"sClass" : "text-right"

						},{
							"data" : "beyondsla",
							"sTitle" : "Beyond SLA",
							"sClass" : "text-right"
						}, {
							"data" : "total",
							"sTitle" : "Total",
							"sClass" : "text-right"

						}],
				"footerCallback" : function(row, data, start, end, display) {
					var api = this.api(), data;
					if (data.length == 0) {
						$('#report-footer').hide();
					} else {
						$('#report-footer').show();
					}
					if (data.length > 0) {
						updateTotalFooter(1, api);
						updateTotalFooter(2, api);
						updateTotalFooter(3, api);
						updateTotalFooter(4, api);
						updateTotalFooter(5, api);
						updateTotalFooter(6, api);
						updateTotalFooter(7, api);

					}
				},
				"aoColumnDefs" : [ {
					"aTargets" : [ 1, 2, 3, 4, 5 ],
					"mRender" : function(data, type, full) {
						return formatNumberInr(data);
					}
				} ]

			});
}
function callAjaxByComplaintDetail() {

	var startDate = "";
	var endDate = "";
	var modeVal = "";
	var when_dateVal = "";
	startDate = $('#start_date').val();
	endDate = $('#end_date').val();
	modeVal = $('#mode').val();
	when_dateVal = $('#when_date').val();

	var groupByobj = "";
	groupByobj = $('input[name="groupBy"]:checked').val();
	if ($('#start_date').val() == "")
		startDate = "";

	if ($('#end_date').val() == "")
		endDate = "";

	$('.report-section').removeClass('display-hide');
	$('#report-footer').hide();

	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "drillDown/resultList-update",
					data : {
						fromDate : startDate,
						toDate : endDate,
						groupBy : modeVal,
						deptid : $('#deptid').val(),
						boundary : $('#boundary').val(),
						locality : $('#locality').val(),
						complainttypeid : $('#complainttypeid').val(),
						selecteduserid : $('#selecteduserid').val(),
						type : $('#type').val(),
						complaintDateType : when_dateVal
					}
				},
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
							"data" : "crn",
							"sTitle" : "Complaint Number",
							"render" : function(data, type, row) {
								return '<a href="javascript:void(0);" onclick="window.open(\'/pgr/public/complaint/view/'
										+ data
										+ '\',\'\', \'width=800, height=600\');" data-hiddenele="selecteduserid" data-eleval="'
										+ data + '">' + data + '</a>';
							},
						}, {
							"mData" : "createddate",
							"sTitle" : "Grievance Date"

						}, {
							"data" : "complainantname",
							"sTitle" : "Complainant Name"
						}, {
							"data" : "boundaryname",
							"sTitle" : "Grievance Address"
						}, {
							"data" : "details",
							"sTitle" : "Grievance Details"
						}, {
							"data" : "status",
							"sTitle" : "Status"

						}, {
							"data" : "feedback",
							"sTitle" : "Feedback"

						}, {
								"data" : "issla",
								"sTitle" : "Within SLA(Yes or No)" 
										
						}]

			});
}
function showChangeDropdown(dropdown) {
	$('.drophide').hide();
	var showele = $(dropdown).find("option:selected").data('show');
	if (showele) {
		$(showele).show();
	} else {
		$('#start_date').val("");
		$('#end_date').val("");

	}
}

function setHiddenValueByLink(obj, paaram) {

	$('input[name=type]').val(paaram);
	$('input[name=' + $(obj).data('hiddenele') + ']')
			.val($(obj).data('eleval'));
	if (paaram == 'Departmentwise') {
		// bootbox.alert('call complaint type');
		callAjaxByComplaintType();
	} else if (paaram == 'Boundarywise') {
		// bootbox.alert('call locality type');
		callAjaxByLocality();
	} else if (paaram == 'ComplaintTypeWise') {
		callAjaxByUserNameType();
	} else if (paaram == 'UserNameWise') {
		callAjaxByComplaintDetail();

	} else if (paaram == 'LocalityWise') {
		callAjaxByDepartment();
	}
	

}
