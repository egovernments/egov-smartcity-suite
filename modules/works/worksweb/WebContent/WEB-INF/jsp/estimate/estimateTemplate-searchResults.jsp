<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%> 
<%@ taglib uri="/WEB-INF/egov-authz.tld" prefix="egov-authz" %>
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
     <s:hidden name="selectedCode" id="selectedCode" />
	     <display:table name="searchResult" pagesize="30"
			uid="currentRow" cellpadding="0" cellspacing="0"
			requestURI=""
			style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
	                           
	        <s:if test="%{sourcePage.equals('searchForEstimate')}">
				<display:column headerClass="pagetableth" class="pagetabletd" title="Select" style="width:2%;" titleKey="column.title.select">
						<input name="radio" type="radio" id="radio" value="<s:property value='%{#attr.currentRow.code}'/>" onClick="setTemplateCode(this);" />
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
                  <a href="${pageContext.request.contextPath}/estimate/estimateTemplate!edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=view">
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
