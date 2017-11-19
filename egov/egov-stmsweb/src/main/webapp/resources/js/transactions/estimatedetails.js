/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces, 
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any 
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines, 
 *            please contact contact@egovernments.org
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
 *
 */
$(document).ready(function()
{	if($('#inspectionDate').val()!=''){
		$('#inspectionDate').val($('#inspectionDate').val());
	}
	
	if($('#sewerageInspectionDate').val()!=null && $('#sewerageInspectionDate').val()!="")
		$("#inspectionDate").datepicker("update", new Date($('#sewerageInspectionDate').val().split("/").reverse().join("/")));
	calculateGrandTotal(); 
	showHideRoadInfo();
	//addRow();
	//add_Inspection_Row();
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
	   if(validateEstimationDetailsOnAdd())	
		   addRow();
	});	
			
	function addRow() {
        var table = document.getElementById('estimateDetails');
        var rowCount = table.rows.length;
        if((rowCount-2) >= 10) {
        	bootbox.alert("Maximum of only 10 rows are allowed!!");
        	return;
        }
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
        slno.setAttribute("class","serialNumber"); 
        
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
 		material.setAttribute("name", "estimationDetailsForUpdate[" + elementIndex + "].itemDescription");
 		material.setAttribute("id", "estimationDetailsForUpdate"+elementIndex+"itemDescription");
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
        quantity.setAttribute("name", "estimationDetailsForUpdate[" + elementIndex + "].quantity");
        quantity.setAttribute("id", "estimationDetailsForUpdate"+elementIndex+"quantity");
        quantity.setAttribute("value", 0);
        quantity.setAttribute("onblur", "calculateTotalAmount();");
        cell3.appendChild(quantity);  
        
        newCol = document.createElement("td");
		newRow.appendChild(newCol);
        var cell4 = row.insertCell(3);
        var uom = document.createElement("select");
        uom.setAttribute("class","form-control");
        uom.setAttribute("name", "estimationDetailsForUpdate[" + elementIndex + "].unitOfMeasurement");
        uom.setAttribute("id", "estimationDetailsForUpdate"+elementIndex+"unitOfMeasurement");
        cell4.appendChild(uom);  
      
        $('#estimationDetailsForUpdate'+elementIndex+'unitOfMeasurement').html($("#estimationDetailsForUpdate0unitOfMeasurement").html());
        $('#estimationDetailsForUpdate'+elementIndex+'unitOfMeasurement').val('');
        
        newCol = document.createElement("td");
		newRow.appendChild(newCol);
        var cell5 = row.insertCell(4);
        cell5.className = "text-right";
        var unitRate = document.createElement("input");
        unitRate.setAttribute("class","form-control table-input text-right patternvalidation");
        unitRate.setAttribute("data-pattern","decimalvalue"); 
        unitRate.type = "text";
        unitRate.setAttribute("maxlength", "8");
        unitRate.setAttribute("name", "estimationDetailsForUpdate[" + elementIndex + "].unitRate");
        unitRate.setAttribute("id", "estimationDetailsForUpdate"+elementIndex+"unitRate");  
        unitRate.setAttribute("value", 0.00); 
        unitRate.setAttribute("onblur", "calculateTotalAmount();");
        cell5.appendChild(unitRate);  
       
        newCol = document.createElement("td");
		newRow.appendChild(newCol);
        var cell6 = row.insertCell(5); 
        cell6.className = "text-right";
        var total = document.createElement("input");
        total.setAttribute("class","form-control table-input text-right patternvalidation");
        total.setAttribute("data-pattern","decimalvalue")
        total.type = "text";
        total.setAttribute("name", "estimationDetailsForUpdate[" + elementIndex + "].amount");
        total.setAttribute("id", "estimationDetailsForUpdate"+elementIndex+"amount");
        total.setAttribute("onblur", "calculateGrandTotal();");
        total.setAttribute("value", 0.00);
        cell6.appendChild(total);  
        
        newCol = document.createElement("td");
		newRow.appendChild(newCol); 
        var cell7 = row.insertCell(6);
        cell7.className = "text-center";
        var actions = document.createElement("a");
        actions.setAttribute("href","javascript:void(0)"); 
        actions.setAttribute("class","btn-sm btn-danger");
        actions.setAttribute("id","delete_row");
       	actions.innerHTML = '<i class="fa fa-trash" id="delete_row" ></i>';
        cell7.appendChild(actions); 
        patternvalidation();
        generateEstimationSeriaNo();
	}
	
	
	$(document).on('click',"#delete_row",function (){
		if(!$("#removedEstimationDtlRowId").val()=="" && $(this).data("record-id")!='undefined'){
			$("#removedEstimationDtlRowId").val($("#removedEstimationDtlRowId").val()+",");
			
		}
		if($(this).data("record-id")!='undefined')
		$("#removedEstimationDtlRowId").val($("#removedEstimationDtlRowId").val()+$(this).data("record-id"));
		
		var table = document.getElementById('estimateDetails');
        var rowCount = table.rows.length;
        
        var rowIndex = $(this).closest('td').parent()[0].sectionRowIndex;
     //   alert($(this).closest('td').parent()[0].sectionRowIndex);
		$(this).closest('tr').remove();	
		
		generateEstimationSeriaNo();
		
		  $("#estimateDetails tbody tr").each(function() {
				$(this).find("input, select, hidden,textarea").each(function() {
					var index = $(this).closest('td').parent()[0].sectionRowIndex;
					if(index>=rowIndex){
						var increment = index++;
						$(this).attr({
							'name': function(_, name){
								return name.replace(/\d+/g,+increment);
							},
							'id': function(_, id){
								if(id==undefined){
									return "";
								}
								return id.replace(/\d+/g, +increment);
							}
						});
					}
					
				});
		 });
		
        calculateGrandTotal();
	});	
	
});


