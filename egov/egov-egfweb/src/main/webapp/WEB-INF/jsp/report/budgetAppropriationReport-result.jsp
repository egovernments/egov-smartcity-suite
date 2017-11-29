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
<s:if test="%{budgetDisplayList.size()>0}">
	<div id="result"
		style="width: 1250px; overflow-x: auto; overflow-y: hidden;">
		<br />
		<div style="overflow-x: scroll; overflow-y: scroll;">
			<table width="100%" cellpadding="0" cellspacing="0" border="0">

				<td>
					<div align="center">
						<br />
						<tr>
							<th class="bluebgheadtd" width="100%" colspan="9"><strong
								style="font-size: 15px;"> <s:property value="heading" /></strong></th>
						</tr>
						<tr>
							<td colspan="9">
								<div class="blueborderfortd" align="right">
									<strong> <s:text name="report.amount.in.rupees" />
										&nbsp;&nbsp;
									</strong>
								</div>
							</td>
						</tr>
					</div>
				</td>

				<td>
					<div align="center">
						<br />
						<table border="0" cellspacing="0" cellpadding="0"
							class="tablebottom" width="100%">
							<tr>
								<th class="bluebgheadtd">Sl No</th>
								<s:if test='%{isFundSelected == "false"}'>
									<th class="bluebgheadtd"><s:text name="report.fund" /></th>
								</s:if>
								<s:if test='%{isFunctionSelected == "false"}'>
									<th class="bluebgheadtd"><s:text name="report.function" /></th>
								</s:if>
								<s:if test='%{isDepartmentSelected == "false"}'>
									<th class="bluebgheadtd"><s:text name="report.department" /></th>
								</s:if>
								<th class="bluebgheadtd"><s:text name="report.budgetHead" /></th>
								<th class="bluebgheadtd"><s:text
										name="report.budgetAppropriationNo" /></th>
								<th class="bluebgheadtd"><s:text
										name="report.appropriationDate" /></th>
								<th class="bluebgheadtd"><s:property value="budgetName" /></th>
								<th class="bluebgheadtd"><s:text
										name="report.budgetAdditionalAmount" /></th>
								<th class="bluebgheadtd"><s:text
										name="report.budgetDeductionAmount" /></th>
							</tr>
							</tr>
							<s:iterator value="budgetDisplayList" status="stat">
								<tr>
									<td class="blueborderfortd"><s:property
											value="#stat.index+1" />&nbsp;</td>
									<s:if test='%{isFundSelected == "false"}'>
										<td class="blueborderfortd"><div align="center">
												<s:property value="fund" /></td>
									</s:if>

									<s:if test='%{isFunctionSelected == "false"}'>
										<td class="blueborderfortd">
											<div align="left">
												<s:property value="function" />
											</div>
										</td>
									</s:if>

									<s:if test='%{isDepartmentSelected == "false"}'>
										<td class="blueborderfortd">
											<div align="left">
												<s:property value="department" />
											</div>
										</td>
									</s:if>

									<td class="blueborderfortd">
										<div align="left">
											<s:property value="budgetHead" />
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="left">
											<s:property value="budgetAppropriationNo" />
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											<s:date name="%{appropriationDate}" format="dd/MM/yyyy" />
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="right">
											<s:property value="actualAmount" />
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="right">
											<s:property value="additionAmount" />
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="right">
											<s:property value="deductionAmount" />
										</div>
									</td>


								</tr>
							</s:iterator>
						</table>
						<div class="buttonbottom">
							<s:text name="report.export.options" />
							: <a
								href='/EGF/report/budgetAppropriationReport-ajaxGenerateReportXls.action?showDropDown=false&model.budgetDetail.executingDepartment.id=<s:property value="model.budgetDetail.executingDepartment.id"/>&model.budgetDetail.fund.id=<s:property value="model.budgetDetail.fund.id"/>&model.budgetDetail.function.id=<s:property value="model.budgetDetail.function.id"/>&model.budgetDetail.budget.id=<s:property value="model.budgetDetail.budget.id"/>&fromDate=<s:property value="fromDate"/>&toDate=<s:property value="toDate"/>&budgetName=<s:property value="budgetName"/>&deptName=<s:property value="deptName"/>&fundName=<s:property value="fundName"/>&functionName=<s:property value="functionName"/>'>
								Excel</a> | <a
								href='/EGF/report/budgetAppropriationReport-ajaxGenerateReportPdf.action?showDropDown=false&model.budgetDetail.executingDepartment.id=<s:property value="model.budgetDetail.executingDepartment.id"/>&model.budgetDetail.fund.id=<s:property value="model.budgetDetail.fund.id"/>&model.budgetDetail.function.id=<s:property value="model.budgetDetail.function.id"/>&model.budgetDetail.budget.id=<s:property value="model.budgetDetail.budget.id"/>&fromDate=<s:property value="fromDate"/>&toDate=<s:property value="toDate"/>&budgetName=<s:property value="budgetName"/>&deptName=<s:property value="deptName"/>&fundName=<s:property value="fundName"/>&functionName=<s:property value="functionName"/>'>PDF</a>
						</div>
					</div>
					</div>
					</div> </s:if> <s:else>
						<tr>
							<td colspan="7"><div style="color: red" align="center">No
									record Found.</div></td>
						</tr>
					</s:else>