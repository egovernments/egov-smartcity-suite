<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>

<style>
#warning {
	display: none;
	color: blue;
}
</style>
<script>

designationLoadHandler = function(req,res){
  results=res.results;
  dom.get("designation").value=results[0].Designation;
}

designationLoadFailureHandler= function(){ 
    dom.get("errorstyle").style.display='';
	document.getElementById("errorstyle").innerHTML='<s:text name="negotiationheader.desg.error" />';
}
function clearDesignation(elem) {
    dom.get("designationNegotiation").value='';
}
function setupPreparedByList(elem){
	deptId=elem.options[elem.selectedIndex].value;
    populatenegotiationPreparedBy({executingDepartment:deptId});
  
}
function showDesignation(elem){
	var empId = elem.options[elem.selectedIndex].value;
    makeJSONCall(["Designation"],'${pageContext.request.contextPath}/estimate/ajaxEstimate!designationForUser.action',{empID:empId},designationLoadHandler,designationLoadFailureHandler) ;
}

/*  for negotiation */
designationNegotiationLoadHandler = function(req,res){
  results=res.results;
  dom.get("designationNegotiation").value=results[0].Designation;
}

designationNegotiationLoadFailureHandler= function(){
    dom.get("errorstyle").style.display='';
	document.getElementById("errorstyle").innerHTML='<s:text name="negotiationheader.desg.error" />';
}

function showNegotiationDesignation(elem){
	var empId = elem.options[elem.selectedIndex].value;
    makeJSONCall(["Designation"],'${pageContext.request.contextPath}/tender/ajaxTenderNegotiation!designationForUser.action',{empID:empId},designationNegotiationLoadHandler,designationNegotiationLoadFailureHandler) ;
}

designationWOInchargeHandler = function(req,res){
  results=res.results;
  dom.get("woInchargeDesignation").value=results[0].Designation;
}

designationWOInchargeFailureHandler= function(){
    dom.get("errorstyle").style.display='';
	document.getElementById("errorstyle").innerHTML='<s:text name="negotiationheader.desg.error" />';
}

function showWOInchargeDesignation(elem){
	var empId = elem.options[elem.selectedIndex].value;
    makeJSONCall(["Designation"],'${pageContext.request.contextPath}/tender/ajaxTenderNegotiation!designationForUser.action',{empID:empId},designationWOInchargeHandler,designationWOInchargeFailureHandler) ;
}

function assignsign(obj){
var persign = dom.get('percSign').value;
if(persign=='1'){
      objVal = -+obj.value;
      return objVal;
  }else{
     return obj.value;;
  }
}
/* for negotiation */


function roundOffEstimateAmount() {
	if(document.tenderNegotiationForm.estimateId.value!="")
		document.tenderNegotiationForm.estimateAmount.value=roundTo('<s:property value="%{abstractEstimate.workValue}" />');
	else if(document.tenderNegotiationForm.worksPackageId.value!="")
		document.tenderNegotiationForm.estimateAmount.value=roundTo('<s:property value="%{worksPackage.totalAmount}" />');
}

