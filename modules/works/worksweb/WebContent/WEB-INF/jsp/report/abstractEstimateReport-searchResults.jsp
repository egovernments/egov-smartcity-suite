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
						<div class="headerplacer">
							Search Results
						</div>
					</td>
				</tr>
				<tr align="center">
					<td colspan="9" class="headingwk" align="center">
							<div class="headerplacer">
								<s:property value="%{reportSubTitle}" escape="false"/> 
							</div>
					</td>
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
				class="pagetabletd" title="Sl No."
				titleKey="abstract.estimate.report.slno"
				style="width:3%;text-align:left" >
					<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
			</display:column>
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Estimate No. / Date"
				titleKey="abstract.estimate.report.estimate.no"
				style="width:10%;text-align:left" >
				<s:property value="%{#attr.currentRow.estimateNumber}"/>&nbsp;,<br>
				<s:date name="#attr.currentRow.estimateDate" format="dd/MM/yyyy" />
			</display:column>	
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Name of Work"
				titleKey="abstract.estimate.report.workname"
				style="width:20%;text-align:left" property="name" />
				
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Jurisdiction"
				titleKey="abstract.estimate.report.jurisdiction"
				style="width:10%;text-align:left" property="ward.name" />
				
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Budget Head"
				titleKey="abstract.estimate.report.budget.head"
				style="width:20%;text-align:left">
				<s:iterator var="s" value="#attr.currentRow.financialDetails" status="status" >
					<s:if test="%{#status.index == 0}">
						<s:property value="%{budgetGroup.maxCode.glcode}"/>~<s:property value="%{budgetGroup.name}"/>
					</s:if>	
				</s:iterator>
			</display:column>	
											
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Estimate Amount(Rs.)"
				titleKey="abstract.estimate.report.estimate.amt"
				style="width:7%;text-align:right" >
				<s:text name="contractor.format.number" >
					  <s:param name="rate" value='%{#attr.currentRow.totalAmount.value}' />
				</s:text>	
			</display:column>	   
			<s:if test="%{estimateStatus=='Technical Sanctioned' || estimateStatus=='Administrative Sanctioned'}">
				<display:column headerClass="pagetableth"
					class="pagetabletd" title="Technical Sanc. No / Date"
					titleKey="abstract.estimate.report.techsanc.no.date"
					style="width:10%;text-align:left" property="techSanctionNumber" >
				</display:column>		
			</s:if>		
			<s:if test="%{estimateStatus=='Administrative Sanctioned'}">	
				<display:column headerClass="pagetableth"
					class="pagetabletd" title="Admin Sanc. No / Date"
					titleKey="abstract.estimate.report.adminsanc.no.date"
					style="width:10%;text-align:left" >
					<s:if test="%{#attr.currentRow.adminsanctionNo!=null && #attr.currentRow.adminsanctionDate!=null}">
						<s:property value="%{#attr.currentRow.adminsanctionNo}"/>&nbsp;,<br>
						<s:date name="#attr.currentRow.adminsanctionDate" format="dd/MM/yyyy" />
					</s:if>
					<s:else>
						<s:property value="%{#attr.currentRow.adminsanctionNo}"/>&nbsp;
						<s:date name="#attr.currentRow.adminsanctionDate" format="dd/MM/yyyy" />
					</s:else>
				</display:column>	
			</s:if>		
			</display:table>
			<div class="buttonholdersearch" align = "center">
      			<s:submit value="Export to PDF" cssClass="buttonpdf" id="pdfButton" onclick="return validateInput();" method="exportToPdf" name="button" />
      			<s:submit value="Export to Excel" cssClass="buttonpdf" id="excelButton" onclick="return validateInput();" method="exportToExcel" name="button" />
    		</div>
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