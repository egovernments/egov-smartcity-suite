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
$(document).ready(function(){
	if($("#marketRateRow td:eq(2) input.marketratefromdate").val() == "") {
		$("#marketRateRow td input.marketrate").val('0.0');
		$("#marketRateRow").hide();
	}
	else
		$("#marketRateRow").show();
	
	if(parseFloat($(".marketratetable tr td:eq(1)").html()) == 0){
		$(".marketrateview").hide();
	}
	
	$('.textfieldsvalidate').on('input',function(){
		var regexp_textfields = /[^0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]/g;
		if($(this).val().match(regexp_textfields)){
			$(this).val( $(this).val().replace(regexp_textfields,'') );
		}
	});
	
	if($("#abstractEstimateExists").val() == "true" || $("#workOrderEstimateExists").val() == "true"){
		$($(".sorrate")[0]).attr('readonly', true);
		$($(".sorratefromdate")[0]).attr('readonly', true);
		$($(".deletesorrate")[0]).hide();
	}
});

$('#submitBtn').click(function() {
	if ($('#scheduleOfRateForm').valid()) {
		var isSuccess=true;
		if($("#marketRateRow").is(":not(':hidden')")){
			var marketRateElements = $(".marketrate")
			for (var i=0;i<marketRateElements.length;i++){
				if(!$.isNumeric($(marketRateElements[i]).val()) || parseFloat($(marketRateElements[i]).val()) == 0){
					bootbox.alert($("#marketRateErrorMessage").val());
					isSuccess = false;
					break;
				}
			}
		}
		var sorRateElements = $(".sorrate")
		for (var j=0;j<sorRateElements.length;j++){
			if(!$.isNumeric($(sorRateElements[j]).val()) || parseFloat($(sorRateElements[j]).val()) == 0){
				bootbox.alert($("#sorRateErrorMessage").val());
				isSuccess = false;
				break;
			}
		}
		
		if(!checkSorRateStartDateAndEndDate())
			isSuccess = false;
		
		if(!checkMarketRateStartDateAndEndDate())
			isSuccess = false;
		
		if(!checkSorRateDateBetweenStages())
			isSuccess = false;
		
		if(!checkMarketRateDateBetweenStages())
			isSuccess = false;
		
		if(isSuccess)
			return true;
	}
	return false;
});

$('#btnsearch').click(function() {
	if ($('#scheduleOfRateSearchRequestForm').valid()) {
		callAjaxSearch();
		return true;
	}
	return false;
});

function checkStartDateAndEndDate(tblId,fromDateElements,toDateElements) {
	var checkDateFlag = true;
	$.each(toDateElements, function(index) {
		var fromDateValue = $(fromDateElements[index]).val().split("/");
		var toDateValue = $(this).val().split("/");
		if ($(this).val() != ""
				&& (new Date(fromDateValue[2], fromDateValue[1] - 1,
						fromDateValue[0]) > new Date(toDateValue[2],
						toDateValue[1] - 1, toDateValue[0]))) {
			if(tblId == "tblsorrate")
				bootbox.alert($("#sorInvalidDateRangeErrorMessage").val());
			else
				bootbox.alert($("#marketRateInvalidDateRangeErrorMessage").val());
			checkDateFlag = false;
			return false;
		}
	});
	if (checkDateFlag)
		return true;
	else
		return false;
}

