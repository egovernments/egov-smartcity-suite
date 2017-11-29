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


<jsp:include page="../budget/budgetHeader.jsp">
	<jsp:param name="heading" value='Fund Flow Analysis Report' />
</jsp:include>
<span class="mandatory"> <s:actionerror /> <s:fielderror /> <s:actionmessage />
</span>
<div class="subheadsmallnew">
	<span class="subheadnew"><s:property value="ulbName" /></span>
</div>
<table id="header" width="100%" cellpadding="0" cellspacing="0"
	border="0">
	<tr>
		<td class="greybox" width="15%"><s:text name="report.fund" /></td>
		<td class="greybox" width="34%"><s:select name="fund" id="fund"
				list="dropdownData.fundList" listKey="id" listValue="name"
				headerKey="" headerValue="-----choose----" /></td>
		<td class="greybox" width="15%"><s:text name="asondate" /><span
			class="mandatory">*</span></td>
		<td class="greybox" width="34%"><s:date name='asOnDate'
				var="asOnDateId" format='dd/MM/yyyy' /> <s:textfield name="asOnDate"
				id="asOnDate" value='%{asOnDateId}'
				onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
			href="javascript:show_calendar('fundFlowReport.asOnDate');"
			style="text-decoration: none">&nbsp;<img tabIndex="-1"
				src="/egi/resources/erp2/images/calendaricon.gif" border="0" />
		</A></td>
	</tr>
	<tr>


	</tr>
</table>
<div class="buttonbottom">
	<s:submit value="Search" method="search" id="search"
		cssClass="buttonsubmit" onclick="return validateFundFlow()" />
	<s:reset name="button" type="submit" cssClass="button" id="button"
		value="Cancel" />
	<s:submit value="Close" onclick="javascript: self.close()"
		cssClass="button" />
</div>

<br />

