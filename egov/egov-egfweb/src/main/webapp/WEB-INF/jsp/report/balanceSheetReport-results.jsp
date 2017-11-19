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
<link href="/EGF/resources/css/budget.css?rnd=${app_release_no}" rel="stylesheet"
	type="text/css" />
<style type="text/css">
@media print {
	div#exportButton {
		display: none;
	}
}
</style>

<s:if test="%{balanceSheet.size()>0}">
	<div id="budgetSearchGrid"
		style="width: 1250px; overflow-x: auto; overflow-y: hidden;">
		<br />
		<div style="overflow-x: scroll; overflow-y: scroll;">

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<div align="center">
							<br />
							<table width="100%" border="0" cellpadding="0" cellspacing="0"
								width="100%">
								<tr>
									<th class="subheadsmallnew" colspan="16" bgcolor="#CCCCCC"><s:property
											value="ulbName" /><br /> <strong><s:text
												name="report.balancesheet.year" /> <s:property
												value="model.financialYear.finYearRange" />
											<s:property value="header" /></strong></th>
									</td>
								</tr>
								<tr>

									<td class="bluebox" colspan="2" align="left"><strong><s:text
												name="report.run.date" />-<s:date name="todayDate"
												format="dd/MM/yyyy" /></strong>
									</div></td>
									<td colspan="16">
										<div class="blueborderfortd" align="right">
											<strong>Amount in <s:property value="model.currency" />
												&nbsp;&nbsp;&nbsp;&nbsp;
											</strong>
										</div>
									</td>
								</tr>
								<tr>
									<th class="bluebgheadtd"><s:text name="report.accountCode" /></th>
									<th class="bluebgheadtd"><s:text
											name="report.headOfAccount" /></th>
									<th class="bluebgheadtd"><s:text name="report.scheduleNo" /></th>
									<s:if test="%{balanceSheet.funds.size()>1}">
										<s:iterator value="balanceSheet.funds" status="stat">
											<th class="bluebgheadtd"><s:property value="name" /></th>
										</s:iterator>
									</s:if>
									<th class="bluebgheadtd"><s:property
											value="currentYearToDate" /></th>
									<th class="bluebgheadtd"><s:property
											value="previousYearToDate" /></th>
								</tr>
								<s:iterator value="balanceSheet.entries" status="stat">
									<tr>
										<td class="blueborderfortd">
											<div align="center">
												<s:if test='%{glCode != ""}'>
													<s:if test='%{displayBold == true}'>
														<strong><s:property value="glCode" /></strong>
													</s:if>
													<s:else>
														<s:property value="glCode" />
													</s:else>
												</s:if>
												&nbsp;
											</div>
										</td>
										<td class="blueborderfortd">
											<div align="left">
												<s:if test='%{scheduleNo == ""}'>
													<strong><s:property value="accountName" /></strong>
												</s:if>
												<s:else>
													<s:property value="accountName" />
												</s:else>
												&nbsp;
											</div>
										</td>
										<td class="blueborderfortd">
											<div align="center">
												<a href="javascript:void(0);"
													onclick='return showSchedule(<s:property value="glCode"/>)'><s:property
														value="scheduleNo" /></a>&nbsp;
											</div>
										</td>
										<s:if test="%{balanceSheet.funds.size()>1}">
											<s:iterator value="balanceSheet.funds" status="stat">
												<td class="blueborderfortd">
													<div align="right">
														<s:if test='%{displayBold == true}'>
															<strong><s:property value="fundWiseAmount[name]" />&nbsp;</strong>
														</s:if>
														<s:else>
															<s:property value="fundWiseAmount[name]" />&nbsp;</s:else>
													</div>
												</td>
											</s:iterator>
										</s:if>
										<td class="blueborderfortd">
											<div align="right">
												<s:if test='%{displayBold == true}'>
													<strong><s:if test='%{currentYearTotal != 0}'>
															<s:property value="currentYearTotal" />
														</s:if>
														<s:else>0.0</s:else></strong>
												</s:if>
												<s:else>
													<s:if test='%{currentYearTotal != 0}'>
														<s:property value="currentYearTotal" />
													</s:if>
													<s:else>0.0</s:else>
												</s:else>
												&nbsp;
											</div>
										</td>
										<td class="blueborderfortd">
											<div align="right">
												<s:if test='%{displayBold == true}'>
													<strong><s:if test='%{previousYearTotal != 0}'>
															<s:property value="previousYearTotal" />
														</s:if>
														<s:else>0.0</s:else></strong>
												</s:if>
												<s:else>
													<s:if test='%{previousYearTotal != 0}'>
														<s:property value="previousYearTotal" />
													</s:if>
													<s:else>0.0</s:else>
												</s:else>
												&nbsp;
											</div>
										</td>
									</tr>
								</s:iterator>
							</table>
						</div>
					</td>
				</tr>
			</table>
			<div class="buttonbottom" id="exportButton">
				<s:text name="report.export.options" />
				: <a
					href='/EGF/report/balanceSheetReport-generateBalanceSheetXls.action?showDropDown=false&model.period=<s:property value="model.period"/>&model.currency=<s:property value="model.currency"/>&model.financialYear.id=<s:property value="model.financialYear.id"/>&model.department.id=<s:property value="model.department.id"/>&model.fund.id=<s:property value="model.fund.id"/>&model.asOndate=<s:property value="model.asOndate"/>&model.function.id=<s:property value="model.function.id"/>&model.functionary.id=<s:property value="model.functionary.id"/>&model.field.id=<s:property value="model.field.id"/>'>Excel</a>
				| <a
					href='/EGF/report/balanceSheetReport-generateBalanceSheetPdf.action?showDropDown=false&model.period=<s:property value="model.period"/>&model.currency=<s:property value="model.currency"/>&model.financialYear.id=<s:property value="model.financialYear.id"/>&model.department.id=<s:property value="model.department.id"/>&model.fund.id=<s:property value="model.fund.id"/>&model.asOndate=<s:property value="model.asOndate"/>&model.function.id=<s:property value="model.function.id"/>&model.functionary.id=<s:property value="model.functionary.id"/>&model.field.id=<s:property value="model.field.id"/>'>PDF</a>
			</div>
		</div>
</s:if>
