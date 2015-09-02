#-------------------------------------------------------------------------------
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
#-------------------------------------------------------------------------------
var contextMenuId = null;
function searchDocument() {
	document.getElementById("loading").style.display = "block";
	var inputs = document.getElementsByTagName("input");
	var args = "";
	for (var i = 0; i < inputs.length; i++) {
		if (inputs[i].type != "button") {
			args = args + inputs[i].name + "=" + inputs[i].value + "&";
		}
	}
	var sUrl = "/egi/docmgmt/documentManager!searchDocument.action?" + args+"rnd="+Math.random();
	var callback = {success:function (oResponse) {
		var responseData = oResponse.responseText;
		if (responseData == "") {
			document.getElementById("loading").style.display = "none";
			document.getElementById("searchrslt").style.display = "none";
			document.getElementById("error").style.display = "block";
			document.getElementById("errorMsg").innerHTML = "No Notice found for the given criteria";
			return;
		}
		populateSearchData(responseData);
		document.getElementById("searchresult").lastChild.style.width = "100%";
		document.getElementById("error").style.display = "none";
		document.getElementById("loading").style.display = "none";
		document.getElementById("searchrslt").style.display = "block";
	}, failure:function (oResponse) {
		document.getElementById("loading").style.display = "none";
	}, timeout:30000, cache:false};
	YAHOO.util.Connect.asyncRequest("GET", sUrl, callback);
	var populateSearchData = function (data) {
		var searchResultColumns = [{key:"documentNumber", label:"Notice Number", parser:"number", sortable:true, resizeable:true}, {key:"noticeDate", label:"Notice Date", sortable:true, resizeable:true, parser:"date"}, {key:"noticeType", label:"Notice Type", sortable:true, resizeable:true, parser:"string"}, {key:"associatedObjectId", label:"Asso:Object No.", sortable:true, resizeable:true, parser:"string"}, {key:"addressedTo", label:"Addressed To", sortable:true, resizeable:true, parser:"string"}, {key:"address", label:"Address", sortable:true, resizeable:true, parser:"string"}, {key:"files", hidden:true,label:""}];
		var searchDataSource = new YAHOO.util.DataSource(eval("(" + data + ")"));
		searchDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
		searchDataSource.responseSchema = {fields:["documentNumber", "noticeDate", "noticeType", "associatedObjectId", "addressedTo", "address", "files"]};
		var searchDataTable = new YAHOO.widget.DataTable("searchresult", searchResultColumns, searchDataSource, {draggableColumns:true});
		searchDataTable.subscribe("rowMouseoverEvent", searchDataTable.onEventHighlightRow);
		searchDataTable.subscribe("rowMouseoutEvent", searchDataTable.onEventUnhighlightRow);
		searchDataTable.subscribe("rowClickEvent", searchDataTable.onEventSelectRow);
		return {oDS:searchDataSource, oDT:searchDataTable};
	};
}
function showDownloadList(eve) {
	var filedwnldpanel = document.getElementById("filedwnldpanel");
	filedwnldpanel.style.display = "block";
	var events = window.all ? event : eve;
	var target = events.target ? events.target : events.srcElement;
	var value = target.nextSibling.value.split("#");
	if (value[1] == "") {
		return;
	}
	var fileNames = value[1].split("^~");
	var filedwnld = document.getElementById("filedwnld");
	while (filedwnld.hasChildNodes()) {
		filedwnld.removeChild(filedwnld.lastChild);
	}
	for (var i = 0; i < fileNames.length; i++) {
		var li = document.createElement("li");
		li.setAttribute("id", "ul" + i);
		li.className = "li";
		var a = document.createElement("a");
		a.setAttribute("href", "/egi/docmgmt/ajaxFileDownload.action?docNumber=" + value[0] + "&fileName=" + fileNames[i]);
		a.setAttribute("target", "_self");
		a.setAttribute("id", "a" + i);
		a.className='a';
		a.innerHTML = fileNames[i];
		li.appendChild(a);
		filedwnld.appendChild(li);
	}
}

function  closeFileDownload() {
	document.getElementById("filedwnldpanel").style.display='none';
}

var dateCompId = "";
function setSelectedDate (type,args,obj) {
  var datedata = args[0][0];
  var year = datedata[0];
  var month = datedata[1];
  var day = datedata[2]; 
  if (dateCompId == 'fromDateBtn') {
  	document.getElementById('noticeDate1').value = day+'/'+month+'/'+year;
  } else {
  	document.getElementById('noticeDate2').value = day+'/'+month+'/'+year;
  }
  obj.hide();
}
var cal = new YAHOO.widget.Calendar("calender", { title:"Choose Date:", close:true, navigator:true } );
cal.render();
cal.hide();	
YAHOO.util.Event.addListener("fromDateBtn", "click", cal.show, cal, true);
YAHOO.util.Event.addListener("toDateBtn", "click", cal.show, cal, true);
cal.selectEvent.subscribe(setSelectedDate, cal, true);

