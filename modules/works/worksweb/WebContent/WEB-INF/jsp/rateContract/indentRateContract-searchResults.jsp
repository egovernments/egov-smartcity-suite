<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%> 
<script>
function gotoPage(obj){
	var currRow=getRow(obj);
	var id = getControlInBranch(currRow,'indentId');
    var indentStateId=getControlInBranch(currRow,'indentStateId');	
	var docNumber = getControlInBranch(currRow,'docNumber');
	var showActions = getControlInBranch(currRow,'showActions');
	if(showActions[1]!=null && obj.value==showActions[1].value){	
		window.open("${pageContext.request.contextPath}/rateContract/indentRateContract!edit.action?id="+id.value+
		"&mode=view&sourcepage=search",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(showActions[2]!=null && obj.value==showActions[2].value){	
	
	window.open("${pageContext.request.contextPath}/rateContract/indentRateContract!viewIndentRateContractPDF.action?id="+id.value
		,'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');	
	}
	if(showActions[3]!=null && obj.value==showActions[3].value){
		window.open("${pageContext.request.contextPath}/rateContract/indentRateContract!workflowHistory.action?stateValue="+
		indentStateId.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(showActions[4]!=null && obj.value==showActions[4].value){ 
		viewDocumentManager(docNumber.value);return false;
	}
}
</script>

<div>
     <s:if test="%{searchResult.fullListSize != 0}">
	     <display:table name="searchResult" pagesize="30"
			uid="currentRow" cellpadding="0" cellspacing="0"
			requestURI=""
			style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
			<s:if test="%{source=='tenderFile'}">
				<display:column headerClass="pagetableth" class="pagetabletd"
				title='<input type="checkbox" id="checkedAll" name="checkedAll" onclick="checkAllIndents(this)" />' 
				style="width:3%;text-align:center">
				<s:checkbox id="selectedIndent" name="selectedIndent" />
				</display:column>
			</s:if>                  
	        <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Sl. No."
			   titleKey="column.title.SLNo"
			   style="width:3%;text-align:left" >
			     <s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
			     <s:hidden name="indentIds" id="indentIds" value="%{#attr.currentRow.id}" />
			     <s:hidden name="docNumber" id="docNumber" value="%{#attr.currentRow.documentNumber}" />
			</display:column>
	        
	        <display:column headerClass="pagetableth"
		       class="pagetabletd" title="Indent Rate Contract#"
			   titleKey="indent.search.column.indentRateContractNumber"
			   style="width:10%;text-align:left">
				<a	href="${pageContext.request.contextPath}/rateContract/indentRateContract!edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=view&sourcepage=search">
				<s:hidden name="indentId" id="indentId" value="%{#attr.currentRow.id}" />
				<s:property  value='%{#attr.currentRow.indentNumber}' />
				
				  </a>
            </display:column>
                   
            <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Type"
			   titleKey="indent.search.column.type"
			   style="width:6%;text-align:left" >
			       <s:property  value='%{#attr.currentRow.indentType}' />
		    </display:column>
                     
            <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Date"
			   titleKey="indent.search.column.date"
			   style="width:6%;text-align:left" >
		          <s:date name="#attr.currentRow.indentDate" format="dd/MM/yyyy" />
		    </display:column>
                 
            <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Department"
			   titleKey="indent.search.column.department"
			   style="width:12%;text-align:left" >
				   <s:property value="%{#attr.currentRow.department.deptName}" />
			</display:column>
                
            <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Class of Contractor"
			   titleKey="indent.search.label.contractorClass"
			   style="width:8%;text-align:center" >
			      <s:property value="%{#attr.currentRow.contractorGrade.grade}" />
			</display:column>
                			  
			<display:column headerClass="pagetableth"
			   class="pagetabletd" title="Status"
			   titleKey="indent.search.label.status"
			   style="width:6%;text-align:left" >
					<s:property value="%{#attr.currentRow.egwStatus.code}" />
					<script><s:property value="%{#attr.currentRow.state.id}" /></script>
					<s:hidden name="indentStateId" id="indentStateId" value="%{#attr.currentRow.state.id}" />
			</display:column> 
			
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Actions"
				titleKey="indent.search.actions"
				style="width:6%;text-align:left">
					<s:select theme="simple"
						list="%{#attr.currentRow.indentActions}"
						name="showActions" id="showActions"
						headerValue="%{getText('default.dropdown.select')}"
						headerKey="-1" onchange="gotoPage(this);">
					</s:select>
			</display:column> 
			                                      
	          	                                      
	   </display:table> 

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
	<s:if test="%{searchResult.fullListSize>0 && source=='tenderFile'}">
	<div class="buttonholderwk">
		<input type="button" class="buttonfinal" value="ADD" id="button"
			name="button" onclick="returnBackToParent()" />
		<input type="button" class="buttonfinal" value="CLOSE"
			id="closeButton" name="closeButton" onclick="window.close();" />
	</div>
	</s:if>
 </div>
