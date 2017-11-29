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
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<html>
<head>
<title><s:text name="subLedgerSchedule.search.title" /></title>
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
	src="/EGF/resources/javascript/jsCommonMethods.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/subLedgerScheduleHelper.js"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/autocomplete-debug.js"></script>
<link rel="stylesheet" href="/EGF/struts/xhtml/styles.css"
	type="text/css" />
</head>
<script>
  function doAfterSubmit(){
		document.getElementById('loading').style.display ='block';
	}
	
  function populateSubLedger(){
		var glCode= document.getElementById("glcode").value;
		populateaccEntityId({glCode:glCode})
	}
</script>
<body>
	<s:form name="subLedgerScheduleForm" id="subLedgerScheduleForm"
		action="subLedgerScheduleReport" theme="css_xhtml" validate="true">
		<s:push value="subLedgerScheduleReport">
			<div class="formmainbox">
				<div class="subheadnew">
					<s:text name="subLedgerSchedule.search.title" />
				</div>
				<div style="color: red">
					<s:actionerror />
				</div>
				<div style="color: red">
					<s:actionmessage />
				</div>
				<s:hidden name="subLedgerTypeName" id="subLedgerTypeName"
					value="%{subLedgerTypeName}" />
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="bluebox"><s:text name="subLedger.accountCode" /><span
							class="mandatory"></span></td>
						<td class="bluebox"><s:textfield id="glcode" name="glcode"
								value="%{glcode}" autocomplete="off"
								onfocus='autocompleteAccountCodes(this);'
								onblur='splitAccountCodes(this);' /></td>
						<td class="bluebox"><s:text name="subLedger.fund" /><span
							class="mandatory"></span></td>
						<td class="bluebox"><s:select name="fund_id" id="fund_id"
								list="dropdownData.fundList" listKey="id" listValue="name"
								headerKey="" headerValue="----Choose----" /></td>
					</tr>
					<tr>
						<td class="greybox"><s:text name="subLedger.startDate" /><span
							class="mandatory"></span></td>
						<td class="greybox"><s:textfield id="startDate"
								name="startDate" value="%{startDate}"
								onkeyup="DateFormat(this,this.value,event,false,'3')"
								placeholder="DD/MM/YYYY" cssClass="form-control datepicker"
								data-inputmask="'mask': 'd/m/y'" /></td>

						<td class="greybox"><s:text name="subLedger.endDate" /><span
							class="mandatory"></span></td>
						<td class="greybox"><s:textfield id="endDate" name="endDate"
								value="%{endDate}"
								onkeyup="DateFormat(this,this.value,event,false,'3')"
								placeholder="DD/MM/YYYY" cssClass="form-control datepicker"
								data-inputmask="'mask': 'd/m/y'" /></td>
					</tr>
					<tr>
						<egov:ajaxdropdown id="accEntityId" fields="['Text','Value']"
							dropdownId="accEntityId"
							url="voucher/common-ajaxLoadSubLedgerTypesByGlCode.action" />
						<td class="bluebox"><s:text name="subLedger.subLedgerType" /><span
							class="mandatory"></span></td>
						<td class="bluebox"><s:select name="accEntityId"
								id="accEntityId" value='%{accEntityId}'
								list="dropdownData.subLedgerTypeList" listKey="id"
								listValue="description" headerKey=""
								headerValue="----Choose----" /></td>
						<td class="bluebox"><s:text name="subLedger.department" /></td>
						<td class="bluebox"><s:select name="deptId" id="deptId"
								value='%{deptId}' list="dropdownData.departmentList"
								listKey="id" listValue="name" headerKey=""
								headerValue="----Choose----" /></td>
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


		<div id="resultDiv" style="display: none;">
			<jsp:include page="subLedgerScheduleReport-result.jsp" />
		</div>

		<div id="codescontainer" />
	</s:form>
</body>
</html>