function validateHeaderBeforeSubmit(tenderNegotiationForm) {

	var tenderNo = tenderNegotiationForm.tenderNo.value;
	if(tenderNo=='') {
		dom.get("negotiation_error").style.display='';     
		document.getElementById("negotiation_error").innerHTML='<s:text name="tenderHeader.tenderNo.null" />';
		//diableTenderType();
		return false
	}
	
	var tenderDate = tenderNegotiationForm.tenderDate.value;
	var tenderNegotiationDate=tenderNegotiationForm.negotiationDate.value;
	var currentDate='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
			
	if(tenderDate=='') {
		dom.get("negotiation_error").style.display='';     
		document.getElementById("negotiation_error").innerHTML='<s:text name="tenderHeader.tenderDate.null" />';
		window.scroll(0,0);
		return false
	}
	
	if(compareDate(formatDate6(tenderNegotiationDate),formatDate6(currentDate)) == -1){
        dom.get("negotiation_error").style.display='';     
		document.getElementById("negotiation_error").innerHTML='<s:text name="tenderHeader.negotiationDate.greaterthanCurrentDate" />';
		window.scroll(0,0);
		return false  	
	}
	
	if(compareDate(formatDate6(tenderDate),formatDate6(currentDate)) == -1){
        dom.get("negotiation_error").style.display='';     
		document.getElementById("negotiation_error").innerHTML='<s:text name="tenderHeader.tenderDate.greaterthanCurrentDate" />';
		window.scroll(0,0);
		return false  	
	}
	
	if(compareDate(formatDate6(tenderNegotiationDate),formatDate6(tenderDate)) == 1){
        dom.get("negotiation_error").style.display='';     
		document.getElementById("negotiation_error").innerHTML='<s:text name="tenderHeader.negotiationDate.lessthanTenderDate" />';
		window.scroll(0,0);
		return false  	 
	}
	
	if(document.tenderNegotiationForm.estimateId.value!=""){
		var estimateAdminApprovalDate='<s:property value="%{abstractEstimate.currentStateCreatedDate}" />';
		if(compareDate(formatDate6(tenderDate),formatDate6(estimateAdminApprovalDate)) == 1){
		 	dom.get("negotiation_error").style.display='';     
			document.getElementById("negotiation_error").innerHTML='<s:text name="tenderHeader.tenderDate.lessthan.estimateAdminApprvDate" />';
			window.scroll(0,0);
			return false  	 
		}
	}
	
	if(!checkContractorDetails())
		return;
    var tenderTypeIndex = document.tenderNegotiationForm.tenderType.selectedIndex;
	var tenderTypeValue=document.tenderNegotiationForm.tenderType.options[tenderTypeIndex].value;
	if (tenderTypeIndex == "0" || tenderTypeValue == "-1") {       
        dom.get("negotiation_error").style.display='';     
        document.getElementById("negotiation_error").innerHTML='<s:text name="tenderEstimate.tenderType.null" />';
        return false;
    }  
    
     if(document.tenderNegotiationForm.tenderType.value==dom.get("tenderType")[1].value){
	 	if(tenderNegotiationForm.percQuotedRateAmount.value==""){
	 		dom.get("negotiation_error").style.display='';
	 		document.getElementById("negotiation_error").innerHTML='<s:text name="tenderNegotiation.nonNumeric.quotedAmount" />';
	 		window.scroll(0,0);
	 		return false;
	 	}
	 	if(tenderNegotiationForm.percQuotedRateAmount.value!=""){
	 		if(!validate_Number(tenderNegotiationForm.percQuotedRateAmount)){
	 			window.scroll(0,0);
	 			return false;
	 		}
	 	}
	}
    
    // checking negotiationPreparedBy mandatory
    	var negotiationPreparedByObj=document.getElementById("negotiationPreparedBy");
		var negotiationPreparedByIndex = negotiationPreparedByObj.selectedIndex;
		var negotiationPreparedByValue=negotiationPreparedByObj.options[negotiationPreparedByIndex].value;
		if(negotiationPreparedByValue == "-1"){
		  dom.get("negotiation_error").style.display='';
		  document.getElementById("negotiation_error").innerHTML='<s:text name="tenderHeader.negotiationPreparedBy.null" />';		  
		  document.tenderNegotiationForm.negotiationPreparedBy.focus();		
		
		  return false;		  	
		}
		
    // checking status
		var statusObj=document.getElementById("status");		
		if(statusObj!=null){
		var statusIndex = statusObj.selectedIndex;
		var statusValue=statusObj.options[statusIndex].value;
		if(statusValue == "-1"){
		  dom.get("negotiation_error").style.display='';
		  document.getElementById("negotiation_error").innerHTML='<s:text name="contractorDetails.status.required" />';		  
		  document.tenderNegotiationForm.status.focus();
		 
		  return false;		  	
		}
	}		
		//approvedBy
		var approvedByObj=document.getElementById("approvedBy");
		if(approvedByObj!=null){
		var approvedByIndex = approvedByObj.selectedIndex;
		var approvedByValue=approvedByObj.options[approvedByIndex].value;
		if(approvedByValue == "-1"){
		  dom.get("negotiation_error").style.display='';
		  document.getElementById("negotiation_error").innerHTML='<s:text name="negotiationheader.approvedBy.error" />';
		  document.tenderNegotiationForm.approvedBy.focus();
		  return false;		  	
		}
	
	}	

if(statusObj!=null){
		if(statusObj.options[statusIndex].text=='Work OrderIssued'){
		
				if(document.getElementById("securityDeposit").value==''){
					document.getElementById("securityDeposit").value="0.0"
				}
				
				if(document.getElementById("labourWelfareFund").value==''){
					document.getElementById("labourWelfareFund").value="0.0"
				}
									
				if(!checkForNumber(document.getElementById("emdAmountDeposited").value) || !checkForNumber(document.getElementById("securityDeposit").value)
		 		|| !checkForNumber(document.getElementById("labourWelfareFund").value)){	
				    dom.get("negotiation_error").style.display='';
					document.getElementById("negotiation_error").innerHTML='<s:text name="negotiationheader.emdAmountDeposited.error" />';
				
					return false;	
				}

				if(document.getElementById("emdAmountDeposited").value=='' || document.getElementById("emdAmountDeposited").value=='0.00' || document.getElementById("emdAmountDeposited").value=='0'){

				 	dom.get("negotiation_error").style.display='';
			  	 	document.getElementById("negotiation_error").innerHTML='<s:text name="negotiationheader.emdAmountDeposited.error" />';
				 	document.tenderNegotiationForm.emdAmountDeposited.focus();
				 	
				 	return false;	
				}	
				var ans=confirm('<s:text name="negotiation.workordergeneration.warning"/>');
				if(ans){
		
					//document.getElementById("statusText").value="workOrdergenerate";
					return true;
				}
				else
				{
					 disableFields();
					  return false;		
				}
		}    
 }
    return true;
}

function checkForNumber(sText)
{   
   if(isNaN(sText)){
    return false; 
   }
   var ValidChars = "0123456789.";
   var IsNumber=true;
   var Char;
//var y=sText.replace(/,/g,''); 
   for (i = 0; i < sText.length && IsNumber == true; i++) 
      { 
      Char = sText.charAt(i);
      if (ValidChars.indexOf(Char) == -1) 
         {
         IsNumber = false;
         }
      }
   return IsNumber;
  }


