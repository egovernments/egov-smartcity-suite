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

<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<script>

function setWOEstimateId(elem){
	dom.get('woEstimateId').value = elem.value;
	checkMilestone(elem);
}
function setWOEstimateIdAndMilestoneId(woEstimateId,milestoneId){
	dom.get('woEstimateId').value = woEstimateId;
	dom.get('milestoneId').value = milestoneId;
}
function checkMilestone(elem){
	var workOrderEstimateId = elem.value;
	

var myMilestoneSuccessHandler = function(req,res) {
                dom.get("searchEstimate_error").style.display='none';
                dom.get("searchEstimate_error").innerHTML='';
                var results=res.results;
				if(results[0].milestoneexistsOrNot=='true'){
					if(results[0].woWorkCommenced=='true')
						dom.get("isWrkOrdrWrkCommenced").value='true';
					else
						dom.get("isWrkOrdrWrkCommenced").value='false';
					dom.get("trackMilestoneButton").style.visibility='visible';
					dom.get("createMilestoneButton").style.visibility='hidden';
				}
				else{
					dom.get("createMilestoneButton").style.visibility='visible'; 
					dom.get("trackMilestoneButton").style.visibility='hidden';
				}
            };
            
var myMilestoneFailureHandler = function() {
	            dom.get("searchEstimate_error").style.display='';
	            document.getElementById("searchEstimate_error").innerHTML='<s:text name="milestone.check.workorder"/>';
	        };
	 <s:if test="%{source=='searchEstimateForMilestone'}">
	 	makeJSONCall(["milestoneexistsOrNot","woWorkCommenced"],'${pageContext.request.contextPath}/milestone/ajaxMilestone!checkMilestone.action',{workOrderEstimateId:workOrderEstimateId},myMilestoneSuccessHandler,myMilestoneFailureHandler) ;
	 </s:if>
	 <s:else>
	 	dom.get("trackMilestoneButton").style.visibility='hidden';
		dom.get("createMilestoneButton").style.visibility='hidden';
		dom.get("viewMilestoneButton").style.visibility='visible';
	 </s:else>
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
     
     <s:text id="workStat" name="%{getText('milestone.work.status')}"></s:text>
     <s:text id="msStatus" name="%{getText('milestone.status')}"></s:text>
     <s:text id="owner" name="%{getText('column.title.Owner')}"></s:text>
    
     
 	     <display:table name="searchResult" pagesize="30"
			uid="currentRow" cellpadding="0" cellspacing="0"
			requestURI=""
			style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">

<s:if test="%{source!='viewMilestone'}">
			<display:column headerClass="pagetableth" class="pagetabletd" title="Select" style="width:2%;" titleKey="column.title.select">
				<input name="radio" type="radio" id="radio"
					value="<s:property value='%{#attr.currentRow.id}'/>"
					onClick="setWOEstimateId(this);" />
				<s:hidden name="woEstimateId" id="woEstimateId" />
			</display:column>
	        <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Sl No"
			   titleKey="column.title.SLNo"
			   style="width:3%;text-align:left" >
			     <s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Estimate Number" style="width:6%;text-align:left" >
				<s:property value="#attr.currentRow.estimate.estimateNumber" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Executing Department" titleKey='estimate.search.executingdept' style="width:8%;text-align:left">
				<s:property value="#attr.currentRow.estimate.executingDepartment.deptName" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Name of the Work" titleKey='estimate.search.name' style="width:20%;text-align:left" >
				<s:property value="#attr.currentRow.estimate.name" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="<a href='javascript:sortBy();'>Status</a>" titleKey='estimate.search.status' style="width:8%;text-align:left">
				<s:property value="#attr.currentRow.estimate.egwStatus.description" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Nature of Work" titleKey='estimate.search.type' style="width:10%;text-align:left"  >
				<s:property value="#attr.currentRow.estimate.natureOfWork.name" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Estimate Date" titleKey='estimate.search.estimateDate' style="width:8%;text-align:center" >
				<s:date name="#attr.currentRow.estimate.estimateDate" format="dd/MM/yyyy" />
			</display:column>	
			<display:column headerClass="pagetableth" class="pagetabletd" title="WO Number" style="width:10%;text-align:left" >
				<s:property value="#attr.currentRow.workOrder.workOrderNumber" />
			</display:column>	
			<display:column headerClass="pagetableth" class="pagetabletd" title="WO Date" titleKey='estimate.search.estimateDate' style="width:8%;text-align:center" >
				<s:date name="#attr.currentRow.workOrder.workOrderDate" format="dd/MM/yyyy" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" 
				title="WO Value(Rs)" titleKey="estimate.search.Wo.value"
				style="width:6%;text-align:right"  >
				<s:text name="contractor.format.number" >
					<s:param name="woAmount" value='%{#attr.currentRow.workOrder.workOrderAmount}' /></s:text>
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Project Code" style="width:10%;text-align:left" >
				<s:property value="#attr.currentRow.estimate.projectCode.code" />
			</display:column>
			
			<s:if test="%{source.equals('searchEstimateForMilestone')}">
				<s:if test="%{#attr.currentRow.milestone.isEmpty()}">
		 				<display:column headerClass="pagetableth" class="pagetabletd" title="${workStat}" style="width:9%;text-align:left" >
							<s:text name='milestone.notCreated.newOrCancelled' />
						</display:column>
						<display:column headerClass="pagetableth" class="pagetabletd" title="${msStatus}" style="width:9%;text-align:left" />
						<display:column headerClass="pagetableth" class="pagetabletd" title="${owner}" style="width:9%;text-align:left" />
		 		</s:if>
	 			<s:else>
		 				<display:column headerClass="pagetableth" class="pagetabletd" title="${workStat}" style="width:9%;text-align:left" >
							<s:if test='%{#attr.currentRow.latestMilestone.egwStatus.code=="CANCELLED"}'>
								<s:text name='milestone.notCreated.newOrCancelled' />
							</s:if>
							<s:else>
								<s:text name='milestone.created.status' />
							</s:else>
						</display:column>
						<display:column headerClass="pagetableth" class="pagetabletd" title="${msStatus}" style="width:9%;text-align:left" >
							<s:property value="#attr.currentRow.latestMilestone.egwStatus.code" />
						</display:column>
						<display:column headerClass="pagetableth" class="pagetabletd" title="${owner}" style="width:9%;text-align:left" >
							<s:property value="#attr.currentRow.latestMilestone.ownerName" />
						</display:column>
		 		</s:else>
			</s:if>

</s:if>

<s:if test="%{source=='viewMilestone'}">
				
			<display:column headerClass="pagetableth" class="pagetabletd" title="Select" style="width:2%;" titleKey="column.title.select">
				<input name="radio" type="radio" id="radio"
					onClick="setWOEstimateIdAndMilestoneId('<s:property value='%{#attr.currentRow[0].id}'/>','<s:property value="%{#attr.currentRow[1].id}" />');" />
				<s:hidden name="woEstimateId" id="woEstimateId" />
				<s:hidden name="milestoneId" id="milestoneId"  /> 	
			</display:column>
	        <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Sl No"
			   titleKey="column.title.SLNo"
			   style="width:3%;text-align:left" >
			     <s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Estimate Number" style="width:6%;text-align:left" >
				<s:property value="#attr.currentRow[0].estimate.estimateNumber" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Executing Department" titleKey='estimate.search.executingdept' style="width:8%;text-align:left">
				<s:property value="#attr.currentRow[0].estimate.executingDepartment.deptName" /> 
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Jurisdiction" titleKey='estimate.search.jurisdiction' style="width:8%;text-align:left">
				<s:property value="#attr.currentRow[0].estimate.ward.name" />
			</display:column>
			
			<display:column headerClass="pagetableth" class="pagetabletd" title="Name of the Work" titleKey='estimate.search.name' style="width:20%;text-align:left" >
				<s:property value="#attr.currentRow[0].estimate.name" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="<a href='javascript:sortBy();'>Status</a>" titleKey='estimate.search.status' style="width:8%;text-align:left">
				<s:property value="#attr.currentRow[0].estimate.egwStatus.description" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Nature of Work" titleKey='estimate.search.type' style="width:10%;text-align:left"  >
				<s:property value="#attr.currentRow[0].estimate.natureOfWork.name" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Estimate Date" titleKey='estimate.search.estimateDate' style="width:8%;text-align:center" >
				<s:date name="#attr.currentRow[0].estimate.estimateDate" format="dd/MM/yyyy" />
			</display:column>	
			<display:column headerClass="pagetableth" class="pagetabletd" title="WO Number" style="width:10%;text-align:left" >
				<s:property value="#attr.currentRow[0].workOrder.workOrderNumber" />
			</display:column>	
			<display:column headerClass="pagetableth" class="pagetabletd" title="WO Date" titleKey='estimate.search.estimateDate' style="width:8%;text-align:center" >
				<s:date name="#attr.currentRow[0].workOrder.workOrderDate" format="dd/MM/yyyy" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" 
				title="WO Value(Rs)" titleKey="estimate.search.Wo.value"
				style="width:8%;text-align:right"  >
				<s:text name="contractor.format.number" >
					<s:param name="woAmount" value='%{#attr.currentRow[0].workOrder.workOrderAmount}' /></s:text>
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Project Code" style="width:10%;text-align:left" >
				<s:property value="#attr.currentRow[0].estimate.projectCode.code" />
			</display:column>
			
			<s:if test="%{#attr.currentRow[2]!=null}">
				<s:if test="%{#attr.currentRow[2].egwStatus.code=='APPROVED' && #attr.currentRow[2].isProjectCompleted==1}">
					<display:column headerClass="pagetableth" class="pagetabletd" title="${workStat}" style="width:9%;text-align:left" >
						<s:text name='project.completed' />
					</display:column>
				</s:if>
				<s:else>
					<display:column headerClass="pagetableth" class="pagetabletd" title="${workStat}" style="width:9%;text-align:left" >
						<s:text name='milestone.tracked' />
					</display:column>
				</s:else>
					<display:column headerClass="pagetableth" class="pagetabletd" title="${msStatus}" style="width:9%;text-align:left" >
						<s:property value="#attr.currentRow[2].egwStatus.code" />
					</display:column>
					<display:column headerClass="pagetableth" class="pagetabletd" title="${owner}" style="width:9%;text-align:left" >
						<s:property value="#attr.currentRow[2].ownerName" />
					</display:column>
				
			</s:if>
			<s:else>
					<display:column headerClass="pagetableth" class="pagetabletd" title="${workStat}" style="width:9%;text-align:left" >
								<s:text name='milestone.created.status' />
					</display:column>
					<display:column headerClass="pagetableth" class="pagetabletd" title="${msStatus}" style="width:9%;text-align:left" >
								<s:property value="#attr.currentRow[1].egwStatus.code" />
					</display:column>
					<display:column headerClass="pagetableth" class="pagetabletd" title="${owner}" style="width:9%;text-align:left" >
					 		<s:property value="#attr.currentRow[1].ownerName" />
					</display:column>
			</s:else>			
			
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
 </div>
