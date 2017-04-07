
<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>

<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<head>
<!-- <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script> -->
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/autocomplete-debug.js?rnd=${app_release_no}"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/receiptinstrument.js?rnd=${app_release_no}"></script>
<style type="text/css">
#bankcodescontainer {position:absolute;left:11em;width:9%;text-align: left;}
	#bankcodescontainer .yui-ac-content {position:absolute;width:350px;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
	#bankcodescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:300px;background:#a0a0a0;z-index:9049;}
	#bankcodescontainer ul {padding:5px 0;width:100%;}
	#bankcodescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
	#bankcodescontainer li.yui-ac-highlight {background:#ff0;}
	#bankcodescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
</style>
<script type="text/javascript">

jQuery.noConflict();

var isDatepickerOpened=false;
jQuery(document).ready(function() {
  	 
     jQuery(" form ").submit(function( event ) {
    	 doLoadingMask();
    });
     doLoadingMask();
     onBodyLoad();

     var nowTemp = new Date();
     var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);
     
     jQuery( "#instrumentDate" ).datepicker({ 
    	 format: 'dd/mm/yyyy',
    	 autoclose:true,
         onRender: function(date) {
      	    return date.valueOf() < now.valueOf() ? 'disabled' : '';
      	  }
	  }).on('changeDate', function(ev) {
		  var string=jQuery(this).val();
		  if(!(string.indexOf("_") > -1)){
			  isDatepickerOpened=false; 
	       	  checkForCurrentDate(this);
		  }
		  
	  }).data('datepicker');

     jQuery( "#manualReceiptDate" ).datepicker({ 
    	 format: 'dd/mm/yyyy',
    	 autoclose:true,
    	 onRender: function(date) {
       	    return date.valueOf() < now.valueOf() ? 'disabled' : '';
       	  }
	  }).on('changeDate', function(ev) {
		  var string=jQuery(this).val();
		  if(!(string.indexOf("_") > -1)){
			  isDatepickerOpened=false; 
	       	  checkForCurrentDate(this);
			  validateManualReceiptDate(this);
		  }
	  }).data('datepicker');

     
 });

jQuery(window).load(function () {
	undoLoadingMask();
});


function onChangeBankAccount(branchId) {
	var serviceName = document.getElementById("serviceName").value;
	var fundName = "", fundId = -1;
	if (document.getElementById('fundId') != null
			&& document.getElementById('fundId') != null) {
		var serviceId = document.getElementById("serviceId").value;
		fundId = document.getElementById('fundId').value;
		populateaccountNumberMaster({
			branchId : branchId,
			serviceId : serviceId,
			fundId : fundId
		});
	} else if (document.getElementById("fundName") != null) {
		fundName = document.getElementById("fundName").value;
		populateaccountNumberMaster({
			branchId : branchId,
			serviceName : serviceName,
			fundName : fundName
		});
	}
}
var accountscount=0;
var initialSetting="true";

<jsp:useBean id="now" class="java.util.Date" />

<fmt:formatDate var = "currDate" pattern="dd/MM/yyyy" value="${now}" />
	var currDate = "${currDate}";
	
	

function calculateCollectionTotal(){
		var collectiontotal=0
		var chequetable=document.getElementById('chequegrid');
		var chequetablelen =chequetable.rows.length;
		
		for(var m=0;m<chequetablelen;m++){
			if(getControlInBranch(chequetable.rows[m],'instrumentChequeAmount')!=null){
				chequeamount=eval(getControlInBranch(chequetable.rows[m],'instrumentChequeAmount').value);
				chequeamount = isNaN(chequeamount)?0:chequeamount;
				collectiontotal=collectiontotal+chequeamount;
			}
		}//end of for loop

		cashamount=eval(document.getElementById("instrHeaderCash.instrumentAmount").value);
		cashamount = isNaN(cashamount)?0:cashamount;
		collectiontotal=collectiontotal+cashamount;
		
		cardamount=eval(document.getElementById("instrHeaderCard.instrumentAmount").value);
		cardamount = isNaN(cardamount)?0:cardamount;
		collectiontotal=collectiontotal+cardamount;
		
		bankamount=eval(document.getElementById("instrHeaderBank.instrumentAmount").value);
		bankamount = isNaN(bankamount)?0:bankamount;
		collectiontotal=collectiontotal+bankamount;

		return collectiontotal;
}

function initialiseCreditAmount(){
	var noofaccounts=document.getElementById("totalNoOfAccounts").value;
	for(var j=0;j<noofaccounts; j++)
	{
		if(document.getElementById('receiptDetailList['+j+'].cramountToBePaid').value>=0){
		document.getElementById('receiptDetailList['+j+'].cramount').value=document.getElementById('receiptDetailList['+j+'].cramountToBePaid').value;
		}
	}
	initialSetting="false";
}

function calculateCreditTotal(){
	var noofaccounts=document.getElementById("totalNoOfAccounts").value;
	var creditamount=0,credittotal=0,debitamount=0;
	//this step is done to populate credit amount
        for(var j=0;j<noofaccounts; j++)
	{
		var receivedAmount=eval(document.getElementById('receiptDetailList['+j+'].cramount').value);
		var rebateAmount=eval(document.getElementById('receiptDetailList['+j+'].dramount').value);
		creditamount = isNaN(receivedAmount)?0:receivedAmount;
		debitamount =  isNaN(rebateAmount)?0:rebateAmount;
		credittotal=credittotal+creditamount;
		credittotal=credittotal-debitamount;
	}
	return Math.round(credittotal*100)/100;
}