function checkDate(obj){
	if(!validateDateFormat(obj)) {
    		dom.get('errornegotiationDate').style.display='none';
		dom.get("negotiation_error").style.display='';
		document.getElementById("negotiation_error").innerHTML='<s:text name="invalid.fieldvalue.negotiationDate" />';
		document.tenderNegotiationForm.negotiationDate.focus();
    	return false;
	}
	else {
		dom.get("negotiation_error").style.display='none';
		return true;
	}
}
//check approved  date
function checkApprovedDate(obj){

	if(!validateDateFormat(obj)) {
    	dom.get('errorapprovedDate').style.display='none';
		dom.get("negotiation_error").style.display='';
		document.getElementById("negotiation_error").innerHTML='<s:text name="invalid.fieldvalue.approvedDate" />';
		document.tenderNegotiationForm.approvedDate.focus();
    	return;
	}
	else {	
		dom.get("negotiation_error").style.display='none';	
	}
}

function searchContractor(rowId){
	var a = new Array(2);	
	
	var date=document.tenderNegotiationForm.negotiationDate.value;
	if(date=='') { 
		dom.get("negotiation_error").style.display='';
    	document.getElementById("negotiation_error").innerHTML='<s:text name="tenderResponse.negotiationDate.null" />';
    	return;
	}
	else {
		dom.get("negotiation_error").style.display='none';
	}
	if(!checkDate(document.tenderNegotiationForm.negotiationDate)) 
		return;
	
	window.open("../masters/contractor-searchPage.action?searchDate="+date+"&rowId="+rowId,"", "height=650,width=980,scrollbars=yes,left=0,top=0,status=yes");	
}

function update(elemValue) {	
	if(elemValue!="" || elemValue!=null) {
	
		if(elemValue=="success") {	
	   		document.tenderNegotiationForm.actionName.value='approve'; 
	 		document.tenderNegotiationForm.action="${pageContext.request.contextPath}/tender/tenderNegotiation!save.action";
	  		document.tenderNegotiationForm.submit();	
		}
		else {
			var a = elemValue.split("`~`");
			var records= contractorsDataTable.getRecordSet();
			var row_id=a[0];
			var contractor_id=a[1];
			var contractor_code=a[2];
			var contractor_name=a[3];
			if(!validateDuplicateContractor(records, contractor_id))
				return false;
			if(dom.get("code"+records.getRecord(getNumber(row_id)).getId()).value!=""){
				var ans=confirm('<s:text name="negotiation.contractorDelete.warning"/>');
				if(!ans)
					return false;
				deleteContractorDetails(dom.get("contractor"+records.getRecord(getNumber(row_id)).getId()).value);
			}
			
			dom.get("code"+records.getRecord(getNumber(row_id)).getId()).value=contractor_code;
			dom.get("name"+records.getRecord(getNumber(row_id)).getId()).value=contractor_name;
			dom.get("contractor"+records.getRecord(getNumber(row_id)).getId()).value=contractor_id;
			dom.get("code"+records.getRecord(getNumber(row_id)).getId()).disabled=true;		
			dom.get("name"+records.getRecord(getNumber(row_id)).getId()).disabled=true;	
			
			//To Add the contractor details
			addContractorDetails(contractor_id,contractor_code,contractor_name);
			
		}	
	}
	setL1BidderName();
}
function addContractorDetails(contractor_id,contractor_code,contractor_name){
	var contractorListObj=document.getElementById('contractorList');
	
			var contractorcount=contractorListObj.length-1; 
			try{	
				contractorListObj.add(new Option(contractor_code+'~'+contractor_name, contractor_id), null); 
 			}
			catch(e){ //for IE
 				contractorListObj.add(new Option(contractor_code+'~'+contractor_name, contractor_id)); 
			}
			
			var column='contractordetails';
  			for(var i=0;i<itemRateDataTable.getRecordSet().getLength();i++)
  			{
    			var contractorid="contractorId_"+contractor_id+"_contractordetails"+itemRateDataTable.getRecord(i).getId();
    			var contractorcode="contractorCode_"+contractor_id+"_contractordetails"+itemRateDataTable.getRecord(i).getId();
    			var quotedrate_id="quotedRate_"+contractor_id+"_contractordetails"+itemRateDataTable.getRecord(i).getId();
    			var quotedAmount_id="quotedAmount_"+contractor_id+"_contractordetails"+itemRateDataTable.getRecord(i).getId();
    	
    			var contractorIdfield="actionTenderResponseActivities[" + itemRateDataTable.getRecord(i).getCount() + "].tenderResponseQuotes["+contractorcount+"].contractor.id";
    			var contractorCodefield="actionTenderResponseActivities[" + itemRateDataTable.getRecord(i).getCount() + "].tenderResponseQuotes["+contractorcount+"].contractor.code";
    			var quotedRatefield="actionTenderResponseActivities[" + itemRateDataTable.getRecord(i).getCount() + "].tenderResponseQuotes["+contractorcount+"].quotedRate";
    			var quotedAmountfield="actionTenderResponseActivities[" + itemRateDataTable.getRecord(i).getCount() + "].tenderResponseQuotes["+contractorcount+"].quotedAmount";
    	
		    	var quotedRateValue=itemRateDataTable.getRecord(i).getData("estimatedRate");
		  		var quotedAmountValue=roundTo(quotedRateValue*getNumber(dom.get('uomFactor'+itemRateDataTable.getRecord(i).getId()).value)*getNumber(itemRateDataTable.getRecord(i).getData("estimatedQuantity")));
		   		var newdivTag = document.createElement("div");
		   		newdivTag.style.border="0";
           	    newdivTag.id = "contractorTable"+contractor_id+"["+ itemRateDataTable.getRecord(i).getCount() + "]";
                newdivTag.innerHTML = "<table style='border-style: none;' cellspacing='0' cellpadding='0'><tr><td style='border-style: none;'><input type='hidden' id='"+contractorid+"' name='"+contractorIdfield+"' value='"+contractor_id+"'/><input type='text' class='selectwk' id='"+contractorcode+"' name='"+contractorCodefield+"' value='"+contractor_code+"' size='10' readOnly='true'/></td><td style='border-style: none;'><input type='text' class='selectamountwk' id='"+quotedrate_id+"' name='"+quotedRatefield+"' value='"+quotedRateValue+"' size='10' maxlength='10' onblur='calculateQRAmountNew(this,\""+itemRateDataTable.getRecord(i).getId()+"\",\""+column+"\");' /><span id='error"+quotedrate_id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span></td><td style='border-style: none;'><input type='text' class='selectamountwk' id='"+quotedAmount_id+"' name='"+quotedAmountfield+"' value='"+quotedAmountValue+"' size='10' maxlength='10' readOnly='true' /></td></tr></table>";
		 		document.getElementById("contractorT["+ itemRateDataTable.getRecord(i).getCount() + "]").appendChild(newdivTag);
		   		if(!document.getElementById("quotedTotal_"+contractor_id))
		   		{
		   			var newtotaldivTag = document.createElement("div");
		   			newtotaldivTag.style.border="0";
           	   		newtotaldivTag.id = "quotedTotal_"+contractor_id;
            		newtotaldivTag.innerHTML = "<table style='border-style: none;' width='100%'><tr><td  style='border-style: none;' width='65%'>"+contractor_code+"</td><td style='border-style: none;' width='35%'>0.0</td></tr></table>";
            		document.getElementById("quotedTotal").appendChild(newtotaldivTag);
            	}
   		 	
     			
    		}
    		calculateQuotedTotalForContractors();
}

