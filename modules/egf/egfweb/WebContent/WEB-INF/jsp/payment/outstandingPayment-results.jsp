<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<s:if test="%{paymentHeaderList.size()>0}">
<br/>
<table width="99%" border="0" cellspacing="0" cellpadding="0">
	<tr>
        <td class="subheadnew">
		<div>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" >
              <tr>
                <th class="subheadnew" width="35%" bgcolor="#CCCCCC">Bank Balance Details as on <s:property value="%{getFormattedDate(asOnDate)}"/></th>
                <th class="subheadnew" width="8%" bgcolor="#CCCCCC">Running Balance<input type="label" style="text-align: right;" readonly="readonly" value='<s:text name="payment.format.number">
					<s:param name="value" value="%{bankBalance.add(currentReceiptsAmount)}"/></s:text>' id="rBalance"/></th>
			  </tr>
		</table>
        </div></td>
      </tr>

      <tr>
        <td class="blueborderfortd">
		<div>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom">
              <tr>
                <th class="bluebgheadtd" width="2%" >Bank Name</th>
                <th class="bluebgheadtd" width="8%" >Branch Name</th>
                <th class="bluebgheadtd" width="10%" >Account Number</th>
                <th class="bluebgheadtd" width="10%" >Account Name</th>
                <th class="bluebgheadtd" width="10%" >Current Balance(Rs.)</th>
				<th class="bluebgheadtd" width="10%" >Current Receipts(Rs.)</th>
				<th class="bluebgheadtd" width="10%" >Total Balance Available(Rs.)</th>
			  </tr>
		<tr>
			<td class="blueborderfortd"><div align="center"><s:property value="bankAccount.bankbranch.bank.name"/>&nbsp;</div></td>
			<td class="blueborderfortd"><div align="center"><s:property value="bankAccount.bankbranch.branchname"/>&nbsp;</div></td>
			<td class="blueborderfortd"><div align="center"><s:property value="bankAccount.accountnumber"/>&nbsp;</div></td>
			<td class="blueborderfortd"><div align="center"><s:property value="bankAccount.chartofaccounts.glcode"/>-<s:property value="bankAccount.chartofaccounts.name"/></div></td>
			<td class="blueborderfortd"><div align="right">
				<s:text name="payment.format.number">
					<s:param name="value" value="bankBalance"/>
				</s:text>
			</div></td>
			<td class="blueborderfortd">
				<div align="right">
				<s:text name="payment.format.number">
					<s:param name="value" value="currentReceiptsAmount"/>
				</s:text>
				</div>
			</td>
			<td class="blueborderfortd">
				<div align="right">
				<s:text name="payment.format.number">
					<s:param name="value" value="%{bankBalance.add(currentReceiptsAmount)}"/>
				</s:text>
				</div>
			</td>			
		</tr>
			</table>
        </div></td>
      </tr>
	</table></td>
	</tr>
</table>
<br/>
<div class="subheadnew">List Of Payments</div>
<br/>
<table width="99%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td class="blueborderfortd">
		<div>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom">
              
              <tr>
                <th class="bluebgheadtd" width="2%" >Slr No</th>
                <th class="bluebgheadtd" width="8%" >Payment Voucher Number</th>
                <th class="bluebgheadtd" width="10%" >Date</th>
                <th class="bluebgheadtd" width="10%" >Department</th>
                <th class="bluebgheadtd" width="10%" >Net Payable</th>
				<th class="bluebgheadtd" width="10%" >Select</th>
			  </tr>
		<s:iterator value="paymentHeaderList" status="stat" var="p">
		<tr>
			<td class="blueborderfortd"><s:property value="#stat.index+1"/>&nbsp;</td>
			<td class="blueborderfortd"><div align="center"><s:property value="voucherheader.voucherNumber"/>&nbsp;</div></td>
			<td class="blueborderfortd"><div align="center"><s:property value="%{getFormattedDate(#p.voucherheader.voucherDate)}"/>&nbsp;</div></td>
			<td class="blueborderfortd"><div align="center"><s:property value="voucherheader.vouchermis.departmentid.deptName"/>&nbsp;</div></td>
			<td class="blueborderfortd"><div align="center"><input type="text" style="text-align: right;" readonly="readonly" value='<s:text name="payment.format.number">
					<s:param name="value" value="paymentAmount"/></s:text>' id='netPayable<s:property value="#stat.index"/>'/>&nbsp;</div></td>
			<td class="blueborderfortd"><div align="center"><input type="checkbox" id='chbox_<s:property value="#stat.index"/>' onclick='computeBalance(<s:property value="#stat.index"/>)'/>&nbsp;</div></td>
		</tr>
		</s:iterator>
			</table>
        </div></td>
      </tr>
	</table></td>
	</tr>
	<tr>
		<input name="button" type="button" class="buttonsubmit" id="non-printable" value="Print" onclick="window.print()"/>&nbsp;&nbsp;
	</tr>
</table>
</s:if>
<s:else>No data found</s:else>
