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
	div#ieReport {
		display: none;
	}
}
</style>

<div id="budgetSearchGrid"
	style="width: 1250px; overflow-x: auto; overflow-y: hidden;">
	<br />
	<div style="overflow-x: scroll; overflow-y: scroll;">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td>
					<div align="center">
						<br />
						<table border="0" cellspacing="0" cellpadding="0"
							class="tablebottom" width="100%">
							<tr>
								<td colspan="12">
									<div class="subheadsmallnew">
										<strong><s:property value="statementheading" /></strong>
									</div>
								</td>
							</tr>
							<tr>
								<td class="bluebox" colspan="4"><strong><s:text
											name="report.run.date" />:<s:date name="todayDate"
											format="dd/MM/yyyy" /></strong></td>
								<td colspan="11">
									<div class="blueborderfortd" align="right">
										<strong> <s:text name="report.amount.in" /> <s:property
												value="model.currency" />&nbsp;&nbsp;&nbsp;&nbsp;
										</strong>
									</div>
								</td>
							</tr>
							<tr>
								<th class="bluebgheadtd"><s:text name="report.accountCode" /></th>
								<th class="bluebgheadtd"><s:text
										name="report.headOfAccount" /></th>
								<th class="bluebgheadtd"><s:text name="report.scheduleNo" /></th>
								<th class="bluebgheadtd"><s:text
										name="report.revisedestimate" />
									<s:property
										value="incomeExpenditureStatement.financialYear.finYearRange" /></th>
								<s:iterator value="incomeExpenditureStatement.funds"
									status="stat">
									<th class="bluebgheadtd" width="30%" colspan="2"><s:property
											value="name" />(Rs)</th>
								</s:iterator>
							</tr>
							<tr>
								<th class="bluebgheadtd"><s:text name="" /></th>
								<th class="bluebgheadtd"><s:text name="" /></th>
								<th class="bluebgheadtd"><s:text name="" /></th>
								<th class="bluebgheadtd"><s:text name="" /></th>
								<s:if test="%{incomeExpenditureStatement.getFunds().size()==1}">
									<s:iterator value="incomeExpenditureStatement.funds"
										status="stat">
										<th class="bluebgheadtd" width="15%" align="center"
											colspan="1"><s:property value="currentYearToDate" />(Rs)</th>
										<th class="bluebgheadtd" width="15%" align="center"
											colspan="1"><s:property value="previousYearToDate" />(Rs)</th>
									</s:iterator>
								</s:if>
								<s:else>
									<s:iterator value="incomeExpenditureStatement.funds"
										status="stat">
										<th class="bluebgheadtd" width="15%" align="center"
											colspan="1"><s:text name="report.currentTotals" />
											<s:property value="currentYearToDate" /></th>
										<th class="bluebgheadtd" width="15%" align="center"
											colspan="1"><s:text name="report.previousTotals" />
											<s:property value="previousYearToDate" /></th>
									</s:iterator>
								</s:else>
							</tr>
							</tr>
							<s:iterator value="incomeExpenditureStatement.ieEntries"
								status="stat">
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
												onclick="return showSchedule('<s:property value="glCode"/>','<s:property value="scheduleNo"/>')"><s:property
													value="scheduleNo" /></a>&nbsp;
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="right">
											<s:if test='%{displayBold == true}'>
												<strong><s:property value="budgetAmount" /></strong>
											</s:if>
											<s:else>
												<s:property value="budgetAmount" />
											</s:else>
											&nbsp;
										</div>
									</td>
									<s:iterator value="incomeExpenditureStatement.funds"
										status="stat">
										<td class="blueborderfortd">
											<div align="right">

												<s:if test='%{displayBold == true}'>
													<strong><s:property value="netAmount[name]" />&nbsp;</strong>
												</s:if>
												<s:else>
													<s:property value="netAmount[name]" />&nbsp;</s:else>
											</div>
										</td>
										<td class="blueborderfortd">
											<div align="right">
												<s:if test='%{displayBold == true}'>
													<strong><s:property
															value="previousYearAmount[name]" />&nbsp;</strong>
												</s:if>
												<s:else>
													<s:property value="previousYearAmount[name]" />&nbsp;</s:else>
											</div>
										</td>

										</td>
									</s:iterator>

								</tr>
							</s:iterator>
						</table>
					</div>
				</td>
			</tr>
		</table>
		<div class="buttonbottom" id="ieReport">
			<s:text name="report.export.options" />
			: <a
				href='/EGF/report/incomeExpenditureReport-generateIncomeExpenditureXls.action?showDropDown=false&model.period=<s:property value="model.period"/>&model.currency=<s:property value="model.currency"/>&model.financialYear.id=<s:property value="model.financialYear.id"/>&model.department.id=<s:property value="model.department.id"/>&model.asOndate=<s:property value="model.asOndate"/>&model.fund.id=<s:property value="model.fund.id"/>&model.function.id=<s:property value="model.function.id"/>&model.functionary.id=<s:property value="model.functionary.id"/>&model.field.id=<s:property value="model.field.id"/>'>Excel</a>
			| <a
				href='/EGF/report/incomeExpenditureReport-generateIncomeExpenditurePdf.action?showDropDown=false&model.period=<s:property value="model.period"/>&model.currency=<s:property value="model.currency"/>&model.financialYear.id=<s:property value="model.financialYear.id"/>&model.department.id=<s:property value="model.department.id"/>&model.asOndate=<s:property value="model.asOndate"/>&model.fund.id=<s:property value="model.fund.id"/>&model.function.id=<s:property value="model.function.id"/>&model.functionary.id=<s:property value="model.functionary.id"/>&model.field.id=<s:property value="model.field.id"/>'>PDF</a>
		</div>
	</div>