function validateDuplicateContractor(records, contractorId) {
    for(i=0;i<records.getLength();i++){       
       if(dom.get("contractor"+records.getRecord(i).getId()).value==contractorId){
          dom.get("negotiation_error").style.display='';
          document.getElementById("negotiation_error").innerHTML='<s:text name="negotiation.contractor.duplicate"/>';
          window.scroll(0,0);
          return false;
       }
    }
    return true;
}

function getMarketValueEstimateAmount(){
	var date=document.tenderNegotiationForm.negotiationDate.value;
	if(date=='') {
		dom.get("negotiation_error").style.display='';
    	document.getElementById("negotiation_error").innerHTML='<s:text name="tenderResponse.negotiationDate.null" />';
    	return;
	}
	else if(!validateDateFormat(document.tenderNegotiationForm.negotiationDate)) {
    	dom.get('errornegotiationDate').style.display='none';
    	return;
	}
	else {
		dom.get("negotiation_error").style.display='none';
	}
	if(document.tenderNegotiationForm.estimateId.value!="")
    	makeJSONCall(["MarketRateEstimateAmount"],'${pageContext.request.contextPath}/tender/ajaxTenderNegotiation!getMarketValue.action',{asOnDate:date, estimateId:document.tenderNegotiationForm.estimateId.value},marketRateEstimateAmountLoadHandler ,marketRateEstimateAmountLoadFailureHandler) ;
    else if(document.tenderNegotiationForm.worksPackageId.value!="")
        makeJSONCall(["MarketRateEstimateAmount"],'${pageContext.request.contextPath}/tender/ajaxTenderNegotiation!getMarketValue.action',{asOnDate:date, packageId:document.tenderNegotiationForm.worksPackageId.value},marketRateEstimateAmountLoadHandler ,marketRateEstimateAmountLoadFailureHandler) ;
}

marketRateEstimateAmountLoadHandler = function(req,res){
  	results=res.results;
  	dom.get("marketRateEstimateAmount").value=roundTo(results[0].MarketRateEstimateAmount);
  	if(document.tenderNegotiationForm.tenderType.value==dom.get("tenderType")[1].value) {		
  		getTenderPercAgainstMarketRate();
  	}
}

marketRateEstimateAmountLoadFailureHandler= function(){
    dom.get("negotiation_error").style.display='';
    document.getElementById("negotiation_error").innerHTML='<s:text name="tenderNegotiation.marketValue_EstimateAmount" />';
}


function clearContractor(){
	var negotiationDate=document.tenderNegotiationForm.negotiationDate.value;
	var id=document.tenderNegotiationForm.id.value;
	var hiddenNegotiationDate=document.forms[0].hiddenNegotiationDate.value;
	if(negotiationDate!='' && hiddenNegotiationDate!= '' && hiddenNegotiationDate!=negotiationDate){
		popup('popUpDiv');		
	}
	else if(id!='' && id!=undefined && negotiationDate!=''){
		popup('popUpDiv');
	}
	document.tenderNegotiationForm.priorHiddenNegotiationDate.value=document.tenderNegotiationForm.hiddenNegotiationDate.value;
	document.tenderNegotiationForm.hiddenNegotiationDate.value=negotiationDate;
}

function resetNegotiationDate(){

	document.tenderNegotiationForm.negotiationDate.value=document.tenderNegotiationForm.priorHiddenNegotiationDate.value;
	document.tenderNegotiationForm.hiddenNegotiationDate.value=document.tenderNegotiationForm.priorHiddenNegotiationDate.value;
	document.tenderNegotiationForm.asOnDate.value=document.tenderNegotiationForm.negotiationDate.value;
}

