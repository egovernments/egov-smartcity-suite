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
jQuery('#btnsearch').click(function(e) {
	if($("form").valid())
		callAjaxSearch();
});

$('#btndownloadpdf').click(function() {
	var departments = $('#departments').val();
	var financialYear = $('#financialyear').val();
	var asOnDate = $('#asOnDate').val();
	var fund = $('#fund').val();
	var functionPDF = $('#function').val();
	var budgetHead = $('budgetHead').val();

	window.open("/egworks/reports/estimateappropriationregister/pdf?departments="
			+ departments
			+ "&financialYear="
			+ financialYear
			+ "&asOnDate="
			+ asOnDate
			+ "&fund="
			+ fund
			+ "&functionPDF="
			+ functionPDF
			+ "&budgetHead=" + budgetHead
			+ "&contentType=pdf", '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
});

$('#btndownloadexcel').click(function() {
	var departments = $('#departments').val();
	var financialYear = $('#financialyear').val();
	var asOnDate = $('#asOnDate').val();
	var fund = $('#fund').val();
	var functionPDF = $('#function').val();
	var budgetHead = $('budgetHead').val();

	window.open("/egworks/reports/estimateappropriationregister/pdf?departments="
			+ departments
			+ "&financialYear="
			+ financialYear
			+ "&asOnDate="
			+ asOnDate
			+ "&fund="
			+ fund
			+ "&functionPDF="
			+ functionPDF
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
				"sPaginationType" : "bootstrap",
				"bDestroy" : true,
				'bAutoWidth': false,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : []
				},
				"fnRowCallback" : function(row, data, index) {
					$('td:eq(0)',row).html(index+1);
					if(index == reportdatatable.fnSettings().fnRecordsTotal() - 1) {
						var balanceAvailable = "Available Balance : " + data.actualBalanceAvailable;
						$('#balanceAvailable').html(balanceAvailable);
					}
					return row;
				},
				aaSorting: [],				
				columns : [ { 
					"data" : "", "sClass" : "text-center"} ,{ 
					"data" : "appropriationNumber", "sClass" : "text-left"} ,{
					"data" : "appropriationDate", "sClass" : "text-left",
					render: function (data, type, full) {
						if(full!=null &&  full.appropriationDate != undefined) {
							var regDateSplit = full.appropriationDate.split(" ")[0].split("-");		
							return regDateSplit[2] + "/" + regDateSplit[1] + "/" + regDateSplit[0];
						}
						else return "";} },{
					"data" : "appropriationValue", "sClass" : "text-left"} ,{ 
					"data" : "estimateNumber", "sClass" : "text-left"} ,{
					"data" : "workIdentificationNumber", "sClass" : "text-left"} ,{
					"data" : "nameOfWork", "sClass" : "text-left"} ,{
					"data" : "estimateDate", "sClass" : "text-left"} ,{
					"data" : "estimateValue", "sClass" : "text-left"} ,{
					"data" : "cumulativeTotal", "sClass" : "text-left"} ,{
					"data" : "balanceAvailable", "sClass" : "text-left"
					}]				
				});
			}
var workIdNumber = new Bloodhound({
    datumTokenizer: function (datum) {
        return Bloodhound.tokenizers.whitespace(datum.value);
    },
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    remote: {
        url: '/egworks/reports/ajax-wincodestosearchworkprogressregister?code=%QUERY',
        filter: function (data) {
            return $.map(data, function (ct) {
                return {
                    name: ct
                };
            });
        }
    }
});

workIdNumber.initialize();
var workIdNumber_typeahead = $('#workIdentificationNumber').typeahead({
	hint : true,
	highlight : true,
	minLength : 3
}, {
	displayKey : 'name',
	source : workIdNumber.ttAdapter()
});