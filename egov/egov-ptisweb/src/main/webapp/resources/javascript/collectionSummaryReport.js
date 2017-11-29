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
jQuery.noConflict();

function populateWard() {
	populatewardId( {
		zoneId : document.getElementById("zoneId").value
	});
	document.getElementById("blockId").options.length = 1;
	jQuery('#blockId').val('-1');
}	

jQuery(document).ready(function() { 
	
	jQuery('.datepicker').datepicker({
		format: 'dd/mm/yyyy',
		autoclose:true
	});
	
	
	jQuery(':input').inputmask();
	drillDowntableContainer = jQuery("#tblCollectionSummary");
	jQuery('#btnsearch').click(function(e) {
		dom.get("colSummaryError").style.display='none';
        dom.get("colSummaryError").innerHTML='';
		var fromDate = document.getElementById("fromDate").value;
		var toDate = document.getElementById("toDate").value;
		var finyearSDate=document.getElementById("finYearStartDate").value;

		if (fromDate == null || fromDate == "" || fromDate == 'DD/MM/YYYY') {
			dom.get("colSummaryError").style.display='';
	        dom.get("colSummaryError").innerHTML='From Date is Mandatory';
	        dom.get("fromDate").focus();
			return false; 
		}
		if (toDate == null || toDate == "" || toDate == 'DD/MM/YYYY') {
			dom.get("colSummaryError").style.display='';
	        dom.get("colSummaryError").innerHTML='To Date is Mandatory';
	        dom.get("toDate").focus();
			return false;
		}
		if ((fromDate != null && fromDate != "" && fromDate != 'DD/MM/YYYY')){
			if(compareDate(finyearSDate,fromDate)== -1){
				dom.get("colSummaryError").style.display='';
		        dom.get("colSummaryError").innerHTML='From Date Should be greater or equal to Current Financial Year Date'+" "+finyearSDate;
				return false;
			}
			
		} 
		if((fromDate != null && fromDate != "" && fromDate != 'DD/MM/YYYY') &&
				(toDate != null && toDate != "" && toDate != 'DD/MM/YYYY')) {
			if(compareDate(fromDate,toDate)== -1){
				dom.get("colSummaryError").style.display='';
		        dom.get("colSummaryError").innerHTML='To Date Should be greater or equal to From Date';
				return false;
			}
		}
		callAjaxForCollectionSummary();
	});
	
});

function callAjaxForCollectionSummary() {
	var modeval = jQuery('#mode').val();
	var fromDate=jQuery('#fromDate').val();
	var toDate=jQuery('#toDate').val();
	var collMode=jQuery('#collMode').val();
	var transMode=jQuery('#transMode').val();
	var boundaryId;
	var zoneId;
	var wardId;
	var blockId;
	var propTypeCategoryId;
	if(modeval!='usageWise'){
		boundaryId=jQuery('#boundaryId').val();
	} else {
		zoneId = jQuery('#zoneId').val();
		wardId = jQuery('#wardId').val();
		blockId=jQuery('#blockId').val();
		propTypeCategoryId=jQuery('#propTypeCategoryId').val();
	}
	jQuery('.report-section').removeClass('display-hide');
	jQuery('#report-footer').show();
	
	
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/ptis/reports/collectionSummaryReport-list.action",        
					data : {
						zoneId : zoneId,
						wardId : wardId,
						blockId : blockId,
						fromDate : fromDate, 
						toDate : toDate,
						collMode : collMode,
						transMode : transMode,
						mode : modeval,
						boundaryId : boundaryId, 
						propTypeCategoryId : propTypeCategoryId  
					}
				},
				"bDestroy" : true,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					/*"aButtons" : [ 
					               {
						             "sExtends": "pdf"
					                },
					                {
							             "sExtends": "xls",
		                                 "fnClick": function ( nButton, oConfig, oFlash ) {
	                            	    	 reCalculateTotalFooterWhenExport('tblCollectionSummary');
	                            		     this.fnSetText(oFlash, this.fnGetTableData(oConfig));
	                            		 }
						             },{
							             "sExtends": "print"
						               }]*/
				"aButtons" : [ 
					               { "sExtends": "pdf","sTitle": jQuery('#pdfTitle').val(),"sPdfMessage": jQuery('#reportTitle').html() },
					               { "sExtends": "xls", "sTitle": jQuery('#pdfTitle').val(),"sPdfMessage": jQuery('#reportTitle').html(), 
					            	   "fnClick": function ( nButton, oConfig, oFlash ) {
	                            	    	 reCalculateTotalFooterWhenExport('tblCollectionSummary');
	                            		     this.fnSetText(oFlash, this.fnGetTableData(oConfig));
	                            		 }},
					               { "sExtends": "print", "sTitle": jQuery('#pdfTitle').val(),"sPdfMessage": jQuery('#reportTitle').html() }
					             ]
						               
				},
					
				aaSorting: [],				
				columns : [ {						
								"data" : function(row, type, set, meta){
									if(modeval!='usageWise'){
										return row.boundaryName;
									}
									else {
										return row.propertyType;
									}
								}
						},
						{
							"data" : "arrearTaxAmount",
							"sTitle" : "Arrear Tax Amount"
						}, {
							"data" : "arrearLibraryCess",
							"sTitle" : "Arrear LibraryCess Amount"
						}, {
							"data" : "arrearTotal",
							"sTitle" : "Arrear Total"
						}, {
							"data" : "taxAmount",
							"sTitle" : "Current Tax Amount"
						}, {
							"data" : "libraryCess",
							"sTitle" : "Current LibraryCess Amount"
						}, {
							"data" : "currentTotal",
							"sTitle" : "Current Total"
						}, {
							"data" : "penalty",
							"sTitle" : "Penalty"
						}, {
							"data" : "arrearPenalty",
							"sTitle" : "Arrear Penalty Amount"
						}, {
							"data" : "penaltyTotal",
							"sTitle" : "Penalty Total"
						}, {
							"data" : "total",
							"sTitle" : "Grand Total"
						}],
						"footerCallback" : function(row, data, start, end, display) {
							var api = this.api(), data;
							if (data.length == 0) {
								jQuery('#report-footer').hide();
							} else {
								jQuery('#report-footer').show();
							}
							if (data.length > 0) {
								updateTotalFooter(1, api);
								updateTotalFooter(2, api);
								updateTotalFooter(3, api);
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
							"aTargets" : [ 1, 2, 3,4,5,6,7,8,9,10],
							"mRender" : function(data, type, full) {
								return formatNumberInr(data);    
							}
						} ]				
			});
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