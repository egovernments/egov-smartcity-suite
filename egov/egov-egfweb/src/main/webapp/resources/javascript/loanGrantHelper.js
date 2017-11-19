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
var PROJECTCODELIST='projectCodeList';
var SANCTIONEDAMOUNTLIST='sanctionedAmountLGDetails';
var UNSANCTIONEDAMOUNTLIST='unsanctionedAmountLGDetails';
var REVISEDAMOUNTLIST='revisedAmountLGDetails';
var RECEIPTLIST='receiptList';
var path;
var projectCodeDetailTableIndex = 0;
var sanctionedAmountTableIndex=0;
var unsanctionedAmountTableIndex=0;
var revisedAmountTableIndex=0;
var receiptTableIndex=0;
var totalrevisedloan=0;
var totalrevisedgrant=0;
var totalunsanctionedloan=0;
var totalunsanctionedgrant=0;
var totalsanctionedloan=0;
var totalsanctionedgrant=0;
var totalreceiptamount=0;
var oAutoCompEntity;

function updateGridNames(AMOUNTLIST, field,index,value){
	if(value==null)
		return;
	if(field=='agreementDate')
		document.getElementsByName(AMOUNTLIST+'['+index+'].'+field)[0].value=value;
	else
		document.getElementById(AMOUNTLIST+'['+index+'].'+field).value=value;
}
function createTextFieldFormatterForProjectCode(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text'  id='"+prefix+"["+projectCodeDetailTableIndex+"]"+suffix+"' name='"+prefix+"["+projectCodeDetailTableIndex+"]"+suffix+"' style='width:160px;' onfocus='autocompleteUnmappedProjectCodesBy20(this);' onblur='fillNeibrAfterSplitLG(this);validateDuplicateProjectCode(this);' autocomplete='off' />";
		
	}
}
function createTextFieldFormatterForProjectName(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text'  id='"+prefix+"["+projectCodeDetailTableIndex+"]"+suffix+"' name='"+prefix+"["+projectCodeDetailTableIndex+"]"+suffix+"' style='width:530px;' readonly  />";
	}
}

function createTextFieldFormatterLG(tableType,prefix,suffix,type){
    return function(el, oRecord, oColumn, oData) {
    	var index=getIndexForTableType(tableType)
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = " <input type='"+type+"' id='"+prefix+"["+index+"]"+suffix+"' name='"+prefix+"["+index+"]"+suffix+"' style='width:90px;' />";
	}
}
function createTextFieldFormatterWithValue(tableType,prefix,suffix,helo,value){
    return function(el, oRecord, oColumn, oData) {
    	var index=getIndexForTableType(tableType)
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = " <input type='"+helo+"' id='"+prefix+"["+index+"]"+suffix+"'  name='"+prefix+"["+index+"]"+suffix+"' style='width:90px;' />";
	}
}

