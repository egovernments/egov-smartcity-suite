/*-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#     accountability and the service delivery of the government  organizations.
#  
#      Copyright (C) <2015>  eGovernments Foundation
#  
#      The updated version of eGov suite of products as by eGovernments Foundation 
#      is available at http://www.egovernments.org
#  
#      This program is free software: you can redistribute it and/or modify
#      it under the terms of the GNU General Public License as published by
#      the Free Software Foundation, either version 3 of the License, or
#      any later version.
#  
#      This program is distributed in the hope that it will be useful,
#      but WITHOUT ANY WARRANTY; without even the implied warranty of
#      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#      GNU General Public License for more details.
#  
#      You should have received a copy of the GNU General Public License
#      along with this program. If not, see http://www.gnu.org/licenses/ or 
#      http://www.gnu.org/licenses/gpl.html .
#  
#      In addition to the terms of the GPL license to be adhered to in using this
#      program, the following additional terms are to be complied with:
#  
#  	1) All versions of this program, verbatim or modified must carry this 
#  	   Legal Notice.
#  
#  	2) Any misrepresentation of the origin of the material is prohibited. It 
#  	   is required that all modified versions of this material be marked in 
#  	   reasonable ways as different from the original version.
#  
#  	3) This license does not grant any rights to any user of the program 
#  	   with regards to rights under trademark law for use of the trade names 
#  	   or trademarks of eGovernments Foundation.
#  
#    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------*/
//Called when user clicks Add More.. to add more file attchment inputs
function addFileAttachHolder() {
	var uploaderTbl = document.getElementById("uploadertbl");
	var tbody = uploaderTbl.lastChild;
	var trNo = (tbody.childElementCount ? tbody.childElementCount : tbody.childNodes.length) + 1;
	var tr = document.createElement("tr");
	var td = document.createElement("td");
	var td2 = document.createElement("td");
	td2.className = "captd";
	var fileInput = document.createElement("input");
	fileInput.setAttribute("type", "file");
	fileInput.setAttribute("size", "40");
	fileInput.setAttribute("name", "upload");
	fileInput.setAttribute("id", "file" + trNo);
	var fileCapInput = document.createElement("input");
	fileCapInput.setAttribute("type", "text");
	fileCapInput.setAttribute("name", "fileCaption");
	fileCapInput.setAttribute("size", "30");
	fileCapInput.setAttribute("id", "fileCaption" + trNo);
	var remarkDiv = document.getElementById('remarkdiv2').cloneNode(true);
	remarkDiv.setAttribute('id','remarkdiv'+trNo);
	var remarkBtn = document.getElementById('remarkbtn2').cloneNode(true);
	remarkBtn.setAttribute('id','remarkbtn'+trNo);
	td.appendChild(fileInput);
	tr.appendChild(td);
	td2.appendChild(fileCapInput);
	td2.appendChild(remarkDiv);
	td2.appendChild(document.createTextNode(" "));
	td2.appendChild(remarkBtn);	
	tr.appendChild(td2);
	tbody.appendChild(tr);
	var txtArea = document.getElementsByName('fileInfo')[(trNo-2)];
	txtArea.setAttribute('id','remark'+trNo);
	txtArea.className = 'remarkarea';
	txtArea.value ='enter file remarks';
	var okbtn = document.getElementsByName('okbtn')[(trNo-2)];
	remarkBtn.onclick = function () {
		showHideRemark (trNo);
	}
	okbtn.onclick = remarkBtn.onclick;	
	fileInput.onchange = function () {
		setFileCaption(fileInput, "fileCaption" + trNo);
	};
}

