<%@ include file="/includes/taglibs.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Online Payment</title>
<script src="common/js/watermark.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/collectionspublic.css" />
<script type="text/javascript">
var billscount=0;
var accountscount=0;
var initialSetting="true";
var multiplePayee="false";
function populateapportioningamountnew(){
	//document.getElementById("button2").disabled = true;
	document.getElementById("receipt_error_area").innerHTML='';
	dom.get("receipt_error_area").style.display="none";
	// total of actual amt to be credited - can be removed
	var billingtotal=dom.get("totalAmountToBeCollected").value;
	var checkpartpaymentvalue=dom.get("partPaymentAllowed").value;
	var checkoverridevalue=dom.get("overrideAccountHeads").value;
	var collectiontotal=0,cashamount=0,chequeamount=0,cardamount=0;

	var noofaccounts=dom.get("totalNoOfAccounts").value;
	var credittotal=0;
	collectiontotal=dom.get("paymentAmount").value;
	var zeroAccHeads=false;
	if(isNaN(collectiontotal)){
		document.getElementById("receipt_error_area").innerHTML+='<s:text name="onlineReceipts.invalidamount" />' + '<br>';
		dom.get("receipt_error_area").style.display="block";
		return;
	}
	collectiontotal=eval(collectiontotal);
    	billingtotal=eval(billingtotal);

	for(var j=0;j<noofaccounts; j++)
	{
		var advanceRebatePresent=document.getElementById('receiptDetailList['+j+'].isActualDemand').value;
		if(advanceRebatePresent==0){
			zeroAccHeads=true;
		}
	}
	if(dom.get("callbackForApportioning").value=="true")
	{
		if(collectiontotal > billingtotal && zeroAccHeads==false)
		{
			document.getElementById("receipt_error_area").innerHTML+='<s:text name="onlineReceipts.greatercollectioamounterror.errormessage" />' + '<br>';
			dom.get("receipt_error_area").style.display="block";
			return false;
		}
		else
		{																														    			//makeJSONCall(["OrderNumber","CreditAmount","DebitAmount","CrAmountToBePaid"],'${pageContext.request.contextPath}/citizen/onlineReceipt!apportionBillAmount.action',{onlineInstrumenttotal:collectiontotal},apportionLoadHandler,apportionLoadFailureHandler);
		}
	}
	else if(dom.get("callbackForApportioning").value=="false")
	{
		if(initialSetting=="true"){
			initialiseCreditAmount();
		}
	
		credittotal=calculateCreditTotal();
		if(checkpartpaymentvalue=="true" && multiplePayee=="false")
		{
			// if overridign accounts is permitted and collected total is same as the 
			// credit total, do not apportion
		    	if(checkoverridevalue=="true" && collectiontotal==credittotal){
		   		return;
		    	}
		    	if(collectiontotal==""){
		   		waterMarkInitialize('paymentAmount','0.0');
		    		return;
		    	}
			if(collectiontotal <= billingtotal)
			{
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
							document.getElementById('receiptDetailList['+j+'].cramount').value=parseFloat(amounttobecollected).toFixed(1);
							document.getElementById('receiptDetailList['+j+'].cramountDisplay').innerHTML=parseFloat(amounttobecollected).toFixed(1);
								
							remainingamount=collectiontotal-amounttobecollected;
							remainingamount=Math.round(remainingamount*100)/100;
							document.getElementById('receiptDetailList['+j+'].balanceAmount').value="0.0";
							document.getElementById('receiptDetailList['+j+'].balanceAmtDisplay').innerHTML="0.0";
						}
						else if(collectiontotal<=amounttobecollected)
						{
							document.getElementById('receiptDetailList['+j+'].cramount').value=parseFloat(collectiontotal).toFixed(1);
							document.getElementById('receiptDetailList['+j+'].cramountDisplay').innerHTML=parseFloat(collectiontotal).toFixed(1);
								
							var amt1=amounttobecollected-collectiontotal;
							amt1=Math.round(amt1*100)/100;
							document.getElementById('receiptDetailList['+j+'].balanceAmount').value=amt1.toFixed(1);
							document.getElementById('receiptDetailList['+j+'].balanceAmtDisplay').innerHTML=amt1.toFixed(1);
						}
					}
					else if(check!=0 && remainingamount>0)
					{
						if(remainingamount>amounttobecollected)
						{
							document.getElementById('receiptDetailList['+j+'].cramount').value=parseFloat(amounttobecollected).toFixed(1);
							document.getElementById('receiptDetailList['+j+'].cramountDisplay').innerHTML=parseFloat(amounttobecollected).toFixed(1);
						
							remainingamount=remainingamount-amounttobecollected;
							remainingamount=Math.round(remainingamount*100)/100;
							document.getElementById('receiptDetailList['+j+'].balanceAmount').value="0.0";
							document.getElementById('receiptDetailList['+j+'].balanceAmtDisplay').innerHTML="0.0"
						}
						else if(remainingamount<=amounttobecollected)
						{
							document.getElementById('receiptDetailList['+j+'].cramount').value=remainingamount.toFixed(1);
								document.getElementById('receiptDetailList['+j+'].cramountDisplay').innerHTML=remainingamount.toFixed(1);
							
						
							var amt2=amounttobecollected-remainingamount;
							amt2=Math.round(amt2*100)/100;
							remainingamount=remainingamount-document.getElementById('receiptDetailList['+j+'].cramount').value;
							document.getElementById('receiptDetailList['+j+'].balanceAmount').value=amt2.toFixed(1);
							document.getElementById('receiptDetailList['+j+'].balanceAmtDisplay').innerHTML=amt2.toFixed(1);
						}
					}
					else if(check!=0 && remainingamount==0)
					{
						document.getElementById('receiptDetailList['+j+'].cramount').value="0.0";
						document.getElementById('receiptDetailList['+j+'].cramountDisplay').innerHTML="0.0";
						
						document.getElementById('receiptDetailList['+j+'].balanceAmount').value=parseFloat(amounttobecollected).toFixed(1);
						document.getElementById('receiptDetailList['+j+'].balanceAmtDisplay').innerHTML=parseFloat(amounttobecollected).toFixed(1);
					}
					check++;
				}//end of for
				//dom.get("totalamountdisplay").value=collectiontotal;
			}
			else
			{
				document.getElementById("receipt_error_area").innerHTML+='<s:text name="onlineReceipts.greatercollectioamounterror.errormessage" />' + '<br>';
			dom.get("receipt_error_area").style.display="block";
			}
		}
			
	}
	//document.getElementById("button2").disabled = false;
}

