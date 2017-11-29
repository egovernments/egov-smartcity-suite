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
<title><s:text name="chartOfAccount" /></title>

<script type="text/javascript">
	function submitForm() {
		var id = '<s:property value="coaId"/>';
		document.chartOfAccountsForm.action = '${pageContext.request.contextPath}/masters/chartOfAccounts-modify.action?model.id='
				+ id;
		document.chartOfAccountsForm.submit();

		return true;
	}
	function submitForAdd() {
		var id = '<s:property value="coaId"/>';
		document.chartOfAccountsForm.action = '${pageContext.request.contextPath}/masters/chartOfAccounts-addNewCoa.action?parentId='
				+ id + "&model.id=" + id;
		document.chartOfAccountsForm.submit();

		return true;
	}
</script>
</head>
<body>
	<jsp:include page="../budget/budgetHeader.jsp" />
	<div class="subheadnew">
		<s:text name="chartOfAccount" />
	</div>
	<span class="mandatory1"> <s:actionmessage theme="simple" /> <s:actionerror />
		<s:fielderror /></span>
	<s:form name="chartOfAccountsForm" id="chartOfAccountsForm"
		action="chartOfAccounts" theme="simple">
		<div class="formmainbox">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				id="chartOfAccountsTable">
				<tr>
					<td width="20%" class="bluebox">&nbsp;</td>
					<td width="10%" class="bluebox"><strong><s:text
								name="chartOfAccount.accountCode" />:</strong></td>
					<td width="22%" class="bluebox"><s:property
							value="model.glcode" /></td>
					<td class="bluebox"><strong><s:text
								name="chartOfAccount.name" />:</strong></td>
					<td class="bluebox"><s:property value="model.name" /></td>
				</tr>
				<tr>
					<td width="20%" class="greybox">&nbsp;</td>
					<td width="10%" class="greybox"><strong><s:text
								name="chartOfAccount.description" />:</strong></td>
					<td width="22%" class="greybox"><s:property value="model.desc" /></td>
					<td width="10%" class="greybox"><strong><s:text
								name="chartOfAccount.type" />:</strong></td>
					<td class="greybox"><s:if test="model.type == 'I'">
							<s:text name="chartOfAccount.income" />
						</s:if> <s:if test="model.type == 'E'">
							<s:text name="chartOfAccount.expense" />
						</s:if> <s:if test="model.type == 'A'">
							<s:text name="chartOfAccount.asset" />
						</s:if> <s:if test="model.type == 'L'">
							<s:text name="chartOfAccount.liability" />
						</s:if></td>
				</tr>
				<tr>
					<td width="20%" class="bluebox">&nbsp;</td>
					<td width="10%" class="bluebox"><strong><s:text
								name="chartOfAccount.classification" />:</strong></td>
					<td width="22%" class="bluebox"><s:if
							test="%{model.classification == 1}">
							<s:text name="chartOfAccount.majorCode" />
						</s:if> <s:elseif test="%{model.classification == 2}">
							<s:text name="chartOfAccount.minorCode" />
						</s:elseif> <s:elseif test="%{model.classification == 4}">
							<s:text name="chartOfAccount.detailedCode" />
						</s:elseif> <s:else>
						</s:else></td>
					<td width="10%" class="bluebox"><strong><s:text
								name="chartOfAccount.purpose" />:</strong></td>
					<td class="bluebox"><s:property
							value="accountcodePurpose.name" /></td>
				</tr>
				<tr>
					<td width="20%" class="greybox">&nbsp;</td>
					<td width="10%" class="greybox"><strong><s:text
								name="chartOfAccount.accountDetailType" />:</strong></td>
					<td width="22%" class="greybox"><s:iterator
							value="model.chartOfAccountDetails" status="status">
							<s:property value="detailTypeId.name" />
							<s:if test="!#status.last">,</s:if>
						</s:iterator></td>
					<td width="10%" class="greybox"><strong><s:text
								name="chartOfAccount.activeForPosting" />:</strong></td>
					<td class="greybox"><s:if
							test="%{getIsActiveForPosting() == true}">
							<s:text name="yes" />
						</s:if> <s:else>
							<s:text name="no" />
						</s:else></td>
				</tr>
				<tr>
					<td width="20%" class="bluebox">&nbsp;</td>
					<td width="10%" class="bluebox"><strong><s:text
								name="Function Required" />:</strong></td>
					<td width="22%" class="bluebox"><s:if
							test="%{getFunctionReqd() == true}">
							<s:text name="yes" />
						</s:if> <s:else>
							<s:text name="no" />
						</s:else></td>
					<td width="10%" class="bluebox"><strong><s:text
								name="Budget Required" />:</strong></td>
					<td class="bluebox"><s:if test="%{budgetCheckReq() == true}">
							<s:text name="yes" />
						</s:if> <s:else>
							<s:text name="no" />
						</s:else></td>
				</tr>
			</table>
			<br /> <br />
		</div>
		<div class="buttonbottom">
			<s:if test="%{coaId !=null || coaId!=''}">
				<s:if test="%{shouldAllowCreation()}">
					<s:submit name="Add" value="Add" cssClass="buttonsubmit"
						onclick="submitForAdd()" />
				</s:if>
			</s:if>
			<input type="button" value="Close"
				onclick="javascript:window.close()" class="button" />
		</div>
	</s:form>
</body>
</html>
