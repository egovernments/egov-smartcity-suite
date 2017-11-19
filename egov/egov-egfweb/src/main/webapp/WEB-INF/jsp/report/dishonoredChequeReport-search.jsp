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
<title><s:text name="dishonoredCheque.search.title" /></title>
<style type="text/css">
#codescontainer {
	position: absolute;
	left: 11em;
	width: 9%;
	text-align: left;
}

#codescontainer .yui-ac-content {
	position: absolute;
	width: 600px;
	border: 1px solid #404040;
	background: #fff;
	overflow: hidden;
	z-index: 9050;
}

#codescontainer .yui-ac-shadow {
	position: absolute;
	margin: .3em;
	width: 300px;
	background: #a0a0a0;
	z-index: 9049;
}

#codescontainer ul {
	padding: 5px 0;
	width: 100%;
}

#codescontainer li {
	padding: 0 5px;
	cursor: default;
	white-space: nowrap;
}

#codescontainer li.yui-ac-highlight {
	background: #ff0;
}

#codescontainer li.yui-ac-prehighlight {
	background: #FFFFCC;
}
</style>
<script type="text/javascript"
	src="/EGF/resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/calender.js"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/calendar.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/dateValidation.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/dishonoredChequeHelper.js"></script>
<link rel="stylesheet" href="/EGF/struts/xhtml/styles.css"
	type="text/css" />
<div id="loading"
	style="position: absolute; left: 25%; top: 70%; padding: 2px; z-index: 20001; height: auto; width: 500px; display: none;">
	<div class="loading-indicator"
		style="background: white; color: #444; font: bold 13px tohoma, arial, helvetica; padding: 10px; margin: 0; height: auto;">
		<img src="/egi/resources/erp2/images/loading.gif" width="32"
			height="32" style="margin-right: 8px; vertical-align: top;" />
		Loading...
	</div>
</div>
</head>

<body onload="return onBodyLoad()">
	<s:form name="dishonoredChequeForm" id="dishonoredChequeForm"
		action="dishonoredChequeReport" theme="css_xhtml" validate="true">
		<s:push value="dishonoredChequeReport">
			<div class="formmainbox">
				<div class="subheadnew">
					<s:text name="dishonoredCheque.search.title" />
				</div>
				<div style="color: red">
					<s:actionerror />
				</div>
				<div style="color: red">
					<s:actionmessage />
				</div>
				<s:hidden name="showMode" value="%{showMode}" id="showMode" />
				<table width="100%" border="0" cellspacing="0" cellpadding="0">

					<tr>
						<td class="bluebox"><s:text
								name="dishonoredCheque.chequeOrDDNumber" /></td>
						<td class="bluebox"><s:textfield name="chequeNo"
								id="chequeNo" /></td>
						<td class="bluebox"></td>
						<td class="bluebox"></td>
					</tr>
					<tr>
						<td class="greybox"><s:text name="dishonoredCheque.fund" /></td>
						<td class="greybox"><s:select name="fundLst" id="fundLst"
								list="dropdownData.fundList" listKey="id" listValue="name"
								headerKey="" headerValue="----Choose----" /></td>
						<td class="greybox"><s:text
								name="dishonoredCheque.modeofPaymentorReceipt" /><span
							class="mandatory">*</span></td>
						<td class="greybox"><s:select name="mode" id="mode"
								list="#{'1':'DD','2':'Cheque'}" headerKey=""
								headerValue="----Choose----" /></td>

					</tr>
					<tr>
						<td class="bluebox"><s:text name="dishonoredCheque.startDate" /><span
							class="mandatory">*</span></td>
						<td class="bluebox"><s:textfield name="startDate"
								id="startDate" cssStyle="width:100px" value='%{startDate}'
								onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
							href="javascript:show_calendar('dishonoredChequeForm.startDate');"
							style="text-decoration: none"><img
								src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)</td>
						<td class="bluebox"><s:text name="dishonoredCheque.endDate" /></td>
						<td class="bluebox"><s:textfield name="endDate" id="endDate"
								cssStyle="width:100px" value='%{endDate}'
								onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
							href="javascript:show_calendar('dishonoredChequeForm.endDate');"
							style="text-decoration: none"><img
								src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)</td>
					</tr>




				</table>
				<br />
			</div>
			<div class="buttonbottom">
				<table align="center">
					<tr>
						<td><input type="button" value="Search" class="buttonsubmit"
							onclick="return validate()" /></td>
						<td><input type="button" id="Close" value="Close"
							onclick="javascript:window.close()" class="button" /></td>
					</tr>
				</table>
			</div>
		</s:push>

		<span class="mandatory">
			<div id="resultDiv" style="display: none;">
				<jsp:include page="dishonoredChequeReport-results.jsp" />
			</div>

		</span>

		<div id="codescontainer" />
	</s:form>
</body>
</html>
