<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%> 
<script>

function setTemplateCode(obj)
{
	document.getElementById('selectedCode').value=obj.value;
}

function selectTemplate()
{
	var rcNumber=document.getElementById('selectedCode').value;
	if(rcNumber=="")
 	{
 		dom.get("rateContractsearcherror").style.display='block';
		document.getElementById("rateContractsearcherror").innerHTML='<s:text name="rateContract.select.template" /><br>';
		return false;
 	}
}

function gotoPage(obj){
	var currRow=getRow(obj);
	var id = getControlInBranch(currRow,'indId');
   	var showActions = getControlInBranch(currRow,'showActions');
   	var rateContractStateId=getControlInBranch(currRow,'rateContractStateId');
   	var docNumber = getControlInBranch(currRow,'docNumber');
	if(showActions[1]!=null && obj.value==showActions[1].value){	
		window.open("${pageContext.request.contextPath}/rateContract/rateContract!edit.action?id="+id.value+
		"&mode=view",'', 'width=800,height=800,resizable=yes,scrollbars=yes,left=250,top=400",status=yes');
	}
	if(showActions[2]!=null && obj.value==showActions[2].value){	
		window.open("${pageContext.request.contextPath}/rateContract/rateContract!viewRateContractPDF.action?id="+id.value);
	}
	if(showActions[3]!=null && obj.value==showActions[3].value){
		window.open("${pageContext.request.contextPath}/rateContract/rateContract!workflowHistory.action?stateValue="+
		rateContractStateId.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
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
					       
	        <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Sl. No."
			   titleKey="column.title.SLNo"
			   style="width:3%;text-align:left" >
			     <s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
			</display:column>
	        
	        <display:column headerClass="pagetableth"
		       class="pagetabletd" title="Rate Contract#"
			   titleKey="rateContract.search.column.rateContractNumber"
			   style="width:10%;text-align:left">
				 <a	href="${pageContext.request.contextPath}/rateContract/rateContract!edit.action?id=<s:property value='%{#attr.currentRow.id}'/>&mode=view&sourcepage=search">
				<s:hidden name="indId" id="indId" value="%{#attr.currentRow.id}" />
				<s:hidden name="rateContractStateId" id="rateContractStateId" value="%{#attr.currentRow.state.id}" />
				<s:hidden name="docNumber" id="docNumber" value="%{#attr.currentRow.documentNumber}" />
				<s:property  value='%{#attr.currentRow.rcNumber}' />
				</a>
            </display:column>
                   
            <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Type"
			   titleKey="indent.search.column.type"
			   style="width:6%;text-align:left" >
			       <s:property  value='%{#attr.currentRow.indent.indentType}' />
		    </display:column>
                     
            <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Date"
			   titleKey="rateContract.search.column.date"
			   style="width:6%;text-align:left" >
		          <s:date name="#attr.currentRow.rcDate" format="dd/MM/yyyy" />
		    </display:column>
                 
            <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Department"
			   titleKey="rateContract.search.column.department"
			   style="width:12%;text-align:left" >
				   <s:property value="%{#attr.currentRow.indent.department.deptName}" />
			</display:column>
                
            <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Class of Contractor"
			   titleKey="rateContract.search.label.contractorClass"
			   style="width:8%;text-align:center" >
			      <s:property value="%{#attr.currentRow.indent.contractorGrade.grade}" />
			</display:column>
  			                
  			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Actions"
				titleKey="rateContract.search.actions"
				style="width:6%;text-align:left">
					<s:select theme="simple"
						list="%{#attr.currentRow.rcActions}"
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
 </div>
