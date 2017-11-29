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
<s:if test="%{message != ''}">
	<label style="color: red"><s:property value="message" /></label>
</s:if>
<s:elseif test="%{remittedTDS.size()>0}">
	<br />
	<table width="99%" border="0" cellspacing="0" cellpadding="0">
		<tr>


			<th class="bluebgheadtd" width="100%" colspan="7"><strong
				style="font-size: 15px;"> Deductions remittance summary for
					<s:property value="%{type}" /> as on <s:property
						value="%{getFormattedDate(asOnDate)}" />
			</strong></th>
		</tr>
		<tr>
			<td
				style="border-right-width: 1px; border-left-style: solid; padding-left: 5px; border-left-color: #E9E9E9"
				class="blueborderfortd">
				<div>
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="tablebottom">
						<tr>
							<th class="bluebgheadtd">Sl No</th>
							<th class="bluebgheadtd">Voucher Type</th>
							<th class="bluebgheadtd">Month</th>
							<th class="bluebgheadtd">Total Deduction(Rs)</th>
							<th class="bluebgheadtd">Total Remitted(Rs)</th>
						</tr>
						<s:iterator value="remittedTDS" status="stat" var="p">
							<tr>
								<td class="blueborderfortd"><div align="center">
										<s:property value="#stat.index+1" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="center">
										<s:property value="natureOfDeduction" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="center">
										<s:property value="month" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd">
									<div align="right">
										<s:if test="%{#p.totalDeduction != null}">
											<s:text name="format.number">
												<s:param name="value" value="totalDeduction" />
											</s:text>&nbsp;
						</s:if>
										<s:else>0.00</s:else>
									</div>
								</td>
								<td class="blueborderfortd">
									<div align="right">
										<s:if test="%{#p.totalRemitted != null}">
											<s:text name="format.number">
												<s:param name="value" value="totalRemitted" />
											</s:text>&nbsp;
						</s:if>
										<s:else>0.00</s:else>
									</div>
								</td>
							</tr>
						</s:iterator>
					</table>
				</div>
			</td>
		</tr>
	</table>
	</td>
	</tr>
	</table>
	<div class="buttonbottom" align="center">
		Export Options: <label onclick="exportXls()"><a
			href='javascript:void(0);'>Excel</a></label> | <label onclick="exportPdf()"><a
			href="javascript:void(0);">PDF</a></label>
	</div>

</s:elseif>
<s:else>No records found</s:else>