function validateInspectionDetailsOnSubmit(){
	var tbl=document.getElementById("inspectionDetails");
    var lastRow = (tbl.rows.length)-1;
    var noOfPipes,pipeSize,pipeLength,screwSize,noOfScrews,distance;
    var mandatoryValEntered=false, mandatoryValNotEntered=false;
    for(var i=1;i<=lastRow;i++){
    	noOfPipes=getControlInBranch(tbl.rows[i],'fieldInspectionDetailsForUpdate'+(i-1)+'noOfPipes').value;
        pipeSize=getControlInBranch(tbl.rows[i],'fieldInspectionDetailsForUpdate'+(i-1)+'pipeSize').value;
        pipeLength=getControlInBranch(tbl.rows[i],'fieldInspectionDetailsForUpdate'+(i-1)+'pipeLength').value;
        screwSize=getControlInBranch(tbl.rows[i],'fieldInspectionDetailsForUpdate'+(i-1)+'screwSize').value; 
        noOfScrews=getControlInBranch(tbl.rows[i],'fieldInspectionDetailsForUpdate'+(i-1)+'noOfScrews').value;
        distance=getControlInBranch(tbl.rows[i],'fieldInspectionDetailsForUpdate'+(i-1)+'distance').value;
         
         if(screwSize!='' ||  pipeSize!='' ||
         		(pipeLength!='' && pipeLength!=0) || 
         		(noOfPipes!='' && noOfPipes!=0) ||
         		(distance!='' && distance!=0) ||
         	    (noOfScrews!='' && noOfScrews!=0)) {
        	 mandatoryValEntered=true;
         }
         if(screwSize=='' ||  pipeSize=='' ||
         		(pipeLength=='' || pipeLength==0) || 
         		(noOfPipes=='' || noOfPipes==0) ||
         		(distance=='' || distance==0) ||
         	    (noOfScrews=='' || noOfScrews==0)) { 
        	 mandatoryValNotEntered = true;
         }
        
    	if(mandatoryValEntered==true && mandatoryValNotEntered==true){
    		bootbox.alert("Enter all mandatory \"Pipe Details\" for  row "+(i)+".");
    		return false; 
    	}
    } 
    return true;
}


