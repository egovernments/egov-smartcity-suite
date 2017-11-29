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
<html>
<head>
<title><s:text name="budget.search" /></title>
<STYLE type="text/css">
.tabbertab {
	border: 1px solid #CCCCCC;
	height: 420px;
	margin-bottom: 8px;
	overflow: scroll;
}
</STYLE>
</head>

<body>
	<script>
			var budgetDetailsTable = null;

			function validateAndSubmit()
			{
				document.forms[0].action='/EGF/budget/budgetSearch-groupedBudgets.action';
				document.forms[0].submit();
				}
			
		</script>
	<jsp:include page="budgetHeader.jsp" />
	<s:form action="budgetSearch" theme="simple">
		<div class="formmainbox">
			<div class="subheadnew">
				<s:text name="budget.search" />
			</div>
			<%@ include file='budgetSearch-form.jsp'%>
			<div class="buttonbottom" style="padding-bottom: 10px;">
				<input type="button" value="Search" class="buttonsubmit"
					  onclick="return validateAndSubmit()" />
				<s:reset value="Reset" cssClass="button" /> 
				<input type="button" value="Close"
					onclick="javascript:window.close()" class="button" />
			</div>
			<h5 style="color:red">
			<s:actionerror/>
			<s:actionmessage/></h5>
	</s:form>

	<s:if test="%{!budgetList.isEmpty()}">
		<div id="detail">
			<table align="center" border="0" cellpadding="0" cellspacing="0"
				width="100%" class="tablebottom"
				style="border-right: 0px solid #C5C5C5;">
				<tr>
					<td colspan="9">
						<div class="subheadsmallnew">
							<strong>Budget</strong>
						</div>
					</td>
				</tr>
				<tr>
					<th class="bluebgheadtd" width="10%"><s:text
							name="budget.budgetname" /></th>
					<th class="bluebgheadtd" width="11%"><s:text
							name="budget.parent" /></th>
					<th class="bluebgheadtd" width="10%"><s:text
							name="budget.description" /></th>
				</tr>
				<s:iterator value="budgetList" status="stat">
					<tr>
						<td class="blueborderfortd">
						<a
							href='<s:url action="budgetSearch-groupedBudgetDetailList">
							<s:param name="budget.id" value="%{id}" />
							<s:param name="skipPrepare" value="true"/>
							</s:url>'><s:property value="name" /></a>  &nbsp;</td>
						<td class="blueborderfortd"><s:property value="parent.name" />&nbsp;</td>
						<td class="blueborderfortd"><s:property value="description" />&nbsp;</td>
					</tr>
				</s:iterator>
			</table>
		</div>
	</s:if>
	
</body>
</html>
