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
<div style="overflow-x: scroll; overflow-y: scroll;">
	<table width="100%" cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td>
				<div align="center">
					<br />
					<table border="0" cellspacing="0" cellpadding="0"
						class="tablebottom" width="100%">
						<tr>
							<td colspan="6">
								<div class="subheadsmallnew">
									<strong><s:property value="%{functionwiseIE.cityName}" /></strong>
								</div>
							</td>
						</tr>
						<tr>
							<td class="bluebox" colspan="4"><strong><s:text
										name="report.run.date" />:<s:date name="todayDate"
										format="dd/MM/yyyy" /></strong></td>
							<td colspan="6">
								<div class="subheadsmallnew">
									<strong>FUNCTIONWISE <s:if
											test='%{functionwiseIE.incExp == "I"}'>INCOME</s:if>
										<s:else>EXPENSE</s:else> SUBSIDARY REGISTER
									</strong>
								</div>
							</td>
						</tr>
						<tr>
							<th class="bluebgheadtd">Sl.No.</th>
							<th class="bluebgheadtd">COA</th>
							<th class="bluebgheadtd">Account Head</th>
							<th class="bluebgheadtd">Schedule</th>
							<th class="bluebgheadtd">BE</th>
							<th class="bluebgheadtd">BE-ReAppropriation</th>
							<th class="bluebgheadtd">RE</th>
							<th class="bluebgheadtd">RE-ReAppropriation</th>
							<th class="bluebgheadtd">Expendiure As On</th>
							<th class="bluebgheadtd">Balancce</th>
							<s:iterator value="majorCodeList" status="stat" var="p">
								<th class="bluebgheadtd"><s:property value="p" /></th>
							</s:iterator>
						</tr>
						<s:iterator value="functionwiseIE.entries" status="stat">
							<tr>
								<td class="blueborderfortd">
									<div align="center">
										<s:property value="slNo" />
									</div>
								</td>
								<td class="blueborderfortd">
									<div align="left">
										<s:property value="functionCode" />
									</div>
								</td>
								<td class="blueborderfortd">
									<div align="left">
										<s:if test='%{slNo == ""}'>
											<strong><s:property value="functionName" /></strong>
										</s:if>
										<s:else>
											<s:property value="functionName" />
										</s:else>
									</div>
								</td>
								<td class="blueborderfortd">
									<div align="right">
										<s:if test='%{slNo == ""}'>
											<strong><s:property value="totalIncome" /></strong>
										</s:if>
										<s:else>
											<s:property value="totalIncome" />
										</s:else>
									</div>
								</td>
								<s:iterator value="functionwiseIE.majorCodeList" var="k"
									status="s">
									<td class="blueborderfortd">
										<div align="right">
											<s:if test='%{slNo == ""}'>
												<strong><s:property value="majorcodeWiseAmount[#k]" /></strong>
											</s:if>
											<s:else>
												<s:property value="majorcodeWiseAmount[#k]" />
											</s:else>
										</div>
									</td>
								</s:iterator>
							</tr>
						</s:iterator>
					</table>
				</div>
			</td>
		</tr>
	</table>
	<jsp:include page="report-filterhidden.jsp" />
	<input type="hidden" name="model.incExp"
		value='<s:property value="model.incExp"/>' />
	<div class="buttonbottom">
		<s:text name="report.export.options" />
		: <a
			href='/EGF/report/functionwiseIE!generateFunctionwiseIEXls.action?model.incExp=<s:property value="model.incExp"/>&model.fund.id=<s:property value="model.fund.id"/>&model.department.id=<s:property value="model.department.id"/>&model.function.id=<s:property value="model.function.id"/>&model.functionary.id=<s:property value="model.functionary.id"/>&model.scheme.id=<s:property value="model.scheme.id"/>&model.subScheme.id=<s:property value="model.subScheme.id"/>&model.fundsource.id=<s:property value="model.fundsource.id"/>&model.field.id=<s:property value="model.field.id"/>&model.startDate=<s:property value="model.startDate"/>&model.endDate=<s:property value="model.endDate"/>'>Excel</a>
		| <a
			href='/EGF/report/functionwiseIE!generateFunctionwiseIEPdf.action?model.incExp=<s:property value="model.incExp"/>&model.fund.id=<s:property value="model.fund.id"/>&model.department.id=<s:property value="model.department.id"/>&model.function.id=<s:property value="model.function.id"/>&model.functionary.id=<s:property value="model.functionary.id"/>&model.scheme.id=<s:property value="model.scheme.id"/>&model.subScheme.id=<s:property value="model.subScheme.id"/>&model.fundsource.id=<s:property value="model.fundsource.id"/>&model.field.id=<s:property value="model.field.id"/>&model.startDate=<s:property value="model.startDate"/>&model.endDate=<s:property value="model.endDate"/>'>PDF</a>
	</div>
</div>
