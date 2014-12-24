 
var windAry3 = new Array();		//Keeps refernce to the opened Workflow Windows
var winCntAry3 = new Array(); 	//Keeps refernce to the opened Workflow Windows
var wincntr3 = 0; 				//Counter for opened Workflow Windows.
var winName="notification";
try {
var msgDialogue = new YAHOO.widget.SimpleDialog("dlg", {width:"30em", height:"100px", zIndex:9051, effect:{effect:YAHOO.widget.ContainerEffect.FADE, duration:0.25}, fixedcenter:true, modal:true, visible:false, draggable:true});
msgDialogue.render(document.body);
msgDialogue.hide();
} catch(e) {}

/*
 * On Change of department this function will be called
 */
function checkBeforeSubmit() {
	var msg = "";
	if (document.getElementById("fileNumber").value === "") {
		msg = "File Number can not be empty\n";
	}
	if (document.getElementById("fileDate").value === "") {
		msg = msg + "File Date can not be empty\n";
	}
	if (document.getElementById("fileCategory").value === "") {
		msg = msg + "File Category can not be empty\n";
	}
	if (document.getElementById("fileHeading").value === "") {
		msg = msg + "File Heading can not be empty\n";
	}
	if (document.getElementById("userSource").value === "") {
		msg = msg + "Received From can not be empty\n";
	}
	if (document.getElementById("senderName").value === "") {
		msg = msg + "Sender Name can not be empty\n";
	}
	if (document.getElementById("notificationGroupsIds").value === "") {
		msg = msg + "Please select at least one Receiver Group\n";
	}
	if (msg != "") {
		alert(msg);
		document.getElementById("loading").style.display = "none";
		return false;
	} else {
		return true;
	}
}

function confirmDelete(deleteAll) {
	if (deleteAll == "true") {
		if (window.confirm("Would you like to delete this File Notification?")) {
			document.getElementById("loading").style.display = "block";
			return true;
		}
	} else {
		if (window.confirm("Would you like to delete this File Notification from your Inbox ?")) {
			document.getElementById("loading").style.display = "block";
			return true;
		}
	}
	
	return false;
}

function hideElement(id) {
	document.getElementById(id).style.display = "none";
}


//Opens Workflow Item links when user select from context menu
function openWindow(recordId) {
	var windowName = winName+recordId;
	if (recordId !== null) {
		var url = "../dms/fileNotification!viewFileNotification.action?id="+recordId;
		if (windAry3[windowName] && !windAry3[windowName].closed) {
			windAry3[windowName].focus();
		} else {
			winCntAry3[wincntr3++] = windAry3[windowName] = window.open(url, windowName, "width=" + (window.screen.width - 200) + ",height=" + (window.screen.height - 100) + ",top=0,left=0,resizable=yes,scrollbars=yes");
		}
	} else {
		msgDialogue.setHeader("Info!");
		msgDialogue.setBody("Please select a record to view !");
		msgDialogue.cfg.setProperty("icon", YAHOO.widget.SimpleDialog.ICON_INFO);
		msgDialogue.show();
	}
}
 
try {
YAHOO.util.Event.addListener("startDateBtn", "click", cal.show, cal, true);
YAHOO.util.Event.addListener("endDateBtn", "click", cal.show, cal, true);
} catch (e){}
try {
window.opener.egovInbox.refresh();
} catch (e){}