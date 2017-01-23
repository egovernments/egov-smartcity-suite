<script>

function callpopulateapportioningamountforbills(){
	<s:if test="%{!isBillSourcemisc()}">  
		populateapportioningamount();
	</s:if>
	
}

apportionLoadFailureHandler= function(){
	   // document.getElementById("errorstyle").style.display='';
		//document.getElementById("errorstyle").innerHTML='Error Loading Apportioned Amount';
	}
	function populateapportioningamount()
	{
	    // total of actual amt to be credited - can be removed
		var billingtotal=document.forms[0].totalAmntToBeCollected.value;
		var checkpartpaymentvalue=document.getElementById("partPaymentAllowed").value;
		var checkoverridevalue=document.getElementById("overrideAccountHeads").value;
		var collectiontotal=0,cashamount=0,chequeamount=0,cardamount=0,bankamount=0;

		var noofaccounts=document.getElementById("totalNoOfAccounts").value;
		var credittotal=0;
		collectiontotal=calculateCollectionTotal();
		document.getElementById("totalamountdisplay").value=isNaN(collectiontotal)?collectiontotal:collectiontotal.toFixed(2);
		
		for(var j=0;j<noofaccounts; j++)
		{
			var advanceRebatePresent=document.getElementById('receiptDetailList['+j+'].isActualDemand').value;
			if(advanceRebatePresent==0){
				zeroAccHeads=true;
				break;
			}
		}

		if(document.getElementById("callbackForApportioning").value=="true")
		{
			document.getElementById("amountoverrideerror").style.display="none";
			if(collectiontotal > billingtotal &&  zeroAccHeads==false)
			{
				document.getElementById("amountoverrideerror").style.display="block";
				return false;
			}
			else
			{
				//makeJSONCall(["OrderNumber","CreditAmount","DebitAmount","CrAmountToBePaid"],'${pageContext.request.contextPath}/receipts/receipt!apportionBillAmount.action',{instrumenttotal:collectiontotal},apportionLoadHandler,apportionLoadFailureHandler);
			}
		}
		else if(document.getElementById("callbackForApportioning").value=="false")
		{
				if(initialSetting=="true"){
					initialiseCreditAmount();
				}
				
				credittotal=calculateCreditTotal();
				
				// logic for advance payt START
				
				// if amt paid by citizen(collectiontotal) is more than the billed amt(billingtottal), 
				// and if overrideaccountheads is permitted, system should not apportion for the "zero" account heads. 
				// The citizen has to manually apportion the amount among the account heads
				var zeroAccHeads=false;
				for(var j=0;j<noofaccounts; j++)
				{
						var advanceRebatePresent=document.getElementById('receiptDetailList['+j+'].isActualDemand').value;
						if(advanceRebatePresent==0){
							zeroAccHeads=true;
						}
				}
				if(collectiontotal>billingtotal && checkoverridevalue=="true" && zeroAccHeads==true){
					// MAIN FUNCTION: new switchcontent("class name", "[optional_element_type_to_scan_for]") REQUIRED
					// Call Instance.init() at the very end. REQUIRED
					var bobexample=new switchcontent("switchgroup1", "div") //Limit scanning of switch contents to just "div" elements
					bobexample.collapsePrevious(true) //Only one content open at any given time
					bobexample.init()
					bobexample.sweepToggle('expand')
				}
				
				// logic for advance payt END
				
				//bill apportioning - the collectiontotal is apportioned among the account heads
				if(checkpartpaymentvalue=="true")
				{
					// if overriding accounts is permitted and collected total is same as the 
					// credit total, do not apportion
					if(checkoverridevalue=="true" && collectiontotal==credittotal){
				    		return;
					}
					else{
						var remainingamount=0;
						var check=0;
						for(var j=0;j<noofaccounts; j++)
						{
							var amounttobecollected=document.getElementById('receiptDetailList['+j+'].cramountToBePaid').value;
							amounttobecollected=Math.round(amounttobecollected*100)/100;
							if(check==0)
							{
								if(collectiontotal>amounttobecollected)
								{
									if(amounttobecollected==0 && checkoverridevalue=="true")
									{
										//This is for advance pyt for account which has 0 as credit amount.
										// document.getElementById('receiptDetailList['+j+'].cramount').value=collectiontotal;
										// DO NOTHING NOW FOR ADVANCE PAYT - I.E., DO NOT APPROTION
										// LATER SHUD APPROTION TREATING THE 'ZERO' ACCOUNT HEADS WITH LEAST PRIORITY 
										continue;
									}
									else
									{
										document.getElementById('receiptDetailList['+j+'].cramount').value=amounttobecollected;
										remainingamount=collectiontotal-amounttobecollected;
										remainingamount=Math.round(remainingamount*100)/100;
									}
								}
								else if(collectiontotal<=amounttobecollected)
								{
									document.getElementById('receiptDetailList['+j+'].cramount').value=collectiontotal;
								}
							}
							else if(check!=0 && remainingamount>0)
							{
								if(remainingamount>amounttobecollected)
								{
									if(amounttobecollected==0){//This is for advance pyt for account which has 0 as credit amount.
									// document.getElementById('receiptDetailList['+j+'].cramount').value=remainingamount;
									// DO NOTHING NOW FOR ADVANCE PAYT - I.E., DO NOT APPROTION
									// LATER SHUD APPROTION TREATING THE 'ZERO' ACCOUNT HEADS WITH LEAST PRIORITY
									continue;
								}
								else
								{
									document.getElementById('receiptDetailList['+j+'].cramount').value=amounttobecollected;
									remainingamount=remainingamount-amounttobecollected;
									remainingamount=Math.round(remainingamount*100)/100;
								}
							}
							else if(remainingamount<=amounttobecollected)
							{
								document.getElementById('receiptDetailList['+j+'].cramount').value=remainingamount;
								remainingamount=remainingamount-document.getElementById('receiptDetailList['+j+'].cramount').value;
							}
						}
						else if(check!=0 && remainingamount==0)
						{
							document.getElementById('receiptDetailList['+j+'].cramount').value=0;
						}
						check++;
						}//end of for
						document.getElementById("totalamountdisplay").value=isNaN(collectiontotal)?collectiontotal:collectiontotal.toFixed(2);
					}//end of if collectiontotal < billingtotal
				}//end of if checkpartpaymentvalue=="true"
			}	
	}//end of function populateapportioningamount



	apportionLoadHandler = function(req,res){
	  results=res.results;
	  var noofaccounts=document.getElementById("totalNoOfAccounts").value;
	  for(var j=0;j<noofaccounts; j++)
	  {
	    for(var k=0;k<results.length;k++)
	    {
		    if(document.getElementById('receiptDetailList['+j+'].ordernumber').value==results[k].OrderNumber)
		    {
			document.getElementById('receiptDetailList['+j+'].cramount').value=results[k].CreditAmount;
			document.getElementById('receiptDetailList['+j+'].cramountToBePaid').value=results[k].CrAmountToBePaid;
			document.getElementById('receiptDetailList['+j+'].dramount').value=results[k].DebitAmount;
		    }	
	    } 
	  }
	}


