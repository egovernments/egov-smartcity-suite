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
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="/EGF/resources/css/ccMenu.css?rnd=${app_release_no}" />
<title>Cheque Assignment View</title>
</head>
<body>
	<s:form action="chequeAssignment" theme="simple">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading" value="Cheque Assignment View" />
		</jsp:include>
		<span class="mandatory1"> <s:actionerror /> <s:fielderror /> <s:actionmessage />
		</span>
		<div class="formmainbox">
			<div class="subheadnew">
				<s:text name="chq.assignment.heading.view" />
			</div>
			<table align="center" width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<s:if test="%{paymentMode=='cheque'}">
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.partycode" /></th>

						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.instrument.serialno" /></th>
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.instrument.no" /></th>
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.transacton.no" /></th>
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.instrument.amount" /></th>
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.instrument.date" /></th>
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.transaction.date" /></th>
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.instrument.status" /></th>
					</s:if>

				</tr>
				<s:if test="%{paymentMode=='cheque'}">
					<s:iterator var="p" value="instHeaderList" status="s">
						<tr>
							<td style="text-align: center" class="blueborderfortdnew"><s:property
									value="%{payTo}" /></td>
							<td style="text-align: center" class="blueborderfortdnew"><s:property
									value="%{serialNo.finYearRange}" /></td>
							<td style="text-align: center" class="blueborderfortdnew"><s:property
									value="%{instrumentNumber}" /></td>
							<td style="text-align: center" class="blueborderfortdnew"><s:property
									value="%{transactionNumber}" /></td>
							<td style="text-align: right" class="blueborderfortdnew"><s:text
									name="format.number">
									<s:param value="%{instrumentAmount}" />
								</s:text></td>
							<td style="text-align: center" class="blueborderfortdnew"><s:date
									name="%{instrumentDate}" format="dd/MM/yyyy" /></td>
							<td style="text-align: center" class="blueborderfortdnew"><s:date
									name="%{transactionDate}" format="dd/MM/yyyy" /></td>
							<td style="text-align: center" class="blueborderfortdnew"><s:property
									value="%{statusId.description}" /></td>
						</tr>
					</s:iterator>
				</s:if>

			</table>
			<br />
			<div class="buttonbottom">


				<input type="button" value="Close"
					onclick="javascript:window.close()" class="buttonsubmit" />
			</div>
		</div>
	</s:form>
</body>
</html>
