/*#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------*/
var isEditable = false;	//Stores to true when record's radio button or Re-assign context menu item clicked 
var editableId = new HashTable({});//Stores State ID value from data table when record's radio button or Re-assign context menu item clicked
var owners = new HashTable({});//Stores Owner ID value from data table when record's radio button or Re-assign context menu item clicked
var owner = null;		//Stores current owner from data table when record's radio button or Re-assign context menu item clicked
var windAry1 = new Array();		//Keeps refernce to the opened Workflow Windows
var winCntAry1 = new Array(); 	//Keeps refernce to the opened Workflow Windows
var wincntr1 = 0; 				//Counter for opened Workflow Windows.
var isFrmCtxt = false;			//To recognize Reassign Dialogue box is open using Context Menu item or not.
var dftText = "Start typing..."
//Creats button for Reassign shown in the menu bar
var reassignBtn = new YAHOO.widget.Button({id:"reassignbtn", type:"push", label:"Re-assign", title:"Select a record for edit and click here to Re-assign", container:"admintoolbar"});
reassignBtn.on("click", showReassign);
var wait = new YAHOO.widget.Panel("wait",{ width:"240px",fixedcenter:true,close:false,draggable:false,zindex:4,modal:true,visible:false}); 
wait.setHeader("Loading, please wait..."); 
wait.setBody('<img src="/egi/images/loadbar.gif" />'); 
wait.render(document.body); 
//Shows Reassign Dialogue Window when user clicks Re-assign button or Re-assign contextmenu
function showReassign() {
	if (isEditable || isFrmCtxt) {
		isFrmCtxt = false;
		document.getElementById("uname").innerHTML = owner;
		reAssign.show();
	} else {
		msgDialogue.setHeader("Warning!");
		msgDialogue.setBody("Please select a record to Re-assign!");
		msgDialogue.cfg.setProperty("icon", YAHOO.widget.SimpleDialog.ICON_BLOCK);
		msgDialogue.show();
	}
}

function showServerError () {
	msgDialogue.setHeader("Error !");
	msgDialogue.setBody("Internal server error occurred !");
	msgDialogue.cfg.setProperty("icon", YAHOO.widget.SimpleDialog.ICON_BLOCK);
	msgDialogue.show();
}

//Handler for Re-assign Dialogue window's Cancel button. Which closes the Re-assign Dialogue window 
var handleCancel = function () {
	tblref.refreshView();
	this.cancel();
};

//Handler for Re-assign Dialogue window's Re-assign button. Which do reassigning Workflow
var handleSubmit = function () {
	var newOwner = null;
	if (document.getElementById("acCont4").style.display != "none") {
		newOwner = parseInt(document.getElementById("newOwner").value.split("-")[1]);
		document.getElementById("newOwner").value = "";
	} else {
		newOwner = document.getElementById("newOwner1").value;
		document.getElementById("newOwner1").value = ""; 
	}
	if (newOwner) {
		var sUrl = "/egi/workflow/workflowAdmin!reassignWFItem.action?wfType=" + document.getElementById("wfTypeValue").value + "&newOwner=" + newOwner + "&stateId=" + editableId.values();
		var callback = {success:function (oResponse) {
			var responseData = oResponse.responseText;
			reAssign.hide();
			document.getElementById('department').value = "";
			document.getElementById('designation').length = 0;
			document.getElementById('mandate1').style.display = "none";
			document.getElementById('mandate2').style.display = "none";
			document.getElementById("acCont4").style.display = "";
			document.getElementById("newOwner1").style.display = "none";
			isEditable = false;
			owner = null;
			editableId.clear();
			owners.clear();
			searchWfItems();
			if (responseData == "RE-ASSIGNED") {
				msgDialogue.setHeader("Info !");
				msgDialogue.setBody("Workflow Item has successfully reassigned.");
				msgDialogue.cfg.setProperty("icon", YAHOO.widget.SimpleDialog.ICON_INFO);
			} else {
				msgDialogue.setHeader("Error !");						
				msgDialogue.cfg.setProperty("icon", YAHOO.widget.SimpleDialog.ICON_BLOCK);
				if (responseData == "OWNER-SAME") {
					msgDialogue.setBody("The current owner and the reassigned owner can not be same.");
				} else {
					msgDialogue.setBody("Error occurred while reassigning Workflow Item.");
				}
			} 
			msgDialogue.show();
			
		}, failure:function (oResponse) {
			showServerError();
		}, timeout:30000, cache:false};
		YAHOO.util.Connect.asyncRequest("GET", sUrl, callback);
	} else {
		msgDialogue.setHeader("Error!");
		msgDialogue.setBody("Please select a User Name for reassignment!");
		msgDialogue.cfg.setProperty("icon", YAHOO.widget.SimpleDialog.ICON_BLOCK);
		msgDialogue.show();
	}
};

