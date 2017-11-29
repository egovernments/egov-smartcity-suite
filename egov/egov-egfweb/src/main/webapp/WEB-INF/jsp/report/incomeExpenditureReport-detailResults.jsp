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
<s:if test="%{incomeExpenditureStatement.size()>0}">
	<br />
	<table width="100%" cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td>
				<div align="center">
					<br />
					<table border="0" cellspacing="0" cellpadding="0"
						class="tablebottom" width="100%">
						<tr>
							<th class="bluebgheadtd" width="100%" colspan="12"><strong
								style="font-size: 15px;"> <s:text
										name="report.ie.schedule.heading" /> <s:property
										value="model.financialYear.finYearRange" /></strong></th>
						</tr>
						<tr>
							<td class="bluebox" colspan="4"><strong><s:text
										name="report.run.date" />:<s:date name="todayDate"
										format="dd/MM/yyyy" /></strong></td>
							<td colspan="14">
								<div class="blueborderfortd" align="right">
									<strong> <s:text name="report.amount.in.rupees" />
										&nbsp;&nbsp;&nbsp;&nbsp;
									</strong>
								</div>
							</td>
						</tr>
						<tr>
							<th class="bluebgheadtd"><s:text name="report.accountCode" /></th>
							<th class="bluebgheadtd"><s:text name="report.headOfAccount" /></th>
							<s:iterator value="incomeExpenditureStatement.funds"
								status="stat">
								<th class="bluebgheadtd"><s:property value="name" /></th>
							</s:iterator>
							<th class="bluebgheadtd"><s:text name="report.currentTotals" />:
								<s:property value="currentYearToDate" /></th>
							<th class="bluebgheadtd"><s:text
									name="report.previousTotals" />: <s:property
									value="previousYearToDate" /></th>
						</tr>
						<s:iterator value="incomeExpenditureStatement.entries"
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
										<s:if test='%{displayBold == true}'>
											<strong><s:property value="accountName" /></strong>
										</s:if>
										<s:else>
											<s:property value="accountName" />
										</s:else>
										&nbsp;
									</div>
								</td>
								<s:iterator value="incomeExpenditureStatement.funds"
									status="stat">
									<td class="blueborderfortd">
										<div align="right">
											<s:property value="fundWiseAmount[name]" />
											&nbsp;
										</div>
									</td>
								</s:iterator>
								<td class="blueborderfortd">
									<div align="right">
										<s:if test='%{displayBold == true}'>
											<strong><s:if test='%{currentYearTotal != 0}'>
													<s:property value="currentYearTotal" />
												</s:if> <s:else>0.00</s:else></strong>
										</s:if>
										<s:else>
											<s:if test='%{currentYearTotal != 0}'>
												<s:property value="currentYearTotal" />
											</s:if>
											<s:else>0.00</s:else>
										</s:else>
										&nbsp;
									</div>
								</td>
								<td class="blueborderfortd">
									<div align="right">
										<s:if test='%{displayBold == true}'>
											<strong><s:if test='%{previousYearTotal != 0}'>
													<s:property value="previousYearTotal" />
												</s:if> <s:else>0.00</s:else></strong>
										</s:if>
										<s:else>
											<s:if test='%{previousYearTotal != 0}'>
												<s:property value="previousYearTotal" />
											</s:if>
											<s:else>0.00</s:else>
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
</s:if>
