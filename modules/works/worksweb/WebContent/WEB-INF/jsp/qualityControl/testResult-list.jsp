<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%> 
                              
<div>
	<table width="100%" border="0" cellspacing="0"
		cellpadding="0">
		<tr>
			<td colspan="7" class="headingwk">
				<div class="arrowiconwk">
					<img
						src="${pageContext.request.contextPath}/image/arrow.gif" />
				</div>
				<div class="headplacer">
					<s:text name="title.search.result" />
				</div>
			</td>
		</tr>
	</table>

   <s:if test="%{searchResult.fullListSize != 0}">
       		
       <display:table name="searchResult" pagesize="30"
			uid="currentRow" cellpadding="0" cellspacing="0"
			requestURI=""
			style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
                    <display:column headerClass="pagetableth"
						class="pagetabletd" title="Sl.No"
						style="width:2%;text-align:right" >
						<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
						<s:hidden name="testResultHeaderId" id="testResultHeaderId" value="%{#attr.currentRow.id}" />
						<s:hidden name="stateId" id="stateId" value="%{#attr.currentRow.state.id}" />
					</display:column>
												
					<display:column headerClass="pagetableth"
						class="pagetabletd" title="Job Number / Date"
						style="width:10%;text-align:left">
	                    <s:property value='%{#attr.currentRow.jobHeader.jobNumber}'/> , <s:date name="#attr.currentRow.jobHeader.jobDate" format="dd/MM/yyyy" />
	                </display:column>
										
					<display:column headerClass="pagetableth"
						class="pagetabletd" title="Work Order Number / Date"  
						style="width:10%;text-align:left">
                        <s:property value='%{#attr.currentRow.jobHeader.sampleLetterHeader.testSheetHeader.workOrder.workOrderNumber}'/> , <s:date name="#attr.currentRow.jobHeader.sampleLetterHeader.testSheetHeader.workOrder.workOrderDate" format="dd/MM/yyyy" />
                    </display:column>	
                                                                            
	                <display:column headerClass="pagetableth"
						class="pagetabletd" title="Sample Letter Number / Date"
						style="width:10%;text-align:left">
                        <s:property value='%{#attr.currentRow.jobHeader.sampleLetterHeader.sampleLetterNumber}'/> , <s:date name="#attr.currentRow.jobHeader.sampleLetterHeader.sampleLetterDate" format="dd/MM/yyyy" />
	                </display:column>
                                          
                    <display:column headerClass="pagetableth"
						class="pagetabletd" title="Covering Letter Number"
						style="width:10%;text-align:left">
                        <s:property  value='%{#attr.currentRow.jobHeader.sampleLetterHeader.coveringLetterNumber}' />
                    </display:column>
                                          	 
                    <display:column headerClass="pagetableth"
						class="pagetabletd" title="Test Sheet Number / Date"
						style="width:10%;text-align:left">
                        <s:property  value='%{#attr.currentRow.jobHeader.sampleLetterHeader.testSheetHeader.testSheetNumber}' /> , <s:date name="##attr.currentRow.jobHeader.sampleLetterHeader.testSheetHeader.testSheetDate" format="dd/MM/yyyy" />
                    </display:column>
                                          	 
                    <display:column headerClass="pagetableth"
						class="pagetabletd" title="Status"
						style="width:10%;text-align:left">
                        <s:property  value='%{#attr.currentRow.egwStatus.code}' />
                    </display:column>
                                          	 	 
					<display:column headerClass="pagetableth"
						class="pagetabletd" title="Total Charges(Rs)"
						style="width:10%;text-align:right" >
                        <s:text name="contractor.format.number" >
						   	 <s:param name="rate" value='%{#attr.currentRow.jobHeader.jhTotalAmount}' />
						</s:text>
		             </display:column>
		             
		             <display:column title='Action' titleKey="slh.search.action" headerClass="pagetableth" class="pagetabletd" style="width:10%;text-align:left">
						<s:select theme="simple" id="searchActions" name="searchActions"
							list="%{#attr.currentRow.testResultActions}"
							headerValue="%{getText('test.result.default.select')}" headerKey="-1"
							onchange="gotoPage(this);"></s:select>
					</display:column>
					
			</display:table>
        </s:if> 
        <s:elseif test="%{searchResult.fullListSize == 0}">
			<div>
				<table width="100%" border="0" cellpadding="0"
					cellspacing="0">
					<tr>
						<td align="center">
							<font color="red">No record Found.</font>  
						</td>
					</tr>
				</table>  
			</div>
		</s:elseif>  
</div> 
                       		