function validate()
{
	callpopulateapportioningamountforbills();	
	if (document.getElementById("bankChallanDate").value=='DD/MM/YYYY')	{
		document.getElementById("bankChallanDate").value="";
	}

    document.getElementById("receipt_error_area").innerHTML="";
	document.getElementById("amountoverrideerror").style.display="none";
	document.getElementById("invaliddateformat").style.display="none";
	document.getElementById("receipt_dateerror_area").style.display="none";
	var validation = true;
		<s:if test="%{!isBillSourcemisc()}"> 
		if(document.getElementById('manualreceiptinfo').checked==true){
				if(document.getElementById("manualReceiptDate").value=="" ){
							document.getElementById("receipt_error_area").innerHTML+=
									'<s:text name="billreceipt.manualreceiptdatemissing.error" />'+ '<br>';
						validation = false;		
				}
				if(document.getElementById("manualReceiptNumber").value==""){
								document.getElementById("receipt_error_area").innerHTML+=
									'<s:text name="billreceipt.manualreceiptnumbermissing.error" />'+ '<br>';
						validation = false;			
				}
		}
		</s:if>
				
	<s:if test="%{isBillSourcemisc()}"> 
		if(validateMiscReceipt){
			if(!validateMiscReceipt()){
				return false;
			}
		}
	</s:if>
	var checkpartpaymentvalue=document.getElementById("partPaymentAllowed").value;
	var collectiontotal=0,cashamount=0,chequeamount=0,cardamount=0,bankamount=0,billingtotal=0;
	var zeroAccHeads=false;
	<s:if test="%{isBillSourcemisc()}"> 
		billingtotal=document.forms[0].misctotalAmount.value;
 	</s:if>
 	<s:else>
		var noofaccounts=document.getElementById("totalNoOfAccounts").value;
		var totalCreditAmountToBePaid = 0;
		for(var j=0;j<noofaccounts; j++)
		{
			var advanceRebatePresent=document.getElementById('receiptDetailList['+j+'].isActualDemand').value;			
			var amounttobecollected=document.getElementById('receiptDetailList['+j+'].cramountToBePaid').value;
			totalCreditAmountToBePaid = eval(totalCreditAmountToBePaid)+eval(amounttobecollected);
			if(advanceRebatePresent==0){
				zeroAccHeads=true;
			}
		}
		if(document.getElementById("callbackForApportioning").value=="false")	
		{	
 			billingtotal=document.forms[0].totalAmntToBeCollected.value;
		}
		else
		{	
 			billingtotal=totalCreditAmountToBePaid;
		}


		
 	</s:else>
	var instrTypeCash = document.getElementById("cashradiobutton").checked;
	var instrTypeCheque = document.getElementById("chequeradiobutton").checked;
	var instrTypeDD = document.getElementById("ddradiobutton").checked;
	var instrTypeCard = document.getElementById("cardradiobutton").checked;
	var instrTypeBank = document.getElementById("bankradiobutton").checked;
	var instrTypeOnline = document.getElementById("onlineradiobutton").checked;
	var chequetable=document.getElementById('chequegrid')
	var chequetablelen1 =chequetable.rows.length;

	//if mode of payment is cash
	if(instrTypeCash){
		if(document.getElementById("instrHeaderCash.instrumentAmount")!=null)
		{
			cashamount=document.getElementById("instrHeaderCash.instrumentAmount").value;
			//|| cashamount.startsWith('+')
			if(cashamount==null || cashamount=="" || isNaN(cashamount) || cashamount<0){
				document.getElementById("receipt_error_area").innerHTML+=
				'<s:text name="billreceipt.invalidcashamount.errormessage" />'+ '<br>';
				validation = false;
			}
			else
			{
			    cashamount=eval(cashamount);
				if(cashamount==0){
					document.getElementById("receipt_error_area").innerHTML+=
					'<s:text name="billreceipt.missingcashamount.errormessage" />'+ '<br>';
					validation = false;
				}
				collectiontotal=collectiontotal+cashamount;
			}
			document.getElementById('instrumentTypeCashOrCard').value="cash";
		}
	}
	//if mode of payment is card
	if(instrTypeCard){
		if(document.getElementById("instrHeaderCard.transactionNumber")!=null){

	    	var transNo=document.getElementById("instrHeaderCard.transactionNumber").value;
		    if(transNo==null || transNo==""){
		    	document.getElementById("receipt_error_area").innerHTML+='<s:text name="billreceipt.missingcardtransactionno.errormessage" /> ' + '<br>';
		    	validation=false;
		    }
		}
		if(document.getElementById("confirmtransactionNumber")!=null){

	    	var confirmtransNo=document.getElementById("confirmtransactionNumber").value;
		    if(confirmtransNo==null || confirmtransNo==""){
		    	document.getElementById("receipt_error_area").innerHTML+='<s:text name="billreceipt.missingcard.confirmtransactionno.errormessage" /> ' + '<br>';
		    	validation=false;
		    }
		    else
		    	validateTransactionNumber();
		}
		if(document.getElementById("instrHeaderCard.instrumentNumber")!=null){
		    var cardNo=document.getElementById("instrHeaderCard.instrumentNumber").value;
		    if(cardNo==null || isNaN(cardNo) || cardNo.length < 4){
		    	document.getElementById("receipt_error_area").innerHTML+='<s:text name="billreceipt.missingcardno.errormessage" />' + '<br>';
		    	validation=false;
		    }
		}
		if(document.getElementById("instrHeaderCard.instrumentAmount")!=null){

			cardamount=document.getElementById("instrHeaderCard.instrumentAmount").value;
			if(cardamount==null || cardamount=="" || isNaN(cardamount) || cardamount<0 || cardamount.startsWith('+')){
				document.getElementById("receipt_error_area").innerHTML+='<s:text name="billreceipt.invalidcardamount.errormessage" />'+ '<br>';
				validation = false;
			}
			else{
				cardamount=eval(cardamount);
				if(cardamount==0){
					document.getElementById("receipt_error_area").innerHTML+='<s:text name="billreceipt.missingcardamount.errormessage" />'+ '<br>';
					validation = false;
				}
				collectiontotal=collectiontotal+cardamount;
			}
		}
		document.getElementById('instrumentTypeCashOrCard').value="card";
	}
	
	//if mode of payment is bank
	if(instrTypeBank){
		if(document.getElementById("instrHeaderBank.transactionNumber")!=null){
	    	var transNo=document.getElementById("instrHeaderBank.transactionNumber").value;
		    if(transNo==null || transNo==""){
		    	document.getElementById("receipt_error_area").innerHTML+='<s:text name="billreceipt.missingbankchallanno.errormessage" /> ' + '<br>';
		    	validation=false;
		    }
		    else if(isNaN(transNo) || transNo < 0 || transNo.length < 6 || transNo.startsWith('+')){
		    	document.getElementById("receipt_error_area").innerHTML+='<s:text name="billreceipt.invalidbankchallanno.errormessage" /> ' + '<br>';
		    	validation=false;
		    }
		}
		if(document.getElementById("bankChallanDate")!=null){
			var bankChallanDate=document.getElementById("bankChallanDate").value;
			if(bankChallanDate==null || bankChallanDate==""){
				document.getElementById("receipt_error_area").innerHTML+='<s:text name="billreceipt.missingbankchallandate.errormessage" />'+ '<br>';
				validation = false;
			}
			else {
				var receiptDate;
				 <s:if test="%{!isBillSourcemisc()}">
				 receiptDate = document.getElementById("manualReceiptDate").value;
				</s:if>
				<s:else>
				receiptDate = document.getElementById("voucherDate").value;
				</s:else>
				if(receiptDate!=null && process(bankChallanDate) > process(receiptDate)){
 	 	    		document.getElementById("receipt_error_area").innerHTML+=
 	 					'<s:text name="miscreceipt.error.transactiondate.greaterthan.receiptdate" />'+ '<br>';   	
 	 				window.scroll(0,0);
 	 				validation=false;
 		 	   	}
			}
		}
		if(document.getElementById("instrHeaderBank.instrumentAmount")!=null){
			bankamount=document.getElementById("instrHeaderBank.instrumentAmount").value;
			if(bankamount==null || bankamount=="" || isNaN(bankamount) || bankamount<0 || bankamount.startsWith('+')){
				document.getElementById("receipt_error_area").innerHTML+='<s:text name="billreceipt.invalidbankchallanamount.errormessage" />'+ '<br>';
				validation = false;
			}
			else{
				bankamount=eval(bankamount);
				if(bankamount==0){
					document.getElementById("receipt_error_area").innerHTML+='<s:text name="billreceipt.missingbankchallanamount.errormessage" />'+ '<br>';
					validation = false;
				}
				collectiontotal=collectiontotal+bankamount;
			}
		}
		if(null == document.getElementById('bankBranchMaster') || document.getElementById('bankBranchMaster').value == 0){
			document.getElementById("receipt_error_area").innerHTML+='<s:text name="billreceipt.missingbankbranchname.errormessage" />'+  "<br>";
			validation=false;
		}
		if(null == document.getElementById('accountNumberMaster') || document.getElementById('accountNumberMaster').value == 0){
			document.getElementById("receipt_error_area").innerHTML+='<s:text name="billreceipt.missingbankaccountname.errormessage" />'+  "<br>";
			validation=false;
		}
	document.getElementById('instrumentTypeCashOrCard').value="bankchallan";
	}
	//if mode of payment is cheque/DD
	if(instrTypeCheque || instrTypeDD){
		var count=document.forms[0].instrumentCount.value;

		if(count == 0) {
 			var  date = document.getElementById('instrumentDate');
   			checkForCurrentDate(date);
  		} else {
       		for(var i=1;i<=count;i++){
          	if(document.forms[0].instrumentDate[i].value==null || document.forms[0].instrumentDate[i].value=="" || document.forms[0].instrumentDate[i].value=="DD/MM/YYYY"){
                 checkForCurrentDate(document.forms[0].instrumentDate[i]);
            	  }
            }
	  	}
	    if(!verifyChequeDetails(chequetable,chequetablelen1)){
	         validation=false;
	    }
	    else{
			for(var m=0;m<chequetablelen1;m++)
			{
				if(getControlInBranch(chequetable.rows[m],'instrumentChequeAmount')!=null)
				{
					chequeamount=getControlInBranch(chequetable.rows[m],'instrumentChequeAmount').value;
					chequeamount=eval(chequeamount);
	    				collectiontotal=collectiontotal+chequeamount;
	    			}
			}//end of for loop
		}//end of else
		document.getElementById('instrumentTypeCashOrCard').value="";
	}
	//if mode of payment is online
	if(instrTypeOnline){
		if(document.getElementById("instrHeaderOnline.instrumentAmount")!=null)
		{
			onlineamount=document.getElementById("instrHeaderOnline.instrumentAmount").value;
			if(onlineamount==null || onlineamount=="" || isNaN(onlineamount) || onlineamount<0){
				document.getElementById("receipt_error_area").innerHTML+=
				'<s:text name="billreceipt.invalidcreditamount.errormessage" />'+ '<br>';
				validation = false;
			}
			else
			{
			    onlineamount=eval(onlineamount);
				if(onlineamount==0){
					document.getElementById("receipt_error_area").innerHTML+=
					'<s:text name="billreceipt.invalidcreditamount.errormessage" />'+ '<br>';
					validation = false;
				}
				collectiontotal=collectiontotal+onlineamount;
			}
			document.getElementById('instrumentTypeCashOrCard').value="online";
		}
	}
	document.getElementById("instrumenttotal").value=collectiontotal;
	var credittotal=calculateCreditTotal();
	var checkoverridevalue=document.getElementById("overrideAccountHeads").value;
	var advancePaymentAllowed=false;
	var minimumAmt=eval(document.getElementById("minimumAmount").value);
	if(minimumAmt==null){
	    minimumAmt=0;
	}
	if(zeroAccHeads==true){
	    advancePaymentAllowed=true;
	}
	if(collectiontotal!=0){
		var billingTotalNumberFormat=Number(billingtotal);
	    //display error if actual payment amt > original billed amt, and there is no 'zero' account head.
	    if(billingTotalNumberFormat < collectiontotal && advancePaymentAllowed==false)
		{
			document.getElementById("receipt_error_area").innerHTML+='<s:text name="billreceipt.greatercollectionamounterror.errormessage" />' + '<br>';
			validation=false;
		}
		// if there is an advance payment and overriding of acc heads is permitted, but value has not been manually entered
		// - display error if credit total (sum of amts paid for the account heads) is not equal to payment amount entered in modes of payt
		if(collectiontotal != credittotal  && advancePaymentAllowed==true && checkoverridevalue==true && document.getElementById("callbackForApportioning").value==false)
		{
			document.getElementById("receipt_error_area").innerHTML+='<s:text name="billreceipt.incorrectaccountheadamt.errormessage" />' + '<br>';
			validation=false;
		}
		if(collectiontotal < billingTotalNumberFormat && checkpartpaymentvalue==='false'){
			document.getElementById("receipt_error_area").innerHTML+='<s:text name="billreceipt.total.errormessage" />' + '<br>';
			validation=false;
		}
		// display error if actual payt amt < original billed amt after system has done apportioning 
		// (citizen might have manually changed credit amounts after system apportioning of account head amounts
		else if(collectiontotal < billingtotal && checkpartpaymentvalue==true){
		        if(collectiontotal < minimumAmt){
		        	document.getElementById("receipt_error_area").innerHTML+='<s:text name="billreceipt.paytlessthanmin.errormessage" />' + '<br>';
		        	validation=false;
		        }
		}
		else
		{
			if(!checkaccountheaderwiseamount()){
				validation=false;
			}
		}

	}
    	var paidby=document.getElementById("paidBy").value;
    	paidby = trimAll(paidby);
   	if(paidby.length == 0 || paidby==""){
   		document.getElementById("receipt_error_area").innerHTML+='<s:text name="billreceipt.missingpayeename.errormessage" /> ' + '<br>';
		validation = false;
   	}

   	<s:if test="%{!isBillSourcemisc()}"> 
	 if(eval(document.getElementById("totalamountdisplay").value)>eval(document.getElementById("totalamounttobepaid").value)){
		 var r = confirm('Collected amount is more than the amount to be paid. Do you want to collect advance amount?');
		 if(r !=true)
			 validation = false;
	 }
	 </s:if>

   	if(validation==false &&  document.getElementById("receipt_error_area").innerHTML!=''){
		document.getElementById("receipt_error_area").style.display="block";
		window.scroll(0,0);
	}
	if(validation==true){
		var instrDate = document.getElementById('instrumentDate').value;
	    if(instrDate==null || instrDate=="" || instrDate=="DD/MM/YYYY"){
	    	document.getElementById('instrumentDate').value="";
	    }
	}
	if(validation==false){
		return false;
	}
	else {
		document.getElementById("receipt_error_area").style.display="block";
		document.collDetails.action="receipt-save.action";
		return validation;
	}
}//end of function 'validate'

