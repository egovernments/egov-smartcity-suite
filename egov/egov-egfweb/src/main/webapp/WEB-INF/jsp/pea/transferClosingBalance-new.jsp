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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Transfer Closing Balance</title>
<script type="text/javascript">
	function transfer() {
		document.getElementById("msg").innerHTML = "";
		document.getElementById("Errors").innerHTML = "";

		if (document.getElementById("financialYear").value == -1) {
			bootbox.alert("Select Financial Year");
			return false;
		}
		document.transferClosingBalance.action = '/EGF/pea/transferClosingBalance-transfer.action';
		return true;
	}
</script>
</head>
<body>
	<s:form action="transferClosingBalance" theme="simple" name="transferClosingBalance"
		id="transferClosingBalance" method="post">
		<div class="formmainbox">
			<div class="formheading"></div>
			<div class="subheadnew">Transfer Closing Balance</div>

			<div align="center">
				<font style='color: red;'>
					<div id="msg">
						<s:property value="message" />
					</div>
					<p class="error-block" id="lblError"></p>
				</font>
			</div>
			<span class="mandatory1">
				<div id="Errors">
					<s:actionerror />
					<s:fielderror />
				</div> <s:actionmessage />
			</span>

			<center>
				<table border="0" width="100%" cellspacing="0" cellpadding="0">
					<tr>
						<td width="50%">
							<table border="0" width="100%" cellspacing="0" cellpadding="0">
								<tr>
									<td class="greybox" ></td>
									<td class="greybox"></td>
									<td class="greybox">Financial Year <span class="greybox"><span
											class="mandatory1">*</span></span></td>
									<td class="greybox"><s:select name="financialYear" id="financialYear"
											list="dropdownData.financialYearList" listKey="id" listValue="finYearRange"
											headerKey="-1" headerValue="----Choose----"
											value="%{financialYear}" /></td>
									<span class="greybox" colspan="2">
										</td>
										<td class="greybox"></td>
								</tr>

							</table>
						</td>
					</tr>
				</table>
				<div class="buttonbottom" id="buttondiv">
					<table>
						<tr>

							<td><s:submit type="submit" cssClass="buttonsubmit"
									value="Transfer" name="transfer" method="transfer"
									onclick="return transfer();" /></td>
							<td><input type="button" value="Close"
								onclick="javascript:window.close()" class="buttonsubmit" /></td>
						</tr>
					</table>
				</div>
			</center>
		</div>

	</s:form>
</body>
</html>
