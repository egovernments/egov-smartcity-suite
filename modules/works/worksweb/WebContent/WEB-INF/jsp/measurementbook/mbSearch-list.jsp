<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%> 
   <div>
           <s:if test="%{searchResult.fullListSize != 0}">
                  <display:table name="searchResult" pagesize="30"
					 uid="currentRow" cellpadding="0" cellspacing="0"
					 requestURI=""
					 style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
					 					  
					<s:if test="%{sourcepage=='cancelMB'}">
						<display:column headerClass="pagetableth" class="pagetabletd" title="Select" titleKey="column.title.select" style="width:4%;text-align:center">						
						<s:if test="%{#attr.currentRow.mbBills!=null && !#attr.currentRow.mbBills.isEmpty}">
						<s:iterator id="mbBillIterator" value="%{#attr.currentRow.mbBills}" status="mb_status">
						<s:if test="%{egBillregister!=null && egBillregister.billstatus !='CANCELLED'}">
								<s:hidden name="billId" id="billId" value="%{egBillregister.id}" />
					 			<s:hidden name="billNo" id="billNo" value="%{egBillregister.billnumber}" />
					 	</s:if>
					 	</s:iterator>
					 	</s:if> 					 	
					 	<s:else>
						 	<s:hidden name="billId" id="billId" value="" />
						 	<s:hidden name="billNo" id="billNo" value="" />
						 </s:else>
						 <s:if test="%{#attr.currentRow.revisionEstimate!=null && #attr.currentRow.revisionEstimate.egwStatus.code !='CANCELLED'}">
						 		<s:hidden name="revEstNo" id="revEstNo" value="%{#attr.currentRow.revisionEstimate.estimateNumber}" />
						 </s:if>
						 <s:else>
						 	<s:hidden name="revEstNo" id="revEstNo" value="" />
						 </s:else>
							<input name="radio" type="radio" id="radio"
								value="<s:property value='%{#attr.currentRow.id}'/>"
								onClick="setMBId(this);" />
						</display:column>
					</s:if>
                               
                   <display:column headerClass="pagetableth"
					  class="pagetabletd" title="Sl.No"
					  titleKey="column.title.SLNo"
					  style="width:3%;text-align:left" >
						<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
				   </display:column>
                                       
                   <display:column headerClass="pagetableth"
					  class="pagetabletd" title="Work Order Number"
					  titleKey="mb.search.column.wono"
					  style="width:10%;text-align:left">
                        <s:property  value='%{#attr.currentRow.workOrder.workOrderNumber}' />
                        <s:hidden name="mbookId" id="mbookId" value="%{#attr.currentRow.id}" />
				        <s:hidden name="mbookStateId" id="mbookStateId" value="%{#attr.currentRow.state.id}" />
				        <s:hidden name="docNo" id="docNo" value="%{#attr.currentRow.documentNumber}" />
                   </display:column>
                   
                   <display:column headerClass="pagetableth"
					  class="pagetabletd" title="Contractor"
					  titleKey="mb.search.column.contractor"
					  style="width:15%;text-align:left" >
				        <s:property value='%{#attr.currentRow.workOrder.contractor.name}' />
			       </display:column>
                     
                  <display:column headerClass="pagetableth"
					 class="pagetabletd" title="MB Number"
					 titleKey="mb.search.column.refno"
					 style="width:8%;text-align:left" >
                       <s:property  value='%{#attr.currentRow.mbRefNo}' />
					   <s:hidden id="mbNo" name="mbNo" 	value="%{#attr.currentRow.mbRefNo}" /> 
                 </display:column>
                 
                 <display:column headerClass="pagetableth"
					class="pagetabletd" title="Pages"
					titleKey="mb.search.column.pages"
					style="width:6%;text-align:left" >
				      <s:property value="%{#attr.currentRow.fromPageNo}" />
					     <s:if test="%{#attr.currentRow.toPageNo > 0 && #attr.currentRow.toPageNo != ''}">
						     <s:property value='%{-#attr.currentRow.toPageNo}' />
					     </s:if>
					     <s:else>
						     <s:property value='%{-#attr.currentRow.fromPageNo}' />
					     </s:else>
                 </display:column>
                
                <display:column headerClass="pagetableth"
				   class="pagetabletd" title="Date"
				   titleKey="mb.search.column.date"
				   style="width:6%;text-align:left" >
			         <s:date name="#attr.currentRow.mbDate" format="dd/MM/yyyy" />
			  		 <s:hidden id="mbookName" name="mbookName" 	value="%{#attr.currentRow.mbDate}" />
			    </display:column>
                
                <display:column headerClass="pagetableth"
				   class="pagetabletd" title="Owner"
				   titleKey="mb.search.column.owner"
				   style="width:12%;text-align:left" property="owner" />
			  
			    <display:column headerClass="pagetableth"
				   class="pagetabletd" title="Status"
				   titleKey="mb.search.column.status"
				   style="width:10%;text-align:left" >
			          <s:if test="%{#attr.currentRow.currentState.value=='END'}">
					    	<s:property value='%{#attr.currentRow.currentState.previous.value}' />
			          </s:if>
			          <s:else>
				            <s:property value='%{#attr.currentRow.currentState.value}' />
			          </s:else>
			    </display:column>				
		      
		        <display:column headerClass="pagetableth"
				   class="pagetabletd" title="Actions"
				   titleKey="measurementbook.search.actions"
				   style="width:10%;text-align:left">
					 <s:select theme="simple"
						list="%{#attr.currentRow.mbActions}"
						name="showMBActions" id="showMBActions"
						headerValue="%{getText('default.dropdown.select')}"
						headerKey="-1" onchange="gotoPage(this);">
					 </s:select>
			    </display:column>
			    
		  </display:table> 
       </s:if>
       <s:elseif test="%{searchResult.fullListSize == 0}">
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td align="center">
							<font color="red"><s:text name="search.result.no.recorod" /></font>
					</td>
				</tr>
			</table>
        </s:elseif>
  </div>