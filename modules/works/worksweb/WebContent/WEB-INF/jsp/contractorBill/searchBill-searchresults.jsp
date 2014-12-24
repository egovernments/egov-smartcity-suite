<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%> 
<script type="text/javascript">
</script>

<table width="100%" border="0" cellspacing="0" cellpadding="0"> 	
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="9" class="headingwk" align="left">
						<div class="arrowiconwk">
							<img src="${pageContext.request.contextPath}/image/arrow.gif"/>
						</div>
							<div class="headerplacer">
								<s:text name='page.title.search.bill' />
							</div>
					</td>
				</tr>
				</table>
			</td>
		</tr>
</table>	

 <div>

     <s:if test="%{searchResult.fullListSize != 0}">
	     <display:table name="searchResult" pagesize="30"
			uid="currentRow" cellpadding="0" cellspacing="0"
			requestURI=""
			style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
	
	        <s:if test="%{sourcePage=='cancelBill'}">
			<display:column headerClass="pagetableth" class="pagetabletd" style="width:4%;text-align:center">
				<input name="radio" type="radio" id="radio" value="<s:property value='%{#attr.currentRow.id}'/>" onClick="setBillId(this);" />
			</display:column>
			</s:if>     
	                                             
	        <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Sl No"
			   titleKey="column.title.SLNo"
			   style="width:3%;text-align:left" >
			     <s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
			</display:column>
	                                               
	        <display:column headerClass="pagetableth"
			   class="pagetabletd" title="Bill No"
			   titleKey="contractorBill.search.billNo"
			   style="width:10%;text-align:left">
	             <s:property value='%{#attr.currentRow.billnumber}' />
	             <s:hidden name="billId" id="billId" value="%{#attr.currentRow.id}" />
		         <s:hidden name="billStateId" id="billStateId" value="%{#attr.currentRow.state.id}" />
		         <s:hidden name="docNo" id="docNo" value="%{#attr.currentRow.documentNumber}" />
		         <s:hidden name="woId" id="woId" value="%{#attr.currentRow.workOrderId}" />
		         <s:hidden name="billNo" id="billNo" value="%{#attr.currentRow.billnumber}" />
		         <s:hidden name="currentPageNum" id="currentPageNum" value="%{searchResult.pageNumber}" />
		     </display:column>
		     
		     <display:column headerClass="pagetableth"
				class="pagetabletd"  title="Bill Type"
				titleKey="contractorBill.search.billType"
				style="width:10%;text-align:left" >
				  <s:property value='%{#attr.currentRow.billtype}' />
	         </display:column>
	                                            
	         <display:column headerClass="pagetableth"
				class="pagetabletd"  title="Bill Date"
				titleKey="contractorBill.search.billDate"
				style="width:6%;text-align:left"  >
	              <s:date name="#attr.currentRow.billdate" format="dd/MM/yyyy" />
	         </display:column>
	                                               
	         <display:column headerClass="pagetableth"
			    class="pagetabletd"  title="Work Order No"
				titleKey="contractorBill.search.workorderCode"
				style="width:10%;text-align:left" >
				  <s:property value='%{#attr.currentRow.workordernumber}' />		
			 </display:column>
	                                            
	         <display:column headerClass="pagetableth"
				class="pagetabletd"  title="Contractor"
				titleKey="contractorBill.search.contractor"
				style="width:15%;text-align:left" >
				  <s:property value='%{#attr.currentRow.egBillregistermis.payto}' />
	         </display:column>
		
		     <display:column headerClass="pagetableth"
				class="pagetabletd"  title="Bill Value"
				titleKey="contractorBill.search.totalbillValue"
				style="width:10%;text-align:right" >
	               <s:text name="contractor.format.number" >
					  <s:param name="rate" value='%{#attr.currentRow.billamount}' />
				   </s:text>
	         </display:column>
	                                              
	         <display:column headerClass="pagetableth"
				class="pagetabletd"  title="Owner"
				titleKey="contractorBill.search.billcreatedBy"
				style="width:15%;text-align:left" >
				  <s:property value='%{#attr.currentRow.owner}' />
			 </display:column>
	                                              
	         <display:column headerClass="pagetableth"
				class="pagetabletd"  title="Status"
				titleKey="contractorBill.status"
				style="width:10%;text-align:left" >				  
		           <s:property value='%{#attr.currentRow.billstatus}' />
			 </display:column>
	                                              
	         <display:column headerClass="pagetableth"
				class="pagetabletd"  title="Actions"
				titleKey="contractorBill.search.actions"
				style="width:10%;text-align:left">
				  <s:select theme="simple"
					 list="%{#attr.currentRow.billActions}"
										name="showBillActions" id="showBillActions"
										headerValue="%{getText('default.dropdown.select')}"
					 headerKey="-1" onchange="gotoPage(this);">
				  </s:select>
				  <s:hidden name="id" value="%{#attr.currentRow.egBillregister.id}"/>
			 </display:column>
		
	     </display:table> 
</s:if>
	   <s:elseif test="%{searchResult.fullListSize == 0}">
	<div >	
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
