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

var jsonObjForCheckedinApplication = [];


jQuery(document).ready(function() {
	$('.updation').hide();
	 
	$('#search-result-table').on( 'draw.dt', function () {
		console.log("draw event triggered");
		var all = $('#search-result-table')
		.find('> tbody > tr > td:first-child > input[type="checkbox"]');
		
		$('#global_checkbox').prop('checked', all.prop('checked'));
			
//		all.each(function(value){
//			$(this)..prop('checked')
//		});
		
	} );
	
});

$('#metered-search-result-table').on('click', 'td button', function() {
	var row = jQuery(this).closest('tr');
	var myObj = {};
	var $tr = jQuery(this).closest('tr');
	
	var table = $("#metered-search-result-table").DataTable();
	var rowData = table.row( $(this).parents('tr') ).data();
	
		$('.meterdtl-update').modal('show', {backdrop : 'static'});
		$(".display-err-msg").hide();
		$(".display-success-msg").hide();
		var resultObject = {};
		var jsonObject = [];

		resultObject = {
				"applicationNumber" : "" +rowData['applicationNumber'], 
				"pipeSizeId" : ""+rowData['pipesizeid']
				}
		
		jsonObject.push(resultObject);
		$(".applnNumber").html(rowData['applicationNumber']);
		var obj = {"executeWaterApplicationDetails" : jsonObject}
		var o = JSON.stringify(obj);
		var result = [];
		$("#meterMake").find('option:gt(0)').remove();
		$.ajax({
			
			url : "/wtms/application/execute-update/search-meter",
			type : "POST",
			dataType : "json",
			data : o,
			cache : false,
			contentType : "application/json ; charset=utf-8",
			success : function(data) {
				
				$.each(data, function(i) {
						var obj = data[i];
						var o = this;
							obj['id'] = i;
							obj['text'] = obj.meterMake;
					result.push(obj);
				});
				
				$.each(result, function(i){
					$("#meterMake").append($('<option>').text(result[i].text).attr('value', result[i].text));
				});
			}
			
		});
	

});


$('#search').on('click', function() {
	
		if($('#executeWaterApplicationForm').valid()) {
			
		var applicationNumber = $('#applicationNumber').val();
		var consumerNumber = $('#consumerNumber').val();
		var fromDate = $('#fromDate').val();
		var toDate = $('#toDate').val();
		var result = compareDate (fromDate, toDate);
		if(result == -1) {
			bootbox.alert(" From Date can not be greater than To Date");
			return false;
		}
		var applicationType = $('#applicationType').val();
		var revenueWard = $('#revenueWard').val();
		var searchResultDataTable = jQuery("#search-result-table");
		oTable = searchResultDataTable.DataTable({
			ajax : {
				url : "/wtms/application/execute-update/search",
				type : "POST",
				data : { 
						'applicationNumber' : applicationNumber,
						'consumerNumber' : consumerNumber,
						'fromDate' : fromDate,
						'toDate' : toDate,
						'applicationType' : applicationType,
						'revenueWard' : revenueWard
					}
			},
			
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"autoWidth": false,
			"bDestroy": true,
	        columns : [
	        	{  
	        	  "class" : "text-center",
	        	  "data" : "id",
	        	  "title" : '<input type="checkbox" id="global_checkbox" class="checkbox" onclick="checkbox_change(this);"/>',
	        	  "render" : function(data, type, full, meta) {
	        		  return '<input type="checkbox"  class="check_box" name="id" onclick="each_checkbox_change(this);" value="'
						+ $('<div/>').text(data).html()
						+ '">';
	        	  }
	        	},
	        	{ "data":"applicationNumber", 
	        		"class":"text-center", 
	        		"title":"Application Number",
	        		"render" : function(data, type, row, meta) {
	        			return '<a onclick="openpopup(\'/wtms/application/view/'+row.applicationNumber+'\')" href="javascript:void(0);">'+data+'</a>';
	        		}
	        	},
	        	{ "data":"pipesizeid", "class":"text-center", "visible":false, "title":"Pipe Size"},
	        	{ "data":"consumerNumber", "class":"text-center", "title":"Consumer Number"},
	        	{ "data":"ownerName", "class":"text-center", "title":"Owner Name"},
	        	{ "data":"address", "class":"text-center", "title":"Address"},
	        	{ "data":"applicationType", "class":"text-center", "title":"Application Type"},
	        	{ "data":"status", "class":"text-center", "title":"Application Status"},
	        	{ "data":"approvalDate",
	        		"class":"text-center",
	        		"title":"Approval Date",
	        		"render" : function(data, type, row, meta) {
	        			var date = data.split("-");
	        			return date[2]+"/"+date[1]+"/"+date[0];
	        		}
	        			
	        	},
	        	{ "data":"revenueWard", "class":"text-center", "title":"Ward"},
	        	{ "data":"executionDate",
	        		"class":"text-center", 
	        		"title":"Connection Execution Date",
	        		"render" : function(data, type, full, meta) {
	        			return '<input class="form-control datepicker execDate" data-date-end-date="0d" id="executiondate" readonly="readonly"/>';
	        		}
	        	 
	        	}
	        	
	        ],
	        columnDefs:[{orderable:false,targets:[0]}],
		});
		
		$('.updation').show();
		
}
else 
	return false;
});

