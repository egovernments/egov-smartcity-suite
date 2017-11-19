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
<html>
<head>
<title><s:text name="chartOfAccount.add" /></title>
<script type="text/javascript">
	function validate() {
		if (document.getElementById('model.name').value == null
				|| document.getElementById('model.name').value == '') {
			bootbox.alert("Please enter Name");
			return false;
		}
		if (document.getElementById('newGlcode').value == null
				|| document.getElementById('newGlcode').value == '') {
			bootbox.alert("Please enter Account Code");
			return false;
		}
		document.chartOfAccountsForm.action = '${pageContext.request.contextPath}/masters/chartOfAccounts-save.action';
		document.chartOfAccountsForm.submit();

		return true;
	}
</script>
</head>
<body>
	<jsp:include page="../budget/budgetHeader.jsp" />
	<s:actionmessage theme="simple" />
	<div class="formmainbox">
		<div class="subheadnew">
			<s:text name="chartOfAccount.add" />
		</div>
		<s:actionerror />
		<s:fielderror />
		<s:form name="chartOfAccountsForm" action="chartOfAccounts"
			id="chartOfAccountsForm" theme="simple">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				id="chartOfAccountsTable">
				<tr>
					<td width="20%" class="bluebox">&nbsp;</td>
					<td width="10%" class="bluebox"><strong><s:text
								name="chartOfAccount.accountCode" />:<span class="mandatory1">*</span></strong></td>
					<td width="22%" class="bluebox"><input type="text"
						readonly="readonly" name="generatedGlcode" id="generatedGlcode"
						size="10" value='<s:property value="generatedGlcode"/>' /> <input
						type="text" name="newGlcode" id="newGlcode" size="5"
						maxlength='<s:property value="glCodeLengths[model.classification]"/>'
						value='<s:property value="newGlcode"/>' /></td>
					<td width="10%" class="bluebox"><strong><s:text
								name="chartOfAccount.name" />:<span class="mandatory1">*</span></strong></td>
					<td class="bluebox"><input type="text" id="model.name"
						name="model.name" onKeyDown="textCounter('model.name',100)"
						onKeyUp="textCounter('model.name',100)"
						onblur="textCounter('model.name',100)" /></td>
				</tr>
				<tr>
					<td width="20%" class="greybox">&nbsp;</td>
					<td width="10%" class="greybox"><strong><s:text
								name="chartOfAccount.description" />:</strong></td>
					<td width="22%" class="greybox"><input type="text"
						id="model.desc" name="model.desc"
						onKeyDown="textCounter('model.desc',250)"
						onKeyUp="textCounter('model.desc',250)"
						onblur="textCounter('model.desc',250)" /></td>
					<td width="10%" class="greybox"><strong><s:text
								name="chartOfAccount.type" />:</strong></td>
					<td class="greybox"><input type="text" name="type"
						value='<s:property value="model.type"/>' id="chartOfAccounts_type"
						style="border: 0px;" readOnly="true" /></td>
				</tr>
				<tr>
					<td width="20%" class="bluebox">&nbsp;</td>
					<td width="10%" class="bluebox"><strong><s:text
								name="chartOfAccount.classification" />:</strong></td>
					<td width="22%" class="bluebox"><input type="text"
						name="model.classification"
						value='<s:property value="model.classification"/>'
						style="border: 0px;" /></td>
					<td width="10%" class="bluebox"><strong><s:text
								name="chartOfAccount.purpose" />:</strong></td>
					<td class="bluebox"><s:select list="dropdownData.purposeList"
							listKey="id" listValue="name" name="accountcodePurpose.id" headerKey="0"
							headerValue="--- Select ---" value="accountcodePurpose.id"></s:select></td>
				</tr>
				<tr>
					<td width="20%" class="greybox">&nbsp;</td>
					<td width="10%" class="greybox"><strong><s:text
								name="chartOfAccount.accountDetailType" />:</strong></td>
					<td width="22%" class="greybox"><s:select
							list="dropdownData.accountDetailTypeList" listKey="id"
							listValue="name" name="accountDetailTypeList" multiple="true"
							value="%{accountDetailTypeList.{id}}"></s:select></td>
					<td width="10%" class="greybox"><strong><s:text
								name="chartOfAccount.activeForPosting" />:</strong></td>
					<td class="greybox"><s:checkbox name="activeForPosting"></s:checkbox></td>
				</tr>
				<tr>
					<td width="20%" class="bluebox">&nbsp;</td>
					<td width="10%" class="bluebox"><strong><s:text
								name="chartOfAccount.functionRequired" />:</strong></td>
					<td width="22%" class="bluebox"><s:checkbox
							name="functionRequired"></s:checkbox></td>
					<td width="10%" class="bluebox"><strong><s:text
								name="chartOfAccount.budgetRequired" />:</strong></td>
					<td class="bluebox"><s:checkbox name="budgetCheckRequired"></s:checkbox></td>
				</tr>
			</table>
			<br />
			<br />
			<div class="buttonbottom">
				<input type="hidden" name="parentId"
					value='<s:property value="parentId"/>' />
				<s:submit name="Save" value="Save"
					onclick="javascript: return validate();" cssClass="buttonsubmit" />
				<s:submit value="Close" onclick="javascript: self.close()"
					cssClass="buttonsubmit" />
			</div>
			<s:token />
		</s:form>
	</div>
</body>
</html>
