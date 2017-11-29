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
var VOUCHERDETAILLIST='billDetailslist';
var VOUCHERREBATEDETAILLIST='billRebateDetailslist';
var VOUCHERCREDITDETAILLIST='billCreditDetailslist';
var SUBLEDGERLIST='subLedgerlist';
var VOUCHERREBATEDETAILTABLE='rebateDetailsTable';
var VOUCHERCREDITDETAILTABLE='billCreditDetailsTable';
var billDetailsTable;
var subLedgersTable;
var billDetailTableIndex = 0;
var path;
var slDetailTableIndex = 0;
var rebateDetailTableIndex=0;
var oAutoCompEntity;
var detailTypeId=0;
var codeTextMessage='Enter 3 letters';

function resetTables(){

    var length = billCreditDetailsTable.getRecordSet().getLength(); 
        if(length > 1) { 
            var count = (length > 2) ? length : 2; 
            billCreditDetailsTable.deleteRows(1,count); 
        } 
        for(var i=0;i<billDetailTableIndex+1;i++)
		{
			if(null != document.getElementById('billCreditDetailslist['+i+'].creditAmountDetail')){
				document.getElementById('billCreditDetailslist['+i+'].creditAmountDetail').value=0;
	
			}
		}
  var rlength = rebateDetailsTable.getRecordSet().getLength(); 
       if(rlength > 1) { 
            var rcount = (rlength > 2) ? rlength : 2; 
            rebateDetailsTable.deleteRows(1,rcount); 
        } 
        
        for(var i=0;i<rebateDetailTableIndex+1;i++)
		{
			if(null != document.getElementById('billRebateDetailslist['+i+'].debitAmountDetail')){
				document.getElementById('billRebateDetailslist['+i+'].debitAmountDetail').value=0;
	
			}
		}
  var slength = subLedgersTable.getRecordSet().getLength(); 
        if(slength > 1) { 
            var scount = (slength > 2) ? slength : 2; 
            subLedgersTable.deleteRows(1,scount); 
        } 
	 for (var j=0; j<slDetailTableIndex+1; j++ )
		{
			if(document.getElementById('subLedgerlist['+j+'].glcode.id')!=null){
				document.getElementById('subLedgerlist['+j+'].amount').value=0;
				document.getElementById('subLedgerlist['+j+'].detailCode').placeholder=codeTextMessage;
				document.getElementById('subLedgerlist['+j+'].detailCode').style.color='DarkGray';
				document.getElementById('subLedgerlist['+j+'].glcode.id').value=0;
				document.getElementById('subLedgerlist['+j+'].detailType.id').value=0;
				var glcodedropdown=document.getElementById('subLedgerlist['+j+'].glcode.id');
				var detailTypedropdown=document.getElementById('subLedgerlist['+j+'].detailType.id');
				for(k=glcodedropdown.options.length-1;k>=1;k--)
				{
					glcodedropdown.remove(k);
				}
				for(l=detailTypedropdown.options.length-1;l>=1;l--)
				{
					detailTypedropdown.remove(l);
				}
			}
		}
}
function updateGridMisc(prefix,field,index,value){
	document.getElementById(prefix+'['+index+'].'+field).value=value;
}

function updateSLGrid(field,index,value){
	if(field=='detailCode' && value==''){
		document.getElementById('subLedgerlist['+index+'].'+field).placeholder=codeTextMessage;
		document.getElementById('subLedgerlist['+index+'].'+field).style.color='DarkGray';
	}
	else{
		document.getElementById('subLedgerlist['+index+'].'+field).value=value;
	}
}

function updateGridSLDropdown(field,index,value,text){
	var element = document.getElementById('subLedgerlist['+index+'].'+field)
	if(value != "" ){
	element.options.length=2;
	element.options[1].text=text;
	element.options[1].value=value;
	element.options[1].selected = true;
	}
}
function createTextFieldFormatterCredit(prefix,suffix,type,table){

    return function(el, oRecord, oColumn, oData) {
    var rec='';
   
if(table=='billCreditDetailsTable')
rec=billDetailTableIndex;
else 
rec=rebateDetailTableIndex;
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='"+type+"' id='"+prefix+"["+rec+"]"+suffix+"' name='"+prefix+"["+rec+"]"+suffix+"' style='width:90;' readOnly tabindex='-1' />";
	
	}
}
function createTextFieldFormatterForFunctionCredit(prefix,suffix,table){

    return function(el, oRecord, oColumn, oData) {
     var rec='';
   
if(table=='billCreditDetailsTable')
rec=billDetailTableIndex;
else 
rec=rebateDetailTableIndex;
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+rec+"]"+suffix+"' name='"+prefix+"["+rec+"]"+suffix+"' style='width:90px;' onfocus='autocompletecodeFunctionCredit(this,event)' onblur='fillNeibrAfterSplitFunctionCredit(this)' />";
	}
}

function createTextFieldFormatterForFunctionRebate(prefix,suffix,table){

    return function(el, oRecord, oColumn, oData) {
     var rec='';
   
if(table=='billCreditDetailsTable')
rec=billDetailTableIndex;
else 
rec=rebateDetailTableIndex;
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+rec+"]"+suffix+"' name='"+prefix+"["+rec+"]"+suffix+"' style='width:90px;' onfocus='autocompletecodeFunctionRebate(this,event)' onblur='fillNeibrAfterSplitFunctionRebate(this)' />";
	
	}
}


function createTextFieldFormatterRebate(prefix,suffix,type,table){

    return function(el, oRecord, oColumn, oData) {
    var rec='';
   
if(table=='billCreditDetailsTable')
rec=billDetailTableIndex;
else 
rec=rebateDetailTableIndex;
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='"+type+"' id='"+prefix+"["+rec+"]"+suffix+"' name='"+prefix+"["+rec+"]"+suffix+"' style='width:90px;' readOnly tabindex='-1'/>";
	}
}

function createLongTextFieldFormatterCredit(prefix,suffix,table){

    return function(el, oRecord, oColumn, oData) {
     var rec='';
   
if(table=='billCreditDetailsTable')
rec=billDetailTableIndex;
else 
rec=rebateDetailTableIndex;
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+rec+"]"+suffix+"' name='"+prefix+"["+rec+"]"+suffix+"' style='width:400px;' onfocus='autocompletecode(this,event)' autocomplete='off' onblur='fillNeibrAfterSplitGlcodeCredit(this)' />";
	}
}
function createLongTextFieldFormatterRebate(prefix,suffix,table){

    return function(el, oRecord, oColumn, oData) {
     var rec='';
   
if(table=='billCreditDetailsTable')
rec=billDetailTableIndex;
else 
rec=rebateDetailTableIndex;
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+rec+"]"+suffix+"' name='"+prefix+"["+rec+"]"+suffix+"' style='width:400px;' onfocus='autocompletecodeRebate(this,event)' autocomplete='off' onblur='fillNeibrAfterSplitGlcodeRebate(this)' />";
	}
}


function createAmountFieldFormatterRebate(prefix,suffix,onblurfunction,table){
    return function(el, oRecord, oColumn, oData) {
     var rec='';
   
	if(table=='billCreditDetailsTable'){
		rec=billDetailTableIndex;
	}
	else{ 
		rec=rebateDetailTableIndex;
	}

		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+rec+"]"+suffix+"' name='"+prefix+"["+rec+"]"+suffix+"' style='text-align:right;width:80px;' maxlength='13' class='form-control patternvalidation text-right' data-pattern='number' onblur='"+onblurfunction+";updatetotalAmount()'/>";
	}
}

function createSLTextFieldFormatter(prefix,suffix,onblurfunction){
    return function(el, oRecord, oColumn, oData) {
		el.innerHTML = "<input type='text' id='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' name='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' style='width:90px;' tabindex='-1' onblur='"+onblurfunction+"'/>";
	}
}
function createSLHiddenFieldFormatter(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		el.innerHTML = "<input type='text' id='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' name='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' tabindex='-1'/>";
	}
}

