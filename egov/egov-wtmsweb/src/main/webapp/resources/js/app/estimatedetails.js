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
$(document).ready(function()
{	
	if($('#pipelineDistance') != null && $('#pipelineDistance').val() == 0.0)
		$('#pipelineDistance').val('');	
	
	if($('#estimationCharges') != null && $('#estimationCharges').val() == 0.0)
		$('#estimationCharges').val('');
	
	loadConnectionCategories();
	
	$('#connectionCategorie').change(function () {
		loadConnectionCategories();
    });
	
	
	
	function loadConnectionCategories(){
	    if ($('#connectionCategorie :selected').text().localeCompare("BPL") == 0)  {
	    	$("#cardHolderDiv").show();
	    	$("#bplCardHolderName").attr('required', 'required');
	    }
	    else{
	    	$("#cardHolderDiv").hide();
	    	$("#bplCardHolderName").removeAttr('required');
	    }
	}
	
	$("#addRowId").click(function(){	
		addRow();
	});	
			
	function addRow() {
        var table = document.getElementById('estimateDetails');
        var rowCount = table.rows.length;
        var row = table.insertRow(rowCount-1);
        var counts = rowCount - 1;

 		elementIndex = counts - 1;
        var newRow = document.createElement("tr");
    	var newCol = document.createElement("td");
		newRow.appendChild(newCol);
		 
        var cell1 = row.insertCell(0);
        cell1.className="text-center";
        var slno = document.createElement("span");
        slno.setAttribute("id","slNo"+counts);
        slno.innerHTML=counts;
        cell1.appendChild(slno); 
        
        newCol = document.createElement("td");        
        newRow.appendChild(newCol);
        var cell2 = row.insertCell(1);
        cell2.className = "text-center";
        var material = document.createElement("textarea");
        var att = document.createAttribute("class");
 		att.value = "form-control table-input";
 		material.setAttributeNode(att); 
 		material.type = "textarea";
 		material.setAttribute("required", "required");
 		material.setAttribute("maxlength", "256");
 		material.setAttribute("name", "estimationDetails[" + elementIndex + "].itemDescription");
 		material.setAttribute("id", "estimationDetails"+elementIndex+"itemDescription");
        cell2.appendChild(material);
        
        newCol = document.createElement("td");
		newRow.appendChild(newCol);
        var cell3 = row.insertCell(2);
        cell3.className = "text-right";
        var quantity = document.createElement("input");
        quantity.setAttribute("class","form-control table-input text-right patternvalidation");
        quantity.setAttribute("data-pattern","decimalvalue"); 
        quantity.type = "text";
        quantity.setAttribute("maxlength", "8");
        quantity.setAttribute("name", "estimationDetails[" + elementIndex + "].quantity");
        quantity.setAttribute("id", "estimationDetails"+elementIndex+"quantity");
        quantity.setAttribute("value", 0);
        quantity.setAttribute("onblur", "calculateTotalAmount();");
        cell3.appendChild(quantity);  
        
        newCol = document.createElement("td");
		newRow.appendChild(newCol);
        var cell4 = row.insertCell(3);
        cell4.className="text-center";
        var uom = document.createElement("input");
        uom.setAttribute("class","form-control table-input patternvalidation");
        uom.setAttribute("data-pattern","alphanumerichyphenbackslash");  
        uom.type = "text";
        uom.setAttribute("maxlength", "50");
        uom.setAttribute("name", "estimationDetails[" + elementIndex + "].unitOfMeasurement");
        uom.setAttribute("id", "estimationDetails"+elementIndex+"unitOfMeasurement");
        cell4.appendChild(uom);  
        
        newCol = document.createElement("td");
		newRow.appendChild(newCol);
        var cell5 = row.insertCell(4);
        cell5.className = "text-right";
        var unitRate = document.createElement("input");
        unitRate.setAttribute("class","form-control table-input text-right patternvalidation");
        unitRate.setAttribute("data-pattern","decimalvalue"); 
        unitRate.type = "text";
        unitRate.setAttribute("maxlength", "8");
        unitRate.setAttribute("name", "estimationDetails[" + elementIndex + "].unitRate");
        unitRate.setAttribute("id", "estimationDetails"+elementIndex+"unitRate");  
        unitRate.setAttribute("value", 0.00); 
        unitRate.setAttribute("onblur", "calculateTotalAmount();");
        cell5.appendChild(unitRate);  
       
        newCol = document.createElement("td");
		newRow.appendChild(newCol);
        var cell6 = row.insertCell(5); 
        cell6.className = "text-right";
        var total = document.createElement("input");
        total.setAttribute("class","form-control table-input text-right");
        total.type = "text";
        total.setAttribute("id", "estimationDetails"+elementIndex+"amount");
        total.setAttribute("disabled", "disabled");
        cell6.appendChild(total);  
        
        newCol = document.createElement("td");
		newRow.appendChild(newCol); 
        var cell7 = row.insertCell(6);
        cell7.className = "text-center";
        var actions = document.createElement("span");
        actions.setAttribute("style","cursor:pointer");
        actions.innerHTML = '<i class="fa fa-trash" id="delete_row" ></i>';
        cell7.appendChild(actions); 
        patternvalidation();
	}
	
	$(document).on('click',"#delete_row",function (){
		var table = document.getElementById('estimateDetails');
        var rowCount = table.rows.length;
		$(this).closest('tr').remove();		
        var counts = rowCount - 1;
        var j = 2;
        var i;
        for(i=2;i<=counts-1;i++){ 
        	var serialNo = '#slNo'+i;
        	var prevIndex = i-1;
        	var currentIndex = j-1; 
        	var itemDesc = '#estimationDetails'+prevIndex+'itemDescription';
        	var quantity = '#estimationDetails'+prevIndex+'quantity';
        	var unitOfMeasurement = '#estimationDetails'+prevIndex+'unitOfMeasurement';
        	var unitRate = '#estimationDetails'+prevIndex+'unitRate';
        	var amount = '#estimationDetails'+prevIndex+'amount';
        	if($(serialNo) != null && $(serialNo).html() != '' && $(serialNo).html() != undefined ) {
	        	$(serialNo).html(j);
	        	$(serialNo).attr("id", 'slNo'+j); 
	        	$(itemDesc).attr("id", 'estimationDetails'+currentIndex+'itemDescription'); 
	        	$(quantity).attr("id", 'estimationDetails'+currentIndex+'quantity'); 
	        	$(unitOfMeasurement).attr("id", 'estimationDetails'+currentIndex+'unitOfMeasurement'); 
	        	$(unitRate).attr("id", 'estimationDetails'+currentIndex+'unitRate'); 
	        	$(amount).attr("id", 'estimationDetails'+currentIndex+'amount');
	        	j++;
        	}
        }	
        calculateTotalAmount();
	});	
	
});

function calculateTotalAmount() {
	var table = document.getElementById('estimateDetails');
    var rowCount = table.rows.length;     
    var i;
    var grandTotal=0;
    for(i=1;i<=rowCount-2;i++){  
    	currentIndex = i-1; 
    	var quantity = $('#estimationDetails'+currentIndex+'quantity').val();
    	var unitRate = $('#estimationDetails'+currentIndex+'unitRate').val();
    	var total = 0;
    	if(quantity!='' && unitRate!='') {
    		total = quantity*unitRate;
    		$('#estimationDetails'+currentIndex+'amount').val(total);
    		grandTotal = grandTotal + total;
    	}
    }
    $('#grandTotal').val(grandTotal);
    $('#supervisionCharges').val(Math.round(0.15*grandTotal).toFixed(2));
    $('#estimationCharges').val(0.0);
}