//Button for Re-assign Dialogue Window
var reAssignBtns = [{text:"Re-assign", handler:handleSubmit, isDefault:true}, {text:"Cancel", handler:handleCancel}];

//Creates Re-assign Dialogue Window
var reAssign = new YAHOO.widget.Dialog("reassignDialogue", {zIndex:9051, fixedcenter:true,close:false, buttons:reAssignBtns, modal:true, visible:false, draggable:true});
reAssign.render();
reAssign.hide();	

//Creates common Message Dialogue box
var msgDialogue = new YAHOO.widget.SimpleDialog("dlg", {width:"30em", height:"100px", zIndex:9051, effect:{effect:YAHOO.widget.ContainerEffect.FADE, duration:0.25}, fixedcenter:true, modal:true, visible:false, draggable:true});
msgDialogue.render(document.body);
msgDialogue.hide();

//Creates autocompletes for the Workflow Admin screen, To populate User, Workflow Type			
var autoComplete = function () {
    // Use an XHRDataSource
	var sUrl1 = "/egi/workflow/workflowAdmin!populateUser.action";
	var sUrl2 = "/egi/workflow/workflowAdmin!populateDocumentType.action";
	var oDS1 = new YAHOO.util.XHRDataSource(sUrl1);
	var oDS2 = new YAHOO.util.XHRDataSource(sUrl2);
    
     // Set the responseType
	oDS1.responseType = oDS2.responseType = YAHOO.util.XHRDataSource.TYPE_TEXT;
    // Define the schema of the delimited results
	oDS1.responseSchema = oDS2.responseSchema = {recordDelim:"\n", fieldDelim:"\t"};
    // Enable caching
	oDS1.maxCacheEntries = oDS2.maxCacheEntries = 5;

    // Instantiate the AutoComplete
	var oAC1 = new YAHOO.widget.AutoComplete("wfType", "docTypeAC", oDS2);
	var oAC2 = new YAHOO.widget.AutoComplete("owner", "pendingWithAC", oDS1);
	var oAC3 = new YAHOO.widget.AutoComplete("sender", "initByAC", oDS1);
	var oAC4 = new YAHOO.widget.AutoComplete("newOwner", "newOwnerAC", oDS1);
	oAC1.useShadow = oAC2.useShadow = oAC3.useShadow = oAC4.useShadow = true;
	oAC1.allowBrowserAutocomplete = oAC2.allowBrowserAutocomplete = oAC3.allowBrowserAutocomplete = oAC4.allowBrowserAutocomplete = false;
	return {oDS1:oDS1, oDS2:oDS2, oAC1:oAC1, oAC2:oAC2, oAC3:oAC3, oAC4:oAC4};
}();


