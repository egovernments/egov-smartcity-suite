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
					<td colspan="9" class="headingwk" align="left">
						<div class="arrowiconwk">
							<img src="${pageContext.request.contextPath}/image/arrow.gif"/>
						</div>
						<s:if test="%{mode!='showDWforEstimate'}">
							<div class="headerplacer">
								<s:text name='depositworks.roadcut.search.request' />
							</div>
						</s:if>
						<s:if test="%{mode=='showDWforEstimate'}">
							<div class="headerplacer">
								<s:text name='estimate.depositworks.search.request' />
							</div>
						</s:if>	
					</td>
				</tr>
				<s:if test="%{zoneId!=null && zoneId!=-1 && mode!='showDWforEstimate'}">
					<tr align="center">
						<td colspan="9" class="headingwk" align="left">
								<div class="headerplacer">
									<s:text name='depositworks.roadcut.search.zone.title' /><s:text name="zoneName" />
								</div>
						</td>
					</tr>
					
				</s:if>
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
			
			<s:if test="%{mode=='edit' || sourcepage=='utilizationCertificate'}">
				<display:column headerClass="pagetableth" class="pagetabletd"
					title="Select" titleKey="column.title.select"
					style="width:2%;text-align:left">					
					<s:hidden name="applRequestStatus" id="applRequestStatus" value="%{#attr.currentRow.applicationRequest.egwStatus.code}" />
					<s:hidden name="serviceConnection" id="serviceConnection" value="%{#attr.currentRow.applicationRequest.depositWorksType.name}" />
					<input name="radio" type="radio" id="radio"
						value="<s:property value='%{#attr.currentRow.id}'/>"
						onClick="gotoEditPage(this);" />
				</display:column>
			</s:if>	
			
			<s:if test="%{ mode=='showDWforEstimate' }">
				<display:column headerClass="pagetableth" class="pagetabletd"
					title="Select" titleKey="column.title.select"
					style="width:2%;text-align:left">
					<input name="radio" type="radio" id="radio"
						value="<s:property value='%{#attr.currentRow.id}'/>"
						onClick="initAppDetailsId(this);" />
				</display:column>
			</s:if>
			
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Sl.No"
				titleKey="column.title.SLNo"
				style="width:2%;text-align:right" >
					<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
					<s:hidden name="applicationDetailsId" id="applicationDetailsId" value="%{#attr.currentRow.id}" />
					<s:hidden name="depositWorksCategory" id="depositWorksCategory" value="%{#attr.currentRow.applicationRequest.depositWorksCategory}" />
			</display:column>
			
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Type Of Cut"
				titleKey="depositworks.roadcut.typeofcut"
				style="width:10%;text-align:left" property="applicationRequest.depositWorksType.name" />
			
			<s:if test="%{zoneId==null || zoneId==-1}">	
				<display:column headerClass="pagetableth"
					class="pagetabletd" title="Zone"
					titleKey="depositworks.zone"
					style="width:10%;text-align:left" >
					<s:iterator var="s" value="#attr.currentRow.applicationRequest.roadCutDetailsList" status="status">
						<s:property value="%{zone.name}" />
						<s:if test="!#status.last">,</s:if>
					</s:iterator>
				</display:column>	
					
			</s:if>	
			
			<s:if test="%{wardId==null || wardId==-1}">	
				<display:column headerClass="pagetableth"
					class="pagetabletd" title="Ward"
					titleKey="depositworks.ward"
					style="width:10%;text-align:left" >
					<s:iterator var="s" value="#attr.currentRow.applicationRequest.roadCutDetailsList" status="status">
						<s:property value="%{ward.name}" />
						<s:if test="!#status.last">,</s:if>
					</s:iterator>
				</display:column>	
			</s:if>			
			
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Application Request Number"
				titleKey="depositworks.roadcut.search.applicationno"
				style="width:10%;text-align:left" property="applicationRequest.applicationNo" />
				
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Initiated By"
				titleKey="depositworks.roadcut.search.initiatedby"
				style="width:10%;text-align:left" property="preparedBy.employeeName" />
				
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
			
			<s:if test="%{mode=='showDWforEstimate'}">
				<display:column headerClass="pagetableth"
					class="pagetabletd" title="Fund"
					titleKey="depositworks.fund"
					style="width:10%;text-align:left" >
				<s:if test="%{#attr.currentRow.depositCode.code!=''}">
					<s:property value="#attr.currentRow.depositCode.fund.name"/>
				</s:if>
				<s:if test="%{#attr.currentRow.applicationRequest.depositWorksCategory==@org.egov.works.models.depositWorks.DepositWorksCategory@BPA}">
					<s:property value="#attr.currentRow.applicationRequest.rateContract.indent.fund.name"/>
				</s:if>
				</display:column>
			</s:if>
			<s:if test="%{mode!='edit' && mode!='showDWforEstimate' && sourcepage!='utilizationCertificate'}" >
				<display:column headerClass="pagetableth"
					class="pagetabletd" title="Application Status"
					titleKey="depositworks.roadcut.status"
					style="width:10%;text-align:left" property="applicationRequest.egwStatus.description" />
				<s:if test="%{mode!='showDWforEstimate'}">
					<s:if test="%{applicationRequest.egwStatus.code=='-1' || applicationRequest.egwStatus.code=='FEASIBILITYREPORTCREATED' 
					|| applicationRequest.egwStatus.code=='ROADCUTAPPROVALLETTERCREATED' || applicationRequest.egwStatus.code=='UTILISATIONCERTIFICATECREATED'}">
						<display:column headerClass="pagetableth"
						class="pagetabletd" title="Owner"
						titleKey="depositworks.owner"
						style="width:10%;text-align:left" property="owner" />
					</s:if>
				</s:if>
			</s:if>
			<s:if test="%{mode!='edit' && mode!='showDWforEstimate' && sourcepage!='utilizationCertificate'}">
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
			</s:if>
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
<s:if test="%{searchResult.fullListSize != 0 && mode=='showDWforEstimate'}">
	<div class="buttonholderwk" id="buttons">
		 <input type="button" class="buttonadd" value="Create Estimate" id="createEstimate" name="createEstimate"  onclick="openEstimateScreen();" />
	     <input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
	</div>
</s:if>	     
</div>	