$('#search-result-table').on('draw.dt', function () {
	jQuery(".datepicker").datepicker({
		format : "dd/mm/yyyy",
		autoclose : true
	});
});

 
function checkbox_change(obj) {

	var isError = false;
	var all = $('#search-result-table').find(
			'> tbody > tr > td:first-child > input[type="checkbox"]')
	var tempJson = [];

	if ($(obj).is(':checked')) {

		all
				.each(function(value) {

					var myObj = {};
					var parentTR = $(this).parent().parent();
					var executionDate = parentTR.find('.execDate').val();
					var currentDate = formatCurrentDate();

					if (executionDate == "" || executionDate == undefined) {
						bootbox
								.alert("Execution date is not entered. Please check once.");
						isError = true;
						return false;
					}

					if (compareDate(executionDate, currentDate) == -1) {
						bootbox
								.alert("Execution date can not be greater than current date : "
										+ currentDate);
						isError = true;
						return false;
					}

					myObj = {
						"id" : "" + parentTR.find('.check_box').val(),
						"executionDate" : "" + executionDate,
						"applicationNumber" : ""
								+ parentTR.find("td:eq(1)")[0].innerText,
						"pipeSizeId" : ""
								+ parentTR.find("td:eq(2)")[0].innerText
					};

					tempJson.push(myObj);
				});

		if (!isError) {

			all.prop('checked', true);
			jsonObjForCheckedinApplication = jsonObjForCheckedinApplication
					.concat(tempJson);
		}else{
			$(obj).prop('checked', false);
		}
	} else {
		
		
		all
		.each(function(value) {

			var myObj = {};
			var parentTR = $(this).parent().parent();
			var executionDate = parentTR.find('.execDate').val();

			
			myObj = {
				"id" : "" + parentTR.find('.check_box').val(),
				"executionDate" : "" + executionDate,
				"applicationNumber" : ""
						+ parentTR.find("td:eq(1)")[0].innerText,
				"pipeSizeId" : ""
						+ parentTR.find("td:eq(2)")[0].innerText
			};

			tempJson.push(myObj);
		});

		
		all.prop('checked', false);
		jsonObjForCheckedinApplication = $.grep(jsonObjForCheckedinApplication,
				function(e) {

					var found = true;

					tempJson.forEach(function(v) {

						if (e.applicationNumber == v.applicationNumber)
							found = false;

					});

				});
	}

}

function each_checkbox_change(obj){
	
	var myObj= {};
	var parentTR = jQuery(obj).parent().parent();
	var executionDate  = parentTR.find('.execDate').val();
	var currentDate = formatCurrentDate();
	
	if(executionDate=="" || executionDate==undefined) {
		bootbox.alert("Execution date is not entered. Please check once.");
		$(obj).prop('checked', false);
		return false;
	}
	
	if(compareDate(executionDate, currentDate)==-1) {
	    bootbox.alert("Execution date can not be greater than current date : "+currentDate);
	    $(obj).prop('checked', false);
	    return false;
	 }
	
	myObj = { 	
				"id" : ""+parentTR.find('.check_box').val(),
				"executionDate" : "" + executionDate,
				"applicationNumber" : "" +parentTR.find("td:eq(1)")[0].innerText,
				"pipeSizeId" : "" +parentTR.find("td:eq(2)")[0].innerText
			};
	
	if($(obj).is(':checked')) {
		jsonObjForCheckedinApplication.push(myObj)
	} else {
		jsonObjForCheckedinApplication = $.grep(jsonObjForCheckedinApplication, function(e){ 
		     return e.applicationNumber != parentTR.find("td:eq(1)")[0].innerText; 
		});
	}
}