function createSLLongTextFieldFormatter(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' name='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' readOnly style='width:180px;' tabindex='-1'/>";
	}
}
function createSLAmountFieldFormatter(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' name='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' tabindex='-1' maxlength='13' style='text-align:right;width:90px;' class='form-control patternvalidation text-right' data-pattern='number'/>";
	}
}
function createTextFieldReadOnly(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+billDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+billDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"' tabindex='-1' readOnly style='width:90px;'/>";
	}
}
function createcheckbox(prefix,suffix,onclickfunction){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='checkbox' id='"+prefix+"["+billDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+billDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"'  style='width:250px;'  onClick='"+onclickfunction+"'/>";
	}
}

function createSLDetailCodeTextFieldFormatter(prefix,suffix,onblurfunction){
	 return function(el, oRecord, oColumn, oData) {
				el.innerHTML = "<input type='text' id='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' name='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' style='width:90px;' onkeyup='autocompleteForEntity(this,event)' autocomplete='off' tabindex='-1' onblur = 'waterMarkTextOut(\""+prefix+"["+slDetailTableIndex+"]"+suffix+"\",\""+codeTextMessage+"\");"+onblurfunction+"' onfocus='onFocusDetailCode(this);waterMarkTextIn(\""+prefix+"["+slDetailTableIndex+"]"+suffix+"\",\""+codeTextMessage+"\");' />";
				
			}
	}




var codeObj;
var acccodeArray;
function loadDropDownCodes()
{
	var	url = "/EGF/voucher/common-ajaxGetAllCoaNames.action";
	var req2 = initiateRequest();
	req2.onreadystatechange = function()
	{
	  if (req2.readyState == 4)
	  {
		  if (req2.status == 200)
		  {
			var codes2=req2.responseText;
			var a = codes2.split("^");
			var codes = a[0];
			acccodeArray=codes.split("+");
			codeObj = new YAHOO.widget.DS_JSArray(acccodeArray);
		  }
	  }
 	};
	req2.open("GET", url, true);
	req2.send(null);
}

var rebatecodeObj;
var rebacccodeArray;
function loadDropDownRebateCodes()
{
	var	url = "/EGF/voucher/common-ajaxGetAllCoaNames.action";
	var req3 = initiateRequest();
	req3.onreadystatechange = function()
	{
	  if (req3.readyState == 4)
	  {
		  if (req3.status == 200)
		  {
			var codes2=req3.responseText;
			var a = codes2.split("^");
			var codes = a[0];
			rebacccodeArray=codes.split("+");
			rebatecodeObj = new YAHOO.widget.DS_JSArray(rebacccodeArray);
		  }
	  }
 	};
	req3.open("GET", url, true);
	req3.send(null);
}

var funcObj;
var funcArray;
function loadDropDownCodesFunction()
{
	var url = "/EGF/voucher/common-ajaxGetAllFunctionName.action";
	var req4 = initiateRequest();
	req4.onreadystatechange = function()
	{
	  if (req4.readyState == 4)
	  {
		  if (req4.status == 200)
		  {
			var codes2=req4.responseText;
			var a = codes2.split("^");
			var codes = a[0];
			funcArray=codes.split("+");
			funcObj= new YAHOO.widget.DS_JSArray(funcArray);
		  }
	   }
	};
	req4.open("GET", url, true);
	req4.send(null);
}


function autocompletecodeFunctionCredit(obj,myEvent)
{
	
	var src = obj;	
	var target = document.getElementById('codescontainer');	
	
	var posSrc=findPos(src); 
	target.style.left=posSrc[0]+"px";	
	target.style.top=posSrc[1]-40+"px";
	target.style.width=650;	
		
	var fObj=obj;
	//var  currRow=getRowIndex(obj);

	//40 --> Down arrow, 38 --> Up arrow
	//if(yuiflagFunc[currRow] == undefined)
	//{
		var key = window.event ? window.event.keyCode : myEvent.charCode;  
		if(key != 40 )
		{
			if(key != 38 )
			{
				var oAutoComp = new YAHOO.widget.AutoComplete(fObj,'codescontainer', funcObj);
				oAutoComp.queryDelay = 0;
				oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
				oAutoComp.useShadow = true;
				oAutoComp.maxResultsDisplayed = 15;
				oAutoComp.useIFrame = true;
				funcObj.applyLocalFilter = true;
				funcObj.queryMatchContains = true;
				oAutoComp.formatResult = function(oResultData, sQuery, sResultMatch) {
					var data = oResultData.toString();
				    return data.split("`~~`")[0];
				};
			}
		}
		//yuiflagFunc[currRow] = 1;
	//}	
}

function autocompletecodeFunctionRebate(obj,myEvent)
{
	
	var src = obj;	
	var target = document.getElementById('codescontainer');	
	
	var posSrc=findPos(src); 
	target.style.left=posSrc[0]+"px";	
	target.style.top=posSrc[1]-40+"px";
	target.style.width=650;	
		
	var fObj=obj;
	//var  currRow=getRowIndex(obj);

	//40 --> Down arrow, 38 --> Up arrow
	//if(yuiflagFunc[currRow] == undefined)
	//{
		var key = window.event ? window.event.keyCode : myEvent.charCode;  
		if(key != 40 )
		{
			if(key != 38 )
			{
				var oAutoComp = new YAHOO.widget.AutoComplete(fObj,'codescontainer', funcObj);
				oAutoComp.queryDelay = 0;
				oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
				oAutoComp.useShadow = true;
				oAutoComp.maxResultsDisplayed = 15;
				oAutoComp.useIFrame = true;
				funcObj.applyLocalFilter = true;
				funcObj.queryMatchContains = true;
				oAutoComp.formatResult = function(oResultData, sQuery, sResultMatch) {
					var data = oResultData.toString();
				    return data.split("`~~`")[0];
				};
			}
		}
		//yuiflagFunc[currRow] = 1;
	//}	
}

function fillNeibrAfterSplitFunctionCredit(obj)
{
	var currRow=getRowIndex(obj);
	var temp = obj.value;
	temp = temp.split("`~~`");
	if(temp.length>1)
	{ 
		var temp1=temp[0];
		temp1=temp1.split("`~`");
		obj.value=temp1[0];
		document.getElementById('billCreditDetailslist['+currRow+'].functionIdDetail').value=temp[1];
	}else if(temp == ''){
		obj.value='';
		document.getElementById('billCreditDetailslist['+currRow+'].functionIdDetail').value='';
	}
	
	
}
function fillNeibrAfterSplitFunctionRebate(obj)
{
	var currRow=getRowIndex(obj);
	var temp = obj.value;
	temp = temp.split("`~~`");
	if(temp.length>1)
	{ 
		var temp1=temp[0];
		temp1=temp1.split("`~`");
		obj.value=temp1[0];
		document.getElementById('billRebateDetailslist['+currRow+'].functionIdDetail').value=temp[1];
	}else if(temp == ''){
		obj.value='';
		document.getElementById('billRebateDetailslist['+currRow+'].functionIdDetail').value='';
	}
	
	
}

var yuiflag = new Array();
var yuiflag1 = new Array();
function autocompletecode(obj,myEvent)
{	var src = obj;	
	var target = document.getElementById('codescontainer');	
	var posSrc=findPos(src); 
	target.style.left=posSrc[0];	
	target.style.top=posSrc[1]-40;
	target.style.width=450;	
	codeObj
	var coaCodeObj=obj;
		var  currRow=getRowIndex(obj);
	//40 --> Down a+rrow, 38 --> Up arrow
	if(yuiflag[currRow] == undefined)
	{
		var key = window.event ? window.event.keyCode : myEvent.charCode;  
		if(key != 40 )
		{
			if(key != 38 )
			{
				var oAutoComp = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', codeObj);
				oAutoComp.queryDelay = 0;
				oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
				oAutoComp.useShadow = true;
				oAutoComp.maxResultsDisplayed = 15;
				oAutoComp.useIFrame = true;
				codeObj.applyLocalFilter = true;
				codeObj.queryMatchContains = true;
				oAutoComp.minQueryLength = 0;
			}
		}
		yuiflag[currRow] = 1;
	}	
}
function getRow(obj){
	if(!obj)return null;
	tag = obj.nodeName.toUpperCase();
	while(tag != 'BODY'){
		if (tag == 'TR') return obj;
		obj=obj.parentNode;
		tag = obj.nodeName.toUpperCase();
	}
	return null;
}

