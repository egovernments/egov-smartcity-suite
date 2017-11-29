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


<table border="0" width="100%">
	<tr>
		<s:if
			test="%{shouldShowHeaderField('vouchernumber') || showMode.equalsIgnoreCase('reverse')}">
			<td class="greybox" width="30%"><s:text name="voucher.number" /><span
				class="mandatory">*</span></td>
			<td class="greybox"><s:textfield
					name="voucherHeader.voucherNumber" id="voucherNumber"
					maxlength="25" /></td>
		</s:if>
		<td class="greybox" width="30%"><s:text name="voucher.date" /><span
			class="mandatory">*</span></td>
		<td class="greybox"><input type="text" id="voucherDate"
			name="voucherHeader.voucherDate" style="width: 100px"
			value='<s:date name="voucherDate" format="dd/MM/yyyy"/>' /> <a
			href="javascript:show_calendar('cashWithDrawalForm.voucherDate',null,null,'DD/MM/YYYY');"
			style="text-decoration: none">&nbsp;<img tabIndex=-1
				src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)
		</td>
	</tr>
	<tr>
		<td class="bluebox" width="30%"><s:text name="payin.bank" /> <span
			class="bluebox"><span class="mandatory">*</span></span></td>
		<td class="bluebox"><s:select name="contraBean.bankBranchId"
				id="bankId" list="dropdownData.bankList" listKey="bankBranchId"
				listValue="bankBranchName" headerKey="-1"
				headerValue="----Choose----" onChange="populateAccNum(this);" /></td>
		<td class="bluebox" width="30%"><s:text name="contra.amount" /><span
			class="mandatory">*</span></td>
		<td class="bluebox"><s:textfield name="contraBean.amount"
				id="amount" onkeyup="validateAmountFormat()"
				cssStyle="text-align:right" /></td>

	</tr>
	<tr>
		<egov:ajaxdropdown id="accountNumber" fields="['Text','Value']"
			dropdownId="accountNumber" url="voucher/common!ajaxLoadAccNum.action" />
		<td class="greybox"><s:text name="payin.accountNum" /><span
			class="bluebox"><span class="mandatory">*</span></span></td>
		<td class="greybox"><s:select name="contraBean.accountNumberId"
				id="accountNumber" list="dropdownData.accNumList" listKey="id"
				listValue="accountnumber" headerKey="-1"
				headerValue="----Choose----"
				onChange="populateNarration(this);populateAvailableBalance(this);" />
			<s:textfield name="contraBean.accnumnar" id="accnumnar"
				value="%{contraBean.accnumnar}" /></td>
		<td class="greybox"><s:text name="balance.available" /></td>
		<td class="greybox"><input type="text" id="availableBalance"
			readonly="readonly" style="text-align: right" /></td>
	</tr>
	<tr>
		<td class="bluebox"><s:text name="cheque.date" /><span
			class="mandatory">*</span></td>
		<td class="bluebox"><input type="text" id="chequeDate"
			name="contraBean.chequeDate" style="width: 100px"
			value='<s:property value="contraBean.chequeDate"/>' /> <a
			href="javascript:show_calendar('cashWithDrawalForm.chequeDate',null,null,'DD/MM/YYYY');"
			style="text-decoration: none">&nbsp;<img tabIndex=-1
				src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)
		</td>
		<s:if test="%{showChequeNumber()}">
			<td class="bluebox"><s:text name="cheque.number" /><span
				class="greybox"><span class="mandatory">*</span></span></td>
			<td class="bluebox"><s:textfield name="contraBean.chequeNumber"
					id="chequeNumber" maxlength="25" />
		</s:if>
	</tr>
	<jsp:include page="../voucher/vouchertrans-filter.jsp" />
	<tr>
		<td class="greybox">Narration &nbsp;</td>
		<td class="greybox"><s:textarea rows="4" cols="60"
				name="narration" onkeydown="textCounter('narration',250)"
				onkeyup="textCounter('narration',250)"
				onblur="textCounter('narration',250)" id="narration" /></td>
		<td class="greybox"><s:text name="contra.cashInHand" /></td>
		<td class="greybox"><s:textfield name="contraBean.cashInHand"
				id="cashInHand" readonly="true" /></td>
	</tr>
</table>
