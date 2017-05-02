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

var reportdatatable;
jQuery.noConflict();

function populateWard() {
	populatewardId( {
		zoneId : document.getElementById("zoneId").value
	});
	document.getElementById("areaId").options.length = 1;
	jQuery('#areaId').val('-1');
}	

function populateBlock() {
	populateareaId({
		wardId : document.getElementById("wardId").value
	});
}


jQuery(document).ready(function() { 
	
	jQuery('.datepicker').datepicker({
		format: 'dd/mm/yyyy',
		autoclose:true
	});
	
	jQuery(':input').inputmask();
	drillDowntableContainer = jQuery("#tblTitleTransfer");
	jQuery('#btnsearch').click(function(e) {
		dom.get("titleTransferError").style.display='none';
        dom.get("titleTransferError").innerHTML='';
		var fromDate = document.getElementById("fromDate").value;
		var toDate = document.getElementById("toDate").value;
		var finyearSDate=document.getElementById("finYearStartDate").value;
		if ((fromDate == null || fromDate == "" || fromDate == 'DD/MM/YYYY') &&
				(toDate != null && toDate != "" && toDate != 'DD/MM/YYYY')) {
			dom.get("titleTransferError").style.display='';
	        dom.get("titleTransferError").innerHTML='Please Enter From Date';
	        dom.get("fromDate").focus();
			return false;
		}
		
		/*if ((fromDate != null && fromDate != "" && fromDate != 'DD/MM/YYYY')){
			if(compareDate(finyearSDate,fromDate)== -1){
				dom.get("titleTransferError").style.display='';
		        dom.get("titleTransferError").innerHTML='From Date Should be greater or equal to Current Financial Year Date'+" "+finyearSDate;;
				return false;
			}
			
		} */
		if((fromDate != null && fromDate != "" && fromDate != 'DD/MM/YYYY') &&
				(toDate != null && toDate != "" && toDate != 'DD/MM/YYYY')) {
			if(compareDate(fromDate,toDate)== -1){
				dom.get("titleTransferError").style.display='';
		        dom.get("titleTransferError").innerHTML='To Date Should be greater or equal to From Date';
				return false;
			}
		}
		callAjaxForTitleTransfer();
	});
	
});


function callAjaxForTitleTransfer() {
	var zoneId = jQuery('#zoneId').val();
	var wardId = jQuery('#wardId').val();
	var areaId=jQuery('#areaId').val();
	var fromDate=jQuery('#fromDate').val();
	var toDate=jQuery('#toDate').val();
	jQuery('.report-section').removeClass('display-hide');
	
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/ptis/reports/titleTransferRegister-getPropertyList.action",      
					data : {
						zoneId : zoneId,
						wardId : wardId,
						areaId : areaId,
						fromDate : fromDate,
						toDate : toDate 
					}
				},
				"fnRowCallback": function (row, data, index) {
					 if(data.assessmentNo!=""){
	                        jQuery(row).css('font-weight', 'bold'); 
					 }
	             },
				"bDestroy" : true,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ 
					               { "sExtends": "pdf","sTitle": jQuery('#pdfTitle').val(),"sPdfMessage": jQuery('#reportTitle').html() },
					               { "sExtends": "xls", "sTitle": jQuery('#pdfTitle').val(),"sPdfMessage": jQuery('#reportTitle').html()  },
					               { "sExtends": "print", "sTitle": jQuery('#pdfTitle').val(),"sPdfMessage": jQuery('#reportTitle').html() }
					             ]
					/*"aButtons" : [ "xls", "pdf", "print" ]*/
				},
				aaSorting: [],				
				columns : [ {
							"data" : "assessmentNo",
							"sTitle" : "Assessment No"
						}, {
							"data" : "ownerName",
							"sTitle" : "Owner Name"
						}, {
							"data" : "doorNo",
							"sTitle" : "Door No"
						}, {
							"data" : "location",
							"sTitle" : "Location"
						}, {
							"data" : "propertyTax",
							"sTitle" : "Property Tax"
						}, {
							"data" : "oldTitle",
							"sTitle" : "Old Title"
						}, {
							"data" : "changedTitle",
							"sTitle" : "Changed Title"
						}, {
							"data" : "dateOfTransfer",
							"sTitle" : "Date of Transfer"
						}, {
							"data" : "commissionerOrder",
							"sTitle" : "Commissioner Orders"
						}, {
							"data" : "mutationFee",
							"sTitle" : "Mutation Fee"
						}]				
			});
}
