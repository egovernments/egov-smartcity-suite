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
var tableContainer;

jQuery(document).ready(function ($) {
    $(":input").inputmask();
	
    $(".datepicker").datepicker({
        format: "dd/mm/yyyy"
	});
	
    $('#toggle-searchcomp').click(function () {
        if ($(this).html() == "Advanced..") {
            $(this).html('Less..');
            $('.show-searchcomp-more').show();
			} else {
            $(this).html('Advanced..');
            $('.show-searchcomp-more').hide();
		}
		
	});
	
    $('#searchComplaints').click(function () {
    	$.post("/pgr/complaint/citizen/anonymous/search", $('#searchComplaintForm').serialize())
		.done(function (searchResult) {
			console.log(JSON.stringify(searchResult));
			
			$('#complaintSearchResults').dataTable({
				destroy:true,
				columnDefs: [
				{
					defaultContent: "Not Available",
					targets: "_all",
					searchable:false
				}],
				data: searchResult,
				columns: [
				{title: 'Complaint Number', data: 'resource.searchable.crn'},
				{title: 'Complaint Type', data: 'resource.searchable.complaintType.name'},
				{title: 'Name', data: 'resource.common.citizen.name'},
				{title: 'Location', data: 'resource.searchable.boundary.name'},
				{title: 'Status', data: 'resource.clauses.status.name'},
				{title: 'Department', data: 'resource.searchable.complaintType.department.name'},
				{title: 'Registration Date', data: 'resource.common.createdDate'},
				{title: 'Expiry Date', data: ''}
				]
			});
		})
	});
	
	
    tableContainer = $("#csearch").dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-6 col-md-3 col-left'i><'col-xs-6 col-md-3 text-right col-left'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-xs-12 col-md-3 col-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"autoWidth": false,
		"oTableTools": {
			"sSwfPath": "../../../../../../egov-egiweb/src/main/webapp/resources/global/swf/copy_csv_xls_pdf.swf",
			"aButtons": ["copy", "csv", "xls", "pdf", "print"]
		}
	});
	
	tableContainer.columnFilter({
		"sPlaceHolder": "head:after"
	});
	
    $("#when_date").change(function () {
        populatedate($('#when_date').val());
	});
	
	
    function populatedate(id) {
        var d = new Date();
        var quarter = getquarter(d);
        var start, end;
        switch (id) {
			
			case "last7":
			$("#end_date").datepicker("setDate", d);
			start = new Date(d.setDate((d.getDate() - 7)));
			var start = new Date(start.getFullYear(), start.getMonth(), start.getDate());
			$("#start_date").datepicker("setDate", start);
			break;
			
			case "last30":
			$("#end_date").datepicker("setDate", d);
			start = new Date(d.setDate((d.getDate() - 30)));
			var start = new Date(start.getFullYear(), start.getMonth(), start.getDate());
			$("#start_date").datepicker("setDate", start);
			break;
			
			case "last90":
			$("#end_date").datepicker("setDate", d);
			start = new Date(d.setDate((d.getDate() - 90)));
			var start = new Date(start.getFullYear(), start.getMonth(), start.getDate());
			$("#start_date").datepicker("setDate", start);
			break;
			
			case "cdate":
			$("#end_date").val("");
			$("#start_date").val("");
			break;
			
			case "all":
			$("#end_date").val("");
			$("#start_date").val("");
			break;
			
			/* case "lastyear":
				var start = ((quarter == 4) ? (d.getFullYear() - 2) : (d.getFullYear() - 1));
				var end = ((quarter == 4) ? (d.getFullYear() - 1) : (d.getFullYear()));
				var pyearstart = new Date(start, 3, 1);
				var pyearend = new Date(end, 2, 31);
				$("#start_date").datepicker("setDate", pyearstart);
				$("#end_date").datepicker("setDate", pyearend);
				break;
				
				case "thisyear":
				var start = ((quarter == 4) ? (d.getFullYear() - 1) : (d.getFullYear()));
				var end = ((quarter == 4) ? (d.getFullYear()) : (d.getFullYear() + 1));
				var cyearstart = new Date(start, 3, 1);
				var cyearend = new Date(end, 2, 31);
				$("#start_date").datepicker("setDate", cyearstart);
				$("#end_date").datepicker("setDate", cyearend);
				break;
				
				case "lastquarter":
				var lqy = ((quarter == 4) ? d.getFullYear() - 1 : d.getFullYear());
				var firstDate = new Date(lqy, quarter * 3 - 3, 1);
				$("#start_date").datepicker("setDate", firstDate);
				$("#end_date").datepicker("setDate", new Date(firstDate.getFullYear(), firstDate.getMonth() + 3, 0));
				break;
				
				case "thisquarter":
				var month;
				if (quarter == 4) {
				month = 0;
				} else if (quarter == 1) {
				month = 3;
				} else if (quarter == 2) {
				month = 6;
				} else if (quarter == 3) {
				month = 9;
				}
				
				var firstDate = new Date(d.getFullYear(), month, 1);
				$("#start_date").datepicker("setDate", firstDate);
				$("#end_date").datepicker("setDate", new Date(firstDate.getFullYear(), firstDate.getMonth() + 3, 0));
				break;
				
				case "lastmonth":
				var firstDay = new Date(d.getFullYear(), d.getMonth() - 1, 1);
				var lastDay = new Date(d.getFullYear(), d.getMonth(), 0);
				$("#start_date").datepicker("setDate", firstDay);
				$("#end_date").datepicker("setDate", lastDay);
				break;
				
				case "thismonth":
				var firstDay = new Date(d.getFullYear(), d.getMonth(), 1);
				var lastDay = new Date(d.getFullYear(), d.getMonth() + 1, 0);
				$("#start_date").datepicker("setDate", firstDay);
				$("#end_date").datepicker("setDate", lastDay);
				break;
				
				case "lastweek":
				var firstday = new Date(d.setDate((d.getDate() - d.getDay() - 7)));
				var lastday = new Date(d.setDate(d.getDate() - d.getDay() + 6));
				var firstDay = new Date(firstday.getFullYear(), firstday.getMonth(), firstday.getDate());
				var lastDay = new Date(lastday.getFullYear(), lastday.getMonth(), lastday.getDate());
				$("#start_date").datepicker("setDate", firstDay);
				$("#end_date").datepicker("setDate", lastDay);
				break;
				
				case "thisweek":
				var firstday = new Date(d.setDate(d.getDate() - d.getDay()));
				var lastday = new Date(d.setDate(d.getDate() - d.getDay() + 6));
				var firstDay = new Date(firstday.getFullYear(), firstday.getMonth(), firstday.getDate());
				var lastDay = new Date(lastday.getFullYear(), lastday.getMonth(), lastday.getDate());
				$("#start_date").datepicker("setDate", firstDay);
				$("#end_date").datepicker("setDate", lastDay);
				break;
				
				case "today":
				$("#start_date").datepicker("setDate", d);
				$("#end_date").val("");
			break; */
		}
	}
	
	function getquarter(d) {
		if (d.getMonth() >= 0 && d.getMonth() <= 2) {
            quarter = 4;
			} else if (d.getMonth() >= 3 && d.getMonth() <= 5) {
            quarter = 1;
			} else if (d.getMonth() >= 6 && d.getMonth() <= 8) {
			quarter = 2;
			} else if (d.getMonth() >= 9 && d.getMonth() <= 11) {
			quarter = 3;
		}
		
        return quarter;
	}
	
	$(".checkdate").focus(function () {
		
        $(".checkdate").change(function () {
			console.log("custom dates");
			$("select#when_date").prop('selectedIndex', 4);
		});
		
	});
	
	$(".checkdate").focusout(function () {
		
        var start = $('#start_date').val();
        var end = $('#end_date').val();
		
        if (start != "" && end != "") {
			var stsplit = start.split("/");
			var ensplit = end.split("/");
			
			start = stsplit[1] + "/" + stsplit[0] + "/" + stsplit[2];
			end = ensplit[1] + "/" + ensplit[0] + "/" + ensplit[2];
			
			ValidRange(start, end);
		}
		
	});
	
	function ValidRange(start, end) {
        var startDate = Date.parse(start);
        var endDate = Date.parse(end);
		
        // Check the date range, 86400000 is the number of milliseconds in one day
        var difference = (endDate - startDate) / (86400000 * 7);
        if (difference < 0) {
			alert("The start date must come before the end date.");
			return false;
			} else {
			return true;
		}
		
        return true;
		
		
	}
	
	$("form").submit(function(event){
		alert("inside submit");
		if($("select#when_date option:selected").index() == 0){
			
			}else{
			
			if($('#start_date').val() != '' && $('#end_date').val() != ''){
				var start = $('#start_date').val();
				var end = $('#end_date').val();
				
				if (start != "" && end != "") {
					var stsplit = start.split("/");
					var ensplit = end.split("/");
					
					start = stsplit[1] + "/" + stsplit[0] + "/" + stsplit[2];
					end = ensplit[1] + "/" + ensplit[0] + "/" + ensplit[2];
					
					ValidRange(start, end);
				}
				}else{
				alert("Select the date");
			}
			
			event.preventDefault();
		}
	});
	
	
});						
