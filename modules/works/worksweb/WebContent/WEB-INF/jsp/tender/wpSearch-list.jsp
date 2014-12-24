<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<script type="text/javascript">
function gotoPage(obj)
{
	var currRow=getRow(obj);
	var packageIden = getControlInBranch(currRow,'packageIden');
	var packageStateId = getControlInBranch(currRow,'packageStateId');
	var docNumber = getControlInBranch(currRow,'docNumber');
	var approvedDate = getControlInBranch(currRow,'approvedDate');
	var objNo = getControlInBranch(currRow,'wpNum');
	if(dom.get('searchActions')[1]!=null && obj.value==dom.get('searchActions')[1].value)
	{
		window.open("${pageContext.request.contextPath}/tender/worksPackage!edit.action?id="+packageIden.value+
		"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(dom.get('searchActions')[2]!=null && obj.value==dom.get("searchActions")[2].value)
	{
		document.location.href="${pageContext.request.contextPath}/tender/worksPackage!viewWorksPackagePdf.action?id="+packageIden.value;
	}
	if(dom.get('searchActions')[3]!=null && obj.value==dom.get("searchActions")[3].value)
	{
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!workflowHistory.action?stateValue="+
		packageStateId.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(dom.get('searchActions')[4]!=null && obj.value==dom.get("searchActions")[4].value)
	{
		if(docNumber.value!= null && docNumber.value!='') {
			viewDocumentManager(docNumber.value);return false;
		}
		else {
			alert("No Documents Found");
			return;
		}
	}
	if(dom.get('searchActions')[5]!=null && obj.value==dom.get("searchActions")[5].value)
	{
		window.open("${pageContext.request.contextPath}/workorder/setStatus!edit.action?objectType=WorksPackage&objId="+
		packageIden.value+"&setStatus="+dom.get('setStatus').value+"&appDate="+approvedDate.value+"&objNo="+objNo.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
}
</script>
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 	
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td>
		<s:hidden name="setStatus" id="setStatus" value="%{setStatus}"/>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="9" class="headingwk" align="left">
						<div class="arrowiconwk">
							<img src="${pageContext.request.contextPath}/image/arrow.gif"/>
						</div>
						<div class="headerplacer">
								<s:text name='page.result.search.estimate' />
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>			
			<display:table name="searchResult" pagesize="30" uid="currentRow"
				cellpadding="0" cellspacing="0" requestURI=""
				style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">

				<display:column title="Sl.No" titleKey='estimate.search.slno'
					headerClass="pagetableth" class="pagetabletd"
					style="width:3%;text-align:right">
					<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
				</display:column>

				<display:column title="Package Number" titleKey='wp.No'
					property="wpNumber" headerClass="pagetableth" class="pagetabletd"
					style="width:5%;text-align:center"></display:column>

				<display:column title="Name" titleKey='wp.name' property="name"
					headerClass="pagetableth" class="pagetabletd"
					style="width:14%;text-align:left">
				</display:column>

				<display:column title="WorksPackage Date" titleKey='wp.date'
					headerClass="pagetableth" class="pagetabletd" style="width:5%;text-align:center">
					<s:date name="#attr.currentRow.packageDate" format="dd/MM/yyyy" />
				</display:column>

				<display:column title="Department" titleKey='wp.dept'
					property="userDepartment.deptName" headerClass="pagetableth"
					class="pagetabletd" style="width:5%;text-align:left">
				</display:column>

				<display:column title="Owner" titleKey='estimate.search.owner'
					property="employeeName" headerClass="pagetableth"
					class="pagetabletd" style="width:5%;text-align:left">
				</display:column>

				<display:column title="Total" titleKey='estimate.search.total'
					headerClass="pagetableth" class="pagetabletd"
					style="width:5%;text-align:right">
					<s:text name="contractor.format.number">
						<s:param name="value" value="%{#attr.currentRow.totalAmount}" />
					</s:text>
				</display:column>

				<display:column title="Actions" titleKey='action'
					headerClass="pagetableth" class="pagetabletd"
					style="width:4%;text-align:center">
					<s:hidden name="wpNum" id="wpNum"
						value="%{#attr.currentRow.wpNumber}" />
					<s:hidden name="docNumber" id="docNumber"
						value="%{#attr.currentRow.documentNumber}" />
					<s:hidden name="packageIden" id="packageIden"
						value="%{#attr.currentRow.id}" />
					<s:hidden name="packageStateId" id="packageStateId"
						value="%{#attr.currentRow.state.id}" />
					<s:if
						test="%{state.previous!=null && approvedValue!=null && approvedValue.equals(state.previous.value)}">
						<s:hidden name="approvedDate" id="approvedDate"
							value="%{#attr.currentRow.packageDate}" />
					</s:if>
					<s:else>
						<s:hidden name="approvedDate" id="approvedDate" value="%{#attr.currentRow.packageDate}" />
					</s:else>

					<s:select theme="simple" id="searchActions" name="searchActions"
						list="worksPackageActions"
						headerValue="%{getText('estimate.default.select')}" headerKey="-1"
						onchange="gotoPage(this);"></s:select>

				</display:column>

			</display:table>
		</td>
	</tr>
</table>
