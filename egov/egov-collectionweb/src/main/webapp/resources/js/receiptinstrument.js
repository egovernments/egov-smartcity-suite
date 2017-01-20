function setCashInstrumentDetails(elem) {
	document.getElementById("instrHeaderCash.instrumentAmount").value = elem.value;
	document.getElementById("instrumentTypeCashOrCard").value = "cash";
}

function setCardInstrumentDetails(elem) {
	document.getElementById("instrHeaderCard.instrumentAmount").value = elem.value;
	document.getElementById("instrumentTypeCashOrCard").value = "card";
}
function setBankInstrumentDetails(elem) {
	document.getElementById("instrHeaderBank.instrumentAmount").value = elem.value;
	document.getElementById("instrumentTypeCashOrCard").value = "bankchallan";
}

function setOnlineInstrumentDetails(elem) {
	document.getElementById("instrHeaderOnline.instrumentAmount").value = elem.value;
	document.getElementById("instrumentTypeCashOrCard").value = "online";
}

var bankfuncObj;
var bankArray;
function loadDropDownCodesBank() {
	var url = "/EGF/voucher/common-ajaxGetAllBankName.action";
	var req2 = initiateRequest();
	req2.onreadystatechange = function() {
		if (req2.readyState == 4) {
			if (req2.status == 200) {
				var codes2 = req2.responseText;
				var a = codes2.split("^");
				var codes = a[0];
				bankArray = codes.split("+");
				bankfuncObj = new YAHOO.widget.DS_JSArray(bankArray);
			}
		}
	};
	req2.open("GET", url, true);
	req2.send(null);
}

var yuiflagBank = new Array();
function autocompletecodeBank(obj, myEvent) {
	// Fix-Me
	var branchObj = document.getElementById('instrumentBranchName');
	//jQuery(branchObj).trigger('focus');
	//jQuery(obj).focus();
	var src = obj;
	var target = document.getElementById('bankcodescontainer');
	var posSrc = findPos(src);
	target.style.left = posSrc[0];
	target.style.top = posSrc[1] - 40;
	target.style.width = 450;
	bankfuncObj
	var coaCodeObj = obj;
	var currRow = getRow(obj);
	// 40 --> Down arrow, 38 --> Up arrow
	if (yuiflagBank[currRow] == undefined) {
		var key = window.event ? window.event.keyCode : myEvent.charCode;
		if (key != 40) {
			if (key != 38) {
				var oAutoComp = new YAHOO.widget.AutoComplete(coaCodeObj,
						'bankcodescontainer', bankfuncObj);
				oAutoComp.queryDelay = 0;
				oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
				oAutoComp.useShadow = true;
				oAutoComp.maxResultsDisplayed = 15;
				oAutoComp.useIFrame = true;
				bankfuncObj.applyLocalFilter = true;
				bankfuncObj.queryMatchContains = true;
				oAutoComp.minQueryLength = 0;
				if(bankfuncObj){
			    bankfuncObj.applyLocalFilter = true;
			    bankfuncObj.queryMatchContains = true;
				 }
			}
		}
		yuiflagBank[currRow] = 1;
	}
}
function fillAfterSplitBank(obj) {
	var currRow = getRow(obj);
	var temp = obj.value;
	temp = temp.split("`-`");
	if (temp[1]) {
		obj.value = temp[0];
		getControlInBranch(currRow, 'bankID').value = temp[1];
		getControlInBranch(currRow, 'bankName').value = temp[0];

	}
	/*
	 * else { getControlInBranch(currRow,'bankID').value="";
	 * getControlInBranch(currRow,'bankName').value=""; }
	 */

}

function clearCashDetails() {
	document.getElementById('instrHeaderCash.instrumentAmount').value = "";
}

