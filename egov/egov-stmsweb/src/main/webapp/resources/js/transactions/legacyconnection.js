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
$(document).ready(function(){
	validateDemand(this);
	propertyID=$('#propertyIdentifier').val()
	if(propertyID != '')
		validateSewerageConnection();
	$('#propertyIdentifier').blur(function(){
		validateSewerageConnection();
	});
	
	if($('#executionDate').val() != ''){
		$("#legacyDemandDetails").show();	
	}
			
	$("#executionDate" ).datepicker({
		format: "dd/mm/yyyy",
		startDate: new Date(1998, 03, 1),
		autoclose: true 
	}).on('changeDate', function(ev) {
		if(isValidDate($('#executionDate').val())){
				clear_formDemandDetailTable();
		}
	});
	
	function isValidDate(s) {
	  var bits = s.split('/');
	  var d = new Date(bits[2] + '/' + bits[1] + '/' + bits[0]);
	  return !!(d && (d.getMonth() + 1) == bits[1] && d.getDate() == Number(bits[0]));
	}
	
	$('#shscNumber').blur(function(){
		validateSewerageConnectionNumber();
	});
	
	function validateSewerageConnection() {
		propertyID=$('#propertyIdentifier').val()
		if(propertyID != '') {
			$.ajax({
				url: "/stms/ajaxconnection/check-connection-exists",      
				type: "GET",
				data: {
					propertyID : propertyID  
				},
				dataType: "json",
				success: function (response) { 
					if(response != '') {
							resetPropertyDetails();
							bootbox.alert(response);
					}
					else {
						loadPropertyDetails();
					}
				}, 
				error: function (response) {
					resetPropertyDetails();
					bootbox.alert("connection validation failed");
				}
			});
		}		
	}
	
	
	function validateSewerageConnectionNumber(){
		shscNumber=$('#shscNumber').val()
		if(shscNumber != '' && shscNumber.length!=10){
			bootbox.alert("Please enter 10 digit SHSC number");
			return false;
		}
		if(shscNumber != '') {
			$.ajax({
				url: "/stms/ajaxconnection/check-shscnumber-exists",      
				type: "GET",
				data: {
					shscNumber : shscNumber  
				},
				dataType: "json",
				success: function (response) { 
					if(response != '') {
							$('#shscNumber').val('');
							bootbox.alert(response);
					}
				}, 
				error: function (response) {
					$('#shscNumber').val('');
					bootbox.alert("connection validation failed");
				}
			});
		}	
	}
	
	function clear_formDemandDetailTable(){
		var resultsTable = document.getElementById('legacyDemandDetails');
		var tablength = resultsTable.rows.length;
		if(tablength>1){
			for(var i = tablength; i >1 ;i--){   
			  resultsTable.deleteRow(i-1);
			}
		}
		formDemandDetailTable(); 
	}
	
	
	function formDemandDetailTable(){
		executionDate=$('#executionDate').val()
		if(executionDate != '') {
			$.ajax({
				url: "/stms/ajaxconnection/getlegacy-demand-details",      
				type: "GET",
				data: {
					executionDate : executionDate  
				},
				dataType: "json",
				success: function (response) { 
					if(response.length > 0) {
						$('#legacyDemandDetails').show();
						for(i=0; i<response.length; i++) {
							addDemandDetailRow(response[i].installment,response[i].reasonMaster,
									response[i].actualAmount,response[i].actualCollection,
									response[i].installmentId,response[i].demandReasonId);
						}
						bootbox.alert(response);
					}
				}, 
				error: function (response) {
					$('#executionDate').val('');
					bootbox.alert("connection validation failed"); 
				}
			});
		}	
	}
});