<s:if test="receiptList!=null && receiptList.size()>0">
	<div id="recaluclateButton" align="right" style="display: none">
		<s:submit value="Re Calculate" method="recalculateOpeningBalance"
			id="recalculate" cssClass="buttonsubmit"
			onclick=" return alertTheMessage();" />
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	</div>
	<table width="99%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td class="blueborderfortd">
				<div>
					<table title="RECEIPT BANK DETAILS" width="100%" cellpadding="0"
						cellspacing="0" border="0" class="tablebottom" id="receiptTable">
						<tr>
							<td colspan=8 align="center"><b>RECEIPT BANK DETAILS</b></td>
							<td align="right" border="0"><strong>(Rupees in
									Lakh)</strong></td>
						</tr>
						<tr>
							<th class="bluebgheadtd"><s:text name="bank" /></th>
							<th class="bluebgheadtd"><s:text name="Fund Name" /></th>
							<th class="bluebgheadtd"><s:text name="coa" /></th>
							<th class="bluebgheadtd"><s:text name="bankaccount" /></th>
							<th class="bluebgheadtd"><s:text name="openingbalance" />
							</th>
							<th class="bluebgheadtd"><s:text name="currentreceipt" />
							</th>
							<th class="bluebgheadtd"><s:text name="fundsavailable" />
							</th>
							<th class="bluebgheadtd"><s:text name="btbpayment" /></th>
							<th class="bluebgheadtd"><s:text name="btbreceipt" /></th>
							<th class="bluebgheadtd"><s:text name="closingbalance" />
							</th>
						</tr>
						<s:iterator id="t" var="fundFlowBean" value="receiptList"
							status="s">
							<s:if test="#s.odd">
								<s:set var="tdcss" value="'bluebox'" />
							</s:if>
							<s:else>
								<s:set var="tdcss" value="'greybox'" />
							</s:else>
							<s:if test="#s.first">
								<s:set var="tdcss" value="'greybox'" />
							</s:if>
							<s:set var="index" value="<s:property value='#s.index'/>" />
							<s:if test="%{receiptList[#s.index].accountNumber!='Total'}">
								<tr>
									<input type="hidden"
										name='receiptList[<s:property value='#s.index'/>].id'
										id="receiptList[<s:property value='#s.index'/>].id"
										value="<s:property value="id"/>" />
									<input type="hidden"
										name='receiptList[<s:property value='#s.index'/>].bankAccountId'
										id="receiptList[<s:property value='#s.index'/>].bankAccountId"
										value="<s:property value="bankAccountId"/>" />
									<s:if test="%{receiptList[#s.index].walkinPaymentAccount}">
										<td class="${tdcss}"
											style="text-align: center; background-color: #FFAC30; color: #000000"><input
											type="text"
											name='receiptList[<s:property value='#s.index'/>].bankName'
											id="receiptList[<s:property value='#s.index'/>].bankName"
											value="<s:property value="bankName"/>" readonly /></td>
									</s:if>
									<s:else>
										<td class="${tdcss}" style="text-align: center;"><input
											type="text"
											name='receiptList[<s:property value='#s.index'/>].bankName'
											id="receiptList[<s:property value='#s.index'/>].bankName"
											value="<s:property value="bankName"/>" readonly /></td>
									</s:else>
									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										name="receiptList[<s:property value='#s.index'/>].fundName"
										id="receiptList[<s:property value='#s.index'/>].fundName"
										value="<s:property value="fundName"/>" readonly /></td>
									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										name="receiptList[<s:property value='#s.index'/>].glcode"
										id="receiptList[<s:property value='#s.index'/>].glcode"
										value="<s:property value="glcode"/>" readonly /></td>
									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										name="receiptList[<s:property value='#s.index'/>].accountNumber"
										id="receiptList[<s:property value='#s.index'/>].accountNumber"
										value="<s:property value="accountNumber"/>" readonly /></td>

									<td class="${tdcss}" style="text-align: center"><b> <input
											type="text"
											style='text-align: right; background-color: #FFFF00; color: #000000'
											name="receiptList[<s:property value='#s.index'/>].openingBalance"
											id="receiptList[<s:property value='#s.index'/>].openingBalance"
											value="<s:property value="openingBalance"/>" readonly
											onblur="calculateFunds(this)" />
									</b></td>
									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										style='text-align: right; right; background-color: #FFAC30; color: #000000'
										name="receiptList[<s:property value='#s.index'/>].currentReceipt"
										id="receiptList[<s:property value='#s.index'/>].currentReceipt"
										value="<s:property value="currentReceipt"/>"
										onblur="calculateFunds(this)" /></td>
									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										style='text-align: right; background-color: #01DFD7; color: #000000'
										name="receiptList[<s:property value='#s.index'/>].fundsAvailable"
										id="receiptList[<s:property value='#s.index'/>].fundsAvailable"
										value="<s:property value="fundsAvailable"/>" readonly /></td>
									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										style='text-align: right; background-color: #81BEF7; color: #000000'
										name="receiptList[<s:property value='#s.index'/>].btbPayment"
										id="receiptList[<s:property value='#s.index'/>].btbPayment"
										value="<s:property value="btbPayment"/>" readonly /></td>
									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										style='text-align: right; background-color: #81BEF7; color: #000000'
										name="receiptList[<s:property value='#s.index'/>].btbReceipt"
										id="receiptList[<s:property value='#s.index'/>].btbReceipt"
										value="<s:property value="btbReceipt"/>" readonly /></td>
									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										style='text-align: right; background-color: #FFFF00; color: #000000'
										name="receiptList[<s:property value='#s.index'/>].closingBalance"
										id="receiptList[<s:property value='#s.index'/>].closingBalance"
										value="<s:property value="closingBalance"/>" readonly /></td>

								</tr>
							</s:if>
							<s:else>
								<td class="${tdcss}" style="text-align: center"></td>
								<td class="${tdcss}" style="text-align: center"></td>
								<td class="${tdcss}" style="text-align: center"></td>
								<strong><td class="${tdcss}"
									style="text-align: center;"><input type="text"
										style='text-align: right; background-color: #DEB887; color: #000000'
										name="receiptList[<s:property value='#s.index'/>].accountNumber"
										id="receiptList[<s:property value='#s.index'/>].accountNumber"
										value="<s:property value="accountNumber"/>" readonly /></td></strong>
								<td class="${tdcss}" style="text-align: center"><input
									type="text"
									style='text-align: right; background-color: #DEB887; color: #000000'
									name="receiptList[<s:property value='#s.index'/>].openingBalance"
									id="receiptList[<s:property value='#s.index'/>].openingBalance"
									value="<s:property value="openingBalance"/>" readonly
									onblur="calculateFunds(this)" /></td>
								<td class="${tdcss}" style="text-align: center"><input
									type="text"
									style='text-align: right; background-color: #DEB887; color: #000000'
									name="receiptList[<s:property value='#s.index'/>].currentReceipt"
									id="receiptList[<s:property value='#s.index'/>].currentReceipt"
									value="<s:property value="currentReceipt" />" readonly
									onblur="calculateFunds(this)" /></td>
								<td class="${tdcss}" style="text-align: center"><input
									type="text"
									style='text-align: right; background-color: #DEB887; color: #000000'
									name="receiptList[<s:property value='#s.index'/>].fundsAvailable"
									id="receiptList[<s:property value='#s.index'/>].fundsAvailable"
									value="<s:property value="fundsAvailable"/>" readonly /></td>
								<td class="${tdcss}" style="text-align: center"><input
									type="text"
									style='text-align: right; background-color: #DEB887; color: #000000'
									name="receiptList[<s:property value='#s.index'/>].btbPayment"
									id="receiptList[<s:property value='#s.index'/>].btbPayment"
									value="<s:property value="btbPayment"/>" readonly /></td>
								<td class="${tdcss}" style="text-align: center"><input
									type="text"
									style='text-align: right; background-color: #DEB887; color: #000000'
									name="receiptList[<s:property value='#s.index'/>].btbReceipt"
									id="receiptList[<s:property value='#s.index'/>].btbReceipt"
									value="<s:property value="btbReceipt"/>" readonly /></td>
								<td class="${tdcss}"
									style="text-align: center; text-decoration: bold"><input
									type="text"
									style='text-align: right; background-color: #DEB887; color: #000000'
									name="receiptList[<s:property value='#s.index'/>].closingBalance"
									id="receiptList[<s:property value='#s.index'/>].closingBalance"
									value="<s:property value="closingBalance"/>" readonly /></td>
							</s:else>
						</s:iterator>
						<tr>
							<td class="${tdcss}" style="text-align: center"></td>
							<td class="${tdcss}" style="text-align: center"></td>
							<td class="${tdcss}" style="text-align: center"></td>
							<td class="${tdcss}" style="text-align: center"><B><s:text
										name="Total (A)" /> </B>
							<td class="${tdcss}" style="text-align: center"><input
								type="text"
								style='text-align: right; background-color: #F7BE81; color: #000000'
								name="total[0].openingBalance" id="total[0].openingBalance"
								readonly value="0.00" /></td>
							<td class="${tdcss}" style="text-align: center"><input
								type="text"
								style='text-align: right; background-color: #F7BE81; color: #000000'
								name="total[0].currentReceipt" id="total[0].currentReceipt"
								readonly value="0.00" /></td>
							<td class="${tdcss}" style="text-align: center"><input
								type="text"
								style='text-align: right; background-color: #F7BE81; color: #000000'
								name="total[0].fundsAvailable" id="total[0].fundsAvailable"
								readonly value="0.00" /></td>
							<td class="${tdcss}" style="text-align: center"><input
								type="text"
								style='text-align: right; background-color: #F7BE81; color: #000000'
								name="total[0].btbPayment" id="total[0].btbPayment" readonly
								value="0.00" /></td>
							<td class="${tdcss}" style="text-align: center"><input
								type="text"
								style='text-align: right; background-color: #F7BE81; color: #000000'
								name="total[0].btbReceipt" id="total[0].btbReceipt" readonly
								value="0.00" /></td>
							<td class="${tdcss}" style="text-align: center"><input
								type="text"
								style='text-align: right; background-color: #F7BE81; color: #000000'
								name="total[0].closingBalance" id="total[0].closingBalance"
								readonly value="0.00" /></td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
	</table>
