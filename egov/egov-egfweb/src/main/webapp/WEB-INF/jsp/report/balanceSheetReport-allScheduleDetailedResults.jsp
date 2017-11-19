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
<script>
	function showDetails(glcode) {
		var deptId = "<s:property value="balanceSheet.department.id"/>";
		var functionaryId = "<s:property value="balanceSheet.functionary.id"/>";
		var functionName = '<s:property value="functionName"/>';
		var functionId = "<s:property value="balanceSheet.function.id"/>";
		var fieldId = '<s:property value="balanceSheet.field.id"/>';
		var fundId = '<s:property value="balanceSheet.fund.id"/>';
		var startDate = '<s:date name="%{fromDate}" format="dd/MM/yyyy"/>';
		var endDate = '<s:date name="%{toDate}" format="dd/MM/yyyy"/>';

		var functionCode1 = functionName + "~" + functionId;

		// bootbox.alert(functionCode1);
		if (functionId == 0) {
			functionCode1 = "";
			functionId = "";
		}

		window
				.open('/EGF/Reports/GeneralLedger.jsp?fromBean=1&glCode1='
						+ glcode + '&fund_id=' + fundId + '&startDate='
						+ startDate + '&endDate=' + endDate + '&departmentId='
						+ deptId + '&functionaryId=' + functionaryId
						+ '&functionCodeId=' + functionId + '&functionCode='
						+ functionCode1 + '&fieldId=' + fieldId, '',
						'resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	}

	function showPreviousYearDetails(glcode) {
		var deptId = "<s:property value="balanceSheet.department.id"/>";
		var functionaryId = "<s:property value="balanceSheet.functionary.id"/>";
		var functionName = '<s:property value="functionName"/>';
		var functionId = "<s:property value="balanceSheet.function.id"/>";
		var fieldId = '<s:property value="balanceSheet.field.id"/>';
		var fundId = '<s:property value="balanceSheet.fund.id"/>';
		var startDate = '<s:date name="%{previousYearfromDate}" format="dd/MM/yyyy"/>';
		var endDate = '<s:date name="%{previousYeartoDate}" format="dd/MM/yyyy"/>';
		var functionCode1 = functionName + "~" + functionId;
		if (functionId == 0) {
			functionCode1 = "";
			functionId = "";
		}

		window
				.open('/EGF/Reports/GeneralLedger.jsp?fromBean=1&glCode1='
						+ glcode + '&fund_id=' + fundId + '&startDate='
						+ startDate + '&endDate=' + endDate + '&departmentId='
						+ deptId + '&functionaryId=' + functionaryId
						+ '&functionCodeId=' + functionId + '&functionCode='
						+ functionCode1 + '&fieldId=' + fieldId, '',
						'resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');

	}
</script>
<s:if test="%{balanceSheet.size()>0}">
	<br />
	<table width="100%" cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td>
				<div align="center">
					<br />
					<table border="0" cellspacing="0" cellpadding="0"
						class="tablebottom" width="100%">
						<tr>
							<td class="subheadsmallnew">
								<div>
									<table width="100%" border="0" cellpadding="0" cellspacing="0">
										<tr>

											<th class="bluebgheadtd" width="100%" colspan="5"><strong
												style="font-size: 15px;"><s:property
														value="ulbName" /><br />
												<s:text name="report.balancesheet.schedule" /> <s:property
														value="model.financialYear.finYearRange" /> <s:property
														value="header" /></strong></th>
											</td>
										</tr>
										<tr>
											<td class="bluebox" colspan="2"><strong><s:text
														name="report.run.date" />-<s:date name="todayDate"
														format="dd/MM/yyyy" /></strong></td>
											<td colspan="16">
												<div class="blueborderfortd" align="right">
													<strong>Amount in <s:property
															value="model.currency" /> &nbsp;&nbsp;&nbsp;&nbsp;
													</strong>
												</div>
											</td>
										</tr>
										<tr>
											<th class="bluebgheadtd"><s:text
													name="report.accountCode" /></th>
											<th class="bluebgheadtd"><s:text
													name="report.headOfAccount" /></th>
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
														<s:if test='%{displayBold == true}'>
															<strong><s:property value="accountName" /></strong>
														</s:if>
														<s:else>
															<s:property value="accountName" />
														</s:else>
														&nbsp;
													</div>
												</td>
												<s:if test="%{balanceSheet.funds.size()>1}">
													<s:iterator value="balanceSheet.funds" status="stat">
														<s:if test='%{displayBold == true}'>
															<td class="blueborderfortd"><strong><div
																		align="right">
																		<s:property value="fundWiseAmount[name]" />
																		&nbsp;
																	</div> </strong></td>
														</s:if>
														<s:else>
															<td class="blueborderfortd">
																<div align="right">
																	<s:property value="fundWiseAmount[name]" />
																	&nbsp;
																</div>
															</td>
														</s:else>
													</s:iterator>
												</s:if>
												<td class="blueborderfortd">
													<div align="right">
														<s:if test='%{displayBold == true}'>
															<strong><s:if test='%{currentYearTotal != 0}'>
																	<s:property value="currentYearTotal" />
																</s:if> <s:else>0.0</s:else></strong>
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
																</s:if> <s:else>0.0</s:else></strong>
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
					</s:if>

					<div class="buttonbottom">
						<s:text name="report.export.options" />
						: <a
							href='/EGF/report/balanceSheetReport-generateDetailedScheduleXls.action?showDropDown=false&model.period=<s:property value="model.period"/>&model.currency=<s:property value="model.currency"/>&model.financialYear.id=<s:property value="model.financialYear.id"/>&model.department.id=<s:property value="model.department.id"/>&model.fund.id=<s:property value="model.fund.id"/>&model.asOndate=<s:property value="model.asOndate"/>&model.function.id=<s:property value="model.function.id"/>&model.functionary.id=<s:property value="model.functionary.id"/>&model.field.id=<s:property value="model.field.id"/>&majorCode=<s:property value="#parameters['majorCode']" />'>Excel</a>
						| <a
							href='/EGF/report/balanceSheetReport-generateDetailedSchedulePdf.action?showDropDown=false&model.period=<s:property value="model.period"/>&model.currency=<s:property value="model.currency"/>&model.financialYear.id=<s:property value="model.financialYear.id"/>&model.department.id=<s:property value="model.department.id"/>&model.fund.id=<s:property value="model.fund.id"/>&model.asOndate=<s:property value="model.asOndate"/>&model.function.id=<s:property value="model.function.id"/>&model.functionary.id=<s:property value="model.functionary.id"/>&model.field.id=<s:property value="model.field.id"/>&majorCode=<s:property value="#parameters['majorCode']" />'>PDF</a>
					</div>