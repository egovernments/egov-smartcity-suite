<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  

<script src="<egov:url path='js/works.js'/>"></script>
<script>
function calculateTotal(){
	var records= accountDetailsDataTable.getRecordSet();
	var totalAmount=0.0; 
	for(i=0;i<records.getLength();i++){
		if(dom.get("amount"+records.getRecord(i).getId()).value>0){
			totalAmount=  totalAmount + eval(getNumber(dom.get("amount"+records.getRecord(i).getId()).value));
		}
	}
	<s:if test="%{isRebatePremLevelBill=='yes' && tenderResponse.tenderEstimate.tenderType==percTenderType}">
		refVal = dom.get('grossAmount').value;
	</s:if>
	<s:else>
		refVal = dom.get('billamount').value;
	</s:else>
	if(getNumber(totalAmount)==refVal && (dom.get("contractorBill_error").innerHTML=='Amount should be equal to work value of bill'
	|| dom.get("accountDetails_error").innerHTML=='Total Amount should be equal to the work value of bill')){
		dom.get("accountDetails_error").style.display='none';
		dom.get("accountDetails_error").innerHTML='';
	}
	var totalstatutoryDeductionsAmount = calculateTotalStatutoryDeductions();
	var totalstandardDeductionsAmount = calculateTotalStandardDeductions();
	var totalcustomDeductionsAmount = calculateTotalCustomDeductions();
	var totalretentionMoneyAmount = calculateTotalRetentionMoneyDeductions();
	var totalReleaseWithHeldAmount=calculateTotalReleaseWithHeldAmount();
	var currenbilldeduction;
	if(isNaN(dom.get("currenbilldeduction").value) || getNumber(dom.get("currenbilldeduction").value)<0 ||
	dom.get("currenbilldeduction").value==""){
		currenbilldeduction=0.0;
	}
	else{currenbilldeduction = dom.get("currenbilldeduction").value;}
	var netPayableAmount=eval(totalAmount) + eval(totalReleaseWithHeldAmount) -(eval(totalstatutoryDeductionsAmount)+eval(currenbilldeduction)+
	eval(totalstandardDeductionsAmount)+eval(totalcustomDeductionsAmount)+eval(totalretentionMoneyAmount));
	dom.get("netPayableAmount").value = roundTo(netPayableAmount);
	return totalAmount;
}
function calculateTotalStatutoryDeductions()
{
	var records= statutoryDeductionsDataTable.getRecordSet();
	var totalstatutoryDeductionsAmount=0.0; 
	for(i=0;i<records.getLength();i++){
		if(dom.get("creditAmount"+records.getRecord(i).getId()).value!=0.0){
			totalstatutoryDeductionsAmount=  totalstatutoryDeductionsAmount + 
			eval(getNumber(dom.get("creditAmount"+records.getRecord(i).getId()).value));
		}
	}
	return totalstatutoryDeductionsAmount;
}
function calculateTotalStandardDeductions()
{
	var records= standardDeductionsDataTable.getRecordSet();
	var totalstandardDeductionsAmount=0.0; 
	for(i=0;i<records.getLength();i++){
		if(dom.get("creditamount"+records.getRecord(i).getId()).value!=0.0){
			totalstandardDeductionsAmount=  totalstandardDeductionsAmount + 
			eval(getNumber(dom.get("creditamount"+records.getRecord(i).getId()).value));
		}
	}
	return totalstandardDeductionsAmount;
}
function calculateTotalCustomDeductions()
{
	var records= customDeductionsDataTable.getRecordSet();
	var totalcustomDeductionsAmount=0.0; 
	for(i=0;i<records.getLength();i++){
		if(dom.get("creditamount"+records.getRecord(i).getId()).value!=0.0){
			totalcustomDeductionsAmount=  totalcustomDeductionsAmount + 
			eval(getNumber(dom.get("creditamount"+records.getRecord(i).getId()).value));
		}
	}
	return totalcustomDeductionsAmount;
}
function calculateTotalRetentionMoneyDeductions()
{
	var records= retentionMoneyDataTable.getRecordSet();
	var totalretentionMoneyAmount=0.0; 
	for(i=0;i<records.getLength();i++){
		if(dom.get("creditamount"+records.getRecord(i).getId()).value!=0.0){
			totalretentionMoneyAmount=  totalretentionMoneyAmount + 
			eval(getNumber(dom.get("creditamount"+records.getRecord(i).getId()).value));
		}
	}
	return totalretentionMoneyAmount;
}
function calculateTotalReleaseWithHeldAmount()
{
	var records= releaseAmountDataTable.getRecordSet();
	var totalreleaseWithHeldAmount=0.0; 
	for(i=0;i<records.getLength();i++){
		if(dom.get("debitamount"+records.getRecord(i).getId()).value!=0.0){
			totalreleaseWithHeldAmount=  totalreleaseWithHeldAmount + 
			eval(getNumber(dom.get("debitamount"+records.getRecord(i).getId()).value));
		}
	}
	return totalreleaseWithHeldAmount;
}
</script>
 <div class="errorstyle" id="accountDetails_error" style="display:none;"></div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<!--  Account Deduction for contractor bill -->
	<tr><td><%@ include file="contractorBill-accountDetails.jsp"%></td></tr>
	<!--  end  -->
	<tr><td colspan="4" class="shadowwk"> </td></tr>
	<tr><td colspan="4">&nbsp;</td></tr>
	<!--  Release Withheld Amount for contractor bill -->
	<tr><td><%@ include file="contractorBill-releaseWithHoldAmount.jsp"%></td></tr>
	<!--  end  -->
	<tr><td colspan="4" class="shadowwk"> </td></tr>
	<tr><td colspan="4">&nbsp;</td></tr>
	<!--  Statutory Deduction for contractor bill -->
	<%@ include file="contractorBill-statutoryDeduction.jsp"%>
	<!--  end  -->		
	<tr><td colspan="4" class="shadowwk"> </td></tr>
	<tr><td colspan="4">&nbsp;</td></tr>
	<!--  Other Deduction for contractor bill -->
	<%@ include file="contractorBill-otherDeductions.jsp"%>
	<!--  end  -->
	<tr><td colspan="4" class="shadowwk"> </td></tr>
	<tr><td colspan="4">&nbsp;</td></tr>
	<!--  Advance Adjustment for contractor bill -->
	<%@ include file="contractorBill-advanceAdjustment.jsp"%>
	<tr>
		<td colspan="4" class="shadowwk"> </td>               
	</tr>
	<tr><td>&nbsp;</td></tr>
	<%@ include file="contractorBill-netPayable.jsp"%>	
</table>