function initialiseCreditAmount(){
	var noofaccounts=dom.get("totalNoOfAccounts").value;
	for(var j=0;j<noofaccounts; j++)
	{
		document.getElementById('receiptDetailList['+j+'].cramount').value=document.getElementById('receiptDetailList['+j+'].cramountToBePaid').value;
	}
	initialSetting="false";
}

function calculateCreditTotal(){
	var noofaccounts=dom.get("totalNoOfAccounts").value;
	var creditamount=0,credittotal=0,debitamount=0;
	//this step is done to populate credit amount
    for(var j=0;j<noofaccounts; j++)
	{
		var receivedAmount=eval(document.getElementById('receiptDetailList['+j+'].cramount').value);
		var rebateamount=eval(document.getElementById('receiptDetailList['+j+'].dramount').value);
		creditamount = isNaN(receivedAmount)?0:receivedAmount;
		debitamount = isNaN(rebateamount)?0:rebateamount;
		credittotal=credittotal+creditamount;
		credittotal=credittotal-debitamount;
	}
	return credittotal;
}

function validateOnlineReceipt(){
	populateapportioningamountnew();
	var amount=dom.get("paymentAmount").value;
	var billingtotal=dom.get("totalAmountToBeCollected").value;
	document.getElementById("receipt_error_area").innerHTML='';
	dom.get("receipt_error_area").style.display="none";
	var checkpartpaymentvalue=dom.get("partPaymentAllowed").value;
	var validation=true;
	var zeroAccHeads=false;
	var noofaccounts=dom.get("totalNoOfAccounts").value;
	for(var j=0;j<noofaccounts; j++)
	{
		var advanceRebatePresent=document.getElementById('receiptDetailList['+j+'].isActualDemand').value;
		if(advanceRebatePresent==0){
			zeroAccHeads=true;
		}
	}
	if(amount==null || amount=="" || isNaN(amount) || amount=="0.0" || amount=="0" || amount<0 || amount.startsWith('+')){
		document.getElementById("receipt_error_area").innerHTML+='<s:text name="onlineReceipts.invalidamount" />' + '<br>';
		dom.get("receipt_error_area").style.display="block";
		validation=false;
		return false;
	}
	amount=eval(amount);
	billingtotal=eval(billingtotal);

	if(checkpartpaymentvalue=="false" && amount < billingtotal){
		document.getElementById("receipt_error_area").innerHTML+='<s:text name="onlineReceipts.partpaymenterror" />' + '<br>';
		dom.get("receipt_error_area").style.display="block";
		validation=false;
		return false;
	}

	if(amount > billingtotal && zeroAccHeads==false)
	{
		document.getElementById("receipt_error_area").innerHTML+='<s:text name="onlineReceipts.greatercollectioamounterror.errormessage" />' + '<br>';
		dom.get("receipt_error_area").style.display="block";
		validation=false;
	}
	if(dom.get("paymentServiceId").value=="-1"){
		document.getElementById("receipt_error_area").innerHTML+='<s:text name="onlineReceipts.selectpaymentgateway.errormessage" />' + '<br>';
		dom.get("receipt_error_area").style.display="block";
		validation=false;
		return false;
	}
	
	if(!dom.get("checkbox").checked){
		document.getElementById("receipt_error_area").innerHTML+='<s:text name="onlineReceipts.acceptterms" />' + '<br>';
		dom.get("receipt_error_area").style.display="block";
		validation=false;
	}
	if(validation==false){
		return false;
	}
	else {
		var r=confirm("Do you really want to proceed ?")
		if (r==true)
  		{
			document.collDetails.action="onlineReceipt!saveNew.action";
			document.collDetails.submit();
  		}
		else
  		{
  			return false;
  		}
	}
}