function autocompletecodeRebate(obj,myEvent)
{

	var src = obj;	
	var target = document.getElementById('rebatecodescontainer');	
	var posSrc=findPos(src); 
	target.style.left=posSrc[0];	
	target.style.top=posSrc[1]-40;
	target.style.width=450;	
		
	
	var coaCodeObj=obj;
		var  currRow=getRowIndex(obj);
	//40 --> Down a+rrow, 38 --> Up arrow
	if(yuiflag1[currRow] == undefined)
	{
		var key = window.event ? window.event.keyCode : myEvent.charCode;  
		if(key != 40 )
		{
			if(key != 38 )
			{
				var oAutoComp = new YAHOO.widget.AutoComplete(coaCodeObj,'rebatecodescontainer', rebatecodeObj);
				oAutoComp.queryDelay = 0;
				oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
				oAutoComp.useShadow = true;
				oAutoComp.maxResultsDisplayed = 15;
				oAutoComp.useIFrame = true;
				rebatecodeObj.applyLocalFilter = true;
				rebatecodeObj.queryMatchContains = true;
				oAutoComp.minQueryLength = 0;
			}
		}
		yuiflag1[currRow] = 1;
	}	
}



function getRowIndex(obj)
{
	var temp =obj.name.split('[');
	var temp1 = temp[1].split(']');
	return temp1[0];
}
function fillNeibrAfterSplitGlcodeCredit(obj)
{
	var temp = obj.value;
	temp = temp.split("`-`");
	var currRow=getRowIndex(obj);
	var glcodeId = document.getElementById('billCreditDetailslist['+currRow+'].glcodeIdDetail').value;
	if(temp.length>1)
	{ 
		obj.value=temp[0];
		document.getElementById('billCreditDetailslist['+currRow+'].glcodeIdDetail').value=temp[2];
		document.getElementById('billCreditDetailslist['+currRow+'].glcodeDetail').value=temp[1];
		check();
	}
	else if(glcodeId==null || glcodeId==""){
		document.getElementById('billCreditDetailslist['+currRow+'].glcodeIdDetail').value="";
		document.getElementById('billCreditDetailslist['+currRow+'].glcodeDetail').value="";
		document.getElementById('billCreditDetailslist['+currRow+'].accounthead').value="";
	}
	
}

function fillNeibrAfterSplitGlcodeRebate(obj)
{

	var temp = obj.value;
	temp = temp.split("`-`");
	var currRow=getRowIndex(obj);
	if(temp.length>1)
	{ 
		obj.value=temp[0];
		document.getElementById('billRebateDetailslist['+currRow+'].glcodeIdDetail').value=temp[2];
		document.getElementById('billRebateDetailslist['+currRow+'].glcodeDetail').value=temp[1];
		check();
	}
	else{
		document.getElementById('billRebateDetailslist['+currRow+'].glcodeIdDetail').value="";
		document.getElementById('billRebateDetailslist['+currRow+'].glcodeDetail').value="";
		document.getElementById('billRebateDetailslist['+currRow+'].accounthead').value="";
	}
}

var lang=YAHOO.lang;

function createDropdownFormatterPJV(prefix){
    return function(el, oRecord, oColumn, oData) {
        var selectedValue = (lang.isValue(oData)) ? oData : oRecord.getData(oColumn.field),
            options = (lang.isArray(oColumn.dropdownOptions)) ?
                oColumn.dropdownOptions : null,
            selectEl,
            collection = el.getElementsByTagName("select");
        if(collection.length === 0) {
            selectEl = document.createElement("select");
            selectEl.className = YAHOO.widget.DataTable.CLASS_DROPDOWN;
            selectEl.name = prefix+'['+subLedgersTable.getRecordIndex(oRecord)+'].'+oColumn.getKey();
			selectEl.id = prefix+'['+subLedgersTable.getRecordIndex(oRecord)+'].'+oColumn.getKey();
			selectEl.tabIndex='-1';
            selectEl = el.appendChild(selectEl);
            YAHOO.util.Event.addListener(selectEl,"change",this._onDropdownChange,this);
			
        }

        selectEl = collection[0];

        if(selectEl) {
            selectEl.innerHTML = "";
            if(options) {
                for(var i=0; i<options.length; i++) {
                    var option = options[i];
                    var optionEl = document.createElement("option");
                    optionEl.value = (lang.isValue(option.value)) ?
                            option.value : option;
                    optionEl.innerHTML = (lang.isValue(option.text)) ?
                            option.text : (lang.isValue(option.label)) ? option.label : option;
                    optionEl = selectEl.appendChild(optionEl);
                    if (optionEl.value == selectedValue) {
                        optionEl.selected = true;
                    }
                }
            }
            else {
                selectEl.innerHTML = "<option selected value=\"" + selectedValue + "\">" + selectedValue + "</option>";
            }
        }
        else {
            el.innerHTML = lang.isValue(oData) ? oData : "";
        }
    }
}



function createDropdownFormatterDetailCode(prefix){
	 return function(el, oRecord, oColumn, oData) {
	        var selectedValue = (lang.isValue(oData)) ? oData : oRecord.getData(oColumn.field),
	            options = (lang.isArray(oColumn.dropdownOptions)) ?
	                oColumn.dropdownOptions : null,
	            selectEl,
	            collection = el.getElementsByTagName("select");
	        if(collection.length === 0) {
	            selectEl = document.createElement("select");
	            selectEl.className = YAHOO.widget.DataTable.CLASS_DROPDOWN;
	            selectEl.name = prefix+'['+slDetailTableIndex+'].'+oColumn.getKey();
				selectEl.id = prefix+'['+slDetailTableIndex+'].'+oColumn.getKey();
				selectEl.tabIndex='-1';
	            selectEl = el.appendChild(selectEl);
	            var selectedIndex = {value: slDetailTableIndex }; 
	            YAHOO.util.Event.addListener(selectEl,"change",onDropdownDetailCodeChange,selectedIndex,this);
				
	        }
	        selectEl = collection[0];
	        if(selectEl) {
	            selectEl.innerHTML = "";
	            if(options) {
	                for(var i=0; i<options.length; i++) {
	                    var option = options[i];
	                    var optionEl = document.createElement("option");
	                    optionEl.value = (lang.isValue(option.value)) ?
	                            option.value : option;
	                    optionEl.innerHTML = (lang.isValue(option.text)) ?
	                            option.text : (lang.isValue(option.label)) ? option.label : option;
	                    optionEl = selectEl.appendChild(optionEl);
	                    if (optionEl.value == selectedValue) {
	                        optionEl.selected = true;
	                    }
	                }
	            }
	            else {
	                selectEl.innerHTML = "<option selected value=\"" + selectedValue + "\">" + selectedValue + "</option>";
	            }
	        }
	        else {
	            el.innerHTML = lang.isValue(oData) ? oData : "";
	        }
	    }
}
	
	var onDropdownDetailCodeChange = function(index,obj) { 
		var detailtypeid = document.getElementById('subLedgerlist['+obj.value+'].detailType.id').value;
			var selecteddetailcode1=document.getElementById('subLedgerlist['+obj.value+'].detailCode').value;
			var url =  path+'/receipts/ajaxReceiptCreate-ajaxValidateDetailCodeNew.action?code='+selecteddetailcode1+'&detailtypeid='+detailtypeid+'&index='+obj.value;
			var transaction = YAHOO.util.Connect.asyncRequest('POST', url, postTypeDetailCode, null);
	};
	var postTypeDetailCode = {
	success: function(o) {
		var res= o.responseText;
		res = res.split('~');
		if(res.length>2)
		{
			document.getElementById(SUBLEDGERLIST+'['+parseInt(res[0])+']'+'.detailKeyId').value=res[1];
			document.getElementById(SUBLEDGERLIST+'['+parseInt(res[0])+']'+'.detailKey').value=res[2];
		}
		else
		{
			document.getElementById(SUBLEDGERLIST+'['+parseInt(res[0])+']'+'.detailKeyId').value='';
			document.getElementById(SUBLEDGERLIST+'['+parseInt(res[0])+']'+'.detailKey').value='';
			return;
		}
	    },
	    failure: function(o) {
	    	bootbox.alert('unable to load subledger details');
	    }
	}	

