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
function ChangeColor(tableRow, highLight)
{
	if (highLight)
	{
	  tableRow.style.backgroundColor = '#dcfac9';
	}
	else
	{
	  tableRow.style.backgroundColor = 'white';
	}
}

var horizontal_offset="9px" //horizontal offset of hint box from anchor link
var vertical_offset="0" //horizontal offset of hint box from anchor link. No need to change.
var ie=document.all
var ns6=document.getElementById&&!document.all


function getposOffset(what, offsettype){
var totaloffset=(offsettype=="left")? what.offsetLeft : what.offsetTop;
var parentEl=what.offsetParent;
while (parentEl!=null){
totaloffset=(offsettype=="left")? totaloffset+parentEl.offsetLeft : totaloffset+parentEl.offsetTop;
parentEl=parentEl.offsetParent;
}
return totaloffset;
}

function iecompattest(){
return (document.compatMode && document.compatMode!="BackCompat")? document.documentElement : document.body
}

function clearbrowseredge(obj, whichedge){
var edgeoffset=(whichedge=="rightedge")? parseInt(horizontal_offset)*-1 : parseInt(vertical_offset)*-1
if (whichedge=="rightedge"){
var windowedge=ie && !window.opera? iecompattest().scrollLeft+iecompattest().clientWidth-30 : window.pageXOffset+window.innerWidth-40
dropmenuobj.contentmeasure=dropmenuobj.offsetWidth
if (windowedge-dropmenuobj.x < dropmenuobj.contentmeasure)
edgeoffset=dropmenuobj.contentmeasure+obj.offsetWidth+parseInt(horizontal_offset)
}
else{
var windowedge=ie && !window.opera? iecompattest().scrollTop+iecompattest().clientHeight-15 : window.pageYOffset+window.innerHeight-18
dropmenuobj.contentmeasure=dropmenuobj.offsetHeight
if (windowedge-dropmenuobj.y < dropmenuobj.contentmeasure)
edgeoffset=dropmenuobj.contentmeasure-obj.offsetHeight
}
return edgeoffset
}

function showhint(menucontents, obj, e, tipwidth){
if ((ie||ns6) && document.getElementById("hintbox")){
dropmenuobj=document.getElementById("hintbox")
dropmenuobj.innerHTML=menucontents
dropmenuobj.style.left=dropmenuobj.style.top=-500
if (tipwidth!=""){
dropmenuobj.widthobj=dropmenuobj.style
dropmenuobj.widthobj.width=tipwidth
}
dropmenuobj.x=getposOffset(obj, "left")
dropmenuobj.y=getposOffset(obj, "top")
dropmenuobj.style.left=dropmenuobj.x-clearbrowseredge(obj, "rightedge")+obj.offsetWidth+"px"
dropmenuobj.style.top=dropmenuobj.y-clearbrowseredge(obj, "bottomedge")+"px"
dropmenuobj.style.visibility="visible"
obj.onmouseout=hidetip
}
}

function hidetip(e){
dropmenuobj.style.visibility="hidden"
dropmenuobj.style.left="-500px"
}

function createhintbox(){
var divblock=document.createElement("div")
divblock.setAttribute("id", "hintbox")
document.body.appendChild(divblock)
}

if (window.addEventListener)
window.addEventListener("load", createhintbox, false)
else if (window.attachEvent)
window.attachEvent("onload", createhintbox)
else if (document.getElementById)
window.onload=createhintbox

//Document Upload Starts
function showDocumentManager()
{
    var v= dom.get("docNumber").value;
    var url;
    if(v==null||v==''||v=='To be assigned')
    {
      url="/egi/docmgmt/basicDocumentManager.action?moduleName=Works";
    }
    else
    {
      url = "/egi/docmgmt/basicDocumentManager!editDocument.action?docNumber="+v+"&moduleName=Works";
    }
    var wdth = 1000;
    var hght = 400;
    window.open(url,'docupload','width='+wdth+',height='+hght);
}

function viewDocumentManager()
{
   var v= dom.get("docNumber").value;
   if(v!='') {
   var url= "/egi/docmgmt/basicDocumentManager!viewDocument.action?docNumber="+v+"&moduleName=Works";
   var wdth = 1000;
    var hght = 400;
    window.open(url,'docupload','width='+wdth+',height='+hght);
   }
   else { 
		alert("No Documents Found");
		return;
	}
}

function viewDocumentManager(docNumber)
{
   if(docNumber!='') {
		var url= "/egi/docmgmt/basicDocumentManager!viewDocument.action?docNumber="+docNumber+"&moduleName=Works";
	   var wdth = 1000;
	   var hght = 400;
	   window.open(url,'docupload','width='+wdth+',height='+hght);
   }
   else { 
		alert("No Documents Found");
		return;
	}
}