</s:if>
<!-- End of RECEIPT -->


<br />
<br />
<s:if test="paymentList!=null && paymentList.size()>0">
	<table width="99%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td class="blueborderfortd">
				<div>
					<table title="PAYMENT BANK DETAILS" width="100%" cellpadding="0"
						cellspacing="0" border="0" class="tablebottom" id="paymentTable">
						<tr>
							<td colspan="11" align="center"><b>PAYMENT BANK DETAILS</b></td>
							<td align="right"><strong>(Rupees in Lakh)</strong></td>
						</tr>
						<tr>
							<th class="bluebgheadtd"><s:text name="bank" /></th>
							<th class="bluebgheadtd"><s:text name="FundName" /></th>
							<th class="bluebgheadtd"><s:text name="coa" /></th>
							<th class="bluebgheadtd"><s:text name="bankaccount" /></th>
							<th class="bluebgheadtd"><s:text name="openingbalance" /></th>
							<th class="bluebgheadtd"><s:text name="currentreceipt" />
							</th>
							<th class="bluebgheadtd"><s:text name="btbpayment" /></th>
							<th class="bluebgheadtd"><s:text name="btbreceipt" /></th>
							<th class="bluebgheadtd"><s:text name="fundsavailable" />
							</th>
							<th class="bluebgheadtd"><s:text name="concurrancebpv" />
							</th>
							<th class="bluebgheadtd"><s:text name="closingbalance" />
							</th>
							<th class="bluebgheadtd"><s:text name="outstandingbpv" />
							</th>

						</tr>
						<s:iterator id="t" var="fundFlowBean" value="paymentList"
							status="s">
							<s:if test="#s.odd">
								<s:set var="tdcss" value="'bluebox'" />
							</s:if>
							<s:else>
								<s:set var="tdcss" value="'greybox'" />
							</s:else>
							<s:if test="#s.first">
								<s:set var="tdcss" value="'greybox'" />
							</s:if>
							<s:set var="index" value="<s:property value='#s.index'/>" />
							<s:if test="%{paymentList[#s.index].accountNumber!='Total'}">
								<tr>
									<input type="hidden"
										name='paymentList[<s:property value='#s.index'/>].id'
										id="paymentList[<s:property value='#s.index'/>].id"
										value="<s:property value="id"/>" />
									<input type="hidden"
										name='paymentList[<s:property value='#s.index'/>].bankAccountId'
										id="paymentList[<s:property value='#s.index'/>].bankAccountId"
										value="<s:property value="bankAccountId"/>" />
									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										name='paymentList[<s:property value='#s.index'/>].bankName'
										id="paymentList[<s:property value='#s.index'/>].bankName"
										value="<s:property value="bankName"/>" readonly /></td>
									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										name='paymentList[<s:property value='#s.index'/>].fundName'
										id="paymentList[<s:property value='#s.index'/>].fundName"
										value="<s:property value="fundName"/>" readonly /></td>
									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										name='paymentList[<s:property value='#s.index'/>].glcode'
										id="paymentList[<s:property value='#s.index'/>].glcode"
										value="<s:property value="glcode"/>" readonly /></td>
									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										name="paymentList[<s:property value='#s.index'/>].accountNumber"
										id="paymentList[<s:property value='#s.index'/>].accountNumber"
										value="<s:property value="accountNumber"/>" readonly /></td>
									<td class="${tdcss}" style="text-align: center;"><input
										type="text"
										style='text-align: right; background-color: #FFFF00; color: #000000'
										name="paymentList[<s:property value='#s.index'/>].openingBalance"
										style='text-align:right'
										id="paymentList[<s:property value='#s.index'/>].openingBalance"
										value="<s:property value="openingBalance"/>" readonly
										onblur="calculateFundsForPayment(this)" /></td>
									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										style='text-align: right; background-color: #FFAC30; color: #000000'
										name="paymentList[<s:property value='#s.index'/>].currentReceipt"
										id="paymentList[<s:property value='#s.index'/>].currentReceipt"
										value="<s:property value="currentReceipt"/>"
										onblur="calculateFundsForPayment(this)" /></td>
									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										style='text-align: right; background-color: #81BEF7; color: #000000'
										name="paymentList[<s:property value='#s.index'/>].btbPayment"
										id="paymentList[<s:property value='#s.index'/>].btbPayment"
										value="<s:property value="btbPayment"/>" readonly /></td>

									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										style='text-align: right; background-color: #81BEF7; color: #000000'
										name="paymentList[<s:property value='#s.index'/>].btbReceipt"
										id="paymentList[<s:property value='#s.index'/>].btbReceipt"
										value="<s:property value="btbReceipt"/>" readonly /></td>
									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										style='text-align: right; background-color: #01DFD7; color: #000000'
										name="paymentList[<s:property value='#s.index'/>].fundsAvailable"
										id="paymentList[<s:property value='#s.index'/>].fundsAvailable"
										value="<s:property value="fundsAvailable"/>" readonly /></td>
									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										style='text-align: right; background-color: #FF8484; color: #000000'
										name="paymentList[<s:property value='#s.index'/>].concurranceBPV"
										id="paymentList[<s:property value='#s.index'/>].concurranceBPV"
										value="<s:property value="concurranceBPV"/>" readonly /></td>

									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										style='text-align: right; background-color: #FFFF00; color: #000000'
										name="paymentList[<s:property value='#s.index'/>].closingBalance"
										id="paymentList[<s:property value='#s.index'/>].closingBalance"
										value="<s:property value="closingBalance"/>" readonly /></td>
									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										style='text-align: right; background-color: #81BEF7; color: #000000'
										name="paymentList[<s:property value='#s.index'/>].outStandingBPV"
										id="paymentList[<s:property value='#s.index'/>].outStandingBPV"
										value="<s:property value="outStandingBPV"/>" readonly /></td>
								</tr>
							</s:if>
							<s:else>
								<tr>

									<td class="${tdcss}" style="text-align: center"></td>
									<td class="${tdcss}" style="text-align: center"></td>
									<td class="${tdcss}" style="text-align: center"></td>
									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										style='text-align: right; background-color: #DEB887; color: #000000'
										name="paymentList[<s:property value='#s.index'/>].accountNumber"
										id="paymentList[<s:property value='#s.index'/>].accountNumber"
										value="<s:property value="accountNumber"/>" readonly /></td>
									<td class="${tdcss}"
										style="text-align: center; font-weight: bold;"><input
										type="text"
										style='text-align: right; background-color: #DEB887; color: #000000'
										name="paymentList[<s:property value='#s.index'/>].openingBalance"
										style='text-align:right'
										id="paymentList[<s:property value='#s.index'/>].openingBalance"
										value="<s:property value="openingBalance"/>" readonly
										onblur="calculateFundsForPayment(this)" /></td>
									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										style='text-align: right; background-color: #DEB887; color: #000000'
										name="paymentList[<s:property value='#s.index'/>].currentReceipt"
										id="paymentList[<s:property value='#s.index'/>].currentReceipt"
										value="<s:property value="currentReceipt"/>"
										onblur="calculateFundsForPayment(this)" /></td>
									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										style='text-align: right; background-color: #DEB887; color: #000000'
										name="paymentList[<s:property value='#s.index'/>].btbPayment"
										id="paymentList[<s:property value='#s.index'/>].btbPayment"
										value="<s:property value="btbPayment"/>" readonly /></td>

									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										style='text-align: right; background-color: #DEB887; color: #000000'
										name="paymentList[<s:property value='#s.index'/>].btbReceipt"
										id="paymentList[<s:property value='#s.index'/>].btbReceipt"
										value="<s:property value="btbReceipt"/>" readonly /></td>
									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										style='text-align: right; background-color: #DEB887; color: #000000'
										name="paymentList[<s:property value='#s.index'/>].fundsAvailable"
										id="paymentList[<s:property value='#s.index'/>].fundsAvailable"
										value="<s:property value="fundsAvailable"/>" readonly /></td>
									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										style='text-align: right; background-color: #DEB887; color: #000000'
										name="paymentList[<s:property value='#s.index'/>].concurranceBPV"
										id="paymentList[<s:property value='#s.index'/>].concurranceBPV"
										value="<s:property value="concurranceBPV"/>" readonly /></td>

									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										style='text-align: right; background-color: #DEB887; color: #000000'
										name="paymentList[<s:property value='#s.index'/>].closingBalance"
										id="paymentList[<s:property value='#s.index'/>].closingBalance"
										value="<s:property value="closingBalance"/>" readonly /></td>
									<td class="${tdcss}" style="text-align: center"><input
										type="text"
										style='text-align: right; background-color: #DEB887; color: #000000'
										name="paymentList[<s:property value='#s.index'/>].outStandingBPV"
										id="paymentList[<s:property value='#s.index'/>].outStandingBPV"
										value="<s:property value="outStandingBPV"/>" readonly /></td>
								</tr>
							</s:else>
						</s:iterator>
						<tr>
							<td class="${tdcss}" style="text-align: center"></td>
							<td class="${tdcss}" style="text-align: center"></td>
							<td class="${tdcss}" style="text-align: center"></td>
							<td class="${tdcss}" style="text-align: center"><b><s:text
										name="Total (B)" /></b></td>
							<td class="${tdcss}" style="text-align: center"><input
								type="text"
								style='text-align: right; background-color: #F7BE81; color: #000000'
								name="total[1].openingBalance" id="total[1].openingBalance"
								readonly value="0.00" /></td>
							<td class="${tdcss}" style="text-align: center"><input
								type="text"
								style='text-align: right; background-color: #F7BE81; color: #000000'
								name="total[1].currentReceipt" id="total[1].currentReceipt"
								readonly value="0.00" /></td>
							<td class="${tdcss}" style="text-align: center"><input
								type="text"
								style='text-align: right; background-color: #F7BE81; color: #000000'
								name="total[1].btbPayment" id="total[1].btbPayment" readonly
								value="0.00" /></td>


							<td class="${tdcss}" style="text-align: center"><input
								type="text"
								style='text-align: right; background-color: #F7BE81; color: #000000'
								name="total[1].btbReceipt" id="total[1].btbReceipt" readonly
								value="0.00" /></td>
							<td class="${tdcss}" style="text-align: center"><input
								type="text"
								style='text-align: right; background-color: #F7BE81; color: #000000'
								name="total[1].fundsAvailable" id="total[1].fundsAvailable"
								readonly value="0.00" /></td>
							<td class="${tdcss}" style="text-align: center"><input
								type="text"
								style='text-align: right; background-color: #F7BE81; color: #000000'
								name="total[1].concurranceBPV" id="total[1].concurranceBPV"
								readonly value="0.00" /></td>
							<td class="${tdcss}" style="text-align: center"><input
								type="text"
								style='text-align: right; background-color: #F7BE81; color: #000000'
								name="total[1].closingBalance" id="total[1].closingBalance"
								readonly value="0.00" /></td>


							<td class="${tdcss}" style="text-align: center"><input
								type="text"
								style='text-align: right; background-color: #F7BE81; color: #000000'
								name="total[1].outStandingBPV" id="total[1].outStandingBPV"
								readonly value="0.00" /></td>
						</tr>

						<tr>
							<td class="${tdcss}" style="text-align: center"></td>
							<td class="${tdcss}" style="text-align: center"></td>
							<td class="${tdcss}" style="text-align: center"></td>
							<td class="${tdcss}" style="text-align: center"><B><s:text
										name=" Grand Total (A + B)" /></B></td>
							<td class="${tdcss}" style="text-align: center"><input
								type="text"
								style='text-align: right; background-color: #F7BE81; color: #000000'
								name="total[2].openingBalance" id="total[2].openingBalance"
								readonly value="0.00" /></td>
							<td class="${tdcss}" style="text-align: center;"></td>

							<td class="${tdcss}" style="text-align: center"></td>

							<td class="${tdcss}" style="text-align: center"></td>
							<td class="${tdcss}" style="text-align: center"></td>

							<td class="${tdcss}" style="text-align: center"></td>
							<td class="${tdcss}" style="text-align: center"><input
								type="text"
								style='text-align: right; background-color: #F7BE81; color: #000000'
								name="total[2].closingBalance" id="total[2].closingBalance"
								readonly value="0.00" /></td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
	</table>

</s:if>

<s:if
	test="receiptList!=null && receiptList.size()==0 && paymentList!=null && paymentList.size()==0 ">
	<div class="error">
		<span class="bluebgheadtd" colspan="7"><s:text
				name="no.data.found" /></span>
	</div>
</s:if>