function createDropdownFormatterDetail(prefix){
    return function(el, oRecord, oColumn, oData) {
        var selectedValue = (lang.isValue(oData)) ? oData : oRecord.getData(oColumn.field),
            options = (lang.isArray(oColumn.dropdownOptions)) ?
                oColumn.dropdownOptions : null,
            selectEl,
            collection = el.getElementsByTagName("select");
        if(collection.length === 0) {
            selectEl = document.createElement("select");
            selectEl.className = YAHOO.widget.DataTable.CLASS_DROPDOWN;
            selectEl.name = prefix+'['+slDetailTableIndex+'].'+oColumn.getKey();
			selectEl.id = prefix+'['+slDetailTableIndex+'].'+oColumn.getKey();
            selectEl = el.appendChild(selectEl);
            var selectedIndex = {value: slDetailTableIndex }; 
            YAHOO.util.Event.addListener(selectEl,"change",onDropdownDetailChange,selectedIndex,this);
			
        }

        selectEl = collection[0];

        if(selectEl) {
            selectEl.innerHTML = "";
            if(options) {
                for(var i=0; i<options.length; i++) {
                    var option = options[i];
                    var optionEl = document.createElement("option");
                    optionEl.value = (lang.isValue(option.value)) ?
                            option.value : option;
                    optionEl.innerHTML = (lang.isValue(option.text)) ?
                            option.text : (lang.isValue(option.label)) ? option.label : option;
                    optionEl = selectEl.appendChild(optionEl);
                    if (optionEl.value == selectedValue) {
                        optionEl.selected = true;
                    }
                }
            }
            else {
                selectEl.innerHTML = "<option selected value=\"" + selectedValue + "\">" + selectedValue + "</option>";
            }
        }
        else {
            el.innerHTML = lang.isValue(oData) ? oData : "";
        }
    }
}

	var selecteddetailcode;
	var onDropdownDetailChange = function(index,obj) { 
		
		document.getElementById(SUBLEDGERLIST+'['+obj.value+']'+'.detailCode').placeholder=codeTextMessage;
		document.getElementById(SUBLEDGERLIST+'['+obj.value+']'+'.detailCode').style.color='DarkGray';
		document.getElementById(SUBLEDGERLIST+'['+obj.value+']'+'.detailKeyId').value='';
		document.getElementById(SUBLEDGERLIST+'['+obj.value+']'+'.detailKey').value='';
		document.getElementById(SUBLEDGERLIST+'['+obj.value+']'+'.amount').value='0';
		document.getElementById(SUBLEDGERLIST+'['+obj.value+']'+'.amount').readOnly=false;
		var detailtypeidObj=document.getElementById('subLedgerlist['+obj.value+'].detailType.id');
		if(detailTypeId != detailtypeidObj.value){ // checks if the subledgercodes already loaded for that detail type
			detailTypeId = detailtypeidObj.value;
			//loadDropDownCodesForEntities(detailtypeidObj); 
		}
		if(entities)
		{
			entities=null;
			if(oAutoCompEntity){
				YAHOO.util.Event.purgeElement(obj, true);
				oAutoCompEntity._elContainer.innerHTML="";
				yuiflag2[obj.value] =undefined;
			}
		}
	};
function onFocusDetailCode(obj){
	var currRow=getRowIndex(obj);
	var detailtypeidObj=document.getElementById('subLedgerlist['+currRow+'].detailType.id');
	if(detailTypeId != detailtypeidObj.value){
		detailTypeId = detailtypeidObj.value;
		//loadDropDownCodesForEntities(detailtypeidObj); 
	}
}


	
function createDropdownFormatterCode(prefix){
    return function(el, oRecord, oColumn, oData) {
        var selectedValue = (lang.isValue(oData)) ? oData : oRecord.getData(oColumn.field),
            options = (lang.isArray(oColumn.dropdownOptions)) ?
                oColumn.dropdownOptions : null,
            selectEl,
            collection = el.getElementsByTagName("select");
        if(collection.length === 0) {
            selectEl = document.createElement("select");
            selectEl.className = YAHOO.widget.DataTable.CLASS_DROPDOWN;
            selectEl.name = prefix+'['+slDetailTableIndex+'].'+oColumn.getKey();
			selectEl.id = prefix+'['+slDetailTableIndex+'].'+oColumn.getKey();
			//selectEl.onfocus=check;
			selectEl.tabIndex='-1';
            selectEl = el.appendChild(selectEl);
			var selectedIndex = {value: slDetailTableIndex }; 
            YAHOO.util.Event.addListener(selectEl,"change",onDropdownChange,selectedIndex,this);
			
        }

        selectEl = collection[0];

        if(selectEl) {
            selectEl.innerHTML = "";
            if(options) {
                for(var i=0; i<options.length; i++) {
                    var option = options[i];
                    var optionEl = document.createElement("option");
                    optionEl.value = (lang.isValue(option.value)) ?
                            option.value : option;
                    optionEl.innerHTML = (lang.isValue(option.text)) ?
                            option.text : (lang.isValue(option.label)) ? option.label : option;
                    optionEl = selectEl.appendChild(optionEl);
                    if (optionEl.value == selectedValue) {
                        optionEl.selected = true;
                    }
                }
            }
            else {
                selectEl.innerHTML = "<option selected value=\"" + selectedValue + "\">" + selectedValue + "</option>";
            }
        }
        else {
            el.innerHTML = lang.isValue(oData) ? oData : "";
        }
    }
}
var selecteddetailtype;
var onDropdownChange = function(index,obj) { 
	
		var subledgerid=document.getElementById('subLedgerlist['+obj.value+'].glcode.id');
		var accountCode = subledgerid.options[subledgerid.selectedIndex].text;
		if(subledgerid.options[subledgerid.selectedIndex].value==0){
			accountCode=0;
		}
		selecteddetailtype=document.getElementById('subLedgerlist['+obj.value+'].detailType.id').value;
		//document.getElementById('subLedgerlist['+obj.value+'].subledgerCode').value= subledgerid.options[subledgerid.selectedIndex].text;
		var url = path+'/receipts/ajaxReceiptCreate-getDetailType.action?accountCode='+accountCode+'&index='+obj.value+'&selectedDetailType='+selecteddetailtype+'&onload=false';
		var transaction = YAHOO.util.Connect.asyncRequest('POST', url, postType, null);
};
var postType = {
success: function(o) {
	var detailType= o.responseText;
	var detailRecord = detailType.split('#');
	var eachItem;
	var obj;
	var rowid;
	var selectedval;
	var onload ='false';
	for(var i=0;i<detailRecord.length;i++)
	{
		eachItem =detailRecord[i].split('~');
		if(eachItem!=""){
			if(obj==null)
			{
				obj = document.getElementById('subLedgerlist['+parseInt(eachItem[0])+']'+'.detailType.id');
				rowid=parseInt(eachItem[0]);
				selectedval=eachItem[1];
				for(k=obj.options.length-1;k>=1;k--)
				{
					obj.remove(k);
				}
				if(entities)
				{
					entities=null;
					if(oAutoCompEntity){
						YAHOO.util.Event.purgeElement(obj, true);
						oAutoCompEntity._elContainer.innerHTML="";
						yuiflag2[parseInt(eachItem[0])] =undefined;
					}
				}
			}
			
			if(eachItem[1]!="error"){
				try{
					obj.add(new Option(eachItem[3],eachItem[4]), null);
				}catch(ex){
					obj.add(new Option(eachItem[3],eachItem[4]));
				}
			onload=eachItem[2];
			}
		}
	}
	for(var k=0;k<obj.options.length;k++){
		if(obj.options[k].value==selectedval){
			obj.options[k].selected=true;
		}
	}	
	if(onload=='false')
	{
		document.getElementById(SUBLEDGERLIST+'['+rowid+']'+'.detailCode').placeholder=codeTextMessage;
		document.getElementById(SUBLEDGERLIST+'['+rowid+']'+'.detailCode').style.color='DarkGray';
		document.getElementById(SUBLEDGERLIST+'['+rowid+']'+'.detailKeyId').value='';
		document.getElementById(SUBLEDGERLIST+'['+rowid+']'+'.detailKey').value='';
		document.getElementById(SUBLEDGERLIST+'['+rowid+']'+'.amount').value='0';
	}
    },
    failure: function(o) {
    	bootbox.alert('unable to load subledger details');
    }
}
function getDetailType(val){
	selecteddetailtype=document.getElementById('subLedgerlist['+val+'].detailType.id').value;
	var detail = document.getElementById('subLedgerlist['+val+'].detailType.id');
	
	for(k=detail.options.length-1;k>=1;k--)
	{
		detail.remove(k);
	}
	var subledgerid=document.getElementById('subLedgerlist['+val+'].glcode.id');
	var accountCode = subledgerid.options[subledgerid.selectedIndex].text;
	if( subledgerid.options[subledgerid.selectedIndex].value!=0){
		var url = path+'/receipts/ajaxReceiptCreate-getDetailType.action?accountCode='+accountCode+'&index='+val+'&selectedDetailType='+selecteddetailtype+'&onload=true';
		var transaction = YAHOO.util.Connect.asyncRequest('POST', url, postType, null);
	}
	else{
		var detailCodeObj = document.getElementById('subLedgerlist['+val+'].detailCode');
		if(detailCodeObj!=null){
			detailCodeObj.placeholder=codeTextMessage;
			detailCodeObj.style.color='DarkGray';
			document.getElementById(SUBLEDGERLIST+'['+val+']'+'.detailKeyId').value='';
			document.getElementById(SUBLEDGERLIST+'['+val+']'+'.detailKey').value='';
			document.getElementById(SUBLEDGERLIST+'['+val+']'+'.amount').value='';
		}
	}
		

}

