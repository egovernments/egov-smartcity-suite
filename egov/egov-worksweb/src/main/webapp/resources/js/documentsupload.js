/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
var maxSize = 2097152;
var inMB = maxSize/1024/1024;
var fileformatsinclude = ['doc','docx','xls','xlsx','rtf','pdf','jpeg','jpg','png','txt','zip','dxf'];

function addFileInputField() {
	var uploaderTbl = document.getElementById("uploadertbl");
	var tbody = uploaderTbl.lastChild;
	var trNo = (tbody.childElementCount ? tbody.childElementCount : tbody.childNodes.length) + 1;
	var tempTrNo = trNo - 1; 
	var curFieldValue = $("#file" + tempTrNo).val();
	var documentsSize = parseFloat($("#documentsSize").val()) + parseFloat(trNo);
	if(curFieldValue == "") {
		bootbox.alert("Field is empty!");
		return;
	}
	if(documentsSize > 4) {
		bootbox.alert("Only 4 documents can be added");
		return;
	}
	var tr = document.createElement("tr");
	tr.setAttribute("id", "row"+trNo);
	var td = document.createElement("td");
	var inputFile = document.createElement("input");
	inputFile.setAttribute("type", "file");
	inputFile.setAttribute("name", "file");
	inputFile.setAttribute("id", "file" + trNo);
	inputFile.setAttribute("class", "padding-10");
	inputFile.setAttribute("onchange", "isValidFile(this.id)");
	td.appendChild(inputFile);
	tr.appendChild(td);
	tbody.appendChild(tr);	
}

function getTotalFileSize() {
	var uploaderTbl = document.getElementById("uploadertbl");
	var tbody = uploaderTbl.lastChild;
	var trNo = (tbody.childElementCount ? tbody.childElementCount : tbody.childNodes.length) + 1;
	var totalSize = 0;
	for(var i = 1; i < trNo; i++) {
		totalSize += $("#file"+i)[0].files[0].size; // in bytes
		if(totalSize > maxSize) {
			bootbox.alert('File size should not exceed '+ inMB +' MB!');
			$("#file"+i).val('');
			return;
		}
	}
} 

function isValidFile(id) {
	var myfile= $("#"+id).val();
	var ext = myfile.split('.').pop();
	if($.inArray(ext.toLowerCase(), fileformatsinclude) > -1){
		getTotalFileSize();
	} else {
		bootbox.alert("Please upload .doc, .docx, .xls, .xlsx, .rtf, .pdf, jpeg, .jpg, .png, .txt, .zip and .dxf format documents only");
		$("#"+id).val('');
		return false;
	}
}

function deleteFileInputField(id){
	var uploaderTbl = document.getElementById("uploadertbl");
	uploaderTbl.deleteRow(document.getElementById(id));
}

function addSelectedFiles() {
	var uploaderTbl = $("#uploadertbl");
	window.opener.$("#documentDetails").append($(uploaderTbl));
}