function verifyChequeDetails(table,len1)
{
	var check=true;

	var instrTypeErrMsg = "";
	var instrNoErrMsg = "";
	var bankNameErrMsg = "";
	var instrAmountErrMsg = "";
	var instrDateErrMsg = "";
	var instrumentType = "";
	var instrAmountErrMsg = "";
	var instrAmountInvalidErrMsg ="";

   
	for(var j=0;j<len1;j++)
	{
	    //validate if one of the instrument types - cheque/DD has been ticked
	    if(getControlInBranch(table.rows[j],'instrumentType')!=null){
	    	instrumentType=getControlInBranch(table.rows[j],'instrumentType').value;
	    	if(instrumentType==null || instrumentType==""){
	    		if(instrTypeErrMsg==""){
	    		    instrTypeErrMsg='<s:text name="billreceipt.selectinstrumenttype.errormessage"/>' + '<br>';
	    			document.getElementById("receipt_error_area").innerHTML+=instrTypeErrMsg;
	    		}
	    		check=false;
	    	}
	    }

	    //validate if cheque/DD number has been entered
	    if(getControlInBranch(table.rows[j],'instrumentChequeNumber')!=null){
	    	var instrNo=getControlInBranch(table.rows[j],'instrumentChequeNumber').value;
	    	if(instrNo==null || instrNo=="" || isNaN(instrNo) || instrNo.length!=6){
	    		if(instrNoErrMsg==""){
	    		    instrNoErrMsg='<s:text name="billreceipt.invalidchequenumber.errormessage" />' + '<br>';
	    			document.getElementById("receipt_error_area").innerHTML+=instrNoErrMsg;
	    		}
	    		check=false;
	    	}
	    }

	    //validate if bank name has been entered
	    if(getControlInBranch(table.rows[j],'bankName')!=null && getControlInBranch(table.rows[j],'bankID')!=null){
	    	var bankName=getControlInBranch(table.rows[j],'bankName').value;
	    	var bankId=getControlInBranch(table.rows[j],'bankID').value;
	    	if(bankName==null || bankName=="" || bankId==null || bankId==""){
	    		if(bankNameErrMsg==""){
	    		    bankNameErrMsg='<s:text name="billreceipt.missingbankid.errormessage" />' + '<br>';
	    			document.getElementById("receipt_error_area").innerHTML+=bankNameErrMsg;
	    		}
	    		check=false;
	    	}
	    }
	    //validate if valid date has been entered
	    if(getControlInBranch(table.rows[j],'instrumentDate')!=null){
	    var instrDate=getControlInBranch(table.rows[j],'instrumentDate');
	    <s:if test="%{!isBillSourcemisc()}">
	    	if(instrDate.value==null || instrDate.value=="" || instrDate.value=="DD/MM/YYYY"){
	    		if(instrDateErrMsg==""){
	    		    instrDateErrMsg='<s:text name="billreceipt.missingchequedate.errormessage" />' + '<br>';
	    			document.getElementById("receipt_error_area").innerHTML+=instrDateErrMsg;
	    		}
	    		check=false;
	    		
	    	} 
	    	else if (document.getElementById('manualreceiptinfo').checked==true){
	    		var receiptDate = document.getElementById("manualReceiptDate").value;
		    	 if(receiptDate !=null && receiptDate != '' && instrDate.value != null && instrDate.value!= '' && check==true ){
	    			if(process(instrDate.value) > process(receiptDate)){
	 	 	    		document.getElementById("receipt_error_area").innerHTML+=
	 	 					'<s:text name="miscreceipt.error.instrumentdate.greaterthan.receiptdate" />'+ '<br>';   	
	 	 				window.scroll(0,0);
	 	 				check=false;
	 		 	   	}
    				checkForCurrentDate(instrDate);
	    	}
	    	}
	    </s:if>

	    <s:else>
	    	if(instrDate.value==null || instrDate.value=="" || instrDate.value=="DD/MM/YYYY"){
	    		if(instrDateErrMsg==""){
	    		    instrDateErrMsg='<s:text name="billreceipt.missingchequedate.errormessage" />' + '<br>';
	    			document.getElementById("receipt_error_area").innerHTML+=instrDateErrMsg;
	    		}
	    		check=false;
	    	 } else {
	 	    		var receiptDate = document.getElementById("voucherDate").value;
	 	 	    	if(instrDate.value != null && instrDate.value!= '' && check==true && process(instrDate.value) > process(receiptDate)){
	 	 	    		document.getElementById("receipt_error_area").innerHTML+=
	 	 					'<s:text name="miscreceipt.error.instrumentdate.greaterthan.receiptdate" />'+ '<br>';   	
	 	 				window.scroll(0,0);
	 	 				check=false;
	 		 	   	}
	    		     checkForCurrentDate(instrDate);
	    		   } 	               
	    </s:else>
	    }

	    if(getControlInBranch(table.rows[j],'instrumentChequeAmount')!=null)
		{
				var chequeamount=getControlInBranch(table.rows[j],'instrumentChequeAmount').value;
				if(chequeamount==null || chequeamount=="" || isNaN(chequeamount) || chequeamount<0 || chequeamount.startsWith('+')){
					if(instrAmountInvalidErrMsg==""){
						instrAmountInvalidErrMsg='<s:text name="billreceipt.invalidchequeamount.errormessage" />' + '<br>';
	    				document.getElementById("receipt_error_area").innerHTML+=instrAmountInvalidErrMsg;
	    				check=false;
	    			}
				}
				else{
					chequeamount=eval(chequeamount);
					if(chequeamount==0){
	    				if(instrAmountErrMsg==""){
	    		    		instrAmountErrMsg='<s:text name="billreceipt.missingchequeamount.errormessage" />' + '<br>';
	    					document.getElementById("receipt_error_area").innerHTML+=instrAmountErrMsg;
	    					check=false;
	    				}
	    			}
	    		}
		}
	}
		return check;
} // end of function verifychecqueDetails


	
	

