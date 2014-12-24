
<script src="<egov:url path='js/works.js'/>"></script>

<script type="text/javascript">
var isError=false;
/*
function setEstimateId(elem){
	document.techSanctionEstimatesForm.estimateId.value = elem.value;
}

function createWorkorder(){
	if(!createWO())
		return false;
	var id = document.techSanctionEstimatesForm.estimateId.value;
	window.open("${pageContext.request.contextPath}/workorder/workOrder!newform.action?sourcepage=workOrderForRC&estimateId="+id,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');

}

function createWO(){
 	var id = document.techSanctionEstimatesForm.estimateId.value;
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
*/
function gotoPage(obj)
{
	var currRow=getRow(obj);
	var estimateIden = getControlInBranch(currRow,'estimateIden');
	var estimateStateId = getControlInBranch(currRow,'estimateStateId');
	var docNumber = getControlInBranch(currRow,'docNumber');
	
	if(dom.get('searchActions')[1]!=null && obj.value==dom.get('searchActions')[1].value)
	{
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!edit.action?id="+estimateIden.value+
		"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(dom.get('searchActions')[2]!=null && obj.value==dom.get("searchActions")[2].value)
	{
		document.location.href="${pageContext.request.contextPath}/estimate/abstractEstimatePDF.action?estimateID="+estimateIden.value;
	}
	if(dom.get('searchActions')[3]!=null && obj.value==dom.get("searchActions")[3].value)
	{
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!workflowHistory.action?stateValue="+
		estimateStateId.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(dom.get('searchActions')[4]!=null && obj.value==dom.get("searchActions")[4].value)
	{
		viewDocumentManager(docNumber.value);return false;
	}
	if(dom.get('searchActions')[5]!=null && obj.value==dom.get("searchActions")[5].value) 
	{
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!viewBillOfQuantitiesPdf.action?id="+
		estimateIden.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(dom.get('searchActions')[6]!=null && obj.value==dom.get("searchActions")[6].value) 
	{
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!viewMeasurementSheetPdf.action?id="+
		estimateIden.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
}


function checkTenderFile(elem){
	clearMessage('searchEstimate_error');
	var estimateId = document.techSanctionEstimatesForm.estimateId.value;
	var myTenderFileSuccessHandler = function(req,res) {
	if(res.results[0].type=='') {
  	dom.get("searchEstimate_error").style.display='none';
  	document.getElementById("searchEstimate_error").innerHTML='';
	isError=false;
  }
  else {
	  if(res.results[0].type!='' && res.results[0].type=='TenderFile') {
	  document.getElementById("searchEstimate_error").style.display='';
	  document.getElementById("searchEstimate_error").innerHTML='<s:text name="cancelEstimate.tenderFile.created.message"/>'+res.results[0].number;
	  isError=true;
	  window.scroll(0,0);
	  }
	  if(res.results[0].type!='' && res.results[0].type=='WorkOrder') {
	   document.getElementById("searchEstimate_error").style.display='';
	   document.getElementById("searchEstimate_error").innerHTML='<s:text name="cancelEstimate.workOrder.created.message"/>'+res.results[0].number;
	   isError=true;
	   window.scroll(0,0);
	  }
  }
 } 

            
	var myTenderFileFailureHandler = function() {
	            dom.get("searchEstimate_error").style.display='';
	            document.getElementById("searchEstimate_error").innerHTML='<s:text name="estimate.check.fail"/>';
	            isError=true;
	            window.scroll(0,0);
	        }
	makeJSONCall(["type","number"],'${pageContext.request.contextPath}/estimate/ajaxEstimate!getApprovedTenderFileorWorkOrder.action',{estimateId:estimateId},myTenderFileSuccessHandler,myTenderFileFailureHandler) ;
}

function setEstimateIds(elem){
	document.techSanctionEstimatesForm.estimateId.value = elem.value;
	checkTenderFile(elem);
}

function setEstimateNumber(elem){
	document.techSanctionEstimatesForm.estimateNumber.value = elem;
}


function cancelAbstractEstimate() {
	if(dom.get('estimateId').value=='') {
		clearMessage('searchEstimate_error');
    	dom.get("searchEstimate_error").style.display='';
		document.getElementById("searchEstimate_error").innerHTML+='<s:text name="estimate.cancel.select.null" /><br>';
		isError=true
		window.scroll(0,0);
	}

    if(!isError && validateCancel()) {
    	document.techSanctionEstimatesForm.action='${pageContext.request.contextPath}/estimate/abstractEstimate!cancelApprovedEstimate.action';
		document.getElementById('status').disabled=false
    	document.techSanctionEstimatesForm.submit();
    }
}

function validateCancel() {
	var msg='<s:text name="estimate.cancel.confirm"/>';
	var estimateNo=document.techSanctionEstimatesForm.estimateNumber.value;
	if(!confirm(msg+": "+estimateNo+" ?")) {
		return false;
	}
	else {
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
						<s:if test="%{source=='financialdetail'}">
							<div class="headerplacer">
								<s:text name='page.title.financial.detail' />
							</div>
						</s:if>
						<s:elseif test="%{source=='technical sanction'}">
							<div class="headerplacer">
								<s:text name='page.title.Technical.Sanction' />
							</div>
						</s:elseif>
						<s:elseif test="%{source=='Financial Sanction'}">
							<div class="headerplacer">
								<s:text name='page.title.Financial.Sanction' />
							</div>
						</s:elseif>
						<s:elseif test="%{source=='AdministrativeSanction'}">
							<div class="headerplacer">
								<s:text name='page.title.Admin.Sanction' />
							</div>
						</s:elseif>
						<s:elseif test="%{source=='createNegotiation'}">
							<div class="headerplacer">
								<s:text name='page.result.search.estimate' />
							</div>
						</s:elseif>

						<s:else>
							<div class="headerplacer">
								<s:text name='page.title.search.estimates' />
							</div>
						</s:else>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

<display:table name="searchResult" pagesize="30" uid="currentRow"
	cellpadding="0" cellspacing="0" requestURI=""
	style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">

	<s:if
		test="%{source=='financialdetail' || source=='createNegotiation'}">
		<display:column headerClass="pagetableth" class="blueborderfortd" style="width:4%;text-align:center">
			<input name="radio" type="radio" id="radio"
				value="<s:property value='%{#attr.currentRow.id}'/>"
				onClick="setEstimateId(this);" />
		</display:column>
	</s:if>
	 <s:if test="%{source=='cancelEstimate'}">
		        <display:column headerClass="pagetableth" class="pagetabletd" title="Select" style="width:2%;" titleKey="column.title.select">
					<input name="radio" type="radio" id="radio"
						value="<s:property value='%{#attr.currentRow.id}'/>"
						onClick='setEstimateIds(this);setEstimateNumber("<s:property value='%{#attr.currentRow.number}'/>");' />
				</display:column>
	        </s:if>
	
	<display:column title="Sl.No" titleKey='estimate.search.slno' headerClass="pagetableth" class="pagetabletd" style="width:4%;text-align:right" >
		<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize" />
	</display:column> 

	<display:column headerClass="pagetableth" class="pagetabletd" title="Estimate Number" style="width:10%;text-align:left" property="estimateNumber" />
	<display:column headerClass="pagetableth" class="pagetabletd" title="Executing Department" titleKey='estimate.search.executingdept' style="width:8%;text-align:left" property='executingDepartment.deptName' />
	<display:column headerClass="pagetableth" class="pagetabletd" title="Name" titleKey='estimate.search.name' style="width:20%;text-align:left" property='name' />
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="<a href='javascript:sortBy();'>Status</a>" titleKey='estimate.search.status' style="width:10%;text-align:left">
		<s:if test="%{#attr.currentRow.state.previous.value=='ADMIN_SANCTIONED' || #attr.currentRow.state.previous.value=='CANCELLED'}">
			<s:property value="#attr.currentRow.state.previous.value" />
		</s:if>
		<s:else>
			<s:property value="#attr.currentRow.state.value" />
		</s:else>
	</display:column>
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="Type" titleKey='estimate.search.type' style="width:10%;text-align:left" property='type.name' />

	<display:column headerClass="pagetableth" class="pagetabletd" title="Estimate Date" titleKey='estimate.search.estimateDate' style="width:10%;text-align:center" >
		<s:date name="#attr.currentRow.estimateDate" format="dd/MM/yyyy" />
	</display:column>
	
	<display:column headerClass="pagetableth" class='pagetabletd' title="Owner" titleKey='estimate.search.owner' style="width:10%;text-align:left" property='positionAndUserName' />
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="Total" titleKey='estimate.search.total' style="width:10%;text-align:right" property='totalAmount.formattedString' />
	
	<display:column title='Action' titleKey="estimate.search.action" headerClass="pagetableth" class="pagetabletd" style="width:16%;text-align:center">
	<s:hidden name="docNumber" id="docNumber" value="%{#attr.currentRow.documentNumber}" />
	<s:hidden name="estimateIden" id="estimateIden" value="%{#attr.currentRow.id}" />
	<s:hidden name="estimateStateId" id="estimateStateId" value="%{#attr.currentRow.state.id}" />
	<s:select theme="simple" id="searchActions" name="searchActions"
			list="estimateActions"
			headerValue="%{getText('estimate.default.select')}" headerKey="-1"
			onchange="gotoPage(this);"></s:select>
	</display:column>

</display:table>
<s:if test="%{searchResult.fullListSize != 0 && source=='cancelEstimate'}" >
	<P align="left">
				<span class="mandatory">*</span><s:text name="cancel.remarks" />:
					<s:select id="cancelRemarks" name="cancelRemarks" cssClass="selectwk" list="#{'DATA ENTRY MISTAKE':'DATA ENTRY MISTAKE','OTHER':'OTHER'}" />
			</P>
			<P align="center">
				<input type="button" class="buttonadd"
					value="Cancel Estimate" id="addButton"
					name="cancelEstimate" onclick="cancelAbstractEstimate();"
					align="center" />
			</P>
		</s:if>
<script>
<s:if test="%{source=='cancelEstimate'}">
	disableSelect();
</s:if>
</script>