function getDetailCode(val){
	var selecteddetailcode1=document.getElementById('subLedgerlist['+val+'].detailCode').value;
	var detailCode=document.getElementById('subLedgerlist['+val+'].detailCode');
	detailCode.value='';
	document.getElementById(SUBLEDGERLIST+'['+val+']'+'.detailKeyId').value='';
	document.getElementById(SUBLEDGERLIST+'['+val+']'+'.detailKey').value='';
	
	
}

function getDetailCodeValue(val){
	var detailtypeid = document.getElementById('subLedgerlist['+val+'].detailType.id').value;
	var selecteddetailcode1=document.getElementById('subLedgerlist['+val+'].detailCode').value;
	if(selecteddetailcode1!=0){
		var url =  path+'/receipts/ajaxReceiptCreate-ajaxValidateDetailCodeNew.action?code='+selecteddetailcode1+'&detailtypeid='+detailtypeid+'&index='+val+'&codeorname=code';
		var transaction = YAHOO.util.Connect.asyncRequest('POST', url, postTypeDetailCode, null);
	}

}
function check(){
	
	var accountCodes=new Array();
	var count=0;
	for(var i=0;i<billDetailTableIndex+1;i++){
		if(null != document.getElementById('billCreditDetailslist['+i+'].glcodeDetail')){
			accountCodes[count] = document.getElementById('billCreditDetailslist['+i+'].glcodeDetail').value;
			count++;
		}
	}
	for(var i=0;i<rebateDetailTableIndex+1;i++){
		if(null != document.getElementById('billRebateDetailslist['+i+'].glcodeDetail')){
			accountCodes[count] = document.getElementById('billRebateDetailslist['+i+'].glcodeDetail').value;
			count++;
		}
	}
	var url =  path+'/receipts/ajaxReceiptCreate-getDetailCode.action?accountCodes='+accountCodes;
	var transaction = YAHOO.util.Connect.asyncRequest('POST', url, callbackJV, null);
	
}
var callbackJV = {
success: function(o) {
		var test= o.responseText;
		test = test.split('~');
		for (var j=0; j<slDetailTableIndex;j++ )
		{
			if(null != document.getElementById('subLedgerlist['+j+'].glcode.id') && null != document.getElementById('subLedgerlist['+j+'].subledgerCode') && test.length >1 )
			{
				var selectedglcode=document.getElementById('subLedgerlist['+j+'].glcode.id').value;
				d=document.getElementById('subLedgerlist['+j+'].glcode.id');
				for(var k=d.options.length-1;k>=1;k--)
				{
					d.remove(k);
				}
				for (var i=1; i<((test.length)/2)+1;i++ )
				{
					try{
						d.add(new Option(test[i*2-2].trim(),test[i*2-1]), null);}
					catch(ex){
						d.add(new Option(test[i*2-2].trim(),test[i*2-1]));
					}
					if(test[i*2-1]==selectedglcode){
						d.options[i].selected=true;
					}
				}
								
				getDetailType(j);
			} 
			if(test.length<2)
			{
				var d = document.getElementById('subLedgerlist['+j+'].glcode.id');
				for(var k=d.options.length-1;k>=1;k--)
				{
					d.remove(k);
				}
				
				var detail = document.getElementById('subLedgerlist['+j+'].detailType.id');
				if(detail!=null){
					for(var k=detail.options.length-1;k>=1;k--)
					{
						detail.remove(k);
					}
				}
				var detailCode = document.getElementById('subLedgerlist['+j+'].detailCode');
				if(detailCode!=null){
					document.getElementById(SUBLEDGERLIST+'['+j+']'+'.detailCode').placeholder=codeTextMessage;
					document.getElementById(SUBLEDGERLIST+'['+j+']'+'.detailCode').style.color='DarkGray';
					document.getElementById(SUBLEDGERLIST+'['+j+']'+'.detailKeyId').value='';
					document.getElementById(SUBLEDGERLIST+'['+j+']'+'.detailKey').value='';
					document.getElementById(SUBLEDGERLIST+'['+j+']'+'.amount').value='';
				}
			}
		}
		patternvalidation();
    },
    failure: function(o) {
    	bootbox.alert('unable to load subledger details');
    }
}
function loaddropdown(){
bootbox.alert(coming);
	
}
function updateDebitAmount()
{
	var amt=0;
	for(var i=0;i<rebateDetailTableIndex+1;i++)
	{
		if(null != document.getElementById('billRebateDetailslist['+i+'].debitAmountDetail')){
			var val = document.getElementById('billRebateDetailslist['+i+'].debitAmountDetail').value;
			if(val=='') val=0;
			if(val!="" && !isNaN(val))
			{
				amt = amt + parseFloat(document.getElementById('billRebateDetailslist['+i+'].debitAmountDetail').value);
			}
		}
	}
	document.getElementById('totaldbamount').value = amt;
}

function updateCreditAmount()
{
	var amt=0;
	for(var i=0;i<billDetailTableIndex+1;i++)
	{
		if(null != document.getElementById('billCreditDetailslist['+i+'].creditAmountDetail')){
			var val = document.getElementById('billCreditDetailslist['+i+'].creditAmountDetail').value;
			if(val=='') val=0;
			if(val!="" && !isNaN(val))
			{
				amt = amt + parseFloat(document.getElementById('billCreditDetailslist['+i+'].creditAmountDetail').value);
			}
		}
	}
	document.getElementById('totalcramount').value = amt;
}

function updatetotalAmount(){
	var totalamount;
	if(document.getElementById('totaldbamount') !=null){
		totalamount=parseFloat(document.getElementById('totalcramount').value)-parseFloat(document.getElementById('totaldbamount').value);
	}
	else {
		totalamount=parseFloat(document.getElementById('totalcramount').value);
	}
	
	var totalAmountStr=(totalamount>0?totalamount:totalamount);
	
	document.getElementById('misctotalAmount').value=totalAmountStr;
	document.getElementById('totalamountdisplay').value=document.getElementById('misctotalAmount').value;
	document.getElementById('instrHeaderCash.instrumentAmount').value=totalAmountStr;	
}

