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
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox" width="20%"><strong><s:text
					name="fund.code" /><span class="mandatory"></span></strong></td>
		<td class="bluebox"><s:textfield id="code" name="code"
				value="%{code}" /></td>
		<td class="bluebox" width="20%"><strong><s:text
					name="fund.name" /><span class="mandatory"></span></strong></td>
		<td class="bluebox"><s:textfield id="name" name="name"
				value="%{name}" /></td>
	</tr>
	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox" width="20%"><strong><s:text
					name="fund.parent" /></strong></td>
		<td class="greybox"><s:select list="dropdownData.fundList"
				id="fund.fund.id" listKey="id" listValue="name" name="fund.fund.id"
				headerKey="" headerValue="---- Choose ----" value="fund.id"></s:select>
		</td>
		<td class="greybox" width="20%"><strong><s:text
					name="fund.identifier" /></strong></td>
		<td class="greybox"><s:textfield id="identifier"
				name="identifier" value="%{identifier}" maxlength="1" /></td>
	</tr>
	<tr>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox" width="20%"><strong><s:text
					name="fund.interFTrnfrCode" /></strong></td>
		<td class="bluebox"><s:hidden
				name="chartofaccountsByPayglcodeid.id"
				id="chartofaccountsByPayglcodeid.id" /> <s:textfield
				id="chartofaccountsByPayglcodeid.name"
				name="chartofaccountsByPayglcodeid.name"
				value="%{chartofaccountsByPayglcodeid.name}" /> <IMG id=IMG1
			onclick="openSearch(this,'12')" height=22
			src="/egi/resources/erp2/images/plus1.gif" width=25 align=top
			border=0></td>
		<td class="bluebox" width="20%"><strong><s:text
					name="fund.active" /></strong></td>
		<td class="bluebox"><s:checkbox name="isactive" id="isactive" />
		</td>
	</tr>
</table>
