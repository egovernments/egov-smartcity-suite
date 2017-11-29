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
<title><s:text name="chartOfAccount.modify" /></title>
<script type="text/javascript">
	function validateAndSubmit() {

		document.chartOfAccountsForm.action = '${pageContext.request.contextPath}/masters/chartOfAccounts-update.action';
		document.chartOfAccountsForm.submit();

		return true;
	}
</script>
</head>
<body>
	<jsp:include page="../budget/budgetHeader.jsp" />
	<div class="subheadnew">
		<s:text name="chartOfAccount.modify" />
	</div>
	<s:actionmessage theme="simple" />
	<s:actionerror />
	<s:fielderror />
	<s:form name="chartOfAccountsForm" action="chartOfAccounts"
		theme="simple">
		<div class="formmainbox">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				id="chartOfAccountsTable">
				<tr>
					<td width="20%" class="bluebox">&nbsp;</td>
					<td width="10%" class="bluebox"><strong><s:text
								name="chartOfAccount.accountCode" />:</strong></td>
					<td width="22%" class="bluebox"><input type="text"
						name="glcode" value='<s:property value="model.glcode"/>'
						id="chartOfAccounts_glcode" style="border: 0px;" readOnly="true" /></td>
					<td width="10%" class="bluebox"><strong><s:text
								name="chartOfAccount.name" />:<span class="mandatory1">*</span></strong></td>
					<td class="bluebox"><input type="text" name="name"
						value='<s:property value="model.name"/>' id="chartOfAccounts_name" /></td>
				</tr>
				<tr>
					<td width="20%" class="greybox">&nbsp;</td>
					<td width="10%" class="greybox"><strong><s:text
								name="chartOfAccount.description" />:</strong></td>
					<td width="22%" class="greybox"><s:textfield name="desc" /></td>
					<td width="10%" class="greybox"><strong><s:text
								name="chartOfAccount.type" />:</strong></td>
					<td class="greybox"><s:if test="model.type == 'I'">
							<input type="text" name="type"
								value='<s:text name="chartOfAccount.income"/>'
								id="chartOfAccounts_type" style="border: 0px;" readOnly="true" />
						</s:if> <s:if test="model.type == 'E'">
							<input type="text" name="type"
								value='<s:text name="chartOfAccount.expense"/>'
								id="chartOfAccounts_type" style="border: 0px;" readOnly="true" />
						</s:if> <s:if test="model.type == 'A'">
							<input type="text" name="type"
								value='<s:text name="chartOfAccount.asset"/>'
								id="chartOfAccounts_type" style="border: 0px;" readOnly="true" />
						</s:if> <s:if test="model.type == 'L'">
							<input type="text" name="type"
								value='<s:text name="chartOfAccount.liability"/>'
								id="chartOfAccounts_type" style="border: 0px;" readOnly="true" />
						</s:if></td>
				</tr>
				<tr>
					<td width="20%" class="bluebox">&nbsp;</td>
					<td width="10%" class="bluebox"><strong><s:text
								name="chartOfAccount.purpose" />:</strong></td>
					<td class="bluebox"><s:select list="dropdownData.purposeList"
							listKey="id" listValue="name" name="accountcodePurpose.id"
							headerKey="0" headerValue="--- Select ---"
							value="accountcodePurpose.id"></s:select></td>
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
				</tr>
				<tr>
					<td width="20%" class="greybox">&nbsp;</td>

					<td width="10%" class="greybox"><strong><s:text
								name="chartOfAccount.activeForPosting" />:</strong></td>
					<td class="greybox"><s:if
							test="%{getIsActiveForPosting() == true}">
							<s:checkbox name="activeForPosting" value="isActiveForPosting"
								checked="checked"></s:checkbox>
						</s:if> <s:else>
							<s:checkbox name="activeForPosting" value="isActiveForPosting"></s:checkbox>
						</s:else></td>
					<td width="10%" class="greybox"><strong><s:text
								name="chartOfAccount.accountDetailType" />:</strong></td>
					<td width="22%"><s:select
							list="dropdownData.accountDetailTypeList" listKey="id"
							listValue="name" name="accountDetailTypeList" multiple="true"
							size="5" value="accountDetailTypeList"></s:select></td>
				</tr>
				<tr>
					<td width="20%" class="bluebox">&nbsp;</td>
					<td width="10%" class="bluebox"><strong><s:text
								name="chartOfAccount.functionRequired" />:</strong></td>
					<td width="22%" class="bluebox"><s:if
							test="%{getFunctionReqd() == true}">
							<s:checkbox name="functionRequired" value="functionReqd"
								checked="checked"></s:checkbox>
						</s:if> <s:else>
							<s:checkbox name="functionRequired" value="functionReqd"></s:checkbox>
						</s:else></td>
					<td width="10%" class="bluebox"><strong><s:text
								name="chartOfAccount.budgetRequired" />:</strong></td>
					<td class="bluebox"><s:if test="%{budgetCheckReq() == true}">
							<s:checkbox name="budgetCheckRequired" value="budgetCheckReq"
								checked="checked"></s:checkbox>
						</s:if> <s:else>
							<s:checkbox name="budgetCheckRequired" value="budgetCheckReq"></s:checkbox>
						</s:else></td>
				</tr>
			</table>
			<br /> <br />
		</div>
		<div class="buttonbottom">
			<input type="hidden" name="model.id"
				value='<s:property value="model.id"/>' /> <input type="button"
				class="buttonsubmit" value="Save" id="Search" name="Search"
				onclick="return validateAndSubmit();" /> <input type="button"
				value="Close" onclick="javascript:window.close()" class="button" />
		</div>
		<s:token />
	</s:form>
</body>
</html>