function validateEstimationDetailsOnSubmit(){
	var tbl=document.getElementById("estimateDetails");
    var lastRow = (tbl.rows.length)-2;
    var material,quantity,uom,rate,amount;
    var mandatoryValEntered=false, mandatoryValNotEntered=false; 
    for(var i=1;i<=lastRow;i++){
    	material=getControlInBranch(tbl.rows[i],'estimationDetailsForUpdate'+(i-1)+'itemDescription').value; 
        quantity=getControlInBranch(tbl.rows[i],'estimationDetailsForUpdate'+(i-1)+'quantity').value;
        uom=getControlInBranch(tbl.rows[i],'estimationDetailsForUpdate'+(i-1)+'unitOfMeasurement').value;
        rate=getControlInBranch(tbl.rows[i],'estimationDetailsForUpdate'+(i-1)+'unitRate').value; 
        amount=getControlInBranch(tbl.rows[i],'estimationDetailsForUpdate'+(i-1)+'amount').value;
     
        if(material!='' ||  uom!='' ||
        		(quantity!='' && quantity!=0) || 
        		(rate!='' && rate!=0) ||
        	    (amount!='' && amount!=0)) { 
        	mandatoryValEntered = true;
        } 
        if(material=='' ||  uom=='' ||
        		(quantity=='' || quantity==0) || 
        		(rate=='' || rate==0) ||
        	    (amount=='' || amount==0)) { 
        	 mandatoryValNotEntered = true;
        } 
        
    	if(mandatoryValEntered==true && mandatoryValNotEntered==true){
    		bootbox.alert("Enter all mandatory \"Estimation Details\" for row "+(i)+".");
    		return false; 
    	}
    }
    return true;
} 


function validateInspectionDetailsOnAdd(){
	
	var tbl=document.getElementById("inspectionDetails");
    var lastRow = tbl.rows.length-2;
    console.log('lastRow Index ->'+lastRow);
    
    var noOfPipes = $('*[name="fieldInspections[0].fieldInspectionDetailsForUpdate['+lastRow+'].noOfPipes"]').val();
  //  alert('noOfPipes'+noOfPipes); 
    var pipeSize= $('*[pipeSize="fieldInspections[0].fieldInspectionDetailsForUpdate['+lastRow+'].pipeSize"]').val();
    var pipeLength = $('*[pipeLength="fieldInspections[0].fieldInspectionDetailsForUpdate['+lastRow+'].pipeLength]').val();    
    var screwSize = $('*[pipeLength="fieldInspections[0].fieldInspectionDetailsForUpdate['+lastRow+'].screwSize]').val();
    var noOfScrews = $('*[pipeLength="fieldInspections[0].fieldInspectionDetailsForUpdate['+lastRow+'].noOfScrews]').val();
    var distance = $('*[pipeLength="fieldInspections[0].fieldInspectionDetailsForUpdate['+lastRow+'].distance]').val();
    if(screwSize=='' ||  pipeSize=='' ||
    		(pipeLength=='' || pipeLength==0) || 
    		(noOfPipes=='' || noOfPipes==0) ||
    		(distance=='' || distance==0) ||
    	    (noOfScrews=='' || noOfScrews==0)) { 
    	bootbox.alert("Enter all mandatory values for existing rows before adding. Values cannot be 0 or empty.");
    	return false;
    } 
    return true;
}

function validateEstimationDetailsOnAdd(){
	var tbl=document.getElementById("estimateDetails");
    var lastRow =tbl.rows.length -3;
    
   // alert(lastRow -1);
  //  var noOfPipes = $('*[name="fieldInspections[0].fieldInspectionDetails['+lastRow+'].noOfPipes"]').val();
  //   alert($('*[name="estimationDetails['+(lastRow)+'].itemDescription"]').val());
    var material=$('*[name="estimationDetailsForUpdate['+lastRow+'].itemDescription"]').val();
   // var  material=getControlInBranch(tbl.rows[lastRow],'estimationDetails['+lastRow+'].itemDescription').val(); 
    var quantity=$('*[name="estimationDetailsForUpdate['+lastRow+'].quantity"]').val();
    var uom=$('*[name="estimationDetailsForUpdate['+lastRow+'].uom"]').val();
    var rate=$('*[name="estimationDetailsForUpdate['+lastRow+'].rate"]').val();
    var amount=$('*[name="estimationDetailsForUpdate['+lastRow+'].amount"]').val();
  //  alert('*'+material+'*');  
    
    if(material=='' ||  uom=='' ||
    		(quantity=='' || quantity==0) || 
    		(rate=='' || rate==0) ||
    	    (amount=='' || amount==0)) { 
    	bootbox.alert("Enter all values for existing rows before adding. Values cannot be 0 or empty.");
    	return false;
    } 
    return true;
}

