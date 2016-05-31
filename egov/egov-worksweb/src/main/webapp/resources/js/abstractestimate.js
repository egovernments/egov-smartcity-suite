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