function resetContractorDetails(){
	contractorsDataTable.deleteRows(0,contractorsDataTable.getRecordSet().getLength());
	contractorsDataTable.addRow({SlNo:contractorsDataTable.getRecordSet().getLength()+1});
	var contractorListObj=document.getElementById('contractorList');
	for (var i = contractorListObj.length - 1; i>0; i--) {
    		contractorListObj.remove(i);
  	}
  	document.getElementById("quotedTotal").innerHTML="";
	itemRateDataTable.render();
	calculateQuotedTotalForContractors();
}

function defaultAsOnDate(){
	document.tenderNegotiationForm.asOnDate.value=document.tenderNegotiationForm.negotiationDate.value;
}

function toggleDetails(obj) {	 
	if(obj.value!='-1') {
		if(obj.value==dom.get("tenderType")[1].value) {
			document.getElementById("percQuotedRow").style.display='';
			$("percQuotedRow1").show();
			document.tenderNegotiationForm.asOnDate.value=document.tenderNegotiationForm.negotiationDate.value;
			getTenderPercAgainstMarketRate();
			document.getElementById("rateContractTable").style.display='';
			document.getElementById("itemRateContractTable").style.display='none';
			document.getElementById("itemRateTable").style.display='none';			
		}
		else if(obj.value==dom.get("tenderType")[2].value) {
			getCurrentDate();
			document.getElementById("percQuotedRow").style.display='none';	
			$("percQuotedRow1").hide();
			document.getElementById("rateContractTable").style.display='none';
			document.getElementById("itemRateContractTable").style.display='';	
			document.getElementById("itemRateTable").style.display='';					
		}
	}
	else {
		document.getElementById("percQuotedRow").style.display='none';
		$("percQuotedRow1").hide();
		document.getElementById("rateContractTable").style.display='none';
		document.getElementById("itemRateContractTable").style.display='none';
	}
}

function getQutAmt(){
	getQuotedAmount(dom.get('percQuotedRateAmount'));
}

function getQuotedAmount(obj) {
	if(obj.value!='') {
	var objVal = obj.value;
     objVal=assignsign(obj);
     dom.get('percQuotedRate').value=objVal;
     document.tenderNegotiationForm.percQuotedAmount.value=eval(document.tenderNegotiationForm.estimateAmount.value)+eval(document.tenderNegotiationForm.estimateAmount.value*objVal)/100;
	 document.tenderNegotiationForm.percQuotedAmount.value=roundTo(document.tenderNegotiationForm.percQuotedAmount.value);
	 }
	 else {
		document.tenderNegotiationForm.percQuotedAmount.value='';
		dom.get('percQuotedRate').value='';
	}
}

function negotaiationDateChangeFromCalender() {
	var negotiationDate=document.forms[0].negotiationDate.value;
	var hiddenNegotiationDate=document.forms[0].hiddenNegotiationDate.value;
	if(hiddenNegotiationDate!='' && negotiationDate!='' && hiddenNegotiationDate!=negotiationDate){
		clearContractor();		
	}	
	document.forms[0].hiddenNegotiationDate.value=negotiationDate;
}

<!-- start setstatus -->
function toggleApprovedBy(newStatus){

	var statusObj=document.getElementById("status");
	var statusIndex = statusObj.selectedIndex;
	var statusValue=statusObj.options[statusIndex].text;
	
	if(statusObj.options[statusIndex].text=='Work OrderIssued'){
	  toggleForSelectedFields(false,['emdAmountDeposited','securityDeposit','labourWelfareFund','woEncharge']);
	  document.getElementById("workOrderamountdetails").style.display='';	
			
	} 
	else{

	  document.getElementById("workOrderamountdetails").style.display='none';	
	}	
}
function uniqueCheckOntenderNumber(obj)
{
	tenderNo = dom.get('tenderNo').value;
	id = dom.get('id').value;
		if(tenderNo!=''){
		     populatenegotiation_error({tenderNo:tenderNo,id:id});
		}else{
			dom.get("negotiation_error").style.display = "none";
		}
}
<!-- end set status -->
function populateDetails()
{
	if(dom.get("negotiationDate").value=='') {
		<s:if test="%{model.negotiationDate==null}">
			document.tenderNegotiationForm.negotiationDate.value='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
		</s:if>
	}
	document.forms[0].hiddenNegotiationDate.value=document.tenderNegotiationForm.negotiationDate.value;
	<s:if test="%{createdBySelection.toLowerCase().equals('no')}">
		dom.get('executingDepartment').disabled=true;
		//dom.get('negotiationPreparedBy').disabled=true;
	</s:if>
	<s:else>
	    dom.get('executingDepartment').disabled=false;
		dom.get('negotiationPreparedBy').disabled=false;
	</s:else>
	<s:if test="%{editableDate.toLowerCase().equals('yes')}">
		dom.get('negotiationDate').disabled=false;
		dom.get("wpDateImg").style.display='';
	</s:if>
	<s:else>
		dom.get('negotiationDate').disabled=true;
		dom.get("wpDateImg").style.display='none';
	</s:else>
	inboxState();
} 

