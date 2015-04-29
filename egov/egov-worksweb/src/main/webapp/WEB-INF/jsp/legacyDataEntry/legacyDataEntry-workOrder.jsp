<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<style>
#warning {
	display: none;
	color: blue;
}
</style>
<script>
function validateWOBeforeSubmit(){
	var workOrderNumber = document.legacyDataEntryForm.workOrderNumber.value;
	var workOrderDate = document.legacyDataEntryForm.workOrderDate.value;
	if(workOrderNumber=='') {
		dom.get("workOrder_error").style.display='';     
		document.getElementById("workOrder_error").innerHTML='<s:text name="lde.tn.workOrderNo.null" />';
		window.scroll(0,0);
		return false
	}
	if(workOrderDate=='') {
		dom.get("workOrder_error").style.display='';     
		document.getElementById("workOrder_error").innerHTML='<s:text name="workOrder.workOrderDate.null" />';
		window.scroll(0,0);
		return false
	}

    if(!checkDateFormat(dom.get("workOrderDate"))){
 		dom.get("workOrder_error").innerHTML='<s:text name="invalid.fieldvalue.workOrderDate"/>'; 
        dom.get("workOrder_error").style.display='';
        window.scroll(0,0);
	 	return false;
 	}
	if(compareDate(formatDate6(workOrderDate),formatDate6(currentDate)) == -1){
        dom.get("workOrder_error").style.display='';     
		document.getElementById("workOrder_error").innerHTML='<s:text name="lde.wo.woDate.greaterthanCurrentDate" />';
		window.scroll(0,0);
		return false  	
	}
	if(compareDate(formatDate6(workOrderDate),formatDate6(tnLastOfflineLastStatusDate)) == 1){
        dom.get("workOrder_error").style.display='';     
		document.getElementById("workOrder_error").innerHTML='<s:text name="lde.wo.woDate.lessthan.tnLastOfflineStatusDate" />'+tnLastOfflineLastStatus+' <s:text name="common.label.date" />';
		window.scroll(0,0);
		return false  	 
	}
  
    if(dom.get("engineerIncharge").value=='-1'){
    	dom.get("workOrder_error").style.display='';     
		dom.get("workOrder_error").innerHTML='<s:text name="lde.engineerIncharge.notselected"/>';
		window.scroll(0,0);
		return false
    }
   
    if(document.legacyDataEntryForm.emdAmountDeposited.value=='') {
		dom.get("workOrder_error").style.display='';     
		document.getElementById("workOrder_error").innerHTML='<s:text name="lde.tn.negotiationDate.null" />';
		window.scroll(0,0);
		return false
	}
 	 
    dom.get("workOrder_error").style.display='none';     
	dom.get("workOrder_error").innerHTML='';
	return true;
}

function isvalidWODateFormat(obj)
 {
 	if(!checkDateFormat(obj)){
 		dom.get("workOrder_error").innerHTML='<s:text name="invalid.fieldvalue.workOrderDate"/>'; 
        dom.get("workOrder_error").style.display='';
        window.scroll(0,0);
	 	return false;
 	}
 	dom.get("workOrder_error").style.display='none';
	dom.get("workOrder_error").innerHTML='';
	return true;
 }

function isNumberKey(evt) {
   var charCode = (evt.which) ? evt.which : event.keyCode
   if (charCode > 31 && (charCode < 48 || charCode > 57))
      return false;

   return true;
}

function roundOffEmdAmountDeposited() {
	document.legacyDataEntryForm.workOrderAmount.value=roundTo(document.legacyDataEntryForm.workOrderAmount.value);
	document.legacyDataEntryForm.emdAmountDeposited.value=roundTo(document.legacyDataEntryForm.emdAmountDeposited.value);
	document.legacyDataEntryForm.securityDeposit.value=roundTo(document.legacyDataEntryForm.securityDeposit.value);
	document.legacyDataEntryForm.labourWelfareFund.value=roundTo(document.legacyDataEntryForm.labourWelfareFund.value);
}

</script>

