<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>

<tr>
	<td colspan="4">
		<div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">				
					<tr>
						<td class="headingwk">
				  			<div class="arrowiconwk">
								<img src="/egworks/resources/erp2/images/arrow.gif" />
				  			</div>
				  			<div class="headplacer">
								Actual Balance
				  			</div>
						</td>
					</tr>
					<tr>
						<td align="center">
							<br/>
							<table border="0">
								<tr>
									<td>
										<s:if test="%{approvedBudgetFolioDetails.size != 0 || approvedBudgetFolioDetails.size == 0}">
											<s:text name="estimate.budgetfolio.balanceavailable" />:&nbsp;
                        				</s:if>
                        			</td>
                        			<td>
										<div align="left">
                        						<s:if test="%{approvedBudgetFolioDetails.size != 0}">
                        							<s:text name="contractor.format.number" >
                        								<s:param name="value" value="%{latestBalance}"/>
                        							</s:text>
                        						</s:if>
                        						<s:else>
                        							<s:if test="%{approvedBudgetFolioDetails.size == 0}">
                        						    	<s:text name="contractor.format.number" >
                        									<s:param name="value" value="%{totalGrantPerc}"/>
                        								</s:text>
                        							</s:if>
                        						</s:else> 
                        				</div>
									</td>		
                        		</tr>
							</table>
						</td>
					</tr>

					<s:if test="%{approvedBudgetFolioDetails.size == 0}">
						<div>	
							<tr>
								<td align="center"><font color="red">Estimates are not appropriated for selected Budget Head.</font></td>
							</tr>
						</div>
					</s:if>
				</table>
			 <s:if test="%{approvedBudgetFolioDetails.size != 0}">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">					
						<tr>
							<td class="aligncenter">
								<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
								 	<tr>
						   				<td width="3%" class="tablesubheadwk">
											<s:text name="column.title.SLNo" />
						   				</td>
						   				<td width="12%" class="tablesubheadwk">
							  				<s:text name="estimate.budgetfolio.budgetapproriationno" />
										</td>
										<td width="8%" class="tablesubheadwk">
											<s:text name="estimate.budgetfolio.appdate" />
										</td>
										<td width="10%" class="tablesubheadwk">
											<s:text name="estimate.budgetfolio.appType" />
										</td>
										<td width="11%" class="tablesubheadwk">
											<s:text name="estimate.budgetfolio.appropriatedvalue" />
										</td>
										<td width="10%" class="tablesubheadwk">
											<s:text name="estimate.budgetfolio.estimateno" />
										</td>
										<td width="14%" class="tablesubheadwk">
											<s:text name="estimate.budgetfolio.workname" />
										</td>		
										<td width="8%" class="tablesubheadwk">
											<s:text name="estimate.budgetfolio.estimatedate" />
										</td>
										<td width="8%" class="tablesubheadwk">
											<s:text name="estimate.budgetfolio.estimatevalue" />
										</td>
										<td width="8%" class="tablesubheadwk">
											<s:text name="estimate.budgetfolio.cumulativetot" />
										</td>
										<td width="8%" class="tablesubheadwk">
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
													<td width="3%" class="whitebox3wka">
														<s:property value="#row_status.count" />
													</td>
													<td width="12%" class="whitebox3wka">
							  							<s:property value='%{budgetApprNo}' />
													</td>
													<td width="8%" class="whitebox3wka">
														<s:property value='%{appDate}' />
													</td>
													<td width="10%" class="whitebox3wka">
							  							<s:property value='%{appType}' />
													</td>
													<td width="11%" class="whitebox3wka">
														<div align="right">
									  						<s:text name="contractor.format.number" >
                        				 						<s:param name="value" value="%{appropriatedValue}"/> 
                        			  						</s:text>
                        			  					</div>
													</td>
													<td width="10%" class="whitebox3wka">
														<s:property value='%{estimateNo}' />
													</td>
													<td width="14%" class="whitebox3wka1" style="WORD-BREAK:BREAK-ALL">
														<div align="left">
															<s:property value='%{nameOfWork}' />
														</div>
													</td>		
													<td width="8%" class="whitebox3wka">
														<s:property value='%{estimateDate}' />
													</td>
													<td width="8%" class="whitebox3wka">
														<div align="right">
									  						<s:text name="contractor.format.number" >
                        				 						<s:param name="value" value="%{workValue}"/> 
                        			  						</s:text>
                        			  					</div>
													</td>
													<td width="8%" class="whitebox3wka">
														<div align="right">
									  						<s:text name="contractor.format.number" >
                        				 						<s:param name="value" value="%{cumulativeTotal}"/> 
                        			 						 </s:text>
                        			  					</div>
													</td>
													<td width="8%" class="whitebox3wka">
														<div align="right">
									 						 <s:text name="contractor.format.number" >
                        				 						<s:param name="value" value="%{balanceAvailable}"/> 
                        			  						</s:text>
                        			  					</div>
													</td>		
								 				</tr>
											</s:iterator>
											<tr>
												<td width="3%" class="whitebox3wka">
														&nbsp;
												</td>
												<td width="12%" class="whitebox3wka">
							  						&nbsp;
												</td>
												<td width="8%" class="whitebox3wka">
													&nbsp;
												</td>
												<td width="10%" class="whitebox3wka">
													&nbsp;
												</td>		
												<td width="11%" class="whitebox3wka">
													&nbsp;
												</td>
												<td width="10%" class="whitebox3wka">
													&nbsp;
												</td>
												<td width="14%" class="whitebox3wka">
													&nbsp;
												</td>
												<td width="8%" class="whitebox3wka">
													&nbsp;
												</td>
												
												<td width="8%" class="whitebox3wka">
													<s:text name="estimate.budgetfolio.lateststatus" />									 
												</td>
												<td width="8%" class="whitebox3wka">
													<div align="right">
									  					<s:text name="contractor.format.number" >
                        				 					<s:param name="value" value="%{latestCumulative}"/> 
                        			  					</s:text>
                        							</div>
												</td>
													<td width="8%" class="whitebox3wka">
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
						</td>
					</tr>
				</table> 
		</s:if>
		
		</td>
	</tr>
				
