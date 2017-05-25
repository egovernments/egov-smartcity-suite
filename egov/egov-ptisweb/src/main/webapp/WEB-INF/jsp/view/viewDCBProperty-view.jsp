<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>

<%@ include file="/includes/taglibs.jsp"%>

<html>

<head>
<title><s:text name="viewDCB"></s:text></title>
<script type="text/javascript">
	function loadOnStartup() {
		var btnCheckbox = document.getElementById('taxEnsureCheckbox');
		var btnPayTax = document.getElementById('PayTax');
		var buttorOperatorPayTax = document.getElementById('operatorPayBill');

		if (btnPayTax != null) {
			btnPayTax.disabled = (btnCheckbox.checked) ? false : true;
		}

		if (buttorOperatorPayTax != null) {
			buttorOperatorPayTax.disabled = (btnCheckbox.checked) ? false
					: true;
		}
	}

	function openShowReceipts() {
		window.open('../view/viewDCBProperty-showMigData.action?'
				+ 'propertyId=<s:property value="%{basicProperty.upicNo}"/>',
				'_blank', 'width=650, height=500, scrollbars=yes', false);
	}

	function openHeadwiseDCBWindow() {
		window.open('../view/viewDCBProperty-displayHeadwiseDcb.action?'
				+ 'propertyId=<s:property value="%{basicProperty.upicNo}"/>',
				'_blank', 'width=650, height=500, scrollbars=yes', false);
	}

	function switchPayTaxButton(ensureCheckbox) {
		var buttonPayTax = document.getElementById('PayTax');

		if (buttonPayTax == null) {
			document.getElementById('operatorPayBill').disabled = (ensureCheckbox.checked) ? false
					: true;
		} else {
			buttonPayTax.disabled = (ensureCheckbox.checked) ? false : true;
		}

	}

	function gotoSearchForm() {
		document.viewform.action = '${pageContext.request.contextPath}/citizen/search/search-searchForm.action';
		document.viewform.submit();
	}
</script>
</head>