function setupDocNumberBeforeSave()
{
	   var v= dom.get("docNumber").value;
       if(v=='To be assigned')
       {
          dom.get("docNumber").value='';
       }
}
// Document Upload Ends

function confirmCancel(msg,objectNo) { 
	var ans=confirm(msg+": "+objectNo+" ?");
	if(ans) {
		return true;
	}
	else {
		return false;		
	}
}

function confirmClose(msg) {  
	var ans=confirm(msg);	
	if(ans) {
		window.close();
	}
	else {
		return false;		
	}
}

function confirmReject(msg) {
	var ans = confirm(msg + " ?");
	if (ans) {
		return true;
	} else {
		return false;
	}
}


function escapeSpecialChars(str) {
	str1 = str.replace(/\'/g, "\\'");
	str2 = str1.replace(/\"/g, '&quot;');
	str3 = str2.replace(/\r\n/g, "&#13;");
	str4 = str3.replace(/([\n]|<br \>)/g,'');
	return str4;
}

function clearForm(formId){
	var form = jQuery('#'+formId);
	jQuery.each(form[0].elements, function(key, val){
		if(form[0].elements[key].type == 'text') {
			form[0].elements[key].value = '';
		}
	});
}

function reinitializeDatepicker()
{
	jQuery(".datepicker").datepicker({
		format : "dd/mm/yyyy",
		autoclose: true
	});
}

function methodTest() {
 	if(document.getElementById("code").value=="Category Code"){
		document.getElementById("code").value="";
	}
	if(document.getElementById("description").value=="Category Name") {
		document.getElementById("description").value="";		
	}
}

function validateSubledgerCodeBeforeSubmit() {
    {
        var codeName = document.getElementById("codeName").value;
        if (codeName == '') {
            showMessage('subledgerCode', 'Deposit Works Name is Required');
            return false;

        }
        var codeDescription = document.getElementById("codeDescription").value;
        if (codeDescription == '') {
            showMessage('subledgerCode', 'Deposit Works Description is Required');
            return false;
        }
        var financialYear = document.getElementById("financialYear").value;
        if (financialYear == '-1') {
            showMessage('subledgerCode', 'Financial Year is Required');
            return false;
        }
        var fund = document.getElementById("fund").value;
        if (fund == '-1') {
            showMessage('subledgerCode', 'Fund is Required');
            return false;
        }
        var fundSource = document.getElementById("fundSource").value;
        if (fundSource == '-1') {
            showMessage('subledgerCode', 'Financing source is Required');
            return false;
        }
    }
    return true;
}

function disableFields() {
	var mode = document.getElementById('mode').value;
	if(mode=='view'){
	document.getElementById("code").disabled = true;
	document.getElementById("description").disabled = true;	
	}
	else if(mode=='edit'){
	document.getElementById("code").disabled = false;
	document.getElementById("description").disabled = false;	
	}
}

function setContractorId(val) {
	document.getElementById('id').value = val;
}

function modifyContractorDataOnSearch() {
	var id = document.getElementById('id').value;
    if (id == '' || id == null) {
    	var message = document.getElementById('selectMessage').value;
        showMessage('contractorError', message);
        window.scrollTo(0, 0);
        return false;
    } else
        window.location = 'contractor-edit.action?mode=edit&id=' + id;
    return true;
}

function validate(){
	jQuery('#id').val('');
}

function viewContractorDataOnSearch() {
	var id = document.getElementById('id').value;
    if (id == '' || id == null) {
    	var message = document.getElementById('selectMessage').value;
        showMessage('contractorError', message);
        window.scrollTo(0, 0);
        return false;
    } else
        window.location = 'contractor-edit.action?mode=view&id=' + id;
    return true;
}

function createNewEsimate() {
	window.location = "estimateTemplate-newform.action";
}

function modifyEstimate() {
	var id=document.getElementById('id')
	window.location = "estimateTemplate-edit.action?mode=edit&id="+ id;
}

function setSorId(val) {
	document.getElementById('id').value = val;
} 

function viewScheduleData() {
	var id = document.getElementById('id').value;
    if (id == '' || id == null) {
    	var message = document.getElementById('selectMessage').value;
        showMessage('sor.sorError', message);
        window.scrollTo(0, 0);
        return false;
    } else
		window.open("scheduleOfRate-edit.action?mode=view&id="+id+"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
   		return true;
}

function modifyScheduleData() {
	var id = document.getElementById('id').value;
    if (id == '' || id == null) {
    	var message = document.getElementById('selectMessage').value;
        showMessage('sor.sorError', message);
        window.scrollTo(0, 0);
        return false;
    } else
        window.open("scheduleOfRate-edit.action?mode=edit&id="+id+"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
    	return true;
}		

function validateSOR(){	
	document.searchSORForm.action='scheduleOfRate-searchSorDetails.action';
	document.searchSORForm.submit();
}