function validateDetailCode(obj)
{
	var index = getRowIndex(obj);
	var element = document.getElementById(SUBLEDGERLIST+'['+index+']'+'.detailType.id');
	var detailtypeid = element.options[element.selectedIndex].value;
	var url =  path+'/receipts/ajaxReceiptCreate-ajaxValidateDetailCodeNew.action?code='+obj.value+'&detailtypeid='+detailtypeid+'&index='+index+'&codeorname=both';
	var transaction = YAHOO.util.Connect.asyncRequest('POST', url, callbackCode, null);
}
var callbackCode = {
	success: function(o) {
		var res= o.responseText;

		res = res.split('~');
		if(res.length>3)
		{
			dom.get("receipt_error_area").style.display="none";
			document.getElementById(SUBLEDGERLIST+'['+parseInt(res[0])+']'+'.detailKeyId').value=res[1];
			document.getElementById(SUBLEDGERLIST+'['+parseInt(res[0])+']'+'.detailKey').value=res[2];
			document.getElementById(SUBLEDGERLIST+'['+parseInt(res[0])+']'+'.detailCode').value=res[3];
		}
		else
		{
			dom.get("receipt_error_area").style.display="block";
			//document.getElementById(SUBLEDGERLIST+'['+parseInt(res[0])+']'+'.detailCode').focus();
			document.getElementById('receipt_error_area').innerHTML='Please enter correct code/name in Subledger Details <br>';
			document.getElementById(SUBLEDGERLIST+'['+parseInt(res[0])+']'+'.detailKeyId').value='';
			document.getElementById(SUBLEDGERLIST+'['+parseInt(res[0])+']'+'.detailKey').value='';
			//document.getElementById(SUBLEDGERLIST+'['+parseInt(res[0])+']'+'.detailCode').value='';
			return;
		}
    },
    failure: function(o) {
    	bootbox.alert('unable to load subledger details');
    }
}

function checkBillId()
{
	if(document.getElementById('id').value!='')
	{
		document.getElementById('print').disabled=false;
		for(var i=0;i<document.forms[0].elements.length;i++)
		{
			if(document.forms[0].elements(i).type=='text')
				document.forms[0].elements(i).readonly=true;
		}
	}
	else
		document.getElementById('print').disabled=true;
}

