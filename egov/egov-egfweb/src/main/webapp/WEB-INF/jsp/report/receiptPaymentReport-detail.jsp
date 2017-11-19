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
<title><s:text name="receipt.payment.report" /></title>

</head>

<body>
	<div class="formmainbox">
		<div class="subheadnew">
			<s:text name="receipt.payment.report" />
		</div>

		<br />
		<br />

		<s:form name="receiptPaymentReportForm" action="receiptPaymentReport"
			theme="simple">


			<br />
			<br />

			<s:if test="%{model.entries.size!=0}">
				<!-- <div align="center" class="extracontent"><h4><s:property value="fundType"/>  <s:property value="budgetType"/></h4></div> 
		<div align="right" class="extracontent"><b>Amount in Thousands</b></div> -->
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="tablebottom">
					<tr>
						<td colspan="8">
							<div class="subheadsmallnew">
								<strong><s:property value="header" /></strong>
							</div>
						</td>
					</tr>
					<tr>
						<td class="bluebox" colspan="4"><strong><s:text
									name="report.run.date" />:<s:date name="todayDate"
									format="dd/MM/yyyy" /></strong></td>
						<td colspan="8">
							<div class="blueborderfortd" align="right">
								<strong> <s:text name="report.amount.in" /> <s:property
										value="model.currency" />&nbsp;&nbsp;&nbsp;
								</strong>
							</div>
						</td>
					</tr>
					<tr>
					<tr>

						<th class="bluebgheadtd" style="width: 6%; text-align: center"
							align="center"></th>
						<th class="bluebgheadtd" style="width: 38%; text-align: center"
							align="center">Schedule Number</th>
						<th class="bluebgheadtd" style="width: 20%; text-align: center"
							align="center">Glcode</th>
						<s:if test="%{model.funds.size()>1}">
							<s:iterator value="model.funds" status="stat">
								<th class="bluebgheadtd"><s:property value="name" /></th>
							</s:iterator>
						</s:if>
						<th class="bluebgheadtd" style="width: 22%; text-align: center"
							align="center"><s:property value="currentYearToDate" /></th>
						<th class="bluebgheadtd" style="width: 22%; text-align: center"
							align="center"><s:property value="previousYearToDate" /></th>
					</tr>
					<c:set var="trclass" value="greybox" />
					<s:iterator value="model.entries" status="stat">
						<tr>
							<s:if
								test='%{accountName == "Sub Total" || accountName == "Grand Total" }'>
								<td class="blueborderfortd"><strong> <s:property
											value="accountName" />&nbsp;
								</strong></td>
								<td class="blueborderfortd"><s:property value="glCode" />&nbsp;</td>
								<td class="blueborderfortd">&nbsp;</td>
								<s:if test="%{model.funds.size()>1}">
									<s:iterator value="model.funds" status="stat">
										<td class="blueborderfortd">
											<div align="right">
												<strong> <s:property value="fundWiseAmount[code]" />&nbsp;
												</strong>
											</div>
										</td>
									</s:iterator>
								</s:if>
								<td class="blueborderfortd"><strong><s:property
											value="currentYearTotal" />&nbsp;</strong></td>
								<td class="blueborderfortd"><strong> <s:property
											value="previousYearTotal" />&nbsp;
								</strong></td>
							</s:if>
							<s:else>
								<td class="blueborderfortd">&nbsp;</td>
								<td class="blueborderfortd"><strong><s:property
											value="accountName" />&nbsp;</strong></td>
								<td class="blueborderfortd"><s:property value="glCode" />&nbsp;</td>
								<s:if test="%{model.funds.size()>1}">
									<s:iterator value="model.funds" status="stat">
										<td class="blueborderfortd">
											<div align="right">
												<s:property value="fundWiseAmount[code]" />
												&nbsp;
											</div>
										</td>
									</s:iterator>
								</s:if>
								<td class="blueborderfortd"><s:property
										value="currentYearTotal" />&nbsp;</td>
								<td class="blueborderfortd"><s:property
										value="previousYearTotal" />&nbsp;</td>
							</s:else>
						</tr>

					</s:iterator>

				</table>

			</s:if>

			<div class="buttonbottom">
				<s:text name="report.export.options" />
				: <a
					href='/EGF/report/receiptPaymentReport!exportReceiptPaymentScheduleXls.action?
showDropDown=false&model.period=<s:property value="model.period"/>
&model.currency=<s:property value="model.currency"/>
&model.financialYear.id=<s:property value="model.financialYear.id"/>
&model.fund.id=<s:property value="model.fund.id"/>
&model.fromDate=<s:property value="model.fromDate"/>
&model.toDate=<s:property value="model.toDate"/>
&scheduleNo=<s:property value="scheduleNo" />'>Excel</a>
				| <a
					href='/EGF/report/receiptPaymentReport!exportReceiptPaymentSchedulePdf.action?
showDropDown=false&model.period=<s:property value="model.period"/>
&model.currency=<s:property value="model.currency"/>
&model.financialYear.id=<s:property value="model.financialYear.id"/>
&model.fund.id=<s:property value="model.fund.id"/>
&model.fromDate=<s:property value="model.fromDate"/>
&model.toDate=<s:property value="model.toDate"/>
&scheduleNo=<s:property value="scheduleNo" />'>PDF</a>
			</div>


		</s:form>
</body>
</html>
