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
			bootbox.alert('Admin Sanction To Date should be greater than Admin Sanction From Date');
		}
	}
	if(flag)
		callAjaxSearch();
});

$('#btndownloadpdf').click(function() {
	var adminSanctionFromDate = $('#adminSanctionFromDate').val();
	var adminSanctionToDate = $('#adminSanctionToDate').val();
	var workIdentificationNumber = $('#workIdentificationNumber').val();
	var contractor = $('#contractor').val();
	var department = $('#department').val();
	var spillOver = document.getElementById("spillOverFlag");
	var spillOverFlag = spillOver.checked ? true : false;

	window.open("/egworks/reports/workprogressregister/pdf?adminSanctionFromDate="
			+ adminSanctionFromDate
			+ "&adminSanctionToDate="
			+ adminSanctionToDate
			+ "&workIdentificationNumber="
			+ workIdentificationNumber
			+ "&contractor="
			+ contractor
			+ "&department="
			+ department
			+ "&spillOverFlag=" + spillOverFlag
			+ "&contentType=pdf", '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
});

$('#btndownloadexcel').click(function() {
	var adminSanctionFromDate = $('#adminSanctionFromDate').val();
	var adminSanctionToDate = $('#adminSanctionToDate').val();
	var workIdentificationNumber = $('#workIdentificationNumber').val();
	var contractor = $('#contractor').val();
	var department = $('#department').val();
	var spillOver = document.getElementById("spillOverFlag");
	var spillOverFlag = spillOver.checked ? true : false;

	window.open("/egworks/reports/workprogressregister/pdf?adminSanctionFromDate="
			+ adminSanctionFromDate
			+ "&adminSanctionToDate="
			+ adminSanctionToDate
			+ "&workIdentificationNumber="
			+ workIdentificationNumber
			+ "&contractor="
			+ contractor
			+ "&department="
			+ department
			+ "&spillOverFlag=" + spillOverFlag
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
					url : "/egworks/reports/ajax-workprogressregister",      
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
					return row;
				},
				aaSorting: [],				
				columns : [ { 
					"data" : "", "sClass" : "text-center"} ,{ 
					"data" : "ward", "sClass" : "text-left"} ,{
					"data" : "location", "sClass" : "text-left"} ,{
					"data" : "typeOfSlum", "sClass" : "text-left"} ,{ 
					"data" : "beneficiaries", "sClass" : "text-left"} ,{
					"data" : "nameOfWork", "sClass" : "text-left"} ,{
					"data" : "winCode", "sClass" : "text-left"} ,{
					"data" : "fund", "sClass" : "text-left"} ,{
					"data" : "function", "sClass" : "text-left"} ,{ 
					"data" : "budgetHead", "sClass" : "text-left"} ,{
					"data" : "typeOfWork", "sClass" : "text-left"} ,{
					"data" : "subTypeOfWork", "sClass" : "text-left"} ,{
					"data" : "adminSanctionAuthorityDate", "sClass" : "text-left"} ,{
					"data" : "technicalSanctionAuthorityDate", "sClass" : "text-right"}, {
					"data" : "estimateAmount", "sClass" : "text-right"}, {
					"data" : "adminSanctionAmount", "sClass" : "text-right"}, {
					"data" : "modeOfAllotment", "sClass" : "text-right"}, {
					"data" : "agreementNumberDate", "sClass" : "text-right"}, {
					"data" : "contractorCodeName", "sClass" : "text-right"}, {
					"data" : "agreementAmount", "sClass" : "text-right"}, {
					"data" : "latestMbNumberDate", "sClass" : "text-right"}, {
					"data" : "latestBillNumberDate", "sClass" : "text-right"}, {
					"data" : "billType", "sClass" : "text-right"}, {
					"data" : "agreementAmount", "sClass" : "text-right"}, {
					"data" : "totalBillPaidSoFar", "sClass" : "text-right"}, {
					"data" : "balanceValueOfWorkToBill", "sClass" : "text-right"
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