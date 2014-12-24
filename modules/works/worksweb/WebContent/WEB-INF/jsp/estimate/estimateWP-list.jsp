<script type="text/javascript">
function setEstimateId(elem){
	document.techSanctionEstimatesForm.estimateId.value = elem.value;
}

 function checkAll(obj){ 
	var len=document.forms[0].selectedEstimate.length;
	if(obj.checked){
		if(len>0){
			for (i = 0; i < len; i++)
				document.forms[0].selectedEstimate[i].checked = true;
		}else document.forms[0].selectedEstimate.checked = true;
	}
	else{
		if(len>0){
			for (i = 0; i < len; i++)
				document.forms[0].selectedEstimate[i].checked = false;
		}else document.forms[0].selectedEstimate.checked = false;
	}
}
</script>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="9" class="headingwk" align="left">
						<div class="arrowiconwk">
							<img src="${pageContext.request.contextPath}/image/arrow.gif" />
						</div>
						<div class="headerplacer">
							<s:text name='page.title.search.estimates' />
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

<display:table name="searchResult" pagesize="30" uid="currentRow"
	cellpadding="0" cellspacing="0" requestURI=""
	style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">

	<display:column headerClass="pagetableth" class="pagetabletd"
		title="Sl.No" titleKey="estimate.search.slno"
		style="width:4%;text-align:right">
		<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
		<s:hidden name="abEstId" id="abEstId" value="%{#attr.currentRow.id}" />
	</display:column>

	<display:column headerClass="pagetableth" class="pagetabletd"
		title='<input type="checkbox" id="checkedAll" name="checkedAll" onclick="checkAll(this)" />' 
		style="width:3%;text-align:center">
		<s:checkbox id="selectedEstimate" name="selectedEstimate" />
	</display:column>

	<display:column headerClass="pagetableth" class="pagetabletd"
		title="Estimate Number" titleKey="estimate.search.estimateNo"
		style="width:11%;text-align:left" property="estimateNumber" />

	<display:column headerClass="pagetableth" class="pagetabletd"
		title="Executing Department" titleKey="estimate.search.executingdept"
		style="width:14%;text-align:left"
		property="executingDepartment.deptName" />

	<display:column headerClass="pagetableth" class="pagetabletd"
		title="Name" titleKey="estimate.search.name"
		style="width:20%;text-align:left">
		<a
			href="${pageContext.request.contextPath}/estimate/abstractEstimate!edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&sourcepage=search"><s:property
				value='%{#attr.currentRow.name}' /> </a>
	</display:column>

	<display:column headerClass="pagetableth" class="pagetabletd"
		title="Type" titleKey="estimate.search.type"
		style="width:10%;text-align:left" property="type.name" />

	<display:column headerClass="pagetableth" class="pagetabletd"
		title="Estimate Date" titleKey="estimate.search.estimateDate" 
		style="width:8%;text-align:left" >
		<s:date name="#attr.currentRow.estimateDate" format="dd/MM/yyyy" />
	</display:column>

	<display:column headerClass="pagetableth" class="pagetabletd"
		title="Owner" titleKey="estimate.search.owner"
		style="width:10%;text-align:left" property="positionAndUserName" />

	<display:column headerClass="pagetableth" class="pagetabletd"
		title="WorkFlow History" titleKey="estimate.wrokflow.history"
		style="width:8%;text-align:left">
		<a href="#"
			onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimate!workflowHistory.action?stateValue=<s:property value='%{#attr.currentRow.state.id}'/>', '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');">
			History </a>
	</display:column>

	<display:column headerClass="pagetableth" class="pagetabletd"
		title="Total" titleKey="estimate.search.total"
		style="width:10%;text-align:right"
		property="workValue" /> 
</display:table>

<s:if test="%{searchResult.fullListSize>0}">
	<div class="buttonholderwk">
		<input type="button" class="buttonfinal" value="ADD" id="button"
			name="button" onclick="returnBackToParent()" />
		<input type="button" class="buttonfinal" value="CLOSE"
			id="closeButton" name="closeButton" onclick="window.close();" />
	</div>
</s:if>