function clearChequeDDDetails() {
	var table = document.getElementById('chequegrid');
	var len = table.rows.length;

	for (var j = 0; j < len; j++) {
		// clear instrument type
		if (getControlInBranch(table.rows[j], 'instrumentType') != null) {
			getControlInBranch(table.rows[j], 'instrumentType').value = "";
		}
		// clear instrument number
		if (getControlInBranch(table.rows[j], 'instrumentChequeNumber') != null) {
			getControlInBranch(table.rows[j], 'instrumentChequeNumber').value = "";
		}
		// clear bank name
		if (getControlInBranch(table.rows[j], 'bankID') != null) {
			getControlInBranch(table.rows[j], 'bankID').value = "-1";
		}

		if (getControlInBranch(table.rows[j], 'bankName') != null) {
			getControlInBranch(table.rows[j], 'bankName').value = "";
		}

		// clear date
		if (getControlInBranch(table.rows[j], 'instrumentDate') != null) {
			getControlInBranch(table.rows[j], 'instrumentDate').value = "";
			waterMarkInitialize('instrumentDate', 'DD/MM/YYYY');
		}

		// clear instrument amount
		if (getControlInBranch(table.rows[j], 'instrumentChequeAmount') != null) {
			getControlInBranch(table.rows[j], 'instrumentChequeAmount').value = "";
		}

		// clear branch name
		if (getControlInBranch(table.rows[j], 'instrumentBranchName') != null) {
			getControlInBranch(table.rows[j], 'instrumentBranchName').value = "";
		}
	}

	for (var z = 5; z < len; z++) {
		table.deleteRow(5);
	}
}

function clearCardDetails() {
	document.getElementById('instrHeaderCard.instrumentAmount').value = "";
	document.getElementById('instrHeaderCard.transactionNumber').value = "";
	document.getElementById('instrHeaderCard.instrumentNumber').value = "";
}
function clearBankDetails() {
	document.getElementById('instrHeaderBank.instrumentAmount').value = "";
	document.getElementById('instrHeaderBank.transactionNumber').value = "";
	document.getElementById('bankChallanDate').value = "";
	if (document.getElementById("accountNumberMaster") != null) {
		document.getElementById("accountNumberMaster").options.length = 1;
	}
}
// 'chequegrid','chequetyperow','chequedetailsrow','chequebankrow','chequeamountrow',this,'chequeaddrow'
function addChequeGrid(tableId, trId2, trId3, trId4, obj, trId5) {
	document.getElementById("error_area").innerHTML = "";
	var chequetable = document.getElementById('chequegrid');
	var chequetablelen1 = chequetable.rows.length;

	if (!verifyChequeDetails(chequetable, chequetablelen1)) {
		instrAmountInvalidErrMsg = '<s:text name="billreceipt.missingchequeamount.errormessage" />'
				+ '<br>';
		document.getElementById("error_area").innerHTML = 'Please Enter Mandatory Cheque/DD Details Before Adding A New Cheque/DD';
		dom.get("error_area").style.display = "block";
	}

	else {
		// To add rows to the cheque grid table
		addtablerow(tableId, trId2, trId3, trId4, trId5);
		var tbl = dom.get(tableId);
		var rowNumber = getRow(obj).rowIndex;
		var newtablelength = tbl.rows.length;

		var count = document.forms[0].instrumentCount.value;
		count = eval(count) + 1;
		document.forms[0].instrumentCount.value = count;
		document.forms[0].instrumentChequeNumber[0].value = "";
		document.forms[0].instrumentDate[0].value = "";
		document.forms[0].instrumentBranchName[0].value = "";
		document.forms[0].bankName[0].value = "";
		document.forms[0].instrumentChequeAmount[0].value = "";
		document.forms[0].bankID[0].value = "-1";
		document.forms[0].bankID[0].name = "instrumentProxyList[" + count
				+ "].bankId.id";
		document.forms[0].bankName[0].name = "instrumentProxyList[" + count
				+ "].bankId.name";
		document.forms[0].instrumentChequeAmount[0].name = "instrumentProxyList["
				+ count + "].instrumentAmount";
		document.forms[0].instrumentBranchName[0].name = "instrumentProxyList["
				+ count + "].bankBranchName";
		document.forms[0].instrumentChequeNumber[0].name = "instrumentProxyList["
				+ count + "].instrumentNumber";
		document.forms[0].instrumentDate[0].name = "instrumentProxyList["
				+ count + "].instrumentDate";
		// document.forms[0].instrumentChequeAmount[0].name="instrumentProxyList["+count+"].instrumentDate";

		getControlInBranch(tbl.rows[rowNumber], 'addchequerow').style.display = "block";
		getControlInBranch(tbl.rows[newtablelength - 1], 'addchequerow').style.display = "none";
		getControlInBranch(tbl.rows[newtablelength - 1], 'deletechequerow').style.display = "block";

		getControlInBranch(tbl.rows[newtablelength - 4],
				'instrumentChequeNumber').readOnly = "true";
		getControlInBranch(tbl.rows[newtablelength - 4], 'instrumentDate').readOnly = "true";
		getControlInBranch(tbl.rows[newtablelength - 3], 'bankName').readOnly = "true";
		getControlInBranch(tbl.rows[newtablelength - 3], 'instrumentBranchName').readOnly = "true";
		getControlInBranch(tbl.rows[newtablelength - 2],
				'instrumentChequeAmount').readOnly = "true";
		getControlInBranch(tbl.rows[newtablelength - 3], 'bankID').readOnly = "true";
	}
}

