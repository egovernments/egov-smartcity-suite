<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*" %>

<html>
<title><s:text name='page.title.workprogressabstract.bydepartment' /></title>
	<script type="text/javascript">
	
	</script>
	<body>
		<div class="errorstyle" id="workProgressPortal_error"
			style="display: none;"></div>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:form name="workProgressPortalForm" action="workProgressPortal!getWorkProgress.action"
			id="workProgressPortalForm" theme="simple">
			<div class="formmainbox">
				<div class="insidecontent">
					<div id="printContent" class="rbroundbox2">
						<div class="rbtop2">
							<div></div>
						</div>
						<div class="rbcontent2">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td>
									
										&nbsp;
									</td>
								</tr>
								<tr>
									<td>
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td colspan="4" class="headingwk">
													<div class="arrowiconwk">
														<img
															src="${pageContext.request.contextPath}/image/arrow.gif" />
													</div>
													<div class="headplacer">
														<s:text name="page.subheader.search.estimate" />
													</div>
												</td>
											</tr>
											<tr>
												<td class="greyboxwk">
													<s:text name="workprogressabstract.fromdate" />
												</td>
												<td class="greybox2wk">
													<s:date name="estimateFromDate" var="fromDateFormat"
														format="dd/MM/yyyy" />
													<s:textfield name="estimateFromDate" id="estimateFromDate"
														cssClass="selectwk" value="%{fromDateFormat}"
														onfocus="javascript:vDateType='3';"
														onkeyup="DateFormat(this,this.value,event,false,'3')" />
													<a
														href="javascript:show_calendar('forms[0].estimateFromDate',null,null,'DD/MM/YYYY');"
														onmouseover="window.status='Date Picker';return true;"
														onmouseout="window.status='';return true;"> <img
															src="${pageContext.request.contextPath}/image/calendar.png"
															alt="Calendar" width="16" height="16" border="0"
															align="absmiddle" />
													</a>

												</td>
												<td width="17%" class="greyboxwk">
													<s:text name="workprogressabstract.todate" />
												</td>
												<td width="17%" class="greybox2wk">
													<s:date name="estimateToDate" var="toDateFormat"
														format="dd/MM/yyyy" />
													<s:textfield name="estimateToDate" id="estimateToDate"
														value="%{toDateFormat}" cssClass="selectwk"
														onfocus="javascript:vDateType='3';"
														onkeyup="DateFormat(this,this.value,event,false,'3')" />
													<a
														href="javascript:show_calendar('forms[0].estimateToDate',null,null,'DD/MM/YYYY');"
														onmouseover="window.status='Date Picker';return true;"
														onmouseout="window.status='';return true;"> <img
															src="${pageContext.request.contextPath}/image/calendar.png"
															alt="Calendar" width="16" height="16" border="0"
															align="absmiddle" />
													</a>
												</td>
											</tr>
											
											<tr>
												<td colspan="4">
													<div class="buttonholdersearch">
														<s:hidden name="reportType" id="reportType" value="deptwise"/>
													    <s:submit cssClass="buttonadd" value="SEARCH" id="saveButton" name="button" method="getWorkProgress"/> &nbsp;&nbsp;&nbsp;
													    <input type="button" class="buttonfinal" value="CLEAR" id="button" name="clear" onclick="this.form.reset();">&nbsp;&nbsp;&nbsp;
													    <input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button" onclick="window.close();" />
													</div>
												</td>
											</tr>
											
											<tr><td colspan="4"> <logic:notEmpty name="woList">
				  				        <display:table name="woList" uid="currentRowObject"
               							 cellpadding="0" cellspacing="0" export="true" id="reportsmodule"
                						 style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;"
               							 requestURI="">
               							 <display:caption media="pdf"> <center><s:text name='page.title.workprogressabstract.bydepartment' /></center>
               							 </display:caption>
               							   <display:column  headerClass="pagetableth" class="pagetabletd" style="width:10%;text-align:center" title="Department"  property="CONDITION">
                								<s:text name ="CONDITION"/> 
                							</display:column>
                							
                							<display:column  headerClass="pagetableth" class="pagetabletd" title="Admin Sanctioned Estimates"
                    							style="text-align:right;width:10%">
                    						<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">
	                    						<s:if test="%{#attr.reportsmodule.adminSanEstimates !=0 }"> 
														 <s:property  value='%{#attr.reportsmodule.adminSanEstimates}' />
												</s:if>
												<s:else>
													0
												</s:else> 
											</s:if>
											<s:else>
												<s:property  value='%{#attr.reportsmodule.adminSanEstimates}' />
											</s:else>
											</display:column>
											
											<display:column  headerClass="pagetableth" class="pagetabletd" title="Works Package Approved"
                    							style="text-align:right;width:10%" >
                    						<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">
	                    						<s:if test="%{#attr.reportsmodule.approvedWorksPackages !=0 }"> 
														 <s:property  value='%{#attr.reportsmodule.approvedWorksPackages}' />
												</s:if>
												<s:else>
													0
												</s:else>
											</s:if>
											<s:else>
												<s:property  value='%{#attr.reportsmodule.approvedWorksPackages}' />
											</s:else>		 
										</display:column> 
										
										<display:column  headerClass="pagetableth" class="pagetabletd" title="Estimates Covered in WP approved"
                    							style="text-align:right;width:10%" >
                    						<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">
	                    						<s:if test="%{#attr.reportsmodule.approvedWPLinkedEst !=0 }"> 
														 <s:property  value='%{#attr.reportsmodule.approvedWPLinkedEst}' />
												</s:if>
												<s:else>
													0
												</s:else>
											</s:if>
											<s:else>
												<s:property  value='%{#attr.reportsmodule.approvedWPLinkedEst}' />
											</s:else>		 
										</display:column>
										
										<display:column  headerClass="pagetableth" class="pagetabletd" title="Tender Negotiated"
                    							style="text-align:right;width:10%" >
                    						<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">	
	                    						<s:if test="%{#attr.reportsmodule.approvedTenderNegotiations !=0 }"> 
														 <s:property  value='%{#attr.reportsmodule.approvedTenderNegotiations}' />
												</s:if>
												<s:else>
													0
												</s:else>
											</s:if>
											<s:else>
												<s:property  value='%{#attr.reportsmodule.approvedTenderNegotiations}' />
											</s:else>	 
										</display:column>
                							
               							 <display:column  headerClass="pagetableth" class="pagetabletd" title="Work Order Given"
                    							style="text-align:right;width:10%" >
                    						<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">		
	                    						<s:if test="%{#attr.reportsmodule.given !=0 }"> 
														 <s:property  value='%{#attr.reportsmodule.given}' />
												</s:if>
												<s:else>
													0
												</s:else> 
											</s:if>
											<s:else>
												<s:property  value='%{#attr.reportsmodule.given}' />
											</s:else>	
										</display:column>
										
										<display:column  headerClass="pagetableth" class="pagetabletd" title="Estimates covered in WO given"
                    							style="text-align:right;width:10%" >
                    						<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">		
	                    						<s:if test="%{#attr.reportsmodule.workOrderGivenLinkedEst !=0 }"> 
														 <s:property  value='%{#attr.reportsmodule.workOrderGivenLinkedEst}' />
												</s:if>
												<s:else>
													0
												</s:else> 
											</s:if>
											<s:else>
												<s:property  value='%{#attr.reportsmodule.workOrderGivenLinkedEst}' />
											</s:else>	
										</display:column>
                    					
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Inprogress 0-25 %"
                    							style="text-align:right;width:10%" >
                    						<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">		
	                    						<s:if test="%{#attr.reportsmodule.total0to25 !=0 }">  
														 <s:property  value='%{#attr.reportsmodule.total0to25}' />
												</s:if>
												<s:else>
													0
												</s:else>
											</s:if>
											<s:else>
												<s:property  value='%{#attr.reportsmodule.total0to25}' />
											</s:else>	 
										</display:column>
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Inprogress 26-50 %"
                    							style="text-align:right;width:10%" >
                    						<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">			
	                    						<s:if test="%{#attr.reportsmodule.total26to50 !=0 }"> 
														 <s:property  value='%{#attr.reportsmodule.total26to50}' />
												</s:if>
												<s:else>
													0
												</s:else>
											</s:if>
											<s:else>
												<s:property  value='%{#attr.reportsmodule.total26to50}' />
											</s:else>		 
										</display:column>
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Inprogress 51-75 %"
                    							style="text-align:right;width:10%" >
                    						<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">		
	                    						<s:if test="%{#attr.reportsmodule.total51to75 !=0 }"> 
														 <s:property  value='%{#attr.reportsmodule.total51to75}' />
												</s:if>
												<s:else>
													0
												</s:else>
											</s:if> 
											<s:else>
												<s:property  value='%{#attr.reportsmodule.total51to75}' />
											</s:else>		
										</display:column>
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Inprogress 76-99 %"
                    							style="text-align:right;width:10%">
                    						<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">		
	                    						<s:if test="%{#attr.reportsmodule.total76to100 !=0 }"> 
														 <s:property  value='%{#attr.reportsmodule.total76to100}' />
												</s:if>
												<s:else>
													0
												</s:else>
											</s:if> 
											<s:else>
												<s:property  value='%{#attr.reportsmodule.total76to100}' />
											</s:else>	 
										</display:column>
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Work Completed"
                    							style="text-align:right;width:10%" >
                    						<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">		
	                    						<s:if test="%{#attr.reportsmodule.completed !=0 }">
														 <s:property  value='%{#attr.reportsmodule.completed}' /> 
												</s:if>
												<s:else>
													0
												</s:else>
											</s:if> 
											<s:else>
												<s:property  value='%{#attr.reportsmodule.completed}' />
											</s:else>	 
										</display:column>
										<display:column headerClass="pagetableth" class="pagetabletd" title="Balance Nos of works"
                    							style="text-align:right;width:10%">
                    							<s:if test="%{#attr.reportsmodule.CONDITION!='Total'}">
	                    							 <s:if test="%{#attr.reportsmodule.BalanceWorks !=0 }">
															 <s:property  value='%{#attr.reportsmodule.BalanceWorks}' /> 
													</s:if>
													<s:else>
														0
													</s:else>
												</s:if> 
												<s:else>
													<s:property  value='%{#attr.reportsmodule.BalanceWorks}' />
												</s:else>		 
                    					</display:column>
                    							
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Work Order Value"
                    							style="text-align:right;width:10%" property="wovalue" />
                    							
                    					<display:column headerClass="pagetableth" class="pagetabletd" title="Payment Released (Nos, Amount)"
                    							style="text-align:right;width:10%" property="paymentInfo" />
                    							
                    																
										<display:setProperty name="export.csv" value="false" />
						                <display:setProperty name="export.excel" value="true" />
						                <display:setProperty name="export.xml" value="false" />
						                <display:setProperty name="export.pdf" value="true" />
						                <display:setProperty name="export.pdf.filename" value="WorkProgressPortal-ByDepartment.pdf" />
										<display:setProperty name="export.excel.filename" value="WorkProgressPortal-ByDepartment.xls" />
               							</display:table>
				  				        </logic:notEmpty></td></tr>	 
				  				       
				 			<tr><td colspan="4">&nbsp;</td></tr>
										</table>
									</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</div>
												
		</s:form>
	</body>
</html>
