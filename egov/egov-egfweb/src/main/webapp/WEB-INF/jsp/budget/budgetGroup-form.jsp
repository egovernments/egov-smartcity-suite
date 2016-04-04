<!--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  -->
<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<s:push value="model">
		<tr>
			<td class="bluebox"><s:text name="budgetgroup.groupname" /> <span
				class="mandatory">*</span></td>
			<td class="bluebox"><s:textfield name="name" id="name"
					maxlength="250" size="120" /><span class="highlight2">Max.
					250 characters</span></td>
		</tr>
		<tr>
			<td class="greybox"><s:text name="budgetgroup.accounttype" /> <span
				class="mandatory">*</span></td>
			<td class="greybox"><s:select name="accountType"
					id="accountType" list="dropdownData.accountTypeList" /></td>
		</tr>
		<tr>
			<td class="bluebox"><s:text name="budgetgroup.budgetingtype" />
				<span class="mandatory">*</span></td>
			<td class="bluebox"><s:select name="budgetingType"
					id="budgetingType" list="dropdownData.budgetingTypeList" /></td>
		</tr>
		<tr>
			<td class="greybox"><s:text name="budgetgroup.majorcode" /></td>
			<td class="greybox"><s:select name="majorCode" id="majorCode"
					list="dropdownData.majorCodeList" listKey="id"
					listValue="%{glcode+'----'+name}" headerKey="-1"
					headerValue="----Select----" value="%{majorCode.id}" /></td>
		</tr>
		<tr>
			<td class="bluebox" colspan="2" style="text-align: center">OR</td>
		</tr>
		<tr>
			<td class="greybox"><s:text name="budgetgroup.mincode" /></td>
			<td class="greybox"><s:select name="minCode" id="minCode"
					list="dropdownData.minCodeList" listKey="id"
					listValue="%{glcode+'----'+name}" headerKey="-1"
					headerValue="----Select----" value="%{minCode.id}" /></td>
		</tr>
		<tr>
			<td class="bluebox"><s:text name="budgetgroup.maxcode" /></td>
			<td class="bluebox"><s:select name="maxCode" id="maxCode"
					list="dropdownData.maxCodeList" listKey="id"
					listValue="%{glcode+'----'+name}" headerKey="-1"
					headerValue="----Select----" value="%{maxCode.id}" /></td>
		</tr>
		<tr>
			<td class="greybox"><s:text name="budgetgroup.description" /></td>
			<td class="greybox"><s:textarea name="description"
					id="description" cols="50" rows="5" /><span class="highlight2">Max.
					250 characters</span></td>
		</tr>
		<tr>
			<td class="bluebox"><s:text name="budgetgroup.isactive" /></td>
			<td class="bluebox"><s:checkbox name="isActive" id="isActive" /></td>
		</tr>
	</s:push>
</table>
