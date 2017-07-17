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


<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>
<html>

<head>
<script type="text/javascript"
	src="/EGF/resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/calender.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/dateValidation.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/remitrecovery-helper.js?rnd=${app_release_no}"></script>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<title><s:text name="remit.recovery.create.title" /></title>
</head>
<body>
	<s:form action="remitRecovery" theme="simple" name="remitRecoveryForm">
	<s:hidden type="hidden" id="selectedRows" name="selectedRows" />
	<s:hidden type="hidden" id="departmentId" name="departmentId" value="%{departmentId}" />
	<s:hidden type="hidden" id="functionId" name="functionId" value="%{functionId}" />
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading" value='Remittance Recovery' />
		</jsp:include>

		<span> <font style='color: red; font-weight: bold'> <s:actionerror />
				<s:fielderror /> <s:actionmessage /></font>
		</span>
		<div class="formmainbox" />
		<div class="subheadnew">
			<s:text name="remit.recovery.new.title" />
		</div>
		<div align="center">
			<font style='color: red; font-weight: bold'>
				<p class="error-block" id="lblError"></p>
			</font>
			<table border="0" width="100%">
				<tr>
					<td class="greybox"></td>
					<td class="greybox"><s:text name="remit.recovery.search.code" /><span
						class="mandatory"></span></td>
					<td class="greybox"><s:select name="remittanceBean.recoveryId"
							id="recoveryId" list="dropdownData.recoveryList" listKey="id"
							listValue="type+'-'+recoveryName" headerKey="-1" headerValue="----Choose----"
							value="%{remittanceBean.recoveryId}" /></td>
					<td class="greybox" width="10%">
					<td class="greybox">
				<tr>
					<td class="bluebox"></td>
					<td class="bluebox"><s:text
							name="remit.recovery.search.fromdate" /></td>
					<td class="greybox"><s:date name="remittanceBean.fromVhDate"
							var="fromVhDateId" format="dd/MM/yyyy" /> <s:textfield
							id="fromVhDate" name="remittanceBean.fromVhDate"
							value="%{voucherDateId}"
							onkeyup="DateFormat(this,this.value,event,false,'3')"
							placeholder="DD/MM/YYYY" cssClass="form-control datepicker"
							data-inputmask="'mask': 'd/m/y'" /></td>

					<td class="bluebox"><s:text
							name="remit.recovery.search.todate" /><span class="mandatory"></span></td>
					<td class="greybox"><s:date name="voucherDate"
							var="voucherDateId" format="dd/MM/yyyy" /> <s:textfield
							id="voucherDate" name="voucherDate" value="%{voucherDateId}"
							onkeyup="DateFormat(this,this.value,event,false,'3')"
							placeholder="DD/MM/YYYY" cssClass="form-control datepicker"
							data-inputmask="'mask': 'd/m/y'" /></td>
				</tr>
				<tr>
					<td class="greybox"></td>
					<td class="greybox"><s:text name="remit.recovery.search.bank" /></td>
					<td class="greybox"><s:select name="remittanceBean.bank"
							id="bank" list="dropdownData.bankList" listKey="id"
							listValue="name" headerKey="" headerValue="----Choose----"
							value="%{remittanceBean.bank}" /></td>
					<td class="greybox"><s:text name="remit.recovery.search.bankbranch" /></td>
					<td class="greybox"><s:select name="remittanceBean.bankBranchId"
							id="bankBranch" list="dropdownData.branchList" listKey="id"
							listValue="name" headerKey="" headerValue="----Choose----"
							value="%{remittanceBean.bankBranchId}" /></td>
					</tr>
					<tr>
					<td class="greybox"></td>		
					<td class="greybox"><s:text name="remit.recovery.search.bankaccount" /></td>
					<td class="greybox"><s:select name="remittanceBean.bankAccountId"
							id="bankAccount" list="dropdownData.accNumList" listKey="id"
							listValue="accountnumber" headerKey="" headerValue="----Choose----"
							value="%{remittanceBean.bankAccountId}" /></td>
				</tr>
				<%@ include file="../payment/paymenttrans-filter.jsp"%>

			</table>
			<jsp:include page="remitRecovery-form.jsp" />
			<label style="text-align: right;"></label>

			<div class="buttonbottom" style="padding-bottom: 10px;">
				<s:submit type="submit" cssClass="buttonsubmit" value="Search"
					id="search" name="search" method="search"
					onclick="return validateSearch();" />
				<input type="button" id="Close" value="Close"
					onclick="javascript:window.close()" class="button" />
			</div>
		</div>
		<s:if test='%{listRemitBean != null }'>
			<s:if test="%{ listRemitBean.size()>0}">
				<div align="center">
					<font style='color: red; font-weight: bold'>
						<p class="error-block" id="remitlblError"></p>
					</font>
				</div>
				<div id="labelAD" align="center">
					<table width="100%" border=0 id="recoveryDetails">
						<th>Recovery Details</th>
					</table>
				</div>
				<table align="center" id="totalAmtTable">
					<tr>
						<td width="1050"></td>
						<td><s:text name="remit.SelectDeSelectAll" /></td>
						<td><s:checkbox id="selectAll" name="selectAll"
								onclick="selectAllORNone(this);"></s:checkbox></td>
					</tr>
				</table>


				<div class="yui-skin-sam" align="center">
					<div id="recoveryDetailsTable"></div>
				</div>
				<script>
		
		populateRecoveryDetails();
		document.getElementById('recoveryDetailsTable').getElementsByTagName('table')[0].width="90%"
	 </script>
				<br>
				<table align="center" id="totalAmtTable">
					<tr>
						<td width="850"></td>
						<td>Total Amount</td>
						<td><s:textfield name="remittanceBean.totalAmount"
								id="totalAmount" style="width:90px;text-align:right"
								readonly="true" value="0" /></td>
					</tr>
				</table>
				<div id ="remitTotal" />
				<s:hidden type="hidden" id="selectedrRemit"
					name="remittanceBean.selectedrRemit" />
				<div class="buttonbottom" style="padding-bottom: 10px;">
					<s:submit type="submit" cssClass="buttonsubmit"
						value="Generate Payment" id="genPayment" name="save&genPayment"
						method="remit" onclick="return validateRemit()" />
			</s:if>
			<s:else>
				<div class="error">
					<span class="bluebgheadtd" colspan="7"><s:text
							name="no.data.found" /></span>
				</div>
			</s:else>
		</s:if>
		</div>
	</s:form>
</body>
</html>
