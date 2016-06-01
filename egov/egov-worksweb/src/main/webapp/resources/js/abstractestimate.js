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

$subTypeOfWorkId = 0;
$(document).ready(function(){
	
	$subTypeOfWorkId = $('#subTypeOfWorkValue').val();
	getAbstractEstimateDate();
	var hint='<a href="#" class="hintanchor" title="@fulldescription@"><i class="fa fa-question-circle" aria-hidden="true"></i></a>'
	
	$('#sorSearch').blur(function() {
		$('#sorSearch').val('');
	});
	
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
	
	$('#addnonSorRow').click(function() {
		var hiddenRowCount = $("#tblNonSor tbody tr:hidden[id='nonSorRow']").length;
		if(hiddenRowCount == 0) {
			addNonSor();
		} else {
			$('#nonSorDesc_0').attr('required', 'required');
			$('#nonSorUom_0').attr('required', 'required');
			$('#nonSorRate_0').attr('required', 'required');
			$('#nonSorQuantity_0').attr('required', 'required');
			$('.nonSorRate').val('');
			$('.nonSorQuantity').val('');
			$('.nonSorServiceTaxPerc').val('');
			$('#nonSorMessage').attr('hidden', 'true');
			$('#nonSorRow').removeAttr('hidden');
		}
		resetIndexes();
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
	        		if(scheduleCategories == null)
	        			bootbox.alert($('#msgschedulecategory').val());
	        	    return url + query + '&scheduleCategories=' + scheduleCategories;
	        	},
	        filter: function (data) {
	            return $.map(data, function (ct) {
	                return {
	                	id: ct.id,
	                    code: ct.code,
	                    description: ct.description,
	                    uom: ct.uom.uom,
	                    rate: ct.sorRates[0].rate.value,
	                    summary: ct.summary,
	                    displayResult: ct.code+' : '+ct.summary
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
			var hiddenRowCount = $("#tblsor tbody tr:hidden[id='sorRow']").length;
			if(hiddenRowCount == 0) {
				addSor();
			} else {
				$('#quantity_0').val('');
				$('#vat_0').val('');
				$('#message').attr('hidden', 'true');;
				$('#sorRow').removeAttr('hidden');
			}
			var rowcount = $("#tblsor tbody tr").length;
			var key = parseInt(rowcount) - 2;
			$.each(data, function(id, val) {
				if(id == "id")
					$('#' + id + "_" + key).val(val);
				if(id == 'description') {
					$('.' + id + "_" + key).html(hint.replace(/@fulldescription@/g, val));
				}
					
				else
					$('.' + id + "_" + key).html(val);
			});
		}
		$('#sorSearch').val('');
		resetIndexes();
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
                        name: ct.name,
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
		minLength : 3
	}, {
		displayKey : 'name',
		source : ward.ttAdapter(),
	});
	
	typeaheadWithEventsHandling(ward_typeahead,
	'#ward');
});

$overheadRowCount = 0;
function addOverheadRow() { 

	if (document.getElementById('overheadRow') != null) {
			// get Next Row Index to Generate
			var nextIdx = 0;
			if($overheadRowCount == 0 || $overheadRowCount == '')
				nextIdx = jQuery("#overheadTable tbody tr").length;
			else
				nextIdx = $overheadRowCount++;
			//nextIdx++;
			// Generate all textboxes Id and name with new index
			jQuery("#overheadRow").clone().find("input,select, errors").each(
			function() {	
				if (jQuery(this).data('server')) {
					jQuery(this).removeAttr('data-server');
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
							'data-idx' : function(_,dataIdx)
							{
								return nextIdx;
							}
						});
	
					// if element is static attribute hold values for next row, otherwise it will be reset
					if (!jQuery(this).data('static')) {
						jQuery(this).val('');
					}
	
			}).end().appendTo("#overheadTable tbody");		
			
		}


}

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

function deleteOverheadRow(obj) {
    var rIndex = getRow(obj).rowIndex;
    //calculating Overhead TotalAmount
    var total = document.getElementById('overheadTotalAmount').value;
    if(total==null || total=="")
    	total = 0;
    var amount = document.getElementById('overheadValues['+ (rIndex-1) + '].amount').value;
    if(amount==null || amount=="")
    	amount = 0;
    total = eval(total) - eval(amount);
    document.getElementById('overheadTotalAmount').value = total;
    
    var id = jQuery(getRow(obj)).children('td:first').children('input:first').val();
    //To get all the deleted rows id
    var aIndex = rIndex - 1;
	var tbl=document.getElementById('overheadTable');	
	var rowcount=jQuery("#overheadTable tbody tr").length;
    if(rowcount<=1) {
		alert("This row can not be deleted");
		return false;
	} else {
		tbl.deleteRow(rIndex);
		//starting index for table fields
		var idx=parseInt($overheadRowCount);
		//regenerate index existing inputs in table row
		jQuery("#overheadTable tbody tr").each(function() {
		
			hiddenElem=jQuery(this).find("input:hidden");
			
			if(!jQuery(hiddenElem).val())
			{
				jQuery(this).find("input,select,errors").each(function() {
					jQuery(this).attr({
					      'name': function(_, name) {
					    	  if(!(jQuery(this).attr('name')===undefined))
					    		  return name.replace(/\[.\]/g, '['+ idx +']'); 
					      },
					      'id': function(_, id) {
					    	  if(!(jQuery(this).attr('id')===undefined))
					    		  return id.replace(/\[.\]/g, '['+ idx +']'); 
					      },
						  'data-idx' : function(_,dataIdx)
						  {
							  return idx;
						  }
					   });
			    });
				idx++;
			}
		});
		return true;
	}
    calculateOverheadTotalAmount();
}

function recalculateOverheads(){
	var resultLength = jQuery('#overheadTable tr').length-1;
	var index;
	for (var i = 1; i <= resultLength; i++) {
		index = i;
		var percentage = document.getElementById('actionOverheadValues['
				+ index + '].percentage').value;
		var amount = document.getElementById('actionOverheadValues['
				+ index + '].amount');
		 if(percentage!=""){
			 amount.value = roundTo((getNumericValueFromInnerHTML("grandTotal")+getNumericValueFromInnerHTML("nonSorGrandTotal")) * getNumber(percentage)/100);
		 }else{
			 amount.value = 0.0;
		 }
	}
	  recalculateOverHeadTotal(); 
}
function recalculateOverHeadTotal(){
	var resultLength = jQuery('#overheadTable tr').length-1;
	var index;
	 var total=0;
	for (var i = 1; i <= resultLength; i++) {
		index = i;
		var amount = document.getElementById('actionOverheadValues['
				+ index + '].amount');
		 total=roundTo((eval(amount.value )+ eval(total))); 
	}
	 dom.get("overHeadTotalAmnt").value=total;
	 document.getElementById("estimateValue").value=roundTo(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+eval(document.getElementById("overHeadTotalAmnt").value));
		  	
}
function calculateOverHeadTotal(elem){
	var index=elem.id.substring(elem.id.indexOf('[')+1, elem.id.indexOf(']'));
	var oldOverheadAmount=document.getElementById('actionOverheadValues['
			+ index + '].amount').value;
	var overHeadTotalAmnt = document.getElementById('overHeadTotalAmnt').value;
	if(overHeadTotalAmnt!=0) {
		dom.get("overHeadTotalAmnt").value=roundTo(getNumericValueFromInnerHTML("overHeadTotalAmnt") -oldOverheadAmount +getNumber(amount.value));
	}
	else {
		dom.get("overHeadTotalAmnt").innerHTML=roundTo(getNumericValueFromInnerHTML("overHeadTotalAmnt") +getNumber(dom.get("amount"+record.getId()).value));
	} 
	 document.getElementById("estimateValue").value=roundTo(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+eval(document.getElementById("overHeadTotalAmnt").innerHTML));
}



function getPercentageOrLumpsumByOverhead(overhead) {
	var objName = overhead.name;
	var index =objName.substring(objName.indexOf("[")+1,objName.indexOf("]")); 
	if (overhead.value != '') {
		$.ajax({
			url : '/egworks/abstractestimate/getpercentageorlumpsumbyoverheadid',
			type : "get",
			data : {
				overheadId : overhead.value
			},
			success : function(data, textStatus, jqXHR) {
				if(data.percentage>0){
					document.getElementById('overheadValues['+ index + '].percentage').value = data.percentage;
					document.getElementById('overheadValues['+ index + '].amount').value = 0;
				}else{
					document.getElementById('overheadValues['+ index + '].percentage').value = "";
					document.getElementById('overheadValues['+ index + '].amount').value = data.lumpsumAmount;
				}
				calculateOverheadTotalAmount();
			},
			error : function(jqXHR, textStatus, errorThrown) {
						bootbox.alert("Error while get Percentage or Lumpsum By Overhead ");
			}
		});
	}else{
		document.getElementById('overheadValues['+ index + '].percentage').value = "";
		document.getElementById('overheadValues['+ index + '].amount').value = 0;
		calculateOverheadTotalAmount();
	}
	
}

function calculateOverheadTotalAmount(){
	var resultLength = jQuery('#overheadTable tr').length-1;
	var index;
	var total = 0;
	for (var i = 0; i < resultLength; i++) {
		index = i;
		var amount = document.getElementById('overheadValues['
				+ index + '].amount').value;
		if(amount==null || amount=="")
			amount = 0;
		total = eval(total) + eval(amount);
	}
	
	document.getElementById('overheadTotalAmount').value = total;
}

function addMultiyearEstimate() {
	var tbl = document.getElementById('multiYeaeEstimateTbl');
    var rowO=tbl.rows.length;
    if(document.getElementById('yearEstimateRow') != null)
	{
    		//get Next Row Index to Generate
    		var nextIdx = tbl.rows.length-1;
    		
    		//validate status variable for exiting function
    		var isValid=1;//for default have success value 0  
    		
    		//validate existing rows in table
    		jQuery("#multiYeaeEstimateTbl tr:not(:first)").find('input, select').each(function(){
    			if((jQuery(this).data('optional') === 0) && (!jQuery(this).val()))
    			{
    				jQuery(this).focus();
    				console.log('called =>' + jQuery(this).attr('id'));
    				bootbox.alert(jQuery(this).data('errormsg'));
    				isValid=0;//set validation failure
    				return false;
    			}
    		});
    		
    		
    		// Generate all textboxes Id and name with new index
			jQuery("#yearEstimateRow").clone().find("input, select").each(function() {
				     
					jQuery(this).attr({
				      'id': function(_, id) { 
				    	  return id.replace('[0]', '['+ nextIdx +']'); 
				       },
				      'name': function(_, name) { 
				    	  return name.replace('[0]', '['+ nextIdx +']'); 
				      }
				    }).val('');
					
					 
		    }).end().appendTo("#multiYeaeEstimateTbl");
			
	}
    
}

function deleteMultiYearEstimate(obj) {
	
	rIndex = getRow(obj).rowIndex;
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
		jQuery("#multiYeaeEstimateTbl tr:not(:first)").each(function() {
			jQuery(this).find("input, select").each(function() {
			   jQuery(this).attr({
			      'id': function(_, id) {  
			    	  return id.replace(/\[.\]/g, '['+ idx +']'); 
			       },
			      'name': function(_, name) {
			    	  return name.replace(/\[.\]/g, '['+ idx +']'); 
			      },
			   });
		
		    });
			
		
			idx++;
		});
		
		return true;
	}	

}

