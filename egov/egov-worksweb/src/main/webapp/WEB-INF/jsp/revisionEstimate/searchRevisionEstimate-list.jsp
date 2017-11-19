<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>

<%@ include file="/includes/taglibs.jsp" %>
<script type="text/javascript">
var estimateArr = new Array();
var selectedFund = "";

function gotoPage(obj){
	var currRow=getRow(obj);
	var id = getControlInBranch(currRow,'revEstId');
	var revWOStateId=getControlInBranch(currRow,'revEstStateId');	
    var document_Number = getControlInBranch(currRow,'docNo');
   if(dom.get('searchActions')[1]!=null && obj.value==dom.get('searchActions')[1].value){	
		var url = '${pageContext.request.contextPath}/revisionEstimate/revisionEstimate!edit.action?sourcepage=search&id='+id.value;
		window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(dom.get('searchActions')[2]!=null && obj.value==dom.get('searchActions')[2].value){
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!workflowHistory.action?stateValue="+
				revWOStateId.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(dom.get('searchActions')[3]!=null && obj.value==dom.get('searchActions')[3].value){
		viewDocumentManager(document_Number.value);return false;
	}
}

function setREWorkorderEstimateId(elem){ 
	var currRow=getRow(elem);
	dom.get("reWOEstimateId").value = elem.value; 
	dom.get("revWOId").value=getControlInBranch(currRow,'woId').value;
	validateCancelJSON(elem.value);	
}

function cancelRevisionEstimate(){
	var reWOEstId = dom.get("reWOEstimateId").value;
	clearErrorMsg(); 	
	if(reWOEstId!=''){		
		validateCancel();
	}		
	else{
		dom.get("searchRevisionEstimate_error").style.display='';
		document.getElementById("searchRevisionEstimate_error").innerHTML='<s:text name="re.cancel.not.selected" />';
		window.scroll(0,0);
		return false;
	  }
}

function clearErrorMsg()
{
	if(dom.get("searchRevisionEstimate_error")){
		dom.get("searchRevisionEstimate_error").style.display='none';
		dom.get("searchRevisionEstimate_error").innerHTML='';
	}
}

function validateCancelJSON(reWOEstId)
{
	makeJSONCall(["errorMessage"],'${pageContext.request.contextPath}/revisionEstimate/ajaxRevisionEstimate!validateCancellation.action',{reWOEstId:reWOEstId},validateSuccessHandler,validateFailureHandler);
}

function validateCancel() 
{	
	var cancellationReason = document.searchRevisionEstimateForm.cancellationReason.value; 
	var cancelRemarks = document.searchRevisionEstimateForm.cancelRemarks.value; 
	if(cancellationReason==''){
		dom.get("searchRevisionEstimate_error").style.display='';
		document.getElementById("searchRevisionEstimate_error").innerHTML='<s:text name="validate.cancel.cancelReasons"/>'; 
		window.scroll(0,0);
		return false;
	}	
	if(cancellationReason=='OTHER' && cancelRemarks == ''){
		dom.get("searchRevisionEstimate_error").style.display='';
		document.getElementById("searchRevisionEstimate_error").innerHTML='<s:text name="validate.cancel.re.remarks"/>'; 
		window.scroll(0,0);
		return false;
	}	
	if(confirmCancellation()){
		document.searchRevisionEstimateForm.action='searchRevisionEstimate!cancelApprovedRE.action';
		doLoadingMask();
		document.searchRevisionEstimateForm.submit();  
	}
	else
		return false;
}

validateSuccessHandler = function(req,res){
	clearErrorMsg();
	results=res.results;
	var errorMessage=results[0].errorMessage;
	if(errorMessage != ''){
		dom.get("searchRevisionEstimate_error").style.display='';
		document.getElementById("searchRevisionEstimate_error").innerHTML=errorMessage;
		window.scroll(0,0);
		return false;
	}
}

validateFailureHandler= function(){
    dom.get("searchRevisionEstimate_error").style.display='';
	document.getElementById("searchRevisionEstimate_error").innerHTML='Error while validating cancellation';
}

function confirmCancellation() {
	var msg='<s:text name="re.cancel.confirm"/>';
	var woNo=""; 
	if(!confirmCancel(msg,woNo)) {
		return false;
	}
	else {
		return true;
	}
}

function toggleCancelRemarks(obj) { 
	if(obj.value=='OTHER') {
		document.getElementById("cancelRemarksDtls").style.display='';
	}
	else {
		document.getElementById("cancelRemarksDtls").style.display='none';
		dom.get("cancelRemarks").value='';
	}	
}

function viewWO(id){
	window.open("${pageContext.request.contextPath}/workorder/workOrder!edit.action?id="+id+"&mode=search",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}   

</script>
<div>
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
							<img src="/egworks/resources/erp2/images/arrow.gif" />
						</div>

						<div class="headerplacer">
							<s:text name='revisionEstimate.estimates.header' />
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

 <s:if test="%{searchResult.fullListSize != 0 }">
		<display:table name="searchResult" pagesize="30" uid="currentRow"
			cellpadding="0" cellspacing="0" requestURI=""
			style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
			<s:if test="%{source=='cancelRE'}">
				<display:column headerClass="pagetableth" class="pagetabletd" title="Select" titleKey="column.title.select" style="width:4%;text-align:center">						
					<input name="radio" type="radio" id="radio"
						value="<s:property value='%{#attr.currentRow.id}'/>"
						onClick="setREWorkorderEstimateId(this);" />
					<s:hidden id="woId" name="woId"
						value="%{#attr.currentRow.workOrder.id}" />
					<s:hidden id="originalEstId" name="originalEstId"
						value="%{#attr.currentRow.estimate.id}" />
				</display:column>
			</s:if>
			<display:column title="Sl.No" titleKey='estimate.search.slno'
				headerClass="pagetableth" class="pagetabletd"
				style="width:4%;text-align:right">
				<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize" />
			</display:column>
			
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Executing Department"
				titleKey='estimate.search.executingdept'
				style="width:13%;text-align:left">
				<s:property
					value='#attr.currentRow.estimate.executingDepartment.deptName' />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Name of Work" titleKey='estimate.search.nameOfWork'
				style="width:30%;text-align:left">
				<s:property value='#attr.currentRow.estimate.name' />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Type of Work" titleKey='estimate.search.typeofWork'
				style="width:10%;text-align:left">
				<s:property value='#attr.currentRow.estimate.parent.parentCategory.description' />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Revision Estimate Number" style="width:12%;text-align:left">
				<s:property value="#attr.currentRow.estimate.estimateNumber" />
				<s:hidden name="revEstId" id="revEstId"
					value="%{#attr.currentRow.estimate.id}" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Revision Estimate Amount" titleKey='revision.estimate.amount'
				style="width:9%;text-align:right">
				<s:property
					value='#attr.currentRow.estimate.totalAmount.formattedString' />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Status" titleKey='revisionEstimate.search.status'
				style="width:7%;text-align:left">
				<s:property value='#attr.currentRow.estimate.egwStatus.description' />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Work Order Number" titleKey='workorder.search.workordernumber' 
				style="width:15%;text-align:left">
				<a href="#" onclick="viewWO('<s:property value='%{#attr.currentRow.workOrder.parent.id}'/>')">
			<s:property value='%{#attr.currentRow.workOrder.parent.workOrderNumber}' /> </a>
			</display:column>
			
			<s:if test="%{source!='cancelRE'}">
				<display:column headerClass="pagetableth" class="pagetabletd"
					title="Owner" titleKey='revisionEstimate.search.status'
					style="width:7%;text-align:left">
					<s:property value='#attr.currentRow.estimate.positionAndUserName' />
				</display:column>
			</s:if>
			
			<display:column title='&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Action' titleKey="estimate.search.action"
				headerClass="pagetableth" class="pagetabletd"
				style="width:10%;text-align:center">
				<s:hidden name="revEstStateId" id="revWOStateId"
					value="%{#attr.currentRow.estimate.state.id}" />
				<s:hidden name="docNo" id="docNo"
					value="%{#attr.currentRow.estimate.documentNumber}" />
				<s:select theme="simple" id="searchActions" name="searchActions"
					list="actionsList"
					headerValue="%{getText('estimate.default.select')}" headerKey="-1"
					onchange="gotoPage(this);"></s:select>
			</display:column>			
		</display:table>
	</s:if>
 <s:elseif test="%{searchResult.fullListSize == 0 && !hasErrors()}">
		  <div>
			<table width="100%" border="0" cellpadding="0" 	cellspacing="0">
				<tr>
				   <td align="center">
					 <font color="red"><s:text name="search.result.no.record" /></font>
				   </td>
			    </tr>
			</table>
		  </div>
</s:elseif>
	
<div class="errorstyle" id="error_search" style="display: none;"></div>
<s:hidden name="revEstimateId" id="revEstimateId"/>
</div>

<table width="100%" border="0" cellspacing="0" cellpadding="0">	
	 <s:if test="%{source.equals('cancelRE')}">
	 <tr>												   
	  <td align="left" class="whitebox2wk">
		<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="mandatory">*</span><s:text name="cancellation.reason" />:</b>&nbsp;&nbsp;
		<s:select id="cancellationReason" name="cancellationReason" cssClass="selectwk" list="#{'':'---------Select---------','DATA ENTRY MISTAKE':'DATA ENTRY MISTAKE','OTHER':'OTHER'}" onChange="toggleCancelRemarks(this)" />
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<span id="cancelRemarksDtls" style="display:none"><b><span class="mandatory">*</span><s:text name="cancel.remarks" />:</b>&nbsp;&nbsp;
		<s:textarea id="cancelRemarks" name="cancelRemarks" rows="2" cols="35" />
		</span> 
	  </td>
	</tr> 
	<tr>
		<td colspan="4">
			<div class="buttonholderwk">
			<input type="button" class="buttonadd"
				value="Cancel Revision Estimate" id="addButton"
				name="cancelREst" onclick="cancelRevisionEstimate()"
				align="center" />
			</div>
		</td>
	</tr>
	</s:if>
</table>	