function calculateTotalAmount() {
	var table = document.getElementById('estimateDetails');
    var rowCount = table.rows.length;     
    var i;
    var grandTotal=0;
    for(i=1;i<=rowCount-2;i++){  
    	currentIndex = i-1; 
    	var quantity = $('#estimationDetailsForUpdate'+currentIndex+'quantity').val();
    	var unitRate = $('#estimationDetailsForUpdate'+currentIndex+'unitRate').val();
    	var total = 0;
    	if(quantity!='' && unitRate!='') {
    		total = quantity*unitRate;
    		$('#estimationDetailsForUpdate'+currentIndex+'amount').val(total);
    		grandTotal = grandTotal + total;
    	}
    }
    $('#grandTotal').val(grandTotal);
}

function calculateGrandTotal(){
	var table = document.getElementById('estimateDetails');
    var rowCount = table.rows.length;     
    var i;
	var grandTotal=0;
    for(i=1;i<=rowCount-2;i++){  
    	currentIndex = i-1; 
    	var total = 0;
    	total = $('#estimationDetailsForUpdate'+currentIndex+'amount').val();
    	grandTotal = eval(grandTotal) + eval(total);
    }
	 $('#grandTotal').val(grandTotal);	
}


$("#addInspctRowId").click(function(){	
	if(validateInspectionDetailsOnAdd()) 	
		add_Inspection_Row();
});	
		
