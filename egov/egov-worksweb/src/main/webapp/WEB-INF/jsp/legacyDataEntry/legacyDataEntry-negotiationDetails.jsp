<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<style>
#warning {
	display: none;
	color: blue;
}
</style>
<script>

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
		document.legacyDataEntryForm.wpAmount.value=roundTo(totalAmount);
}

function validateTNBeforeSubmit(legacyDataEntryForm) {

	var tenderNo = legacyDataEntryForm.tenderNo.value;
	var negotiationNo = legacyDataEntryForm.negotiationNumber.value;
	if(tenderNo=='') {
		dom.get("negotiation_error").style.display='';     
		document.getElementById("negotiation_error").innerHTML='<s:text name="tenderHeader.tenderNo.null" />';
		return false
	}
	
	var tenderDate = legacyDataEntryForm.tenderDate.value;
	var tenderNegotiationDate=legacyDataEntryForm.negotiationDate.value;
			
	if(tenderDate=='') {
		dom.get("negotiation_error").style.display='';     
		document.getElementById("negotiation_error").innerHTML='<s:text name="tenderHeader.tenderDate.null" />';
		window.scroll(0,0);
		return false
	}

	if(negotiationNo=='') {
		dom.get("negotiation_error").style.display='';     
		document.getElementById("negotiation_error").innerHTML='<s:text name="lde.tn.negotiationNo.null" />';
		window.scroll(0,0);
		return false
	}
	if(tenderNegotiationDate=='') {
		dom.get("negotiation_error").style.display='';     
		document.getElementById("negotiation_error").innerHTML='<s:text name="lde.tn.negotiationDate.null" />';
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

	if(compareDate(formatDate6(tenderNegotiationDate),formatDate6(wpLastOfflineLastStatusDate)) == 1){
        dom.get("negotiation_error").style.display='';     
		document.getElementById("negotiation_error").innerHTML='<s:text name="tenderHeader.negotiationDate.lessthan.wpLastOfflineStatusDate" />'+wpLastOfflineLastStatus+' <s:text name="common.label.date" />';
		window.scroll(0,0);
		return false  	 
	}
	
	if(compareDate(formatDate6(tenderNegotiationDate),formatDate6(tenderDate)) == 1){
        dom.get("negotiation_error").style.display='';     
		document.getElementById("negotiation_error").innerHTML='<s:text name="tenderHeader.negotiationDate.lessthanTenderDate" />';
		window.scroll(0,0);
		return false  	 
	}
	
    var tenderTypeIndex = document.legacyDataEntryForm.tenderType.selectedIndex;
	var tenderTypeValue=document.legacyDataEntryForm.tenderType.options[tenderTypeIndex].value;
	if (tenderTypeIndex == "0" || tenderTypeValue == "-1") {       
        dom.get("negotiation_error").style.display='';     
        document.getElementById("negotiation_error").innerHTML='<s:text name="tenderEstimate.tenderType.null" />';
        return false;
    }  

	var contractor = document.legacyDataEntryForm.contractorSearch.value;
	if (contractor == "") {       
        dom.get("negotiation_error").style.display='';     
        document.getElementById("negotiation_error").innerHTML='<s:text name="tenderResponseContractors.contractor.not.null" />';
        return false;
    }  
    
     if(document.legacyDataEntryForm.tenderType.value==dom.get("tenderType")[1].value){
	 	if(legacyDataEntryForm.percQuotedRateAmount.value==""){
	 		dom.get("negotiation_error").style.display='';
	 		document.getElementById("negotiation_error").innerHTML='<s:text name="tenderNegotiation.nonNumeric.quotedAmount" />';
	 		window.scroll(0,0);
	 		return false;
	 	}
	 	if(legacyDataEntryForm.percQuotedRateAmount.value!=""){
	 		if(!validate_Number(legacyDataEntryForm.percQuotedRateAmount)){
	 			window.scroll(0,0);
	 			return false;
	 		}
	 	}
	 	
 	 	if(legacyDataEntryForm.percNegotiatedRate.value==""){
 	 		dom.get("negotiation_error").style.display='';
 	 		document.getElementById("negotiation_error").innerHTML='<s:text name="tenderNegotiation.nonNumeric.negotiatedAmount" />';
 	 		return false;
 	 	}
 	 	if(legacyDataEntryForm.percNegotiatedRate.value!=""){
 	 		if(!validate_Number(legacyDataEntryForm.percNegotiatedRate))
 	 			return false;
 	 	} 	
 	}

    if(legacyDataEntryForm.percQuotedAmount.value=="") {
		dom.get("negotiation_error").style.display='';
		document.getElementById("negotiation_error").innerHTML='<s:text name="tenderNegotiation.nonNumeric.percQuotedAmount" />';
		window.scroll(0,0);
		return false;
	}
	
	if(legacyDataEntryForm.percQuotedAmount.value!="") {
		if(!validate_Number(legacyDataEntryForm.percQuotedAmount)) {
			window.scroll(0,0);
			return false;
		}
	}
	
	if(legacyDataEntryForm.percNegotiatedAmount.value=="") {
		dom.get("negotiation_error").style.display='';
		document.getElementById("negotiation_error").innerHTML='<s:text name="tenderNegotiation.nonNumeric.percNegotiatedAmount" />';
		window.scroll(0,0);
		return false;
	}
	
	if(legacyDataEntryForm.percNegotiatedAmount.value!="") {
		if(!validate_Number(legacyDataEntryForm.percNegotiatedAmount)) {
			window.scroll(0,0);
			return false;
		}
	}
	if(!validateTNOfflineStatus())
		return false;
    return true;
}

function checkForNumber(sText) {   
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
		document.legacyDataEntryForm.negotiationDate.focus();
    	return false;
	}
	else {
		dom.get("negotiation_error").style.display='none';
		return true;
	}
}

