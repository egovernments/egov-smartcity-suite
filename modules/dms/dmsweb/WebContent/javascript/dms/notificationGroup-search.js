
var windAry2 = new Array();		//Keeps refernce to the opened Workflow Windows
var winCntAry2 = new Array(); 	//Keeps refernce to the opened Workflow Windows
var wincntr2 = 0; 				//Counter for opened Workflow Windows.
var link = null;
var msgDialogue = new YAHOO.widget.SimpleDialog("dlg", {width:"30em", height:"100px", zIndex:9051, effect:{effect:YAHOO.widget.ContainerEffect.FADE, duration:0.25}, fixedcenter:true, modal:true, visible:false, draggable:true});
msgDialogue.render(document.body);
msgDialogue.hide();
var autoComplete = function () {
    // Use an XHRDataSource
	var sUrl1 = "../dms/notificationGroup!populateGroupName.action";
	var oDS1 = new YAHOO.util.XHRDataSource(sUrl1);
     // Set the responseType
	oDS1.responseType = YAHOO.util.XHRDataSource.TYPE_TEXT;
    // Define the schema of the delimited results
	oDS1.responseSchema = {recordDelim:"\n", fieldDelim:"\t"};
    // Enable caching
	oDS1.maxCacheEntries = 5;
    // Instantiate the AutoComplete
	var oAC1 = new YAHOO.widget.AutoComplete("groupName", "groupNameAC", oDS1);
	oAC1.useShadow = true;
	oAC1.allowBrowserAutocomplete = false;
	return {oDS1:oDS1, oAC1:oAC1};
}();
function searchNotificationGroup() {
	var args = "";
	if (document.getElementById("groupName").value != "") {
		args = args + "groupName=" + document.getElementById("groupName").value;
	}
	if (document.getElementById("active").value != "") {
		args = args + "&active=" + document.getElementById("active").value;
	}
	args += "&rnd=" + Math.random();
	if (document.getElementById("effDtFrom").value != "") {
		args = args + "&effDtFrom=" + document.getElementById("effDtFrom").value;
	}
	if (document.getElementById("effDtTo").value != "") {
		args = args + "&effDtTo=" + document.getElementById("effDtTo").value;
	}
	var sUrl = "../dms/notificationGroup!searchNotificationGroup.action?" + args;
	var callback = {success:function (oResponse) {
		var responseData = oResponse.responseText;
		if (responseData == "" || responseData == "error") {
			document.getElementById("searchrslt").style.display = "none";
			msgDialogue.setHeader("Error!");
			msgDialogue.setBody("No Notification Group found for the given criteria!");
			msgDialogue.cfg.setProperty("icon", YAHOO.widget.SimpleDialog.ICON_BLOCK);
			msgDialogue.show();
			return;
		}
		populateSearchData(responseData);
		document.getElementById("searchresult").lastChild.style.width = "100%";
		document.getElementById("searchrslt").style.display = "block";
	}, failure:function (oResponse) {
		msgDialogue.setHeader("Error!");
		msgDialogue.setBody("Server error, Failed to get the search result !");
		msgDialogue.cfg.setProperty("icon", YAHOO.widget.SimpleDialog.ICON_BLOCK);
		msgDialogue.show();
	}, timeout:30000, cache:false};
	YAHOO.util.Connect.asyncRequest("GET", sUrl, callback);
	var populateSearchData = function (data) {
		var searchResultColumns = 
		[{key:"radio", label:"Select" ,formatter:"radio",resizeable:true},
		{key:"Id", parser:"number", sortable:true, hidden:true, resizeable:true}, 
		{key:"Name", label:"Group Name", abbr:"Notification Group Name", sortable:true, resizeable:true, parser:"string"}, 
		{key:"Desc", label:"Description", abbr:"Notification Group Description", sortable:true, resizeable:true, parser:"string"}, 
		{key:"Effective", label:"Effective Date", abbr:"Notification Group Effective Date", sortable:true, parser:"date", formatter:"date", resizeable:true}, 
		{key:"Active", sortable:true, resizeable:true, parser:"string"}, 
		{key:"Link", hidden:true, sortable:true, resizeable:true}];
		var searchDataSource = new YAHOO.util.DataSource(eval("(" + data + ")"));
		searchDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
		searchDataSource.responseSchema = {fields:["Id", "Name", "Desc", "Effective", "Active", "Link"]};
		var oConfigs = {paginator:new YAHOO.widget.Paginator({rowsPerPage:15, alwaysVisible:false, totalRecords:searchDataSource.length}), draggableColumns:true};
		var searchDataTable = new YAHOO.widget.DataTable("searchresult", searchResultColumns, searchDataSource, oConfigs);
		searchDataTable.subscribe("rowMouseoverEvent", searchDataTable.onEventHighlightRow);
		searchDataTable.subscribe("rowMouseoutEvent", searchDataTable.onEventUnhighlightRow);
		searchDataTable.subscribe("rowClickEvent", searchDataTable.onEventSelectRow);
		searchDataTable.subscribe("radioClickEvent", function(oArgs){ 
								var elRadio = oArgs.target;
								var oRecord = this.getRecord(elRadio); 
								oRecord.setData("radio",true); 
								link = oRecord.getData("Link");
					});
		
		var onContextMenuClick = function (p_sType, p_aArgs, p_myDataTable) {
			var task = p_aArgs[1];
			if (task) {
				// Extract which TR element triggered the context menu 
				var elRow = this.contextEventTarget;
				elRow = p_myDataTable.getTrEl(elRow);
				if (elRow) {
					switch (task.index) {
					  case 0:
						openWindow(elRow, "view");
						break;
					  case 1:
						openWindow(elRow, "edit");
						break;
					  case 2:
						openWindow(elRow, "delete");
						break;
					}
				}
			}
		};
		var wfContextMenu = new YAHOO.widget.ContextMenu("wfContextMenu" + Math.random(), {trigger:searchDataTable.getTbodyEl()});
		wfContextMenu.addItem("View");
		wfContextMenu.addItem("Edit");
		wfContextMenu.addItem("Delete");
		wfContextMenu.render("searchrslt");
		wfContextMenu.clickEvent.subscribe(onContextMenuClick, searchDataTable);
		document.getElementById("searchrslt").style.display = "block";
		return {oDS:searchDataSource, oDT:searchDataTable};
	};
}

