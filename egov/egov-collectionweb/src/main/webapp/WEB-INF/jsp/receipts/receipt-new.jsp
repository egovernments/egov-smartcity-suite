
<!-- eGov suite of products aim to improve the internal efficiency,transparency, 
    accountability and the service delivery of the government  organizations.
 
     Copyright (C) <2015>  eGovernments Foundation
 
     The updated version of eGov suite of products as by eGovernments Foundation 
     is available at http://www.egovernments.org
 
     This program is free software: you can redistribute it and/or modify
     it under the terms of the GNU General Public License as published by
     the Free Software Foundation, either version 3 of the License, or
     any later version.
 
     This program is distributed in the hope that it will be useful,
     but WITHOUT ANY WARRANTY; without even the implied warranty of
     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
     GNU General Public License for more details.
 
     You should have received a copy of the GNU General Public License
     along with this program. If not, see http://www.gnu.org/licenses/ or 
     http://www.gnu.org/licenses/gpl.html .
 
     In addition to the terms of the GPL license to be adhered to in using this
     program, the following additional terms are to be complied with:
 
 	1) All versions of this program, verbatim or modified must carry this 
 	   Legal Notice.
 
 	2) Any misrepresentation of the origin of the material is prohibited. It 
 	   is required that all modified versions of this material be marked in 
 	   reasonable ways as different from the original version.
 
 	3) This license does not grant any rights to any user of the program 
 	   with regards to rights under trademark law for use of the trade names 
 	   or trademarks of eGovernments Foundation.
 
   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
-->

<%@ include file="/includes/taglibs.jsp" %>

<head>
<!-- <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script> -->


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

function showInstrumentDetails(obj){
	if(obj.id=='cashradiobutton'){
		document.getElementById('cashdetails').style.display='table-row';
		document.getElementById('chequeDDdetails').style.display='none';
		document.getElementById('carddetails').style.display='none';
		document.getElementById('bankdetails').style.display='none';
		document.getElementById('instrumentTypeCashOrCard').value="cash";
		clearCardDetails();
		clearChequeDDDetails();
		clearBankDetails();
	}
	else  if(obj.id=='chequeradiobutton'){
		document.getElementById('cashdetails').style.display='none';
		document.getElementById('chequeDDdetails').style.display='table-row';
		document.getElementById('carddetails').style.display='none';
		document.getElementById('bankdetails').style.display='none';
		document.getElementById('instrumentTypeCashOrCard').value="";
		clearCashDetails();
		clearCardDetails();
		clearBankDetails();
		displayChequeDDInstrumentTypeDetails();
	}
	else if(obj.id=='cardradiobutton'){
		document.getElementById('cashdetails').style.display='none';
		document.getElementById('chequeDDdetails').style.display='none';
		document.getElementById('carddetails').style.display='table-row';
		document.getElementById('bankdetails').style.display='none';
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
		document.getElementById('instrumentTypeCashOrCard').value="bank";
		clearCashDetails();
		clearCardDetails();
		clearChequeDDDetails();
		<s:if test="%{isBillSourcemisc()}">
			if(document.getElementById("fundId")!=null && document.getElementById("fundId").value!="-1"){
				getBankBranchList(document.getElementById('fundId'));
			}
		</s:if>
	}
}

function clearCashDetails(){
	document.getElementById('instrHeaderCash.instrumentAmount').value="";
}

function clearChequeDDDetails(){
	var table=document.getElementById('chequegrid');
	var len=table.rows.length;
	
	for(var j=0;j<len;j++)
	{
	    //clear instrument type
	    if(getControlInBranch(table.rows[j],'instrumentType')!=null){
	    	getControlInBranch(table.rows[j],'instrumentType').value="";
	    }
	    
	    //deselect dd checkbox  
	    if(getControlInBranch(table.rows[j],'instrumenttypedd')!=null){
	    	getControlInBranch(table.rows[j],'instrumenttypedd').checked=false;
    }
	    
	    //deselect cheque checkbox  
	    if(getControlInBranch(table.rows[j],'instrumenttypecheque')!=null){
	    	getControlInBranch(table.rows[j],'instrumenttypecheque').checked=false;
	    }

	    //clear instrument number
	    if(getControlInBranch(table.rows[j],'instrumentChequeNumber')!=null){
	    	getControlInBranch(table.rows[j],'instrumentChequeNumber').value="";
	    }
	    //clear bank name 
	    if(getControlInBranch(table.rows[j],'bankID')!=null){
	    	getControlInBranch(table.rows[j],'bankID').value="-1";
	    }
	    
	   if(getControlInBranch(table.rows[j],'bankName')!=null){
	    	getControlInBranch(table.rows[j],'bankName').value="";
	    }

	    //clear date
	    if(getControlInBranch(table.rows[j],'instrumentDate')!=null){
	    	getControlInBranch(table.rows[j],'instrumentDate').value="";
	    	waterMarkInitialize('instrumentDate','DD/MM/YYYY');
	    }

	    //clear instrument amount
	    if(getControlInBranch(table.rows[j],'instrumentChequeAmount')!=null){
	    	getControlInBranch(table.rows[j],'instrumentChequeAmount').value="";
	    }
	    
	    //clear branch name 
	    if(getControlInBranch(table.rows[j],'instrumentBranchName')!=null){
	    	getControlInBranch(table.rows[j],'instrumentBranchName').value="";
	    }
	}
	
	for(var z=5;z<len;z++)
	{
		table.deleteRow(5);
	}
}

function clearCardDetails(){
	document.getElementById('instrHeaderCard.instrumentAmount').value="";
	document.getElementById('instrHeaderCard.transactionNumber').value="";
	document.getElementById('instrHeaderCard.instrumentNumber').value="";
}
function clearBankDetails(){
	document.getElementById('instrHeaderBank.instrumentAmount').value="";
	document.getElementById('instrHeaderBank.transactionNumber').value="";
	document.getElementById('bankChallanDate').value="";
	if(document.getElementById("accountNumberMaster")!=null){
		document.getElementById("accountNumberMaster").options.length = 1;
	}
}
var accountscount=0;
var initialSetting="true";

<jsp:useBean id="now" class="java.util.Date" />

<fmt:formatDate var = "currDate" pattern="dd/MM/yyyy" value="${now}" />
	var currDate = "${currDate}";
	
	