function clearTNDetails(){
	var negotiationDate=document.legacyDataEntryForm.negotiationDate.value;
	var id=document.legacyDataEntryForm.id.value;
	var hiddenNegotiationDate=document.forms[0].hiddenNegotiationDate.value;
	if(negotiationDate!='' && hiddenNegotiationDate!= '' && hiddenNegotiationDate!=negotiationDate){
		popup('popUpDiv');		
	}
	else if(id!='' && id!=undefined && negotiationDate!=''){
		popup('popUpDiv');
	}
	document.legacyDataEntryForm.priorHiddenNegotiationDate.value=document.legacyDataEntryForm.hiddenNegotiationDate.value;
	document.legacyDataEntryForm.hiddenNegotiationDate.value=negotiationDate;
}

function resetNegotiationDate(){

	document.legacyDataEntryForm.negotiationDate.value=document.legacyDataEntryForm.priorHiddenNegotiationDate.value;
	document.legacyDataEntryForm.hiddenNegotiationDate.value=document.legacyDataEntryForm.priorHiddenNegotiationDate.value;
}

function toggleDetails(obj) {	 
	if(obj.value!='-1') {
		if(obj.value==dom.get("tenderType")[1].value) {
			//document.getElementById("percQuotedRow").style.display='';
			//$("percQuotedRow1").show();			
		}
		else if(obj.value==dom.get("tenderType")[2].value) {
			//document.getElementById("percQuotedRow").style.display='none';	
			//$("percQuotedRow1").hide();			
		}
	}
	else {
		//document.getElementById("percQuotedRow").style.display='none';
		//$("percQuotedRow1").hide();
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
     document.legacyDataEntryForm.percQuotedAmount.value=eval(document.legacyDataEntryForm.wpAmount.value)+eval(document.legacyDataEntryForm.wpAmount.value*objVal)/100;
	 document.legacyDataEntryForm.percQuotedAmount.value=roundTo(document.legacyDataEntryForm.percQuotedAmount.value);
	 }
	 else {
		document.legacyDataEntryForm.percQuotedAmount.value='';
		dom.get('percQuotedRate').value='';
	}
}

function negotaiationDateChangeFromCalender() {
	var negotiationDate=document.forms[0].negotiationDate.value;
	var hiddenNegotiationDate=document.forms[0].hiddenNegotiationDate.value;
	if(hiddenNegotiationDate!='' && negotiationDate!='' && hiddenNegotiationDate!=negotiationDate){
		clearTNDetails();		
	}	
	document.forms[0].hiddenNegotiationDate.value=negotiationDate;
}

