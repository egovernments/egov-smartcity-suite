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


<html>
<head>
<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/voucherHelper.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}"></script>

</head>
<body onload="onloadTask();">
	<s:form action="paymentReversal" theme="simple" name="cbtcform">
		<s:push value="model">
			<jsp:include page="../budget/budgetHeader.jsp">
				<jsp:param value="Reverse Payment" name="heading" />
			</jsp:include>
			<div class="formmainbox">
				<div class="formheading" />
				<div class="subheadnew">Payment Details</div>
				<div id="listid" style="display: block">
					<br />
				</div>
				<div align="center">
					<font style='color: red;'><p class="error-block"
							id="lblError"></p></font> <span class="mandatory"> <s:actionerror
							id="actionerror" /> <s:fielderror id="fielderror" /> <s:actionmessage
							id="actionmessage" />
					</span>
				</div>

				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="9%" class="bluebox">&nbsp;</td>
						<s:if test="%{shouldShowHeaderField('fund')}">
							<td width="12%" class="bluebox"><strong><s:text
										name="voucher.fund" /></strong></td>
							<td width="20%" class="bluebox"><s:property
									value="paymentHeader.voucherheader.fundId.name" /></td>
						</s:if>
						<s:if test="%{shouldShowHeaderField('fundsource')}">
							<td width="17%" class="bluebox"><strong><s:text
										name="voucher.fundsource" /></strong></td>
							<td width="33%" class="bluebox"><s:property
									value="paymentHeader.voucherheader.fundsourceId.name" /></td>
						</s:if>
					</tr>
					<tr>
						<td class="greybox">&nbsp;</td>
						<s:if test="%{shouldShowHeaderField('department')}">
							<td class="greybox"><strong><s:text
										name="voucher.department" /></strong></td>
							<td class="greybox"><s:property
									value="paymentHeader.voucherheader.vouchermis.departmentid.deptName" /></td>
						</s:if>
						<s:if test="%{shouldShowHeaderField('functionary')}">
							<td class="greybox"><strong><s:text
										name="voucher.functionary" /></strong></td>
							<td class="greybox" colspan="2"><s:property
									value="paymentHeader.voucherheader.vouchermis.functionary.name" /></td>
						</s:if>
					</tr>
					<tr>
						<td class="bluebox">&nbsp;</td>
						<s:if test="%{shouldShowHeaderField('scheme')}">
							<td class="bluebox"><strong><s:text
										name="voucher.scheme" /></strong></td>
							<td class="bluebox"><s:property
									value="paymentHeader.voucherheader.vouchermis.schemeid.name" /></td>
						</s:if>
						<s:if test="%{shouldShowHeaderField('subscheme')}">
							<td class="bluebox"><strong><s:text
										name="voucher.subscheme" /></strong></td>
							<td class="bluebox"><s:property
									value="paymentHeader.voucherheader.vouchermis.subschemeid.name" /></td>
						</s:if>
					</tr>
					<tr>
						<td class="greybox">&nbsp;</td>
						<s:if test="%{shouldShowHeaderField('field')}">
							<td class="greybox"><strong><s:text
										name="voucher.field" /></strong></td>
							<td class="greybox" colspan="4"><s:property
									value="paymentHeader.voucherheader.vouchermis.divisionid.name" /></td>
						</s:if>
					</tr>
					<tr>
						<td class="bluebox">&nbsp;</td>
						<td class="bluebox"><strong><s:text
									name="payment.voucherno" /></strong></td>
						<td class="bluebox"><s:property
								value="paymentHeader.voucherheader.voucherNumber" /></td>
						<td class="bluebox"><strong><s:text
									name="payment.voucherdate" /></strong></td>
						<td class="bluebox"><s:date
								name="paymentHeader.voucherheader.voucherDate"
								format="dd/MM/yyyy" /></td>
					</tr>
					<tr>
						<td class="greybox">&nbsp;</td>
						<td class="greybox"><strong><s:text
									name="payment.bank" /></strong></td>
						<td class="greybox"><s:property
								value="paymentHeader.bankaccount.bankbranch.bank.name" />-<s:property
								value="paymentHeader.bankaccount.bankbranch.branchname" /></td>
						<td class="greybox"><strong><s:text
									name="payment.bankaccount" /></strong></td>
						<td class="greybox" colspan="2"><s:property
								value="paymentHeader.bankaccount.accountnumber" /></td>
					</tr>
					<tr>
						<td class="bluebox">&nbsp;</td>
						<td class="bluebox" width="15%"><strong><s:text
									name="payment.narration" /></strong></td>
						<td class="bluebox" colspan="4"><s:property
								value="voucherHeader.description" /></td>
					</tr>
					<tr>
						<td class="greybox">&nbsp;</td>
						<td class="greybox"><strong><s:text
									name="payment.mode" /></strong></td>
						<td class="greybox"><s:property value="paymentHeader.type" /></td>
						<td class="greybox"><strong><s:text
									name="payment.amount" /></strong></td>
						<td class="greybox" colspan="2"><s:text name="format.number">
								<s:param value="%{paymentHeader.voucherheader.totalAmount}" />
							</s:text></td>
					</tr>
					<tr>
						<td class="bluebox">&nbsp;</td>
						<td class="bluebox"><strong>Comments</strong></td>
						<td class="bluebox" colspan="4"><s:textarea name="comments"
								id="comments" cols="100" rows="3" onblur="checkLength(this)" /></td>
					</tr>
				</table>
				<tr>
					<s:if test="%{shouldShowHeaderField('vouchernumber')}">
						<td class="bluebox"><s:text name="reversalVoucherNumber" /><span
							class="mandatory">*</span></td>
						<td class="bluebox"><s:textfield name="reverseVoucherNumber"
								id="reversalVoucherNumber" /></td>
					</s:if>
					<td class="bluebox"><s:text name="reversalVoucherDate" /><span
						class="mandatory">*</span></td>
					<td class="bluebox"><s:textfield name="reverseVoucherDate"
							id="reversalVoucherDate"
							onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
						href="javascript:show_calendar('cbtcform.reversalVoucherDate');"
						style="text-decoration: none">&nbsp;<img tabIndex="-1"
							src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></A>(dd/mm/yyyy)</td>
				</tr>
		</s:push>
		<br />
		<br />
		<input type="hidden" name="voucherHeader.id"
			value='<s:property value="paymentHeader.voucherheader.id"/>'
			id="voucherHeaderId" />
		<div id="buttons">
			<s:submit type="submit" cssClass="buttonsubmit"
				value="Reverse and View" method="saveReverse" id="reverse"
				onclick="return validate();" />
			<s:submit type="submit" cssClass="buttonsubmit"
				value="Reverse and Close" method="saveReverseAndClose" id="reverse"
				onclick="return validate();" />
			<s:submit value="Close" onclick="javascript: self.close()"
				id="button2" cssClass="button" />
		</div>
		<div id="resultGrid"></div>
		</div>
	</s:form>
	<SCRIPT type="text/javascript">
function onloadTask(){
	var message = '<s:property value="message"/>';
	var voucherHeaderId = <s:property value="voucherHeader.id"/>;
	if(message != ''){
		bootbox.alert(message);
		document.forms[0].action = "${pageContext.request.contextPath}/voucher/preApprovedVoucher!loadvoucherview.action?vhid="+voucherHeaderId;
		document.forms[0].submit();
	}
	var close = '<s:property value="close"/>';
	if(close == 'true'){
		self.close();
	}
}
function validate(){
	document.getElementById('lblError').innerHTML = "";
		
	if(document.getElementById('reversalVoucherDate').value == ''){
		document.getElementById('lblError').innerHTML = "Please enter Reversal Voucher Date";
		return false;
	}
	if(document.getElementById('reversalVoucherNumber') != undefined && document.getElementById('reversalVoucherNumber').value == ''){
		document.getElementById('lblError').innerHTML = "Please enter Reversal Voucher Number";
		return false;
	}
}
</SCRIPT>
</body>
</html>