<div class="errorstyle" id="workOrder_error" style="display: none;"></div> 

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
		<td class="whiteboxwk">
			<span class="mandatory">*</span><s:text name='workOrder.workorderNo' />	:
		</td>
		<td class="whitebox2wk">
			<input type="text" name="workOrder.workOrderNumber" id="workOrderNumber"
				value='<s:property value="%{workOrder.workOrderNumber}" />' class="selectboldwk" />
		</td>
		<td width="11%" class="whiteboxwk">
   			<span class="mandatory">*</span><s:text name="wo.date"/> :
   		</td>
       	<td width="21%" class="whitebox2wk">
	   		<s:date name="workOrderDate" var="workOrderDateFormat" format="dd/MM/yyyy"/>
		    <s:textfield name="workOrder.workOrderDate" value="%{workOrderDateFormat}" id="workOrderDate" cssClass="selectboldwk" 
        		 onfocus="javascript:vDateType='3';" 
        		 onkeyup="DateFormat(this,this.value,event,false,'3')"  onblur="isvalidWODateFormat(this)"/>
        		 <a href="javascript:show_calendar('forms[0].workOrderDate',null,null,'DD/MM/YYYY');" id="dateHref2"
        		 onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;">
        		 <img src="${pageContext.request.contextPath}/image/calendar.png" id="wpDateImg" alt="Calendar" width="16" height="16" 
       			  border="0" align="absmiddle" /></a> 
	    </td>    
	</tr>
	 <tr id="wpAmountRow" style="display:none">
   		<td class="greyboxwk">
			<s:text name="estimate.amount"/> :
		</td>
		<td class="greybox2wk"> 
			<input type="text" id="estimateAmount" readonly="readonly" tabIndex="-1" class="selectamountwk" />
		</td>
   		<td class="greyboxwk">
			<s:text name="workorder.percentage.negotiated"/> :
		</td>
		<td class="greybox2wk" >
			<input type="text" id="tenderpercentage"
				readonly="readonly" tabIndex="-1" class="selectamountwk" />
		</td>	
   </tr>
	 <tr>
   		<td class="greyboxwk">
			<s:text name="workorder.amount"/> :
		</td>
		<td class="greybox2wk" colspan="3">
			<input type="text" name="workOrderAmount" id="workOrderAmount"
				readonly="readonly" tabIndex="-1" class="selectamountwk" value='<s:property value="%{workOrder.workOrderAmount}" />' />
		</td>	
        
   </tr>
	 <tr>
   		<td class="whiteboxwk">
   			 <s:text name="sec.deposit"/> :
		</td>
		<td class="whitebox2wk">
			<s:textfield name="workOrder.securityDeposit" value="%{workOrder.securityDeposit}" id="securityDeposit" cssClass="selectamountwk" 
			onblur="roundOffEmdAmountDeposited()"/>
		</td>	
        <td class="whiteboxwk">
        	<s:text name="labour.welfund"/> :
        </td>
        <td class="whitebox2wk">
        	<s:textfield name="workOrder.labourWelfareFund" value="%{workOrder.labourWelfareFund}" id="labourWelfareFund" cssClass="selectamountwk" 
        	onblur="roundOffEmdAmountDeposited()"/>
        </td>
   </tr>
   <tr>
   		<td class="greyboxwk">
   			<span class="mandatory">*</span><s:text name="emd.amt"/> :
		</td>
		<td class="greybox2wk">
			<s:textfield name="workOrder.emdAmountDeposited" cssClass="selectamountwk" id="emdAmountDeposited" cssClass="selectamountwk" 
			onblur="roundOffEmdAmountDeposited()"/>
		</td>	
       <td class="greyboxwk"><span class="mandatory">*</span>
   			<s:text name="con.period"/> : 
		</td>
		<td class="greybox2wk">
        	<s:textfield name="workOrder.contractPeriod" value="%{workOrder.contractPeriod}" id="contractPeriod" cssClass="selectwk" onkeypress="return isNumberKey(event)" />
        </td>
       
   </tr>
    <tr>
   		
        <td class="whiteboxwk">
        	<span class="mandatory">*</span><s:text name="lde.wo.allocatedTo"/> :
        </td>
        <td class="whitebox2wk">
        	<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="workOrder.engineerIncharge" 
	         id="engineerIncharge"  cssClass="selectwk" 
	         list="dropdownData.assignedUserList" listKey="id" listValue="employeeName" />
        </td>
        <td class="whiteboxwk"><s:text name='tenderNegotiation.nameOfContractor'/> : </td>
		<td class="whitebox2wk"> <input type="text" name="contractorName" id="contractorName" value='<s:property value="%{contractor.name}" />' 
			 		class="selectboldwk" readonly="readonly"/>
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
            <div id="wo_offlineStatus_details">
           		
            </div>        
            </td> 
          </tr> 
</table>
