<%--
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
  --%>


<html>
<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="bankreconciliation" /></title>
<script type="text/javascript">
	
</script>
</head>
<body>
	<s:form action="autoReconciliation" theme="simple" name="arform">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param value="Auto Bank Reconciliation Report" name="heading" />
		</jsp:include>
		<div class="formmainbox">
			<div class="formheading"></div>
			<div class="subheadnew">
				<s:text name="autobankreconciliation" />
			</div>

			<div align="center">
				<font style='color: red;'>
					<p class="error-block" id="lblError"></p>
				</font>
			</div>
			<span class="mandatory1">
				<div id="Errors">
					<s:actionerror />
					<s:fielderror />
				</div> <s:actionmessage />
			</span>
			<center>
				<table border="0" width="50%" cellspacing="0" cellpadding="0">
					<tr>
						<td class="greybox"><s:text name="bank.name" /></td>
						<td class="greybox"><s:property
								value="bankAccount.bankbranch.bank.name" /></td>
					<tr>
						<td class="bluebox"><s:text name="accountnumber" /></td>
						<td class="bluebox"><s:property
								value="bankAccount.accountnumber" /></td>
					</tr>
					<tr>
						<td class="greybox"><s:text name="accountcode" /></td>
						<td class="greybox"><s:property
								value="bankAccount.chartofaccounts.glcode" /></td>
					</tr>
					<tr>
						<td class="bluebox"><s:text name="account.description" /></td>
						<td class="bluebox"><s:property
								value="bankAccount.chartofaccounts.name" /></td>
					</tr>

				</table>
				<br />
				<div>
					<h2>
						Bank reconcilation statement from
						<s:property value="fromDate" />
						to
						<s:property value="toDate" />
						on
						<s:property value="reconciliationDate" />
					</h2>
				</div>
				<div align="right" style="text-align: right">
					<h4>
						Balance as per Bank book (A) :
						<s:property value="bankBookBalance" />
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</h4>
				</div>
				<br />
				<center>
					<div>
						<h3>Bank statement entries not in bank book (AS PER BANK
							STATEMENT DATA)</h3>
					</div>
					<table border="0" width="70%" cellspacing="0" cellpadding="0">
						<tr>
							<th class="bluebgheadtd">Sl No</th>
							<th class="bluebgheadtd">Type</th>
							<th class="bluebgheadtd">Date</th>
							<th class="bluebgheadtd">Cheque No</th>
							<th class="bluebgheadtd">Debit</th>
							<th class="bluebgheadtd">Credit</th>
							<th class="bluebgheadtd">Narration</th>
							<th class="bluebgheadtd">Action</th>
							<th class="bluebgheadtd">Message</th>
						</tr>
						<s:if test="statementsNotInBankBookList.size()>0">
							<s:iterator value="statementsNotInBankBookList" status="stat"
								var="p">
								<tr>
									<td class="blueborderfortd" width="4%"><div align="left">
											<s:property value="#stat.index+1" />
											&nbsp;
										</div></td>
									<td class="blueborderfortd" width="5%"><div align="left">
											<s:property value="type" />
											&nbsp;
										</div></td>
									<td class="blueborderfortd" width="8%"><div align="left">
											<s:property value="txDate" />
											&nbsp;
										</div></td>
									<td class="blueborderfortd" width="10%"><div align="left">
											<s:property value="instrumentNo" />
											&nbsp;
										</div></td>
									<td class="blueborderfortd" width="8%"><div align="right">
											<s:property value="debit" />
											&nbsp;
										</div></td>
									<td class="blueborderfortd" width="8%"><div align="right">
											<s:property value="credit" />
											&nbsp;
										</div></td>
									<td class="blueborderfortd" width="25%"><div align="left">
											<s:property value="narration" />
											&nbsp;
										</div></td>
									<td class="blueborderfortd" width="14%"><div align="left">
											<s:property value="errorCode" />
											&nbsp;
										</div></td>
									<td class="blueborderfortd" width="18%"><div align="left">
											<s:property value="errorMessage" />
											&nbsp;
										</div></td>
								</tr>
							</s:iterator>

						</s:if>
						<s:else>
							<tr>
								<td colspan="6" style="text-align: center">No Data Found</td>
							</tr>
						</s:else>
					</table>
					<br />
					<div align="right" style="text-align: right">
						<h4>
							Net balance : B=
							<s:property value="notInBookNetBal" />
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</h4>
					</div>
					<br /> <br />
					<div>
						<h3>Bank statement entries found in bank book but could not
							process</h3>
					</div>
					<table border="0" width="70%" cellspacing="0" cellpadding="0">
						<tr>
							<th class="bluebgheadtd">Sl No</th>
							<th class="bluebgheadtd">Type</th>
							<th class="bluebgheadtd">Date</th>
							<th class="bluebgheadtd">Cheque No</th>
							<th class="bluebgheadtd">Debit</th>
							<th class="bluebgheadtd">Credit</th>
							<th class="bluebgheadtd">Narration</th>
							<th class="bluebgheadtd">Action</th>
							<th class="bluebgheadtd">Message</th>
						</tr>
						<s:if test="statementsFoundButNotProcessed.size()>0">
							<s:iterator value="statementsFoundButNotProcessed" status="stat"
								var="p">
								<tr>
									<td class="blueborderfortd" width="4%"><div align="left">
											<s:property value="#stat.index+1" />
											&nbsp;
										</div></td>
									<td class="blueborderfortd" width="5%"><div align="left">
											<s:property value="type" />
											&nbsp;
										</div></td>
									<td class="blueborderfortd" width="8%"><div align="left">
											<s:property value="txDate" />
											&nbsp;
										</div></td>
									<td class="blueborderfortd" width="10%"><div align="left">
											<s:property value="instrumentNo" />
											&nbsp;
										</div></td>
									<td class="blueborderfortd" width="8%"><div align="right">
											<s:property value="debit" />
											&nbsp;
										</div></td>
									<td class="blueborderfortd" width="8%"><div align="right">
											<s:property value="credit" />
											&nbsp;
										</div></td>
									<td class="blueborderfortd" width="25%"><div align="left">
											<s:property value="narration" />
											&nbsp;
										</div></td>
									<td class="blueborderfortd" width="14%"><div align="left">
											<s:property value="errorCode" />
											&nbsp;
										</div></td>
									<td class="blueborderfortd" width="18%"><div align="left">
											<s:property value="errorMessage" />
											&nbsp;
										</div></td>
								</tr>
							</s:iterator>

						</s:if>
						<s:else>
							<tr>
								<td colspan="6" style="text-align: center">No Data Found</td>
							</tr>
						</s:else>
					</table>
					<br />
					<div align="right" style="text-align: right">
						<h4>
							Net balance : C=
							<s:property value="notprocessedNet" />
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</h4>
					</div>
					<br />
					<div>
						<h3>Bank book entries not in bank statement( AS PER SYSTEM
							DATA)</h3>
					</div>
					<s:hidden name="fromDate" />
					<s:hidden name="toDate" />
					<s:hidden name="accountId" />
					<s:hidden name="reconciliationDate" />
					<s:hidden name="branchId" />
					<s:hidden name="bankId" />
					<table border="0" width="70%" cellspacing="0" cellpadding="0">
						<tr>
							<th class="bluebgheadtd">Sl No</th>
							<th class="bluebgheadtd">Date</th>
							<th class="bluebgheadtd">Cheque No</th>
							<th class="bluebgheadtd">Debit</th>
							<th class="bluebgheadtd">Credit</th>
							<th class="bluebgheadtd">Narration</th>
						</tr>
						<s:if test="entriesNotInBankStament.size()>0">
							<s:iterator value="entriesNotInBankStament" status="stat" var="p">
								<tr>
									<td class="blueborderfortd" width="5%"><div align="left">
											<s:property value="#stat.index+1" />
											&nbsp;
										</div></td>
									<td class="blueborderfortd" width="10%"><div align="left">
											<s:property value="txDate" />
											&nbsp;
										</div></td>
									<td class="blueborderfortd" width="10%"><div align="left">
											<s:property value="instrumentNo" />
											&nbsp;
										</div></td>
									<td class="blueborderfortd" width="10%"><div align="right">
											<s:property value="debit" />
											&nbsp;
										</div></td>
									<td class="blueborderfortd" width="10%"><div align="right">
											<s:property value="credit" />
											&nbsp;
										</div></td>
									<td class="blueborderfortd" width="55%"><div align="left">
											<s:property value="narration" />
											&nbsp;
										</div></td>
								</tr>
							</s:iterator>
						</s:if>
						<s:else>
							<tr>
								<td colspan="6">No Data Found</td>
							</tr>
						</s:else>
					</table>
					<br />
					<div align="right" style="text-align: right">
						<h4>
							Net balance: D=
							<s:property value="notInStatementNet" />
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</h4>
					</div>
					<br /> <br />
					<div align="right" style="text-align: right">
						<h4>
							Total Not Reconciled balance(C+D) =
							<s:property value="totalNotReconciledAmount" />
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</h4>
					</div>
					<br /> <br />
					<div align="right" style="text-align: right">
						<h4>
							BRS Balance (A+B+C+D):E=
							<s:property value="brsBalance" />
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</h4>
					</div>
				</center>
				<div class="buttonbottom" id="buttondiv" align="center">
					<table>
						<tr>
							<s:submit value="Export EXCEL" method="generateXLS"
								cssClass="button"
								onclick="javascript:document.forms[0].action='autoReconciliation-generateXLS.action'" />
							<s:submit value="Export PDF" method="generatePDF"
								cssClass="button"
								onclick="javascript:document.forms[0].action='autoReconciliation-generatePDF.action'" />
						</tr>
					</table>
				</div>
		</div>
	</s:form>
</body>
</html>