// 'chequegrid','chequetyperow','chequedetailsrow','chequebankrow','chequeamountrow',this,'chequeaddrow'
function addChequeGrid(tableId,trId1,trId2,trId3,trId4,obj,trId5)
{
	document.getElementById("receipt_error_area").innerHTML=""; 
	document.getElementById("receipt_error_area").style.display="none";
	var chequetable=document.getElementById('chequegrid');
	var chequetablelen1 =chequetable.rows.length;
    if(!verifyChequeDetails(chequetable,chequetablelen1)){
    	instrAmountInvalidErrMsg='<s:text name="billreceipt.missingchequeamount.errormessage" />' + '<br>';
    	document.getElementById("receipt_error_area").innerHTML='Please Enter Mandatory Cheque/DD Details Before Adding A New Cheque/DD';  
    	document.getElementById("receipt_error_area").style.display="block";
    }
    
    else{
   		 //To add rows to the cheque grid table
		addtablerow(tableId,trId1,trId2,trId3,trId4,trId5);
		var tbl = document.getElementById(tableId);
		var rowNumber=getRow(obj).rowIndex;
		var newtablelength=tbl.rows.length;
		
		var count=document.forms[0].instrumentCount.value;
		count=eval(count)+1;
		document.forms[0].instrumentCount.value=count;
		
		document.forms[0].instrumentChequeNumber[0].value="";
		document.forms[0].instrumentDate[0].value="";
		waterMarkInitialize('instrumentDate','DD/MM/YYYY');
		document.forms[0].instrumentBranchName[0].value="";
		document.forms[0].bankName[0].value="";
		document.forms[0].bankID[0].value="-1";
		document.forms[0].instrumentChequeAmount[0].value="";

		document.forms[0].instrumentType[0].value="";

		document.forms[0].instrumentType[0].name="instrumentProxyList["+count+"].instrumentType.type";
		document.forms[0].bankID[0].name="instrumentProxyList["+count+"].bankId.id";
		document.forms[0].bankName[0].name="instrumentProxyList["+count+"].bankId.name";
		document.forms[0].instrumentChequeAmount[0].name="instrumentProxyList["+count+"].instrumentAmount";
		document.forms[0].instrumentBranchName[0].name="instrumentProxyList["+count+"].bankBranchName";
		document.forms[0].instrumentChequeNumber[0].name="instrumentProxyList["+count+"].instrumentNumber";
		document.forms[0].instrumentDate[0].name="instrumentProxyList["+count+"].instrumentDate";
		
	
		getControlInBranch(tbl.rows[rowNumber],'addchequerow').style.display="block";
		getControlInBranch(tbl.rows[newtablelength-1],'addchequerow').style.display="none";
		getControlInBranch(tbl.rows[newtablelength-1],'deletechequerow').style.display="block";


		getControlInBranch(tbl.rows[newtablelength-4],'instrumentChequeNumber').readOnly="true";
		getControlInBranch(tbl.rows[newtablelength-4],'instrumentDate').readOnly="true";
		getControlInBranch(tbl.rows[newtablelength-3],'bankName').readOnly="true";
		getControlInBranch(tbl.rows[newtablelength-3],'instrumentBranchName').readOnly="true";		
		getControlInBranch(tbl.rows[newtablelength-2],'instrumentChequeAmount').readOnly="true";
		getControlInBranch(tbl.rows[newtablelength-3],'bankID').readOnly="true";

		var chequeAllowed=document.getElementById("chequeAllowed").value;
		var ddAllowed=document.getElementById("ddAllowed").value;
		
		if(chequeAllowed=='true' && ddAllowed=='false'){
	    		// getControlInBranch(tbl.rows[newtablelength-5],'instrumentType').value="cheque";
	    		document.forms[0].instrumentType[0].value="cheque";
	    		
	    		getControlInBranch(tbl.rows[newtablelength-5],'instrumenttypecheque').checked=true;
	    		getControlInBranch(tbl.rows[newtablelength-5],'instrumenttypedd').checked=false;
	    		
	    		getControlInBranch(tbl.rows[newtablelength-5],'instrumenttypecheque').disabled=true;
	    		getControlInBranch(tbl.rows[newtablelength-5],'instrumenttypedd').disabled=true;
	    }
	    else if(chequeAllowed=='false' && ddAllowed=='true'){
	    		// getControlInBranch(tbl.rows[newtablelength-5],'instrumentType').value="dd";
	    		document.forms[0].instrumentType[0].value="dd";
	    		
	    		getControlInBranch(tbl.rows[newtablelength-5],'instrumenttypecheque').checked=false;
	    		getControlInBranch(tbl.rows[newtablelength-5],'instrumenttypedd').checked=true;
	    		
	    		getControlInBranch(tbl.rows[newtablelength-5],'instrumenttypecheque').disabled=true;
	    		
	    		getControlInBranch(tbl.rows[newtablelength-5],'instrumenttypedd').disabled=true;
	    }
	    else{
		
			if(document.forms[0].instrumenttypecheque[0].checked==true)
			{
				getControlInBranch(tbl.rows[newtablelength-5],'instrumenttypecheque').checked=true;
			}
			else if(document.forms[0].instrumenttypedd[0].checked==true)
			{
				getControlInBranch(tbl.rows[newtablelength-5],'instrumenttypedd').checked=true;
			}
			getControlInBranch(tbl.rows[0],'instrumenttypecheque').checked=false;
			getControlInBranch(tbl.rows[0],'instrumenttypedd').checked=false;
			document.forms[0].instrumentType[0].value="";
			document.forms[0].instrumentType[0].name="instrumentProxyList["+count+"].instrumentType.type";
		}
	}
}
function addtablerow(tableId,trId1,trId2,trId3,trId4,trId5){
	var tbl = document.getElementById(tableId);
		var rowObj1 = document.getElementById(trId1).cloneNode(true);
		var rowObj2 = document.getElementById(trId2).cloneNode(true);
		var rowObj3 = document.getElementById(trId3).cloneNode(true);
		var rowObj4 = document.getElementById(trId4).cloneNode(true);
		var rowObj5 = document.getElementById(trId5).cloneNode(true);
		addRow(tbl,rowObj1);
		addRow(tbl,rowObj2);
		addRow(tbl,rowObj3);
		addRow(tbl,rowObj4);
		addRow(tbl,rowObj5);
		document.getElementById("delerror").style.display="none";
}


