//Populates Designation for the department selected
var loading = new Image(16, 16);
loading.src = "../images/loading.gif";
function getFileCommentHistory(fileNumber, fileName) {
	var sUrl = "/egi/dms/fileManagement!getFileCommentHistory.action?fileNumber=" + fileNumber + "&fileName=" + fileName;
	var callback = {success:function (oResponse) {
		var responseData = oResponse.responseText;
		document.getElementById("fileCmmtHstry").style.display = "block";
		if (responseData == "") {
			document.getElementById("infodiv").innerHTML = "No History";
		} else {
			document.getElementById("infodiv").innerHTML = responseData;
		}
	}, failure:function (oResponse) {
	}, timeout:30000, cache:false};
	YAHOO.util.Connect.asyncRequest("GET", sUrl, callback);
}

function getFileHistory(obj) {
	if(obj.src.indexOf("add") > 0) {
		var url = "../dms/fileManagement!getFileHistory.action?rnd="+Math.random()+"&id="+document.getElementById('id').value;
		var callback = {
			 success:function (oResponse) {
				 var responseData = oResponse.responseText;
				 if(responseData == "error") {
					document.getElementById('fileHistory').innerHTML = "No History data available";
				 } else {
				 	var fileHistoryDefs = [ 
						{key:"Id", parser:"number",sortable:true,hidden:true},
						{key:"Date", sortable:true, parser:"date", resizeable:true}, 
						{key:"SenderDepartment",label:"Sender Department",sortable:true, resizeable:true,parser:"string"}, 
						{key:"Sender",label:"Sender/Receiver",sortable:true, resizeable:true,parser:"string"}, 
						{key:"Task", sortable:true, resizeable:true,parser:"string"}, 
						{key:"Status", sortable:true, resizeable:true,parser:"string"}, 
						{key:"ForwardingType",label:"Forwarding Type",sortable:true, resizeable:true,parser:"string"}, 
						{key:"ReceiverDepartment",label:"Receiver Department",sortable:true, resizeable:true,parser:"string"}, 
						{key:"ForwardingDetails",label:"Forwarding Details",sortable:true, resizeable:true,parser:"string"}, 
						{key:"OutboundFileNumber",label:"Outbound Number",sortable:true, resizeable:true,parser:"string"}, 
						{key:"Comments", label: "Comments",sortable:true, resizeable:true,parser:"string"}
						];

					var fileHistoryDs = new YAHOO.util.DataSource(eval("("+responseData+")"));
					fileHistoryDs.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
					fileHistoryDs.responseSchema = {fields:["Id","Date","SenderDepartment", "Sender", "Task", "Status","ForwardingType","ReceiverDepartment","ForwardingDetails", "OutboundFileNumber","Comments"]};
					var fileHistoryTable = new YAHOO.widget.DataTable("fileHistory", fileHistoryDefs, fileHistoryDs);
					fileHistoryTable.subscribe("rowMouseoverEvent", fileHistoryTable.onEventHighlightRow);   
					fileHistoryTable.subscribe("rowMouseoutEvent", fileHistoryTable.onEventUnhighlightRow);
					fileHistoryTable.subscribe("rowClickEvent", fileHistoryTable.onEventSelectRow); 
					if(document.all) {
						document.getElementById('fldsetfile').style.display = "block";
					} else {
						if(navigator.userAgent.indexOf("Chrome") > -1) {
							document.getElementById('fldsetfile').style.width = "1234px";
						} else {
							document.getElementById('fldsetfile').style.display = "table-column";
						}
					}
					return {oDS:fileHistoryDs, oDT:fileHistoryTable};
					
				 }					
			 }, 
			 failure:function (oResponse) {
				document.getElementById('fileHistory').innerHTML = "No History data available";
			 }, 
			 timeout:30000, 
			 cache:false
		};
		YAHOO.util.Connect.asyncRequest("GET", url, callback);		
		document.getElementById('fileHistoryCtnr').style.display="block";
		obj.src = "../images/delete.png";
	} else {
		document.getElementById('fileHistoryCtnr').style.display="none";
		obj.src = "../images/add.png";
	}
}

