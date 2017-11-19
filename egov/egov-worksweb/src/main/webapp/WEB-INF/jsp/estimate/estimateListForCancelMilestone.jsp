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
							<img src="/egworks/resources/erp2/images/arrow.gif" />
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
			<display:column headerClass="pagetableth" class="pagetabletd" title="Type" titleKey='estimate.search.type' style="width:10%;text-align:left" property='estimate.natureOfWork.name' />
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
