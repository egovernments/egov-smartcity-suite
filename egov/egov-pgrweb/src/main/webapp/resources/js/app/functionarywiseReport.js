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

jQuery(document)
		.ready(
				function($) {

					functionaryreportContainer = $("#functionaryReport-table");
					compreportContainer = $("#compReport-table") ;

					$('#report-backbutton').hide(); 
					
					$('#backButton').click(function(e) {
						$('#report-backbutton').hide();
						$('#status').val("");
						$('#functionaryWithStatus').val("");
						$('#usrid').val("");
						$('#functionaryReport-table_wrapper').show();
						$('#compReport-table_wrapper').hide();
						
						callajaxdatatableForCompTypeReport();
						
					});

					$('#functionaryReportSearch').click(function(e) {
						callajaxdatatableForCompTypeReport();
						
						$('#functionaryReport-table_wrapper').show();
						$('#compReport-table_wrapper').hide();
					});

					function callajaxdatatableForCompTypeReport() {
						var startDate = "";
						var endDate = "";
						var usrid = "";
						var when_dateVal = "";
						var compStatus="";
						
						compStatus=$('#functionaryWithStatus').val();
						startDate = $('#start_date').val();
						endDate = $('#end_date').val();

						when_dateVal = $('#when_date').val();
						usrid = $('#usrid').val();
						if ($('#start_date').val() == "")
							startDate = "";

						if ($('#end_date').val() == "")
							endDate = "";

						$('.report-section').removeClass('display-hide');
						$('#report-footer').show();
						$('#report-backbutton').hide();

						functionaryreportContainer
								.dataTable({
									ajax : {
										url : "/pgr/functionaryWiseReport/result",
										data : {
											fromDate : startDate,
											toDate : endDate,
											usrid : usrid,
											complaintDateType : when_dateVal,
											status: ""
										}
									},
									"autoWidth" : false,
									"bDestroy" : true,
									"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
									"aLengthMenu" : [ [ 10, 25, 50, -1 ],
											[ 10, 25, 50, "All" ] ],
									"oTableTools" : {
										"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
										"aButtons" : [ "xls", "pdf", "print" ]
									},
									columns : [
											{
												"data" : "name",
												"sTitle" : "Employee Name"
											}, {
												"mData" : getComplaintTypeIdWithDatakey,
												"render" : function(data, type,row) {
													return '<a href="javascript:void(0);" onclick="setHiddenValueByLink(this,\'Registered\');" data-hiddenele="boundary" data-eleval="'+ data.registered + '" data-complaintname="'+ data.usrid + '">'
															+ data.registered + '</a>';
												},
												"sTitle" : "Registered","sClass" : "text-right"

											}, {
												"mData" : getComplaintTypeIdWithDatakey,
												"render" : function(data, type,row) {
													return '<a href="javascript:void(0);" onclick="setHiddenValueByLink(this,\'Inprocess\');" data-hiddenele="boundary" data-eleval="'+ data.inprocess + '" data-complaintname="'+ data.usrid + '">'
															+ data.inprocess + '</a>';
												},
												"sTitle" : "Inprocess","sClass" : "text-right"
											}, {
												"mData" : getComplaintTypeIdWithDatakey,
												"render" : function(data, type,row) {
													return '<a href="javascript:void(0);" onclick="setHiddenValueByLink(this,\'Completed\');" data-hiddenele="boundary" data-eleval="'+ data.completed + '" data-complaintname="'+ data.usrid + '">'
															+ data.completed + '</a>';
												},
												"sTitle" : "Completed","sClass" : "text-right"
											}, {
												"mData" : getComplaintTypeIdWithDatakey,
												"render" : function(data, type,row) {
													return '<a href="javascript:void(0);" onclick="setHiddenValueByLink(this,\'Rejected\');" data-hiddenele="boundary" data-eleval="'+ data.rejected + '" data-complaintname="'+ data.usrid + '">'
															+ data.rejected + '</a>';
												},
												"sTitle" : "Rejected","sClass" : "text-right"

											}, {
												"mData" : getComplaintTypeIdWithDatakey,
												"render" : function(data, type,row) {
													return '<a href="javascript:void(0);" onclick="setHiddenValueByLink(this,\'Reopened\');" data-hiddenele="boundary" data-eleval="'+ data.reopened + '" data-complaintname="'+ data.usrid + '">'
															+ data.reopened + '</a>';
												},
												"sTitle" : "Reopened","sClass" : "text-right"

											}, {
												"mData" : getComplaintTypeIdWithDatakey,
												"render" : function(data, type,row) {
													return '<a href="javascript:void(0);" onclick="setHiddenValueByLink(this,\'Within SLA\');" data-hiddenele="boundary" data-eleval="'+ data.withinsla + '" data-complaintname="'+ data.usrid + '">'
															+ data.withinsla + '</a>';
												},
												"sTitle" : "Within SLA","sClass" : "text-right"
											},{
												"mData" : getComplaintTypeIdWithDatakey,
												"render" : function(data, type,row) {
													return '<a href="javascript:void(0);" onclick="setHiddenValueByLink(this,\'Beyond SLA\');" data-hiddenele="boundary" data-eleval="'+ data.beyondsla + '" data-complaintname="'+ data.usrid + '">'
															+ data.beyondsla + '</a>';
												},
												"sTitle" : "Beyond SLA","sClass" : "text-right"
											},{
												"data" : "total",
												"sTitle" : "Total","sClass" : "text-right"

											} ],
									"footerCallback" : function(row, data,
											start, end, display) {
										var api = this.api(), data;
										if (data.length == 0) {
											$('#report-footer').hide();
										} else {
											$('#report-footer').show();
										}
										if (data.length > 0) {
											updateTotalFooter(1, api, 'registered');
											updateTotalFooter(2, api, 'inprocess');
											updateTotalFooter(3, api, 'completed');
											updateTotalFooter(4, api, 'rejected');
											updateTotalFooter(5, api, 'reopened');
											updateTotalFooter(6, api, 'withinsla');
											updateTotalFooter(7, api, 'beyondsla');
											updateTotalFooter(8, api, '');

										}
									},
									"aoColumnDefs" : [ {
										"aTargets" : [ 1, 2, 3, 4, 5 , 6 ],
										"mRender" : function(data, type, full) {
											return formatNumberInr(data);
										}
									} ]

								});
					}
					
					function getComplaintTypeIdWithDatakey(row, type, set, meta)
					{
						var data;
						if(meta!=undefined)
						 {	
						if(meta.col===1)
						 data={'usrid':row.usrid, 'registered':row.registered}
						else if(meta.col===2)
							 data={'usrid':row.usrid, 'inprocess':row.inprocess}
						else if(meta.col===3)
							 data={'usrid':row.usrid, 'completed':row.completed}
						else if(meta.col===4)
							 data={'usrid':row.usrid, 'rejected':row.rejected}
						else if(meta.col===5)
							 data={'usrid':row.usrid, 'reopened':row.reopened}
						else if(meta.col===6)
							 data={'usrid':row.usrid, 'withinsla':row.withinsla}
						else if(meta.col===7)
							 data={'usrid':row.usrid, 'beyondsla':row.beyondsla}
							
						 }
						return data;
					}

				});


