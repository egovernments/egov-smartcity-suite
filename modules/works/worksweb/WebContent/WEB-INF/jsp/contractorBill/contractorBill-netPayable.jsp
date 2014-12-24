
<tr>
	<td colspan="3" class="headingwk">
		<div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
		<div class="headplacer"><s:text name="contractorBill.tab.netpayable" /></div>
	</td>
	<td align="right" class="headingwk" style="border-left-width: 0px">
      		&nbsp;
      	</td>
</tr>
<tr>
		<td class="greyboxwk"><s:text name="contractorBill.netpayable.code" /><span class="mandatory">*</span> : </td>
        <td class="greybox2wk"><s:select headerKey="0" 
        headerValue="%{getText('estimate.default.select')}" name="netPayableCode" id="netPayableCode" 
        cssClass="selectwk" list="contratorCoaPayableMap"   /></td></td>
        <td class="greyboxwk"><s:text name="contractorBill.tab.amount" /><span class="mandatory">*</span> : </td>
        <td class="greybox2wk" ><s:textfield  name="netPayableAmount" cssClass='selectamountwk' id="netPayableAmount" readonly="true"/></td>
    </tr>
<script>
 <s:if test="%{skipBudget && dropdownData.assestList.isEmpty() && model.id==null && !hasErrors()}">
                	workValue(); </s:if> 
               
</script>
