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

jQuery(document).ready(function(){
	
	jQuery('#sorSearch').blur(function() {
		jQuery('#sorSearch').val('');
	});
	
	$isServiceVATRequired = $('#isServiceVATRequired').val();
	
	if($isServiceVATRequired == 'true') {
		$('#nonSorServiceVatHeader').removeAttr('hidden');
		$('#nonSorVatAmountHeader').removeAttr('hidden');
		$('.nonSorServiceTaxPerc').removeAttr('hidden');
		$('.nonSorVatAmount').removeAttr('hidden');
		$('.emptytd').removeAttr('hidden');
		$('.nonSorServiceVatAmt').removeAttr('hidden');
	}
	
	$('#addnonSorRow').click(function() {
		var hiddenRowCount = jQuery("#tblNonSor tbody tr:hidden[id='nonSorRow']").length;
		if(hiddenRowCount == 0) {
			addNonSor();
		} else {
			$('.nonSorRate').val('');
			$('.nonSorQuantity').val('');
			$('.nonSorServiceTaxPerc').val('');
			jQuery('#nonSorMessage').hide();
			jQuery('#nonSorRow').show();
		}
	});
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

function addNonSor() {
	var rowcount = jQuery("#tblNonSor tbody tr").length;
	if (rowcount < 31) {
		if (document.getElementById('nonSorRow') != null) {
			// get Next Row Index to Generate
			var nextIdx = jQuery("#tblNonSor tbody tr").length - 1;

			// Generate all textboxes Id and name with new index
			jQuery("#nonSorRow").clone().find("input, span, select").each(
					function() {

						if(!jQuery(this).is('span')) {
							jQuery(this).attr({
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
							jQuery(this).attr({
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
	jQuery(".spannonsorslno").each(function(){
		jQuery(this).text(idx);
		idx++;
	});
}

function deleteNonSor(obj) {
    var rIndex = getRow(obj).rowIndex;
    
	var tbl=document.getElementById('tblNonSor');	
	var rowcount=jQuery("#tblNonSor tbody tr").length;

	if(rowcount==2) {
		jQuery('#nonSorId_1').val('');
		$('#nonSorId_1').val('');
		$('#nonSorDesc_1').val('');
		$('#nonSorUom_1').val('');
		$('#nonSorRate_1').val('');
		$('#nonSorQuantity_1').val('');
		$('.nonSorAmount_1').html('');
		$('#nonSorServiceTaxPerc_1').val('');
		$('.nonSorVatAmt_1').html('');
		$('.nonSorTotal_1').html('');
		jQuery('#nonSorRow').hide();
		jQuery('#nonSorMessage').show();
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
	jQuery(".sorRow").each(function() {
		jQuery(this).find("input, span").each(function() {
			if (!jQuery(this).is('span')) {
				jQuery(this).attr({
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
				jQuery(this).attr({
					'class' : function(_, name) {
						return name.replace(/\d+/, idx);
					}
				});
			}
		});
		idx++;
	});
	
	var hiddenRowCount = jQuery("#tblsor tbody tr:hidden[id='sorRow']").length;
	if(hiddenRowCount == 1)
		idx = 0;
	
	jQuery(".nonSorRow").each(function() {
		jQuery(this).find("input, span, select").each(function() {
			if (!jQuery(this).is('span')) {
				jQuery(this).attr({
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
				jQuery(this).attr({
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

var ie=document.all
var ns6=document.getElementById&&!document.all

function showhint(menucontents, obj, e, tipwidth) {
	if ((ie || ns6) && document.getElementById("hintbox")) {
		dropmenuobj = document.getElementById("hintbox")
		dropmenuobj.innerHTML = menucontents
		dropmenuobj.style.left = dropmenuobj.style.top = -500
		if (tipwidth != "") {
			dropmenuobj.widthobj = dropmenuobj.style
			dropmenuobj.widthobj.width = tipwidth
		}
		dropmenuobj.x = getposOffset(obj, "left")
		dropmenuobj.y = getposOffset(obj, "top")
		dropmenuobj.style.left = dropmenuobj.x
				- clearbrowseredge(obj, "rightedge") + obj.offsetWidth + "px"
		dropmenuobj.style.top = dropmenuobj.y
				- clearbrowseredge(obj, "bottomedge") + "px"
		dropmenuobj.style.visibility = "visible"
		obj.onmouseout = hidetip
	}
}