function uniqueCheckOntenderNumber(obj) {
	var tenderNo = dom.get('tenderNo').value;
	var id = dom.get('tenderResponseId').value;
		if(tenderNo!=''){
		     populatetenderNo_error({tenderNo:tenderNo,id:id});
		}else{
			dom.get("tenderNo_error").style.display = "none";
		}
}


function defaultQuotedRateToNegotiateRate(){
	if($("sorRateDiffChk").checked){
		dom.get('percSignNegoRate').value=dom.get('percSign').value;
		$("percNegotiatedRate").value=$F("percQuotedRateAmount"); 
		validateNegativeNumber(dom.get("percNegotiatedRate"));
		validate_Number(dom.get("percNegotiatedRate"));
		getNegotiatedAmount(dom.get("percNegotiatedRate"));
	}
}

function defaultTenderNegotionPercSign(){
	if(dom.get('sorRateDiffChk').checked==true){
		dom.get('percSignNegoRate').value=dom.get('percSign').value;
		defaultQuotedRateToNegotiateRate();
	}
}

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
       	else if(elem.id=='percQuotedAmount')
        	dom.get("negotiation_error").innerHTML='<s:text name="tenderNegotiation.nonNumeric.percQuotedAmount" />'; 
        else if(elem.id=='percNegotiatedAmount')
        	dom.get("negotiation_error").innerHTML='<s:text name="tenderNegotiation.nonNumeric.percNegotiatedAmount" />'; 
       		
       	window.scroll(0,0);	
      	return false;
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

function clearHiddenContractorId(obj) {
	if(obj.value=="") {
		document.getElementById("contractorId").value="";
	}	
}

var contractorSearchSelectionHandler = function(sType, arguments) {  
    var oData = arguments[2];
    dom.get("contractorSearch").value=oData[0];
    dom.get("contractorId").value = oData[1];
};

var contractorSelectionEnforceHandler = function(sType, arguments) {
	alert('<s:text name="lde.tn.search.contractor.loading.failure"/>');
};

function contractorSearchParameters(){
	dom.get("negotiation_error").style.display='none';    
	var negotiationDate = dom.get('negotiationDate').value
	if(negotiationDate == '') {
		dom.get("negotiation_error").style.display='';     
		document.getElementById("negotiation_error").innerHTML='<s:text name="lde.tn.negotiationDate.null" />';
		window.scroll(0,0);
		return false
	}
	else {
		return "searchDate="+negotiationDate;
    }
}


function getNegotiatedAmount(obj) {
	if(obj.value!=''){
		var objVal = obj.value;
	    objVal=assignSignPercNegoAmount(obj);
	    dom.get('percNegotiatedAmountRate').value=objVal;
		document.legacyDataEntryForm.percNegotiatedAmount.value=eval(document.legacyDataEntryForm.wpAmount.value)+eval(document.legacyDataEntryForm.wpAmount.value*objVal)/100;
		document.legacyDataEntryForm.percNegotiatedAmount.value=roundTo(document.legacyDataEntryForm.percNegotiatedAmount.value);
	}
	else {
		document.legacyDataEntryForm.percNegotiatedAmount.value='';
		$("percNegotiatedAmount").value="";
		dom.get('percNegotiatedAmountRate').value="";
	}
}

function assignSignPercNegoAmount(obj){
var persign = dom.get('percSignNegoRate').value;
if(persign=='1'){
      objVal = -+obj.value;
      return objVal;
  }else{
     return obj.value;;
  }
}

function checkPercNegotiatedRate(elem){
	if(!validate_Number(elem))
	 return false; 
	if(!validateNegativeNumber(elem))
	 return false;
	getNegotiatedAmount(elem); 
}


</script>

<div class="errorstyle" id="negotiation_error" style="display: none;"></div> 

<div class="errorstyle" id="tenderNo_error" style="display: none;"> 
	<s:text name="tenderHeader.tenderNo.isunique"/>
</div>

