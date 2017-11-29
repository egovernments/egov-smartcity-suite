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
$createdDate="";
$subTypeOfWorkId = 0;
jQuery('#btnsearch').click(function(e) {
	$('#btndownloadpdf').hide();
	$('#btndownloadexcel').hide();
	var adminSanctionFromDate = '';
	var adminSanctionToDate = '';
	if($('#adminSanctionFromDate').val() != "") {
		adminSanctionFromDate = $('#adminSanctionFromDate').data('datepicker').date;
	}
	if($('#adminSanctionToDate').val() != "") {
		adminSanctionToDate = $('#adminSanctionToDate').data('datepicker').date;
	}
	var flag = true; 
	if(adminSanctionToDate != '' && adminSanctionFromDate != '') {
		if(adminSanctionFromDate > adminSanctionToDate) {
			flag = false;
			bootbox.alert('Line Estimate Admin Sanction To Date should be greater than Line Estimate Admin Sanction From Date');
		}
	}
	if(flag && $('form').valid())
		callAjaxSearch();
	
	var spillOver = document.getElementById("spillOverFlag");
    var spillOverFlag = spillOver.checked ? true : false;
    
	var queryParameters = "Estimate Abstract Report By Type Of Work  ";
	if(spillOverFlag)
		queryParameters = "Estimate Abstract Report By Type Of Work for Spill Over Line Estimates ";
	
	if(adminSanctionFromDate != "" && adminSanctionToDate != "") {
		queryParameters += "Date Range : " + $('#adminSanctionFromDate').val() + " - " + $('#adminSanctionToDate').val() + ", ";
	}
    if(adminSanctionFromDate != "" && adminSanctionToDate == "") {
        queryParameters += "Line Estimate Admin Sanction From Date : " + $('#adminSanctionFromDate').val() + ", ";
    }
    if(adminSanctionToDate != "" && adminSanctionFromDate == "") {
        queryParameters += "Line Estimate Admin Sanction To Date : " + $('#adminSanctionToDate').val() + ", ";
    }
    
    if($('#typeOfWork').val() != "") {
        queryParameters += "Type Of Work : " + $('#typeOfWork').find(":selected").text() + ", ";
    }
    
    if($('#subTypeOfWork').val() != "") {
        queryParameters += "Sub Type Of Work : " + $('#subTypeOfWork').find(":selected").text() + ", ";
    }
    
    if($('#departmentsSelect').val() != null) {
    	var departmentNames= "";
    	$("#departmentsSelect option:selected").each(function () {
    		   var $this = $(this);
    		   if ($this.length) {
    			   departmentNames = departmentNames + $this.text() + ",";
    		   }
    		});
    	departmentNames = departmentNames.substring(0, departmentNames.length - 1);
        queryParameters += "Departments : " + departmentNames + ", ";
    }
    
    if($('#scheme').val() != "") {
        queryParameters += "Scheme : " + $('#scheme').find(":selected").text() + ", ";
    }
    
    if($('#subScheme').val() != "") {
        queryParameters += "Sub Scheme : " + $('#subScheme').find(":selected").text() + ", ";
    }
    
    if($('#workCategory').val() != "") {
        queryParameters += "Work Category : " + $('#workCategory').val().replace(/_/g,' ') + ", ";
    }
    
    if($('#beneficiary').val() != "") {
        queryParameters += "Beneficiary : " + $('#beneficiary').val().replace(/_C/g, '/C').replace('_',' ') + ", ";
    }
    
    if($('#natureOfWork').val() != "") {
        queryParameters += "Nature Of Work : " + $('#natureOfWork').find(":selected").text() + ", ";
    }
    
    
    if (queryParameters.endsWith(", "))
        queryParameters = queryParameters.substring(0, queryParameters.length - 2);
    
    $('#searchCriteria').html(queryParameters);
    
});

