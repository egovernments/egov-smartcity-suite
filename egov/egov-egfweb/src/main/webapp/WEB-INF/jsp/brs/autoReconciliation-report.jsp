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
<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="bankreconciliation" /></title>
<script type="text/javascript">
function exportXls()
{
	document.forms[0].action='autoReconciliation-generateXLS.action';
	document.forms[0].submit();
	}

function exportPdf()
{
	document.forms[0].action='autoReconciliation-generatePDF.action';
	document.forms[0].submit();
	}
</script>
</head>
<body>
	<s:form action="autoReconciliation" theme="simple" name="arform">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param value="Auto Bank Reconciliation Report" name="heading" />
		</jsp:include>
		<!-- <div class="formmainbox"> -->
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
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title">Bank Details</div>
			</div>
			<div class="panel-body">
				<div class="row add-border">
					<div class="col-md-3 col-xs-6 add-margin">
						<s:text name="bank.name" />
					</div>
					<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-date">
						<s:property value="bankAccount.bankbranch.bank.name" />
					</div>
					<div class="col-md-3 col-xs-6 add-margin">
						<s:text name="accountnumber" />
					</div>
					<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-date">
						<s:property value="bankAccount.accountnumber" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-md-3 col-xs-6 add-margin">
						<s:text name="accountcode" />
					</div>
					<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-date">
						<s:property value="bankAccount.chartofaccounts.glcode" />
					</div>
					<div class="col-md-3 col-xs-6 add-margin">
						<s:text name="account.description" />
					</div>
					<div class="col-md-3 col-xs-6 add-margin view-content" id="ct-date">
						<s:property value="bankAccount.chartofaccounts.name" />
					</div>
				</div>
			</div>
		</div>
		<s:date name="fromDate" var="fromDateId" format="dd/MM/yyyy" />
		<s:date name="toDate" var="toDateId" format="dd/MM/yyyy" />
		<s:date name="reconciliationDate" var="reconciliationDateId"
			format="dd/MM/yyyy" />
		<h4 class="text-center">
			Bank reconciliation statement from
			<s:property value="%{fromDateId}" />
			to
			<s:property value="%{toDateId}" />
			on
			<s:property value="%{reconciliationDateId}" />
		</h4>
		<div class="row">
			<div class="col-md-3 pull-right">
				<div class="alert alert-success" role="alert">
					<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
					Balance as per Bank book (A) :
					<s:property value="bankBookBalance" />
				</div>
			</div>

		</div>
		<h4 class="text-center">Bank statement entries not in bank book
			(AS PER BANK STATEMENT DATA)</h4>
		<table class="table table-bordered">
			<thead>
				<tr>
					<th>Sl No</th>
					<th>Type</th>
					<th>Date</th>
					<th>Cheque No</th>
					<th>Debit</th>
					<th>Credit</th>
					<th>Narration</th>
					<th>Action</th>
					<th>Message</th>
				</tr>
			</thead>
			<tbody>
				<s:if test="statementsNotInBankBookList.size()>0">
					<s:iterator value="statementsNotInBankBookList" status="stat"
						var="p">
						<tr>
							<td><s:property value="#stat.index+1" /></td>
							<td><s:property value="type" /></td>
							<td><s:property value="txDate" /></td>
							<td><s:property value="instrumentNo" /></td>
							<td class="text-right"><s:property value="debit" /></td>
							<td class="text-right"><s:property value="credit" /></td>
							<td><s:property value="narration" /></td>
							<td><s:property value="errorCode" /></td>
							<td><s:property value="errorMessage" /></td>
						</tr>
					</s:iterator>

				</s:if>
				<s:else>
					<tr>
						<td class="text-center" colspan="9">No data found</td>
					</tr>
				</s:else>
			</tbody>
		</table>
		<div class="row">
			<div class="col-md-3 pull-right">
				<div class="alert alert-success" role="alert">
					<span class="glyphicon glyphicon-ok" aria-hidden="true"></span> Net
					balance (B) :
					<s:property value="notInBookNetBal" />
				</div>
			</div>
		</div>
		<h4 class="text-center">Bank statement entries found in bank book
			but could not process</h4>
		<table class="table table-bordered">
			<thead>
				<tr>
					<th>Sl No</th>
					<th>Type</th>
					<th>Date</th>
					<th>Cheque No</th>
					<th>Debit</th>
					<th>Credit</th>
					<th>Narration</th>
					<th>Action</th>
					<th>Message</th>
				</tr>
			</thead>
			<tbody>
				<s:if test="statementsFoundButNotProcessed.size()>0">
					<s:iterator value="statementsFoundButNotProcessed" status="stat"
						var="p">
						<tr>
							<td><s:property value="#stat.index+1" /></td>
							<td><s:property value="type" /></td>
							<td><s:property value="txDate" /></td>
							<td><s:property value="instrumentNo" /></td>
							<td class="text-right"><s:property value="debit" /></td>
							<td class="text-right"><s:property value="credit" /></td>
							<td><s:property value="narration" /></td>
							<td><s:property value="errorCode" /></td>
							<td><s:property value="errorMessage" /></td>
						</tr>
					</s:iterator>

				</s:if>
				<s:else>
					<tr>
						<td class="text-center" colspan="9">No data found</td>
					</tr>
				</s:else>
			</tbody>
		</table>
		<div class="row">
			<div class="col-md-3 pull-right">
				<div class="alert alert-success" role="alert">
					<span class="glyphicon glyphicon-ok" aria-hidden="true"></span> Net
					balance (C) :
					<s:property value="notprocessedNet" />
				</div>
			</div>
		</div>
		<h4 class="text-center">Bank book entries not in bank statement
			(AS PER SYSTEM DATA)</h4>
		<s:hidden name="fromDate" />
		<s:hidden name="toDate" />
		<s:hidden name="accountId" />
		<s:hidden name="reconciliationDate" />
		<s:hidden name="branchId" />
		<s:hidden name="bankId" />
		<table class="table table-bordered">
			<thead>
				<tr>
					<th>Sl No</th>
					<th>Date</th>
					<th>Cheque No</th>
					<th>Debit</th>
					<th>Credit</th>
					<th>Narration</th>
				</tr>
			</thead>
			<tbody>
				<s:if test="entriesNotInBankStament.size()>0">
					<s:iterator value="entriesNotInBankStament" status="stat" var="p">
						<tr>
							<td><s:property value="#stat.index+1" /></td>
							<td><s:property value="txDate" /></td>
							<td><s:property value="instrumentNo" /></td>
							<td class="text-right"><s:property value="debit" /></td>
							<td class="text-right"><s:property value="credit" /></td>
							<td><s:property value="narration" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td class="text-center" colspan="6">No data found</td>
					</tr>
				</s:else>
			</tbody>
		</table>
		<div class="row">
			<div class="col-md-3 pull-right">
				<div class="alert alert-success" role="alert">
					<span class="glyphicon glyphicon-ok" aria-hidden="true"></span> Net
					balance (D) :
					<s:property value="notInStatementNet" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-4 pull-right">
				<div class="alert alert-success" role="alert">
					<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
					Total Not Reconciled balance (C+D) :
					<s:property value="totalNotReconciledAmount" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-4 pull-right">
				<div class="alert alert-success" role="alert">
					<span class="glyphicon glyphicon-ok" aria-hidden="true"></span> BRS
					Balance (A+B+C+D) :
					<s:property value="brsBalance" />
				</div>
			</div>
		</div>
		<div class="buttonbottom" id="buttondiv" align="center">
			<table>
				<tr>
					<input type="submit" value="Export EXCEL" 
						class="buttonsubmit"
						onclick="exportXls();" />
					<input type="submit" value="Export PDF" class="buttonsubmit"
						onclick="exportPdf();" />
					<input type="button" value="Close"	onclick="javascript:window.close()" class="button" />
				</tr>
			</table>
		</div>
		<!-- </div> -->
	</s:form>
</body>
</html>