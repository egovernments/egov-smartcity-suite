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
$.validator.setDefaults({
	ignore: ""
});
$subTypeOfWorkId = 0;
$ExceptionalUOMs = "";
$schemeId = "";
$subSchemeId = 0;
$functionId = 0;
$budgetHeadId=0;
$isEstimateDeductionGrid = $('#isEstimateDeductionGrid').val();
var sorMsArray=new Array(200);
var nonSorMsArray=new Array(200);
var headstart="<!--only for validity head start -->";
var headend="<!--only for validity head end -->";
var tailstart="<!--only for validity tail start -->";
var tailend="<!--only for validity tail end -->";


var hint='<a href="#" class="hintanchor" title="@fulldescription@"><i class="fa fa-question-circle" aria-hidden="true"></i></a>';
$(document).ready(function(){
	if($('#lineEstimateRequired').val() == 'true') {
		$('.disablefield').attr('disabled', 'disabled');
	}
	$('.readonlyfield').attr('disabled', 'disabled');
	$subTypeOfWorkId = $('#subTypeOfWorkValue').val();
	$ExceptionalUOMs = $('#exceptionaluoms').val();  
	$schemeId = $('#schemeValue').val();
	$subSchemeId = $('#subSchemeValue').val();
	$functionId = $('#functionId').val();
	$budgetHeadId = $('#budgetHeadValue').val();
	getSubSchemsBySchemeId($schemeId);
	var nameOfWork = $('#nameOfWork').val();
	$('#workName').val(nameOfWork);
	$('#fund').trigger('change');
	$('#parentCategory').trigger('blur');
	var beneficiary = $('#beneficiary').val(); 
	if(beneficiary != undefined) {
		$('#beneficiary').val($('#beneficiary').val().replace(/_C/g, '/C').replace(/_/g, ' '));
	}
	$( "input[name$='percentage']" ).each(function() {
		var value = $(this).val();
		if(value != 0)
			$(this).val(parseFloat(value).toFixed(2));
	});

	if($('#natureOfWork').val() != '') {
		$('.alert-danger').hide();
	} else {
		$('.alert-danger').show();
	}
	$mode = $("#mode").val();
	$('#Cancel').prop('type',"hidden");
	if($mode === 'edit')
		$('#Cancel').prop('type',"submit");
	if($mode == '') {
		if($("#latitude").val() != '' && $("#longitude").val() != '') {
			$("#latlonDiv").show(); 
		} else	$("#latlonDiv").hide(); 
	} else if($mode == 'view' || $mode == '') {
		$(".input-group-addon").hide();
		if($("#latitude").val() != '' && $("#longitude").val() != '') {
			$("#latlonDiv").show();
		} else 
			$("#latlonDiv").hide();
	} else if($mode == 'edit') {
		if($("#latitude").val() != '' && $("#longitude").val() != '') {
			$("#latlonDiv").show();
		} else 
			$("#latlonDiv").hide();
	}

	$locationAppConfig = $('#locationAppConfig').val();
	if($locationAppConfig == 'true') {
		$('#spanlocation').show();
	} else 
		$('#spanlocation').hide();

	$isServiceVATRequired = $('#isServiceVATRequired').val();

	if($isServiceVATRequired == 'true') {
		//For Sor Screen
		$('#serviceVatHeader').removeAttr('hidden');
		$('#vatAmountHeader').removeAttr('hidden');
		$('.serviceTaxPerc').removeAttr('hidden');
		$('.vatAmount').removeAttr('hidden');
		$('.emptytd').removeAttr('hidden');
		$('.serviceVatAmt').removeAttr('hidden');

		//For Non-Sor Screen
		$('#nonSorServiceVatHeader').removeAttr('hidden');
		$('#nonSorVatAmountHeader').removeAttr('hidden');
		$('.nonSorServiceTaxPerc').removeAttr('hidden');
		$('.nonSorVatAmount').removeAttr('hidden');
		$('.emptytd').removeAttr('hidden');
		$('.nonSorServiceVatAmt').removeAttr('hidden');
	}

	$('#natureOfWork').click(function() {
		if($('#natureOfWork').val() != '') {
			$('.alert-danger').hide();
		} else {
			$('.alert-danger').show();
		}
	});

	$('#addnonSorRow').click(function() {
		if(ismsheetOpen())
		{
			bootbox.alert("Measurement Sheet is open Please close it first");
			return ;
		}
		var hiddenRowCount = $("#tblNonSor tbody tr:hidden[id='nonSorRow']").length;
		if(hiddenRowCount == 0) {
			var key = $("#tblNonSor tbody tr:visible[id='nonSorRow']").length;
			addRow('tblNonSor', 'nonSorRow');
			resetIndexes();
			$('#activityid_' + key).val('');
			$('#nonSorId_' + key).val('');
			$('#nonSorDesc_' + key).val('');
			$('#nonSorUom_' + key).val('');
			$('#nonSorRate_' + key).val('');
			$('#nonSorEstimateRate_' + key).val('');
			$('#nonSorQuantity_' + key).val('');
			$('#nonSorQuantity_' + key).removeAttr('readonly');
			$('.nonSorAmount_' + key).html('');
			$('#nonSorServiceTaxPerc_' + key).val('');
			$('.nonSorVatAmt_' + key).html('');
			$('.nonSorTotal_' + key).html('');
			if(document.getElementById('nonSorActivities['+key+'].mstd'))
				document.getElementById('nonSorActivities['+key+'].mstd').innerHTML=""; 
			if(document.getElementById('nonSorActivities['+key+'].mspresent'))
				document.getElementById('nonSorActivities['+key+'].mspresent').value="0"; 

			generateSlno();
		} else {
			var key = 0;
			$('#nonSorDesc_' + key).attr('required', 'required');
			$('#nonSorUom_' + key).attr('required', 'required');
			$('#nonSorEstimateRate_' + key).attr('required', 'required');
			$('#nonSorQuantity_' + key).attr('required', 'required');
			$('#nonSorQuantity_' + key).removeAttr('readonly');
			$('.nonSorEstimateRate').val('');
			$('.nonSorRate').val('');
			$('.nonSorQuantity').val('');
			$('.nonSorServiceTaxPerc').val('');
			$('#nonSorMessage').attr('hidden', 'true');
			$('#nonSorRow').removeAttr('hidden');
			$('#nonSorRow').removeAttr('nonsorinvisible');
			if(document.getElementById('nonSorActivities['+key+'].mstd'))
				document.getElementById('nonSorActivities['+key+'].mstd').innerHTML=""; 
			if(document.getElementById('nonSorActivities['+key+'].mspresent'))
				document.getElementById('nonSorActivities['+key+'].mspresent').value="0"; 
		}
	});

	var sorSearch = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url: '/egworks/abstractestimate/ajaxsor-byschedulecategories?code=',
			replace: function (url, query) {
				var scheduleCategories = $('#scheduleCategory').val();
				var estimateDate = $('#estimateDate').val();
				if(scheduleCategories == null)
					bootbox.alert($('#msgschedulecategory').val());
				if(estimateDate == "" || estimateDate == null)
					bootbox.alert($('#msgestimatedate').val());
				return url + query + '&scheduleCategories=' + scheduleCategories + "&estimateDate=" + estimateDate;
			},
			filter: function (data) {
				return $.map(data, function (ct) {
					return {
						id: ct.id,
						code: ct.code,
						description: ct.description,
						uom: ct.uom.uom,
						uomid: ct.uom.id,
						estimateRate: parseFloat(ct.sorRate).toFixed(2),
						summary: ct.summary,
						categoryCode: ct.scheduleCategory.code,
						displayResult: ct.code+' : '+ct.summary+' : '+ct.scheduleCategory.code 
					};
				});
			}
		}
	});

	sorSearch.initialize();
	var sorSearch_typeahead = $('#sorSearch').typeahead({
		hint : true,
		highlight : true,
		minLength : 2
	}, {
		displayKey : 'displayResult',
		source : sorSearch.ttAdapter()
	}).on('typeahead:selected', function (event, data) {

		if(ismsheetOpen())
		{
			bootbox.alert("Measurement Sheet is open Please close it first");
			return ;
		}
		var flag = false;
		$('.sorhiddenid').each(function() {
			if($(this).val() == data.id) {
				flag = true;
			}
		});
		if(flag) {
			bootbox.alert($('#erroradded').val(), function() {
				$('#sorSearch').val('');
			});
		}
		else {
			var hiddenRowCount = $("#tblsor > tbody > tr:hidden[id='sorRow']").length;
			var key = $("#tblsor > tbody > tr:visible[id='sorRow']").length;
			if(hiddenRowCount == 0) {
				addRow('tblsor', 'sorRow');
				resetIndexes();
				$('#soractivityid_' + key).val('');
				$('#quantity_' + key).val('');
				$('#quantity_' + key).removeAttr('readonly');
				$('.amount_' + key).html('');
				$('#vat_' + key).val('');
				$('.vatAmount_' + key).html('');
				$('.total_' + key).html('');
				if(document.getElementById('sorActivities['+key+'].mstd'))
					document.getElementById('sorActivities['+key+'].mstd').innerHTML=""; 
				if(document.getElementById('sorActivities['+key+'].mspresent'))
					document.getElementById('sorActivities['+key+'].mspresent').value="0"; 
				//generateSorSno();
			} else {
				$('#quantity_0').val('');
				$('#quantity_0').removeAttr('readonly');
				$('#quantity_0').attr('required', 'required');
				$('#vat_0').val('');
				key = 0;
				$('#message').attr('hidden', 'true');;
				$('#sorRow').removeAttr('hidden');
				$('#sorRow').removeAttr('sorinvisible');
				if(document.getElementById('sorActivities[0].mstd'))
					document.getElementById('sorActivities[0].mstd').innerHTML=""; 
				if(document.getElementById('sorActivities[0].mspresent'))
					document.getElementById('sorActivities[0].mspresent').value="0"; 
			}

			$.each(data, function(id, val) {
				if(id == "id")
					$('#' + id + "_" + key).val(val);
				else if(id == "uomid")
					$('#sorUomid_' + key).val(val);
				else if(id == 'description') {
					$('.' + id + "_" + key).html(hint.replace(/@fulldescription@/g, val));
				} else if(id == 'estimateRate') {
					if(val != null) {
						$('.' + id + "_" + key).html(val);
						$('#' + id + "_" + key).val(val);
						//$('#rate_' + key).val(val);
					} else {
						$('.' + id + "_" + key).html(0);
						$('#' + id + "_" + key).val(0);
						//$('#rate_' + key).val(0);
					}
				}else
					$('.' + id + "_" + key).html(val);
			});
			$('#rate_' + key).val(getUnitRate($('.uom_' + key).html(),$('#estimateRate_' + key).val()));

		}
		$('#sorSearch').typeahead('val','');
	});


	var ward = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url: '/egworks/lineestimate/ajax-getward?name=%QUERY',
			filter: function (data) {
				return $.map(data, function (ct) {
					return {
						name: ct.boundaryType.name.toUpperCase() == 'CITY' ? ct.name : ct.boundaryNum + '',
						value: ct.id
					};
				});
			}
		}
	});

	ward.initialize();
	var ward_typeahead = $('#wardInput').typeahead({
		hint : false,
		highlight : false,
		minLength : 1
	}, {
		displayKey : 'name',
		source : ward.ttAdapter(),
	});

	typeaheadWithEventsHandling(ward_typeahead,
	'#ward');

	var $rowId = 0;
	var index = 0;
	var currentState = $('#currentState').val();
	if(currentState == 'Technical Sanctioned') {
		$('#approverDetailHeading').hide();

		removeApprovalMandatoryAttribute();
	}
	
	calculateEstimateAmountTotal();
	calculateVatAmountTotal();
	total();

	calculateNonSorEstimateAmountTotal();
	calculateNonSorVatAmountTotal();
	nonSorTotal();

	generateSorSno();
	generateSlno();


	resetAddedOverheads();
	calculateOverheadTotalAmount();
	$('#isOverheadValuesLoading').val('false');
	
	if($isEstimateDeductionGrid == 'true') {
		deductionAccountCodeAndHead_initialize();
		calculateDeductionTotalAmount();
	}
	
	$('.slide-copy-estimate-menu').click(function(){
		$(this).parent().find('.copy-estimate-slide').slideToggle();
		if($(this).parent().find('#toggle-his-icon').hasClass('fa fa-angle-down'))
		{
			$(this).parent().find('#toggle-his-icon').removeClass('fa fa-angle-down').addClass('fa fa-angle-up');
			//$('#see-more-link').hide();
			}else{
			$(this).parent().find('#toggle-his-icon').removeClass('fa fa-angle-up').addClass('fa fa-angle-down');
			//$('#see-more-link').show();
		}
	});
	replaceWorkCategoryChar();
	replaceBeneficiaryChar();
	if($('#budgetControlType').val() != 'NONE')
		getFunctionsByFundAndDepartment();
	
	var defaultDepartmentId = $("#defaultDepartmentId").val();
	if(defaultDepartmentId != "") {
		$("#approvalDepartment").val(defaultDepartmentId);
		$('#approvalDepartment').trigger('change');
	}
	
	if ($('#spillOverFlag').val() == 'true')
		removeApprovalMandatoryAttribute();

});

