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
							<s:text name='label.search.result' />
						</div>
					</td>
				</tr>
				
				</table>
			</td>
		</tr>
</table>

<div>
<s:if test="%{searchResult.fullListSize != 0}">

		<s:text id="SlNo" name="%{getText('label.slno')}"></s:text>		
		<s:text id="billdepartment" name="%{getText('retentionMoneyRecoveryRegister.label.billdepartment')}"></s:text>
		<s:text id="contractorcode" name="%{getText('retentionMoneyRecoveryRegister.label.contractorcode')}"></s:text>
		<s:text id="contractorname" name="%{getText('retentionMoneyRecoveryRegister.label.contractorname')}"></s:text>
		<s:text id="projectcode" name="%{getText('retentionMoneyRecoveryRegister.label.projectcode')}"></s:text>
		<s:text id="projectname" name="%{getText('retentionMoneyRecoveryRegister.label.projectname')}"></s:text>
		<s:text id="billnumber" name="%{getText('retentionMoneyRecoveryRegister.label.billnumber')}"></s:text>
		<s:text id="billtype" name="%{getText('retentionMoneyRecoveryRegister.label.billtype')}"></s:text>
		<s:text id="billdate" name="%{getText('retentionMoneyRecoveryRegister.label.billdate')}"></s:text>
		<s:text id="vouchernumber" name="%{getText('retentionMoneyRecoveryRegister.label.vouchernumber')}"></s:text>
		<s:text id="billamount" name="%{getText('retentionMoneyRecoveryRegister.label.billamount')}"></s:text>
		<s:text id="retentionmoneyrecovered" name="%{getText('retentionMoneyRecoveryRegister.label.retentionmoneyrecovered')}"></s:text>
		<s:text id="dateOfRefund" name="%{getText('retentionMoneyRecoveryRegister.label.refundDate')}"></s:text>
		
<display:table name="searchResult" pagesize="30" uid="currentRow"
	cellpadding="0" cellspacing="0" requestURI=""
	style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">

	<display:caption style='font-weight:bold'>
			<s:property value="%{reportSubTitle}"/>
	</display:caption>
	<display:column title="${SlNo}" headerClass="pagetableth" class="pagetabletd" style="width:3%;text-align:right" >
		<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize" />
	</display:column> 

	<display:column headerClass="pagetableth" class="pagetabletd" title="${billdepartment}" style="width:10%;text-align:left" property="billDepartment" />
	<display:column headerClass="pagetableth" class="pagetabletd" title="${contractorcode}" style="width:8%;text-align:left" property='contractorCode' />
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="${contractorname}" style="width:8%;text-align:left" property='contractorName' />
		
	<display:column headerClass="pagetableth" class="pagetabletd" title="${projectcode}" style="width:12%;text-align:left" property='projectCode' />

	<display:column headerClass="pagetableth" class="pagetabletd" title="${projectname}" style="width:24%;text-align:left" property='projectName' />

	<display:column headerClass="pagetableth" class="pagetabletd" title="${billnumber}" style="width:10%;text-align:left" property='billNumber' />
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="${billtype}" style="width:9%;text-align:left" property='billType' />
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="${billdate}" style="width:9%;text-align:left" property='billDate' />
	
	<display:column headerClass="pagetableth" class='pagetabletd' title="${vouchernumber}" style="width:12%;text-align:left" property='voucherNumber' />
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="${billamount}"	style="width:8%;text-align:right" >
		<s:text name="contractor.format.number">
			<s:param name="value" value="%{#attr.currentRow.billAmount}" />
		</s:text>
	</display:column>
	<display:column headerClass="pagetableth" class="pagetabletd" title="${retentionmoneyrecovered}" style="width:8%;text-align:right" > 
		<s:text name="contractor.format.number">
			<s:param name="value" value="%{#attr.currentRow.retentionMoneyRecoveredAmount}" />
		</s:text>
	</display:column>
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="${dateOfRefund}" style="width:9%;text-align:left" >
			<s:property value="#attr.currentRow.refundDate" />
	</display:column>
	
	</display:table>
	<div class="buttonholdersearch" align = "center">
   			<s:submit value="Export to PDF" cssClass="buttonpdf" id="pdfButton" method="exportToPdf" name="button" onClick="disableMasking();" />
   			<s:submit value="Export to Excel" cssClass="buttonpdf" id="excelButton" method="exportToExcel" name="button" onClick="disableMasking();" />
   	</div>
		
	</s:if>
	<s:elseif test="%{searchResult.fullListSize == 0}">
			<div>
				<table width="100%" border="0" cellpadding="0"
					cellspacing="0">
					<tr>
						<td align="center">
							<font color="red"><s:text name="label.no.records.found" /></font>
						</td>
					</tr>
				</table>
			</div>
	</s:elseif>
</div>		