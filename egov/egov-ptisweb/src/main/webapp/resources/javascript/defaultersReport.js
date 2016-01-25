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

var reportdatatable;
jQuery.noConflict();

jQuery(document).ready(function() { 
	jQuery(':input').inputmask();
	drillDowntableContainer = jQuery("#tblDefaultersReport");
	jQuery('#btnsearch').click(function(e) {
		dom.get("defaultersReportError").style.display='none';
        dom.get("defaultersReportError").innerHTML='';
		var fromDemand = document.getElementById("fromDemand").value;
		var toDemand = document.getElementById("toDemand").value;
		var limit = document.getElementById("limit").value;
		
		if (((fromDemand == null || fromDemand == "") &&
				(toDemand == null || toDemand == "")) && limit == -1 ) {
			dom.get("defaultersReportError").style.display='';
	        dom.get("defaultersReportError").innerHTML='Either From and To Amounts or Top Defaulters is mandatory';
			return false;
		}
		
		if ((fromDemand == null || fromDemand == "") &&
				(toDemand != null && toDemand != "")) {
			dom.get("defaultersReportError").style.display='';
	        dom.get("defaultersReportError").innerHTML='Please Enter From Amount';
	        dom.get("fromDemand").focus();
			return false;
		}
		if ((fromDemand != null || fromDemand != "") &&
				(toDemand == null && toDemand == "")) {
			dom.get("defaultersReportError").style.display='';
	        dom.get("defaultersReportError").innerHTML='Please Enter To Amount';
	        dom.get("toDemand").focus();
			return false;
		}
		callAjaxForDefaultersReport();
	});
	
});


function callAjaxForDefaultersReport() {
	var wardId = jQuery('#wardId').val();
	var fromDemand=jQuery('#fromDemand').val();
	var toDemand=jQuery('#toDemand').val();
	var limit=jQuery('#limit').val();
	
	jQuery('.report-section').removeClass('display-hide');
	
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/ptis/reports/defaultersReport-getDefaultersList.action",      
					data : {
						wardId : wardId,
						fromDemand : fromDemand,
						toDemand : toDemand,
						limit : limit
					}
				},
				"sPaginationType" : "bootstrap",
				"bDestroy" : true,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ "xls", "pdf", "print" ]
				},
				aaSorting: [],				
				columns : [{
							"data" : "slNo",
							"sTitle" : "Sl No"
						}, {
							"data" : "assessmentNo",
							"sTitle" : "Assessment No"
						}, {
							"data" : "ownerName",
							"sTitle" : "Owner Name"
						}, {
							"data" : "wardName",
							"sTitle" : "Revenue Ward"
						}, {
							"data" : "houseNo",
							"sTitle" : "Door No"
						}, {
							"data" : "locality",
							"sTitle" : "Locality"
						}, {
							"data" : "mobileNumber",
							"sTitle" : "Mobile Number"
						}, {
							"data" : "arrearsDue",
							"sTitle" : "Arrears Amount"
						},  {
							"data" : "currentDue",
							"sTitle" : "Current Amount"
						}, {
							"data" : "totalDue",
							"sTitle" : "Total"
						}]				
			});
}
