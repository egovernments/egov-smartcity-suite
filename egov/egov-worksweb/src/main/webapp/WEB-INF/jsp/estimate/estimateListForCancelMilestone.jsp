<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%> 
<script src="<egov:url path='js/works.js'/>"></script>
<script>

function setWOEstimateId(elem){
	dom.get('woEstimateId').value = elem.value;
}

function gotoPage(obj)
{
	var currRow=getRow(obj);
	var woEstimateId = getControlInBranch(currRow,'woEstimateId');
	
	if(dom.get('searchActions')[1]!=null && obj.value==dom.get('searchActions')[1].value)
	{
		window.open("${pageContext.request.contextPath}/milestone/trackMilestone!view.action?woEstimateId="+woEstimateId.value+"&mode=view",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
}

function viewWorkOrder(obj) {
	window.open("${pageContext.request.contextPath}/workorder/workOrder!edit.action?id="+obj+"&mode=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function viewWorkPackage(obj) { 
	window.open("${pageContext.request.contextPath}/tender/worksPackage!edit.action?packageNumber="+obj+"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}
</script>

<div>
<s:hidden name="isWrkOrdrWrkCommenced" id="isWrkOrdrWrkCommenced" />
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
							<s:text name='searh.milestone.estimate.header' />
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
     <s:if test="%{searchResult.fullListSize != 0 && !hasErrors()}">
 	     <display:table name="searchResult" pagesize="30"
			uid="currentRow" cellpadding="0" cellspacing="0"
			requestURI=""
			style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
		<s:if test="%{checkWO==true}">	
			<display:column headerClass="pagetableth" class="pagetabletd" title="<input name='hcheck' type='checkbox' id='hcheck' onClick='toggleSelectAll(this)'/>Select All" style="width:2%;" titleKey="column.title.select">
				<input name="msCheck" type="checkbox" id="msCheck"/>
				<s:hidden name="woEstimateId" id="woEstimateId" value='%{#attr.currentRow.id}'/>
				<s:iterator value='%{#attr.currentRow.milestone}' status="row_status1" >
					<s:if test='%{!trackMilestone.isEmpty()}'> 
						<s:iterator value='%{trackMilestone}' status="row_status" >
							<s:hidden name="isTmsExist" id="isTmsExist" value="true"/>
						</s:iterator> 
					</s:if>
					<s:else>
							<s:hidden name="isTmsExist" id="isTmsExist" value="false"/>
					</s:else>
				</s:iterator>
			</display:column>
		</s:if>
		<s:else>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Select" style="width:2%;" titleKey="column.title.select">
				<input name="msCheck" type="checkbox" id="msCheck"/>
				<s:hidden name="woEstimateId" id="woEstimateId"  value='%{#attr.currentRow.id}'/>
				<s:iterator value='%{#attr.currentRow.milestone}' status="row_status1" >
					<s:if test='%{!trackMilestone.isEmpty()}'> 
						<s:iterator value='%{trackMilestone}' status="row_status" >
							<s:hidden name="isTmsExist" id="isTmsExist" value="true"/>
						</s:iterator> 
					</s:if>
					<s:else>
							<s:hidden name="isTmsExist" id="isTmsExist" value="false"/>
					</s:else>
				</s:iterator>
			</display:column>
		</s:else>
	        <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Sl No"
			   titleKey="column.title.SLNo"
			   style="width:3%;text-align:left" >
			     <s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Estimate Number" style="width:6%;text-align:left" >
				<s:property value="#attr.currentRow.estimate.estimateNumber" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Estimate Value" style="width:5%;text-align:right">
				<s:property value="#attr.currentRow.estimate.totalAmount" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Executing Department" titleKey='estimate.search.executingdept' style="width:8%;text-align:left">
				<s:property value="#attr.currentRow.estimate.executingDepartment.deptName" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Name" titleKey='estimate.search.name' style="width:20%;text-align:left" >
				<s:property value="#attr.currentRow.estimate.name" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Milestone Status" titleKey='estimate.search.status' style="width:5%;text-align:left">
					<s:property value="%{status}"/>
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Type" titleKey='estimate.search.type' style="width:10%;text-align:left" property='estimate.type.name' />
			<display:column headerClass="pagetableth" class="pagetabletd" title="Estimate Date" titleKey='estimate.search.estimateDate' style="width:8%;text-align:center" >
				<s:date name="#attr.currentRow.estimate.estimateDate" format="dd/MM/yyyy" />
			</display:column>	
			<display:column headerClass="pagetableth" class="pagetabletd" title="WO Number" style="width:12%;text-align:left">
				<a href="Javascript:viewWorkOrder('<s:property  value='%{#attr.currentRow.workOrder.id}' />')">
					<s:property value="#attr.currentRow.workOrder.workOrderNumber" />
				</a>
		
				<s:hidden name="workOrderNum" id="workOrderNum" value='%{#attr.currentRow.workOrder.workOrderNumber}'/>
			</display:column> 
			<display:column headerClass="pagetableth" class="pagetabletd" title="WP Number" style="width:10%;text-align:left">
				<a href="Javascript:viewWorkPackage('<s:property  value='%{#attr.currentRow.workOrder.packageNumber}' />')"> 
					<s:property value='%{#attr.currentRow.workOrder.packageNumber}' />
				</a> 
				
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="WO Date" titleKey='estimate.search.estimateDate' style="width:8%;text-align:center" >
				<s:date name="#attr.currentRow.workOrder.workOrderDate" format="dd/MM/yyyy" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="WO Value" titleKey="estimate.search.Wo.value"
				style="width:10%;text-align:right"  >
				<s:text name="contractor.format.number" >
					<s:param name="woAmount" value='%{#attr.currentRow.workOrder.workOrderAmount}' /></s:text>
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Project Code" style="width:10%;text-align:left">
				<s:property value="#attr.currentRow.estimate.projectCode.code" />
				<s:hidden name="projectCodeMS" id="projectCodeMS" value='%{#attr.currentRow.estimate.projectCode.code}'/>
				<s:hidden name="projClosed" id="projClosed" value='%{#attr.currentRow.estimate.projectCode.egwStatus.code}'/>
			</display:column>
			
			<display:column title='Action' titleKey="estimate.search.action" headerClass="pagetableth" class="pagetabletd" style="width:16%;text-align:center">
				<s:hidden name="milestoneId" id="milestoneId" value="%{#attr.currentRow.milestone.id}" />
				<s:select theme="simple" id="searchActions" name="searchActions"
						list="milestoneActions"
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
					 <font color="red"><s:text name="search.result.no.recorod" /></font>
				   </td>
			    </tr>
			</table>
		  </div>
	</s:elseif>   
 </div>
