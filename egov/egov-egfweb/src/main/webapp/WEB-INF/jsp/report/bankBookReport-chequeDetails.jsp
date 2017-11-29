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

<s:if test="%{chequeDetails.size()>0}">
	<br />
	<table width="99%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td class="blueborderfortd">
				<div>
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="tablebottom">
						<tr>
							<th class="bluebgheadtd">Cheque/RTGS Number</th>
							<th class="bluebgheadtd">Cheque/RTGS Date</th>
							<th class="bluebgheadtd">Cheque/RTGS Amount</th>
							<th class="bluebgheadtd">Cheque/RTGS Status</th
						</tr>
						<s:iterator value="chequeDetails" status="stat" var="p">
							<tr>
								<s:if test="%{#p.instrumentNumber == null}">
									<td class="blueborderfortd"><div
											style="text-align: center">
											<s:property value="transactionNumber" />
										</div></td>
								</s:if>
								<s:else>
									<td class="blueborderfortd"><div
											style="text-align: center">
											<s:property value="instrumentNumber" />
										</div></td>
								</s:else>
								<s:if test="%{#p.instrumentDate == null}">
									<td class="blueborderfortd"><div
											style="text-align: center">
											<s:date name="%{transactionDate}" format="dd/MM/yyyy" />
										</div></td>
								</s:if>
								<s:else>
									<td class="blueborderfortd"><div
											style="text-align: center">
											<s:date name="%{instrumentDate}" format="dd/MM/yyyy" />
										</div></td>
								</s:else>
								<td class="blueborderfortd"><div style="text-align: right">
										<s:text name="format.number">
											<s:param value="#p.instrumentAmount"></s:param>
										</s:text>
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div style="text-align: center">
										<s:property value="statusId.description" />
									</div></td>
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
</s:if>
<s:else>No data found</s:else>
