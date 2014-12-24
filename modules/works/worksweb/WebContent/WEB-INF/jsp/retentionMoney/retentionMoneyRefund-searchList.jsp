<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%> 
<script type="text/javascript">
function gotoPage(obj){
	var currRow=getRow(obj);
	var id = getControlInBranch(currRow,'retentionMoneyRefundId');
	var showActions = getControlInBranch(currRow,'showActions');
	var retentionMoneyRefundStateId=getControlInBranch(currRow,'retentionMoneyRefundStateId');
	if(showActions[1]!=null && obj.value==showActions[1].value){	
		window.open("${pageContext.request.contextPath}/retentionMoney/retentionMoneyRefund!edit.action?id="+id.value+
		"&sourcepage=search",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(showActions[2]!=null && obj.value==showActions[2].value){	
	
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!workflowHistory.action?stateValue="+
				retentionMoneyRefundStateId.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');	
	}
}
</script>
<div>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="7" class="headingwk">
				<div class="arrowiconwk">
					<img
						src="${pageContext.request.contextPath}/image/arrow.gif" />
				</div>
				<div class="headplacer">
					<s:text name="title.search.result" />
				</div>
			</td>
		</tr>
	</table>

	<s:if test="%{searchResult.fullListSize != 0}">
		<display:table name="searchResult" pagesize="30"
		uid="currentRow" cellpadding="0" cellspacing="0"
		requestURI=""
		style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
		
			<display:column headerClass="pagetableth"
			class="pagetabletd" title="Sl.No"
			titleKey="column.title.SLNo"
			style="width:2%;text-align:right" >
				<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
				<s:hidden name="retentionMoneyRefundId" id="retentionMoneyRefundId" value="%{#attr.currentRow.id}" />
				<s:hidden name="retentionMoneyRefundStateId" id="retentionMoneyRefundStateId" value="%{#attr.currentRow.state.id}" />
			</display:column>
		
			<display:column headerClass="pagetableth"
			class="pagetabletd" title="Workorder Number"
			titleKey="workorder.search.workordernumber"
			style="width:10%;text-align:left" property="workOrder.workOrderNumber" />
		
			<display:column headerClass="pagetableth"
			class="pagetabletd" title="Work Order Date"
			titleKey="workorder.search.workorderdate"
			style="width:6%;text-align:left" >
				<s:date name="#attr.currentRow.workOrder.workOrderDate" format="dd/MM/yyyy" />
			</display:column>
				
		
			<display:column headerClass="pagetableth"
			class="pagetabletd" title="Contractor"
			titleKey="workorder.search.contractor"
			style="width:15%;text-align:left" property="contractor.name" />
		
			<display:column headerClass="pagetableth"
			class="pagetabletd" title="Owner"
			titleKey="workorder.search.owner"
			style="width:10%;text-align:left" property="owner" />
		
			<display:column headerClass="pagetableth"
			class="pagetabletd" title="Status"
			titleKey="workorder.search.status"
			style="width:8%;text-align:left" property="egwStatus.code" />
		
			<display:column headerClass="pagetableth"
			class="pagetabletd" title="Work Order Amount"
			titleKey="rmr.search.workordervalue"
			style="width:10%;text-align:right" >
				<s:text name="contractor.format.number" >
					<s:param name="rate" value='%{#attr.currentRow.workOrder.workOrderAmount}' />
				</s:text>
			</display:column>
			
			<display:column headerClass="pagetableth"
			class="pagetabletd" title="Amount Refunded"
			titleKey="rmr.amount.refunded"
			style="width:10%;text-align:right" >
				<s:text name="contractor.format.number" >
					<s:param name="refund" value='%{#attr.currentRow.retentionMoneyBeingRefunded}' />
				</s:text>
			</display:column>
			
			<display:column headerClass="pagetableth"
			class="pagetabletd" title="Bill Number"
			titleKey="contractorBill.billNumber"
			style="width:8%;text-align:left" property="egBillregister.billnumber" />
			
			<display:column headerClass="pagetableth"
			class="pagetabletd" title="Actions"
			titleKey="rsd.search.actions"
			style="width:10%;text-align:left">
		
				<s:select theme="simple"
						list="%{#attr.currentRow.refundRetentionMoneyActions}"
						name="showActions" id="showActions"
						headerValue="--- Select ---"
						headerKey="-1" onchange="gotoPage(this);">
				</s:select>
			</display:column>
			
		</display:table> 
	</s:if> 
	<s:elseif test="%{searchResult.fullListSize == 0}">
		<div>
			<table width="100%" border="0" cellpadding="0"
				cellspacing="0">
				<tr>
					<td align="center">
						<font color="red">No record Found.</font>
					</td>
				</tr>
			</table>
		</div>
	</s:elseif>  
</div> 

                           		