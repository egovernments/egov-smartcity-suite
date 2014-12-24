<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td colspan="4" class="headingwk">
					<div class="arrowiconwk">
						<img src="${pageContext.request.contextPath}/image/arrow.gif" />
					</div>
					<div class="headplacer"><s:text name="returnsecuritydeposit.header" /></div>
				</td>
			</tr>
      	 	<tr>
				<td class="whiteboxwk"><s:text name="returnsecuritydeposit.workname" />:</td>
          		<td class="whitebox2wk"><s:property value="%{model.workOrder.workOrderEstimates.get(0).estimate.name}" /></td>
          		<td class="whiteboxwk"><s:text name="contractorBill.workOrderEstValue" />:</td>
          		<td class="whitebox2wk"><s:textfield readonly="true" name="totalWorkValue"  id="totalWorkValue" align="right"  value="%{totalWorkValue}" /></td>
     		 </tr>
  	    	<tr>
				<td class="greyboxwk"><s:text name="returnsecuritydeposit.workordernum" />:</td>
          		<td class="greybox2wk"><s:property value="%{model.workOrder.workOrderNumber}" /></td>
              	<td class="greyboxwk"><s:text name="returnsecuritydeposit.workorderDate" />:</td>
              	<td class="greybox2wk"><s:date name="workOrder.workOrderDate" var="workOrderDateFormat" format="dd/MM/yyyy"/>
              	<s:property value="%{workOrderDateFormat}" /></td>
     		</tr>	
     		<tr>
				<td class="whiteboxwk"><s:text name="returnsecuritydeposit.contractor" />:</td>
  	        	<td class="whitebox2wk"><s:property value="%{model.contractor.name}" /></td>
      	        <td class="whiteboxwk"><s:text name="returnsecuritydeposit.owner" />:</td>
          	    <td class="whitebox2wk"><s:property value="%{model.workOrder.owner}" /></td>
     		 </tr>
     		 <tr>
				<td class="greyboxwk"><s:text name="work.completionDate" />:</td>
	  	        <td class="greybox2wk"><s:date name="model.workOrder.workOrderEstimates.get(0).workCompletionDate" var="workCompletionDateFormat" format="dd/MM/yyyy"/>
	            <s:property value="%{workCompletionDateFormat}" /></td>
	      	    <td class="greyboxwk"><s:text name="workCompletion.defectliabilityperiod" />:</td>
	          	<td class="greybox2wk">
	          		<s:if test="%{workOrder.defectLiabilityPeriod==0}">
	          			<s:textfield  name="defectLiabilityPeriod" value="%{defectLiabilityPeriod}" id="defectLiabilityPeriod" />
	          	    </s:if>
	          	    <s:else>
	          	    	<s:property value="%{workOrder.defectLiabilityPeriod}" />
	          	    </s:else>
	          	 </td>
     		 </tr>		
     		 <tr>
				<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="returnsecuritydeposit.accounthead" />:</td>
	            <td class="whitebox2wk">
	            	<s:select id="glcode" name="glcode" headerKey="-1"  headerValue="%{getText('accounthead.default.select')}"
					cssClass="selectwk" list="%{dropdownData.sdcoaList}" value="%{model.glcode.id}"
					listKey="id" listValue='glcode  + " ~ " + name'
					onchange= "getSDAmountdeducted()"/></td>
				<td class="whiteboxwk"><s:text name="returnsecuritydeposit.amountdeducted" />:</td>
	          	<td class="whitebox2wk"><div id="SDdeductedAmount"><s:property value="%{model.securityDepositAmountdeducted}" /></div>
	          	<s:hidden  name="securityDepositAmountdeducted" id="securityDepositAmountdeducted" value="%{model.securityDepositAmountdeducted}"/></td>
           	</tr>
      	 
      		<tr>
				<td class="greyboxwk"><span class="mandatory">*</span><s:text name="returnsecuritydeposit.returnamount" />:</td>
          		<td class="greybox2wk"><s:textfield name="returnSecurityDepositAmount"  id="returnSecurityDepositAmount"  cssClass="amount" value="%{returnSecurityDepositAmount}" /></td>
              	<td class="greyboxwk">                		
              	<s:text name="returnsecuritydeposit.remarks" />:</td>
          		<td class="greybox2wk"><s:textfield name="remarks"  id="remarks"   value="%{remarks}" /></td>
      		</tr>
      		<tr>		
				<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
				<div class="headplacer"><s:text name='contractorBill.mb.details' /></div></td>
			</tr>
			<tr>
                	<td colspan="4">
	                	<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
							<tr>
								<td width="4%" class="tablesubheadwk">
									<s:text name='estimate.search.slno' />
								</td>
								<td colspan="2" width="13%" class="tablesubheadwk">
									<s:text name="measurementbook.mbref" />
								</td>
								<td colspan="2" width="13%" class="tablesubheadwk">
									<s:text name="returnsecuritydeposit.mbdate" />
								</td>
								<td width="14%" class="tablesubheadwk" style="WORD-BREAK:BREAK-ALL">
									<s:text name="measurementbook.frompage" />-<s:text name="measurementbook.topage" />
								</td>
							
							</tr>					
						</table>
					</td>
			</tr>
</table>
<% 
						int count=0;
					%>
				<s:iterator var="e" value="mbHeaderList"> 
					<% 
						count++;
					%>
				</s:iterator>
			<% if(count>10){ %>

			<div class="scrollerboxaddestimate" >
			<%}%>
			
				<table width="100%" border="0" cellspacing="0" cellpadding="0" id="mbListTable" name="mbListTable">
					<s:iterator var="e" value="mbHeaderList" status="s">	
						<tr>
							<td width="4%" class="whitebox3wk">
								<s:property value='%{#s.index+1}' />
							</td>
							<td width="13%" class="whitebox3wk">
								<s:property value='%{mbRefNo}' />
							</td>
							<td width="13%" class="whitebox3wk">
								<s:date name="mbDate" var="mbDateFormat" format="dd/MM/yyyy"/>
              					<s:property value="%{mbDateFormat}" />
							</td>
							<td width="14%" class="whitebox3wk">
								<s:property value="fromPageNo"/><s:if test="%{toPageNo!=null && toPageNo!='' && toPageNo>0}">-<s:property value="toPageNo"/></s:if>
							</td>
													
						</tr>
					</s:iterator>	
				</table>
				
				<% if(count>10){ %>

					</div>
				<%}%>
<table width="100%" align="center">		
	<tr>
		<td colspan="4" class="shadowwk"></td>
	</tr>	
</table>