//Populate Workflow Types combobox for the selected Document Type (Workflow Type)
function populateWorkflowState() {
	var value = document.getElementById("wfType").value;
	var sUrl = "/egi/workflow/workflowAdmin!populateWorkflowState.action?wfType=" + value.split("/")[1];
	var callback = {success:function (oResponse) {
		var responseData = oResponse.responseText;
		var data = responseData.split("\n");
		var selectBox = document.getElementById("wfState");
		selectBox.length = 0;
		selectBox.options[0] = new Option("--Select a Workflow State--", "");
		for (var i = 1; i < data.length; i++) {
			selectBox.options[i] = new Option(data[(i - 1)], data[(i - 1)]);
		}
	}, failure:function (oResponse) {
		showServerError();
	}, timeout:30000, cache:false};
	YAHOO.util.Connect.asyncRequest("GET", sUrl, callback);
}

function populateWFSearchFields() {
	var value = document.getElementById("wfType").value;
	var sUrl = "/egi/workflow/workflowAdmin!populateWFItemSearchFields.action?wfType=" + value.split("/")[1];
	var callback = {success:function (oResponse) {
		var responseData = oResponse.responseText;
		var data = responseData.split("\n");
		if (data[0].length > 1) {
			var selectBox = document.getElementById("searchField");
			selectBox.length = 0;
			selectBox.options[0] = new Option("--Select an Identifier--", "");
			for (var i = 1; i < data.length; i++) {
				var dataAry = data[(i - 1)].split("-");
				selectBox.options[i] = new Option(dataAry[0], dataAry[1]);
			}
			document.getElementById('idearchrow').style.display = document.all ? 'block' : 'table-row';
		} else {
			document.getElementById('idearchrow').style.display = 'none';
		}		
	}, failure:function (oResponse) {
		showServerError();
	}, timeout:30000, cache:false};
	YAHOO.util.Connect.asyncRequest("GET", sUrl, callback);
}

function setSearchOp(select) {
	if (select.value == "") {
		document.getElementById('searchOp').innerHTML = "";
		document.getElementById('opbtwn').style.display = "none";
		document.getElementById('identifier').style.display = "none";
		document.getElementById('identifier2').style.display = "none";
		document.getElementById('identifier2Btn').style.display = "none";
		document.getElementById('identifierBtn').style.display = "none";
	} else {
		var op = select.options[select.selectedIndex].value.split("*")[2];
		document.getElementById('searchOp').innerHTML = "&nbsp;"+(op)+"&nbsp;";
		document.getElementById('identifier').style.display = "inline";
		document.getElementById('identifier').value = "";
		document.getElementById('identifier2').value = "";
		if (op === "Between") {
			document.getElementById('opbtwn').style.display = "inline";
			document.getElementById('identifier2').style.display = "inline";
			if (select.options[select.selectedIndex].value.split("*")[3] === "Date") {
				document.getElementById('identifier2Btn').style.display = "inline";
				document.getElementById('identifierBtn').style.display = "inline";
			} else {
				document.getElementById('identifier2Btn').style.display = "none";
				document.getElementById('identifierBtn').style.display = "none";
			}
		} else  {
			document.getElementById('opbtwn').style.display = "none";
			document.getElementById('identifier2').style.display = "none";
			document.getElementById('identifier').style.display = "inline";
			document.getElementById('identifier2Btn').style.display = "none";
			document.getElementById('identifierBtn').style.display = "none";
		}
	}
}
//Populates Designation for the department selected
function populateDesignation(deptObj) {
	var value = deptObj.value;
	if (value == "") {
		document.getElementById("designation").length = 0;
		document.getElementById("mandate1").style.display = "none";
		document.getElementById("mandate2").style.display = "none";
		document.getElementById("acCont4").style.display = "";
		document.getElementById("newOwner1").style.display = "none";
		return;
	} else {
		document.getElementById("mandate1").style.display = "inline";
		document.getElementById("mandate2").style.display = "inline";
		document.getElementById("acCont4").style.display = "none";
		document.getElementById("newOwner1").style.display = "block";
	}
	var sUrl = "/egi/workflow/workflowAdmin!populateDesignation.action?deptId=" + value;
	var callback = {success:function (oResponse) {
		var responseData = oResponse.responseText;
		if (responseData == "ERROR") {
			document.getElementById("designation").length = 0;
		} else {
			var data = eval("(" + responseData + ")");
			var selectBox = document.getElementById("designation");
			selectBox.length = 0;
			selectBox.options[0] = new Option("--Select a Designation--", "");
			for (var i = 0; i < data.length; i++) {
				selectBox.options[i+1] = new Option(data[(i)].name, data[(i)].id);
			}
		}
	}, failure:function (oResponse) {
		showServerError();
	}, timeout:30000, cache:false};
	YAHOO.util.Connect.asyncRequest("GET", sUrl, callback);
}

