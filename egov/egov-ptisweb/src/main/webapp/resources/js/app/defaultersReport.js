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
jQuery(document).ready(function() { 
	jQuery(':input').inputmask();
	drillDowntableContainer = jQuery("#tblDefaultersReport");
	jQuery('#btnsearch').click(function(e) {
		$("#defaultersError").hide();
		if(jQuery('#limit').val()=="" && (jQuery('#fromAmount').val()=="" && jQuery('#toAmount').val()=="")){
			$("#defaultersError").html('Either From and To Amounts or Top Defaulters is mandatory');
			$("#defaultersError").show();
			return false;
		}
		if(jQuery('#fromAmount').val()=="" && jQuery('#toAmount').val()!=""){
			$("#defaultersError").html('Please enter the From Amount');
			$("#defaultersError").show();
			return false;
		}
		if(jQuery('#fromAmount').val()!="" && jQuery('#toAmount').val()==""){
			$("#defaultersError").html('Please enter the To Amount');
			$("#defaultersError").show();
			return false;
		}
		callAjaxForDefaultersReport();
	});
});


function callAjaxForDefaultersReport() {
	var category;
	var wardId = jQuery('#wardId').val();
	var fromAmount=jQuery('#fromAmount').val();
	var toAmount=jQuery('#toAmount').val();
	var limit=jQuery('#limit').val();
	var noofyr=jQuery('#noofyr').val();
	var proptype=jQuery('#mode').val();
	jQuery('.report-section').removeClass('display-hide');
	
	if(jQuery('#mode').val() == 'VLT'){
		categoryVal = "VAC_LAND";
	} else {
		categoryVal = jQuery('#ownershipCategory').val();
	}
	
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/ptis/report/defaultersReport/result",      
					data : {
						wardId : wardId,
						fromAmount : fromAmount,
						toAmount : toAmount,
						limit : limit,
						category : categoryVal,
						noofyr : noofyr,
						proptype : proptype
					}
				},
				"bDestroy" : true,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [
					               {
							            "sExtends": "pdf",
							            "sTitle": jQuery('#pdfTitle').val(),
							            "sPdfMessage": jQuery('#reportTitle').html()
					                },
					                {
							            "sExtends": "xls",
							            "sTitle": jQuery('#pdfTitle').val(),
							            "sPdfMessage": jQuery('#reportTitle').html()
						             },{
							            "sExtends": "print",
							            "sTitle": jQuery('#pdfTitle').val(),
							            "sPdfMessage": jQuery('#reportTitle').html()
						             }
					             ]
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
							"data" : "arrearsFrmInstallment",
							"sTitle" : "Arrears From Installment" 
						}, {
							"data" : "arrearsToInstallment",
							"sTitle" : "Arrears To Installment"
						}, {
							"data" : "arrearsDue",
							"sTitle" : "Arrears Amount"
						}, {
							"data" : "arrearsPenaltyDue",
							"sTitle" : "Arrears Penalty"
						}, {
							"data" : "currentDue",
							"sTitle" : "Current Amount"
						},  {
							"data" : "currentPenaltyDue",
							"sTitle" : "Current Penalty"
						}, {
							"data" : "totalDue",
							"sTitle" : "Total"
						} ]				
			});
}