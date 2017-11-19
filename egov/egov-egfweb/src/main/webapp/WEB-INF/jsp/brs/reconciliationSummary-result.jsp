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
<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>
<head>

<title>Insert title here</title>
<style type="text/css">
@media print {
	input#Close {
		display: none;
	}
}

@media print {
	input#button1 {
		display: none;
	}
}
</style>
<link rel="stylesheet" type="text/css"
	href="/EGF/resources/css/jquery-ui/css/smoothness/jquery-ui-1.8.4.custom.css" />
<SCRIPT type="text/javascript"
	src="../resources/javascript/calendar.js?rnd=${app_release_no}"
	type="text/javascript"></SCRIPT>
</head>
<body>
	<div class="formmainbox">
		<div class="subheadnew">
			<s:text name="Reconciliation Summary" />
		</div>
		<s:form name="brsDetails" action="brsDetails" theme="simple">

			<%-- <s:iterator value="bankReconList" status="stat" var="p">  --%>
			<table width="99%" border="0" cellspacing="0" cellpadding="0">

				<tr>

				</tr>
				<tr>
					<td colspan="4"></td>
				</tr>
				<tr>
					<td style="width: 5%"></td>
					<td class="bluebox">Bank <span class="bluebox"><span
							class="mandatory1">*</span></span></td>
					<td class="bluebox"><s:textfield name="bank" id="bank"
							readonly="true" /></td>
					<td class="bluebox">Branch <span class="bluebox"><span
							class="mandatory1">*</span></span></td>
					<td class="bluebox"><s:textfield name="branch" id="branch"
							readonly="true" /></td>


					</td>
				</tr>


				<tr>
					<td style="width: 5%"></td>
					<td class="bluebox">Account Number:<span class="bluebox"><span
							class="mandatory1">*</span></span></td>
					<td class="bluebox"><s:textfield name="accountNum"
							id="accountNum" readonly="true" /></td>
					<td class="bluebox">Bank Statement Balance:<span
						class="bluebox"><span class="mandatory1">*</span></span></td>
					<td class="bluebox"><s:textfield name="balanceAsPerStatement"
							id="balanceAsPerStatement" readonly="true" /></td>

				</tr>

				<tr>
					<td style="width: 5%"></td>
					<td class="bluebox">Bank Statement Date:<span class="bluebox"><span
							class="mandatory1">*</span></span></td>
					<s:date name="bankStmtDate" format="dd/MM/yyyy" var="formtDate" />
					<td class="greybox"><s:textfield name="bankStmtDate"
							id="bankStmtDate" readonly="true" cssStyle="width:100px"
							value='%{formtDate}'
							onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
						href="javascript:show_calendar('bankReconciliation.bankStmtDate');"
						style="text-decoration: none"></a><br /></td>
				</tr>
			</table>
			<br />
			<br />
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				class="tablebottom">

				<tr>
					<td colspan=3 width="70%" class="blueborderfortd"
						style="font-weight: bold;"><div align="center">Particulars</div></td>
					<td width="15%" class="blueborderfortd" style="font-weight: bold;"><div
							align="center">Amount (Rs)</div></td>
					<td width="15%" class="blueborderfortd" style="font-weight: bold;"><div
							align="center">Amount (Rs)</div></td>
				</tr>

				<tr>
					<td colspan=3 class="blueborderfortd" valign="center"
						style="font-weight: bold;">&nbsp;&nbsp;&nbsp;Balance as per
						Bank Book</td>
					<td class="blueborderfortd" align="right"></td>
					<td class="blueborderfortd" align="right"><div align="right">
							<s:property value="accountBalance" />
						</div></td>
				</tr>
				<tr>
					<td colspan=3 class=blueborderfortd valign="center">&nbsp;&nbsp;&nbsp;Add:
						Cheques/DD issued but not presented in bank</td>
					<td class="blueborderfortd"><div align="right"
							name="addAmountDebit" id="addAmountDebit" readOnly>&nbsp;</div></td>
					<td class="blueborderfortd"><div align="right"
							name="addAmountCredit" id="addAmountCredit" readOnly>
							<s:property value="unReconciledCr" />
						</div></td>
				</tr>

				<tr>
					<td colspan=3 class="blueborderfortd" valign="center">&nbsp;&nbsp;&nbsp;Add:
						Other instruments issued but not presented in bank</td>
					<td class="blueborderfortd"><div align="right"
							name="addAmountDebit" id="addAmountDebit" readOnly>&nbsp;</div></td>
					<td class="blueborderfortd"><div align="right"
							name="addAmountCredit" id="addAmountCredit" readOnly>
							<s:property value="unReconciledCrOthers" />
						</div></td>
				</tr>
				<tr>
					<td colspan=3 class="blueborderfortd" valign="center">&nbsp;&nbsp;&nbsp;Add:
						Credit given by Bank either for interest or for any other account<br>&nbsp;&nbsp;&nbsp;
						but not accounted for in Bank Book
					</td>
					<td class="blueborderfortd"><div align="right"
							name="addOthersAmountDebit" id="addOthersAmountDebit" readOnly>&nbsp;</div></td>
					<td class="blueborderfortd"><div align="right"
							name="addOthersAmountCredit" id="addOthersAmountCredit" readOnly>
							<s:property value="unReconciledCrBrsEntry" />
						</div></td>
				</tr>

				<tr>
					<td colspan=3 class="blueborderfortd" align="middle"
						valign="center" style="font-weight: bold;"><div
							align="center">
							<i>Sub-total</i>
						</div></td>
					<td class="blueborderfortd"><div align="right"
							name="subTotalAmountDebit" id="subTotalAmountDebit" readOnly>&nbsp;</div></td>
					<td class="blueborderfortd"><div align="right"
							name="subTotalAmountCredit" id="subTotalAmountCredit" readOnly>
							<s:property value="subTotal" />
						</div></td>
				</tr>
				<tr>
					<td colspan=3 class="blueborderfortd" valign="center">&nbsp;&nbsp;&nbsp;
						Less: Cheques Deposited but not cleared</td>
					<td class="blueborderfortd"><div align="right"
							name="lessAmountDebit" id="lessAmountDebit" readOnly>&nbsp;</div></td>
					<td class="blueborderfortd"><div align="right"
							name="lessAmountCredit" id="lessAmountCredit" readOnly>
							<s:property value="unReconciledDr" />
						</div></td>
				</tr>

				<tr>
					<td colspan=3 class="blueborderfortd" valign="center">&nbsp;&nbsp;&nbsp;
						Less: Other Instruments Deposited but not cleared</td>
					<td class="blueborderfortd"><div align="right"
							name="lessAmountDebit" id="lessAmountDebit" readOnly>&nbsp;</div></td>
					<td class="blueborderfortd"><div align="right"
							name="lessAmountCredit" id="lessAmountCredit" readOnly>
							<s:property value="unReconciledDrOthers" />
						</div></td>
				</tr>
				<tr>
					<td colspan=3 class="blueborderfortd" valign="center">
						&nbsp;&nbsp;&nbsp; Less: Service Charges / Bank Charges or any
						other charge levied by<br> &nbsp;&nbsp;&nbsp;&nbsp;the Bank
						but not accounted for Bank book
					</td>
					<td class="blueborderfortd"><div align="right"
							name="lessOthersAmountDebit" id="lessOthersAmountDebit" readOnly>&nbsp;</div></td>
					<td class="blueborderfortd"><div align="right"
							name="lessOthersAmountCredit" id="lessOthersAmountCredit"
							readOnly>
							<s:property value="unReconciledDrBrsEntry" />
						</div></td>
				</tr>



				<tr>
					<td colspan=3 class="blueborderfortd" style="font-weight: bold;"><div
							align="center">
							<i>Net-total</i>
						</div></td>
					<td class="blueborderfortd"><div align="right"
							name="totalAmountDebit" id="totalAmountDebit" readOnly>&nbsp;</div></td>
					<td class="blueborderfortd"><div align="right"
							name="totalAmountCredit" id="totalAmountCredit" readOnly>
							<div align="right">
								<s:property value="netTotal" />
							</div></td>
				</tr>
				<tr>
					<td colspan=3 class="blueborderfortd" valign="center"
						style="font-weight: bold;">&nbsp;&nbsp;&nbsp; Bank Balance as
						per Bank Statement</td>
					<td class="blueborderfortd"><div align="right"
							name="bankBalanceDebit" id="bankBalanceDebit" readOnly>&nbsp;</div></td>
					<td class="blueborderfortd"><div align="right"
							name="bankBalanceCredit" id="bankBalanceCredit" readOnly>
							<s:property value="balanceAsPerStatement" />
						</div></td>
				</tr>

			</table>
			<%-- </s:iterator>  --%>

			<br>
			<br>
			<table align=center>
				<tr id="hideRow1" class="row1">

					<td><input type="button" id="Close" value="Close"
						onclick="javascript:window.close()" class="button" /></td>
					<td><input name="button" type="button" class="buttonsubmit"
						id="button1" value="Print" onclick="window.print()" />&nbsp;</td>
				</tr>
			</table>


		</s:form>
	</div>
</body>
</html>