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


<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>
<%@ page import="org.egov.budget.model.*"%>
<html>
<head>
<title><s:text name="budget.reappropriation.title" /></title>
<link rel="stylesheet" href="/EGF/resources/css/tabber.css?rnd=${app_release_no}"
	TYPE="text/css">
<script type="text/javascript" src="/EGF/resources/javascript/tabber.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/tabber2.js?rnd=${app_release_no}"></script>
<script type="text/javascript" src="/EGF/resources/javascript/helper.js?rnd=${app_release_no}"></script>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/calenderNew.js?rnd=${app_release_no}"></script>
<script>
    if(opener != null && opener.top != null){
		opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
	}
	
	function onLoadTask(){
		if('<s:property value="message"/>' != ''){
			bootbox.alert('<s:property value="message"/>');
			window.close();
		}
		return;
	}
	
	function validateAppoveUser(name,value){
		<s:if test="%{workFlowItem.currentState.value =='END'}">
			if(value == 'Approve' || value == 'Reject') {
				document.getElementById("approverUserId").value=-1;
				return true;
			}
		</s:if>
		<s:else>
			if( (value == 'forward' || value == 'Forward') && null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
				bootbox.alert("please select User");
				return false;
			}
		</s:else>
		return true;
	}
	</script>
</head>
<body>
	<%@ include file='common-includes.jsp'%>
	<jsp:include page="budgetHeader.jsp" />
	<s:form name="budgetReAppropriationForm"
		action="budgetReAppropriationModify" theme="simple">
		<div class="formmainbox">
			<div class="subheadnew">
				<s:text name="budgetReApp" />
			</div>
			<s:actionmessage theme="simple" />
			<s:actionerror />
			<s:fielderror />
		</div>
		<div align="left">
			<br />
			<table border="0" cellspacing="0" cellpadding="0" width="100%">
				<tr>
					<td>
						<div class="tabber">
							<div class="tabbertab">
								<h2>Additional Appropriations</h2>
								<span> <s:if
										test="%{not savedBudgetReAppropriationList.empty}">
										<div id="detail"
											style="width: 100%; overflow-x: auto; overflow-y: hidden;">
											<table align="center" border="0" cellpadding="0"
												cellspacing="0" width="100%" class="tablebottom"
												style="border-right: 0px solid #C5C5C5;">
												<tr>
													<th class="bluebgheadtd" width="10%"><s:text
															name="budgetdetail.budget" /></th>
													<th class="bluebgheadtd" width="11%"><s:text
															name="budgetdetail.budgetGroup" /></th>
													<s:if test="%{shouldShowField('executingDepartment')}">
														<th class="bluebgheadtd" width="16%"><s:text
																name="budgetdetail.executingDepartment" /></th>
													</s:if>
													<s:if test="%{shouldShowField('fund')}">
														<th class="bluebgheadtd" width="10%"><s:text
																name="budgetdetail.fund" /></th>
													</s:if>
													<s:if test="%{shouldShowField('function')}">
														<th class="bluebgheadtd" width="10%"><s:text
																name="budgetdetail.function" /></th>
													</s:if>
													<s:if test="%{shouldShowField('functionary')}">
														<th class="bluebgheadtd" width="10%"><s:text
																name="budgetdetail.functionary" /></th>
													</s:if>
													<s:if test="%{shouldShowField('scheme')}">
														<th class="bluebgheadtd" width="10%"><s:text
																name="budgetdetail.scheme" /></th>
													</s:if>
													<s:if test="%{shouldShowField('subScheme')}">
														<th class="bluebgheadtd" width="10%"><s:text
																name="budgetdetail.subScheme" /></th>
													</s:if>
													<s:if test="%{shouldShowField('boundary')}">
														<th class="bluebgheadtd" width="10%"><s:text
																name="budgetdetail.field" /></th>
													</s:if>
													<th class="bluebgheadtd" width="16%">Sanctioned Budget</th>
													<th class="bluebgheadtd" width="16%">Expenditure
														Incurred</th>
													<th class="bluebgheadtd" width="16%">Added/Released</th>
													<th class="bluebgheadtd" width="16%">Balance Fund
														Available</th>
													<th class="bluebgheadtd" width="16%">Change Requested</th>
													<th class="bluebgheadtd" width="16%">Addition of<br />Funds
														sought(Rs)
													</th>
													<s:if test="%{enableApprovedAmount()}">
														<th class="bluebgheadtd" width="16%">Addition of<br />Funds
															sought Approved Amount(Rs)
														</th>
													</s:if>
												</tr>
												<s:iterator value="savedBudgetReAppropriationList"
													status="stat">
													<tr>
														<input type='hidden'
															name="savedBudgetReAppropriationList[<s:property value='#stat.index'/>].id"
															value="<s:property value='id'/>" />
														<td class="blueborderfortd"><s:property
																value="budgetDetail.budget.name" /> &nbsp;</td>
														<td class="blueborderfortd"><s:property
																value="budgetDetail.budgetGroup.name" />&nbsp;</td>
														<s:if test="%{shouldShowField('executingDepartment')}">
															<td class="blueborderfortd"><s:property
																	value="budgetDetail.executingDepartment.deptName" />&nbsp;</td>
														</s:if>
														<s:if test="%{shouldShowField('fund')}">
															<td class="blueborderfortd"><s:property
																	value="budgetDetail.fund.name" />&nbsp;</td>
														</s:if>
														<s:if test="%{shouldShowField('function')}">
															<td class="blueborderfortd"><s:property
																	value="budgetDetail.function.name" />&nbsp;</td>
														</s:if>
														<s:if test="%{shouldShowField('functionary')}">
															<td class="blueborderfortd"><s:property
																	value="budgetDetail.functionary.name" />&nbsp;</td>
														</s:if>
														<s:if test="%{shouldShowField('scheme')}">
															<td class="blueborderfortd"><s:property
																	value="budgetDetail.scheme.name" />&nbsp;</td>
														</s:if>
														<s:if test="%{shouldShowField('subScheme')}">
															<td class="blueborderfortd"><s:property
																	value="budgetDetail.subScheme.name" />&nbsp;</td>
														</s:if>
														<s:if test="%{shouldShowField('boundary')}">
															<td class="blueborderfortd"><s:property
																	value="budgetDetail.boundary.name" />&nbsp;</td>
														</s:if>
														<td class="blueborderfortd">
															<div style="text-align: right">
																<s:text name="format.number">
																	<s:param value="approvedAmount" />
																</s:text>
															</div>
														</td>
														<td class="blueborderfortd">
															<div style="text-align: right">
																<s:text name="format.number">
																	<s:param value="actuals" />
																</s:text>
															</div>
														</td>
														<td class="blueborderfortd">
															<div style="text-align: right">
																<s:text name="format.number">
																	<s:param value="addedReleased" />
																</s:text>
															</div>
														</td>
														<td class="blueborderfortd">
															<div style="text-align: right">
																<s:text name="format.number">
																	<s:param value="availableAmount" />
																</s:text>
															</div>
														</td>
														<td class="blueborderfortd"><input type="hidden"
															name='savedBudgetReAppropriationList[<s:property value="#stat.index"/>].changeRequestType'
															id='savedBudgetReAppropriationList[<s:property value="#stat.index"/>].changeRequestType'
															value='<s:property value="changeRequestType"/>' />
															<div style="text-align: right">
																<s:property value="changeRequestType" />
															</div></td>
														<s:if test="%{enableOriginalAmount()}">
															<td class="blueborderfortd">
																<div style="text-align: right">
																	<input type="text"
																		style="text-align: right; size: 50px;"
																		id='savedBudgetReAppropriationList[<s:property value="#stat.index"/>].deltaAmount'
																		name='savedBudgetReAppropriationList[<s:property value="#stat.index"/>].deltaAmount'
																		value='<s:property value="deltaAmount"/>' />
																</div>
															</td>
														</s:if>
														<s:else>
															<td class="blueborderfortd">
																<div style="text-align: right">
																	<s:text name="format.number">
																		<s:param value="deltaAmount" />
																	</s:text>
																</div>
															</td>
														</s:else>
														<s:if test="%{enableApprovedAmount()}">
															<td class="blueborderfortd">
																<div style="text-align: right">
																	<input type="text"
																		style="text-align: right; size: 50px;"
																		id='savedBudgetReAppropriationList[<s:property value="#stat.index"/>].approvedDeltaAmount'
																		name='savedBudgetReAppropriationList[<s:property value="#stat.index"/>].approvedDeltaAmount'
																		value='<s:property value="approvedDeltaAmount"/>' />
																</div>
															</td>
														</s:if>
													</tr>
												</s:iterator>
											</table>
										</div>
									</s:if>
								</span>
							</div>
							<div class="tabbertab">
								<h2>Approval Details</h2>
								<span> <%@include file="../voucher/workflowApproval.jsp"%>
									<script>
						    var dept_callback = {
								success: function(o){
									if(trimStr(o.responseText) != '' && trimStr(o.responseText) != '0'){
										document.getElementById('departmentid').value = trimStr(o.responseText);
										if(document.getElementById('departmentid').value!=-1){
											document.getElementById('departmentid').disabled=true;
											populateDesg();
										}else{
											document.getElementById('departmentid').disabled=false;
										}}
									},
									failure: function(o) {
								    }
								}
								
								function defaultDept(){
									var url = '/EGF/voucher/common!ajaxLoadDefaultDepartment.action';
									YAHOO.util.Connect.asyncRequest('POST', url, dept_callback, null);
								}
							defaultDept();
							onLoadTask();
						</script>
								</span>
							</div>
							<!-- Individual tab -->
						</div>
					</td>
				</tr>
			</table>
			Comments:
			<s:textarea name="comment" cols="50" id="comment"
				onkeydown="textCounter('comment',250)"
				onkeyup="textCounter('comment',250)"
				onblur="textCounter('comment',250)" />
			<br />
			<div class="buttonholderwk" id="buttonsDiv" align="center">
				<s:hidden name="actionName" />
				<div class="buttonbottom">
					<s:iterator value="%{validActions}">
						<s:submit type="submit" cssClass="buttonsubmit"
							value="%{capitalize(description)}" id="%{name}" name="%{name}"
							method="performAction"
							onclick="javascript:document.getElementById('actionName').value='%{name}';return validateAppoveUser('%{name}','%{description}')" />
					</s:iterator>
					<input type="submit" value="Close"
						onclick="javascript:window.close()" class="button" />
				</div>
			</div>
		</div>
		<s:hidden id="scriptName" value="BudgetDetail.nextDesg" />
		<s:hidden name="actionName" id="actionName" />
		<input type="hidden" name="miscId"
			value='<s:property value="miscId"/>' />
	</s:form>
</body>
</html>
