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

$(document).ready(function(){
	sorTotal();
	nonSorTotal();
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

function resetIndexes() {
	var idx = 0;
	
	//regenerate index existing inputs in table row
	$(".sorRow").each(function() {
		$(this).find("input, span, select, textarea").each(function() {
			if (!$(this).is('span')) {
				$(this).attr({
					'name' : function(_, name) {
						return name.replace(/\d+/, idx);
					},
					'id' : function(_, id) {
						return id.replace(/\d+/, idx);
					},
					'data-idx' : function(_, dataIdx) {
						return idx;
					}
				});
			} else {
				$(this).attr({
					'class' : function(_, name) {
						return name.replace(/\d+/, idx);
					}
				});
			}
		});
		idx++;
	});
	
	idx = 0;
	
	$(".nonSorRow").each(function() {
		$(this).find("input, span, select, textarea").each(function() {
			if (!$(this).is('span')) {
				$(this).attr({
					'name' : function(_, name) {
						return name.replace(/\d+/, idx);
					},
					'id' : function(_, id) {
						return id.replace(/\d+/, idx);
					},
					'data-idx' : function(_, dataIdx) {
						return idx;
					}
				});
			} else {
				$(this).attr({
					'class' : function(_, name) {
						return name.replace(/\d+/, idx);
					}
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
		var sorCount = $("#tblsor tbody tr:visible[id='sorRow']").length;
		var nonSorCount = $("#tblNonSor tbody tr:visible[id='nonSorRow']").length;
		$(data.data).each(function(index, workOrderActivity){
			if($.inArray("" + workOrderActivity.id + "", existingActivityArray) == -1
					&& $.inArray("" + workOrderActivity.id + "", activityArray) != -1) {
				if(sorCount==0 && workOrderActivity.sorNonSorType == 'SOR'){
					$('#message').prop("hidden",true);
					$('#sorRow').removeAttr("hidden");
					$('#sorRow').removeAttr('sorinvisible');
				}else{
					if(workOrderActivity.sorNonSorType == 'SOR'){
						var key = $("#tblsor tbody tr:visible[id='sorRow']").length;
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
							var key = $("#tblNonSor tbody tr:visible[id='nonSorRow']").length;
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
					nonSorCount++;
				}
			}
		});
	} else {
		var sorCount = $("#tblsor tbody tr:visible[id='sorRow']").length;
		var nonSorCount = $("#tblNonSor tbody tr:visible[id='nonSorRow']").length;
		$(data.data).each(function(index, workOrderActivity){
			if($.inArray("" + workOrderActivity.id + "", existingActivityArray) == -1) {
				if(sorCount==0 && workOrderActivity.sorNonSorType == 'SOR'){
					$('#message').prop("hidden",true);
					$('#sorRow').removeAttr("hidden");
					$('#sorRow').removeAttr('sorinvisible');
				}else{
					if(workOrderActivity.sorNonSorType == 'SOR'){
						var key = $("#tblsor tbody tr:visible[id='sorRow']").length;
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
							var key = $("#tblNonSor tbody tr:visible[id='nonSorRow']").length;
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
					nonSorCount++;
				}
			}
		});
	}
	
	sorTotal();
	nonSorTotal();
}

function deleteSor(obj) {
	var rIndex = getRow(obj).rowIndex;
	var id = $(getRow(obj)).children('td:first').children('input:first').val();
    //To get all the deleted rows id
    var aIndex = rIndex - 1;
    if(!$("#removedDetailIds").val()==""){
		$("#removedDetailIds").val($("#removedDetailIds").val()+",");
	}
    $("#removedDetailIds").val($("#removedDetailIds").val()+id);
	
	var tbl=document.getElementById('tblsor');	
	var rowcount=$("#tblsor tbody tr").length;
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
    var rIndex = getRow(obj).rowIndex;
    
    var id = $(getRow(obj)).children('td:first').children('input:first').val();
    //To get all the deleted rows id
    var aIndex = rIndex - 1;
    if(!$("#removedDetailIds").val()==""){
		$("#removedDetailIds").val($("#removedDetailIds").val()+",");
	}
    $("#removedDetailIds").val($("#removedDetailIds").val()+id);
    
	var rowcount=$("#tblNonSor tbody tr").length;

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
	} else {
		deleteRow('tblNonSor',obj);
	}
	resetIndexes();
	//starting index for table fields
	generateSlno('spannonsorslno');
	
	nonSorTotal();
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
		nextIdx = jQuery("#"+tableName+" tbody tr").length;
		sno = nextIdx + 1;
		
		// Generate all textboxes Id and name with new index
		jQuery("#"+rowName).clone().find("input,select, errors, span, input:hidden, textarea").each(function() {	
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

		}).end().appendTo("#"+tableName+" tbody");	
		sno++;
		
	}
}

function deleteRow(tableName,obj){
	 var rIndex = getRow(obj).rowIndex;
	    var id = jQuery(getRow(obj)).children('td:first').children('input:first').val();
	    //To get all the deleted rows id
	    var aIndex = rIndex - 1;
		var tbl=document.getElementById(tableName);	
		var rowcount=jQuery("#"+tableName+" tbody tr").length;
	    if(rowcount<=1) {
	    	bootbox.alert("This row can not be deleted");
			return false;
		} else {
			tbl.deleteRow(rIndex);
			//starting index for table fields
			var idx= 0;
			var sno = 1;
			//regenerate index existing inputs in table row
			jQuery("#"+tableName+" tbody tr").each(function() {
			
					jQuery(this).find("input, select, errors, span, input:hidden, textarea").each(function() {
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
		bootbox.alert($('#errorcumulativequantity').val());
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
		bootbox.alert($('#errorcumulativequantity').val());
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

function sorTotal() {
	var total = 0.0;
	$('.amountCurrentEntry').each(function() {
		if($(this).html().trim() != "")
			total = parseFloat(parseFloat(total) + parseFloat($(this).html().replace(',', ''))).toFixed(2);
	});
	$('#sorTotal').html(total);
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

function calculateTotalValue() {
	var sorTotal = $('#sorTotal').html();
	var nonSorTotal = $('#nonSorTotal').html();
	if(sorTotal == '')
		sorTotal = 0.0;
	if(nonSorTotal == '')
		nonSorTotal = 0.0;
	
	var total = parseFloat(parseFloat(sorTotal) + parseFloat(nonSorTotal) ).toFixed(2);
	$('#pageTotal').html(total);
	
	var tenderFinalisedPercentage = 0;
	if($('#tenderFinalisedPercentage').html().trim() != '')
		tenderFinalisedPercentage = parseFloat($('#tenderFinalisedPercentage').html());
	var mbAmount = parseFloat(parseFloat(total) + (parseFloat(parseFloat(tenderFinalisedPercentage) / 100) * parseFloat(total))).toFixed(2);
	
	$('#mbAmount').val(mbAmount);
	$('#mbAmountSpan').html(mbAmount);
}

function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}

function validateSORDetails() {
	if($('#mbHeader').valid()) {
		var hiddenRowCount = $("#tblsor tbody tr[sorinvisible='true']").length;
		if(hiddenRowCount == 1) {
			
			$('#tblsor').find('input, textarea').each(function() {
				$(this).attr('disabled', 'disabled');
			});
		}
		
		hiddenRowCount = $("#tblNonSor tbody tr[nonsorinvisible='true']").length;
		if(hiddenRowCount == 1) {
			$('#tblNonSor').find('input, textarea').each(function() {
				$(this).attr('disabled', 'disabled');
			});
		}
		return true;
	} else
		return false;
}

function validateFormData() {
	var flag = true;
	var mbDate = $('#mbDate').data('datepicker').date;
	var workCommencedDate = $('#workCommencedDate').val();
	var mbIssuedDate = $('#mbIssuedDate').data('datepicker').date;
	var fromPageNo = $('#fromPageNo').val();
	var toPageNo = $('#toPageNo').val();
	var totalMBAmountOfMBs = $('#totalMBAmountOfMBs').val();
	var mbAmount = $('#mbAmount').val();
	var workOrderAmount = $('#workOrderAmount').val();
	
	if (parseFloat(workOrderAmount) < parseFloat(parseFloat(totalMBAmountOfMBs) + parseFloat(mbAmount))) {
		var message = $('#errortotalmbamount').val();
		message = message.replace(/\{0\}/g, parseFloat(totalMBAmountOfMBs) + parseFloat(mbAmount));
		message = message.replace(/\{1\}/g, workOrderAmount);
		bootbox.alert(message);
		return false;
	}
	
	if(mbDate.getTime() < new Date(workCommencedDate).getTime()) {
		bootbox.alert($('#errorentrydate').val());
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
	
	var inVisibleSorCount = $("#tblsor tbody tr[sorinvisible='true']").length;
	var inVisibleNonSorCount = $("#tblNonSor tbody tr[nonsorinvisible='true']").length;
	if (inVisibleSorCount == 1 && inVisibleNonSorCount == 1) {
		bootbox.alert($('#errorsornonsor').val());
		return false;
	}
	
	$("#tblsor tbody tr[sorinvisible!='true'] .quantity").each(function() {
		if (parseFloat($(this).val()) <= 0)
			flag = false;
	});
	$("#tblNonSor tbody tr[nonsorinvisible!='true'] .quantity").each(function() {
		if (parseFloat($(this).val()) <= 0)
			flag = false;
	});
	if (!flag) {
		bootbox.alert($('#errorquantitieszero').val());
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