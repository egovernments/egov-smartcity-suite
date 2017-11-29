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
function setTemplateCode(obj){
document.getElementById('selectedCode').value=obj.value;
}

function selectTemplate(){
var code=document.getElementById('selectedCode').value;
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
     	<div class="row report-section">
				<div class="col-md-12 table-header text-left">
				  <s:text name="title.search.result" />
				</div>
				
				<div class="col-md-12 report-table-container">
     	 <s:hidden name="selectedCode" id="selectedCode" />
     	 <s:hidden name="id" id="id" />
     	 
 	     <display:table name="searchResult" pagesize="30"
			uid="currentRow" cellpadding="0" cellspacing="0"
			requestURI=""
			class="table table-hover">
			<s:if test="%{mode == 'edit'}">
			<display:column headerClass="pagetableth" class="pagetabletd" title="Select" style="width:2%;" titleKey="column.title.select">
					<input name="radio" type="radio" id="radio" value="<s:property value='%{#attr.currentRow.id}'/>" onClick="setTemplateCode(this);" />
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
			   titleKey="milestone.template.search.code"
			   style="width:8%;text-align:left">
                  <a href="${pageContext.request.contextPath}/masters/milestoneTemplate-view.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=view&sourcepage=search">
					 <s:property  value='%{#attr.currentRow.code}' />
				  </a>
            </display:column>
                   
            <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Template Name"
			   titleKey="milestone.template.search.name"
			   style="width:8%;text-align:left" >
                   <s:property  value='%{#attr.currentRow.name}' />
 		    </display:column>
                 
            <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Template Description"
			   titleKey="milestone.template.search.description"
			   style="width:15%;text-align:left" >
			       <s:property  value='%{#attr.currentRow.description}' />
		    </display:column>
                     
            <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Type of Work"
			   titleKey="milestone.template.search.type"
			   style="width:10%;text-align:left" >
				   <s:property value="%{#attr.currentRow.typeOfWork.description}" />
			</display:column>
                
            <display:column headerClass="pagetableth"
			   class="pagetabletd" title="SubType of Work"
			   titleKey="milestone.template.search.subtype"
			   style="width:10%;text-align:left" >
			      <s:property value="%{#attr.currentRow.subTypeOfWork.description}" />
			</display:column>
                			  
			<display:column headerClass="pagetableth"
			   class="pagetabletd" title="Status"
			   titleKey="milestone.template.status"
			   style="width:6%;text-align:left" >
			      <s:if test="%{#attr.currentRow.status == 0}">
					 <s:property value="%{'INACTIVE'}" />
				  </s:if>
				  <s:else>
					  <s:property value="%{'ACTIVE'}" />
				  </s:else>
			</display:column>                                       
	          	                                      
	   </display:table>
	   <div class="buttonholderwk">
  			<br/>
	   		<s:if test="%{sourcepage.equals('searchForMilestone')}">
	    		<input type="button" class="btn btn-primary"
					value="Select Template" id="addButton"
					name="selectTemplateButton" onclick="selectTemplate()"
					align="center" />
	     	</s:if>
	   </div> 
	  </div>
	 </div>
	 </s:if> 
	 <s:elseif test="%{searchResult.fullListSize == 0}">
		  <div class="row report-section">
			<div class="col-md-12 table-header text-left">
			  <s:text name="title.search.result" />
			</div>
			
			<div class="col-md-12 text-center report-table-container">
			   <div class="alert alert-warning no-margin"><s:text name="label.no.records.found"/></div>
			</div>
		 </div>
	</s:elseif>   
	<s:if test="%{searchResult.fullListSize != 0 && mode == 'edit'}"> 
	<div align="center">
		<input type="button" name="MODIFY" Class="btn btn-primary" value="Modify" id="MODIFY" onclick="return modifyMilestoneTemplate();" /> 
		<input type="button" name="closeButton" id="closeButton" value="Close" Class="btn btn-default" onclick="window.close();" /> &nbsp;&nbsp;
	</div>
</s:if>
	
 </div>
 