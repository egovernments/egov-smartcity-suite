function validate(name,value)
{
	if(!validateAppoveUser(name,value))
	{
	return false;
	}
 enableAll();
return updateAndCheckAmount();
}
function updateAndCheckAmount()
{
var debit=document.getElementById('totaldbamount').value;
var credit=document.getElementById('totalcramount').value ;
var dbcr=debit-credit;
var amt=document.getElementById('amount').value;
if(isNaN(amt))
{
	alert(amountshouldbenumeric);
	return false;
}
//document.getElementById('amount').value=debit-credit;
if(amt!=dbcr)
{
alert(totalsnotmatchingamount);
return false;
}
else
{
return true;
}

}




function populateNarration(accnumObj){
    
	var accnum =  accnumObj.options[accnumObj.selectedIndex].value;
	var bankbranchObj=document.getElementById('bankId');
	var bankbranchId = bankbranchObj.options[bankbranchObj.selectedIndex].value;
	var index=bankbranchId.indexOf("-");
	var branchId=bankbranchId.substring(index+1,bankbranchId.length);
	var url = '../voucher/common!loadAccNumNarration.action?accnum='+accnum+'&branchId='+branchId;
	YAHOO.util.Connect.asyncRequest('POST', url, postTypeFrom, null);
}

/*function loadBalance(obj)
{
	if(dom.get('voucherdate').value=='')
	{
		alert("Please Select the Voucher Date!!");
		obj.options.value=-1;
		return;
	}
	if(obj.options[obj.selectedIndex].value==-1)
		dom.get('balance').value='';
	else
		populatebalance({bankaccount:obj.options[obj.selectedIndex].value,voucherDate:dom.get('voucherdate').value+'&date='+new Date()});
}
*/

function populateAvailableBalance(accnumObj){
	if(document.getElementById('voucherDate').value=='')
	{
		alert("Please Select the Voucher Date!!");
		accnumObj.options.value=-1;
		return;
	}
	if(accnumObj.options[accnumObj.selectedIndex].value==-1)
		document.getElementById('availableBalance').value='';
	else
		populateavailableBalance({bankaccount:accnumObj.options[accnumObj.selectedIndex].value,voucherDate:document.getElementById('voucherDate').value+'&date='+new Date()});

}

var callback = {
		success: function(o){
	        alert(o.responseText.value);
			document.getElementById('availableBalance').value=o.responseText;
			},
			failure: function(o) {
		    }
		}                  
		
var postTypeFrom = {
success: function(o) {
		document.getElementById('accnumnar').value= o.responseText;
		},
    failure: function(o) {
    	alert('failure');
    }
}

function loadBank(fund)
{
	var vTypeOfAccount = document.getElementById('typeOfAccount').value;
populatebankId({fundId:fund.options[fund.selectedIndex].value, typeOfAccount:vTypeOfAccount})	
}




function enableAll()
{
	for(var i=0;i<document.forms[0].length;i++)
		document.forms[0].elements[i].disabled =false;
}

function disableControls(frmIndex, isDisable)
{
	for(var i=0;i<document.forms[frmIndex].length;i++)
		document.forms[frmIndex].elements[i].disabled =isDisable;
}

function balanceCheck(obj, name, value)   
		{
			
			if(!validateAppoveUser(name,value))
				return false;
	
			if(obj.id=='wfBtn1') // in case of Reject
				return true;
			if(document.getElementById('balanceAvl') && document.getElementById('balanceAvl').style.display=="block" )
			{
				if(parseFloat(document.getElementById('amount').value)>parseFloat(document.getElementById('availableBalance').value))
				{
					alert(insuffiecientBankBalance);
					return false;
				}
			}
			return true;
		}
function updateVoucherNumber()
{
document.getElementById('voucherNumber').value=document.getElementById('voucherNumberRest').value;
}

function disableForNonBillPayment()
{
	var frmIndex=0;
	for(var i=0;i<document.forms[frmIndex].length;i++)
	document.forms[frmIndex].elements[i].disabled =true;
	document.getElementById("closeButton").disabled=false;
	if(document.getElementById("closeButtonNew"))
		document.getElementById("closeButtonNew").disabled=false;
	if(document.getElementById("comments"))
		document.getElementById("comments").disabled=false;
	document.getElementById("bankId").disabled=false;
	document.getElementById("accountNumber").disabled=false;
	document.getElementById("modeOfPaymentcheque").disabled=false;
	document.getElementById('commonBean.documentNumber').disabled=false;
	document.getElementById("documentDate").disabled=false;
	if(document.getElementById("voucherNumber"))
	{
			document.getElementById("voucherNumber").disabled=false;
			document.getElementById("voucherNumber").value="";
	}
	document.getElementById('paidTo').disabled=false;
	document.getElementById("description").disabled=false;
	if(document.getElementById('wfBtn0'))
		document.getElementById('wfBtn0').disabled=false;
	if(document.getElementById('wfBtn1'))
		document.getElementById('wfBtn1').disabled=false;
	if(document.getElementById('wfBtn2'))
		document.getElementById('wfBtn2').disabled=false;
	if(document.getElementById('designationId'))
		document.getElementById('designationId').disabled=false;
	if(document.getElementById('departmentid'))
		document.getElementById('departmentid').disabled=false;
	if(document.getElementById('approverUserId'))
		document.getElementById('approverUserId').disabled=false;
}





