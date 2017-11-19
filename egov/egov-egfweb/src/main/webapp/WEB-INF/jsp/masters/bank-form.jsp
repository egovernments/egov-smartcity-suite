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

<div style="color: red">
	<s:actionerror />
	<s:fielderror />
</div>
<div style="color: green">
	<s:actionmessage />
</div>

<s:form name="bankForm" action="bank" theme="simple">
	<s:token name="%{tokenName()}" />
	<s:push value="model">
		<s:hidden id="bank_id" name="id" />
		<s:hidden id="created" name="created" />
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="bank.create.name" /><span
					class="mandatory1"> *</span></td>
				<s:if test="%{mode.equals('VIEW')}">
					<td class="bluebox"><s:textfield id="name" name="name"
							readonly="true" /></td>
				</s:if>
				<s:else>
					<td class="bluebox"><s:textfield id="name" name="name"
							onblur="checkUniqueBankName(this);" /> <span
						style="display: none; color: red" id="nameuniquename"> <s:text
								name="bank.name.already.exists" />
					</span></td>

				</s:else>

			</tr>
			<tr>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox"><s:text name="bank.create.code" /><span
					class="mandatory1"> *</span></td>
				<s:if test="%{mode.equals('VIEW')}">
					<td class="bluebox"><s:textfield id="code" name="code"
							readonly="true" /></td>
				</s:if>
				<s:else>
					<td class="bluebox"><s:textfield id="code" name="code"
							onblur="checkUniqueBankCode(this);" /> <span
						style="display: none; color: red" id="codeuniquecode"> <s:text
								name="bank.code.already.exists" />
					</span></td>

				</s:else>
			</tr>
			<tr>
				<td class="bluebox"></td>
				<td class="bluebox"><s:text name="bank.create.isactive" /></td>
				<s:if test="%{mode.equals('VIEW')}">
					<td class="bluebox" colspan="2"><s:checkbox id="isActive"
							name="isActive" value="%{isActive}" disabled="disabled"
							checked="checked" /></td>
				</s:if>
				<s:else>
					<td class="bluebox" colspan="2"><s:checkbox id="isActive"
							name="isActive" value="%{isActive}" /></td>

				</s:else>
			</tr>

			<tr>
				<td class="greybox"></td>
				<td class="greybox"><s:text name="bank.create.remarks" /></td>
				<s:if test="%{mode.equals('VIEW')}">
					<td class="greybox" colspan="2"><s:textarea id="narration"
							name="narration" cols="80" rows="5" readonly="true" /></td>
				</s:if>
				<s:else>
					<td class="greybox" colspan="2"><s:textarea id="narration"
							name="narration" cols="80" rows="5" /></td>

				</s:else>
			</tr>
		</table>
		<br />
		<s:if test="%{id != null}">
			<div class="center">
				<table id="listsg11"></table>
				<div id="pagersg11"></div>
			</div>
		</s:if>
		<br />
	</s:push>


</s:form>
<script src="../resources/javascript/bank.js" type="text/javascript"></script>