function addtablerow(tableId, trId2, trId3, trId4, trId5) {
	var tbl = dom.get(tableId);
	var rowObj2 = dom.get(trId2).cloneNode(true);
	var rowObj3 = dom.get(trId3).cloneNode(true);
	var rowObj4 = dom.get(trId4).cloneNode(true);
	var rowObj5 = dom.get(trId5).cloneNode(true);
	addRow(tbl, rowObj2);
	addRow(tbl, rowObj3);
	addRow(tbl, rowObj4);
	addRow(tbl, rowObj5);
	dom.get("delerror").style.display = "none";
}

function addRow(tableObj, rowObj) {
	var tbody = tableObj.tBodies[0];
	tbody.appendChild(rowObj);
}

function deleteChequeObj(obj, tableId, deltable) {
	var tbl = dom.get(tableId);
	var rowNumber = getRow(obj).rowIndex;
	if (tbl.rows.length == 6) {
		dom.get("delerror").style.display = "block";
	} else {
		dom.get("delerror").style.display = "none";
		tbl.deleteRow(rowNumber);
		tbl.deleteRow(rowNumber - 1);
		tbl.deleteRow(rowNumber - 2);
		tbl.deleteRow(rowNumber - 3);
		// tbl.deleteRow(rowNumber-4);
	}
	setChequeInstrumentDetails();
	var tbl = dom.get(tableId);
	var rowNumber = getRow(obj).rowIndex;
	var newtablelength = tbl.rows.length;
	var countUI = document.forms[0].instrumentCount.value;
	var count = document.forms[0].instrumentCount.value;
	for (var j = newtablelength; j > 0; j = j - 4) {
		count = eval(count) - 1;
		getControlInBranch(tbl.rows[j - 4], 'instrumentChequeNumber').name = "instrumentProxyList["
				+ count + "].instrumentNumber";
		getControlInBranch(tbl.rows[j - 4], 'instrumentDate').name = "instrumentProxyList["
				+ count + "].instrumentDate";
		getControlInBranch(tbl.rows[j - 3], 'instrumentBranchName').name = "instrumentProxyList["
				+ count + "].bankBranchName";
		getControlInBranch(tbl.rows[j - 2], 'instrumentChequeAmount').name = "instrumentProxyList["
				+ count + "].instrumentAmount";
		getControlInBranch(tbl.rows[j - 3], 'bankName').name = "instrumentProxyList["
				+ count + "].bankId.name";
		getControlInBranch(tbl.rows[j - 3], 'bankID').name = "instrumentProxyList["
				+ count + "].bankId.id";
	}
	count = eval(countUI) - 1;
	document.forms[0].instrumentCount.value = count;
}

function setChequeInstrumentDetails() {
	var chequetable = document.getElementById('chequegrid');
	var chequetablelen1 = chequetable.rows.length;
	var chequeamount = 0;
	var chequeTotal = 0;
	for (var m = 0; m < chequetablelen1; m++) {
		if (getControlInBranch(chequetable.rows[m], 'instrumentChequeAmount') != null) {
			chequeamount = getControlInBranch(chequetable.rows[m],
					'instrumentChequeAmount').value;
			chequeamount = eval(chequeamount);
			chequeTotal = chequeTotal + chequeamount;
		}
	}// end of for loop
	dom.get("totalamountdisplay").value = eval(chequeTotal).toFixed(2);
}

