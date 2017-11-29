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

<%-- <%@ taglib uri="/egov-authz.tld" prefix="egov-authz" %> --%>
<script type="text/javascript">
function setTemplateCode(obj){
document.getElementById('selectedCode').value=obj.value;
}

function setEstimateTemplateId(id){
	document.getElementById('estimateTemplateId').value=id;	
}
function selectTemplate(){
var code=document.getElementById('selectedCode').value;
var estimateTemplateId =document.getElementById('estimateTemplateId').value;

if(code==""){
 	dom.get("estimateTemplate_error").style.display='block';
	document.getElementById("estimateTemplate_error").innerHTML='Please select at least one template';
	return false;
 }
 window.opener.resetTemplate(code,document.getElementById('workType').value,document.getElementById('subType').value);
 window.close();
}
</script>

<div>
     <s:if test="%{searchResult.fullListSize != 0}">
     <s:hidden name="selectedCode" id="selectedCode" />
     <s:hidden name="estimateTemplateId" id="estimateTemplateId" />
     
	     <display:table name="searchResult" pagesize="30"
			uid="currentRow" cellpadding="0" cellspacing="0"
			requestURI="" class="table table-hover">
	                           
	        <s:if test="%{sourcePage.equals('searchForEstimate')}">
	     		<display:column headerClass="pagetableth" class="pagetabletd" title="Select" style="width:2%;" titleKey="column.title.select">
						<input name="radio" type="radio" id="radio" value="<s:property value='%{#attr.currentRow.code}'/>" onClick="setTemplateCode(this);setEstimateTemplateId(<s:property  value="%{#attr.currentRow.id}" />);" />
				</display:column>
			</s:if>       
			<display:column headerClass="pagetableth"
			   class="pagetabletd" title="Sl No"
			   titleKey="column.title.SLNo"
			   style="width:3%;text-align:left" >
			     <s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
			</display:column>
	        
	        <display:column headerClass="pagetableth"
		       class="pagetabletd" title="Template Code"
			   titleKey="mb.search.column.wono"
			   style="width:8%;text-align:left">
                  <egov-authz:authorize actionName="viewEstimateTemplate">
                  <a href="${pageContext.request.contextPath}/estimate/estimateTemplate-edit.action?mode=view&id=<s:property value='%{#attr.currentRow.id}'/>">
				  </egov-authz:authorize>	 
					 <s:property  value='%{#attr.currentRow.code}' />
				  <egov-authz:authorize actionName="viewEstimateTemplate">
				  </a>
				  </egov-authz:authorize>
            </display:column>
                   
            <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Template Description"
			   titleKey="mb.search.column.contractor"
			   style="width:15%;text-align:left" >
			       <s:property  value='%{#attr.currentRow.description}' />
		    </display:column>
                     
            <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Template Name"
			   titleKey="mb.search.column.refno"
			   style="width:8%;text-align:left" >
                   <s:property  value='%{#attr.currentRow.name}' />
		    </display:column>
                 
            <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Work Type"
			   titleKey="mb.search.column.pages"
			   style="width:10%;text-align:left" >
				   <s:property value="%{#attr.currentRow.workType.description}" />
			</display:column>
                
            <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Work SubType"
			   titleKey="mb.search.column.date"
			   style="width:10%;text-align:left" >
			      <s:property value="%{#attr.currentRow.subType.description}" />
			</display:column>
                			  
			<display:column headerClass="pagetableth"
			   class="pagetabletd" title="Status"
			   titleKey="mb.search.column.status"
			   style="width:6%;text-align:left" >
			      <s:if test="%{#attr.currentRow.status == 0}">
					 <s:property value="%{'INACTIVE'}" />
				  </s:if>
				  <s:else>
					  <s:property value="%{'ACTIVE'}" />
				  </s:else>
			</display:column>                                       
	          	                                      
	   </display:table> 
	    <s:if test="%{sourcePage.equals('searchForEstimate')}">
	    	<input type="button" class="buttonadd"
										value="Select Template" id="addButton"
										name="selectTemplateButton" onclick="selectTemplate()"
										align="center" />
	     </s:if>
	 </s:if> 
	 <s:elseif test="%{searchResult.fullListSize == 0}">
	        <br/>
			<div class="col-md-6 col-md-offset-3 text-center report-table-container">
			   <div class="alert alert-warning no-margin"><s:text name="search.result.no.estimate.template" /></div>
			</div>
	</s:elseif>   
 </div>
