function populateAccNum(branch){
	var bankbranchId = branch.options[branch.selectedIndex].value;
	var index=bankbranchId.indexOf("-");
	var bankId = bankbranchId.substring(0,index);
	var brId=bankbranchId.substring(index+1,bankbranchId.length);
	populateaccountNumber({bankId:bankId,branchId:brId})
}


function populateNarration(element){
	var accountNumber =  element.options[element.selectedIndex].value;
	var url = '../voucher/common!loadAccNumNarrationAndFund.action?accnum='+accountNumber;
	YAHOO.util.Connect.asyncRequest('POST', url, postType, null);
}

var postType = {
		success: function(o) {
				var narrationfund= o.responseText;
				var index=narrationfund.indexOf("-");
				document.getElementById('accnumnar').value=narrationfund.substring(o,index);	
				var fundid = narrationfund.substring(index+1,narrationfund.length);	
				document.getElementById('fundId').value = fundid;
				document.getElementById('fundId').disabled =true;
				document.getElementById('accnumnar').disabled =true;
				if(document.getElementById('subschemeid')!=null && document.getElementById('subschemeid')!= 'undefined'){
					populateschemeid({fundId:fundid})
				}
		    },
		    failure: function(o) {
		    	alert('Failure');
		    }
	}

var callback = {
		success: function(o){
			document.getElementById('resultGrid').innerHTML=o.responseText;
			document.getElementById('availableBalance').value=document.getElementById('balanceResult').value;
			},
			failure: function(o) {
		    }
		}

function populateAvailableBalance(accnumObj){
	var accnum =  accnumObj.options[accnumObj.selectedIndex].value;
	var voucherDate = document.getElementById('voucherDate').value;
	var url = '../contra/contraBTC!ajaxAvailableBalance.action?accountNumberId='+accnum+'&voucherHeader.voucherDate='+voucherDate;
	YAHOO.util.Connect.asyncRequest('POST', url, callback, null);
}

function disableControls(frmIndex, isDisable){
	for(var i=0;i<document.forms[frmIndex].length;i++){
		if(document.forms[frmIndex].elements[i].value != "Close" && document.forms[frmIndex].elements[i].value != "Print"){
			document.forms[frmIndex].elements[i].disabled =isDisable;
		}
	}
}

function nextChqNo(){
	var obj=document.getElementById("accountNumber");
	var bankBr=document.getElementById("bankId");
	if( bankBr.selectedIndex==-1){
	  alert("Select Bank Branch and Account No!!");
	  return;
	}

	if(obj.selectedIndex==-1){
	  alert("Select Account No!!");
	  return;
	}
	var accNo=obj.options[obj.selectedIndex].text;
	var accNoId=obj.options[obj.selectedIndex].value;
	var sRtn =showModalDialog("../HTML/SearchNextChqNo.html?accntNo="+accNo+"&accntNoId="+accNoId,"","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
	if(sRtn!=undefined ) document.getElementById("chequeNumber").value=sRtn;
}