/* Modified for card #1110 . Providing a drop down to select the +/- sign for negotiated rate in the Negotiation Details */
function defaultQuotedRateToNegotiateRate(){
	if($("sorRateDiffChk").checked){
		dom.get('percSignNegoRate').value=dom.get('percSign').value;
		$("percNegotiatedRate").value=$F("percQuotedRateAmount"); 
		validateNegativeNumber(dom.get("percNegotiatedRate"));
		validate_Number(dom.get("percNegotiatedRate"));
		getNegotiatedAmount(dom.get("percNegotiatedRate"));
		getTenderPercAgainstMarketRate();
	}
}

/* Added for story card #1110. Providing a drop down to select the +/- sign for negotiated rate in the Negotiation Details */
function defaultTenderNegotionPercSign(){
	if(dom.get('sorRateDiffChk').checked==true){
		dom.get('percSignNegoRate').value=dom.get('percSign').value;
		defaultQuotedRateToNegotiateRate();
	}
}

/* Added for story card #1110. Providing a drop down to select the +/- sign for negotiated rate in the Negotiation Details */
function validate_Number(elem){
      dom.get('error'+elem.id).style.display='none';
      dom.get("negotiation_error").style.display = "none";
      if(isNaN(elem.value) || getNumber(elem.value)<0 || elem.value.indexOf("+")==0){
       	dom.get('error'+elem.id).style.display='';
       	dom.get("negotiation_error").style.display = "block";
       	if(elem.id=='percNegotiatedRate')
       		dom.get("negotiation_error").innerHTML='<s:text name="tenderNegotiation.nonNumeric.negotiatedAmount" />';
       	else if(elem.id=='percQuotedRateAmount')
       		dom.get("negotiation_error").innerHTML='<s:text name="tenderNegotiation.nonNumeric.quotedAmount" />'; 
       	window.scroll(0,0);	
      	return false;
      }
      return true; 
}

function checkContractorDetails() {
	var contractorsRecords= contractorsDataTable.getRecordSet();
	for(i=0;i<contractorsRecords.getLength();i++){ 
		 if(dom.get("code"+contractorsRecords.getRecord(i).getId()).value==''){
		 	dom.get("negotiation_error").innerHTML='<s:text name="tenderResponse.contractor.null" />'; 
        	dom.get("negotiation_error").style.display='';
        	window.scroll(0,0);
         	return false;
		} 
	}
    return true;
}

function checkTenderDate(){
	if(dom.get('tenderDate').value=='' || dom.get('tenderDate').value==null){
			dom.get("negotiation_error").innerHTML='<s:text name="tenderResponse.tenderDate.null" />'; 
        	dom.get("negotiation_error").style.display='';
        	window.scroll(0,0);
         	return false;
	}
	return true;
}