function setinstrumenttypevalue(obj) {
	document.getElementById('instrumentType').value = obj.value;
}

function displayPaymentDetails() {
	if (document.getElementById("totalamounttobepaid") != null
			&& document.getElementById("totalamounttobepaid").value != "") {
		var collectionamount = parseFloat(document
				.getElementById("totalamounttobepaid").value);
		document.getElementById("totalamounttobepaid").value = isNaN(collectionamount) ? collectionamount
				: collectionamount.toFixed(2);
	}
	if (document.getElementById("instrHeaderBank.instrumentAmount") != null
			&& document.getElementById("instrHeaderBank.instrumentAmount").value != "") {
		document.getElementById('bankradiobutton').checked = true;
		document.getElementById('bankdetails').style.display = 'table-row';
		document.getElementById('instrumentTypeCashOrCard').value = "bankchallan";
		document.getElementById('cashdetails').style.display = "none";
		document.getElementById('onlinedetails').style.display = "none";
		// document.getElementById('carddetails').style.display="none";
	}
	if (document.getElementById("instrHeaderCard.instrumentAmount") != null
			&& document.getElementById("instrHeaderCard.instrumentAmount").value != "") {
		document.getElementById('cardradiobutton').checked = true;
		document.getElementById('carddetails').style.display = 'table-row';
		document.getElementById('instrumentTypeCashOrCard').value = "card";
		document.getElementById('cashdetails').style.display = "none";
		document.getElementById('onlinedetails').style.display = "none";
	}

	var chequetable = document.getElementById('chequegrid');
	var chequetablelen1 = chequetable.rows.length;
	for (var m = 0; m < chequetablelen1; m++) {
		var chequeAmt = getControlInBranch(chequetable.rows[m],
				'instrumentChequeAmount');
		if (chequeAmt != null && chequeAmt.value != "") {
			document.getElementById('chequeradiobutton').checked = true;
			document.getElementById('chequeDDdetails').style.display = 'table-row';
			document.getElementById('instrumentTypeCashOrCard').value = "";
			document.getElementById('cashdetails').style.display = "none";
			document.getElementById('onlinedetails').style.display = "none";
		}
	}

	// loadchequegrid('chequegrid','chequetyperow','chequedetailsrow','chequebankrow','chequeamountrow','chequeaddrow');
}

function loadchequedetails() {
	var chequetable = document.getElementById('chequegrid');
	var chequetablelen1 = chequetable.rows.length;
	var tbl = document.getElementById("chequegrid");

	for (var j = chequetablelen1; j > 4; j = j - 4) {
		if (chequetablelen1 > 4) {
			getControlInBranch(tbl.rows[j - 1], 'deletechequerow').style.display = "block";
		}
	}
}

