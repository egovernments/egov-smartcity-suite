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
<%@ taglib uri="/WEB-INF/tags/cdn.tld" prefix="cdn" %>
<head>
<script src="<cdn:url value='/resources/app/js/reconciliationHelper.js?rnd=${app_release_no}' />"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="bankreconciliation" /></title>
<script type="text/javascript">
function validateSubmit() {
	if(!validate())
		{
			return false;
		}
		document.forms[0].action = "autoReconciliation-schedule.action"

		return true;
	}
	function populatebranch(obj) {
		var bid = document.getElementById("bankId").value;
		populatebranchId({
			bankId : bid
		})
	}

	function populateaccount(obj) {
		var bid = document.getElementById("branchId").value;
		populateaccountId({
			branchId : bid
		})
	}
</script>
</head>
<body>
	<s:form action="autoReconciliation" theme="simple" name="arform">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param value="Auto Bank Reconciliation" name="heading" />
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
				<table border="0" width="100%" cellspacing="0" cellpadding="0">
					<tr>
						<td class="greybox"></td>
						<td class="greybox"><s:text name="bank" /> <span
							class="greybox"><span class="mandatory1">*</span></span></td>
						<egov:ajaxdropdown id="branchId" fields="['Text','Value']"
							dropdownId="branchId"
							url="/voucher/common-ajaxLoadBankBranchesByBank.action" />
						<td class="greybox"><s:select name="bankId" id="bankId"
								list="dropdownData.bankList" listKey="id" listValue="name"
								headerKey="" headerValue="----Choose----"
								onchange="populatebranch(this);" value="%{bankId}" /></td>
						<td class="greybox"><s:text name="bankbranch" /> <span
							class="greybox"><span class="mandatory1">*</span></span></td>
						<egov:ajaxdropdown id="accountId" fields="['Text','Value']"
							dropdownId="accountId"
							url="/voucher/common-ajaxLoadBankAccountsByBranch.action" />
						<td class="greybox"><s:select name="branchId" id="branchId"
								list="dropdownData.branchList" listKey="id" listValue="name"
								headerKey="" headerValue="----Choose----"
								onchange="populateaccount(this);" /></td>
					</tr>
					<tr>
						<td class="bluebox"></td>
						<td class="bluebox"><s:text name="bankaccount" /> <span
							class="bluebox"><span class="mandatory1">*</span></span></td>
						<td class="bluebox"><s:select name="accountId" id="accountId"
								list="dropdownData.accountList" listKey="id"
								listValue="accountnumber" headerKey=""
								headerValue="----Choose----" /></td>
						<td class="bluebox"><s:text name="reconciliationdate" /> <span
							class="bluebox"><span class="mandatory1">*</span></span></td>
						<td class="greybox"><s:date name="reconciliationDate"
								var="reconciliationDate" format="dd/MM/yyyy" /> <s:textfield
								id="reconciliationDate" name="reconciliationDate"
								value="%{reconciliationDate}"
								onkeyup="DateFormat(this,this.value,event,false,'3')"
								placeholder="DD/MM/YYYY" cssClass="form-control datepicker"
								data-inputmask="'mask': 'd/m/y'" /></td>
					</tr>
					<tr>
						<td class="greybox"></td>
						<td class="greybox"><s:text name="fromdate" /> <span
							class="greybox"><span class="mandatory1">*</span></span></td>
						<td class="greybox"><s:date name="fromDate" var="fromDate"
								format="dd/MM/yyyy" /> <s:textfield id="fromDate"
								name="fromDate" value="%{fromDate}"
								onkeyup="DateFormat(this,this.value,event,false,'3')"
								placeholder="DD/MM/YYYY" cssClass="form-control datepicker"
								data-inputmask="'mask': 'd/m/y'" /></td>
						<td class="greybox"><s:text name="todate" /> <span
							class="greybox"><span class="mandatory1">*</span></span></td>

						<td class="greybox"><s:date name="toDate" var="toDate"
								format="dd/MM/yyyy" /> <s:textfield id="toDate" name="toDate"
								value="%{toDate}"
								onkeyup="DateFormat(this,this.value,event,false,'3')"
								placeholder="DD/MM/YYYY" cssClass="form-control datepicker"
								data-inputmask="'mask': 'd/m/y'" /></td>
					</tr>

				</table>

				<div class="buttonbottom" id="buttondiv">
					<table>
						<tr>
							<td><input type="submit" class="buttonsubmit"
								value="Process" name="Schedule" method="schedule"
								onclick="return validateSubmit();" /></td>
							<td><input type="button" value="Close"
								onclick="javascript:window.close()" class="buttonsubmit" /></td>
						</tr>
					</table>
				</div>
			</center>
		</div>
	</s:form>
	<div class="mandatory1">
		Note:
		<ol>
			<li><s:text name="autoreconciliation.process.footnote" />
		</ol>
	</div>
</body>
</html>
