<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>

<s:if test="%{bankBookViewEntries.size()>0}">
<br/>
<table width="99%" border="0" cellspacing="0" cellpadding="0">
	<tr>
        <td class="subheadnew">
		<div>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" >
              <tr>
                <th class="subheadnew" width="40%" bgcolor="#CCCCCC"><s:property value="ulbName"/><br/>
                Bank Book for <s:property value="bankAccount.bankbranch.bank.name"/>-<s:property value="bankAccount.bankbranch.branchname"/>-<s:property value="bankAccount.accountnumber"/> 
from <s:property value="%{getFormattedDate(startDate)}"/> to <s:property value="%{getFormattedDate(endDate)}"/></th>
			  </tr>
		</table>
        </div></td>
      </tr>
      <tr>
        <td class="blueborderfortd">
		<div>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom">
			<tr>
                <th class="bluebgheadtd" colspan="5">Receipts</th>
                <th class="bluebgheadtd" colspan="5">Payments</th>
			  </tr>            
              <tr>
                <th class="bluebgheadtd" width="2%" >Voucher Date</th>
                <th class="bluebgheadtd" width="10%" >Voucher Number</th>
                <th class="bluebgheadtd" width="15%" >Particulars</th>
                <th class="bluebgheadtd" width="10%" >Amount</th>
                <th class="bluebgheadtd" width="10%" >Cheque #/Date</th>
                <th class="bluebgheadtd" width="2%" >Voucher Date</th>
                <th class="bluebgheadtd" width="10%" >Voucher Number</th>
                <th class="bluebgheadtd" width="15%" >Particulars</th>
                <th class="bluebgheadtd" width="10%" >Amount</th>
                <th class="bluebgheadtd" width="10%" >Cheque #/Date</th>
			  </tr>
		<s:iterator value="bankBookViewEntries" status="stat" var="p">
			<tr>
					<td class="blueborderfortd"><div align="center"><s:property value="receiptVoucherDate"/>&nbsp;</div></td>
					<td class="blueborderfortd">
					<a href="javascript:void(0);" onclick="viewVoucher(<s:property value='voucherId'/>);"><s:property value="receiptVoucherNumber" /> </a>&nbsp;</td>
					<td class="blueborderfortd">
						<s:if test="%{#p.receiptParticulars == 'Total'}">
							<strong><s:property value="receiptParticulars"/>&nbsp;</strong>
						</s:if>
						<s:else><s:property value="receiptParticulars"/>&nbsp;</s:else>
					</td>
					<td class="blueborderfortd"><div align="right">
						<s:if test="%{#p.receiptParticulars == 'Total'}">
							<strong><s:text name="format.number"><s:param name="value" value="receiptAmount"/></s:text></strong>
						</s:if>
						<s:else>
							<s:if test="%{#p.receiptAmount != null}">
								<s:text name="format.number"><s:param name="value" value="receiptAmount"/></s:text>&nbsp;
							</s:if>
						</s:else>
					</div></td>
					<td class="blueborderfortd">
						<s:if test="%{#p.receiptChequeDetail == 'MULTIPLE'}">
							<a href="javascript:void(0);" onclick='showChequeDetails(<s:property value="voucherId"/>);'><s:property value="receiptChequeDetail"/></a>&nbsp;
						</s:if>
						<s:else><s:property value="receiptChequeDetail"/>&nbsp;</s:else>
					</td>
					<td class="blueborderfortd"><s:property value="paymentVoucherDate"/></td>
					<td class="blueborderfortd">
					<a href="javascript:void(0);" onclick="viewVoucher(<s:property value='voucherId'/>);"><s:property value="paymentVoucherNumber" /> </a>&nbsp;</td>
					<td class="blueborderfortd">
						<s:if test="%{#p.paymentParticulars == 'Total'}">
							<strong><s:property value="paymentParticulars"/></strong>&nbsp;
						</s:if>
						<s:else>
							<s:property value="paymentParticulars"/>&nbsp;
						</s:else>
					</td>
					<td class="blueborderfortd"><div align="right">
						<s:if test="%{#p.receiptParticulars == 'Total'}">
							<strong><s:text name="format.number"><s:param name="value" value="paymentAmount"/></s:text></strong>&nbsp;
						</s:if>
						<s:else>
							<s:if test="%{#p.paymentAmount != null}">
								<s:text name="format.number"><s:param name="value" value="paymentAmount"/></s:text>&nbsp;
							</s:if>
						</s:else>
					</div></td>
					<td class="blueborderfortd">
						<s:if test="%{#p.paymentChequeDetail == 'MULTIPLE'}">
							<a href="javascript:void(0);" onclick='showChequeDetails(<s:property value="voucherId"/>);'><s:property value="paymentChequeDetail"/></a>&nbsp;
						</s:if>
						<s:else><s:property value="paymentChequeDetail"/>&nbsp;</s:else>

					</td>
			</tr>
		</s:iterator>
			</table>
        </div></td>
      </tr>
	</table></td>
	</tr>
	<tr>
		<div class="buttonbottom">
		Export Options: 
		<label onclick="exportXls()"><a href='javascript:void(0);'>Excel</a></label> 
		| <label onclick="exportPdf()"><a href="javascript:void(0);">PDF</a></label>
		</div>
	</tr>
</table>
</s:if>
<s:else>No data found</s:else>
