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
<%!
       int count = -1;
   %>
<tr>
	<td style="width: 5%"></td>
	<s:if test="%{shouldShowHeaderField('fund')}">
		<% count++; %>
		<td class="bluebox"><s:text name="voucher.fund" /> <s:if
				test="%{isFieldMandatory('fund')}">
				<span class="mandatory1" id="disableFundCheck">*</span>
			</s:if></td>
		<td class="bluebox"><s:select name="fundId" id="fundId"
				list="dropdownData.fundList" listKey="id" listValue="name"
				headerKey="-1" headerValue="----Choose----"
				onChange="getSchemelist(this)" value="%{fundId.id}" /></td>
	</s:if>
	<%if(count % 2 == 1) {%>
</tr>
<tr>
<td style="width: 5%"></td>
	<%} %>


	<s:if test="%{shouldShowHeaderField('fundsource')}">
		<% count++; %>
		<td class="bluebox"><s:text name="voucher.fundsource" /> <s:if
				test="%{isFieldMandatory('fundsource')}">
				<span class="mandatory1">*</span>
			</s:if></td>
		<td class="bluebox"><s:select name="fundsourceId"
				id="fundsourceId" list="dropdownData.fundsourceList" listKey="id"
				listValue="name" headerKey="-1" headerValue="----Choose----"
				value="%{vouchermis.fundsource.id}" /></td>
	</s:if>



	<%if(count % 2 == 1) {%>
</tr>
<tr>
<td style="width: 5%"></td>
	<%} %>

	<s:if test="%{shouldShowHeaderField('scheme')}">
		<% count++; %>
		<egov:ajaxdropdown id="schemeid" fields="['Text','Value']"
			dropdownId="schemeid" url="voucher/common-ajaxLoadSchemes.action" />
		<td class="greybox"><s:text name="voucher.scheme" /> <s:if
				test="%{isFieldMandatory('scheme')}">
				<span class="mandatory1">*</span>
			</s:if></td>
		<td class="greybox"><s:select name="vouchermis.schemeid"
				id="schemeid" list="dropdownData.schemeList" listKey="id"
				listValue="name" headerKey="-1" headerValue="----Choose----"
				onChange="getSubSchemelist(this)" value="%{vouchermis.schemeid.id}" /></td>
	</s:if>


	<%if(count % 2 == 1) {%>
</tr>
<tr>
<td style="width: 5%"></td>
	<%} %>
	<s:if test="%{shouldShowHeaderField('subscheme')}">
		<% count++; %>
		<egov:ajaxdropdown id="subschemeid" fields="['Text','Value']"
			dropdownId="subschemeid"
			url="voucher/common-ajaxLoadSubSchemes.action" />
		<td class="greybox"><s:text name="voucher.subscheme" /> <s:if
				test="%{isFieldMandatory('subscheme')}">
				<span class="mandatory1">*</span>
			</s:if></td>
		<td class="greybox"><s:select name="vouchermis.subschemeid"
				id="subschemeid" list="dropdownData.subschemeList" listKey="id"
				listValue="name" headerKey="-1" headerValue="----Choose----"
				value="%{vouchermis.subschemeid.id}" /></td>
	</s:if>
	
	<%if(count % 2 == 1) {%>
</tr>
<tr>
<td style="width: 5%"></td>
	<%} %>
	<s:if test="%{shouldShowHeaderField('functionary')}">
		<% count++; %>
		<td class="bluebox"><s:text name="voucher.functionary" /> <s:if
				test="%{isFieldMandatory('functionary')}">
				<span class="mandatory1">*</span>
			</s:if></td>
		<td class="bluebox"><s:select name="vouchermis.functionary"
				id="vouchermis.functionary" list="dropdownData.functionaryList"
				listKey="id" listValue="name" headerKey="-1"
				headerValue="----Choose----" value="%{vouchermis.functionary.id}" /></td>
	</s:if>

	<%if(count % 2 == 1) {%>
</tr>
<tr>
<td style="width: 5%"></td>
	<%} %>
	<s:if test="%{shouldShowHeaderField('department')}">
		<% count++; %>
		<td class="bluebox"><s:text name="voucher.department" /> <s:if
				test="%{isFieldMandatory('department')}">
				<span class="mandatory1">*</span>
			</s:if></td>
		<td class="bluebox"><s:select name="vouchermis.departmentid"
				id="vouchermis.departmentid" list="dropdownData.departmentList"
				listKey="id" listValue="name" headerKey="-1"
				headerValue="----Choose----" value="%{vouchermis.departmentid.id}" /></td>
	</s:if>

	<%if(count % 2 == 1) {%>
</tr>
<tr>
<td style="width: 5%"></td>
	<%} %>
	<s:if test="%{shouldShowHeaderField('field')}">
		<% count++; %>
		<td class="greybox"><s:text name="voucher.field" /> <s:if
				test="%{isFieldMandatory('field')}">
				<span class="mandatory1">*</span>
			</s:if><br> <br></td>
		<td class="greybox"><s:select name="vouchermis.divisionid"
				id="vouchermis.divisionid" list="dropdownData.fieldList"
				listKey="id" listValue="name" headerKey="-1"
				headerValue="----Choose----" value="%{vouchermis.divisionid.id}" /></td>
	</s:if>

	<%if(count % 2 == 1) {%>
</tr>
<%} else {%>
<td></td>
<td></td>
</tr>
<%} %>




<script>
	function getSchemelist(obj)
	{
		if(document.getElementById('schemeid'))
			populateschemeid({fundId:obj.value});
	}
	function getSubSchemelist(obj)
	{
		if(document.getElementById('subschemeid'))
			populatesubschemeid({schemeId:obj.value});
	}
</script>
