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
												titleKey="column.title.SLNo"
												style="width:2%;text-align:right" >
												<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
												</display:column>
										
											<display:column headerClass="pagetableth"
												class="pagetabletd" title="Work Order Number"
												titleKey="slh.search.workOrderNumber"
												style="width:10%;text-align:left">
                                          	 <a onclick="gotoWorkOrderView(this)" href="#"><s:property value='%{#attr.currentRow.testSheetHeader.workOrder.workOrderNumber}'/></a>	
                                          	 <s:hidden name="workOrderId" id="workOrderId" value="%{#attr.currentRow.testSheetHeader.workOrder.id}" />
                                          	</display:column>	
            	 
                                          	   <display:column headerClass="pagetableth"
												class="pagetabletd" title="Contractor Name"
												titleKey="slh.search.contractor"
												style="width:15%;text-align:left" property="testSheetHeader.workOrder.contractor.name" />
                                            
                                             <display:column headerClass="pagetableth"
												class="pagetabletd" title="Test Sheet Number"
												titleKey="slh.search.testSheetNumber"
												style="width:10%;text-align:left">
                                                <s:property  value='%{#attr.currentRow.testSheetHeader.testSheetNumber}' />
                                            </display:column>
                                          
	                                          <display:column headerClass="pagetableth"
													class="pagetabletd" title="Sample Letter Number"
													titleKey="slh.search.sampleLetterNumber"
													style="width:10%;text-align:left">
	                                                <s:property  value='%{#attr.currentRow.sampleLetterNumber}' />
	                                                
	                                                <s:hidden name="sampleLetterHeaderId" id="sampleLetterHeaderId" value="%{#attr.currentRow.id}" />
	                                           </display:column>
                                          
                                          <display:column headerClass="pagetableth"
												class="pagetabletd" title="Covering Letter Number"
												titleKey="slh.search.coveringLetterNumber"
												style="width:10%;text-align:left">
                                                <s:property  value='%{#attr.currentRow.coveringLetterNumber}' />
                                          	 </display:column>
                                          	 
                                          	 <display:column headerClass="pagetableth"
												class="pagetabletd" title="Status"
												titleKey="slh.search.status"
												style="width:10%;text-align:left">
                                                <s:property  value='%{#attr.currentRow.egwStatus.Code}' />
                                          	 </display:column>
												
											 <display:column headerClass="pagetableth"
												class="pagetabletd" title="Total Charges(Rs)"
												titleKey="slh.search.testCharges"
												style="width:10%;text-align:right" >
		                                            <s:text name="contractor.format.number" >
													   	 <s:param name="rate" value='%{#attr.currentRow.slTotalAmount}' />
													   </s:text>
		                                        </display:column>
												
												<display:column title='Action' titleKey="slh.search.action" headerClass="pagetableth" class="pagetabletd" style="width:10%;text-align:left">
													<s:select theme="simple" id="searchActions" name="searchActions"
															list="%{#attr.currentRow.sampleLetterActions}"
															headerValue="%{getText('sampleLetter.default.select')}" headerKey="-1"
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
                       		