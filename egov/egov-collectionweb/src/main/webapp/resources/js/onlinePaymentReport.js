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

jQuery(document).ready(function() {
	$('#onlinePayment-header').hide();
$('#onlinePaymentReportSearch').click(function(e){
		var ulbname = $("#ulbname").val();
		var districtname = $("#districtname").val();
		var todate = $("#todate").val();
		var fromdate = $("#fromdate").val();
		var transid = $("#transid").val();
		oTable= $('#onlinePaymentReport-table');
		$('#onlinePayment-header').show();
		
		console.log('district name --->'+districtname);
		
		var resultInfo="Online Payment Report Result for District: "+ (districtname?districtname:"All Districts") +" and Ulb: " +(ulbname?ulbname:"All Ulbs")+ "  and from Date: " + (fromdate?fromdate:"All Dates") + " and to Date: " + (todate?todate:"All Dates") +" and Transaction id: " + (transid?transid: "All transaction ids");
		
		$('#resultinfo').html(resultInfo);
		
		oTable.dataTable({
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"autoWidth": false,
			"bDestroy": true,
			"oTableTools" : {
				"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
				"aButtons" : [ 
				               {
					             "sExtends": "pdf",
					             "sPdfMessage": resultInfo, 
		                         "sTitle": "Online Payment Report",
		                         "sPdfOrientation": "landscape"
		                        	 
				                },
				                {
						             "sExtends": "xls",
						             "sPdfMessage": resultInfo,
						             "sTitle": "Online Payment Report"
					             },
					             {
						             "sExtends": "print",
						             "sPdfMessage": resultInfo,
		                             "sTitle": "Online Payment Report"
					       }]
				
			},
			ajax : {
				url : "/collection/citizen/onlinePaymentReport/result",
				data : {
					'districtname' : districtname,
					'ulbname' : ulbname,
					'fromdate' : fromdate,
					'todate' : todate,
					'transid' : transid
				}
			},
			"columns" : [
			              { "data" : "payeename", "title": "Applicant name"},
			              { "data" : "receiptnumber", "title": "Application no"},
			              { "data" : "transactionnumber", "title":  "Payment ID"},
			              { "data" : "referenceid", "title": "Reference ID"},
						  { "data" : "ulbname" , "title": "Name of the ULB"},  
						  { "data" : "districtname", "title": "Name of District"},
						  { "data" : "onlineservicename", "title": "Payment Gateway"},
						  { "data" : "totalamount", "title": "Pay Amount"},
						  { "data" : "transactiondate", "title": "Payment date"},
						  { "data" : "status", "title": "Payment Status"}
						  ],
						  "aaSorting": [],
						  "aoColumnDefs": [
						                   { "sClass": "text-right", "aTargets": [ 7 ] },
						                   { "sClass": "text-center", "aTargets": [ 6 ] }
						                 ]
				}
				
		);
		
		e.stopPropagation();
	});

$('#districtname').change(function(){
	console.log("came on change of districtname "+$('#districtname').val());
	$.ajax({
		url: "/collection/citizen/getUlbNamesByDistrict",
		type: "GET",
		data: {
			districtname : jQuery('#districtname').val()
		},
		cache: false,
		dataType: "json",
		success: function (response) {
			console.log("success"+response);
			jQuery('#ulbname').html("");
			jQuery('#ulbname').append("<option value=''>Select</option>");
			jQuery.each(response, function(index, value) {
				jQuery('#ulbname').append($('<option>').text(value.ulbname).attr('value', value.ulbname));
			});
		}, 
		error: function (response) {
			jQuery('#ulbname').html("");
			jQuery('#ulbname').append("<option value=''>Select</option>");
			console.log("failed");
		}
	});
});
});

