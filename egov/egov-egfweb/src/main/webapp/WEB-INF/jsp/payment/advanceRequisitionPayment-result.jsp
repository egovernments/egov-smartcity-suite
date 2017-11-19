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
The voucher has been successfully created for Advance Details with the
voucher number
<s:property
	value="advanceRequisition.egAdvanceReqMises.voucherheader.voucherNumber" />
<span>
	<table align="center" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td colspan="11"><div class="subheadsmallnew">Advance
					Details</div></td>
		</tr>
		<tr>
			<td colspan="11">
				<div style="overflow-x: auto; overflow-y: hidden;">
					<table width="100%" cellspacing="0" cellpadding="0" border="0"
						align="center">
						<tbody>
							<tr>
								<th class="bluebgheadtdnew" rowspan="2" width="15%">Advance
									Requisition No.</th>
								<th class="bluebgheadtdnew" rowspan="2" width="12%">Advance
									Bill Date</th>
								<th class="bluebgheadtdnew" rowspan="2" width="20%">Party
									Name</th>
								<th class="bluebgheadtdnew" colspan="3" style="height: 32px;">Account
									Head</th>
								<th class="bluebgheadtdnew" rowspan="2">Advance Amount</th>
							</tr>
							<tr>
								<th class="bluebgheadtdnew">Code</th>
								<th class="bluebgheadtdnew">Debit Amount</th>
								<th class="bluebgheadtdnew">Credit Amount</th>
							</tr>

							<s:iterator value="advanceRequisition.egAdvanceReqDetailses"
								var="detail">
								<s:iterator value="%{#detail.egAdvanceReqpayeeDetailses}"
									var="payee">
									<tr>
										<td class="blueborderfortdnew"><div align="center">
												<s:property
													value="%{#payee.egAdvanceRequisitionDetails.egAdvanceRequisition.advanceRequisitionNumber}" />
											</div></td>
										<td class="blueborderfortdnew"><div align="center">
												<s:property
													value="%{formatDate(#payee.egAdvanceRequisitionDetails.egAdvanceRequisition.advanceRequisitionDate)}" />
											</div></td>
										<td class="blueborderfortdnew"><div align="center">
												<s:property
													value="#payee.egAdvanceRequisitionDetails.egAdvanceRequisition.egAdvanceReqMises.payto" />
											</div></td>
										<td class="blueborderfortdnew"><div align="center">
												<s:property
													value="#payee.egAdvanceRequisitionDetails.chartofaccounts.glcode" />
											</div></td>
										<td class="blueborderfortdnew">
											<div align="right">
												<s:if
													test="#payee.egAdvanceRequisitionDetails.debitamount == null">
						0.00
					</s:if>
												<s:else>
													<s:text name="payment.format.number">
														<s:param name="value"
															value="#payee.egAdvanceRequisitionDetails.debitamount" />
													</s:text>
												</s:else>
											</div>
										</td>
										<td class="blueborderfortdnew">
											<div align="right">
												<s:if
													test="#payee.egAdvanceRequisitionDetails.creditamount == null">
						0.00
					</s:if>
												<s:else>
													<s:text name="payment.format.number">
														<s:param name="value"
															value="#payee.egAdvanceRequisitionDetails.creditamount" />
													</s:text>
												</s:else>
											</div>
										</td>
										<td class="blueborderfortdnew">
											<div align="right">
												<s:if
													test="#payee.egAdvanceRequisitionDetails.egAdvanceRequisition.advanceRequisitionAmount == null">
						0.00
					</s:if>
												<s:else>
													<s:text name="payment.format.number">
														<s:param name="value"
															value="#payee.egAdvanceRequisitionDetails.egAdvanceRequisition.advanceRequisitionAmount" />
													</s:text>
												</s:else>
											</div>
										</td>
									</tr>
								</s:iterator>
							</s:iterator>
						</tbody>
					</table>
				</div>