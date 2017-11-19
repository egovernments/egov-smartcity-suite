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


<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox" width="20%"><strong><s:text
					name="subscheme.form.scheme" /><span class="mandatory1"> *</span></strong></td>
		<td class="bluebox"><s:select list="dropdownData.schemeList"
				listKey="id" listValue="name" id="scheme" name="scheme"
				headerKey="0" headerValue="--- Select ---" value="%{scheme.id}"></s:select>
		</td>
		<td class="bluebox" width="20%"><strong><s:text
					name="subscheme.form.name" /><span class="mandatory1"> *</span></strong></td>
		<td class="bluebox"><s:textfield id="name" name="name"
				value="%{name}" cssStyle="width: 250px" /></td>
	</tr>
	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><strong><s:text
					name="subscheme.form.code" /></strong><span class="mandatory1"> *</span></td>
		<td class="greybox"><s:textfield id="code" name="code"
				value="%{code}" /></td>
		<td class="greybox"><strong><s:text
					name="subscheme.form.validfrom" /></strong><span class="mandatory1">
				*</span></td>
		<td class="greybox"><input type="text" id="validfrom"
			name="validfrom" style="width: 100px"
			value='<s:date name="validfrom" format="dd/MM/yyyy"/>'
			onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
			href="javascript:show_calendar('subSchemeForm.validfrom');"
			id="calendar0" style="text-decoration: none">&nbsp;<img
				src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a></td>
	</tr>
	<tr>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox"><strong><s:text
					name="subscheme.form.validto" /></strong><span class="mandatory1"> *</span></td>
		<td class="bluebox"><input type="text" id="validto"
			name="validto" style="width: 100px"
			value='<s:date name="validto" format="dd/MM/yyyy"/>'
			onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
			href="javascript:show_calendar('subSchemeForm.validto');"
			id="calendar1" style="text-decoration: none">&nbsp;<img
				src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a></td>
		<td class="bluebox"><strong><s:text
					name="subscheme.form.isactive" /></strong></td>
		<td class="bluebox"><s:checkbox name="isActive" id="isActive" /></td>
	</tr>
	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><strong><s:text
					name="subscheme.form.department" /></strong></td>
		<td class="greybox"><s:select list="dropdownData.departmentList"
				listKey="id" listValue="name" headerKey="0"
				headerValue="--- Select ---" name="department" id="department"
				value="%{department.id}"></s:select></td>
		<td class="greybox"><strong><s:text
					name="subscheme.form.initialestimate" /></strong></td>
		<td class="greybox"><s:textfield cssStyle="text-align: right;"
				id="initialEstimateAmount" name="initialEstimateAmount"
				value="%{initialEstimateAmount}" /></td>
	</tr>
	<tr>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox"><strong><s:text
					name="subscheme.form.councilloanproposalnumber" /></strong></td>
		<td class="bluebox"><s:textfield id="councilLoanProposalNumber"
				name="councilLoanProposalNumber"
				value="%{councilLoanProposalNumber}" /></td>
		<td class="bluebox"><strong><s:text
					name="subscheme.form.councilloanproposaldate" /></strong></td>
		<td class="bluebox"><input type="text"
			id="councilLoanProposalDate" name="councilLoanProposalDate"
			style="width: 100px"
			value='<s:date name="councilLoanProposalDate" format="dd/MM/yyyy"/>'
			onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
			href="javascript:show_calendar('subSchemeForm.councilLoanProposalDate');"
			id="calendar2" style="text-decoration: none">&nbsp;<img
				src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a></td>
	</tr>
	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><strong><s:text
					name="subscheme.form.counciladminsanctionednumber" /></strong></td>
		<td class="greybox"><s:textfield id="councilAdminSanctionNumber"
				name="councilAdminSanctionNumber"
				value="%{councilAdminSanctionNumber}" /></td>
		<td class="greybox"><strong><s:text
					name="subscheme.form.counciladminsanctioneddate" /></strong></td>
		<td class="greybox"><input type="text"
			id="councilAdminSanctionDate" name="councilAdminSanctionDate"
			style="width: 100px"
			value='<s:date name="councilAdminSanctionDate" format="dd/MM/yyyy"/>'
			onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
			href="javascript:show_calendar('subSchemeForm.councilAdminSanctionDate');"
			id="calendar3" style="text-decoration: none">&nbsp;<img
				src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a></td>
	</tr>
	<tr>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox"><strong><s:text
					name="subscheme.form.governmentloanproposalnumber" /></strong></td>
		<td class="bluebox"><s:textfield id="govtLoanProposalNumber"
				name="govtLoanProposalNumber" value="%{govtLoanProposalNumber}" /></td>
		<td class="bluebox"><strong><s:text
					name="subscheme.form.governmentloanproposaldate" /></strong></td>
		<td class="bluebox"><input type="text" id="govtLoanProposalDate"
			name="govtLoanProposalDate" style="width: 100px"
			value='<s:date name="govtLoanProposalDate" format="dd/MM/yyyy"/>'
			onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
			href="javascript:show_calendar('subSchemeForm.govtLoanProposalDate');"
			id="calendar4" style="text-decoration: none">&nbsp;<img
				src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a></td>
	</tr>
	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><strong><s:text
					name="subscheme.form.governmentadminsanctionnumber" /></strong></td>
		<td class="greybox"><s:textfield id="govtAdminSanctionNumber"
				name="govtAdminSanctionNumber" value="%{govtAdminSanctionNumber}" /></td>
		<td class="greybox"><strong><s:text
					name="subscheme.form.governmentadminsanctiondate" /></strong></td>
		<td class="greybox"><input type="text" id="govtAdminSanctionDate"
			name="govtAdminSanctionDate" style="width: 100px"
			value='<s:date name="govtAdminSanctionDate" format="dd/MM/yyyy"/>'
			onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
			href="javascript:show_calendar('subSchemeForm.govtAdminSanctionDate');"
			id="calendar5" style="text-decoration: none">&nbsp;<img
				src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a></td>
	</tr>
</table>
