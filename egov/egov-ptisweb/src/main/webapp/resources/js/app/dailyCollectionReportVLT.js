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
jQuery(document).ready(function() {
	$('#dailyCollectionReport-header').hide();
	$('#report-footer').hide();
	tableContainer=$('#dailyCollReport-table');
$('#dailyCollectionReportSearchVLT').click(function(e){
	var fromDate = $("#fromDate").val();
	var toDate = $("#toDate").val(); 
	$.post("/ptis/report/dailyCollectionVLT", $('#dailyCollectionformVLT').serialize())
	.done(function (searchResult) {
		//console.log(JSON.stringify(searchResult));
		
		$.each(searchResult, function( key, record ) {
			record.totalLibCess=record.currentCess + record.arrearCess;
		});
		$('#dailyCollectionReport-header').show();
		$("#resultDateLabel").html(fromDate+" - "+toDate);	
		$('#report-footer').show();
		tableContainer.dataTable({
			destroy:true,
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"autoWidth": false,
			"oTableTools" : {
				"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
				"aButtons" : [ 
				               {
					             "sExtends": "pdf",
					             "sTitle": jQuery('#pdfTitle').val(),
                                "sPdfMessage": "Daily Collection (VLT) Report, Daily Collection (VLT) Report result for dates : "+fromDate+" - "+toDate+"",
                                "sPdfOrientation": "landscape"
				                },
				                {
						             "sExtends": "xls",
						             "sPdfMessage": "Daily Collection (VLT) Report, Daily Collection (VLT) Report result for dates : "+fromDate+" - "+toDate+"",
						             "sTitle": jQuery('#pdfTitle').val(),
					             },{
						             "sExtends": "print",
						             "sPdfMessage": "Daily Collection (VLT) Report, Daily Collection (VLT) Report result for dates : "+fromDate+" - "+toDate+"",
						             "sTitle": jQuery('#pdfTitle').val(),
					              }],
				
			},
			searchable:true,
			data: searchResult,
			columns: [
			{title: 'Receipt Number', data: 'receiptNumber'},
			{title: 'Receipt Date', data: 'receiptDate',
				render: function (data, type, full) {
					if(data!=null && data!=undefined &&  data!= undefined) {
						var regDateSplit = data.split("T")[0].split("-");		
						return regDateSplit[2] + "/" + regDateSplit[1] + "/" + regDateSplit[0];
					}
					else return "";
		    	}
			},
			{title: 'Assessment Number', data: 'consumerCode'},
			{title: 'Owner Name', data: 'consumerName'},
			{title: 'Ward', data: 'revenueWard'}, 
			{title: 'Paid At', data: 'channel'},
			{title: 'Payment mode', data: 'paymentMode'},
			{title: 'Status', data: 'status'},
			{title: 'Paid From', data: 'installmentFrom'},
			{title: 'Paid To', data: 'installmentTo'},
			{title: 'Arrear Amount', data: 'arrearAmount',
				render: function (data, type, full) {
					return !data?"0":data;
				}
			},
			{title: 'Current Amount', data: 'currentAmount',
				render: function (data, type, full) {
					return !data?"0":data;
				}
			},
			{title: 'Total Penalty', data: 'latePaymentCharges',
				render: function (data, type, full) {
					return !data?"0":data;
				}
			},
			{title: 'Arrear Library Cess', data: 'arrearCess',
				render: function (data, type, full) {
					return !data?"0":data;
				}
			},
			{title: 'Current Library Cess', data: 'currentCess',
				render: function (data, type, full) {
					return !data?"0":data;
				}
			},
			{title: 'Total Library Cess', data: 'totalLibCess',
				
			},
			{title: 'Rebate Amount', data: 'reductionAmount',
				render: function (data, type, full) {
					return !data?"0":data;
				}
			},
			{title: 'Total Collection', data: 'totalAmount'},
			],
			"aaSorting": [[3, 'desc']],
			"footerCallback" : function(row, data, start, end, display) {
				var api = this.api(), data;
				if (data.length == 0) {
					jQuery('#report-footer').hide();
				} else {
					jQuery('#report-footer').show(); 
				}
				if (data.length > 0) {
					updateTotalFooter(10, api);
					updateTotalFooter(11, api);
					updateTotalFooter(12, api);
					updateTotalFooter(13, api);
					updateTotalFooter(14, api);
					updateTotalFooter(15, api);
					updateTotalFooter(16, api);
					updateTotalFooter(17, api);
				}
			},
			"aoColumnDefs" : [ {
				"aTargets" : [10,11,12,13,14,15,16,17], 
				"mRender" : function(data, type, full) {
					return formatNumberInr(data);    
				}
			} ]		
		});
		
	});
	
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
