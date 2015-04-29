
<script type="text/javascript">
function setEstimateId(elem){
	document.techSanctionEstimatesForm.estimateId.value = elem.value;
}
</script>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr height="5">
		<td></td>
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
							<s:text name='page.result.search.estimate' />
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
		style="width:3%;text-align:left">
		<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize" />
	</display:column>

	<display:column headerClass="pagetableth" class="pagetabletd"
		title="Select" titleKey="column.title.select"
		style="width:2%;text-align:left">
		<input name="radio" type="radio" id="radio"
			value="<s:property value='%{#attr.currentRow.id}'/>"
			onClick="setEstimateId(this);" />
	</display:column>

	<display:column headerClass="pagetableth" class="pagetabletd"
		title="Estimate Date" titleKey="estimate.search.estimateDate"
		style="width:8%;text-align:left" >
		<s:date name="#attr.currentRow.estimateDate" format="dd/MM/yyyy" />
	</display:column>

	<display:column headerClass="pagetableth" class="pagetabletd"
		title="Estimate Number" titleKey="estimate.search.estimateNo"
		style="width:10%;text-align:left" property="estimateNumber" />

	<display:column headerClass="pagetableth" class="pagetabletd"
		title="Name" titleKey="estimate.search.name"
		style="width:20%;text-align:left">
		<a
			href="${pageContext.request.contextPath}/estimate/abstractEstimate!edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&sourcepage=search">
			<s:property value='%{#attr.currentRow.name}' /> </a>
	</display:column>

	<display:column headerClass="pagetableth" class="pagetabletd"
		title="Total" titleKey="estimate.search.total"
		style="width:10%;text-align:right"
		property="totalAmount.formattedString"></display:column>

	<display:column headerClass="pagetableth" class="pagetabletd"
		title="Type" titleKey="estimate.search.type"
		style="width:10%;text-align:left" property="type.name"></display:column>

	<display:column headerClass="pagetableth" class="pagetabletd"
		title="Executing Depatrtment" titleKey="estimate.search.executingdept"
		style="width:10%;text-align:left"
		property="executingDepartment.deptName"></display:column>

</display:table>
