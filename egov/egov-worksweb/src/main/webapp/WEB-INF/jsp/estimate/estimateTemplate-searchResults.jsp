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

<div>
     <s:if test="%{searchResult.fullListSize != 0}">
     <s:hidden name="selectedCode" id="selectedCode" />
     <s:hidden name="estimateTemplateId" id="estimateTemplateId" />
     <s:hidden name="mode" />
	     <display:table name="searchResult" pagesize="30"
			uid="currentRow" cellpadding="0" cellspacing="0"
			requestURI="" class="table table-hover">

			<display:column class="hidden" style="width:2%;cursor:pointer" headerClass="hidden" title="fsd">
						<s:property value="#attr.currentRow.id" />
			</display:column>
			
			<s:if test="%{sourcePage.equals('searchForEstimate')}">
				<display:column headerClass="pagetableth" class="pagetabletd" title="Select" style="width:2%;" titleKey="column.title.select">
					<input name="radio" type="radio" id="radio"	value="<s:property value='%{#attr.currentRow.code}'/>" onClick="setTemplateCode(this);setEstimateTemplateId(<s:property  value="%{#attr.currentRow.id}" />);" />
				</display:column>
			</s:if>

			<display:column headerClass="pagetableth"
			   class="pagetabletd" title="Sl No"
			   titleKey="column.title.SLNo"
			   style="width:3%;cursor:pointer;text-align:left" >
			     <s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
			</display:column>
	        
	        <display:column headerClass="pagetableth"
		       class="pagetabletd" title="Template Code"
			   titleKey="mb.search.column.wono"
			   style="width:8%;text-align:left;cursor:pointer">
				<egov-authz:authorize actionName="viewEstimateTemplate">
						<s:property  value='%{#attr.currentRow.code}' />
				</egov-authz:authorize>
			</display:column>
                   
            <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Template Description"
			   titleKey="mb.search.column.contractor"
			   style="width:15%;text-align:left;cursor:pointer" >
			       <s:property  value='%{#attr.currentRow.description}' />
		    </display:column>
                     
            <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Template Name"
			   titleKey="mb.search.column.refno"
			   style="width:8%;text-align:left;cursor:pointer" >
                   <s:property  value='%{#attr.currentRow.name}' />
		    </display:column>
                 
            <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Work Type"
			   titleKey="mb.search.column.pages"
			   style="width:10%;text-align:left;cursor:pointer" >
				   <s:property value="%{#attr.currentRow.workType.name}" />
			</display:column>
                
            <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Work SubType"
			   titleKey="mb.search.column.date"
			   style="width:10%;text-align:left;cursor:pointer" >
			      <s:property value="%{#attr.currentRow.subType.name}" />
			</display:column>
                			  
			<display:column headerClass="pagetableth"
			   class="pagetabletd" title="Status"
			   titleKey="mb.search.column.status"
			   style="width:6%;text-align:left;cursor:pointer" >
			      <s:if test="%{#attr.currentRow.status == 0}">
					 <s:property value="%{'INACTIVE'}" />
				  </s:if>
				  <s:else>
					  <s:property value="%{'ACTIVE'}" />
				  </s:else>
			</display:column>          
			
			<s:if test="%{!(sourcePage.equals('searchForEstimate') || mode == 'view') }">
			<display:column headerClass="pagetableth"
		       class="pagetabletd" title="Modify"
			   titleKey="column.title.modify"
			   style="width:8%;text-align:left;cursor:pointer">
                  <a href="${pageContext.request.contextPath}/estimate/estimateTemplate-edit.action?mode=edit&id=<s:property value='%{#attr.currentRow.id}'/>">
					 <s:text name="column.title.modify" />
				  </a>
            </display:column>
            </s:if>                         
	          	                       
	          	                                      
	   </display:table> 
	   
	    <s:if test="%{sourcePage.equals('searchForEstimate')}">
	    	<div id="buttons" align="center">
		    	<input type="button" class="btn btn-primary" value="Add Template" id="addButton" name="selectTemplateButton" onclick="selectTemplate()" align="center" />
				<input type="button" name="button2" id="button2" value="Close" class="btn btn-default" onclick="window.close();" />
			</div>
	     </s:if>
	 </s:if> 
	 <s:elseif test="%{searchResult.fullListSize == 0}">
	        <br/>
			<div class="col-md-6 col-md-offset-3 text-center report-table-container">
			   <div class="alert alert-warning no-margin"><s:text name="search.result.no.estimate.template" /></div>
			</div>
	</s:elseif>   
 </div>
<script type="text/javascript">
<s:if test="%{mode == 'view'}"> 
jQuery(document).on("click", ".report-table-container table tbody tr", function(e) {
    window.open("${pageContext.request.contextPath}/estimate/estimateTemplate-view.action?id="+jQuery(this).find('td:eq(0)').text()+"&mode=edit",'popup', 'width=900, height=700, top=300, left=260,scrollbars=yes', '_blank');
});
</s:if>
</script>

<script type="text/javascript"
	src="<cdn:url value='/resources/js/searchestimatetemplate.js?rnd=${app_release_no}'/>"></script>