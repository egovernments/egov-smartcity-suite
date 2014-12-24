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
		<s:text id="SlNo" name="%{getText('label.slno')}"></s:text>		
		<s:text id="estNoAndDate" name="%{getText('label.estNoAndDate')}"></s:text>
		<s:text id="nameOfWork" name="%{getText('label.nameOfWork')}"></s:text>
		<s:text id="jurisdiction" name="%{getText('label.jurisdiction')}"></s:text>
		<s:text id="budgetHead" name="%{getText('label.budgetHead')}"></s:text>
		<s:text id="estimateAmount" name="%{getText('label.estimateAmount')}"></s:text>
		<s:text id="woNoAndDate" name="%{getText('label.woNoAndDate')}"></s:text>
		<s:text id="woAmount" name="%{getText('label.woAmount')}"></s:text>
		<s:text id="totalRecordedMbAmount" name="%{getText('label.totalRecordedMbAmount')}"></s:text>		
		<s:text id="contractorName" name="%{getText('label.contractorName')}"></s:text>
		
		<display:table name="searchResult" 
			uid="currentRow" cellpadding="0" cellspacing="0" 
			requestURI="" sort="external" class="its"  
			style="border:0px;width:100%;empty-cells:show;border-collapse:collapse;">	 	
			<display:caption  media="html"style='font-weight:bold' >
				<s:property value="%{reportSubTitle}" escape="false"/> 
			</display:caption>				
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${SlNo}"
				style="width:3%;text-align:left" >
					<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
			</display:column>
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${estNoAndDate}"
				style="width:10%;text-align:left" property="estNoAndDate" />
				
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${nameOfWork}"
				style="width:25%;text-align:left" property="nameOfWork" />
				 
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${jurisdiction}"
				style="width:7%;text-align:left" property="jurisdiction" />
				
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${budgetHead}"
				style="width:12%;text-align:left" property="budgetHead" />
			
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${estimateAmount}"
				style="width:8%;text-align:right" property="estimateAmount" />
															
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${woNoAndDate}"
				style="width:10%;text-align:left" property="woNoAndDate" />
				
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${woAmount}"
				style="width:8%;text-align:right" property="woAmount" />
				
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${totalRecordedMbAmount}"
				style="width:8%;text-align:right" property="totalRecordedMbAmount" />		
			
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${contractorName}"
				style="width:10%;text-align:left" property="contractorName" />					
					
			</display:table> 
			<div class="buttonholdersearch" align = "center">
      			<s:submit value="Export to PDF" cssClass="buttonpdf" id="pdfButton" onclick="return validateInput();" method="exportToPdf" name="button" />
      			<s:submit value="Export to Excel" cssClass="buttonpdf" id="excelButton" onclick="return validateInput();" method="exportToExcel" name="button" />
    		</div>
    		<div align="right" class="mandatory" style="font-size:11px;padding-right:20px;"># <s:text name="mb.report.woAmount.footnote" /></div>
    		<div align="right" class="mandatory" style="font-size:11px;padding-right:20px;"># <s:text name="mb.report.estAmount.footnote" /></div>
    		
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