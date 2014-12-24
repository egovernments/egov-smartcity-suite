<tr>
	<td colspan="4">
		<div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">				
				<tr>
				<td colspan="7" class="headingwk">
				  <div class="arrowiconwk">
					<img src="${pageContext.request.contextPath}/image/arrow.gif" />
				  </div>
				  <div class="headplacer">
					Actual Balance
				  </div>
				</td>
				</tr>						
				<tr>
					<td class="aligncenter">
						<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
						 <tr>
						   <td width="4%" class="tablesubheadwk">
							<s:text name="column.title.SLNo" />
						   </td>
						   <td width="13%" class="tablesubheadwk">
							  <s:text name="estimate.budgetfolio.budgetapproriationno" />
							</td>
							<td width="13%" class="tablesubheadwk">
								<s:text name="estimate.budgetfolio.estimateno" />
							</td>
							<td width="42%" class="tablesubheadwk">
								<s:text name="estimate.budgetfolio.workname" />
							</td>		
							<td width="7%" class="tablesubheadwk">
								<s:text name="estimate.budgetfolio.estimatedate" />
							</td>
							<td width="7%" class="tablesubheadwk">
								<s:text name="estimate.budgetfolio.estimatevalue" />
							</td>
							<td width="7%" class="tablesubheadwk">
								<s:text name="estimate.budgetfolio.cumulativetot" />
							</td>
							<td width="7%" class="tablesubheadwk">
								<s:text name="estimate.budgetfolio.balanceavailable" />
							</td>
						</tr>													
						</table>
						<% 
							int cnt=0;
						%>
					<s:iterator  id="approvedBudgetFolioDetailsCntIterator" value="approvedBudgetFolioDetails"> 
						<% 
							cnt++;
						%>
					</s:iterator>
								
								<s:if test="%{approvedBudgetFolioDetails.size != 0}">
							<% if(cnt>20){ %>
								<div style=" height:350px" class="scrollerboxaddestimate" >
							<%} else {%> 													
							<div>
							<%} %>	
							<table width="100%" border="0" cellpadding="0" cellspacing="0" id="approvedBudgetFolioDetailsInnerTable">
							<s:iterator id="approvedBudgetFolioDetailsCntIterator" value="approvedBudgetFolioDetails" status="row_status">							
								<tr>
									<td width="4%" class="whitebox3wka">
										<s:property value="#row_status.count" />
									</td>
									<td width="13%" class="whitebox3wka">
							  			<s:property value='%{budgetApprNo}' />
									</td>
									<td width="13%" class="whitebox3wka">
										<s:property value='%{estimateNo}' />
									</td>
									<td width="42%" class="whitebox3wka1" style="WORD-BREAK:BREAK-ALL">
										<div align="left">
											<s:property value='%{nameOfWork}' />
										</div>
									</td>		
									<td width="7%" class="whitebox3wka">
										<s:property value='%{estimateDate}' />
									</td>
									<td width="7%" class="whitebox3wka">
									<div align="right">
									  <s:text name="contractor.format.number" >
                        				 <s:param name="value" value="%{workValue}"/> 
                        			  </s:text>
                        			  </div>
									</td>
									<td width="7%" class="whitebox3wka">
									<div align="right">
									  <s:text name="contractor.format.number" >
                        				 <s:param name="value" value="%{cumulativeTotal}"/> 
                        			  </s:text>
                        			  </div>
									</td>
									<td width="7%" class="whitebox3wka">
									<div align="right">
									  <s:text name="contractor.format.number" >
                        				 <s:param name="value" value="%{balanceAvailable}"/> 
                        			  </s:text>
                        			  </div>
									</td>		
								 </tr>
							</s:iterator>
							<tr>
									<td width="4%" class="whitebox3wka">
										&nbsp;
									</td>
									<td width="13%" class="whitebox3wka">
							  			&nbsp;
									</td>
									<td width="13%" class="whitebox3wka">
										&nbsp;
									</td>
									<td width="42%" class="whitebox3wka">
										&nbsp;
									</td>		
									<td width="7%" class="whitebox3wka">
									&nbsp;
									</td>
									<td width="7%" class="whitebox3wka">
										<s:text name="estimate.budgetfolio.lateststatus" />									 
									</td>
									<td width="7%" class="whitebox3wka">
									<div align="right">
									  <s:text name="contractor.format.number" >
                        				 <s:param name="value" value="%{latestCumulative}"/> 
                        			  </s:text>
                        			</div>
									</td>
									<td width="7%" class="whitebox3wka">
									<div align="right">
									  <s:text name="contractor.format.number" >
                        				 <s:param name="value" value="%{latestBalance}"/> 
                        			  </s:text>
                        			  </div>
									</td>		
								 </tr>
							</table>																			
							</div>											
							</s:if>
							<s:elseif test="%{approvedBudgetFolioDetails.size == 0}">
								<div >	
								  <table width="100%" border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td align="center"><font color="red">No record Found.</font></td>
									</tr>
								  </table>
								</div>
							</s:elseif>
						</td>
						</tr>
						</table>
			</div>
		</td>
	</tr>
				