<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<script type="text/javascript">
</script>

<table width="100%" border="0" cellspacing="0" cellpadding="0"> 	
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
		        	<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
		            <div class="headplacer"><s:text name="depositworks.search.results" /></div></td>
				</tr>
			</table>
		</td>
	</tr>
</table>	

<div>
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
					<s:hidden name="applicationDetailsId" id="applicationDetailsId" value="%{#attr.currentRow.id}" />
			</display:column>
			
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Type Of Cut"
				titleKey="depositworks.roadcut.typeofcut"
				style="width:8%;text-align:left" property="applicationRequest.depositWorksType.name" />
			
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Zone"
				titleKey="depositworks.zone"
				style="width:5%;text-align:left" >
				<s:iterator var="s" value="#attr.currentRow.applicationRequest.roadCutDetailsList" status="status">
					<s:property value="%{zone.name}" />
					<s:if test="!#status.last">,</s:if>
				</s:iterator>
			</display:column>	
					
			
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Ward"
				titleKey="depositworks.ward"
				style="width:5%;text-align:left" >
				<s:iterator var="s" value="#attr.currentRow.applicationRequest.roadCutDetailsList" status="status">
					<s:property value="%{ward.name}" />
					<s:if test="!#status.last">,</s:if>
				</s:iterator>
			</display:column>	
			
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Application Request Number"
				titleKey="depositworks.roadcut.search.applicationno"
				style="width:10%;text-align:left" property="applicationRequest.applicationNo" />
				
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Application Date"
				titleKey="depositworks.date"
				style="width:6%;text-align:left" >
					<s:date name="#attr.currentRow.applicationRequest.applicationDate" format="dd/MM/yyyy" />
			</display:column>
			
			<s:if test="%{applicationType!='BPA'}">
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Deposit Code"
				titleKey="depositworks.deposit.code"
				style="width:10%;text-align:left" >
				<s:if test="%{#attr.currentRow.depositCode.code!=''}">
					<s:property value="#attr.currentRow.depositCode.code"/>
				</s:if>
				<s:elseif test="%{#attr.currentRow.applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@BPA}">
					<s:text name='search.appRequest.depositWorksCategory.BPA' />
				</s:elseif>
				<s:else>
					<s:text name='search.appRequest.deposit.code.not.generated' />
				 </s:else>
				
			</display:column>
			</s:if>
			
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Claim Charges"
				titleKey="depositworks.claimcharges"
				style="width:6%;text-align:right" >
				<s:if test="%{#attr.currentRow.applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@BPA}">
					<s:text name='search.appRequest.depositWorksCategory.BPA' />
				</s:if>
				<s:else>
					<s:text name="contractor.format.number" >
						<s:param name="rate" value="#attr.currentRow.estimatedCost"/>
					</s:text>
				</s:else>
			</display:column>	
			
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Application Status"
				titleKey="depositworks.roadcut.status"
				style="width:10%;text-align:left" property="applicationRequest.egwStatus.description" />
				
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Actions"
				titleKey="depositworks.roadcut.search.actions"
				style="width:10%;text-align:left">
				
				<s:select theme="simple"
						list="%{#attr.currentRow.applicationDetailsActions}"
						name="appDetailsActions" id="appDetailsActions"
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