$('#update').on('click', function(){
	
	var jsonObj = jsonObjForCheckedinApplication;
	var myObj = {};
	var isError = false;
	
		
		var obj = {"executeWaterApplicationDetails" : jsonObj};
		var o = JSON.stringify(obj);
		
		$.ajax({
			url : "/wtms/application/execute-update/result",
			type : "POST",
			beforeSend : function() {
				$('.loader-class').modal('show', {
					backdrop : 'static'
				});
			},
			data : o,
			complete : function() {
				$('.loader-class').modal('hide');
			},
			cache : false,
			contentType : "application/json ; charset=utf-8",
			success : function(response) {
				if(response=="Success") {
					bootbox.alert(
						"The water connection applications executed successfully", function(){
							$("#search").trigger('click');
						});
					return false;
				}
				else if (response == "EmptyList") {
					bootbox.alert("Please select atleast one application to execute connection");
					return false;
				}
				else if (response == "DateValidationFailed") {
					bootbox.alert("Please check  one of selected application connection execution date is entered wrongly, connection execution date must be greater than the approval date.");
					return false;
				}
				else if (response == "UpdateExecutionFailed") {
					bootbox.alert("Please enter connection execution date of selected applications");
					return false;
				} else if (response == "WaterRatesNotDefined") {
					bootbox.alert(" Active Monthly Water Rates are not defined for the selected application");
					return false;
				}
				else if (response == "InvalidExecutionDate") {
					bootbox.alert(" Execution date can not be greater than current date");
					return false;
				}
				else if(response.includes("Application Process Time not defined")) {
					bootbox.alert(" Application process time is not defined for this application type and category. Please define using create application process time master and then update execution date.")
					return false;
				}
				
				else if(response.includes("MaterialsFlaggingNotDone")) {
					var str = response.split(/[~ ]+/).pop();
					var res = str.slice(0, str.length - 1) ;
					bootbox.alert("Non-Metered and Non-BPL Consumers "+res+" have not been flagged on whether ULB has supplied Material or Citizen has Procured on his own. Please flag and then continue to execute.")
					return false;
				}
				else {
					bootbox.alert("water connection update failed");
					return false;
				}
			},
			error : function(response) {
				bootbox.alert("water connection update failed");
			}
			
		})
		
	});
	
	
