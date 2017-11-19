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

$(document)
		.ready(
				function() {
					usagetableContainer = $("#tblUlbDailycollection");
					
					var reportdatatable =  usagetableContainer
					.dataTable({
						type : 'GET',
						responsive : true,
						destroy : true,
						"bPaginate": false,
						"bInfo": false,
						"autoWidth" : false,
						"bDestroy" : true,
						"sDom" : "<'row'<'col-xs-19 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-19'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
						"oTableTools" : {
							"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
							"aButtons" : [ "print" ]
						},
						ajax : {
							url : "/ptis/reports/ulbWiseCollectionList"
						
						},
						columns : [
								{
									"sTitle" : "Sl.no",
								},
								{
									"data" : "district",
									"sTitle" : "District"
								},
								{
									"data" : "ulbName",
									"sTitle" : "Ulb Name"
								},
								{
									"data" : "ulbCode",
									"sTitle" : "Ulb Code"
								},
								{
									"data" : "collectorname",
									"sTitle" : "Name" ,"sWidth": "180px"
								},
								{
									"data" : "mobilenumber",
									"sTitle" : "Mobile Number"
								},
								{
									"data" : "target_arrears_demand",
									"sTitle" : "Arrears", "sClass": "text-right"
								},
								{
									"data" : "target_current_demand",
									"sTitle" : "Current", "sClass": "text-right"
								},
								{
									"data" : "target_total_demand",
									"sTitle" : "Total", "sClass": "text-right"
								},
								{
									"data" : "day_target",
									"sTitle" : "Day Target", "sClass": "text-right"
								},
								{
									"data" : "today_total_collection",
									"sTitle" : "Day Collection", "sClass": "text-right"
								},
								{
									"data" : "cummulative_arrears_collection",
									"sTitle" : "Arrears", "sClass": "text-right"
								},
								{
									"data" : "cummulative_currentyear_collection",
									"sTitle" : "Current", "sClass": "text-right"
								},
								{
									"data" : "cummulative_total_Collection",
									"sTitle" : " Total", "sClass": "text-right"
								},
								{
									"data" : "cummulative_currentYear_Percentage",
									"sTitle" : "%", "sClass": "text-center"
								},
								{
									"data" : "lastyear_collection",
									"sTitle" : "Today", "sClass": "text-right"
								},
								{
									"data" : "lastyear_cummulative_collection",
									"sTitle" : "Cummulative", "sClass": "text-right"
								},
								{
									"data" : "Percentage_compareWithLastYear",
									"sTitle" : "Increase of collection", "sClass": "text-right"
								},
								{
									"data" : "growth",
									"sTitle" : "% of growth" , "sClass": "text-center" 
								}
								 ],"footerCallback" : function(row, data, start, end, display) {
										var api = this.api(), data;
										if (data.length == 0) {
											$('#report-footer').hide();
										} else {
											$('#report-footer').show();
										}
										if (data.length > 0) {
											updateTotalFooter(6, api);
											updateTotalFooter(7, api);
											updateTotalFooter(8, api);
											updateTotalFooter(9, api);
											updateTotalFooter(10, api);
											updateTotalFooter(11, api);
											updateTotalFooter(12, api);
											updateTotalFooter(13, api);
											averageTotalFooter(14, api,data.length); 
											updateTotalFooter(15, api);
											updateTotalFooter(16, api);
											updateTotalFooter(17, api);
											averageTotalFooter(18, api,data.length);

										}
									},
								"aoColumnDefs" : [ {
									"aTargets" : [ 2, 3, 4, 5, 6],
									"mRender" : function(data, type, full) {
										return data;
									}
								} ],
								"fnRowCallback" : function(nRow, aData, iDisplayIndex){
					                $("td:first", nRow).html(iDisplayIndex +1);
					               return nRow;
								}
					});
					/*$('#btnsearch')
							.on('click',
									function(event) {
										event.preventDefault();
										
										jQuery('.loader-class').modal('hide');
										
										reportdatatable.fnSetColumnVis(1, true);
									});*/
				});

function averageTotalFooter(colidx, api,totalsize) { 
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
			formatNumberInr((pageTotal/totalsize).toFixed(2)) + ' (' + formatNumberInr((total/totalsize).toFixed(2)) + ')');
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
			formatNumberInr(pageTotal.toFixed(2)) + ' (' + formatNumberInr(total.toFixed(2)) + ')');
}
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