function openTerms(serviceCode){
	newwindow=window.open ("../termsandconditons/"+serviceCode+"_terms_conditions.html","Terms and Conditions",'height=300,width=1000'); 
}

function initiateRequest() 
{
	if (window.XMLHttpRequest) {
		return new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		isIE = true;
		return new ActiveXObject("Microsoft.XMLHTTP");
	}
}

function trimResponse(responseTxt) 
{
	var responseTxt =responseTxt.replace(/^\s+|\s+$/g,"");	
	return responseTxt;
}

var serviceMsg;

function displayTransactionDetails(){
	document.getElementById('transactiondiv').style.display = 'none';
	var paymentServiceId=dom.get("paymentServiceId").value;
	if(paymentServiceId>0)
	{
		callAjax(paymentServiceId);
	}
}

function callAjax(paymentServiceId){

	var url = "../termsandconditons/getServiceByID.jsp?serviceid=" + paymentServiceId ;
	var req = initiateRequest();
	
	req.onreadystatechange = function()
	{
		if (req.readyState == 4)
	    	{
	        	strResponse = trimResponse(req.responseText);
		 	if (req.status == 200)
			{
				serviceMsg=trimResponse(req.responseText);
			 	dom.get("transactiondiv").innerHTML=serviceMsg;
			 	document.getElementById('transactiondiv').style.display = 'block';
		     	}//close If  
			else
		     	{
		        	alert("<s:text name='onlineReceipts.transactionmessage.errormessage'/>");
		        	document.getElementById('transactiondiv').style.display = 'none';
		     	}
	     	}//close If
	}//close req.onreadystatechange
	req.open("GET", url, true);
	req.send(null);
	
}

function onLoad(){
	 //document.getElementById("button2").disabled = true;
}
 </script>
<style type="text/css">
<!--
#apDiv1 {
	position:absolute;
	width:36px;
	height:27px;
	z-index:1;
	left: 424px;
	top: 760px;
}
-->
</style>
</head>

