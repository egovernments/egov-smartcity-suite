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

<html>
<head>
<link rel="stylesheet" type="text/css" href="/EGF/resources/css/ccMenu.css?rnd=${app_release_no}" />
<title>TNEB Bill Payment Search</title>
</head>
<body>
	<s:form action="payment" theme="simple">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading" value="Bill Payment Search" />
		</jsp:include>
		<span class="mandatory" id="errorSpan"> <s:actionerror /> <s:fielderror />
			<s:actionmessage />
		</span>
		<div class="formmainbox">
			<div class="subheadnew">TNEB Bill Payment Search</div>
			<table align="center" width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td class="bluebox"><s:text name="payment.billnumber" /></td>
					<td class="bluebox"><s:textfield name="billNumber"
							id="billNumber" maxlength="25" value="%{billNumber}" /></td>
					<td class="bluebox"></td>
					<td class="bluebox"></td>
				</tr>
				<tr>
					<td class="greybox"><s:text name="tnebpayment.monthyear" /></td>
					<td class="greybox"><s:select name="month" id="month"
							list="%{monthMap}" headerKey="" headerValue="----Choose----" />
						<s:select name="year" id="year"
							list="dropdownData.financialYearsList" listKey="id"
							listValue="finYearRange" headerKey=""
							headerValue="----Choose----" /></td>
					<td class="greybox"><s:text name="payment.tneb.bill.region" /><span
						class="mandatory">*</span></td>
					<td class="greybox" id="regionRowId2"><s:select name="region"
							id="region" list="dropdownData.regionsList" headerKey=""
							headerValue="----Choose----" /></td>

					</td>
				</tr>
				<tr>
					<td class="bluebox"><s:text name="payment.expendituretype" /></td>
					<td class="bluebox"><s:property value="%{expType}" /></td>
					<td class="bluebox"><s:text name="voucher.fund" /></td>
					<td class="bluebox"><s:property
							value="%{voucherHeader.fundId.name}" /></td>
				</tr>
				<tr>
					<td class="greybox"><s:text name="voucher.department" /></td>
					<td class="greybox"><s:property
							value="%{voucherHeader.vouchermis.departmentid.deptName}" /></td>
					<td class="greybox"><s:text name="voucher.function" /></td>
					<td class="greybox"><s:property
							value="%{voucherHeader.vouchermis.function.name}" /></td>

				</tr>
			</table>
			<div class="buttonbottom">
				<s:submit method="tnebBills" value="Search" id="searchBtn"
					cssClass="buttonsubmit" onclick="return search()" />
				<input type="button" value="Close"
					onclick="javascript:window.close()" class="button" />
			</div>
		</div>
		<s:if test="%{!validateUser('createpayment')}">
			<script>
			document.getElementById('searchBtn').disabled=true;
			document.getElementById('errorSpan').innerHTML='<s:text name="payment.invalid.user"/>';
			if(document.getElementById('vouchermis.departmentid'))
			{
				var d = document.getElementById('vouchermis.departmentid');
				d.options[d.selectedIndex].text='----Choose----';
				d.options[d.selectedIndex].text.value=-1;
			}
		</script>
		</s:if>
		<s:if test="%{validateUser('deptcheck')}">
			<script>
				if(document.getElementById('vouchermis.departmentid'))
				{
					document.getElementById('vouchermis.departmentid').disabled=true;
				}
			</script>
		</s:if>
		<s:hidden name="disableExpenditureType" id="disableExpenditureType"
			value="%{disableExpenditureType}" />
		<s:hidden name="enablePensionType" id="enablePensionType"
			value="%{enablePensionType}" />
		<s:hidden name="billSubType" id="billSubType" value="%{billSubType}" />
		<s:hidden name="bank_branch" id="bank_branch" />
		<s:hidden name="bank_account" id="bank_account" />
		<s:hidden name="bankaccount" id="bankaccount" />
		<s:hidden name="bankbranch" id="bankbranch" />
		<s:hidden name="expType" id="expType" value="%{expType}" />
		<s:hidden name="voucherHeader.fundId.id" id="voucherHeader.fundId.id"
			value="%{voucherHeader.fundId.id}" />
		<s:hidden name="voucherHeader.vouchermis.function.id"
			id="voucherHeader.vouchermis.function.id"
			value="%{voucherHeader.vouchermis.function.id}" />
		<s:hidden name="voucherHeader.vouchermis.departmentid.id"
			id="voucherHeader.vouchermis.departmentid.id"
			value="%{voucherHeader.vouchermis.departmentid.id}" />
	</s:form>
	<script>
			function search()
			{
				if(document.getElementById('vouchermis.departmentid'))
					document.getElementById('vouchermis.departmentid').disabled=false;
				var region = document.getElementById('region').value;
				if(region == ""){
					 bootbox.alert("Please select Region");
					 return false;
				}
				var month = document.getElementById('month').value;
				var year = document.getElementById('year').value;
				if(month!=""){
					if(year == ""){
						 bootbox.alert("Please select Year");
					  return false;
					}
				}
				if(year!=""){
					if(month == ""){
						 bootbox.alert("Please select Month");
					  return false;
					}
				}
				return true;
			}
			</script>
</body>
</html>
