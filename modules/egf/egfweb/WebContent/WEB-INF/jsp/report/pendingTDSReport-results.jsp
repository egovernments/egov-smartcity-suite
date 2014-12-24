<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<s:if test="%{pendingTDS.size()>0}">
<br/>
<table width="99%" border="0" cellspacing="0" cellpadding="0">
		<tr>
            <td colspan="7">
			<div class="subheadsmallnew"><strong>Pending TDS Report as on <s:property value="asOnDate"/></strong></div></td>
        </tr>
      <tr>
        <td class="blueborderfortd">
		<div>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom">
			<tr>
				<th class="bluebgheadtd">Sl No</th>
				<th class="bluebgheadtd">Nature Of deduction</th>
				<th class="bluebgheadtd">Reference Number</th>
                <th class="bluebgheadtd">Voucher Date</th>
                <th class="bluebgheadtd">Party Name</th>
                <th class="bluebgheadtd">PAN Number</th>
                <th class="bluebgheadtd">Amount(Rs)</th>
		  </tr>
		<s:iterator value="pendingTDS" status="stat" var="p">
			<tr>
				<td class="blueborderfortd"><div align="left"><s:property value="#stat.index+1"/>&nbsp;</div></td>
				<td class="blueborderfortd"><div align="left"><s:property value="voucherName"/>&nbsp;</div></td>
				<td class="blueborderfortd"><div align="left"><s:property value="voucherNumber"/>&nbsp;</div></td>
				<td class="blueborderfortd"><div align="left"><s:property value="voucherDate"/>&nbsp;</div></td>
				<td class="blueborderfortd"><div align="left"><s:property value="partyName"/>&nbsp;</div></td>
				<td class="blueborderfortd"><div align="left"><s:property value="panNo"/>&nbsp;</div></td>
				<td class="blueborderfortd">
					<div align="right">
						<s:if test="%{#p.amount != null}">
							<s:text name="format.number"><s:param name="value" value="amount"/></s:text>&nbsp;
						</s:if>
						<s:else>0.00</s:else>
					</div>
				</td>
			</tr>
		</s:iterator>
			</table>
        </div></td>
      </tr>
	</table></td>
	</tr>
</table>
</s:if>
<s:else>No Pending TDS found</s:else>
<s:if test="%{showRemittedEntries==true && remittedTDS.size()>0}">
<br/><br/>
<table width="99%" border="0" cellspacing="0" cellpadding="0">
		<tr>
            <td colspan="7">
			<div class="subheadsmallnew"><strong>Remitted Details</strong></div></td>
        </tr>
      <tr>
        <td class="blueborderfortd">
		<div>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom">
			<tr>
				<th class="bluebgheadtd">Sl No</th>
				<th class="bluebgheadtd">Nature Of deduction</th>
				<th class="bluebgheadtd">Remitted On</th>
				<th class="bluebgheadtd">Reference Number</th>
                <th class="bluebgheadtd">Voucher Date</th>
                <th class="bluebgheadtd">Party Name</th>
                <th class="bluebgheadtd">PAN Number</th>
                <th class="bluebgheadtd">Payment Voucher</th>
                <th class="bluebgheadtd">Cheque Number</th>
                <th class="bluebgheadtd">Drawn On</th>
                <th class="bluebgheadtd">Cheque Amount(Rs)</th>
		  </tr>
		<s:iterator value="remittedTDS" status="stat" var="p">
			<tr>
				<td class="blueborderfortd"><div align="left"><s:property value="#stat.index+1"/>&nbsp;</div></td>
				<td class="blueborderfortd"><div align="left"><s:property value="natureOfDeduction"/>&nbsp;</div></td>
				<td class="blueborderfortd"><div align="left"><s:property value="remittedOn"/>&nbsp;</div></td>
				<td class="blueborderfortd"><div align="left"><s:property value="voucherNumber"/>&nbsp;</div></td>
				<td class="blueborderfortd"><div align="left"><s:property value="voucherDate"/>&nbsp;</div></td>
				<td class="blueborderfortd"><div align="left"><s:property value="partyName"/>&nbsp;</div></td>
				<td class="blueborderfortd"><div align="left"><s:property value="panNo"/>&nbsp;</div></td>
				<td class="blueborderfortd"><div align="left"><s:property value="paymentVoucherNumber" /> </div>&nbsp;</td>
				<td class="blueborderfortd"><div align="left"><s:property value="chequeNumber"/>&nbsp;</div></td>
				<td class="blueborderfortd"><div align="left"><s:property value="drawnOn"/>&nbsp;</div></td>
				<td class="blueborderfortd"><div align="right">
						<s:if test="%{#p.chequeAmount != null}">
							<s:text name="format.number"><s:param name="value" value="chequeAmount"/></s:text>&nbsp;
						</s:if>
						<s:else>0.00</s:else>
					</div></td>
			</tr>
		</s:iterator>
			</table>
        </div></td>
      </tr>
	</table></td>
	</tr>
</table>
</s:if>
<s:if test="%{pendingTDS.size()>0 || showRemittedEntries==true && remittedTDS.size()>0}">
<div class="buttonbottom" align="center">
		Export Options: 
		<label onclick="exportXls()"><a href='javascript:void(0);'>Excel</a></label> 
		| <label onclick="exportPdf()"><a href="javascript:void(0);">PDF</a></label>
		</div>
</s:if>