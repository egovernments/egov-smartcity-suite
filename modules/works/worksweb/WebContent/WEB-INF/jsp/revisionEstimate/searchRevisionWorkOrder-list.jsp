<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>

<script type="text/javascript">

function gotoPage(obj){
	var currRow=getRow(obj);
	var id = getControlInBranch(currRow,'revWorkOrderId');
	var revWOStateId=getControlInBranch(currRow,'revWOStateId');	
    var document_Number = getControlInBranch(currRow,'docNo');
	if(dom.get('searchActions')[1]!=null && obj.value==dom.get('searchActions')[1].value){	
		var url = '${pageContext.request.contextPath}/revisionEstimate/revisionWorkOrder!view.action?sourcepage=search&revWorkOrderId='+id.value;
		window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(dom.get('searchActions')[2]!=null && obj.value==dom.get('searchActions')[2].value){
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!workflowHistory.action?stateValue="+
				revWOStateId.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(dom.get('searchActions')[3]!=null && obj.value==dom.get('searchActions')[3].value){
		viewDocumentManager(document_Number.value);return false;
	}
	if(dom.get('searchActions')[4]!=null && obj.value==dom.get('searchActions')[4].value){
		var url = '${pageContext.request.contextPath}/revisionEstimate/revisionWorkOrder!viewRevWorkOrderPdf.action?revWorkOrderId='+id.value;
		window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
}

function setworkorderId_Cancel(elem){ 
	var currRow=getRow(elem);
	dom.get("woId").value = elem.value; 
}


function cancelRevisionWO(){
	var id = dom.get("woId").value; 	
	if(id!=''){		
		 getApprovedRevisionWO(id);			
		}		
	else{
		dom.get("searchEstimate_error").style.display='';
		document.getElementById("searchEstimate_error").innerHTML='<s:text name="rwo.cancel.not.selected" />';
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
    makeJSONCall(["mbRefNo"],'${pageContext.request.contextPath}/revisionEstimate/ajaxRevisionWorkOrder!getMBDetails.action',{workOrderId:workOrderId},rwoLoadHandler,rwoLoadFailureHandler);
}

rwoLoadHandler = function(req,res){
  results=res.results;
  var mbRefNo='';
  if(results != '') {
	  	for(var i=0; i<results.length;i++) {
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
		document.getElementById("searchEstimate_error").innerHTML='<s:text name="cancelRWO.MB.created.message"/>'+mbRefNo;
		window.scroll(0,0);
		return false;
	}	
	
	var id = dom.get("woId").value; 	 	
	var cancelRemarks = document.getElementById("cancelRemarks").value; 
	if(validateCancel())
		window.open('${pageContext.request.contextPath}/revisionEstimate/searchRevisionWorkOrder!cancelApprovedRWO.action?workOrderId='+id+'&sourcepage=cancelRWO&cancelRemarks='+cancelRemarks,'_self');
	else
		return false;
}

rwoLoadFailureHandler= function(){
    dom.get("searchEstimate_error").style.display='';
	document.getElementById("searchEstimate_error").innerHTML='Unable to get MBs for Revision WorkOrder';
}

function validateCancel() {
	var msg='<s:text name="rwo.cancel.confirm"/>';
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
							<s:text name='revisionEstimate.workOrders.header' />
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
 <s:if test="%{searchResult.fullListSize != 0 && !hasErrors()}">
<display:table name="searchResult" pagesize="30" uid="currentRow" cellpadding="0" cellspacing="0" requestURI=""style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">

	<s:if test="%{source=='cancelRWO'}">
		<display:column headerClass="pagetableth" class="pagetabletd" title="Select" titleKey="column.title.select" style="width:4%;text-align:center">						
			<input name="radio" type="radio" id="radio"
				value="<s:property value='%{#attr.currentRow.workOrder.id}'/>"
				onClick="setworkorderId_Cancel(this);" />
		</display:column>
	</s:if>
	
	<display:column title="Sl.No" titleKey='estimate.search.slno' headerClass="pagetableth" class="pagetabletd" style="width:4%;text-align:right" >
		<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize" />
	</display:column>  
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="Revision WorkOrder Number" style="width:10%;text-align:left">
	<s:property value="#attr.currentRow.workOrder.workOrderNumber" />
	<s:hidden name="revWorkOrderId" id="revWorkOrderId" value="%{#attr.currentRow.workOrder.id}" />
	</display:column>
	<display:column headerClass="pagetableth" class="pagetabletd" title="Revision WorkOrder Date" titleKey='revisionWorkOrder.search.Date' style="width:10%;text-align:center" >
		<s:date name="#attr.currentRow.workOrder.workOrderDate" format="dd/MM/yyyy" />
	</display:column>
	<display:column headerClass="pagetableth" class="pagetabletd" title="Contractor" titleKey="revisionWorkOrder.search.contractor" style="width:15%;text-align:left">
	<s:property value="#attr.currentRow.workOrder.contractor.name" />
	</display:column>
	<display:column headerClass="pagetableth" class="pagetabletd" title="Owner" titleKey="revisionWorkOrder.search.owner" style="width:10%;text-align:left">
		<s:property value="#attr.currentRow.workOrder.owner"/>
	</display:column>
	<display:column headerClass="pagetableth" class="pagetabletd" title="Status" titleKey="revisionWorkOrder.search.status" style="width:8%;text-align:left"> 
				<s:property value='%{#attr.currentRow.workOrder.egwStatus.code}' />
	 </display:column>
	<display:column headerClass="pagetableth" class="pagetabletd" title="Total" titleKey='revisionWorkOrder.search.total' style="width:10%;text-align:right">
					<s:text name="contractor.format.number" >
					<s:param name="woAmount" value='#attr.currentRow.workOrder.workOrderAmount' /></s:text>
	</display:column>
	<s:if test="%{!source.equals('cancelRWO')}">
		<display:column title='Action' titleKey="estimate.search.action" headerClass="pagetableth" class="pagetabletd" style="width:16%;text-align:center">
		<s:hidden name="revWOStateId" id="revWOStateId" value="%{#attr.currentRow.workOrder.state.id}" />
		<s:hidden name="docNo" id="docNo" value="%{#attr.currentRow.workOrder.documentNumber}" />
		<s:select theme="simple" id="searchActions" name="searchActions"
			list="actionsList"
			headerValue="%{getText('estimate.default.select')}" headerKey="-1"
			onchange="gotoPage(this);"></s:select>
		</display:column>
	</s:if>
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
</div>

<s:if test="%{source.equals('cancelRWO')}">
<p align="left">
<table>
 <tr>
	  <td colspan="2" class="whiteboxwk">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="mandatory">*</span><s:text name="cancel.remarks" />:
	 </td>
	 <td colspan="2"  class="whitebox2wk">
		&nbsp;&nbsp;<s:select id="cancelRemarks" name="cancelRemarks" cssClass="selectwk" list="#{'DATA ENTRY MISTAKE':'DATA ENTRY MISTAKE','OTHER':'OTHER'}" />
	</td>	
	</tr>
</table>
</p>
<P align="center">  
	<input type="button" class="buttonadd"
		value="Cancel Revision WorkOrder" id="addButton"
		name="cancelRWO" onclick="cancelRevisionWO()" 
		align="center" />
</P>
</s:if>