function add_Inspection_Row() {
    var table = document.getElementById('inspectionDetails');
    var rowCount = table.rows.length;
    if(rowCount >= 20) {
    	bootbox.alert("Maximum of only 20 rows are allowed!!");
    	return;
    }
    
    var row = table.insertRow((rowCount));
    var counts = (rowCount-1);
    var elementIndex=(counts);
    

	/*elementIndex = counts - 1;*/
    var newRow = document.createElement("tr");
	var newCol = document.createElement("td");
	newRow.appendChild(newCol);
	 
    var cell1 = row.insertCell(0);
    cell1.className="text-center";
    var slno = document.createElement("span");
    slno.setAttribute("id","slNoInsp"+counts);
    slno.setAttribute("class","serialNo");
    slno.innerHTML=counts;
    cell1.appendChild(slno); 
    
    newCol = document.createElement("td");        
    newRow.appendChild(newCol);
    var cell2 = row.insertCell(1);
    cell2.className = "text-right";
    var pipe = document.createElement("input");
    pipe.setAttribute("class","form-control table-input text-right patternvalidation");
    pipe.setAttribute("data-pattern","decimalvalue"); 
    pipe.type = "text";
    pipe.setAttribute("maxlength", "8");
    pipe.setAttribute("name", "fieldInspections[0].fieldInspectionDetailsForUpdate[" + elementIndex + "].noOfPipes");
    pipe.setAttribute("id", "fieldInspectionDetailsForUpdate"+elementIndex+"noOfPipes");
    cell2.appendChild(pipe);
    
    newCol = document.createElement("td");
	newRow.appendChild(newCol);
    var cell3 = row.insertCell(2);
   // cell3.className = "text-right";
    var pipeSize = document.createElement("select");
    pipeSize.setAttribute("class","form-control");
    pipeSize.setAttribute("name", "fieldInspections[0].fieldInspectionDetailsForUpdate[" + elementIndex + "].pipeSize");
    pipeSize.setAttribute("id", "fieldInspectionDetailsForUpdate"+elementIndex+"pipeSize");
    cell3.appendChild(pipeSize); 
    $('#fieldInspectionDetailsForUpdate'+elementIndex+'pipeSize').html($("#fieldInspectionDetailsForUpdate0pipeSize").html());
    $('#fieldInspectionDetailsForUpdate'+elementIndex+'pipeSize').val('');
    
    newCol = document.createElement("td");        
    newRow.appendChild(newCol);
    var cell4 = row.insertCell(3);
    cell4.className = "text-right";
    var pipeLength = document.createElement("input");
    pipeLength.setAttribute("class","form-control table-input text-right patternvalidation");
    pipeLength.setAttribute("data-pattern","decimalvalue"); 
    pipeLength.type = "text";
    pipeLength.setAttribute("maxlength", "8");
    pipeLength.setAttribute("name", "fieldInspections[0].fieldInspectionDetailsForUpdate[" + elementIndex + "].pipeLength");
    pipeLength.setAttribute("id", "fieldInspectionDetailsForUpdate"+elementIndex+"pipeLength");
    cell4.appendChild(pipeLength);
    
    
    newCol = document.createElement("td");
	newRow.appendChild(newCol);
    var cell5 = row.insertCell(4);
    //cell5.className = "text-right";
    var screwSize = document.createElement("select");
    screwSize.setAttribute("class","form-control");
    screwSize.setAttribute("name", "fieldInspections[0].fieldInspectionDetailsForUpdate[" + elementIndex + "].screwSize");
    screwSize.setAttribute("id", "fieldInspectionDetailsForUpdate"+elementIndex+"screwSize");
    cell5.appendChild(screwSize); 
    $('#fieldInspectionDetailsForUpdate'+elementIndex+'screwSize').html($("#fieldInspectionDetailsForUpdate0screwSize").html());
    $('#fieldInspectionDetailsForUpdate'+elementIndex+'screwSize').val('');
  
    newCol = document.createElement("td");
	newRow.appendChild(newCol);
    var cell6 = row.insertCell(5);
    cell6.className="text-right";
    var screw = document.createElement("input");
    screw.setAttribute("class","form-control table-input text-right patternvalidation");
    screw.setAttribute("data-pattern","decimalvalue");  
    screw.type = "text";
    screw.setAttribute("maxlength", "8");
    screw.setAttribute("name", "fieldInspections[0].fieldInspectionDetailsForUpdate[" + elementIndex + "].noOfScrews");
    screw.setAttribute("id", "fieldInspectionDetailsForUpdate"+elementIndex+"noOfScrews");
    cell6.appendChild(screw); 
    
    
    newCol = document.createElement("td");
	newRow.appendChild(newCol);
    var cell7 = row.insertCell(6);
    cell7.className="text-right";
    var distance = document.createElement("input");
    distance.setAttribute("class","form-control table-input text-right patternvalidation");
    distance.setAttribute("data-pattern","decimalvalue");  
    distance.type = "text";
    distance.setAttribute("maxlength", "8");
    distance.setAttribute("name", "fieldInspections[0].fieldInspectionDetailsForUpdate[" + elementIndex + "].distance");
    distance.setAttribute("id", "fieldInspectionDetailsForUpdate"+elementIndex+"distance");
    cell7.appendChild(distance);
    
    newCol = document.createElement("td");
	newRow.appendChild(newCol);
    var cell8 = row.insertCell(7);
    cell8.className="text-center";
    var roadDigging = document.createElement("input");
    roadDigging.type= 'checkbox';
    roadDigging.checked='';
    roadDigging.value='false';
   /* roadDigging.setAttribute("class","form-control table-input text-right patternvalidation");
    roadDigging.setAttribute("data-pattern","decimalvalue");  
    roadDigging.type = "text";
    roadDigging.setAttribute("maxlength", "8");*/
    roadDigging.setAttribute("maxlength","8");
    roadDigging.setAttribute("onclick","enableDisableRoadInfo(this)");
    roadDigging.setAttribute("name", "fieldInspections[0].fieldInspectionDetailsForUpdate[" + elementIndex + "].roadDigging");
    roadDigging.setAttribute("id", "fieldInspectionDetailsForUpdate"+elementIndex+"roadDigging");
    cell8.appendChild(roadDigging);
    
    newCol = document.createElement("td");
	newRow.appendChild(newCol);
    var cell9 = row.insertCell(8);
    cell9.className="text-right";
    var roadLength = document.createElement("input");
    roadLength.setAttribute("class","form-control table-input text-right patternvalidation roadLength"); 
    roadLength.setAttribute("data-pattern","decimalvalue");  
    roadLength.type = "text";
    roadLength.setAttribute("maxlength", "8");
    roadLength.setAttribute("disabled","true");
    roadLength.setAttribute("name", "fieldInspections[0].fieldInspectionDetailsForUpdate[" + elementIndex + "].roadLength");
    roadLength.setAttribute("id", "fieldInspectionDetailsForUpdate"+elementIndex+"roadLength");
    cell9.appendChild(roadLength);
    
    newCol = document.createElement("td");
	newRow.appendChild(newCol);
    var cell10 = row.insertCell(9);
    //cell10.className = "text-right";
    var roadOwner = document.createElement("select");
    roadOwner.setAttribute("class","form-control roadOwner");
    roadOwner.setAttribute("disabled","true");
    roadOwner.setAttribute("name", "fieldInspections[0].fieldInspectionDetailsForUpdate[" + elementIndex + "].roadOwner");
    roadOwner.setAttribute("id", "fieldInspectionDetailsForUpdate"+elementIndex+"roadOwner");
    cell10.appendChild(roadOwner); 
    $('#fieldInspectionDetailsForUpdate'+elementIndex+'roadOwner').html($("#fieldInspectionDetailsForUpdate0roadOwner").html());
    $('#fieldInspectionDetailsForUpdate'+elementIndex+'roadOwner').val('');
    
    newCol = document.createElement("td");
	newRow.appendChild(newCol); 
    var cell11 = row.insertCell(10);
    cell11.className = "text-center";
    var actions = document.createElement("a");
    actions.setAttribute("href","javascript:void(0)");
    actions.setAttribute("class","btn-sm btn-danger");
    actions.setAttribute("id","delete_insp_row");
   	actions.innerHTML = '<i class="fa fa-trash" id="delete_insp_row" ></i>';
    cell11.appendChild(actions); 
    patternvalidation();
    
    generateSno();
 }


