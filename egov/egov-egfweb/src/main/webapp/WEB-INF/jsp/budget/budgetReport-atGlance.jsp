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
<title>Budget Report - Functionwise</title>
<script type="text/javascript">
    
    function validateFinYear()
	{
		if(document.getElementById('financialYear').value==0)
		{
			bootbox.alert('Please select a financial year');
			return false;
		}
		else
			return true;
	}
    </script>
</head>
<body>
	<s:form action="budgetReport" theme="simple">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading" value="Budget Report - Functionwise" />
		</jsp:include>
		<div class="formmainbox">
			<div class="subheadnew">Budget Report - Budget At A Glance</div>
			<table>
				<tr>
					<td class="greybox" width="5%" />
					<td class="greybox"><s:text name="report.financialYear" /> <span
						class="mandatory">*</span></td>
					<td class="greybox"><s:select name="financialYear"
							id="financialYear" list="dropdownData.financialYearList"
							listKey="id" listValue="finYearRange" headerKey="0"
							headerValue="----Select----" value="%{model.financialYear.id}" />
					</td>
					<td class="bluebox" id="function_label" style="visibility: visible"><s:text
							name="report.function" /></td>
					<td class="bluebox"><s:select name="function" id="function"
							list="dropdownData.functionList" listKey="id" listValue="name"
							headerKey="-1" headerValue="----Select----"
							value="%{function.id}" /></td>
				</tr>
			</table>
			<s:hidden name="onSaveOrForward" value="true" />
			<div class="buttonbottom" style="padding-bottom: 10px;">
				<s:submit value="Submit" method="getAtGlanceReport"
					cssClass="buttonsubmit" onclick="return validateFinYear()" />
				<input type="button" value="Close"
					onclick="javascript:window.close()" class="button" />
			</div>
		</div>
	</s:form>
</body>
</html>