function addSor() {
	var rowcount = $("#tblsor tbody tr").length;
	if (rowcount < 31) {
		if (document.getElementById('sorRow') != null) {
			// get Next Row Index to Generate
			var nextIdx = $("#tblsor tbody tr").length - 1;

			// Generate all textboxes Id and name with new index
			$("#sorRow").clone().find("input, span").each(
					function() {

						if(!$(this).is('span')) {
							$(this).attr({
								'name' : function(_, name) {
									return name.replace(/\d+/,nextIdx);
								},
								'id' : function(_, id) {
									return id.replace(/\d+/, nextIdx);
								},
								'data-idx' : function(_,dataIdx)
								{
									return nextIdx;
								}
							});
						} else {
							$(this).attr({
								'class' : function(_, name) {
									return name.replace(/\d+/, nextIdx);
								}
							});
						}
					}).end().appendTo("#tblsor tbody");
			$('#quantity_' + nextIdx).val('');
			$('.amount_' + nextIdx).html('');
			$('#vat_' + nextIdx).val('');
			$('.vatAmount_' + nextIdx).html('');
			$('.total_' + nextIdx).html('');
			
			generateSorSno();
			
			resetIndexes();
			
		}
	} else {
		  bootbox.alert('limit reached!');
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
	
	var tbl=document.getElementById('tblsor');	
	var rowcount=$("#tblsor tbody tr").length;

	if(rowcount==2) {
		$('.sorhiddenid').val('');
		$('#quantity_0').val('');
		$('.amount_0').html('');
		$('#vat_0').val('');
		$('.vatAmount_0').html('');
		$('.total_0').html('');
		$('#sorRow').attr('hidden', 'true');;
		$('#message').removeAttr('hidden');
	} else {
		tbl.deleteRow(rIndex);
	}
	//starting index for table fields
	generateSorSno();
	
	resetIndexes();
	
	calculateEstimateAmountTotal();
	calculateVatAmountTotal();
	total();
	return true;
}

function calculateEstimateAmount(currentObj) {
	rowcount = getRow(currentObj).rowIndex - 2;
	var rate = parseFloat($('.rate_' + rowcount).html().trim());
	var amount = parseFloat($(currentObj).val() * rate).toFixed(2);
	var vatAmount = parseFloat(($('#vat_' + rowcount).val() * amount) / 100).toFixed(2);
	$('.amount_' + rowcount).html(amount);
	$('.vatAmount_' + rowcount).html(vatAmount);
	$('.total_' + rowcount).html(parseFloat(parseFloat(amount) + parseFloat(vatAmount)).toFixed(2));
	calculateEstimateAmountTotal();
	calculateVatAmountTotal();
	total();
}

function calculateVatAmount(currentObj) {
	rowcount = getRow(currentObj).rowIndex - 2;
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
			total = parseFloat(parseFloat(total) + parseFloat($(this).html().trim())).toFixed(2);
	});
	$('#sorEstimateTotal').html(total);
}

