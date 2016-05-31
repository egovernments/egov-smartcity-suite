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
	
	var hint='<a href="#" class="hintanchor" title="@fulldescription@"><i class="fa fa-question-circle" aria-hidden="true"></i></a>'
	
	jQuery('#sorSearch').blur(function() {
		jQuery('#sorSearch').val('');
	});
	
	$isServiceVATRequired = $('#isServiceVATRequired').val();
	
	if($isServiceVATRequired == 'true') {
		$('#serviceVatHeader').removeAttr('hidden');
		$('#vatAmountHeader').removeAttr('hidden');
		$('.serviceTaxPerc').removeAttr('hidden');
		$('.vatAmount').removeAttr('hidden');
		$('.emptytd').removeAttr('hidden');
		$('.serviceVatAmt').removeAttr('hidden');
	}
	
	var sorSearch = new Bloodhound({
	    datumTokenizer: function (datum) {
	        return Bloodhound.tokenizers.whitespace(datum.value);
	    },
	    queryTokenizer: Bloodhound.tokenizers.whitespace,
	    remote: {
	        url: '/egworks/abstractestimate/ajaxsor-byschedulecategories?code=',
	        replace: function (url, query) {
	        		var scheduleCategories = jQuery('#scheduleCategory').val();
	        		if(scheduleCategories == null)
	        			bootbox.alert($('#msgschedulecategory').val());
	        	    return url + query + '&scheduleCategories=' + scheduleCategories;
	        	},
	        filter: function (data) {
	            return jQuery.map(data, function (ct) {
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
	var sorSearch_typeahead = jQuery('#sorSearch').typeahead({
		hint : true,
		highlight : true,
		minLength : 2
	}, {
		displayKey : 'displayResult',
		source : sorSearch.ttAdapter()
	}).on('typeahead:selected', function (event, data) {
		var flag = false;
		jQuery('.sorhiddenid').each(function() {
			if(jQuery(this).val() == data.id) {
				flag = true;
			}
		});
		if(flag) {
			bootbox.alert($('#erroradded').val(), function() {
				jQuery('#sorSearch').val('');
			});
		}
		else {
			var hiddenRowCount = jQuery("#tblsor tbody tr:hidden[id='sorRow']").length;
			if(hiddenRowCount == 0) {
				addSor();
			} else {
				$('#quantity_0').val('');
				$('#vat_0').val('');
				jQuery('#message').hide();
				jQuery('#sorRow').show();
			}
			var rowcount = jQuery("#tblsor tbody tr").length;
			var key = parseInt(rowcount) - 2;
			jQuery.each(data, function(id, val) {
				if(id == "id")
					jQuery('#' + id + "_" + key).val(val);
				if(id == 'description') {
					jQuery('.' + id + "_" + key).html(hint.replace(/@fulldescription@/g, val));
				}
					
				else
					jQuery('.' + id + "_" + key).html(val);
			});
		}
		jQuery('#sorSearch').val('');
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

function addSor() {
	var rowcount = jQuery("#tblsor tbody tr").length;
	if (rowcount < 31) {
		if (document.getElementById('sorRow') != null) {
			// get Next Row Index to Generate
			var nextIdx = jQuery("#tblsor tbody tr").length - 1;

			// Generate all textboxes Id and name with new index
			jQuery("#sorRow").clone().find("input, span").each(
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
	jQuery(".spansorslno").each(function(){
		jQuery(this).text(idx);
		idx++;
	});
}

function deleteSor(obj) {
	var rIndex = getRow(obj).rowIndex;
	
	var tbl=document.getElementById('tblsor');	
	var rowcount=jQuery("#tblsor tbody tr").length;

	if(rowcount==2) {
		jQuery('.sorhiddenid').val('');
		$('#quantity_0').val('');
		$('.amount_0').html('');
		$('#vat_0').val('');
		$('.vatAmount_0').html('');
		$('.total_0').html('');
		jQuery('#sorRow').hide();
		jQuery('#message').show();
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