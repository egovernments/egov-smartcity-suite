<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td  class="headingwk" align="left">
						<div class="arrowiconwk">
							<img src="${pageContext.request.contextPath}/image/arrow.gif"/>
						</div>
							<div class="headerplacer">
								<s:text name='workCompletion.originalDetail' />
							</div>
					</td>
				</tr>
</table>
<table width="100%" border="1" cellspacing="0" cellpadding="0">
	<tr>
		<td class="tablesubheadwk"><s:text name='workCompletion.workcompletiondate' /></td>
		<td class="tablesubheadwk"><s:text name='workCompletion.contractperiod' /></td>
		<td class="tablesubheadwk"><s:text name='workCompletion.defectliabilityperiod' /></td>
	</tr>
	<s:if test="%{workCompletionDetailsList==null || workCompletionDetailsList.size==0}">
		<tr>
		<td align="center"><s:date name="workOrder.workCompletionDate" format="dd/MM/yyyy" /></td>
		<td align="center"><s:property value="%{workOrder.contractPeriod}"/></td>
		<td align="center"><s:property value="%{workOrder.defectLiabilityPeriod}" /></td>
		</tr>
	</s:if>
	<s:else>
		<s:iterator value="%{workCompletionDetailsList}" status="wocdrowstatus"> 
		<s:if test="%{currentState==null && egwStatus==null}">
			<tr>
				<td><s:date name="workCompletionDate" format="dd/MM/yyyy" /></td>
				<td><s:property value="%{contractPeriod}"/></td>
				<td><s:property value="%{defectLiabilityPeriod}"/></td>
			</tr>
		</s:if>
	</s:iterator>
	</s:else>
</table>
<s:if test="%{workCompletionDetailsList.size>0}">
<br/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td  class="headingwk" align="left">
						<div class="arrowiconwk">
							<img src="${pageContext.request.contextPath}/image/arrow.gif"/>
						</div>
							<div class="headerplacer">
								<s:text name='workCompletion.updatedDetails' />
							</div>
					</td>
				</tr>
</table>
<table width="100%" border="1" cellspacing="0" cellpadding="0">
	<tr>
		<td class="tablesubheadwk"><s:text name='column.title.SLNo' /></td>
		<td class="tablesubheadwk"><s:text name='workCompletion.workcompletiondate' /></td>
		<td class="tablesubheadwk"><s:text name='workCompletion.contractperiod' /></td>
		<td class="tablesubheadwk"><s:text name='workCompletion.defectliabilityperiod' /></td>
		<td class="tablesubheadwk"><s:text name='workCompletion.reasonforextension' /></td>
	</tr>
	<%int slno=1; %>
	
	<s:iterator value="%{workCompletionDetailsList}" status="wocdrowstatus"> 
		<s:if test="%{currentState!=null && egwStatus!=null}">
			<tr>
				<td align="center"><%=slno%></td>
				<td align="center"><s:date name="workCompletionDate" format="dd/MM/yyyy" /></td>
				<td align="center"><s:property value="%{contractPeriod}"/></td>
				<td align="center"><s:property value="%{defectLiabilityPeriod}"/></td>
				<td align="center"><s:property value="%{reasonForExtension}"/></td>
			</tr>
			<%slno=slno+1;%>
			
		</s:if>
	</s:iterator>
</table>
</s:if>
<br/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td  class="headingwk" align="left">
						<div class="arrowiconwk">
							<img src="${pageContext.request.contextPath}/image/arrow.gif"/>
						</div>
							<div class="headerplacer">
								<s:text name='workCompletion.newDetails' />
							</div>
					</td>
				</tr>
</table>
<table width="100%" border="1" cellspacing="0" cellpadding="0">
	<tr>
		<td class="tablesubheadwk"><s:text name='column.title.SLNo' /></td>
		<td class="tablesubheadwk"><s:text name='workCompletion.workcompletiondate' /><span class="mandatory">*</span></td>
		<td class="tablesubheadwk"><s:text name='workCompletion.contractperiod' /></td>
		<td class="tablesubheadwk"><s:text name='workCompletion.defectliabilityperiod' /></td>
		<td class="tablesubheadwk"><s:text name='workCompletion.reasonforextension' /></td>
	</tr>
	<tr><td align="center">1.</td>
		<td align="center">
		 <s:date name="workCompletionDate" var="workCompletionDateFormat" format="dd/MM/yyyy"/>
		    <s:textfield name="workCompletionDate" value="%{workCompletionDateFormat}" id="workCompletionDate" cssClass="selectboldwk" 
        		 onfocus="javascript:vDateType='3';" 
        		 onkeyup="DateFormat(this,this.value,event,false,'3')"  onblur="isvalidFormat(this)"/>
        		 <a href="javascript:show_calendar('forms[0].workCompletionDate',null,null,'DD/MM/YYYY');" id="dateHref2"
        		 onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;">
        		 <img src="${pageContext.request.contextPath}/image/calendar.png" id="wpDateImg" alt="Calendar" width="16" height="16" 
       			  border="0" align="absmiddle" /></a> </td>
		<td align="center"><s:textfield name="contractPeriod" value="%{contractPeriod}" id="contractPeriod" cssClass="selectwk" onkeypress="return isNumberKey(event)"/></td>
		<td align="center"><s:textfield  name="defectLiabilityPeriod" value="%{defectLiabilityPeriod}" id="defectLiabilityPeriod" cssClass="selectamountwk" onkeypress="return isNumberKey(event)"/></td>
		<td><s:textarea name="reasonForExtension" cols="27" rows="4" cssClass="selectwk" id="reasonForExtension" />
		</td>
	</tr>
	
</table>