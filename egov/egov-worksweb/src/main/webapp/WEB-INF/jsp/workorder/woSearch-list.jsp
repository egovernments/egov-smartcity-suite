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

<script type="text/javascript">
</script>

	<div id="header-container"> 	                       
		<s:if test="%{sourcepage.equals('searchWOForMBCreation') || sourcepage.equals('searchWOForBillCreation') }">
                       
              <s:if test="%{workOrderList.size != 0}">
                         
                     <display:table name="workOrderList" pagesize="30" uid="currentRowObject" cellpadding="0" cellspacing="0" requestURI="" 
							style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">		
							<s:text id="SlNo" name="%{getText('column.title.SLNo')}"></s:text>
							<s:text id="woNumber" name="%{getText('workorder.search.workordernumber')}"></s:text>
							<s:text id="woDate" name="%{getText('workorder.search.workorderdate')}"></s:text>
							<s:text id="contractorName" name="%{getText('workorder.search.contractor')}"></s:text>
							<s:text id="owner" name="%{getText('workorder.search.owner')}"></s:text>
							<s:text id="woValue" name="%{getText('workorder.search.workordervalue')}"></s:text>
							<s:text id="searchAction" name="%{getText('workorder.search.actions')}"></s:text>
	
	
							<display:column class="hidden" headerClass="hidden"  media="html">
				 					<s:hidden id="workorderId" name="workorderId" value="%{#attr.currentRowObject.id}" />
							</display:column>
							
							<display:column class="hidden" headerClass="hidden"  media="html">
								<s:hidden id="objNo" name="objNo" value="%{#attr.currentRowObject.workOrderNumber}" />
							</display:column>	
		
							<display:column class="hidden" headerClass="hidden"  media="html">
			 					<s:hidden id="workOrderStateId" name="workOrderStateId" value="%{#attr.currentRowObject.state.id}" />
							</display:column>
						
							<display:column class="hidden" headerClass="hidden"  media="html">
			 					<s:hidden id="docNumber" name="docNumber" value="%{#attr.currentRowObject.documentNumber}" />
							</display:column>
		 				
						 <s:if test="%{(sourcepage=='searchWOForMBCreation' || sourcepage=='searchWOForBillCreation' || sourcepage=='cancelWO')}">
							 <display:column  headerClass="pagetableth" class="pagetabletd" title="Select" style="width:3%;text-align:center">
								<input name="radio" type="radio" id="radio"  value="<s:property value='%{#attr.currentRowObject.id}'/>" onClick="setworkorderId(this);" />
								<s:if test="%{#attr.currentRowObject.workOrderEstimates.size()==1 && #attr.currentRowObject.workOrderEstimates[0].estimate.applicationRequest!=null}">
				 						<s:hidden id="dwCategory" name="dwCategory" value="%{#attr.currentRowObject.workOrderEstimates[0].estimate.applicationRequest.depositWorksCategory}" />
				 				 		<s:hidden id="roadCutDate" name="roadCutDate" value="%{#attr.currentRowObject.workOrderEstimates[0].estimate.applicationRequest.workStartDate}" />
				 						
					 			</s:if>
					 			<s:else>
					 						<s:hidden id="dwCategory" name="dwCategory" value="" />
					 						<s:hidden id="roadCutDate" name="roadCutDate" value="" />
					 			</s:else>
					  	   </display:column>
					  	  </s:if>   
							
						<display:column headerClass="pagetableth" class="pagetabletd"   title="${SlNo}" style="width:3%;text-align:center">
						<s:property value="%{#attr.currentRowObject_rowNum + (page-1)*pageSize}"/></display:column>	
		 						 	 
		  			 	<display:column  headerClass="pagetableth" class="pagetabletd"   title="${woNumber}"  style="width:15%text-align:center;">
		  			 	<s:property value="%{#attr.currentRowObject.workOrderNumber}" /> </display:column>
		  			 	
		  			 	
		  			 	
		  			 	<display:column headerClass="pagetableth" class="pagetabletd"    title="${woDate}"  style="width:15%text-align:center;">
		  			 		<s:date name="%{#attr.currentRowObject.workOrderDate}"  format="dd/MM/yyyy"/> 
		  			 		<s:hidden id="appDate" name="appDate" 	value="%{#attr.currentRowObject.state.createdDate}" />
		  			 	</display:column>
		  			 	
		  			 	<display:column headerClass="pagetableth" class="pagetabletd"    title="${contractorName}"  style="width:15%text-align:center;">
		  			 		<s:property value="%{#attr.currentRowObject.contractor.name}" /> 
		  			 	</display:column>
		  			 	
		  			 	<display:column   headerClass="pagetableth" class="pagetabletd" title="Status"  style="width:15%text-align:right;">
		  			 	<s:property value="%{#attr.currentRowObject.status}" /> </display:column>
		  			 	
		  			 	<display:column   headerClass="pagetableth" class="pagetabletd" title="${woValue}"  style="width:15%text-align:right;">
			  			 	<s:text name="contractor.format.number" >
								<s:param name="rate" value="%{#attr.currentRowObject.workOrderAmount}"/>
							</s:text>
		  			    </display:column>
						   
			</display:table>
           
            </s:if>
           	<s:elseif test="%{workOrderList.size == 0}">
                <tr>
					<td colspan="3" align="center">
						<div align="center"><font color="red">No record Found.</font></div>
					</td>
				</tr>
            </s:elseif>
           </s:if>
                       
           <s:elseif test="%{pagedResults.fullListSize != 0}">
           
            <display:table name="pagedResults" pagesize="30" uid="currentRowObject" cellpadding="0" cellspacing="0" requestURI="" 
			style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">		
			<s:text id="SlNo" name="%{getText('column.title.SLNo')}"></s:text>
			<s:text id="woNumber" name="%{getText('workorder.search.workordernumber')}"></s:text>
			<s:text id="woDate" name="%{getText('workorder.search.workorderdate')}"></s:text>
			<s:text id="contractorName" name="%{getText('workorder.search.contractor')}"></s:text>
			<s:text id="owner" name="%{getText('workorder.search.owner')}"></s:text>
			<s:text id="woValue" name="%{getText('workorder.search.workordervalue')}"></s:text>
			<s:text id="searchAction" name="%{getText('workorder.search.actions')}"></s:text>
			
			
				<display:column class="hidden" headerClass="hidden"  media="html">
	 					<s:hidden id="workorderId" name="workorderId" value="%{#attr.currentRowObject.id}" />
				</display:column>
				
				<display:column class="hidden" headerClass="hidden"  media="html">
					<s:hidden id="objNo" name="objNo" value="%{#attr.currentRowObject.workOrderNumber}" />
				</display:column>	
				
					<display:column class="hidden" headerClass="hidden"  media="html">
	 					<s:hidden id="workOrderStateId" name="workOrderStateId" value="%{#attr.currentRowObject.state.id}" />
					</display:column>
				
					<display:column class="hidden" headerClass="hidden"  media="html">
	 					<s:hidden id="docNumber" name="docNumber" value="%{#attr.currentRowObject.documentNumber}" />
					</display:column>
				 
				 <s:if test="%{(sourcepage=='searchWOForMBCreation' || sourcepage=='searchWOForBillCreation' || sourcepage=='cancelWO')}">
					 <display:column  headerClass="pagetableth" class="pagetabletd" title="Select" style="width:3%;text-align:center">
						<input name="radio" type="radio" id="radio"  value="<s:property value='%{#attr.currentRowObject.id}'/>" onClick="setworkorderId(this);" />
			  	   </display:column>
			  	  </s:if>   
					
				<display:column headerClass="pagetableth" class="pagetabletd"   title="${SlNo}" style="width:3%;text-align:center">
				<s:property value="%{#attr.currentRowObject_rowNum + (page-1)*pageSize}"/></display:column>	
 						 	 
  			 	<display:column  headerClass="pagetableth" class="pagetabletd"   title="${woNumber}"  style="width:15%text-align:center;">
  			 	<s:property value="%{#attr.currentRowObject.workOrderNumber}" /> </display:column>
  			 	
  			 	
  			 	
  			 	<display:column headerClass="pagetableth" class="pagetabletd"    title="${woDate}"  style="width:15%text-align:center;">
  			 		<s:date name="%{#attr.currentRowObject.workOrderDate}"  format="dd/MM/yyyy"/> 
  			 		<s:hidden id="appDate" name="appDate" 	value="%{#attr.currentRowObject.state.createdDate}" />
  			 	</display:column>
  			 	
  			 	<display:column headerClass="pagetableth" class="pagetabletd"    title="${contractorName}"  style="width:15%text-align:center;">
  			 		<s:property value="%{#attr.currentRowObject.contractor.name}" /> 
  			 	</display:column>
 				
 				<s:if test="%{status!='APPROVED' && status!='CANCELLED' && status!='Work Order acknowledged' && status!='Site handed over' && status!='Work commenced'}"> 			 	
	  			 	<display:column   headerClass="pagetableth" class="pagetabletd" title="${owner}"  style="width:15%text-align:right;">
	  			 	<s:property value="%{#attr.currentRowObject.owner}" />  </display:column>
  			    </s:if>
  			    
  			 	<display:column   headerClass="pagetableth" class="pagetabletd" title="Status"  style="width:15%text-align:right;">
  			 	<s:property value="%{#attr.currentRowObject.status}" /> </display:column>
  			 	
  			 	<display:column   headerClass="pagetableth" class="pagetabletd" title="${woValue}"  style="width:15%text-align:right;">
	  			 	<s:text name="contractor.format.number" >
						<s:param name="rate" value="%{#attr.currentRowObject.workOrderAmount}"/>
					</s:text>
  			    </display:column>
  			 	
  		  	<s:if test="%{!(sourcepage=='searchWOForMBCreation' || sourcepage=='searchWOForBillCreation' || sourcepage=='cancelWO')}">
			<display:column  headerClass="pagetableth" class="pagetabletd"   title="${searchAction}"  style="width:4%text-align:center;">												
					<s:select theme="simple" list="#attr.currentRowObject.workOrderActions"
							  name="showActions" id="showActions"	headerValue="%{getText('default.dropdown.select')}"
							  headerKey="-1" onchange="gotoPage(this);">
					</s:select>
			 </display:column>
			 </s:if>
  			   
  			</display:table>
  		</s:elseif>
  		<s:else>
            <tr>
				<td colspan="3" align="center">
					<div align="center"><font color="red">No record Found.</font></div>
				</td>
			</tr>
        </s:else>
  			  	
                           
                           		
