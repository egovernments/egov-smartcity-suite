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
							<img src="/egworks/resources/erp2/images/arrow.gif" />
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
     <s:if test="%{searchResult.fullListSize != 0 && !hasErrors()}">
 	     <display:table name="searchResult" pagesize="30"
			uid="currentRow" cellpadding="0" cellspacing="0"
			requestURI=""
			style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">

			<display:column headerClass="pagetableth" class="pagetabletd" title="Select" style="width:2%;" titleKey="column.title.select">
				<input name="radio" type="radio" id="radio"
					value="<s:property value='%{#attr.currentRow.id}'/>" />
					
				<s:hidden id="woId" name="woId" value="%{#attr.currentRow.workOrder.id}" />
 				<s:hidden id="estId" name="estId" value="%{#attr.currentRow.estimate.id}" />
 				<s:hidden id="source" name="source" />
					
			</display:column>
		
	        <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Sl No"
			   titleKey="column.title.SLNo"
			   style="width:3%;text-align:left" >
			     <s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Work Order Number" style="width:10%;text-align:left">
				<a href="#" onclick="urlLoad('<s:property value='%{#attr.currentRow.workOrder.id}'/>')">
			<s:property value='%{#attr.currentRow.workOrder.workOrderNumber}' /> </a>
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Estimate Number" style="width:6%;text-align:left" >
				<s:property value="#attr.currentRow.estimate.estimateNumber" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Estimate Value" titleKey="estimate.value" style="width:10%;text-align:right"  >
				<s:text name="contractor.format.number" >
					<s:param name="estimateAmount" value='%{#attr.currentRow.estimate.totalAmount.value}' /></s:text>
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Contractor Name" titleKey="workorder.search.contractor" style="width:15%;text-align:left">
			<s:property value="#attr.currentRow.workOrder.contractor.name" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Executing Department" titleKey='estimate.search.executingdept' style="width:8%;text-align:left">
				<s:property value="#attr.currentRow.estimate.executingDepartment.deptName" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Name of the Work" titleKey='estimate.search.name' style="width:20%;text-align:left" >
				<s:property value="#attr.currentRow.estimate.name" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="Type of Work" titleKey='estimate.search.typeofWork' style="width:10%;text-align:left" property="estimate.parentCategory.description" />
			<display:column headerClass="pagetableth" class="pagetabletd" title="Estimate Date" titleKey='estimate.search.estimateDate' style="width:10%;text-align:center" >
				<s:date name="#attr.currentRow.estimate.estimateDate" format="dd/MM/yyyy" />
			</display:column>	
			
			<display:column headerClass="pagetableth" class="pagetabletd" title="WO Value" titleKey="estimate.search.Wo.value" style="width:10%;text-align:right"  >
				<s:text name="contractor.format.number" >
					<s:param name="woAmount" value='%{#attr.currentRow.workOrder.workOrderAmount}' /></s:text>
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
</div>
<script>
	function urlLoad(id){
		window.open("${pageContext.request.contextPath}/workorder/workOrder!edit.action?id="+id+"&mode=search",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}       
	</script>