$('#btndownloadpdf').click(function() {
	var adminSanctionFromDate = $('#adminSanctionFromDate').val();
	var adminSanctionToDate = $('#adminSanctionToDate').val();
	var typeOfWork = $('#typeOfWork').val();
	var subTypeOfWork = $('#subTypeOfWork').val();
	var departments = $('#departmentsSelect').val();
	var scheme = $('#scheme').val();
	var subScheme = $('#subScheme').val();
	var workCategory = $('#workCategory').val();
	var beneficiary = $('#beneficiary').val();
	var natureOfWork = $('#natureOfWork').val();
	var spillOver = document.getElementById("spillOverFlag");
	
	var spillOverFlag = spillOver.checked ? true : false;
	
	if(departments == null)
		departments = 0;
	//TO-DO : Need to change to ajax submit
	var url = "/egworks/reports/estimateabstractreport/typeofworkwise/pdf?adminSanctionFromDate="
	+ adminSanctionFromDate
	+ "&adminSanctionToDate="
	+ adminSanctionToDate
	+ "&typeOfWork="
	+ typeOfWork
	+ "&subTypeOfWork="
	+ subTypeOfWork
	+ "&departments="
	+ departments
	+ "&scheme="
	+ scheme
	+ "&subScheme="
	+ subScheme
	+ "&workCategory="
	+ workCategory
	+ "&beneficiary="
	+ beneficiary
	+ "&natureOfWork="
	+ natureOfWork
	+ "&spillOverFlag=" + spillOverFlag
	+ "&contentType=pdf";

	window.open(url, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
});

$('#btndownloadexcel').click(function() {
	var adminSanctionFromDate = $('#adminSanctionFromDate').val();
	var adminSanctionToDate = $('#adminSanctionToDate').val();
	var typeOfWork = $('#typeOfWork').val();
	var subTypeOfWork = $('#subTypeOfWork').val();
	var departments = $('#departmentsSelect').val();
	var scheme = $('#scheme').val();
	var subScheme = $('#subScheme').val();
	var workCategory = $('#workCategory').val();
	var beneficiary = $('#beneficiary').val();
	var natureOfWork = $('#natureOfWork').val();
	var spillOver = document.getElementById("spillOverFlag");
	
	var spillOverFlag = spillOver.checked ? true : false;
	if(departments == null)
		departments = 0;
	//TO-DO : Need to change to ajax submit
	var url = "/egworks/reports/estimateabstractreport/typeofworkwise/pdf?adminSanctionFromDate="
	+ adminSanctionFromDate
	+ "&adminSanctionToDate="
	+ adminSanctionToDate
	+ "&typeOfWork="
	+ typeOfWork
	+ "&subTypeOfWork="
	+ subTypeOfWork
	+ "&departments="
	+ departments
	+ "&scheme="
	+ scheme
	+ "&subScheme="
	+ subScheme
	+ "&workCategory="
	+ workCategory
	+ "&beneficiary="
	+ beneficiary
	+ "&natureOfWork="
	+ natureOfWork
	+ "&spillOverFlag=" + spillOverFlag
	+ "&contentType=excel";

	window.open(url, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
});

	
function getFormData($form){
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i){
    	if (indexed_array.hasOwnProperty(n['name']))
    		indexed_array[n['name']] = indexed_array[n['name']] + ',' + n['value'];	
    	else
    		indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}

function callAjaxSearch() {
	drillDowntableContainer = jQuery("#resultTable");		
	jQuery('.report-section').removeClass('display-hide');
	 $("#adminSanctionFromDate").removeAttr("disabled");
	 $("#adminSanctionToDate").removeAttr("disabled");
	 $("#departmentSelected").val($("#departmentsSelect").val());
	 var postData = getFormData(jQuery('form'));
	 $("#adminSanctionFromDate").attr('disabled', 'disabled');
	 $("#adminSanctionToDate").attr('disabled', 'disabled');
		reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/egworks/reports/ajax-estimateabstractreportbytypeofworkwise",      
					type: "POST",
					"data":  postData
				},
				"bDestroy" : true,
				'bAutoWidth': false,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : []
				},
				"fnRowCallback" : function(row, data, index) {
					$('#btndownloadpdf').show();
					$('#btndownloadexcel').show();
					/*$('td:eq(0)',row).html(index+1);*/
					disableDateFields();
					if(index == 0) {
						$createdDate = data.createdDate;
						var dataRunmTime = "The information in this report is not real time, it provides information of the transactions that happened till " + $createdDate;
						$('#dataRun').html(dataRunmTime);
					}
					
					return row;
				},
				aaSorting: [],				
				columns : [ { 
					/*"data" : "", "sClass" : "text-center"} ,{*/ 
					"data" : "typeOfWorkName", "sClass" : "text-center"} ,{
					"data" : "subTypeOfWorkName", "sClass" : "text-center"} ,{
					"data" : "departmentName", "sClass" : "text-center"} ,{
					"data" : "lineEstimates", "sClass" : "text-right"} ,{
					"data" : "adminSanctionedEstimates", "sClass" : "text-right"} ,{ 
					"data" : "adminSanctionedAmountInCrores", "sClass" : "text-right"} ,{
					"data" : "technicalSanctionedEstimates", "sClass" : "text-right"} ,{
					"data" : "loaCreated", "sClass" : "text-right"} ,{
					"data" : "agreementValueInCrores", "sClass" : "text-right"} ,{
					"data" : "workInProgress", "sClass" : "text-right"} ,{ 
					"data" : "workCompleted", "sClass" : "text-right"} ,{
					"data" : "billsCreated", "sClass" : "text-right"} ,{
					"data" : "billValueInCrores", "sClass" : "text-right"}]				
				});
		
		//show/hide department
		var department = $("#departmentsSelect").val();

		if(department==null){
			var oTable = $('#resultTable').DataTable();
			oTable.column(2).visible(false);
		}
		
}

