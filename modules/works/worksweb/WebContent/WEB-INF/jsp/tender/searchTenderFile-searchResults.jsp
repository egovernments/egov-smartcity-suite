<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%> 
<script type="text/javascript">

function setworkorderId(obj){
	var currRow=getRow(obj);
	dom.get("tenderRespId").value=getControlInBranch(currRow,'tenderResponseId').value;
}

function gotoWorkorder(){
	var id = document.searchTenderFileForm.tenderRespId.value;
	if(id!='' ){
		window.open('${pageContext.request.contextPath}/workorder/workOrder!newform.action?tenderRespId='+id,'_self');
	}
	else{
		dom.get("searchTenderFile_error").style.display=''; 
		document.getElementById("searchTenderFile_error").innerHTML='<s:text name="tenderResponse.not.selected" />';  
		return false;
	  }
	  dom.get("searchTenderFile_error").style.display='none';
	  document.getElementById("searchTenderFile_error").innerHTML='';
}


var RADIOFLAG;
var CONTRACTORCODE;
var PERCENTAGE;
var NOTICENUMBER;


/*function setBidResponseId(obj)
{
	var currRow = getRow(obj);
	RADIOFLAG = obj;
	CONTRACTORCODE = getControlInBranch(currRow,'contractorCode').value;
	PERCENTAGE     = getControlInBranch(currRow,'percentage').value;
	NOTICENUMBER   = dom.get("noticeNumber").value;
	
	makeJSONCall(
			["responseFlag"],
			'${pageContext.request.contextPath}/tender/tenderResponse!validateReposeForContractor.action',
			{
				noticeNumber   : NOTICENUMBER,
				contractorCode : CONTRACTORCODE
			},
			successResponseHandler, failureResponseHandler);
}


successResponseHandler = function(req, res) {
	var resultdatas  = res.results;
	var responseflag = resultdatas[0].responseFlag;
	dom.get("searchTenderFile_error").style.display = 'none';
	if(responseflag == "true")
	{
		  dom.get("searchTenderFile_error").style.display = ''; 
		  document.getElementById("searchTenderFile_error").innerHTML= 'Bid response is already exists for the selected contractor';  
		  RADIOFLAG.checked = false;
	}
}

failureResponseHandler = function() {
	alert("Unable to validate contractor for bid");
	return false;
}

*/

/*function gotoWorkorderAndBidResponse()
{
	var url = "${pageContext.request.contextPath}/tender/tenderResponse!create.action";
	var submitForm = document.createElement("FORM");
	document.body.appendChild(submitForm);
	submitForm.method = "POST";
	submitForm.action = url;
	submitForm.appendChild(createHiddenElement("noticeNumber",NOTICENUMBER));
	submitForm.appendChild(createHiddenElement("contractorCode",CONTRACTORCODE));
	submitForm.appendChild(createHiddenElement("percentage",PERCENTAGE));
	submitForm.submit();
	return true;
}


function createHiddenElement(name,value)
{
	 var el  = document.createElement("input");
	 el.type = "hidden";
	 el.name = name;
	 el.value = value;
	 return el; 
}
*/

</script>
 
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 	
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td><s:if test="%{searchResult != null}">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="9" class="headingwk" align="left">
						<div class="arrowiconwk">
							<img src="${pageContext.request.contextPath}/image/arrow.gif"/>
						</div>
							<div class="headerplacer">
								<s:text name='page.title.search.TenderResponse' />
							</div>
					</td>
				</tr>
				</table></s:if>
			</td>
		</tr>
</table>	

 <div>
