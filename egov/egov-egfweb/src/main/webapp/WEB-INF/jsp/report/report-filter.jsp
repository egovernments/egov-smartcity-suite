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


<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<tr>
	<s:if test="%{shouldShowHeaderField('fund')}">
		<td class="bluebox"><s:text name="voucher.fund" /> <s:if
				test="%{isFieldMandatory('fund')}">
				<span class="mandatory">*</span>
			</s:if></td>
		<td class="bluebox"><s:select name="fund" id="fund"
				list="dropdownData.fundList" listKey="id" listValue="name"
				headerKey="-1" headerValue="----Choose----"
				onChange="getSchemelist(this)" value="%{fund.id}" /></td>
	</s:if>
	<s:if test="%{shouldShowHeaderField('department')}">
		<td class="bluebox"><s:text name="voucher.department" /> <s:if
				test="%{isFieldMandatory('department')}">
				<span class="mandatory">*</span>
			</s:if></td>
		<td class="bluebox"><s:select name="department" id="department"
				list="dropdownData.departmentList" listKey="id" listValue="deptName"
				headerKey="-1" headerValue="----Choose----" value="%{department.id}" /></td>
	</s:if>
</tr>
<tr>
	<s:if test="%{shouldShowHeaderField('scheme')}">
		<egov:ajaxdropdown id="schemeid" fields="['Text','Value']"
			dropdownId="schemeid" url="report/report!ajaxLoadSchemes.action" />
		<td class="greybox"><s:text name="voucher.scheme" /> <s:if
				test="%{isFieldMandatory('scheme')}">
				<span class="mandatory">*</span>
			</s:if></td>
		<td class="greybox"><s:select name="scheme" id="scheme"
				list="dropdownData.schemeList" listKey="id" listValue="name"
				headerKey="-1" headerValue="----Choose----"
				onChange="getSubSchemelist(this)" value="%{scheme.id}" /></td>
	</s:if>
	<s:if test="%{shouldShowHeaderField('subscheme')}">
		<egov:ajaxdropdown id="subscheme" fields="['Text','Value']"
			dropdownId="subscheme" url="report/report!ajaxLoadSubSchemes.action" />
		<td class="greybox"><s:text name="voucher.subscheme" /> <s:if
				test="%{isFieldMandatory('subscheme')}">
				<span class="mandatory">*</span>
			</s:if></td>
		<td class="greybox"><s:select name="subscheme" id="subscheme"
				list="dropdownData.subschemeList" listKey="id" listValue="name"
				headerKey="-1" headerValue="----Choose----" value="%{subscheme.id}" /></td>
	</s:if>
</tr>
<tr>
	<s:if test="%{shouldShowHeaderField('functionary')}">
		<td class="bluebox"><s:text name="voucher.functionary" /> <s:if
				test="%{isFieldMandatory('functionary')}">
				<span class="mandatory">*</span>
			</s:if></td>
		<td class="bluebox"><s:select name="functionary" id="functionary"
				list="dropdownData.functionaryList" listKey="id" listValue="name"
				headerKey="-1" headerValue="----Choose----"
				value="%{functionary.id}" /></td>
	</s:if>
	<s:if test="%{shouldShowHeaderField('fundsource')}">
		<td class="bluebox"><s:text name="voucher.fundsource" /> <s:if
				test="%{isFieldMandatory('fundsource')}">
				<span class="mandatory">*</span>
			</s:if></td>
		<td class="bluebox"><s:select name="fundsource" id="fundsource"
				list="dropdownData.fundsourceList" listKey="id" listValue="name"
				headerKey="-1" headerValue="----Choose----" value="%{fundsource.id}" /></td>
	</s:if>
</tr>
<tr>
	<s:if test="%{shouldShowHeaderField('function')}">
		<td class="greybox"><s:text name="voucher.function" /> <s:if
				test="%{isFieldMandatory('function')}">
				<span class="mandatory">*</span>
			</s:if></td>
		<td class="greybox"><s:select name="function" id="function"
				list="dropdownData.functionList" listKey="id" listValue="name"
				headerKey="-1" headerValue="----Choose----" value="%{function.id}" /></td>
	</s:if>
	<s:if test="%{shouldShowHeaderField('field')}">
		<td class="greybox"><s:if test="%{isFieldMandatory('field')}">
				<span class="mandatory">*</span>
			</s:if><br>
		<br></td>
		<td class="greybox">&gt;<s:select name="field" id="field"
				list="dropdownData.fieldList" listKey="id" listValue="name"
				headerKey="-1" headerValue="----Choose----" value="%{field.id}" /></td>
	</s:if>
</tr>

<script>
	function getSchemelist(obj)
	{
		if(document.getElementById('scheme'))
			populatescheme({fund:obj.value})
	}
	function getSubSchemelist(obj)
	{
		if(document.getElementById('subscheme'))
			populatesubscheme({scheme:obj.value})
	}
</script>
