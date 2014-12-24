<jsp:include page="vouchertrans-filter.jsp" />
<tr>
	<td width="30%" class="greybox">
		Narration &nbsp;
	</td>
	<td colspan="10" class="greybox">
		<s:textarea maxlength="250" rows="4" cols="60" name="narration" />
	</td>
</tr>
</table>
<br />
<div id="labelAD" align="center">
	<table width="80%" border=0 id="labelid">
		<th>
			Account Details
		</th>
	</table>
</div>
<div class="yui-skin-sam" align="center">
	<div id="billDetailTable"></div>
</div>
<script>
	makeVoucherDetailTable();
	document.getElementById('billDetailTable').getElementsByTagName('table')[0].width="80%"
</script>
<div id="codescontainer"></div>
<br />
<div id="labelSL" align="center">
	<table width="80%" border=0 id="labelid">
		<th>
			Sub-Ledger Details
		</th>
	</table>
</div>

<div class="yui-skin-sam" align="center">
	<div id="subLedgerTable"></div>
</div>
<script>
	makeSubLedgerTable();
	document.getElementById('subLedgerTable').getElementsByTagName('table')[0].width="80%"
</script>