</script>
<div id="blanket" style="display:none;"></div>
<div id="popUpDiv" style="display:none;" ><s:text name="negotiation.dateChange.warning"/>(<a href="#" onclick="popup('popUpDiv');resetContractorDetails();">Yes</a>/<a href="#" onclick="popup('popUpDiv');resetNegotiationDate();">No</a>)?</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<s:hidden name="estimateId" value="%{abstractEstimate.id}" />
		<s:hidden name="worksPackageId" value="%{worksPackage.id}" />
		<td colspan="4" class="headingwk">
			<div class="arrowiconwk">
				<img src="/egworks/resources/erp2/images/arrow.gif" />
			</div>
			<div class="headplacer">
				<s:text name="page.header.negotiation" />
			</div>
		</td>
	</tr>
	<tr>
		<td class="greyboxwk">
			<s:text name='tenderNegotiation.executingDepartment' />
			:
		</td>
		<td class="greybox2wk">
			<s:select id="executingDepartment" name="deptId" headerKey="-1"
				cssClass="selectwk" list="%{dropdownData.departmentList}"
				listKey="id" listValue="deptName" headervalue="--select--"
				value="%{deptId}"
				onChange="setupPreparedByList(this);clearDesignation(this);" />
		</td>
		<div id="ajaxCall" style="display: none;">
			<egov:ajaxdropdown id="negotiationPreparedBy"
				fields="['Text','Value','Designation']"
				dropdownId='negotiationPreparedBy' optionAttributes='Designation'
				url='estimate/ajaxEstimate!usersInExecutingDepartment.action' />
		</div>
		<s:hidden name="mode"></s:hidden>
		<s:if test="%{tenderSource=='estimate'}">
			<td class="greyboxwk">
				<s:text name='tenderNegotiation.estimateNo' />
				:
			</td>
			<td class="greybox2wk">
				<input type="text" name="estimateNo" id="estimateNo"
					value='<s:property value="%{abstractEstimate.estimateNumber}" />'
					readonly="readonly" tabIndex="-1" class="selectboldwk" />
			</td>
		</s:if>
		<s:else>
		<s:hidden name="wpNumber" id="wpNumber" value="%{worksPackage.wpNumber}"/>
			<td class="greyboxwk">
				<s:text name='tenderNegotiation.WorksPackageNo' />
				:
			</td>
			<td class="greybox2wk">
				<input type="text" name="estimateNo" id="estimateNo"
					value='<s:property value="%{worksPackage.wpNumber}" />'
					readonly="readonly" tabIndex="-1" class="selectboldwk" />
			</td>
		</s:else>

	</tr>
	<s:hidden name="tenderSource"></s:hidden>
	<tr>
        <s:if test="%{tenderSource=='estimate'}">
		<td class="whiteboxwk"><s:text name='tenderNegotiation.nameOfWork'/>: </td>
        <td class="whitebox2wk"><input type="text" name="nameOfWork" id="nameOfWork" value='<s:property value="%{abstractEstimate.name}" />'  readonly="readonly" tabIndex="-1" class="selectboldwk" /></td>
        </s:if>
        <s:else>
        <td class="whiteboxwk"><s:text name='tenderNegotiation.nameOfWork'/>: </td>
        <td class="whitebox2wk"><input type="text" name="nameOfWork" id="nameOfWork" value='<s:property value="%{worksPackage.name}" />'  readonly="readonly" tabIndex="-1" class="selectboldwk" /></td>
        </s:else>
        <s:if test="%{tenderSource=='estimate'}">
        <td class="whiteboxwk"><s:text name='tenderNegotiation.projectCode'/>: </td>
        <td class="whitebox2wk"><input type="text" name="projectCode" id="projectCode" value='<s:property value="%{abstractEstimate.projectCode.code}" />' readonly="readonly" tabIndex="-1" class="selectboldwk"/></td>
        </s:if>
        <s:else><td class="whiteboxwk"><s:text name='tenderNegotiation.tenderFileNo'/>: </td>
        		<td class="whitebox2wk"><input type="text" name="tenderFileNo" id="tenderFileNo" value='<s:property value="%{worksPackage.tenderFileNumber}" />' readonly="readonly" tabIndex="-1" class="selectboldwk"/></td>
        </s:else>
	</tr>
	<tr>
		<td class="greyboxwk"><span class="mandatory">*</span><s:text name='tenderNegotiation.tenderNo'/>: </td>
        <td class="greybox2wk"><s:textfield name="tenderHeader.tenderNo" id="tenderNo" value="%{tenderHeader.tenderNo}" maxlength="50" cssClass="selectboldwk" onchange="uniqueCheckOntenderNumber(this);"/></td>
        <egov:uniquecheck id="negotiation_error" fields="['Value']" url='tender/ajaxTenderNegotiation!tenderNumberUniqueCheck.action'/>
        <td class="greyboxwk"><span class="mandatory">*</span><s:text name='tenderNegotiation.tenderDate'/>: </td>
             <td class="greybox2wk">
	         <s:date name="tenderHeader.tenderDate" var="tenderDateFormat" format="dd/MM/yyyy"/>
	         <s:if test="%{tenderSource!='estimate' && tenderHeader.tenderDate!=null}">
	         	 <input type="text" name="tenderHeader.tenderDate" id="tenderDate" value='<s:property value="%{tenderDateFormat}" />' readonly="readonly" tabIndex="-1" class="selectboldwk"/>
	         </s:if>  
	          <s:else>
	          	<s:textfield name="tenderHeader.tenderDate" value="%{tenderDateFormat}" id="tenderDate" cssClass="selectboldwk" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
	          	<a href="javascript:show_calendar('forms[0].tenderDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"><img src="/egworks/resources/erp2/images/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" /></a>
	          </s:else>	         
	        </td>    
	</tr>
	<tr>
		<td class="whiteboxwk">
			<s:if test="%{tenderSource=='estimate'}">
				<s:text name='tenderNegotiation.dept.estimateAmount' />
			</s:if>
			<s:else>
				<s:text name='tenderNegotiation.dept.worksPackagevalue' />
			</s:else>
			:
		</td>
		<td class="whitebox2wk">
			<input type="text" name="estimateAmount" id="estimateAmount"
				readonly="readonly" tabIndex="-1" class="selectamountwk" />
		</td>
		<s:if test="%{tenderSource=='estimate'}">
			<td class="whiteboxwk">
				<s:text name='tenderNegotiation.estimateYear' />
				:
			</td>
			<td class="whitebox2wk">
				<input type="text" name="estimateYear" id="estimateYear"
					value='<s:property value="%{abstractEstimate.leastFinancialYearForEstimate.finYearRange}" />'
					readonly="readonly" tabIndex="-1" class="selectboldwk" />
			</td>
		</s:if>
		<s:else>
			<td class="whiteboxwk">
				<s:text name='tenderNegotiation.workpackageYear' />
				:
			</td>
			<td class="whitebox2wk">
				<input type="text" name="estimateYear" id="estimateYear"
					value='<s:property value="%{wpYear}" />' readonly="readonly"
					tabIndex="-1" class="selectboldwk" />
			</td>
		</s:else>
	</tr>
	<tr>

		<td class="greyboxwk">
			<s:text name='tenderNegotiation.Amount_as_per_market' />
			:
		</td>
		<td class="greybox2wk">
			<input type="text" name="marketRateEstimateAmount"
				id="marketRateEstimateAmount"
				value='<s:property value="%{marketRateEstimateAmount}" />'
				readonly="readonly" tabIndex="-1" class="selectamountwk" />
		</td>
		<td class="greyboxwk">
			<span class="mandatory">*</span><s:text name='tenderNegotiation.negotiationDate' />
			:
		</td>
		<td class="greybox2wk">
			<s:date name="negotiationDate" var="negotiationDateFormat"
				format="dd/MM/yyyy" />
			<s:textfield name="negotiationDate" value="%{negotiationDateFormat}"
				id="negotiationDate" cssClass="selectboldwk"
				onfocus="javascript:vDateType='3';"
				onkeyup="DateFormat(this,this.value,event,false,'3');checkTenderDate();"
				onChange="checkTenderDate();clearContractor();defaultAsOnDate();"
				onBlur="checkTenderDate();negotaiationDateChangeFromCalender();" />
			<a
				href="javascript:show_calendar('forms[0].negotiationDate',null,null,'DD/MM/YYYY');"
				onmouseover="window.status='Date Picker';return true;"
				onmouseout="window.status='';return true;"> <img
					src="/egworks/resources/erp2/images/calendar.png"
					id="wpDateImg" alt="Calendar" width="16" height="16" border="0"
					align="absmiddle" /> </a>
			<input type='hidden' id='hiddenNegotiationDate' name='hiddenNegotiationDate' />
			<input type='hidden' id='priorHiddenNegotiationDate' name='priorHiddenNegotiationDate' />
			<span id='errornegotiationDate'
				style="display: none; color: red; font-weight: bold">&nbsp;x</span>
			<input type='button' class="buttonadd" value='Re-Calculate'
				onClick='getMarketValueEstimateAmount();' />
		</td>
	</tr>	
	<!-- <tr>
		<td class="whiteboxwk"><span class="mandatory">*</span><s:text name='tenderNegotiation.nameOfContractor'/>: </td>
		<td class="whitebox2wk" colspan="4">
			<table>
			<tr>
			<td><input type="text" name="contractorName" id="contractorName" value='<s:property value="%{contractor.name}" />'  class="selectboldwk" /></td>
			<td><input type="text" name="contractorCode" id="contractorCode"  disabled="true" value='<s:property value="%{contractor.code}" />' class="selectboldwk" />
			<s:hidden id="contractor" name="contractor" value="%{contractor.id}"/></td>
			<td><a onclick="searchContractor(this)" href="#"><IMG id="img" height=16 src="/egworks/resources/erp2/images/magnifier.png" width=16 alt="Search" border="0" align="absmiddle"></a></td>
			</tr>
			</table>
       </td>
      
	</tr>  -->

	<tr>
		<td class="whiteboxwk">
			<span class="mandatory">*</span><s:text name='tenderNegotiation.tenderType' />
			:
		</td>
		<td class="whitebox2wk">
			<s:select name="tenderType" id="tenderType" headerKey="-1"
				headerValue="%{getText('estimate.default.select')}"
				list="%{dropdownData.tenderTypeList}" value="%{tenderType}"
				onChange="toggleDetails(this);" />
		</td>
		<td class="whiteboxwk">
			<s:text name='tenderNegotiation.narration' />
			:
		</td>
		<td class="whitebox2wk">
			<s:textarea name="narration" cols="35" cssClass="selectwk"
				id="narration" value="%{narration}" />
		</td>
	</tr>
		
	<tr id="percQuotedRow1" style="display: none">
		<td class="greyboxwk">
		<input type="checkbox" name="sorRateDiffChk" id="sorRateDiffChk" onclick="defaultTenderNegotionPercSign();" />
		</td>
		<td class="greybox2wk"><s:text name='tenderNegotiation.defaultsortonegotiatedrate' /></td>
		<td class="greyboxwk">&nbsp;</td>
		<td class="greybox2wk">&nbsp;</td>
	</tr>

	<tr id="percQuotedRow" style="display: none">
		<td class="whiteboxwk">
		<span class="mandatory">*</span>
			<s:text name='tenderNegotiation.percQuotedRate' />
			:
		</td>
		<td class="whitebox2wk">
			<s:select name="percSign" id="percSign" list="#{'0':'+','1':'-'}"
				value="%{percSign}" onchange="getQutAmt();defaultQuotedRateToNegotiateRate();"/>
			<s:textfield name="percQuotedRateAmount" 
				id="percQuotedRateAmount" cssClass="selectamountwk"
				onblur="validate_Number(this);getQuotedAmount(this);getTenderPercAgainstMarketRate();defaultQuotedRateToNegotiateRate()"
				maxlength="10" />
				<s:hidden id="percQuotedRate" name="percQuotedRate" value="%{formattedPercQuotedRate}"/>
			<span id='errorpercQuotedRateAmount'
				style="display: none; color: red; font-weight: bold">&nbsp;x</span>
		</td>
		<td class="whiteboxwk">
			<s:text name='tenderNegotiation.percQuotedAmount' />
			:
		</td>
		<td class="whitebox2wk">
			<input type="text" name="percQuotedAmount" id="percQuotedAmount"
				value='<s:property value="%{percQuotedAmount}" />'
				readonly="readonly" tabIndex="-1" class="selectamountwk" />
		</td>
	</tr>
	<!--  -----------------------spacer row----------      
	<tr id="spacerRow">
		<td class="whiteboxwk">
			&nbsp;&nbsp;
		</td>
		<td class="whitebox2wk">
			&nbsp;&nbsp;
		</td>
		<td class="whiteboxwk">
			&nbsp;&nbsp;
		</td>
		<td class="whitebox2wk">
			&nbsp;&nbsp;
		</td>
	</tr>   -->
	<tr>
		<td colspan="4">
			<div>
			<%@ include file="negotiation-contractors.jsp"%>
			</div>
		</td>
	</tr>
	
	<%--  ---------------------------   end ---------------------------------    --%>


	<tr>
		<td colspan="4" class="shadowwk"> </td>               
	</tr>
</table>