$overheadRowCount = 0;
$('#addOverheadRow').click(function() { 
	addRow('overheadTable','overheadRow');
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

var addedOverheads = new Array();

function deleteOverheadRow(obj) {

	var rIndex = getRow(obj).rowIndex;
	var rowcount=jQuery("#overheadTable tbody tr").length;
	if(rowcount<=1) {
		bootbox.alert("This row can not be deleted");
		return false;
	} else {
		//calculating Overhead TotalAmount
		var total = $("#overheadTotalAmount").html();
		if(total==null || total=="")
			total = 0;
		var amount = document.getElementById('tempOverheadValues['+ (rIndex-1) + '].amount').value;
		if(amount==null || amount=="")
			amount = 0;
		total = eval(total) - eval(amount);
		$("#overheadTotalAmount").html(total);

		deleteRow("overheadTable",obj);

		resetAddedOverheads();
		calculateOverheadTotalAmount();
		return true;
	}

}

function recalculateOverheads(){
	var resultLength = jQuery('#overheadTable tr').length-1;
	var index;
	var workValue = $('#workValue').val();
	for (var i = 0; i < resultLength; i++) {
		index = i;
		var percentage = document.getElementById('tempOverheadValues['
				+ index + '].percentage').value;
		var amount = document.getElementById('tempOverheadValues['
				+ index + '].amount');
		if(percentage!="")
			amount.value = ((workValue*percentage)/100).toFixed(2);
		calculateOverheadTotalAmount();
	}
}

function resetAddedOverheads(){
	addedOverheads = new Array();
	var resultLength = jQuery('#overheadTable tr').length-1;
	var index;
	for (var i = 0; i < resultLength; i++) {
		index = i;
		var overheadvalue = document.getElementById('tempOverheadValues['
				+ index + '].name').value;
		if(overheadvalue!="")
			addedOverheads.push(overheadvalue);
		else{
			document.getElementById('tempOverheadValues['+ index + '].percentage').value = "";
			document.getElementById('tempOverheadValues['+ index + '].amount').value = 0;
			calculateOverheadTotalAmount();
		}
	}
}

function getPercentageOrLumpsumByOverhead(overhead) {
	if(overhead.value==""){
		resetAddedOverheads();
	}else
		if(addedOverheads.indexOf(overhead.value) == -1) {
			if($('#isOverheadValuesLoading').val() == 'false')
				resetAddedOverheads();
			var objName = overhead.name;
			var index =objName.substring(objName.indexOf("[")+1,objName.indexOf("]")); 
			var workValue = $('#workValue').val();
			if (overhead.value != '') {
				$.ajax({
					url : '/egworks/abstractestimate/getpercentageorlumpsumbyoverheadid',
					type : "get",
					data : {
						overheadId : overhead.value
					},
					success : function(data, textStatus, jqXHR) {
						document.getElementById('tempOverheadValues['+ index + '].overhead.id').value = data.overhead.id;
						if(data.percentage>0){
							document.getElementById('tempOverheadValues['+ index + '].percentage').value = data.percentage;
							document.getElementById('tempOverheadValues['+ index + '].amount').value = ((workValue*data.percentage)/100).toFixed(2);
						}else{
							document.getElementById('tempOverheadValues['+ index + '].percentage').value = "";
							document.getElementById('tempOverheadValues['+ index + '].amount').value = data.lumpsumAmount;
						}
						calculateOverheadTotalAmount();
					},
					error : function(jqXHR, textStatus, errorThrown) {
						bootbox.alert("Error while get Percentage or Lumpsum By Overhead ");
					}
				});
			}else{
				document.getElementById('tempOverheadValues['+ index + '].percentage').value = "";
				document.getElementById('tempOverheadValues['+ index + '].amount').value = 0;
				calculateOverheadTotalAmount();
			}
		}else{
			var index =overhead.name.substring(overhead.name.indexOf("[")+1,overhead.name.indexOf("]")); 
			document.getElementById('tempOverheadValues['+ index + '].percentage').value = "";
			document.getElementById('tempOverheadValues['+ index + '].amount').value = 0;
			overhead.value= "";
			calculateOverheadTotalAmount();
			bootbox.alert("The overhead is already added");
			resetAddedOverheads();
		}

}

function calculateOverheadTotalAmount(){
	var resultLength = jQuery('#overheadTable tr').length-1;
	var index;
	var total = 0;
	for (var i = 0; i < resultLength; i++) {
		index = i;
		var amount = document.getElementById('tempOverheadValues['
				+ index + '].amount').value;
		if(amount==null || amount=="")
			amount = 0;
		total = eval(total) + eval(amount);
	}

	$("#overheadTotalAmount").html(parseFloat(total).toFixed(2));
	calculateEstimateValue();
}

function addMultiyearEstimate() {
	var tbl = document.getElementById('multiYeaeEstimateTbl');
	var rowO = tbl.rows.length;
	if(document.getElementById('yearEstimateRow') != null)
	{
		//get Next Row Index to Generate
		var nextIdx = tbl.rows.length;
		var sno = 1;
		sno = nextIdx + 1;
		//validate status variable for exiting function
		var isValid=1;//for default have success value 0  

		//validate existing rows in table
		jQuery("#tblyearestimate tbody tr").find('input, select').each(function(){
			if((jQuery(this).data('optional') === 0) && (!jQuery(this).val()))
			{
				//console.log('calling :)');
				jQuery(this).focus();
				bootbox.alert(jQuery(this).data('errormsg'));
				isValid=0;//set validation failure
				return false;
			}
		});

		if (isValid === 0) {
			return false;
		}
		// Generate all textboxes Id and name with new index
		jQuery("#yearEstimateRow").clone().find("input, select ,span,textarea").each(function() {

			if($(this).is('span')) {
				jQuery(this).text(sno);
				sno++;
			} else {
				jQuery(this).attr({
					'id': function(_, id) { 
						return id.replace('[0]', '['+ nextIdx +']'); 
					},
					'name': function(_, name) { 
						return name.replace('[0]', '['+ nextIdx +']'); 
					},
					'data-idx' : function(_,dataIdx)
					{
						return nextIdx;
					}

				}).val(''); 
			}
			if($(this).is('input')){
				$(this).val(0);
			}

		}).end().appendTo("#multiYeaeEstimateTbl");

	}

}

function deleteMultiYearEstimate(obj) {

	rIndex = getRow(obj).rowIndex - 1;
	var tbl=document.getElementById('multiYeaeEstimateTbl');
	var rowo=tbl.rows.length;
	if(rowo<=1)
	{
		bootbox.alert("This row cannot be deleted");
		return false;
	}
	else
	{	
		tbl.deleteRow(rIndex);	

		//starting index for table fields
		var idx=0;

		//regenerate index existing inputs in table row
		$("#tblyearestimate tbody tr").each(function() {
			$(this).find("input, select, span,textarea").each(function() {
				if(!$(this).is('span')) {
					$(this).attr({
						'id': function(_, id) {  
							return id.replace(/\[.\]/g, '['+ idx +']'); 
						},
						'name': function(_, name) {
							return name.replace(/\[.\]/g, '['+ idx +']'); 
						},
					});
				}
				else{
					$(this).text((idx+1));
				}
			});

			idx++;

		});

		calculatePercentage('#estimateTotal', '.inputYearEstimatePercentage');

		return true;
	}	

}

function generateSorSno()
{
	var idx=1;
	$(".spansorslno").each(function(){
		$(this).text(idx);
		idx++;
	});
}

function deleteSor(obj) {
	var rIndex = getRow(obj).rowIndex;
	var id = $(getRow(obj)).children('td:first').children('input:first').val();
	//To get all the deleted rows id
	var aIndex = rIndex - 1;
	if(!$("#removedActivityIds").val()==""){
		$("#removedActivityIds").val($("#removedActivityIds").val()+",");
	}
	$("#removedActivityIds").val($("#removedActivityIds").val()+id);

	var tbl=document.getElementById('tblsor');	
	var rowcount=$("#tblsor > tbody >tr").length;
	if(rowcount==2) {
		var rowId = $(obj).attr('class').split('_').pop();
		$('#soractivityid_' + rowId).val('');
		$('.sorhiddenid').val('');
		$('#quantity_' + rowId).val('');
		$('#quantity_' + rowId).removeAttr('required');
		$('.amount_' + rowId).html('');
		$('#vat_' + rowId).val('');
		$('.vatAmount_' + rowId).html('');
		$('.total_' + rowId).html('');
		$('#sorRow').attr('hidden', 'true');
		$('#sorRow').attr('sorinvisible', 'true');
		$('#message').removeAttr('hidden');
	} else {
		deleteRow('tblsor',obj);
	}
	resetIndexes();
	//starting index for table fields
	generateSorSno();
	calculateEstimateAmountTotal();
	calculateVatAmountTotal();
	total();
	getDeductionAmountByPercentage();
	return true;
}

function calculateEstimateAmount(currentObj) {
	rowcount = $(currentObj).attr('id').split('_').pop();
	var rate = parseFloat($('#rate_' + rowcount).val().trim());
	var amount = parseFloat($(currentObj).val() * rate).toFixed(2);
	var vatAmount = parseFloat(($('#vat_' + rowcount).val() * amount) / 100).toFixed(2);
	$('.amount_' + rowcount).html(amount);
	$('.vatAmount_' + rowcount).html(vatAmount);
	$('.total_' + rowcount).html(parseFloat(parseFloat(amount) + parseFloat(vatAmount)).toFixed(2));
	calculateEstimateAmountTotal();
	calculateVatAmountTotal();
	total();
	getDeductionAmountByPercentage();
}

function calculateVatAmount(currentObj) {
	rowcount = $(currentObj).attr('id').split('_').pop();
	var estimatedAmount = parseFloat($('.amount_' + rowcount).html().trim());
	var vatAmount = parseFloat(($(currentObj).val() * estimatedAmount) / 100).toFixed(2);
	$('.vatAmount_' + rowcount).html(vatAmount);
	$('.total_' + rowcount).html(parseFloat(parseFloat(estimatedAmount) + parseFloat(vatAmount)).toFixed(2));
	calculateVatAmountTotal();
	total();
}

function calculateEstimateAmountTotal() {
	var total = 0;
	$('.amount').each(function() {
		if($(this).html().trim() != "")
			total = parseFloat(parseFloat(total) + parseFloat($(this).html().replace(',', ''))).toFixed(2);
	});
	$('#sorEstimateTotal').html(total);
}

function calculateVatAmountTotal() {
	var total = 0;
	$('.vatAmt').each(function() {
		if($(this).html().trim() != "")
			total = parseFloat(parseFloat(total) + parseFloat($(this).html().replace(',', ''))).toFixed(2);
	});
	$('#serviceVatAmtTotal').html(total);
}

function total() {
	var total = 0.0;
	$('.total').each(function() {
		if($(this).html().trim() != "")
			total = parseFloat(parseFloat(total) + parseFloat($(this).html().replace(',', ''))).toFixed(2);
	});
	$('#sorTotal').html(total);
	calculateEstimateValue();
}

function generateSlno()
{
	var idx=1;
	$(".spannonsorslno").each(function(){
		$(this).text(idx);
		idx++;
	});
}

function deleteNonSor(obj) {
	var rIndex = getRow(obj).rowIndex;

	var id = $(getRow(obj)).children('td:first').children('input:first').val();
	//To get all the deleted rows id
	var aIndex = rIndex - 1;
	if(!$("#removedActivityIds").val()==""){
		$("#removedActivityIds").val($("#removedActivityIds").val()+",");
	}
	$("#removedActivityIds").val($("#removedActivityIds").val()+id);

	var rowId = $(obj).attr('class').split('_').pop();
	var rowcount=$("#tblNonSor tbody tr").length;

	if(rowcount==2) {
		$('#activityid_' + rowId).val('');
		$('#nonSorId_' + rowId).val('');
		$('#nonSorId_' + rowId).val('');
		$('#nonSorDesc_' + rowId).val('');
		$('#nonSorUom_' + rowId).val('');
		$('#nonSorEstimateRate_' + rowId).val('');
		$('#nonSorRate_' + rowId).val('');
		$('#nonSorQuantity_' + rowId).val('');
		$('#nonSorQuantity_' + rowId).removeAttr('required');
		$('.nonSorAmount_' + rowId).html('');
		$('#nonSorServiceTaxPerc_' + rowId).val('');
		$('.nonSorVatAmount_' + rowId).html('');
		$('.nonSorTotal_' + rowId).html('');
		$('#nonSorRow').attr('hidden', 'true');
		$('#nonSorRow').attr('nonsorinvisible', 'true');
		$('#nonSorMessage').removeAttr('hidden');
	} else {
		deleteRow('tblNonSor',obj);
	}
	resetIndexes();
	//starting index for table fields
	generateSlno();

	calculateNonSorEstimateAmountTotal();
	calculateNonSorVatAmountTotal();
	nonSorTotal();
	getDeductionAmountByPercentage();
	return true;
}

function resetIndexes() {
	var idx = 0;

	//regenerate index existing inputs in table row
	$(".sorRow").each(function() {
		$(this).find("input,button, select,textarea,td,tbody,tr,table, errors, span, input:hidden").each(function() {


			if (!$(this).is('span')) {
				$(this).attr({
					'name' : function(_, name) {
						//console.log(name);
						if(name)
							{
							name= name.replace(/sorActivities\[.\]/g, "sorActivities["+idx+"]");
							return name.replace(/_\d+/,"_"+idx);
							}
					},
					'id' : function(_, id) {
						//console.log(id);
						if(id)
							{
							id= id.replace(/sorActivities\[.\]/g, "sorActivities["+idx+"]");
							return id.replace(/_\d+/,"_"+idx);
							}
							
					},
					'data-idx' : function(_, dataIdx) {
						return idx;
					}
				});
			} else {
				$(this).attr({
					'class' : function(_, name) {
						//console.log(name);
						if(name)
							{
							name= name.replace(/sorActivities\[.\]/g, "sorActivities["+idx+"]");
						return	name=name.replace(/_\d+/,"_"+idx);
							
							}
					},
					'id' : function(_, id) {
						if(id)
						{
							//console.log(id);
							id= id.replace(/sorActivities\[.\]/g, "sorActivities["+idx+"]");
							return id.replace(/_\d+/,"_"+idx);
						}
					}
				});
			}
		});
		idx++;
	});

	idx = 0;

	$(".nonSorRow").each(function() {
		$(this).find("input,button, select,textarea,td,tbody,tr,table, errors, span, input:hidden").each(function() {

			if (!$(this).is('span')) {
				$(this).attr({
					'name' : function(_, name) {
						if(name)
							return name.replace(/nonSorActivities\[.\]/g, "nonSorActivities["+idx+"]");
					},
					'id' : function(_, id) {
						if(id)
							return id.replace(/nonSorActivities\[.\]/g, "nonSorActivities["+idx+"]");
					},
					'data-idx' : function(_, dataIdx) {
						return idx;
					}
				});
			} else {
				$(this).attr({
					'class' : function(_, name) {
						if(name)
							return name.replace(/nonSorActivities\[.\]/g, "nonSorActivities["+idx+"]");
					},
					'id' : function(_, id) {
						if(id)
							{	
							id= id.replace(/nonSorActivities\[.\]/g, "nonSorActivities["+idx+"]");
							return id.replace(/_\d+/,"_"+idx);
								}
					}
				});
			}
		});
		idx++;
	});
}

function calculateNonSorEstimateAmount(currentObj) {
	var rowcount = $(currentObj).attr('id').split('_').pop();
	var description = $('#nonSorDesc_' + rowcount).val();
	var uom = $('#nonSorUom_' + rowcount).val();
	var flag = false;
	if(description == '') {
		bootbox.alert($('#errordescription').val());
		$('#nonSorDesc_' + rowcount).val('');
		flag = true;
	}
	if(!flag && uom == '') {
		bootbox.alert($('#erroruom').val());
		$('#nonSorUom_' + rowcount).val('');
		$('.nonSorAmount_' + rowcount).html('');
		$('.nonSorVatAmount_' + rowcount).html('');
		$('.nonSorTotal_' + rowcount).html('');
		calculateNonSorEstimateAmountTotal();
		calculateNonSorVatAmountTotal();
		nonSorTotal();
		flag = true;
	}
	if(!flag) {
		var estimateRate = $('#nonSorEstimateRate_' + rowcount).val();
		var unitRate;
		if(estimateRate == "")
			unitRate = 0.0;
		else{
			unitRate = getUnitRate($('#nonSorUom_' + rowcount).find(":selected").text().split(" -- ")[1],estimateRate);
			$('#nonSorRate_' + rowcount).val(unitRate);
		}
		var quantity = $('#nonSorQuantity_' + rowcount).val();
		if(quantity == "")
			quantity = 0.0;
		var amount = parseFloat(parseFloat(quantity) * parseFloat(unitRate)).toFixed(2);
		var vatAmount = parseFloat(($('#nonSorServiceTaxPerc_' + rowcount).val() * amount) / 100).toFixed(2);
		$('.nonSorAmount_' + rowcount).html(amount);
		$('.nonSorVatAmount_' + rowcount).html(vatAmount);
		$('.nonSorTotal_' + rowcount).html(parseFloat(parseFloat(amount) + parseFloat(vatAmount)).toFixed(2));
		calculateNonSorEstimateAmountTotal();
		calculateNonSorVatAmountTotal();
		nonSorTotal();
		getDeductionAmountByPercentage();
	}
}

function calculateNonSorVatAmount(currentObj) {
	var rowcount = $(currentObj).attr('id').split('_').pop();
	var estimatedAmount = $('.nonSorAmount_' + rowcount).html();
	if(estimatedAmount == "")
		estimatedAmount = 0.0;
	var serviceTaxPerc = $('#nonSorServiceTaxPerc_' + rowcount).val();
	if(serviceTaxPerc == "")
		serviceTaxPerc = 0.0;
	var vatAmount = parseFloat((serviceTaxPerc * estimatedAmount) / 100).toFixed(2);
	$('.nonSorVatAmount_' + rowcount).html(vatAmount);
	$('.nonSorTotal_' + rowcount).html(parseFloat(parseFloat(estimatedAmount) + parseFloat(vatAmount)).toFixed(2));
	calculateNonSorVatAmountTotal();
	nonSorTotal();
}

function calculateNonSorEstimateAmountTotal() {
	var total = 0;
	$('.nonsoramount').each(function() {
		if($(this).html().trim() != "")
			total = parseFloat(parseFloat(total) + parseFloat($(this).html().replace(',', ''))).toFixed(2);
	});
	$('#nonSorEstimateTotal').html(total);
}

function calculateNonSorVatAmountTotal() {
	var total = 0;
	$('.nonSorVatAmt').each(function() {
		if($(this).html().trim() != "")
			total = parseFloat(parseFloat(total) + parseFloat($(this).html().replace(',', ''))).toFixed(2);
	});
	$('#nonSorServiceVatAmtTotal').html(total);
}

function nonSorTotal() {
	var total = 0.0;
	$('.nonSorTotal').each(function() {
		if($(this).html().trim() != "")
			total = parseFloat(parseFloat(total) + parseFloat($(this).html().replace(',', ''))).toFixed(2);
	});
	$('#nonSorTotal').html(total);
	calculateEstimateValue();
}

function calculateEstimateValue() {
	var sorTotal = $('#sorTotal').html();
	var nonSorTotal = $('#nonSorTotal').html();
	var overheadTotal = $('#overheadTotalAmount').html();
	if(sorTotal == '')
		sorTotal = 0.0;
	if(nonSorTotal == '')
		nonSorTotal = 0.0;
	var workValue = parseFloat(parseFloat(sorTotal) + parseFloat(nonSorTotal));
	var estimateValue = parseFloat(parseFloat(workValue) + parseFloat(overheadTotal)).toFixed(2);
	if($isEstimateDeductionGrid == 'true') {
		var deductionTotal = $('#deductionTotalAmount').html();
		estimateValue = parseFloat(parseFloat(workValue) + parseFloat(overheadTotal) + parseFloat(deductionTotal)).toFixed(2);
	}
	$('#estimateValue').val(estimateValue);
	$('#workValue').val(workValue);
	$('#estimateValueTotal').html(estimateValue);
	$('#workValueTotal').html(workValue);
}


function validateInput(object) {
	var valid = /^[1-9](\d{0,9})(\.\d{0,2})?$/.test($(object).val()),
	val = $(object).val();

	if(!valid){
		//console.log("Invalid input!");
		$(object).val(val.substring(0, val.length - 1));
	}
}

function validateQuantityInput(object) {
	var valid = /^[0-9](\d{0,9})(\.\d{0,4})?$/.test($(object).val()),
	val = $(object).val();

	if(!valid){
		//console.log("Invalid input!");
		$(object).val(val.substring(0, val.length - 1));
	}
}


function limitCharatersBy10_4(object)
{
	var valid = /^[0-9](\d{0,9})(\.\d{0,4})?$/.test($(object).val()),
	val = $(object).val();

	if(!valid){
		//console.log("Invalid input!");
		$(object).val(val.substring(0, val.length - 1));
	}	

}

function limitCharatersBy3_2(object)
{
	var valid = /^[0-9](\d{0,2})(\.\d{0,2})?$/.test($(object).val()),
	val = $(object).val();

	if(!valid){
		//console.log("Invalid input!");
		$(object).val(val.substring(0, val.length - 1));
	}	

}

 

function validateOverheads(){
	var resultLength = jQuery('#overheadTable tr').length-1;
	var index;
	for (var i = 0; i < resultLength; i++) {
		index = i;
		var overheadvalue = document.getElementById('tempOverheadValues['
				+ index + '].name').value;
		var amount = document.getElementById('tempOverheadValues['
				+ index + '].amount').value;
		if(overheadvalue=="" && resultLength != 1){
			document.getElementById('tempOverheadValues['+ index + '].name').focus();
			bootbox.alert("Select overhead name for overheads line:  "+(index+1) + " in Schedule B Tab");
			return false;
		}
		if(resultLength != 1 && (amount=="" || amount<=0)){
			document.getElementById('tempOverheadValues['+ index + '].amount').focus();
			bootbox.alert("Amount is requried for overheads line:  "+(index+1) + " in Schedule B Tab");
			return false;
		}
	}
	return true;
}

$('#parentCategory').blur(function(){
	if ($('#parentCategory').val() === '') {
		$('#category').empty();
		$('#category').append($('<option>').text('Select from below').attr('value', ''));
		return;
	} else {
		$.ajax({
			type: "GET",
			url: "/egworks/lineestimate/getsubtypeofwork",
			cache: true,
			dataType: "json",
			data:{'id' : $('#parentCategory').val()}
		}).done(function(value) {
			//console.log(value);
			$('#category').empty();
			$('#category').append($("<option value=''>Select from below</option>"));
			$.each(value, function(index, val) {
				var selected="";
				if($subTypeOfWorkId)
				{
					if($subTypeOfWorkId==val.id)
					{
						selected="selected";
					}
				}
				$('#category').append($('<option '+ selected +'>').text(val.name).attr('value', val.id));
			});
		});
	}
});

var templateCode = new Bloodhound({
	datumTokenizer: function (datum) {
		return Bloodhound.tokenizers.whitespace(datum.value);
	},
	queryTokenizer: Bloodhound.tokenizers.whitespace,
	remote: {
		url: '/egworks/abstractestimate/ajaxestimatetemplatebycode?code=%QUERY', 
		filter: function (data) {
			var estimateDate = $('#estimateDate').val();
			if(estimateDate == "" || estimateDate == null){
				bootbox.alert($('#msgestimatedate').val());
				return false;
			}
			return $.map(data, function (ct) {
				return {
					code: ct.code,
					id:ct.id
				};
			});
		}
	}
});

templateCode.initialize();
var templateCode_typeahead = $('#templateCode').typeahead({
	hint : true,
	highlight : true,
	minLength : 3
}, {
	displayKey : 'code',
	source : templateCode.ttAdapter()
}).on('typeahead:selected typeahead:autocompleted', function(event, data){            
	$("#templateId").val(data.id);   
});
$('#templateCode').blur(function() {

});

$('#searchTemplate').click(function() {
	
	var templateCode = $('#templateCode').val();
	var typeOfWork =$('#parentCategory').val();
	var subTypeOfWork = $('#category').val();
	if(templateCode=="")
		window.open("/egworks/estimatetemplate/searchestimatetemplateform?typeOfWork="+typeOfWork+"&subTypeOfWork="+subTypeOfWork,'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	else{
		var templateId = $('#templateId').val();
		var sorHiddenRowCount = $("#tblsor tbody tr:hidden[id='sorRow']").length;
		var nonSorHiddenRowCount = $("#tblNonSor tbody tr:hidden[id='nonSorRow']").length;
		if(templateId!="" && (sorHiddenRowCount !=1 || nonSorHiddenRowCount!=1)){ 
			var ans=confirm($("#estimateTemplateConfirmMsg").val());	
			if(ans) {
				clearActivities();
				getActivitiesForTemplate(templateId);
				$('#templateCode').val('');
			}
			else {
				return false;		
			}
		}else{
			getActivitiesForTemplate(templateId);
			$('#templateCode').val('');
		}
	}

});


function resetTemplate(id){
	var estimateDate = $('#estimateDate').val();
	if(estimateDate == "" || estimateDate == null){
		bootbox.alert($('#msgestimatedate').val());
		return false;
	}
	var sorHiddenRowCount = $("#tblsor tbody tr:hidden[id='sorRow']").length;
	var nonSorHiddenRowCount = $("#tblNonSor tbody tr:hidden[id='nonSorRow']").length;
	if(id!="" && (sorHiddenRowCount !=1 || nonSorHiddenRowCount!=1)){ 
		var ans=confirm($("#estimateTemplateConfirmMsg").val());	
		if(ans) {
			clearActivities();
			getActivitiesForTemplate(id);
		}
		else {
			return false;		
		}
	}
	else{
		getActivitiesForTemplate(id);
	}
}

function clearActivities(){
	$('.soractivityid').each(function() {
		if($(this).val() != "") {
			var id = $(this).val();
			if(!$("#removedActivityIds").val()==""){
				$("#removedActivityIds").val($("#removedActivityIds").val()+",");
			}
			$("#removedActivityIds").val($("#removedActivityIds").val()+id);
		}
	});
	$('.activityid').each(function() {
		if($(this).val() != "") {
			var id = $(this).val();
			if(!$("#removedActivityIds").val()==""){
				$("#removedActivityIds").val($("#removedActivityIds").val()+",");
			}
			$("#removedActivityIds").val($("#removedActivityIds").val()+id);
		}
	});
	var hiddenRowCount = $("#tblsor tbody tr[sorinvisible='true']").length;
	if(hiddenRowCount == 0) {
		var sorrowcount = jQuery("#tblsor > tbody > tr").length;
		var sortbl=document.getElementById('tblsor');
		for(rowcount=2;rowcount<=sorrowcount;rowcount++){
			resetIndexes();
			if(rowcount==2) {
				$('.soractivityid').val('');
				$('.sorhiddenid').val('');
				$('#quantity_0').val('');
				$('#categoryCode_0').val('');
				$('#quantity_0').removeAttr('readonly');
				$('.amount_0').html('');
				$('#vat_0').val('');
				$('.vatAmount_0').html('');
				$('.total_0').html('');
				$('.code_0').html('');
				$('.summary_0').html('');
				$('.uom_0').html('');
				$('.rate_0').html('');
				$('.description_0').html('');
				$('.mstd').html('');
				$('.mspresent').val('0');
				$('#sorRow').prop("hidden",true);
				$('.msopen').val('0');
				$('#sorRow').attr('sorinvisible', 'true');
				$('#message').removeAttr("hidden");
			} else {
				sortbl.deleteRow(3);
			}
			calculateEstimateAmountTotal();
			calculateVatAmountTotal();
			total();
		}
	}

	var nonSorHiddenRowCount = $("#tblNonSor tbody tr[nonsorinvisible='true']").length;
	if(nonSorHiddenRowCount == 0) {
		var nonsorrowcount = jQuery("#tblNonSor > tbody > tr").length;
		var nonsortbl=document.getElementById('tblNonSor');
		for(rowcount=2;rowcount<=nonsorrowcount;rowcount++){
			if(rowcount==2) {
				$('.activityid').val('');
				$('.nonSorId').val('');
				$('.nonSorDesc').val('');
				$('.nonSorUom').val('');
				$('.nonSorEstimateRate').val('');
				$('.nonSorRate').val('');
				$('.nonSorQuantity').val('');
				$('.nonSorQuantity').removeAttr('readonly');
				$('.nonsoramount').html('');
				$('.nonSorServiceTaxPerc').val('');
				$('.nonSorVatAmt').html('');
				$('.nonSorTotal').html('');
				$('.mstd').html('');
				$('.msopen').val('0');
				$('.mspresent').val('0');
				$('#nonSorRow').prop("hidden",true);
				$('#nonSorRow').attr('nonsorinvisible', 'true');
				$('#nonSorMessage').removeAttr("hidden");
			} else {
				nonsortbl.deleteRow(3);
			}
			calculateNonSorEstimateAmountTotal();
			calculateNonSorVatAmountTotal();
			nonSorTotal();
		}
		resetIndexes();
	}
}

function getActivitiesForTemplate(id){
	var estimateDate = $('#estimateDate').val();
	var nonSorCheck  = false;
	$.ajax({
		url: "/egworks/abstractestimate/ajaxgetestimatetemplatebyid?id="+ id + "&estimateDate="+estimateDate, 
		type: "GET",
		dataType: "json",
		success: function (data) {
			populateActivities(data, nonSorCheck);
		}, 
		error: function (response) {
			//console.log("failed");
		}
	});

	resetIndexes();
	calculateEstimateAmountTotal();
	calculateVatAmountTotal();
	total();
	calculateNonSorEstimateAmountTotal();
	calculateNonSorVatAmountTotal();
	nonSorTotal();
}

function populateActivities(data, nonSorCheck){
	var responseObj = JSON.parse(data);
	var sorCount = 0;
	var nonSorCount = 0;
	$.each(responseObj, function(index,value) {
		if(sorCount==0 && responseObj[index].scheduleId != null){
			$('#message').prop("hidden",true);
			$('#sorRow').removeAttr("hidden");
			$('#sorRow').removeAttr('sorinvisible');
			if (responseObj[index].ms != "" && responseObj[index].ms != undefined) {
				var newrow= $('#msheaderrowtemplate').html();
				
				newrow=  newrow.replace(/msrowtemplate/g, 'msrowsorActivities[0]');
				newrow=  newrow.replace(/templatesorActivities\[0\]/g, 'sorActivities[0]');
				newrow = newrow.replace(/templatesorActivities_0/g, 'sorActivities_0');
				newrow = newrow.replace(/templatemssubmit_0/g, 'mssubmit_0');
				document.getElementById('sorActivities[0].mstd').innerHTML=newrow;
				$(responseObj[index].ms).each(function(index, measurementSheet){
					if (index > 0) {
						var msrowname= "sorActivities[0].mstable";
						var msrownameid=msrowname.split(".")[0];
						var rep='measurementSheetList\['+ index +'\]';

						var $newrow = "<tr>"+$('#msrowtemplate').html()+"</tr>";
						$newrow = $newrow.replace(/templatesorActivities\[0\]/g,msrownameid);
						$newrow = $newrow.replace(/measurementSheetList\[0\]/g,rep);
						$newrow = $newrow.replace(/templatesorActivities_0/g, 'sorActivities_0');
						$newrow = $newrow.replace(/measurementSheetList_0_id/g, 'measurementSheetList_' + index + '_id');
						$newrow = $newrow.replace(/measurementSheetList_0_parent/g, 'measurementSheetList_' + index + '_parent');
						$newrow = $newrow.replace('value="1"','value="'+(index+1)+'"');
						$('#sorActivities\\[0\\]\\.mssubmit').closest('tr').before($newrow);
						patternvalidation();
					}
					
					$.each(measurementSheet, function(obj, value){
						if(obj == "identifier") {
							if(value == 'A')
								document.getElementById('sorActivities[0].measurementSheetList[' + index + '].' + obj).options[0].setAttribute('selected', 'selected');
							else
								document.getElementById('sorActivities[0].measurementSheetList[' + index + '].' + obj).options[1].setAttribute('selected', 'selected');
						} else if(obj == 'remarks') {
							$('#sorActivities\\[0\\]\\.measurementSheetList\\[' + index + '\\]\\.' + obj).html(value);
						} else {
							$('#sorActivities\\[0\\]\\.measurementSheetList\\[' + index + '\\]\\.' + obj).attr('value', value);
							if(obj != "slNo" && obj != "remarks"){
								document.getElementById('sorActivities[0].measurementSheetList[' + index + '].' + obj).setAttribute('data-' + obj, value);
							}
						}
					});
					
				});
				var total = 0;
				var identifier;
				$(responseObj[index].ms).each(function(index, measurementSheet){
					if(document.getElementById('sorActivities[0].measurementSheetList[' + index + '].identifier').value=='A')
						identifier = "A";
					else
						identifier = "D";
					var quantity = document.getElementById('sorActivities[0].measurementSheetList[' + index + '].quantity').value;
					if(identifier == "A")
						total = total + Number(quantity);
					else
						total = total - Number(quantity);
				});
				$('#sorActivities\\[0\\]\\.msnet').html(total);
			}
		} else if(nonSorCount==0 && responseObj[index].scheduleId == null){
			$('#nonSorMessage').prop("hidden",true);
			$('#nonSorRow').removeAttr("hidden");
			$('#nonSorRow').removeAttr('nonsorinvisible');
			if (responseObj[index].ms != "" && responseObj[index].ms != undefined) {
				var newrow= $('#msheaderrowtemplate').html();
				
				newrow=  newrow.replace(/msrowtemplate/g, 'msrownonSorActivities[0]');
				newrow=  newrow.replace(/templatesorActivities\[0\]/g, 'nonSorActivities[0]');
				newrow = newrow.replace(/templatesorActivities_0/g, 'nonSorActivities_0');
				newrow = newrow.replace(/templatemssubmit_0/g, 'mssubmit_0');
				document.getElementById('nonSorActivities[0].mstd').innerHTML=newrow;
				$(responseObj[index].ms).each(function(index, measurementSheet){
					if (index > 0) {
						var msrowname= "nonSorActivities[0].mstable";
						var msrownameid=msrowname.split(".")[0];
						var rep='measurementSheetList\['+ index +'\]';

						var $newrow = "<tr>"+$('#msrowtemplate').html()+"</tr>";
						$newrow = $newrow.replace(/templatesorActivities\[0\]/g,msrownameid);
						$newrow = $newrow.replace(/measurementSheetList\[0\]/g,rep);
						$newrow = $newrow.replace(/templatesorActivities_0/g, 'nonSorActivities_0');
						$newrow = $newrow.replace(/measurementSheetList_0_id/g, 'measurementSheetList_' + index + '_id');
						$newrow = $newrow.replace(/measurementSheetList_0_parent/g, 'measurementSheetList_' + index + '_parent');
						$newrow = $newrow.replace('value="1"','value="'+(index+1)+'"');
						$('#nonSorActivities\\[0\\]\\.mssubmit').closest('tr').before($newrow);
						patternvalidation();
					}
					
					$.each(measurementSheet, function(obj, value){
						if(obj == "identifier") {
							if(value == 'A')
								document.getElementById('nonSorActivities[0].measurementSheetList[' + index + '].' + obj).options[0].setAttribute('selected', 'selected');
							else
								document.getElementById('nonSorActivities[0].measurementSheetList[' + index + '].' + obj).options[1].setAttribute('selected', 'selected');
						} else if(obj == 'remarks') {
							$('#nonSorActivities\\[0\\]\\.measurementSheetList\\[' + index + '\\]\\.' + obj).html(value);
						} else {
							$('#nonSorActivities\\[0\\]\\.measurementSheetList\\[' + index + '\\]\\.' + obj).attr('value', value);
							if(obj != "slNo" && obj != "remarks"){
								document.getElementById('nonSorActivities[0].measurementSheetList[' + index + '].' + obj).setAttribute('data-' + obj, value);
							}
						}
					});
					
				});
				var total = 0;
				var identifier;
				$(responseObj[index].ms).each(function(index, measurementSheet){
					if(document.getElementById('nonSorActivities[0].measurementSheetList[' + index + '].identifier').value=='A')
						identifier = "A";
					else
						identifier = "D";
					var quantity = document.getElementById('nonSorActivities[0].measurementSheetList[' + index + '].quantity').value;
					if(identifier == "A")
						total = total + Number(quantity);
					else
						total = total - Number(quantity);
				});
				$('#nonSorActivities\\[0\\]\\.msnet').html(total);
			}
			nonSorCheck = true;
		} else{
			if(responseObj[index].scheduleId != null){
				var key = $("#tblsor tbody tr:visible[id='sorRow']").length;
				addRow('tblsor', 'sorRow');
				resetIndexes();
				$('#soractivityid_' + key).val('');
				$('#quantity_' + key).val('');
				$('#categoryCode_' + key).val('');
				$('#quantity_' + key).removeAttr('readonly');
				$('#quantity_' + key).attr('required', 'required');
				$('.amount_' + key).html('');
				$('#vat_' + key).val('');
				$('.vatAmount_' + key).html('');
				$('.total_' + key).html('');
				if(document.getElementById('sorActivities['+key+'].mstd') && (responseObj[index].ms == "" || responseObj[index].ms == undefined))
					document.getElementById('sorActivities['+key+'].mstd').innerHTML=""; 
				if(document.getElementById('sorActivities['+key+'].mspresent'))
					document.getElementById('sorActivities['+key+'].mspresent').value="0"; 
				if(document.getElementById('sorActivities['+key+'].msopen'))
					document.getElementById('sorActivities['+key+'].msopen').value="0";
				generateSorSno();	
				if (responseObj[index].ms != "" && responseObj[index].ms != undefined) {
					var newrow= $('#msheaderrowtemplate').html();
					
					newrow=  newrow.replace(/msrowtemplate/g, 'msrowsorActivities[' + key + ']');
					newrow=  newrow.replace(/templatesorActivities\[0\]/g, 'sorActivities[' + key + ']');
					newrow = newrow.replace(/templatesorActivities_0/g, 'sorActivities_' + key );
					newrow = newrow.replace(/templatemssubmit_0/g, 'mssubmit_' + key );
					document.getElementById('sorActivities[' + key + '].mstd').innerHTML=newrow;
					$(responseObj[index].ms).each(function(index, measurementSheet){
						if (index > 0) {
							var msrowname= "sorActivities[" + key + "].mstable";
							var msrownameid=msrowname.split(".")[0];
							var rep='measurementSheetList\['+ index +'\]';

							var $newrow = "<tr>"+$('#msrowtemplate').html()+"</tr>";
							$newrow = $newrow.replace(/templatesorActivities\[0\]/g,msrownameid);
							$newrow = $newrow.replace(/measurementSheetList\[0\]/g,rep);
							$newrow = $newrow.replace(/templatesorActivities_0/g, 'sorActivities_' + key);
							$newrow = $newrow.replace(/measurementSheetList_0_id/g, 'measurementSheetList_' + index + '_id');
							$newrow = $newrow.replace(/measurementSheetList_0_parent/g, 'measurementSheetList_' + index + '_parent');
							$newrow = $newrow.replace('value="1"','value="'+(index+1)+'"');
							$('#sorActivities\\[' + key + '\\]\\.mssubmit').closest('tr').before($newrow);
							patternvalidation();
						}
						
						$.each(measurementSheet, function(obj, value){
							if(obj == "identifier") {
								if(value == 'A')
									document.getElementById('sorActivities[' + key + '].measurementSheetList[' + index + '].' + obj).options[0].setAttribute('selected', 'selected');
								else
									document.getElementById('sorActivities[' + key + '].measurementSheetList[' + index + '].' + obj).options[1].setAttribute('selected', 'selected');
							} else if(obj == 'remarks') {
								$('#sorActivities\\[' + key + '\\]\\.measurementSheetList\\[' + index + '\\]\\.' + obj).html(value);
							} else {
								$('#sorActivities\\[' + key + '\\]\\.measurementSheetList\\[' + index + '\\]\\.' + obj).attr('value', value);
								if(obj != "slNo" && obj != "remarks"){
									document.getElementById('sorActivities[' + key + '].measurementSheetList[' + index + '].' + obj).setAttribute('data-' + obj, value);
								}
							}
						});
						
					});
					var total = 0;
					var identifier;
					$(responseObj[index].ms).each(function(index, measurementSheet){
						if(document.getElementById('sorActivities[' + key + '].measurementSheetList[' + index + '].identifier').value=='A')
							identifier = "A";
						else
							identifier = "D";
						var quantity = document.getElementById('sorActivities[' + key + '].measurementSheetList[' + index + '].quantity').value;
						if(identifier == "A")
							total = total + Number(quantity);
						else
							total = total - Number(quantity);
					});
					$('#sorActivities\\[' + key + '\\]\\.msnet').html(total);
				}
			}else{
				if(!nonSorCheck){
					$('#nonSorMessage').prop("hidden",true);
					$('#nonSorRow').removeAttr("hidden");
					$('#nonSorRow').removeAttr('nonsorinvisible');
					if (responseObj[index].ms != "" && responseObj[index].ms != undefined) {
						var newrow= $('#msheaderrowtemplate').html();
						
						newrow=  newrow.replace(/msrowtemplate/g, 'msrownonSorActivities[0]');
						newrow=  newrow.replace(/templatesorActivities\[0\]/g, 'nonSorActivities[0]');
						newrow = newrow.replace(/templatesorActivities_0/g, 'nonSorActivities_0');
						newrow = newrow.replace(/templatemssubmit_0/g, 'mssubmit_0');
						document.getElementById('nonSorActivities[0].mstd').innerHTML=newrow;
						$(responseObj[index].ms).each(function(index, measurementSheet){
							if (index > 0) {
								var msrowname= "nonSorActivities[0].mstable";
								var msrownameid=msrowname.split(".")[0];
								var rep='measurementSheetList\['+ index +'\]';

								var $newrow = "<tr>"+$('#msrowtemplate').html()+"</tr>";
								$newrow = $newrow.replace(/templatesorActivities\[0\]/g,msrownameid);
								$newrow = $newrow.replace(/measurementSheetList\[0\]/g,rep);
								$newrow = $newrow.replace(/templatesorActivities_0/g, 'nonSorActivities_0');
								$newrow = $newrow.replace(/measurementSheetList_0_id/g, 'measurementSheetList_' + index + '_id');
								$newrow = $newrow.replace(/measurementSheetList_0_parent/g, 'measurementSheetList_' + index + '_parent');
								$newrow = $newrow.replace('value="1"','value="'+(index+1)+'"');
								$('#nonSorActivities\\[0\\]\\.mssubmit').closest('tr').before($newrow);
								patternvalidation();
							}
							
							$.each(measurementSheet, function(obj, value){
								if(obj == "identifier") {
									if(value == 'A')
										document.getElementById('nonSorActivities[0].measurementSheetList[' + index + '].' + obj).options[0].setAttribute('selected', 'selected');
									else
										document.getElementById('nonSorActivities[0].measurementSheetList[' + index + '].' + obj).options[1].setAttribute('selected', 'selected');
								} else if(obj == 'remarks') {
									$('#nonSorActivities\\[0\\]\\.measurementSheetList\\[' + index + '\\]\\.' + obj).html(value);
								} else {
									$('#nonSorActivities\\[0\\]\\.measurementSheetList\\[' + index + '\\]\\.' + obj).attr('value', value);
									if(obj != "slNo" && obj != "remarks"){
										document.getElementById('nonSorActivities[0].measurementSheetList[' + index + '].' + obj).setAttribute('data-' + obj, value);
									}
								}
							});
							
						});
						var total = 0;
						var identifier;
						$(responseObj[index].ms).each(function(index, measurementSheet){
							if(document.getElementById('nonSorActivities[0].measurementSheetList[' + index + '].identifier').value=='A')
								identifier = "A";
							else
								identifier = "D";
							var quantity = document.getElementById('nonSorActivities[0].measurementSheetList[' + index + '].quantity').value;
							if(identifier == "A")
								total = total + Number(quantity);
							else
								total = total - Number(quantity);
						});
						$('#nonSorActivities\\[0\\]\\.msnet').html(total);
					}
				}
				if(nonSorCheck) {
					var key = $("#tblNonSor tbody tr:visible[id='nonSorRow']").length;
					addRow('tblNonSor', 'nonSorRow');
					resetIndexes();
					$('#activityid_' + key).val('');
					$('#nonSorId_' + key).val('');
					$('#nonSorDesc_' + key).val('');
					$('#nonSorUom_' + key).val('');
					$('#nonSorRate_' + key).val('');
					$('#nonSorEstimateRate_' + key).val('');
					$('#nonSorQuantity_' + key).val('');
					$('#nonSorQuantity_' + key).removeAttr('readonly');
					$('#nonSorQuantity_' + key).attr('required', 'required');
					$('.nonSorAmount_' + key).html('');
					$('#nonSorServiceTaxPerc_' + key).val('');
					$('.nonSorVatAmt_' + key).html('');
					$('.nonSorTotal_' + key).html('');
					if(document.getElementById('nonSorActivities['+key+'].mstd') && (responseObj[index].ms == "" || responseObj[index].ms == undefined))
						document.getElementById('nonSorActivities['+key+'].mstd').innerHTML=""; 
					if(document.getElementById('nonSorActivities['+key+'].mspresent'))
						document.getElementById('nonSorActivities['+key+'].mspresent').value="0"; 
					if(document.getElementById('nonSorActivities['+key+'].msopen'))
						document.getElementById('nonSorActivities['+key+'].msopen').value="0";
					generateSlno();
					if (responseObj[index].ms != "" && responseObj[index].ms != undefined) {
						var newrow= $('#msheaderrowtemplate').html();
						
						newrow=  newrow.replace(/msrowtemplate/g, 'msrownonSorActivities[' + key + ']');
						newrow=  newrow.replace(/templatesorActivities\[0\]/g, 'nonSorActivities[' + key + ']');
						newrow = newrow.replace(/templatesorActivities_0/g, 'nonSorActivities_' + key );
						newrow = newrow.replace(/templatemssubmit_0/g, 'mssubmit_' + key );
						document.getElementById('nonSorActivities[' + key + '].mstd').innerHTML=newrow;
						$(responseObj[index].ms).each(function(index, measurementSheet){
							if (index > 0) {
								var msrowname= "nonSorActivities[" + key + "].mstable";
								var msrownameid=msrowname.split(".")[0];
								var rep='measurementSheetList\['+ index +'\]';

								var $newrow = "<tr>"+$('#msrowtemplate').html()+"</tr>";
								$newrow = $newrow.replace(/templatesorActivities\[0\]/g,msrownameid);
								$newrow = $newrow.replace(/measurementSheetList\[0\]/g,rep);
								$newrow = $newrow.replace(/templatesorActivities_0/g, 'nonSorActivities_' + key);
								$newrow = $newrow.replace(/measurementSheetList_0_id/g, 'measurementSheetList_' + index + '_id');
								$newrow = $newrow.replace(/measurementSheetList_0_parent/g, 'measurementSheetList_' + index + '_parent');
								$newrow = $newrow.replace('value="1"','value="'+(index+1)+'"');
								$('#nonSorActivities\\[' + key + '\\]\\.mssubmit').closest('tr').before($newrow);
								patternvalidation();
							}
							
							$.each(measurementSheet, function(obj, value){
								if(obj == "identifier") {
									if(value == 'A')
										document.getElementById('nonSorActivities[' + key + '].measurementSheetList[' + index + '].' + obj).options[0].setAttribute('selected', 'selected');
									else
										document.getElementById('nonSorActivities[' + key + '].measurementSheetList[' + index + '].' + obj).options[1].setAttribute('selected', 'selected');
								} else if(obj == 'remarks') {
									$('#nonSorActivities\\[' + key + '\\]\\.measurementSheetList\\[' + index + '\\]\\.' + obj).html(value);
								} else {
									$('#nonSorActivities\\[' + key + '\\]\\.measurementSheetList\\[' + index + '\\]\\.' + obj).attr('value', value);
									if(obj != "slNo" && obj != "remarks"){
										document.getElementById('nonSorActivities[' + key + '].measurementSheetList[' + index + '].' + obj).setAttribute('data-' + obj, value);
									}
								}
							});
							
						});
						var total = 0;
						var identifier;
						$(responseObj[index].ms).each(function(index, measurementSheet){
							if(document.getElementById('nonSorActivities[' + key + '].measurementSheetList[' + index + '].identifier').value=='A')
								identifier = "A";
							else
								identifier = "D";
							var quantity = document.getElementById('nonSorActivities[' + key + '].measurementSheetList[' + index + '].quantity').value;
							if(identifier == "A")
								total = total + Number(quantity);
							else
								total = total - Number(quantity);
						});
						$('#nonSorActivities\\[' + key + '\\]\\.msnet').html(total);
					}
				}
				nonSorCheck = true;
			}
		}
		if(responseObj[index].scheduleId != null){
			$('#id_'+sorCount).val(responseObj[index].scheduleId);
			$('.categoryCode_'+sorCount).html(responseObj[index].scheduleCategoryCode);
			$('.code_'+sorCount).html(responseObj[index].scheduleCode);
			$('.summary_'+sorCount).html(responseObj[index].scheduleSummary);
			$('.description_'+sorCount).html(hint.replace(/@fulldescription@/g, responseObj[index].scheduleDescription));
			$('.uom_'+sorCount).html(responseObj[index].scheduleUom);
			$('#sorUomid_'+sorCount).val(responseObj[index].scheduleUomId);
			if(responseObj[index].scheduleRate!=null) {
				$('.estimateRate_'+sorCount).html(responseObj[index].scheduleRate);
				$('#rate_'+sorCount).val(getUnitRate(responseObj[index].scheduleUom, responseObj[index].scheduleRate));
				$('#estimateRate_'+sorCount).val(responseObj[index].scheduleRate);
			}
			else {
				$('.estimateRate_'+sorCount).html(0);
				$('#rate_'+sorCount).val(0);
				$('#estimateRate_'+sorCount).val(0);
			}
			if (responseObj[index].scheduleQuantity != null) {
				$('#quantity_'+sorCount).val(responseObj[index].scheduleQuantity);
				$('#quantity_'+sorCount).trigger('blur');
			}
			if(document.getElementById('sorActivities['+sorCount+'].mstd') && (responseObj[index].ms == "" || responseObj[index].ms == undefined))
				document.getElementById('sorActivities['+sorCount+'].mstd').innerHTML=""; 
			if(document.getElementById('sorActivities['+sorCount+'].mspresent'))
				document.getElementById('sorActivities['+sorCount+'].mspresent').value="0";
			if(document.getElementById('sorActivities['+sorCount+'].msopen'))
				document.getElementById('sorActivities['+sorCount+'].msopen').value="0";
			sorCount++;
		}else{
			$('#nonSorDesc_'+nonSorCount).val(responseObj[index].nonSorDescription);
			$('#nonSorUom_'+nonSorCount).val(responseObj[index].nonSorUomId);
			$('#nonSorUomid_'+nonSorCount).val(responseObj[index].nonSorUomId);
			$('#nonSorEstimateRate_'+nonSorCount).val(parseFloat(responseObj[index].nonSorRate).toFixed(2)); 
			$('#nonSorRate_'+nonSorCount).val(getUnitRate(responseObj[index].nonSorUom,responseObj[index].nonSorRate));
			if (responseObj[index].nonSorQuantity != null) {
				$('#nonSorQuantity_'+nonSorCount).val(responseObj[index].nonSorQuantity);
				$('#nonSorQuantity_'+nonSorCount).trigger('change');
			}
			if(document.getElementById('nonSorActivities['+nonSorCount+'].mstd') && (responseObj[index].ms == "" || responseObj[index].ms == undefined))
				document.getElementById('nonSorActivities['+nonSorCount+'].mstd').innerHTML=""; 
			if(document.getElementById('nonSorActivities['+nonSorCount+'].mspresent'))
				document.getElementById('nonSorActivities['+nonSorCount+'].mspresent').value="0";
			if(document.getElementById('nonSorActivities['+nonSorCount+'].msopen'))
				document.getElementById('nonSorActivities['+nonSorCount+'].msopen').value="0";
			nonSorCount++;
		}
		resetIndexes();
	});
}

function getSchemsByFundId(fundId) {
	if ($('#fund').val() === '') {
		$('#scheme').empty();
		$('#scheme').append($('<option>').text('Select from below').attr('value', ''));
		$('#subScheme').empty();
		$('#subScheme').append($('<option>').text('Select from below').attr('value', ''));
		return;
	} else {

		$.ajax({
			method : "GET",
			url : "/egworks/lineestimate/getschemesbyfundid",
			data : {
				fundId : $('#fund').val()
			},
			async : true
		}).done(
				function(response) {
					$('#scheme').empty();
					$('#scheme').append($("<option value=''>Select from below</option>"));
					var output = '<option value="">Select from below</option>';
					$.each(response, function(index, value) {
						var selected="";
						if($schemeId)
						{
							if($schemeId==value.id)
							{
								selected="selected";
							}
						}
						$('#scheme').append($('<option '+ selected +'>').text(value.name).attr('value', value.id));
					});
				});

	}
}

function getSubSchemsBySchemeId(schemeId) {
	if (schemeId === '') {
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
				if(response)
				var responseObj = JSON.parse(response);
				$.each(responseObj, function(index, value) {
					$('#subScheme').append($('<option>').text(responseObj[index].name).attr('value', responseObj[index].id));
					$('#subScheme').val($subSchemeId);
				});
			}, 
			error: function (response) { 
				//console.log("failed");
			}
		});
	}
}