function checkaccountheaderwiseamount()
{	
    var zeroAccHeads=false; 	
    var checkoverridevalue=document.getElementById("overrideAccountHeads").value;
    

	var noofaccounts=document.getElementById("totalNoOfAccounts").value;
	for(var j=0;j<noofaccounts; j++)
	{
		var advanceRebatePresent=document.getElementById('receiptDetailList['+j+'].isActualDemand').value;
		if(advanceRebatePresent==0){
			zeroAccHeads=true;
			break;
		}
	}

	var advancePaymentAllowed=false;
	if(zeroAccHeads==true){
	    advancePaymentAllowed=true;
	}
	
	for(var j=0;j<accountscount; j++)
	{
		var tobecollectedamount=eval(document.getElementById('receiptDetailList['+j+'].cramountToBePaid').value);
		var amountreceived=eval(document.getElementById('receiptDetailList['+j+'].cramount').value);
		if(isNaN(amountreceived)){
		    document.getElementById("receipt_error_area").innerHTML+='<s:text name="billreceipt.invalidcreditamount.errormessage" />' + '<br>';
			return false;
		}
		if(advancePaymentAllowed==true && tobecollectedamount>0 && amountreceived>tobecollectedamount){
			document.getElementById("amountoverrideerror").style.display="block";
			return false;
		}
		else if(advancePaymentAllowed==false && tobecollectedamount>0 && amountreceived>tobecollectedamount) // advance payt is false  
		{
			document.getElementById("amountoverrideerror").style.display="block";
			return false;
		}
		else if(j<accountscount-1) // if the grid still have more records to validate then continue the loop to fetch next record
		{
			continue;
		}
		else
		{
			document.getElementById("amountoverrideerror").style.display="none";
			return true;
		}
	}
	return true;
}

