<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%> 

     <s:if test="%{!(sourcepage.equals('searchWOForMBCreation') || sourcepage.equals('searchWOForBillCreation')|| sourcepage.equals('searchWOForReturnSD') || sourcepage.equals('searchWOForRetentionMR'))}">
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
		                                           <s:if test="%{sourcepage=='cancelWO'}">
														<display:column headerClass="pagetableth" class="pagetabletd" title="Select" titleKey="column.title.select" style="width:4%;text-align:center">						
															<input name="radio" type="radio" id="radio"
																value="<s:property value='%{#attr.currentRow.id}'/>"
																onClick="setworkorderId(this);" />
														</display:column>
													</s:if>
	                                           
	                                                 <display:column headerClass="pagetableth"
																class="pagetabletd" title="Sl.No"
																titleKey="column.title.SLNo"
																style="width:2%;text-align:right" >
																<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
													</display:column>
	                                               
	                                                <display:column headerClass="pagetableth"
																class="pagetabletd" title="Workorder Number"
																titleKey="workorder.search.workordernumber"
																style="width:10%;text-align:left">
	                                                   <s:property  value='%{#attr.currentRow.workOrderNumber}' />
	                                                   <s:hidden name="workorderId" id="workorderId" value="%{#attr.currentRow.id}" />
													   <s:hidden name="workOrderStateId" id="workOrderStateId" value="%{#attr.currentRow.state.id}" />
													   <s:hidden name="docNumber" id="docNumber" value="%{#attr.currentRow.documentNumber}" />
													   <s:hidden id="objNo" name="objNo" value="%{#attr.currentRow.workOrderNumber}" />
	                                               </display:column>
	                                             
	                                               <display:column headerClass="pagetableth"
																class="pagetabletd" title="Work Order Date"
																titleKey="workorder.search.workorderdate"
																style="width:6%;text-align:left" >
													  <s:date name="#attr.currentRow.workOrderDate" format="dd/MM/yyyy" />
	                                                  <s:hidden id="appDate" name="appDate" 	value="%{#attr.currentRow.workOrderDate}" />
	                                               </display:column>
	                                            
	                                               <display:column headerClass="pagetableth"
																class="pagetabletd" title="Contractor"
																titleKey="workorder.search.contractor"
																style="width:15%;text-align:left" property="contractor.name" />
	                                                 
	                                               <display:column headerClass="pagetableth"
																class="pagetabletd" title="Owner"
																titleKey="workorder.search.owner"
																style="width:10%;text-align:left" property="owner" />
	                                            
	                                              <display:column headerClass="pagetableth"
																class="pagetabletd" title="Status"
																titleKey="workorder.search.status"
																style="width:8%;text-align:left" property="status" />
											
											      <display:column headerClass="pagetableth"
																class="pagetabletd" title="Total"
																titleKey="workorder.search.workordervalue"
																style="width:10%;text-align:right" >
	                                               <s:text name="contractor.format.number" >
												   	 <s:param name="rate" value='%{#attr.currentRow.workOrderAmount}' />
												   </s:text>
	                                              </display:column>
	                                              <display:column headerClass="pagetableth"
																	class="pagetabletd" title="Actions"
																	titleKey="workorder.search.actions"
																	style="width:10%;text-align:left">
																	
																<s:select theme="simple"
																		list="%{#attr.currentRow.workOrderActions}"
																		name="showActions" id="showActions"
																		headerValue="%{getText('default.dropdown.select')}"
																		headerKey="-1" onchange="gotoPage(this);">
																</s:select>
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

     </s:if>
     <s:else>
                      <div id="header-container">
	                        <table id="table-header" cellpadding="0" cellspacing="0" align="center">
									<tr>
											
												<th width="4%">
													<s:text name='column.title.select' />
												</th>
											
											<th width="2%"><s:text name='column.title.SLNo' /></th>	
											<th width="8%"><s:text name='workorder.search.workordernumber' /></th>
											<th width="8%"><s:text name='workorder.search.workorderdate'/></th>
											<th width="8%"><s:text name='workorder.search.contractor' /></th>
											<th width="8%"><s:text name='workorder.search.owner' /></th>
											<th width="6%"><s:text name='workorder.search.status' /></th>
											<th width="8%"><s:text name='workorder.search.workordervalue' /></th>
											
									</tr>
	                      </table>
                         
                           <table width="100%" border="0" cellpadding="0" cellspacing="0" id="table-body" name="workOrderSearchInnerTable">		
                           
                           <s:if test="%{workOrderList.size != 0}">
                           <s:iterator id="workorderIterator" value="workOrderList" status="row_status">	
												<tr>
												    <s:hidden name="workorderId" id="workorderId" value="%{id}" />
													<s:hidden name="workOrderStateId" id="workOrderStateId" value="%{state.id}" />
													<s:hidden name="docNumber" id="docNumber" value="%{documentNumber}" />
													
														<td width="4%">
															<input name="radio" type="radio" id="radio"
																value="<s:property value='%{id}'/>"
																onClick="setworkorderId(this);" />
														</td>
													
													<td width="2%"><s:property value="#row_status.count" /></td>
													<td width="8%"><s:property value='%{workOrderNumber}' /></td>
													<s:hidden id="objNo" name="objNo" value="%{workOrderNumber}" />
													<td width="8%"><s:date name="workOrderDate" format="dd/MM/yyyy" /></td>
													<s:hidden id="appDate" name="appDate" 	value="%{workOrderDate}" />
													<td width="8%"><s:property value='%{contractor.name}' /></td>
												   	<td width="8%"><s:property value='%{owner}' /></td>
												   	<td width="6%"><s:property value='%{status}' /></td>	 		 
												    <td width="8%"><div align="right"><s:text name="contractor.format.number" >
												   	<s:param name="rate" value='%{workOrderAmount}' /></s:text></div></td>
												   
												</tr>
						  </s:iterator>
							                   
                           </s:if>
                           	<s:elseif test="%{workOrderList.size == 0}">
                               <tr>
								<td colspan="3" align="center">
											<div align="center"><font color="red">No record Found.</font></div>
										</td>
							 </tr>
                           </s:elseif>
                           </table>
                              </div>
    </s:else>
                           		