<body onload="onLoad();">
<div class="maincontainer">
<s:form theme="simple" name="collDetails" action="onlineReceipt">
 <div class="errorstyle" id="receipt_error_area" style="display:none;"></div>
	<div class="margin20"><div class="rbroundbox3">
	
		<div class="rbcontent4"><div class="containerformsg1">
		  
		  <div><span class="complaintmsg"><s:text name="onlineReceipts.payyourtax"/></span>
      <div class="dottedgridlarge2"></div></div>
      <table width="100%" border="0" cellspacing="0" cellpadding="0" id="billsheaderinfotable">
     <s:hidden name="collectXML" id="collectXML" value="%{collectXML}" />
<s:hidden label="totalNoOfBills" id="totalNoOfBills" name="totalNoOfBills" value="%{totalNoOfBills}"/>
<s:hidden label="totalNoOfAccounts" id="totalNoOfAccounts" value="%{totalNoOfAccounts}" name="totalNoOfAccounts"/>
<s:hidden label="multiplePayee" id="multiplePayee" name="multiplePayee" value="%{multiplePayee}"/>
<s:hidden label="reasonForCancellation" id="reasonForCancellation" name="reasonForCancellation" />
<s:hidden label="oldReceiptId" id="oldReceiptId" name="oldReceiptId"/>
<s:hidden label="overrideAccountHeads" id="overrideAccountHeads" value="%{overrideAccountHeads}" name="overrideAccountHeads"/>
<s:hidden label="partPaymentAllowed" id="partPaymentAllowed" value="%{partPaymentAllowed}" name="partPaymentAllowed"/>
<s:hidden label="cashAllowed" id="cashAllowed" value="%{cashAllowed}" name="cashAllowed"/>
<s:hidden label="cardAllowed" id="cardAllowed" value="%{cardAllowed}" name="cardAllowed"/>
<s:hidden label="chequeDDAllowed" id="chequeDDAllowed" value="%{chequeDDAllowed}" name="chequeDDAllowed"/>
<s:hidden label="totalAmountToBeCollected" id="totalAmountToBeCollected" value="%{totalAmountToBeCollected}" name="totalAmountToBeCollected"/>
<s:hidden label="callbackForApportioning" id="callbackForApportioning" value="%{callbackForApportioning}" name="callbackForApportioning"/>
<%int i=1;%>
<%int rcptDtlCnt=0; %>