<s:hidden id="tenderRespId" name="tenderRespId" />
								<s:hidden id="tenderFileId" name="tenderFileId" />
     <s:if test="%{searchResult.fullListSize != 0}">
	     <display:table name="searchResult" pagesize="20"
			uid="currentRow" cellpadding="0" cellspacing="0"
			requestURI=""
			style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
	                      <display:column headerClass="pagetableth"
																	class="pagetabletd" title="Select" titleKey="column.title.select" style="width:3%;text-align:left" >
																	<input name="radio" type="radio" id="radio"
																		value="<s:property value='%{#attr.currentRow.id}'/>"
																		onClick="setworkorderId(this);" />
															</display:column>                            
	        <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Sl No"
			   titleKey="column.title.SLNo"
			   style="width:3%;text-align:left" >
			     <s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
			</display:column>
	                                               
	        <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Bid Response No"
			   titleKey="tenderfile.search.responseNo"
			   style="width:10%;text-align:left">
	             <s:property value='%{#attr.currentRow.number}' />
	              <s:hidden name="tenderResponseId"
																	id="tenderResponseId" value="%{#attr.currentRow.id}" />
		     </display:column>
		     
		     <display:column headerClass="pagetableth"
				class="pagetabletd"  title="Contractor/Supplier"
				titleKey="tenderfile.search.contractor"
				style="width:10%;text-align:left" >
				  <s:property value='%{#attr.currentRow.bidder.name}' />
	         </display:column>
	               
	          <display:column headerClass="pagetableth"
				class="pagetabletd"  title="Total"
				titleKey="tenderfile.search.bidvalue"
				style="width:10%;text-align:left" >
				  <s:property value='%{#attr.currentRow.bidValue}' />
	         </display:column>   
	       
	     </display:table> 
	     
	     <input type="button" class="buttonadd"
										value="Create Work Order " id="addButton"
										name="createWorkOrderButton" onclick="gotoWorkorder()"
										align="center" />
	   </s:if> 
	   
	  <!--  <s:elseif test="%{pagedResults.list.size != 0}">
	     
	     <display:table name="pagedResults" pagesize="20" uid="currentRow" cellpadding="0" cellspacing="0"
			requestURI="" style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
		    
		    <display:column headerClass="pagetableth" class="pagetabletd" title="Select" titleKey="column.title.select" 
		    		style="width:3%;text-align:left" >
		    		
					<input name="radio" type="radio" id="radio" value="<s:property value='%{#attr.currentRow.id}'/>"
					        onClick="setBidResponseId(this);" />
			</display:column>
			                            
	        <display:column headerClass="pagetableth" class="pagetabletd" title="Sl No" titleKey="column.title.SLNo" 
	        		style="width:3%;text-align:left" >
	        		
			     <s:property value="%{#attr.currentRow_rowNum}"/>
			     
			</display:column>
	                                               
	        <display:column headerClass="pagetableth" class="pagetabletd"  title="Contractor Code" 
	        				titleKey="tenderfile.search.contractor"
	        				style="width:3%;text-align:left" >
					
				  	<s:property value="%{#attr.currentRow.contractor.code}"/>
				  	<s:hidden name="contractorCode" id="contractorCode" value="%{#attr.currentRow.contractor.code}"/>
				  	
	       </display:column>
	       
	       <display:column headerClass="pagetableth" class="pagetabletd"  title="Contractor Name" 
	        				titleKey="tenderfile.search.contractor"
	        				style="width:3%;text-align:left" >
					
				  	<s:property value="%{#attr.currentRow.contractor.name}"/>
				  	
	       </display:column>
	       
	       <display:column headerClass="pagetableth" class="pagetabletd"  title="Percentage" 
	        				titleKey="tenderfile.search.contractor"
	        				style="width:3%;text-align:left" >
					
				  	<s:property value="%{#attr.currentRow.percentage}"/>
				  	<s:hidden name="percentage" id="percentage" value="%{#attr.currentRow.percentage}"/>
				  	
	       </display:column>
		
		</display:table>
		<br>
		
		<div align="center">
			<input type="button" class="buttonadd" value="Create bid response and Work Order " id="addButton"
			   			name="createWorkOrderButton" onclick="gotoWorkorderAndBidResponse();" />
	    
	   		<input type="button" class="buttonadd" value="Close" id="clsButton" onclick="window.close();" />
	   </div>
	   
	   </s:elseif>
	   -->
	   
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