function showWaiting() {
	document.getElementById("loading").style.display = "block";
}
try {
//Creates Calendar object
	var cal = new YAHOO.widget.Calendar("calender", {title:"Choose Date:", close:true, navigator:true});
	cal.render();
	cal.hide();
	YAHOO.util.Event.addListener("fileDateBtn", "click", cal.show, cal, true);
	YAHOO.util.Event.addListener("fileReceivedOrSentDateBtn", "click", cal.show, cal, true);
	cal.selectEvent.subscribe(setSelectedDate, cal, true);
}
catch (e) {}
//on click of Calendar img button, open the Calendar
function showCalendar(obj, eve) {
	dateCompId = obj.id;
	var event = window.event ? window.event : eve;
	var posX = event.clientX ? event.clientX : event.pageX;
	var posY = event.clientY ? event.clientY : event.pageY;
	document.getElementById("calender").style.top = posY;
	document.getElementById("calender").style.left = posX;
}
var dateCompId = "";
function setSelectedDate(type, args, obj) {
	var datedata = args[0][0];
	var year = datedata[0];
	var month = datedata[1];
	var day = datedata[2];
	var fieldId = dateCompId.split("Btn")[0];
	if (day.toString().length == 1) {
		day = "0" + day.toString();
	}
	if (month.toString().length == 1) {
		month = "0" + month.toString();
	}
	document.getElementById(fieldId).value = day + "/" + month + "/" + year;
	obj.hide();
}
function addFileAttachment(obj) {
	var tbody = document.getElementById("uploadtbl").lastChild;
	var trNo = (tbody.childElementCount ? tbody.childElementCount : tbody.childNodes.length) + 1;
	var mod = trNo % 2;
	var tr = document.createElement("tr");
	var fileTd = document.createElement("td");
	var cmmtTd = document.createElement("td");
	var fileInput = document.createElement("input");
	var fileCmmt = document.createElement("textarea");
	fileCmmt.setAttribute("name", "comment");
	fileInput.setAttribute("type", "file");
	fileInput.setAttribute("size", "20");
	fileInput.setAttribute("name", "file");
	fileInput.setAttribute("id", "file" + trNo);
	fileCmmt.setAttribute("cols", "20");
	fileCmmt.setAttribute("rows", "2");
	fileTd.appendChild(fileInput);
	var deleteTd = document.createElement("td");
	var deletImg = new Image(16, 16);
	deletImg.src = "../images/delete.png";
	deleteTd.appendChild(deletImg);
	tr.setAttribute("align", "center");
	if (mod == 1) {
		tr.className = "graybox";
	}
	tr.appendChild(fileTd);
	cmmtTd.appendChild(fileCmmt);
	tr.appendChild(cmmtTd);
	tr.appendChild(deleteTd);
	tbody.appendChild(tr);
	deletImg.setAttribute("title", "Remove File");
	deletImg.onclick = function () {
		tbody.removeChild(tr);
	};
}
function hideElement(id) {
	document.getElementById(id).style.display = "none";
}
function loadInternalUserDesignation(elem) {
	var deptId = elem.value;
	populateinternalUserdesignation({deptId:deptId});
	document.getElementById("internalUser.department").value = deptId;
}
function loadInternalUsers(desigElem, deptElemId) {
	var departmentId = document.getElementById(deptElemId).value;
	var designationId = desigElem.value;
	populateinternalUserposition({deptId:departmentId, desigId:designationId});
	document.getElementById("internalUserdesignation").value = designationId;
}
function loadSecondLevelBoundary(elem) {
	var firstLevelBndryId = elem.value;
	populatesecondLevelBndry({firstLevelBndry:firstLevelBndryId});
	document.getElementById("firstLevelBndry").value = firstLevelBndryId;
}
function loadFileSubcategories(elem) {
	var fileCategoryId = elem.value;
	populatefileSubcategory({fileCategory:fileCategoryId});
	document.getElementById("fileCategory").value = fileCategoryId;
}
function loadDesignation(elem) {
	var deptId = elem.value;
	populatereceiverdesignation({deptId:deptId});
	document.getElementById("receiver.department").value = deptId;
}
function loadUsers(desigElem, deptElemId) {
	var departmentId = document.getElementById(deptElemId).value;
	var designationId = desigElem.value;
	populatereceiverposition({deptId:departmentId, desigId:designationId});
	document.getElementById("receiverdesignation").value = designationId;
}
function loadSenderDesignation(elem) {
	var deptId = elem.value;
	populatesenderdesignation({deptId:deptId});
	document.getElementById("sender.department").value = deptId;
}
function loadSenderUsers(desigElem, deptElemId) {
	var departmentId = document.getElementById(deptElemId).value;
	var designationId = desigElem.value;
	populatesenderposition({deptId:departmentId, desigId:designationId});
	document.getElementById("senderdesignation").value = designationId;
}
function setReadOnly() {
	if (document.forms[0] && document.forms[0].fileNumber) {
		document.forms[0].fileNumber.disabled = true;
		document.forms[0].fileDate.disabled = true;
		document.forms[0].department.disabled = true;
		document.forms[0].firstLevelBndry.disabled = true;
		document.forms[0].secondLevelBndry.disabled = true;
		//document.forms[0].filePriority.disabled = true;
		document.forms[0].fileReceivedOrSentDate.disabled = true;
		document.forms[0].fileCategory.disabled = true;
		document.forms[0].fileSubcategory.disabled = true;
		//document.forms[0].fileHeading.disabled = true;
		//document.forms[0].fileSummary.disabled = true;
		//document.forms[0].fileSearchTag.disabled = true;
	}
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
function checkBeforeSubmitFMS(isDraft) {
	var fileType = document.getElementById("fileType").innerHTML.toString();
	var msg = "";
	var spcDateMsg = ""
	var count = 1;
	if (document.getElementById("fileNumber").value === "") {
		msg = (count++)+") File Number\n";
	}
	if (document.getElementById("fileDate").value === "") {
		msg = msg +(count++)+") File Date\n";
	}
	if (document.getElementById("fileCategory").value === "") {
		msg = msg +(count++)+") File Category\n";
	}
	if (document.getElementById("fileHeading").value === "") {
		msg = msg +(count++)+") File Heading\n";
	}
	if (document.getElementById("fileReceivedOrSentDate").value === "") {
		msg = msg +(count++)+") File Received/Sent Date\n";
	} else if (document.getElementById("fileReceivedOrSentDate").value !== "") {
			var fileRorSDate = document.getElementById("fileReceivedOrSentDate").value.split("/");
			var year = parseInt(fileRorSDate[2]);
			var month = (parseInt(fileRorSDate[1],10)-1);
			var date = parseInt(fileRorSDate[0],10);
			var fileDate = new Date();
			fileDate.setFullYear(year,month,date);
			var today = new Date();
			if (fileDate > today){
  				spcDateMsg = "File Received/Sent Date can not be a future date.\n";
  			} 
	}
	if (document.getElementById("fileStatus").value === "") {
		msg = msg +(count++)+") File Status\n";
	}
	if (id == null || id=="") {
		
				if (fileType == "Inbound") {
					
					if (document.getElementById("sentFrom").value === "") {
						msg = msg +(count++)+") Sent from\n";
					}
					if (document.getElementById("sender.userName").value === "") {
						msg = msg +(count++)+") Name of Sender\n";
					}
					
					if (document.getElementById("receiver.department").value === "") {
						msg = msg +(count++)+") Receiver Department\n";
					}
					if (document.getElementById("receiverdesignation").value === "-1") {
						msg = msg +(count++)+") Receiver Designation\n";
					}
					if (document.getElementById("receiverposition").value === "-1") {
						msg = msg +(count++)+") Receiver \n";
					}
				} else if (fileType == "Outbound") {
					if (document.getElementById("sentTo").value === "") {
						msg = msg +(count++)+") Sent To\n";
					}
					if (document.getElementById("receiverName").value === "") {
						msg = msg +(count++)+") Name of Receiver\n";
					}
				} else if (fileType == "Internal") {
					
					if (document.getElementById("sender.department").value === "") {
						msg = msg +(count++)+") Sender Department\n";
					}
					if (document.getElementById("senderdesignation").value === "-1") {
						msg = msg +(count++)+") Sender Designation\n";
					}
					if (document.getElementById("senderposition").value === "-1") {
						msg = msg +(count++)+") Sender\n";
					}
					
					if (document.getElementById("receiver.department").value === "") {
						msg = msg +(count++)+") Receiver Department\n";
					}
					if (document.getElementById("receiverdesignation").value === "-1") {
						msg = msg +(count++)+") Receiver Designation\n";
					}
					if (document.getElementById("receiverposition").value === "-1") {
						msg = msg +(count++)+") Receiver\n";
					}
			
				}
			
	} else {

	if(!isDraft &&
	 document.getElementById("fileStatus").options[document.getElementById("fileStatus").selectedIndex].text.toUpperCase()!= "CLOSED"){

			if (document.getElementById("internalUserDetails").style.display == 'block') {
				if (document.getElementById("internalUser.department").value === "") {
					msg = msg +(count++)+") Department\n";
				}
				if (document.getElementById("internalUserdesignation").value === "-1") {
					msg = msg +(count++)+") Receiver Designation\n";
				}
				if (document.getElementById("internalUserposition").value === "-1") {
					msg = msg +(count++)+") Receiver\n";
				}
			
			} else {
				if (document.getElementById("externalUser.userSource").value === "") {
					msg = msg +(count++)+") Sent To\n";
				}
				if (document.getElementById("externalUser.userName").value === "") {
					msg = msg +(count++)+") Name of Receiver\n";
				}
			
			
			}
		}
	}
	
	if(msg == "" && spcDateMsg=="") {
		return true;
	} else {
		alert(spcDateMsg+"The following fields can not be empty \n"+msg);
		document.getElementById("loading").style.display = "none";
		return false;
	}
}

if (document.all) {
	if (document.getElementById("loading")) {
		document.getElementById("loading").style.height = parseInt(window.screen.height) + 100;
	}
	if (document.getElementById("mask")) {
		document.getElementById("mask").style.height = parseInt(window.screen.height) + 100;
	}
}