function onBodyLoad()
{
	var headertable=document.getElementById('billsheaderinfotable');
	var headertablelength=headertable.rows.length;
	var checkoverridevalue=document.getElementById("overrideAccountHeads").value;
	if(null != document.getElementById('asteriskId')){
		document.getElementById('asteriskId').style.display="";
	}
	if(null != document.getElementById('manualreceiptinfo')){
		document.getElementById('manualreceiptinfo').checked=false;
	}
	if(null != document.getElementById('manualReceiptNumber')){
		document.getElementById('manualReceiptNumber').disabled=true;
	}
	if(null != document.getElementById('manualReceiptDate')){
		document.getElementById('manualReceiptDate').disabled=true;
	}

	<s:if test="%{isBillSourcemisc()}"> 
	 document.getElementById("totalNoOfAccounts").value = 0;
	 accountscount=document.getElementById("totalNoOfAccounts").value;
	</s:if>
	<s:else>
	accountscount=document.getElementById("totalNoOfAccounts").value;
	</s:else>

	<s:if test="%{isBillSourcemisc()}"> 
	//To load the account codes if only a misc receipt request
	if(onBodyLoadMiscReceipt){
		onBodyLoadMiscReceipt();
	}
	</s:if>
	<s:else>
    //set credit amount fields as non editable if 'checkoverride' flag is false
	for(var j=0;j<accountscount;j++)
	{
		if(checkoverridevalue=="true"){
			document.getElementById('receiptDetailList['+j+'].cramount').readOnly=false;
		}
		else{
			document.getElementById('receiptDetailList['+j+'].cramount').readOnly=true;
		}
	}
	</s:else>
	
	loadDropDownCodesBank();
	
	// To hide delete button in cheque grid on page load
	var chequetable=document.getElementById('chequegrid');
	if(getControlInBranch(chequetable.rows[3],'addchequerow')!=null)
		getControlInBranch(chequetable.rows[3],'addchequerow').style.display="block";
	if(getControlInBranch(chequetable.rows[3],'deletechequerow')!=null)
		getControlInBranch(chequetable.rows[3],'deletechequerow').style.display="none";
	
	if(document.getElementById('instrHeaderCash.instrumentAmount').value==""){
		document.getElementById('instrHeaderCash.instrumentAmount').value="";
	}
	if(document.getElementById('instrHeaderCard.instrumentAmount').value==""){
		document.getElementById('instrHeaderCard.instrumentAmount').value="";
	}
	if(document.getElementById('instrHeaderBank.instrumentAmount').value==""){
		document.getElementById('instrHeaderBank.instrumentAmount').value="";
	}
	if(document.getElementById('instrumentChequeAmount').value==""){
		document.getElementById('instrumentChequeAmount').value="";
	}
	if(document.getElementById('paidBy').value==""){
		var paidby =  '<s:property value="%{payeeName}" escapeJavaScript="true"/>';
		paidby = paidby.replace('&amp;','&');
		document.getElementById('paidBy').value=paidby;
	}
	if(document.getElementById('instrumentDate').value==""){
		waterMarkInitialize('instrumentDate','DD/MM/YYYY');
		
	}
	if(document.getElementById('bankChallanDate').value==""){
		waterMarkInitialize('bankChallanDate','DD/MM/YYYY');
		
	}
	displayPaytModes();
	displayPaymentDetails();
	loadchequedetails();
}

