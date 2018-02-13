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
var tableContainer;
var oDataTable;
jQuery(document).ready(function ($) {
	$('#reportgeneration-header').hide();
		$.fn.dataTable.moment( 'DD/MM/YYYY' );
	
	    $(":input").inputmask();
		
	    $(".datepicker").datepicker({
	        format: "dd/mm/yyyy"
		});
	    tableContainer=$('#reportResults');
	    $('#compensationreport').click(function () {
	    	if($('#compensationReportRequestForm').valid())
	    		submitForm();
	    	else
	    		return false;
	    });
		
	
	
	  
		function validRange(start, end) {
	        var startDate = Date.parse(start);
	        var endDate = Date.parse(end);
			
	        // Check the date range, 86400000 is the number of milliseconds in one day
	        var difference = (endDate - startDate) / (86400000 * 7);
	        if (difference < 0) {
	        	bootbox.alert("The start date must come before the end date.");
				$('#end_date').val('');
				return false;
				} else {
				return true;
			}
			
	        return true;
		}
		
		$(".btn-primary").click(function(event){
			if($('#start_date').val() != '' && $('#end_date').val() != ''){
					var start = $('#start_date').val();
					var end = $('#end_date').val();
					var stsplit = start.split("/");
						var ensplit = end.split("/");
						
						start = stsplit[1] + "/" + stsplit[0] + "/" + stsplit[2];
						end = ensplit[1] + "/" + ensplit[0] + "/" + ensplit[2];
						
						if(!validRange(start,end))
						{
						return false;
						}
					
					else {
						document.forms[0].submit;
						return true;
					}
				}else{
			 }
				event.preventDefault();
			
		});
		
	
	
	$('#moduleName').change(function(){
		$.ajax({
			url: "/wtms/elastic/compensationreport/ajax-applicationType",     
			type: "GET",
			data: {
				appModuleName : $('#moduleName').val()   
			},
			dataType: "json",
			success: function (response) {
				console.log("success"+response);
				$('#applicationType').empty();
				$('#applicationType').append($("<option value=''>Select from below</option>"));
				$.each(response, function(index, value) {
					$('#applicationType').append($('<option>').text(value));
				});
				
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	});


	
	
});

$(document).on("keypress", 'form', function (e) {
    var code = e.keyCode || e.which;
    if (code == 13) {
        e.preventDefault();
        return false;
    }
});

function getdate()
{
	var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth()+1; //January is 0!

    var yyyy = today.getFullYear();
    if(dd<10){
        dd='0'+dd
    } 
    if(mm<10){
        mm='0'+mm
    } 
    var today = dd+'/'+mm+'/'+yyyy;
    return today;
}
function submitForm(){
	$('#reportgeneration-header').show();
	var today = getdate();
	$('.loader-class').modal('show', {backdrop: 'static'});
	$.post("/wtms/elastic/compensationreport/", $('#compensationReportRequestForm').serialize())
	.done(function (searchResult) {
		//console.log(JSON.stringify(searchResult));
		
		oDataTable=tableContainer.DataTable({
			dom : "<'row'<'col-xs-4 pull-right'f>r>t<'row add-margin'<'col-md-3 col-xs-6'i><'col-md-2 col-xs-6'l><'col-md-3 col-xs-6 text-right'B><'col-md-4 col-xs-6 text-right'p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"autoWidth": false,
			"bDestroy": true,
			"processing": true,
			buttons: [
						{
						    extend: 'excel',
						    filename: 'Compensation Report',
							message : "Report generated on "+today+"",
							exportOptions : {
								columns : [0,1, 2, 3, 4, 5, 6, 7, 8,9]
							}
						}
					],
			data: searchResult,
			columns: [  {
	        	   "sTitle" : "S.no",
	           },  
            {title: 'Application Number', data: 'applicationNumber'},
			{title: 'Application Type', data: 'applicationType'},
			{title: 'Application Date', data :"applicationDate"},
			{title: 'Application Close Date', data :"applicationCloseDate"},
			{title: 'SLA', data: 'sla'},
			{title: 'Number of days delayed', data: 'delayedDays'},
			{title: 'Applicant Name', data: 'applicantName'},
			{title: 'Applicant Aadhar Number', data: 'aadharNumber'},
			{title: 'Compensation Amount', data: 'compensationAmount'}
			]
		});
		
		$('.loader-class').modal('hide');
		
		oDataTable.on( 'order.dt search.dt', function () {
			oDataTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
               cell.innerHTML = i+1;
               oDataTable.cell(cell).invalidate('dom'); 
           } );
       } ).draw();
		
	});
	
}
