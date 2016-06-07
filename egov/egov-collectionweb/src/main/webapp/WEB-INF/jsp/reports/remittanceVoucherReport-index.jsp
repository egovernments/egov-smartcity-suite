<%@ include file="/includes/taglibs.jsp"%>
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

<head>
<title><s:text name="remittanceVoucherReport.title" /></title>
<script>
function onChangeBankAccount(branchId) {
	populatebankAccountId({
		branchId : branchId,
	});
}

function onChangeServiceList(bankAccountId) {
	populateserviceId({
		bankAccountId : bankAccountId,
	});
}


function validate()
{
	var fromdate=dom.get("fromDate").value;
	var todate=dom.get("toDate").value;
	var valSuccess = true;
	document.getElementById("report_error_area").innerHTML = "";
	document.getElementById("report_error_area").style.display="none"; 
	

		if (fromdate == "") {
			document.getElementById("report_error_area").style.display = "block";
			document.getElementById("report_error_area").innerHTML += '<s:text name="common.datemandatory.fromdate" />'
					+ '<br>';
			valSuccess = false;
		}

		if (todate == "") {
			document.getElementById("report_error_area").style.display = "block";
			document.getElementById("report_error_area").innerHTML += '<s:text name="common.datemandatory.todate" />'
					+ '<br>';
			valSuccess = false;
		}

		if (fromdate != "" && todate != "" && fromdate != todate) {
			if (!checkFdateTdate(fromdate, todate)) {
				document.getElementById("report_error_area").style.display = "block";
				document.getElementById("report_error_area").innerHTML += '<s:text name="common.comparedate.errormessage" />'
						+ '<br>';
				valSuccess = false;
			}
			if (!validateNotFutureDate(fromdate, currDate)) {
				document.getElementById("report_error_area").style.display = "block";
				document.getElementById("report_error_area").innerHTML += '<s:text name="reports.fromdate.futuredate.message" />'
						+ '<br>';
				valSuccess = false;
			}
			if (!validateNotFutureDate(todate, currDate)) {
				document.getElementById("report_error_area").style.display = "block";
				document.getElementById("report_error_area").innerHTML += '<s:text name="reports.todate.futuredate.message" />'
						+ '<br>';
				valSuccess = false;
			}
		}

		return valSuccess;
	}
</script>
</head>
<body>
<div class="errorstyle" id="report_error_area" style="display:none;"></div>
<s:form theme="simple" name="remittanceVoucherReportForm"
	action="remittanceVoucherReport-report.action">
	<div class="formmainbox">
	<div class="subheadnew"><s:text name="remittanceVoucherReport.title" /></div>
	<div class="subheadsmallnew"><span class="subheadnew"><s:text
		name="collectionReport.criteria" /></span></div>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="4%" class="bluebox">&nbsp;</td>
			<td width="21%" class="bluebox"><s:text
				name="collectionReport.criteria.fromdate" /><span class="mandatory"></span></td>
			<s:date name="fromDate" var="cdFormat" format="dd/MM/yyyy" />
			<td width="24%" class="bluebox"><s:textfield id="fromDate"
				name="fromDate" value="%{cdFormat}"
				onfocus="javascript:vDateType='3';"
				onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
				href="javascript:show_calendar('forms[0].fromDate');"
				onmouseover="window.status='Date Picker';return true;"
				onmouseout="window.status='';return true;"><img
				src="/egi/resources/erp2/images/calendaricon.gif"
				alt="Date" width="18" height="18" border="0" align="absmiddle" /></a>
			<div class="highlight2" style="width: 80px">DD/MM/YYYY</div>
			</td>
			<td width="21%" class="bluebox"><s:text
				name="collectionReport.criteria.todate" /><span class="mandatory"></span></td>
			<s:date name="toDate" var="cdFormat1" format="dd/MM/yyyy" />
			<td width="24%" class="bluebox"><s:textfield id="toDate"
				name="toDate" value="%{cdFormat1}"
				onfocus="javascript:vDateType='3';"
				onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
				href="javascript:show_calendar('forms[0].toDate');"
				onmouseover="window.status='Date Picker';return true;"
				onmouseout="window.status='';return true;"><img
				src="/egi/resources/erp2/images/calendaricon.gif"
				alt="Date" width="18" height="18" border="0" align="absmiddle" /></a>
			<div class="highlight2" style="width: 80px">DD/MM/YYYY</div>
			</td>
		</tr>
	
		<tr>
		 <td width="4%" class="bluebox">&nbsp;</td>
			<td width="21%" class="bluebox2"><s:text
								name="collectionReport.bank.name" />:</td>
						<td width="24%" class="bluebox"><s:select
								headerValue="--Select--" headerKey="-1"
								list="dropdownData.bankBranchList" listKey="id"
								id="bankBranchMaster" listValue="branchname"
								label="bankBranchMaster" name="branchId"
								value="%{branchId}"
								onChange="onChangeBankAccount(this.value)" /> <egov:ajaxdropdown
								id="accountNumberIdDropdown" fields="['Text','Value']"
								dropdownId='bankAccountId'
								url='receipts/ajaxBankRemittance-accountListOfService.action'
								selectedValue="%{bankAccountId}" /></td>
		            	<td width="21%" class="bluebox2"><s:text
								name="collectionReport.criteria.bankaccount" />:</td>
						<td width="24%" class="bluebox"><s:select
								headerValue="--Select--" headerKey="-1"
								list="dropdownData.accountNumberList" listKey="id"
								id="bankAccountId" listValue="accountnumber"
								label="accountNumberMaster" name="bankAccountId"
								value="%{bankAccountId}" 
								onChange="onChangeServiceList(this.value)" /> <egov:ajaxdropdown
								id="serviceIdDropdown" fields="['Text','Value']"
								dropdownId='serviceId'
								url='receipts/ajaxBankRemittance-serviceListOfAccount.action'
								selectedValue="%{serviceId}" /></td></td>
		</tr>
		<tr>
         <td width="4%" class="bluebox">&nbsp;</td>
			<td width="21%" class="bluebox2"><s:text
				name="collectionReport.criteria.service" /></td>
			<td width="24%" class="bluebox2"><s:select headerKey="-1"
				headerValue="%{getText('collectionReport.service.all')}" name="serviceId" id="serviceId" cssClass="selectwk"
				list="dropdownData.collectionServiceList" listKey="id" listValue="name"
				value="%{serviceId}" /></td>
			<td width="21%" class="bluebox2">
				<s:text name="collectionReport.criteria.user"/></td>
	        	<td width="24%" class="bluebox2"><s:select headerKey="-1"
				headerValue="%{getText('collectionReport.user.all')}" name="createdById" id="createdById" cssClass="selectwk"
				list="dropdownData.remittanceVoucherCreatorList" listKey="id" listValue="name"
				value="%{createdById}" /></td>
		</tr>
	</table>
<div align="left" class="mandatorycoll"><s:text name="common.mandatoryfields"/></div>
	</div>
	
	<div class="buttonbottom">
			<label>
				<s:submit type="submit" cssClass="buttonsubmit" id="button"
					value="%{getText('collectionReport.create')}"
					onclick="return validate();" />
			</label>&nbsp;
			<label>
				<s:reset type="submit" cssClass="button"
					value="%{getText('collectionReport.reset')}"
					onclick="return clearErrors();" />
			</label>&nbsp;
			<label>
				<input type="button" class="button" id="buttonClose"
					value="<s:text name='common.buttons.close'/>"
					onclick="window.close()" />
			</label>
		</div>
</s:form>
</body>
</html>