<!-- <div id="blanket" style="display:none;"></div>
<div id="popUpDiv" style="display:none;" ><s:text name="negotiation.dateChange.warning"/>(<a href="#" onclick="popup('popUpDiv');resetContractorDetails();">Yes</a>/<a href="#" onclick="popup('popUpDiv');resetNegotiationDate();">No</a>)?</div>
 -->
 <s:hidden name="tenderResponse.id" id="tenderResponseId" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="4" class="headingwk">
			<div class="arrowiconwk">
				<img src="${pageContext.request.contextPath}/image/arrow.gif" />
			</div>
			<div class="headplacer">
				<s:text name="page.header.negotiation" />
			</div>
		</td>
	</tr>
	<tr>
		<td class="greyboxwk"><span class="mandatory">*</span><s:text name='tenderNegotiation.tenderNo'/>: </td>
        <td class="greybox2wk"><s:textfield name="tenderHeader.tenderNo" id="tenderNo" value="%{tenderHeader.tenderNo}" maxlength="50" cssClass="selectboldwk" onkeyup="uniqueCheckOntenderNumber(this);"/></td>
        <egov:uniquecheck id="tenderNo_error" fields="['Value']" url='tender/ajaxTenderNegotiation!tenderNumberUniqueCheck.action'/>
        <td class="greyboxwk"><s:text name='tenderNegotiation.tenderDate'/>: </td>
             <td class="greybox2wk">
	         <s:date name="tenderHeader.tenderDate" var="tenderDateFormat" format="dd/MM/yyyy"/>
	         	 <input type="text" name="tenderHeader.tenderDate" id="tenderDate" value='<s:property value="%{tenderDateFormat}" />' readonly="readonly" tabIndex="-1" class="selectboldwk"/>
	             
	        </td>    
	</tr>
	<tr>
		<td class="whiteboxwk">
				<s:text name='tenderNegotiation.dept.worksPackagevalue' />
			:
		</td>
		<td class="whitebox2wk">
			<input type="text" name="wpAmount" id="wpAmount"
				readonly="readonly" tabIndex="-1" class="selectamountwk" />
		</td>
		
	</tr>
	<tr>		
			<td class="greyboxwk">
				<span class="mandatory">*</span><s:text name='tenderNegotiation.negotiationNo' />
				:
			</td>
			<td class="greybox2wk">
				<input type="text" name="tenderResponse.negotiationNumber" id="negotiationNumber"
					value='<s:property value="%{tenderResponse.negotiationNumber}" />' class="selectboldwk" />
			</td>
				<td class="greyboxwk">
			<span class="mandatory">*</span><s:text name='tenderNegotiation.negotiationDate' />
			:
		</td>
		<td class="greybox2wk">
			<s:date name="negotiationDate" var="negotiationDateFormat"
				format="dd/MM/yyyy" />
			<s:textfield name="tenderResponse.negotiationDate" value="%{negotiationDateFormat}"
				id="negotiationDate" cssClass="selectboldwk"
				onfocus="javascript:vDateType='3';"
				onkeyup="DateFormat(this,this.value,event,false,'3');"
				onChange="clearTNDetails();"
				onBlur="negotaiationDateChangeFromCalender();" />
			<a
				href="javascript:show_calendar('forms[0].negotiationDate',null,null,'DD/MM/YYYY');"
				onmouseover="window.status='Date Picker';return true;"
				onmouseout="window.status='';return true;"> <img
					src="${pageContext.request.contextPath}/image/calendar.png"
					id="wpDateImg" alt="Calendar" width="16" height="16" border="0"
					align="absmiddle" /> </a>
			<input type='hidden' id='hiddenNegotiationDate' name='hiddenNegotiationDate' />
			<input type='hidden' id='priorHiddenNegotiationDate' name='priorHiddenNegotiationDate' />
			<span id='errornegotiationDate'
				style="display: none; color: red; font-weight: bold">&nbsp;x</span>
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
			<td><a onclick="searchContractor(this)" href="#"><IMG id="img" height=16 src="${pageContext.request.contextPath}/image/magnifier.png" width=16 alt="Search" border="0" align="absmiddle"></a></td>
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
			<s:select name="tenderResponse.tenderEstimate.tenderType" id="tenderType" headerKey="-1"
				headerValue="%{getText('estimate.default.select')}"
				list="%{dropdownData.tenderTypeList}" value="%{tenderType}"
				onChange="toggleDetails(this);" />
		</td>
		<td class="whiteboxwk"><span class="mandatory">*</span><s:text name='tenderNegotiation.nameOfContractor'/>: </td>
		<td class="whitebox2wk">
			<div class="yui-skin-sam">
               <div id="contractorSearch_autocomplete">
               <div><s:textfield id="contractorSearch" type="text" name="contractorName" value="%{contractor.name}" onBlur="clearHiddenContractorId(this)" class="selectwk"/>
               <s:hidden id="contractorId" name="contractorId" value="%{contractorId}"/></div>
               <span id="contractorSearchResults"></span>
               </div>
               </div>
               <egov:autocomplete name="contractorSearch" width="20" field="contractorSearch" url="ajaxLegacyDataEntry!searchContractorAjax.action?"  queryQuestionMark="false" results="contractorSearchResults" handler="contractorSearchSelectionHandler" forceSelectionHandler="contractorSelectionEnforceHandler" paramsFunction="contractorSearchParameters" queryLength="3" />
		</td>
		
	</tr>
		
	<tr id="percQuotedRow1" > 
		<td class="greyboxwk">
		<input type="checkbox" name="sorRateDiffChk" id="sorRateDiffChk" onclick="defaultTenderNegotionPercSign();" />
		</td>
		<td class="greybox2wk"><s:text name='tenderNegotiation.defaultsortonegotiatedrate' /></td>
		<td class="greyboxwk">&nbsp;</td>
		<td class="greybox2wk">&nbsp;</td>
	</tr>

	<tr id="percQuotedRow" >
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
				onblur="validate_Number(this);getQuotedAmount(this);defaultQuotedRateToNegotiateRate();"
				maxlength="10" size="10" />
				<s:hidden id="percQuotedRate" name="tenderResponse.percQuotedRate" value="%{formattedPercQuotedRate}"/>
			<span id='errorpercQuotedRateAmount'
				style="display: none; color: red; font-weight: bold">&nbsp;x</span>
		</td>
		<td class="whiteboxwk"><span class="mandatory">*</span>
			<s:text name='tenderNegotiation.percQuotedAmount' />
			:
		</td>
		<td class="whitebox2wk">
			<s:textfield name="percQuotedAmount" id="percQuotedAmount" value="%{percQuotedAmount}" onblur="validate_Number(this);" cssClass="selectamountwk" />
			 <span id='errorpercQuotedAmount' style="display:none;color:red;font-weight:bold">&nbsp;x</span></td>
		</td>
	</tr>
	<tr>
		<td class="greyboxwk"><span class="mandatory">*</span><s:text name='tenderNegotiation.tenderPercAftrNegotiation'/>: </td>
        <td class="greybox2wk">
        <s:select name="percSignNegoRate" id="percSignNegoRate" list="#{'0':'+','1':'-'}"
				value="%{percSignNegoRate}" onchange="getNegotiatedAmount(dom.get('percNegotiatedRate'));"/>
        <s:textfield name="percNegotiatedRate" value="%{percNegotiatedRate}" id="percNegotiatedRate" cssClass="selectamountwk" onblur="checkPercNegotiatedRate(this);"  maxlength="10" size="10"/>
        <span id='errorpercNegotiatedRate' style="display:none;color:red;font-weight:bold">&nbsp;x</span></td>
         <s:hidden id="percNegotiatedAmountRate" name="tenderResponse.percNegotiatedAmountRate" value="%{formattedPercNegotiatedAmountRate}"/>
        <td class="greyboxwk"><span class="mandatory">*</span><s:text name='tenderNegotiation.tenderValueAftrNegotiation'/>: </td>
        <td class="greybox2wk">
        <s:textfield name="percNegotiatedAmount" id="percNegotiatedAmount" value="%{percNegotiatedAmount}" onblur="validate_Number(this);" cssClass="selectamountwk" />
        <span id='errorpercNegotiatedAmount' style="display:none;color:red;font-weight:bold">&nbsp;x</span></td>
        </td>                         
	</tr>
	
	<tr>
		<td colspan="4" class="shadowwk"> </td>               
	</tr>
	
           <tr>
                <td  colspan="4" >&nbsp; </td>                 
          </tr>  
          <tr>
            <td colspan="4">
            <div id="tn_offlineStatus_details">
           		<%@ include file="lde-offlineStatus-tenderNegotiation.jsp"%>  
            </div>        
            </td> 
          </tr> 
</table>