$(document).on('blur', '.inputYearEstimatePercentage', function(e){
	calculatePercentage('#estimateTotal', '.inputYearEstimatePercentage');
});

$(document).on('paste', '.inputYearEstimatePercentage', function(e){
	calculatePercentage('#estimateTotal', '.inputYearEstimatePercentage');
});

function calculatePercentage(displayElem, classname)
{
	var percentage=0;
	$(classname).each(function(idx, elem){
		if($(elem).val()){
			percentage = percentage + parseFloat($(elem).val());
			if(percentage > 100) {
				bootbox.alert("Total percentage should not be greater than 100 ");
				$(elem).val("");
			} else
				$(displayElem).text(percentage);
		}
	});
}

$(document).on('change', '.dropdownYear', function(e){
	validateDuplicateYear();
});

function validateDuplicateYear()
{

	var isValidationSuccess=true;
	var selectedYearCollection=[];

	$(".dropdownYear").each(function(idx, elem){

		var selectedYear=jQuery(this).find("option:selected").text();

		if(selectedYearCollection.indexOf(selectedYear) === -1)
		{
			selectedYearCollection.push(selectedYear);
		}
		else
		{
			//duplicate value handling statement
			isValidationSuccess=false;
			$(this).prop('selectedIndex', 0);
			bootbox.alert("The year "+ selectedYear +" already selected!");
			return false;
		}
	});

	return isValidationSuccess;

}

