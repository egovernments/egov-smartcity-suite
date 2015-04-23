function showPropertyHeaderTab() {
	document.getElementById('property_header').style.display = '';
	setCSSClasses('propertyHeaderTab', 'First Active');
	setCSSClasses('recoveryDetailTab', '');
	setCSSClasses('approvalTab', 'Last');
	hiderecoveryHeaderTab();
	hideApprovalHeaderTab();

}
function showRecoveryHeaderTab() {
	document.getElementById('recovery_header').style.display = '';
	setCSSClasses('propertyHeaderTab', 'First BeforeActive');
	setCSSClasses('recoveryDetailTab', 'Active');
	setCSSClasses('approvalTab', 'Last');
	hidepropertyHeaderTab();
	hideApprovalHeaderTab();

}

function showApprovalTab() {
	document.getElementById('approval_header').style.display = '';
	setCSSClasses('propertyHeaderTab', 'First');
	setCSSClasses('recoveryDetailTab', 'BeforeActive');
	setCSSClasses('approvalTab', 'Last Active ActiveLast');
	hidepropertyHeaderTab();
	hiderecoveryHeaderTab();

}

function setCSSClasses(id, classes) {
	document.getElementById(id).setAttribute('class', classes);
	document.getElementById(id).setAttribute('className', classes);
}
function hidepropertyHeaderTab() {
	document.getElementById('property_header').style.display = 'none';
}
function hiderecoveryHeaderTab() {
	document.getElementById('recovery_header').style.display = 'none';
}
function hideApprovalHeaderTab() {
	document.getElementById('approval_header').style.display = 'none';

}

function validateIntimateRecovery(obj) {

	document.getElementById("lblError").style.display = 'none';
	if (dom.get('paymentDueDate').value == '') {
		document.getElementById("lblError").style.display = 'block';
		document.getElementById("lblError").innerHTML = "Please Enter Payment Due Date";
		return false;
	} else {
		return validateApproval(obj);
	}

}
function validateApproval(obj) {
	document.getElementById("workflowBean.actionName").value = obj.value;

	if (obj.value.toUpperCase() == "REJECT"
			|| obj.value.toUpperCase() == "SAVE"
			|| obj.value.toUpperCase() == "APPROVE") {
		return true;
	} else if (document.getElementById("approverUserId")
			&& document.getElementById("approverUserId").value == "-1") {
		document.getElementById("lblError").style.display = 'block';
		document.getElementById("lblError").innerHTML = 'Please select approver ';
		return false;
	} else {
		return true;
	}

}
function checkLength1024(obj) {
	if (obj.value.length > 1024) {
		alert('Max 1024 characters are allowed. Remaining characters are truncated.')
		obj.value = obj.value.substring(1, 1024);
	}
}

function validateWarrantApplication(obj) {
	document.getElementById("lblError").style.display = "none";
	document.getElementById("lblError").innerHTML ="";
	var warrentFee = document.getElementById('warrentFee').value;
	var noticeFee = document.getElementById('noticeFee').value;
	
		if (warrentFee == '' || warrentFee =="0" || warrentFee =="0.00" ) {
			document.getElementById("lblError").style.display = 'block';
			document.getElementById("lblError").innerHTML = "Please Enter Warrant Fee";
			return false;
		}
		/*else  if (courtFee == '') {
			document.getElementById("lblError").style.display = 'block';
			if(document.getElementById("lblError").innerHTML == ''){
				document.getElementById("lblError").innerHTML = "Please Enter Court Fee";
			}else{			
			document.getElementById("lblError").innerHTML = document.getElementById("lblError").innerHTML+ ", Court Fee";
			}
		}*/
		else if (noticeFee == ''|| noticeFee =="0" || noticeFee =="0.00") {
			document.getElementById("lblError").style.display = 'block';
			document.getElementById("lblError").innerHTML = "Please Enter Notice Fee";
			return false;
		}else{
			document.getElementById('courtFee').value=0.00;
			return validateApproval(obj);
		}
		
	
}

function totalFees(obj) {
	var warrentFee = document.getElementById('warrentFee').value != ""?document.getElementById('warrentFee').value:0.00;
	var courtFee = document.getElementById('courtFee').value != ""?document.getElementById('courtFee').value:0.00;
	var noticeFee = document.getElementById('noticeFee').value != ""?document.getElementById('noticeFee').value:0.00;
	var totalFee = parseFloat(noticeFee) + parseFloat(courtFee)
			+ parseFloat(warrentFee);
	  var roundedOffTotalFee = roundoff(totalFee);
	document.getElementById('ttlFee').innerHTML = roundedOffTotalFee;
		}

function validateWarrantNotice(obj){
	document.getElementById("lblError").style.display='none';
	if(dom.get('warrantReturnByDate').value==''){
		document.getElementById("lblError").style.display='block';
		document.getElementById("lblError").innerHTML  = "Please Enter Warrant Retuen By Date";
		return false;
	}else{
		return validateApproval(obj);
	} 
}
function validateCeaseNotice(obj){
		document.getElementById("lblError").style.display='none';
		if(dom.get('executionDate').value==''){
			document.getElementById("lblError").style.display='block';
			document.getElementById("lblError").innerHTML  = "Please Enter Execution Date";
			return false;
		}else{
			return validateApproval(obj);
		} 
}
function displayNotice(noticeNumber) {
		var sUrl = "/egi/docmgmt/ajaxFileDownload.action?moduleName=PT&docNumber="+noticeNumber+"&fileName="+noticeNumber+".pdf";
		window.open(sUrl,"window",'scrollbars=yes,resizable=no,height=200,width=400,status=yes');
}


