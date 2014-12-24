<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%> 

<script type="text/javascript">

function setTemplateCode(obj){
document.getElementById('selectedCode').value=obj.value;
}
function selectTemplate()
{
	var responseNumber=document.getElementById('selectedCode').value;
	var contractorId=document.getElementById('contractorId').value;
	var tenderFileNum = document.getElementById('fileNumber').value;
	var indentId = document.getElementById('indent').value;
	if(responseNumber=="")
 	{
 		dom.get("rateContract_error").style.display='block';
		document.getElementById("rateContract_error").innerHTML='<s:text name="rateContract.select.template" /><br>';
		return false;
 	}
	window.open("${pageContext.request.contextPath}/rateContract/rateContract!newForm.action?responseNumber="+responseNumber+"&contractorId="+contractorId+"&tenderFileNum="+tenderFileNum+"&indentId="+indentId,'',"width=800,height=800,resizable=yes,scrollbars=yes,left=250,top=400");
	window.close();
}
</script>
<div>											
     <s:if test="%{responseList.size > 0}">
     <s:hidden name="selectedCode" id="selectedCode" />
	     <display:table name="responseList" pagesize="30"
			uid="currentRow" cellpadding="0" cellspacing="0"
			requestURI=""
			style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
	
				<display:column headerClass="pagetableth" class="pagetabletd" title="Select" style="width:2%;" titleKey="column.title.select">
						<input name="radio" type="radio" id="radio" value="<s:property value='%{#attr.currentRow.number}'/>" onClick="setTemplateCode(this);" />
				</display:column>
				
				<display:column headerClass="pagetableth"
			   		class="pagetabletd" title="Sl. No."
			   		titleKey="column.title.SLNo"
			   		style="width:3%;text-align:left" >
			     <s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
				</display:column>
	        
			<display:column headerClass="pagetableth"
		       class="pagetabletd" title="BID Response#"
			   titleKey="rateContract.search.bidder.responseNumber"
			   style="width:10%;text-align:left">
			   
				<s:property  value="%{#attr.currentRow.number}" />
			</display:column>
			
			<display:column headerClass="pagetableth"
			   class="pagetabletd" title="Contractor"
			   titleKey="rateContract.search.column.contractor"
			   style="width:6%;text-align:left" >
			       <s:property  value='%{#attr.currentRow.bidder.name}' />
			       <s:hidden name="contractorId" id="contractorId" value="%{#attr.currentRow.bidder.id}" />
		    </display:column>
		    
		    <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Type"
			   titleKey="rateContract.search.column.type"
			   style="width:6%;text-align:left" >
			       <s:property  value='%{#attr.currentRow.tenderUnit.tenderNotice.tenderFileType.description}' />
		    </display:column>
		    
		    </display:table> 
		   <br/>
	    	<center><input type="button" class="buttonadd"
										value="<s:text name="rateContract.create.title" />" id="addButton"
										name="selectTemplateButton" onclick="selectTemplate()"
										align="center" /></center>
	 </s:if> 
	 <s:elseif test="%{responseList.size == 0}" >
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