function createTextFieldFormatterWithStyle(tableType,prefix,suffix,style){
	return function(el, oRecord, oColumn, oData) {
		var tableIndex=getIndexForTableType(tableType);
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = " <input type='text' id='"+prefix+"["+tableIndex+"]"+suffix+"' name='"+prefix+"["+tableIndex+"]"+suffix+"' style='"+style+"' />";
	}
}
function createTextFieldFormatter(tableType,prefix,suffix,type){
	return function(el, oRecord, oColumn, oData) {
		var tableIndex=getIndexForTableType(tableType);
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = " <input type='"+type+"' id='"+prefix+"["+tableIndex+"]"+suffix+"' name='"+prefix+"["+tableIndex+"]"+suffix+"' style='width:90px;' />";
	}
}
function createTextFieldFormatterImg(tableType,prefix,suffix,type){
	return function(el, oRecord, oColumn, oData) {
		var tableIndex=getIndexForTableType(tableType);
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		var imgsuffix=suffix+"img";
		el.innerHTML = " <input type='"+type+"' id='"+prefix+"["+tableIndex+"]"+suffix+"' name='"+prefix+"["+tableIndex+"]"+suffix+"' style='width:90px;' /><img src='/egi/resources/erp2/images/searchicon.gif' id='"+prefix+"["+tableIndex+"]"+imgsuffix+"' name='"+prefix+"["+tableIndex+"]"+imgsuffix+"' onclick='openViewVouchers(this)'/>";
	}    
}
function createTextFieldLGAmount(tableType,prefix,suffix,onblurFunction)
{
	return function(el, oRecord, oColumn, oData) {
		var tableIndex=getIndexForTableType(tableType);
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = " <input type='text' id='"+prefix+"["+tableIndex+"]"+suffix+"' name='"+prefix+"["+tableIndex+"]"+suffix+"' style='text-align:right;width:85px' onblur="+onblurFunction+" />";
	}
}
function updateTotalAmount(tableType,loanGrantType)
{	
	var prefix,suffix,tableIndex,objId,totalObjId;
	var amt=0;
	var i,totalg,totall;
	if(tableType=='sanctioned')
	{
		prefix='sanctionedAmountLGDetails';
		tableIndex=sanctionedAmountTableIndex;
		objId='totalsanctioned';
	}
	if(tableType=='unsanctioned')
	{
		prefix='unsanctionedAmountLGDetails';
		tableIndex=unsanctionedAmountTableIndex;
		objId='totalunsanctioned';
	}
	if(tableType=='revised')
	{
		prefix='revisedAmountLGDetails';
		tableIndex=revisedAmountTableIndex;
		objId='totalrevised';
	}
	totalObjId=objId;
	if(loanGrantType=='loan')
	{
		suffix='loanAmount';
		objId=objId+'loan';
	}
	if(loanGrantType=='grant')
	{
		suffix='grantAmount';
		objId=objId+'grant';
	}
	for( i=0;i<tableIndex;i++)
	{
		
		if(null != document.getElementById(prefix+'['+i+'].'+suffix)){
			var val = document.getElementById(prefix+'['+i+'].'+suffix).value;
			if(val!="" && !isNaN(val))
			{
				amt = amt + parseFloat(val);
			}
		}
	}
	document.getElementById(objId).value = amountConverter(amt);
	totall=document.getElementById(totalObjId+"loan").value;
	totalg=document.getElementById(totalObjId+"grant").value
	document.getElementById(totalObjId).value=amountConverter(parseFloat(totall)+parseFloat(totalg));
}
function updateReceiptTotalAmount()
{	
	var amt=0;
	var i;
	for( i=0;i<receiptTableIndex;i++)
	{
		
		if(null != document.getElementById(RECEIPTLIST+'['+i+'].amount')){
			var val = document.getElementById(RECEIPTLIST+'['+i+'].amount').value;
			if(val!="" && !isNaN(val))
			{
				amt = amt + parseFloat(val);
			}
		}
	}
	document.getElementById('totalreceiptamount').value = amountConverter(amt);
}
function updateAllTotalAmounts()
{	
	var prefix,tableIndex,objId;
	var loanAmt=0;
	var grantAmt=0;
	var i;
	var loanSuffix,grantSuffix;
	prefix='sanctionedAmountLGDetails';
	grantSuffix='grantAmount';
	loanSuffix='loanAmount';
	for( i=0;i<sanctionedAmountTableIndex;i++)
	{
		
		if(null != document.getElementById(prefix+'['+i+'].'+grantSuffix)){
			var val = document.getElementById(prefix+'['+i+'].'+grantSuffix).value;
			if(val!="" && !isNaN(val))
			{
				grantAmt = grantAmt + parseFloat(val);
			}
		}
		if(null != document.getElementById(prefix+'['+i+'].'+loanSuffix)){
			var val = document.getElementById(prefix+'['+i+'].'+loanSuffix).value;
			if(val!="" && !isNaN(val))
			{
				loanAmt = loanAmt + parseFloat(val);
			}
		}
	}
	document.getElementById('totalsanctionedloan').value = amountConverter(loanAmt);
	document.getElementById('totalsanctionedgrant').value = amountConverter(grantAmt);
	document.getElementById('totalsanctioned').value =amountConverter(loanAmt+grantAmt);
	prefix='unsanctionedAmountLGDetails';
	loanAmt=0;
	grantAmt=0;
	for( i=0;i<unsanctionedAmountTableIndex;i++)
	{
		
		if(null != document.getElementById(prefix+'['+i+'].'+loanSuffix)){
			var val = document.getElementById(prefix+'['+i+'].'+loanSuffix).value;
			if(val!="" && !isNaN(val))
			{
				loanAmt = loanAmt + parseFloat(val);
			}
		}
		if(null != document.getElementById(prefix+'['+i+'].'+grantSuffix)){
			var val = document.getElementById(prefix+'['+i+'].'+grantSuffix).value;
			if(val!="" && !isNaN(val))
			{
				grantAmt = grantAmt + parseFloat(val);
			}
		}
	}
	document.getElementById('totalunsanctionedloan').value =amountConverter(loanAmt);
	document.getElementById('totalunsanctionedgrant').value = amountConverter(grantAmt);
	document.getElementById('totalunsanctioned').value = amountConverter(loanAmt+grantAmt);
	prefix='revisedAmountLGDetails';
	loanAmt=0;
	grantAmt=0;
	for( i=0;i<revisedAmountTableIndex;i++)
	{
		
		if(null != document.getElementById(prefix+'['+i+'].'+loanSuffix)){
			var val = document.getElementById(prefix+'['+i+'].'+loanSuffix).value;
			if(val!="" && !isNaN(val))
			{
				loanAmt = loanAmt + parseFloat(val);
			}
		}
		if(null != document.getElementById(prefix+'['+i+'].'+grantSuffix)){
			var val = document.getElementById(prefix+'['+i+'].'+grantSuffix).value;
			if(val!="" && !isNaN(val))
			{
				grantAmt = grantAmt + parseFloat(val);
			}
		}
	}
	document.getElementById('totalrevisedloan').value = amountConverter(loanAmt);
	document.getElementById('totalrevisedgrant').value = amountConverter(grantAmt);
	document.getElementById('totalrevised').value = amountConverter(loanAmt+grantAmt);
}
function updateProjectCodeTableIndex()
{
	projectCodeDetailTableIndex++;
}
function updateSanctionedAmountTableIndex()
{
	sanctionedAmountTableIndex++;
}
function updateUnsanctionedAmountTableIndex()
{
	unsanctionedAmountTableIndex++;
}
function updateRevisedAmountTableIndex(){
	revisedAmountTableIndex=revisedAmountTableIndex+1;
}
function autocompleteUnmappedProjectCodesBy20(obj)
{
	   oACDS = new YAHOO.widget.DS_XHR("/EGF/voucher/common!ajaxLoadUnmappedProjectCodesBy20.action", [ "~^"]);
	   oACDS.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
	   oACDS.scriptQueryParam = "startsWith";
	   var oAutoComp1 = new YAHOO.widget.AutoComplete(obj.name,'codescontainer',oACDS);
	   oAutoComp1.doBeforeSendQuery = function(sQuery){
		   loadWaitingImage(); 
		   return sQuery+"&subSchemeId="+document.getElementById("subSchemeId").value;
	   } 
	   oAutoComp1.queryDelay = 0.5;
	   oAutoComp1.minQueryLength = 3;
	   oAutoComp1.prehighlightClassName = "yui-ac-prehighlight";
	   oAutoComp1.useShadow = true;
	   oAutoComp1.forceSelection = true;
	   oAutoComp1.maxResultsDisplayed = 20;
	   oAutoComp1.useIFrame = true;
	   oAutoComp1.doBeforeExpandContainer = function(oTextbox, oContainer, sQDetauery, aResults) {
		   clearWaitingImage();
	           var pos = YAHOO.util.Dom.getXY(oTextbox);
	           pos[1] += YAHOO.util.Dom.get(oTextbox).offsetHeight + 6;
	           oContainer.style.width=300;
	           YAHOO.util.Dom.setXY(oContainer,pos);
	           return true;
	   }
}
var onlyName;
var chequeNoAndDate={
		success: function(o) {
			if(o.responseText!="")
			{
				var docs=o.responseText.split("$");
				document.getElementById(onlyName+".instrumentHeader.id").value= ((docs[0]=='0')?"":docs[0]);
				document.getElementById(onlyName+".instrumentHeader.instrumentNumber" ).value= ((docs[1]=='0')?"":docs[1]);
				document.getElementById(onlyName+".instrumentHeader.instrumentDate" ).value= ((docs[2]=='-')?"":docs[2]);
				document.getElementById(onlyName+".amount" ).value= ((docs[3]=='0')?"":docs[3]);
				document.getElementById(onlyName+".bankaccount.id" ).value= ((docs[4]=='0')?"":docs[4]);
				document.getElementById(onlyName+".bankaccount.accountnumber" ).value= ((docs[5]=='0')?"":docs[5]);
				document.getElementById(onlyName+".bankBranch" ).value= ((docs[6]=='-')?"":docs[6]);
				document.getElementById(onlyName+".description" ).value= ((docs[7]=='0')?"":docs[7]);
				document.getElementById(onlyName+".fundingAgency.id" ).value= ((docs[8]=='0')?"":docs[8]);
				document.getElementById(onlyName+".fundingAgency.name" ).value= ((docs[9]=='0')?"":docs[9]);
				updateReceiptTotalAmount();
			}
		},
		failure: function(o) {
			bootbox.alert('Cannot fetch instrument and account details');
		}
	}
