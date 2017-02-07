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
var hint='<a href="#" class="hintanchor" title="@fulldescription@"><i class="fa fa-question-circle" aria-hidden="true"></i></a>';
var isDatepickerOpened=false;
$(document).ready(function(){
	if($("#isMBHeaderEditable") && $("#isMBHeaderEditable").val()!="" && $("#isMBHeaderEditable").val()=="false"){
		$("#mbHeaderTab").find('input,button,textarea').prop('disabled',true);
		/*$("#searchAndAdd").addClass('hide');
		$("#addAll").addClass('hide');
		$("#searchNonTenderedAndAdd").addClass('hide');
		$("#addAllNonTendered").addClass('hide');
		$(".sordelete").addClass('hide');
		$(".nonTenderedDelete").addClass('hide');
		$(".nonSorDelete").addClass('hide');
		$(".lumpSumDelete").addClass('hide');*/
	}
		
	sorTotal();
	nonSorTotal();
	nonTenderedTotal();
	lumpSumTotal();
	
	if($('#isMeasurementsExist').val() == 'false') {
		$('.openCloseAll').hide();
	}
	
	 jQuery( "#mbDate" ).datepicker({ 
    	 format: 'dd/mm/yyyy',
    	 autoclose:true,
         onRender: function(date) {
      	    return date.valueOf() < now.valueOf() ? 'disabled' : '';
      	  }
	  }).on('changeDate', function(ev) {
		  var string=jQuery(this).val();
		  if(!(string.indexOf("_") > -1)){
			  var mbDate = $('#mbDate').val();
				bootbox.alert("Change in MB Entry date will reset the Tendered and non tendered items data");
				clearAllTenderedAndNonTenderedItems();
		  }
		  
	  }).data('datepicker');
	 var defaultDepartmentId = $("#defaultDepartmentId").val();
	 if (defaultDepartmentId != "") {
		$("#approvalDepartment").val(defaultDepartmentId);
		$('#approvalDepartment').trigger('change');
	 }
});

function clearAllTenderedAndNonTenderedItems(){
	var inVisibleNonTenderedCount = $("#tblNonTendered > tbody > tr[sorinvisible='true']").length;
	var inVisibleLumpSumCount = $("#tbllumpSum > tbody > tr[nonsorinvisible='true']").length;
	var nonTenderedCount = $("#tblNonTendered > tbody > tr[id='nonTenderedRow']").length;
	var lumpSumCount = $("#tblLumpSum > tbody > tr[id='lumpSumRow']").length;
	if(inVisibleNonTenderedCount != 1){
		for (var i = 0; i < nonTenderedCount; i++) {
			var tbl=document.getElementById('nonTenderedTable');
			var objects = $('.nonTenderedDelete');
			deleteNonTendered(objects[i]);
		}
	}
	if(inVisibleLumpSumCount != 1){
		for (var i = 0; i < lumpSumCount; i++) {
			var tbl=document.getElementById('lumpSumTable');
			var objects = $('.lumpSumDelete');
			deleteLumpSum(objects[i]);
		}
	}
}

