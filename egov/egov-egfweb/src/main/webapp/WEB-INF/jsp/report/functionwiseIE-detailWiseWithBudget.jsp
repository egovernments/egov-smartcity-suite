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
<head>
<script>
function openDeptWiseIEWithBudget(glcode,deptName)
{
var url="/EGF/report/functionwiseIE!detailWiseIEWithBudget.action?";
url=url+"&"+'model.incExp=<s:property value="model.incExp"/>';
url=url+"&"+'model.fund.id=<s:property value="model.fund.id"/>';
url=url+"&"+'model.function.id=<s:property value="model.function.id"/>';
url=url+"&"+'model.asOnDate=<s:property value="model.asOnDate"/>';
url=url+"&"+'model.glcode='+glcode;
url=url+"&"+'model.department.deptName='+deptName;
url=url+"&"+'model.scheduleName=<s:property value="model.scheduleName"/>';
window.open(url,"detailIEReport","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
}
function generateReport(type)
{
var url="/EGF/report/functionwiseIE!exportDetailwise.action?";
url=url+"&"+'model.incExp=<s:property value="model.incExp"/>';
url=url+"&"+'model.fund.id=<s:property value="model.fund.id"/>';
url=url+"&"+'model.function.id=<s:property value="model.function.id"/>';
url=url+"&"+'model.asOnDate=<s:property value="model.asOnDate"/>';
url=url+"&"+'model.glcode=<s:property value="model.glcode"/>';
url=url+"&"+'model.department.deptName=<s:property value="model.department.deptName"/>';
url=url+"&"+'model.scheduleName=<s:property value="model.scheduleName"/>';     
url=url+"&"+'model.exportType='+type;
window.open(url,"MajorAndMinorCodewiseReprt","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
}

</script>
</head>
<div style="overflow-x: auto; overflow-y: auto;">
	<s:if test='%{ieWithBudgetList.size()==0}'>
		<div class="subheadsmallnew">No Data Found</div>
	</s:if>
	<s:else>

		<s:if test='%{model.incExp=="E"}'>
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td>
						<div align="center">
							<br />
							<table border="0" cellspacing="0" cellpadding="0"
								class="tablebottom" width="100%">
								<tr>
									<td colspan="10">
										<div class="subheadsmallnew">
											<strong><s:property
													value="%{functionwiseIE.cityName}" /></strong>
										</div>
									</td>
								</tr>
								<tr>
									<td colspan="10">
										<div class="subheadsmallnew">
											<strong><s:property value="heading" /></strong>
										</div>
									</td>
								</tr>
								<tr>
									<td class="bluebox" colspan="4"><strong><s:text
												name="report.run.date" />:<s:date name="todayDate"
												format="dd/MM/yyyy" /></strong></td>
									<td colspan="10" class="blueborderfortd">
										<div style="text-align: right">
											<strong>Amount in Rupees&nbsp;&nbsp;
												&nbsp;&nbsp;&nbsp;</strong>
										</div>
									</td>
								</tr>
								<tr>
									<th class="bluebgheadtd">Sl.No.</th>
									<th class="bluebgheadtd">COA</th>
									<th class="bluebgheadtd">Account Head</th>
									<th class="bluebgheadtd">BE (Rs)</th>
									<th class="bluebgheadtd">BE-Appropriation (Rs)</th>
									<th class="bluebgheadtd">RE (Rs)</th>
									<th class="bluebgheadtd">RE-Appropriation (Rs)</th>
									<th class="bluebgheadtd">Expenditure As On(Previous Year)
										(Rs)</th>
									<th class="bluebgheadtd">Expenditure As On(Current Year)
										(Rs)</th>
									<th class="bluebgheadtd">Balance (Rs)</th>
								</tr>
								<s:set var="i" value="0" />
								<s:iterator value="ieWithBudgetList" status="stat" var="p">
									<s:if test="%{!isZero()}">
										<s:set var="i" value="#i+1" />

										<tr>
											<td class="blueborderfortd">
												<div align="center">
													<s:property value="#i" />
												</div>
											</td>
											<td class="blueborderfortd">
												<div align="left">
													<s:property value="accCode" />
												</div>
											</td>
											<td class="blueborderfortd">
												<div align="left">
													<s:property value="name" />
												</div>
											</td>
											<td class="blueborderfortd">
												<div align="right">
													<s:text name="format.number">
														<s:param value="%{beAmount}" />
													</s:text>
												</div>
											</td>
											<td class="blueborderfortd">
												<div align="right">
													<s:text name="format.number">
														<s:param value="%{beAppAmount}" />
													</s:text>
												</div>
											</td>
											<td class="blueborderfortd">
												<div align="right">
													<s:text name="format.number">
														<s:param value="%{reAmount}" />
													</s:text>
												</div>
											</td>
											<td class="blueborderfortd">
												<div align="right">
													<s:text name="format.number">
														<s:param value="%{reAppAmount}" />
													</s:text>
												</div>
											</td>
											<td class="blueborderfortd">
												<div align="right">
													<s:text name="format.number">
														<s:param value="%{pyAmount}" />
													</s:text>
												</div>
											</td>
											<td class="blueborderfortd">
												<div align="right">
													<s:text name="format.number">
														<s:param value="%{amount}" />
													</s:text>
												</div>
											</td>
											<td class="blueborderfortd">
												<div align="right">
													<s:text name="format.number">
														<s:param value="%{computedBalance}" />
													</s:text>
												</div>
											</td>
										</tr>
									</s:if>
								</s:iterator>
							</table>
						</div>
					</td>
				</tr>
			</table>
		</s:if>
		<s:else>
			<center>
				<table width="99%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td>
							<div align="center">
								<br />
								<table border="0" cellspacing="0" cellpadding="0"
									class="tablebottom" width="100%">
									<tr>
										<td colspan="10">
											<div class="subheadsmallnew">
												<strong><s:property
														value="%{functionwiseIE.cityName}" /></strong>
											</div>
										</td>
									</tr>
									<tr>
										<td colspan="10">
											<div class="subheadsmallnew">
												<strong><s:property value="heading" /></strong>
											</div>
										</td>
									</tr>
									<tr>
										<td class="bluebox" colspan="4"><strong><s:text
													name="report.run.date" />:<s:date name="todayDate"
													format="dd/MM/yyyy" /></strong></td>
										<td colspan="10" class="blueborderfortd">
											<div style="text-align: right">
												<strong>Amount in Rupees&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;</strong>
											</div>
										</td>
									</tr>
									<tr>
										<th class="bluebgheadtd">Sl.No.</th>
										<th class="bluebgheadtd">COA</th>
										<th class="bluebgheadtd">Account Head</th>
										<th class="bluebgheadtd">BE(Rs)</th>
										<th class="bluebgheadtd">RE (Rs)</th>
										<th class="bluebgheadtd">Receipt(Current Year) (Rs)</th>
										<th class="bluebgheadtd">Receipt(Previous Year) (Rs)</th>
									</tr>
									<s:set var="i" value="0" />


									<s:iterator value="ieWithBudgetList" status="stat" var="p">
										<s:if test="%{!isZero()}">
											<s:set var="i" value="#i+1" />
											<tr>
												<td class='blueborderfortd'>
													<div align="center">
														<s:property value="#i" />
													</div>
												</td>
												<td class="blueborderfortd">
													<div align="left">
														<s:property value="accCode" />
													</div>
												</td>
												<td class='blueborderfortd'>
													<div align="left">
														<s:property value="name" />
													</div>
												</td>
												<td class="blueborderfortd">
													<div align="right">
														<s:text name="format.number">
															<s:param value="%{beAmount}" />
														</s:text>
													</div>
												</td>
												<td class="blueborderfortd">
													<div align="right">
														<s:text name="format.number">
															<s:param value="%{reAmount}" />
														</s:text>
													</div>
												</td>
												<td class='blueborderfortd'>
													<div align="right">
														<s:text name="format.number">
															<s:param value="%{amount}" />
														</s:text>
													</div>
												</td>
												<td class='blueborderfortd'>
													<div align="right">
														<s:text name="format.number">
															<s:param value="%{pyAmount}" />
														</s:text>
													</div>
												</td>
											</tr>
										</s:if>
									</s:iterator>
								</table>
							</div>
						</td>
					</tr>
				</table>
			</center>
		</s:else>
		<jsp:include page="report-filterhidden.jsp" />
		<input type="hidden" name="model.incExp"
			value='<s:property value="model.incExp"/>' />
		<div class="buttonbottom">
			<s:text name="report.export.options" />
			: <a href='#' onclick="generateReport('xls')">Excel</a> |<a href='#'
				onclick="generateReport('pdf')">PDF</a>
		</div>
	</s:else>
</div>