function checkandcalculatecredittotal(index,elem){
	var credittotal=0,creditamount=0,debitamount=0;
	var collectiontotal=0;
	
	document.getElementById("receipt_error_area").innerHTML="";
	document.getElementById("receipt_error_area").style.display="none";
	for(var j=0;j<accountscount;j++)
	{
		var tobecollectedamount=eval(document.getElementById('receiptDetailList['+j+'].cramountToBePaid').value);
		var receivedAmount=eval(document.getElementById('receiptDetailList['+j+'].cramount').value);
		var rebateamount=eval(document.getElementById('receiptDetailList['+j+'].dramount').value);		

		if(receivedAmount>tobecollectedamount && tobecollectedamount>0)
		{
			document.getElementById("amountoverrideerror").style.display="block";
			return false;
		}
		else
		{
			document.getElementById("amountoverrideerror").style.display="none";
		}

		creditamount = isNaN(receivedAmount)?0:receivedAmount;
		debitamount = isNaN(rebateamount)?0:rebateamount;
		credittotal=credittotal+creditamount;
		credittotal=credittotal-debitamount;
	}
	collectiontotal=calculateCollectionTotal();
	if(collectiontotal!=credittotal){
		document.getElementById("receipt_error_area").innerHTML+='<s:text name="billreceipt.total.amountoverride.errormessage" />' + '<br>';
		document.getElementById("receipt_error_area").style.display="block";
		}
	else{
		document.getElementById("receipt_error_area").innerHTML="";
		document.getElementById("receipt_error_area").style.display="none";
	}
	document.getElementById("totalamountdisplay").value=isNaN(credittotal)?credittotal:credittotal.toFixed(2);

	//set values
    fieldname="receiptDetailList["+index+"].cramount";
    document.getElementById(fieldname).value=elem.value;
}

function validateChallanDate(obj)
{
	if(validateDateFormat(obj)){
		document.getElementById("receipt_dateerror_area").style.display="none";
		document.getElementById("receipt_dateerror_area").innerHTML="";
	   	if(obj.value!="");
		if(!validateNotFutureDate(obj.value,currDate)){
		   document.getElementById("receipt_dateerror_area").style.display="block";
	       document.getElementById("receipt_dateerror_area").innerHTML+=
					'<s:text name="billreceipt.bankchallandate.futuredate.errormessage" />'+ '<br>';
	       obj.value = "";
	       // obj.focus();
	       obj.tabIndex="-1";
	       var keyCode = document.all? window.event.keyCode:event.which;
		   if(keyCode==9) {
	       window.scroll(0,0);
	       }
	       window.scroll(0,0);
	       return false;
		}
	}
	return true;
}

function scrolltop()
{
	 setTimeout(function(){ 
		  jQuery("html, body").animate({ scrollTop: 0 }, "fast");
	  }, 50);
}

function validateManualReceiptDate(obj)
{
      document.getElementById("receipt_dateerror_area").style.display="none";
	  document.getElementById("receipt_dateerror_area").innerHTML="";
	  if(jQuery(obj).val())
	  {
		  var dmy=jQuery(obj).val().split('/');

		  if(dmy.length === 3)
		  {
			  var seldate=new Date(dmy[2],(dmy[1]-1), dmy[0]);
			  if(seldate>new Date())
			  {
				  document.getElementById("receipt_dateerror_area").style.display="block";
			      document.getElementById("receipt_dateerror_area").innerHTML+=
							'<s:text name="billreceipt.manualreceipt.futuredate.errormessage" />'+'<br/>';
				  jQuery(obj).val('');
				  scrolltop();
				  return false;
			  }
		  }
		 
	  }
	
		/*trim(obj,obj.value);
		document.getElementById("receipt_dateerror_area").style.display="none";
		document.getElementById("receipt_dateerror_area").innerHTML="";
	   	if(obj.value!="");
		if(!validateNotFutureDate(obj.value,currDate)){
		   document.getElementById("receipt_dateerror_area").style.display="block";
	       document.getElementById("receipt_dateerror_area").innerHTML+=
					'<s:text name="billreceipt.manualreceipt.futuredate.errormessage" />'+ '<br>';
	       obj.value = "";
	       // obj.focus();
	       obj.tabIndex="-1";
	       var keyCode = document.all? window.event.keyCode:event.which;
		   if(keyCode==9) {
	       window.scroll(0,0);
	       }
	       window.scroll(0,0);
	       return false;
		}*/

		
	  return true;
	
	
}


function checkForCurrentDate(obj)
{
	var receiptDate;
	   document.getElementById("receipt_dateerror_area").style.display="none";
		document.getElementById("receipt_dateerror_area").innerHTML="";
	   <s:if test="%{!isBillSourcemisc()}">
		   if (  document.getElementById('manualreceiptinfo').checked==true){
			   if(document.getElementById("manualReceiptDate").value != null  && document.getElementById("manualReceiptDate").value != ''){
			   receiptDate = document.getElementById("manualReceiptDate").value;
			   } else {
				       receiptDate = "${currDate}";
				}
		   }
		   else {
			   receiptDate = "${currDate}"; 
			}
		</s:if>
		<s:else>
		{
		receiptDate = document.getElementById("voucherDate").value; 
		}
		</s:else>
	   var finDate = new Date('2012-04-01');
	   var year = receiptDate.substr(6,4);
	   var month = receiptDate.substr(3,2);
	   var day = receiptDate.substr(0,2);
	   var receiptDateFormat = new Date(year +'-' + month+ '-' +day);

	   if(obj.value != null && obj.value != "") {
	   if(!validatedays(obj.value,receiptDate)){
		   <s:if test="%{!isBillSourcemisc()}">
		   if (document.getElementById("manualReceiptDate").value != null && document.getElementById("manualReceiptDate").value != '') {
			if(receiptDateFormat<finDate) {
				 document.getElementById("receipt_dateerror_area").style.display="block";
					document.getElementById("receipt_dateerror_area").innerHTML+=
						'<s:text name="billreceipt.datelessthan6monthdate.errormessage" />'+ '<br>';
			}
			else {
				 document.getElementById("receipt_dateerror_area").style.display="block";
				  document.getElementById("receipt_dateerror_area").innerHTML+=
						'<s:text name="billreceipt.manualReceiptdatelessthanfinancialdate.errormessage" />'+ '<br>';
				}
		   } else{
			   document.getElementById("receipt_dateerror_area").style.display="block";
				  document.getElementById("receipt_dateerror_area").innerHTML+=
						'<s:text name="billreceipt.datelessthanreceiptdate.errormessage" />'+ '<br>';
			  	 }
		   </s:if>
		   <s:else>
		   document.getElementById("receipt_dateerror_area").style.display="block";
			  document.getElementById("receipt_dateerror_area").innerHTML+=
					'<s:text name="billreceipt.datelessthanreceiptdate.errormessage" />'+ '<br>';
		   </s:else>
		   jQuery(obj).val('');
		   scrolltop();
	       return false;
		   }
	   }
}

