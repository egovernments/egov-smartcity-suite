/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *  
 *  Copyright (C) 2017  eGovernments Foundation
 *  
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *  
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *  
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *  
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *  
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *  
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

function openTradeLicense(obj) {
	window.open("/tl/public/viewtradelicense/viewTradeLicense-view.action?id="
			+ $(obj).data('eleval'), '',
			'scrollbars=yes,width=1000,height=700,status=yes');
}

function populateData(reportData) {
	$('.report-section').removeClass('display-hide');
	$('#report-footer').show();
	var dcbTable = $("#tbldcbdrilldown");
	var reportdatatable = dcbTable
			.dataTable({
				dom : "<'row'<'col-xs-4 pull-right'f>r>t<'row add-margin'<'col-md-3 col-xs-6'i><'col-md-2 col-xs-6'l><'col-md-3 col-xs-6 text-right'B><'col-md-4 col-xs-6 text-right'p>>",
	 			"autoWidth" : false,
	 			"bDestroy" : true,
				responsive : true,
				destroy : true,
				'aaData' : reportData,
				buttons : [  {
					extend : 'pdf',
					title : 'DCB Report By Trade Wise',
					filename : 'DCB Report By Trade Wiset',
					orientation : 'landscape',
					footer : true,
					pageSize:'A3',
					exportOptions : {
						columns : ':visible'
					}
				}, {
					extend : 'excel',
					filename : 'DCB Report By Trade Wise',
					footer : true,
					exportOptions : {
						columns : ':visible'
					}
				}, {
					extend : 'print',
					title : 'DCB Report By Trade Wise',
					filename : 'DCB Report By Trade Wise',
					footer : true,
					exportOptions : {
						columns : ':visible'
					}
				} ],
				columns : [
						{
							"data" : function(row, type, set, meta) {
								return {
									name : row.licensenumber,
									id : row.licenseid
								};
							},
							"render" : function(data, type, row) {
								return '<a href="javascript:void(0);" onclick="openTradeLicense(this);" data-hiddenele="id" data-eleval="'
										+ data.id + '">' + data.name + '</a>';
							},
							"sTitle" : "License No."
						}, {
							"data" : "arr_demand",
							"sTitle" : "Arrears"
						}, {
							"data" : "curr_demand",
							"sTitle" : "Current"
						}, {
							"data" : "username",
							"sTitle" : "username",
							"bVisible" : false
						}, {
							"data" : "total_demand",
							"sTitle" : "Total"
						}, {
							"data" : "arr_coll",
							"sTitle" : "Arrears"
						}, {
							"data" : "curr_coll",
							"sTitle" : "Current"
						}, {
							"data" : "total_coll",
							"sTitle" : "Total"
						}, {
							"data" : "arr_balance",
							"sTitle" : "Arrears"
						}, {
							"data" : "curr_balance",
							"sTitle" : "Current"
						}, {
							"data" : "total_balance",
							"sTitle" : "Total"
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
						updateTotalFooter(4, api);
						updateTotalFooter(5, api);
						updateTotalFooter(6, api);
						updateTotalFooter(7, api);
						updateTotalFooter(8, api);
						updateTotalFooter(9, api);
						updateTotalFooter(10, api);
					}
				},
				"aoColumnDefs" : [ {
					"aTargets" : [ 2, 3, 4, 5, 6, 7, 8, 9, 10 ],
					"mRender" : function(data, type, full) {
						return formatNumberInr(data);
					}
				} ]
			});
	$('.loader-class').modal('hide');

	if ($('#mode').val() == 'property') {
		reportdatatable.fnSetColumnVis(1, false);
	} else {
		reportdatatable.fnSetColumnVis(1, true);
	}
}