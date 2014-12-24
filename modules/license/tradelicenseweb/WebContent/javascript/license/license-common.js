
function validatePan(panField) {
	var panValue =panField.value;	  
	var regex1=/^[A-Z]{5}\d{4}[A-Z]{1}$/;
	
	if(!regex1.test(panValue)) {
	  alert('Please enter a valid PAN');
	  panField.value = "";
	  return false;
	}
}
function numbersonly(myfield, e) {
	var key;
	var keychar;

	if (window.event)
		key = window.event.keyCode;
	else if (e)
		key = e.which;
	else
		return true;
	keychar = String.fromCharCode(key);

	// control keys
	if ((key == null) || (key == 0) || (key == 8) || (key == 9) || (key == 13)
			|| (key == 27))
		return true;

	// numbers
	else if ((("0123456789").indexOf(keychar) > -1))
		return true;
	else
		return false;
}

function setupAjaxDivision(elem) {
	zone_id = elem.options[elem.selectedIndex].value;
	populatedivision({
		zoneId : zone_id
	});
}
function setupAjaxArea(elem) {
	// will be uncommented once area is loaded
	// wardid=elem.options[elem.selectedIndex].value;
	// populatearea({divisionId:wardid});
}

function setupLicenseeAjaxDivision(elem) {
	zone_id = elem.options[elem.selectedIndex].value;
	populatelicenseedivision({
		zoneId : zone_id
	});
}
function setupLicenseeAjaxArea(elem) {
	// will be uncommented once area is loaded
	// wardid=elem.options[elem.selectedIndex].value;
	// populatearea({divisionId:wardid});
}

function waterMarkTextIn(styleId, value) {
	var txt = document.getElementById(styleId).value;
	if (txt == value) {
		document.getElementById(styleId).value = '';
		document.getElementById(styleId).style.color = '';
	}
}

function waterMarkTextOut(styleId, value) {
	var txt = document.getElementById(styleId).value;
	if (txt == '') {
		document.getElementById(styleId).value = value;
		document.getElementById(styleId).style.color = 'DarkGray';
	}
}

function waterMarkInitialize(styleId, value) {
	if (document.getElementById(styleId).value == '') {
		document.getElementById(styleId).value = value;
		document.getElementById(styleId).style.color = 'DarkGray';
	}
}

function chkNum(obj, checkAssessee) {
	obj.value = obj.value.replace(/[^0-9]/g, '') // numbers only
	if (checkAssessee) {
		checkAssesseeExists();
	}
}
function chkNum(obj) {
	obj.value = obj.value.replace(/[^0-9]/g, '') // numbers only
}
function chkDecimal(obj) {
	obj.value = obj.value.replace(/[^0-9.]/g, '') // numbers only
	obj.value = obj.value.replace(/[.]+/g, '.')
}

function showDocumentManager() {
	var docNum = document.getElementById("docNumber").value;
	showDocumentManagerView(docNum);
}

function showDocumentManagerForDoc(docId) {
	var docNum = document.getElementById(docId).value;
	showDocumentManagerView(docNum);
}

function showDocumentManagerView(docNumber) {
	var url;
	if (docNumber == null || docNumber == '' || docNumber == 'To be assigned' || docNumber == 'null') {
		url = "/tradelicense/citizen/document/uploadDocumentLicense.action?moduleName=tradelicense";
	} else {
		url = "/tradelicense/citizen/document/uploadDocumentLicense!editDocument.action?docNumber="+ docNumber + "&moduleName=tradelicense";
	}
	window.open(url, 'docupload', 'width=1000,height=400');
}

var currentDocId = null;
function docNumberUpdater(docNumber) {
	if (currentDocId != null) {
		document.getElementById(currentDocId).value = docNumber;
		currentDocId=null;
    }
}

function updateCurrentDocId(docId) {
	currentDocId = docId;
}

function refreshInbox() {		
	if(opener && opener.top.document.getElementById('inboxframe')) {
		opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
	} else if (opener && opener.opener && opener.opener.top.document.getElementById('inboxframe')) {
		opener.opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
	}
}

function numbersforamount(myfield, e)
{
	var key;
	var keychar;

	if (window.event)
		key = window.event.keyCode;
	else if (e)
		key = e.which;
	else
		return true;
	keychar = String.fromCharCode(key);

	// control keys
	if ((key==null) || (key==0) || (key==8) || 
			(key==9) || (key==13) || (key==27) )
		return true;

	// numbers
	else if (((".0123456789").indexOf(keychar) > -1))
		return true;
	else
		return false;
}

function lessThanOrEqualToCurrentDate(dt) {
	if(dt.value == "") {
		return;
	}
	var selectedDate = dt.value.split("/");
	var currentDate = new Date();
	var date = new Date(selectedDate[2],eval(selectedDate[1]-1),selectedDate[0],currentDate.getHours(),currentDate.getMinutes(),currentDate.getSeconds() ,currentDate.getMilliseconds())
	if(date >  currentDate ) {
		alert("Application Date should be less than or equal to today!");
		dt.value = "";
		return false;
	}
}
