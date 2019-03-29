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

<%@ include file="/includes/taglibs.jsp"%>

<html>
	<head>
		<title><s:text name="viewHeadwiseDCB"></s:text></title>
	</head>
	<body>
		<div class="formmainbox">
			<div class="headingbg">
				<s:text name="viewHeadwiseDCB" />
			</div>
			<s:form action="#" theme="simple">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td colspan="8">
							<table width="100%" border="0" align="center" cellpadding="0"
								cellspacing="0" class="tablebottom">

								<tr>
									<th class="bluebgheadtd" width="10%">
										<s:text name="Installment" />
									</th>
									<th class="bluebgheadtd" width="10%">
										<s:text name="TaxHead" />
									</th>
									<th class="bluebgheadtd" width="20%" align="center" colspan="3">
										<s:text name="Details" />
									</th>
									<th class="bluebgheadtd" width="10%" align="center" colspan="3">
										<s:text name="Balance" />
									</th>
								</tr>
								<tr>
									<td class="blueborderfortd">
										<div align="center">
											&nbsp;
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											&nbsp;
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											<span class="bold"><s:text name="Demand" /> </span>
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											<span class="bold"><s:text name="Collection" />
											</span>
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											<span class="bold"><s:text name="Rebate" />
											</span>
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											&nbsp;
										</div>
									</td>
								</tr>
								<s:set value="0" var="advance" />
								<s:set value="0" var="advrebate" />
								<s:iterator value="dcbReport.getRecords()" var="dcbreportmap">
									<s:set value="0" var="instDmdTotal" />
									<s:set value="0" var="instCollTotal" />
									<s:set value="0" var="instRebateTotal" />
									<s:set value="0" var="instBalanceTotal" />
									<s:set value="true" var="firstRecord" />
									<s:iterator value="dcbReport.getFieldNames()" var="fieldnames">
										<tr>
											<s:if test="%{fieldnames != 'Advance Collection' && fieldnames != 'Fines' && value.getDemands()[#fieldnames]!=0}">
											<td class="blueborderfortd">
												<div align="center">
													<s:if test="#firstRecord">
														<s:property value="%{key.getDescription()}" />
														<s:set value="false" var="firstRecord" />
													</s:if>
													<s:else>
														&nbsp;
													</s:else>
												</div>
											</td>
											<td class="blueborderfortd">
												<div align="left">
													<s:property value="%{fieldnames}" />
												</div>
											</td>
											<td class="blueborderfortd">
												<div align="right">
													<s:text name="format.money">
														<s:param value="value.getDemands()[#fieldnames]" />
													</s:text>
													<s:set value="value.getDemands()[#fieldnames]" var="instDmd"/>
													<c:set value="${instDmdTotal + instDmd}" var="instDmdTotal"/>
												</div>
											</td>
											<td class="blueborderfortd">
												<div align="right">
													<s:text name="format.money">
														<s:param value="value.getCollections()[#fieldnames]" />
														<s:set value="value.getCollections()[#fieldnames]" var="instColl"/>
														<c:set value="${instCollTotal + instColl}" var="instCollTotal"/>
													</s:text>
												</div>
											</td>
											<td class="blueborderfortd">
												<div align="right">
													<s:text name="format.money">
														<s:param value="value.getRebates()[#fieldnames]" />
														<s:set value="value.getRebates()[#fieldnames]" var="advreb" />
														<c:set value="${advrebate + advreb}" var="advrebate" />
														<c:set value="${advrebate}" var="instRebateTotal" />
													</s:text>
												</div>
											</td>
											<td class="blueborderfortd">
												<div align="right">
													<s:text name="format.money">
														<s:param value="value.getBalances()[#fieldnames]" />
														<s:set value="value.getBalances()[#fieldnames]" var="instBal"/>
														<c:set value="${instBalanceTotal + instBal}" var="instBalanceTotal"/>
													</s:text>
												</div>
											</td>
											</s:if>
											<s:if test="%{fieldnames == 'Advance Collection'}">
											<s:set value="%{value.getCollections()[#fieldnames]}"
													var="adv" />
											<c:set value="${advance + adv}" var="advance" />
											</s:if>
										</tr>	
									</s:iterator>
									<tr>
										<td class="blueborderfortd">
											<div align="center">
												&nbsp;
											</div>
										</td>
										<td class="blueborderfortd">
											<div align="center">
												&nbsp;
											</div>
										</td>
										<td class="blueborderfortd">
											<div align="right">
												<span class="bold"><fmt:formatNumber pattern="#,##0.00" value="${instDmdTotal}"/></span>
											</div>
										</td>
										<td class="blueborderfortd">
											<div align="right">
												<span class="bold"><fmt:formatNumber pattern="#,##0.00" value="${instCollTotal}"/></span>
											</div>
										</td>
										<td class="blueborderfortd">
											<div align="right">
												<span class="bold"><fmt:formatNumber pattern="#,##0.00" value="${instRebateTotal}"/></span>
											</div>
										</td>
										<td class="blueborderfortd">
											<div align="right">
												<span class="bold"><fmt:formatNumber pattern="#,##0.00" value="${instBalanceTotal}"/></span>
											</div>
										</td>
									</tr>
								</s:iterator>
								<tr>
									<td class="blueborderfortd">
										<div align="center">
											&nbsp;
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											&nbsp;
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											&nbsp;
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											&nbsp;
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="right">
											<b><s:text name="amtDue" />:</b>
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="right">
											<span class="bold"> 
												<s:text name="format.money">
													<s:param value="dcbReport.getTotalBalance()" />
												</s:text>
											</span>
										</div>
									</td>
								</tr>
		<s:if test="%{!getActiveRcpts().isEmpty()}" >
		<tr>
      		<td colspan="9"><div class="headingsmallbg"><s:text name="PaymentDetails"/></div></td>
		</tr>		
		
			<tr>		
				<td colspan="9">
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tablebottom">

						<tr>
							<th class="bluebgheadtd">
								<s:text name="receiptNo"/>
							</th>
							<th class="bluebgheadtd">
								<s:text name="receiptDate"/>
							</th>
							<th class="bluebgheadtd">
								<s:text name="totalAmount"/>
							</th>
						</tr>
						
						<s:iterator value="getActiveRcpts()" var="rcpt" status="r">		
								<s:hidden id="%{#r.index}" value="%{#rcpt.receiptNumber}" />																										
								<tr>
									<td class="blueborderfortd">
										<div align="center">
										<a href="/../collection/citizen/onlineReceipt!viewReceipt.action?receiptNumber=<s:property value="#rcpt.getReceiptNumber()" />&consumerCode=<s:property value="%{encodedConsumerCode}" />&serviceCode=PT" target="_blank" >
                                               <s:property value="#rcpt.getReceiptNumber()" /> 
                                            </a>
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											<s:date format="dd/MM/yyyy HH:mm:ss" name="#rcpt.getReceiptDate()" var="rcptDate" />
											<s:property value="rcptDate" />
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
										<s:text name="format.money" >
											<s:param value="#rcpt.getReceiptAmt()" />
										</s:text>
										</div>
									</td>
							  	</tr>
						</s:iterator>
				</table>
				</td>
				</tr>
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
							<table width="100%" border="0" align="center" cellpadding="0"
								cellspacing="0" class="tablebottom">

								<tr>
									<th class="bluebgheadtd">
										<s:text name="receiptNo" />
									</th>
									<th class="bluebgheadtd">
										<s:text name="receiptDate" />
									</th>
									<th class="bluebgheadtd">
										<s:text name="totalAmount" />
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
												<s:date format="dd/MM/yyyy HH24:mm:ss" name="#rcpt.getReceiptDate()" var="rcptDate" />
												<s:property value="rcptDate" />
											</div>
										</td>
										<td class="blueborderfortd">
											<div align="center">
												<s:property value="#rcpt.getReceiptAmt()" />
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
							<table width="100%" border="0" align="center" cellpadding="0"
								cellspacing="0" class="tablebottom">

								<tr>
									<th class="bluebgheadtd">
										<s:text name="receiptNo" />
									</th>
									<th class="bluebgheadtd">
										<s:text name="receiptDate" />
									</th>
									<th class="bluebgheadtd">
										<s:text name="totalAmount" />
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
												<s:property value="#rcpt.getReceiptDate()" />
											</div>
										</td>
										<td class="blueborderfortd">
											<div align="center">
												<s:property value="#rcpt.getReceiptAmt()" />
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
				<div class="buttonbottom" align="center">
					<input type="button" name="button2" id="button2" value="Close"
							class="button" onclick="return confirmClose();" />
				</div>
		</s:form>
	</div>
	</body>
</html>
