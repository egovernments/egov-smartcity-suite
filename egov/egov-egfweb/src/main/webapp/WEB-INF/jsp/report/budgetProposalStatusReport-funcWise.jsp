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
											name="report.run.date" />:</strong>
								<s:date name="todayDate" format="dd/MM/yyyy" /></td>
							</tr>
							<tr>
								<th class="bluebgheadtd"><s:text name="report.function" /></th>
								<th class="bluebgheadtd"><s:text name="report.asstadmin" /></th>
								<th class="bluebgheadtd"><s:text name="report.smadmin" /></th>
								<th class="bluebgheadtd"><s:text name="report.hod" /></th>
							</tr>
							</tr>
							<s:iterator value="budgetProposalStatusFuncList" status="stat">
								<tr>
									<td class="blueborderfortd">
										<div align="center">
											<s:property value="function.name" />
											&nbsp;
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											<s:property value="asstAdmin" />
											&nbsp;
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											<s:property value="smAdmin" />
											&nbsp;
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											<s:property value="hod" />
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
	</div>