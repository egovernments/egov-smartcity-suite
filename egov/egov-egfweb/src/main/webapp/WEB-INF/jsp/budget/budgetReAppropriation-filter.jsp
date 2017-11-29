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
<tr>
	<td width="10%" class="bluebox">&nbsp;</td>
	<td class="bluebox"><s:text name="budget.financialYear" /><span
		class="mandatory">*</span></td>
	<td class="bluebox"><s:select list="dropdownData.finYearList"
			listKey="id" listValue="finYearRange" name="financialYear.id"
			value="financialYear.id" id="financialYear" headerKey="0"
			headerValue="--- Select ---"></s:select></td>
	<td class="bluebox"><s:text name="budget.bere" /></td>
	<td class="bluebox"><s:select name="isBeRe" id="isBeRe"
			list="#{'BE':'BE','RE':'RE'}" name="budgetDetail.budget.isbere"
			value="budgetDetail.budget.isbere" /></td>
</tr>
<tr>
	<s:if
		test="%{shouldShowHeaderField('fund') || shouldShowGridField('fund')}">
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="fund" /></td>
		<td class="greybox"><s:select list="dropdownData.fundList"
				listKey="id" listValue="name" name="budgetDetail.fund.id"
				headerKey="0" headerValue="--- Select ---" value="fund.id"
				id="budgetReAppropriation_fund"></s:select></td>
	</s:if>
	<s:if
		test="%{shouldShowHeaderField('executingDepartment') || shouldShowGridField('executingDepartment')}">
		<td class="greybox"><s:text
				name="budgetdetail.executingDepartment" /></td>
		<td width="22%" class="greybox"><s:select
				list="dropdownData.executingDepartmentList" listKey="id"
				listValue="name" name="budgetDetail.executingDepartment.id"
				headerKey="0" headerValue="--- Select ---"
				value="budgetDetail.executingDepartment.id"
				id="budgetReAppropriation_executingDepartment"></s:select></td>
	</s:if>
</tr>
<tr>
	<s:if
		test="%{shouldShowField('function') || shouldShowGridField('function')}">
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox"><s:text name="function" /></td>
		<td class="bluebox"><s:select list="dropdownData.functionList"
				listKey="id" listValue="name" name="budgetDetail.function.id"
				headerKey="0" headerValue="--- Select ---" value="function.id"
				id="budgetReAppropriation_function"></s:select></td>
	</s:if>
	<s:if
		test="%{shouldShowHeaderField('functionary') || shouldShowGridField('functionary')}">
		<td class="bluebox"><s:text name="functionary" /></td>
		<td class="bluebox"><s:select list="dropdownData.functionaryList"
				listKey="id" listValue="name" headerKey="0"
				headerValue="--- Select ---" name="budgetDetail.functionary.id"
				value="functionary.id" id="budgetReAppropriation_functionary"></s:select></td>
	</s:if>
</tr>
<tr>
	<s:if
		test="%{shouldShowHeaderField('scheme') || shouldShowGridField('scheme')}">
		<td width="10%" class="bluebox">&nbsp;</td>
		<td class="greybox"><s:text name="scheme" /></td>
		<td class="greybox"><s:select list="dropdownData.schemeList"
				listKey="id" listValue="name" headerKey="0"
				headerValue="--- Select ---" name="budgetDetail.scheme.id"
				onchange="populateSubSchemes(this);" value="scheme.id"
				id="budgetReAppropriation_scheme"></s:select></td>
	</s:if>
	<s:if
		test="%{shouldShowHeaderField('subScheme') || shouldShowGridField('subScheme')}">
		<egov:ajaxdropdown id="subScheme" fields="['Text','Value']"
			dropdownId="budgetReAppropriation_subScheme"
			url="budget/budgetDetail!ajaxLoadSubSchemes.action"
			afterSuccess="onHeaderSubSchemePopulation" />
		<td class="greybox"><s:text name="subscheme" /></td>
		<td class="greybox"><s:select list="dropdownData.subSchemeList"
				listKey="id" listValue="name" headerKey="0"
				headerValue="--- Select ---" name="budgetDetail.subScheme"
				value="subScheme.id" id="budgetReAppropriation_subScheme"></s:select></td>
	</s:if>

</tr>
<tr>
	<s:if
		test="%{shouldShowHeaderField('boundary') || shouldShowGridField('boundary')}">
		<td class="bluebox"><s:text name="field" /></td>
		<td class="bluebox"><s:select list="dropdownData.boundaryList"
				listKey="id" listValue="name" headerKey="0"
				headerValue="--- Select ---" name="budgetDetail.boundary.id"
				value="boundary.id" id="budgetReAppropriation_boundary"></s:select></td>
	</s:if>
</tr>