<body onload="loadOnStartup();">
	<div class="formmainbox">
		<s:if test="%{hasErrors()}">
			<div class="errorstyle" id="property_error_area">
				<div class="errortext">
					<s:actionerror />
					<s:fielderror />
				</div>
			</div>
		</s:if>
		<s:if test="%{basicProperty== null}">
			<div class="headermessage" align="center">
				Property does not exists with given Assessment Number :
				<s:property value="%{propertyId}" />
				, Please Enter a Valid Assessment Number.<span class="bold"></span>
			</div>
		</s:if>
		<s:else>
			<div class="headingbg">
				<s:if test="%{isCitizen}">
					<s:text name="taxdetailsheader" />
				</s:if>
				<s:else>
					<s:text name="viewDCB" />
				</s:else>
			</div>
			<s:form action="#" theme="simple">
				<s:if test="%{viewMap.taxExempted == true}">
					<div class="headermessage">
						This property tax is exempted with reason <span class="bold"><s:property
								default="N/A"
								value="%{basicProperty.property.taxExemptedReason.name}" /></span>
					</div>
				</s:if>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="greybox" width="5%">&nbsp;</td>
						<td class="greybox" width="20%"><s:text name="prop.Id" /> :
						</td>
						<td class="greybox" width="20%"><span class="bold"> <s:property
									value="%{propertyId}" />
						</span></td>
						<td class="greybox" width="20%"><s:text name="Zone" /> :</td>
						<td class="bluebox" width="20%"><span class="bold"> <s:property
									default="N/A" value="%{viewMap.propID.zone.name}" />
						</span></td>
					</tr>

					<tr>
						<td class="greybox" width="5%">&nbsp;</td>
						<td class="greybox" width="20%"><s:text name="Ward" /> :</td>
						<td class="greybox" width="20%"><span class="bold"> <s:property
									default="N/A" value="%{viewMap.propID.ward.name}" />
						</span></td>
						<td class="greybox" width="5%"><s:text name="Block" /> :</td>
						<td class="greybox" width="20%"><span class="bold"> <s:property
									default="N/A" value="%{viewMap.propID.area.name}" />
						</span></td>
					</tr>

					<tr>
						<td class="greybox" width="5%">&nbsp;</td>
						<td class="greybox" width="20%"><s:text name="locality" /> :
						</td>
						<td class="greybox" width="20%"><span class="bold"> <s:property
									value="%{viewMap.propID.locality.name}" />
						</span></td>
						<td class="greybox" width="20%"><s:text name="OwnerName" />
							:</td>
						<td class="greybox" width="20%"><span class="bold"> <s:property
									value="%{viewMap.ownerName}" />
						</span></td>
					</tr>

					<tr>
						<td class="greybox" width="5%">&nbsp;</td>
						<td class="greybox" width="20%"><s:text
								name="PropertyAddress" /> :</td>
						<td class="greybox" width="20%"><span class="bold"> <s:property
									value="%{viewMap.propAddress}" />
						</span></td>
						<td class="greybox" width="20%"><s:text name="ownership.type"></s:text>
							:</td>
						<td class="greybox" width="20%"><span class="bold"> <s:property
									default="N/A" value="%{viewMap.ownershipType}" />
						</span></td>

					</tr>

					<tr>
						<td class="greybox" width="5%">&nbsp;</td>
						<td class="greybox"><s:text name="CurrentTax" /> :</td>
						<td class="greybox" width="20%"><span class="bold">Rs.
								<s:text name="format.money">
									<s:param value="viewMap.currFirstHalfTaxAmount" />
								</s:text>
						</span></td>
						<td class="greybox" width="20%"><s:text name="CurrentTaxDue" />
							:</td>
						<td class="greybox" width="20%"><span class="bold">Rs.
								<s:text name="format.money">
									<s:param value="viewMap.currFirstHalfTaxDue" />
								</s:text>
						</span></td>
					</tr>
					<tr>
						<td class="greybox" width="5%">&nbsp;</td>
						<td class="greybox"><s:text name="CurrentSecondHalfTax" /> :
						</td>
						<td class="greybox" width="20%"><span class="bold">Rs.
								<s:text name="format.money">
									<s:param value="viewMap.currSecondHalfTaxAmount" />
								</s:text>
						</span></td>
						<td class="greybox" width="20%"><s:text
								name="CurrentSecondHalfTaxDue" /> :</td>
						<td class="greybox" width="20%"><span class="bold">Rs.
								<s:text name="format.money">
									<s:param value="viewMap.currSecondHalfTaxDue" />
								</s:text>
						</span></td>
					</tr>

					<tr>
						<td class="greybox" width="10%"></td>
						<td class="greybox"><s:text name="ArrearsDue" /> :</td>
						<td class="greybox"><span class="bold">Rs. <s:text
									name="format.money">
									<s:param value="viewMap.totalArrDue" />
								</s:text>
						</span></td>
					</tr>
					<tr>
						<td colspan="10">
							<table width="100%" border="0" align="center" cellpadding="0"
								cellspacing="0" class="tablebottom">

								<tr>
									<th class="bluebgheadtd" width="15%"><s:text
											name="Installment" /></th>
									<th class="bluebgheadtd" width="20%" align="center" colspan="3">
										<s:text name="Demand" />
									</th>

									<th class="bluebgheadtd" width="20%" align="center" colspan="3">
										<s:text name="Collection" />
									</th>

									<th class="bluebgheadtd" width="10%" align="center" colspan="1">
										<s:text name="Rebate" />
									</th>

									<th class="bluebgheadtd" width="20%" align="center" colspan="3">
										<s:text name="Balance" />
									</th>
								</tr>
								<tr>

									<td class="blueborderfortd">
										<div align="center">&nbsp;</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											<span class="bold"><s:text name="Tax" /> </span>
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											<span class="bold"><s:text name="chkBncPenalty" /> </span>
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											<span class="bold"><s:text name="ltPmtPenalty" /> </span>
										</div>
									</td>

									<td class="blueborderfortd">
										<div align="center">
											<span class="bold"><s:text name="Tax" /> </span>
										</div>
									</td>

									<td class="blueborderfortd">
										<div align="center">
											<span class="bold"><s:text name="chkBncPenalty" /> </span>
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											<span class="bold"><s:text name="ltPmtPenalty" /> </span>
										</div>
									</td>

									<td class="blueborderfortd">
										<div align="center">
											<span class="bold">&nbsp; </span>
										</div>
									</td>

									<td class="blueborderfortd">
										<div align="center">
											<span class="bold"><s:text name="Tax" /> </span>
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											<span class="bold"><s:text name="chkBncPenalty" /> </span>
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											<span class="bold"><s:text name="ltPmtPenalty" /> </span>
										</div>
									</td>
								</tr>
								<s:set value="0" var="advance" />
								<s:set value="0" var="advrebate" />

								<s:iterator value="dcbReport.getRecords()" var="dcbreportmap">
									<tr>
										<td class="blueborderfortd">
											<div align="center">
												<s:property value="%{key.getDescription()}" />
											</div>
										</td>

										<s:iterator value="dcbReport.getFieldNames()" var="fieldnames">
											<c:if
												test="${fieldnames != 'Advance Collection' && fieldnames != 'Fines'}">
												<td class="blueborderfortd">
													<div align="right">
														<s:text name="format.money">
															<s:param value="value.getDemands()[#fieldnames]" />
														</s:text>
													</div>
												</td>
											</c:if>
										</s:iterator>
										<s:iterator value="dcbReport.getFieldNames()" var="fieldnames">
											<c:if
												test="${fieldnames != 'Advance Collection' && fieldnames != 'Fines'}">
												<td class="blueborderfortd">
													<div align="right">
														<s:text name="format.money">
															<s:param value="value.getCollections()[#fieldnames]" />
														</s:text>
													</div>
												</td>
											</c:if>
										</s:iterator>

										<s:iterator value="dcbReport.getFieldNames()" var="fieldnames">

											<c:if
												test="${fieldnames != 'Advance Collection' && fieldnames != 'PENALTY' && fieldnames != 'FINES'}">
												<td class="blueborderfortd">
													<div align="right">
														<s:text name="format.money">
															<s:param value="value.getRebates()[#fieldnames]" />
														</s:text>
													</div>
												</td>
											</c:if>
											<c:if
												test="${fieldnames != 'Advance Collection' && fieldnames != 'Fines'}">
												<s:set value="value.getRebates()[#fieldnames]" var="advreb" />
												<c:set value="${advrebate + advreb}" var="advrebate" />
											</c:if>
										</s:iterator>

										<s:iterator value="dcbReport.getFieldNames()" var="fieldnames">
											<c:if
												test="${fieldnames != 'Advance Collection' && fieldnames != 'Fines'}">
												<td class="blueborderfortd">
													<div align="right">
														<s:text name="format.money">
															<s:param value="value.getBalances()[#fieldnames]" />
														</s:text>
													</div>
												</td>
											</c:if>
										</s:iterator>
									</tr>
								</s:iterator>
								<tr>
									<td class="blueborderfortd">
										<div align="right">
											<b>Total:</b>
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="right">
											<span class="bold"> <s:text name="format.money">
													<s:param value="dcbReport.getTotalDmdTax()" />
												</s:text>
											</span>
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="right">
											<span class="bold"> <s:text name="format.money">
													<s:param value="dcbReport.getTotalDmdPnlty()" />
												</s:text>
											</span>
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="right">
											<span class="bold"> <s:text name="format.money">
													<s:param value="dcbReport.getTotalLpayPnlty()" />
												</s:text>
											</span>
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="right">
											<span class="bold"> <s:text name="format.money">
													<s:param value="dcbReport.getTotalColTax()" />
												</s:text>
											</span>
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="right">
											<span class="bold"> <s:text name="format.money">
													<s:param value="dcbReport.getTotalColPnlty()" />
												</s:text>
											</span>
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="right">
											<span class="bold"> <s:text name="format.money">
													<s:param value="dcbReport.getTotalColLpayPnlty()" />
												</s:text>
											</span>
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="right">
											<span class="bold"> <s:text name="format.money">
													<s:param value="dcbReport.getTotalRebate()" />
												</s:text>
											</span>
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="right">&nbsp;</div>
									</td>
									<td class="blueborderfortd">
										<div align="right">&nbsp;</div>
									</td>
									<td class="blueborderfortd">
										<div align="right">&nbsp;</div>
									</td>
								</tr>
								<tr>
									<td class="blueborderfortd">
										<div align="center">&nbsp;</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">&nbsp;</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">&nbsp;</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">&nbsp;</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">&nbsp;</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">&nbsp;</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">&nbsp;</div>
									</td>
									<td class="blueborderfortd">
										<div align="right">
											<b><s:text name="amtDue" />:</b>
										</div>
									</td>

									<s:iterator value="dcbReport.getFieldNames()" var="FieldNames">
										<c:if
											test="${FieldNames != 'Advance Collection' && FieldNames != 'Fines'}">
											<td class="blueborderfortd">
												<div align="right">
													<span class="bold"> <s:text name="format.money">
															<s:param
																value="dcbReport.getFieldBalanceTotals()[#FieldNames]" />
														</s:text>
													</span>
												</div>
											</td>
										</c:if>
									</s:iterator>
								</tr>
								<c:if test="${dcbReport.getTotalAdvance()>0}">
									<tr>
										<td class="blueborderfortd">
											<div align="center">&nbsp;</div>
										</td>
										<td class="blueborderfortd">
											<div align="center">&nbsp;</div>
										</td>
										<td class="blueborderfortd">
											<div align="center">&nbsp;</div>
										</td>
										<td class="blueborderfortd">
											<div align="center">&nbsp;</div>
										</td>
										<td class="blueborderfortd">
											<div align="center">&nbsp;</div>
										</td>
										<td class="blueborderfortd">
											<div align="center">&nbsp;</div>
										</td>
										<td class="blueborderfortd">
											<div align="center">&nbsp;</div>
										</td>

										<td class="blueborderfortd">
											<div align="right">
												<b><s:text name="Advance" />:</b>
											</div>
										</td>

										<td class="blueborderfortd">
											<div align="right">
												<span class="bold"> <s:text name="format.money">
														<s:param value="dcbReport.getTotalAdvance()" />
													</s:text>
												</span>
											</div>
										</td>
										<td class="blueborderfortd">
											<div align="center">&nbsp;</div>
										</td>
										<td class="blueborderfortd">&nbsp;</td>
									</tr>
								</c:if>
								<tr>
									<td class="blueborderfortd">
										<div align="center">&nbsp;</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">&nbsp;</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">&nbsp;</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">&nbsp;</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">&nbsp;</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">&nbsp;</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">&nbsp;</div>
									</td>

									<td class="blueborderfortd">
										<div align="right">
											<b><s:text name="balanceDue" />:</b>
										</div>
									</td>

									<td class="blueborderfortd">
										<div align="right">
											<span class="bold"> <s:text name="format.money">
													<s:param value="dcbReport.getTotalBalance()" />
												</s:text>
											</span>
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">&nbsp;</div>
									</td>
									<td class="blueborderfortd">&nbsp;</td>
								</tr>
								<s:if
									test="%{getActiveRcpts() != null && !getActiveRcpts().isEmpty()}">
									<table width="100%" border="0" align="center" cellpadding="0"
										cellspacing="0" class="tablebottom">
										<tr>
											<td align="center">
												<div class="headingsmallbg">
													<s:text name="propRcptDet" />
												</div>
											</td>
										</tr>

										<tr>
											<td align="center">
												<table width="100%" border="0" align="center"
													cellpadding="0" cellspacing="0" class="tablebottom">

													<tr>
														<th class="bluebgheadtd"><s:text name="receiptNo" />
														</th>
														<th class="bluebgheadtd"><s:text name="receiptDate" />
														</th>
														<th class="bluebgheadtd"><s:text name="totalAmount" />
														</th>
													</tr>

													<s:iterator value="getActiveRcpts()" var="rcpt">
														<tr>
															<td class="blueborderfortd">
																<div align="center">
																	<a
																		href="/../collection/citizen/onlineReceipt-viewReceipt.action?receiptNumber=<s:property value="#rcpt.getReceiptNumber()" />&consumerCode=<s:property value="%{propertyId}" />&serviceCode=<s:property value="%{serviceCode}"/>"
																		target="_blank"> <s:property
																			value="#rcpt.getReceiptNumber()" />
																	</a>
																</div>
															</td>
															<td class="blueborderfortd">
																<div align="center">
																	<s:date name="#rcpt.getReceiptDate()"
																		format="dd/MM/yyyy h:mm:ss aa" />
																</div>
															</td>
															<td class="blueborderfortd">
																<div align="center">
																	<s:text name="format.money">
																		<s:param name="value" value="#rcpt.getReceiptAmt()" />
																	</s:text>
																</div>
															</td>
														</tr>
													</s:iterator>
												</table>
											</td>
										</tr>
									</table>
								</s:if>
								<s:if
									test="%{getCancelledReceipts() != null && !getCancelledReceipts().isEmpty()}">
									<table width="100%" border="0" align="center" cellpadding="0"
										cellspacing="0" class="tablebottom">
										<tr>
											<td align="center">
												<div class="headingsmallbg">
													<s:text name="rcptHeader" />
												</div>
											</td>
										</tr>

										<tr>
											<td align="center">
												<table width="100%" border="0" align="center"
													cellpadding="0" cellspacing="0" class="tablebottom">

													<tr>
														<th class="bluebgheadtd"><s:text name="receiptNo" />
														</th>
														<th class="bluebgheadtd"><s:text name="receiptDate" />
														</th>
														<th class="bluebgheadtd"><s:text name="totalAmount" />
														</th>
													</tr>

													<s:iterator value="getCancelledReceipts()" var="rcpt">

														<tr>
															<td class="blueborderfortd">
																<div align="center">
																	<s:property value="#rcpt.getReceiptNumber()" />
																</div>
															</td>
															<td class="blueborderfortd">
																<div align="center">
																	<s:date name="#rcpt.getReceiptDate()"
																		format="dd/MM/yyyy h:mm:ss aa" />
																</div>
															</td>
															<td class="blueborderfortd">
																<div align="center">
																	<s:text name="format.money">
																		<s:param name="value" value="#rcpt.getReceiptAmt()" />
																	</s:text>
																</div>
															</td>
														</tr>
													</s:iterator>
												</table>
											</td>
										</tr>
									</table>
								</s:if>
								<s:if
									test="%{getMutationRcpts() != null && !getMutationRcpts().isEmpty()}">
									<table width="100%" border="0" align="center" cellpadding="0"
										cellspacing="0" class="tablebottom">
										<tr>
											<td align="center">
												<div class="headingsmallbg">
													<s:text name="mutationFeeRcptHeader" />
												</div>
											</td>
										</tr>

										<tr>
											<td align="center">
												<table width="100%" border="0" align="center"
													cellpadding="0" cellspacing="0" class="tablebottom">

													<tr>
														<th class="bluebgheadtd"><s:text name="receiptNo" />
														</th>
														<th class="bluebgheadtd"><s:text name="receiptDate" />
														</th>
														<th class="bluebgheadtd"><s:text name="totalAmount" />
														</th>
													</tr>

													<s:iterator value="getMutationRcpts()" var="rcpt">

														<tr>
															<td class="blueborderfortd">
																<div align="center">
																	<a
																		href="/../collection/citizen/onlineReceipt-viewReceipt.action?receiptNumber=<s:property value="#rcpt.getReceiptNumber()" />&consumerCode=<s:property value="#rcpt.getConsumerCode()" />&serviceCode=PTMF"
																		target="_blank"> <s:property
																			value="#rcpt.getReceiptNumber()" />
																	</a>
																</div>
															</td>
															<td class="blueborderfortd">
																<div align="center">
																	<s:date name="#rcpt.getReceiptDate()"
																		format="dd/MM/yyyy" />
																</div>
															</td>
															<td class="blueborderfortd">
																<div align="center">
																	<s:text name="format.money">
																		<s:param name="value" value="#rcpt.getReceiptAmt()" />
																	</s:text>
																</div>
															</td>
														</tr>
													</s:iterator>
												</table>
											</td>
										</tr>
									</table>
								</s:if>
							</table>
						</td>
					</tr>
				</table>
				<div class="buttonbottom" align="center">
					<s:if test="%{errorMessage != null}">
						<div align="center" style="font-size: 15px; color: red">
							<s:property value="%{errorMessage}" />
							<br> <br>
							<s:text name="msg.activeDemand" />
						</div>
					</s:if>
					<s:elseif
						test="%{viewMap.taxExempted == false && isCitizen || roleName.contains(@org.egov.ptis.constants.PropertyTaxConstants@CSC_OPERATOR_ROLE.toUpperCase())}">
						<div align="center">
							<s:checkbox name="taxEnsureCheckbox" id="taxEnsureCheckbox"
								onclick="switchPayTaxButton(this);" required="true" />
							<span style="font-size: 15px; color: red"> <s:text
									name="msg.payBill.verification" /> <br> <br> <s:text
									name="msg.activeDemand" />
							</span>
						</div>
						<br>
						<div align="center">
							<s:if test="%{isCitizen && viewMap.taxExempted == false}">
								<input type="button" name="PayTax" id="PayTax" value="Pay Tax"
									class="buttonsubmit"
									onclick="window.location='../citizen/collection/collection-searchOwnerDetails.action?assessmentNumber=<s:property value="%{propertyId}" />';" />
							</s:if>
							<s:else>
								<input type="button" name="operatorPayBill" id="operatorPayBill"
									value="Pay Bill" class="buttonsubmit"
									onclick="window.location='/../ptis/collection/collectPropertyTax-generateBill.action?propertyId=<s:property value="%{propertyId}" />';" />
							</s:else>
						</div>
					</s:elseif>
					<br>

				</div>

			</s:form>
		</s:else>
		<div align="center">
			<s:if test="%{isCitizen && searchUrl.contains('onlineSearch')}">
				<input id="SearchProperty" class="buttonsubmit" type="button"
					onclick="window.location='/ptis/citizen/search/search-searchByAssessmentForm.action';"
					value="Search Property" name="SearchProperty">
			</s:if>
			<s:elseif test="%{isCitizen}">
				<input id="SearchProperty" class="buttonsubmit" type="button"
					onclick="window.location='/ptis/citizen/search/search-searchForm.action';"
					value="Search Property" name="SearchProperty">
			</s:elseif>
			<s:if test="%{viewMap.taxExempted == false}">
				<input type="button" name="button3" id="button3"
					value="Head Wise DCB" class="buttonsubmit"
					onclick="openHeadwiseDCBWindow();" />
				<s:if test="%{basicProperty.source == 'M'}">
					<input type="button" name="button4" id="button4"
						value="Show Receipts" class="buttonsubmit"
						onclick="openShowReceipts();" />
				</s:if>
				<s:else></s:else>
			</s:if>
			<input type="button" name="button2" id="button2" value="Close"
				class="button" onclick="return confirmClose();" />
		</div>
	</div>
</body>

</html>