function showInstrumentDetails(obj){
	if(obj.id=='cashradiobutton'){
		document.getElementById('cashdetails').style.display='table-row';
		document.getElementById('chequeDDdetails').style.display='none';
		document.getElementById('carddetails').style.display='none';
		document.getElementById('bankdetails').style.display='none';
		document.getElementById('instrumentTypeCashOrCard').value="cash";
		document.getElementById('onlinedetails').style.display='none';
 		document.getElementById('instrHeaderCash.instrumentAmount').value=document.getElementById('totalamountdisplay').value;
 		clearCardDetails();
		clearChequeDDDetails();
		clearBankDetails();
	}
	else  if(obj.id=='chequeradiobutton'){
		document.getElementById('cashdetails').style.display='none';
		document.getElementById('chequeDDdetails').style.display='table-row';
		document.getElementById('carddetails').style.display='none';
		document.getElementById('bankdetails').style.display='none';
		document.getElementById('onlinedetails').style.display='none';
		document.getElementById('instrumentTypeCashOrCard').value="";
		clearCashDetails();
		clearCardDetails();
		clearBankDetails();
	}
	else  if(obj.id=='ddradiobutton'){
		document.getElementById('cashdetails').style.display='none';
		document.getElementById('chequeDDdetails').style.display='table-row';
		document.getElementById('carddetails').style.display='none';
		document.getElementById('bankdetails').style.display='none';
		document.getElementById('onlinedetails').style.display='none';
		document.getElementById('instrumentTypeCashOrCard').value="";
		clearCashDetails();
		clearCardDetails();
		clearBankDetails();
	}
	else if(obj.id=='cardradiobutton'){
		document.getElementById('cashdetails').style.display='none';
		document.getElementById('chequeDDdetails').style.display='none';
		document.getElementById('carddetails').style.display='table-row';
		document.getElementById('bankdetails').style.display='none';
		document.getElementById('onlinedetails').style.display='none';
		document.getElementById('instrumentTypeCashOrCard').value="card";
		clearCashDetails();
		clearBankDetails();
		clearChequeDDDetails();
	}
	else if(obj.id=='bankradiobutton'){
		document.getElementById('cashdetails').style.display='none';
		document.getElementById('chequeDDdetails').style.display='none';
		document.getElementById('carddetails').style.display='none';
		document.getElementById('bankdetails').style.display='table-row';
		document.getElementById('onlinedetails').style.display='none';
		document.getElementById('instrumentTypeCashOrCard').value="bankchallan";
		clearCashDetails();
		clearCardDetails();
		clearChequeDDDetails();
		<s:if test="%{isBillSourcemisc()}">
			if(document.getElementById("fundId")!=null && document.getElementById("fundId").value!="-1"){
				getBankBranchList(document.getElementById('fundId'));
			}
		</s:if>
	}
	else if(obj.id=='onlineradiobutton'){
		document.getElementById('onlinedetails').style.display='table-row';
		document.getElementById('cashdetails').style.display='none';
		document.getElementById('chequeDDdetails').style.display='none';
		document.getElementById('carddetails').style.display='none';
		document.getElementById('bankdetails').style.display='none';
		document.getElementById('instrumentTypeCashOrCard').value="cash";
 		document.getElementById('instrHeaderCash.instrumentAmount').value=document.getElementById('totalamountdisplay').value;
 		clearCardDetails();
		clearChequeDDDetails();
		clearBankDetails();
	}
}
function validateTransactionNumber()
{
	document.getElementById("receipt_error_area").innerHTML="";    
	document.getElementById("receipt_error_area").style.display="none";
	 if(document.getElementById("cardradiobutton").checked)
		 {    
		 	 var instrumentNum="";
		 	 var confirmInstrumentNo="";
			 if(document.getElementById("instrHeaderCard.transactionNumber")!=null)
			 	 instrumentNum = document.getElementById("instrHeaderCard.transactionNumber").value;
			 if(document.getElementById("confirmtransactionNumber")!=null)
			 	 confirmInstrumentNo =  document.getElementById("confirmtransactionNumber").value;
			 if(confirmInstrumentNo!=null && confirmInstrumentNo !="" && confirmInstrumentNo !=null && confirmInstrumentNo !=""){
					 if (instrumentNum !=confirmInstrumentNo)	
			        {		
				         document.getElementById("confirmtransactionNumber").value="";
				         document.getElementById("receipt_error_area").style.display="block";
				         document.getElementById("receipt_error_area").innerHTML+=
				 			'<s:text name="billreceipt.missingcard.confirmtransactionno.validationmessage" />'+ '<br>';
				 		 return(false);
					}
			}
		 }
}
</script>
<tr>
	<td class="bluebox" width="3%"></td>
	<td class="bluebox" width="22%"><s:text
			name="billreceipt.payment.mode" />:<span class="mandatory1">*</span></td>
	<td class="bluebox" colspan="2"><span style="float: left;"
		id="cashradiobuttonspan"> <input
			onClick="showInstrumentDetails(this)" type="radio" align="absmiddle"
			id="cashradiobutton" name="paytradiobutton" /> Cash &nbsp; <s:hidden
				name="instrumentTypeCashOrCard" id="instrumentTypeCashOrCard"
				value="cash" />
	</span> <span style="float: left;" id="chequeradiobuttonspan"> <input
			onClick="showInstrumentDetails(this);setinstrumenttypevalue(this);"
			type="radio" value="cheque" align="absmiddle" id="chequeradiobutton"
			name="paytradiobutton" /> Cheque &nbsp;
	</span> <span style="float: left;" id="ddradiobuttonspan"> <input
			onClick="showInstrumentDetails(this);setinstrumenttypevalue(this);"
			type="radio" align="absmiddle" id="ddradiobutton"
			name="paytradiobutton" value="dd" /> DD &nbsp;
	</span> <span style="float: left;" id="cardradiobuttonspan"> <input
			onClick="showInstrumentDetails(this)" type="radio" align="absmiddle"
			id="cardradiobutton" name="paytradiobutton" /> Credit/Debit card
			&nbsp;
	</span> <span style="float: left;" id="bankradiobuttonspan"> <input
			onClick="showInstrumentDetails(this)" type="radio" align="absmiddle"
			id="bankradiobutton" name="paytradiobutton" /> Direct Bank &nbsp;
	</span> </span> <span style="float: left;" id="onlineradiobuttonspan"> <input
			onClick="showInstrumentDetails(this)" type="radio" align="absmiddle"
			id="onlineradiobutton" name="paytradiobutton" /> SBI MOPS Bank
			challan &nbsp;
	</span></td>