function checkreset()
{
	document.forms[0].reset();
	document.getElementById("delerror").style.display="none";
	document.getElementById("amountoverrideerror").style.display="none";
	document.getElementById("invaliddateformat").style.display="none";
	document.getElementById("receipt_dateerror_area").style.display="none";
	document.getElementById("receipt_error_area").style.display="none";
	
	clearCashDetails();
	clearCardDetails();
	clearBankDetails();
	clearChequeDDDetails();
	displayPaytModes();
	document.getElementById('paidBy').value='<s:property value="%{payeeName}" escapeJavaScript="true"/>';
	<s:if test="%{isBillSourcemisc()}"> 
		//To load the account codes if only a misc receipt request
		if(resetMisc){
			resetMisc();
		}
	</s:if>
}

function showHideMandataryMark(obj){
	
	if(obj.checked)
	{
		document.getElementById('asteriskId').style.display="";
		document.getElementById('manualReceiptNumber').disabled=false;
		document.getElementById('manualReceiptDate').disabled=false;
		document.getElementById('manualreceiptinfo').value=true;
		
	}
	else
	{
		
		document.getElementById('asteriskId').style.display="none";
		document.getElementById('manualReceiptNumber').value="";
	    document.getElementById('manualReceiptDate').value="";
		document.getElementById('manualReceiptNumber').disabled=true;
		document.getElementById('manualReceiptDate').disabled=true;
		document.getElementById('manualreceiptinfo').value=false;
		
	}
}
</script>

	<title><s:text name="billreceipt.pagetitle"/></title>
</head>
<!-- Area for error display -->
<body><br>


<div class="errorstyle" id="receipt_error_area" style="display:none;"></div>
<div class="errorstyle" id="error_area" style="display:none;"></div>
<div class="errorstyle" id="common_error_area" style="display:none;"></div>
<div class="errorstyle" id="receipt_dateerror_area" style="display:none;"></div>

<span align="center" style="display:none" id="delerror">
  <li>
     <font size="2" color="red"><b><s:text name="common.lastrow.error"/></b></font>
  </li>
</span>

<span align="center" style="display:none" id="amountoverrideerror">
  <li>
     <font size="2" color="red"><b>
		<s:text name="billreceipt.amountoverride.errormessage"/>
	 </b></font>
  </li>
</span>

<span align="center" style="display:none" id="invaliddateformat">
  <li>
     <font size="2" color="red"><b>
		<s:text name="common.dateformat.errormessage"/>
	</b></font>
  </li>
</span>
<s:if test="%{hasErrors()}">
	<div align="center">
	    <div id="actionErrorMessages" class="alert alert-danger">
	      <s:actionerror/>
	      <s:fielderror/>	      
	    </div>
	    <input name="button" type="button" class="button" id="buttonclose" value="Close" onclick="window.close();" />
	</div>
	</s:if>
	<s:else>
