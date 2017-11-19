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
<s:if test="%{paymentHeaderList.size()>0}">
	<br />
	<table width="99%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td class="subheadnew">
				<div>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<th class="subheadnew" width="35%" bgcolor="#CCCCCC">Bank
								Balance Details as on <s:property
									value="%{getFormattedDate(asOnDate)}" />
							</th>
							<th class="subheadnew" width="8%" bgcolor="#CCCCCC">Running
								Balance<input type="label" style="text-align: right;"
								readonly="readonly"
								value='<s:text name="payment.format.number">
					<s:param name="value" value="%{bankBalance.add(currentReceiptsAmount)}"/></s:text>'
								id="rBalance" />
							</th>
						</tr>
					</table>
				</div>
			</td>
		</tr>

		<tr>
			<td class="blueborderfortd">
				<div>
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="tablebottom">
						<tr>
							<th class="bluebgheadtd" width="2%">Bank Name</th>
							<th class="bluebgheadtd" width="8%">Branch Name</th>
							<th class="bluebgheadtd" width="10%">Account Number</th>
							<th class="bluebgheadtd" width="10%">Chart Of Account</th>
							<th class="bluebgheadtd" width="10%">Current Balance(Rs.)</th>
							<th class="bluebgheadtd" width="10%">Total Balance
								Available(Rs.)</th>
						</tr>
						<tr>
							<td class="blueborderfortd"><div align="center">
									<s:property value="bankAccount.bankbranch.bank.name" />
									&nbsp;
								</div></td>
							<td class="blueborderfortd"><div align="center">
									<s:property value="bankAccount.bankbranch.branchname" />
									&nbsp;
								</div></td>
							<td class="blueborderfortd"><div align="center">
									<s:property value="bankAccount.accountnumber" />
									&nbsp;
								</div></td>
							<td class="blueborderfortd"><div align="center">
									<s:property value="bankAccount.chartofaccounts.glcode" />
								</div></td>
							<td class="blueborderfortd"><div align="right">
									<s:text name="payment.format.number">
										<s:param name="value" value="bankBalance" />
									</s:text>
								</div></td>


							<td class="blueborderfortd">
								<div align="right">
									<s:text name="payment.format.number">
										<s:param name="value"
											value="%{bankBalance.add(currentReceiptsAmount)}" />
									</s:text>
								</div>
							</td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
	</table>
	</td>
	</tr>
	</table>
	<br />
	<div class="subheadnew">List Of Payments</div>
	<br />
	<table width="99%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td class="blueborderfortd">
				<div>
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="tablebottom">

						<tr>
							<th class="bluebgheadtd" width="2%">Sl No</th>
							<th class="bluebgheadtd" width="8%">Payment Voucher Number</th>
							<th class="bluebgheadtd" width="10%">Date</th>
							<th class="bluebgheadtd" width="10%">Department</th>
							<th class="bluebgheadtd" width="10%">Net Payable</th>
							<th class="bluebgheadtd" width="10%">Select</th>
						</tr>
						<s:iterator value="paymentHeaderList" status="stat" var="p">
							<tr>
								<td class="blueborderfortd"><s:property
										value="#stat.index+1" />&nbsp;</td>
								<td class="blueborderfortd"><div align="center">
										<a href="javascript:void(0);"
											onclick='viewVoucher(<s:property value="voucherheader.id"/>);'><s:property
												value="voucherheader.voucherNumber" /> </a>&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="center">
										<s:property
											value="%{getFormattedDate(#p.voucherheader.voucherDate)}" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="center">
										<s:property
											value="voucherheader.vouchermis.departmentid.deptName" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="center">
										<input type="text" style="text-align: right;"
											readonly="readonly"
											value='<s:text name="payment.format.number">
					<s:param name="value" value="paymentAmount"/></s:text>'
											id='netPayable<s:property value="#stat.index"/>' />&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="center">
										<input type="checkbox" name="selectVhs"
											value='<s:property value="voucherheader.id"/>'
											id='chbox_<s:property value="#stat.index"/>'
											onclick='computeBalance(<s:property value="#stat.index"/>)' />&nbsp;
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
	<div class="buttonbottom">
		Export Options: <label onclick="exportXls()"><a
			href='javascript:void(0);'>Excel</a></label> | <label onclick="exportPdf()"><a
			href="javascript:void(0);">PDF</a></label>
	</div>
	</table>
</s:if>
<s:else>No data found</s:else>