function populateUser(obj) {
	var deptId = document.getElementById("department").value;
	var desigId = obj.value;
	if (deptId == "" || desigId=="") {
		return;
	}
	var sUrl = "/egi/workflow/workflowAdmin!populateUsersByDeptAndDesig.action?deptId="+deptId+"&desigId="+desigId;
	var callback = {success:function (oResponse) {
		var responseData = oResponse.responseText;
		if (responseData == "error") {
			document.getElementById("newOwner1").length = 0;
		} else {
			var data = eval("(" + responseData + ")");
			var selectBox = document.getElementById("newOwner1");
			selectBox.length = 0;
			selectBox.options[0] = new Option("----Select a User----", "");
			for (var i = 0; i < data.length; i++) {
				selectBox.options[i+1] = new Option(data[(i)].name, data[(i)].id);
			}
		}
	}, failure:function (oResponse) {
		showServerError();
	}, timeout:30000, cache:false};
	YAHOO.util.Connect.asyncRequest("GET", sUrl, callback);
}
var tblref = null;
//Populates all Workflow Item as datatable when Search button is clicked.
function searchWfItems() {
	if (document.getElementById("wfType").value === "") {
		msgDialogue.setHeader("Error!");
		msgDialogue.setBody("Please enter a Document Type !");
		msgDialogue.cfg.setProperty("icon", YAHOO.widget.SimpleDialog.ICON_BLOCK);
		msgDialogue.show();
		return;
	}
	wait.show();
	try {
		var wfTypeValue = document.getElementById("wfTypeValue").value = document.getElementById("wfType").value.split("/")[1];
		var args = "wfType=" + wfTypeValue;
		if (document.getElementById("wfState").value != "") {
			args = args + "&wfState=" + document.getElementById("wfState").value;
		}
		if (document.getElementById("owner").value != "") {
			args = args + "&owner=" + parseInt(document.getElementById("owner").value.split("-")[1]);
		}
		if (document.getElementById("sender").value != "") {
			args = args + "&sender=" + parseInt(document.getElementById("sender").value.split("-")[1]);
		}
		if (document.getElementById("fromDate").value != "") {
			args = args + "&fromDate=" + document.getElementById("fromDate").value;
		}
		if (document.getElementById("toDate").value != "") {
			args = args + "&toDate=" + document.getElementById("toDate").value;
		}
		
		if (document.getElementById("searchField").value != "" && document.getElementById("identifier").value != "") {
			var searchOpt = document.getElementById("searchField").options[document.getElementById("searchField").selectedIndex].value.split("*");
			args = args + "&searchOp="+searchOpt[0]+"&searchField="+searchOpt[1];
			
			if (searchOpt[0] == "between" ) {
				if (document.getElementById("identifier").value === "" || document.getElementById("identifier2").value === "") {
					alert("Identifier Date range input values can not be empty..!")				
				} else {
					args = args+"&Identifier="+ document.getElementById("identifier").value+" and "+document.getElementById("identifier2").value;
				}
			} else {
				args = args+"&Identifier="+ document.getElementById("identifier").value;
			}
		}
	}
	catch (e) {
		msgDialogue.setHeader("Error!");
		msgDialogue.setBody("Error in input values, Please try to use Autocomplete to fill values !");
		msgDialogue.cfg.setProperty("icon", YAHOO.widget.SimpleDialog.ICON_BLOCK);
		msgDialogue.show();
	}
	var sUrl = "/egi/workflow/workflowAdmin!searchWfItems.action?" + args + "&rnd=" + Math.random();
	var callback = {success:function (oResponse) {
		var responseData = oResponse.responseText;
		if (responseData == "" || responseData == "error") {
			document.getElementById("searchrslt").style.display = "none";
			document.getElementById("msg").style.display = "block";
			document.getElementById("msgSpace").innerHTML = "No Workflow Item found for the given criteria";
			wait.hide();
			return;
		}
		populateSearchData(responseData);
		document.getElementById("searchresult").lastChild.style.width = "100%";
		document.getElementById("msg").style.display = "none";
		document.getElementById("searchrslt").style.display = "block";
		wait.hide();
	}, failure:function (oResponse) {
		showServerError();
		wait.hide();
	}, timeout:300000, cache:false};
	YAHOO.util.Connect.asyncRequest("GET", sUrl, callback);
	var populateSearchData = function (data) {
		var searchResultColumns = [{key:"Id", parser:"number", sortable:false, hidden:true}, {key:"Edit", formatter:"checkbox"}, {key:"Date", sortable:true, parser:"date", formatter:"date", resizeable:true}, {key:"Sender", sortable:true, resizeable:true, parser:"string"}, {key:"Owner", sortable:true, resizeable:true, parser:"string"}, {key:"Task", sortable:true, resizeable:true, parser:"string"}, {key:"Status", sortable:true, resizeable:true, parser:"string"}, {key:"Details", sortable:true, resizeable:true, parser:"string"}, {key:"Link", hidden:true}];
		var searchDataSource = new YAHOO.util.DataSource(eval("(" + data + ")"));
		searchDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
		searchDataSource.responseSchema = {fields:["Id", "Edit", "Date", "Owner", "Sender", "Task", "Status", "Details", "Link"]};
		var oConfigs = {
			paginator: new YAHOO.widget.Paginator({rowsPerPage: 15,alwaysVisible : false,totalRecords : searchDataSource.length}),
			 draggableColumns:true
    	};
		var searchDataTable = new YAHOO.widget.DataTable("searchresult", searchResultColumns, searchDataSource, oConfigs);
		tblref = searchDataTable;
		searchDataTable.subscribe("rowMouseoverEvent", searchDataTable.onEventHighlightRow);
		searchDataTable.subscribe("rowMouseoutEvent", searchDataTable.onEventUnhighlightRow);
		searchDataTable.subscribe("rowClickEvent", searchDataTable.onEventSelectRow);
		searchDataTable.subscribe("checkboxClickEvent", function (oArgs) {
			var elCheckbox = oArgs.target;
			var oRecord = this.getRecord(elCheckbox);
			var editId = oRecord.getData("Id").split("#DILIM#")[0];				
			if(owners.isEmpty() || owners.hasItem(editId) || oRecord.getData("Owner") == owner) {
				owner = oRecord.getData("Owner");
				var newValue = elCheckbox.checked;
				if(newValue) {
					editableId.put(editId,editId);
					owners.put(editId,owner);					
				} else {
					editableId.removeItem(editId); 
					owners.removeItem(editId);
				}
				var column = this.getColumn(elCheckbox);
				oRecord.setData(column.key,newValue);
				if(editableId.isEmpty()) {
					isEditable = false;
				} else {
					isEditable = true;
				}
				
			} else {
				elCheckbox.checked = false;
				msgDialogue.setHeader("Warning!");
				msgDialogue.setBody("Please select record's of same Owner to Re-assign!");
				msgDialogue.cfg.setProperty("icon", YAHOO.widget.SimpleDialog.ICON_BLOCK);
				msgDialogue.show();
			}
			
			
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
						isFrmCtxt = true;
						var oRecord = p_myDataTable.getRecord(elRow);
						var editId = oRecord.getData("Id").split("#DILIM#")[0];
						editableId.clear();
						owners.clear();
						editableId.put(editId,editId);	
						owner = oRecord.getData("Owner");
						showReassign();
						break;
					  case 1:
						openWFWindow(elRow);
						break;
					}
				}
			}
		};
		var wfContextMenu = new YAHOO.widget.ContextMenu("wfContextMenu" + Math.random(), {trigger:searchDataTable.getTbodyEl()});
		wfContextMenu.addItem("Re-assign");
		wfContextMenu.addItem("Show Task");
		wfContextMenu.render("searchrslt");
		wfContextMenu.clickEvent.subscribe(onContextMenuClick, searchDataTable);
		document.getElementById("searchrslt").style.display = "block";
		return {oDS:searchDataSource, oDT:searchDataTable};
	};
}