// This function is called to display the payt modes at the time of body load
// and
// at the time of reset
function displayPaytModes() {
	var cashAllowed = document.getElementById("cashAllowed").value;
	var cardAllowed = document.getElementById("cardAllowed").value;
	// var chequeAllowed=document.getElementById("chequeAllowed").value;
	// var ddAllowed=document.getElementById("ddAllowed").value;
	var chequeDDAllowed = isChequeDDAllowed();
	var bankAllowed = document.getElementById("bankAllowed").value;
	var onlineAllowed = document.getElementById("onlineAllowed").value;
	clearPaytModes();

	if (cashAllowed == 'true') {
		// display cash radio button, set it as checked and display cash details
		document.getElementById('cashradiobuttonspan').style.display = "block";

		document.getElementById('cashradiobutton').checked = true;
		document.getElementById('cashdetails').style.display = 'table-row';
		document.getElementById('instrumentTypeCashOrCard').value = "cash";
	} else {
		// do not display cash details
		document.getElementById('cashradiobuttonspan').style.display = "none";
		document.getElementById('cashdetails').style.display = 'table-row';
	}
	
	if (cardAllowed == 'true') {
		// display card radio button
		document.getElementById('cardradiobuttonspan').style.display = "block";
		document.getElementById('instrumentTypeCashOrCard').value = "card";
	} else {
		// do not display card radio button
		document.getElementById('cardradiobuttonspan').style.display = "none";
	}
	if (chequeDDAllowed == 'true') {
		// display cheque DD radio button
		document.getElementById('chequeradiobuttonspan').style.display = "block";
	} else {
		// do not display cheque/DD radio button
		document.getElementById('chequeradiobuttonspan').style.display = "none";
	}
	if (bankAllowed == 'true') {
		// display bank radio button
		document.getElementById('bankradiobuttonspan').style.display = "block";
		document.getElementById('instrumentTypeCashOrCard').value = "bankchallan";
	} else {
		// do not display card radio button
		document.getElementById('bankradiobuttonspan').style.display = "none";
	}
	
	if (onlineAllowed == 'true') {
		// display bank radio button
		document.getElementById('onlineradiobuttonspan').style.display = "block";
		document.getElementById('instrumentTypeCashOrCard').value = "online";
	} else {
		// do not display card radio button
		document.getElementById('onlineradiobuttonspan').style.display = "none";
	}
	// if cash is not allowed and cheque is allowed, set cheque as the default
	// payt
	if (chequeDDAllowed == 'true' && cashAllowed == 'false') {
		document.getElementById('chequeradiobutton').checked = true;
		document.getElementById('chequeDDdetails').style.display = 'table-row';
		document.getElementById('cashdetails').style.display = "none";
		document.getElementById('instrumentTypeCashOrCard').value = "";
	}
	// if cash, cheque/DD are not allowed and card is allowed, set card as the
	// default payt
	if (cardAllowed == 'true' && cashAllowed == 'false'
			&& chequeDDAllowed == 'false') {
		document.getElementById('cardradiobutton').checked = true;
		document.getElementById('carddetails').style.display = 'table-row';
		document.getElementById('instrumentTypeCashOrCard').value = "card";
	}
	// if cash, cheque/DD and card are not allowed and bank is allowed, set bank
	// as the default payt
	if (bankAllowed == 'true' && cashAllowed == 'false'
			&& chequeDDAllowed == 'false' && cardAllowed == 'false') {
		document.getElementById('bankradiobutton').checked = true;
		document.getElementById('bankdetails').style.display = 'table-row';
		document.getElementById('instrumentTypeCashOrCard').value = "bankchallan";
	}
	if (onlineAllowed == 'true' && cashAllowed == 'false'
		&& chequeDDAllowed == 'false' && cardAllowed == 'false' && bankAllowed=='false') {
		// display online radio button, set it as checked and display online details
		document.getElementById('onlineradiobuttonspan').style.display = "block";

		document.getElementById('onlineradiobutton').checked = true;
		document.getElementById('onlinedetails').style.display = 'table-row';
		document.getElementById('instrumentTypeCashOrCard').value = "online";
	} 
}

function isChequeDDAllowed() {
	var chequeAllowed = document.getElementById("chequeAllowed").value;
	var ddAllowed = document.getElementById("ddAllowed").value;
	if (chequeAllowed == 'true' || ddAllowed == 'true') {
		return "true";
	}

	return "false";
}

function clearPaytModes() {
	// deselect all payt mode radio buttons
	document.getElementById('cashradiobuttonspan').style.display = "none";
	document.getElementById('cashdetails').style.display = "table-row";

	document.getElementById('cardradiobuttonspan').style.display = "none";
	document.getElementById('carddetails').style.display = "none";

	document.getElementById('chequeradiobuttonspan').style.display = "none";
	document.getElementById('chequeDDdetails').style.display = "none";

	document.getElementById('bankradiobuttonspan').style.display = "none";
	document.getElementById('bankdetails').style.display = "none";
	
	document.getElementById('onlineradiobuttonspan').style.display = "none";
	document.getElementById('onlinedetails').style.display = "none";
}

function process(date){
	   var parts = date.split("/");
	   return new Date(parts[2], parts[1] - 1, parts[0]);
	}