function checkStartDateAndEndDateBetweenStages(tblId){
	var rowcount=$("#"+tblId+" tbody tr").length;
	for(var i=1;i<= rowcount;i++){
		var temp = i-1;
		if(tblId == "tblsorrate"){
			var sorPreviousStageEndDateVal = $("#tempSorRates"+temp+"\\.validity\\.endDate").val();
			var sorCurrentStageStartDateVal = $("#tempSorRates"+i+"\\.validity\\.startDate").val();
			if(sorPreviousStageEndDateVal != "" && sorCurrentStageStartDateVal != undefined){
				var sorPreviousStageEndDate = sorPreviousStageEndDateVal.split("/");
				var sorCurrentStageStartDate = sorCurrentStageStartDateVal.split("/");
				if(new Date(sorPreviousStageEndDate[2], sorPreviousStageEndDate[1] - 1,
						sorPreviousStageEndDate[0]) > new Date(sorCurrentStageStartDate[2],
								sorCurrentStageStartDate[1] - 1, sorCurrentStageStartDate[0])){
						bootbox.alert($("#sorStartEndDateErrorMessage").val());
						return false;
				}
		}
	}else if(tblId == "tblmarketrate"){
			var marketPreviousStageEndDateVal = $("#tempMarketRates"+temp+"\\.validity\\.endDate").val();
			var marketCurrentStageStartDateVal = $("#tempMarketRates"+i+"\\.validity\\.startDate").val();
			if(marketPreviousStageEndDateVal != "" && marketCurrentStageStartDateVal != undefined){
				var marketPreviousStageEndDate = marketPreviousStageEndDateVal.split("/");
				var marketCurrentStageStartDate = marketCurrentStageStartDateVal.split("/");
				if(new Date(marketPreviousStageEndDate[2], marketPreviousStageEndDate[1] - 1,
					marketPreviousStageEndDate[0]) > new Date(marketCurrentStageStartDate[2],
							marketCurrentStageStartDate[1] - 1, marketCurrentStageStartDate[0])){
					bootbox.alert($("#marketRateStartEndDateErrorMessage").val());
					return false;
			}
	}
		}
	}
	return true;
}

function checkSorRateDateBetweenStages(){
	return checkStartDateAndEndDateBetweenStages("tblsorrate");
}

function checkMarketRateDateBetweenStages(){
	if($("#marketRateRow").is(":not(':hidden')")){
		return checkStartDateAndEndDateBetweenStages("tblmarketrate");
	}else
		return true;
}

function checkSorRateStartDateAndEndDate(){
	return checkStartDateAndEndDate("tblsorrate",$(".sorratefromdate"),$(".sorratetodate"));
}

function checkMarketRateStartDateAndEndDate(){
	if($("#marketRateRow").is(":not(':hidden')")){
		return checkStartDateAndEndDate("tblmarketrate",$(".marketratefromdate"),$(".marketratetodate"));
	}else
		return true;
}

var sorRateRowIndex = 0;
function addSorRate() {
	addRow("tblsorrate","sorRateRow");
	if($("#abstractEstimateExists").val() == "true" ||  $("#workOrderEstimateExists").val() == "true"){
		sorRateRowIndex++;
		$($(".sorrate")[sorRateRowIndex]).attr('readonly', false);
		$($(".sorratefromdate")[sorRateRowIndex]).attr('readonly', false);
		$($(".deletesorrate")[sorRateRowIndex]).show();
	}
}

function addMarketRate(){
	if($("#marketRateRow").is(":visible")) 
		addRow("tblmarketrate","marketRateRow");
	$("#marketRateRow").show();
}

function deleteSorRateRow(obj){
	deleteRow(obj,"tblsorrate");
	if($("#abstractEstimateExists").val() == "true" ||  $("#workOrderEstimateExists").val() == "true")
		sorRateRowIndex--;
}

function deleteMarketRateRow(obj){
	var rowcount=$("#tblmarketrate tbody tr").length;
	if(rowcount == 1){
		$("#marketRateRow td").eq(0).find("input").val('');
		$("#marketRateRow td").eq(1).find("input").val('0.0');
		$("#marketRateRow td").eq(2).find("input").val('');
		$("#marketRateRow td").eq(3).find("input").val('');
		$("#marketRateRow").hide();
	}else{
		deleteRow(obj,"tblmarketrate");
	}
}

function addRow(tblId,rowId){
	var rowcount = $("#"+tblId+" tbody tr").length;
	if (rowcount < 30) {
		if ($('#'+rowId+'') != null) {
			// get Next Row Index to Generate
			var nextIdx = 0;
			nextIdx = $("#"+tblId+" tbody tr").length;
			
			// validate status variable for exiting function
			var isValid = 1;// for default have success value 0

			// validate existing rows in table
			$("#"+tblId+" tbody tr").find('input').each(
					function() {
						if (($(this).data('optional') === 0)
								&& (!$(this).val())) {
							$(this).focus();
							bootbox.alert($(this).data('errormsg'));
							isValid = 0;// set validation failure
							return false;
						}
			});

			if (isValid === 0) {
				return false;
			}
			
			// Generate all textboxes Id and name with new index
			$("#"+rowId+"").clone().find("input, errors").each(
					function() {

						if ($(this).data('server')) {
							$(this).removeAttr('data-server');
						}
						
							$(this).attr(
									{
										'id' : function(_, id) {
											return id.replace(/\d+/, nextIdx);
										},
										'name' : function(_, name) {
											return name.replace(/\d+/, nextIdx);
										},
										'data-idx' : function(_,dataIdx)
										{
											return nextIdx;
										}
									});

							// if element is static attribute hold values for
							// next row, otherwise it will be reset
							if (!$(this).data('static')) {
								$(this).val('');
								// set default selection for dropdown
								if ($(this).is("select")) {
									$(this).prop('selectedIndex', 0);
								}
							}
							
							$(this).removeAttr('disabled');
							$(this).prop('checked', false);

					}).end().appendTo("#"+tblId+" tbody");
			
			if(tblId == "tblmarketrate")
				generateSno("marketrate");
			else
				generateSno("sorrate");
			initializeDatePicker();
		}
	} else {
		  bootbox.alert('limit reached!');
	}
}