function validateAccountDetail(){

	var i, j, debit=0, credit=0,creditTotal=0,debitTotal=0;
	for (var i=0; i<billDetailTableIndex+1; i++)
	{
		for (var j=0; j<rebateDetailTableIndex+1; j++ )
		{	
			if(document.getElementById('billCreditDetailslist['+i+'].glcodeDetail')!=null && document.getElementById('billRebateDetailslist['+j+'].glcodeDetail')!=null){
				var s=document.getElementById('billCreditDetailslist['+i+'].glcodeDetail').value.length;
						
				if ((document.getElementById('billCreditDetailslist['+i+'].glcodeDetail').value.length!=0 && 
				document.getElementById('billRebateDetailslist['+j+'].glcodeDetail').value.length!=0)&&
				(document.getElementById('billCreditDetailslist['+i+'].glcodeDetail').value == document.getElementById('billRebateDetailslist['+j+'].glcodeDetail').value ))
				{
					document.getElementById('receipt_error_area').innerHTML+='Duplicate record in Rebates/Discounts. Please check account : ' + document.getElementById('billRebateDetailslist['+j+'].glcodeDetail').value+'<br>';
					return false;
				}
				
			}
			
		}
	}
	for (var i=0; i<billDetailTableIndex+1; i++)
	{
		for (var j=0; j<billDetailTableIndex+1; j++ )
		{	
			if (i!=j)
			{
				if(document.getElementById('billCreditDetailslist['+i+'].glcodeDetail')!=null && document.getElementById('billCreditDetailslist['+j+'].glcodeDetail')!=null  ){
					if ((document.getElementById('billCreditDetailslist['+i+'].glcodeDetail').value.length!=0 && document.getElementById('billCreditDetailslist['+j+'].glcodeDetail').value.length!=0)&&(document.getElementById('billCreditDetailslist['+i+'].glcodeDetail').value == document.getElementById('billCreditDetailslist['+j+'].glcodeDetail').value ))
					{	document.getElementById('receipt_error_area').innerHTML+='Duplicate record in Receipt Heads. Please check account : ' + document.getElementById('billCreditDetailslist['+j+'].glcodeDetail').value+'<br>';
						i=billCreditDetailsTable.getRecordSet().getLength();
						return false;
					}
				}
			}
			
		}
		if(document.getElementById('billCreditDetailslist['+i+'].glcodeDetail')!=null){
			if (  document.getElementById('billCreditDetailslist['+i+'].glcodeDetail').value.length > 0 && document.getElementById('billCreditDetailslist['+i+'].accounthead').value.length==0)
			{
				document.getElementById('billCreditDetailslist['+i+'].glcodeDetail').focus();
				document.getElementById('receipt_error_area').innerHTML+='Please enter correct data in Receipt Heads for the account code:'+document.getElementById('billCreditDetailslist['+i+'].glcodeDetail').value +'<br>';
				return false;
			}
			if(document.getElementById('billCreditDetailslist['+i+'].creditAmountDetail').value.startsWith('+')){
				document.getElementById('billCreditDetailslist['+i+'].creditAmountDetail').focus();
				document.getElementById('receipt_error_area').innerHTML+='Please enter correct amount in Receipt Heads for account :'+document.getElementById('billCreditDetailslist['+i+'].glcodeDetail').value+'<br>';
				return false;
			}
			if(isNaN(document.getElementById('billCreditDetailslist['+i+'].creditAmountDetail').value)){
				document.getElementById('billCreditDetailslist['+i+'].creditAmountDetail').focus();
				document.getElementById('receipt_error_area').innerHTML+='Please enter correct amount in Receipt Heads for account :'+document.getElementById('billCreditDetailslist['+i+'].glcodeDetail').value+'<br>';
				return false;
			}
			credit = eval(document.getElementById('billCreditDetailslist['+i+'].creditAmountDetail').value);
			credit = isNaN(credit)?0:credit;
			creditTotal = creditTotal + credit;
			if(credit<0){
				document.getElementById('billCreditDetailslist['+i+'].creditAmountDetail').focus();
				document.getElementById('receipt_error_area').innerHTML+='Please enter correct amount in Receipt Heads for account :'+document.getElementById('billCreditDetailslist['+i+'].glcodeDetail').value+'<br>';
				return false;
			}
			if (credit>0 &&  document.getElementById('billCreditDetailslist['+i+'].glcodeDetail').value.length == 0)
			{
				document.getElementById('billCreditDetailslist['+i+'].creditAmountDetail').focus();
				document.getElementById('receipt_error_area').innerHTML+='Account code is missing for credit supplied field'+'<br>';
				return false;
			}
			
			if (credit == 0 && document.getElementById('billCreditDetailslist['+i+'].glcodeDetail').value.length!= 0)
			{
				document.getElementById('billCreditDetailslist['+i+'].creditAmountDetail').focus();
				document.getElementById('receipt_error_area').innerHTML+='Please enter credit amount for the account code ' +document.getElementById('billCreditDetailslist['+i+'].glcodeDetail').value +'<br>';
				return false;
			}
			
		}
	}
	for (var i=0; i<rebateDetailTableIndex+1; i++)
	{
		for (var j=0; j<rebateDetailTableIndex+1; j++ )
		{	
			if (i!=j)
			{
				if(document.getElementById('billRebateDetailslist['+i+'].glcodeDetail')!=null && document.getElementById('billRebateDetailslist['+j+'].glcodeDetail')!=null){
					if ( (document.getElementById('billRebateDetailslist['+i+'].glcodeDetail').value.length!=0 && document.getElementById('billRebateDetailslist['+j+'].glcodeDetail').value.length!=0)&&(document.getElementById('billRebateDetailslist['+i+'].glcodeDetail').value == document.getElementById('billRebateDetailslist['+j+'].glcodeDetail').value ))
					{
						document.getElementById('receipt_error_area').innerHTML+='Duplicate record in Rebates/Discounts. Please check account :: ' + document.getElementById('billRebateDetailslist['+i+'].glcodeDetail').value +'<br>';
						i=billCreditDetailsTable.getRecordSet().getLength();
						return false;
					}
				}
			}
			
		}
		if(document.getElementById('billRebateDetailslist['+i+'].glcodeDetail')!=null){
			if (document.getElementById('billRebateDetailslist['+i+'].glcodeDetail').value.length > 0 && document.getElementById('billRebateDetailslist['+i+'].accounthead').value.length==0)
			{
				document.getElementById('billRebateDetailslist['+i+'].glcodeDetail').focus();
				document.getElementById('receipt_error_area').innerHTML+='Please enter correct data in Rebates/Discounts for the account code:'+document.getElementById('billRebateDetailslist['+i+'].glcodeDetail').value +'<br>';
				return false;
			}
			if(document.getElementById('billRebateDetailslist['+i+'].debitAmountDetail').value.startsWith('+')){
				document.getElementById('billRebateDetailslist['+i+'].debitAmountDetail').focus();
				document.getElementById('receipt_error_area').innerHTML+='Please enter correct amount in Rebates/Discounts for account :'+document.getElementById('billRebateDetailslist['+i+'].glcodeDetail').value+'<br>';
				return false;
			}
			if(isNaN(document.getElementById('billRebateDetailslist['+i+'].debitAmountDetail').value)){
				document.getElementById('billRebateDetailslist['+i+'].debitAmountDetail').focus();
				document.getElementById('receipt_error_area').innerHTML+='Please enter correct amount in Rebates/Discounts for account :'+document.getElementById('billRebateDetailslist['+i+'].glcodeDetail').value+'<br>';
				return false;
			}
			debit = eval(document.getElementById('billRebateDetailslist['+i+'].debitAmountDetail').value);
			debit = isNaN(debit)?0:debit;
			debitTotal = debitTotal + debit;
			if(debit<0){
				document.getElementById('billRebateDetailslist['+i+'].debitAmountDetail').focus();
				document.getElementById('receipt_error_area').innerHTML+='Please enter correct amount in Rebates/Discounts for account :'+document.getElementById('billRebateDetailslist['+i+'].glcodeDetail').value+'<br>';
				return false;
			}
			if (debit>0 &&  document.getElementById('billRebateDetailslist['+i+'].glcodeDetail').value.length == 0)
			{
				document.getElementById('billRebateDetailslist['+i+'].debitAmountDetail').focus();
				document.getElementById('receipt_error_area').innerHTML+='Account code is missing for debit supplied field'+'<br>';
				return false;
			}
			
			if (debit==0  && document.getElementById('billRebateDetailslist['+i+'].glcodeDetail').value.length!= 0)
			{
				document.getElementById('billRebateDetailslist['+i+'].debitAmountDetail').focus();
				document.getElementById('receipt_error_area').innerHTML+='Please enter debit/credit amount for the account code ' +document.getElementById('billRebateDetailslist['+i+'].glcodeDetail').value+'<br>';
				return false;
			}
			
		}
	}
	if(creditTotal<=0)
	{
			document.getElementById('receipt_error_area').innerHTML+="Please enter credit account details in 'Receipt Heads'"+"<br>";
			return false;
	}
	return true;
}
function validateSubLedgerDetailforCredit(){

var subledgerselected = new Array();

	for (var i=0; i<billDetailTableIndex+1; i++ )
	{
		var accountDetailamount=0;
		var subledgerTotalAmt=0;
		if(document.getElementById('billCreditDetailslist['+i+'].glcodeDetail')!=null){
			for (var j=0; j<slDetailTableIndex+1; j++ )
			{
				if(document.getElementById('subLedgerlist['+j+'].glcode.id')!=null){
			
					var accountCode = document.getElementById('billCreditDetailslist['+i+'].glcodeDetail').value;
					var subledgerid = document.getElementById('subLedgerlist['+j+'].glcode.id');
					var detailtypeid = document.getElementById('subLedgerlist['+j+'].detailType.id');
					var detailKeyid = document.getElementById('subLedgerlist['+j+'].detailKeyId').value
				
						var subledgerAccCode =0;
					if(subledgerid!='null')
						subledgerAccCode= subledgerid.options[subledgerid.selectedIndex].value;
					
					if( ( subledgerAccCode !=0) && (detailtypeid.value == "" || detailKeyid ==""))
					{
							document.getElementById('receipt_error_area').innerHTML += "Please enter subledger details correctly<br>";
							return false;
					}
					if(document.getElementById('subLedgerlist['+j+'].amount').value.startsWith('+')){
						document.getElementById('subLedgerlist['+j+'].amount').focus();
						document.getElementById('receipt_error_area').innerHTML+='Please enter correct amount in Subledger Details '+'<br>';
						return false;
					}
					if(isNaN(document.getElementById('subLedgerlist['+j+'].amount').value)){
						document.getElementById('subLedgerlist['+j+'].amount').focus();
						document.getElementById('receipt_error_area').innerHTML+='Please enter correct amount in Subledger Details '+'<br>';
						return false;
					}
					if (accountCode == subledgerid.options[subledgerid.selectedIndex].text.trim())
					{
						if(eval(document.getElementById('billCreditDetailslist['+i+'].creditAmountDetail').value) > 0){
		
							accountDetailamount = document.getElementById('billCreditDetailslist['+i+'].creditAmountDetail').value;
						}
						subledgerTotalAmt = subledgerTotalAmt + eval(document.getElementById('subLedgerlist['+j+'].amount').value);
					}
					if(subledgerselected == 0){
						subledgerselected[0] = subledgerAccCode;
					}
					else {
						var found =0;
						for (var k=0; k<subledgerselected.length;k++ )
						{
							if(subledgerselected[k] == subledgerAccCode) found=1
						}
						if(found == 0)subledgerselected[subledgerselected.length+1] = subledgerAccCode;
					}
					
					//document.getElementById('subLedgerlist['+j+'].subledgerCode').value= subledgerid.options[subledgerid.selectedIndex].text;
					//document.getElementById('subLedgerlist['+j+']'+'.detailTypeName').value =  detailtypeid.options[detailtypeid.selectedIndex].text;
				}
			}
		
			if(Math.round(accountDetailamount*100)/100  != Math.round(subledgerTotalAmt*100)/100)
			{
				document.getElementById('receipt_error_area').innerHTML += "Total subledger amount is not matching for account code : "+ document.getElementById('billCreditDetailslist['+i+'].glcodeDetail').value+'<br>';
				return false;
			}
		}
	}
	
	return true;
}