function disableDateFields(){
	$("#adminSanctionFromDate").attr('disabled', 'disabled');
	var currentFYId = $("#currentFinancialYearId").val();
	var selectedFYId = $("#financialYear").val();
	  if(currentFYId!= null && currentFYId!="" && selectedFYId == currentFYId ){
		  $("#adminSanctionToDate").removeAttr("disabled");
        }else{
        	 $("#adminSanctionToDate").attr('disabled', 'disabled');
        }
}

function getSubSchemsBySchemeId(schemeId) {
	if ($('#scheme').val() === '') {
		   $('#subScheme').empty();
		   $('#subScheme').append($('<option>').text('Select from below').attr('value', ''));
			return;
			} else {
				$.ajax({
					url: "/egworks/lineestimate/getsubschemesbyschemeid/"+schemeId,     
					type: "GET",
					dataType: "json",
					success: function (response) {
						$('#subScheme').empty();
						$('#subScheme').append($("<option value=''>Select from below</option>"));
						var responseObj = JSON.parse(response);
						$.each(responseObj, function(index, value) {
							$('#subScheme').append($('<option>').text(responseObj[index].name).attr('value', responseObj[index].id));
							$('#subScheme').val($subSchemeId);
						});
					}, 
					error: function (response) {
						console.log("failed");
					}
				});
			}
}

function getFinancialYearDatesByFYId(fyId) {
	if ($('#financialYear').val() === '') {
		   $('#adminSanctionFromDate').val('');
		   $('#adminSanctionToDate').val('');
			return;
			} else {
				$.ajax({
					url : '/egworks/lineestimate/getfinancilyearbyid',
					type : "get",
					data : {
						fyId : fyId
					},
					success : function(data, textStatus, jqXHR) {
						var currentFYId = $("#currentFinancialYearId").val();
						var selectedFYId = $("#financialYear").val();
						var sdate = new Date(data.startingDate);
						var sd = sdate.getDate();
				        var sm = sdate.getMonth();
				        var sy = sdate.getFullYear();
				        var startingDate = new Date(sy,sm, sd );
				        $("#adminSanctionFromDate").datepicker('setDate', startingDate);
				        $("#adminSanctionFromDate").attr('disabled', 'disabled');
				        if(currentFYId!= null && currentFYId!="" && selectedFYId == currentFYId ){
				        	var edate = new Date();
							var ed = edate.getDate();
					        var em = edate.getMonth();
					        var ey = edate.getFullYear();
					        var endingDate = new Date(ey,em, ed );
					        $("#adminSanctionToDate").datepicker('setDate', endingDate);
					        $("#adminSanctionToDate").removeAttr("disabled");
				        }else{
					        var edate = new Date(data.endingDate);
							var ed = edate.getDate();
					        var em = edate.getMonth();
					        var ey = edate.getFullYear();
					        var endingDate = new Date(ey,em, ed );
					        $("#adminSanctionToDate").datepicker('setDate', endingDate);
					        $("#adminSanctionToDate").attr('disabled', 'disabled');
				        }
				        
				        
					},
					error : function(jqXHR, textStatus, errorThrown) {
						bootbox.alert("Error while get financilyear by id ");
					}
				});
			}
}

function replaceWorkCategoryChar() {
	$('#workCategory option').each(function() {
	   var $this = $(this);
	   $this.text($this.text().replace(/_/g, ' '));
	});
}

function replaceBeneficiaryChar() {
	$('#beneficiary option').each(function() {
	   var $this = $(this);
	   $this.text($this.text().replace(/_C/g, '/C').replace(/_/g, ' '));
	});
}

$(document).ready(function(){
	replaceWorkCategoryChar();
	replaceBeneficiaryChar();
	
	$('#btnsearch').click(function(e) {
		if ($('form').valid()) {
		} else {
			e.preventDefault();
		}
	});
	
	var selectedFYId = $("#financialYear").val();
	getFinancialYearDatesByFYId(selectedFYId);
	
	$subTypeOfWorkId = $('#subTypeOfWorkValue').val();
	$('#typeOfWork').change(function(){
		 if ($('#typeOfWork').val() === '') {
			   $('#subTypeOfWork').empty();
			   $('#subTypeOfWork').append($('<option>').text('Select from below').attr('value', ''));
				return;
				} else {
				$.ajax({
					type: "GET",
					url: "/egworks/lineestimate/getsubtypeofwork",
					cache: true,
					dataType: "json",
					data:{'id' : $('#typeOfWork').val()}
				}).done(function(value) {
					console.log(value);
					$('#subTypeOfWork').empty();
					$('#subTypeOfWork').append($("<option value=''>Select from below</option>"));
					$.each(value, function(index, val) { 
					var selected="";
						if($subTypeOfWorkId)
						{
							if($subTypeOfWorkId==val.id)
							{
								selected="selected";
							}
						}
					     $('#subTypeOfWork').append($('<option '+ selected +'>').text(val.description).attr('value', val.id));
					});
				});
		}
	});
	
		
});