$(document).on('click',"#delete_insp_row",function (){
	
	var table = document.getElementById('inspectionDetails');
    var rowCount = table.rows.length-1;
    console.log(" rowCount --> "+rowCount);
	//var rowIndex = $(this).parent().index();
	var rowIndex = $(this).closest('td').parent()[0].sectionRowIndex;	
	$(this).closest('tr').remove();
	
	generateSno();
	
	  $("#inspectionDetails tbody tr").each(function() {
			$(this).find("input, select, checkbox, hidden,textarea").each(function() {
				var index = $(this).closest('td').parent()[0].sectionRowIndex;
				console.log(" index --> "+ index);
				console.log(" rowIndex --> "+ rowIndex);
				if(index>=rowIndex){
					var increment = index++;
					$(this).attr({
						'name': function(_, name){
							return name.replace(/fieldInspectionDetailsForUpdate\[.\]/g, "fieldInspectionDetailsForUpdate["+increment+"]");
						},
						'id': function(_, id){
							if(id==undefined){
								return "";
							}
							return id.replace(/\d+/g, +increment);
						}
					});
				}
				
			});
	 });
  
});	


function enableDisableRoadInfo(obj){
	
	if($(obj).is(':checked')){
		$(obj).closest('tr').find('.roadLength, .roadOwner').removeAttr('disabled');
	}else{
		$(obj).closest('tr').find('.roadLength, .roadOwner').attr('disabled','disabled');
	}
}  

function generateSno()
{
	var idx=1;
	$(".serialNo").each(function(){
		$(this).text(idx);
		idx++;
	});
}	

function generateEstimationSeriaNo(){
	var index=1;
	$(".serialNumber").each(function(){
		$(this).text(index);
		index++;
	});
}

function showHideRoadInfo(){
	var tbl=document.getElementById("inspectionDetails");
    var tabLength = (tbl.rows.length)-1;
    for(var i=1;i<=tabLength;i++){
    	enableDisableRoadInfo(getControlInBranch(tbl.rows[i],'fieldInspectionDetailsForUpdate'+(i-1)+'roadDigging'));
    }
}


