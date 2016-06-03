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
$subSchemeId = 0;
var hint='<a href="#" class="hintanchor" title="@fulldescription@"><i class="fa fa-question-circle" aria-hidden="true"></i></a>';
$(document).ready(function(){
	
	$('.disablefield').attr('disabled', 'disabled');
	$subTypeOfWorkId = $('#subTypeOfWorkValue').val();
	$subTypeOfWorkId = $('#subTypeOfWorkValue').val();
	var nameOfWork = $('#nameOfWork').val();
	$('#workName').val(nameOfWork);
	$('#scheme').trigger('change');
	getAbstractEstimateDate();
	
	
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
$('#addOverheadRow').click(function() { 

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
    var overhead = document.getElementById('overheadValues['+ (rIndex-1) + '].name').value;
    var id = jQuery(getRow(obj)).children('td:first').children('input:first').val();
    //To get all the deleted rows id
    var aIndex = rIndex - 1;
	var tbl=document.getElementById('overheadTable');	
	var rowcount=jQuery("#overheadTable tbody tr").length;
    if(rowcount<=1) {
    	bootbox.alert("This row can not be deleted");
		return false;
	} else {
	    //calculating Overhead TotalAmount
	    var total = document.getElementById('overheadTotalAmount').value;
	    if(total==null || total=="")
	    	total = 0;
	    var amount = document.getElementById('overheadValues['+ (rIndex-1) + '].amount').value;
	    if(amount==null || amount=="")
	    	amount = 0;
	    total = eval(total) - eval(amount);
	    document.getElementById('overheadTotalAmount').value = total;
	    
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
		resetAddedOverheads();
	    calculateOverheadTotalAmount();
		return true;
	}
    
}

function recalculateOverheads(){
	var resultLength = jQuery('#overheadTable tr').length-1;
	var index;
	var estimateValue = $("#estimateValue").val();
	for (var i = 1; i <= resultLength; i++) {
		index = i;
		var percentage = document.getElementById('actionOverheadValues['
				+ index + '].percentage').value;
		var amount = document.getElementById('actionOverheadValues['
				+ index + '].amount');
		amount.value = (estimateValue*percentage)/100;
		calculateOverheadTotalAmount();
	}
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

function resetAddedOverheads(){
	addedOverheads = new Array();
	var resultLength = jQuery('#overheadTable tr').length-1;
	var index;
	for (var i = 0; i < resultLength; i++) {
		index = i;
		var overheadvalue = document.getElementById('overheadValues['
				+ index + '].name').value;
		if(overheadvalue!="")
			addedOverheads.push(overheadvalue);
		else{
			document.getElementById('overheadValues['+ index + '].percentage').value = "";
			document.getElementById('overheadValues['+ index + '].amount').value = 0;
			calculateOverheadTotalAmount();
		}
	}
}

function getPercentageOrLumpsumByOverhead(overhead) {
	if(overhead.value==""){
		resetAddedOverheads();
	}else
	if(addedOverheads.indexOf(overhead.value) == -1) {
		resetAddedOverheads();
		var objName = overhead.name;
		var index =objName.substring(objName.indexOf("[")+1,objName.indexOf("]")); 
		var estimateValue = $("#estimateValue").val();
		if (overhead.value != '') {
			$.ajax({
				url : '/egworks/abstractestimate/getpercentageorlumpsumbyoverheadid',
				type : "get",
				data : {
					overheadId : overhead.value
				},
				success : function(data, textStatus, jqXHR) {
					document.getElementById('overheadValues['+ index + '].overhead.id').value = data.overhead.id;
					if(data.percentage>0){
						document.getElementById('overheadValues['+ index + '].percentage').value = data.percentage;
						document.getElementById('overheadValues['+ index + '].amount').value = (estimateValue*data.percentage)/100;
						
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
	}else{
		var index =overhead.name.substring(overhead.name.indexOf("[")+1,overhead.name.indexOf("]")); 
		document.getElementById('overheadValues['+ index + '].percentage').value = "";
		document.getElementById('overheadValues['+ index + '].amount').value = 0;
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
    				console.log('calling :)');
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
			jQuery("#yearEstimateRow").clone().find("input, select ,span").each(function() {
				     
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
			$(this).find("input, select, span").each(function() {
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
		var overheadTableLength = jQuery('#overheadTable tr').length-1;
		if(overheadTableLength == 1){
			var overhead = document.getElementById('overheadValues[0].overhead.id').value;
			if(overhead==""){
				var tbl=document.getElementById('overheadTable');
				tbl.deleteRow(1);
			}
		}
		if(!validateOverheads())
			return false;
		$('.disablefield').removeAttr("disabled");
		return true;
	} else
		return false;

}

function validateOverheads(){
	var resultLength = jQuery('#overheadTable tr').length-1;
	var index;
	for (var i = 0; i < resultLength; i++) {
		index = i;
		var overheadvalue = document.getElementById('overheadValues['
				+ index + '].name').value;
		var amount = document.getElementById('overheadValues['
				+ index + '].amount').value;
		if(overheadvalue!=""){
			if(amount=="" || amount<=0){
				document.getElementById('overheadValues['+ index + '].amount').focus();
				bootbox.alert("Amount is requried for overheads line:  "+(index+1));
				return false;
			}
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

var templateCode = new Bloodhound({
    datumTokenizer: function (datum) {
        return Bloodhound.tokenizers.whitespace(datum.value);
    },
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    remote: {
        url: '/egworks/abstractestimate/ajaxestimatetemplatebycode?code=%QUERY', 
        filter: function (data) {
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
		window.open("/egworks/estimate/estimateTemplate-search.action?sourcePage=searchForEstimate&typeOfWork="+typeOfWork+"&subTypeOfWork="+subTypeOfWork,'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	else{
		var templateId = $('#templateId').val();
		var sorHiddenRowCount = $("#tblsor tbody tr:hidden[id='sorRow']").length;
		var nonSorHiddenRowCount = $("#tblNonSor tbody tr:hidden[id='nonSorRow']").length;
		if(templateId!="" && (sorHiddenRowCount !=1 || nonSorHiddenRowCount!=1)){ 
			var ans=confirm($("#estimateTemplateConfirmMsg").val());	
			if(ans) {
				clearActivities();
				getActivitiesForTemplate(templateId);
			}
			else {
				return false;		
			}
		}else{
			getActivitiesForTemplate(templateId);
		}
	}
	
});


function resetTemplate(id){
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
	var hiddenRowCount = $("#tblsor tbody tr:hidden[id='sorRow']").length;
	if(hiddenRowCount == 0) {
		var sorrowcount = jQuery("#tblsor tbody tr").length;
		var sortbl=document.getElementById('tblsor');
		for(rowcount=2;rowcount<=sorrowcount;rowcount++){
			if(rowcount==2) {
				$('.sorhiddenid').val('');
				$('#quantity_0').val('');
				$('.amount_0').html('');
				$('#vat_0').val('');
				$('.vatAmount_0').html('');
				$('.total_0').html('');
				$('.code_0').html('');
				$('.summary_0').html('');
				$('.uom_0').html('');
				$('.rate_0').html('');
				$('.description_0').html('');
				$('#sorRow').prop("hidden",true);
			} else {
				sortbl.deleteRow(rowcount);
			}
		}
		resetIndexes();
	}
	
	hiddenRowCount = $("#tblNonSor tbody tr:hidden[id='nonSorRow']").length;
	if(hiddenRowCount == 0) {
		var nonsorrowcount = jQuery("#tblNonSor tbody tr").length;
		var nonsortbl=document.getElementById('tblNonSor');
		for(rowcount=2;rowcount<=nonsorrowcount;rowcount++){
			if(rowcount==2) {
				$('.nonSorId').val('');
				$('.nonSorDesc').val('');
				$('.nonSorUom').val('');
				$('.nonSorRate').val('');
				$('.nonSorQuantity').val('');
				$('.nonSorAmount').html('');
				$('.nonSorServiceTaxPerc').val('');
				$('.nonSorVatAmt').html('');
				$('.nonSorTotal').html('');
				$('#nonSorRow').prop("hidden",true);
				$('#nonSorMessage').removeAttr("hidden");
			} else {
				nonsortbl.deleteRow(rowcount);
			}
		}
		resetIndexes();
	}
}

function getActivitiesForTemplate(id){
	var nonSorCheck  = false;
	$.ajax({
		url: "/egworks/abstractestimate/ajaxgetestimatetemplatebyid/"+id,     
		type: "GET",
		dataType: "json",
		success: function (estimateTemplateActivities) {
			$.each(estimateTemplateActivities, function(index,estimateTemplateActivity){
				
				if(index==0){
					$('#message').prop("hidden",true);
					$('#sorRow').removeAttr("hidden");
				}else{
					if(estimateTemplateActivity.schedule != null){
						addSor();	
					}else{
						if(!nonSorCheck){
							$('#nonSorMessage').prop("hidden",true);
							$('#nonSorRow').removeAttr("hidden");
						}
						if(nonSorCheck)
							addNonSor();
						nonSorCheck = true;
					}
				}
				if(estimateTemplateActivity.schedule != null){
					$('.code_'+index).html(estimateTemplateActivity.schedule.code);
					$('.summary_'+index).html(estimateTemplateActivity.schedule.summary);
					$('.description_'+index).html(hint.replace(/@fulldescription@/g, estimateTemplateActivity.schedule.description));
					$('.uom_'+index).html(estimateTemplateActivity.schedule.uom.uom);
					if(estimateTemplateActivity.schedule.sorRate!=null)
						$('.rate_'+index).html(estimateTemplateActivity.schedule.sorRate);
					else
						$('.rate_'+index).html(0);
				}else{
					$('#nonSorDesc_'+index).val(estimateTemplateActivity.nonSor.description);
					$('#nonSorUom_'+index).val(estimateTemplateActivity.uom.id);
					$('#nonSorRate_'+index).val(estimateTemplateActivity.rate.formattedString);
				}
				resetIndexes();
			});
		}, 
		error: function (response) {
			console.log("failed");
		}
	});
	
	resetIndexes();
	calculateNonSorEstimateAmountTotal();
	calculateNonSorVatAmountTotal();
	nonSorTotal();
	resetIndexes();
}

function getSubSchemsBySchemeId(schemeId) {
	if ($('#scheme').val() === '') {
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
						var responseObj = JSON.parse(response);
						$.each(responseObj, function(index, value) {
							$('#subScheme').append($('<option>').text(responseObj[index].name).attr('value', responseObj[index].id));
							$('#subScheme').val($subSchemeId);
						});
					}, 
					error: function (response) {
						console.log("failed");
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
	        console.log("Invalid input!");
	        this.value = val.substring(0, val.length - 1);
	    }
	});
}