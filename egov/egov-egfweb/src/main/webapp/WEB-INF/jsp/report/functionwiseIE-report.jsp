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
<title>Functionwise Income/Expense Subsidary Report</title>
</head>
<body>
	<s:form action="functionwiseIE" theme="simple">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading"
				value="Functionwise Income/Expense Subsidary Report" />
		</jsp:include>
		<span class="mandatory"> <s:actionerror /> <s:fielderror /> <s:actionmessage />
		</span>
		<div class="formmainbox">
			<div class="formheading"></div>
			<table align="center" width="80%" cellpadding="0" cellspacing="0">
				<tr>
					<td class="bluebox" width="30%"><s:text
							name="report.income.expense" /><span class="mandatory">*</span></td>
					<td class="bluebox"><s:select name="incExp" id="incExp"
							list="#{'-1':'---Select---','I':'Income','E':'Expenditure'}" />
					</td>
				</tr>
				<tr>
					<td class="greybox" width="30%"><s:text name="report.fromdate" /><span
						class="mandatory">*</span></td>
					<td class="greybox"><s:textfield name="startDate"
							id="startDate" maxlength="20" /><a
						href="javascript:show_calendar('forms[0].startDate');"
						style="text-decoration: none">&nbsp;<img
							src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a><br />(dd/mm/yyyy)</td>
					<td class="greybox" width="30%"><s:text name="report.todate" /><span
						class="mandatory">*</span></td>
					<td class="greybox"><s:textfield name="endDate" id="endDate"
							maxlength="20" /><a
						href="javascript:show_calendar('forms[0].endDate');"
						style="text-decoration: none">&nbsp;<img
							src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)</td>
				</tr>
				<jsp:include page="report-filter.jsp" />
				<tr class="buttonbottom" id="buttondiv" style="align: middle">
					<td align="right"></td>
					<td><s:submit method="search" value="Submit"
							cssClass="buttonsubmit" /></td>
					<td><input type="submit" value="Close"
						onclick="javascript:window.close()" class="button" /></td>
				</tr>
			</table>
			<br />
	</s:form>
</body>
</html>
