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
												class="pagetabletd" title="TestSheet Number"
												titleKey="tsh.search.testSheetNumber"
												style="width:10%;text-align:left">
                                                <s:property  value='%{#attr.currentRow.testSheetNumber}' />
                                                
                                                <s:hidden name="testSheetHeaderId" id="testSheetHeaderId" value="%{#attr.currentRow.id}" />
                                            </display:column>
                                          
                                            <display:column headerClass="pagetableth"
												class="pagetabletd" title="TestSheet Date"
												titleKey="tsh.search.testSheetDate"
												style="width:6%;text-align:left" >
									  <s:date name="#attr.currentRow.testSheetDate" format="dd/MM/yyyy" />
                                               <s:hidden id="appDate" name="appDate" 	value="%{#attr.currentRow.testSheetDate}" />
                                            </display:column>
                                            
                                             <display:column headerClass="pagetableth"
												class="pagetabletd" title="WorkOrder Number"
												titleKey="tsh.search.workOrderNumber"
												style="width:10%;text-align:left">
                                                <s:property  value='%{#attr.currentRow.workOrder.workOrderNumber}' />
                                          	 </display:column>
                                          
                                            <display:column headerClass="pagetableth"
												class="pagetabletd" title="Contractor"
												titleKey="tsh.search.contractor"
												style="width:15%;text-align:left" property="workOrder.contractor.name" />
												
											 <display:column headerClass="pagetableth"
												class="pagetabletd" title="Total"
												titleKey="tsh.search.testAmount"
												style="width:10%;text-align:right" >
		                                            <s:text name="contractor.format.number" >
													   	 <s:param name="rate" value='%{#attr.currentRow.testSheetCharges}' />
													   </s:text>
		                                           </display:column>
                                              
                                           	 <display:column headerClass="pagetableth"
												class="pagetabletd" title="Status"
												titleKey="tsh.search.status"
												style="width:8%;text-align:left" property="egwStatus.code" />
												
												
												<display:column title='Action' titleKey="tsh.search.action" headerClass="pagetableth" class="pagetabletd" style="width:16%;text-align:center">
													<s:hidden name="testSheetStateId" id="testSheetStateId" value="%{#attr.currentRow.state.id}" />
													<s:select theme="simple" id="searchActions" name="searchActions"
															list="testSheetActions"
															headerValue="%{getText('testSheet.default.select')}" headerKey="-1"
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
                       		