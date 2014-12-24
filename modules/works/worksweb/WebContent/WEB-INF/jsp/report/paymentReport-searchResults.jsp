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
		<s:text id="SlNo" name="%{getText('label.slno')}"></s:text>	
		<s:text id="estNoAndDate" name="%{getText('estimate.number')}"></s:text>
		<s:text id="nameOfWork" name="%{getText('label.nameOfWork')}"></s:text>
		<s:text id="jurisdiction" name="%{getText('label.jurisdiction')}"></s:text>
		<s:text id="budgetHead" name="%{getText('label.budgetHead')}"></s:text>	
		<s:text id="woNo" name="%{getText('workorder.search.workordernumber')}"></s:text>
		<s:text id="contractorName" name="%{getText('label.contractorName')}"></s:text>
		<s:text id="pjctcode" name="%{getText('payment.report.projectcode')}"></s:text>
		<s:text id="billNumDate" name="%{getText('payment.report.billNumDate')}"></s:text>
		<s:text id="billtype" name="%{getText('payment.report.billtype')}"></s:text>
		<s:text id="billAmount" name="%{getText('payment.report.amount')}"></s:text>
		
		<display:table name="searchResult" pagesize="30"
			uid="currentRow" cellpadding="0" cellspacing="0"
			requestURI=""
			style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
			
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${SlNo}"
				titleKey="workorder.report.slno"
				style="width:3%;text-align:left" >
					<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
			</display:column>
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${budgetHead}" style="width:12%;text-align:left" 
				property="budgetHead" >
			</display:column>
			
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${estNoAndDate}" style="width:10%;text-align:left" 
				property="estNumAndDate" >
			</display:column>
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${nameOfWork}" style="width:18%;text-align:left" 
				property="nameOfWork" >
			</display:column>
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${jurisdiction}" style="width:6%;text-align:left" 
				property="jurisdiction" >
			</display:column>
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${pjctcode}" style="width:8%;text-align:left" 
				property="projectCode" >
			</display:column>
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${woNo}" style="width:8%;text-align:left" 
				property="WONumAndDate" >
			</display:column>
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${contractorName}" style="width:13%;text-align:left" 
				property="contractorName" >
			</display:column>
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${billNumDate}" style="width:9%;text-align:left" 
				property="billNumAndDate" >
			</display:column>
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${billtype}" style="width:7%;text-align:left" 
				property="typeOfBill" >
			</display:column>
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${billAmount}" style="width:6%;text-align:right" 
				property="billAmount" >
			</display:column>
		</display:table>
		<div class="buttonholdersearch" align = "center">
      			<s:submit value="Export to PDF" cssClass="buttonpdf" id="pdfButton" method="exportToPdf" name="button" />
      			<s:submit value="Export to Excel" cssClass="buttonpdf" id="excelButton" method="exportToExcel" name="button" />
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
		