function addBasicFileAttach() {
	var uploaderTbl = document.getElementById("uploadertbl");
	var tbody = uploaderTbl.lastChild;
	var trNo = (tbody.childElementCount ? tbody.childElementCount : tbody.childNodes.length) + 1;
	var tr = document.createElement("tr");
	var td = document.createElement("td");
	var fileInput = document.createElement("input");
	fileInput.setAttribute("type", "file");
	fileInput.setAttribute("size", "40");
	fileInput.setAttribute("name", "upload");
	fileInput.setAttribute("id", "file" + trNo);
	td.setAttribute('colspan','2');
	td.appendChild(fileInput);
	tr.appendChild(td);
	tbody.appendChild(tr);	
	fileInput.onchange = function () {
		isValidFile(fileInput,true);
	};
}
function isValidFile(file,basic) {
	if(file.value.indexOf(".") == -1) {
		var parentNode = file.parentNode;
		var fileInput = document.createElement("input");
		fileInput.setAttribute("type", "file");
		fileInput.setAttribute("size", "40");
		fileInput.setAttribute("name", "upload");
		fileInput.setAttribute("id", file.id);
		fileInput.onchange = function () {
			if (!basic) {
				setFileCaption(fileInput, "fileCaption" + file.id.substring(4,file.id.length));
			} else {
				isValidFile(fileInput,true);
			}
		};
		parentNode.replaceChild(fileInput,file);
		bootbox.alert("File is invalid, Please select a valid file.");
		return false;
	}
	return true;
}
//Called when user browse the File and selects a File to set the default file cation
function setFileCaption(file, fileCap) {
	if (isValidFile(file,false)) {
		var fullFileName = file.value;
		var fileName = fullFileName.substring(fullFileName.lastIndexOf("\\") + 1, fullFileName.lastIndexOf("."));
		document.getElementById(fileCap).value = fileName;
	}
}
function detachFile(obj, rowNo) {
	if (obj.src.indexOf("empty") > 0) {
		if (confirm("Would you like [ " + document.getElementById("fileName" + rowNo).value + " ] file to mark for Delete ?")) {
			//fileListTable = document.getElementById('fileListTbl');
			tr = document.getElementById("fileItem" + rowNo);
			tr.style.backgroundColor = "#e9f5fc";
			obj.src = "../images/recycle-full.png";
			document.getElementById("downloadbtn" + rowNo).style.display = "none";
			obj.setAttribute("title", "Click to remove detach marking");
			document.getElementById("fileName" + rowNo).disabled = false;
			tr.setAttribute("title", "Marked for deletion");
		}
	} else {
		obj.src = "../images/recycle-empty.png";
		document.getElementById("downloadbtn" + rowNo).style.display = "";
		obj.setAttribute("title", "Click to mark for detach");
		document.getElementById("fileName" + rowNo).disabled = true;
		tr = document.getElementById("fileItem" + rowNo);
		tr.setAttribute("title", "");
		tr.style.backgroundColor = "";
	}
}
function closeAndClearResource() {
	window.close();
}

function showHideRemark(rowNo) {
	if (document.getElementById("remarkdiv"+rowNo).style.display == "block") {
		document.getElementById("remarkdiv"+rowNo).style.display = "none";
	} else {
		document.getElementById("remarkdiv"+rowNo).style.display = "block"; 
		document.getElementById("remark"+rowNo).focus(); 				
	}
}
 function clearText(obj) {
  if (obj.value == 'enter file remarks') {
  	obj.value = '';
  	obj.style.color = '#000000';
  }
 }
var zIndexgcl = 1000;
function showInfo(idNo,toShow) {
	if (toShow) {
		document.getElementById('info'+idNo).style.display = 'block';
		document.getElementById('info'+idNo).style.zIndex  = ++zIndexgcl;
	} else {
		document.getElementById('info'+idNo).style.display = 'none';
	}
}
 
function showWaiting() {
	document.getElementById('loading').style.display ='block';
}

if (actionCmd == "") {
	document.getElementById("filechooser").style.display = "none";
	var detach = document.getElementsByName("detachbtn");
	for (var i = 0; i < detach.length; i++) {
		detach[i].style.display = "none";
	}
	document.getElementById("savebtn").style.display = "none";
	document.getElementById("searchTag").readOnly = true;
}

if (docnum == "") {
	document.getElementById("filechooser").style.display = "none";
	var detach = document.getElementsByName("detachbtn");
	var download = document.getElementsByName("downloadbtn");
	for (var i = 0; i < detach.length; i++) {
		detach[i].style.display = download[i].style.display = "none";
	}
	document.getElementById("savebtn").style.display = "none";
	document.getElementById("searchTag").readOnly = true;
}

document.getElementById('loading').style.display ='none';
