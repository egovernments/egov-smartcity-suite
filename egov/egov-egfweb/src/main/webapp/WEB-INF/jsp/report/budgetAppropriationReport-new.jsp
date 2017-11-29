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


<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<link href="/EGF/resources/css/budget.css?rnd=${app_release_no}"
	rel="stylesheet" type="text/css" />
<script>
	var budName = "";
	var depName = "";
	var fndName = "";
	var funcName = "";
	var callback = {
		success : function(o) {
			document.getElementById('result').innerHTML = o.responseText;
			undoLoadingMask();
		},
		failure : function(o) {
			undoLoadingMask();
		}
	}

	function getData() {
		if (validateMandatoryField()) {
			//doAfterSubmit();
			getFilterName();
			doLoadingMask();
			var frmdate = document.getElementById('fromDate').value;
			var todate = document.getElementById('toDate').value;
			//bootbox.alert("<<<>>>"+budName+"<<>>"+depName+"<<>>"+fndName+"<<<>>>"+funcName);
			var url = '/EGF/report/budgetAppropriationReport-ajaxGenerateReport.action?showDropDown=false'
					+ '&model.budgetDetail.executingDepartment.id='
					+ document.getElementById('department').value
					+ '&model.budgetDetail.function.id='
					+ document.getElementById('function').value
					+ '&model.budgetDetail.fund.id='
					+ document.getElementById('fund').value
					+ '&model.budgetDetail.budget.id='
					+ document.getElementById('budget').value
					+ '&budgetName='
					+ budName
					+ '&fromDate='
					+ frmdate
					+ '&toDate='
					+ todate
					+ '&deptName='
					+ depName
					+ '&fundName='
					+ fndName
					+ '&functionName=' + funcName;

			YAHOO.util.Connect.asyncRequest('POST', url, callback, null);
			return true;
		}
		return false;
	}
	function getFilterName() {
		budName = document.getElementById("budget").options[document
				.getElementById("budget").selectedIndex].text;
		if (document.getElementById("function").value != null
				&& document.getElementById("function").value != '') {
			funcName = document.getElementById("function").options[document
					.getElementById("function").selectedIndex].text;
		} else {
			funcName = "";
		}
		if (document.getElementById("department").value != null
				&& document.getElementById("department").value != '') {
			depName = document.getElementById("department").options[document
					.getElementById("department").selectedIndex].text;
		} else {
			depName = "";
		}
		if (document.getElementById("fund").value != null
				&& document.getElementById("fund").value != '') {
			fndName = document.getElementById("fund").options[document
					.getElementById("fund").selectedIndex].text;
		} else {
			fndName = "";
		}
	}

	function validateMandatoryField() {
		var budgetval = document.getElementById('budget');
		var frmdate = document.getElementById('fromDate').value;
		var todate = document.getElementById('toDate').value;
		if (budgetval.value == null || budgetval.value == '') {
			bootbox.alert("Please Select Budget");
			return false;
		}
		if (frmdate == '' || todate == '') {
			bootbox.alert("Please Select Date Range");
			return false;
		}
		doLoadingMask();
		return true;
	}
</script>

<style type="text/css">
@media print {
	#non-printable {
		display: none;
	}
}
</style>

<style>
th.bluebgheadtd {
	padding: 0px;
	margin: 0px;
}

.extracontent {
	font-weight: bold;
	font-size: xx-small;
	color: #CC0000;
}
</style>
<div id="non-printable">
	<s:form name="budgetAppropriationReport"
		action="budgetAppropriationReport" theme="simple">
		<div class="formmainbox">
			<div class="subheadnew">Budget Addition/Deduction Appropriation
				Report</div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">

				<tr>
					<td class="bluebox" width="4%"></td>
					<td class="bluebox" width="8%"><s:text name="report.fund" /></td>
					<td class="bluebox"><s:select name="budgetDetail.fund"
							id="fund" list="dropdownData.fundDropDownList" listKey="id"
							listValue="name" headerKey="" headerValue="----Select----"
							value="budgetDetail.fund.id" /></td>

					<td class="bluebox"><s:text name="report.department" /></td>
					<td class="bluebox" width="8%"><s:select
							name="budgetDetail.executingDepartment" id="department"
							list="dropdownData.departmentList" listKey="id" listValue="name"
							headerKey="" headerValue="----Select----"
							value="budgetDetail.executingDepartment.id" /></td>

				</tr>
				<tr>
					<td class="greybox" width="4%"></td>
					<td class="greybox" width="8%"><s:text name="report.function" /></td>
					<td class="greybox" width="8%"><s:select
							name="budgetDetail.function" id="function"
							list="dropdownData.functionList" listKey="id" listValue="name"
							headerKey="" headerValue="----Select----"
							value="budgetDetail.function.id" /></td>

					<td class="greybox" width="8%"><s:text name="report.budget" /><span
						class="mandatory1">*</span></td>
					<td class="greybox"><s:select name="budgetDetail.budget"
							id="budget" list="dropdownData.budList" listKey="id"
							listValue="name" headerKey="" headerValue="----Select----"
							value="%{budgetDetail.budget.id}" /></td>

				</tr>
				<tr>
					<td class="bluebox" width="4%"></td>
					<td class="bluebox" width="8%"><s:text name="report.fromdate" /><span
						class="mandatory1">*</span></td>
					<td class="bluebox" width="8%"><s:date name="fromDate"
							format="dd/MM/yyyy" var="fromDateId" /> <s:textfield
							id="fromDate" name="budgetAppropriationReport.fromDate"
							value="%{fromDateId}"
							onkeyup="DateFormat(this,this.value,event,false,'3')"
							placeholder="DD/MM/YYYY" cssClass="form-control datepicker"
							data-inputmask="'mask': 'd/m/y'" /></td>
					<td class="bluebox" width="8%"><s:text name="report.todate" /><span
						class="mandatory1">*</span></td>
					<td class="bluebox"><s:date name="toDate" var="toDateId"
							format="dd/MM/yyyy" /> <s:textfield id="toDate"
							name="budgetAppropriationReport.toDate" value="%{toDateId}"
							onkeyup="DateFormat(this,this.value,event,false,'3')"
							placeholder="DD/MM/YYYY" cssClass="form-control datepicker"
							data-inputmask="'mask': 'd/m/y'" /></td>
				</tr>
			</table>
			<div align="left" class="mandatory1">
				*
				<s:text name="report.mandatory.fields" />
			</div>

			<div class="buttonbottom" style="padding-bottom: 10px;">
				<input type="hidden" id="budgetName" name="budgetName" /> <input
					type="hidden" id="fundName" name="fundName" /> <input
					type="hidden" id="functionName" name="functionName" /> <input
					type="hidden" id="deptName" name="deptName" /> <input
					type="button" value="Submit" class="buttonsubmit"
					onclick="return getData()" /> <input type="button" value="Close"
					onclick="javascript:window.close()" class="button" />
			</div>

		</div>
	</s:form>
</div>

<div id="result"></div>