function loadPropertyDetails() {
	propertyID=$('#propertyIdentifier').val()
	if(propertyID != '') {
		$.ajax({
			url: "/ptis/rest/property/"+propertyID,      
			type: "GET",
			dataType: "json",
			success: function (response) { 
				if(response.ownerNames==null && response.boundaryDetails==null && response.propertyAddress==null){
					bootbox.alert("Property does not exist with property id : "+propertyID);
					return false;
				}
				if(response.boundaryDetails==null || response.boundaryDetails==''){
					bootbox.alert("Property does not exist with property id : "+propertyID);
					return false;
				}
					
				var waterTaxDue = getWaterTaxDue(propertyID);
						$('#propertyIdentifierError').html('');
						applicantName = '';
							if(response.ownerNames!=null) {
							for(i=0; i<response.ownerNames.length; i++) {
								if(applicantName == '')
									applicantName = response.ownerNames[i].ownerName;
								else 							
									applicantName = applicantName+ ', '+response.ownerNames[i].ownerName;
								}
							if(response.ownerNames[0].mobileNumber != '')
								$("#mobileNumber").val(response.ownerNames[0].mobileNumber);
							if(response.ownerNames[0].emailId != '')
								$("#email").val(response.ownerNames[0].emailId);
							$("#aadhaar").val(response.ownerNames[0].aadhaarNumber);

							}
						$("#applicantname").val(applicantName);
						if(response.propertyDetails != null && response.propertyDetails.noOfFloors!=null)
						$("#nooffloors").val(response.propertyDetails.noOfFloors);
						if(response.propertyAddress!=null)
							$("#propertyaddress").val(response.propertyAddress);
						else
							$("#propertyaddress").val('');
						
						boundaryData = '';
						if(response.boundaryDetails!=null){
							if(response.boundaryDetails.zoneName != null && response.boundaryDetails.zoneName != '')
								boundaryData = response.boundaryDetails.zoneName;
							if(response.boundaryDetails.wardName != null && response.boundaryDetails.wardName != '') {
								if(boundaryData == '')
									boundaryData = response.boundaryDetails.wardName;
								else
									boundaryData = boundaryData + " / " + response.boundaryDetails.wardName;
							}
							if(response.boundaryDetails.blockName != null && response.boundaryDetails.blockName != '') {
								if(boundaryData == '')
									boundaryData = response.boundaryDetails.blockName;
								else
									boundaryData = boundaryData + " / " +response.boundaryDetails.blockName; 
							}
							$("#locality").val(response.boundaryDetails.localityName);

						}
						$("#zonewardblock").val(boundaryData);
						if(response.propertyDetails != null && response.propertyDetails.currentTax!=null)
						$("#propertytax").val(response.propertyDetails.currentTax);
						if(waterTaxDue['PROPERTYID']!="")
							$('#watercharges').val(waterTaxDue['CURRENTWATERCHARGE']);
						else
							$('#watercharges').val('N/A');
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	}		
}

function resetPropertyDetails() {
	$('#propertyIdentifier').val('');
	$('#applicantname').val('');
	$('#mobileNumber').val('');
	$('#email').val('');
	$('#aadhaar').val('');	
	$('#propertyaddress').val('');
	$('#locality').val('');
	$('#zonewardblock').val('');
	$('#nooffloors').val('');
	$('#propertytax').val('0.00');
	$('#watercharges').val('0.00');

}

function getWaterTaxDue(propertyID) {
	var result;
	if(propertyID != "") {
		$.ajax({
			url: "/stms/ajaxconnection/check-watertax-due",
				type: "GET",
				'async':false,
				cache: true,
				data: {
					assessmentNo : propertyID
				},
				dataType: "json",
		}).done(function(value) {
				result = value; 
		});
		return result;
	}
}

$("#submit").click(function() {
	var table = document.getElementById('legacyDemandDetails');
    var rowCount = table.rows.length;
    var index = rowCount -2;
    var i;
    
    for(i=0; i<= rowCount-2; i++){
    	var index1 = i;
    	actualCollection = $('#demandDetailBeanList'+ index1 +'actualCollection').val();
    	if(actualCollection != "")
    		$('#demandDetailBeanList'+index1+'actualAmount').attr('required','required');
    	else
    		$('#demandDetailBeanList'+index1+'actualAmount').removeAttr('required');
    }
    
    for(i=0; i<= index; i++){
    	if($('#demandDetailBeanList'+i+'actualAmount').val() != ''){
    		var j=i+1;
    		for(k=j; k<= index; k++)
    			$('#demandDetailBeanList'+k+'actualAmount').attr('required', 'required');
    	}
    	if(i===index){
    		if($('#demandDetailBeanList'+i+'actualAmount').val() === ''){
    			$('#demandDetailBeanList'+i+'actualAmount').attr('required', 'required');
    		}
    		if( $('#demandDetailBeanList'+i+'actualCollection').val() === ''){
    			$('#demandDetailBeanList'+i+'actualCollection').attr('required','required');
    		}
    			
    	}
    }
    
	if($('form').valid()){
		if(!validateDemandDetailsOnSubmit()){
		return false;
		}
		else{
			return true;
		}
	}
	else{
		return false;
	}
});

function validateDemandDetailsOnSubmit(){
	var tbl=document.getElementById("legacyDemandDetails");
    var lastRow = (tbl.rows.length)-1;
    var instlmnt,demandamount,collectionamount;
    var mandatoryValEntered=false, mandatoryValNotEntered=false;
    for(var i=1;i<=lastRow;i++){
    	instlmnt=getControlInBranch(tbl.rows[i],'demandDetailBeanList'+(i-1)+'installment').value;
    	demandamount=getControlInBranch(tbl.rows[i],'demandDetailBeanList'+(i-1)+'actualAmount').value;
    	collectionamount=getControlInBranch(tbl.rows[i],'demandDetailBeanList'+(i-1)+'actualCollection').value;
        if((demandamount!='' && collectionamount!='' &&  parseFloat(collectionamount) > parseFloat(demandamount))) { 
           	bootbox.alert("Collection cannot be more than Demand for installment \""+instlmnt+"\".");
     		return false; 
     	}
        
    } 
    return true;
}

function loadDonationAmount(){
	var propertytype = document.getElementById('propertyType').value;
	var noofclosetsnonresidential = document.getElementById('noOfClosetsNonResidential').value;
	var noofclosetsresidential = document.getElementById('noOfClosetsResidential').value;
	if(propertytype=="MIXED" && noofclosetsresidential=="" || propertytype=="MIXED" && noofclosetsnonresidential==""){
		return false;
	}
	else{
		fetchdonationamount();
	}
}

function fetchdonationamount(){
	var noofclosetsresidential=0;
	var noofclosetsnonresidential=0;
	var propertytype = document.getElementById('propertyType').value;
	if(propertytype=='RESIDENTIAL')
		noofclosetsresidential = document.getElementById('noOfClosetsResidential').value;
	else if(propertytype=='NON_RESIDENTIAL')
		noofclosetsnonresidential = document.getElementById('noOfClosetsNonResidential').value;
	else{
		noofclosetsresidential = document.getElementById('noOfClosetsResidential').value;
		noofclosetsnonresidential = document.getElementById('noOfClosetsNonResidential').value;
	}
	
	if(propertytype != '' ) {
		$.ajax({
			url: "/stms/ajaxconnection/getlegacy-donation-amount",      
			type: "GET",
			data: {
				propertytype : propertytype,
				noofclosetsresidential : noofclosetsresidential,
				noofclosetsnonresidential : noofclosetsnonresidential
			},
			dataType: "json",
			success: function (response) { 
				if(jQuery.isNumeric(response)){
				var inspectionDtlTable = document.getElementById('inspectionChargesDetails');
				var rowCount = inspectionDtlTable.rows.length;
				var i;
				for(i=0;i<=rowCount-1;i++){
				var feeName = $(inspectionDtlTable.rows[i].cells[1]).text();
				if(feeName.trim()==="Donation Charge"){
					var j=i-1;
					var donation = document.getElementById('feesDetail'+j+'amount').value;
					$('#feesDetail'+j+'amount').attr('value', parseFloat(response));
				}
				}
				return true;
				}
				else{
					bootbox.alert(response);
					return false;
				}
			}, 
			error: function (response) {
				bootbox.alert("connection validation failed");
				return false;
			}
		});
	}		
	
}

function addDemandDetailRow(instlmntDesc,reasondsc,demandamount,collectionamount,instlmntId,dmndReasonId) {
    var table = document.getElementById('legacyDemandDetails');
    var rowCount = table.rows.length;
  
    var row = table.insertRow(rowCount); 
    var counts = rowCount;

	elementIndex = counts;
    var newRow = document.createElement("tr");
	var newCol = document.createElement("td");
	newRow.appendChild(newCol);
	 
    var cell1 = row.insertCell(0);
    cell1.className="text-center";
    var instllmnt = document.createElement("input");
    var att = document.createAttribute("class");
	att.value = "form-control";
	instllmnt.setAttributeNode(att); 
	instllmnt.type = "text";
	instllmnt.setAttribute("readonly", "readonly"); 
	instllmnt.setAttribute("required", "required");
	instllmnt.setAttribute("maxlength", "18");
	instllmnt.setAttribute("name", "demandDetailBeanList[" + (elementIndex-1) + "].installment");
	instllmnt.setAttribute("id", "demandDetailBeanList"+(elementIndex-1)+"installment");
	instllmnt.setAttribute("value", instlmntDesc);
	cell1.appendChild(instllmnt);
    
    newCol = document.createElement("td");        
    newRow.appendChild(newCol);
    var cell2 = row.insertCell(1);
    cell2.className = "text-center";
    var reasonDesc = document.createElement("input");
    var att = document.createAttribute("class");
	att.value = "form-control";
	reasonDesc.setAttributeNode(att); 
	reasonDesc.type = "text";
	reasonDesc.setAttribute("readonly", "readonly"); 
	reasonDesc.setAttribute("required", "required");
	reasonDesc.setAttribute("maxlength", "18");
	reasonDesc.setAttribute("name", "demandDetailBeanList[" + (elementIndex-1) + "].reasonMaster");
	reasonDesc.setAttribute("id", "demandDetailBeanList"+(elementIndex-1)+"reasonMaster");
	reasonDesc.setAttribute("value", reasondsc);
    cell2.appendChild(reasonDesc);
    
    newCol = document.createElement("td");
	newRow.appendChild(newCol);
    var cell3 = row.insertCell(2);
    cell3.className = "text-right";
    var actualAmount = document.createElement("input");
    actualAmount.setAttribute("class","form-control table-input text-right patternvalidation");
    actualAmount.setAttribute("data-pattern","decimalvalue"); 
    actualAmount.type = "text";
    actualAmount.setAttribute("maxlength", "7");
    actualAmount.setAttribute("name", "demandDetailBeanList[" + (elementIndex-1) + "].actualAmount");
    actualAmount.setAttribute("id", "demandDetailBeanList"+(elementIndex-1)+"actualAmount");
    actualAmount.setAttribute("onkeyup","validateDemand(this);");
    actualAmount.setAttribute("onclick","validateDemand(this);");
    actualAmount.setAttribute("onblur","validateDemand(this);");
    actualAmount.setAttribute("autocomplete","off");

    cell3.appendChild(actualAmount);  
    
    newCol = document.createElement("td");
	newRow.appendChild(newCol);
    var cell4 = row.insertCell(3);
    cell4.className = "text-right";
    var actualCollection = document.createElement("input");
    actualCollection.setAttribute("class","form-control table-input text-right patternvalidation");
    actualCollection.setAttribute("data-pattern","decimalvalue"); 
    actualCollection.type = "text";
    actualCollection.setAttribute("maxlength", "7");
    actualCollection.setAttribute("name", "demandDetailBeanList[" + (elementIndex-1) + "].actualCollection");
    actualCollection.setAttribute("id", "demandDetailBeanList"+(elementIndex-1)+"actualCollection");
    actualCollection.setAttribute("autocomplete","off");
    cell4.appendChild(actualCollection);  
    
    var installmentId = document.createElement("input");
    installmentId.setAttribute("class","form-control");
    installmentId.setAttribute("data-pattern","decimalvalue"); 
    installmentId.type = "hidden";
    installmentId.setAttribute("name", "demandDetailBeanList[" + (elementIndex-1) + "].installmentId");
    installmentId.setAttribute("id", "demandDetailBeanList"+(elementIndex-1)+"installmentId");
    installmentId.setAttribute("value", instlmntId);
    cell4.appendChild(installmentId);  
    
    var demandReasonId = document.createElement("input");
    demandReasonId.setAttribute("class","form-control");
    demandReasonId.setAttribute("data-pattern","decimalvalue"); 
    demandReasonId.type = "hidden";
    demandReasonId.setAttribute("name", "demandDetailBeanList[" + (elementIndex-1) + "].demandReasonId");
    demandReasonId.setAttribute("id", "demandDetailBeanList"+(elementIndex-1)+"demandReasonId");
    demandReasonId.setAttribute("value", dmndReasonId);
    cell4.appendChild(demandReasonId); 
    patternvalidation();
}

$("#amountCollected,#feesDetail1amount")
.blur(
	function() {
		var amountCollected = $('#amountCollected').val();
		var donationCharge = $('#feesDetail1amount').val();
		if(amountCollected){
		if (parseFloat(amountCollected) > parseFloat(donationCharge)) {
			alert("Collected Donation Charge should not be greater than the actual donation charge");
			$('#amountCollected').val('');
			$('#pendingAmtForCollection').val('');
		}
		else{
			$("#pendingAmtForCollection").val( parseFloat(donationCharge)
					- parseFloat(amountCollected));
		}
		
	}
});

function validateDemand(obj) {
	$( "input[name$='actualAmount']" ).on("keyup", function(){
	    var valid = /^[1-9](\d{0,9})(\.\d{0,3})?$/.test(this.value),
	        val = this.value;
	    
	    if(!valid){
	        console.log("Invalid input!");
	        this.value = val.substring(0, val.length - 1);
	    }
	});
}