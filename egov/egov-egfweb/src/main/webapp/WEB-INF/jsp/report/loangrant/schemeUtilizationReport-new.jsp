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


<html>
<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>
<head>
<script type="text/javascript" language="javascript"
	src="${pageContext.request.contextPath}/resources/javascript/voucherHelper.js?rnd=${app_release_no}"></script>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
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
<script>
function validate()
{
if(document.getElementById("errors"))
{
document.getElementById("errors").innerHTML="";
}
if(document.getElementById("fundId").value=="-1" )
{
bootbox.alert("Select fund");
return false;
}

if(document.getElementById("schemeId").value==null || document.getElementById("schemeId").value=="")
{
   bootbox.alert("Select Scheme");
   return false;
}
return true;
}
</script>
</head>

<body>
	<jsp:include page="../../budget/budgetHeader.jsp">
		<jsp:param value="Scheme Utilization Report" name="heading" />
	</jsp:include>
	<div class="formmainbox">
		<div class="subheadnew">
			<s:text name="scheme.utilization.report" />
		</div>
	</div>
	<div style="color: red" id="errors">
		<s:actionmessage theme="simple" />
		<s:actionerror />
		<s:fielderror />
	</div>
	<s:form name="schemeUtilizationReport" action="schemeUtilizationReport"
		theme="simple">
		<table align="center" width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="bluebox"><s:text name="voucher.fund" /> <s:if
						test="%{defaultFundId==-1}">
						<span class="mandatory">*</span>
					</s:if></td>
				<td class="bluebox"><s:select name="fundId" id="fundId"
						list="dropdownData.fundList" listKey="id" listValue="name"
						headerKey="-1" headerValue="----Choose----" value="%{fundId.id}" /></td>
				<s:if test="%{defaultFundId!=-1}">
					<script>
		document.getElementById("fundId").value='<s:property value="defaultFundId"/>';
		</script>
				</s:if>
			</tr>
			<tr>
				<td class="greybox"><s:text name="voucher.scheme" /> <span
					class="mandatory">*</span></td>
				<s:hidden name="schemeId" id="schemeId" />
				<td class="greybox"><input type="text"
					name="subScheme.scheme.name" id="subScheme.scheme.name"
					autocomplete="off" onFocus="autocompleteSchemeBy20(this);"
					onBlur="splitSchemeCode(this)" /></td>
				<td class="greybox"><s:text name="voucher.subscheme" /></td>
				<s:hidden name="subSchemeId" id="subSchemeId" />
				<td class="greybox"><input type="text" name="subScheme.name"
					id="subScheme.name" autocomplete="off"
					onFocus="autocompleteSubSchemeBy20(this);"
					onBlur="splitSubSchemeCode(this);loadProjectCodes()" /></td>
			</tr>

			<tr>
				<td class="bluebox"><s:text name="voucher.fromdate" /></td>
				<td class="bluebox"><s:textfield name="fromDate" id="fromDate"
						maxlength="20" value="%{fromDate}" /> <a
					href="javascript:show_calendar('forms[0].fromDate');"
					style="text-decoration: none">&nbsp;<img
						src="/egi/resources/erp2/images/calendaricon.gif" border="0"
						alt="" />
				</a> <br /> (dd/mm/yyyy)</td>
				<td class="bluebox"><s:text name="voucher.todate" /></td>
				<td class="bluebox"><s:textfield name="toDate" id="toDate"
						maxlength="20" value="%{toDate}" /> <a
					href="javascript:show_calendar('forms[0].toDate');"
					style="text-decoration: none">&nbsp; <img
						src="/egi/resources/erp2/images/calendaricon.gif" border="0"
						alt="" />
				</a>(dd/mm/yyyy)</td>
			</tr>
		</table>

		<table width="100%">
			<tr>
				<td colspan="4" class="greybox" class="subheadnew"
					style=padding-top:15px; ><div align="center" ><h3> Project Codes</h3></div> </td>
			</tr>
			<tr>
				<td colspan="4" width="100%">
					<div id="projectCodes" align="center" />
				</td>

			</tr>
		</table>
		<table>
			<tr>
				<td>
					<div id="codescontainer" name="codescontainer"></div>
				</td>
			</tr>
		</table>
		<table align="center" class="buttonbottom">
			<tr class="buttonbottom" id="buttondiv" style="align: middle">
				<td><s:submit name="Search" value="Search" method="exportHTML"
						onclick="return validate()" cssClass="button" /></td>
				<td><s:submit value="Export Excel" method="exportXls"
						cssClass="button" onclick="return validate()" /></td>
				<td><s:submit value="Export Pdf" method="exportPdf"
						cssClass="button" onclick="return validate()" /></td>
				<td><input type="button" value="Close"
					onclick="javascript:window.close()" class="button" /></td>
			</tr>
		</table>
	</s:form>

</body>
</html>