function formatCurrentDate () {
	var currentDate = new Date();
	var date = currentDate.getDate();
	var month = currentDate.getMonth() + 1;
	var year = currentDate.getFullYear();
	
	if(date<10) {
		date = '0' + date;
	}
	
	if(month<10) {
		month = '0' + month; 
	}
	
	return date+'/'+month+'/'+year;	
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

function openpopup(url) {
	window.open(url, 'window','scrollbars=yes, resizable=yes, height=700,width=800,status=yes');
}


$('#searchApplication').on('click', function() {
	
	if($('#executeMeteredWaterApplicationForm').valid()) {
		
	var applicationNumber = $('#applicationNumber').val();
	var consumerNumber = $('#consumerNumber').val();
	var fromDate = $('#fromDate').val();
	var toDate = $('#toDate').val();
	var result = compareDate (fromDate, toDate);
	if(result == -1) {
		bootbox.alert(" From Date can not be greater than To Date");
		return false;
	}
	var applicationType = $('#applicationType').val();
	var revenueWard = $('#revenueWard').val();
	var searchResultDataTable = jQuery("#metered-search-result-table");
	oTable = searchResultDataTable.DataTable({
		ajax : {
			url : "/wtms/application/execute-update/search-form",
			type : "POST",
			data : { 
					'applicationNumber' : applicationNumber,
					'consumerNumber' : consumerNumber,
					'fromDate' : fromDate,
					'toDate' : toDate,
					'applicationType' : applicationType,
					'revenueWard' : revenueWard
				}
		},
		
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"autoWidth": false,
		"bDestroy": true,
        columns : [
        	{ "data":"applicationNumber", 
        		"class":"text-center", 
        		"title":"Application Number",
        		"render" : function(data, type, row, meta) {
        			return '<a onclick="openpopup(\'/wtms/application/view/'+row.applicationNumber+'\')" href="javascript:void(0);">'+data+'</a>';
        		}
        	},
        	{ "data":"pipesizeid", "visible":false,"title":"Pipe Size"},
        	{ "data":"consumerNumber", "class":"text-center", "title":"Consumer Number"},
        	{ "data":"ownerName", "class":"text-center", "title":"Owner Name"},
        	{ "data":"address", "class":"text-center", "title":"Address"},
        	{ "data":"applicationType", "class":"text-center", "title":"Application Type"},
        	{ "data":"status", "class":"text-center", "title":"Application Status"},
        	{ "data":"approvalDate",
        		"class":"text-center",
        		"title":"Approval Date",
        		"render" : function(data, type, row, meta) {
        			var date = data.split("-");
        			return date[2]+"/"+date[1]+"/"+date[0];
        		}
        			
        	},
        	{ "data":"revenueWard", "class":"text-center", "title":"Ward"},
        	{ "data":"executionDate",
        		"class":"text-center", 
        		"title":"Actions",
        		"render" : function(data, type, full, meta) {
        			return '<button type="button" class="btn btn-primary" id="executeTap">Execute Tap</button>';
        		}
        	 
        	}
        	
        ],
        columnDefs:[{orderable:false,targets:[0]}],
        "initComplete": function(settings, json) {
          }
	});
	
}
else 
return false;
});


$("#save").unbind('click').on('click', function(e) {
	var meterMake = $("#meterMake").val();
	var applicationNumber = $("div.applnNumber").text();
	var executionDate = $("#executionDate").val();
	var initialReading = $("#initialReading").val();
	var serialNumber = $("#meterSerialNumber").val();
	
		if(meterMake==="" || executionDate==="" || initialReading==="" || serialNumber==="") {
			$(".display-err-msg").text("Please Enter Mandatory Fields");
			$(".display-err-msg").show();
			return false;
		}
		var jsonObj = [];
		var myObj = {};
		myObj = { "meterMake" : ""+meterMake,
				"executionDate" : "" +executionDate,
				"initialReading" : "" +initialReading,
				"meterSerialNumber" : "" +serialNumber,
				"applicationNumber" : "" +applicationNumber
		}
		jsonObj.push(myObj);
		var obj = {"executeWaterApplicationDetails" : jsonObj};
		var o = JSON.stringify(obj);
		
		$.ajax({
			url:"/wtms/application/execute-update/search-result",
			type:"POST",
			data:o,
			contentType : "application/json ; charset=utf-8",
			beforeSend : function() {
				$('.loader-class').modal('show', {
					backdrop : 'static'
				});
			},
			complete : function() {
				$('.loader-class').modal('hide');
			},
			success : function(response) {
				if(response=="Success") {
					$(".display-success-msg").text("The water connection application updated successfully");
					$("#save").hide();
					$(".display-err-msg").hide();
					$(".display-success-msg").show();
					return true;
				}
				else if (response == "EmptyList") {
					$(".display-err-msg").text('Please select atleast one application to execute connection');
					$(".display-err-msg").show();
					return false;
				}
				else if (response === "MeterMakeRequired") {
					$(".display-err-msg").text('Please select Meter Maker value');
					$(".display-err-msg").show();
					return false;
				}
				else if (response === "ExecutionDateRequired") {
					$(".display-err-msg").text('Please select Execution Date');
					$(".display-err-msg").show();
					return false;
				}
				else if (response === "InitialReadingRequired") {
					$(".display-err-msg").text('Please enter Meter Initial Reading value');
					$(".display-err-msg").show();
					return false;
				}
				else if(response === "MeterSerialNumberRequired") {
					$(".display-err-msg").text('Please enter Meter Serial Number value');
					$(".display-err-msg").show();
					return false;
				}
				else if (response == "DateValidationFailed") {
					$(".display-err-msg").text('Connection execution date must be greater than the approval date');
					$(".display-err-msg").show();
					return false;
				}
				else if (response == "UpdateExecutionFailed") {
					$(".display-err-msg").text('Water connection application update failed');
					$(".display-err-msg").show();
					return false;
				}
				return true;
			},
			error : function(response) {
				$(".display-err-msg").text('Water connection application update failed');
				$(".display-err-msg").show();
				return false;
			}
			
		});
		
});

