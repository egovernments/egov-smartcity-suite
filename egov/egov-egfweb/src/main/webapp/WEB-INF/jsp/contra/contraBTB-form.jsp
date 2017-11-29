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


<tr>
	<td class="bluebox" colspan="5">
		<table width="100%" cellspacing="0" cellpadding="0" border="0">
			<tr>
				<th class="bluebgheadtd" width="100%" colspan="5"><strong
					style="font-size: 15px;"><s:text
							name="contra.fromBank.header" /></strong></th>
			</tr>
		</table>
	</td>
</tr>
<%@include file="../voucher/vouchertrans-filter-new.jsp"%>
<tr>
	<td class="greybox"></td>
	<egov:ajaxdropdown id="fromBankId" fields="['Text','Value']"
		dropdownId="fromBankId" url="/voucher/common-ajaxLoadBanks.action" />
	<td class="greybox"><s:text name="contra.fromBank" /> <span
		class="greybox"><span class="mandatory1">*</span></span></td>
	<s:hidden name="temp" value="contraBean.fromBankId" />
	<td class="greybox"><s:select name="contraBean.fromBankId"
			id="fromBankId" list="%{fromBankBranchMap}" headerKey="-1"
			headerValue="----Choose----" onChange="loadFromAccNum(this);" /></td>
	<egov:ajaxdropdown id="fromAccountNumber" fields="['Text','Value']"
		dropdownId="fromAccountNumber"
		url="/voucher/common-ajaxLoadAccountNumbers.action" />
	<td class="greybox"><s:text name="contra.fromBankAccount" /> <span
		class="greybox"><span class="mandatory1">*</span></span></td>
	<td class="greybox"><s:select name="contraBean.fromBankAccountId"
			value="%{contraBean.fromBankAccountId}" id="fromAccountNumber"
			list="dropdownData.fromAccNumList" listKey="id"
			listValue="accountnumber" headerKey="-1" headerValue="----Choose----"
			onChange="populatefromNarration(this);loadFromBalance(this)" /> <s:textfield
			name="fromAccnumnar" id="fromAccnumnar" value="%{fromAccnumnar}"
			readonly="true" tabindex="-1" /></td>
</tr>

<tr>
	<td class="bluebox"></td>
	<egov:updatevalues id="fromBankBalance" fields="['Text']"
		url="/payment/payment-ajaxGetAccountBalance.action" />
	<td class="bluebox"><s:text name="contra.fromBankBalance" />
		(Rs.) <span class="bluebox"><span class="mandatory1">*</span></span></td>
	<td class="bluebox"><s:textfield name="contraBean.fromBankBalance"
			id="fromBankBalance" readonly="true" tabindex="-1"
			cssStyle="text-align:right" /></td>
	<td class="bluebox"></td>
	<td class="bluebox"></td>
</tr>
<tr>
	<td class="bluebox" colspan="5">
		<table width="100%" cellspacing="0" cellpadding="0" border="0">
			<tr>
				<th class="bluebgheadtd" width="100%" colspan="5"><strong
					style="font-size: 15px;"><s:text
							name="contra.toBank.header" /></strong></th>
			</tr>
		</table>
	</td>
</tr>
<tr>
	<td class="greybox"></td>
	<s:if test="%{shouldShowHeaderField('fund')}">
		<td class="greybox"><s:text name="voucher.fund" /><span
			class="mandatory1">*</span></td>
		<td class="greybox"><s:select name="contraBean.toFundId"
				id="toFundId" list="dropdownData.fundList" listKey="id"
				listValue="name" onChange="loadToBank(this);checkInterFund();"
				headerKey="" headerValue="----Choose----" /></td>
	</s:if>
	<s:if test="%{shouldShowHeaderField('department')}">
		<td id="interFundRow1" style="visibility: hidden" class="greybox"><s:text
				name="voucher.department" /> <s:if
				test="%{isFieldMandatory('department')}">
				<span class="bluebox"><span class="mandatory1">*</span></span>
			</s:if></td>
		<td id="interFundRow2" style="visibility: hidden" class="greybox"><s:select
				name="contraBean.toDepartment" id="contraBean.toDepartment"
				list="dropdownData.departmentList" listKey="id" listValue="name"
				headerKey="" headerValue="----Choose----"
				value="voucherHeader.vouchermis.departmentid.id"
				onChange="populateApproverDept(this);" /></td>
	</s:if>