<s:iterator value="%{modelPayeeList}" status="rowpayeestatus">
 <s:iterator value="%{receiptHeaders}" status="receiptheaderrowstatus"> <!--  iterate through receipt headers -->
	<tr>
	  <td>
	    <div class="switchgroup1" id="bobcontent<s:property value="#receiptheaderrowstatus.index + 1" />">
		       <table width="100%" border="0" cellpadding="0" cellspacing="0"  id="accountdetailtable<%=i%>" name="accountdetailtable<%=i%>" >
		              <tr>
		               <td class="head" width="55%" ><s:text name="onlineReceipts.accountdetails.description"/></td>
		                <td class="head" width="45%" ><div align="right"><s:text name="onlineReceipts.demand"/></div></td>
		                 <!--td class="head" width="19%" ><s:text name="onlineReceipts.collectedamount"/></td-->
		        	 <!--td class="head" width="19%" ><s:text name="onlineReceipts.balance"/></td-->
		                </tr>
		                <s:iterator value="%{receiptDetails}" status="rowreceiptdetailstatus">
			              	<tr>

				               
				                <td class="tablestyle"><s:property  value="%{description}" /></td>

				                <td class="tablestyle">
				                	<div align="right">
				                	<s:property value="%{cramountToBePaid}"/>
				                	  	<input id="receiptDetailList[<%=rcptDtlCnt%>].cramountToBePaid" name="receiptDetailList[<%=rcptDtlCnt%>].cramountToBePaid" value='<s:property value="%{cramountToBePaid}"/>' type="hidden" readonly="true" disabled="disabled" size="12"/></div></td>
							
				            	<td class="tablestyle2">
									<input type="hidden" align="right" id="receiptDetailList[<%=rcptDtlCnt%>].cramountDisplay" />
									<input name="receiptDetailList[<%=rcptDtlCnt%>].cramount" value="0.0" type="hidden" id="receiptDetailList[<%=rcptDtlCnt%>].cramount"  size="12" onblur='checkandcalculatecredittotal(<%=rcptDtlCnt%>,this);'/>
									
									<input type="hidden" name="receiptDetailList[<%=rcptDtlCnt%>].ordernumber" id="receiptDetailList[<%=rcptDtlCnt%>].ordernumber" value='<s:property value="ordernumber"/>' />
									<input type="hidden" name="receiptDetailList[<%=rcptDtlCnt%>].receiptHeader.referencenumber" id="receiptDetailList[<%=rcptDtlCnt%>].receiptHeader.referencenumber" value='<s:property value="referencenumber"/>'/>
									<input type="hidden" name="receiptDetailList[<%=rcptDtlCnt%>].dramount" id="receiptDetailList[<%=rcptDtlCnt%>].dramount" />
									<input type="hidden" name="receiptDetailList[<%=rcptDtlCnt%>].isActualDemand" id="receiptDetailList[<%=rcptDtlCnt%>].isActualDemand" value='<s:property value="isActualDemand"/>' />
								</td>
								<td class="tablestyle2"><input type="hidden" id="receiptDetailList[<%=rcptDtlCnt%>].balanceAmtDisplay" />
								<input type="hidden" name="receiptDetailList[<%=rcptDtlCnt%>].balanceAmount" id="receiptDetailList[<%=rcptDtlCnt%>].balanceAmount" value="0.0"/>
										
								
								</td>
								
			              	</tr>
			              	<%rcptDtlCnt=rcptDtlCnt+1;%>
		                </s:iterator> <!--  Finished iterating through the account heads (receipt detail) -->
		                  <tr>
		          <!--td class="tablestyle">&nbsp;</td-->
		          <!--td class="tablestyle">&nbsp;</td-->
		          <td class="tablestyle2"><span class="justbold"><s:text name="onlineReceipts.totalbalance"/></span></td>
		          <td class="tablestyle2"><span class="justbold"><s:property  value="%{totalAmountToBeCollected}" /></span></td>
	            </tr>
	             <tr>
		          <!--td class="tablestyle3">&nbsp;</td-->
		          <td colspan="1" class="tablestyle4"><span class="justbold"><s:text name="onlineReceipts.balanceamounttopay"/><span class="mandatory">*</span></span></td>
		          <td class="tablestyle4"><s:textfield label="paymentAmount" id="paymentAmount" maxlength="12" name="paymentAmount" size="12" value="0.0" cssStyle="color:DarkGray; text-align:right" onfocus = "waterMarkTextIn('paymentAmount','0.0');" onkeyup="populateapportioningamountnew()" onload="waterMarkInitialize('paymentAmount','0.0');"/></td>
	            </tr>
		            </table> <!-- End of accountdetailtable i -->
	            
	          
	          <%i=i+1;%>
	        <!-- End of table enclosing all account detail tables -->
	      </div></td>
	  </tr>
  </s:iterator><!-- end of iterating through receipt headers -->
</s:iterator> <!--  iterating through receipt payee list(model) end -->
      
      </table>
      
      
      
      </div><div class="contentright"><span class="mandatory"><s:text name="onlineReceipts.mandatoryfields"/></span></div><div class="dottedgridlarge"></div>
      <div>
			  <td width="21%" class="bluebox"><s:text name="onlineReceipts.paythrough"/><span class="mandatory">*</span></td>
			  <td width="30%" class="bluebox">&nbsp;<s:select headerKey="-1" headerValue="%{getText('onlineReceipts.select')}" name="paymentServiceId" id="paymentServiceId" cssClass="selectwk" list="dropdownData.paymentServiceList" listKey="id" listValue="serviceName" value="%{serviceName}" onchange="displayTransactionDetails()" /> </td>	
		  </div>
       <div id="transactiondiv" style="display:none">
       
		  </div>
   	  <div class="bottombuttonholder" >
   	    <input align="center" type="button"   class="buttonsubmitnew" id="button2" value="Pay Online" onclick="return validateOnlineReceipt();"/>
   	 </div></div>
	</div></div>
</div>
  </div>
</div>
</s:form>
</body>



