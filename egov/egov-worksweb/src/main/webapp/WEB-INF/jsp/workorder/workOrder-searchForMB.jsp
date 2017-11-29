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

<%@ include file="/includes/taglibs.jsp" %>

<html>
	<head>
		<title><s:text name='workOrder.search.title' /></title>
	</head> 
		<div class="errorstyle" id="form_error" style="display: none;">
			<span id="date_error" style="display: none;"><s:text name="workorder.search.createDate.invalid" /></span><br>
			<span id="mandatory_error" style="display: none;"><s:text name="workorder.serach.mandatory.error" /></span>
			<span id="mandatory_length_error" style="display: none;"><s:text name="workorder.serach.mandatory.length.error" /></span>
		    <span id='errorcreateDate' style="display:none;color:red;font-weight:bold">&nbsp;</span>
		</div>
	<body>
		<s:form name="searchWorkOrderForm" id="searchWorkOrderForm" action="searchWorkOrderAction" theme="simple" onsubmit="validateAndSubmit();">
			<div class="formmainbox">
				<div class="insidecontent">
					<div class="rbroundbox2">
						<div class="rbtop2">
							<div></div>
						</div>
						<div class="rbcontent2">
						
						<!--  div class="datewk">
								<div class="estimateno">
									Estimate No: &lt;Not Assigned&gt;
								</div>
								<span class="bold">Today</span> 12/12/2009
							</div>
							
						-->
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td></td>
								</tr>
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
														<img src="/egworks/resources/erp2/images/arrow.gif" />
													</div>
													<div class="headplacer">
														<s:text name='workOrder.search.label' />
													</div>
												</td>
											</tr>
											<tr>
												<td width="19%" class="greyboxwk">
													<s:text name='workOrder.search.label.contractor' />:
												</td>
												<td width="30%" class="greybox2wk">
													<s:select id="contractorId" name="contractorId" cssClass="selectwk"
														list="%{dropdownData.contractorList}" listKey="id" listValue="name" 
														headerKey="-1" headerValue="--- Select ---" />
												</td>
												<td width="14%" class="greyboxwk">
													<s:text name='workOrder.search.label.createDate' />:
												</td>
												<td width="37%" class="greybox2wk">
													<s:date name="createDate" var="createDate" format="dd/MM/yyyy"/>
										 			<s:textfield name="createDate"  
													        id="createDate" cssClass="selectboldwk" onfocus="javascript:vDateType='3';"
												         	onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="checkDate(this)"/>
										         	<a href="javascript:show_calendar('forms[0].createDate',null,null,'DD/MM/YYYY');" 
												         	onmouseover="window.status='Date Picker';return true;" 
											          		onmouseout="window.status='';return true;">
										          			<img src="/egworks/resources/erp2/images/calendar.png" 
													          alt="Calendar" width="16" height="16" border="0" align="absmiddle" /></a>
												</td>
											</tr>
											<tr>
												<td class="whiteboxwk">
													<s:text name='workOrder.search.label.tenderNo' />:
												</td>
												<td class="whitebox2wk">
													<s:select id="tenderNo" name="tenderNo" cssClass="selectwk"
														list="%{dropdownData.tenderHeaderList}" listKey="tenderNo" listValue="tenderNo" 
														headerKey="" headerValue="--- Select ---" />
												</td>
												<td class="whiteboxwk">
													<s:text name='workOrder.search.label.workOrderNo' />:
												</td>
												<td class="whitebox2wk">
													<s:textfield name="workorderNo" id="workorderNo" cssClass="selectboldwk"/>
												</td>
											</tr>
											<tr>
												<td colspan="4" class="shadowwk"></td>
											</tr>
											<tr>
												<td colspan="4">
													<div class="buttonholdersearch">
														<input type="submit" class="buttonadd" value="Search" id="searchButton" 
															name="Search"  onclick="return validateAndSubmit();" />
													</div>
													<s:if test="%{workOrderList != null}">
													<div id="capsearch" >
														<table width="100%" border="0" cellspacing="0"
															cellpadding="0">
															<tr>
																<td>
																	<table width="100%" border="0" cellspacing="0"
																		cellpadding="0">
																		<tr>
																			<td width="4%" class="headingwk">
																				<div class="arrowiconwk">
																					<img src="/egworks/resources/erp2/images/arrow.gif" />
																				</div>
																				<div class="headplacer">
																					<s:text name='workOrder.search.label.searchResult' />
																				</div>
																			</td>
																		</tr>
																		<tr>
																			<td class="aligncenter">
																				<table width="400" border="0" cellpadding="0"
																					cellspacing="0" align="center">
																					<tr>
																						<td width="17%" class="tablesubheadwka">
																							<s:text name='workOrder.search.label.slno' />:
																						</td>
																						<td width="83%" class="tablesubheadwka">
																							<s:text name='workOrder.search.label.workOrderNo' />:
																						</td>
																					</tr>
																				</table>
																				<div class="searchscrollershort">

																					<table width="400" border="0" cellpadding="0"
																						cellspacing="0">
																						<s:if test="%{workOrderList.size() == 0}">
																							<s:text name="search.noresultfound" />
																						</s:if>
																						<s:iterator value="workOrderList" var="cont" status="stat">
																						<tr onmousedown="" onmouseover="ChangeColor(this, true);"
																							onmouseout="ChangeColor(this, false);" href="#"
																							onclick="javascript:returnBackToParent('<s:property value="id"/>','<s:property value="workOrderNumber"/>')"
																							id="getdate" style="cursor: hand">
																							<td width="17%" class="whitebox3wka">
																								<s:label value="%{#stat.index + 1}" />
																							</td>
																							<td width="83%" class="whitebox3wka">
																								<s:property value="workOrderNumber"/>
																							</td>
																						</tr>
																						</s:iterator>
																					</table>
																				</div>
																			</td>
																		</tr>
																		<tr>
																			<td class="shadowwk"></td>
																		</tr>
																	</table>
																</td>
															</tr>
														</table>
													</div>
													</s:if>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
										<div align="right" class="mandatory">
											* <s:text name='workorder.serach.mandatory' />
										</div>
									</td>
								</tr>
							</table>
						</div>
						<div class="rbbot2">
							<div></div>
						</div>
					</div>
				</div>
			</div>
			<div class="buttonholderwk">
			<input type="button" class="buttonfinal" value="CLEAR" id="button" name="clear" onclick="clearAll();">
			&nbsp;
			<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();" />
			</div>
		</s:form>
		
		<script type="text/javascript">
			function clearAll() {
				$('searchWorkOrderForm').reset();
				$('contractorId').value= '-1';
				$('tenderNo').value = '';
				$('createDate').clear();
				$('workorderNo').clear();
			}
		
			function validateAndSubmit() {
				if($F('contractorId') == -1
						&& $F('tenderNo') == ''
						&& $F('workorderNo').blank()
						&& $F('createDate').blank()){
						
			    	$('date_error').hide();
					$('mandatory_length_error').hide();	
					$('mandatory_error').show();
					$('form_error').show();				
					return false;
				}
				else
					$('mandatory_error').hide();
				
				if(!$F('workorderNo').blank() && $F('workorderNo').strip().length < 4) {
					$('mandatory_length_error').show();		
					$('form_error').show();			
					return false;
				}
				else
					$('mandatory_length_error').hide();	
				
				if(!checkDate($('createDate'))) {
					return false;
				}
						
				$('form_error').hide();
				document.searchWorkOrderForm.action='${pageContext.request.contextPath}/workorder/workOrder!searchWOForMB.action';
    			document.searchWorkOrderForm.submit();
			}
			
			function checkDate(obj){
				if(!validateDateFormat(obj)) {
					$('mandatory_error').hide();
			    	$('date_error').show();
			    	$('form_error').show();
					$('createDate').focus();
			    	return false;
				}
				
				return true;
			}
			
			function returnBackToParent(workOrderId,workOrderNumber) {
				//document.all.ret.value =  workOrderId+ '`~`' + workOrderCode+ '`~`' + workOrdername;
				var wind;
				var data = new Array();
				wind=window.dialogArguments;
		
				if(wind==undefined){
					wind=window.opener;
					data=workOrderId+ '`~`' + workOrderNumber;
					window.opener.update(data);
				}
		
				else{
					wind=window.dialogArguments;
					wind.result=workOrderId+ '`~`' + workOrderNumber;
				}
				window.close();
			}

			

			function ChangeColor(tableRow, highLight)
			{
				if (highLight)
				{
				  tableRow.style.backgroundColor = '#dcfac9';
				}
				else
				{
				  tableRow.style.backgroundColor = 'white';
				}
			}
	    
		</script>
	</body>
</html>