function loadchequegrid(tableId,trId1,trId2,trId3,trId4,trId5){
	var chequetable=document.getElementById('chequegrid');
	var chequetablelen1 =chequetable.rows.length;
	var chqAmtArray=new Array();
	var chqNumberArray=new Array();
	var bankIdArray=new Array();
	var chqDateArray=new Array();
	var bankBranchArray=new Array();
	var bankNameArray=new Array();
	var instrumentTypeArray=new Array();
	for(var m=0;m<chequetablelen1;m++)
	{
		var chequeAmt=getControlInBranch(chequetable.rows[m],'instrumentChequeAmount');
		var chequeNo=getControlInBranch(chequetable.rows[m],'instrumentChequeNumber');
		var chequeDate=getControlInBranch(chequetable.rows[m],'instrumentDate');
		var bankName=getControlInBranch(chequetable.rows[m],'bankName');
		var bankId=getControlInBranch(chequetable.rows[m],'bankID');
		var bankBranch=getControlInBranch(chequetable.rows[m],'instrumentBranchName');
		var instrumentType=getControlInBranch(chequetable.rows[m],'instrumentType');
		
		if(chequeNo!=null&&chequeNo.value!=""){
			chqNumberArray=chequeNo.value.split(',');
		}
		if(chequeAmt!=null&&chequeAmt.value!=""){
			chqAmtArray=chequeAmt.value.split(',');
		}
		if(chequeDate!=null&&chequeDate.value!="DD/MM/YYYY"){
			chqDateArray=chequeDate.value.split(',');
		}
		if(bankId!=null&&bankId.value!=""){
			bankIdArray=bankId.value.split(',');
		}
		if(bankBranch!=null&&bankBranch.value!=""){
			bankBranchArray=bankBranch.value.split(',');
		}
		if(bankName!=null&&bankName.value!=""){
			bankNameArray=bankName.value.split(',');
		}
		if(instrumentType!=null&&instrumentType.value!=""){
			instrumentTypeArray=instrumentType.value.split(',');
		}
	}
				
	if(chqAmtArray.length>1){
		document.forms[0].instrumentChequeAmount.value=chqAmtArray[0];
		document.forms[0].instrumentChequeNumber.value=chqNumberArray[0].trim();
		document.forms[0].instrumentDate.value=chqDateArray[0];
		document.forms[0].instrumentBranchName.value=bankBranchArray[0];
		document.forms[0].bankName.value=bankNameArray[0];
		document.forms[0].bankID.value=bankIdArray[0];
		document.forms[0].instrumentType.value=instrumentTypeArray[0];
		
		if(instrumentTypeArray[0].trim()=="dd"){
			document.forms[0].instrumenttypedd.checked=true;
		}
		else if(instrumentTypeArray[0].trim()=="cheque"){
			document.forms[0].instrumenttypecheque.checked=true;
		}
		for(var k=1;k<chqAmtArray.length;k++){
			addtablerow(tableId,trId1,trId2,trId3,trId4,trId5);
			var tbl = document.getElementById(tableId);
			var rowNumber=4;
			var newtablelength=tbl.rows.length;
	
			document.forms[0].instrumentChequeNumber[0].value=chqNumberArray[k].trim();
			document.forms[0].instrumentDate[0].value=chqDateArray[k];
			document.forms[0].instrumentBranchName[0].value=bankBranchArray[k];
			document.forms[0].instrumentBankName[0].value=bankNameArray[k];
			document.forms[0].instrumentBankId[0].value=bankIdArray[k];
			document.forms[0].instrumentChequeAmount[0].value=chqAmtArray[k];
	
			getControlInBranch(tbl.rows[rowNumber],'addchequerow').style.display="block";
			getControlInBranch(tbl.rows[newtablelength-1],'addchequerow').style.display="none";
			getControlInBranch(tbl.rows[newtablelength-1],'deletechequerow').style.display="block";
			getControlInBranch(tbl.rows[0],'instrumenttypecheque').checked=false;
			getControlInBranch(tbl.rows[0],'instrumenttypedd').checked=false;
			if(instrumentTypeArray[k].trim()=="cheque")
			{
				getControlInBranch(tbl.rows[0],'instrumenttypecheque').checked=true;
			}
			else if(instrumentTypeArray[k].trim()=="dd")
			{
				getControlInBranch(tbl.rows[0],'instrumenttypedd').checked=true;
			}
			document.forms[0].instrumentType[0].value=instrumentTypeArray[k];
							
		}
	}
			
}

function addRow(tableObj,rowObj)
{
	var tbody=tableObj.tBodies[0];
	tbody.appendChild(rowObj);
}

function deleteChequeObj(obj,tableId)
{
	var tbl = document.getElementById(tableId);
	var rowNumber=getRow(obj).rowIndex; 
	if(tbl.rows.length==6)
	{
	   document.getElementById("delerror").style.display="block";
	}
	else
	{
		document.getElementById("delerror").style.display="none";
		tbl.deleteRow(rowNumber);
		tbl.deleteRow(rowNumber-1);
		tbl.deleteRow(rowNumber-2);
		tbl.deleteRow(rowNumber-3);
		tbl.deleteRow(rowNumber-4);
	}
	callpopulateapportioningamountforbills();
	var tbl = document.getElementById(tableId);
	var rowNumber=getRow(obj).rowIndex;
	var newtablelength=tbl.rows.length;
	var countUI=document.forms[0].instrumentCount.value;
	var count=document.forms[0].instrumentCount.value;
	for(var j=newtablelength;j>0;j=j-5){
		count=eval(count)-1;
		getControlInBranch(tbl.rows[j-5],'instrumentType').name="instrumentProxyList["+count+"].instrumentType.type";
		getControlInBranch(tbl.rows[j-4],'instrumentChequeNumber').name="instrumentProxyList["+count+"].instrumentNumber";
		getControlInBranch(tbl.rows[j-4],'instrumentDate').name="instrumentProxyList["+count+"].instrumentDate";
		getControlInBranch(tbl.rows[j-3],'instrumentBranchName').name="instrumentProxyList["+count+"].bankBranchName";
		getControlInBranch(tbl.rows[j-2],'instrumentChequeAmount').name="instrumentProxyList["+count+"].instrumentAmount";
		getControlInBranch(tbl.rows[j-3],'bankName').name="instrumentProxyList["+count+"].bankId.name";
		getControlInBranch(tbl.rows[j-3],'bankID').name="instrumentProxyList["+count+"].bankId.id";
		}
	count=eval(countUI)-1;
	document.forms[0].instrumentCount.value=count;
}

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

function callpopulateapportioningamountforbills(){
	<s:if test="%{!isBillSourcemisc()}">  
		populateapportioningamount();
	</s:if>
	
}

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

