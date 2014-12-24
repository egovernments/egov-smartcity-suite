<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
The voucher has been successfully created for Advance Details with the voucher number <s:property value="advanceRequisition.egAdvanceReqMises.voucherheader.voucherNumber"/>
<span>
<table align="center" border="0" cellpadding="0" cellspacing="0">
<tr><td colspan="11"><div class="subheadsmallnew">Advance Details</div></td></tr>
<tr >
<td colspan="11">
<div style="overflow-x:auto;overflow-y:hidden;">
<table width="100%" cellspacing="0" cellpadding="0" border="0" align="center">
<tbody>
	<tr>
		<th class="bluebgheadtdnew" rowspan="2" width="15%">Advance Requisition No.</th>
		<th class="bluebgheadtdnew" rowspan="2" width="12%">Advance Bill Date</th>
		<th class="bluebgheadtdnew" rowspan="2" width="20%">Party Name</th>
		<th class="bluebgheadtdnew" colspan="3"  style="height:32px;">Account Head</th>
		<th class="bluebgheadtdnew" rowspan="2">Advance Amount</th>
	</tr>
	<tr>
		<th class="bluebgheadtdnew">Code</th>
		<th class="bluebgheadtdnew">Debit Amount</th>
		<th class="bluebgheadtdnew">Credit Amount</th>
	</tr>

<s:iterator value="advanceRequisition.egAdvanceReqDetailses" var="detail">
	<s:iterator value="%{#detail.egAdvanceReqpayeeDetailses}" var="payee">
		<tr>
			<td class="blueborderfortdnew"><div align="center"><s:property value="%{#payee.egAdvanceRequisitionDetails.egAdvanceRequisition.advanceRequisitionNumber}"/></div></td>
			<td class="blueborderfortdnew"><div align="center"><s:property value="%{formatDate(#payee.egAdvanceRequisitionDetails.egAdvanceRequisition.advanceRequisitionDate)}"/></div></td>
			<td class="blueborderfortdnew"><div align="center"><s:property value="#payee.egAdvanceRequisitionDetails.egAdvanceRequisition.egAdvanceReqMises.payto"/></div></td>
			<td class="blueborderfortdnew"><div align="center"><s:property value="#payee.egAdvanceRequisitionDetails.chartofaccounts.glcode"/></div></td>
			<td class="blueborderfortdnew">
				<div align="right">
					<s:if test="#payee.egAdvanceRequisitionDetails.debitamount == null">
						0.00
					</s:if>
					<s:else>
						<s:text name="payment.format.number"><s:param name="value" value="#payee.egAdvanceRequisitionDetails.debitamount"/></s:text>
					</s:else>					
				</div>
			</td>
			<td class="blueborderfortdnew">
				<div align="right">
					<s:if test="#payee.egAdvanceRequisitionDetails.creditamount == null">
						0.00
					</s:if>
					<s:else>
						<s:text name="payment.format.number"><s:param name="value" value="#payee.egAdvanceRequisitionDetails.creditamount"/></s:text>
					</s:else>					
				</div>
			</td>
			<td class="blueborderfortdnew">
				<div align="right">
					<s:if test="#payee.egAdvanceRequisitionDetails.egAdvanceRequisition.advanceRequisitionAmount == null">
						0.00
					</s:if>
					<s:else>
						<s:text name="payment.format.number"><s:param name="value" value="#payee.egAdvanceRequisitionDetails.egAdvanceRequisition.advanceRequisitionAmount"/></s:text>
					</s:else>					
				</div>
			</td>
		</tr>
	</s:iterator>
</s:iterator>
</tbody></table>
</div>
