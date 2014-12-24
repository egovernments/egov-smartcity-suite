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
							<s:text name="label.search.results" />
						</div>
					</td>
				</tr>
				</table>
			</td>
		</tr>
</table>
<div align="center">
	<s:if test="%{searchResult.fullListSize != 0}">
		<s:text id="SlNo" name="%{getText('lwc.report.serialNo')}"></s:text>
		<s:text id="contractorName" name="%{getText('lwc.report.contractorName')}"></s:text>
		<s:text id="woValue" name="%{getText('lwc.report.woValue')}"></s:text>
		<s:text id="paymentAmount" name="%{getText('lwc.report.paymentAmount')}"></s:text>
		<s:text id="paymentDate" name="%{getText('lwc.report.paymentDate')}"></s:text>
		<s:text id="amountDeducted" name="%{getText('lwc.report.amountDeducted')}"></s:text>
		<s:text id="billDate" name="%{getText('lwc.report.billDate')}"></s:text>
		<s:text id="remittedDate" name="%{getText('lwc.report.remittedDate')}"></s:text>
		
		<display:table name="searchResult" 
			uid="currentRow" cellpadding="0" cellspacing="0" 
			requestURI="" sort="external" class="its"  
			style="border:0px;width:100%;empty-cells:show;border-collapse:collapse;">	
			<display:caption  media="html"style='font-weight:bold' >
				<s:text name="lwc.report.header" />
			</display:caption> 		
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${SlNo}"
				style="width:3%;text-align:left" >
					<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
			</display:column>
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${contractorName}"
				style="width:20%;text-align:left" property="contractorNameAndAddress" />
				
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${woValue}"
				style="width:10%;text-align:right" property="workOrderValue" />
				 
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${paymentAmount}"
				style="width:10%;text-align:right" property="paymentAmount" />
				
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${paymentDate}"
				style="width:10%;text-align:left" property="paymentDate" />
			
											
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${billDate}"
				style="width:10%;text-align:left" property="billDate" />
				
				<display:column headerClass="pagetableth"
					class="pagetabletd" title="${amountDeducted}"
					style="width:10%;text-align:right" property="taxDeductedAmt" />
					
				<display:column headerClass="pagetableth"
					class="pagetabletd" title="${remittedDate}"
					style="width:10%;text-align:left" property="remittedDate" />					
					
			</display:table> 
			<div class="buttonholdersearch" align = "center">
      			<s:submit value="Export to PDF" cssClass="buttonpdf" id="pdfButton" onclick="return validateInput();" method="exportToPdf" name="button" />
      			<s:submit value="Export to Excel" cssClass="buttonpdf" id="excelButton" onclick="return validateInput();" method="exportToExcel" name="button" />
    		</div>
    		<div align="right" class="mandatory" style="font-size:11px;padding-right:20px;"># <s:text name="lwc.report.footnote" /></div>
	</s:if>
	<s:elseif test="%{searchResult.fullListSize == 0}">
			<div>
				<table width="100%" border="0" cellpadding="0"
					cellspacing="0">
					<tr>
						<td align="center">
							<font color="red"><s:text name="search.result.no.record" /></font>
						</td>
					</tr>
				</table>
			</div>
	</s:elseif>	
</div>	