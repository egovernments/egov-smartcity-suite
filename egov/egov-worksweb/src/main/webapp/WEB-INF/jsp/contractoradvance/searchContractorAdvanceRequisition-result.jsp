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
							<s:text name='search.result' />
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
     <s:if test="%{searchResult.fullListSize != 0 && !hasErrors()}">
     	<s:text id="slNo" name="%{getText('label.slno')}"></s:text>		
		<s:text id="arfnumber" name="%{getText('advancerequisition.search.arfnumber')}"></s:text>
		<s:text id="arfdate" name="%{getText('advancerequisition.search.arfdate')}"></s:text>
		<s:text id="estimatenumber" name="%{getText('contractoradvance.estimatenumber')}"></s:text>	
		<s:text id="workordernumber" name="%{getText('contractoradvance.workordernumber')}"></s:text>
		<s:text id="contractorcodeandname" name="%{getText('contractoradvance.contractorcode_name')}"></s:text>
		<s:text id="drawingofficer" name="%{getText('advancerequisition.search.drawingofficer')}"></s:text>
		<s:text id="status" name="%{getText('advancerequisition.search.arfstatus')}"></s:text>
		<s:text id="owner" name="%{getText('advancerequisition.search.owner')}"></s:text>
		<s:text id="advanceamount" name="%{getText('advancerequisition.search.advanceamount')}"></s:text>
		<s:text id="actions" name="%{getText('advancerequisition.search.actions')}"></s:text>
		
 	     <display:table name="searchResult" pagesize="30" uid="currentRow" cellpadding="0" cellspacing="0" requestURI=""
			style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
	        <display:column headerClass="pagetableth"  class="pagetabletd" title="${slNo}" style="width:5%;text-align:left" >
			     <s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="${arfnumber}" style="width:20%;text-align:left">
				<s:property value="#attr.currentRow.advanceRequisitionNumber" /> 
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="${arfdate}" style="width:8%;text-align:left">
				<s:date name="#attr.currentRow.advanceRequisitionDate" format="dd/MM/yyyy" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="${estimatenumber}" style="width:10%;text-align:left">
				<a href="#" onclick="viewEstimate('<s:property value='%{#attr.currentRow.workOrderEstimate.estimate.id}'/>')"> 
					<s:property value="#attr.currentRow.workOrderEstimate.estimate.estimateNumber" /> 
				</a>
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="${workordernumber}" style="width:15%;text-align:left">
				<a href="#" onclick="viewWorkOrder('<s:property value='%{#attr.currentRow.workOrderEstimate.workOrder.id}'/>')"> 
					<s:property value="#attr.currentRow.workOrderEstimate.workOrder.workOrderNumber" /> 
				</a>
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="${contractorcodeandname}" style="width:20%;text-align:left">
				<s:property value="#attr.currentRow.workOrderEstimate.workOrder.contractor.code" /> / <s:property value="#attr.currentRow.workOrderEstimate.workOrder.contractor.name" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="${drawingofficer}" style="width:15%;text-align:left">
				<s:property value="#attr.currentRow.drawingOfficer.code" /> - <s:property value="#attr.currentRow.drawingOfficer.name" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="${status}" style="width:15%;text-align:left">
				<s:property value="#attr.currentRow.status.description" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="${owner}" style="width:15%;text-align:left">
				<s:property value="#attr.currentRow.ownerName" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd" title="${advanceamount}" style="width:15%;text-align:right" >
				<s:text name="contractor.format.number" >
					<s:param name="advanceRequisitionAmount" value='%{#attr.currentRow.advanceRequisitionAmount}' />
				</s:text>
			</display:column>
			<display:column title="${actions}" headerClass="pagetableth" class="pagetabletd" style="width:10%;text-align:center">
				<s:hidden name="arfId" id="arfId" value="%{#attr.currentRow.id}" />
				<s:hidden name="arfStateId" id="arfStateId" value="%{#attr.currentRow.state.id}" />				
				<s:select theme="simple" id="searchActions" name="searchActions" list="actionsList"
					headerValue="%{getText('list.default.select')}" headerKey="-1"
					onchange="goToPage(this);"></s:select>
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
	function viewEstimate(estimateId){
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!edit.action?id="+estimateId+"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	} 

	function viewWorkOrder(workOrderId){
		window.open("${pageContext.request.contextPath}/workorder/workOrder!edit.action?id="+workOrderId+"&mode=search",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}       
</script>
