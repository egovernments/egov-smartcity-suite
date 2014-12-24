<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>

<script type="text/javascript">
var estimateArr = new Array();
var selectedFund = "";
function setEstimateId(elem){
	dom.get('revEstimateId').value = elem.value; 
}

function gotoPage(obj){
	var currRow=getRow(obj);
	var id = getControlInBranch(currRow,'revEstId');
	var revWOStateId=getControlInBranch(currRow,'revEstStateId');	
    var document_Number = getControlInBranch(currRow,'docNo');
   if(dom.get('searchActions')[1]!=null && obj.value==dom.get('searchActions')[1].value){	
		var url = '${pageContext.request.contextPath}/revisionEstimate/revisionEstimate!view.action?sourcepage=search&revEstimateId='+id.value;
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

function setworkorderId(elem){ 
	var currRow=getRow(elem);
	dom.get("workOrderId").value = elem.value; 
}

function cancelRevisionEstimate(){
	var id = dom.get("workOrderId").value; 	
	if(id!=''){		
		 getApprovedRevisionWO(id);			
		}		
	else{
		dom.get("searchEstimate_error").style.display='';
		document.getElementById("searchEstimate_error").innerHTML='<s:text name="re.cancel.not.selected" />';
		window.scroll(0,0);
		return false;
	  }
	  dom.get("searchEstimate_error").style.display='none';
	  document.getElementById("searchEstimate_error").innerHTML='';
	  if(dom.get("searchEstimate_error")){
	  	dom.get("searchEstimate_error").style.display='none';
	 	dom.get("searchEstimate_error").innerHTML='';
	}
}


function getApprovedRevisionWO(workOrderId){ 
    makeJSONCall(["revisionWO","mbRefNo"],'${pageContext.request.contextPath}/revisionEstimate/ajaxRevisionEstimate!getWOandMBDetails.action',{workOrderId:workOrderId},reLoadHandler,reLoadFailureHandler);
}

reLoadHandler = function(req,res){
  results=res.results;
  var mbRefNo='';
  var revisionWONo='';
  if(results != '') {
	  	revisionWONo=results[0].revisionWO;
	  	for(var i=1; i<results.length;i++) {
	  	  	if(results[i].mbRefNo != '' && results[i].mbRefNo != undefined){
			  	if(mbRefNo!='')
					mbRefNo=mbRefNo+', MB#: '+results[i].mbRefNo;
				else 
					mbRefNo=results[i].mbRefNo; 
	  	  	}
  		}
  }
	if(mbRefNo != ''){
		dom.get("searchEstimate_error").style.display='';
		document.getElementById("searchEstimate_error").innerHTML='<s:text name="cancelRE.MB.created.message"/>'+mbRefNo;
		window.scroll(0,0);
		return false;
	}	
	if(revisionWONo != ''){
		dom.get("searchEstimate_error").style.display='';
		document.getElementById("searchEstimate_error").innerHTML='<s:text name="cancelRE.RWO.created.message"/>'+revisionWONo;
		window.scroll(0,0);
		return false;
	}
	var id = dom.get("workOrderId").value; 	 	
	var cancelRemarks = document.getElementById("cancelRemarks").value; 
	if(validateCancel())
		window.open('${pageContext.request.contextPath}/revisionEstimate/searchRevisionEstimate!cancelApprovedRE.action?workOrderId='+id+'&sourcepage=cancelWO&cancelRemarks='+cancelRemarks,'_self');
	else
		return false;
}

reLoadFailureHandler= function(){
    dom.get("searchEstimate_error").style.display='';
	document.getElementById("searchEstimate_error").innerHTML='Unable to get RevisionWorkOrder / MBs for Revision Estimate';
}

function validateCancel() {
	var msg='<s:text name="re.cancel.confirm"/>';
	var woNo=""; 
	if(!confirmCancel(msg,woNo)) {
		return false;
	}
	else {
		return true;
	}
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
							<img src="${pageContext.request.contextPath}/image/arrow.gif" />
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

 <s:if test="%{searchResult.fullListSize != 0 && !hasErrors()}">
		<display:table name="searchResult" pagesize="30" uid="currentRow"
			cellpadding="0" cellspacing="0" requestURI=""
			style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">

			<s:if test="%{source=='cancelRE'}">
				<display:column headerClass="pagetableth" class="pagetabletd" title="Select" titleKey="column.title.select" style="width:4%;text-align:center">						
					<input name="radio" type="radio" id="radio"
						value="<s:property value='%{#attr.currentRow.workOrder.id}'/>"
						onClick="setworkorderId(this);" />
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
				style="width:8%;text-align:left">
				<s:property
					value='#attr.currentRow.estimate.executingDepartment.deptName' />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Name" titleKey='estimate.search.name'
				style="width:20%;text-align:left">
				<s:property value='#attr.currentRow.estimate.name' />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Type" titleKey='estimate.search.type'
				style="width:10%;text-align:left">
				<s:property value='#attr.currentRow.estimate.type.name' />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Revision Estimate Number" style="width:10%;text-align:left">
				<s:property value="#attr.currentRow.estimate.estimateNumber" />
				<s:hidden name="revEstId" id="revEstId"
					value="%{#attr.currentRow.estimate.id}" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Revision Estimate Date"
				titleKey='estimate.search.estimateDate'
				style="width:10%;text-align:center">
				<s:date name="#attr.currentRow.estimate.estimateDate"
					format="dd/MM/yyyy" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Total" titleKey='estimate.search.total'
				style="width:10%;text-align:right">
				<s:property
					value='#attr.currentRow.estimate.totalAmount.formattedString' />
			</display:column>
			<s:if test="%{!source.equals('cancelRE')}">
				<display:column title='Action' titleKey="estimate.search.action"
					headerClass="pagetableth" class="pagetabletd"
					style="width:16%;text-align:center">
					<s:hidden name="revEstStateId" id="revWOStateId"
						value="%{#attr.currentRow.estimate.state.id}" />
					<s:hidden name="docNo" id="docNo"
						value="%{#attr.currentRow.estimate.documentNumber}" />
					<s:select theme="simple" id="searchActions" name="searchActions"
						list="actionsList"
						headerValue="%{getText('estimate.default.select')}" headerKey="-1"
						onchange="gotoPage(this);"></s:select>
				</display:column>
			</s:if>
			
			<s:hidden id="woId" name="woId"
				value="%{#attr.currentRow.workOrder.id}" />
			<s:hidden id="originalEstId" name="originalEstId"
				value="%{#attr.currentRow.estimate.id}" />
		</display:table>
	</s:if>
 <s:elseif test="%{searchResult.fullListSize == 0 && !hasErrors()}">
		  <div>
			<table width="100%" border="0" cellpadding="0" 	cellspacing="0">
				<tr>
				   <td align="center">
					 <font color="red"><s:text name="search.result.no.recorod" /></font>
				   </td>
			    </tr>
			</table>
		  </div>
</s:elseif>
	
<div class="errorstyle" id="error_search" style="display: none;"></div>
<s:hidden name="revEstimateId" id="revEstimateId"/>
</div>

<s:if test="%{source.equals('cancelRE')}">
 <P align="left">
 <tr>
  <td colspan="2" class="whiteboxwk">
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="mandatory">*</span><s:text name="cancel.remarks" />:
 </td>
 <td colspan="2"  class="whitebox2wk">
	&nbsp;&nbsp;<s:select id="cancelRemarks" name="cancelRemarks" cssClass="selectwk" list="#{'DATA ENTRY MISTAKE':'DATA ENTRY MISTAKE','OTHER':'OTHER'}" />
</td>	
</tr>
</P>
<P align="center">
	<input type="button" class="buttonadd"
		value="Cancel Revision Estimate" id="addButton"
		name="cancelREst" onclick="cancelRevisionEstimate()" 
		align="center" />
</P>
</s:if>