//Opens Workflow Item links when user select from context menu
function openWFWindow(elRow) {
	var url = elRow.lastChild.firstChild.innerText ? elRow.lastChild.firstChild.innerText : elRow.lastChild.firstChild.textContent;
	var id = "adminWin" + (elRow.firstChild.firstChild.innerHTML.split("#DILIM#")[1]);
	if (url !== "") {
		if (windAry1[id] && !windAry1[id].closed) {
			windAry1[id].focus();
		} else {
			winCntAry1[wincntr1++] = windAry1[id] = window.open(url, id, "width=" + (window.screen.width - 200) + ",height=" + (window.screen.height - 100) + ",top=0,left=0,resizable=yes,scrollbars=yes");
			window.setTimeout(disableChildWin, 1500);
		}
	} else {
		msgDialogue.setHeader("Error!");
		msgDialogue.setBody("Could not open this work, Link is missing !");
		msgDialogue.cfg.setProperty("icon", YAHOO.widget.SimpleDialog.ICON_BLOCK);
		msgDialogue.show();
	}
}

//disable all input elements in the Workflow item Window opened
function disableChildWin() {
	var win = winCntAry1[wincntr1 - 1];
	var form = win.document.getElementsByTagName("form")[0];
	var length = form.elements.length;
	for (i = 0; i < length; i++) {
		form.elements[i].disabled = true;		
	}
}

