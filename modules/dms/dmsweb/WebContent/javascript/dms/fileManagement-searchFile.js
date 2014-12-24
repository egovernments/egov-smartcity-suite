
/*
 * On Change of department this function will be called
 */
function submitForm(arg) {
	var fromdate = document.getElementById("fromDateSearchParam").value;
	var todate = document.getElementById("toDateSearchParam").value;
	if (fromdate != null && fromdate != "" && fromdate != undefined && todate != null && todate != "" && todate != undefined) {
		if (compareDate(fromdate, todate) == -1) {
			alert("From Date  should be less than or equals to To Date");
			document.getElementById("fromDateSearchParam").focus();
			return false;
		}
	}
}
function populateSecondLevelBoundaries(obj) {
	var zoneValue = obj.value;
	if (zoneValue == "") {
		return;
	}
	var sUrl = "../dms/searchFile!getSecondLevelBoundries.action?parentId=" + zoneValue;
	var callback = {success:function (oResponse) {
		var responseData = oResponse.responseText;
		if (responseData == "error") {
			document.getElementById("ward").length = 0;
			document.getElementById("ward").options[0] = new Option("--Select a Ward--", "");
		} else {
			var data = eval("(" + responseData + ")");
			var selectBox = document.getElementById("ward");
			selectBox.length = 0;
			document.getElementById("ward").options[0] = new Option("--Select a Ward--", "");
			for (var i = 0; i < data.length; i++) {
				selectBox.options[i + 1] = new Option(data[(i)].name, data[(i)].id);
			}
		}
	}, failure:function (oResponse) {
		showServerError();
	}, timeout:30000, cache:false};
	YAHOO.util.Connect.asyncRequest("GET", sUrl, callback);
}
function updateComment() {
	var fileComments = document.getElementsByName("fileComment");
	for (var i = 0; i < fileComments.length; i++) {
		var fileComment = fileComments[i].value;
		if (fileComment !== "") {
			fileComments[i].value = fileComment + "#FILENAME#" + document.getElementsByName("fileName")[i].value;
		}
	}
}
function returnfileNumber(fileId, fileNo) {
	window.opener.document.getElementById("fileNo").value = fileNo;
	window.opener.document.getElementById("fileId").value = fileId;
	if (window.opener.fileUpdater != undefined) {
		window.opener.fileUpdater(fileId);
	}
	window.close();
}
function popnewwindow(url) {
	var newwindow = window.open(url, "name","width=850,height=600,resizable=yes,scrollbars=yes");
	if (window.focus) {
		newwindow.focus();
	}
}
function resetFields() {
	document.getElementById("fileNumber").value = "";
	document.getElementById("fileDateFrom").value = "";
	document.getElementById("fileDateTo").value = "";
	document.getElementById("departmentId").value = "";
	document.getElementById("zone").value = "";
	document.getElementById("ward").value = "";
	document.getElementById("fileReceivedDateFrom").value = "";
	document.getElementById("fileReceivedDateTo").value = "";
	document.getElementById("fileStatusId").value = "";
	document.getElementById("fileSourceId").value = "";
	document.getElementById("fileTypeId").value = "";
	document.getElementById("fileCategoryId").value = "";
	document.getElementById("fileSubCategoryId").value = "";
	document.getElementById("filePriorityId").value = "";
}
YAHOO.util.Event.addListener("fileDateFromBtn", "click", cal.show, cal, true);
YAHOO.util.Event.addListener("fileDateToBtn", "click", cal.show, cal, true);
YAHOO.util.Event.addListener("fileReceivedDateFromBtn", "click", cal.show, cal, true);
YAHOO.util.Event.addListener("fileReceivedDateToBtn", "click", cal.show, cal, true);