apportionLoadFailureHandler= function(){
   // document.getElementById("errorstyle").style.display='';
	//document.getElementById("errorstyle").innerHTML='Error Loading Apportioned Amount';
}
function populateapportioningamount()
{
    // total of actual amt to be credited - can be removed
	var billingtotal=document.forms[0].totalAmountToBeCollected.value;
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
		if(collectiontotal > billingtotal && zeroAccHeads==false)
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
				validation = false;
				document.getElementById("receipt_error_area").style.display="block";
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
 			billingtotal=document.forms[0].totalAmountToBeCollected.value;
		}
		else
		{	
 			billingtotal=totalCreditAmountToBePaid;
		}


		
 	</s:else>
	var instrTypeCash = document.getElementById("cashradiobutton").checked;
	var instrTypeCheque = document.getElementById("chequeradiobutton").checked;
	var instrTypeCard = document.getElementById("cardradiobutton").checked;
	var instrTypeBank = document.getElementById("bankradiobutton").checked;
	var chequetable=document.getElementById('chequegrid')
	var chequetablelen1 =chequetable.rows.length;

	//if mode of payment is cash
	if(instrTypeCash){
		if(document.getElementById("instrHeaderCash.instrumentAmount")!=null)
		{
			cashamount=document.getElementById("instrHeaderCash.instrumentAmount").value;
			if(cashamount==null || cashamount=="" || isNaN(cashamount) || cashamount<0 || cashamount.startsWith('+')){
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
	document.getElementById('instrumentTypeCashOrCard').value="bank";
	}
	//if mode of payment is cheque/DD
	if(instrTypeCheque){
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
	    //display error if actual payment amt > original billed amt, and there is no 'zero' account head.
		if(collectiontotal>billingtotal  && advancePaymentAllowed!=true)
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
	    // display error if actual payt amt < original billed amt and system has not done apportioning(part payt is false)
		if(collectiontotal < billingtotal && checkpartpaymentvalue=="false"){
			document.getElementById("receipt_error_area").innerHTML+='<s:text name="billreceipt.total.errormessage" />' + '<br>';
			validation=false;
		}
		// display error if actual payt amt < original billed amt after system has done apportioning 
		// (citizen might have manually changed credit amounts after system apportioning of account head amounts
		else if(collectiontotal < billingtotal && checkpartpaymentvalue=="true"){
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

   	if(validation==false){
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
	    if(getControlInBranch(table.rows[j],'bankName')!=null){
	    	var bankName=getControlInBranch(table.rows[j],'bankName').value;
	    	if(bankName==null || bankName==""){
	    		if(bankNameErrMsg==""){
	    		    bankNameErrMsg='<s:text name="billreceipt.missingbankid.errormessage" />' + '<br>';
	    			document.getElementById("receipt_error_area").innerHTML+=bankNameErrMsg;
	    		}
	    		check=false;
	    	}
	    }
	    //validate if valid date has been entered
	    <s:if test="%{!isBillSourcemisc()}">
	    if(getControlInBranch(table.rows[j],'instrumentDate')!=null){
	    	var instrDate=getControlInBranch(table.rows[j],'instrumentDate');
	    	if(instrDate.value==null || instrDate.value=="" || instrDate.value=="DD/MM/YYYY"){
	    		if(instrDateErrMsg==""){
	    		    instrDateErrMsg='<s:text name="billreceipt.missingchequedate.errormessage" />' + '<br>';
	    			document.getElementById("receipt_error_area").innerHTML+=instrDateErrMsg;
	    		}
	    		check=false;
	    		
	    	} 
	    	else if (document.getElementById('manualreceiptinfo').checked==true){
		    	 if(document.getElementById("manualReceiptDate").value !=null && document.getElementById("manualReceiptDate").value != '' ){
	    		if(getControlInBranch(table.rows[j],'instrumentDate') != null && getControlInBranch(table.rows[j],'instrumentDate')!= '' && check==true) {
    			checkForCurrentDate(instrDate);
	    		}
	    	}
	    	} 
	    	checkForCurrentDate(instrDate);
	    }
	    </s:if>

	    <s:else>
	    if(getControlInBranch(table.rows[j],'instrumentDate')!=null){
	    	var instrDate=getControlInBranch(table.rows[j],'instrumentDate');
	    	if(instrDate.value==null || instrDate.value=="" || instrDate.value=="DD/MM/YYYY"){
	    		if(instrDateErrMsg==""){
	    		    instrDateErrMsg='<s:text name="billreceipt.missingchequedate.errormessage" />' + '<br>';
	    			document.getElementById("receipt_error_area").innerHTML+=instrDateErrMsg;
	    		}
	    		check=false;
	    	 } else {
	    		     checkForCurrentDate(instrDate);
	    		   } 	               
	    }
	    </s:else>

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

function clearPaytModes(){
	//deselect all payt mode radio buttons
	document.getElementById('cashradiobuttonspan').style.display="none";
	document.getElementById('cashdetails').style.display="table-row";
	
	document.getElementById('cardradiobuttonspan').style.display="none";
	document.getElementById('carddetails').style.display="none";
	
	document.getElementById('chequeradiobuttonspan').style.display="none";
	document.getElementById('chequeDDdetails').style.display="none";
	
	document.getElementById('bankradiobuttonspan').style.display="none";
	document.getElementById('bankdetails').style.display="none";
}

function isChequeDDAllowed(){
	 var chequeAllowed=document.getElementById("chequeAllowed").value;
     var ddAllowed=document.getElementById("ddAllowed").value;
     if(chequeAllowed=='true'||ddAllowed=='true'){
       	return "true";
     }
     
    return "false";
}

//if either of cheque or dd modes are restricted, both instrument type
//check boxes should be disabled, the permitted mode should be default 
//selected and the corresponding instrument type value should be assigned 
function displayChequeDDInstrumentTypeDetails(){
	var table=document.getElementById('chequegrid');
	var len=table.rows.length;
	
	 var chequeAllowed=document.getElementById("chequeAllowed").value;
     var ddAllowed=document.getElementById("ddAllowed").value;

	for(var j=0;j<len;j++)
	{
	    //clear instrument type
	    if(getControlInBranch(table.rows[j],'instrumentType')!=null ){
	    	if(chequeAllowed=='true' && ddAllowed=='false'){
	    		getControlInBranch(table.rows[j],'instrumentType').value="cheque";
	    		
	    		getControlInBranch(table.rows[j],'instrumenttypecheque').checked=true;
	    		getControlInBranch(table.rows[j],'instrumenttypedd').checked=false;
	    		
	    		getControlInBranch(table.rows[j],'instrumenttypecheque').disabled=true;
	    		getControlInBranch(table.rows[j],'instrumenttypedd').disabled=true;
	    	}
	    	if(chequeAllowed=='false' && ddAllowed=='true'){
	    		getControlInBranch(table.rows[j],'instrumentType').value="dd";
	    		
	    		getControlInBranch(table.rows[j],'instrumenttypecheque').checked=false;
	    		getControlInBranch(table.rows[j],'instrumenttypedd').checked=true;
	    		
	    		getControlInBranch(table.rows[j],'instrumenttypecheque').disabled=true;
	    		
	    		getControlInBranch(table.rows[j],'instrumenttypedd').disabled=true;
	    	}
	    }
    }
}

// This function is called to display the payt modes at the time of body load and 
// at the time of reset
function displayPaytModes(){
       var cashAllowed=document.getElementById("cashAllowed").value;
       var cardAllowed=document.getElementById("cardAllowed").value;
      // var chequeAllowed=document.getElementById("chequeAllowed").value;
       //var ddAllowed=document.getElementById("ddAllowed").value;
       var chequeDDAllowed=isChequeDDAllowed();
       var bankAllowed=document.getElementById("bankAllowed").value;
	   clearPaytModes();
	   
       if(cashAllowed=='true'){
       		//display cash radio button, set it as checked and display cash details
       		document.getElementById('cashradiobuttonspan').style.display="block";

       		document.getElementById('cashradiobutton').checked=true;
       		document.getElementById('cashdetails').style.display='table-row';
			document.getElementById('instrumentTypeCashOrCard').value="cash";
       }
       else{
            // do not display cash details
       		document.getElementById('cashradiobuttonspan').style.display="none";
       		document.getElementById('cashdetails').style.display='table-row';
       }
       if(cardAllowed=='true'){
            //display card radio button
       		document.getElementById('cardradiobuttonspan').style.display="block";
       		document.getElementById('instrumentTypeCashOrCard').value="card";
       }
       else{
       		//do not display card radio button
       		document.getElementById('cardradiobuttonspan').style.display="none";
       }
       if(chequeDDAllowed=='true'){
       		//display cheque DD radio button
       		document.getElementById('chequeradiobuttonspan').style.display="block";
       		displayChequeDDInstrumentTypeDetails();
       }
       else{
       		//do not display cheque/DD radio button
       		document.getElementById('chequeradiobuttonspan').style.display="none";
       }
       if(bankAllowed=='true'){
            //display bank radio button
       		document.getElementById('bankradiobuttonspan').style.display="block";
       		document.getElementById('instrumentTypeCashOrCard').value="bank";
       }
       else{
       		//do not display card radio button
       		document.getElementById('bankradiobuttonspan').style.display="none";
       }
       //if cash is not allowed and cheque is allowed, set cheque as the default payt
       if(chequeDDAllowed=='true' && cashAllowed=='false'){
       		document.getElementById('chequeradiobutton').checked=true;
       		document.getElementById('chequeDDdetails').style.display='table-row';
       		document.getElementById('cashdetails').style.display="none";
       		document.getElementById('instrumentTypeCashOrCard').value="";
       		
       		displayChequeDDInstrumentTypeDetails();
       }
       //if cash, cheque/DD are not allowed and card is allowed, set card as the default payt
       if(cardAllowed=='true' && cashAllowed=='false' && chequeDDAllowed=='false'){
       		document.getElementById('cardradiobutton').checked=true;
       		document.getElementById('carddetails').style.display='table-row';
       		document.getElementById('instrumentTypeCashOrCard').value="card";
       }
       //if cash, cheque/DD and card are not allowed and bank is allowed, set bank as the default payt
       if(bankAllowed=='true' && cashAllowed=='false' && chequeDDAllowed=='false' && cardAllowed=='false'){
       		document.getElementById('bankradiobutton').checked=true;
       		document.getElementById('bankdetails').style.display='table-row';
       		document.getElementById('instrumentTypeCashOrCard').value="bank";
       }
}

function refreshInbox() {
        var x=opener.top.opener;
        if(x==null){
            x=opener.top;
        }
        x.document.getElementById('inboxframe').contentWindow.egovInbox.from = 'Inbox';
	    x.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
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
	if(getControlInBranch(chequetable.rows[4],'addchequerow')!=null)
		getControlInBranch(chequetable.rows[4],'addchequerow').style.display="block";
	if(getControlInBranch(chequetable.rows[4],'deletechequerow')!=null)
		getControlInBranch(chequetable.rows[4],'deletechequerow').style.display="none";
	
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

function displayPaymentDetails(){
	if(document.getElementById("totalamounttobepaid")!=null && document.getElementById("totalamounttobepaid").value!=""){
	var collectionamount = parseFloat(document.getElementById("totalamounttobepaid").value);
	document.getElementById("totalamounttobepaid").value=isNaN(collectionamount)?collectionamount:collectionamount.toFixed(2);
	}
	if(document.getElementById("instrHeaderBank.instrumentAmount")!=null && document.getElementById("instrHeaderBank.instrumentAmount").value!=""){
		document.getElementById('bankradiobutton').checked=true;
		document.getElementById('bankdetails').style.display='table-row';
       	document.getElementById('instrumentTypeCashOrCard').value="bank";
       	document.getElementById('cashdetails').style.display="none";
       	// document.getElementById('carddetails').style.display="none";
	}
	if(document.getElementById("instrHeaderCard.instrumentAmount")!=null && document.getElementById("instrHeaderCard.instrumentAmount").value!=""){
		document.getElementById('cardradiobutton').checked=true;
		document.getElementById('carddetails').style.display='table-row';
       	document.getElementById('instrumentTypeCashOrCard').value="card";
       	document.getElementById('cashdetails').style.display="none";
	}

	var chequetable=document.getElementById('chequegrid');
	var chequetablelen1 =chequetable.rows.length;
	for(var m=0;m<chequetablelen1;m++)
	{
		var chequeAmt=getControlInBranch(chequetable.rows[m],'instrumentChequeAmount');
		if(chequeAmt!=null && chequeAmt.value!="")
		{
			document.getElementById('chequeradiobutton').checked=true;
			document.getElementById('chequeDDdetails').style.display='table-row';
    		document.getElementById('instrumentTypeCashOrCard').value="";
       		document.getElementById('cashdetails').style.display="none";
	    }
	}
		
	//loadchequegrid('chequegrid','chequetyperow','chequedetailsrow','chequebankrow','chequeamountrow','chequeaddrow');	 
}

function loadchequedetails(){
	var chequetable=document.getElementById('chequegrid');
	var chequetablelen1 =chequetable.rows.length;
	var tbl = document.getElementById("chequegrid");
	
	for(var j=chequetablelen1;j>5;j=j-5){
		if(chequetablelen1>5){
		getControlInBranch(tbl.rows[j-1],'deletechequerow').style.display="block";
		}
	}
	for(var j=chequetablelen1;j>=5;j=j-5){
		
		if(getControlInBranch(tbl.rows[j-5],'instrumentType').value=="cheque"){
			getControlInBranch(tbl.rows[j-5],'instrumenttypecheque').checked=true;
		}
		else if(getControlInBranch(tbl.rows[j-5],'instrumentType').value=="dd"){
			getControlInBranch(tbl.rows[j-5],'instrumenttypedd').checked=true;
		}
	}
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
	/* if(validateDateFormat(obj))
	   { */
	   document.getElementById("receipt_dateerror_area").style.display="none";
		document.getElementById("receipt_dateerror_area").innerHTML="";
	   //trim(obj,obj.value);
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
		receiptDate = "${currDate}"; 
		}
		</s:else>
	   var finDate = new Date('2012-04-01');
	   var year = receiptDate.substr(6,4);
	   var month = receiptDate.substr(3,2);
	   var day = receiptDate.substr(0,2);
	   var receiptDateFormat = new Date(year +'-' + month+ '-' +day);

	   if(obj.value != null && obj.value != '') {
	   if(!validateChequeDate(obj.value,receiptDate)){
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
						'<s:text name="billreceipt.datelessthancurrentdate.errormessage" />'+ '<br>';
			  	 }
		   </s:if>
		   <s:else>
		   document.getElementById("receipt_dateerror_area").style.display="block";
			  document.getElementById("receipt_dateerror_area").innerHTML+=
					'<s:text name="billreceipt.datelessthancurrentdate.errormessage" />'+ '<br>';
		   </s:else>
		   jQuery(obj).val('');
		   scrolltop();
	       return false;
		   }
	   /* } */
	   }
}



function setinstrumenttypevalue(obj)
{
	var currRow=getRow(obj);
	if(obj.value=="cheque")
	{
		getControlInBranch(currRow,'instrumenttypedd').checked=false;
		getControlInBranch(currRow,'instrumentType').value=obj.value;
	}
	else if(obj.value=="dd")
	{
		getControlInBranch(currRow,'instrumenttypecheque').checked=false;
		getControlInBranch(currRow,'instrumentType').value=obj.value;
	}
	if(getControlInBranch(currRow,'instrumenttypedd').checked==false && getControlInBranch(currRow,'instrumenttypecheque').checked==false)
	{
		getControlInBranch(currRow,'instrumentType').value="";
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



function setCashInstrumentDetails(elem){
     document.getElementById("instrHeaderCash.instrumentAmount").value=elem.value;
     document.getElementById("instrumentTypeCashOrCard").value="cash";
}

function setCardInstrumentDetails(elem){
     document.getElementById("instrHeaderCard.instrumentAmount").value=elem.value;
     document.getElementById("instrumentTypeCashOrCard").value="card";
}
function setBankInstrumentDetails(elem){
     document.getElementById("instrHeaderBank.instrumentAmount").value=elem.value;
     document.getElementById("instrumentTypeCashOrCard").value="bank";
}

var bankfuncObj;
var bankArray;
function loadDropDownCodesBank()
{
	var url = "<c:url value='/commons/Process.jsp?type=getAllBankName' context='/EGF'/>";
	var req2 = initiateRequest();
	req2.onreadystatechange = function()
	{
	  if (req2.readyState == 4)
	  {
		  if (req2.status == 200)
		  {
			var codes2=req2.responseText;
			var a = codes2.split("^");
			var codes = a[0];
			bankArray=codes.split("+");
			bankfuncObj= new YAHOO.widget.DS_JSArray(bankArray);
		  }
	   }
	};
	req2.open("GET", url, true);
	req2.send(null);
}

var yuiflagBank = new Array();
function autocompletecodeBank(obj,myEvent)
{
	//Fix-Me
	var branchObj = document.getElementById('instrumentBranchName');
	jQuery(branchObj).trigger('focus');
	jQuery(obj).focus();
	var src = obj;	
	var target = document.getElementById('bankcodescontainer');	
	var posSrc=findPos(src); 
	target.style.left=posSrc[0];	
	target.style.top=posSrc[1]+22;
	target.style.width=450;	
		
	var coaCodeObj=obj;
	var  currRow=getRow(obj);
	//40 --> Down arrow, 38 --> Up arrow
	if(yuiflagBank[currRow] == undefined)
	{
		var key = window.event ? window.event.keyCode : myEvent.charCode; 
		if(key != 40 )
		{
			if(key != 38 )
			{ var oAutoComp = new YAHOO.widget.AutoComplete(coaCodeObj,'bankcodescontainer', bankfuncObj);
				oAutoComp.queryDelay = 0;
				oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
				oAutoComp.useShadow = true;
				oAutoComp.maxResultsDisplayed = 15;
				oAutoComp.useIFrame = true;
				//if(bankfuncObj){
				//	bankfuncObj.applyLocalFilter = true;
				//	bankfuncObj.queryMatchContains = true;
				//}
			}
		}
		yuiflagBank[currRow] = 1;
	}	
}
function fillAfterSplitBank(obj)
{
	var currRow=getRow(obj);
	var temp = obj.value;
	temp = temp.split("`-`");
	if(temp[1])
	{
		obj.value=temp[0];
		getControlInBranch(currRow,'bankID').value=temp[1];
		getControlInBranch(currRow,'bankName').value=temp[0];
		
	}
	/* else
	{
		getControlInBranch(currRow,'bankID').value="";
		getControlInBranch(currRow,'bankName').value="";
	} */
	
}

function onChangeBankAccount(branchId)
{
    var serviceName=document.getElementById("serviceName").value;
    var fundName="",fundId=-1;
    if(document.getElementById('fundId')!=null){
    	fundId=document.getElementById('fundId').value;
    	populateaccountNumberMaster({branchId:branchId,serviceName:serviceName,fundId:fundId});
    }
    else if(document.getElementById("fundName")!=null){
    	fundName=document.getElementById("fundName").value;
    	populateaccountNumberMaster({branchId:branchId,serviceName:serviceName,fundName:fundName});
    }
    
    
	
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
<body onLoad="refreshInbox();"><br>


<div class="errorstyle" id="receipt_error_area" style="display:none;"></div>
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
<div class="formmainbox" style="width:100%;max-width:960px;">
	<s:if test="%{hasErrors()}">
	    <div id="actionErrorMessages" class="errorstyle">
	      <s:actionerror/>
	      <s:fielderror/>
	    </div>
	</s:if>
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
	    			
	   				<input style="border:0px;background-color:#FFFFCC;font-weight:bold;" type="text" name="totalamounttobepaid" id="totalamounttobepaid" readonly="readonly" value='<s:property value="%{totalAmountToBeCollected}" />' >
	   				</span>
	   			</s:if>
	   			<s:text name="billreceipt.payment.totalamt.received"/><span><input style="border:0px;background-color:#FFFFCC;font-weight:bold;" type="text" name="totalamountdisplay" id="totalamountdisplay" readonly="readonly"></span>
   			</div>
   			<s:hidden label="totalAmountToBeCollected" name="totalAmountToBeCollected" value="%{totalAmountToBeCollected}"/>
    	</td></tr>
  	
	  	<tr><td>
			    	<table width="100%" border="0" cellspacing="0" cellpadding="0"><tr><td></td></tr>
			  		</table>
	  	</td></tr>
	</table>
	<!--  Table to hold all modes of payment -->
	<table width="100%" border="0" cellpadding="0" cellspacing="0" >
	   <tr>
	   <td class="bluebox" width="3%" ></td>
	   <td class="bluebox" width="22%" ><s:text name="billreceipt.payment.mode"/>:<span class="mandatory1">*</span></td>
	   <td class="bluebox" colspan="2" >
      		
	            	<span style="float:left;" id="cashradiobuttonspan">
						<input onClick="showInstrumentDetails(this)"  type="radio" align="absmiddle" id="cashradiobutton"  name="paytradiobutton"/> Cash &nbsp; <s:hidden name="instrumentTypeCashOrCard" id="instrumentTypeCashOrCard" value="cash" />
					</span>
					<span style="float:left;"  id="chequeradiobuttonspan">
						<input onClick="showInstrumentDetails(this)"  type="radio" align="absmiddle" id="chequeradiobutton" name="paytradiobutton"/> Cheque/DD &nbsp;</span>
					<span style="float:left;" id="cardradiobuttonspan">
						<input onClick="showInstrumentDetails(this)"  type="radio" align="absmiddle" id="cardradiobutton" name="paytradiobutton"/> Credit  card	&nbsp;</span>
	             	<span style="float:left;" id="bankradiobuttonspan">
						<input onClick="showInstrumentDetails(this)"  type="radio" align="absmiddle" id="bankradiobutton" name="paytradiobutton"/> Direct Bank &nbsp;</span>
	   </td>
	   </tr>
	   
	   <tr id="cashdetails" >
		   <td class="bluebox" width="3%" ></td>
		   <td class="bluebox" width="21%"><s:text name="billreceipt.payment.instrumentAmount"/><span class="mandatory1">*</span></td>
		   <td class="bluebox" colspan="3"><s:textfield label="instrumentAmount" id="instrHeaderCash.instrumentAmount" name="instrHeaderCash.instrumentAmount" maxlength="14" size="18" cssClass="amount" placeholder="0.0" onblur="callpopulateapportioningamountforbills();setCashInstrumentDetails(this);" onkeyup="callpopulateapportioningamountforbills();setCashInstrumentDetails(this);"/></td>
	   </tr>
	   
	   
		<tr>
			<td colspan="5" >
				<!--  Table to hold each mode of payment -->
				<table border="0" width="100%" cellspacing="0" cellpadding="0" align="left">
				
				<!--for cheque NEW USING PROXY-->
			 <tr id="chequeDDdetails" style="display:none">
		       	<td class="bluebox2cheque" width="11%">
		       		<s:hidden label="instrumentCount" id="instrumentCount" name="instrumentCount"/>
		       		
			   <table width="100%" border="0" cellspacing="0" cellpadding="0" name="chequegrid" id="chequegrid">
		       		<!-- This row contains check boxes to choose between cheque and DD -->
		       		<s:if test="instrumentProxyList==null || instrumentProxyList.size()==0">
		       		<tr id="chequetyperow">
		       		    <td class="bluebox" width="3%"></td>
			   			<td class="bluebox" width="21%">
			   				<INPUT TYPE="CHECKBOX" NAME="cheque" onclick="setinstrumenttypevalue(this);" value="cheque" id="instrumenttypecheque">Cheque
			   				<INPUT TYPE="CHECKBOX" NAME="dd" onclick="setinstrumenttypevalue(this);" value="dd" id="instrumenttypedd">DD<BR>
			   				<s:hidden label="instrumentType" id="instrumentType" name="instrumentProxyList[0].instrumentType.type"/>
			   			</td>
					</tr>
					<!-- This row captures the cheque/DD No and the cheque/DD date -->
					<tr id="chequedetailsrow"> 
					    <td class="bluebox" width="3%"></td>
					    <td class="bluebox" width="22%"><s:text name="billreceipt.payment.chequeddno"/><span class="mandatory1">*</span></td>
					    <td class="bluebox"><s:textfield label="instrumentNumber" id="instrumentChequeNumber" maxlength="6" name="instrumentProxyList[0].instrumentNumber" size="18" /></td>
					    <td class="bluebox" ><s:text name="billreceipt.payment.chequedddate"/><span class="mandatory1">*</span></td>
					    <td class="bluebox"><input type ="text" id="instrumentDate" name="instrumentProxyList[0].instrumentDate"   onfocus = "waterMarkTextIn('instrumentDate','DD/MM/YYYY');" onblur="checkForCurrentDate(this);"  data-inputmask="'mask': 'd/m/y'" /></td>
				    </tr>
				    <!-- This row captures the cheque/DD Bank and Branch names -->
		     		<tr id="chequebankrow">
		     		    <td class="bluebox" width="3%"></td>
				       	<td class="bluebox"><s:text name="billreceipt.payment.bankname"/><span class="mandatory1">*</span></td>
				       	<td class="bluebox">
					   			<s:textfield id="bankName" type="text" name="instrumentProxyList[0].bankId.name"  onkeyup='autocompletecodeBank(this,event)' onblur='fillAfterSplitBank(this)' />
					   			<s:hidden id="bankID" name="instrumentProxyList[0].bankId.id" />
		   						<div id="bankcodescontainer"></div>
		       			</td>
		       			<td class="bluebox"><s:text name="billreceipt.payment.branchname"/></td>
		       			<td class="bluebox"><s:textfield label="instrumentBranchName" id="instrumentBranchName" maxlength="50" name="instrumentProxyList[0].bankBranchName" size="18" /></td>
		       		</tr>
		       		<!-- This row captures the cheque/DD Amount -->
		       		<tr id="chequeamountrow">
		       		    <td class="bluebox" width="3%"></td>
						<td class="bluebox"><s:text name="billreceipt.payment.instrumentAmount"/><span class="mandatory1">*</span></td>
						<td class="bluebox"><s:textfield label="instrumentAmount" id="instrumentChequeAmount" maxlength="14" name="instrumentProxyList[0].instrumentAmount"  size="18"  cssClass="amount" placeholder="0.0" onblur="callpopulateapportioningamountforbills();" onkeyup="callpopulateapportioningamountforbills();"/></td>
						<td class="bluebox">&nbsp;</td>
						<td class="bluebox">&nbsp;</td>
					</tr>
					<tr id="chequeaddrow">
						<td colspan="5" class="blueborderfortd4">
							<div id="addchequerow" style="display:none">
								<a href="#" id="addchequelink" onclick="addChequeGrid('chequegrid','chequetyperow','chequedetailsrow','chequebankrow','chequeamountrow',this,'chequeaddrow')">
									<s:text name="billreceipt.payment.add"/></a>
								<img src="../../egi/images/add.png" id="addchequeimg" alt="Add" width="16" height="16" border="0" align="absmiddle" onclick="addChequeGrid('chequegrid','chequetyperow','chequedetailsrow','chequebankrow','chequeamountrow',this,'chequeaddrow')"/>
							</div>
							<div id="deletechequerow" style="display:none">
								<a href="#" id="deletechequelink" onclick="deleteChequeObj(this,'chequegrid','delerror')">
									<s:text name="billreceipt.payment.delete"/></a>
								<img src="../../egi/images/delete.png" alt="Delete" width="16" height="16" border="0" align="absmiddle"  onclick="deleteChequeObj(this,'chequegrid','delerror')"/>
							</div>
						</td>
					</tr>
					</s:if>
					<s:else>
					  <s:iterator value="(instrumentProxyList.size).{#this}" status="instrstatus">
					<tr id="chequetyperow">
			   			<td class="blueboxcheckbox" width="19%">
			   				<INPUT TYPE="CHECKBOX" NAME="cheque" onclick="setinstrumenttypevalue(this);" value="cheque" id="instrumenttypecheque">Cheque
			   				<INPUT TYPE="CHECKBOX" NAME="dd" onclick="setinstrumenttypevalue(this);" value="dd" id="instrumenttypedd">DD<BR>
			   				<s:hidden label="instrumentType" id="instrumentType" name="instrumentProxyList[%{#instrstatus.index}].instrumentType.type"/>
			   			</td>
			       		<td class="bluebox" colspan="3">&nbsp;</td>
					</tr>
					<!-- This row captures the cheque/DD No and the cheque/DD date -->
					<tr id="chequedetailsrow"> 
					    <td class="bluebox2new"><s:text name="billreceipt.payment.chequeddno"/><span class="mandatory1">*</span></td>
					    <td class="bluebox2" width="20%"><s:textfield label="instrumentNumber" id="instrumentChequeNumber" maxlength="6" name="instrumentProxyList[%{#instrstatus.index}].instrumentNumber" size="18" /></td>
					    <td class="bluebox2" width="23%"><s:text name="billreceipt.payment.chequedddate"/><span class="mandatory1">*</span></td>
					   <td class="bluebox2"><input type ="text" id="instrumentDate" name="instrumentProxyList[%{#instrstatus.index}].instrumentDate" data-inputmask="'mask': 'd/m/y'"  onfocus = "waterMarkTextIn('instrumentDate','DD/MM/YYYY');"/><div>(DD/MM/YYYY)</div></td>
				    </tr>
				    <!-- This row captures the cheque/DD Bank and Branch names -->
		     		<tr id="chequebankrow">
				       	<td class="blueboxnew"><s:text name="billreceipt.payment.bankname"/><span class="mandatory1">*</span></td>
				       	<td class="bluebox">
					   			<s:textfield id="bankName" type="text" name="instrumentProxyList[%{#instrstatus.index}].bankId.name"  onkeyup='autocompletecodeBank(this,event)' onblur='fillAfterSplitBank(this)' />
					   			<s:hidden id="bankID" name="instrumentProxyList[%{#instrstatus.index}].bankId.id" />
		   						<div id="bankcodescontainer"></div>
		       			</td>
		       			<td class="bluebox"><s:text name="billreceipt.payment.branchname"/></td>
		       			<td class="bluebox"><s:textfield label="instrumentBranchName" id="instrumentBranchName" maxlength="50" name="instrumentProxyList[%{#instrstatus.index}].bankBranchName" size="18" /></td>
		       		</tr>
		       		<!-- This row captures the cheque/DD Amount -->
		       		<tr id="chequeamountrow">
						<td class="bluebox2new"><s:text name="billreceipt.payment.instrumentAmount"/><span class="mandatory1">*</span></td>
						<td class="bluebox2"><s:textfield label="instrumentAmount" id="instrumentChequeAmount" maxlength="14" name="instrumentProxyList[%{#instrstatus.index}].instrumentAmount"  size="18"  cssClass="amount" placeholder="0.0" onblur="callpopulateapportioningamountforbills();" onkeyup="callpopulateapportioningamountforbills();"/></td>
						<td class="bluebox2">&nbsp;</td>
						<td class="bluebox2">&nbsp;</td>
					</tr>
					<tr id="chequeaddrow">
						<td colspan="5" class="blueborderfortd4">
							<div id="addchequerow" style="display:none">
								<a href="#" id="addchequelink" onclick="addChequeGrid('chequegrid','chequetyperow','chequedetailsrow','chequebankrow','chequeamountrow',this,'chequeaddrow')">
									<s:text name="billreceipt.payment.add"/></a>
								<img src="<egov:url path='../../../../egi/images/add.png' />" id="addchequeimg" alt="Add" width="16" height="16" border="0" align="absmiddle" onclick="addChequeGrid('chequegrid','chequetyperow','chequedetailsrow','chequebankrow','chequeamountrow',this,'chequeaddrow')"/>
							</div>
							<div id="deletechequerow" style="display:none">
								<a href="#" id="deletechequelink" onclick="deleteChequeObj(this,'chequegrid','delerror')">
									<s:text name="billreceipt.payment.delete"/></a>
								<img src="<egov:url id="deletechequeimg" path='../../egi/images/delete.png' />" alt="Delete" width="16" height="16" border="0" align="absmiddle"  onclick="deleteChequeObj(this,'chequegrid','delerror')"/>
							</div>
						</td>
					</tr>
					  </s:iterator>
					</s:else>
		       </table>
		       <!-- End of table 'chequegrid' -->
		   	    	</td>
					</tr><!--  End of row 'chequeDDdetails' -->
				<!--for cheque-->
				<!--for card-->
					<tr id="carddetails" style="display:none">
		        	<td class="bluebox2cheque" width="11%">
			        	<table width="100%" border="0" cellspacing="0" cellpadding="0" name="cardgrid" id="cardgrid">
					  		<tr id="carddetailsrow">
					  		    <td class="bluebox" width="3%"></td>
					            <td width="22%" class="bluebox"><s:text name="billreceipt.payment.cardno"/><span class="mandatory1">*</span></td>
					            <td class="bluebox"><s:textfield label="instrHeaderCard.instrumentNumber" id="instrHeaderCard.instrumentNumber" maxlength="4" name="instrHeaderCard.instrumentNumber" value="%{instrHeaderCard.instrumentNumber}" size="18" /></td>
						        <td class="bluebox"><s:text name="billreceipt.payment.transactionnumber"/><span class="mandatory1">*</span></td>
								<td class="bluebox"><s:textfield label="instrHeaderCard.transactionNumber" id="instrHeaderCard.transactionNumber" maxlength="14" name="instrHeaderCard.transactionNumber" size="18" value="%{instrHeaderCard.transactionNumber}"/></td>
					        </tr>
					        
					        <tr id="carddetailsrow">
					  		    <td class="bluebox" width="3%"></td>
								<td class="bluebox"><s:text name="billreceipt.payment.instrumentAmount"/><span class="mandatory1">*</span></td>
					            <td class="bluebox"><s:textfield label="instrHeaderCard.instrumentAmount" id="instrHeaderCard.instrumentAmount" maxlength="14" name="instrHeaderCard.instrumentAmount" size="18"  cssClass="amount" placeholder="0.0" onblur="callpopulateapportioningamountforbills();setCardInstrumentDetails(this);" onkeyup="setCardInstrumentDetails(this);"/></td>
					        </tr>
					        
			            </table> 
			   <!-- End of table 'cardgrid' -->
		        	</td>
					</tr><!-- End of row 'carddetails' -->
				<!-- for card-->
				<!--for bank-->
					<tr id="bankdetails" style="display:none">
					<td class="bluebox2cheque" width="11%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0" name="bankgrid" id="bankgrid" style="padding:0px;margin:0px;">
				       		
							<!-- This row captures the bank challan No and the bank challan date -->
							<tr id="bankchallandetailsrow">
							    <td class="bluebox" width="3%">&nbsp;</td> 
							    <td class="bluebox" width="22%"><s:text name="billreceipt.payment.bankchallano"/><span class="mandatory1">*</span></td>
							    <td class="bluebox" ><s:textfield label="transactionNumber" id="instrHeaderBank.transactionNumber" maxlength="6" name="instrHeaderBank.transactionNumber" value="%{instrHeaderBank.transactionNumber}" size="18" /></td>
							    <td class="bluebox"><s:text name="billreceipt.payment.bankchallandate"/><span class="mandatory1">*</span></td>
							    <s:date name="instrHeaderBank.transactionDate" var="cdFormat" format="dd/MM/yyyy"/>
							    <td class="bluebox">
							    	<s:textfield id="bankChallanDate" name="instrHeaderBank.transactionDate"  value="%{cdFormat}" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3');waterMarkTextOut('bankChallanDate','DD/MM/YYYY');" onblur="validateChallanDate(this);"/>
							    	<a  id="calendarLink" href="javascript:show_calendar('forms[0].bankChallanDate');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"  >
			      						<img src="/../../egi/images/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="middle" />
			      					</a>
							    </td>
						    </tr>
						    <!-- This row captures the bank and account names -->
				     		<tr id="bankaccountrow">
				     		    <td class="bluebox" width="3%">&nbsp;</td> 
						       	<td class="bluebox"><s:text name="billreceipt.payment.bankname"/><span class="mandatory1">*</span></td>
						       	<td class="bluebox">
						       		<egov:ajaxdropdown id="bankBranchMasterDropdown" fields="['Text','Value']" dropdownId='bankBranchMaster'
				                 		url='receipts/ajaxBankRemittance-bankBranchList.action' selectedValue="%{bankbranch.id}"/>
							   		<s:select headerValue="--Select--"  headerKey="0" list="dropdownData.bankBranchList" listKey="id" 
							   		id="bankBranchMaster" listValue="branchname" label="bankBranchMaster" name="bankBranchId" 
							   		onChange="onChangeBankAccount(this.value)" value="%{bankBranchId}"/>
									<egov:ajaxdropdown id="accountNumberMasterDropdown" fields="['Text','Value']" dropdownId='accountNumberMaster'
				         				url='receipts/ajaxBankRemittance-accountList.action' selectedValue="%{bankaccount.id}"/>
				       			</td>
				       			<td class="bluebox"><s:text name="billreceipt.payment.bankaccountname"/></td>
				       			<td class="bluebox"><s:select headerValue="--Select--"  headerKey="0"
		                			list="dropdownData.accountNumberList" listKey="id" id="accountNumberMaster" listValue="accountnumber"
		                			label="accountNumberMaster" name="bankAccountId" value="%{bankAccountId}"/>
		                		</td>
				       		</tr>
				       		<tr id="bankamountrow">
				       		<td class="bluebox" width="3%">&nbsp;</td>
				       		<td class="bluebox" width="22%"><s:text name="billreceipt.payment.instrumentAmount"/><span class="mandatory1">*</span></td>
				       		<td class="bluebox"><s:textfield label="instrumentAmount" id="instrHeaderBank.instrumentAmount" name="instrHeaderBank.instrumentAmount" maxlength="14" size="18" cssClass="amount" placeholder="0.0" onblur="callpopulateapportioningamountforbills();setBankInstrumentDetails(this);" onkeyup="callpopulateapportioningamountforbills();setBankInstrumentDetails(this);"/></td>
				       		</tr>
						</table> 
				<!-- End of bank grid table -->
					</td>
					</tr>
					
		</table> <!-- End of mode of payments table -->
		</td></tr>
		
		<!-- Paid by details -->
		<tr >
		   <td class="bluebox" width="3%" ></td>
		   <td class="bluebox" width="21%"><s:text name="billreceipt.counter.paidby"/><span class="mandatory1">*</span></td>
		   <td class="bluebox"><s:textfield label="paidBy" id="paidBy" maxlength="150" name="paidBy" value="%{payeeName}" /></td>
	    </tr>
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
			 
			 <div id="loadingMask" style="display:none;overflow:hidden;text-align: center"><img src="/egi/resources/erp2/images/bar_loader.gif"/> <span style="color: red">Please wait....</span></div>
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
<script type="text/javascript">
// MAIN FUNCTION: new switchcontent("class name", "[optional_element_type_to_scan_for]") REQUIRED
// Call Instance.init() at the very end. REQUIRED
var bobexample=new switchcontent("switchgroup1", "div") //Limit scanning of switch contents to just "div" elements
bobexample.collapsePrevious(true) //Only one content open at any given time
bobexample.init()
</script>
<script src="<c:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>
<script>
jQuery(":input").inputmask();
</script>
</body>
