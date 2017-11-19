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

var employeeCode = new Bloodhound({
    datumTokenizer: function (datum) {
        return Bloodhound.tokenizers.whitespace(datum.value);
    },
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    remote: {
        url: '/eis/reports/searchemployeecodes?code=%QUERY',
        filter: function (data) {
            return $.map(data, function (ct) {
                return {
                    name: ct
                };
            });
        }
    }
});

employeeCode.initialize();
var workIdNumber_typeahead = $('#employeeCode').typeahead({
	hint : true,
	highlight : true,
	minLength : 3
}, {
	displayKey : 'name',
	source : employeeCode.ttAdapter()
});

$(document).ready(function() {
var designation = new Bloodhound({
    datumTokenizer: function (datum) {
        return Bloodhound.tokenizers.whitespace(datum.value);
    },
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    remote: {
        url: '/eis/reports/searchdesignations?name=%QUERY',
        filter: function (data) {
            return $.map(data, function (ct) {
                return {
                    name: ct.name,
                    value: ct.id
                };
            });
        }
    }
});

designation.initialize();
var designation_typeahead = $('#designationInput').typeahead({
	hint : true,
	highlight : true,
	minLength : 3
}, {
	displayKey : 'name',
	source : designation.ttAdapter()
});

typeaheadWithEventsHandling(designation_typeahead,'#designation');

});

$(document).ready(function() {
var position = new Bloodhound({
    datumTokenizer: function (datum) {
        return Bloodhound.tokenizers.whitespace(datum.value);
    },
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    remote: {
        url: '/eis/reports/searchpositions?name=%QUERY',
        filter: function (data) {
            return $.map(data, function (ct) {
                return {
                    name: ct.name,
                    value: ct.id
                };
            });
        }
    }
});


position.initialize();
var position_typeahead = $('#positionInput').typeahead({
	hint : true,
	highlight : true,
	minLength : 3
}, {
	displayKey : 'name',
	source : position.ttAdapter()
});

typeaheadWithEventsHandling(position_typeahead,'#position');

});


function getFormData($form){
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i){
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}

jQuery(document).ready(function($) {
jQuery('#btnsearch').click(function(e) {
	var code = $('#employeeCode').val();
	var name = $('#employeeName').val();
	var department = $("#department").val();
	var designation = $("#designation").val();
	var position = $("#position").val();
	var date = $("#assignmentDate").val();
	if(code == '' && name == '' && department == '' && designation == '' && position == '') {
		bootbox.alert("Please select atleast one search criteria");
		return false;
	}
	
	var searchCriteria = "Employee Assignment Report as on  ";
	if(date != '') 
		searchCriteria +=  date+ " ";
	if(name != "")
		searchCriteria +=  "," +" Employee Name  : " + name + " ";
	if(code != '')
		searchCriteria += "," +" Employee Code  : " + code + " ";
	if(department != '')
		searchCriteria += "for Department  : " + $('#department').find(":selected").text() + " ";
	if(designation != '')
		searchCriteria += "and Designation  : " + $("#designationInput").val()+ " ";
	if(position != '')
		searchCriteria += "and Position  : " + $("#positionInput").val()+ " ";
	
	  if (searchCriteria.endsWith(" "))
		  searchCriteria = searchCriteria.substring(0, searchCriteria.length - 1);
	  
	  $('#searchCriteria').html(searchCriteria);
	  
	if($('form').valid())
		callAjaxSearch();
});
});

function goToView(obj) {
	jQuery('input[name=' + jQuery(obj).data('hiddenele') + ']')
	.val(jQuery(obj).data('eleval'));   
	/*window.open("/wtms/viewDcb/consumerCodeWis/"
			+ boundaryId, '',
			'scrollbars=yes,width=1000,height=700,status=yes');*/
	window.open("/eis/employee/view/"+jQuery('#employeeCode').val(), '', 'scrollbars=yes,width=1000,height=700,status=yes');
} 


function callAjaxSearch() {
	employeeAssignmentContainer = jQuery("#resultTable");
	jQuery('.report-section').removeClass('display-hide');
	var maxTemp = 0;
		reportdatatable = employeeAssignmentContainer
			.dataTable({
				ajax : {
					url : "/eis/reports/employeeassignments/search",      
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
					$('#btndownloadpdf').show();
					$('#btndownloadexcel').show();
				},
				columns : [  
                             {
	                            "data" : function(row, type, set, meta){
		                     	return { name:row.employeeCode, id:row.id };
	                         },
	                            "render" : function(data, type, row) {
		                         return '<a href="javascript:void(0);" onclick="goToView(this);" data-hiddenele="employeeCode" data-eleval="'
				                + data.name + '">' + data.name + '</a>';
	                           },
	                     } ,{
					"data" : "employeeName", "sClass" : "text-center"} ,{
					"data" : "primaryDepartment", "sClass" : "text-center"} ,{ 
					"data" : "primaryDesignation", "sClass" : "text-center"} ,{
					"data" : "primaryPosition", "sClass" : "text-center"} ,{
					"data" : "primaryDateRange", "sClass" : "text-center"},{ 
					 "data" : "temporaryDepartment_0", "sClass" : "text-center"} ,{
					"data" : "temporaryDesignation_0", "sClass" : "text-center"} ,{
					"data" : "temporaryPosition_0", "sClass" : "text-center"},{
					"data" : "temporaryDateRange_0", "sClass" : "text-center"},{ 
					"data" : "temporaryDepartment_1", "sClass" : "text-center"} ,{
					"data" : "temporaryDesignation_1", "sClass" : "text-center"} ,{
					"data" : "temporaryPosition_1", "sClass" : "text-center"},{
					"data" : "temporaryDateRange_1", "sClass" : "text-center"},{ 
					"data" : "temporaryDepartment_2", "sClass" : "text-center"} ,{
					"data" : "temporaryDesignation_2", "sClass" : "text-center"} ,{
					"data" : "temporaryPosition_2", "sClass" : "text-center"},{
					"data" : "temporaryDateRange_2", "sClass" : "text-center"},{ 
					"data" : "temporaryDepartment_3", "sClass" : "text-center"} ,{
					"data" : "temporaryDesignation_3", "sClass" : "text-center"} ,{
					"data" : "temporaryPosition_3", "sClass" : "text-center"},{
					"data" : "temporaryDateRange_3", "sClass" : "text-center"}],
			
			});
}

$('#btndownloadpdf').click(function() {
	var code = $('#employeeCode').val();
	var name = $('#employeeName').val();
	var department = $('#department').val();
	var designation = $('#designation').val();
	var position = $('#position').val(); 
	var date = $('#assignmentDate').val();

	window.open("/eis/reports/employeeassignments/pdf?code="+ code + "&name="+ name + "&departmentId="+ department + "&designationId="
			+ designation  + "&positionId=" + position +"&date=" + date 
			+ "&contentType=pdf", '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
});

$('#btndownloadexcel').click(function() {
	var code = $('#employeeCode').val();
	var name = $('#employeeName').val();
	var department = $('#department').val();
	var designation = $('#designation').val();
	var position = $('#position').val();
	var date = $('#assignmentDate').val();

	window.open("/eis/reports/employeeassignments/pdf?code="+ code + "&name="+ name + "&departmentId="+ department + "&designationId="
			+ designation  + "&positionId=" + position  +"&date=" + date 
			+ "&contentType=excel", '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
});
