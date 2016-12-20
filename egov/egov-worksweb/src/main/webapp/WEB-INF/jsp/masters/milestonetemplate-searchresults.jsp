<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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

	        <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Sl No"
			   titleKey="column.title.SLNo"
			   style="width:3%;text-align:left;cursor:pointer" >
			     <s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
			</display:column>
			
			<display:column class="hidden" headerClass="hidden" title="fsd" style="width:13%;text-align:left;cursor:pointer">
				<s:property value="#attr.currentRow.id" />
			</display:column>
	        
	        <display:column headerClass="pagetableth"
		       class="pagetabletd" title="Template Code"
			   titleKey="milestone.template.search.code"
			   style="width:8%;text-align:left;cursor:pointer">
				<s:property  value='%{#attr.currentRow.code}' />
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
			   style="width:15%;text-align:left;cursor:pointer" >
			       <s:property  value='%{#attr.currentRow.description}' />
		    </display:column>
                     
            <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Type of Work"
			   titleKey="milestone.template.search.type"
			   style="width:10%;text-align:left;cursor:pointer" >
				   <s:property value="%{#attr.currentRow.typeOfWork.name}" />
			</display:column>
                
            <display:column headerClass="pagetableth"
			   class="pagetabletd" title="SubType of Work"
			   titleKey="milestone.template.search.subtype"
			   style="width:10%;text-align:left;cursor:pointer" >
			      <s:property value="%{#attr.currentRow.subTypeOfWork.name}" />
			</display:column>
                			  
			<display:column headerClass="pagetableth"
			   class="pagetabletd" title="Status"
			   titleKey="milestone.template.status"
			   style="width:6%;text-align:left;cursor:pointer" >
			      <s:if test="%{#attr.currentRow.status == 0}">
					 <s:property value="%{'INACTIVE'}" />
				  </s:if>
				  <s:else>
					  <s:property value="%{'ACTIVE'}" />
				  </s:else>
			</display:column> 
			
			<s:if test="%{mode != 'view'}">
				<display:column headerClass="pagetableth" class="pagetabletd"
					title="Modify" style="width:13%;text-align:left;cursor:pointer">
					<a href="${pageContext.request.contextPath}/masters/milestoneTemplate-edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=edit">
						<s:text name="column.title.modify" />
					</a>
				</display:column>
			</s:if>                                      
	          	                                      
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
 </div>
<script type="text/javascript">
<s:if test="%{mode == 'view'}"> 
jQuery(document).on("click", ".report-table-container table tbody tr", function(e) {
    window.open("${pageContext.request.contextPath}/masters/milestoneTemplate-edit.action?id="+jQuery(this).find('td:eq(1)').text()+"&mode=view",'popup', 'width=900, height=700, top=300, left=260,scrollbars=yes', '_blank');
});
</s:if> 
</script>