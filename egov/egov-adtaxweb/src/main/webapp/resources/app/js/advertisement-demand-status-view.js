/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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

$(document).ready(function(){
	$('.report-section').removeClass('display-hide');
	var currentDate = new Date();
	var day = currentDate.getDate();
	var month = currentDate.getMonth() + 1;
	var year = currentDate.getFullYear();
	var currDate = day + "-" + month + "-" + year;
	var financialyear = $("#hdnfinancialyr").val();
	$("#resultTable").dataTable({
		ajax : {
			url : "/adtax/advertisement/demand-status-records-view/",
			type : "POST",
			beforeSend : function(){
				$('.loader-class').modal(
							'show',
							{
								backdrop : 'static'
							});
			},
			data : {
				'financialyear' : financialyear
			},
			complete : function(){
				$('.loader-class').modal('hide');
			}
		},
			"bDestroy" : true,
			"autoWidth": false,
			"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
			"oTableTools" : {
				"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
				"aButtons" : [ {
		             "sExtends": "pdf",
	                 "sPdfMessage": "Report Generated On "+ currDate + "",
	                 "sTitle": "Sewerage Demand Generation Details Report",
	                 "sPdfOrientation": "landscape"
	                },
	                {
			             "sExtends": "xls",
	                     "sPdfMessage": "Sewerage Demand Generation Details Report",
	                     "sTitle": "Sewerage Demand Generation Details Report"
		             },
		             {
			             "sExtends": "print",
	                     "sTitle": "Sewerage Demand Generation Details Report"
		             } ]
			},
			aaSorting : [],
			columns : [
						{ "data" : "financialyear",
						  "sClass" : "text-left"
						},
						{
							"data" : "advertisementnumber",
							"sClass" : "text-left"
						},
						{
							"data" : "status",
							"sClass" : "text-left"
						},
						{
							"data" : "details",
							"sClass" : "text-left"
						}
							
				
			]
	});
	
});