function deleteRow(obj,tblId) {
    var rIndex = getRow(obj).rowIndex;
    
	var tbl=document.getElementById(''+tblId+'');	
	var rowcount=$("#"+tblId+" tbody tr").length;

    if(tblId != "tblmarketrate" && rowcount<=1) {
		bootbox.alert($("#rowDeleteErrorMessage").val());
		return false;
	} else {
		tbl.deleteRow(rIndex);
		//starting index for table fields
		var idx= 0;
		var sno = 1;
		//regenerate index existing inputs in table row
		jQuery("#"+tblId+" tbody tr").each(function() {
		
				jQuery(this).find("input, select, textarea, errors, span, input:hidden").each(function() {
					var classval = jQuery(this).attr('class');
					if(classval == 'spansno') {
						jQuery(this).text(sno);
						sno++;
					} else {
					jQuery(this).attr({
					      'name': function(_, name) {
					    	  if(!(jQuery(this).attr('name')===undefined))
					    		  return name.replace(/\[.\]/g, '['+ idx +']'); 
					      },
					      'id': function(_, id) {
					    	  if(!(jQuery(this).attr('id')===undefined))
					    		  return id.replace(/\[.\]/g, '['+ idx +']'); 
					      },
					      'class' : function(_, name) {
								if(!(jQuery(this).attr('class')===undefined))
									return name.replace(/\[.\]/g, '['+ idx +']'); 
							},
						  'data-idx' : function(_,dataIdx)
						  {
							  return idx;
						  }
					   });
					}
			    });
				
				idx++;
		});
		if(tblId == "tblmarketrate")
			generateSno("marketrate");
		else
			generateSno("sorrate");
		initializeDatePicker();
		return true;
	}	
}

function callAjaxSearch() {
	drillDowntableContainer = jQuery("#resultTable");
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
	.dataTable({
		ajax : {
			url : "/egworks/masters/scheduleofrate-searchdetails",
			type : "POST",
			"data" : getFormData(jQuery('form'))
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
			$('td:eq(0)',row).html(index+1);
			$('td:eq(7)', row).html(
					'<a href="javascript:void(0);" onclick="modifyScheduleOfRate(\''
							+ data.scheduleOfRateId + '\')">Modify</a>');
			return row;
		},
		aaSorting : [],
		columns : [{
				"data" : ""} ,{
				"data" : "sorCode"},{
				"data" : "unitOfMeasure"},{
				"data" : "sorDescription"},{
				"data" : "rate"},{
				"data" : "fromDate"},{
				"data" : "toDate"},{
				"data" : ""
				}]
	});
}

function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}

function generateSno(tblName)
{
	var idx=1;
	$("."+tblName+"spansno").each(function(){
		$(this).text(idx);
		idx++;
	});
}

function getRow(obj) {
	if(!obj)
		return null;
	tag = obj.nodeName.toUpperCase();
	while(tag != 'BODY'){
		if (tag == 'TR') 
			return obj;
		obj=obj.parentNode ;
		tag = obj.nodeName.toUpperCase();
	}
	return null;
}

function initializeDatePicker()
{
	$(".datepicker").datepicker({
		format: "dd/mm/yyyy",
		autoclose:true
	});
	try { $(".datepicker").inputmask(); }catch(e){}	
}

function createNewScheduleOfRate(){
	window.location = "scheduleofrate-newform";
}

function modifyScheduleOfRate(scheduleOfRateId) {
	window.location = '/egworks/masters/scheduleofrate-edit/'+scheduleOfRateId;
}