function loadBank(fund){
	populatefromBankId({fundId:fund.options[fund.selectedIndex].value,typeOfAccount:"RECEIPTS_PAYMENTS,RECEIPTS"})	
	populatetoBankId({fundId:fund.options[fund.selectedIndex].value,typeOfAccount:"RECEIPTS_PAYMENTS,PAYMENTS"})
	}
	
function loadFromAccNum(branch){

	var fundObj = document.getElementById('fundId');
	var bankbranchId = branch.options[branch.selectedIndex].value;
	var index=bankbranchId.indexOf("-");
	var bankId = bankbranchId.substring(0,index);
	var brId=bankbranchId.substring(index+1,bankbranchId.length);
	populatefromAccountNumber({fundId: fundObj.options[fundObj.selectedIndex].value,branchId:brId,typeOfAccount:"RECEIPTS_PAYMENTS,RECEIPTS"})
}
function loadToAccNum(branch){
	var fundObj = document.getElementById('fundId');
	var bankbranchId = branch.options[branch.selectedIndex].value;
	var index=bankbranchId.indexOf("-");
	var bankId = bankbranchId.substring(0,index);
	var brId=bankbranchId.substring(index+1,bankbranchId.length);
	populatetoAccountNumber({fundId: fundObj.options[fundObj.selectedIndex].value,branchId:brId,typeOfAccount:"RECEIPTS_PAYMENTS,PAYMENTS"})
}


function populatefromNarration(accnumObj){
    
	var accnum =  accnumObj.options[accnumObj.selectedIndex].value;
	var bankbranchObj=document.getElementById('fromBankId');
	var bankbranchId = bankbranchObj.options[bankbranchObj.selectedIndex].value;
	var index=bankbranchId.indexOf("-");
	var branchId=bankbranchId.substring(index+1,bankbranchId.length);
	var url = '../voucher/common!loadAccNumNarration.action?accnum='+accnum+'&branchId='+branchId;
	YAHOO.util.Connect.asyncRequest('POST', url, postTypeFrom, null);
}
function populatetoNarration(accnumObj){
    
	var accnum =  accnumObj.options[accnumObj.selectedIndex].value;
	var bankbranchObj=document.getElementById('toBankId');
	var bankbranchId = bankbranchObj.options[bankbranchObj.selectedIndex].value;
	var index=bankbranchId.indexOf("-");
	var branchId=bankbranchId.substring(index+1,bankbranchId.length);
	var url = '../voucher/common!loadAccNumNarration.action?accnum='+accnum+'&branchId='+branchId;
	YAHOO.util.Connect.asyncRequest('POST', url, postTypeTo, null);

}

var postTypeFrom = {
success: function(o) {
		var fromNarration= o.responseText;
		//var index=fromNarration.indexOf("-");
		document.getElementById('fromAccnumnar').value=fromNarration;	
		},
    failure: function(o) {
    	alert('failure');
    }
}

var postTypeTo = {
success: function(o) {
		var toNarration= o.responseText;
		//var index=fromNarration.indexOf("-");
		document.getElementById('toAccnumnar').value=toNarration;	
		},
    failure: function(o) {
    	alert('failure');
    }
}



	function nextChqNo() {
		var obj = document.getElementById("fromAccountNumber");
		var bankBr = document.getElementById("fromBankId");
		if (bankBr.selectedIndex == -1) {
			alert("Select Bank Branch and Account No!!");
			return;
		}

		if (obj.selectedIndex == -1) {
			alert("Select Account No!!");
			return;
		}
		var accNo = obj.options[obj.selectedIndex].text;
		var accNoId = obj.options[obj.selectedIndex].value;
		var sRtn = showModalDialog("../HTML/SearchNextChqNo.html?accntNo="
				+ accNo + "&accntNoId=" + accNoId, "",
				"dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
		if (sRtn != undefined)
			document.getElementById("chequeNum").value = sRtn;
	}
	

 function loadFromBalance(obj)
		{
			if(document.getElementById('voucherDate').value=='')
			{
				alert("Please Select the Voucher Date!!");
				obj.options.value=-1;
				return;
			}
			if(obj.options[obj.selectedIndex].value==-1)
				document.getElementById('fromBankBalance').value='';
			else
			{
			
				populatefromBankBalance({bankaccount:obj.options[obj.selectedIndex].value,voucherDate:document.getElementById('voucherDate').value+'&date='+new Date()});
				
				}
		}
		
		function loadToBalance(obj)
		{
			if(document.getElementById('voucherDate').value=='')
			{
				alert("Please Select the Voucher Date!!");
				obj.options.value=-1;
				return;
			}
			if(obj.options[obj.selectedIndex].value==-1)
				document.getElementById('toBankBalance').value='';
			else
				populatetoBankBalance({bankaccount:obj.options[obj.selectedIndex].value,voucherDate:document.getElementById('voucherDate').value+'&date='+new Date()});
		}
				function disableControls(frmIndex, isDisable)
		 {
			for(var i=0;i<document.forms[frmIndex].length;i++)
				document.forms[frmIndex].elements[i].disabled =isDisable;
		 }
			
			function enableAll()
		 {
			for(var i=0;i<document.forms[0].length;i++)
				document.forms[0].elements[i].disabled =false;
		 }

			function validate()  
			{
			var 	insuffientAlert='There is no sufficient bank balance. ';
			var 	continueAlert='Do you want to continue ? ';
			var 	fundFlowNotGeneratedAlert='';				
				if(parseFloat(document.getElementById('fromBankBalance').value)==-1 || parseFloat(document.getElementById('toBankBalance').value)==-1)
				{
					fundFlowNotGeneratedAlert="FundFlowReport is not generated for the for the day. ";
				}
				if(parseFloat(document.getElementById('amount').value)>parseFloat(document.getElementById('fromBankBalance').value))
				{
					if(document.getElementById('bankBalanceMandatory').value=='true')
					{
						alert(insuffientAlert);
						return false;
					}
					else
					{
						if(confirm(fundFlowNotGeneratedAlert+insuffientAlert+continueAlert))
						{
							return true;
						}
						else
						{
							return false;
						}
					}
				}
				return true;
			}	

			function validateReverse()
			{
				return true;
			}
			