function loadChequeNoAndDate(billVhId,name){
	var url = '../voucher/common!ajaxLoadChequeNoAndDate.action?billVhId='+billVhId;
	YAHOO.util.Connect.asyncRequest('POST', url, chequeNoAndDate, null);
}
function openViewVouchers(obj)
{
	var url = '../voucher/voucherSearch!beforesearch.action?showMode=sourceLink';
	var val=	window.showModalDialog(url,"SearchBillVouchers","dialogwidth: 800; dialogheight: 600;");
	if(val!=undefined && val!=null && val!="" && val.split("$").length>0)
	{
		var objName=obj.name;
		var name=objName.replace("img","");
		var data=val.split("$");
		document.getElementById(name).value=data[0];
		var id=name.replace("voucherNumber","id");
		document.getElementById(id).value=data[2];
		onlyName=name.replace(".voucherHeader.voucherNumber","")
		loadChequeNoAndDate(data[2],onlyName);
		
	}
}
function fillNeibrAfterSplitLG(obj)
{
	var splitValue1=obj.value.split("`-`");
	var splitValue2,name, id,code;
	var currow=getRowIndex(obj);
	var objName="projectCodeList[";
	if(splitValue1.length>1)
	{
		code=splitValue1[0];
		splitValue2=splitValue1[1].split("`~`");
		if(splitValue2.length>1)
		{
			name=splitValue2[0];
			id=splitValue2[1];
			document.getElementById(objName+currow+"].code").value=code;
			document.getElementById(objName+currow+"].name").value=name;
			document.getElementById(objName+currow+"].id").value=id;
		}
		else 
			obj.value='';
	}
	else
		obj.value='';
}
function validateDuplicateProjectCode(obj)
{
	var i,idObj;
	var currow=getRowIndex(obj);
	var currId=document.getElementById("projectCodeList["+currow+"].id").value;
	if(currId=='')
		return;
	for(i=0;i<projectCodeDetailTableIndex;i++)
	{
		idObj=document.getElementById("projectCodeList["+i+"].id");
		if(idObj!=null )
		{
			if(currId==idObj.value && currow!=i)
			{
				bootbox.alert("Project Code has already been entered");
				document.getElementById("projectCodeList["+currow+"].id").value="";
				document.getElementById("projectCodeList["+currow+"].code").value="";
				document.getElementById("projectCodeList["+currow+"].name").value="";
				break;
			}
		}
	}
}
function createDocUploadFormatterLGSanctioned(tableType,prefix,suffix){
	var deleteImageFormatter = function(el, oRecord, oColumn, oData) {
		var tableIndex =getIndexForTableType(tableType);
		markup='<input type="submit" class="buttonsubmit" value="Attach" id="'+prefix+"["+tableIndex+"]"+suffix+'" name="'+prefix+"["+tableIndex+"]"+suffix+'" style="width:60px" onclick=" showDocumentManagerLGSanctioned(this);return false;" />';
	    el.innerHTML = markup;
	}
	return deleteImageFormatter;
}
var ELEMENTID;
function showDocumentManagerLGSanctioned(obj){
	var index=getRowIndex(obj);
	ELEMENTID = "sanctionedAmountLGDetails["+index+"].docId";
    docManager(document.getElementById(ELEMENTID).value);
}
function createDocUploadFormatterLGUnsanctioned(tableType,prefix,suffix){
	var deleteImageFormatter = function(el, oRecord, oColumn, oData) {
		var tableIndex =getIndexForTableType(tableType);
		markup='<input type="submit" class="buttonsubmit" value="Attach" id="'+prefix+"["+tableIndex+"]"+suffix+'" name="'+prefix+"["+tableIndex+"]"+suffix+'" style="width:60px" onclick=" showDocumentManagerLGUnsanctioned(this);return false;" />';
	    el.innerHTML = markup;
	}
	return deleteImageFormatter;
}
function showDocumentManagerLGUnsanctioned(obj){
	var index=getRowIndex(obj);
	ELEMENTID = "unsanctionedAmountLGDetails["+index+"].docId";
    docManager(document.getElementById(ELEMENTID).value);
}
function createDocUploadFormatterLGRevised(tableType,prefix,suffix){
	var deleteImageFormatter = function(el, oRecord, oColumn, oData) {
		var tableIndex =getIndexForTableType(tableType);
		markup='<input type="submit" class="buttonsubmit" value="Attach" id="'+prefix+"["+tableIndex+"]"+suffix+'" name="'+prefix+"["+tableIndex+"]"+suffix+'" style="width:60px" onclick=" showDocumentManagerLGRevised(this);return false;" />';
	    el.innerHTML = markup;
	}
	return deleteImageFormatter;
}
function showDocumentManagerLGRevised(obj){
	var index=getRowIndex(obj);
	ELEMENTID = "revisedAmountLGDetails["+index+"].docId";
    docManager(document.getElementById(ELEMENTID).value);
}
var docNumberUpdater = function (docNumber){
		document.getElementById(ELEMENTID).value = docNumber;
	}

