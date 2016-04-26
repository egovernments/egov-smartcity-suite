/*#-------------------------------------------------------------------------------
	# eGov suite of products aim to improve the internal efficiency,transparency, 
	#    accountability and the service delivery of the government  organizations.
	# 
	#     Copyright (C) <2015>  eGovernments Foundation
	# 
	#     The updated version of eGov suite of products as by eGovernments Foundation 
	#     is available at http://www.egovernments.org
	# 
	#     This program is free software: you can redistribute it and/or modify
	#     it under the terms of the GNU General Public License as published by
	#     the Free Software Foundation, either version 3 of the License, or
	#     any later version.
	# 
	#     This program is distributed in the hope that it will be useful,
	#     but WITHOUT ANY WARRANTY; without even the implied warranty of
	#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	#     GNU General Public License for more details.
	# 
	#     You should have received a copy of the GNU General Public License
	#     along with this program. If not, see http://www.gnu.org/licenses/ or 
	#     http://www.gnu.org/licenses/gpl.html .
	# 
	#     In addition to the terms of the GPL license to be adhered to in using this
	#     program, the following additional terms are to be complied with:
	# 
	# 	1) All versions of this program, verbatim or modified must carry this 
	# 	   Legal Notice.
	# 
	# 	2) Any misrepresentation of the origin of the material is prohibited. It 
	# 	   is required that all modified versions of this material be marked in 
	# 	   reasonable ways as different from the original version.
	# 
	# 	3) This license does not grant any rights to any user of the program 
	# 	   with regards to rights under trademark law for use of the trade names 
	# 	   or trademarks of eGovernments Foundation.
	# 
	#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------*/

jQuery(document).ready(function() {
	$('#dailyCollectionReport-header').hide();
	$('#report-footer').hide();
$('#dailyCollectionReportSearch').click(function(e){
		var fromDate = $("#fromDate").val();
		var toDate = $("#toDate").val(); 
		var mode = $("#mode").val();
		var operator = $("#operator").val();
		var status = $("#status").val();
		var ward = $("#ward").val();
		oTable= $('#dailyCollReport-table');
		$('#dailyCollectionReport-header').show();
        $("#resultDateLabel").html(fromDate+" - "+toDate);	
		oTable.dataTable({
			"sPaginationType": "bootstrap",
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"autoWidth": false,
			"bDestroy": true,
			"oTableTools" : {
				"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
				"aButtons" : [ 
				               {
					             "sExtends": "pdf",
                                 "sPdfMessage": "Daily Collection Report result for dates : "+fromDate+" - "+toDate+"",
                                 "sTitle": "Daily Collection Report",
                                 "sPdfOrientation": "landscape"
				                },
				                {
						             "sExtends": "xls",
	                                 "sPdfMessage": "Daily Collection Report result for dates : "+fromDate+" - "+toDate+"",
	                                 "sTitle": "Daily Collection Report"
					             },{
						             "sExtends": "print",
	                                 "sPdfMessage": "Daily Collection Report result for dates : "+fromDate+" - "+toDate+"",
	                                 "sTitle": "Daily Collection Report"
					               }],
				
			},
			ajax : {
				url : "/ptis/report/dailyCollection/result",
				data : {
					'fromDate' : fromDate,
					'toDate' : toDate,
					'collectionMode': mode,
					'collectionOperator':operator,
					'status':status,
					'ward':ward
				}
			},
			"columns" : [
						  { "data" : "receiptNo" , "title": "Receipt no"},  
						  { "data" : "receiptDate", "title": "Receipt date"},
						  { "data" : "assessmentNumber", "title": "Assessment Number"},
						  { "data" : "ownerName", "title": "Owner Name"},
						  { "data" : "ward" , "title": "ward"},
						  { "data" : "doorNumber", "title": "Door no	"},
						  { "data" : "paidAt", "title": "Paid at"},
						  { "data" : "paymentMode", "title": "Pay mode"},
						  { "data" : "status", "title": "Status"},
						  { "data" : "fromDate", "title": "Paid From date"}, 
						  { "data" : "toDate", "title": "Paid To date"},
						  { "data" : "arrearAmt", "title": "Arrear amount"},
						  { "data" : "currAmt", "title": "Current amount"},
						  { "data" : "totalPenalty", "title": "Total Penalty"},
						  { "data" : "arrearLibCess","title": "Arrear Library cess"},
						  { "data" : "currLibCess","title": "Current Library cess"},
						  { "data" : "totalLibCess","title": "Total library cess"},
						  { "data" : "totalRebate","title": "Rebate"}, 
						  { "data" : "totalCollection", "title": "Total collection"}, 
						  ],
						  "aaSorting": [[2, 'desc']] ,
						  "footerCallback" : function(row, data, start, end, display) {
								var api = this.api(), data;
								if (data.length == 0) {
									jQuery('#report-footer').hide();
								} else {
									jQuery('#report-footer').show(); 
								}
								if (data.length > 0) {
									updateTotalFooter(11, api);
									updateTotalFooter(12, api);
									updateTotalFooter(13, api);
									updateTotalFooter(14, api);
									updateTotalFooter(15, api);
									updateTotalFooter(16, api);
									updateTotalFooter(17, api);
									updateTotalFooter(18, api);
								}
							},
							"aoColumnDefs" : [ {
								"aTargets" : [11,12,13,14,15,16,17,18],
								"mRender" : function(data, type, full) {
									return formatNumberInr(data);    
								}
							} ]		
				});
		e.stopPropagation();
	});
	
});


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

