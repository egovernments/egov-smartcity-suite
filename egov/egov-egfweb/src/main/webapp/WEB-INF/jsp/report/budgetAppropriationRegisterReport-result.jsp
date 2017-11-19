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
<body>
	<s:form action="budgetAppropriationRegisterReport" theme="simple"
		name="budgetAppropriationRegister-Result">
		
			</br>
			</br>
			<div class="formmainbox">
				<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="greybox">&nbsp;</td>
						<td class="greybox">BE - (<s:property value="finYearRange" />)
							(Rs): <s:if test="%{isBeDefined==true}">
								<s:text name="format.number">
									<s:param value="%{beAmount}" />
								</s:text>
							</s:if> <s:else>
								<s:text name="report.budget.not.found" />
							</s:else></td>
						<td class="greybox"><s:text name="report.additional.apprn" />
							<s:if test="%{isBeDefined==true}">
								<s:text name="format.number">
									<s:param value="%{addtionalAppropriationForBe}" />
								</s:text>
							</s:if> <s:else></s:else></td>
						<td class="greybox" align="left">RE - (<s:property
								value="finYearRange" />) (Rs):<s:if test="%{isReDefined==true}">
								<s:text name="format.number">
									<s:param value="%{reAmount}" />
								</s:text>
							</s:if> <s:else></s:else></td>
						<s:if test="%{shouldShowREAppropriations}">
							<td class="greybox"><s:text
									name="report.additional.apprn.re" /> <s:text
									name="format.number">
									<s:param value="%{addtionalAppropriationForRe}" />
								</s:text></td>
						</s:if>
					</tr>
				</table>

			<s:if test="%{updatedBdgtAppropriationRegisterList.size()>0}">
				<table width="99%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="bluebox">
							<table width="100%" border="0" cellpadding="0" cellspacing="0"
								class="tablebottom">
								<tr>
									<th class="bluebgheadtd">Sl. No.</th>
									<th class="bluebgheadtd">Budget Allocation #</th>
									<th class="bluebgheadtd">BA Date</th>
									<th class="bluebgheadtd" colspan="4">Description</th>
									<th class="bluebgheadtd">Amount (Rs)</th>
									<th class="bluebgheadtd">Cumulative Total (Rs)</th>
									<th class="bluebgheadtd">Balance Available (Rs)</th>
								</tr>
								<tr>
									<th class="bluebgheadtd" colspan="3"></th>
									<th class="bluebgheadtd">Bill Number</th>
									<th class="bluebgheadtd">Bill Date</th>
									<th class="bluebgheadtd">Voucher Number</th>
									<th class="bluebgheadtd">Voucher Date</th>
									<th class="bluebgheadtd" colspan="3"></th>
								</tr>
								<s:iterator value="updatedBdgtAppropriationRegisterList"
									status="stat">
									<tr>
										<td class="blueborderfortd"><div align="center">
												<s:property value="serailNumber" />
											</div></td>
										<td class="blueborderfortd"><div align="left">
												<s:property value="bdgApprNumber" />
											</div></td>
										</div>
										<s:if test="%{billCreatedDate != null}">
											<td class="blueborderfortd"><div align="left">
													<s:date name="%{billCreatedDate}" format="dd/MM/yyyy" />
													&nbsp;
												</div></td>
										</s:if>
										<s:else>
											<td class="blueborderfortd"><div align="left">
													<s:date name="%{createdDate}" format="dd/MM/yyyy" />
													&nbsp;
												</div></td>
										</s:else>
										<td class="blueborderfortd"><div align="left">
												<s:property value="billNumber" />
												&nbsp;
											</div></td>
										<td class="blueborderfortd"><div align="left">
												<s:date name="%{billDate}" format="dd/MM/yyyy" />
												&nbsp;
											</div></td>
										<td class="blueborderfortd"><div align="left">
												<s:property value="VoucherNumber" />
												&nbsp;
											</div></td>
										<td class="blueborderfortd"><div align="left">
												<s:date name="%{voucherDate}" format="dd/MM/yyyy" />
												&nbsp;
											</div></td>
										<td class="blueborderfortd"><div align="right">
												<s:text name="format.number">
													<s:param value="%{billAmount}" />
												</s:text>
												&nbsp;
											</div></td>
										<td class="blueborderfortd"><div align="right">
												<s:text name="format.number">
													<s:param value="%{cumulativeAmount}" />
												</s:text>
												&nbsp;
											</div></td>
										<td class="blueborderfortd"><div align="right">
												<s:text name="format.number">
													<s:param value="%{balanceAvailableAmount}" />
												</s:text>
												&nbsp;
											</div></td>
									</tr>
								</s:iterator>
							</table>
							</div>
						</td>
					</tr>
					<tr>
						<td align="center"><a
							href='/EGF/report/budgetAppropriationRegisterReport-generatePdf.action?asOnDate=<s:property value="strAsOnDate"/>&department.id=<s:property value="department.id"/>&function.id=<s:property value="function.id"/>&budgetGroup.id=<s:property value="budgetGroup.id"/>&fund.id=<s:property value="fund.id"/>'>PDF
								| </a> <a
							href='/EGF/report/budgetAppropriationRegisterReport-generateXls.action?asOnDate=<s:property value="strAsOnDate"/>&department.id=<s:property value="department.id"/>&function.id=<s:property value="function.id"/>&budgetGroup.id=<s:property value="budgetGroup.id"/>&fund.id=<s:property value="fund.id"/>'>Excel</a>
						</td>
					</tr>
					</td>
					</tr>
				</table>
		</s:if>
		</div>
	</s:form>
</body>

