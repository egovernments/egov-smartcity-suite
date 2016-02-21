<!--  #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------  -->
<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<span class="mandatory1"> <font
	style='color: red; font-weight: bold'> <s:actionerror /> <s:fielderror />
		<s:actionmessage /></font>
</span>
<s:if test="%{pendingTDS.size()>0}">
	<br />
	<table width="99%" border="0" cellspacing="0" cellpadding="0">

		<tr>
			<td colspan="7"><s:if test="%{fromDate!=null}">
					<div class="subheadsmallnew">
						<strong>Deduction detailed report from <s:property
								value="fromDate" /> to <s:property value="asOnDate" /></strong>
					</div></td>
			</s:if>
			<s:else>
				<div class="subheadsmallnew">
					<strong>Deduction detailed report as on <s:property
							value="asOnDate" /></strong>
				</div>
				</td>
			</s:else>
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
							<th class="bluebgheadtd">Reference Number</th>
							<th class="bluebgheadtd">Voucher Date</th>
							<th class="bluebgheadtd">Party Name</th>
							<th class="bluebgheadtd">PAN Number</th>
							<th class="bluebgheadtd">TotalDeduction(Rs)</th>
							<th class="bluebgheadtd">Paid(Rs)</th>
							<th class="bluebgheadtd">Pending(Rs)</th>
						</tr>
						<s:iterator value="pendingTDS" status="stat" var="p">
							<tr>
								<td class="blueborderfortd"><div align="left">
										<s:property value="#stat.index+1" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="left">
										<s:property value="voucherName" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="left">
										<s:property value="voucherNumber" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="left">
										<s:property value="voucherDate" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="left">
										<s:property value="partyName" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="left">
										<s:property value="panNo" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="right">
										<s:text name="format.number">
											<s:param name="value" value="deductionAmount" />
										</s:text>
									</div></td>
								<td class="blueborderfortd"><div align="right">
										<s:text name="format.number">
											<s:param name="value" value="earlierPayment" />
										</s:text>
									</div></td>
								<td class="blueborderfortd"><div align="right">
										<s:text name="format.number">
											<s:param name="value" value="Amount" />
										</s:text>
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
<s:if test="%{ inWorkflowTDS.size()>0}">
	<br />
	<br />
	<table width="99%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="7">
				<div class="subheadsmallnew">
					<strong>Deduction remittance in workflow </strong>
				</div>
			</td>
		</tr>
		<tr>
			<td class="blueborderfortd">
				<div>
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="tablebottom">
						<tr>
							<th class="bluebgheadtd">Sl No</th>
							<th class="bluebgheadtd">Voucher Type</th>
							<th class="bluebgheadtd">Reference Number</th>
							<th class="bluebgheadtd">Voucher Date</th>
							<th class="bluebgheadtd">Party Name</th>
							<th class="bluebgheadtd">PAN Number</th>
							<th class="bluebgheadtd">Payment Voucher</th>
							<th class="bluebgheadtd">Remitted On</th>
							<th class="bluebgheadtd">Payment Amount</th>
							<th class="bluebgheadtd">Cheque Number</th>
							<th class="bluebgheadtd">Drawn On</th>
							<th class="bluebgheadtd">Cheque Amount(Rs)</th>
						</tr>
						<s:iterator value="inWorkflowTDS" status="stat" var="p">
							<tr>
								<td class="blueborderfortd"><div align="left">
										<s:property value="#stat.index+1" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="left">
										<s:property value="natureOfDeduction" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="left">
										<s:property value="voucherNumber" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="left">
										<s:property value="voucherDate" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="left">
										<s:property value="partyName" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="left">
										<s:property value="panNo" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="left">
										<s:property value="paymentVoucherNumber" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="left">
										<s:property value="remittedOn" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="right">
										<s:text name="format.number">
											<s:param name="value" value="amount" />
										</s:text>
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="left">
										<s:property value="chequeNumber" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="left">
										<s:property value="drawnOn" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="right">
										<s:if test="%{#p.chequeAmount != null}">
											<s:text name="format.number">
												<s:param name="value" value="chequeAmount" />
											</s:text>&nbsp;
						</s:if>
										<s:else>0.00</s:else>
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
<s:if test="%{showRemittedEntries==true && remittedTDS.size()>0}">
	<br />
	<br />
	<table width="99%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="7">
				<div class="subheadsmallnew">
					<strong>Remitted Details</strong>
				</div>
			</td>
		</tr>
		<tr>
			<td class="blueborderfortd">
				<div>
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="tablebottom">
						<tr>
							<th class="bluebgheadtd">Sl No</th>
							<th class="bluebgheadtd">Voucher Type</th>
							<th class="bluebgheadtd">Reference Number</th>
							<th class="bluebgheadtd">Voucher Date</th>
							<th class="bluebgheadtd">Party Name</th>
							<th class="bluebgheadtd">PAN Number</th>
							<th class="bluebgheadtd">Payment Voucher</th>
							<th class="bluebgheadtd">Remitted On</th>
							<th class="bluebgheadtd">Payment Amount</th>
							<th class="bluebgheadtd">Cheque Number</th>
							<th class="bluebgheadtd">Drawn On</th>
							<th class="bluebgheadtd">Cheque Amount(Rs)</th>
						</tr>
						<s:iterator value="remittedTDS" status="stat" var="p">
							<tr>
								<td class="blueborderfortd"><div align="left">
										<s:property value="#stat.index+1" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="left">
										<s:property value="natureOfDeduction" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="left">
										<s:property value="voucherNumber" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="left">
										<s:property value="voucherDate" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="left">
										<s:property value="partyName" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="left">
										<s:property value="panNo" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="left">
										<s:property value="paymentVoucherNumber" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="left">
										<s:property value="remittedOn" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="right">
										<s:text name="format.number">
											<s:param name="value" value="amount" />
										</s:text>
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="left">
										<s:property value="chequeNumber" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="left">
										<s:property value="drawnOn" />
										&nbsp;
									</div></td>
								<td class="blueborderfortd"><div align="right">
										<s:if test="%{#p.chequeAmount != null}">
											<s:text name="format.number">
												<s:param name="value" value="chequeAmount" />
											</s:text>&nbsp;
						</s:if>
										<s:else>0.00</s:else>
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
<s:if
	test="%{pendingTDS.size()<=0  && remittedTDS.size()<=0 && inWorkflowTDS.size()<=0}">
No pending deduction found
</s:if>
<s:if
	test="%{pendingTDS.size()>0 || (showRemittedEntries==true && remittedTDS.size()>0) || inWorkflowTDS.size()>0 }">
	<div class="buttonbottom" align="center">
		Export Options: <label onclick="exportXls()"><a
			href='javascript:void(0);'>Excel</a></label> | <label onclick="exportPdf()"><a
			href="javascript:void(0);">PDF</a></label>
	</div>
</s:if>
