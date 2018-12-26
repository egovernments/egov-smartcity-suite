/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
	if($("#ulbMaterialValue").val()!="") {
		if($("#ulbMaterialValue").val()=="true") {
			$("div.dropdown-div select").val("YES");
		}
		else 
			$("div.dropdown-div select").val("NO");
	}
	
	$('#itemDescription').css('height','auto');
	
});

$('#search').on("click", function() {
	if($("#materialDemandApplicationSearchForm").valid()) {
		var applicationType=$("#applicationType").val();
		var revenueWard = $("#revenueWard").val();
		var applicationNumber = $("#applicationNumber").val();
		var consumerNumber = $("#consumerNumber").val();
		var fromDate = $("#fromDate").val();
		var toDate = $("#toDate").val();
		
		var result = compareDate(fromDate, toDate);
		if(result==-1) {
			bootbox.alert("From Date can not be greater than To Date")
			return false;
		}
		var searchResultDataTable = jQuery("#application-search-result-table");
		var oTable = searchResultDataTable.DataTable({
			ajax : {
				url : "/wtms/application/material-demand/search",
				type : "POST",
				data : {
					"applicationType" : applicationType,
					"revenueWard" : revenueWard,
					"applicationNumber" : applicationNumber,
					"consumerNumber" : consumerNumber,
					"fromDate" : fromDate,
					"toDate" : toDate
				}
			},
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"autoWidth": false,
			"bDestroy": true,
			columns : [
				{ "data":"applicationNumber", 
	        		"class":"text-center", 
	        		"title":"Application Number",
	        		"render" : function(data, type, row, meta) {
	        			return '<a onclick="openpopup(\'/wtms/application/material-demand/update/'+row.applicationNumber+'\')" href="javascript:void(0);">'+data+'</a>';
	        		}
	        	},
	        	{ "data":"consumerNumber", "class":"text-center", "title":"Consumer Number"},
				{
					"data" : "applicationType",
					"class" : "text-center",
					"title" : "Application Type"
				},
				{
					"title" : "Revenue Ward",
					"class" : "text-center",
					"data" : "revenueWard"
				},
	        	{ "data":"ownerName", "class":"text-center", "title":"Owner Name"},
	        	{ "data":"address", "class":"text-center", "title":"Address"},
	        	{ "data":"approvalDate", 
	        		"class":"text-center",
	        		"title":"Approval Date",
	        		"render" : function(data, type, row, meta) {
	        			var date = data.split("-");
	        			return date[2]+"/"+date[1]+"/"+date[0];
	        		}
	        	},
	        	{ "data":"status", "class":"text-center", "title":"Application Status"}
	        	],
		        columnDefs:[{orderable:false,targets:[0]}],
		        "initComplete": function(settings, json) {
		            reinitialiseDatePicker();
		          }
		});
	}
	else
		return false;
});

function reinitialiseDatePicker() {
	jQuery(".datepicker").datepicker({
		format : "dd/mm/yyyy",
		autoclose : true
	});
}

function compareDate(dt1, dt2) {
	var d1, m1, y1, d2, m2, y2, ret;
	dt1 = dt1.split('/');
	dt2 = dt2.split('/');
	ret = (eval(dt2[2]) > eval(dt1[2])) ? 1
			: (eval(dt2[2]) < eval(dt1[2])) ? -1
					: (eval(dt2[1]) > eval(dt1[1])) ? 1
							: (eval(dt2[1]) < eval(dt1[1])) ? -1															// decimal points
									: (eval(dt2[0]) > eval(dt1[0])) ? 1
											: (eval(dt2[0]) < eval(dt1[0])) ? -1
													: 0;
	return ret;
}

function openpopup(url){
	window.open(url, 'window', 'scrollbar=yes, resizable=yes, height=700, width=800, status=yes');
}

$("#ulbMaterial").on("change", function() {
	if($("#ulbMaterial").val()=="YES") {
		$("#submit").text("Generate Demand");
	}
	else if ($("#ulbMaterial").val()=="NO"){
		$("#submit").text("Save");
	}
});


$("#submit").on("click", function() {
	if($("#materialDemandApplicationUpdateForm").valid())
		return true;
	else
		return false;
});