function callAjaxByComplaintDetail() {
	console.log('calling inside callAjaxByComplaintDetail');
	var startDate = "";
	var endDate = "";
	var usrid = "";
	var when_dateVal = "";
	startDate = $('#start_date').val();
	endDate = $('#end_date').val();

	when_dateVal = $('#when_date').val();
	usrid =$('#usrid').val();
	if ($('#start_date').val() == "")
		startDate = "";

	if ($('#end_date').val() == "")
		endDate = "";

	$('.report-section').removeClass('display-hide');
	$('#functionaryReport-table_wrapper').hide();
	$('#compReport-table_wrapper').show();
	$('#report-backbutton').show();


	compreportContainer
			.dataTable({
				ajax : {
					url : "/pgr/functionaryWiseReport/result",
					data : {
						fromDate : startDate,
						toDate : endDate,
						usrid : usrid,
						complaintDateType : when_dateVal,
						status: $('#status').val()
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
								return '<a href="javascript:void(0);" onclick="window.open(\'/pgr/complaint/view/'
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

						},{
							"data" : "feedback",
							"sTitle" : "Feedback" 
						},{
							"data" : "issla",
							"sTitle" : "Within SLA(Yes or No)" 
						}  ]

			});
}
function updateTotalFooter(colidx, api, key) {
	// Remove the formatting to get integer data for summation
	var intVal = function(i) {
		return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1
				: typeof i === 'number' ? i : 0;
	};

	// Total over all pages
	total = api.column(colidx).data().reduce(function(a, b) {
		var a1 = (a !== null && typeof a === 'object'? a[key]:a);
		var b1 = (b !== null && typeof b === 'object'? b[key]:b);
		return intVal(a1) + intVal(b1);
	});
	
	if(api.column(colidx).data().length === 1)
    {
		if(typeof api.column(colidx).data()[0] === 'object')
		total = api.column(colidx).data()[0][key];
    }
	
	// Total over this page
	pageTotal = api.column(colidx, {
		page : 'current'
	}).data().reduce(function(a, b) {
		var a1 = (a !== null && typeof a === 'object'? a[key]:a);
		var b1 = (b !== null && typeof b === 'object'? b[key]:b);
		return intVal(a1) + intVal(b1);
	}, 0);

	
	// Update footer
	$(api.column(colidx).footer()).html(
			'<b>' + formatNumberInr(pageTotal) + ' (' + formatNumberInr(total) + ')</b>');
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
	if ($(obj).data('eleval')>0) {
		$('input[name=status]').val(paaram);
		$('input[name=usrid]').val($(obj).data('complaintname'));
		callAjaxByComplaintDetail();
	}


}