function getIndexForTableType(tableType)
{
	if(tableType=='sanctioned')
		return sanctionedAmountTableIndex;
	if(tableType=='unsanctioned')
		return unsanctionedAmountTableIndex;
	if(tableType=='revised')
		return revisedAmountTableIndex;
	if(tableType=='receipt')
		return receiptTableIndex;
	if(tableType=='projectcode')
		return projectCodeDetailTableIndex;
}
function createPercentageFieldFormatter(tableType,prefix,suffix)
{
	return function(el, oRecord, oColumn, oData) {
		var tableIndex=getIndexForTableType(tableType);
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = " <input type='text' readonly id='"+prefix+"["+tableIndex+"]"+suffix+"' name='"+prefix+"["+tableIndex+"]"+suffix+"' maxlength='5' style='width:40px;text-align:right;' />";
	}
}
function validateLGAmount(tableType,obj,prefix)
{
	var projectCost=document.getElementById('projectCost').value;
	var sanctionedCost=document.getElementById('sanctionedCost').value;
	var revisedCost=document.getElementById('revisedCost').value;
	var index=getRowIndex(obj);
	var loanObjId=prefix+'['+index+'].loanAmount';
	var grantObjId=prefix+'['+index+'].grantAmount';
	var percentObjId=prefix+'['+index+'].percentage';
	var loanValue= document.getElementById(loanObjId).value;
	var grantValue= document.getElementById(grantObjId).value;
	var denom,percentage;
	if(projectCost==undefined || projectCost=='' )
		projectCost=0;
	else
		projectCost=parseFloat(projectCost);

	if(revisedCost==undefined || revisedCost=='' )
		revisedCost=0;
	else
		revisedCost=parseFloat(revisedCost);
	if(revisedCost!=0)
		denom=revisedCost;
	else
		denom=projectCost;
	if((projectCost==undefined || projectCost=='') &&  (revisedCost==undefined || revisedCost=='') )
	{
		bootbox.alert("Project Cost and Revised Cost cannot be empty");
		obj.value='0';
		return;
	}
	projectCost=parseFloat(projectCost);
	revisedCost=parseFloat(revisedCost);
	if(!(projectCost>0 || revisedCost>0))
	{
		bootbox.alert("Project Cost and Revised Cost cannot be 0");
		obj.value='0';
		return;
	}
	if(loanValue==undefined || loanValue=='' )
		loanValue=0;
	else
		loanValue=parseFloat(loanValue);
	if(grantValue==undefined|| grantValue=='' )
		grantValue=0;
	else
		grantValue=parseFloat(grantValue);
	if(loanValue>0 && grantValue>0)
	{
		bootbox.alert("Both loan and grant amount cannot be specified in a row");
		obj.value='0';
		loanValue=parseFloat(document.getElementById(loanObjId).value);
		grantValue=parseFloat(document.getElementById(grantObjId).value);
	}
	document.getElementById(percentObjId).value=calculatePercentage(loanValue+grantValue,denom);
}
function calculatePercentage(numerator,denomerator)
{
	var result;
	if(numerator==0||denomerator==0)
		return 0;
	else
	{
		result=(numerator/denomerator)*100;
		return result.toFixed(2);
	}
}
function validateAmounts(obj)
{
	var projectCost=document.getElementById('projectCost').value;
	var sanctionedCost=document.getElementById('sanctionedCost').value;
	var revisedCost=document.getElementById('revisedCost').value;
	if(obj.id=='projectCost')
	{
		document.getElementById('sanctionedCost').value='0';
		document.getElementById('revisedCost').value='0';
		return;
	}
	if(projectCost==undefined||projectCost==''||parseFloat(projectCost)==0)
	{
		bootbox.alert("Please enter Project Cost first");
		obj.value='0';
		return;
	}
	if(obj.id=='sanctionedCost')
	{
		if(parseFloat(sanctionedCost)>parseFloat(projectCost))
		{
			bootbox.alert("Sanctioned Cost cannot be greater than Project Cost");
			obj.value='0';
			return;
		}
	}
	if(obj.id=='revisedCost')
	{
		if(parseFloat(revisedCost)!=0 && parseFloat(revisedCost)<=parseFloat(projectCost))
		{
			bootbox.alert("Revised Cost cannot be less than or equal to  Project Cost");
			obj.value='0';
			return;
		}
	}
}
function calculateAllPercentages()
{
	var projectCost=document.getElementById('projectCost').value;
	var sanctionedCost=document.getElementById('sanctionedCost').value;
	var revisedCost=document.getElementById('revisedCost').value;
	var unsanctionedCost;
	var i, percentCalculated;
	var sanctionedLAmount,sanctionedGAmount;
	var unsanctionedLAmount,unsanctionedGAmount;
	var revisedLAmount,revisedGAmount;
	var percentDenom;
	if(projectCost==undefined || projectCost=='')
	{
		projectCost=0;
	}
	else
		projectCost=parseFloat(projectCost);
	if(sanctionedCost==undefined || sanctionedCost=='')
	{
		sanctionedCost=0;
	}
	else
		sanctionedCost=parseFloat(sanctionedCost);
	if(revisedCost==undefined || revisedCost=='')
	{
		revisedCost=0;
	}
	else
		revisedCost=parseFloat(revisedCost);
	unsanctionedCost=projectCost-sanctionedCost;
	if(revisedCost!=0)
		percentDenom=revisedCost;
	else
		percentDenom=projectCost;
	if(percentDenom==0)
	{
		for( i=0;i<sanctionedAmountTableIndex;i++)
		{
			if(document.getElementById("sanctionedAmountLGDetails["+i+"].loanAmount")!=null)
			{
				document.getElementById("sanctionedAmountLGDetails["+i+"].loanAmount").value='0';
				document.getElementById("sanctionedAmountLGDetails["+i+"].grantAmount").value='0';
				document.getElementById("sanctionedAmountLGDetails["+i+"].percentage").value='0';
			}
		}
		for( i=0;i<unsanctionedAmountTableIndex;i++)
		{
			if(document.getElementById("unsanctionedAmountLGDetails["+i+"].loanAmount")!=null)
			{
				document.getElementById("unsanctionedAmountLGDetails["+i+"].loanAmount").value='0';
				document.getElementById("unsanctionedAmountLGDetails["+i+"].grantAmount").value='0';
				document.getElementById("unsanctionedAmountLGDetails["+i+"].percentage").value='0';
			}
		}		
		for( i=0;i<revisedAmountTableIndex;i++)
		{
			if(document.getElementById("revisedAmountLGDetails["+i+"].loanAmount")!=null)
			{
				document.getElementById("revisedAmountLGDetails["+i+"].loanAmount").value='0';
				document.getElementById("revisedAmountLGDetails["+i+"].grantAmount").value='0';
				document.getElementById("revisedAmountLGDetails["+i+"].percentage").value='0';
			}
		}
	}
	if(percentDenom!=0)
	{
		for( i=0;i<sanctionedAmountTableIndex;i++)
		{
			if(document.getElementById("sanctionedAmountLGDetails["+i+"].loanAmount")!=null)
			{
				sanctionedLAmount=document.getElementById('sanctionedAmountLGDetails['+i+'].loanAmount').value;
				sanctionedGAmount= document.getElementById('sanctionedAmountLGDetails['+i+'].grantAmount').value;
				if(sanctionedLAmount==undefined || sanctionedLAmount=='')
				{
					sanctionedLAmount=0;
				}
				else
					sanctionedLAmount=parseFloat(sanctionedLAmount);
				if(sanctionedGAmount==undefined || sanctionedGAmount=='')
				{
					sanctionedGAmount=0;
				}
				else
					sanctionedGAmount=parseFloat(sanctionedGAmount);
				percentCalculated=calculatePercentage(sanctionedGAmount+sanctionedLAmount,percentDenom);
				if(percentCalculated>100)
				{
					document.getElementById('sanctionedAmountLGDetails['+i+'].loanAmount').value='0';
					document.getElementById('sanctionedAmountLGDetails['+i+'].grantAmount').value='0';
					document.getElementById('sanctionedAmountLGDetails['+i+'].percentage').value='0';
				}
				else
					document.getElementById('sanctionedAmountLGDetails['+i+'].percentage').value=percentCalculated;
			}
		 }
		for( i=0;i<unsanctionedAmountTableIndex;i++)
		{
			if(document.getElementById("unsanctionedAmountLGDetails["+i+"].loanAmount")!=null)
			{
				unsanctionedLAmount=document.getElementById('unsanctionedAmountLGDetails['+i+'].loanAmount').value;
				unsanctionedGAmount= document.getElementById('unsanctionedAmountLGDetails['+i+'].grantAmount').value;
				if(unsanctionedLAmount==undefined || unsanctionedLAmount=='')
				{
					unsanctionedLAmount=0;
				}
				else
					unsanctionedLAmount=parseFloat(unsanctionedLAmount);
				if(unsanctionedGAmount==undefined || unsanctionedGAmount=='')
				{
					unsanctionedGAmount=0;
				}
				else
					unsanctionedGAmount=parseFloat(unsanctionedGAmount);
				percentCalculated=calculatePercentage(unsanctionedGAmount+unsanctionedLAmount,percentDenom);
				if(percentCalculated>100)
				{
					document.getElementById('unsanctionedAmountLGDetails['+i+'].loanAmount').value='0';
					document.getElementById('unsanctionedAmountLGDetails['+i+'].grantAmount').value='0';
					document.getElementById('unsanctionedAmountLGDetails['+i+'].percentage').value='0';
				}
				else
					document.getElementById('unsanctionedAmountLGDetails['+i+'].percentage').value=percentCalculated;
			}
		}
		for( i=0;i<revisedAmountTableIndex;i++)
		{
			if(document.getElementById("revisedAmountLGDetails["+i+"].loanAmount")!=null)
			{
				revisedLAmount=document.getElementById('revisedAmountLGDetails['+i+'].loanAmount').value;
				revisedGAmount= document.getElementById('revisedAmountLGDetails['+i+'].grantAmount').value;
				if(revisedLAmount==undefined || revisedLAmount=='')
				{
					revisedLAmount=0;
				}
				else
					revisedLAmount=parseFloat(revisedLAmount);
				if(revisedGAmount==undefined || revisedGAmount=='')
				{
					revisedGAmount=0;
				}
				else
					revisedGAmount=parseFloat(revisedGAmount);
				percentCalculated=calculatePercentage(revisedLAmount+revisedGAmount,percentDenom);
				if(percentCalculated>100)
				{
					document.getElementById('revisedAmountLGDetails['+i+'].loanAmount').value='0';
					document.getElementById('revisedAmountLGDetails['+i+'].grantAmount').value='0';
					document.getElementById('revisedAmountLGDetails['+i+'].percentage').value='0';
				}
				else
					document.getElementById('revisedAmountLGDetails['+i+'].percentage').value=percentCalculated;
			}
		}
	}
}
function validateAmountsBeforeSubmit()
{
	var projectCost=document.getElementById('projectCost').value;
	var sanctionedCost=document.getElementById('sanctionedCost').value;
	var revisedCost=document.getElementById('revisedCost').value;
	if(projectCost==undefined || projectCost==''|| sanctionedCost==undefined|| sanctionedCost=='')
	{
		bootbox.alert( bootbox.alert("Project Cost or Sanctioned Cost cannot be empty"));
		return false;
	}
	projectCost=parseFloat(projectCost);
	sanctionedCost=parseFloat(sanctionedCost);
	if(projectCost==0 )
	{
		bootbox.alert("Project Cost cannot be 0");
		return false;
	}
	if(sanctionedCost>projectCost)
	{
		bootbox.alert("Sanctioned Cost cannot be greater than Project Cost");
		document.getElementById('sanctionedCost').value='0';
		return false;
	}
	var totalsanctionedloan=document.getElementById('totalsanctionedloan').value;
	var totalsanctionedgrant=document.getElementById('totalsanctionedgrant').value;
	var totalunsanctionedloan=document.getElementById('totalunsanctionedloan').value;
	var totalunsanctionedgrant=document.getElementById('totalunsanctionedgrant').value;
	if((totalsanctionedloan==undefined || totalsanctionedloan=='')&& (totalsanctionedgrant==undefined|| totalsanctionedgrant==''))
	{
		bootbox.alert("Please enter sanctioned amount details");
		return false;
	}
	totalsanctionedloan=parseFloat(totalsanctionedloan);
	totalsanctionedgrant=parseFloat(totalsanctionedgrant);
	if((totalsanctionedgrant+totalsanctionedloan)!=sanctionedCost)
	{
		bootbox.alert("Total of sanctioned loans and grants does not match Sanctioned Project Cost ");
		return false;
	}
	var unsanctionedCost=projectCost-sanctionedCost;
	if(unsanctionedCost!=0)
	{
		if((totalunsanctionedloan==undefined || totalunsanctionedloan=='')&& (totalunsanctionedgrant==undefined|| totalunsanctionedgrant==''))
		{
			bootbox.alert("Please enter unsanctioned amount details");
			return false;
		}
		totalunsanctionedloan=parseFloat(totalunsanctionedloan);
		totalunsanctionedgrant=parseFloat(totalunsanctionedgrant);
		if((totalunsanctionedgrant+totalunsanctionedloan)==0)
		{
			bootbox.alert("Total of unsanctioned loans and grants cannot be 0");
			return false;
		}
		if((totalunsanctionedgrant+totalunsanctionedloan)!=unsanctionedCost)
		{
			bootbox.alert("Total of unsanctioned loans and grants does not match Unsanctioned Project Cost ");
			return false;
		}
	}
	else
	{
		// when unsanctioned cost is 0 then don't accept unsanctioned amount details
		if((totalunsanctionedloan!=undefined && totalunsanctionedloan!='')|| (totalunsanctionedgrant!=undefined|| totalunsanctionedgrant!=''))
		{
			totalunsanctionedloan=parseFloat(totalunsanctionedloan);
			totalunsanctionedgrant=parseFloat(totalunsanctionedgrant);
			if((totalunsanctionedgrant+totalunsanctionedloan)!=unsanctionedCost)
			{
				bootbox.alert("Total of unsanctioned loans and grants does not match Unsanctioned Project Cost ");
				return false;
			}
		}
	}
	var totalrevisedloan=document.getElementById('totalrevisedloan').value;
	var totalrevisedgrant=document.getElementById('totalrevisedgrant').value;
	if(revisedCost!=undefined && revisedCost!='')
	{
		revisedCost=parseFloat(revisedCost);
		if(revisedCost<=projectCost)
		{
			bootbox.alert("Revised Project Cost cannot be less than or equal to Project Cost");
			document.getElementById('revisedCost').value='';
			return false;
		}
		if(revisedCost!=0)
		{
			if((totalrevisedloan==undefined || totalrevisedloan=='')&& (totalrevisedgrant==undefined|| totalrevisedgrant==''))
			{
				bootbox.alert("Please enter revised amount funding details");
				return false;
			}
			totalrevisedloan=parseFloat(totalrevisedloan);
			totalrevisedgrant=parseFloat(totalrevisedgrant);
			if((totalrevisedloan+totalrevisedgrant)==0)
			{
				bootbox.alert("Total of revised loans and grants cannot be 0");
				return false;
			}
			if((totalrevisedloan+totalrevisedgrant)!=(revisedCost-projectCost))
			{
				bootbox.alert("Total of revised loans and grants does not match Revised Project Cost ");
				return false;
			}
		}
		else
		{
			// when revised cost is 0 then don't accept revised amount details
			if((totalrevisedloan!=undefined && totalrevisedloan!='')|| (totalrevisedgrant!=undefined|| totalrevisedgrant!=''))
			{
				totalrevisedloan=parseFloat(totalrevisedloan);
				totalrevisedgrant=parseFloat(totalrevisedgrant);
				if((totalrevisedloan+totalrevisedgrant)!=0)
				{
					bootbox.alert("Total of revised loans and grants does not match Revised Project Cost ");
					return false;
				}
			}
		}
	}
	else
	{
		// when revised cost is 0 then don't accept revised amount details
		if((totalrevisedloan!=undefined && totalrevisedloan!='')|| (totalrevisedgrant!=undefined|| totalrevisedgrant!=''))
		{
			totalrevisedloan=parseFloat(totalrevisedloan);
			totalrevisedgrant=parseFloat(totalrevisedgrant);
			if((totalrevisedloan+totalrevisedgrant)!=0)
			{
				bootbox.alert("Total of revised loans and grants does not match Revised Project Cost ");
				return false;
			}
		}
	}
	return true;
}
function emptyRevisedCostIfZero(obj)
{
	if(parseFloat(obj.value)==0)
		obj.value='';
}
	function validateInputs()
	{
		var councilResNoObj=document.getElementById('councilResNo');
		var govtOrderNoObj=document.getElementById('govtOrderNo');
		var amendmentNoObj=document.getElementById('amendmentNo');
		var councilResDateObj=document.getElementById('councilResDate');
		var govtOrderDateObj=document.getElementById('govtOrderDate');
		var amendmentDateObj=document.getElementById('amendmentDate');
		var bankBranchObj=document.getElementById('bank_branch');
		var bankAccountObj=document.getElementById('bankaccount');
		var schemeNameObj=document.getElementById('subScheme.scheme.name');
		var schemeIdObj=document.getElementById('schemeId');
		var subSchemeIdObj=document.getElementById('subSchemeId');
		var subSchemeNameObj=document.getElementById('subScheme.name');
		var obj1,obj2,obj3;
		if(document.getElementById('fundId').value==-1)
		{
			bootbox.alert("Please select a fund ");
			return false;
		}
		if(document.getElementById('codeuniquecode').style.display !='none')
		{
			bootbox.alert("Sub Scheme has been already mapped to another loan grant header");
			return false;
		}
		if(bankBranchObj.value==-1)
		{
			bootbox.alert("Please enter Bank Branch");
			return false;
		}
		if(bankAccountObj.value==-1)
		{
			bootbox.alert("Please enter Bank Account");
			return false;
		}
		if(schemeNameObj.value==''||schemeIdObj.value=='')
		{
			bootbox.alert("Please enter Scheme");
			return false;
		}
		if(subSchemeIdObj.value==''||subSchemeNameObj.value=='')
		{
			bootbox.alert("Please enter Sub Scheme");
			return false;
		}
		if(councilResNoObj.value=='')
		{
			bootbox.alert("Please enter Council Resolution Number");
			return false;
		}
		if(govtOrderNoObj.value=='')
		{
			bootbox.alert("Please enter Government Order Number");
			return false;
		}
		if(amendmentNoObj.value=='')
		{
			bootbox.alert("Please enter Amendment Number");
			return false;
		}
		if(councilResDateObj.value=='')
		{
			bootbox.alert("Please enter Council Resolution Date");
			return false;
		}
		if(govtOrderDateObj.value=='')
		{
			bootbox.alert("Please enter Government Order Date");
			return false;
		}
		if(amendmentDateObj.value=='')
		{
			bootbox.alert("Please enter Amendment Date");
			return false;
		}
		for( i=0;i<projectCodeDetailTableIndex;i++)
		{
			isPCIdPresent=false;
			isPCCodePresent=false;
			obj1=document.getElementById(PROJECTCODELIST+'['+i+'].id');
			obj2=document.getElementById(PROJECTCODELIST+'['+i+'].code');
			if(obj1!=null && obj1.value=='' && obj2!=null && obj2.value=='')
			{
				bootbox.alert("Please enter values for Project Code for row "+(i+1)+" or delete the row");
				return false;
			}
			if(obj1!=null && obj1.value!='')
				isPCIdPresent=true;
			if(obj2!=null && obj2.value!='')
				isPCCodePresent=true;
			if((isPCIdPresent || isPCCodePresent)&& !(isPCIdPresent && isPCCodePresent))
			{
				bootbox.alert("Please re-enter Project Code for row "+(i+1));
				return false;
			}
			
		}
		for( i=0;i<sanctionedAmountTableIndex;i++)
		{
			isFundSelected=false;
			isLGAmountPresent=false;
			obj1=document.getElementById(SANCTIONEDAMOUNTLIST+'['+i+'].fundingAgency.id');
			obj2=document.getElementById(SANCTIONEDAMOUNTLIST+'['+i+'].loanAmount');
			obj3=document.getElementById(SANCTIONEDAMOUNTLIST+'['+i+'].grantAmount');
			if(obj1!=null && obj1.value!=-1  && obj1.value!=0 && obj1.value!='')
				isFundSelected=true;
			if((obj2!=null && obj2.value!=''&& parseFloat(obj2.value)!=0 ) || (obj3!=null && obj3.value!=''&& parseFloat(obj3.value)!=0))
				isLGAmountPresent=true;
			if((isFundSelected || isLGAmountPresent)&& !(isFundSelected && isLGAmountPresent))
			{
				bootbox.alert("Please enter both Funding Agency and Loan Amount or Grant Amount for Sanctioned amount List for row "+(i+1));
				return false;
			}
			
		}
		for( i=0;i<unsanctionedAmountTableIndex;i++)
		{
			isFundSelected=false;
			isLGAmountPresent=false;
			obj1=document.getElementById(UNSANCTIONEDAMOUNTLIST+'['+i+'].fundingAgency.id');
			obj2=document.getElementById(UNSANCTIONEDAMOUNTLIST+'['+i+'].loanAmount');
			obj3=document.getElementById(UNSANCTIONEDAMOUNTLIST+'['+i+'].grantAmount');
			if(obj1!=null && obj1.value!=-1  && obj1.value!=0 && obj1.value!='')
				isFundSelected=true;
			if((obj2!=null && obj2.value!=''&& parseFloat(obj2.value)!=0 ) || (obj3!=null && obj3.value!=''&& parseFloat(obj3.value)!=0))
				isLGAmountPresent=true;
			if((isFundSelected || isLGAmountPresent)&& !(isFundSelected && isLGAmountPresent))
			{
				bootbox.alert("Please enter both Funding Agency and Loan Amount or Grant Amount for Unsanctioned amount List for row "+(i+1));
				return false;
			}
			
		}
		for( i=0;i<revisedAmountTableIndex;i++)
		{
			isFundSelected=false;
			isLGAmountPresent=false;
			obj1=document.getElementById(REVISEDAMOUNTLIST+'['+i+'].fundingAgency.id');
			obj2=document.getElementById(REVISEDAMOUNTLIST+'['+i+'].loanAmount');
			obj3=document.getElementById(REVISEDAMOUNTLIST+'['+i+'].grantAmount');
			if(obj1!=null && obj1.value!=-1 && obj1.value!=0 && obj1.value!='')
				isFundSelected=true;
			if((obj2!=null && obj2.value!=''&& parseFloat(obj2.value)!=0 ) || (obj3!=null && obj3.value!=''&& parseFloat(obj3.value)!=0))
				isLGAmountPresent=true;
			if((isFundSelected || isLGAmountPresent)&& !(isFundSelected && isLGAmountPresent))
			{
				bootbox.alert("Please enter both Funding Agency and Loan Amount or Grant Amount for Revised amount List for row "+(i+1));
				return false;
			}
			
		}
		return validateAmountsBeforeSubmit();
	}
	function checkDateLG(obj)
	{
		var dat=validateDate(obj.value);
		if (!dat) 
		{
			bootbox.alert('Invalid date format : Enter Date as dd/mm/yyyy');
			obj.value="";
			return;
		}
	}
