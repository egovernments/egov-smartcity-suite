
<script src="<egov:url path='js/works.js'/>"></script>

<script type="text/javascript">

function setEstimateRcId(elem){
document.techSanctionEstimatesForm.estimateRcId.value=elem.value;
}

function createWorkorder(){
	if(!createWO())
		return false;
	var id = document.techSanctionEstimatesForm.estimateRcId.value;
	//window.open("${pageContext.request.contextPath}/workorder/workOrder!newform.action?type=workOrderForRC&estimateRcId="+id,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	window.open('${pageContext.request.contextPath}/workorder/workOrder!newform.action?type=workOrderForRC&estimateRcId='+id,'_self');
}

function createWO(){
 	var id = document.techSanctionEstimatesForm.estimateRcId.value;
	if(id==null || id==''){
		showMessage('error_search','<s:text name="search.estimate.WorkOrder.create"/>');
		disableSelect();
		return false;
	}
	else{
		 clearMessage('error_search');
		 return true;
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

	<display:column title="Select" headerClass="pagetableth" class="pagetabletd" style="width:4%;text-align:center">
			<input name="radio" type="radio" id="radio"
				value="<s:property value='%{#attr.currentRow.id}'/>"
				onClick="setEstimateRcId(this);" />
	</display:column>
	<display:column title="Sl.No" titleKey='estimate.search.slno' headerClass="pagetableth" class="pagetabletd" style="width:4%;text-align:right" >
		<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize" />
	</display:column> 

	<display:column headerClass="pagetableth" class="pagetabletd" title="Estimate Number" style="width:10%;text-align:left" property="estimate.estimateNumber" />

	<display:column headerClass="pagetableth" class="pagetabletd" title="Executing Department" titleKey='estimate.search.executingdept' style="width:8%;text-align:left" property='estimate.executingDepartment.deptName' />

	<display:column headerClass="pagetableth" class="pagetabletd" title="Name" titleKey='estimate.search.name' style="width:20%;text-align:left" property='estimate.name' />
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="Contractor" titleKey='estimate.search.name' style="width:20%;text-align:left" property='rateContract.contractor.name' />	

	<display:column headerClass="pagetableth" class="pagetabletd" title="<a href='javascript:sortBy();'>Status</a>" titleKey='estimate.search.status' style="width:10%;text-align:left">
		<s:if test="%{#attr.currentRow.estimate.state.previous.value=='ADMIN_SANCTIONED' || #attr.currentRow.estimate.state.previous.value=='CANCELLED'}">
			<s:property value="#attr.currentRow.estimate.state.previous.value" />
		</s:if>
		<s:else>
			<s:property value="#attr.currentRow.estimate.state.value" />
		</s:else>
	</display:column>
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="Type" titleKey='estimate.search.type' style="width:10%;text-align:left" property='estimate.type.name' />

	<display:column headerClass="pagetableth" class="pagetabletd" title="Estimate Date" titleKey='estimate.search.estimateDate' style="width:10%;text-align:center" >
		<s:date name="#attr.currentRow.estimate.estimateDate" format="dd/MM/yyyy" />
	</display:column>
	
	<display:column headerClass="pagetableth" class='pagetabletd' title="Owner" titleKey='estimate.search.owner' style="width:10%;text-align:left" property='estimate.positionAndUserName' >
	</display:column>
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="Total" titleKey='estimate.search.total' style="width:10%;text-align:right" property='workValueIncludingTaxes.formattedString' />
</display:table>

<s:if test="%{searchResult.fullListSize != 0}" >
	<table width="100%" border="0" cellpadding="0" 	cellspacing="0">
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
				<td align="center">
			    	<input type="button" class="buttonadd" value="Create Work Order" id="addButton" name="selectWorkOrderButton" onclick="createWorkorder();" align="center" />
				</td>
			    </tr>
	</table>
</s:if>