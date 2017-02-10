/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

function validateContractorClassFormAndSubmit(){
		if($("#grade").val() == ''){
			bootbox.alert($('#contractorGradeError').val());
			return false;
		}
		if($("#description").val() == ''){
			bootbox.alert($('#contractorDescriptionError').val());
			return false;
		}
		var minAmount = $("#minAmount").val();
		var maxAmount = $("#maxAmount").val();	
		if(minAmount == ''){
			bootbox.alert($('#contractorMinamtError').val());
			return false;
		}
		if(maxAmount == ''){
			bootbox.alert($('#contractorMaxamtError').val());
			return false;
		}
		if(parseFloat(maxAmount) <= 0){
			bootbox.alert($('#gradeValidError').val());
			return false;
		}
	    if (parseFloat(maxAmount) <= parseFloat(minAmount) ) {
	    	bootbox.alert($('#gradeError').val());
	        return false;
	    }
		return true;
}

function validateMinimumAmount(elem) {
	var valid = /^[0-9](\d{0,9})(\.\d{0,2})?$/.test(jQuery(elem).val()),
    val = jQuery(elem).val();

	if(!valid){
	    console.log("Invalid input!");
	    jQuery(elem).val(val.substring(0, val.length - 1));
	}
}

function validateMaximumAmount(elem) {
	var valid = /^[0-9](\d{0,9})(\.\d{0,2})?$/.test(jQuery(elem).val()),
    val = jQuery(elem).val();

	if(!valid){
	    console.log("Invalid input!");
	    jQuery(elem).val(val.substring(0, val.length - 1));
	}
}

function validateContractorClassForm(){
	if($("#contractorClassForm").valid()){
		return validateContractorClassFormAndSubmit();
	}else{
		return false;
	}
}

if($('#minAmount').val() != ""){
	$('#minAmount').val(parseFloat($('#minAmount').val()).toFixed(2));
}
if($('#maxAmount').val() != ""){
	$('#maxAmount').val(parseFloat($('#maxAmount').val()).toFixed(2));
}

jQuery('#grade').on('input',function(){
	var regexp_grade = /[^a-zA-Z-/ ]/g;
	if($(this).val().match(regexp_grade)){
		$(this).val( $(this).val().replace(regexp_grade,'') );
	}
})

jQuery('#btnsearch').click(function(e) {
	callAjaxSearch();
});

function callAjaxSearch() {
	drillDowntableContainer = jQuery("#resultTable");
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/egworks/masters/ajaxsearch-contractorclass",
					type : "POST",
					"data" : getFormData(jQuery('form'))
				},
				"bDestroy" : true,
				'bAutoWidth' : false,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : []
				},
				"fnRowCallback" : function(row, data, index) {
					$('td:eq(0)', row).html(index + 1);
					$('td:eq(3)',row).html(parseFloat(data.minamount).toFixed(2));
					$('td:eq(4)',row).html(parseFloat(data.maxamount).toFixed(2));
					$('td:eq(5)', row).html(
							'<a href="javascript:void(0);" onclick="openContractorClass(\''
									+ data.id + '\')">Modify</a>');
					return row;
					
				},
				aaSorting : [],
				columns : [ {
					"data" : "",
					"sClass" : "text-center","sWidth": "1%"
				}, {
					"data" : "grade",
					"sWidth": "10%"
				}, {
					"data" : "description",
					"sWidth": "20%"
				}, {
					"data" : "minamount",
					"sClass" : "text-right","sWidth": "10%"
				}, {
					"data" : "maxamount",
					"sClass" : "text-right","sWidth": "10%"
				}, {
					"data" : "",
					"sClass" : "text-center","sWidth": "10%"
				}
			] });
}

function getFormData($form){
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i){
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}

function openContractorClass(contractorClassId) {
	window.location = "/egworks/masters/contractorclass-edit/"+contractorClassId;
}

function createNewContractorClass(){
	window.location = "/egworks/masters/contractorclass-newform";
}