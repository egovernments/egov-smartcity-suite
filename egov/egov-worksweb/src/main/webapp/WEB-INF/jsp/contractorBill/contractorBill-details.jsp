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
	var currenbilldeduction;
	if(isNaN(dom.get("currenbilldeduction").value) || getNumber(dom.get("currenbilldeduction").value)<0 ||
	dom.get("currenbilldeduction").value==""){
		currenbilldeduction=0.0;
	}
	else{currenbilldeduction = dom.get("currenbilldeduction").value;}
	var netPayableAmount=eval(totalAmount)-(eval(totalstatutoryDeductionsAmount)+eval(currenbilldeduction)+
	eval(totalstandardDeductionsAmount)+eval(totalcustomDeductionsAmount)+eval(totalretentionMoneyAmount));
	dom.get("netPayableAmount").value = roundTo(netPayableAmount);
	return totalAmount;
}
function roundToRupees(value){
	var amount = roundToWhole(value);
	document.getElementById(value.id).value = amount;
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
</script>
 <div class="errorstyle" id="accountDetails_error" style="display:none;"></div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<!--  Account Deduction for contractor bill -->
	<tr><td><%@ include file="contractorBill-accountDetails.jsp"%></td></tr>
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

