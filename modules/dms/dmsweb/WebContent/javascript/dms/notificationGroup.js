
var searchDataTable = null;
function listPositions(data, isView) {
	var searchResultColumns = null;
	if (isView) {
		searchResultColumns = [{key:"Position", label:"Positions", abbr:"Position Names", sortable:true, resizeable:true, parser:"string"}, {key:"id", parser:"number", sortable:true, hidden:true}];
	} else {
		var deleteFunction = function (el, oRecord, oColumn, oData) {
			var img = new Image();
			img.src = "../images/delete.png";
			el.appendChild(img);
			img.onclick = function () {
				if (confirm("Do you want to delete this position from the group ?")) {
					var obj = document.getElementById("positions");
					for (var i = obj.options.length - 1; i >= 0; i--) {
						if (obj.options[i].value == oRecord.getData("id")) {
							obj.remove(i);
						}
					}
					searchDataTable.deleteRow(el);
				}
			};
		};
		searchResultColumns = [{key:"Position", label:"Positions", abbr:"Position Names", sortable:true, resizeable:true, parser:"string"}, {key:"Delete", label:"Delete", formatter:deleteFunction}, {key:"id", parser:"number", sortable:true, hidden:true}];
	}
	var searchDataSource = new YAHOO.util.DataSource(eval("(" + data + ")"));
	searchDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
	if (isView) {
		searchDataSource.responseSchema = {fields:["id", "Position"]};
	} else {
		searchDataSource.responseSchema = {fields:["id", "Position", "Delete"]};
	}
	var oConfigs = {paginator:new YAHOO.widget.Paginator({rowsPerPage:15, alwaysVisible:false, totalRecords:searchDataSource.length}), draggableColumns:true};
	searchDataTable = new YAHOO.widget.DataTable("searchresult", searchResultColumns, searchDataSource, oConfigs);
	searchDataTable.subscribe("rowMouseoverEvent", searchDataTable.onEventHighlightRow);
	searchDataTable.subscribe("rowMouseoutEvent", searchDataTable.onEventUnhighlightRow);
	searchDataTable.subscribe("rowClickEvent", searchDataTable.onEventSelectRow);
	return {oDS:searchDataSource, oDT:searchDataTable};
}
function loadWards(obj) {
	var zoneValue = obj.value;
	if (zoneValue == "") {
		return;
	}
	var sUrl = "../dms/notificationGroup!getSecondLevelBndries.action?";
	for (var i = 0; i < obj.options.length; i++) {
		if (obj.options[i].selected) {
			sUrl = sUrl + "firstLevelIds=" + obj.options[i].value + "&";
		}
	}
	var ward = document.getElementById("secondLevelBndry");
	var callback = {success:function (oResponse) {
		var responseData = oResponse.responseText;
		ward.length = 0;
		ward.options[0] = new Option("--Select Ward--", "");
		if (responseData != "error") {
			var data = eval("(" + responseData + ")");
			ward.length = 0;
			ward.options[0] = new Option("--Select Ward--", "");
			for (var i = 0; i < data.length; i++) {
				ward.options[i + 1] = new Option(data[(i)].name, data[(i)].id);
			}
		}
	}, failure:function (oResponse) {
	}, timeout:30000, cache:false};
	YAHOO.util.Connect.asyncRequest("GET", sUrl, callback);
}
function loadDepartments(obj) {
	var wardValue = obj.value;
	if (wardValue == "") {
		return;
	}
	var sUrl = "../dms/notificationGroup!getDepartments.action?";
	innerHTML;
	var dept = document.getElementById("departments");
	var callback = {success:function (oResponse) {
		var responseData = oResponse.responseText;
		dept.length = 0;
		dept.options[0] = new Option("--Select Department--", "");
		if (responseData != "error") {
			var data = eval("(" + responseData + ")");
			dept.length = 0;
			dept.options[0] = new Option("--Select Department--", "");
			for (var i = 0; i < data.length; i++) {
				dept.options[i + 1] = new Option(data[(i)].name, data[(i)].id);
			}
		}
	}, failure:function (oResponse) {
	}, timeout:30000, cache:false};
	YAHOO.util.Connect.asyncRequest("GET", sUrl, callback);
}
function loadDesignations(obj) {
	var deptValue = obj.value;
	if (deptValue == "") {
		return;
	}
	var sUrl = "../dms/notificationGroup!getDesignations.action?";
	for (var i = 0; i < obj.options.length; i++) {
		if (obj.options[i].selected) {
			sUrl = sUrl + "departmentIds=" + obj.options[i].value + "&";
		}
	}
	var desig = document.getElementById("designations");
	var callback = {success:function (oResponse) {
		var responseData = oResponse.responseText;
		desig.length = 0;
		desig.options[0] = new Option("--Select Designation--", "");
		if (responseData != "error") {
			var data = eval("(" + responseData + ")");
			desig.length = 0;
			desig.options[0] = new Option("--Select Designation--", "");
			for (var i = 0; i < data.length; i++) {
				desig.options[i + 1] = new Option(data[(i)].name, data[(i)].id);
			}
		}
	}, failure:function (oResponse) {
	}, timeout:30000, cache:false};
	YAHOO.util.Connect.asyncRequest("GET", sUrl, callback);
}
function loadPositions() {
	var sUrl = "../dms/notificationGroup!getPositions.action?";
	var obj = document.getElementById("designations");
	for (var i = 0; i < obj.options.length; i++) {
		if (obj.options[i].selected && obj.options[i].value !== "") {
			sUrl = sUrl + "designationIds=" + obj.options[i].value + "&";
		}
	}
	obj = document.getElementById("secondLevelBndry");
	for (var i = 0; i < obj.options.length; i++) {
		if (obj.options[i].selected && obj.options[i].value !== "") {
			sUrl = sUrl + "secondLevelIds=" + obj.options[i].value + "&";
		}
	}
	obj = document.getElementById("departments");
	for (var i = 0; i < obj.options.length; i++) {
		if (obj.options[i].selected && obj.options[i].value != "") {
			sUrl = sUrl + "departmentIds=" + obj.options[i].value + "&";
		}
	}
	var position = null;
	if (document.getElementsByName("members").length > 1) {
		position = document.getElementsByName("members")[1];
	} else {
		position = document.getElementById("positions");
	}
	var callback = {success:function (oResponse) {
		var responseData = oResponse.responseText;
		position.length = 0;
		position.options[0] = new Option("--Select Position--", "");
		if (responseData != "error") {
			var data = eval("(" + responseData + ")");
			position.length = 0;
			position.options[0] = new Option("--Select Position--", "");
			for (var i = 0; i < data.length; i++) {
				position.options[i + 1] = new Option(data[(i)].name, data[(i)].id);
			}
		}
	}, failure:function (oResponse) {
	}, timeout:30000, cache:false};
	YAHOO.util.Connect.asyncRequest("GET", sUrl, callback);
}
function checkGroupNameExist(obj) {
	if (obj.value == "") {
		return;
	}
	if (document.getElementById("editGrpName")) {
		if (obj.value === document.getElementById("editGrpName").innerHTML) {
			return;
		}
	}
	var sUrl = "../dms/notificationGroup!checkGroupNameExist.action?groupName=" + obj.value;
	var callback = {success:function (oResponse) {
		var responseData = oResponse.responseText;
		if (responseData == "exist") {
			alert("A Notification Group already exist with the same name.");
			obj.value = "";
		}
	}, failure:function (oResponse) {
	}, timeout:30000, cache:false};
	YAHOO.util.Connect.asyncRequest("GET", sUrl, callback);
}
function submits() {
	document.forms[0].action = "notificationGroup!edit.action";
	document.forms[0].submit();
}
function editSubmit() {
	var obj = document.getElementById("positions");
	for (var i = 0; i < obj.options.length; i++) {
		obj.options[i].selected = true;
	}
	document.forms[0].action = "notificationGroup!save.action";
	document.forms[0].submit();
}
function addPositions() {
	if (document.getElementById("posSearch").style.display == "none") {
		document.getElementById("posSearch").style.display = "";
		document.getElementById("usrinfo").style.display = "";
	} else {
		var obj = document.getElementsByName("members");
		for (var i = 0; i < obj[1].options.length; i++) {
			if (obj[1].options[i].selected) {
				if (obj[1].options[i].value != "") {
					obj[0].options[obj[0].options.length] = new Option(obj[1].options[i].text, obj[1].options[i].value);
					var data = new Object();
					data.Position = obj[1].options[i].text;
					data.id = obj[1].options[i].value;
					searchDataTable.addRow(data, 0);
				}
			}
		}
		document.getElementById("posSearch").style.display = "none";
		document.getElementById("usrinfo").style.display = "none";
	}
}
try {
	YAHOO.util.Event.addListener("dateBtn", "click", cal.show, cal, true);
}
catch (e) {
}

