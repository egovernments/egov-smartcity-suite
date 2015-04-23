<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<div id="errMsg">
	</div>
	<tr>
		<td colspan="5">
			<div class="headingsmallbg"> 
				<s:text name="warrantApp.title"></s:text>
			</div>
		</td>
	</tr>
	<s:hidden name="warrant.recovery.id" value="%{id}"></s:hidden>
             <tr>
		<td colspan="5">
			 <table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr><td class="bluebox" width="25%"><s:text name="warrantFee" />:<span class="mandatory1">*</span></td>
			    <td class="bluebox" width="25%">
			   
				<s:textfield name="warrant.warrantFees[0].amount" id="warrentFee" value="%{warrentFee}"   onkeyup="validateDecimal(this);" onblur="validateDigitsAndDecimal(this);totalFees();" maxlength="11" cssStyle="text-align:right;"/>
				<s:hidden name="warrant.warrantFees[0].demandReason.id" value="%{getDemandReason('WARRANT_FEE')}" ></s:hidden>
				</td>
<td class="bluebox" width="25%"><s:text name="noticeFee" />:<span class="mandatory1">*</span></td>
			    <td class="bluebox" width="25%">
				<s:textfield name="warrant.warrantFees[1].amount"  id="noticeFee" value="%{noticeFee}" onkeyup="validateDecimal(this);" onblur="validateDigitsAndDecimal(this);totalFees()" maxlength="11" cssStyle="text-align:right;"/>
				<s:hidden name="warrant.warrantFees[1].demandReason.id" value="%{getDemandReason('NOTICE_FEE')}"></s:hidden>
				</td>
				</tr>
              <tr>
<td class="bluebox" width="25%"><s:text name="courtFee" />:</td>
			    <td class="bluebox" width="25%">
				<s:textfield name="warrant.warrantFees[2].amount"  id="courtFee" value="%{courtFee}"  onkeyup="validateDecimal(this);" onblur="validateDigitsAndDecimal(this);totalFees()" maxlength="11" cssStyle="text-align:right;"/>
				<s:hidden name="warrant.warrantFees[2].demandReason.id" value="%{getDemandReason('COURT_FEE')}"></s:hidden>
				</td>
            
<td class="bluebox" width="25%"><s:text name="totalFee" />:<span class="mandatory1">*</span></td>
			    <td class="bluebox" width="25%">
			    
			    <div id="ttlFee"> </div>
				<s:property value="%{ttlFee}" />
				</td>
			</tr>
			<tr>
			<td class="bluebox" width="25%"><s:text name="remarks"/></td>
			<td class="bluebox" width="25%"><s:textarea name="recovery.warrant.remarks" id="warratRemarks" cols="40" rows="2"  onblur="checkLength1024(this)" ></s:textarea></td>
			<td class="bluebox" width="25%"></td>
			<td class="bluebox" width="25%"></td>
			</tr>
			
		</table>
		</td>
	</tr>


</table>
<script>
</script>