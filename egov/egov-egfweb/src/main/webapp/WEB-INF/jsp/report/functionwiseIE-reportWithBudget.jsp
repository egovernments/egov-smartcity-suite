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
<script>
    function validate()
    {
	    if(document.getElementById('incExp').value==-1)
	    {
	     bootbox.alert("Select Income/ expenditure");
	     return false;
	     }
	     if(document.getElementById('asOnDate').value=="")
	    {
	     bootbox.alert("Select As On Date");
	     return false;
	     }
	     if(document.getElementById('fund').value==-1)
	    {
	     bootbox.alert("Select Fund");
	     return false;
	     }  
	   if(document.getElementById('function').value==-1)
	    {
	     bootbox.alert("Select Function");
	     return false;
	     }
	   
     return true;
    }
    </script>
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
					<td class="greybox" width="30%">Income/ expenditure<span
						class="mandatory">*</span></td>
					<td class="greybox"><s:select name="incExp" id="incExp"
							list="#{'-1':'---Select---','I':'Income','E':'Expenditure'}" />
					</td>
					<td class="greybox" width="30%">As On Date<span
						class="mandatory">*</span></td>
					<td class="greybox"><s:textfield name="asOnDate" id="asOnDate"
							maxlength="20" /><a
						href="javascript:show_calendar('forms[0].asOnDate');"
						style="text-decoration: none">&nbsp;<img
							src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a><br />(dd/mm/yyyy)</td>
				</tr>
				<tr>
					<td class="bluebox"><s:text name="voucher.fund" /><span
						class="mandatory">*</span>
					<td class="bluebox"><s:select name="fund" id="fund"
							list="dropdownData.fundList" listKey="id" listValue="name"
							headerKey="-1" headerValue="----Choose----" value="%{fund.id}" /></td>
					<td class="bluebox"><s:text name="voucher.function" /><span
						class="mandatory">*</span>
					<td class="bluebox"><s:select name="function" id="function"
							list="dropdownData.functionList" listKey="id" listValue="name"
							headerKey="-1" headerValue="----Choose----"
							value="%{function.id}" /></td>
				</tr>
			</table>
			<div class="buttonbottom" id="buttondiv" style="align: middle">
				<CENTER>
					<table>
						<tr>
							<td align="right"></td>
							<td><s:submit method="searchWithBudget" value="Submit"
									cssClass="buttonsubmit" onclick="return validate()" /></td>
							<td><input type="button" value="Close"
								onclick="javascript:window.close()" class="button" /></td>
						</tr>
					</table>
				</CENTER>
			</div>
			<br />
	</s:form>
</body>
</html>
