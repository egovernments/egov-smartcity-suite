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
<title><s:text name="bill.salarybill.register" /></title>
<link href="/EGF/resources/css/budget.css?rnd=${app_release_no}" rel="stylesheet"
	type="text/css" />
<link href="/EGF/css/commonegov.css" rel="stylesheet" type="text/css" />
<script>
var callback = {
		success: function(o){
			document.getElementById('results').innerHTML=o.responseText;
			},
			failure: function(o) {
		    }
		};
function searchBills(){
	var fromDate =  document.getElementById('fromDate').value;
	var toDate = document.getElementById('toDate').value;
	var dept = document.getElementById('department').value;
	var month = document.getElementById('month').value;
	isValid = validateDates();
	if(isValid == false)
		return false;
	var url = '../bill/salaryBillRegisterView!ajaxSearch.action?fromDate='+fromDate+'&toDate='+toDate+'&department.id='+dept+'&month='+month;
	YAHOO.util.Connect.asyncRequest('POST', url, callback, null);
}

function validateDates(){
	var fromDate =  Date.parse(document.getElementById('fromDate').value);
	var toDate = Date.parse(document.getElementById('toDate').value);
	if(isNaN(toDate) || isNaN(fromDate)){
		bootbox.alert("Please enter valid dates")
		return false;
	}
	if (toDate < fromDate){
		bootbox.alert("From date should be greater than To date")
		return false;
	}
	if(fromDate == '' || toDate == ''){
		bootbox.alert("Please select the dates")
		return false;
	}
	return true;	
}
</script>
</head>
<body>
	<s:form action="salaryBillRegisterView" theme="simple"
		name="salaryBillView">
		<span class="mandatory"> <s:actionerror /> <s:fielderror /> <s:actionmessage />
		</span>
		<div class="formmainbox">
			<div class="subheadnew">
				<s:text name="bill.salarybill.register.view" />
			</div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="9%" class="bluebox">&nbsp;</td>
					<td width="18%" class="bluebox"><s:text name="billDate" /></>:<span
						class="mandatory">*</span></td>
					<td width="23%" class="bluebox"><input type="text"
						name="fromDate" id="fromDate"
						onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
						href="javascript:show_calendar('salaryBillView.fromDate');"
						style="text-decoration: none">&nbsp;<img tabIndex="-1"
							src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></A>(dd/mm/yyyy)</td>
					</td>
					<td width="18%" class="bluebox"><s:text name="billDate" /></>:<span
						class="mandatory">*</span></td>
					<td width="23%" class="bluebox"><input type="text"
						name="toDate" id="toDate"
						onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
						href="javascript:show_calendar('salaryBillView.toDate');"
						style="text-decoration: none">&nbsp;<img tabIndex="-1"
							src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></A>(dd/mm/yyyy)</td>
					</td>
				</tr>
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox">For the Month of:</td>
					<td class="greybox"><select name="month" id="month">
							<option value="-1" selected="selected">------Choose-----</option>
							<option value="1">January</option>
							<option value="2">February</option>
							<option value="3">March</option>
							<option value="4">April</option>
							<option value="5">May</option>
							<option value="6">June</option>
							<option value="7">July</option>
							<option value="8">August</option>
							<option value="9">September</option>
							<option value="10">October</option>
							<option value="11">November</option>
							<option value="12">December</option>
					</select></td>
					<td class="greybox"><s:text name="department" />:</td>
					<td class="greybox"><s:select name="department.id"
							id="department" list="dropdownData.departmentList" listKey="id"
							listValue="name" headerKey="-1" headerValue="----Choose----" /></td>
				</tr>
				</tr>
			</table>

			<div class="buttonbottom">
				<input type="button" class="buttonsubmit" id="search" name="search"
					value="Search" onClick="return searchBills();" />
				<s:reset name="button" type="submit" cssClass="button" id="button"
					value="Cancel" />
				<s:submit value="Close" onclick="javascript: self.close()"
					cssClass="button" />
			</div>
		</div>
		<div id="results"></div>
	</s:form>
</body>
</html>