function calculateVatAmountTotal() {
	var total = 0;
	$('.vatAmt').each(function() {
		if($(this).html().trim() != "")
			total = parseFloat(parseFloat(total) + parseFloat($(this).html().trim())).toFixed(2);
	});
	$('#serviceVatAmtTotal').html(total);
}

function total() {
	var total = 0.0;
	$('.total').each(function() {
		if($(this).html().trim() != "")
			total = parseFloat(parseFloat(total) + parseFloat($(this).html().trim())).toFixed(2);
	});
	$('#sorTotal').html(total);
	calculateEstimateValue();
}

function addNonSor() {
	var rowcount = $("#tblNonSor tbody tr").length;
	if (rowcount < 31) {
		if (document.getElementById('nonSorRow') != null) {
			// get Next Row Index to Generate
			var nextIdx = $("#tblNonSor tbody tr").length - 1;

			// Generate all textboxes Id and name with new index
			$("#nonSorRow").clone().find("input, span, select").each(
					function() {

						if(!$(this).is('span')) {
							$(this).attr({
								'name' : function(_, name) {
									return name.replace(/\d+/,nextIdx);
								},
								'id' : function(_, id) {
									return id.replace(/\d+/, nextIdx);
								},
								'data-idx' : function(_,dataIdx)
								{
									return nextIdx;
								}
							});
						} else {
							$(this).attr({
								'class' : function(_, name) {
									return name.replace(/\d+/, nextIdx);
								}
							});
						}
					}).end().appendTo("#tblNonSor tbody");
			$('#nonSorId_' + nextIdx).val('');
			$('#nonSorDesc_' + nextIdx).val('');
			$('#nonSorUom_' + nextIdx).val('');
			$('#nonSorRate_' + nextIdx).val('');
			$('#nonSorQuantity_' + nextIdx).val('');
			$('.nonSorAmount_' + nextIdx).html('');
			$('#nonSorServiceTaxPerc_' + nextIdx).val('');
			$('.nonSorVatAmt_' + nextIdx).html('');
			$('.nonSorTotal_' + nextIdx).html('');
			
			generateSlno();
			
			resetIndexes();
			
		}
	} else {
		  bootbox.alert('limit reached!');
	}
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
    
	var tbl=document.getElementById('tblNonSor');	
	var rowcount=$("#tblNonSor tbody tr").length;

	if(rowcount==2) {
		$('#nonSorId_1').val('');
		$('#nonSorId_1').val('');
		$('#nonSorDesc_1').val('');
		$('#nonSorUom_1').val('');
		$('#nonSorRate_1').val('');
		$('#nonSorQuantity_1').val('');
		$('.nonSorAmount_1').html('');
		$('#nonSorServiceTaxPerc_1').val('');
		$('.nonSorVatAmt_1').html('');
		$('.nonSorTotal_1').html('');
		$('#nonSorRow').attr('hidden', 'true');;
		$('#nonSorMessage').removeAttr('hidden');
	} else {
		tbl.deleteRow(rIndex);
	}
	//starting index for table fields
	generateSlno();
	
	resetIndexes();
	
	calculateNonSorEstimateAmountTotal();
	calculateNonSorVatAmountTotal();
	nonSorTotal();
	return true;
}

