<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<script src="<egov:url path='js/works.js'/>"></script>

<script type="text/javascript">

function gotoPage(obj)
{
	var currRow=getRow(obj);
	var estimateId = getControlInBranch(currRow,'estimateId');
	
	if(dom.get('searchActions')[1]!=null && obj.value==dom.get('searchActions')[1].value)
	{
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!edit.action?id="+estimateId.value+
		"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(dom.get('searchActions')[2]!=null && obj.value==dom.get("searchActions")[2].value)
	{
		document.location.href="${pageContext.request.contextPath}/estimate/abstractEstimatePDF.action?estimateID="+estimateId.value;
	}
	
}
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
						<div class="headerplacer">
							<s:text name='search.results' />
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
		<s:text id="estNumber" name="%{getText('label.estimateNumber')}"></s:text>
		<s:text id="execdept" name="%{getText('label.execDept')}"></s:text>
		<s:text id="userdept" name="%{getText('label.userDept')}"></s:text>
		<s:text id="estName" name="%{getText('label.name')}"></s:text>
		<s:text id="estType" name="%{getText('label.type')}"></s:text>
		<s:text id="estDate" name="%{getText('label.estimateDate')}"></s:text>
		<s:text id="owner" name="%{getText('label.owner')}"></s:text>
		<s:text id="total" name="%{getText('label.totalAmount')}"></s:text>
		<s:text id="actions" name="%{getText('label.actions')}"></s:text>
		
<display:table name="searchResult" pagesize="30" uid="currentRow"
	cellpadding="0" cellspacing="0" requestURI=""
	style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">

	<display:caption style='font-weight:bold'>
			<s:property value="%{reportSubTitle}"/>
	</display:caption>
	<display:column title="${SlNo}" titleKey='estimate.search.slno' headerClass="pagetableth" class="pagetabletd" style="width:3%;text-align:right" >
		<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize" />
	</display:column> 

	<display:column headerClass="pagetableth" class="pagetabletd" title="${estNumber}" style="width:10%;text-align:left" property="estimateNumber" />
	<display:column headerClass="pagetableth" class="pagetabletd" title="${execdept}" titleKey='estimate.search.executingdept' style="width:8%;text-align:left" property='executingDepartment.deptName' />
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="${userdept}" titleKey='estimate.search.userdept' style="width:8%;text-align:left" property='userDepartment.deptName' />
		
	<display:column headerClass="pagetableth" class="pagetabletd" title="${estName}" titleKey='estimate.search.name' style="width:24%;text-align:left" property='name' />

	<display:column headerClass="pagetableth" class="pagetabletd" title="${estType}" titleKey='estimate.search.type' style="width:13%;text-align:left" property='type.name' />

	<display:column headerClass="pagetableth" class="pagetabletd" title="${estDate}" titleKey='estimate.search.estimateDate' style="width:9%;text-align:center" >
		<s:date name="#attr.currentRow.estimateDate" format="dd/MM/yyyy" />
	</display:column>	
	
	<display:column headerClass="pagetableth" class='pagetabletd' title="${owner}" titleKey='estimate.search.owner' style="width:12%;text-align:left" property='positionAndUserName' />
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="${total}" titleKey='estimate.search.total' 
	style="width:8%;text-align:right" property='totalAmount.formattedString' />
	
	<display:column title="${actions}" titleKey="estimate.search.action" headerClass="pagetableth" class="pagetabletd" style="width:16%;text-align:center">
	
	<s:hidden name="estimateId" id="estimateId" value="%{#attr.currentRow.id}" />
	<s:hidden name="estimateStateId" id="estimateStateId" value="%{#attr.currentRow.state.id}" />
	<s:select theme="simple" id="searchActions" name="searchActions"
			list="plannedEstimateActions"
			headerValue="%{getText('estimate.default.select')}" headerKey="-1"
			onchange="gotoPage(this);"></s:select>
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