//set the selected date from calendar to appropriate date field
var dateCompId = "";
function setSelectedDate(type, args, obj) {
	var datedata = args[0][0];
	var year = datedata[0];
	var month = datedata[1];
	var day = datedata[2];
	var fieldId = dateCompId.split("Btn")[0]; 
	document.getElementById(fieldId).value = day + "/" + month + "/" + year;	
	obj.hide();
}

//Creates Calendar object
var cal = new YAHOO.widget.Calendar("calender", {title:"Choose Date:", close:true, navigator:true});
cal.render();
cal.hide();
YAHOO.util.Event.addListener("fromDateBtn", "click", cal.show, cal, true);
YAHOO.util.Event.addListener("toDateBtn", "click", cal.show, cal, true);
YAHOO.util.Event.addListener("identifierBtn", "click", cal.show, cal, true);
YAHOO.util.Event.addListener("identifier2Btn", "click", cal.show, cal, true);
cal.selectEvent.subscribe(setSelectedDate, cal, true);

//on click of Calendar img button, open the Calendar
function showCalendar(obj, eve) {
	dateCompId = obj.id;
	var event = window.event ? window.event : eve;
	var posX = event.clientX ? event.clientX : event.pageX;
	var posY = event.clientY ? event.clientY : event.pageY;
	document.getElementById("calender").style.top = posY;
	document.getElementById("calender").style.left = posX;
}

function closeWin() {
	window.close();
}
