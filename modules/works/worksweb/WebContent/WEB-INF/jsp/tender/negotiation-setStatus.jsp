
<s:if test="%{model.currentState!=null && model.currentState.value!=null && model.currentState.previous!=null && model.currentState.previous.value=='APPROVED' && sourcepage!='inbox'}">
	  <tr>
		<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
		<div class="headplacer">Negotiation Statement Status</div></td>
	</tr>

	<tr>
		<td class="greyboxwk">Current Status : </td>
        	<td class="greybox2wk"> <s:textfield name="currentStatus" type="text" readonly="true" disabled="disabled" cssClass="selectboldwk" id="currentStatus" size="28" tabIndex="-1" value="%{currentStatus}"/>	</td>

        <td class="greyboxwk">Current Approver Name:</td>
        <td class="greybox2wk"><s:textfield name="currentApproverName" type="text" readonly="true" disabled="disabled" cssClass="selectboldwk" id="currentApproverName" size="25" tabIndex="-1" value="%{currentApproverName}"/></td>                         
	</tr>
<tr>
		<td class="whiteboxwk">Current Approved Date: </td>
        	<td class="whitebox2wk"> <s:textfield name="currentApprovedDate" type="text" readonly="true" disabled="disabled" cssClass="selectboldwk" id="currentApprovedDate" size="25" tabIndex="-1" value="%{currentApprovedDateStr}"/></td>

        <td class="whiteboxwk">&nbsp;</td>
        <td class="whitebox2wk">&nbsp;</td>                         
	</tr>

	  <tr id="newStatusRow1">
		<td class="greyboxwk"><span class="mandatory">*</span><s:text name='tenderNegotiation.setstatus'/>: </td>
        <td class="greybox2wk">
	     <s:if test="%{statusIdValue!=null}">	
     		<s:select id="status" name="negotiationStatusId" cssClass="selectwk" list="%{dropdownData.statusList}" 
     		listKey="id" listValue="description" onchange="return toggleApprovedBy(this);" headerKey="-1" headerValue="--- Select ---" value="%{statusIdValue}"/>
	     </s:if>
	     <s:else>
     		<s:select id="status" name="negotiationStatusId" cssClass="selectwk" list="%{dropdownData.statusList}" 
     		listKey="id" listValue="description" onchange="return toggleApprovedBy(this);" headerKey="-1" headerValue="--- Select ---"/>
	    </s:else>
        </td>
        <td class="greyboxwk"><span class="mandatory">*</span><s:text name='tenderNegotiation.statusdate'/>: </td>
        <td class="greybox2wk">
         <s:date name="negotiationStatus.approvedDate" var="approvedDateDateFormat" format="dd/MM/yyyy"/>
         <s:textfield name="negotiationStatus.approvedDate" value="%{approvedDateDateFormat}" id="approvedDate" cssClass="selectboldwk" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="checkApprovedDate(this);"/>
         <a name="aprdDatelnk" id="aprdDatelnk" href="javascript:show_calendar('forms[0].approvedDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"><img name="aprdDateimg" id="aprdDateimg" src="${pageContext.request.contextPath}/image/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" /></a>
         <span id='errorapprovedDate' style="display:none;color:red;font-weight:bold">&nbsp;x</span>       
         <input type="hidden" name="approvedDateHidden" id="approvedDateHidden" value=""/>     
        </td>                         
	</tr>
		
    <tr id="newStatusRow2">
		<td class="whiteboxwk"><span class="mandatory">*</span><s:text name='tenderNegotiation.approver'/>: </td>
        <td class="whitebox2wk"> 
   		<s:select id="approvedBy" name="negotiationStatusApprovedById" cssClass="selectwk" list="%{dropdownData.approvedByList}"    listKey="id" listValue="employeeName" onchange='showDesignation(this);' disabled="disabled" headerKey="-1" headerValue="--- Select ---" />
 	</td>
        <td class="whiteboxwk"><s:text name='tenderNegotiation.approverdesignation'/>: </td>
        <td class="whitebox2wk">
		<s:textfield name="designation" type="text" readonly="true" disabled="disabled" cssClass="selectboldwk" id="designation" size="25" tabIndex="-1" value="%{designation}"/>
		</td>                         
	</tr>
		<s:hidden name="nsTenderResponseId" value="%{id}"/>
		
</s:if>	