$('#ContractorMeasurements').click(function(event) {
	event.preventDefault();
	$('#contractorMeasurementsTab').removeClass("hide");
	$('#contractorMeasurementsTab a').tab('show');
	$.ajax({
		method : "GET",
		url : "/egworks/mbheader/ajaxcontractormbheaders",
		data : {
			workOrderEstimateId : $('#workOrderEstimateId').val()
		},
		async : true
	}).done(
			function(response) {
				if(response.length > 0){
					$('#contractorMbs tbody').empty();
					var output = '';
					$.each(response, function(index, value) {
						var mbDate = new Date(value.mbDate);
						var monthVal = mbDate.getMonth() + 1;
						var date = (mbDate.getDate() > 9) ? mbDate.getDate() : 0 + "" + mbDate.getDate();
						var month = (monthVal > 9) ? monthVal : 0 + "" + monthVal;
						output = '<tr>';
						output = output + '<td class="text-center"> <a href="javascript:void(0);" onclick="viewContractorMB('+value.id+')">' + value.mbRefNo + '</a></td>'
						output = output + '<td class="text-center">' + date + "/" + month + "/" + mbDate.getFullYear() + '</td>'
						output = output + '<td class="text-right">' + value.mbAmount + '</td>'
						output = output + '</tr>';
						$('#contractorMbs tbody').append(output);
					});
				}else{
					$('#contractorMbs tbody').empty();
					var output = '';
					output = '<tr>';
					output = output + '<td class="text-center">NA</td>'
					output = output + '<td class="text-center">NA</td>'
					output = output + '<td class="text-right">NA</td>'
					output = output + '</tr>';
					$('#contractorMbs tbody').append(output);
				}
		});
});
$('#searchAndAdd').click(function() {
	var workOrderEstimateId = $('#workOrderEstimateId').val();
	var workOrderNumber = $('#workOrderNumber').html();
	var mbHeaderId = $('#id').val();
	if(mbHeaderId == '')
		mbHeaderId = -1;
	
	window.open("/egworks/measurementbook/searchactivityform?woeId=" + workOrderEstimateId + "&workOrderNo=" + workOrderNumber + "&mbHeaderId=" + mbHeaderId, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
});

$('#addAll').click(function() {
	var workOrderEstimateId = $('#workOrderEstimateId').val();
	var mbHeaderId = $('#id').val();
	if(mbHeaderId == '')
		mbHeaderId = -1;
	var selectedActivities = "";
	
	$('.loader-class').modal('show', {backdrop: 'static'});
	$.ajax({
		type: "POST",
		url: "/egworks/measurementbook/ajax-searchactivities",
		cache: true,
		dataType: "json",
		"data": {"workOrderEstimateId" : workOrderEstimateId,
			"id" : mbHeaderId},
		success: function (data) {
			populateActivities(data, selectedActivities);
		},
		error: function (error) {
			console.log(error.responseText.slice(0,-2));
			var json = $.parseJSON(error.responseText.slice(0,-2));
			
			$.each(json, function(key, value){
				$('#errorMessage').append(value + '</br>');
			});
			$('#errorMessage').show();
		}
	});
	$('.loader-class').modal('hide');
});

$('#searchNonTenderedAndAdd').click(function() {
	var mbDate = $('#mbDate').val();
	if(mbDate!=""){
		var workOrderEstimateId = $('#workOrderEstimateId').val();
		var workOrderNumber = $('#workOrderNumber').html();
		var mbHeaderId = $('#id').val();
		if(mbHeaderId == '')
			mbHeaderId = -1;
		window.open("/egworks/measurementbook/searchreactivityform?woeId=" + workOrderEstimateId + "&workOrderNo=" + workOrderNumber + "&mbHeaderId=" + mbHeaderId + "&mbDate="+mbDate ,'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}else{
		bootbox.alert("Please select MB Entry date first");
	}
});

$('#addAllNonTendered').click(function() {
	var mbDate = $('#mbDate').val();
	if(mbDate!=""){
		var workOrderEstimateId = $('#workOrderEstimateId').val();
		var mbHeaderId = $('#id').val();
		if(mbHeaderId == '')
			mbHeaderId = -1;
		var selectedActivities = "";
		
		$('.loader-class').modal('show', {backdrop: 'static'});
		$.ajax({
			type: "POST",
			url: "/egworks/measurementbook/ajax-searchreactivities",
			cache: true,
			dataType: "json",
			"data": {"workOrderEstimateId" : workOrderEstimateId,
				"id" : mbHeaderId,"mbDate" : mbDate},
			success: function (data) {
				populateREActivities(data, selectedActivities);
			},
			error: function (error) {
				console.log(error.responseText.slice(0,-2));
				var json = $.parseJSON(error.responseText.slice(0,-2));
				
				$.each(json, function(key, value){
					$('#errorMessage').append(value + '</br>');
				});
				$('#errorMessage').show();
			}
		});
		$('.loader-class').modal('hide');
	}else{
		bootbox.alert("Please select MB Entry date first");
	}
});

function getRow(obj) {
	if(!obj)return null;
	tag = obj.nodeName.toUpperCase();
	while(tag != 'BODY'){
		if (tag == 'TR') return obj;
		obj=obj.parentNode ;
		tag = obj.nodeName.toUpperCase();
	}
	return null;
}

function populateActivities(data, selectedActivities) {
	populateData(data, selectedActivities);
}

function populateREActivities(data, selectedActivities) {
	var lumpSumCheck  = false;
	var addAll = false;
	if(selectedActivities == "")
		addAll = true;
	
	var activityArray = selectedActivities.split(",");
	var existingActivityArray = [];
	$('.nonTenderedworkOrderActivity, .lumpSumWorkOrderActivity').each(function() {
		existingActivityArray.push($(this).val());
	});
	if(!addAll) {
		var nonTenderedCount = $("#tblNonTendered > tbody > tr:visible[id='nonTenderedRow']").length;
		var lumpSumCount = $("#tblLumpSum > tbody > tr:visible[id='lumpSumRow']").length;
		$(data.data).each(function(index, workOrderActivity){
			if($.inArray("" + workOrderActivity.id + "", existingActivityArray) == -1
					&& $.inArray("" + workOrderActivity.id + "", activityArray) != -1) {
				if(nonTenderedCount==0 && workOrderActivity.sorNonSorType == 'SOR'){
					$('#nonTenderedMessage').prop("hidden",true);
					$('#nonTenderedRow').removeAttr("hidden");
					$('#nonTenderedRow').removeAttr('sorinvisible');
				}else{
					if(workOrderActivity.sorNonSorType == 'SOR'){
						var key = $("#tblNonTendered > tbody > tr:visible[id='nonTenderedRow']").length;
						addRow('tblNonTendered', 'nonTenderedRow');
						resetIndexes();
						$('#nonTenderedMbDetailsId_' + key).val('');
						$('#nonTenderedquantity_' + key).val('');
						$('.nonTenderedcumulativeIncludingCurrentEntry_' + key).html('');
						$('.nonTenderedAmountCurrentEntry_' + key).html('');
						$('.nonTenderedamountIncludingCurrentEntry_' + key).html('');
						$('#nonTenderedremarks_' + key).val('');
						generateSlno('spannontenderedslno');	
					}else{
						if (lumpSumCount > 0)
							lumpSumCheck = true;
						if(!lumpSumCheck){
							$('#lumpSumMessage').prop("hidden",true);
							$('#lumpSumRow').removeAttr("hidden");
							$('#lumpSumRow').removeAttr('nonsorinvisible');
						}
						if(lumpSumCheck) {
							var key = $("#tblLumpSum > tbody > tr:visible[id='lumpSumRow']").length;
							addRow('tblLumpSum', 'lumpSumRow');
							resetIndexes();
							$('#lumpSumMbDetailsId_' + key).val('');
							$('#lumpSumQuantity_' + key).val('');
							$('.lumpSumCumulativeIncludingCurrentEntry_' + key).html('');
							$('.lumpSumAmountCurrentEntry_' + key).html('');
							$('.lumpSumAmountIncludingCurrentEntry_' + key).html('');
							$('#lumpSumRemarks_' + key).val('');
							generateSlno('spanlumpsumslno');
						}
						lumpSumCheck = true;
					}
				}
				if(workOrderActivity.sorNonSorType == 'SOR'){
					$('#nonTenderedworkOrderActivity_' + nonTenderedCount).val(workOrderActivity.id);
					$('.nonTenderedsorCategory_' + nonTenderedCount).html(workOrderActivity.categoryType);
					$('.nonTenderedsorCode_' + nonTenderedCount).html(workOrderActivity.sorCode);
					$('.nonTenderedsummary_' + nonTenderedCount).html(workOrderActivity.summary);
					$('.nonTendereddescription_' + nonTenderedCount).html(hint.replace(/@fulldescription@/g, workOrderActivity.description));
					$('.nonTendereduom_' + nonTenderedCount).html(workOrderActivity.uom);
					$('.nonTenderedapprovedQuantity_' + nonTenderedCount).html(workOrderActivity.approvedQuantity);
					$('.nonTenderedapprovedRate_' + nonTenderedCount).html(parseFloat(workOrderActivity.estimateRate).toFixed(2));
					$('#nonTenderedunitRate_' + nonTenderedCount).val(workOrderActivity.unitRate);
					$('.nonTenderedapprovedAmount_' + nonTenderedCount).html(parseFloat(workOrderActivity.activityAmount).toFixed(2));
					$('.nonTenderedcumulativePreviousEntry_' + nonTenderedCount).html(workOrderActivity.cumulativePreviousEntry);
					if (workOrderActivity.woms != "") {
						$('#nonTenderedquantity_' + nonTenderedCount).attr('readonly', 'readonly');
						document.getElementById('nonTenderedMbDetails[' + nonTenderedCount + '].msadd').removeAttribute('style');
						var newrow= $('#msheaderrowtemplate').html();
						
						newrow = newrow.replace(/findNet/g, 'findNonTenderedNet');
						newrow=  newrow.replace(/msrowtemplate/g, 'msrownonTenderedMbDetails[' + nonTenderedCount + ']');
						newrow=  newrow.replace(/templatesorMbDetails\[0\]/g, 'nonTenderedMbDetails[' + nonTenderedCount + ']');
						newrow = newrow.replace(/templatesorMbDetails_0/g, 'nonTenderedMbDetails_' + nonTenderedCount);
						newrow = newrow.replace(/templatemssubmit_0/g, 'mssubmit_' + nonTenderedCount);
						newrow = newrow.replace('msrowslNo_0_0', 'nontenderedmsrowslNo_' + nonTenderedCount + '_0');
						newrow = newrow.replace('msrowremarks_0_0', 'nontenderedmsrowremarks_' + nonTenderedCount + '_0');
						newrow = newrow.replace('msrowno_0_0', 'nontenderedmsrowno_' + nonTenderedCount + '_0');
						newrow = newrow.replace('msrowlength_0_0', 'nontenderedmsrowlength_' + nonTenderedCount + '_0');
						newrow = newrow.replace('msrowwidth_0_0', 'nontenderedmsrowwidth_' + nonTenderedCount + '_0');
						newrow = newrow.replace('msrowdepthOrHeight_0_0', 'nontenderedmsrowdepthOrHeight_' + nonTenderedCount + '_0');
						newrow = newrow.replace('msrowquantity_0_0', 'nontenderedmsrowquantity_' + nonTenderedCount + '_0');
						newrow = newrow.replace('msrowidentifier_0_0', 'nontenderedmsrowidentifier_' + nonTenderedCount + '_0');
						newrow = newrow.replace('msrowmbmsPreviousEntry_0_0', 'nontenderedmsrowmbmsPreviousEntry_' + nonTenderedCount + '_0');
						document.getElementById('nonTenderedMbDetails[' + nonTenderedCount + '].mstd').innerHTML=newrow;
						$(workOrderActivity.woms).each(function(index, measurementSheet){
							if (index > 0) {
								var msrowname= "nonTenderedMbDetails[" + nonTenderedCount + "].mstable";
								var msrownameid=msrowname.split(".")[0];
								var rep='measurementSheets\['+ index +'\]';

								var $newrow = "<tr>"+$('#msrowtemplate').html()+"</tr>";
								newrow = newrow.replace(/findNet/g, 'findNonTenderedNet');
								$newrow = $newrow.replace(/templatesorMbDetails\[0\]/g,msrownameid);
								$newrow = $newrow.replace(/measurementSheets\[0\]/g,rep);
								$newrow = $newrow.replace(/templatesorMbDetails_0/g, 'nonTenderedMbDetails_' + nonTenderedCount);
								$newrow = $newrow.replace(/measurementSheets_0_id/g, 'measurementSheets_' + index + '_id');
								$newrow = $newrow.replace(/measurementSheets_0_woMeasurementSheet/g, 'measurementSheets_' + index + '_woMeasurementSheet');
								$newrow = $newrow.replace('msrowslNo_0_0', 'nontenderedmsrowslNo_' + nonTenderedCount + '_' + index);
								$newrow = $newrow.replace('msrowremarks_0_0', 'nontenderedmsrowremarks_' + nonTenderedCount + '_' + index);
								$newrow = $newrow.replace('msrowno_0_0', 'nontenderedmsrowno_' + nonTenderedCount + '_' + index);
								$newrow = $newrow.replace('msrowlength_0_0', 'nontenderedmsrowlength_' + nonTenderedCount + '_' + index);
								$newrow = $newrow.replace('msrowwidth_0_0', 'nontenderedmsrowwidth_' + nonTenderedCount + '_' + index);
								$newrow = $newrow.replace('msrowdepthOrHeight_0_0', 'nontenderedmsrowdepthOrHeight_' + nonTenderedCount + '_' + index);
								$newrow = $newrow.replace('msrowquantity_0_0', 'nontenderedmsrowquantity_' + nonTenderedCount + '_' + index);
								$newrow = $newrow.replace('msrowidentifier_0_0', 'nontenderedmsrowidentifier_' + nonTenderedCount + '_' + index);
								$newrow = $newrow.replace('msrowmbmsPreviousEntry_0_0', 'nontenderedmsrowmbmsPreviousEntry_' + nonTenderedCount + '_' + index);
								$newrow = $newrow.replace('value="1"','value="'+(index+1)+'"');
								$('.mssubmit_' + nonTenderedCount).closest('tr').before($newrow);

								patternvalidation();
							}
							$.each(measurementSheet, function(key, value){
								if(key == "womsid") {
									$('#nonTenderedMbDetails_' + nonTenderedCount + '_measurementSheets_' + index + '_woMeasurementSheet').attr('value', value);
								} else if(key == "id") {
									$('#nonTenderedMbDetails_' + nonTenderedCount + '_measurementSheets_' + index + '_id').attr('value', value);
								} else if(key == "identifier") {
									if(value == 'A')
										$('#nontenderedmsrowidentifier_' + nonTenderedCount + '_' + index).html('No');
									else
										$('#nontenderedmsrowidentifier_' + nonTenderedCount + '_' + index).html('Yes');
								} else
									$('#nontenderedmsrow' + key + '_' + nonTenderedCount + '_' + index).html(value);
							});
						});
					} else {
						document.getElementById('nonTenderedMbDetails[' + nonTenderedCount + '].msadd').style.display = 'none';
						$('#nonTenderedquantity_' + nonTenderedCount).removeAttr('readonly');
					}
					nonTenderedCount++;
				}else{
					$('#lumpSumWorkOrderActivity_' + lumpSumCount).val(workOrderActivity.id);
					$('.lumpSumSummary_' + lumpSumCount).html(workOrderActivity.summary);
					$('.lumpSumDescription_' + lumpSumCount).html(hint.replace(/@fulldescription@/g, workOrderActivity.description));
					$('.lumpSumUom_' + lumpSumCount).html(workOrderActivity.uom);
					$('.lumpSumApprovedQuantity_' + lumpSumCount).html(workOrderActivity.approvedQuantity);
					$('.lumpSumApprovedRate_' + lumpSumCount).html(parseFloat(workOrderActivity.estimateRate).toFixed(2));
					$('#lumpSumUnitRate_' + lumpSumCount).val(workOrderActivity.unitRate);
					$('.lumpSumApprovedAmount_' + lumpSumCount).html(parseFloat(workOrderActivity.activityAmount).toFixed(2));
					$('.lumpSumCumulativePreviousEntry_' + lumpSumCount).html(workOrderActivity.cumulativePreviousEntry);
					if (workOrderActivity.woms != "") {
						$('#lumpSumQuantity_' + lumpSumCount).attr('readonly', 'readonly');
						document.getElementById('lumpSumMbDetails[' + lumpSumCount + '].msadd').removeAttribute('style');
						var newrow= $('#msheaderrowtemplate').html();

						newrow = newrow.replace(/findNet/g, 'findLumpSumNet');
						newrow=  newrow.replace(/msrowtemplate/g, 'msrowLumpSumMbDetails[' + lumpSumCount + ']');
						newrow=  newrow.replace(/templatesorMbDetails\[0\]/g, 'lumpSumMbDetails[' + lumpSumCount + ']');
						newrow = newrow.replace(/templatesorMbDetails_0/g, 'lumpSumMbDetails_' + lumpSumCount);
						newrow = newrow.replace(/templatemssubmit_0/g, 'lumpsummssubmit_' + lumpSumCount);
						newrow = newrow.replace('msrowslNo_0_0', 'lumpsummsrowslNo_' + lumpSumCount + '_0');
						newrow = newrow.replace('msrowremarks_0_0', 'lumpsummsrowremarks_' + lumpSumCount + '_0');
						newrow = newrow.replace('msrowno_0_0', 'lumpsummsrowno_' + lumpSumCount + '_0');
						newrow = newrow.replace('msrowlength_0_0', 'lumpsummsrowlength_' + lumpSumCount + '_0');
						newrow = newrow.replace('msrowwidth_0_0', 'lumpsummsrowwidth_' + lumpSumCount + '_0');
						newrow = newrow.replace('msrowdepthOrHeight_0_0', 'lumpsummsrowdepthOrHeight_' + lumpSumCount + '_0');
						newrow = newrow.replace('msrowquantity_0_0', 'lumpsummsrowquantity_' + lumpSumCount + '_0');
						newrow = newrow.replace('msrowidentifier_0_0', 'lumpsummsrowidentifier_' + lumpSumCount + '_0');
						newrow = newrow.replace('msrowmbmsPreviousEntry_0_0', 'lumpsummsrowmbmsPreviousEntry_' + lumpSumCount + '_0');
						document.getElementById('lumpSumMbDetails[' + lumpSumCount + '].mstd').innerHTML=newrow;
						$(workOrderActivity.woms).each(function(index, measurementSheet){
							if (index > 0) {
								var msrowname= "lumpSumMbDetails[" + lumpSumCount + "].mstable";
								var msrownameid=msrowname.split(".")[0];
								var rep='measurementSheets\['+ index +'\]';

								var $newrow = "<tr>"+$('#msrowtemplate').html()+"</tr>";
								$newrow = $newrow.replace(/findNet/g, 'findLumpSumNet');
								$newrow = $newrow.replace(/templatesorMbDetails\[0\]/g,msrownameid);
								$newrow = $newrow.replace(/measurementSheets\[0\]/g,rep);
								$newrow = $newrow.replace(/templatesorMbDetails_0/g, 'lumpSumMbDetails_' + lumpSumCount);
								$newrow = $newrow.replace(/measurementSheets_0_id/g, 'measurementSheets_' + index + '_id');
								$newrow = $newrow.replace(/measurementSheets_0_woMeasurementSheet/g, 'measurementSheets_' + index + '_woMeasurementSheet');
								$newrow = $newrow.replace('msrowslNo_0_0', 'lumpsummsrowslNo_' + lumpSumCount + '_' + index);
								$newrow = $newrow.replace('msrowremarks_0_0', 'lumpsummsrowremarks_' + lumpSumCount + '_' + index);
								$newrow = $newrow.replace('msrowno_0_0', 'lumpsummsrowno_' + lumpSumCount + '_' + index);
								$newrow = $newrow.replace('msrowlength_0_0', 'lumpsummsrowlength_' + lumpSumCount + '_' + index);
								$newrow = $newrow.replace('msrowwidth_0_0', 'lumpsummsrowwidth_' + lumpSumCount + '_' + index);
								$newrow = $newrow.replace('msrowdepthOrHeight_0_0', 'lumpsummsrowdepthOrHeight_' + lumpSumCount + '_' + index);
								$newrow = $newrow.replace('msrowquantity_0_0', 'lumpsummsrowquantity_' + lumpSumCount + '_' + index);
								$newrow = $newrow.replace('msrowidentifier_0_0', 'lumpsummsrowidentifier_' + lumpSumCount + '_' + index);
								$newrow = $newrow.replace('msrowmbmsPreviousEntry_0_0', 'lumpsummsrowmbmsPreviousEntry_' + lumpSumCount + '_' + index);
								$newrow = $newrow.replace('value="1"','value="'+(index+1)+'"');
								$('.lumpsummssubmit_' + lumpSumCount).closest('tr').before($newrow);

								patternvalidation();
							}
							$.each(measurementSheet, function(key, value){
								if(key == "womsid") {
									$('#lumpSumMbDetails_' + lumpSumCount + '_measurementSheets_' + index + '_woMeasurementSheet').attr('value', value);
								} else if(key == "id") {
									$('#lumpSumMbDetails_' + lumpSumCount + '_measurementSheets_' + index + '_id').attr('value', value);
								} else if(key == "identifier") {
									if(value == 'A')
										$('#lumpsummsrowidentifier_' + lumpSumCount + '_' + index).html('No');
									else
										$('#lumpsummsrowidentifier_' + lumpSumCount + '_' + index).html('Yes');
								} else
									$('#lumpsummsrow' + key + '_' + lumpSumCount + '_' + index).html(value);
							});
						});
					} else {
						document.getElementById('lumpSumMbDetails[' + lumpSumCount + '].msadd').style.display = 'none';
						$('#lumpSumQuantity_' + lumpSumCount).removeAttr('readonly');
					}
					lumpSumCount++;
				}
			}
		});
	} else {
		var nonTenderedCount = $("#tblNonTendered > tbody > tr:visible[id='nonTenderedRow']").length;
		var lumpSumCount = $("#tblLumpSum > tbody > tr:visible[id='lumpSumRow']").length;
		$(data.data).each(function(index, workOrderActivity){
			if($.inArray("" + workOrderActivity.id + "", existingActivityArray) == -1) {
				if(nonTenderedCount==0 && workOrderActivity.sorNonSorType == 'SOR'){
					$('#nonTenderedMessage').prop("hidden",true);
					$('#nonTenderedRow').removeAttr("hidden");
					$('#nonTenderedRow').removeAttr('sorinvisible');
				}else{
					if(workOrderActivity.sorNonSorType == 'SOR'){
						var key = $("#tblNonTendered > tbody > tr:visible[id='nonTenderedRow']").length;
						addRow('tblNonTendered', 'nonTenderedRow');
						resetIndexes();
						$('#nonTenderedMbDetailsId_' + key).val('');
						$('#nonTenderedquantity_' + key).val('');
						$('.nonTenderedcumulativeIncludingCurrentEntry_' + key).html('');
						$('.nonTenderedAmountCurrentEntry_' + key).html('');
						$('.nonTenderedamountIncludingCurrentEntry_' + key).html('');
						$('#nonTenderedremarks_' + key).val('');
						generateSlno('spannontenderedslno');
					}else{
						if (lumpSumCount > 0)
							lumpSumCheck = true;
						if(!lumpSumCheck){
							$('#lumpSumMessage').prop("hidden",true);
							$('#lumpSumRow').removeAttr("hidden");
							$('#lumpSumRow').removeAttr('nonsorinvisible');
						}
						if(lumpSumCheck) {
							var key = $("#tblLumpSum > tbody > tr:visible[id='lumpSumRow']").length;
							addRow('tblLumpSum', 'lumpSumRow');
							resetIndexes();
							$('#lumpSumMbDetailsId_' + key).val('');
							$('#lumpSumQuantity_' + key).val('');
							$('.lumpSumCumulativeIncludingCurrentEntry_' + key).html('');
							$('.lumpSumAmountCurrentEntry_' + key).html('');
							$('.lumpSumAmountIncludingCurrentEntry_' + key).html('');
							$('#lumpSumRemarks_' + key).val('');
							generateSlno('spanlumpsumslno');
						}
						lumpSumCheck = true;
					}
				}
				if(workOrderActivity.sorNonSorType == 'SOR'){
					$('#nonTenderedworkOrderActivity_' + nonTenderedCount).val(workOrderActivity.id);
					$('.nonTenderedsorCategory_' + nonTenderedCount).html(workOrderActivity.categoryType);
					$('.nonTenderedsorCode_' + nonTenderedCount).html(workOrderActivity.sorCode);
					$('.nonTenderedsummary_' + nonTenderedCount).html(workOrderActivity.summary);
					$('.nonTendereddescription_' + nonTenderedCount).html(hint.replace(/@fulldescription@/g, workOrderActivity.description));
					$('.nonTendereduom_' + nonTenderedCount).html(workOrderActivity.uom);
					$('.nonTenderedapprovedQuantity_' + nonTenderedCount).html(workOrderActivity.approvedQuantity);
					$('.nonTenderedapprovedRate_' + nonTenderedCount).html(parseFloat(workOrderActivity.estimateRate).toFixed(2));
					$('#nonTenderedunitRate_' + nonTenderedCount).val(workOrderActivity.unitRate);
					$('.nonTenderedapprovedAmount_' + nonTenderedCount).html(parseFloat(workOrderActivity.activityAmount).toFixed(2));
					$('.nonTenderedcumulativePreviousEntry_' + nonTenderedCount).html(workOrderActivity.cumulativePreviousEntry);
					if (workOrderActivity.woms != "") {
						$('#nonTenderedquantity_' + nonTenderedCount).attr('readonly', 'readonly');
						document.getElementById('nonTenderedMbDetails[' + nonTenderedCount + '].msadd').removeAttribute('style');
						var newrow= $('#msheaderrowtemplate').html();
						newrow = newrow.replace(/findNet/g, 'findNonTenderedNet');
						newrow=  newrow.replace(/msrowtemplate/g, 'msrownonTenderedMbDetails[' + nonTenderedCount + ']');
						newrow=  newrow.replace(/templatesorMbDetails\[0\]/g, 'nonTenderedMbDetails[' + nonTenderedCount + ']');
						newrow = newrow.replace(/templatesorMbDetails_0/g, 'nonTenderedMbDetails_' + nonTenderedCount);
						newrow = newrow.replace(/templatemssubmit_0/g, 'mssubmit_' + nonTenderedCount);
						newrow = newrow.replace('msrowslNo_0_0', 'nontenderedmsrowslNo_' + nonTenderedCount + '_0');
						newrow = newrow.replace('msrowremarks_0_0', 'nontenderedmsrowremarks_' + nonTenderedCount + '_0');
						newrow = newrow.replace('msrowno_0_0', 'nontenderedmsrowno_' + nonTenderedCount + '_0');
						newrow = newrow.replace('msrowlength_0_0', 'nontenderedmsrowlength_' + nonTenderedCount + '_0');
						newrow = newrow.replace('msrowwidth_0_0', 'nontenderedmsrowwidth_' + nonTenderedCount + '_0');
						newrow = newrow.replace('msrowdepthOrHeight_0_0', 'nontenderedmsrowdepthOrHeight_' + nonTenderedCount + '_0');
						newrow = newrow.replace('msrowquantity_0_0', 'nontenderedmsrowquantity_' + nonTenderedCount + '_0');
						newrow = newrow.replace('msrowidentifier_0_0', 'nontenderedmsrowidentifier_' + nonTenderedCount + '_0');
						newrow = newrow.replace('msrowmbmsPreviousEntry_0_0', 'nontenderedmsrowmbmsPreviousEntry_' + nonTenderedCount + '_0');
						document.getElementById('nonTenderedMbDetails[' + nonTenderedCount + '].mstd').innerHTML=newrow;
						$(workOrderActivity.woms).each(function(index, measurementSheet){
							if (index > 0) {
								var msrowname= "nonTenderedMbDetails[" + nonTenderedCount + "].mstable";
								var msrownameid=msrowname.split(".")[0];
								var rep='measurementSheets\['+ index +'\]';

								var $newrow = "<tr>"+$('#msrowtemplate').html()+"</tr>";
								newrow = newrow.replace(/findNet/g, 'findNonTenderedNet');
								$newrow = $newrow.replace(/templatesorMbDetails\[0\]/g,msrownameid);
								$newrow = $newrow.replace(/measurementSheets\[0\]/g,rep);
								$newrow = $newrow.replace(/templatesorMbDetails_0/g, 'nonTenderedMbDetails_' + nonTenderedCount);
								$newrow = $newrow.replace(/measurementSheets_0_id/g, 'measurementSheets_' + index + '_id');
								$newrow = $newrow.replace(/measurementSheets_0_woMeasurementSheet/g, 'measurementSheets_' + index + '_woMeasurementSheet');
								$newrow = $newrow.replace('msrowslNo_0_0', 'nontenderedmsrowslNo_' + nonTenderedCount + '_' + index);
								$newrow = $newrow.replace('msrowremarks_0_0', 'nontenderedmsrowremarks_' + nonTenderedCount + '_' + index);
								$newrow = $newrow.replace('msrowno_0_0', 'nontenderedmsrowno_' + nonTenderedCount + '_' + index);
								$newrow = $newrow.replace('msrowlength_0_0', 'nontenderedmsrowlength_' + nonTenderedCount + '_' + index);
								$newrow = $newrow.replace('msrowwidth_0_0', 'nontenderedmsrowwidth_' + nonTenderedCount + '_' + index);
								$newrow = $newrow.replace('msrowdepthOrHeight_0_0', 'nontenderedmsrowdepthOrHeight_' + nonTenderedCount + '_' + index);
								$newrow = $newrow.replace('msrowquantity_0_0', 'nontenderedmsrowquantity_' + nonTenderedCount + '_' + index);
								$newrow = $newrow.replace('msrowidentifier_0_0', 'nontenderedmsrowidentifier_' + nonTenderedCount + '_' + index);
								$newrow = $newrow.replace('msrowmbmsPreviousEntry_0_0', 'nontenderedmsrowmbmsPreviousEntry_' + nonTenderedCount + '_' + index);
								$newrow = $newrow.replace('value="1"','value="'+(index+1)+'"');
								$('.mssubmit_' + nonTenderedCount).closest('tr').before($newrow);

								patternvalidation();
							}
							$.each(measurementSheet, function(key, value){
								if(key == "womsid") {
									$('#nonTenderedMbDetails_' + nonTenderedCount + '_measurementSheets_' + index + '_woMeasurementSheet').attr('value', value);
								} else if(key == "id") {
									$('#nonTenderedMbDetails_' + nonTenderedCount + '_measurementSheets_' + index + '_id').attr('value', value);
								} else if(key == "identifier") {
									if(value == 'A')
										$('#nontenderedmsrowidentifier_' + nonTenderedCount + '_' + index).html('No');
									else
										$('#nontenderedmsrowidentifier_' + nonTenderedCount + '_' + index).html('Yes');
								} else
									$('#nontenderedmsrow' + key + '_' + nonTenderedCount + '_' + index).html(value);
							});
						});
					} else {
						document.getElementById('nonTenderedMbDetails[' + nonTenderedCount + '].msadd').style.display = 'none';
						$('#nonTenderedquantity_' + nonTenderedCount).removeAttr('readonly');
					}
					nonTenderedCount++;
				}else{
					$('#lumpSumWorkOrderActivity_' + lumpSumCount).val(workOrderActivity.id);
					$('.lumpSumSummary_' + lumpSumCount).html(workOrderActivity.summary);
					$('.lumpSumDescription_' + lumpSumCount).html(hint.replace(/@fulldescription@/g, workOrderActivity.description));
					$('.lumpSumUom_' + lumpSumCount).html(workOrderActivity.uom);
					$('.lumpSumApprovedQuantity_' + lumpSumCount).html(workOrderActivity.approvedQuantity);
					$('.lumpSumApprovedRate_' + lumpSumCount).html(parseFloat(workOrderActivity.estimateRate).toFixed(2));
					$('#lumpSumUnitRate_' + lumpSumCount).val(workOrderActivity.unitRate);
					$('.lumpSumApprovedAmount_' + lumpSumCount).html(parseFloat(workOrderActivity.activityAmount).toFixed(2));
					$('.lumpSumCumulativePreviousEntry_' + lumpSumCount).html(workOrderActivity.cumulativePreviousEntry);
					if (workOrderActivity.woms != "") {
						$('#lumpSumQuantity_' + lumpSumCount).attr('readonly', 'readonly');
						document.getElementById('lumpSumMbDetails[' + lumpSumCount + '].msadd').removeAttribute('style');
						var newrow= $('#msheaderrowtemplate').html();

						newrow = newrow.replace(/findNet/g, 'findLumpSumNet');
						newrow=  newrow.replace(/msrowtemplate/g, 'msrowLumpSumMbDetails[' + lumpSumCount + ']');
						newrow=  newrow.replace(/templatesorMbDetails\[0\]/g, 'lumpSumMbDetails[' + lumpSumCount + ']');
						newrow = newrow.replace(/templatesorMbDetails_0/g, 'lumpSumMbDetails_' + lumpSumCount);
						newrow = newrow.replace(/templatemssubmit_0/g, 'lumpsummssubmit_' + lumpSumCount);
						newrow = newrow.replace('msrowslNo_0_0', 'lumpsummsrowslNo_' + lumpSumCount + '_0');
						newrow = newrow.replace('msrowremarks_0_0', 'lumpsummsrowremarks_' + lumpSumCount + '_0');
						newrow = newrow.replace('msrowno_0_0', 'lumpsummsrowno_' + lumpSumCount + '_0');
						newrow = newrow.replace('msrowlength_0_0', 'lumpsummsrowlength_' + lumpSumCount + '_0');
						newrow = newrow.replace('msrowwidth_0_0', 'lumpsummsrowwidth_' + lumpSumCount + '_0');
						newrow = newrow.replace('msrowdepthOrHeight_0_0', 'lumpsummsrowdepthOrHeight_' + lumpSumCount + '_0');
						newrow = newrow.replace('msrowquantity_0_0', 'lumpsummsrowquantity_' + lumpSumCount + '_0');
						newrow = newrow.replace('msrowidentifier_0_0', 'lumpsummsrowidentifier_' + lumpSumCount + '_0');
						newrow = newrow.replace('msrowmbmsPreviousEntry_0_0', 'lumpsummsrowmbmsPreviousEntry_' + lumpSumCount + '_0');
						document.getElementById('lumpSumMbDetails[' + lumpSumCount + '].mstd').innerHTML=newrow;
						$(workOrderActivity.woms).each(function(index, measurementSheet){
							if (index > 0) {
								var msrowname= "lumpSumMbDetails[" + lumpSumCount + "].mstable";
								var msrownameid=msrowname.split(".")[0];
								var rep='measurementSheets\['+ index +'\]';

								var $newrow = "<tr>"+$('#msrowtemplate').html()+"</tr>";
								$newrow = $newrow.replace(/findNet/g, 'findLumpSumNet');
								$newrow = $newrow.replace(/templatesorMbDetails\[0\]/g,msrownameid);
								$newrow = $newrow.replace(/measurementSheets\[0\]/g,rep);
								$newrow = $newrow.replace(/templatesorMbDetails_0/g, 'lumpSumMbDetails_' + lumpSumCount);
								$newrow = $newrow.replace(/measurementSheets_0_id/g, 'measurementSheets_' + index + '_id');
								$newrow = $newrow.replace(/measurementSheets_0_woMeasurementSheet/g, 'measurementSheets_' + index + '_woMeasurementSheet');
								$newrow = $newrow.replace('msrowslNo_0_0', 'lumpsummsrowslNo_' + lumpSumCount + '_' + index);
								$newrow = $newrow.replace('msrowremarks_0_0', 'lumpsummsrowremarks_' + lumpSumCount + '_' + index);
								$newrow = $newrow.replace('msrowno_0_0', 'lumpsummsrowno_' + lumpSumCount + '_' + index);
								$newrow = $newrow.replace('msrowlength_0_0', 'lumpsummsrowlength_' + lumpSumCount + '_' + index);
								$newrow = $newrow.replace('msrowwidth_0_0', 'lumpsummsrowwidth_' + lumpSumCount + '_' + index);
								$newrow = $newrow.replace('msrowdepthOrHeight_0_0', 'lumpsummsrowdepthOrHeight_' + lumpSumCount + '_' + index);
								$newrow = $newrow.replace('msrowquantity_0_0', 'lumpsummsrowquantity_' + lumpSumCount + '_' + index);
								$newrow = $newrow.replace('msrowidentifier_0_0', 'lumpsummsrowidentifier_' + lumpSumCount + '_' + index);
								$newrow = $newrow.replace('msrowmbmsPreviousEntry_0_0', 'lumpsummsrowmbmsPreviousEntry_' + lumpSumCount + '_' + index);
								$newrow = $newrow.replace('value="1"','value="'+(index+1)+'"');
								$('.lumpsummssubmit_' + lumpSumCount).closest('tr').before($newrow);

								patternvalidation();
							}
							$.each(measurementSheet, function(key, value){
								if(key == "womsid") {
									$('#lumpSumMbDetails_' + lumpSumCount + '_measurementSheets_' + index + '_woMeasurementSheet').attr('value', value);
								} else if(key == "id") {
									$('#lumpSumMbDetails_' + lumpSumCount + '_measurementSheets_' + index + '_id').attr('value', value);
								} else if(key == "identifier") {
									if(value == 'A')
										$('#lumpsummsrowidentifier_' + lumpSumCount + '_' + index).html('No');
									else
										$('#lumpsummsrowidentifier_' + lumpSumCount + '_' + index).html('Yes');
								} else
									$('#lumpsummsrow' + key + '_' + lumpSumCount + '_' + index).html(value);
							});
						});
					} else {
						document.getElementById('lumpSumMbDetails[' + lumpSumCount + '].msadd').style.display = 'none';
						$('#lumpSumQuantity_' + lumpSumCount).removeAttr('readonly');
					}
					lumpSumCount++;
				}
			}
		});
	}
	
	nonTenderedTotal();
	lumpSumTotal();

}

function resetIndexes() {
	var idx = 0;
	
	//regenerate index existing inputs in table row
	$(".sorRow").each(function() {
		$(this).find("input, span, select, textarea, button").each(function() {
			
			if (!$(this).is('span')) {
				$(this).attr({
					'name' : function(_, name) {
						if(name != undefined)
							if(name) {
							  name= name.replace(/sorMbDetails\[.\]/g, "sorMbDetails["+idx+"]");
							  return name.replace(/_\d+/,"_"+idx);
							}
					},
					'id' : function(_, id) {
						if(id != undefined)
							return id.replace(/\d+/, idx);
					},
					'class' : function(_, name) {
						if(name != undefined)
							return name.replace(/\d+/, idx);
					},
					'data-idx' : function(_, dataIdx) {
						return idx;
					}
				});
			} else {
				if($(this).attr('class').endsWith('.mstd')) {
					var subRowIdx=0;
					$(this).find("table > tbody > tr").each(function() {
					  	$(this).find("input, textarea, td").each(function() {
							if ($(this).is('td')) {
								$(this).attr({
									'id' : function(_, id) {
										if(id != undefined && id.indexOf('msrow') >= 0) {
											return id.split('_')[0] + '_' + idx + '_' + subRowIdx;
										} else {
											if(id != undefined)
												return id.replace(/\d+/, idx);
										}
									}
								});
							}
							else{
								$(this).attr({
									'name' : function(_, name) {
										if(name != undefined)
											if(name) {
												name = name.replace(/measurementSheets\[.\]/g, "measurementSheets["+subRowIdx+"]");
												return name;
											}
									},
									'id' : function(_, id) {
										if(id != undefined)
											if(id.indexOf('_') == -1) {
												id = id.replace(/measurementSheets\[.\]/g, "measurementSheets["+subRowIdx+"]");
												return id;
											} else {
												if(id.indexOf('_woMeasurementSheet') == -1)
													return 'sorMbDetails_' + idx + '_measurementSheets_' + subRowIdx + '_id';
												else
													return 'sorMbDetails_' + idx + '_measurementSheets_' + subRowIdx + '_woMeasurementSheet';
											}
									},
									'data-idx' : function(_, dataIdx) {
										return subRowIdx;
									}
								});
							}
						});
						subRowIdx++;
					});
				}
				
				$(this).attr({
					'class' : function(_, name) {
						if(name != undefined)
							return name.replace(/\d+/, idx);
					},
					'id' : function(_, id) {
						if(id != undefined)
							return id.replace(/\d+/, idx);
					},
				});
			}
		});
		idx++;
	});
	
	idx = 0;
	
	$(".nonSorRow").each(function() {
		$(this).find("input, span, select, textarea, button").each(function() {
			if (!$(this).is('span')) {
				$(this).attr({
					'name' : function(_, name) {
						if(name != undefined)
							if(name) {
								name= name.replace(/nonSorMbDetails\[.\]/g, "nonSorMbDetails["+idx+"]");
								return name.replace(/_\d+/,"_"+idx);
							}
					},
					'id' : function(_, id) {
						if(id != undefined)
							return id.replace(/\d+/, idx);
					},
					'class' : function(_, name) {
						if(name != undefined)
							return name.replace(/\d+/, idx);
					},
					'data-idx' : function(_, dataIdx) {
						return idx;
					}
				});
			} else {
				if($(this).attr('class').endsWith('.mstd')) {
					var subRowIdx=0;
					$(this).find("table > tbody > tr").each(function() {
					  	$(this).find("input, textarea, td").each(function() {
							if ($(this).is('td')) {
								$(this).attr({
									'id' : function(_, id) {
										if(id != undefined && id.indexOf('msrow') >= 0) {
											return id.split('_')[0] + '_' + idx + '_' + subRowIdx;
										} else {
											if(id != undefined)
												return id.replace(/\d+/, idx);
										}
									}
								});
							}
							else{
								$(this).attr({
									'name' : function(_, name) {
										if(name != undefined)
											if(name) {
												name = name.replace(/measurementSheets\[.\]/g, "measurementSheets["+subRowIdx+"]");
												return name;
											}
									},
									'id' : function(_, id) {
										if(id != undefined)
											if(id.indexOf('_') == -1) {
												id = id.replace(/measurementSheets\[.\]/g, "measurementSheets["+subRowIdx+"]");
												return id;
											} else {
												if(id.indexOf('_woMeasurementSheet') == -1)
													return 'nonSorMbDetails_' + idx + '_measurementSheets_' + subRowIdx + '_id';
												else
													return 'nonSorMbDetails_' + idx + '_measurementSheets_' + subRowIdx + '_woMeasurementSheet';
											}
									},
									'data-idx' : function(_, dataIdx) {
										return subRowIdx;
									}
								});
							}
						});
						subRowIdx++;
					});
				}
				$(this).attr({
					'class' : function(_, name) {
						if(name != undefined)
							return name.replace(/\d+/, idx);
					},
					'id' : function(_, id) {
						if(id != undefined)
							return id.replace(/\d+/, idx);
					},
				});
			}
		});
		idx++;
	});
	
 idx = 0;
	
	//regenerate index existing inputs in table row
	$(".nonTenderedRow").each(function() {
		$(this).find("input, span, select, textarea, button").each(function() {
			
			if (!$(this).is('span')) {
				$(this).attr({
					'name' : function(_, name) {
						if(name != undefined)
							if(name) {
							  name= name.replace(/nonTenderedMbDetails\[.\]/g, "nonTenderedMbDetails["+idx+"]");
							  return name.replace(/_\d+/,"_"+idx);
							}
					},
					'id' : function(_, id) {
						if(id != undefined)
							return id.replace(/\d+/, idx);
					},
					'class' : function(_, name) {
						if(name != undefined)
							return name.replace(/\d+/, idx);
					},
					'data-idx' : function(_, dataIdx) {
						return idx;
					}
				});
			} else {
				if($(this).attr('class').endsWith('.mstd')) {
					var subRowIdx=0;
					$(this).find("table > tbody > tr").each(function() {
					  	$(this).find("input, textarea, td").each(function() {
							if ($(this).is('td')) {
								$(this).attr({
									'id' : function(_, id) {
										if(id != undefined && id.indexOf('msrow') >= 0) {
											return id.split('_')[0] + '_' + idx + '_' + subRowIdx;
										} else {
											if(id != undefined)
												return id.replace(/\d+/, idx);
										}
									}
								});
							}
							else{
								$(this).attr({
									'name' : function(_, name) {
										if(name != undefined)
											if(name) {
												name = name.replace(/measurementSheets\[.\]/g, "measurementSheets["+subRowIdx+"]");
												return name;
											}
									},
									'id' : function(_, id) {
										if(id != undefined)
											if(id.indexOf('_') == -1) {
												id = id.replace(/measurementSheets\[.\]/g, "measurementSheets["+subRowIdx+"]");
												return id;
											} else {
												if(id.indexOf('_woMeasurementSheet') == -1)
													return 'nonTenderedMbDetails_' + idx + '_measurementSheets_' + subRowIdx + '_id';
												else
													return 'nonTenderedMbDetails_' + idx + '_measurementSheets_' + subRowIdx + '_woMeasurementSheet';
											}
									},
									'data-idx' : function(_, dataIdx) {
										return subRowIdx;
									}
								});
							}
						});
						subRowIdx++;
					});
				}
				
				$(this).attr({
					'class' : function(_, name) {
						if(name != undefined)
							return name.replace(/\d+/, idx);
					},
					'id' : function(_, id) {
						if(id != undefined)
							return id.replace(/\d+/, idx);
					},
				});
			}
		});
		idx++;
	});
	
idx = 0;
	
	//regenerate index existing inputs in table row
	$(".lumpSumRow").each(function() {
		$(this).find("input, span, select, textarea, button").each(function() {
			
			if (!$(this).is('span')) {
				$(this).attr({
					'name' : function(_, name) {
						if(name != undefined)
							if(name) {
							  name= name.replace(/lumpSumMbDetails\[.\]/g, "lumpSumMbDetails["+idx+"]");
							  return name.replace(/_\d+/,"_"+idx);
							}
					},
					'id' : function(_, id) {
						if(id != undefined)
							return id.replace(/\d+/, idx);
					},
					'class' : function(_, name) {
						if(name != undefined)
							return name.replace(/\d+/, idx);
					},
					'data-idx' : function(_, dataIdx) {
						return idx;
					}
				});
			} else {
				if($(this).attr('class').endsWith('.mstd')) {
					var subRowIdx=0;
					$(this).find("table > tbody > tr").each(function() {
					  	$(this).find("input, textarea, td").each(function() {
							if ($(this).is('td')) {
								$(this).attr({
									'id' : function(_, id) {
										if(id != undefined && id.indexOf('msrow') >= 0) {
											return id.split('_')[0] + '_' + idx + '_' + subRowIdx;
										} else {
											if(id != undefined)
												return id.replace(/\d+/, idx);
										}
									}
								});
							}
							else{
								$(this).attr({
									'name' : function(_, name) {
										if(name != undefined)
											if(name) {
												name = name.replace(/measurementSheets\[.\]/g, "measurementSheets["+subRowIdx+"]");
												return name;
											}
									},
									'id' : function(_, id) {
										if(id != undefined)
											if(id.indexOf('_') == -1) {
												id = id.replace(/measurementSheets\[.\]/g, "measurementSheets["+subRowIdx+"]");
												return id;
											} else {
												if(id.indexOf('_woMeasurementSheet') == -1)
													return 'lumpSumMbDetails_' + idx + '_measurementSheets_' + subRowIdx + '_id';
												else
													return 'lumpSumMbDetails_' + idx + '_measurementSheets_' + subRowIdx + '_woMeasurementSheet';
											}
									},
									'data-idx' : function(_, dataIdx) {
										return subRowIdx;
									}
								});
							}
						});
						subRowIdx++;
					});
				}
				
				$(this).attr({
					'class' : function(_, name) {
						if(name != undefined)
							return name.replace(/\d+/, idx);
					},
					'id' : function(_, id) {
						if(id != undefined)
							return id.replace(/\d+/, idx);
					},
				});
			}
		});
		idx++;
	});
}

function populateData(data, selectedActivities){
	var nonSorCheck  = false;
	var addAll = false;
	if(selectedActivities == "")
		addAll = true;
	
	var activityArray = selectedActivities.split(",");
	var existingActivityArray = [];
	$('.workOrderActivity, .nonSorWorkOrderActivity').each(function() {
		existingActivityArray.push($(this).val());
	});
	if(!addAll) {
		var sorCount = $("#tblsor > tbody > tr:visible[id='sorRow']").length;
		var nonSorCount = $("#tblNonSor > tbody > tr:visible[id='nonSorRow']").length;
		$(data.data).each(function(index, workOrderActivity){
			if($.inArray("" + workOrderActivity.id + "", existingActivityArray) == -1
					&& $.inArray("" + workOrderActivity.id + "", activityArray) != -1) {
				if(sorCount==0 && workOrderActivity.sorNonSorType == 'SOR'){
					$('#message').prop("hidden",true);
					$('#sorRow').removeAttr("hidden");
					$('#sorRow').removeAttr('sorinvisible');
				}else{
					if(workOrderActivity.sorNonSorType == 'SOR'){
						var key = $("#tblsor > tbody > tr:visible[id='sorRow']").length;
						addRow('tblsor', 'sorRow');
						resetIndexes();
						$('#sorMbDetailsId_' + key).val('');
						$('#quantity_' + key).val('');
						$('.cumulativeIncludingCurrentEntry_' + key).html('');
						$('.amountCurrentEntry_' + key).html('');
						$('.amountIncludingCurrentEntry_' + key).html('');
						$('#remarks_' + key).val('');
						generateSlno('spansorslno');	
					}else{
						if (nonSorCount > 0)
							nonSorCheck = true;
						if(!nonSorCheck){
							$('#nonSorMessage').prop("hidden",true);
							$('#nonSorRow').removeAttr("hidden");
							$('#nonSorRow').removeAttr('nonsorinvisible');
						}
						if(nonSorCheck) {
							var key = $("#tblNonSor > tbody > tr:visible[id='nonSorRow']").length;
							addRow('tblNonSor', 'nonSorRow');
							resetIndexes();
							$('#nonSorMbDetailsId_' + key).val('');
							$('#nonSorQuantity_' + key).val('');
							$('.nonSorCumulativeIncludingCurrentEntry_' + key).html('');
							$('.nonSorAmountCurrentEntry_' + key).html('');
							$('.nonSorAmountIncludingCurrentEntry_' + key).html('');
							$('#nonSorRemarks_' + key).val('');
							generateSlno('spannonsorslno');
						}
						nonSorCheck = true;
					}
				}
				if(workOrderActivity.sorNonSorType == 'SOR'){
					$('#workOrderActivity_' + sorCount).val(workOrderActivity.id);
					$('.sorCategory_' + sorCount).html(workOrderActivity.categoryType);
					$('.sorCode_' + sorCount).html(workOrderActivity.sorCode);
					$('.summary_' + sorCount).html(workOrderActivity.summary);
					$('.description_' + sorCount).html(hint.replace(/@fulldescription@/g, workOrderActivity.description));
					$('.uom_' + sorCount).html(workOrderActivity.uom);
					$('.approvedQuantity_' + sorCount).html(workOrderActivity.approvedQuantity);
					$('.approvedRate_' + sorCount).html(parseFloat(workOrderActivity.estimateRate).toFixed(2));
					$('#unitRate_' + sorCount).val(workOrderActivity.unitRate);
					$('.approvedAmount_' + sorCount).html(parseFloat(workOrderActivity.activityAmount).toFixed(2));
					$('.cumulativePreviousEntry_' + sorCount).html(workOrderActivity.cumulativePreviousEntry);
					if (workOrderActivity.woms != "") {
						$('#quantity_' + sorCount).attr('readonly', 'readonly');
						document.getElementById('sorMbDetails[' + sorCount + '].msadd').removeAttribute('style');
						var newrow= $('#msheaderrowtemplate').html();

						newrow=  newrow.replace(/msrowtemplate/g, 'msrowsorMbDetails[' + sorCount + ']');
						newrow=  newrow.replace(/templatesorMbDetails\[0\]/g, 'sorMbDetails[' + sorCount + ']');
						newrow = newrow.replace(/templatesorMbDetails_0/g, 'sorMbDetails_' + sorCount);
						newrow = newrow.replace(/templatemssubmit_0/g, 'mssubmit_' + sorCount);
						newrow = newrow.replace('msrowslNo_0_0', 'msrowslNo_' + sorCount + '_0');
						newrow = newrow.replace('msrowremarks_0_0', 'msrowremarks_' + sorCount + '_0');
						newrow = newrow.replace('msrowno_0_0', 'msrowno_' + sorCount + '_0');
						newrow = newrow.replace('msrowlength_0_0', 'msrowlength_' + sorCount + '_0');
						newrow = newrow.replace('msrowwidth_0_0', 'msrowwidth_' + sorCount + '_0');
						newrow = newrow.replace('msrowdepthOrHeight_0_0', 'msrowdepthOrHeight_' + sorCount + '_0');
						newrow = newrow.replace('msrowquantity_0_0', 'msrowquantity_' + sorCount + '_0');
						newrow = newrow.replace('msrowidentifier_0_0', 'msrowidentifier_' + sorCount + '_0');
						newrow = newrow.replace('msrowmbmsPreviousEntry_0_0', 'msrowmbmsPreviousEntry_' + sorCount + '_0');
						document.getElementById('sorMbDetails[' + sorCount + '].mstd').innerHTML=newrow;
						$(workOrderActivity.woms).each(function(index, measurementSheet){
							if (index > 0) {
								var msrowname= "sorMbDetails[" + sorCount + "].mstable";
								var msrownameid=msrowname.split(".")[0];
								var rep='measurementSheets\['+ index +'\]';

								var $newrow = "<tr>"+$('#msrowtemplate').html()+"</tr>";
								$newrow = $newrow.replace(/templatesorMbDetails\[0\]/g,msrownameid);
								$newrow = $newrow.replace(/measurementSheets\[0\]/g,rep);
								$newrow = $newrow.replace(/templatesorMbDetails_0/g, 'sorMbDetails_' + sorCount);
								$newrow = $newrow.replace(/measurementSheets_0_id/g, 'measurementSheets_' + index + '_id');
								$newrow = $newrow.replace(/measurementSheets_0_woMeasurementSheet/g, 'measurementSheets_' + index + '_woMeasurementSheet');
								$newrow = $newrow.replace('msrowslNo_0_0', 'msrowslNo_' + sorCount + '_' + index);
								$newrow = $newrow.replace('msrowremarks_0_0', 'msrowremarks_' + sorCount + '_' + index);
								$newrow = $newrow.replace('msrowno_0_0', 'msrowno_' + sorCount + '_' + index);
								$newrow = $newrow.replace('msrowlength_0_0', 'msrowlength_' + sorCount + '_' + index);
								$newrow = $newrow.replace('msrowwidth_0_0', 'msrowwidth_' + sorCount + '_' + index);
								$newrow = $newrow.replace('msrowdepthOrHeight_0_0', 'msrowdepthOrHeight_' + sorCount + '_' + index);
								$newrow = $newrow.replace('msrowquantity_0_0', 'msrowquantity_' + sorCount + '_' + index);
								$newrow = $newrow.replace('msrowidentifier_0_0', 'msrowidentifier_' + sorCount + '_' + index);
								$newrow = $newrow.replace('msrowmbmsPreviousEntry_0_0', 'msrowmbmsPreviousEntry_' + sorCount + '_' + index);
								$newrow = $newrow.replace('value="1"','value="'+(index+1)+'"');
								$('.mssubmit_' + sorCount).closest('tr').before($newrow);

								patternvalidation();
							}
							$.each(measurementSheet, function(key, value){
								if(key == "womsid") {
									$('#sorMbDetails_' + sorCount + '_measurementSheets_' + index + '_woMeasurementSheet').attr('value', value);
								} else if(key == "id") {
									$('#sorMbDetails_' + sorCount + '_measurementSheets_' + index + '_id').attr('value', value);
								} else if(key == "identifier") {
									if(value == 'A')
										$('#msrowidentifier_' + sorCount + '_' + index).html('No');
									else
										$('#msrowidentifier_' + sorCount + '_' + index).html('Yes');
								} else
									$('#msrow' + key + '_' + sorCount + '_' + index).html(value);
							});
						});
					} else {
						document.getElementById('sorMbDetails[' + sorCount + '].msadd').style.display = 'none';
						$('#quantity_' + sorCount).removeAttr('readonly');
					}
					sorCount++;
				}else{
					$('#nonSorWorkOrderActivity_' + nonSorCount).val(workOrderActivity.id);
					$('.nonSorSummary_' + nonSorCount).html(workOrderActivity.summary);
					$('.nonSorDescription_' + nonSorCount).html(hint.replace(/@fulldescription@/g, workOrderActivity.description));
					$('.nonSorUom_' + nonSorCount).html(workOrderActivity.uom);
					$('.nonSorApprovedQuantity_' + nonSorCount).html(workOrderActivity.approvedQuantity);
					$('.nonSorApprovedRate_' + nonSorCount).html(parseFloat(workOrderActivity.estimateRate).toFixed(2));
					$('#nonSorUnitRate_' + nonSorCount).val(workOrderActivity.unitRate);
					$('.nonSorApprovedAmount_' + nonSorCount).html(parseFloat(workOrderActivity.activityAmount).toFixed(2));
					$('.nonSorCumulativePreviousEntry_' + nonSorCount).html(workOrderActivity.cumulativePreviousEntry);
					if (workOrderActivity.woms != "") {
						$('#nonSorQuantity_' + nonSorCount).attr('readonly', 'readonly');
						document.getElementById('nonSorMbDetails[' + nonSorCount + '].msadd').removeAttribute('style');
						var newrow= $('#msheaderrowtemplate').html();

						newrow = newrow.replace(/findNet/g, 'findNonSorNet');
						newrow=  newrow.replace(/msrowtemplate/g, 'msrownonSorMbDetails[' + nonSorCount + ']');
						newrow=  newrow.replace(/templatesorMbDetails\[0\]/g, 'nonSorMbDetails[' + nonSorCount + ']');
						newrow = newrow.replace(/templatesorMbDetails_0/g, 'nonSorMbDetails_' + nonSorCount);
						newrow = newrow.replace(/templatemssubmit_0/g, 'nonsormssubmit_' + nonSorCount);
						newrow = newrow.replace('msrowslNo_0_0', 'nonsormsrowslNo_' + nonSorCount + '_0');
						newrow = newrow.replace('msrowremarks_0_0', 'nonsormsrowremarks_' + nonSorCount + '_0');
						newrow = newrow.replace('msrowno_0_0', 'nonsormsrowno_' + nonSorCount + '_0');
						newrow = newrow.replace('msrowlength_0_0', 'nonsormsrowlength_' + nonSorCount + '_0');
						newrow = newrow.replace('msrowwidth_0_0', 'nonsormsrowwidth_' + nonSorCount + '_0');
						newrow = newrow.replace('msrowdepthOrHeight_0_0', 'nonsormsrowdepthOrHeight_' + nonSorCount + '_0');
						newrow = newrow.replace('msrowquantity_0_0', 'nonsormsrowquantity_' + nonSorCount + '_0');
						newrow = newrow.replace('msrowidentifier_0_0', 'nonsormsrowidentifier_' + nonSorCount + '_0');
						newrow = newrow.replace('msrowmbmsPreviousEntry_0_0', 'nonsormsrowmbmsPreviousEntry_' + nonSorCount + '_0');
						document.getElementById('nonSorMbDetails[' + nonSorCount + '].mstd').innerHTML=newrow;
						$(workOrderActivity.woms).each(function(index, measurementSheet){
							if (index > 0) {
								var msrowname= "nonSorMbDetails[" + nonSorCount + "].mstable";
								var msrownameid=msrowname.split(".")[0];
								var rep='measurementSheets\['+ index +'\]';

								var $newrow = "<tr>"+$('#msrowtemplate').html()+"</tr>";
								$newrow = $newrow.replace(/findNet/g, 'findNonSorNet');
								$newrow = $newrow.replace(/templatesorMbDetails\[0\]/g,msrownameid);
								$newrow = $newrow.replace(/measurementSheets\[0\]/g,rep);
								$newrow = $newrow.replace(/templatesorMbDetails_0/g, 'nonSorMbDetails_' + nonSorCount);
								$newrow = $newrow.replace(/measurementSheets_0_id/g, 'measurementSheets_' + index + '_id');
								$newrow = $newrow.replace(/measurementSheets_0_woMeasurementSheet/g, 'measurementSheets_' + index + '_woMeasurementSheet');
								$newrow = $newrow.replace('msrowslNo_0_0', 'nonsormsrowslNo_' + nonSorCount + '_' + index);
								$newrow = $newrow.replace('msrowremarks_0_0', 'nonsormsrowremarks_' + nonSorCount + '_' + index);
								$newrow = $newrow.replace('msrowno_0_0', 'nonsormsrowno_' + nonSorCount + '_' + index);
								$newrow = $newrow.replace('msrowlength_0_0', 'nonsormsrowlength_' + nonSorCount + '_' + index);
								$newrow = $newrow.replace('msrowwidth_0_0', 'nonsormsrowwidth_' + nonSorCount + '_' + index);
								$newrow = $newrow.replace('msrowdepthOrHeight_0_0', 'nonsormsrowdepthOrHeight_' + nonSorCount + '_' + index);
								$newrow = $newrow.replace('msrowquantity_0_0', 'nonsormsrowquantity_' + nonSorCount + '_' + index);
								$newrow = $newrow.replace('msrowidentifier_0_0', 'nonsormsrowidentifier_' + nonSorCount + '_' + index);
								$newrow = $newrow.replace('msrowmbmsPreviousEntry_0_0', 'nonsormsrowmbmsPreviousEntry_' + nonSorCount + '_' + index);
								$newrow = $newrow.replace('value="1"','value="'+(index+1)+'"');
								$('.nonsormssubmit_' + nonSorCount).closest('tr').before($newrow);

								patternvalidation();
							}
							$.each(measurementSheet, function(key, value){
								if(key == "womsid") {
									$('#nonSorMbDetails_' + nonSorCount + '_measurementSheets_' + index + '_woMeasurementSheet').attr('value', value);
								} else if(key == "id") {
									$('#nonSorMbDetails_' + nonSorCount + '_measurementSheets_' + index + '_id').attr('value', value);
								} else if(key == "identifier") {
									if(value == 'A')
										$('#nonsormsrowidentifier_' + nonSorCount + '_' + index).html('No');
									else
										$('#nonsormsrowidentifier_' + nonSorCount + '_' + index).html('Yes');
								} else
									$('#nonsormsrow' + key + '_' + nonSorCount + '_' + index).html(value);
							});
						});
					} else {
						document.getElementById('nonSorMbDetails[' + nonSorCount + '].msadd').style.display = 'none';
						$('#nonSorQuantity_' + nonSorCount).removeAttr('readonly');
					}
					nonSorCount++;
				}
			}
		});
	} else {
		var sorCount = $("#tblsor > tbody > tr:visible[id='sorRow']").length;
		var nonSorCount = $("#tblNonSor > tbody > tr:visible[id='nonSorRow']").length;
		$(data.data).each(function(index, workOrderActivity){
			if($.inArray("" + workOrderActivity.id + "", existingActivityArray) == -1) {
				if(sorCount==0 && workOrderActivity.sorNonSorType == 'SOR'){
					$('#message').prop("hidden",true);
					$('#sorRow').removeAttr("hidden");
					$('#sorRow').removeAttr('sorinvisible');
				}else{
					if(workOrderActivity.sorNonSorType == 'SOR'){
						var key = $("#tblsor > tbody > tr:visible[id='sorRow']").length;
						addRow('tblsor', 'sorRow');
						resetIndexes();
						$('#sorMbDetailsId_' + key).val('');
						$('#quantity_' + key).val('');
						$('.cumulativeIncludingCurrentEntry_' + key).html('');
						$('.amountCurrentEntry_' + key).html('');
						$('.amountIncludingCurrentEntry_' + key).html('');
						$('#remarks_' + key).val('');
						generateSlno('spansorslno');
					}else{
						if (nonSorCount > 0)
							nonSorCheck = true;
						if(!nonSorCheck){
							$('#nonSorMessage').prop("hidden",true);
							$('#nonSorRow').removeAttr("hidden");
							$('#nonSorRow').removeAttr('nonsorinvisible');
						}
						if(nonSorCheck) {
							var key = $("#tblNonSor > tbody > tr:visible[id='nonSorRow']").length;
							addRow('tblNonSor', 'nonSorRow');
							resetIndexes();
							$('#nonSorMbDetailsId_' + key).val('');
							$('#nonSorQuantity_' + key).val('');
							$('.nonSorCumulativeIncludingCurrentEntry_' + key).html('');
							$('.nonSorAmountCurrentEntry_' + key).html('');
							$('.nonSorAmountIncludingCurrentEntry_' + key).html('');
							$('#nonSorRemarks_' + key).val('');
							generateSlno('spannonsorslno');
						}
						nonSorCheck = true;
					}
				}
				if(workOrderActivity.sorNonSorType == 'SOR'){
					$('#workOrderActivity_' + sorCount).val(workOrderActivity.id);
					$('.sorCategory_' + sorCount).html(workOrderActivity.categoryType);
					$('.sorCode_' + sorCount).html(workOrderActivity.sorCode);
					$('.summary_' + sorCount).html(workOrderActivity.summary);
					$('.description_' + sorCount).html(hint.replace(/@fulldescription@/g, workOrderActivity.description));
					$('.uom_' + sorCount).html(workOrderActivity.uom);
					$('.approvedQuantity_' + sorCount).html(workOrderActivity.approvedQuantity);
					$('.approvedRate_' + sorCount).html(parseFloat(workOrderActivity.estimateRate).toFixed(2));
					$('#unitRate_' + sorCount).val(workOrderActivity.unitRate);
					$('.approvedAmount_' + sorCount).html(parseFloat(workOrderActivity.activityAmount).toFixed(2));
					$('.cumulativePreviousEntry_' + sorCount).html(workOrderActivity.cumulativePreviousEntry);
					if (workOrderActivity.woms != "") {
						$('#quantity_' + sorCount).attr('readonly', 'readonly');
						document.getElementById('sorMbDetails[' + sorCount + '].msadd').removeAttribute('style');
						var newrow= $('#msheaderrowtemplate').html();

						newrow=  newrow.replace(/msrowtemplate/g, 'msrowsorMbDetails[' + sorCount + ']');
						newrow=  newrow.replace(/templatesorMbDetails\[0\]/g, 'sorMbDetails[' + sorCount + ']');
						newrow = newrow.replace(/templatesorMbDetails_0/g, 'sorMbDetails_' + sorCount);
						newrow = newrow.replace(/templatemssubmit_0/g, 'mssubmit_' + sorCount);
						newrow = newrow.replace('msrowslNo_0_0', 'msrowslNo_' + sorCount + '_0');
						newrow = newrow.replace('msrowremarks_0_0', 'msrowremarks_' + sorCount + '_0');
						newrow = newrow.replace('msrowno_0_0', 'msrowno_' + sorCount + '_0');
						newrow = newrow.replace('msrowlength_0_0', 'msrowlength_' + sorCount + '_0');
						newrow = newrow.replace('msrowwidth_0_0', 'msrowwidth_' + sorCount + '_0');
						newrow = newrow.replace('msrowdepthOrHeight_0_0', 'msrowdepthOrHeight_' + sorCount + '_0');
						newrow = newrow.replace('msrowquantity_0_0', 'msrowquantity_' + sorCount + '_0');
						newrow = newrow.replace('msrowidentifier_0_0', 'msrowidentifier_' + sorCount + '_0');
						newrow = newrow.replace('msrowmbmsPreviousEntry_0_0', 'msrowmbmsPreviousEntry_' + sorCount + '_0');
						document.getElementById('sorMbDetails[' + sorCount + '].mstd').innerHTML=newrow;
						$(workOrderActivity.woms).each(function(index, measurementSheet){
							if (index > 0) {
								var msrowname= "sorMbDetails[" + sorCount + "].mstable";
								var msrownameid=msrowname.split(".")[0];
								var rep='measurementSheets\['+ index +'\]';

								var $newrow = "<tr>"+$('#msrowtemplate').html()+"</tr>";
								$newrow = $newrow.replace(/templatesorMbDetails\[0\]/g,msrownameid);
								$newrow = $newrow.replace(/measurementSheets\[0\]/g,rep);
								$newrow = $newrow.replace(/templatesorMbDetails_0/g, 'sorMbDetails_' + sorCount);
								$newrow = $newrow.replace(/measurementSheets_0_id/g, 'measurementSheets_' + index + '_id');
								$newrow = $newrow.replace(/measurementSheets_0_woMeasurementSheet/g, 'measurementSheets_' + index + '_woMeasurementSheet');
								$newrow = $newrow.replace('msrowslNo_0_0', 'msrowslNo_' + sorCount + '_' + index);
								$newrow = $newrow.replace('msrowremarks_0_0', 'msrowremarks_' + sorCount + '_' + index);
								$newrow = $newrow.replace('msrowno_0_0', 'msrowno_' + sorCount + '_' + index);
								$newrow = $newrow.replace('msrowlength_0_0', 'msrowlength_' + sorCount + '_' + index);
								$newrow = $newrow.replace('msrowwidth_0_0', 'msrowwidth_' + sorCount + '_' + index);
								$newrow = $newrow.replace('msrowdepthOrHeight_0_0', 'msrowdepthOrHeight_' + sorCount + '_' + index);
								$newrow = $newrow.replace('msrowquantity_0_0', 'msrowquantity_' + sorCount + '_' + index);
								$newrow = $newrow.replace('msrowidentifier_0_0', 'msrowidentifier_' + sorCount + '_' + index);
								$newrow = $newrow.replace('msrowmbmsPreviousEntry_0_0', 'msrowmbmsPreviousEntry_' + sorCount + '_' + index);
								$newrow = $newrow.replace('value="1"','value="'+(index+1)+'"');
								$('.mssubmit_' + sorCount).closest('tr').before($newrow);

								patternvalidation();
							}
							$.each(measurementSheet, function(key, value){
								if(key == "womsid") {
									$('#sorMbDetails_' + sorCount + '_measurementSheets_' + index + '_woMeasurementSheet').attr('value', value);
								} else if(key == "id") {
									$('#sorMbDetails_' + sorCount + '_measurementSheets_' + index + '_id').attr('value', value);
								} else if(key == "identifier") {
									if(value == 'A')
										$('#msrowidentifier_' + sorCount + '_' + index).html('No');
									else
										$('#msrowidentifier_' + sorCount + '_' + index).html('Yes');
								} else
									$('#msrow' + key + '_' + sorCount + '_' + index).html(value);
							});
						});
					} else {
						document.getElementById('sorMbDetails[' + sorCount + '].msadd').style.display = 'none';
						$('#quantity_' + sorCount).removeAttr('readonly');
					}
					sorCount++;
				}else{
					$('#nonSorWorkOrderActivity_' + nonSorCount).val(workOrderActivity.id);
					$('.nonSorSummary_' + nonSorCount).html(workOrderActivity.summary);
					$('.nonSorDescription_' + nonSorCount).html(hint.replace(/@fulldescription@/g, workOrderActivity.description));
					$('.nonSorUom_' + nonSorCount).html(workOrderActivity.uom);
					$('.nonSorApprovedQuantity_' + nonSorCount).html(workOrderActivity.approvedQuantity);
					$('.nonSorApprovedRate_' + nonSorCount).html(parseFloat(workOrderActivity.estimateRate).toFixed(2));
					$('#nonSorUnitRate_' + nonSorCount).val(workOrderActivity.unitRate);
					$('.nonSorApprovedAmount_' + nonSorCount).html(parseFloat(workOrderActivity.activityAmount).toFixed(2));
					$('.nonSorCumulativePreviousEntry_' + nonSorCount).html(workOrderActivity.cumulativePreviousEntry);
					if (workOrderActivity.woms != "") {
						$('#nonSorQuantity_' + nonSorCount).attr('readonly', 'readonly');
						document.getElementById('nonSorMbDetails[' + nonSorCount + '].msadd').removeAttribute('style');
						var newrow= $('#msheaderrowtemplate').html();

						newrow = newrow.replace(/findNet/g, 'findNonSorNet');
						newrow=  newrow.replace(/msrowtemplate/g, 'msrownonSorMbDetails[' + nonSorCount + ']');
						newrow=  newrow.replace(/templatesorMbDetails\[0\]/g, 'nonSorMbDetails[' + nonSorCount + ']');
						newrow = newrow.replace(/templatesorMbDetails_0/g, 'nonSorMbDetails_' + nonSorCount);
						newrow = newrow.replace(/templatemssubmit_0/g, 'nonsormssubmit_' + nonSorCount);
						newrow = newrow.replace('msrowslNo_0_0', 'nonsormsrowslNo_' + nonSorCount + '_0');
						newrow = newrow.replace('msrowremarks_0_0', 'nonsormsrowremarks_' + nonSorCount + '_0');
						newrow = newrow.replace('msrowno_0_0', 'nonsormsrowno_' + nonSorCount + '_0');
						newrow = newrow.replace('msrowlength_0_0', 'nonsormsrowlength_' + nonSorCount + '_0');
						newrow = newrow.replace('msrowwidth_0_0', 'nonsormsrowwidth_' + nonSorCount + '_0');
						newrow = newrow.replace('msrowdepthOrHeight_0_0', 'nonsormsrowdepthOrHeight_' + nonSorCount + '_0');
						newrow = newrow.replace('msrowquantity_0_0', 'nonsormsrowquantity_' + nonSorCount + '_0');
						newrow = newrow.replace('msrowidentifier_0_0', 'nonsormsrowidentifier_' + nonSorCount + '_0');
						newrow = newrow.replace('msrowmbmsPreviousEntry_0_0', 'nonsormsrowmbmsPreviousEntry_' + nonSorCount + '_0');
						document.getElementById('nonSorMbDetails[' + nonSorCount + '].mstd').innerHTML=newrow;
						$(workOrderActivity.woms).each(function(index, measurementSheet){
							if (index > 0) {
								var msrowname= "nonSorMbDetails[" + nonSorCount + "].mstable";
								var msrownameid=msrowname.split(".")[0];
								var rep='measurementSheets\['+ index +'\]';

								var $newrow = "<tr>"+$('#msrowtemplate').html()+"</tr>";
								$newrow = $newrow.replace(/findNet/g, 'findNonSorNet');
								$newrow = $newrow.replace(/templatesorMbDetails\[0\]/g,msrownameid);
								$newrow = $newrow.replace(/measurementSheets\[0\]/g,rep);
								$newrow = $newrow.replace(/templatesorMbDetails_0/g, 'nonSorMbDetails_' + nonSorCount);
								$newrow = $newrow.replace(/measurementSheets_0_id/g, 'measurementSheets_' + index + '_id');
								$newrow = $newrow.replace(/measurementSheets_0_woMeasurementSheet/g, 'measurementSheets_' + index + '_woMeasurementSheet');
								$newrow = $newrow.replace('msrowslNo_0_0', 'nonsormsrowslNo_' + nonSorCount + '_' + index);
								$newrow = $newrow.replace('msrowremarks_0_0', 'nonsormsrowremarks_' + nonSorCount + '_' + index);
								$newrow = $newrow.replace('msrowno_0_0', 'nonsormsrowno_' + nonSorCount + '_' + index);
								$newrow = $newrow.replace('msrowlength_0_0', 'nonsormsrowlength_' + nonSorCount + '_' + index);
								$newrow = $newrow.replace('msrowwidth_0_0', 'nonsormsrowwidth_' + nonSorCount + '_' + index);
								$newrow = $newrow.replace('msrowdepthOrHeight_0_0', 'nonsormsrowdepthOrHeight_' + nonSorCount + '_' + index);
								$newrow = $newrow.replace('msrowquantity_0_0', 'nonsormsrowquantity_' + nonSorCount + '_' + index);
								$newrow = $newrow.replace('msrowidentifier_0_0', 'nonsormsrowidentifier_' + nonSorCount + '_' + index);
								$newrow = $newrow.replace('msrowmbmsPreviousEntry_0_0', 'nonsormsrowmbmsPreviousEntry_' + nonSorCount + '_' + index);
								$newrow = $newrow.replace('value="1"','value="'+(index+1)+'"');
								$('.nonsormssubmit_' + nonSorCount).closest('tr').before($newrow);

								patternvalidation();
							}
							$.each(measurementSheet, function(key, value){
								if(key == "womsid") {
									$('#nonSorMbDetails_' + nonSorCount + '_measurementSheets_' + index + '_woMeasurementSheet').attr('value', value);
								} else if(key == "id") {
									$('#nonSorMbDetails_' + nonSorCount + '_measurementSheets_' + index + '_id').attr('value', value);
								} else if(key == "identifier") {
									if(value == 'A')
										$('#nonsormsrowidentifier_' + nonSorCount + '_' + index).html('No');
									else
										$('#nonsormsrowidentifier_' + nonSorCount + '_' + index).html('Yes');
								} else
									$('#nonsormsrow' + key + '_' + nonSorCount + '_' + index).html(value);
							});
						});
					} else {
						document.getElementById('nonSorMbDetails[' + nonSorCount + '].msadd').style.display = 'none';
						$('#nonSorQuantity_' + nonSorCount).removeAttr('readonly');
					}
					nonSorCount++;
				}
			}
		});
	}
	
	sorTotal();
	nonSorTotal();
}

function deleteSor(obj) {
	// close all measurement sheets before deleting
	closeAllmsheet();
	var rIndex = getRow(obj).rowIndex;
	var id = $(getRow(obj)).children('td:first').children('input:first').val();
    //To get all the deleted rows id
    var aIndex = rIndex - 1;
    if(!$("#removedDetailIds").val()==""){
		$("#removedDetailIds").val($("#removedDetailIds").val()+",");
	}
    $("#removedDetailIds").val($("#removedDetailIds").val()+id);
	
	var tbl=document.getElementById('tblsor');	
	var rowcount=$("#tblsor > tbody > tr").length;
	if(rowcount==2) {
		var rowId = $(obj).attr('class').split('_').pop();
		$('#sorMbDetailsId_' + rowId).val('');
		$('#workOrderActivity_' + rowId).val('');
		$('.sorCategory_' + rowId).html('');
		$('.sorCode_' + rowId).html('');
		$('.summary_' + rowId).html('');
		$('.description_' + rowId).html('');
		$('.uom_' + rowId).html('');
		$('.approvedQuantity_' + rowId).html('');
		$('.approvedRate_' + rowId).html('');
		$('#unitRate_' + rowId).val('');
		$('.cumulativePreviousEntry_' + rowId).html('');
		$('#quantity_' + rowId).val('');
		$('.cumulativeIncludingCurrentEntry_' + rowId).html('');
		$('.amountCurrentEntry_' + rowId).html('');
		$('.amountIncludingCurrentEntry_' + rowId).html('');
		$('.approvedAmount_' + rowId).html('');
		$('#remarks_' + rowId).val('');
		$('#sorRow').prop("hidden",true);
		$('#sorRow').attr('sorinvisible', true);
		$('#message').removeAttr('hidden');
		document.getElementById('sorMbDetails[' + rowId + '].mstd').innerHTML = "";
		sorMsArray[rowId]="";
	} else {
		deleteRow('tblsor',obj);
	}
	resetIndexes();
	//starting index for table fields
	generateSlno('spansorslno');
	sorTotal();
	return true;
}

function deleteNonSor(obj) {
	// close all measurement sheets before deleting
	closeAllmsheet();
    var rIndex = getRow(obj).rowIndex;
    
    var id = $(getRow(obj)).children('td:first').children('input:first').val();
    //To get all the deleted rows id
    var aIndex = rIndex - 1;
    if(!$("#removedDetailIds").val()==""){
		$("#removedDetailIds").val($("#removedDetailIds").val()+",");
	}
    $("#removedDetailIds").val($("#removedDetailIds").val()+id);
    
	var rowcount=$("#tblNonSor > tbody > tr").length;

	if(rowcount==2) {
		var rowId = $(obj).attr('class').split('_').pop();
		$('#nonSorMbDetailsId_' + rowId).val('');
		$('#nonSorWorkOrderActivity_' + rowId).val('');
		$('.nonSorSummary_' + rowId).html('');
		$('.nonSorDescription_' + rowId).html('');
		$('.nonSorUom_' + rowId).html('');
		$('.nonSorApprovedQuantity_' + rowId).html('');
		$('.nonSorApprovedRate_' + rowId).html('');
		$('#nonSorUnitRate_' + rowId).val('');
		$('.nonSorCumulativePreviousEntry_' + rowId).html('');
		$('#nonSorQuantity_' + rowId).val('');
		$('.nonSorCumulativeIncludingCurrentEntry_' + rowId).html('');
		$('.nonSorAmountCurrentEntry_' + rowId).html('');
		$('.nonSorAmountIncludingCurrentEntry_' + rowId).html('');
		$('.nonSorApprovedAmount_' + rowId).html('');
		$('#nonSorRemarks_' + rowId).val('');
		$('#nonSorRow').prop("hidden",true);
		$('#nonSorRow').attr('nonsorinvisible', true);
		$('#nonSorMessage').removeAttr('hidden');
//		document.getElementById('nonSorMbDetails[' + rowId + '].mstd').innerHTML = $('#msheaderrowtemplate').html();
	} else {
		deleteRow('tblNonSor',obj);
	}
	resetIndexes();
	//starting index for table fields
	generateSlno('spannonsorslno');
	
	nonSorTotal();
	return true;
}

function deleteNonTendered(obj) {
	// close all measurement sheets before deleting
	closeAllmsheet();
	var rIndex = getRow(obj).rowIndex;
	var id = $(getRow(obj)).children('td:first').children('input:first').val();
    //To get all the deleted rows id
    var aIndex = rIndex - 1;
    if(!$("#removedDetailIds").val()==""){
		$("#removedDetailIds").val($("#removedDetailIds").val()+",");
	}
    $("#removedDetailIds").val($("#removedDetailIds").val()+id);
	
	var tbl=document.getElementById('tblsor');	
	var rowcount=$("#tblNonTendered > tbody > tr").length;
	if(rowcount==2) {
		var rowId = $(obj).attr('class').split('_').pop();
		$('#nonTenderedMbDetailsId_' + rowId).val('');
		$('#nonTenderedworkOrderActivity_' + rowId).val('');
		$('.nonTenderedsorCategory_' + rowId).html('');
		$('.nonTenderedsorCode_' + rowId).html('');
		$('.nonTenderedsummary_' + rowId).html('');
		$('.nonTendereddescription_' + rowId).html('');
		$('.nonTendereduom_' + rowId).html('');
		$('.nonTenderedapprovedQuantity_' + rowId).html('');
		$('.nonTenderedapprovedRate_' + rowId).html('');
		$('#nonTenderedunitRate_' + rowId).val('');
		$('.nonTenderedcumulativePreviousEntry_' + rowId).html('');
		$('#nonTenderedquantity_' + rowId).val('');
		$('.nonTenderedcumulativeIncludingCurrentEntry_' + rowId).html('');
		$('.nonTenderedAmountCurrentEntry_' + rowId).html('');
		$('.nonTenderedamountIncludingCurrentEntry_' + rowId).html('');
		$('.nonTenderedapprovedAmount_' + rowId).html('');
		$('#nonTenderedremarks_' + rowId).val('');
		$('#nonTenderedRow').prop("hidden",true);
		$('#nonTenderedRow').attr('sorinvisible', true);
		$('#nonTenderedMessage').removeAttr('hidden');
		document.getElementById('nonTenderedMbDetails[' + rowId + '].mstd').innerHTML = "";
		nonTenderedMsArray[rowId]="";
	} else {
		deleteRow('tblNonTendered',obj);
	}
	resetIndexes();
	//starting index for table fields
	generateSlno('spannontenderedslno');
	nonTenderedTotal();
	return true;
}

function deleteLumpSum(obj) {
	// close all measurement sheets before deleting
	closeAllmsheet();
    var rIndex = getRow(obj).rowIndex;
    
    var id = $(getRow(obj)).children('td:first').children('input:first').val();
    //To get all the deleted rows id
    var aIndex = rIndex - 1;
    if(!$("#removedDetailIds").val()==""){
		$("#removedDetailIds").val($("#removedDetailIds").val()+",");
	}
    $("#removedDetailIds").val($("#removedDetailIds").val()+id);
    
	var rowcount=$("#tblLumpSum > tbody > tr").length;

	if(rowcount==2) {
		var rowId = $(obj).attr('class').split('_').pop();
		$('#lumpSumMbDetailsId_' + rowId).val('');
		$('#lumpSumWorkOrderActivity_' + rowId).val('');
		$('.lumpSumSummary_' + rowId).html('');
		$('.lumpSumDescription_' + rowId).html('');
		$('.lumpSumUom_' + rowId).html('');
		$('.lumpSumApprovedQuantity_' + rowId).html('');
		$('.lumpSumApprovedRate_' + rowId).html('');
		$('#lumpSumUnitRate_' + rowId).val('');
		$('.lumpSumCumulativePreviousEntry_' + rowId).html('');
		$('#lumpSumQuantity_' + rowId).val('');
		$('.lumpSumCumulativeIncludingCurrentEntry_' + rowId).html('');
		$('.lumpSumAmountCurrentEntry_' + rowId).html('');
		$('.lumpSumAmountIncludingCurrentEntry_' + rowId).html('');
		$('.lumpSumApprovedAmount_' + rowId).html('');
		$('#lumpSumRemarks_' + rowId).val('');
		$('#lumpSumRow').prop("hidden",true);
		$('#lumpSumRow').attr('nonsorinvisible', true);
		$('#lumpSumMessage').removeAttr('hidden');
//		document.getElementById('lumpSumMbDetails[' + rowId + '].mstd').innerHTML = $('#msheaderrowtemplate').html();
	} else {
		deleteRow('tblLumpSum',obj);
	}
	resetIndexes();
	//starting index for table fields
	generateSlno('spanlumpsumslno');
	
	lumpSumTotal();
	return true;
}

function generateSlno(className)
{
	var idx=1;
	$("." + className).each(function(){
		$(this).text(idx);
		idx++;
	});
}

function addRow(tableName,rowName) {
	if (document.getElementById(rowName) != null) {
		// get Next Row Index to Generate
		var nextIdx = 0;
		var sno = 1;
		nextIdx = jQuery("#"+tableName+" > tbody > tr").length;
		sno = nextIdx + 1;
		
		// Generate all textboxes Id and name with new index
		jQuery("#"+rowName).clone().find("input,select, errors, span, input:hidden, textarea, button").each(function() {	
			var classval = jQuery(this).attr('class');
			if (jQuery(this).data('server')) {
				jQuery(this).removeAttr('data-server');
			}
			if(classval == 'spansno') {
				jQuery(this).text(sno);
			}
			
			if(classval == 'assetdetail') {
				 $(this).html('');
			     $(this).val(''); 
			} 
			jQuery(this).attr(
					{
						'name' : function(_, name) {
							if(!(jQuery(this).attr('name')===undefined))
								return name.replace(/\d+/, nextIdx); 
						},
						'id' : function(_, id) {
							if(!(jQuery(this).attr('id')===undefined))
								return id.replace(/\d+/, nextIdx); 
						},
						'class' : function(_, name) {
							if(!(jQuery(this).attr('class')===undefined))
								return name.replace(/\d+/, nextIdx); 
						}/*,
						'data-idx' : function(_,dataIdx)
						{
							return nextIdx++;
						}*/
					});
				// if element is static attribute hold values for next row, otherwise it will be reset
				if (!jQuery(this).data('static')) {
					jQuery(this).val('');
				}

		}).end().appendTo("#"+tableName+" > tbody");	
		sno++;
		
	}
}

function deleteRow(tableName,obj){
	 var rIndex = getRow(obj).rowIndex;
	    var id = jQuery(getRow(obj)).children('td:first').children('input:first').val();
	    //To get all the deleted rows id
	    var aIndex = rIndex - 1;
		var tbl=document.getElementById(tableName);	
		var rowcount=jQuery("#"+tableName+" > tbody > tr").length;
	    if(rowcount<=1) {
	    	bootbox.alert("This row can not be deleted");
			return false;
		} else {
			tbl.deleteRow(rIndex);
			//starting index for table fields
			var idx= 0;
			var sno = 1;
			//regenerate index existing inputs in table row
			jQuery("#"+tableName+" > tbody > tr").each(function() {
			
					jQuery(this).find("input, select, errors, span, input:hidden, textarea, button").each(function() {
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
			return true;
		}
}

function validateQuantityInput(object) {
    var valid = /^[0-9](\d{0,9})(\.\d{0,4})?$/.test($(object).val()),
        val = $(object).val();
    
    if(!valid){
        console.log("Invalid input!");
        $(object).val(val.substring(0, val.length - 1));
    }
}

function calculateSorAmounts(currentObj) {
	rowcount = $(currentObj).attr('id').split('_').pop();
	
	var toleranceLimit = $('#quantityTolerance').val();
	var cumulativePreviousEntry = parseFloat($('.cumulativePreviousEntry_' + rowcount).html().trim());
	var currentQuantity = $(currentObj).val() == "" ? 0 : $(currentObj).val();
	var unitRate = parseFloat($('#unitRate_' + rowcount).val().trim());
	var approvedQuantity = parseFloat($('.approvedQuantity_' + rowcount).html().trim());
	var toleranceQuantity = parseFloat(approvedQuantity * parseFloat(toleranceLimit) / 100);
	var cumulativeIncludingCurrentEntry = parseFloat(parseFloat(cumulativePreviousEntry) + parseFloat(currentQuantity)).toFixed(4);
	if (toleranceQuantity < cumulativeIncludingCurrentEntry) {
		var message = $('#errorcumulativequantity').val();
		var sorCode = $('.sorCode_' + rowcount).html();
		message = message.replace(/\{0\}/g, toleranceLimit) + " for the SOR " + sorCode;
		bootbox.alert(message);
		$(currentObj).val('');
		$('.cumulativeIncludingCurrentEntry_' + rowcount).html('');
		$('.amountCurrentEntry_' + rowcount).html('');
		$('.amountIncludingCurrentEntry_' + rowcount).html('');
		return false;
	}
	
	var amountCurrentEntry = parseFloat(parseFloat(unitRate) * parseFloat(currentQuantity)).toFixed(2);
	var amountIncludingCurrentEntry = parseFloat(parseFloat(unitRate) * parseFloat(cumulativeIncludingCurrentEntry)).toFixed(2);
	
	$('.cumulativeIncludingCurrentEntry_' + rowcount).html(cumulativeIncludingCurrentEntry);
	$('.amountCurrentEntry_' + rowcount).html(amountCurrentEntry);
	$('.amountIncludingCurrentEntry_' + rowcount).html(amountIncludingCurrentEntry);
	$('#amount_' + rowcount).val(amountCurrentEntry);
	
	sorTotal();
}
function calculateNonTenderedAmounts(currentObj) {
	rowcount = $(currentObj).attr('id').split('_').pop();
	
	var toleranceLimit = $('#quantityTolerance').val();
	var cumulativePreviousEntry = parseFloat($('.nonTenderedcumulativePreviousEntry_' + rowcount).html().trim());
	var currentQuantity = $(currentObj).val() == "" ? 0 : $(currentObj).val();
	var unitRate = parseFloat($('#nonTenderedunitRate_' + rowcount).val().trim());
	var approvedQuantity = parseFloat($('.nonTenderedapprovedQuantity_' + rowcount).html().trim());
	var toleranceQuantity = parseFloat(approvedQuantity * parseFloat(toleranceLimit) / 100);
	var cumulativeIncludingCurrentEntry = parseFloat(parseFloat(cumulativePreviousEntry) + parseFloat(currentQuantity)).toFixed(4);
	if (toleranceQuantity < cumulativeIncludingCurrentEntry) {
		var message = $('#errorcumulativequantity').val();
		var sorCode = $('.nonTenderedsorCode_' + rowcount).html();
		message = message.replace(/\{0\}/g, toleranceLimit) + " for the Non Tendered " + sorCode;
		bootbox.alert(message);
		$(currentObj).val('');
		$('.nonTenderedcumulativeIncludingCurrentEntry_' + rowcount).html('');
		$('.nonTenderedAmountCurrentEntry_' + rowcount).html('');
		$('.nonTenderedamountIncludingCurrentEntry_' + rowcount).html('');
		return false;
	}
	
	var amountCurrentEntry = parseFloat(parseFloat(unitRate) * parseFloat(currentQuantity)).toFixed(2);
	var amountIncludingCurrentEntry = parseFloat(parseFloat(unitRate) * parseFloat(cumulativeIncludingCurrentEntry)).toFixed(2);
	
	$('.nonTenderedcumulativeIncludingCurrentEntry_' + rowcount).html(cumulativeIncludingCurrentEntry);
	$('.nonTenderedAmountCurrentEntry_' + rowcount).html(amountCurrentEntry);
	$('.nonTenderedamountIncludingCurrentEntry_' + rowcount).html(amountIncludingCurrentEntry);
	$('#nonTenderedamount_' + rowcount).val(amountCurrentEntry);
	
	nonTenderedTotal();
}
function calculateNonSorAmounts(currentObj) {
	rowcount = $(currentObj).attr('id').split('_').pop();
	
	var toleranceLimit = $('#quantityTolerance').val();
	var cumulativePreviousEntry = parseFloat($('.nonSorCumulativePreviousEntry_' + rowcount).html().trim());
	var currentQuantity = $(currentObj).val() == "" ? 0 : $(currentObj).val();
	var unitRate = parseFloat($('#nonSorUnitRate_' + rowcount).val().trim());
	var approvedQuantity = parseFloat($('.nonSorApprovedQuantity_' + rowcount).html().trim());
	var toleranceQuantity = parseFloat(approvedQuantity * parseFloat(toleranceLimit) / 100);
	var cumulativeIncludingCurrentEntry = parseFloat(parseFloat(cumulativePreviousEntry) + parseFloat(currentQuantity)).toFixed(4);
	if (toleranceQuantity < cumulativeIncludingCurrentEntry) {
		var message = $('#errorcumulativequantity').val();
		var nonSorCode = $('.nonSorCode_' + rowcount).html();
		message = message.replace(/\{0\}/g, toleranceLimit) + " for the NON SOR " + nonSorCode;
		bootbox.alert(message);
		$(currentObj).val('');
		$('.nonSorCumulativeIncludingCurrentEntry_' + rowcount).html('');
		$('.nonSorAmountCurrentEntry_' + rowcount).html('');
		$('.nonSorAmountIncludingCurrentEntry_' + rowcount).html('');
		return false;
	}
	
	var amountCurrentEntry = parseFloat(parseFloat(unitRate) * parseFloat(currentQuantity)).toFixed(2);
	var amountIncludingCurrentEntry = parseFloat(parseFloat(unitRate) * parseFloat(cumulativeIncludingCurrentEntry)).toFixed(2);
	
	$('.nonSorCumulativeIncludingCurrentEntry_' + rowcount).html(cumulativeIncludingCurrentEntry);
	$('.nonSorAmountCurrentEntry_' + rowcount).html(amountCurrentEntry);
	$('.nonSorAmountIncludingCurrentEntry_' + rowcount).html(amountIncludingCurrentEntry);
	$('#nonSorAmount_' + rowcount).val(amountCurrentEntry);
	
	nonSorTotal();
}

function calculateLumpSumAmounts(currentObj) {
	rowcount = $(currentObj).attr('id').split('_').pop();
	
	var toleranceLimit = $('#quantityTolerance').val();
	var cumulativePreviousEntry = parseFloat($('.lumpSumCumulativePreviousEntry_' + rowcount).html().trim());
	var currentQuantity = $(currentObj).val() == "" ? 0 : $(currentObj).val();
	var unitRate = parseFloat($('#lumpSumUnitRate_' + rowcount).val().trim());
	var approvedQuantity = parseFloat($('.lumpSumApprovedQuantity_' + rowcount).html().trim());
	var toleranceQuantity = parseFloat(approvedQuantity * parseFloat(toleranceLimit) / 100);
	var cumulativeIncludingCurrentEntry = parseFloat(parseFloat(cumulativePreviousEntry) + parseFloat(currentQuantity)).toFixed(4);
	if (toleranceQuantity < cumulativeIncludingCurrentEntry) {
		var message = $('#errorcumulativequantity').val();
		var nonSorCode = $('.lumpSumCode_' + rowcount).html();
		message = message.replace(/\{0\}/g, toleranceLimit) + " for the Lump Sum " + nonSorCode;
		bootbox.alert(message);
		$(currentObj).val('');
		$('.lumpSumCumulativeIncludingCurrentEntry_' + rowcount).html('');
		$('.lumpSumAmountCurrentEntry_' + rowcount).html('');
		$('.lumpSumAmountIncludingCurrentEntry_' + rowcount).html('');
		return false;
	}
	
	var amountCurrentEntry = parseFloat(parseFloat(unitRate) * parseFloat(currentQuantity)).toFixed(2);
	var amountIncludingCurrentEntry = parseFloat(parseFloat(unitRate) * parseFloat(cumulativeIncludingCurrentEntry)).toFixed(2);
	
	$('.lumpSumCumulativeIncludingCurrentEntry_' + rowcount).html(cumulativeIncludingCurrentEntry);
	$('.lumpSumAmountCurrentEntry_' + rowcount).html(amountCurrentEntry);
	$('.lumpSumAmountIncludingCurrentEntry_' + rowcount).html(amountIncludingCurrentEntry);
	$('#lumpSumAmount_' + rowcount).val(amountCurrentEntry);
	
	lumpSumTotal();
}

function sorTotal() {
	var total = 0.0;
	$('.amountCurrentEntry').each(function() {
		if($(this).html().trim() != "")
			total = parseFloat(parseFloat(total) + parseFloat($(this).html().replace(',', ''))).toFixed(2);
	});
	$('#sorTotal').html(total);
	calculateTotalValue();
}

function nonTenderedTotal() {
	var total = 0.0;
	$('.nonTenderedAmountCurrentEntry').each(function() {
		if($(this).html().trim() != "")
			total = parseFloat(parseFloat(total) + parseFloat($(this).html().replace(',', ''))).toFixed(2);
	});
	$('#nonTenderedTotal').html(total);
	calculateTotalValue();
}

function nonSorTotal() {
	var total = 0.0;
	$('.nonSorAmountCurrentEntry').each(function() {
		if($(this).html().trim() != "")
			total = parseFloat(parseFloat(total) + parseFloat($(this).html().replace(',', ''))).toFixed(2);
	});
	$('#nonSorTotal').html(total);
	calculateTotalValue();
}

function lumpSumTotal() {
	var total = 0.0;
	$('.lumpSumAmountCurrentEntry').each(function() {
		if($(this).html().trim() != "")
			total = parseFloat(parseFloat(total) + parseFloat($(this).html().replace(',', ''))).toFixed(2);
	});
	$('#lumpSumTotal').html(total);
	calculateTotalValue();
}

function calculateTotalValue() {
	var sorTotal = $('#sorTotal').html();
	var nonSorTotal = $('#nonSorTotal').html();
	var nonTenderedTotal = $('#nonTenderedTotal').html();
	var lumpSumTotal = $('#lumpSumTotal').html();
	if(sorTotal == '')
		sorTotal = 0.0;
	if(nonSorTotal == '')
		nonSorTotal = 0.0;
	if(nonTenderedTotal == '')
		nonTenderedTotal = 0.0;
	if(lumpSumTotal == '')
		lumpSumTotal = 0.0;
	
	var total = parseFloat(parseFloat(sorTotal) + parseFloat(nonSorTotal)).toFixed(2);
	
	$('#pageTotal').html(parseFloat(parseFloat(sorTotal) + parseFloat(nonSorTotal)).toFixed(2));
	$('#nonTenderedPageTotal').html(parseFloat(parseFloat(nonTenderedTotal) + parseFloat(lumpSumTotal)).toFixed(2));
	
	var tenderFinalisedPercentage = 0;
	if($('#tenderFinalisedPercentage').html().trim() != '')
		tenderFinalisedPercentage = parseFloat($('#tenderFinalisedPercentage').html());
	var mbAmount = parseFloat(parseFloat(total) + (parseFloat(parseFloat(tenderFinalisedPercentage) / 100) * parseFloat(total))).toFixed(2);
	mbAmount = parseFloat(parseFloat(mbAmount) + parseFloat(nonTenderedTotal) + parseFloat(lumpSumTotal)).toFixed(2);
	$('#mbAmount').val(mbAmount);
	$('#mbAmountSpan').html(mbAmount);
	if(!isNaN(mbAmount))
		$('#amountRule').val(mbAmount);
}

function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}

function deleteEmptyRows(){
	var hiddenRowCount = $("#tblsor > tbody > tr[sorinvisible='true']").length;
	if(hiddenRowCount == 1) {
		
		$('#tblsor').find('input, textarea').each(function() {
			$(this).attr('disabled', 'disabled');
		});
	}
	
	hiddenRowCount = $("#tblNonSor > tbody > tr[nonsorinvisible='true']").length;
	if(hiddenRowCount == 1) {
		$('#tblNonSor').find('input, textarea').each(function() {
			$(this).attr('disabled', 'disabled');
		});
	}
	
	hiddenRowCount = $("#tblNonTendered > tbody > tr[sorinvisible='true']").length;
	if(hiddenRowCount == 1) {
		$('#tblNonTendered').find('input, textarea').each(function() {
			$(this).attr('disabled', 'disabled');
		});
	}
	
	hiddenRowCount = $("#tblLumpSum > tbody > tr[nonsorinvisible='true']").length;
	if(hiddenRowCount == 1) {
		$('#tblLumpSum').find('input, textarea').each(function() {
			$(this).attr('disabled', 'disabled');
		});
	}
}

function validateSORDetails() {
	if($('#mbHeader').valid()) {
		deleteEmptyRows();
	} else
		return false;
}

function validateFormData() {
	var flag = true;
	var isRequired = true;
	var previousMBDate = $('#previousMBDate').val();
	var mbDate = $('#mbDate').data('datepicker').date;
	var workCommencedDate = $('#workCommencedDate').val();
	var mbIssuedDate = $('#mbIssuedDate').data('datepicker').date;
	var fromPageNo = $('#fromPageNo').val();
	var toPageNo = $('#toPageNo').val();
	var totalMBAmountOfMBs = $('#totalMBAmountOfMBs').val();
	var mbAmount = $('#mbAmount').val();
	var workOrderAmount = $('#workOrderAmount').val();
	$('#amountRule').val(mbAmount);
	
	$(".required").each(function() {
		if ($(this).val() == '') {
			isRequired = false;
		}
	});
	
	if(!isRequired) {
		bootbox.alert($('#errorMandatory').val());
		return false;
	}
	if (totalMBAmountOfMBs == "")
		totalMBAmountOfMBs = 0;
	if (parseFloat(workOrderAmount) < parseFloat(parseFloat(totalMBAmountOfMBs) + parseFloat(mbAmount))) {
		var message = $('#errortotalmbamount').val();
		message = message.replace(/\{0\}/g, parseFloat(totalMBAmountOfMBs) + parseFloat(mbAmount));
		message = message.replace(/\{1\}/g, parseFloat(parseFloat(totalMBAmountOfMBs) + parseFloat(mbAmount) - parseFloat(workOrderAmount)).toFixed(2));
		bootbox.alert(message);
		return false;
	}
	
	if(previousMBDate != "" && new Date(previousMBDate).getTime() > mbDate.getTime()) {
		bootbox.alert($('#errorpreviousmbdate').val());
		$('#mbDate').val('');
		return false;
	}
	
	if(mbDate.getTime() < new Date(workCommencedDate).getTime()) {
		var dateArray = workCommencedDate.split('-');
		var message = $('#errorentrydate').val();
		message = message.replace(/\{0\}/g, $('#mbDate').val());
		message = message.replace(/\{1\}/g, dateArray[2] + '/' + dateArray[1] + '/' + dateArray[0]);
		bootbox.alert(message);
		$('#mbDate').val('');
		return false;
	}
	
	if($('#mbIssuedDate').val() != "" && mbIssuedDate > mbDate) {
		bootbox.alert($('#errorentryissueddate').val());
		$('#mbDate').val('');
		return false;
	}
	
	if($('#mbIssuedDate').val() != "" && mbIssuedDate.getTime() < new Date(workCommencedDate).getTime()) {
		bootbox.alert($('#errorissueddate').val());
		$('#mbIssuedDate').val('');
		return false;
	}
	
	if(parseFloat(toPageNo) < parseFloat(fromPageNo)) {
		bootbox.alert($('#errorfromtopage').val());
		return false;
	}
	
	var inVisibleSorCount = $("#tblsor > tbody > tr[sorinvisible='true']").length;
	var inVisibleNonSorCount = $("#tblNonSor > tbody > tr[nonsorinvisible='true']").length;
	var inVisibleNonTenderedCount = $("#tblNonTendered > tbody > tr[sorinvisible='true']").length;
	var inVisibleLumpSumCount = $("#tbllumpSum > tbody > tr[nonsorinvisible='true']").length;
	if (inVisibleSorCount == 1 && inVisibleNonSorCount == 1 && inVisibleNonTenderedCount == 1 && inVisibleLumpSumCount == 1) {
		bootbox.alert($('#errorsornonsor').val());
		return false;
	}
	
	var message = "";
	message = " for SOR Sl No : ";
	var index = 0;
	$("#tblsor > tbody > tr[sorinvisible!='true'] .quantity").each(function() {
		index++;
		if ($(this).val() == "" || parseFloat($(this).val()) <= 0) {
			flag = false;
			message = message + index + ", ";
		}
	});
	if (!flag) {
		message = message.slice(0,-2);
	} else {
		message = "";
	}
	index = 0;
	var slNo = "";
	$("#tblNonSor > tbody > tr[nonsorinvisible!='true'] .quantity").each(function() {
		index++;
		if ($(this).val() == "" || parseFloat($(this).val()) <= 0) {
			flag = false;
			slNo = slNo + index + ", ";
		}
	});
	
	if (!flag) {
		if(slNo != "") {
			slNo = slNo.slice(0, -2);
			if(message == "")
				message += " for Non SOR Sl No : " + slNo;
			else
				message += " and for Non SOR Sl No : " + slNo;
		}
		bootbox.alert($('#errorquantitieszero').val() + message);
		return false;
	}
	index = 0;
	var slNo = "";
	$("#tblNonTendered > tbody > tr[sorinvisible!='true'] .quantity").each(function() {
		index++;
		if ($(this).val() == "" || parseFloat($(this).val()) <= 0) {
			flag = false;
			slNo = slNo + index + ", ";
		}
	});
	
	if (!flag) {
		if(slNo != "") {
			slNo = slNo.slice(0, -2);
			if(message == "")
				message += " for Non Tendered Sl No : " + slNo;
			else
				message += " and for Non Tendered Sl No : " + slNo;
		}
		bootbox.alert($('#errorquantitieszero').val() + message);
		return false;
	}
	index = 0;
	var slNo = "";
	$("#tblLumpSum > tbody > tr[nonsorinvisible!='true'] .quantity").each(function() {
		index++;
		if ($(this).val() == "" || parseFloat($(this).val()) <= 0) {
			flag = false;
			slNo = slNo + index + ", ";
		}
	});
	
	if (!flag) {
		if(slNo != "") {
			slNo = slNo.slice(0, -2);
			if(message == "")
				message += " for Lump Sum Sl No : " + slNo;
			else
				message += " and for Lump Sum Sl No : " + slNo;
		}
		bootbox.alert($('#errorquantitieszero').val() + message);
		return false;
	}
	return flag;
}

function viewMBWorkOrder() {
	var workOrderId = $('#workOrderId').val();
	window.open("/egworks/letterofacceptance/view/" + workOrderId, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function viewEstimate() {
	var estimateId = $('#estimateId').val();
	window.open("/egworks/abstractestimate/view/" + estimateId, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function viewMBHistory() {
	var mbheaderId = $('#id').val();
	window.open("/egworks/mb/history/" + mbheaderId, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}
function viewContractorMB(id) {
	window.open("/egworks//contractorportal/mb/view/" + id, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function showHideApprovalDetails(workFlowAction) {
	var isValidAction=true;
	var mbAmount = $('#mbAmount').val();

	$.ajax({
		url: "/egworks/mbheader/ajax-showhidembappravaldetails",     
		type: "GET",
		async: false,
		data: {
			amountRule : mbAmount,
			additionalRule : $('#additionalRule').val()
		},
		dataType: "json",
		success: function (response) {
			if(response) {
				removeApprovalMandatoryAttribute();
				if(workFlowAction == 'Forward') {
					bootbox.alert($('#errorAmountRuleApprove').val());
					isValidAction = false;
				} else {
					isValidAction = true;
				}
				
			} else {
				addApprovalMandatoryAttribute();
				if(workFlowAction == 'Create And Approve') {
					bootbox.alert($('#errorAmountRuleForward').val());
					isValidAction = false;
				} else {
					isValidAction = true;
				}

			}
			
		}, 
		error: function (response) {
			console.log("failed");
		},
		complete : function(){
			$('.loader-class').modal('hide');
		}
	});
	if(isValidAction)
		return true;
	else
		return false;
}