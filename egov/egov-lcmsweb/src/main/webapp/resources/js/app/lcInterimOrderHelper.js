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

$(document).ready(function(){
	
	var mode = $('#mode').val();
	if(mode == 'edit')  {
		$('#employeeInput').val($("#employeeName").val());
	}

	var employee = new Bloodhound({
		datumTokenizer : function(datum) {
			return Bloodhound.tokenizers
					.whitespace(datum.value);
		},
		queryTokenizer : Bloodhound.tokenizers.whitespace,
		remote : {
			url : '/lcms/ajax/getemployeeNames',
			replace : function(url, uriEncodedQuery) {
				return url + '?employeeName=' + uriEncodedQuery;

			},
			filter : function(data) {
				return $.map(data, function(index,em) {
					return {
						name: index,
						value: em

					};
				});
			}
		}
	});
	
	employee.initialize();
	var employee_typeahead = $('#employeeInput').typeahead({
		hint : false,
		highlight : false,
		minLength : 2
	}, {
		displayKey : 'name',
		source : employee.ttAdapter()
	});
	typeaheadWithEventsHandling(employee_typeahead , '#employee'); 
	
	
	loadDateFields();
	$('#interimOrder').change(function(){
		loadDateFields();
	});
	
	function loadDateFields(){
	if ($('#interimOrder :selected').text().localeCompare("Stay") == 0 || 
			$('#interimOrder :selected').text().localeCompare("Stay on Condition") ==0) { 
		$("#staydetails").show();
		}
	else{
		$("#staydetails").hide();
	}
	
	/*if($('#interimOrder :selected').text().localeCompare("Report File") == 0) {  
		$("#reportdetails1").show();
    	$("#reportdetails2").show();
	}else{
		$("#reportdetails1").hide();
    	$("#reportdetails2").hide();
	}*/
}
	$('#lcInterimOrderTbl').dataTable({
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 hidden col-xs-12'i><'col-md-3 hidden col-xs-6'l><'col-md-3 hidden col-xs-6 text-right'p>>",
		"autoWidth": false,
		"destroy":true,
		/* Disable initial sort */
		"paging":false,
	    "aaSorting": [],
		"oLanguage": {
			"sInfo": ""
		},
		"columnDefs": [ {
			"targets": 4,
			"orderable": false
		} ]
	});

});



$('#btnclose').click(function(){
	bootbox.confirm({
	    message: 'Information entered in this screen will be lost if you close this page ? Please confirm if you want to close. ',
	    buttons: {
	        'cancel': {
	            label: 'No',
	            className: 'btn-default pull-right'
	        },
	        'confirm': {
	            label: 'Yes',
	            className: 'btn-danger pull-right'
	        }
	    },
	    callback: function(result) {
	        if (result) {
	             window.close();
	        }
	    }
	});
	
});

function edit(legalCaseInterimOrder){  
		var url = '/lcms/lcinterimorder/edit/'+legalCaseInterimOrder;
		window.location = url;
       }
$('#buttonBack').click(function() {
	var lcNumber = $('#lcNumber').val();
	var url = '/lcms/lcinterimorder/list/?lcNumber='+lcNumber;
	window.location = url;
});

$('#createnewinterimorder').click(function() {
	var lcNumber = $('#lcNumber').val();
	var url = '/lcms/lcinterimorder/new/?lcNumber='+lcNumber;
	$('#lcInterimOrderform').attr('method', 'get');
	$('#lcInterimOrderform').attr('action', url);
	window.location = url;
 
});


function viewInterimorder(legalCaseInterimOrder){
	window.open('/lcms/lcinterimorder/view/' + legalCaseInterimOrder, "_self",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}
	
function vacatestay(Obj){  
	var url = '/lcms/vacatestay/new/?lcInterimOrderId='+Obj;
	window.location = url;
   }