<div class="formmainbox" style="width:100%;max-width:960px;">
	
	<s:if test="%{hasActionMessages()}">
	    <div id="actionMessages" class="messagestyle">
	    	<s:actionmessage theme="simple"/>
	    </div>
	</s:if>

	<s:form theme="simple" name="collDetails" action="receipt">
	<s:token />
	<s:push value="model">

	<s:if test="%{!isBillSourcemisc()}"> 
		<div class="subheadnew"><s:property value="%{serviceName}" /></div>
	</s:if>
	<s:if test="%{isBillSourcemisc()}"> 
		<div class="subheadnew"><s:text name="miscreceipt.title"/></div>
	</s:if>
	<s:if test="%{!isBillSourcemisc()}"> 
		<div class="subheadsmallnew">
		<span class="subheadnew"><s:text name="billreceipt.billdetails"/></span>
		</div>
	</s:if>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr><td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" id="billsheaderinfotable">
		<s:hidden name="collectXML" id="collectXML" value="%{collectXML}" />
		<s:hidden label="totalNoOfAccounts" id="totalNoOfAccounts" value="%{totalNoOfAccounts}" name="totalNoOfAccounts"/>
		<s:hidden label="reasonForCancellation" id="reasonForCancellation" name="reasonForCancellation" />
		<s:hidden label="oldReceiptId" id="oldReceiptId" name="oldReceiptId"/>
		<s:hidden label="overrideAccountHeads" id="overrideAccountHeads" value="%{overrideAccountHeads}" name="overrideAccountHeads"/>
		<s:hidden label="partPaymentAllowed" id="partPaymentAllowed" value="%{partPaymentAllowed}" name="partPaymentAllowed"/>
		<s:hidden label="minimumAmount" id="minimumAmount" value="%{minimumAmount}" name="minimumAmount"/>
		<s:hidden label="cashAllowed" id="cashAllowed" value="%{cashAllowed}" name="cashAllowed"/>
		<s:hidden label="cardAllowed" id="cardAllowed" value="%{cardAllowed}" name="cardAllowed"/>
		<s:hidden label="chequeAllowed" id="chequeAllowed" value="%{chequeAllowed}" name="chequeAllowed"/>
		<s:hidden label="bankAllowed" id="bankAllowed" value="%{bankAllowed}" name="bankAllowed"/>
		<s:hidden label="ddAllowed" id="ddAllowed" value="%{ddAllowed}" name="ddAllowed"/>
		<s:hidden label="onlineAllowed" id="onlineAllowed" value="%{onlineAllowed}" name="onlineAllowed"/>
		<s:hidden label="billSource" id="billSource" value="%{billSource}" name="billSource"/>
		<s:hidden label="serviceName" id="serviceName" value="%{serviceName}" name="serviceName"/>
		<s:hidden label="fundName" id="fundName" value="%{fundName}" name="fundName"/>
		<s:hidden label="callbackForApportioning" id="callbackForApportioning" value="%{callbackForApportioning}" name="callbackForApportioning"/>
		<s:hidden label="instrumenttotal" id="instrumenttotal" name="instrumenttotal"/>
		<s:hidden label="manualReceiptNumberAndDateReq" id="manualReceiptNumberAndDateReq" name="manualReceiptNumberAndDateReq" value="%{manualReceiptNumberAndDateReq}"/>
				
		<s:if test="%{!isBillSourcemisc()}">
		      	<%@ include file='collection-billdetails.jsp'%>
		</s:if> 
		</table>
		</td></tr>

		<s:if test="%{isBillSourcemisc()}"> 
		<tr><td>
		<%@ include file="miscReceipts-new.jsp" %>
		</td></tr>
		</s:if>


	  	<tr><td>
	  	  <div class="subheadsmallnew"><s:text name="billreceipt.paymentdetails"/></div>
	  	</td></tr>
	  	<tr><td>
    	
    		<div class="billhead2">
	    		<s:if test="%{!isBillSourcemisc()}">
	    			<s:text name="billreceipt.payment.totalamt.tobereceived"/>
	   				<span class="bold">
	    			
	   				<input style="border:0px;background-color:#FFFFCC;font-weight:bold;" type="text" name="totalamounttobepaid" id="totalamounttobepaid" readonly="readonly" value='<s:property value="%{totalAmntToBeCollected}" />' >
	   				</span>
	   			</s:if>
	   			<s:text name="billreceipt.payment.totalamt.received"/><span><input style="border:0px;background-color:#FFFFCC;font-weight:bold;" type="text" name="totalamountdisplay" id="totalamountdisplay" readonly="readonly" tabindex='-1'></span>
   			</div>
   			<s:hidden label="totalAmntToBeCollected" name="totalAmntToBeCollected" value="%{totalAmntToBeCollected}"/>
    	</td></tr>
  	
	  	<tr><td>
			    	<table width="100%" border="0" cellspacing="0" cellpadding="0"><tr><td></td></tr>
			  		</table>
	  	</td></tr>
	</table>
	<!--  Table to hold all modes of payment -->
	<table width="100%" border="0" cellpadding="0" cellspacing="0" >
			<%@include file="receipt-instrumentdetails.jsp" %>
		<!-- Paid by details -->
		<s:if test="%{!isBillSourcemisc()}">
		<tr >
		   <td class="bluebox" width="3%" ></td>
		   <td class="bluebox" width="21%"><s:text name="billreceipt.counter.paidby"/><span class="mandatory1">*</span></td>
		   <td class="bluebox"><s:textfield label="paidBy" id="paidBy" maxlength="150" name="paidBy" value="%{payeeName}" /></td>
	    </tr>
	    </s:if>
		<table id="manualreceipt" style="display:none">
		<s:if test="%{!isBillSourcemisc()}">
					<tr>
					<td class="bluebox" width="3%" ></td>
					<td class="bluebox"><s:text name="billreceipt.manualreceiptinfo"/><span id="asteriskId"  class="mandatory1">*</span></td>
					 <td class="bluebox"><s:checkbox label="manualreceiptinfo" id="manualreceiptinfo" name="receiptInfo" onChange="showHideMandataryMark(this)"/></td>
					</tr>
		 </s:if>
		 
		 <s:if test="%{!isBillSourcemisc()}">
				<tr>
				    <td class="bluebox" width="3%" ></td>
					<td class="bluebox"><s:text name="billreceipt.manualreceipt.receiptnumber"/></td>
					<td class="bluebox"><s:textfield label="manualReceiptNumber" id="manualReceiptNumber" maxlength="50" name="manualReceiptNumber" size="18" /></td>
					<td class="bluebox"><s:text name="billreceipt.manualreceipt.receiptdate"/></td>
					<td class="bluebox"><s:textfield id="manualReceiptDate" name="manualReceiptDate" cssClass="datepicker"  styleId="manualReceiptDate" onblur="validateManualReceiptDate(this);" data-inputmask="'mask': 'd/m/y'"/><div>(DD/MM/YYYY)</div></td>
				</tr>
		 </s:if>
		</table>
		</table>
			

			<table border="0" width="100%" border="0" cellpadding="0" cellspacing="0" align="center" style="padding:0px;margin:0px;">
				<tr>
			        	<td class="bluebox2new" width="21%"></td>
			            	<td class="bluebox2"></td>
					<td class="bluebox2">&nbsp;</td>
					<td class="bluebox2">&nbsp;</td>	
			     	</tr>				
			</table>
			 
			 <div id="loadingMask" style="display:none;overflow:hidden;text-align: center"><img src="/collection/resources/images/bar_loader.gif"/> <span style="color: red">Please wait....</span></div>
			<div align="left" class="mandatorycoll"><s:text name="common.mandatoryfields"/></div>
			<div class="buttonbottom" align="center">
			      <label><input align="center" type="submit" class="buttonsubmit" id="button2" value="Pay" onclick="return validate();"/></label>
			      &nbsp;
			      <input name="button" type="button" class="button" id="button" value="Reset" onclick="checkreset();"/>
			      &nbsp;
			      <input name="button" type="button" class="button" id="buttonclose2" value="Close" onclick="window.close();" />
				</div>

<table width="100%" >

     <tr> 
       <td colspan="5">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td colspan="5"><div class="subheadsmallnew"><s:text name="billreceipt.counterdetails"/></div></td>
         </tr>
         <tr style="display:none">
           <td class="bluebox">&nbsp;</td>
           <td class="bluebox"><s:text name="billreceipt.counter.point"/></td>
           <td class="bluebox"><input name="dt" type="text" disabled="disabled" /></td>
           <td class="bluebox"><s:text name="billreceipt.counter.collector"/></td>
           <td class="bluebox"><input name="dt2" type="text" disabled="disabled" /></td>
         </tr>
         <tr>
           <td class="bluebox">&nbsp;</td>
           <td class="bluebox"><s:text name="billreceipt.counter.operator"/>&nbsp;&nbsp;&nbsp; <b><s:property value="%{receiptCreatedByCounterOperator.name}"/> </b></td>
           <td class="bluebox"><s:text name="billreceipt.service"/>&nbsp;&nbsp;&nbsp; <b><s:property value="%{serviceName}"/></b></td>
          </tr>

          <tr>
            <td colspan="5"></td>
          </tr>
    	</table>
       </td>
     </tr>
     <tr>
       <td colspan="5"></td>
     </tr>
</table>



</s:push>

</s:form>

</div>
</s:else>
<script type="text/javascript">
// MAIN FUNCTION: new switchcontent("class name", "[optional_element_type_to_scan_for]") REQUIRED
// Call Instance.init() at the very end. REQUIRED
var bobexample=new switchcontent("switchgroup1", "div") //Limit scanning of switch contents to just "div" elements
bobexample.collapsePrevious(true) //Only one content open at any given time
bobexample.init()
</script>
<script src="<cdn:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>
<script>
jQuery(":input").inputmask();
</script>
</body>