//Opens Workflow Item links when user select from context menu
function openWindow(elRow, mode) {
	var url = elRow.lastChild.firstChild.innerText ? elRow.lastChild.firstChild.innerText : elRow.lastChild.firstChild.textContent;	
	if (url !== "") {
		url = url.replace("##", mode);
		var id = "notifyWin"+url.substr((url.lastIndexOf("="))+1,url.length);
		if (windAry2[id] && !windAry2[id].closed) {
			windAry2[id].focus();
		} else {
		
			winCntAry2[(wincntr2)++] = windAry2[id] = window.open(url,id, "width=" + (window.screen.width - 200) + ",height=" + (window.screen.height - 100) + ",top=0,left=0,resizable=yes,scrollbars=yes");
		}
	} else {
		msgDialogue.setHeader("Error!");
		msgDialogue.setBody("Could not open, Link is missing !");
		msgDialogue.cfg.setProperty("icon", YAHOO.widget.SimpleDialog.ICON_BLOCK);
		msgDialogue.show();
	}
}

function openNotification(mode) {
	var url = link;
	if (url != null && url !== "") {
		url = url.replace("##", mode);
		var id = "notifyWin"+url.substr((url.lastIndexOf("="))+1,url.length);
		if (windAry2[id] && !windAry2[id].closed) {
			windAry2[id].focus();
		} else {
			winCntAry2[wincntr2++] = windAry2[id] = window.open(url,id,"width=" + (window.screen.width - 200) + ",height=" + (window.screen.height - 100) + ",top=0,left=0,resizable=yes,scrollbars=yes");			
		}
	} else {
		msgDialogue.setHeader("Error");
		msgDialogue.setBody("Please select a row!");
		msgDialogue.cfg.setProperty("icon", YAHOO.widget.SimpleDialog.ICON_BLOCK);
		msgDialogue.show();
	}
}

YAHOO.util.Event.addListener("effDtFromBtn", "click", cal.show, cal, true);
YAHOO.util.Event.addListener("effDtToBtn", "click", cal.show, cal, true);

