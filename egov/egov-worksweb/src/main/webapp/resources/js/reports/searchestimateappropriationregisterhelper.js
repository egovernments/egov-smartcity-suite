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
$budgetHeadId=0;
jQuery('#btnsearch').click(function(e) {
	$('#report-footer').hide();
	$('#btndownloadpdf').hide();
	$('#btndownloadexcel').hide();
	$('#balanceAvailable').html("");
	if($("form").valid())
		callAjaxSearch();
});

$('#btndownloadpdf').click(function() {
	var departments = $('#departments').val();
	var financialYear = $('#financialYear').val();
	var asOnDate = $('#asOnDate').val();
	var fund = $('#fund').val();
	var functionName = $('#function').val();
	var budgetHead = $('#budgetHead').val();

	window.open("/egworks/reports/estimateappropriationregister/pdf?departments="
			+ departments
			+ "&financialYear="
			+ financialYear
			+ "&asOnDate="
			+ asOnDate
			+ "&fund="
			+ fund
			+ "&functionName="
			+ functionName
			+ "&budgetHead=" + budgetHead
			+ "&contentType=pdf", '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
});

$('#btndownloadexcel').click(function() {
	var departments = $('#departments').val();
	var financialYear = $('#financialYear').val();
	var asOnDate = $('#asOnDate').val();
	var fund = $('#fund').val();
	var functionName = $('#function').val();
	var budgetHead = $('#budgetHead').val();

	window.open("/egworks/reports/estimateappropriationregister/pdf?departments="
			+ departments
			+ "&financialYear="
			+ financialYear
			+ "&asOnDate="
			+ asOnDate
			+ "&fund="
			+ fund
			+ "&functionName="
			+ functionName
			+ "&budgetHead=" + budgetHead
			+ "&contentType=excel", '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
});
	
function getFormData($form){
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i){
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}
function callAjaxSearch() {
	drillDowntableContainer = jQuery("#resultTable");		
	jQuery('.report-section').removeClass('display-hide');
		reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/egworks/reports/ajax-estimateappropriationregister",      
					type: "POST",
					"data":  getFormData(jQuery('form'))
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
					if(data.appropriationValue != "")
						$('td:eq(3)',row).html(parseFloat(Math.round(data.appropriationValue * 100) / 100).toFixed(2));
					if(data.estimateValue != "")
						$('td:eq(8)',row).html(parseFloat(Math.round(data.estimateValue * 100) / 100).toFixed(2));
					if(data.cumulativeTotal != "")
						$('td:eq(9)',row).html(parseFloat(Math.round(data.cumulativeTotal * 100) / 100).toFixed(2));
					if(data.balanceAvailable != "")
						$('td:eq(10)',row).html(parseFloat(Math.round(data.balanceAvailable * 100) / 100).toFixed(2));
					$('#btndownloadpdf').show();
					$('#btndownloadexcel').show();
					$('td:eq(0)',row).html(index+1);
					if(index == 0) {
						var balanceAvailable = "Available Balance : " + parseFloat(Math.round(data.actualBalanceAvailable * 100) / 100).toFixed(2);
						$('#balanceAvailable').html(balanceAvailable);
					}
					return row;
				},
				aaSorting: [],				
				"columns" : [{
					"data" : "", "sClass" : "text-center"} ,{ 
					"data" : "appropriationNumber", "sClass" : "text-left"} ,{
					"data" : "appropriationDate", "sClass" : "text-left" },{
					"data" : "appropriationValue", "sClass" : "text-right"} ,{ 
					"data" : "estimateNumber", "sClass" : "text-left"} ,{
					"data" : "workIdentificationNumber", "sClass" : "text-left"} ,{
					"data" : "nameOfWork", "sClass" : "text-left"} ,{
					"data" : "estimateDate", "sClass" : "text-left"} ,{
					"data" : "estimateValue", "sClass" : "text-right"} ,{
					"data" : "cumulativeTotal", "sClass" : "text-right"} ,{
					"data" : "balanceAvailable", "sClass" : "text-right"}],
					  "footerCallback" : function(row, data, start, end, display) {
							var api = this.api(), data;
							if (data.length == 0) {
								jQuery('#report-footer').hide();
							} else {
								jQuery('#report-footer').show(); 
							}
							if (data.length > 0) {
								$('td:eq(1)',row).html(parseFloat(Math.round(data[0].cumulativeExpensesIncurred * 100) / 100).toFixed(2));
								$('td:eq(2)',row).html(parseFloat(Math.round(data[0].actualBalanceAvailable * 100) / 100).toFixed(2));
							}
						},
						"aoColumnDefs" : [ {
							"aTargets" : [9,10],
							"mRender" : function(data, type, full) {
								return data;    
							}
						} ]		
					});
	}

$('#function').change(function(){
	 if ($('#function').val() === '') {
		   $('#budgetHead').empty();
		   $('#budgetHead').append($('<option>').text('Select from below').attr('value', ''));
			return;
			} else {
			$.ajax({
				type: "GET",
				url: "/egworks/lineestimate/getbudgetheadbyfunction",
				cache: true,
				dataType: "json",
				data:{'functionId' : $('#function').val()}	
			}).done(function(value) {
				console.log(value);
				$('#budgetHead').empty();
				$('#budgetHead').append($("<option value=''>Select from below</option>"));
				$.each(value, function(index, val) {
					var selected="";
					if($budgetHeadId)
					{
						if(budgetHeadId==val.id)
						{
							selected="selected";
						}
					}
				     $('#budgetHead').append($('<option '+ selected +'>').text(val.name).attr('value', val.id));
				});
			});
		}
	});