</tr>

<tr id="cashdetails">
	<td class="bluebox" width="3%"></td>
	<td class="bluebox" width="21%"><s:text
			name="billreceipt.payment.instrumentAmount" /><span
		class="mandatory1">*</span></td>
	<td class="bluebox" colspan="3"><s:textfield
			label="instrumentAmount" id="instrHeaderCash.instrumentAmount"
			name="instrHeaderCash.instrumentAmount" maxlength="14" size="18"
			cssClass="form-control patternvalidation text-right"
			data-pattern="number" placeholder="0"
			onblur="callpopulateapportioningamountforbills();setCashInstrumentDetails(this);"
			onkeyup="callpopulateapportioningamountforbills();setCashInstrumentDetails(this);" /></td>
</tr>
<tr>
	<td colspan="5">
		<!--  Table to hold each mode of payment -->
		<table border="0" width="100%" cellspacing="0" cellpadding="0"
			align="left">

			<!--for cheque NEW USING PROXY-->
			<tr id="chequeDDdetails" style="display: none">
				<td class="bluebox2cheque" width="11%"><s:hidden
						label="instrumentCount" id="instrumentCount"
						name="instrumentCount" />

					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						name="chequegrid" id="chequegrid">
						<!-- This row contains check boxes to choose between cheque and DD -->
						<s:if
							test="instrumentProxyList==null || instrumentProxyList.size()==0">
							<s:hidden label="instrumentType" id="instrumentType"
								name="instrumentType" />
							<!-- This row captures the cheque/DD No and the cheque/DD date -->
							<tr id="chequedetailsrow">
								<td class="bluebox" width="3%"></td>
								<td class="bluebox" width="22%"><s:text
										name="billreceipt.payment.chequeddno" /><span
									class="mandatory1">*</span></td>
								<td class="bluebox"><s:textfield label="instrumentNumber"
										id="instrumentChequeNumber" maxlength="6"
										name="instrumentProxyList[0].instrumentNumber" size="18" /></td>
								<td class="bluebox"><s:text
										name="billreceipt.payment.chequedddate" /><span
									class="mandatory1">*</span></td>
								<td class="bluebox"><input type="text" id="instrumentDate"
									name="instrumentProxyList[0].instrumentDate"
									data-date-end-date="0d" data-inputmask="'mask': 'd/m/y'" /></td>
							</tr>
							<!-- This row captures the cheque/DD Bank and Branch names -->
							<tr id="chequebankrow">
								<td class="bluebox" width="3%"></td>
								<td class="bluebox"><s:text
										name="billreceipt.payment.bankname" /><span
									class="mandatory1">*</span></td>
								<td class="bluebox"><s:textfield id="bankName" type="text"
										name="instrumentProxyList[0].bankId.name"
										onfocus='autocompletecodeBank(this,event)'
										onblur='fillAfterSplitBank(this)' /> <s:hidden id="bankID"
										name="instrumentProxyList[0].bankId.id" />
									<div id="bankcodescontainer"></div></td>
								<td class="bluebox"><s:text
										name="billreceipt.payment.branchname" /></td>
								<td class="bluebox"><s:textfield
										label="instrumentBranchName" id="instrumentBranchName"
										maxlength="50" name="instrumentProxyList[0].bankBranchName"
										size="18" /></td>
							</tr>
							<!-- This row captures the cheque/DD Amount -->
							<tr id="chequeamountrow">
								<td class="bluebox" width="3%"></td>
								<td class="bluebox"><s:text
										name="billreceipt.payment.instrumentAmount" /><span
									class="mandatory1">*</span></td>
								<td class="bluebox"><s:textfield label="instrumentAmount"
										id="instrumentChequeAmount" maxlength="14"
										name="instrumentProxyList[0].instrumentAmount" size="18"
										cssClass="form-control patternvalidation text-right"
										data-pattern="number" placeholder="0"
										onblur="callpopulateapportioningamountforbills();"
										onkeyup="callpopulateapportioningamountforbills();" /></td>
								<td class="bluebox">&nbsp;</td>
								<td class="bluebox">&nbsp;</td>
							</tr>
							<tr id="chequeaddrow">
								<td colspan="5" class="blueborderfortd4">
									<div id="addchequerow" style="display: none">
										<a href="#" id="addchequelink"
											onclick="addChequeGrid('chequegrid','chequedetailsrow','chequebankrow','chequeamountrow',this,'chequeaddrow')">
											<s:text name="billreceipt.payment.add" />
										</a> <img src="../../egi/resources/erp2/images/add.png"
											id="addchequeimg" alt="Add" width="16" height="16" border="0"
											align="absmiddle"
											onclick="addChequeGrid('chequegrid',chequedetailsrow','chequebankrow','chequeamountrow',this,'chequeaddrow')" />
									</div>
									<div id="deletechequerow" style="display: none">
										<a href="#" id="deletechequelink"
											onclick="deleteChequeObj(this,'chequegrid','delerror')">
											<s:text name="billreceipt.payment.delete" />
										</a> <img src="../../egi/resources/erp2/images/delete.png"
											alt="Delete" width="16" height="16" border="0"
											align="absmiddle"
											onclick="deleteChequeObj(this,'chequegrid','delerror')" />
									</div>
								</td>
							</tr>
						</s:if>
						<s:else>
							<s:iterator value="(instrumentProxyList.size).{#this}"
								status="instrstatus">
								<s:hidden label="instrumentType" id="instrumentType"
									name="instrumentType" />
								<td class="bluebox" colspan="3">&nbsp;</td>
								</tr>
								<!-- This row captures the cheque/DD No and the cheque/DD date -->
								<tr id="chequedetailsrow">
									<td class="bluebox2new"><s:text
											name="billreceipt.payment.chequeddno" /><span
										class="mandatory1">*</span></td>
									<td class="bluebox2" width="20%"><s:textfield
											label="instrumentNumber" id="instrumentChequeNumber"
											maxlength="6"
											name="instrumentProxyList[%{#instrstatus.index}].instrumentNumber"
											size="18" /></td>
									<td class="bluebox2" width="23%"><s:text
											name="billreceipt.payment.chequedddate" /><span
										class="mandatory1">*</span></td>
									<td class="bluebox2"><input type="text"
										id="instrumentDate"
										name="instrumentProxyList[%{#instrstatus.index}].instrumentDate"
										data-inputmask="'mask': 'd/m/y'"
										onfocus="waterMarkTextIn('instrumentDate','DD/MM/YYYY');" />
										<div>(DD/MM/YYYY)</div></td>
								</tr>
								<!-- This row captures the cheque/DD Bank and Branch names -->
								<tr id="chequebankrow">
									<td class="blueboxnew"><s:text
											name="billreceipt.payment.bankname" /><span
										class="mandatory1">*</span></td>
									<td class="bluebox"><s:textfield id="bankName" type="text"
											name="instrumentProxyList[%{#instrstatus.index}].bankId.name"
											onkeyup='autocompletecodeBank(this,event)'
											onblur='fillAfterSplitBank(this)' /> <s:hidden id="bankID"
											name="instrumentProxyList[%{#instrstatus.index}].bankId.id" />
										<div id="bankcodescontainer"></div></td>
									<td class="bluebox"><s:text
											name="billreceipt.payment.branchname" /></td>
									<td class="bluebox"><s:textfield
											label="instrumentBranchName" id="instrumentBranchName"
											maxlength="50"
											name="instrumentProxyList[%{#instrstatus.index}].bankBranchName"
											size="18" /></td>
								</tr>
								<!-- This row captures the cheque/DD Amount -->
								<tr id="chequeamountrow">
									<td class="bluebox2new"><s:text
											name="billreceipt.payment.instrumentAmount" /><span
										class="mandatory1">*</span></td>
									<td class="bluebox2"><s:textfield label="instrumentAmount"
											id="instrumentChequeAmount" maxlength="14"
											name="instrumentProxyList[%{#instrstatus.index}].instrumentAmount"
											size="18"
											cssClass="form-control patternvalidation text-right"
											data-pattern="number" placeholder="0"
											onblur="callpopulateapportioningamountforbills();"
											onkeyup="callpopulateapportioningamountforbills();" /></td>
									<td class="bluebox2">&nbsp;</td>
									<td class="bluebox2">&nbsp;</td>
								</tr>
								<tr id="chequeaddrow">
									<td colspan="5" class="blueborderfortd4">
										<div id="addchequerow" style="display: none">
											<a href="#" id="addchequelink"
												onclick="addChequeGrid('chequegrid','chequedetailsrow','chequebankrow','chequeamountrow',this,'chequeaddrow')">
												<s:text name="billreceipt.payment.add" />
											</a> <img
												src="<egov:url path='../../../../egi/resources/erp2/images/add.png' />"
												id="addchequeimg" alt="Add" width="16" height="16"
												border="0" align="absmiddle"
												onclick="addChequeGrid('chequegrid','chequedetailsrow','chequebankrow','chequeamountrow',this,'chequeaddrow')" />
										</div>
										<div id="deletechequerow" style="display: none">
											<a href="#" id="deletechequelink"
												onclick="deleteChequeObj(this,'chequegrid','delerror')">
												<s:text name="billreceipt.payment.delete" />
											</a> <img
												src="<egov:url id="deletechequeimg" path='../../egi/resources/erp2/images/delete.png' />"
												alt="Delete" width="16" height="16" border="0"
												align="absmiddle"
												onclick="deleteChequeObj(this,'chequegrid','delerror')" />
										</div>
									</td>
								</tr>
							</s:iterator>
						</s:else>
					</table> <!-- End of table 'chequegrid' --></td>
			</tr>
			<!--  End of row 'chequeDDdetails' -->
			<!--for cheque-->
			<!--for card-->
			<tr id="carddetails" style="display: none">
				<td class="bluebox2cheque" width="11%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						name="cardgrid" id="cardgrid">
						<tr id="carddetailsrow">
							<td class="bluebox" width="3%"></td>
							<td width="22%" class="bluebox"><s:text
									name="billreceipt.payment.cardno" /><span class="mandatory1">*</span></td>
							<td class="bluebox"><s:textfield
									label="instrHeaderCard.instrumentNumber"
									id="instrHeaderCard.instrumentNumber" maxlength="4"
									name="instrHeaderCard.instrumentNumber"
									value="%{instrHeaderCard.instrumentNumber}" size="18" /></td>
							<td class="bluebox"><s:text
									name="billreceipt.payment.transactionnumber" /><span
								class="mandatory1">*</span></td>
							<td class="bluebox"><s:textfield
									label="instrHeaderCard.transactionNumber"
									id="instrHeaderCard.transactionNumber" maxlength="14"
									name="instrHeaderCard.transactionNumber" size="18"
									value="%{instrHeaderCard.transactionNumber}" onblur="validateTransactionNumber();" /></td>
								<td class="bluebox"><s:text
									name="billreceipt.payment.reenter.transactionnumber" /><span
								class="mandatory1">*</span></td>									
							<td class="bluebos"> <s:password id="confirmtransactionNumber"  maxlength="14"
							                   name ="confirmtransactionNumber"  size="18" onblur="validateTransactionNumber();" /></td>		
						</tr>

						<tr id="carddetailsrow">
							<td class="bluebox" width="3%"></td>
							<td class="bluebox"><s:text
									name="billreceipt.payment.instrumentAmount" /><span
								class="mandatory1">*</span></td>
							<td class="bluebox"><s:textfield
									label="instrHeaderCard.instrumentAmount"
									id="instrHeaderCard.instrumentAmount" maxlength="14"
									name="instrHeaderCard.instrumentAmount" size="18"
									cssClass="form-control patternvalidation text-right"
									data-pattern="number" placeholder="0"
									onblur="callpopulateapportioningamountforbills();setCardInstrumentDetails(this);"
									onkeyup="setCardInstrumentDetails(this);" /></td>
						</tr>

					</table> <!-- End of table 'cardgrid' -->
				</td>
			</tr>
			<!-- End of row 'carddetails' -->
			<!-- for card-->
			<!--for bank-->
			<tr id="bankdetails" style="display: none">
				<td class="bluebox2cheque" width="11%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						name="bankgrid" id="bankgrid" style="padding: 0px; margin: 0px;">

						<!-- This row captures the bank challan No and the bank challan date -->
						<tr id="bankchallandetailsrow">
							<td class="bluebox" width="3%">&nbsp;</td>
							<td class="bluebox" width="22%"><s:text
									name="billreceipt.payment.bankchallano" /><span
								class="mandatory1">*</span></td>
							<td class="bluebox"><s:textfield label="transactionNumber"
									id="instrHeaderBank.transactionNumber" maxlength="6"
									name="instrHeaderBank.transactionNumber"
									value="%{instrHeaderBank.transactionNumber}" size="18" /></td>
							<td class="bluebox"><s:text
									name="billreceipt.payment.bankchallandate" /><span
								class="mandatory1">*</span></td>
							<s:date name="instrHeaderBank.transactionDate" var="cdFormat"
								format="dd/MM/yyyy" />
							<td class="bluebox"><s:textfield id="bankChallanDate"
									name="instrHeaderBank.transactionDate" value="%{cdFormat}"
									onfocus="javascript:vDateType='3';"
									onkeyup="DateFormat(this,this.value,event,false,'3');waterMarkTextOut('bankChallanDate','DD/MM/YYYY');"
									onblur="validateChallanDate(this);" /> <a id="calendarLink"
								href="javascript:show_calendar('forms[0].bankChallanDate');"
								onmouseover="window.status='Date Picker';return true;"
								onmouseout="window.status='';return true;"> <img
									src="/../../egi/resources/erp2/images/calendaricon.gif"
									alt="Date" width="18" height="18" border="0" align="middle" />
							</a></td>
						</tr>
						<!-- This row captures the bank and account names -->
						<tr id="bankaccountrow">
							<td class="bluebox" width="3%">&nbsp;</td>
							<td class="bluebox"><s:text
									name="billreceipt.payment.bankname" /><span class="mandatory1">*</span></td>
							<td class="bluebox"><egov:ajaxdropdown
									id="bankBranchMasterDropdown" fields="['Text','Value']"
									dropdownId='bankBranchMaster'
									url='receipts/ajaxBankRemittance-bankBranchList.action'
									selectedValue="%{bankbranch.id}" /> <s:select
									headerValue="--Select--" headerKey="0"
									list="dropdownData.bankBranchList" listKey="id"
									id="bankBranchMaster" listValue="branchname"
									label="bankBranchMaster" name="bankBranchId"
									onChange="onChangeBankAccount(this.value)"
									value="%{bankBranchId}" /> <egov:ajaxdropdown
									id="accountNumberMasterDropdown" fields="['Text','Value']"
									dropdownId='accountNumberMaster'
									url='receipts/ajaxBankRemittance-accountList.action'
									selectedValue="%{bankaccount.id}" /></td>
							<td class="bluebox"><s:text
									name="billreceipt.payment.bankaccountname" /><span
								class="mandatory"></td>
							<td class="bluebox"><s:select headerValue="--Select--"
									headerKey="0" list="dropdownData.accountNumberList"
									listKey="id" id="accountNumberMaster" listValue="accountnumber"
									label="accountNumberMaster" name="bankAccountId"
									value="%{bankAccountId}" /></td>
						</tr>
						<tr id="bankamountrow">
							<td class="bluebox" width="3%">&nbsp;</td>
							<td class="bluebox" width="22%"><s:text
									name="billreceipt.payment.instrumentAmount" /><span
								class="mandatory1">*</span></td>
							<td class="bluebox"><s:textfield label="instrumentAmount"
									id="instrHeaderBank.instrumentAmount"
									name="instrHeaderBank.instrumentAmount" maxlength="14"
									size="18" cssClass="form-control patternvalidation text-right"
									data-pattern="number" placeholder="0"
									onblur="callpopulateapportioningamountforbills();setBankInstrumentDetails(this);"
									onkeyup="callpopulateapportioningamountforbills();setBankInstrumentDetails(this);" /></td>
						</tr>
					</table> <!-- End of bank grid table -->
				</td>
			</tr>
			<tr id="onlinedetails" style="display: none">
				<td class="bluebox2cheque" width="11%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						name="onlinegrid" id="onlinegrid"
						style="padding: 0px; margin: 0px;">
						<tr id="onlinedetailsrow">
							<td class="bluebox" width="3%"></td>
							<td class="bluebox" width="22%"><s:text
									name="billreceipt.payment.instrumentAmount" /><span
								class="mandatory1">*</span></td>
							<td class="bluebox" ><s:textfield
									label="instrumentAmount"
									id="instrHeaderOnline.instrumentAmount"
									name="instrHeaderOnline.instrumentAmount" maxlength="14"
									size="18" cssClass="form-control patternvalidation text-right"
									data-pattern="number" placeholder="0"
									onblur="callpopulateapportioningamountforbills();setOnlineInstrumentDetails(this);"
									onkeyup="callpopulateapportioningamountforbills();setOnlineInstrumentDetails(this);" /></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</td>
</tr>
<!-- End of mode of payments table -->