</tr>
<tr>
	<td class="bluebox"></td>
	<egov:ajaxdropdown id="toBankId" fields="['Text','Value']"
		dropdownId="toBankId" url="/voucher/common-ajaxLoadBanks.action" />

	<td class="bluebox"><s:text name="contra.toBank" /> <span
		class="bluebox"><span class="mandatory1">*</span></span></td>
	<td class="bluebox"><s:select name="contraBean.toBankId"
			id="toBankId" list="%{toBankBranchMap}" headerKey="-1"
			headerValue="----Choose----" onChange="loadToAccNum(this);" /></td>
	<egov:ajaxdropdown id="toAccountNumber" fields="['Text','Value']"
		dropdownId="toAccountNumber"
		url="/voucher/common-ajaxLoadAccountNumbers.action" />
	<td class="bluebox"><s:text name="contra.toBankAccount" /> <span
		class="bluebox"><span class="mandatory1">*</span></span></td>
	<td class="bluebox"><s:select name="contraBean.toBankAccountId"
			id="toAccountNumber" list="dropdownData.toAccNumList" listKey="id"
			listValue="accountnumber" headerKey="-1" headerValue="----Choose----"
			onChange="populatetoNarration(this);loadToBalance(this)" /> <s:textfield
			name="toAccnumnar" id="toAccnumnar" value="%{toAccnumnar}"
			readonly="true" tabindex="-1" /></td>
</tr>

<tr>
	<td class="greybox"></td>
	<egov:updatevalues id="toBankBalance" fields="['Text']"
		url="/payment/payment-ajaxGetAccountBalance.action" />
	<td class="greybox"><s:text name="contra.toBankBalance" /> (Rs.)
		<span class="greybox"><span class="mandatory1">*</span></span></td>
	<td class="greybox"><s:textfield name="contraBean.toBankBalance"
			id="toBankBalance" readonly="true" tabindex="-1"
			cssStyle="text-align:right" /></td>
	<td class="greybox"></td>
	<td class="greybox"></td>
</tr>
<tr id="interFundRow3" style="visibility: hidden">
	<td class="greybox"></td>
	<td class="greybox"><s:text name="Source Inter Fund code" />
	<td class="greybox"><span class="mandatory1">*</span> <s:select
			name="contraBean.sourceGlcode" id="sourceGlcode"
			list="dropdownData.interFundList" listKey="glcode"
			listValue="glcode+'-'+name" headerKey="-1"
			headerValue="----Choose----" /></td>
	<td class="greybox"><s:text name="Destination Inter Fund code" /></td>
	<td class="greybox"><span class="mandatory1">*</span> <s:select
			name="contraBean.destinationGlcode" id="destinationGlcode"
			list="dropdownData.interFundList" listKey="glcode"
			listValue="glcode+'-'+name" headerKey="-1"
			headerValue="----Choose----" /></td>
</tr>


<tr>
	<td class="bluebox"></td>
	<td class="bluebox"><s:text name="contra.modeOfCollection" /> <span
		class="bluebox"><span class="mandatory1">*</span></span></td>
	<td class="bluebox"><s:radio name="contraBean.modeOfCollection"
			id="modeOfCollection" list="%{modeOfCollectionMap}"
			onclick="toggleChequeAndRefNumber(this)" checked="checked"/></td>
	<td class="bluebox"></td>
	<td class="bluebox"></td>
</tr>

<tr id="chequeGrid">
	<td class="greybox"></td>
	<td class="greybox"><span id="mdcNumber"><s:text
				name="contra.refNumber" /></span> <span class="greybox"><span
			class="mandatory1">*</span></span></td>
	<td class="greybox"><s:textfield name="contraBean.chequeNumber"
			id="chequeNum" value="%{contraBean.chequeNumber}" /></td>
	<td class="greybox"><span id="mdcDate"><s:text
				name="contra.refDate" /></span></td>
	<td class="greybox"><s:textfield id="chequeDate"
			name="contraBean.chequeDate" data-date-end-date="0d"
			onkeyup="DateFormat(this,this.value,event,false,'3')"
			placeholder="DD/MM/YYYY" class="form-control datepicker"
			data-inputmask="'mask': 'd/m/y'" /></td>

</tr>

<tr>
	<td class="bluebox"></td>
	<td class="bluebox"><s:text name="contra.amount" /> (Rs.) <span
		class="bluebox"><span class="mandatory1">*</span></span></td>
	<td class="bluebox"><s:textfield name="amount" id="amount"
			cssStyle="text-align:right" /></td>
	<td class="bluebox"></td>
	<td class="bluebox"></td>
</tr>

<tr>
	<td class="greybox"></td>
	<td class="greybox"><s:text name="voucher.narration" /></td>
	<td class="greybox" colspan="3"><s:textarea name="description"
			id="description" style="width:580px" /></td>
	<td class="greybox"></td>
	<td class="greybox"></td>
</tr>

<script>
	var fund_map = new Array();
	var i=0;
	<s:iterator var="f" value="%{dropdownData.fundList}" status="stat">
		fund_map[i++]= '<s:property value="%{id}"/>'+"_"+'<s:property value="%{chartofaccountsByPayglcodeid.glcode}"/>';
	</s:iterator>	
	
	</script>

