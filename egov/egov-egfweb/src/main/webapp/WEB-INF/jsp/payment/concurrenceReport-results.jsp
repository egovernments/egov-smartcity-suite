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
<s:if test="%{paymentHeaderListFnd.size()>0}">
	<br />
	<div class="subheadnew">List Of Payments Made</div>
	<%
Integer srno=0;
%>
	<table width="99%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td class="blueborderfortd">
				<div>
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="tablebottom">

						<tr>
							<th class="bluebgheadtd" width="2%">Sl No</th>
							<th class="bluebgheadtd" width="10%">Bank</th>
							<th class="bluebgheadtd" width="10%">Bank Account</th>
							<th class="bluebgheadtd" width="10%">Department</th>
							<th class="bluebgheadtd" width="10%">Bill Number</th>
							<th class="bluebgheadtd" width="10%">Bill Date</th>
							<th class="bluebgheadtd" width="10%">UAC</th>
							<th class="bluebgheadtd" width="10%">BPV Number</th>
							<th class="bluebgheadtd" width="10%">BPV Date</th>
							<th class="bluebgheadtd" width="10%">BPV Account Code</th>
							<th class="bluebgheadtd" width="10%">Amount</th>

						</tr>
						<s:iterator value="paymentHeaderListFnd" status="stat" var="p">
							<tr>

								<td class="blueborderfortd"><s:if
										test="%{#p.bpvNumber != 'Total'}"><%=++srno %></s:if> &nbsp;</td>
								<td class="blueborderfortd"><div align="center">
										<s:property value="bankName" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="center">
										<s:property value="bankAccountNumber" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="center">
										<s:property value="departmentName" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="center">
										<s:property value="billNumber" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="center">
										<s:property value="%{getFormattedDate(#p.billDate)}" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="center">
										<s:property value="uac" />
										&nbsp;
									</div></td>

								<td class="blueborderfortd"><div align="center">
										<s:property value="bpvNumber" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="center">
										<s:property value="%{getFormattedDate(#p.bpvDate)}" />
										&nbsp;
									</div></td>
								<s:if test="%{#p.bpvNumber == 'Total'}">
									<td class="blueborderfortd"><div align="center">
											<strong><s:property value="bpvAccountCode" />&nbsp;
										</div></td>
									</strong>
									<td class="blueborderfortd"><div align="center">
											<input type="text" style="text-align: right;"
												readonly="readonly"
												value='<s:text name="payment.format.number">
			<strong><s:param name="value" value="amount"/></s:text>' />&nbsp;
										</div></td>
									</strong>
								</s:if>
								<s:else>
									<td class="blueborderfortd"><div align="center">
											<s:property value="bpvAccountCode" />
											&nbsp;
										</div></td>
									<td class="blueborderfortd"><div align="center">
											<input type="text" style="text-align: right;"
												readonly="readonly"
												value='<s:text name="payment.format.number">
			<s:param name="value" value="amount"/></s:text>'
												id='netPayable<s:property value="#stat.index"/>' />&nbsp;
										</div></td>
								</s:else>
							</tr>
						</s:iterator>
						<tr>
							<td style="text-align: right" colspan="10"
								class="blueborderfortdnew"><strong>Grand Total</strong></td>
							<td class="blueborderfortd"><div align="center">
									<input type="text" style="text-align: right;"
										readonly="readonly"
										value='<s:text name="payment.format.number">
			<s:param name="grandTol" value="grandTol"/></s:text>' />&nbsp;
								</div></td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
		<tr>
		</tr>
	</table>
	</td>
	</tr>
	<tr>
		<div class="buttonbottom">
			Export Options: <label onclick="return exportXls()"><a
				href='javascript:void(0);'>Excel</a></label> | <label
				onclick="return exportPdf()"><a href="javascript:void(0);">PDF</a></label>
		</div>
	</tr>
	</table>
</s:if>
<s:else>No data found</s:else>
