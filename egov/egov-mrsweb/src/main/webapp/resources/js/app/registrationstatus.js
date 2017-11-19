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

$(document)
.ready(
		function() {

			
			$('#btn_registrationstatus_search').click(function() {
				if ($('form').valid()) {
					callAjaxSearch();
				} else {
					return false;
				}
				
			});

			function callAjaxSearch() {
				// To get current date
				var currentDate = new Date();
				var day = currentDate.getDate();
				var month = currentDate.getMonth() + 1;
				var year = currentDate.getFullYear();
				var currentDate = day + "-" + month + "-" + year;
				var fromdate = $('#txt-fromdate').val();
				var todate = $('#txt-todate').val();
				var status = $('#status').val();
				var registrationunit = $('#registrationunit').val();
				$('.report-section').removeClass('display-hide');
				$("#registrationstatus_table")
						.dataTable(
								{
									ajax : {
										url : "/mrs/report/applicantionsstatus-count",
										type : "POST",
										beforeSend : function() {
											$('.loader-class')
													.modal(
															'show',
															{
																backdrop : 'static'
															});
										},
										"data" : getFormData($('form')),
										complete : function() {
											$('.loader-class').modal(
													'hide');
										}
									},
									"bDestroy" : true,
									"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
									"aLengthMenu" : [
											[ 10, 25, 50, -1 ],
											[ 10, 25, 50, "All" ] ],
									"oTableTools" : {
										"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
										"aButtons" : [ {
								             "sExtends": "pdf",
						                     "sPdfMessage": "Report Generated On "+ currentDate + "",
						                     "sTitle": "Marriage Registration Status Report",
						                     "sPdfOrientation": "landscape"
							                },
							                {
									             "sExtends": "xls",
						                         "sPdfMessage": "Marriage Registration Status Report",
						                         "sTitle": "Marriage Registration Status Report"
								             },
								             {
									             "sExtends": "print",
						                         "sTitle": "Marriage Registration Status Report"
								             } ]
									},
									aaSorting : [],
									columns : [
									           {"data":null,
									        	   render: function (data, type, row, meta) {
									        	        return meta.row + meta.settings._iDisplayStart + 1;
								                },   
									           },
											{
												"data" : "registrationUnit",
												"sClass" : "text-left"
											},
											{
												"data" : "createdCount",
												render : function(data,
														type, row, meta) {
													return parseInt(row.createdCount) !== 0 ? '<a onclick="openPopup(\'/mrs/report/applicantionsstatus-count?'
															+ 'registrationunit='
															+ row.registrationUnit
															+ '&'
															+ 'status=CREATED'
															+ '&'
															+ 'fromdate='
															+ fromdate
															+ '&'
															+ 'todate='
															+ todate
															+ '\')" href="javascript:void(0);">'
															+ row.createdCount
															+ '</a>'
															: row.createdCount;
												},
												"sClass" : "text-center"
											},
											{
												"data" : "approvedCount",
												render : function(data,
														type, row, meta) {
													return parseInt(row.approvedCount) !== 0 ? '<a onclick="openPopup(\'/mrs/report/applicantionsstatus-count?'
															+ 'registrationunit='
															+ row.registrationUnit
															+ '&'
															+ 'status=APPROVED'
															+ '&'
															+ 'fromdate='
															+ fromdate
															+ '&'
															+ 'todate='
															+ todate
															+ '\')" href="javascript:void(0);">'
															+ row.approvedCount
															+ '</a>'
															: row.approvedCount;
												},
												"sClass" : "text-center"
											},
											{
												"data" : "registeredCount",
												render : function(data,
														type, row, meta) {
													return parseInt(row.registeredCount) !== 0 ? '<a onclick="openPopup(\'/mrs/report/applicantionsstatus-count?'
															+ 'registrationunit='
															+ row.registrationUnit
															+ '&'
															+ 'status=REGISTERED'
															+ '&'
															+ 'fromdate='
															+ fromdate
															+ '&'
															+ 'todate='
															+ todate
															+ '\')" href="javascript:void(0);">'
															+ row.registeredCount
															+ '</a>'
															: row.registeredCount;
												},
												"sClass" : "text-center"
											},
											{
												"data" : "rejectedCount",
												render : function(data,
														type, row, meta) {
													return parseInt(row.rejectedCount) !== 0 ? '<a onclick="openPopup(\'/mrs/report/applicantionsstatus-count?'
															+ 'registrationunit='
															+ row.registrationUnit
															+ '&'
															+ 'status=REJECTED'
															+ '&'
															+ 'fromdate='
															+ fromdate
															+ '&'
															+ 'todate='
															+ todate
															+ '\')" href="javascript:void(0);">'
															+ row.rejectedCount
															+ '</a>'
															: row.rejectedCount;
												},
												"sClass" : "text-center"
											},
											{
												"data" : "cancelledCount",
												render : function(data,
														type, row, meta) {
													return parseInt(row.cancelledCount) !== 0 ? '<a onclick="openPopup(\'/mrs/report/applicantionsstatus-count?'
															+ 'regunit='
															+ row.registrationUnit
															+ '&'
															+ 'status=CANCELLED'
															+ '&'
															+ 'fromdate='
															+ fromdate
															+ '&'
															+ 'todate='
															+ todate
															+ '\')" href="javascript:void(0);">'
															+ row.cancelledCount
															+ '</a>'
															: row.cancelledCount;
												},
												"sClass" : "text-center"
											},
											{
												"data" : "total",
												"sClass" : "text-center"
											}],
											"footerCallback" : function(row, data, start, end, display) {
												var api = this.api(), data;
												if (data.length == 0) {
													jQuery('#report-footer').hide();
												} else {
													jQuery('#report-footer').show(); 
												}
												if (data.length > 0) {
													updateTotalFooter(2, api);
													updateTotalFooter(3, api);
													updateTotalFooter(4, api);
													updateTotalFooter(5, api);
													updateTotalFooter(6, api);
													updateTotalFooter(7, api);
												}
											},
											"aoColumnDefs" : [ {
												"aTargets" : [2,3,4,5,6,7],
												"mRender" : function(data, type, full) {
													return formatNumberInr(data);    
												}
											} ]		
								});

			}

		});

function openPopup(url) {
	window.open(url, 'window',
			'scrollbars=1,resizable=yes,height=600,width=800,status=yes');

}

function getFormData($form) {
var unindexed_array = $form.serializeArray();
var indexed_array = {};

$.map(unindexed_array, function(n, i) {
indexed_array[n['name']] = n['value'];
});

return indexed_array;
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
	jQuery(api.column(colidx).footer()).html(
			formatNumberInr(pageTotal) + ' (' + formatNumberInr(total)
					+ ')');
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