function viewLineEstimate(id) {
	window.open("/egworks/lineestimate/view/" + id, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function viewLOA(id) {
	window.open("/egworks/letterofacceptance/view/" + id, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function validateQuantity() {
	$( "input[name$='quantity']" ).on("keyup", function(){
		var valid = /^[1-9](\d{0,9})(\.\d{0,2})?$/.test(this.value),
		val = this.value;

		if(!valid){
			//console.log("Invalid input!");
			this.value = val.substring(0, val.length - 1);
		}
	});
}

function updateUom(obj) {
	var rowId = $(obj).attr('id').split('_').pop();
	$('#nonSorUomid_' + rowId).val($(obj).val());
	$('#nonSorUomid_' + rowId).val($(obj).val());
	$('#nonSorEstimateRate_' + rowId).val("");
	$('#nonSorQuantity_' + rowId).val("");
	calculateNonSorEstimateAmount($('#nonSorQuantity_' + rowId));
}

$(document).on('click','.viewAsset',function(e) {
	var assetId = jQuery(this).text(); 
	var url = "/egassets/asset/view/"+assetId;
	window.open(url,'', 'height=650,width=980,scrollbars=yes,status=yes'); 
});

$(document).on('click','.searchAssetbtn',function(e) {
	var index = $(this).attr("data-idx"); 
	var status = getStatusForNatureOfWork($("#natureOfWork option:selected" ).text());
	if((status != '') || (e.target.textContent === " Search" && $inputHiddenAssetId.val() !== '')) {
		window.open("/egassets/asset/showsearchpage?mode=view&rowId="+index+"&assetStatus="+status,"",
		"height=600,width=1200,scrollbars=yes,left=0,top=0,status=yes");
	} else {
			bootbox.alert("No Asset can be link for selected nature of work");
	}
}); 

function update(data)
{
	var index = 0;
	var isValid = 1;
	jQuery("#assetDetailRow").clone().find("input:hidden").each(function() {
		var assetId = $('input[name="tempAssetValues['+ index +'].asset.id"]').val();
		if(data.id == assetId) {
			isValid = 0;
			return false;
		}
		index++;
	});
	if(isValid == 1) {
		$('span[id="assetname['+ data.rowidx +']"]').html(data.name);
		$('a[id="assetcode['+ data.rowidx +']"]').html(data.code);
		$('input[name="tempAssetValues['+ data.rowidx +'].asset.code"]').val(data.code);
		$('input[name="tempAssetValues['+ data.rowidx +'].asset.name"]').val(data.name);
		$('input[name="tempAssetValues['+ data.rowidx +'].asset.id"]').val(data.id);
	} else {
		bootbox.alert("Selected Asset details already added");
	}
}


function getStatusForNatureOfWork(name) {
	if(name=='Deposit Works' || name=='Deposit Works')
		return '';
	else
		return 'Created&assetStatus=CWIP&assetStatus=Capitalized&assetStatus=Revaluated';
}

function addAssetDetails() {
	var tbl = document.getElementById('tblassetdetails');
	var rowO = tbl.rows.length;
	if(document.getElementById('assetDetailRow') != null)
	{
		//get Next Row Index to Generate
		var nextIdx = tbl.rows.length-1;
		var sno = 1;
		sno = nextIdx + 1;
		//validate status variable for exiting function
		var isValid=1;//for default have success value 0  

		//validate existing rows in table
		jQuery("#assetDetailsTbl tbody tr").find("input:hidden").each(function() {
			if((jQuery(this).data('optional') === 0) && (jQuery(this).val()))
			{
				//console.log('calling :)');
				jQuery(this).focus();
				bootbox.alert("Please enter value for the row");
				isValid=0;//set validation failure
				return false;
			}
		});

		if (isValid === 0) {
			return false;
		}

		jQuery("#assetDetailRow").clone().find("input:hidden, span, button").each(function() {

			if($(this).attr("id"))
			{
				$(this).attr({
					'id': function(_, id) { 
						return id.replace(/\[.\]/g, '['+ nextIdx +']'); 
					}});
				$(this).html('');
				$(this).val('');
			}

			if($(this).attr("name"))
			{
				$(this).attr({
					'name': function(_, name) {
						return name.replace(/\[.\]/g, '['+ nextIdx +']'); 
					}});
			}

			if($(this).attr("data-idx"))
			{
				$(this).attr({
					'data-idx' : function(_,dataIdx)
					{
						return nextIdx;
					}
				});

			}

			if($(this)[0].hasAttribute("data-sno"))
			{
				$(this).text((nextIdx+1));
			}



		}).end().appendTo("#assetDetailsTbl");
	}
}

function deleteAssetDetail(obj) {
	rIndex = getRow(obj).rowIndex - 1;
	var tbl=document.getElementById('assetDetailsTbl');
	var rowo=tbl.rows.length;
	if(rowo<=1)
	{
		bootbox.alert("This row cannot be deleted");
		return false;
	}
	else
	{	
		tbl.deleteRow(rIndex);	

		//starting index for table fields
		var idx=0;

		//regenerate index existing inputs in table row
		$("#tblassetdetails tbody tr").each(function() {

			$spanSNo=$(this).find('span[data-sno]');
			if($spanSNo)
			{
				$spanSNo.text((idx+1));
			}
			$(this).find("input, select, span, button").each(function() {

				if($(this).attr("id"))
				{
					$(this).attr({
						'id': function(_, id) {  
							return id.replace(/\[.\]/g, '['+ idx +']'); 
						}});
				}

				if($(this).attr("name"))
				{
					$(this).attr({
						'name': function(_, name) {
							return name.replace(/\[.\]/g, '['+ idx +']'); 
						}});
				}

				if($(this).attr("data-idx"))
				{
					$(this).attr({
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

function changeColor(tableRow, highLight)
{
	if (highLight)
	{
		tableRow.style.backgroundColor = '#dcfac9';
	}
	else
	{
		tableRow.style.backgroundColor = 'white';
	}
}

function validateWorkFlowApprover(name) {
	document.getElementById("workFlowAction").value = name;
	var button = document.getElementById("workFlowAction").value;
	var flag = true;
	
	if ($('#location').val() == '' || $('#description').val() == ''
		|| $('#parentCategory').val() == '' || $('#fund').val() == ''
			|| $('#function').val() == '' || $('#budgethead').val() == '') {
		bootbox.alert($('#mandatoryError').val());
		return false;
	}

	if (button != null && button == 'Save') {
		removeApprovalMandatoryAttribute();
		$('#approvalComent').removeAttr('required');

		flag = validateSORDetails();
		
		if(flag && $('#abstractEstimate').valid()) {
			if($('#isWorkOrderCreated').prop("checked") == true) {
				var isValidationSuccess=true;
				
				var adminSanctionDate = $('#adminSanctionDate').data('datepicker').date;
				var technicalSanctionDate = $('#technicalSanctionDate').data('datepicker').date;
				var technicalSanctionNumber = $('#technicalSanctionNumber').val();
				
				if(adminSanctionDate > technicalSanctionDate && technicalSanctionDate != '') {
					bootbox.alert($('#errorTechDate').val());
					$('#technicalSanctionDate').val("");
					return false;
				}
	
				if($('#isBillsCreated').prop("checked") == true && $('#isWorkOrderCreated').prop("checked") == false) {
					bootbox.alert($('#msgWorkOrderCreated').val());
					return false;
				}
				
				if(!flag)
					return false;
			}
			var hiddenRowCount = $("#tblsor tbody tr[sorinvisible='true']").length;
			if(hiddenRowCount != 1) {
				
				$('.estimateRate').each(function() {
					if (parseFloat($(this).html()) <= 0)
						flag = false;
				});
				if (!flag ) {
					bootbox.alert($('#errorrateszero').val());
					return false;
				}

				$('.quantity').each(function() {
					if (parseFloat($(this).val()) <= 0)
						flag = false;
				});
				
			}
			hiddenRowCount = $("#tblNonSor tbody tr[nonsorinvisible='true']").length;
			if(hiddenRowCount != 1) {
				$('.nonSorEstimateRate').each(function() {
					if (parseFloat($(this).val()) <= 0)
						flag = false;
				});
				if (!flag) {
					bootbox.alert($('#errorrateszero').val());
					return false;
				}

				$('.nonSorQuantity').each(function() {
					if (parseFloat($(this).val()) <= 0)
						flag = false;
				});

				if (!flag && estimateAmount>0) {
					bootbox.alert($('#errorquantityzero').val());
					return false;
				}
			}
		}		
	}
	if (button != null && button == 'Approve') {
		removeApprovalMandatoryAttribute();
		$('#approvalComent').removeAttr('required');
	}
	if (button != null && button == 'Submit') {
		addApprovalMandatoryAttribute();
		$('#approvalComent').removeAttr('required');
	}
	if (button != null && button == 'Reject') {
		removeApprovalMandatoryAttribute();
		$('#approvalComent').attr('required', 'required');
		$('#councilResolutionNumber').removeAttr('required');
		$('#councilResolutionDate').removeAttr('required');
		$('#adminSanctionNumber').removeAttr('required');
	}
	if (button != null && button == 'Cancel') {
		removeApprovalMandatoryAttribute();
		$('#approvalComent').attr('required', 'required');

		if($("form").valid())
		{
			bootbox.confirm($('#cancelConfirm').val(), function(result) {
				if(!result) {
					bootbox.hideAll();
					return false;
				} else {
					validateSORDetails();
					deleteHiddenRows();
					document.forms[0].submit();
				}
			});
		}
		return false;
	}
	if (button != null && button == 'Forward') {
		flag = showHideApprovalDetails("Forward");
		if(!flag)
			return false;
		addApprovalMandatoryAttribute();
		$('#approvalComent').removeAttr('required');
		
		var estimateValue = parseFloat($('#estimateValueTotal').html());
		if ($('#lineEstimateRequired').val() == 'false') {
			var modeOfEntrustment = $('#modeOfAllotment').val();
			var nominationLimit = $('#nominationLimit').val();
			var nominationName = $('#nominationName').val();
			var message = nominationName + " Recommended mode of entrustment can be awarded for the estimates with value less than Rs." + nominationLimit + "/- . Please enter proper value";
			if(modeOfEntrustment == nominationName && parseFloat(estimateValue) > parseFloat(nominationLimit) ){
				bootbox.alert(message);
				return false;
			}
		}

		var lineEstimateAmount = parseFloat($('#lineEstimateAmount').val());
		if($('#lineEstimateRequired').val() == 'true' && estimateValue > lineEstimateAmount) {
			var diff = estimateValue - lineEstimateAmount;
			bootbox.alert("Abstract/Detailed estimate amount is Rs."+ parseFloat(diff).toFixed(2) +"/- more than the administrative sanctioned amount (Rs." + lineEstimateAmount + "/-) for this estimate , please create abstract estimate with less amount");
			return false;
		}

		var inVisibleSorCount = $("#tblsor tbody tr[sorinvisible='true']").length;
		var inVisibleNonSorCount = $("#tblNonSor tbody tr[nonsorinvisible='true']").length;
		if (inVisibleSorCount == 1 && inVisibleNonSorCount == 1) {
			bootbox.alert($('#errorsornonsor').val());
			return false;
		}

		$locationAppConfig = $('#locationAppConfig').val();
		if($locationAppConfig == 'true') {
			if($('#location').val() == '' || $('#latitude').val() == ''  || $('#longitude').val() == '') {
				bootbox.alert($('#errorlocation').val());
				return false;
			}
		}
		
		var resultLengthForDeductionTable = jQuery('#deductionTable tr').length - 1;
		for (var i = 0; i < resultLengthForDeductionTable; i++) {
			var indexForDeduction=i;
			var accountCode = document.getElementById('tempDeductionValues[' + indexForDeduction + '].accountCode').value;
			var deductionAmount = document.getElementById('tempDeductionValues[' + indexForDeduction + '].amount').value;
			if((accountCode == '' ) && (parseFloat(deductionAmount) != 0 && deductionAmount != '')){
				bootbox.alert($('#msgAccountCode').val());
				return false;				
			} 
			if((accountCode != '') && (parseFloat(deductionAmount) == 0 || deductionAmount == '')){
				bootbox.alert($('#msgDeductionAmount').val());
				return false;				
			} 
		}
		
		flag = validateSORDetails();

		if(flag && $('#abstractEstimate').valid()) {
			var hiddenRowCount = $("#tblsor tbody tr[sorinvisible='true']").length;
			if(hiddenRowCount != 1) {
				
				$('.estimateRate').each(function() {
					if (parseFloat($(this).html()) <= 0)
						flag = false;
				});
				if (!flag ) {
					bootbox.alert($('#errorrateszero').val());
					return false;
				}

				$('.quantity').each(function() {
					if (parseFloat($(this).val()) <= 0)
						flag = false;
				});
				
			}
			hiddenRowCount = $("#tblNonSor tbody tr[nonsorinvisible='true']").length;
			if(hiddenRowCount != 1) {
				$('.nonSorEstimateRate').each(function() {
					if (parseFloat($(this).val()) <= 0)
						flag = false;
				});
				if (!flag) {
					bootbox.alert($('#errorrateszero').val());
					return false;
				}

				$('.nonSorQuantity').each(function() {
					if (parseFloat($(this).val()) <= 0)
						flag = false;
				});
				
				if (!flag) {
					bootbox.alert($('#errorquantityzero').val());
					return false;
				}
			}
		}
		
	}
	
	if (button != null && button == 'Create And Approve') {
		if ($('#spillOverFlag').val() == 'false') {
			flag = showHideApprovalDetails("Create And Approve");
			if(!flag)
				return false;
		}
		removeApprovalMandatoryAttribute();
		$('#approvalComent').removeAttr('required');

		var lineEstimateAmount = parseFloat($('#lineEstimateAmount').val());
		var estimateValue = parseFloat($('#estimateValueTotal').html());
		if($('#lineEstimateRequired').val() == 'true' && estimateValue > lineEstimateAmount) {
			var diff = estimateValue - lineEstimateAmount;
			bootbox.alert("Abstract/Detailed estimate amount is Rs."+ parseFloat(diff).toFixed(2) +"/- more than the administrative sanctioned amount (Rs." + lineEstimateAmount + "/-) for this estimate , please create abstract estimate with less amount");
			return false;
		}

		var inVisibleSorCount = $("#tblsor tbody tr[sorinvisible='true']").length;
		var inVisibleNonSorCount = $("#tblNonSor tbody tr[nonsorinvisible='true']").length;
		if (inVisibleSorCount == 1 && inVisibleNonSorCount == 1) {
			bootbox.alert($('#errorsornonsor').val());
			return false;
		}

		$locationAppConfig = $('#locationAppConfig').val();
		if($locationAppConfig == 'true') {
			if($('#location').val() == '' || $('#latitude').val() == ''  || $('#longitude').val() == '') {
				bootbox.alert($('#errorlocation').val());
				return false;
			}
		}
		
		var resultLengthForDeductionTable = jQuery('#deductionTable tr').length - 1;
		for (var i = 0; i < resultLengthForDeductionTable; i++) {
			var indexForDeduction=i;
			var accountCode = $('#tempDeductionValues\\[' + indexForDeduction + '\\]\\.accountCode').val();
			var deductionAmount = $('#tempDeductionValues\\[' + indexForDeduction + '\\]\\.amount').val();
			if((accountCode == '' ) && (parseFloat(deductionAmount) != 0 && deductionAmount != '')){
				bootbox.alert($('#msgAccountCode').val());
				return false;				
			} 
			if((accountCode != '') && (parseFloat(deductionAmount) == 0 || deductionAmount == '')){
				bootbox.alert($('#msgDeductionAmount').val());
				return false;				
			} 
		}
		
		flag = validateSORDetails();

		if(flag && $('#abstractEstimate').valid()) {
			var hiddenRowCount = $("#tblsor tbody tr[sorinvisible='true']").length;
			if(hiddenRowCount != 1) {
				
				$('.estimateRate').each(function() {
					if (parseFloat($(this).html()) <= 0)
						flag = false;
				});
				if (!flag ) {
					bootbox.alert($('#errorrateszero').val());
					return false;
				}

				$('.quantity').each(function() {
					if (parseFloat($(this).val()) <= 0)
						flag = false;
				});
				
			}
			hiddenRowCount = $("#tblNonSor tbody tr[nonsorinvisible='true']").length;
			if(hiddenRowCount != 1) {
				$('.nonSorEstimateRate').each(function() {
					if (parseFloat($(this).val()) <= 0)
						flag = false;
				});
				if (!flag) {
					bootbox.alert($('#errorrateszero').val());
					return false;
				}

				$('.nonSorQuantity').each(function() {
					if (parseFloat($(this).val()) <= 0)
						flag = false;
				});
				
				if (!flag) {
					bootbox.alert($('#errorquantityzero').val());
					return false;
				}
			}
		}
		
	}

	if(flag) {
		deleteHiddenRows();
		var resultLengthForDeductionTable = jQuery('#deductionTable tr').length - 1;
		var deleteDeductionTable=$('#deductionTable tr:last');
		var accountCode = $('#tempDeductionValues\\[0\\]\\.accountCode').val();
		if(resultLengthForDeductionTable<=1 && accountCode == "") {
			var i=0;
			deleteDeductionTable.remove();
		}
		document.forms[0].submit;
		return true;
	} else
		return false;
}

function deleteHiddenRows(){
	
	var hiddenRowCount = $("#tblsor tbody tr[sorinvisible='true']").length;
	if(hiddenRowCount == 1) {
		var tbl=document.getElementById('tblsor');
		tbl.deleteRow(2);
	}

	hiddenRowCount = $("#tblNonSor tbody tr[nonsorinvisible='true']").length;
	if(hiddenRowCount == 1) {
		var tbl=document.getElementById('tblNonSor');
		tbl.deleteRow(2);
	}
	var overheadTableLength = jQuery('#overheadTable tr').length-1;
	if(overheadTableLength == 1){
		var overhead = document.getElementById('tempOverheadValues[0].name').value;
		if(overhead==""){
			var tbl=document.getElementById('overheadTable');
			tbl.deleteRow(1);
		}
	}
	var tbl=document.getElementById('tblassetdetails');
	var assetTableLength = jQuery('#tblassetdetails tr').length-1;
	for (var i = 0; i < assetTableLength; i++) {
		var assetname = document.getElementById('tempAssetValues['+ i + '].asset.name').value;
		var assetcode = document.getElementById('tempAssetValues['+ i + '].asset.code').value;
		if(assetname == "" && assetcode== ""){
			tbl.deleteRow(i+1);
		}
	}
}

function validateSORDetails() {
	if($('#abstractEstimate').valid()) {
		if(!validateOverheads()){
			return false;
		}
		deleteHiddenRows();
		$('.disablefield').removeAttr("disabled");
		return true;
	} else
		return false;
}

function viewBOQ() {
	var estimateId = $("#estimateId").val();
	window.open("/egworks/abstractestimate/viewBillOfQuantitiesXls/"+estimateId,"","height=600,width=1200,scrollbars=yes,left=0,top=0,status=yes");
}

function openMap()
{
	var params = [
	              'height='+screen.height,
	              'width='+screen.width,
	              'fullscreen=yes' 
	              ].join(',');
	var popup ;
	var  lat = document.getElementById("latitude").value ;
	var lon = document.getElementById("longitude").value ;
	var status = $("#statusCode").val();
	if(status==null || status=='' || status =='NEW' || status=='REJECTED')
	{
		if(lat!='' && lon!='')
		{
			popup = window.open('/egworks/abstractestimate/maps?mapMode=edit&latitude='+lat+'&longitude='+lon,'popup_window', params);
		}
		else
		{
			popup = window.open('/egworks/abstractestimate/maps?mapMode=edit','popup_window', params);	
		}
	}	
	else
	{
		if(lat!='' && lon!='')
		{
			popup = window.open('/egworks/abstractestimate/maps?mapMode=view&latitude='+lat+'&longitude='+lon,'popup_window', params);
		}
		else
			return;
	}	
	//popup.moveTo(0,0);
}

function addRow(tableName,rowName) {


	if (document.getElementById(rowName) != null) {
		// get Next Row Index to Generate
		var nextIdx = 0;
		var sno = 1;
		var isValid=1;//for default have success value 0  
		//nextIdx =document.getElementsByName("sorRow").length;
		nextIdx = jQuery("#"+tableName+" > tbody > tr").length-1;
		 
		//sno = nextIdx;

		//console.log('TABLE ->', tableName);

		//console.log('NEXT IDX ->', nextIdx);

		// Generate all textboxes Id and name with new index
		var $row;
		if(tableName.indexOf("overheadTable")>=0)
			{
			$row=jQuery("#"+tableName+" tr:eq(1)").clone();
			nextIdx=nextIdx+1;
			} else if(tableName.indexOf("deductionTable")>=0){
				$row=jQuery("#"+tableName+" tr:eq(1)").clone();
				nextIdx=nextIdx+1;
			} else if(tableName.indexOf("tblyearestimate")>=0){
				$row=jQuery("#"+tableName+" tr:eq(1)").clone();
				nextIdx=nextIdx+1;
			} else
				{
		      var $row=jQuery("#"+tableName+" tr:eq(2)").clone();
				}

		if(tableName == 'tblassetdetails') {
			//validate existing rows in table
			jQuery("#"+tableName+" tr").find("input:hidden").each(function() {
				if((jQuery(this).data('optional') === 0) && (!jQuery(this).val()))
				{
					jQuery(this).focus();
					bootbox.alert("Please enter value for the row");
					isValid=0;//set validation failure
					return false;
				}
			});

			if (isValid === 0) {
				return false;
			}
			
			$row=jQuery("#"+tableName+" tr:eq(1)").clone();
			nextIdx++;
		}
		$row.find("a,input,select, errors,button, span,textarea").each(function() {
			var classval = jQuery(this).attr('class');
			if (jQuery(this).data('server')) {
				jQuery(this).removeAttr('data-server');
			}
			if(classval == 'spansorslno') {
				jQuery(this).text(nextIdx+1);
			}
			
			if(classval == 'spansno') {
				jQuery(this).text(nextIdx+1);
			}

			if(classval == 'assetdetail' || classval == 'viewAsset') {
				$(this).html('');
				$(this).val(''); 
				jQuery(this).text('');
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
						},
						'data-idx' : function(_,dataIdx)
						{
							return nextIdx;
						}
					});
			// if element is static attribute hold values for next row, otherwise it will be reset
			if (!jQuery(this).data('static')) {
				jQuery(this).val('');
			}

		}).end().appendTo("#"+tableName+" > tbody");	

		//console.log($row.html());

		sno++;

	}
}

function deleteRow(tableName,obj){
	if(ismsheetOpen())
	{
		bootbox.alert("Measurement Sheet is open Please close it first");
		return ;
	}
	var rIndex = getRow(obj).rowIndex;
	var id = jQuery(getRow(obj)).children('td:first').children('input:first').val();
	//To get all the deleted rows id
	var aIndex = rIndex - 1;
	var tbl=document.getElementById(tableName);	
	var rowcount=jQuery("#"+tableName+" > tbody > tr").length;
	if(rowcount<=1) {
		if(tableName == 'tblassetdetails') {
			$('a[id="assetcode[0]').html('');
			$('span[id="assetname[0]').html('');
			$('input[name="tempAssetValues[0].asset.code"]').val('');
			$('input[name="tempAssetValues[0].asset.name"]').val('');
			$('input[name="tempAssetValues[0].asset.id"]').val('');
			$('input[name="tempAssetValues[0].id"]').val('');
		}
	  else {
		bootbox.alert("This row can not be deleted");
		return false;
		}
	} else {
		tbl.deleteRow(rIndex);
		//starting index for table fields
		var idx= 0;
		var sno = 1;
		//regenerate index existing inputs in table row
		jQuery("#"+tableName+" > tbody > tr").each(function() {
			if(tableName=='tblsor')
			{
				jQuery(this).find("input,button, select,textarea,td,tbody,table, errors, span, input:hidden").each(function() {
					var classval = jQuery(this).attr('class');

					if(classval == 'spansno') {
						jQuery(this).text(sno);
						sno++;
					} else {
						jQuery(this).attr({
							'name': function(_, name) {
								if(!(jQuery(this).attr('name')===undefined))
									return name.replace(/sorActivities\[.\]/g, "sorActivities["+idx+"]"); 
							},
							'id': function(_, id) {
								if(!(jQuery(this).attr('id')===undefined))
									return id.replace(/sorActivities\[.\]/g, "sorActivities["+idx+"]"); 
							},
							'class' : function(_, name) {
								if(!(jQuery(this).attr('class')===undefined))
									return name.replace(/sorActivities\[.\]/g, "sorActivities["+idx+"]"); 
							},
							'data-idx' : function(_,dataIdx)
							{
								if(!(jQuery(this).attr('data-idx')===undefined))
									return idx;
							}
						});
					}
				});

				idx++;
			}
			else if( tableName=="tblNonSor")
			{
				jQuery(this).find("input, select,textarea,td,tbody,tr,table, errors, span, input:hidden").each(function() {
					var classval = jQuery(this).attr('class');

					if(classval == 'spansno') {
						jQuery(this).text(sno);
						sno++;
					} else {
						jQuery(this).attr({
							'name': function(_, name) {
								if(!(jQuery(this).attr('name')===undefined))
									return name.replace(/nonSorActivities\[.\]/g, "nonSorActivities["+idx+"]"); 
							},
							'id': function(_, id) {
								if(!(jQuery(this).attr('id')===undefined))
									return id.replace(/nonSorActivities\[.\]/g, "nonSorActivities["+idx+"]"); 
							},
							'class' : function(_, name) {
								if(!(jQuery(this).attr('class')===undefined))
									return name.replace(/nonSorActivities\[.\]/g, "nonSorActivities["+idx+"]"); 
							},
							'data-idx' : function(_,dataIdx)
							{
								if(!(jQuery(this).attr('data-idx')===undefined))
									return idx;
							}
						});
					}
				});

				idx++;




			}else
			{

				jQuery(this).find("a,input, select,button,textarea, errors, span, input:hidden").each(function() {
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
								if(!(jQuery(this).attr('data-idx')===undefined))
									return idx;
							}
						});
					}
				});

				idx++;
			}
		});
		return true;
	}
}

function getUnitRate(uom,estimateRate){
	var unitRate=0;
	var exceptionalUOMValues = $ExceptionalUOMs.split(':');
	var exceptionalUOMArray = $.makeArray( exceptionalUOMValues );
	$.map( exceptionalUOMArray, function( val, i ) {
		if(val.split(",")[0] == uom)
			unitRate = parseFloat( parseFloat(estimateRate) / parseFloat( val.split(",")[1] ));
	});
	if(unitRate!=0)
		return unitRate;
	else
		return estimateRate;
}


function viewEstimatePDF() {
	var estimateId = $("#estimateId").val();
	window.open("/egworks/abstractestimate/abstractEstimatePDF/" + estimateId, "", "height=600,width=1200,scrollbars=yes,left=0,top=0,status=yes");
}
function addMSheet(obj)    
{
//	console.log("adding msheet for "+obj.id);
	var rowid=obj.id;
	sorId=rowid.split(".");
	var	sortable=sorId[0];


	var msfieldsName=rowid.replace("msadd","measurementSheetList");
	var   mscontent=document.getElementById(rowid.replace("msadd","mstd")).innerHTML;

	var   msopen=document.getElementById(rowid.replace("msadd","msopen")).value;
	if(msopen==1)
		return ;

	if(mscontent!='')
	{
		  if(mscontent.indexOf(headstart) >=0)
			  {
			  var head= mscontent.substring(mscontent.indexOf(headstart),mscontent.indexOf(headend));
			  var tail= mscontent.substring(mscontent.indexOf(tailstart),mscontent.indexOf(tailend));
			  mscontent= mscontent.replace(head,"");
			  mscontent= mscontent.replace(tail,"");
			  }
		
		var curRow = $(obj).closest('tr');
		var k= "<tr class='msheet-tr' id=\""+sortable+".mstr\"><td colspan=\"9\">";
		mscontent=k+mscontent+"</td></tr>";
		curRow.after(mscontent);
		if(document.getElementById(rowid.replace("msadd","mstd")))
			document.getElementById(rowid.replace("msadd","mstd")).innerHTML="";
		if(document.getElementById(rowid.replace("msadd","msopen")))
			document.getElementById(rowid.replace("msadd","msopen")).value="1";
		var idx=sortable.substr(sortable.indexOf("["),sortable.indexOf("]"));
		
		if(sortable.indexOf("sorActivities") >= 0)
		{
			sorMsArray[idx]=mscontent;
		}
		else
		{
			nonSorMsArray[idx]=mscontent;
		}


	}else
	{

		var curRow = $(obj).closest('tr');
		var newrow= $('#msheaderrowtemplate').html();

		newrow=  newrow.replace(/msrowtemplate/g,'msrow'+sortable);
		newrow=  newrow.replace(/templatesorActivities\[0\]/g,sortable);
		if(document.getElementById(rowid.replace("msadd","msopen")))
			document.getElementById(rowid.replace("msadd","msopen")).value="1";
		if(document.getElementById(rowid.replace("msadd","mspresent")))
			document.getElementById(rowid.replace("msadd","mspresent")).value="1";
		curRow.after(newrow);
		var idx=sortable.substr(sortable.indexOf("["),sortable.indexOf("]"));
		if(sortable.indexOf("sorActivities") >= 0)
		{
			sorMsArray[idx]="";
		}
		else
		{
			nonSorMsArray[idx]="";
		}

	}
	patternvalidation();
}

$(document).on('click','.hide-ms',function () {

	var sid=$(this).closest('tr').attr("id");
	var name=	sid.split(".")[0]
	var idx=name.substr(name.indexOf("["),name.indexOf("]"));
	if(sid.split(".")[0].indexOf("sorActivities") >= 0)
	{
		//to support view close option
		if(sorMsArray[idx])
			{
		document.getElementById(sid.split(".")[0]+".mstd").innerHTML=sorMsArray[idx];
		if(sorMsArray[idx].length==0)
			document.getElementById(sid.split(".")[0]+".mspresent").value="0";
			}
			
	}else
	{
		if(nonSorMsArray[idx])
			{
		document.getElementById(sid.split(".")[0]+".mstd").innerHTML=nonSorMsArray[idx];
		if(nonSorMsArray[idx].length==0)
			document.getElementById(sid.split(".")[0]+".mspresent").value="0";
			}
	}

	document.getElementById(sid.split(".")[0]+".msopen").value="0";
	
	var mstr=document.getElementById(sid.split(".")[0]+".mstr");
	$(mstr).remove();

	 
});



$(document).on('change','.runtime-update',function (e) {


	if($(this).is("input"))
	{
		if($(this).val()==0)
			{
			bootbox.alert("Zero is not allowed");
			$(this).val('');
			}
			$(this).attr('value', $(this).val());
		

	}
	else if($(this).is("select"))
	{
		if($(this).val()=='A')
			{
			$(this).find('option[value="D"]').removeAttr('selected');	
			//console.log('dropdown value change triggered!');
			$(this).find('option[value="A"]').attr('selected', 'selected');
			}else
				{
				$(this).find('option[value="A"]').removeAttr('selected');	
				//console.log('dropdown value change triggered!');
				$(this).find('option[value="D"]').attr('selected', 'selected');	
				}
		
	}
	else if($(this).is("textarea"))
	{
		//console.log('dropdown value change triggered!');
		$(this).html($(this).val());
	}
	if($(this).attr('id').indexOf("quantity")>=0)
		findNet(this);
	else
	findTotal(this);
	//$(this).closest('tr').hide();
});

/*$(document).on('click','.ms-submit',function () {

	var sid=$(this).attr("id");
	var mscontent="<tr id=\""+sid.split(".")[0]+".mstr\">";
});*/

$(document).on('click','.ms-submit',function () {

	var sid=$(this).attr("id");
	var mscontent="<tr id=\""+sid.split(".")[0]+".mstr\">";

	var net=eval(document.getElementById(sid.split(".")[0]+".msnet").innerHTML);
	if(net==NaN ||net<=0)
	{
		bootbox.alert("Net Quantity should be greater than 0");
		return false;
	}
	var qobj1=document.getElementById(sid.split(".")[0]+".measurementSheetList[0].no");
	if(!validateMsheet(qobj1))
	{
		return false;
	}

	document.getElementsByName(sid.split(".")[0]+".quantity")[0].value=document.getElementById(sid.split(".")[0]+".msnet").innerHTML;
	mscontent=document.getElementById(sid.split(".")[0]+".mstr").innerHTML;
	document.getElementById(sid.split(".")[0]+".mstr")
	document.getElementById(sid.split(".")[0]+".mstd")
	document.getElementById(sid.split(".")[0]+".mstd").innerHTML=mscontent;
	document.getElementById(sid.split(".")[0]+".msopen").value="0";
	var mstr=document.getElementById(sid.split(".")[0]+".mstr");
	$(mstr).remove();
	var qobj=document.getElementsByName(sid.split(".")[0]+".quantity")[0];
	if(sid.split(".")[0].indexOf("sorActivities") >= 0)
	{
		calculateEstimateAmount(document.getElementsByName(sid.split(".")[0]+".quantity")[0]);
	}else
	{
		calculateNonSorEstimateAmount(document.getElementsByName(sid.split(".")[0]+".quantity")[0]);
	}
	$(qobj).attr("readonly","readonly");


})

function  deleteThisRow(obj) {
	var rIndex = getRow(obj).rowIndex;
	var tablename=$(obj).closest('table').attr('id');
	var tbl=document.getElementById( tablename);
	var rowcount=$(obj).closest('table').find('tr').length;
	//console.log(tbl);
	if(rowcount<=3) {
		
		var retVal = confirm("This action will remove complete Measurement Sheet for SOR/NonSOR. Do you want to continue ?");
		if( retVal == false )
		{
			return ;
		}
		else{
	   var sid=	tablename.split(".")[0];	
	   var mstr=document.getElementById(sid+".msopen").value=0;
	   var mstr=document.getElementById(sid+".mspresent").value=0;
	   var mstr=document.getElementById(sid+".mstd").innerHTML="";
	   document.getElementsByName(sid+".quantity")[0].value=0;
	   var quantity=document.getElementsByName(sid+".quantity")[0];
	   $(quantity).removeAttr("readonly");
	   var mstr=document.getElementById(sid+".mstr");
	   $(mstr).remove();
	   if(sid.indexOf("sorActivities") >= 0)
		{
			calculateEstimateAmount(document.getElementsByName(sid+".quantity")[0]);
		}else
		{
			calculateNonSorEstimateAmount(document.getElementsByName(sid+".quantity")[0]);
		}
		}
		return ;
	} else {
		tbl.deleteRow(rIndex);
	}
	reindex(tablename);
	findNet(tbl);  

}

function reindex(tableId)
{

	var idx=0;
	tbl=document.getElementById(tableId);
	////console.log($(tbl).html());

	$(tbl).find("tbody tr").each(function(e) {

		//console.log('for loop');
		$(this).find("input,select,textarea").each(function() {
			var classval = jQuery(this).attr('class');	
			 
			if(classval && classval.indexOf("spanslno") > -1) {
				jQuery(this).val(idx+1);
				$(this).attr('value', $(this).val());
			}

			$(this).attr({
				'name' : function(_, name) {
					if(name)
						return name.replace(/measurementSheetList\[.\]/g, "measurementSheetList["+idx+"]");
				},
				'id' : function(_, id) {
					if(id)
						return id.replace(/measurementSheetList\[.\]/g, "measurementSheetList["+idx+"]");
				},
				'data-idx' : function(_, dataIdx) {
					return idx;
				}
			});

		});
		idx++;
	});


}

$(document).on('click','.delete-ms',function () {

	$(this).closest('tr').hide();
});

$(document).on('click','.reset-ms',function () {

	var len=$(this).closest('table').find('tr').length;
	var msrowname= $(this).closest('table').attr('id');
	var tbl=document.getElementById(msrowname);
	var sid=msrowname.split(".")[0];
	var newrow= document.getElementById("templatesorActivities[0].mstr").innerHTML;

	newrow=  newrow.replace(/msrowtemplate/g,'msrow'+sid);
	newrow=  newrow.replace(/templatesorActivities\[0\]/g,sid);
	document.getElementById(sid+".mstr").innerHTML=newrow;
	
	
});

$(document).on('click','.add-msrow',function () {
	var len=$(this).closest('table').find('tr').length;
	var msrowname= $(this).closest('table').attr('id');
	
 

	//var msrowname1=	msrowname.id;
	len=len-2;
	var msrownameid=msrowname.split(".")[0];
	var rep='measurementSheetList\['+len+'\]';

	//console.log(len+'===='+rep);
	var $newrow= "<tr>"+$('#msrowtemplate').html()+"</tr>";
	$newrow=  $newrow.replace(/templatesorActivities\[0\]/g,msrownameid);
	$newrow=  $newrow.replace(/measurementSheetList\[0\]/g,rep);
	$newrow=$newrow.replace('value="1"','value="'+(len+1)+'"');
	////console.log($newrow)
	$(this).closest('tr').before($newrow);

	patternvalidation();


})

function findTotal(obj)
{

	var name=obj.name.split(".");
	var lengthname=name[0]+'.'+name[1]+'.length';
	var no1,depthOrHeight1,width1,length1;
	var lent=$('input[id="'+lengthname+'"]');
	//console.log($(lent).attr('value'));
	var length=$(lent).attr('value');
	var no=$('input[id="'+name[0]+'.'+name[1]+'.no'+'"]').attr('value');
	var depthOrHeight=$('input[id="'+name[0]+'.'+name[1]+'.depthOrHeight'+'"]').attr('value');
	var width=$('input[id="'+name[0]+'.'+name[1]+'.width'+'"]').attr('value');

	if(isEmpty(length) && isEmpty(no) && isEmpty(depthOrHeight)  && isEmpty(width))
		$('input[id="'+name[0]+'.'+name[1]+'.quantity'+'"]').attr('value',0);
	else {
		if (length === undefined || length == '' || length == 0)
			length = 1;
		if (no === undefined || no == '' || no == 0)
			no = 1;
		if (depthOrHeight === undefined || depthOrHeight == '' || depthOrHeight == 0)
			depthOrHeight = 1;
		if (width === undefined || width == '' || width == 0)
			width = 1;
		var net=length * no * width * depthOrHeight;
		var x=net+"";
		var y=x.split(".");
		if(y.length>1)
		  if(y[1].length>4)
			  net=net.toFixed(4);  
		
		document.getElementById(name[0]+'.'+name[1]+'.quantity').value=net;
		$('input[id="'+name[0]+'.'+name[1]+'.quantity'+'"]').attr('value',net);

	}
	var netObj=document.getElementById(name[0]+'.'+name[1]+'.quantity');
	$(netObj).attr('value', document.getElementById(name[0] + '.' + name[1] + '.quantity').value);
	var len=$(obj).closest('table').find('tbody').children.length;
	//console.log(len);
	findNet(netObj);
}

function isEmpty(str)
{
	if(!str)
	{
		return true;
	}
	else if(!str.trim()) {
		return true;
	}

	return false;
}


function findNet(obj)
{
	var len=$(obj).closest('table').find('tr').length;


	var name=obj.id.split(".");

	var sum=0;
	for(var i=0;i<len-2;i++)
	{
		var qname=name[0]+'.measurementSheetList['+i+'].quantity';
		var quantity=eval(document.getElementById(qname).value);
		var oname=name[0]+'.measurementSheetList['+i+'].identifier';
		var operationObj=document.getElementById(oname);
		var operation=operationObj.options[operationObj.selectedIndex].value;
		//console.log(quantity+"---"+operation);
		if(quantity===undefined)
			quantity=0;
		if(quantity==NaN)
			quantity=0;
		if(quantity=='')
			quantity=0;
		if(operation=='A')
			sum=sum+quantity;
		else
			sum=sum-quantity;
	}
	//var fname=obj.name.split(".");
	var netName=name[0]+'.msnet';
	var x=sum+"";
	var y=x.split(".");
	if(y.length>1)
	  if(y[1].length>4)
		  sum=sum.toFixed(4);  
	
	//sum=parseFloat(sum).toFixed(4);
	//console.log(document.getElementById(netName).innerHTML);
	document.getElementById(netName).innerHTML=sum;
	return true;


}

function closeAllViewmsheet()
{
	var open=false;
	$('.classmsopen').each(function (index)
			{

		if($( this ).val()==1)
		{
			var sid=$( this ).attr('id');
			var mscontent="<tr id=\""+sid.split(".")[0]+".mstr\">";
			mscontent=document.getElementById(sid.split(".")[0]+".mstr").innerHTML;
			document.getElementById(sid.split(".")[0]+".mstd").innerHTML=mscontent;
			document.getElementById(sid.split(".")[0]+".msopen").value="0";
			var mstr=document.getElementById(sid.split(".")[0]+".mstr");
			$(mstr).remove(); 
		}
			});
	
}
function openAllViewmsheet()
{
	var open=false;
	$('.classmsopen').each(function (index)
			{

		if($( this ).val()==0)
			
		{
			var sid=$( this ).attr('id');
			var	sortable=sid.split(".")[0];
			if(document.getElementById(sid.split(".")[0]+".mspresent").value==1)
			{
				
				var   mscontent=document.getElementById(sid.replace("msopen","mstd")).innerHTML;

				if(mscontent!='')
				{
					if(mscontent.indexOf(headstart) >=0)
					{
						var head= mscontent.substring(mscontent.indexOf(headstart),mscontent.indexOf(headend));
						var tail= mscontent.substring(mscontent.indexOf(tailstart),mscontent.indexOf(tailend));
						mscontent= mscontent.replace(head,"");
						mscontent= mscontent.replace(tail,"");
					}

					var curRow = $(this).closest('tr');
					var k= "<tr id=\""+sortable+".mstr\" class='msheet-tr' ><td colspan=\"9\">";
					mscontent=k+mscontent+"</td></tr>";
					curRow.after(mscontent);
					document.getElementById(sid.replace("msopen","mstd")).innerHTML="";
					$( this ).val(1);
					var idx=sortable.substr(sortable.indexOf("["),sortable.indexOf("]"));
					if(sortable.indexOf("sorActivities") >= 0)
					{
						sorMsArray[idx]=mscontent;
					}
					else
					{
						nonSorMsArray[idx]=mscontent;
					}
				}

			}
		}

			});
	return open;	
}

function openAllmsheet()
{
	var open=false;
	$('.classmsopen').each(function (index)
			{

		if($( this ).val()==0)
			
		{
			var sid=$( this ).attr('id');
			var	sortable=sid.split(".")[0];
			if(document.getElementById(sid.split(".")[0]+".mspresent").value==1)
			{
				
				var   mscontent=document.getElementById(sid.replace("msopen","mstd")).innerHTML;

				if(mscontent!='')
				{
					if(mscontent.indexOf(headstart) >=0)
					{
						var head= mscontent.substring(mscontent.indexOf(headstart),mscontent.indexOf(headend));
						var tail= mscontent.substring(mscontent.indexOf(tailstart),mscontent.indexOf(tailend));
						mscontent= mscontent.replace(head,"");
						mscontent= mscontent.replace(tail,"");
					}

					var curRow = $(this).closest('tr');
					var k= "<tr id=\""+sortable+".mstr\" class='msheet-tr'><td colspan=\"9\">";
					mscontent=k+mscontent+"</td></tr>";
					curRow.after(mscontent);
					document.getElementById(sid.replace("msopen","mstd")).innerHTML="";
					$( this ).val(1);
					var idx=sortable.substr(sortable.indexOf("["),sortable.indexOf("]"));
					
					if(sortable.indexOf("sorActivities") >= 0)
					{
						sorMsArray[idx]=mscontent;
					}
					else
					{
						nonSorMsArray[idx]=mscontent;
					}

					
				}

			}
		}

			});
	return open;	
}

function closeAllmsheet()
{
	var retVal = confirm("This will validate and update quantities . Do you want to continue?");
	if( retVal == false )
	{
		return ;
	}
	else{


		var open=false;
		$('.classmsopen').each(function (index)
				{

			if($( this ).val()==1)
			{

				var sid=$( this ).attr('id');
				var qobj1=document.getElementById(sid.split(".")[0]+".measurementSheetList[0].no");
				if(!validateMsheet(qobj1))
				{
					return false;
				}
				
				var mscontent="<tr id=\""+sid.split(".")[0]+".mstr\">";
				document.getElementsByName(sid.split(".")[0]+".quantity")[0].value=document.getElementById(sid.split(".")[0]+".msnet").innerHTML;

				mscontent=document.getElementById(sid.split(".")[0]+".mstr").innerHTML;

				
				document.getElementById(sid.split(".")[0]+".mstd").innerHTML=mscontent;
				document.getElementById(sid.split(".")[0]+".msopen").value="0";
				var mstr=document.getElementById(sid.split(".")[0]+".mstr");
				$(mstr).remove(); 
				var qobj=document.getElementsByName(sid.split(".")[0]+".quantity")[0];
				$(qobj).attr("readonly","readonly");
				if(sid.split(".")[0].indexOf("sorActivities") >= 0)
				{
					calculateEstimateAmount(document.getElementsByName(sid.split(".")[0]+".quantity")[0]);
				}else
				{
					calculateNonSorEstimateAmount(document.getElementsByName(sid.split(".")[0]+".quantity")[0]);
				}

			    }
				});
	}
	//console.log("mssheet open:"+open);
	return open;

}



function ismsheetOpen()
{
	var open=false;
	$('.classmsopen').each(function (index)
			{

		if($( this ).val()==1)
			open=true
			});
	//console.log("mssheet open:"+open);
	return open;
}

function validateMsheet(obj)
{

	var len=$(obj).closest('table').find('tr').length;


	var name=obj.id.split(".");

	var sum=0;
	for(var i=0;i<len-2;i++)
	{
		var qname=name[0]+'.measurementSheetList['+i+'].quantity';
		var no=eval(document.getElementById(name[0]+'.measurementSheetList['+i+'].no').value);
		var lent=eval(document.getElementById(name[0]+'.measurementSheetList['+i+'].length').value);
		var width=eval(document.getElementById(name[0]+'.measurementSheetList['+i+'].width').value);
		var depthorheight=eval(document.getElementById(name[0]+'.measurementSheetList['+i+'].depthOrHeight').value);
		var qunatity=eval(document.getElementById(name[0]+'.measurementSheetList['+i+'].quantity').value);

		if((no===undefined ||no==NaN) && (width===undefined ||width==NaN) && (lent===undefined ||lent==NaN) 
				&&(depthorheight===undefined ||depthorheight==NaN) &&  (qunatity===undefined ||qunatity==NaN))
		{
			bootbox.alert("Empty row is not allowed. Please delete the empty row or Enter Quantity");
			return false;
		}
		if(qunatity==NaN || qunatity<=0)
		{
			bootbox.alert("Zero is not allowed in Quantity");
			return false;
		}
			


	}
	return true;

}

function clearOverHeads() {
	var overheadTableLength = jQuery('#overheadTable tr').length-1;
	for (var i = 1; i <= overheadTableLength; i++) {
		index=i-1;
		if(overheadTableLength == i){
			document.getElementById('tempOverheadValues[0].id').value = "";
			document.getElementById('tempOverheadValues[0].name').value = "";
			document.getElementById('tempOverheadValues[0].overhead.id').value = "";
			document.getElementById('tempOverheadValues[0].percentage').value = "";
			document.getElementById('tempOverheadValues[0].amount').value = "";
		}else{
			var tbl=document.getElementById('overheadTable');
			var objects = $('.delete-row');
			deleteRow('overheadTable', objects[i]);
		}
	}
}

function deleteDeductionRow(obj) {
	
	var id = $(getRow(obj)).children('td:first').children('input:first').val();
    if(!$("#removedDeductionIds").val()==""){
		$("#removedDeductionIds").val($("#removedDeductionIds").val()+",");
	}
    $("#removedDeductionIds").val($("#removedDeductionIds").val()+id);
    
    var rIndex = getRow(obj).rowIndex;
    
    //To get all the deleted rows id
    var aIndex = rIndex - 1;

    var tbl=document.getElementById('deductionTable');	
	var rowcount=$("#deductionTable tbody tr").length;
	if(rowcount<=1) {
		bootbox.alert("This row can not be deleted");
		return false;
	} else {
	tbl.deleteRow(rIndex);
	
	var idx= 0;
	//regenerate index existing inputs in table row
	jQuery("#deductionTable tbody tr").each(function() {
	
			jQuery(this).find("input, select, textarea, errors, span, input:hidden").each(function() {
				var classval = jQuery(this).attr('class');
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
		    });
			
			idx++;
	});
	calculateDeductionTotalAmount();
	calculateEstimateValue();

	return true;
	}
}


function calculateDeductionTotalAmount(){
	var resultLength = jQuery('#deductionTable tr').length-1;
	var index;
	var total = 0;
	for (var i = 0; i < resultLength; i++) {
		index = i;
		var deductionAmount = document.getElementById('tempDeductionValues[' + i + '].amount').value;
		if(deductionAmount==null || deductionAmount=="")
			deductionAmount = 0;
		total = eval(total) + eval(deductionAmount);
	}

	$("#deductionTotalAmount").html(parseFloat(total).toFixed(2));
	calculateEstimateValue();
}

$('#addDeductionRow').click(function() { 
	var flag=false;
	var resultLength = jQuery('#deductionTable tr').length-1;
	for (var i = 0; i < resultLength; i++) {
		var deductionAmount = document.getElementById('tempDeductionValues[' + i + '].amount').value;
		var deductionAccountCode = document.getElementById('tempDeductionValues[' + i + '].accountCode').value;
		if((deductionAccountCode == '' || deductionAccountCode == null)){
			bootbox.alert($('#msgAccountCode').val());
			return false;
		}
		if((deductionAmount == '' || parseFloat(deductionAccountCode) == 0)){
			bootbox.alert($('#msgAmountZero').val());
			return false;
		}
				
		if(parseFloat(deductionAmount) == 0){
			bootbox.alert($('#msgAmountZero').val());
			return false;
		}
		flag = true;
	}
	if(flag = true) {
		$('.deductionAccountCode').typeahead('destroy');
		addRow('deductionTable','deductionRow');
		deductionAccountCodeAndHead_initialize();
	}
		
});

function deductionAccountCodeAndHead_initialize() {
	 var custom = new Bloodhound({
	    datumTokenizer: function(d) { return d.tokens; },
	    queryTokenizer: Bloodhound.tokenizers.whitespace,
		   remote: {
	            url: '/egworks/abstractestimate/ajaxdeduction-coa?searchQuery=%QUERY',
	            filter: function (data) {
	                return $.map(data, function (ct) {
	                    return {
	                        id: ct.id,
	                        name: ct.name,
	                        glcode: ct.glcode,
	                        glcodesearch: ct.glcode+' ~ '+ct.name
	                    };
	                });
	            }
	        }
   });

   custom.initialize();

   $('.deductionAccountCode').typeahead({
   	hint : true,
		highlight : true,
		minLength : 3
		
	}, {		    
         displayKey: 'glcodesearch',
         source: custom.ttAdapter()
   }).on('typeahead:selected typeahead:autocompleted', function (event, data) {
   	$(this).parents("tr:first").find('.deductionAccountHead').val(data.name);
   	$(this).parents("tr:first").find('.deductionid').val(data.id);    
   });
}

function getDeductionAmountByPercentage(){
	var resultLength = jQuery('#deductionTable tr').length-1;
	var workValue = $('#workValue').val();
	var index;
	for (var i = 0; i < resultLength; i++) {
		var deductionPercentage = document.getElementById('tempDeductionValues[' + i + '].percentage').value;
		var deductionAccountCode = document.getElementById('tempDeductionValues[' + i + '].accountCode').value;
		if(deductionPercentage != undefined && parseFloat(deductionPercentage) != 0 && deductionPercentage != '')
			$('#deductionTable tbody tr:eq('+i+')').find('.deductionAmount').val(((workValue*deductionPercentage)/100).toFixed(2));
		if(parseFloat(workValue) == 0){
			$('#deductionTable tbody tr:eq('+i+')').find('.deductionPercentage').val('');
			$('#deductionTable tbody tr:eq('+i+')').find('.deductionAmount').val('');
		}
		if(deductionAccountCode == '' && parseFloat(deductionPercentage) != 0 && deductionPercentage != ''){
			$('#deductionTable tbody tr:eq('+i+')').find('.deductionAmount').val('');
			bootbox.alert($('#msgAccountCode').val());
			$('#deductionTable tbody tr:eq('+i+')').find('.deductionPercentage').val('');
		}
		
	}
	calculateDeductionTotalAmount();
	calculateEstimateValue();
		
}

var copiedEstimateNumber = new Bloodhound({
	datumTokenizer: function (datum) {
		return Bloodhound.tokenizers.whitespace(datum.value);
	},
	queryTokenizer: Bloodhound.tokenizers.whitespace,
	remote: {
		url: '/egworks/abstractestimate/ajaxestimatenumbers-estimatetocopy?code=%QUERY', 
		filter: function (data) {
			var estimateDate = $('#estimateDate').val();
			if(estimateDate == "" || estimateDate == null){
				bootbox.alert($('#msgestimatedate').val());
				return false;
			}
			return $.map(data, function (ct) {
				for (var key in ct) {
					  if (ct.hasOwnProperty(key)) {
					    return {
					    	code: ct[key],
					    	id: key
					    	};
					  }
					}
			});
		}
	}
});

copiedEstimateNumber.initialize();
var copiedEstimateNumber_typeahead = $('#copiedEstimateNumber').typeahead({
	hint : true,
	highlight : true,
	minLength : 3
}, {
	displayKey : 'code',
	source : copiedEstimateNumber.ttAdapter()
}).on('typeahead:selected typeahead:autocompleted', function(event, data){  
	var sorHiddenRowCount = $("#tblsor tbody tr:hidden[id='sorRow']").length;
	var nonSorHiddenRowCount = $("#tblNonSor tbody tr:hidden[id='nonSorRow']").length;
	if(sorHiddenRowCount !=1 || nonSorHiddenRowCount!=1){
		var ans=confirm($("#copyEstimateConfirmMsg").val());	
		if(ans) {
			confirmOverWriteActivities(data.id);
			$('#copiedEstimateNumber').typeahead('val','');
		}
		else {
			return false;		
		}
	} else {
		confirmOverWriteActivities(data.id);
		$('#copiedEstimateNumber').typeahead('val','');
	}
});

function isResetRequired() {
	var sorHiddenRowCount = $("#tblsor tbody tr:hidden[id='sorRow']").length;
	var nonSorHiddenRowCount = $("#tblNonSor tbody tr:hidden[id='nonSorRow']").length;
	if(sorHiddenRowCount !=1 || nonSorHiddenRowCount!=1)
		return true;
	else
		return false;
}


function confirmOverWriteActivities(id) {
	$("#copiedEstimateId").val(id);
	var copiedEstimateId = id;
	var sorHiddenRowCount = $("#tblsor tbody tr:hidden[id='sorRow']").length;
	var nonSorHiddenRowCount = $("#tblNonSor tbody tr:hidden[id='nonSorRow']").length;
	if(copiedEstimateId!="" && (sorHiddenRowCount !=1 || nonSorHiddenRowCount!=1)){ 
		clearActivities();
		getActivitiesForEstimate(copiedEstimateId);
	}else{
		getActivitiesForEstimate(copiedEstimateId);
	}
}
function getActivitiesForEstimate(id){
	var estimateDate = $('#estimateDate').val();
	var nonSorCheck  = false;
	$.ajax({
		url: "/egworks/abstractestimate/ajaxactivities-estimatetocopy?id="+ id + "&estimateDate="+estimateDate, 
		type: "GET",
		dataType: "json",
		success: function (data) {
			populateActivities(data, nonSorCheck);
		}, 
		error: function (response) {
		}
	});

	resetIndexes();
	calculateEstimateAmountTotal();
	calculateVatAmountTotal();
	total();
	calculateNonSorEstimateAmountTotal();
	calculateNonSorVatAmountTotal();
	nonSorTotal();
}

$('#searchEstimates').click(function() {
	var typeOfWork =$('#parentCategory').val();
	window.open("/egworks/abstractestimate/searchestimateform?typeOfWork="+typeOfWork,'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
});

function showHideApprovalDetails(workFlowAction) {
	var isValidAction=true;
	var estimateValue = $('#estimateValueTotal').html();

	$.ajax({
		url: "/egworks/abstractestimate/ajax-showhideappravaldetails",     
		type: "GET",
		async: false,
		data: {
			amountRule : estimateValue,
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

function getFunctionsByFundAndDepartment() {
	if ($('#fund').val() === '' || $('#executingDepartment').val() === '') {
		   $('#function').empty();
		   $('#function').append($('<option>').text('Select from below').attr('value', ''));
			return;
			} else {
				$.ajax({
					method : "GET",
					url : "/egworks/lineestimate/getfunctionsbyfundidanddepartmentid",
					data : {
						fundId : $('#fund').val(),
						departmentId : $('#executingDepartment').val()
					},
					async : true
				}).done(
						function(response) {
							$('#function').empty();
							$('#function').append($("<option value=''>Select from below</option>"));
							var output = '<option value="">Select from below</option>';
							$.each(response, function(index, value) {
								var selected="";
								if($functionId)
								{
									if($functionId==value.id)
									{
										selected="selected";
									}
								}
								$('#function').append($('<option '+ selected +'>').text(value.code + ' - ' + value.name).attr('value', value.id));
							});
				});
			}
}

function getBudgetHeads() {
	if($('#function').val() != '')
		$functionId = $('#function').val();
	 if ($('#fund').val() === '' || $('#executingDepartment').val() === '' || ($('#function').val() === '' && $functionId == 0) || $('#natureOfWork').val() === '') {
		   $('#budgethead').empty();
		   $('#budgethead').append($('<option>').text('Select from below').attr('value', ''));
			return;
			} else {
			$.ajax({
				type: "GET",
				url: "/egworks/lineestimate/getbudgethead",
				cache: true,
				dataType: "json",
				data:{
					'fundId' : $('#fund').val(),
					'functionId' : $functionId,
					'departmentId' : $('#executingDepartment').val(),
					'natureOfWorkId' : $('#natureOfWork').val()
					
					}	
			}).done(function(value) {
				console.log(value);
				$('#budgethead').empty();
				$('#budgethead').append($("<option value=''>Select from below</option>"));
				$.each(value, function(index, val) {
					var selected="";
					if($budgetHeadId)
					{
						if($budgetHeadId==val.id)
						{
							selected="selected";
						}
					}
				     $('#budgethead').append($('<option '+ selected +'>').text(val.name).attr('value', val.id));
				});
			});
		}
}