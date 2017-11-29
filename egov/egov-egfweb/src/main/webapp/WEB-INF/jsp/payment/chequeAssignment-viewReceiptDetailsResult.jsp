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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>

<%@ page language="java"%>

<span class="mandatory"> <font
	style='color: red; font-weight: bold'> <s:actionerror /> <s:fielderror />
		<s:actionmessage /></font>
</span>
<div class="formmainbox">
	<div class="subheadnew">VoucherDetails</div>
	<s:if test="%{viewReceiptDetailsList.size()>0}">
		<br />
		<table width="99%" border="0" cellspacing="0" cellpadding="0">

			<tr>
				<td class="blueborderfortd">
					<div>
						<table width="100%" border="0" cellpadding="0" cellspacing="0"
							class="tablebottom">
							<tr>
								<th class="bluebgheadtdnew"><s:text name="Sl No" /></th>
								<th class="bluebgheadtdnew"><s:text
										name="chq.assignment.receipt.voucherno" /></th>
								<th class="bluebgheadtdnew"><s:text
										name="chq.assignment.receipt.amount" /></th>
								<th class="bluebgheadtdnew"><s:text
										name="chq.assignment.receipt.deductedamount" /></th>
							</tr>
							<s:iterator value="viewReceiptDetailsList" status="stat"
								var="ChequeAssignment">
								<tr>
									<td class="blueborderfortd"><div align="left">
											<s:property value="#stat.index+1" />
											&nbsp;
										</div></td>
									<s:hidden id="voucherHeaderId"
										name="rtgsList[%{#counter}].voucherHeaderId"
										value="%{voucherHeaderId}" />
									<td class="blueborderfortd"><div align="left">
											<a href="#"
												onclick="viewVoucher(<s:property value='voucherHeaderId'/>)">
												<s:property value="voucherNumber" />
											</a>&nbsp;
										</div></td>
									<td class="blueborderfortd"><div align="right">
											<s:text name="format.number">
												<s:param value="%{receiptAmount}" />
											</s:text>
											&nbsp;
										</div></td>
									<td class="blueborderfortd"><div align="right">
											<s:text name="format.number">
												<s:param value="%{deductedAmount}" />
											</s:text>
											&nbsp;
										</div></td>
								</tr>
							</s:iterator>
							<tr>
								<td class="blueborderfortd" colspan="3"><div align="right">
										<s:text name="total" />
									</div></td>
								<td class="blueborderfortd"><div align="right">
										<s:property value="%{totalDeductedAmount}" />
									</div></td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
		</table>
	</s:if>
	<s:else>No Voucher Details Found</s:else>

	<script>
			function viewVoucher(vid){
				var url = '../voucher/preApprovedVoucher!loadvoucherview.action?vhid='+vid;
				window.open(url,'Search','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
			}
			                
		</script>