function validateSubLedgerDetailforRebate(){

var subledgerselected = new Array();
	for (var i=0; i<rebateDetailTableIndex+1; i++ )
	{
		var accountDetailamount=0;
		var subledgerTotalAmt=0;
		
		if(document.getElementById('billRebateDetailslist['+i+'].glcodeDetail')!=null){
			for (var j=0; j<slDetailTableIndex+1; j++ )
			{
				if(document.getElementById('subLedgerlist['+j+'].glcode.id')!=null)
				{
						var accountCode = document.getElementById('billRebateDetailslist['+i+'].glcodeDetail').value;
						var subledgerid = document.getElementById('subLedgerlist['+j+'].glcode.id');
						var detailtypeid = document.getElementById('subLedgerlist['+j+'].detailType.id');
						var detailKeyid = document.getElementById('subLedgerlist['+j+'].detailKeyId').value
					
						
						
							var subledgerAccCode = subledgerid.options[subledgerid.selectedIndex].value;
						
					
						if( ( subledgerAccCode !=0) && (detailtypeid.value == "" || detailKeyid ==""))
						{
								document.getElementById('receipt_error_area').innerHTML += "Please enter subledger details correctly"+'<br>';
								return false;
						}
						if(isNaN(document.getElementById('subLedgerlist['+j+'].amount').value)){
							document.getElementById('subLedgerlist['+j+'].amount').focus();
							document.getElementById('receipt_error_area').innerHTML+='Please enter correct amount in Subledger Details '+'<br>';
							return false;
						}
						if (accountCode == subledgerid.options[subledgerid.selectedIndex].text.trim())
						{
							if(eval(document.getElementById('billRebateDetailslist['+i+'].debitAmountDetail').value) >0){
			
								accountDetailamount = document.getElementById('billRebateDetailslist['+i+'].debitAmountDetail').value;
					
							} 
							subledgerTotalAmt = subledgerTotalAmt + eval(document.getElementById('subLedgerlist['+j+'].amount').value);
						}
						if(subledgerselected == 0){
							subledgerselected[0] = subledgerAccCode;
						}
						else {
							var found =0;
							for (var k=0; k<subledgerselected.length;k++ )
							{
								if(subledgerselected[k] == subledgerAccCode) found=1
							}
							if(found == 0)subledgerselected[subledgerselected.length+1] = subledgerAccCode;
						}
						
						document.getElementById('subLedgerlist['+j+'].subledgerCode').value= subledgerid.options[subledgerid.selectedIndex].text;
						document.getElementById('subLedgerlist['+j+']'+'.detailTypeName').value =  detailtypeid.options[detailtypeid.selectedIndex].text;
				}
			}
			
			if(Math.round(accountDetailamount*100)/100  != Math.round(subledgerTotalAmt*100)/100)
			{
				document.getElementById('receipt_error_area').innerHTML += "Total subledger amount is not matching for account code : "+ document.getElementById('billRebateDetailslist['+i+'].glcodeDetail').value+'<br>';
				return false;
			}
		}
	}
	
	return true;
}

function checkLength(obj)
{
	if(obj.value.length>1024)
	{
		bootbox.alert('Max 1024 characters are allowed for comments. Remaining characters are truncated.')
		obj.value = obj.value.substring(1,1024);
	}
}

function updateAccountTableIndex(){
	
	billDetailTableIndex = billDetailTableIndex +1 ;
	patternvalidation();
}
function updateSLTableIndex(){
	
	 slDetailTableIndex = slDetailTableIndex +1 ;
}
function updateRebateDetailTableIndex(){
	
	 rebateDetailTableIndex = rebateDetailTableIndex +1 ;
}
function validateAlphaNumeric( strValue )
{
	var objRegExp  = /^[0-9a-zA-Z]+$/;
	return objRegExp.test(strValue)
}

var entities;
var entitiesArray;
var firstthreechars;
var savedthreechars;
var yuiflag2 = new Array();
var entobj;
var entevent;
function autocompleteForEntity(obj,myEvent){
var  currRow=getRowIndex(obj);
	if(obj.value.length<3){
		if(entities)
			{
				entities=null;
				if(oAutoCompEntity){
					YAHOO.util.Event.purgeElement(obj, true);//To remove the listners of the autocomplete instance
					oAutoCompEntity._elContainer.innerHTML="";
					yuiflag2[currRow] =undefined;
				}
			}
		savedthreechars="";
	}
	if(obj.value.length>=3){
		firstthreechars=obj.value.substring(0,3);
		var detailtypeidObj=document.getElementById('subLedgerlist['+currRow+'].detailType.id');
		if(savedthreechars!=firstthreechars && detailtypeidObj.value!=0){
			savedthreechars=firstthreechars;
			if(entities)
			{
				entities=null;
				if(oAutoCompEntity){
					YAHOO.util.Event.purgeElement(obj, true);
					oAutoCompEntity._elContainer.innerHTML="";
					yuiflag2[currRow] =undefined;
				}
			}
			entobj=obj;
			entevent=myEvent;

			if(onElementFocused(obj)){
				ShowImage(obj);//To start loading image
				var url =   path+ "/receipts/ajaxReceiptCreate-getCodeNew.action?detailTypeId="+detailtypeidObj.value+"&filterKey="+obj.value;
				var transaction = YAHOO.util.Connect.asyncRequest('POST', url, callbackAutoCompleteEntities, null);
			}
		}
		
	}
}


var callbackAutoCompleteEntities = {
	success: function(o) {
		var res= o.responseText;
		var entity=res.trim();
		var a = entity.split("^");
		var eachEntity = a[0];
		entitiesArray=eachEntity.split("+");
		entities = new YAHOO.widget.DS_JSArray(entitiesArray);
		var src = entobj;	
	var target = document.getElementById('subledgercodescontainer');	
	var posSrc=findPos(src); 
	
	target.style.left=posSrc[0]+"px";	
	target.style.top=posSrc[1]-40+"px";
	target.style.width=650;	
	      		
	
	var coaCodeObj=entobj;
if(onElementFocused(entobj))//To check if the element is still under focus
{
	var  currRow=getRowIndex(entobj);
	//40 --> Down arrow, 38 --> Up arrow
	if(yuiflag2[currRow] == undefined)//To make sure autocomplete instance is created only once with that event 
	{
		var key = window.event ? window.event.keyCode : entevent.charCode;  

		if(key != 40 )
		{
			if(key != 38 )
			{
				
				oAutoCompEntity = new YAHOO.widget.AutoComplete(coaCodeObj,'subledgercodescontainer', entities);
				oAutoCompEntity.queryDelay = 0;
				oAutoCompEntity.prehighlightClassName = "yui-ac-prehighlight";
				oAutoCompEntity.useShadow = true;
				oAutoCompEntity.maxResultsDisplayed = 15;
				oAutoCompEntity.useIFrame = true;
				if(entities){
					entities.applyLocalFilter = true;
					entities.queryMatchContains = true;
				}
				oAutoCompEntity.minQueryLength = 0;
			oAutoCompEntity.sendQuery(coaCodeObj.value);
			oAutoCompEntity.itemSelectEvent.subscribe(function fnCallback(e, args) {//This is to set the values on mouse click
    			document.getElementById(SUBLEDGERLIST+'['+currRow+']'+'.detailCode').value=args[2];
    			dom.get("receipt_error_area").style.display="none";
			});
			}
		}
		yuiflag2[currRow] = 1;
	}

}
HideImage(entobj);
	
    },
    failure: function(o) {
    	bootbox.alert('failure');
    }
}
function splitEntitiesDetailCode(obj)
{
	HideImage(obj);
	var currRow=getRowIndex(obj);
	var entity=obj.value;
	var detailtypeidObj=document.getElementById('subLedgerlist['+currRow+'].detailType.id');
	if(entity.trim()!="" && detailtypeidObj.value!=0 && entity.trim()!=codeTextMessage)
	{
		var entity_array=entity.split("`-`");
		if(entity_array.length==3)
		{
			dom.get("receipt_error_area").style.display="none";
			document.getElementById(SUBLEDGERLIST+'['+currRow+']'+'.detailCode').value=entity_array[0];
			document.getElementById(SUBLEDGERLIST+'['+currRow+']'+'.detailKeyId').value=entity_array[2];
			document.getElementById(SUBLEDGERLIST+'['+currRow+']'+'.detailKey').value=entity_array[1];
			var glcodenew=document.getElementById(SUBLEDGERLIST+'['+currRow+']'+'.glcode.id');
			
		}
		else{
			validateDetailCode(obj);// to validate for the code/name on blur
		}
	}
	if(entity.trim()==codeTextMessage){
		document.getElementById(SUBLEDGERLIST+'['+currRow+']'+'.detailKeyId').value='';
		document.getElementById(SUBLEDGERLIST+'['+currRow+']'+'.detailKey').value='';
	}

}
function ShowImage(obj)
{
 obj.style.backgroundImage  = 'url('+path+'/images/LoadingV2.gif)';
 
 obj.style.backgroundRepeat= 'no-repeat';
                    
 obj.style.backgroundPosition = 'right';
}
function HideImage(obj)
{
 obj.style.backgroundImage  = 'none';
} 
function onElementFocused(e)
{
    return document.activeElement ==e?true:false;
       
} 
