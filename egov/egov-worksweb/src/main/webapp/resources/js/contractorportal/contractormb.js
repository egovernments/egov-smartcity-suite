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

function validateQuantityInput(object) {
    var valid = /^[0-9](\d{0,9})(\.\d{0,4})?$/.test($(object).val()),
        val = $(object).val();
    
    if(!valid){
        console.log("Invalid input!");
        $(object).val(val.substring(0, val.length - 1));
    }
}

function calculateActivityAmounts(currentObj) {
	rowcount = $(currentObj).attr('id').split('_').pop();
	var currentQuantity = $(currentObj).val() == "" ? 0 : $(currentObj).val();
	var unitRate = parseFloat($('#unitRate_' + rowcount).val().trim());
	var amountCurrentEntry = parseFloat(parseFloat(unitRate) * parseFloat(currentQuantity)).toFixed(2);
	$('#amount_' + rowcount).html(amountCurrentEntry);
	$('#hiddenAmount_' + rowcount).val(amountCurrentEntry);
	mbTotal();
}

function mbTotal() {
	var total = 0.0;
	var tenderedTotal = 0.0;
	var nonTenderedTotal = 0.0;
	var tenderPercentage = $('#tenderFinalizedPercentage').html();
	$('.tendered').each(function() {
		if($(this).html().trim() != "")
			tenderedTotal = parseFloat(parseFloat(tenderedTotal) + parseFloat($(this).html().replace(',', ''))).toFixed(2);
	});
	tenderedTotal = parseFloat(parseFloat(tenderedTotal) + (parseFloat(tenderedTotal) * parseFloat(tenderPercentage) / 100)).toFixed(2);
	
	$('.nontendered').each(function() {
		if($(this).html().trim() != "")
			nonTenderedTotal = parseFloat(parseFloat(nonTenderedTotal) + parseFloat($(this).html().replace(',', ''))).toFixed(2);
	});
	
	total = parseFloat(parseFloat(nonTenderedTotal) + parseFloat(tenderedTotal)).toFixed(2);
	$('#mbTotal').html(total);
	calculateMBAmount();
}

function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}

$('#btncreatemb').click(function() {
	var loaNumber = $('#workOrderNumber').val();
	var otp = $('#otp').val();
	if (loaNumber != '' && otp != '') {
		$.ajax({
			method : "GET",
			url : "/egworks/contractorportal/mb/ajax-validateotp?workOrderNo=" + loaNumber + "&otp=" + otp,
			async : true
		}).done(
				function(result) {
					if (!result)
						bootbox.alert($('#errorOTP').val());
					else {
						$.ajax({
							method : "GET",
							url : "/egworks/contractorportal/mb/ajaxworkorder-mbheader?workOrderNo=" + loaNumber,
							async : true
						}).done(
								function(loaNumbers) {
									var flag = false;
									$.each(loaNumbers, function(index, value) {
										if (value == loaNumber)
											flag = true;
									});
									if (!flag)
										bootbox.alert($('#errorLoaNumber').val());
									else {
										$('#isotpvalidated').val('true');
										$('#searchForm').submit();
									}
							});
					}
			});
	}
	else {
		if (loaNumber == '')
			bootbox.alert($('#errorLoaNumber').val());
		else if (otp == '')
			bootbox.alert($('#errorOTP').val());
	}
});

$(document).ready(function() {
	var workOrderNumber = new Bloodhound({
		datumTokenizer : function(datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer : Bloodhound.tokenizers.whitespace,
		remote : {
			url : '/egworks/contractorportal/mb/ajaxworkorder-mbheader?workOrderNo=%QUERY',
			filter : function(data) {
				return $.map(data, function(ct) {
					return {
						name : ct
					};
				});
			}
		}
	});

	workOrderNumber.initialize();
	var workOrderNumber_typeahead = $('#workOrderNumber').typeahead({
		hint : true,
		highlight : true,
		minLength : 3
	}, {
		displayKey : 'name',
		source : workOrderNumber.ttAdapter()
	}).on('typeahead:selected typeahead:autocompleted', function(event, data){            
		$.ajax({
			method : "GET",
			url : "/egworks/contractorportal/mb/ajax-sendotp?workOrderNo=" + data.name,
			async : true
		}).done(
				function(result) {
					if (result == 'true')
						$('#otp-section').show();
					else
						bootbox.alert(result);
			});    
    });
});

function viewContractorMB(id) {
	window.open("/egworks/contractorportal/mb/view/" + id, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

$('#contractormbs').click(function(event) {
	event.preventDefault();
	window.open("/egworks/contractorportal/mb/contractormbs/" + $('#workOrderEstimateId').val(), '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
});

$('#viewEstimatePhotograph').click(function(event) {
	event.preventDefault();
	if($("#lineEstimateRequired").val() == 'true')
		window.open("/egworks/estimatephotograph/view?lineEstimateDetailsId=" + $('#lineEstimateDetailsId').val() + "&mode=contractorPortal", '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	else
		window.open("/egworks/estimatephotograph/view?abstractEstimateId=" + $('#abstractEstimateId').val() + "&mode=contractorPortal", '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
});

$('#uploadEstimatePhotograph').click(function(event) {
	event.preventDefault();
	if($("#lineEstimateRequired").val() == 'true')
		window.open("/egworks/estimatephotograph/newform?lineEstimateDetailsId=" + $('#lineEstimateDetailsId').val() + "&mode=contractorPortal", '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	else
		window.open("/egworks/estimatephotograph/newform?abstractEstimateId=" + $('#abstractEstimateId').val() + "&mode=contractorPortal", '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
});

function validateWorkFlowApprover(name) {
	var flag = false;

	if($('#contractorMBHeader').valid()) {
		var hiddenRowCount = $("#tblsor tbody tr[sorinvisible='true']").length;
		if(hiddenRowCount != 1) {
			$('.quantity').each(function() {
				if (parseFloat($(this).val()) > 0)
					flag = true;
			});
		}
		hiddenRowCount = $("#tblNonSor tbody tr[nonsorinvisible='true']").length;
		if(hiddenRowCount != 1) {
			$('.nonSorQuantity').each(function() {
				if (parseFloat($(this).val()) > 0)
					flag = true;
			});
		}
		
		if (!flag) {
			bootbox.alert($('#errorquantityzero').val());
			return false;
		}
	} else
		return false;

	if(flag) {
		deleteHiddenRows();
		document.forms[0].submit;
		return true;
	} else
		return false;
}

function deleteHiddenRows(){
	hiddenRowCount = $("#tblNonSor tbody tr[nonsorinvisible='true']").length;
	if(hiddenRowCount == 1) {
		var tbl=document.getElementById('tblNonSor');
		tbl.deleteRow(2);
	}
}