function resetIndexes() {
	var idx = 0;
	
	//regenerate index existing inputs in table row
	$(".sorRow").each(function() {
		$(this).find("input, span").each(function() {
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
	
	var hiddenRowCount = $("#tblsor tbody tr:hidden[id='sorRow']").length;
	if(hiddenRowCount == 1)
		idx = 0;
	
	$(".nonSorRow").each(function() {
		$(this).find("input, span, select").each(function() {
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
		flag = true;
	}
	if(!flag) {
		var rate = $('#nonSorRate_' + rowcount).val();
		if(rate == "")
			rate = 0.0;
		var quantity = $('#nonSorQuantity_' + rowcount).val();
		if(quantity == "")
			quantity = 0.0;
		var amount = parseFloat(parseFloat(quantity) * parseFloat(rate)).toFixed(2);
		var vatAmount = parseFloat(($('#nonSorServiceTaxPerc_' + rowcount).val() * amount) / 100).toFixed(2);
		$('.nonSorAmount_' + rowcount).html(amount);
		$('.nonSorVatAmount_' + rowcount).html(vatAmount);
		$('.nonSorTotal_' + rowcount).html(parseFloat(parseFloat(amount) + parseFloat(vatAmount)).toFixed(2));
		calculateNonSorEstimateAmountTotal();
		calculateNonSorVatAmountTotal();
		nonSorTotal();
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
			total = parseFloat(parseFloat(total) + parseFloat($(this).html().trim())).toFixed(2);
	});
	$('#nonSorEstimateTotal').html(total);
}

function calculateNonSorVatAmountTotal() {
	var total = 0;
	$('.nonSorVatAmt').each(function() {
		if($(this).html().trim() != "")
			total = parseFloat(parseFloat(total) + parseFloat($(this).html().trim())).toFixed(2);
	});
	$('#nonSorServiceVatAmtTotal').html(total);
}

function nonSorTotal() {
	var total = 0.0;
	$('.nonSorTotal').each(function() {
		if($(this).html().trim() != "")
			total = parseFloat(parseFloat(total) + parseFloat($(this).html().trim())).toFixed(2);
	});
	$('#nonSorTotal').html(total);
	calculateEstimateValue();
}

function calculateEstimateValue() {
	var sorTotal = $('#sorTotal').html();
	var nonSorTotal = $('#nonSorTotal').html();
	if(sorTotal == '')
		sorTotal = 0.0;
	if(nonSorTotal == '')
		nonSorTotal = 0.0;
	
	var estimateValue = parseFloat(parseFloat(sorTotal) + parseFloat(nonSorTotal)).toFixed(2);
	
	$('#estimateValue').val(estimateValue);
}

function validateInput(object) {
    var valid = /^[1-9](\d{0,9})(\.\d{0,2})?$/.test($(object).val()),
        val = $(object).val();
    
    if(!valid){
        console.log("Invalid input!");
        $(object).val(val.substring(0, val.length - 1));
    }
}


function getAbstractEstimateDate() {
	var dt = new Date(); 
	var dd = dt.getDate() < 10 ? "0" + dt.getDate() : dt.getDate(); 
	var mm = dt.getMonth() + 1;
	var mm = mm < 10 ? "0" + mm : mm;
	var yy = 1900 + dt.getYear();
	jQuery('[name="estimateDate"]').val(dd+"/"+mm+"/"+yy);
}


function enableFileds() {
	console.log('submit called!');
	jQuery("#estimateNumber").removeAttr('disabled');
	jQuery("#projectCode").removeAttr('disabled');
	jQuery("#executingDepartment").removeAttr('disabled');
	jQuery("#estimateDate").removeAttr('disabled');
	jQuery("#wardInput").removeAttr('disabled');
	jQuery("#wardInput").removeAttr('disabled');
	jQuery("#natureOfWork").removeAttr('disabled');
	jQuery("#category").removeAttr('disabled');
	jQuery("#parentCategory").removeAttr('disabled');
	
	if($('#abstractEstimate').valid()) {
		var hiddenRowCount = $("#tblsor tbody tr:hidden[id='sorRow']").length;
		if(hiddenRowCount == 1) {
			var tbl=document.getElementById('tblsor');
			tbl.deleteRow(2);
		}
		
		hiddenRowCount = $("#tblNonSor tbody tr:hidden[id='nonSorRow']").length;
		if(hiddenRowCount == 1) {
			var tbl=document.getElementById('tblNonSor');
			tbl.deleteRow(2);
		}
		return true;
	}
		
	return false; 
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
				console.log(value);
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
				     $('#category').append($('<option '+ selected +'>').text(val.description).attr('value', val.id));
				});
			});
		}
	});