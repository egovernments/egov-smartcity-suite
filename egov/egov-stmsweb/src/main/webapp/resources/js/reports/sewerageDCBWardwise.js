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
$(document).ready(function() {
	
	var isSubmit = false;
	$('#search').click(function(){
		if(isSubmit){
			return true;
		}
			submitButton();
			return true;
	});
	 
	function submitButton() {
	var drillDowntableContainer = $('#tbldcbdrilldown-report');
	$('.report-section').removeClass('display-hide');
	$("#report-footer").show();
	var wardsList = $('#ward').val();
	var temp=null;
	if(wardsList!=null && wardsList!="undefined"){
		if(wardsList.length>0){
			for(var i=0;i<wardsList.length;i++){
				if(temp===null){
					temp=wardsList[i];
				}
				else{
					temp=temp+"~"+wardsList[i];
				}
			}
		}
	}
	drillDowntableContainer.dataTable({
		type : 'POST',
		responsive : true,
		destroy : true,
		ajax : {
			url : "/stms/reports/dcbReportWardwiseList",
			beforeSend : function() {
				$('.loader-class').modal('show', {
					backdrop : 'static'
				});
			},
			data : {
				'propertyType' : $("#propertyType").val(),
				'mode' : temp
			},
			complete : function() {
				$('.loader-class').modal('hide');
			},
			dataSrc: function ( json ) {  
				jQuery('.loader-class').modal('hide'); 
				return json.data;
			}       
		},
		
		"bDestroy" : true,
		"autoWidth" : false,
		"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
		"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
		"oTableTools" : {
			"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
			"aButtons" : [ 
			               {
				             "sExtends": "pdf",
		                     "sPdfMessage": "",
		                     "sTitle": "Sewerage Tax Wardwise DCB Report",
		                     "sPdfOrientation": "landscape",
		                     "mColumns": [0, 1, 2, 3, 4, 5, 6, 7, 8,9,10]	 
			                },
			                {
					             "sExtends": "xls",
		                         "sTitle": "Sewerage Tax Wardwise DCB Report",
		                         "mColumns": [0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10]
				             },
				             {
					             "sExtends": "print",
		                         "sTitle": "Sewerage Tax Wardwise DCB Report",
		                         "mColumns": [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
				             }]
		},
		columns : [
		            {
		            	"data" : 	function(row, type, meta, set){
		            		return {
		            			name : row.revenueWard,
		            			id : row.wardId,
		            			propertyType: row.propertyType,
		            		};
		            	},	
		            
		            	"render" : function(data, type, meta, row){
							return '<a href="javascript:void(0);" onclick="wardWiseConnectionsDCB('+data.id+');">'+data.name+'</a> ';
		            	},
		            	"title" : "Ward"
		            		
		            },
		           {
						"data" : "noofassessments",
						"sTitle" : "No.of Assessments",
						"className": "text-center"
					},{
						"data" : "arr_demand",
						"sTitle" : "Arrears",
						"className": "text-right"
					},{
						"data" : "curr_demand",
						"sTitle" : "Current",
						"className": "text-right"
					}, {
						"data" : "total_demand",
						"sTitle" : "Total",
						"className": "text-right"
					}, {
						"data" : "arr_collection",
						"sTitle" : "Arrears",
						"className": "text-right"
					}, {
						"data" : "curr_collection",
						"sTitle" : "Current",
						"className": "text-right"
					}, {
						"data" : "total_collection",
						"sTitle" : "Total",
						"className": "text-right"
					}, {
						"data" : "arr_balance",
						"sTitle" : "Arrears",
						"className": "text-right"
					}, {
						"data" : "curr_balance",
						"sTitle" : "Current",
						"className": "text-right"
					}, {
						"data" : "total_balance",
						"sTitle" : "Total",
						"className": "text-right"
					} ],
					
					"footerCallback" : function(row, data, start, end, display) {
						var api = this.api(), data;
						if (data.length == 0) {
							$('#report-footer').hide();
						} else {
							$('#report-footer').show();
						}
						if (data.length > 0) {
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
						"aTargets" : [ 2, 3, 4, 5, 6, 7, 8, 9, 10 ],
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
		$(api.column(colidx).footer()).html(
				formatNumberInr(pageTotal) + ' (' + formatNumberInr(total) + ')');
	}
	
	
	// inr formatting number
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
});

function wardWiseConnectionsDCB(id){
	var propertyType = document.getElementById("propertyType").value;
	if(propertyType!=null)
		{
		id=id+"~"+propertyType;
		}
	window.open("/stms/reports/dcbViewWardConnections/"+id,'_blank',"width=800, height=600, scrollbars=yes");
	
}

$('#sewerageWardDCBTable').dataTable({
	"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
	"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
	"autoWidth": false,
	"bDestroy": true,
	"bProcessing" : true,
	"oTableTools" : {
		"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
		"aButtons" : [ 
		               {
			             "sExtends": "pdf",
	                     "sPdfMessage": "",
	                     "sTitle": "Sewerage Connection Report",
	                     "sPdfOrientation": "landscape",
	                     "mColumns": [0, 1, 2, 3, 4, 5, 6, 7, 8,9,10]	 
		                },
		                {
				             "sExtends": "xls",
	                         "sPdfMessage": "Sewerage Connection Report",
	                         "sTitle": "Sewerage Connection Report",
	                         "mColumns": [0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10]
			             },
			             {
				             "sExtends": "print",
	                         "sTitle": "Sewerage Connection Report",
	                         "mColumns": [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
			             }]
		
	},
});


