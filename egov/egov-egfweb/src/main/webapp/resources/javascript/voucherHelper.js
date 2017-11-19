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
var SUBLEDGERLIST='subLedgerlist';                       
var INSTRUMENTLIST='iHeaderList';
var billDetailsTable;
var subLedgersTable;
var path;
var billDetailTableIndex = 0;
var slDetailTableIndex = 0;
var instrTableIndex = 0;
var oAutoCompEntity;
var detailTypeId=0;
var acctTypeCurrRow = 0;
var allGlcodes={}
var codesForAccountDetailType={}
var funcIdfuncAccCodeArray;
var accCodeFuncFuncIdArray
var slAccountCodes = new Array();
function updateGridPJV(field,index,value){
	
	document.getElementById(VOUCHERDETAILLIST+'['+index+'].'+field).value=value;
}
function updateGridPayInSlip(field,index,value){
	
	document.getElementById(INSTRUMENTLIST+'['+index+'].'+field).value=value;
}

function updateGrid(tableName,field,index,value){
	//bootbox.alert(index);   
	document.getElementById(tableName+'['+index+'].'+field).value=value;
	//bootbox.alert(document.getElementById(tableName+'['+index+'].'+field).value);
}

function updateSLGridPJV(field,index,value){
	document.getElementById('subLedgerlist['+index+'].'+field).value=value;
}

function updateGridSLDropdownPJV(field,index,value){
	var element = document.getElementById('subLedgerlist['+index+'].'+field)
	var len = element.options.length
		
	for(var i=0;i<len;i++){
		if(element.options[i].value == value){
			
			element.options[i].selected = true;
		}
	}
}
function updateGridSLDropdownJV(field,index,value,text){
	var element = document.getElementById('subLedgerlist['+index+'].'+field);
	if(value != "" ){
	element.options.length=2;
	element.options[1].text=text;
	element.options[1].value=value;
	element.options[1].selected = true;
	}
}
function createTextFieldFormatterPJV(prefix,suffix,type){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='"+type+"' id='"+prefix+"["+billDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+billDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"' style='width:90px;' onkeyup='autocompletecode(this,event)' autocomplete='off' onblur='fillNeibrAfterSplitGlcode(this)'/>";
	}
}
function createTextFieldFormatterForFunctionPJV(prefix,suffix){  
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text'  id='"+prefix+"["+billDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+billDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"' style='width:90px;' onkeyup='autocompletecodeFunction(this,event)' autocomplete='off' onblur='fillNeibrAfterSplitFunction(this)'/>";
	}
}


function createDebitFormatterForCBill(prefix,suffix)
{
	 return function(el, oRecord, oColumn, oData) {
			var value = (YAHOO.lang.isValue(oData))?oData:"";
			el.innerHTML = "<div  id='"+prefix+"["+billDetailsTable1.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+billDetailsTable1.getRecordIndex(oRecord)+"]"+suffix+"' readOnly style='width:800;'/>";
		}
}
function createLongTextFieldFormatterPJV(prefix,suffix){
	 return function(el, oRecord, oColumn, oData) {
			var value = (YAHOO.lang.isValue(oData))?oData:"";
			el.innerHTML = "<input type='text'  id='"+prefix+"["+billDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+billDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"' readOnly style='width:250px;'/>";
		}
}

function createAmountFieldFormatterPJV(prefix,suffix,onblurfunction){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text'  id='"+prefix+"["+billDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"'  name='"+prefix+"["+billDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"' style='text-align:right;width:90px;'  onblur='"+onblurfunction+"'/>";
		
	}
}
// JV Datatable code starts

function createTextFieldFormatterJV(prefix,suffix,type){
	
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = " <input type='"+type+"' id='"+prefix+"["+billDetailTableIndex+"]"+suffix+"' name='"+prefix+"["+billDetailTableIndex+"]"+suffix+"' style='width:90px;' onkeyup='autocompletecode(this,event)' autocomplete='off' onblur='fillNeibrAfterSplitGlcode(this);'/>";
		
	}
}
function createTextFieldFormatterForFunctionJV(prefix,suffix,type){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='"+type+"'  id='"+prefix+"["+billDetailTableIndex+"]"+suffix+"' name='"+prefix+"["+billDetailTableIndex+"]"+suffix+"' style='width:90px;' onkeyup='autocompletecodeFunction(this,event)' autocomplete='off' onblur='fillNeibrAfterSplitFunction(this)'/>";
		
	}
		
}
function createSLTextFieldFormatterForFunctionJV(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text'  id='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' name='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' style='width:90px;' onkeyup='autocompletecodeFunction(this,event)' autocomplete='off' onblur='fillNeibrAfterSplitFunctionSL(this)'/>";
		
	}
		
}

function createSLTextFieldwithSearchBtnFormatterJV(prefix,suffix,onblurfunction, suffix2, onClickForSearch) {
	return function(el, oRecord, oColumn, oData) {
		el.innerHTML = "<input type='text' id='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' name='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' style='width:90px;' onblur='"+onblurfunction+"'/>"
					  +" <img src='/egi/resources/erp2/images/plus1.gif' id='"+prefix+"["+slDetailTableIndex+"]"+suffix2+"' name='"+prefix+"["+slDetailTableIndex+"]"+suffix2+"' style='width:18px;'  onClick='"+onClickForSearch+"'/>";	
	}
}

function createLongTextFieldFormatterJV(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
    	var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+index+"]"+suffix+"' name='"+prefix+"["+index+"]"+suffix+"' readOnly style='width:250px;'/>";

    
    }
}
function createLongTextFieldFormatterJV(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+billDetailTableIndex+"]"+suffix+"' name='"+prefix+"["+billDetailTableIndex+"]"+suffix+"' readOnly style='width:250px;' tabindex='-1'/>";
	}
}
function createAmountFieldFormatterJV(prefix,suffix,onblurfunction){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+billDetailTableIndex+"]"+suffix+"' name='"+prefix+"["+billDetailTableIndex+"]"+suffix+"' style='text-align:right;width:90px;' maxlength='18' onblur='validateDigitsAndDecimal(this);"+onblurfunction+"'/>";
	}
}
function createSLTextFieldFormatterJV(prefix,suffix,onblurfunction){
    return function(el, oRecord, oColumn, oData) {
		el.innerHTML = "<input type='text' id='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' name='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' style='width:90px;' onblur='"+onblurfunction+"'/>";
	}
}

function createSLDetailCodeTextFieldFormatterJV(prefix,suffix,onblurfunction, suffix2, onClickForSearch){
    return function(el, oRecord, oColumn, oData) {
		el.innerHTML = "<input type='text' id='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' name='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' style='width:90px;'  autocomplete='off'  onfocus='onFocusDetailCode(this);autocompleteEntities1By20(this);' onblur='"+onblurfunction+"'/>"
		+" <img src='/egi/resources/erp2/images/plus1.gif' id='"+prefix+"["+slDetailTableIndex+"]"+suffix2+"' name='"+prefix+"["+slDetailTableIndex+"]"+suffix2+"' style='width:15px;'  onClick='"+onClickForSearch+"'/>";
		
	}
}


function createSLHiddenFieldFormatterJV(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		el.innerHTML = "<input type='text' id='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' name='"+prefix+"["+slDetailTableIndex+"]"+suffix+"'/>";
	}
}

function createSLLongTextFieldFormatterJV(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' name='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' readOnly style='width:120px;'/>";
	}
}

function createSLAmountFieldFormatterJV(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' name='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' onblur='validateDigitsAndDecimal(this);' maxlength='18' style='text-align:right;width:90px;'/>";
	}
}
function createLongTextFieldFormatterPayin(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+instrTableIndex+"]"+suffix+"' name='"+prefix+"["+instrTableIndex+"]"+suffix+"' readOnly style='width:90px;'/>";
	}
}
function createDropdownFormatterJV1(prefix){
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
            YAHOO.util.Event.addListener(selectEl,"change",onDropdownDetailTypeChange,selectedIndex,this);
			
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

function createDropdownFormatterJV(prefix){
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
function updateAccountTableIndex(){
	
	billDetailTableIndex = billDetailTableIndex +1 ;
}
function updateSLTableIndex(){
	
	 slDetailTableIndex = slDetailTableIndex +1 ;
}

function updateInstrTableIndex(){
	
	instrTableIndex = instrTableIndex +1 ;
}


function createSLTextFieldFormatterPJV(prefix,suffix,onblurfunction){
    return function(el, oRecord, oColumn, oData) {
		el.innerHTML = "<input type='text' id='"+prefix+"["+subLedgersTable.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+subLedgersTable.getRecordIndex(oRecord)+"]"+suffix+"' style='width:90px;' onblur='"+onblurfunction+"'/>";
	}
}
function createSLHiddenFieldFormatterPJV(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		el.innerHTML = "<input type='text' id='"+prefix+"["+subLedgersTable.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+subLedgersTable.getRecordIndex(oRecord)+"]"+suffix+"'/>";
	}
}
function createSLLongTextFieldFormatterPJV(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+subLedgersTable.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+subLedgersTable.getRecordIndex(oRecord)+"]"+suffix+"' readOnly style='width:180px;'/>";
	}
}

function createSLAmountFieldFormatterPJV(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+subLedgersTable.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+subLedgersTable.getRecordIndex(oRecord)+"]"+suffix+"' style='text-align:right;width:90px;'/>";
	}
}
function createTextFieldReadOnly(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+billDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+billDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"' readOnly style='width:90px;'/>";
	}
}
function createcheckbox(prefix,suffix,onclickfunction){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='checkbox' id='"+prefix+"["+billDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+billDetailsTable.getRecordIndex(oRecord)+"]"+suffix+"'  style='width:90px;'  onClick='"+onclickfunction+"'/>";
	}
}

function createSearchImageFormatterJV(prefix, suffix, onblurfunction) {
	return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = " <img src='/egi/resources/erp2/images/plus1.gif' id='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' name='"+prefix+"["+slDetailTableIndex+"]"+suffix+"' style='width:20px;'  onblur='fillNeibrAfterSplitGlcode(this)'/>";
	}
}

function calcTotal(){
	var amt=0;
	var table = document.getElementById('billDetailTable');
	var row = table.getElementsByTagName('tr');
	for(var i=0;i<row.length-2;i++)
	{
		chkBox = document.getElementById('iHeaderList['+i+'].selectChq');
		var val = document.getElementById('iHeaderList['+i+'].amount').value;
		if(chkBox.checked)
		{
			amt = amt + parseFloat(document.getElementById('iHeaderList['+i+'].amount').value);
		}
	}
	document.getElementById('totalAmount').value = amt;
}
var codeObj;
var entities;
var entitiesArray;
var acccodeArray;

function loadCoa(id){
	var coaCode = document.getElementById(id).value;
	if(coaCode != 'undefined' && coaCode!=''){
		var url;
		if(isNaN(coaCode))
			url = '/EGF/payment/advanceRequisitionPayment-ajaxLoadCoa.action?coaDescription='+coaCode;
		else
			url = '/EGF/payment/advanceRequisitionPayment-ajaxLoadCoa.action?coaCode='+coaCode;
	
		var req2 = initiateRequest();
		req2.onreadystatechange = function(){
		  if (req2.readyState == 4){
			  if (req2.status == 200){
				var codes2=req2.responseText;
				acccodeArray=codes2.split("+");
				codeObj = new YAHOO.widget.DS_JSArray(acccodeArray);
			  }
		  }
	 	};
		req2.open("GET", url, true);
		req2.send(null);
	}
}                 

function loadDropDownCodes()
{
	var	url = "/EGF/voucher/common-ajaxGetAllCoaCodes.action";
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
			for(i=0;i<acccodeArray.length;i++){
				data = acccodeArray[i].split("`~`")
				acccodeArray[i] = data[0];
				var key = data[0];
				var value = data[1]
				allGlcodes[key] = value;
			}			
			codeObj = new YAHOO.widget.DS_JSArray(acccodeArray);
		  }
	  }
 	};
	req2.open("GET", url, true);
	req2.send(null);
}

function loadDropDownCodesExcludingCashAndBank()
{
	var	url = "/EGF/voucher/common-ajaxGetAllCoaCodesExceptCashBank.action";
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
			for(i=0;i<acccodeArray.length;i++){
				data = acccodeArray[i].split("`~`")
				acccodeArray[i] = data[0];
				var key = data[0];
				var value = data[1]
				allGlcodes[key] = value;
			}			
			codeObj = new YAHOO.widget.DS_JSArray(acccodeArray);
		  }         
	  }
 	};
	req2.open("GET", url, true);
	req2.send(null);
}
/**
 *  this will load all non-subledger and only those subledgers which are mapped to given account detail type
 */       
function resetonChangeofSubledger()
{

	if(mode=='new'){
		//document.getElementById("commonBean.payto").value="";
		document.getElementById("detailName").value="";
		document.getElementById("detailKey").value="";
		document.getElementById("detailCode").value="";
	}
	
	if(document.getElementById("billDetailsTableNet[0].isSubledger").value=='true')
	{
	//	bootbox.alert("hii");      
		document.getElementById("billDetailsTableNet[0].glcodeDetail").value="-1";
		document.getElementById("billDetailsTableNet[0].glcodeDetail").disabled=true;
		document.getElementById("billDetailsTableNet[0].glcodeDetail").tabIndex="-1";
		document.getElementById("billDetailsTableNet[0].glcodeIdDetail").value="";
		document.getElementById("billDetailsTableNet[0].accounthead").value="";
		document.getElementById("billDetailsTableNet[0].debitAmountDetail").value="";
		document.getElementById("billDetailsTableNet[0].isSubledger").value="";
		document.getElementById("billDetailsTableNet[0].glcodeDetail").disabled=false;
	}
	var debitTable=document.getElementById('billDetailTable').getElementsByTagName('table')[0];
	var dtLen=debitTable.rows.length;
	 // bootbox.alert("Length of debit table is "+dtLen);
	// debit code table
	var i=0;
	var j=0;
	var netbilltableLen=billDetailsTable.getRecordSet().getLength();
	while(j<dtLen)
	{
		if(document.getElementById("billDetailsTable["+j+"].isSubledger")!=null &&  document.getElementById("billDetailsTable["+j+"].isSubledger").value=='true'){
			document.getElementById("billDetailsTable["+j+"].glcodeDetail").value="";
			document.getElementById("billDetailsTable["+j+"].glcodeIdDetail").value="";
			document.getElementById("billDetailsTable["+j+"].accounthead").value="";
			document.getElementById("billDetailsTable["+j+"].debitAmountDetail").value="";
			document.getElementById("billDetailsTable["+j+"].isSubledger").value="";
		}
		j++
	
	}
	// credit code table billDetailsTableCredit[0].glcodeDetail
	//var creditTable=document.getElementById('billDetailsTableCredit').getElementsByTagName('table')[0];
	//var creditTableLen=creditTable.rows.length;
	var creditTableLen=billDetailsTableCredit.getRecordSet().getLength();
	//bootbox.alert("Length of credit table is "+creditTableLen);
	while(i<creditTableLen)    
	{
		if(document.getElementById("billDetailsTableCredit["+i+"].isSubledger").value=='true')
		{
			document.getElementById("billDetailsTableCredit["+i+"].glcodeDetail").value="";
			document.getElementById("billDetailsTableCredit["+i+"].glcodeIdDetail").value="";
			document.getElementById("billDetailsTableCredit["+i+"].accounthead").value="";
			document.getElementById("billDetailsTableCredit["+i+"].debitAmountDetail").value="";
			document.getElementById("billDetailsTableCredit["+i+"].isSubledger").value="";
		}i++
	}
}
function loadDropDownCodesForAccountDetailType(obj)
{
     
//	bootbox.alert("Hiii  inside loadDropDownCodesForAccountDetailType");
	// reset deatiltable when subledger=true. 
	resetonChangeofSubledger();
	var val=0;
	if(obj==null)
		val=0;
	else
		val=obj.value;
		
	var	url = path+"/voucher/common-ajaxLoadCodesOfDetailType.action?accountDetailType="+val;
	var req2 = initiateRequest();
	req2.onreadystatechange = function()
	{
	  if (req2.readyState == 4)
	  {
		  if (req2.status == 200)
		  {
			var codes2=req2.responseText;
			
			codes2=	codes2.trim();
			
			
			var a = codes2.split("^");
			var codes = a[0];
			acccodeArray=codes.split("+");
			for(i=0;i<acccodeArray.length;i++){
				data = acccodeArray[i].split("`~`")
				acccodeArray[i] = data[0];
				var key = data[0];
				var value = data[1]
				codesForAccountDetailType[key] = value;
			}	
			codeObj = new YAHOO.widget.DS_JSArray(acccodeArray);
		  }
	  }
 	};
	req2.open("GET", url, true);
	req2.send(null);
}

function loadDropDownCodesForEntities(obj)
{
	if(entities)
	{
	entities=null;
	if(oAutoCompEntity)
		oAutoCompEntity.destroy();
	}	
	var	url = path+"/voucher/common-ajaxLoadEntites.action?accountDetailType="+obj.value;
	var req2 = initiateRequest();
	req2.onreadystatechange = function()
	{
	  if (req2.readyState == 4)
	  {
		  if (req2.status == 200)
		  {
			var entity=req2.responseText;
			
			var a = entity.split("^");
			var eachEntity = a[0];
			entitiesArray=eachEntity.split("+");
			//bootbox.alert(":"+entitiesArray[0]+":");
			entities = new YAHOO.widget.DS_JSArray(entitiesArray);
		  }
	  }
 	};
	req2.open("GET", url, true);
	req2.send(null);
}

function autocompleteEntitiesBy20()
{
		
	   oACDS = new YAHOO.widget.DS_XHR(path+"/voucher/common-ajaxLoadEntitesBy20.action", [ "~^"]);
	   oACDS.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
	   oACDS.scriptQueryParam = "startsWith";
	   var oAutoComp1 = new YAHOO.widget.AutoComplete('detailCode','codescontainer',oACDS);
	   oAutoComp1.doBeforeSendQuery = function(sQuery){
		   loadWaitingImage(); 
		   return sQuery+"&accountDetailType="+document.getElementById("commonBean.subledgerType").value;
	   } 
	   oAutoComp1.queryDelay = 0.5;
	   oAutoComp1.minQueryLength = 1;
	   oAutoComp1.prehighlightClassName = "yui-ac-prehighlight";
	   oAutoComp1.useShadow = true;
	   //oAutoComp1.forceSelection = true;
	   oAutoComp1.maxResultsDisplayed = 20;
	   oAutoComp1.useIFrame = true;
	   oAutoComp1.doBeforeExpandContainer = function(oTextbox, oContainer, sQDetauery, aResults) {
		   clearWaitingImage();
	           var pos = YAHOO.util.Dom.getXY(oTextbox);
	           pos[1] += YAHOO.util.Dom.get(oTextbox).offsetHeight + 6;
	           oContainer.style.width=300;
	           YAHOO.util.Dom.setXY(oContainer,pos);
	           return true;
	   };


	
}

function getDetailType()
{
	bootbox.alert("hello");
	return 
}


function autocompleteEntities1(obj,myEvent)
{
	//bootbox.alert('autocomplete');
	var src = obj;	
	var target = document.getElementById('codescontainer');	
	var posSrc=findPos(src); 
	
	target.style.left=posSrc[0]+"px";	
	target.style.top=posSrc[1]+22+"px";
	target.style.width=650;	
	      		
	
	var coaCodeObj=obj;
	//var  currRow=getRowIndex(obj);
	//40 --> Down arrow, 38 --> Up arrow
//	if(yuiflag[currRow] == undefined)
	//{
		var key = window.event ? window.event.keyCode : myEvent.charCode;  
		if(key != 40 )
		{
			if(key != 38 )
			{
				oAutoCompEntity = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', entities);
				oAutoCompEntity.queryDelay = 0;
				oAutoCompEntity.prehighlightClassName = "yui-ac-prehighlight";
				oAutoCompEntity.useShadow = true;
				oAutoCompEntity.maxResultsDisplayed = 15;
				oAutoCompEntity.useIFrame = true;
				if(entities)
				{
					entities.applyLocalFilter = true;
					entities.queryMatchContains = true;
				}
				oAutoCompEntity.minQueryLength = 0;
				oAutoCompEntity.formatResult = function(oResultData, sQuery, sResultMatch) {
					var data = oResultData.toString();
				    return data.split("`~`")[0];
				};
			}
		}
	//	yuiflag[currRow] = 1;
	//}	
}

function auto(obj)
{
	if(this.value!="")
	{
		var x=obj.value;
		var y=x.length;
		if (y%2==0)
		{
			autocompleteEntities1By20(obj);
		}
	}
}
var oAutoCompEntityForJV;
function autocompleteEntities1By20(obj)
{
  
	 oACDS = new YAHOO.widget.DS_XHR(path+"/voucher/common-ajaxLoadEntitesBy20.action", [ "~^"]);
	   oACDS.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
	   oACDS.scriptQueryParam = "startsWith";
	 //bootbox.alert(obj.name);
	   if (oAutoCompEntityForJV!= undefined) {
		   oAutoCompEntityForJV.destroy();
		   oAutoCompEntityForJV = null;
	   } 
	   
	   oAutoCompEntityForJV = new YAHOO.widget.AutoComplete(obj.name,'codescontainer',oACDS);
	   oAutoCompEntityForJV.doBeforeSendQuery = function(sQuery){
		   loadWaitingImage(); 
		   var detailTypeName=obj.name.replace('detailCode','detailType.id');
		   return sQuery+"&accountDetailType="+document.getElementById(detailTypeName).value;
	   } 
	   oAutoCompEntityForJV.queryDelay = 0.5;
	   oAutoCompEntityForJV.minQueryLength = 1;
	   oAutoCompEntityForJV.prehighlightClassName = "yui-ac-prehighlight";
	   oAutoCompEntityForJV.useShadow = true;
	   //oAutoCompEntityForJV.forceSelection = true;
	   oAutoCompEntityForJV.maxResultsDisplayed = 10;
	   oAutoCompEntityForJV.useIFrame = true;
	   oAutoCompEntityForJV.doBeforeExpandContainer = function(oTextbox, oContainer, sQDetauery, aResults) {
	           var pos = YAHOO.util.Dom.getXY(oTextbox);
	           pos[1] += YAHOO.util.Dom.get(oTextbox).offsetHeight + 6;
	           oContainer.style.width=300;
	           YAHOO.util.Dom.setXY(oContainer,pos);
	           return true;
	   };


	
}



var yuiflag = new Array();
function autocompletecode(obj,myEvent)
{
	//bootbox.alert('autocomplete');
	var src = obj;	
	var target = document.getElementById('codescontainer');	
	var posSrc=findPos(src); 
	target.style.left=posSrc[0];	
	target.style.top=posSrc[1]-40;
	target.style.width=450;	
	
	var coaCodeObj=obj;
	var  currRow=getRowIndex(obj);
	//40 --> Down arrow, 38 --> Up arrow
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
				/*oAutoComp.doBeforeExpandContainer = function(oTextbox, oContainer, sQDetauery, aResults) {
					 var pos = YAHOO.util.Dom.getXY(oTextbox);
					 pos[1] += YAHOO.util.Dom.get(oTextbox).offsetHeight + 6;
					 oContainer.style.width=300;
					 YAHOO.util.Dom.setXY(oContainer,pos);
					 return true;

				 };*/  
			}
		}
		yuiflag[currRow] = 1;
	}	
}

function autocompletecodeCommon(obj,myEvent)
{
	
	var src = obj;	
	//bootbox.alert(obj.scrollHeight);
	var target = document.getElementById('codescontainer');	
	var posSrc=findPos(src); 
	target.style.left=posSrc[0]+"px";	
	target.style.top=(posSrc[1]-((452/3)+20))+"px"; 
	target.style.width="450px";	
	var coaCodeObj=obj;
//if multiple tables are there this wont support
	//var  currRow=getRowIndex(obj);
	//40 --> Down arrow, 38 --> Up arrow
//	if(yuiflag[currRow] == undefined)
//	{
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
				if(codeObj!=null){
					codeObj.applyLocalFilter = true;
					codeObj.queryMatchContains = true;
				}
				oAutoComp.minQueryLength = 0;
				oAutoComp.formatResult = function(oResultData, sQuery, sResultMatch) {
					var data = oResultData.toString();
				    return data.split("`~`")[0];
				};
			}
		}
	//	yuiflag[currRow] = 1;
	//}	
}
var funcObj;
var funcArray;
function loadDropDownCodesFunction()
{
	var url = "/EGF/voucher/common-ajaxGetAllFunctionName.action";
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
			funcArray=codes.split("+");
			funcObj= new YAHOO.widget.DS_JSArray(funcArray);
		  }
	   }
	};
	req2.open("GET", url, true);
	req2.send(null);
}

var yuiflagFunc = new Array();
function autocompletecodeFunction(obj,myEvent)
{
	
	var src = obj;	
	var target = document.getElementById('codescontainer');	
	
	var posSrc=findPos(src); 
	target.style.left=posSrc[0];	
	target.style.top=posSrc[1]-40;
	target.style.width=450;	
		
	var coaCodeObj=obj;
	var  currRow=getRowIndex(obj);

	//40 --> Down arrow, 38 --> Up arrow
	if(yuiflagFunc[currRow] == undefined)
	{
		var key = window.event ? window.event.keyCode : myEvent.charCode;  
		if(key != 40 )
		{
			if(key != 38 )
			{
				var oAutoComp = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', funcObj);
				oAutoComp.queryDelay = 0;
				oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
				oAutoComp.useShadow = true;
				oAutoComp.maxResultsDisplayed = 15;
				oAutoComp.useIFrame = true;
				funcObj.applyLocalFilter = true;
				funcObj.queryMatchContains = true;
				oAutoComp.minQueryLength = 0;
				/*oAutoComp.doBeforeExpandContainer = function(oTextbox, oContainer, sQDetauery, aResults) {
					 var pos = YAHOO.util.Dom.getXY(oTextbox);
					 pos[1] += YAHOO.util.Dom.get(oTextbox).offsetHeight + 6;
					 oContainer.style.width=300;
					 YAHOO.util.Dom.setXY(oContainer,pos);
					 return true;

				 };  */
			}
		}
		yuiflagFunc[currRow] = 1;
	}	
}



function autocompletecodeFunctionHeader(obj,myEvent)
{
	var src = obj;	
	var target = document.getElementById('codescontainer');	
	var posSrc=findPos(src); 
	target.style.left=posSrc[0]+"px";	
	target.style.top=(posSrc[1]-((452/3)+10))+"px";  
	console.log(posSrc[1]);
	console.log(target.style.top);
	
	target.style.width=650;	
		
	var coaCodeObj=obj;
	//var  currRow=getRowIndex(obj);
	//40 --> Down arrow, 38 --> Up arrow
	//if(yuiflagFunc[currRow] == undefined)
	//{
		var key = window.event ? window.event.keyCode : myEvent.charCode;  
		if(key != 40 )
		{
			if(key != 38 )
			{
				var oAutoComp = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', funcObj);
				oAutoComp.queryDelay = 0;
				oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
				oAutoComp.useShadow = true;
				oAutoComp.maxResultsDisplayed = 15;
				oAutoComp.useIFrame = true;
				funcObj.applyLocalFilter = true;
				funcObj.queryMatchContains = true;
				oAutoComp.minQueryLength = 0;
				oAutoComp.formatResult = function(oResultData, sQuery, sResultMatch) {
					var data = oResultData.toString();
				    return data.split("`~`")[0];
				};
			}
		}
		//yuiflagFunc[currRow] = 1;
	//}	
}
function getRowIndex(obj)
{
	var temp =obj.name.split('[');
	var temp1 = temp[1].split(']');
	return temp1[0];
}

function fillNeibrAfterSplitGlcode(obj)
{ 
	var key = obj.value;
	var temp = obj.value;
	temp = temp.split("`-`");
	var currRow=getRowIndex(obj);
	/*var acchead= document.getElementById('billDetailslist['+currRow+'].accounthead').value;
	console.log(acchead);
	if(acchead !=null && acchead !=""){
		key = key+"`-`";
		key = key+acchead;
	}*/
	var accCodeid = allGlcodes[key];
	if(temp.length>1)
	{ 
		obj.value=temp[0];
		var currRow=getRowIndex(obj);
		document.getElementById('billDetailslist['+currRow+'].glcodeIdDetail').value=allGlcodes[key];
		document.getElementById('billDetailslist['+currRow+'].accounthead').value=temp[1].split("`~`")[0];
		var flag=false;
		for (var i=0; i<slDetailTableIndex;i++ )
		{
			for(var j=0; j<billDetailTableIndex;j++){
				if(null != document.getElementById(SUBLEDGERLIST+'['+i+'].glcode.id')){
					var subledgerSel = document.getElementById(SUBLEDGERLIST+'['+i+'].glcode.id').value;
					
				}
				if(null != document.getElementById(VOUCHERDETAILLIST+'['+j+'].glcodeIdDetail')){
					var billDetailSel = document.getElementById(VOUCHERDETAILLIST+'['+j+'].glcodeIdDetail').value;
				}
				if(subledgerSel == billDetailSel){
					
					flag = true;break;
				}
				
			}
			if(!flag){
				document.getElementById(SUBLEDGERLIST+'['+i+'].glcode.id').value=0;
				document.getElementById(SUBLEDGERLIST+'['+i+'].detailType.id').value=0;
				document.getElementById(SUBLEDGERLIST+'['+i+'].detailTypeName').value="";
				document.getElementById(SUBLEDGERLIST+'['+i+'].detailCode').value="";
				document.getElementById(SUBLEDGERLIST+'['+i+'].detailKeyId').value="";
				document.getElementById(SUBLEDGERLIST+'['+i+'].detailKey').value="";
				document.getElementById(SUBLEDGERLIST+'['+i+'].amount').value="";
			}
			
		}
		for (var i=0; i<slDetailTableIndex;i++ )
		{
			d=document.getElementById(SUBLEDGERLIST+'['+i+'].glcode.id');
			if(null != d){
				for(p=d.options.length-1;p>=0;p--)
				{
					var flag1 = false;
					for(var j=0; j<billDetailTableIndex;j++){
						if(null != document.getElementById(VOUCHERDETAILLIST+'['+j+'].glcodeIdDetail')){
							if(d.options[p].value == document.getElementById(VOUCHERDETAILLIST+'['+j+'].glcodeIdDetail').value){
									flag1=true;
							}			
						}
						
					}
					if(!flag1 && d.options[p].value !=0){
						d.remove(p);
					}
				}
			}
			
		}
		check();
	}else if (temp!="" &&(accCodeid==null || accCodeid=="")){
		/*bootbox.alert("Invalid Account Code selected .Please select code from auto complete.");
		obj.value="";*/
		//document.getElementById('billDetailslist['+currRow+'].glcodeIdDetail').value="";
	}
	var currRow=getRowIndex(obj);
	var funcObj = document.getElementById('billDetailslist['+currRow+'].functionDetail');
	if(funcObj)
	fillNeibrAfterSplitFunction(funcObj);
	
}
function fillNeibrAfterSplitFunction(obj)
{
	var temp = obj.value;
	var currRow=getRowIndex(obj);
	var funId = document.getElementById('billDetailslist['+currRow+'].functionIdDetail').value;
	temp = temp.split("`~`");
	if(temp.length>1)
	{ 
		obj.value=temp[0];
		if(document.getElementById('functionValue'))
			document.getElementById('functionValue').value =temp[0]; 
		if(document.getElementById('functionId'))
			document.getElementById('functionId').value =temp[1];
		document.getElementById('billDetailslist['+currRow+'].functionIdDetail').value=temp[1];
	}else if(temp!=""){
		/*//var functionValue = document.getElementById('functionValue').value;
		//var functionId1 = document.getElementById('functionId').value;
		var functionId2 = document.getElementById('billDetailslist['+currRow+'].functionIdDetail').value;
		if(functionValue=="" && functionId1==""){
			//bootbox.alert("Invalid function selected .Please select code from auto complete.");
			//obj.value="";
			document.getElementById('billDetailslist['+currRow+'].functionIdDetail').value="";
		}else if(functionValue!="" && functionId1!="" && functionId2!="" && functionValue != temp[0] && functionId1==functionId2){
			bootbox.alert("Invalid function selected .Please select code from auto complete.");
			obj.value="";
			document.getElementById("billDetailslist['+currRow+'].functionIdDetail").value="";
		}*/
		
		//bootbox.alert("Invalid function selected .Please select code from auto complete.");
		
	}
		
	loadSlFunction();
}
function loadSlFunction(){
	
	// CODE TO POPULATE THE FUNCTION SELECT BOX IN THE SUBLEDGER GRID IN JV.
	var accGridFunc;
	var accGridFuncId;
	var accountCode ;
	var accountCodeId;
	var functionArray = new Array();
	var functionIdArray = new Array();
	//var slAccountCodeArray = getSlAccountCodes(); // array contains the list of control codes.
	funcIdfuncAccCodeArray = new Array(); // requres for sorting in the SL grid based on the function or account code selection.
	accCodeFuncFuncIdArray = new Array();
	
	// PREPARING THE LIST OF FUNCTIONS THAT WILL POPULATE IN THE SUBLEDGER FUNCTION DROP DOWN LIST
	
	for (var i=0; i<billDetailTableIndex;i++ )
	{
		if(null != document.getElementById('billDetailslist['+i+'].functionDetail')){
			 accGridFunc =  document.getElementById('billDetailslist['+i+'].functionDetail').value;
		}
		if(null !=  document.getElementById('billDetailslist['+i+'].functionIdDetail')){
			accGridFuncId = document.getElementById('billDetailslist['+i+'].functionIdDetail').value;
		}
		if(null !=  document.getElementById('billDetailslist['+i+'].glcodeIdDetail')){
			 accountCodeId = document.getElementById('billDetailslist['+i+'].glcodeIdDetail').value;
			 accountCode = document.getElementById('billDetailslist['+i+'].glcodeDetail').value;
		}
		console.log(slAccountCodes);
		if(accGridFunc !=''  && slAccountCodes.indexOf(accountCodeId) !=-1){
			if(functionArray.indexOf(accGridFunc) == -1){
				functionArray.push(accGridFunc);
				functionIdArray.push(accGridFuncId);
			}
			funcIdfuncAccCodeArray.push(accGridFuncId+"~"+accGridFunc+"~"+accountCodeId+"~"+accountCode);
			funcIdfuncAccCodeArray.push("0~0~"+accountCodeId+"~"+accountCode);
			accCodeFuncFuncIdArray.push(accGridFuncId+"~"+accGridFunc+"~"+accountCodeId+"~"+accountCode);
			accCodeFuncFuncIdArray.push(accGridFuncId+"~"+accGridFunc+"~"+"0~0");
		}
		
	}	
	
	for (var i=0; i<slDetailTableIndex;i++ )
	{
		obj1 = document.getElementById('subLedgerlist['+i+'].functionDetail');
		if(obj1 != null){

			if(functionArray.length == 0){
				obj1.options.length=1;
				obj1.options[0].text='---Select---';
				obj1.options[0].value=0;
			}else{
				obj1.options.length = functionArray.length +1;
				for (var j=1; j<=functionArray.length;j++ )
				{
					obj1.options[j].text=functionArray[j-1];
					obj1.options[j].value=functionIdArray[j-1];
				}
			}
			
		}
		
		
	}
}
function getSlAccountCodes(){
	
	var slAccountCodeArray = new Array();
	var obj = document.getElementById('subLedgerlist[0].glcode.id');
	for (var j=0; j< obj.options.length;j++ )
	{
		console.log("---"+obj.options[j].value+"---");
		console.log("---"+(obj.options[j].value).trim()+"---");
		slAccountCodeArray.push((obj.options[j].value).trim());
		
	}
	return slAccountCodeArray;
}

function fillNeibrAfterSplitFunctionSL(obj)
{
	var temp = obj.value;
	temp = temp.split("`~`");
	var currRow=getRowIndex(obj);
	if(temp.length>1)
	{ 
		obj.value=temp[0];
		document.getElementById('subLedgerlist['+currRow+'].functionIdDetail').value=temp[1];
	}else{
		document.getElementById('subLedgerlist['+currRow+'].functionIdDetail').value='';
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


var onDropdownDetailTypeChange = function(index,obj) { 
	var detailtypeidObj=document.getElementById('subLedgerlist['+obj.value+'].detailType.id');
	if(detailTypeId != detailtypeidObj.value){ // checks if the subledgercodes already loaded for that detail type
		detailTypeId = detailtypeidObj.value;
		//loadDropDownCodesForEntities(detailtypeidObj); 
	}
	
};
function onFocusDetailCode(obj){
	var currRow=getRowIndex(obj);
	var detailtypeidObj=document.getElementById('subLedgerlist['+currRow+'].detailType.id');
	if(detailTypeId != detailtypeidObj.value){
		detailTypeId = detailtypeidObj.value;
		loadDropDownCodesForEntities(detailtypeidObj); 
	}
}
function loadSLFunc(selectedIndex,funcSelected){

	var filterFuncArray = new Array();
	var accCodeObj = document.getElementById('subLedgerlist['+selectedIndex.value+'].glcode.id'); 
	
	for(var i=0 ; i<accCodeFuncFuncIdArray.length;i++){
		var tokens = accCodeFuncFuncIdArray[i].split("~");
		
		if(accCodeObj.value == tokens[2] ){
			
			if(filterFuncArray.indexOf(tokens[0]+"~"+tokens[1]) == -1){
				filterFuncArray.push(tokens[0]+"~"+tokens[1]);
			}
			
		}
	}
	slFuncObj = document.getElementById('subLedgerlist['+selectedIndex.value+'].functionDetail');
	slFuncObj.options.length=filterFuncArray.length+1;
	for(var i=0 ; i<filterFuncArray.length;i++){
		var tokens = filterFuncArray[i].split("~");
		slFuncObj.options[i+1].text=tokens[1];
		slFuncObj.options[i+1].value=tokens[0];
	}
	slFuncObj.value = funcSelected ;
}
var onDropdownChange = function(index,obj) { 
		// loadSLFunc(obj,document.getElementById('subLedgerlist['+obj.value+'].functionDetail').value);
		var subledgerid=document.getElementById('subLedgerlist['+obj.value+'].glcode.id');
		var accountCode = subledgerid.options[subledgerid.selectedIndex].text;
		console.log("---"+accountCode+"-------");
		document.getElementById('subLedgerlist['+obj.value+'].subledgerCode').value =accountCode;
		if(accountCode != '---Select---'){
			var url = path+'/voucher/common-getDetailType.action?accountCode='+accountCode+'&index='+obj.value;
			var transaction = YAHOO.util.Connect.asyncRequest('POST', url, postType, null);
		}else{
				var d = document.getElementById('subLedgerlist['+obj.value+'].detailType.id');
				d.options.length=1;
				d.options[0].text='---Select---';
				d.options[0].value=0;
		}
};
var postType = {
success: function(o) {
		var detailType= o.responseText;
		var detailRecord = detailType.split('#');
		var eachItem;
		var obj;
		for(var i=0;i<detailRecord.length;i++)
		{
			eachItem =detailRecord[i].split('~');
			if(obj==null)
			{
				obj = document.getElementById('subLedgerlist['+parseInt(eachItem[0])+']'+'.detailType.id');
				if(obj!=null)
					obj.options.length=detailRecord.length+1;
			}
			if(obj!=null)
			{
				obj.options[i+1].text=eachItem[1];
				obj.options[i+1].value=eachItem[2];
				document.getElementById('subLedgerlist['+parseInt(eachItem[0])+']'+'.detailTypeName').value = eachItem[1];
			}
			
			if(eachItem.length==1) // for deselect the subledger code
			{
				var d = document.getElementById('subLedgerlist['+i+'].detailType.id');
				d.options.length=1;
				d.options[0].text='---Select---';
				d.options[0].value=0;
			}
		} 
    },
    failure: function(o) {
    	bootbox.alert('failure');
    }
}
function check(){
	var accountCodes=new Array();
	for(var i=0;i<billDetailTableIndex+1;i++){
	if(null != document.getElementById('billDetailslist['+i+'].glcodeDetail')){
		accountCodes[i] = document.getElementById('billDetailslist['+i+'].glcodeDetail').value;
	}
	}
	var url = path+'/voucher/common-getDetailCode.action?accountCodes='+accountCodes;
	var transaction = YAHOO.util.Connect.asyncRequest('POST', url, callbackJV, null);
}
var callbackJV = {
success: function(o) {
		var test= o.responseText;
		test = test.split('~');
		for (var j=0; j<slDetailTableIndex;j++ )
		{
			
			if(null != document.getElementById('subLedgerlist['+j+'].glcode.id')&& test.length >1 )
			{
				d=document.getElementById('subLedgerlist['+j+'].glcode.id');
				d.options.length=((test.length)/2)+1;
				for (var i=1; i<((test.length)/2)+1;i++ )
				{
					d.options[i].text=test[i*2-2];
					d.options[i].value=test[i*2 -1];
					
				}
			} 
			if(test.length<2)
			{
				var d = document.getElementById('subLedgerlist['+j+'].glcode.id');
				if(d)
				{
				d.options.length=1;
				d.options[0].text='---Select---';
				d.options[0].value=0;
				}
			}
		}
			
    },
    failure: function(o) {
    	bootbox.alert('failure');
    }
}

function loaddropdown(){
//bootbox.alert(coming);
	
}
function updateDebitAmountJV()
{	
	var amt=0;
	
	for(var i=0;i<billDetailTableIndex+1;i++)
	{
		
		if(null != document.getElementById('billDetailslist['+i+'].debitAmountDetail')){
			var val = document.getElementById('billDetailslist['+i+'].debitAmountDetail').value;
			if(val!="" && !isNaN(val))
			{
				amt = amt + parseFloat(val);
			}
		}
	}
	document.getElementById('totaldbamount').value = amountConverter(amt);
}

function updateCreditAmountJV()
{
	var amt=0;
	for(var i=0;i<billDetailTableIndex+1;i++)
	{
		if(null != document.getElementById('billDetailslist['+i+'].creditAmountDetail')){
			var val = document.getElementById('billDetailslist['+i+'].creditAmountDetail').value;
			
			if(val!="" && !isNaN(val))
			{
				amt = amt + parseFloat(val);
			}
		}
		
		
	}
	document.getElementById('totalcramount').value = amountConverter(amt);
}

function amountConverter(amt) {
	var formattedAmt = amt.toFixed(2);
	return formattedAmt;
}

function updateDebitAmount()
{
	var amt=0;
	var tbody = document.getElementById('billDetailTable').getElementsByTagName('tbody')[0];
	var table = document.getElementById('billDetailTable');
	var row = table.getElementsByTagName('tr');
	for(var i=0;i<row.length-3;i++)
	{
		var val = document.getElementById('billDetailslist['+i+'].debitAmountDetail').value;
		if(val=='') val=0;
		if(val!="" && !isNaN(val))
		{
			amt = amt + parseFloat(document.getElementById('billDetailslist['+i+'].debitAmountDetail').value);
		}
	}
	document.getElementById('totaldbamount').value = amt;
}

function updateCreditAmount()
{
	var amt=0;
	var tbody = document.getElementById('billDetailTable').getElementsByTagName('tbody')[0];
	var table = document.getElementById('billDetailTable');
	var row = table.getElementsByTagName('tr');
	for(var i=0;i<row.length-3;i++)
	{
		var val = document.getElementById('billDetailslist['+i+'].creditAmountDetail').value;
		if(val=='') val=0;
		if(val!="" && !isNaN(val))
		{
			amt = amt + parseFloat(document.getElementById('billDetailslist['+i+'].creditAmountDetail').value);
		}
	}
	document.getElementById('totalcramount').value = amt;
}

function validateDetailCode(obj)
{
	var index = getRowIndex(obj);
	var element = document.getElementById(SUBLEDGERLIST+'['+index+']'+'.detailType.id');
	var detailtypeid = element.options[element.selectedIndex].value;
	var url = path+'/voucher/preApprovedVoucher-afillNeibrAfterSplitFunctionCommonjaxValidateDetailCode.action?code='+obj.value+'&detailtypeid='+detailtypeid+'&index='+index;
	var transaction = YAHOO.util.Connect.asyncRequest('POST', url, callbackPJV, null);
}

// in the place of validateDetailCodeForJV method 

function splitEntitiesDetailCode(obj)
{
	var currRow=getRowIndex(obj);
	var entity=obj.value;
	if(entity.trim()!="")
	{
		var entity_array=entity.split("`~`");
		if(entity_array.length==2)
		{
			document.getElementById(SUBLEDGERLIST+'['+currRow+']'+'.detailCode').value=entity_array[0].split("`-`")[0];
			document.getElementById(SUBLEDGERLIST+'['+currRow+']'+'.detailKeyId').value=entity_array[1];
			document.getElementById(SUBLEDGERLIST+'['+currRow+']'+'.detailKey').value=entity_array[0].split("`-`")[1];
		}
	}

}

function splitSchemeCode(obj)
{
	var entity=obj.value;
	if(entity.trim()!="")
	{
		var entity_array=entity.split("`~`");
		if(entity_array.length==2)
		{
			document.getElementById('subScheme.scheme.name').value=entity_array[0].split("`-`")[1];
			document.getElementById('schemeId').value=entity_array[1];
		}else
		{
			document.getElementById('schemeId').value="";
		}
	}else
	{
		document.getElementById('schemeId').value="";	
	}

}

function splitSubSchemeCode(obj)
{
	var entity=obj.value;
	if(entity.trim()!="")
	{
		var entity_array=entity.split("`~`");
		if(entity_array.length==2)
		{
			document.getElementById('subScheme.name').value=entity_array[0].split("`-`")[1];
			document.getElementById('subSchemeId').value=entity_array[1];
		}else
		{
			document.getElementById('subSchemeId').value="";
		}
	}else
	{
		document.getElementById('subSchemeId').value="";
	}

}


function openSearchWindowFromJV(obj) {

	var index = getRowIndex(obj);
	acctTypeCurrRow = index;
	var element = document.getElementById(SUBLEDGERLIST+'['+index+']'+'.detailType.id');
	var detailtypeid = element.options[element.selectedIndex].value;
	if( detailtypeid != null && detailtypeid != 0) {
		var	url = "../voucher/common-searchEntites.action?accountDetailType="+detailtypeid;
		window.open(url, 'EntitySearch','resizable=no,scrollbars=yes,left=300,top=40, width=400, height=500');
	} else {
		bootbox.alert("Select the Type.");
	}
}

function popupCallback(arg0, srchType) {
	var entity_array = arg0.split("^#");
	if(srchType == 'EntitySearch' ) {
		if(entity_array.length==3)
		{
			document.getElementById(SUBLEDGERLIST+'['+acctTypeCurrRow+']'+'.detailCode').value=entity_array[0];
			document.getElementById(SUBLEDGERLIST+'['+acctTypeCurrRow+']'+'.detailKey').value=entity_array[1];
			document.getElementById(SUBLEDGERLIST+'['+acctTypeCurrRow+']'+'.detailKeyId').value=entity_array[2];
		}
		else
		{
			bootbox.alert("Invalid entity selected.");
			document.getElementById(SUBLEDGERLIST+'['+acctTypeCurrRow+']'+'.detailCode').value="";
			document.getElementById(SUBLEDGERLIST+'['+acctTypeCurrRow+']'+'.detailKeyId').value="";
			document.getElementById(SUBLEDGERLIST+'['+acctTypeCurrRow+']'+'.detailKey').value="";
		}
	}
}

function validateDetailCodeForJV(obj)
{
	var index = getRowIndex(obj);
	var element = document.getElementById(SUBLEDGERLIST+'['+index+']'+'.detailType.id');
	var detailtypeid = element.options[element.selectedIndex].value;
	var url = path+'/voucher/common-ajaxValidateDetailCode.action?code='+obj.value+'&detailtypeid='+detailtypeid+'&index='+index;
	var transaction = YAHOO.util.Connect.asyncRequest('POST', url, callbackPJV, null);
}
var callbackPJV = {
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
	    	bootbox.alert('failure');
	    }
	}

function validateDetailCodeCommon(obj)
{
	//var index = getRowIndex(obj);
	var element = document.getElementById('commonBean.subledgerType');
	var detailtypeid = element.options[element.selectedIndex].value;
	var url = path+'/voucher/common-ajaxValidateDetailCode.action?code='+obj.value+'&detailtypeid='+detailtypeid+'&index=0';
	var transaction = YAHOO.util.Connect.asyncRequest('POST', url, callbackCommon, null);
}

var callbackCommon = {
	success: function(o) {
		var res= o.responseText;
		res = res.split('~');
		if(res.length>2)
		{
			document.getElementById('detailKey').value=res[1];
			document.getElementById('detailName').value=res[2];
		}
		else
		{
			bootbox.alert('Enter valid Code');
			document.getElementById('detailKey').value='';
			document.getElementById('detailName').value='';
			return;
		}
    },
    failure: function(o) {
    	bootbox.alert('failure');
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

function refreshInbox()
{
	if(opener && opener.top.document.getElementById('inboxframe'))
		opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
}

function validate()
{
	var dbtotal=document.getElementById('totaldbamount').value;
	var crtotal=document.getElementById('totalcramount').value;
	if(dbtotal=='' || dbtotal==0)
	{
		bootbox.alert('Total Debit Amount can not be zero.');
		return false;
	}
	if(crtotal=='' || crtotal==0)
	{
		bootbox.alert('Total Credit Amount can not be zero.');
		return false;
	}
	if(dbtotal!=crtotal)
	{
		bootbox.alert('Total Debit & Credit amount should be same.');
		return false;
	}
	return true;
}

String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
}

function checkLength(obj)
{
	if(obj.value.length>1024)
	{
		bootbox.alert('Max 1024 characters are allowed for comments. Remaining characters are truncated.')
		obj.value = obj.value.substring(1,1024);
	}
}

function checkVoucherNarrationLen(obj)
{
	if(obj.value.length>250)
	{
		bootbox.alert('Max 250 characters are allowed for Narration. Remaining characters are truncated.')
		obj.value = obj.value.substring(0,250);
	}
}

function updatecheckBox(field,index,value){
	
	if(value == 'true'){
		document.getElementById(INSTRUMENTLIST+'['+index+'].'+field).checked=true;
	}
}


   
function findPos(ob) 
{
	var obj=eval(ob);
	var curleft = curtop = 0;
	if (obj.offsetParent) 
	{
		curleft = obj.offsetLeft;
		curtop = obj.offsetTop;
		while (obj = obj.offsetParent) 
		{	//bootbox.alert(obj.nodeName+"---"+obj.offsetTop+"--"+obj.offsetLeft+"-----"+curtop);
			curleft =curleft + obj.offsetLeft;
			curtop =curtop + obj.offsetTop; 
			//bootbox.alert(curtop);
		}
	}
	//bootbox.alert(curleft+"             "+curtop);
	return [curleft,curtop];
		
}
	/*
	function findPos( oElement ) {
		  if( typeof( oElement.offsetParent ) != 'undefined' ) {
		    var originalElement = oElement;
		    for( var posX = 0, posY = 0; oElement; oElement = oElement.offsetParent ) {
		      posX += oElement.offsetLeft;
		      posY += oElement.offsetTop;
		      if( oElement != originalElement && oElement != document.body && oElement != document.documentElement ) {
		     //  bootbox.alert(oElement.scrollTop+""+oElement.nodeName)
		    	  posX -= oElement.scrollLeft;
		        posY -= oElement.scrollTop;
		      }
		    }
		    return [ posX, posY ];
		  } else {
		    return [ oElement.x, oElement.y ];
		  }
		}*/


   
function limitDigits(obj)
{



}

// for CBILL and Common


function functionFormatter(tableName,columnName,type){
    return function(el, oRecord, oColumn, oData) {
    	var table_name=eval(tableName);
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		var index=table_name.getRecordIndex(oRecord);
		el.innerHTML = "<input type='"+type+"'  id='"+tableName+"["+index+"]"+columnName+"' name='"+tableName+"["+index+"]"+columnName+"'  onkeyup='autocompletecodeFunction(this,event)' autocomplete='off' onblur='fillNeibrAfterSplitFunctionCommon(this)' size='25' />";
		el.innerHTML = el.innerHTML+"<input type='hidden'  id='"+tableName+"["+index+"]"+".functionIdDetail"+"' name='"+tableName+"["+index+"]"+".functionIdDetail"+"'/>";
	}
		
}

function functionidFormatter(tableName,columnName,type){
    return function(el, oRecord, oColumn, oData) {
    	 var table_name=eval(tableName);  var index=table_name.getRecordIndex(oRecord);  	 var fieldName=tableName+"["+index+"]"+columnName;  	 while(document.getElementById(fieldName))    	 {    		 index++;    fieldName=tableName+"["+index+"]"+columnName; 	 }
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='"+type+"'  id='"+tableName+"["+index+"]"+columnName+"' name='"+tableName+"["+index+"]"+columnName+"'/>";
		
	}
		
}   

function glcodeFormatter(tableName,columnName,type){
    return function(el, oRecord, oColumn, oData) {
    	 var table_name=eval(tableName);  var index=table_name.getRecordIndex(oRecord);  	 var fieldName=tableName+"["+index+"]"+columnName;  	 while(document.getElementById(fieldName))    	 {    		 index++;    fieldName=tableName+"["+index+"]"+columnName; 	 }
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='"+type+"'  id='"+tableName+"["+index+"]"+columnName+"' name='"+tableName+"["+index+"]"+columnName+"' onkeyup='autocompletecodeCommon(this,event)' autocomplete='off' onblur='fillNeibrAfterSplitGlcodeCommon(this)' size='15'/>";
		el.innerHTML = el.innerHTML+ "<input type='hidden'  id='"+tableName+"["+index+"]"+".glcodeIdDetail"+"' name='"+tableName+"["+index+"]"+".glcodeIdDetail"+"'/>";
		el.innerHTML = el.innerHTML+ "<input type='hidden'  id='"+tableName+"["+index+"]"+".isSubledger"+"' name='"+tableName+"["+index+"]"+".isSubledger"+"'/>";
	}
		
}   

function glcodeFormatterCbillModify(tableName,columnName,type){
    return function(el, oRecord, oColumn, oData) {
    	 var table_name=eval(tableName);  var index=table_name.getRecordIndex(oRecord);  	 
    	 var fieldName=tableName+"["+index+"]"+columnName;  	 
    	 while(document.getElementById(fieldName)) 
    	 {    		 index++;  //bootbox.alert(index) ;
    	 fieldName=tableName+"["+index+"]"+columnName; 
    	 }
    	
    	 var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='"+type+"'  id='"+tableName+"["+index+"]"+columnName+"' name='"+tableName+"["+index+"]"+columnName+"' onkeyup='autocompletecodeCommon(this,event)' autocomplete='off' onblur='fillNeibrAfterSplitGlcodeModify(this)' size='15'/>";
		el.innerHTML = el.innerHTML+ "<input type='hidden'  id='"+tableName+"["+index+"]"+".glcodeIdDetail"+"' name='"+tableName+"["+index+"]"+".glcodeIdDetail"+"'/>";
		el.innerHTML = el.innerHTML+ "<input type='hidden'  id='"+tableName+"["+index+"]"+".isSubledger"+"' name='"+tableName+"["+index+"]"+".isSubledger"+"'/>";
	}
		
}
function subledgerFormatter(tableName,columnName,type){
    return function(el, oRecord, oColumn, oData) {
    	 var table_name=eval(tableName);  var index=table_name.getRecordIndex(oRecord);  	 var fieldName=tableName+"["+index+"]"+columnName;  	 while(document.getElementById(fieldName))    	 {    		 index++;    fieldName=tableName+"["+index+"]"+columnName; 	 }
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='"+type+"'  id='"+tableName+"["+index+"]"+columnName+"' name='"+tableName+"["+index+"]"+columnName+"' onkeyup='autocompletecodeCommon(this,event)' autocomplete='off' onblur=fillNeibrAfterSplitSubledgercodeCommon(this,'subledgerCode') size='15'/>";
		el.innerHTML = el.innerHTML+ "<input type='hidden'  id='"+tableName+"["+index+"]"+".glcodeIdDetail"+"' name='"+tableName+"["+index+"]"+".glcodeIdDetail"+"'/>";
		el.innerHTML = el.innerHTML+ "<input type='hidden'  id='"+tableName+"["+index+"]"+".isSubledger"+"' name='"+tableName+"["+index+"]"+".isSubledger"+"'/>";
	}
		
}
function glcodeidFormatter(tableName,columnName,type){
    return function(el, oRecord, oColumn, oData) {
    	 var table_name=eval(tableName);  var index=table_name.getRecordIndex(oRecord);  	 var fieldName=tableName+"["+index+"]"+columnName;  	 while(document.getElementById(fieldName))    	 {    		 index++;    fieldName=tableName+"["+index+"]"+columnName; 	 }
		var value = (YAHOO.lang.isValue(oData))?oData:"";  
		el.innerHTML = "<input type='"+type+"'  id='"+tableName+"["+index+"]"+columnName+"' name='"+tableName+"["+index+"]"+columnName+"'/>";
		
	}
		
}

function accountheadFormatter(tableName,columnName,type){
    return function(el, oRecord, oColumn, oData) {
    	 var table_name=eval(tableName);  var index=table_name.getRecordIndex(oRecord);  	 var fieldName=tableName+"["+index+"]"+columnName;  	 while(document.getElementById(fieldName))    	 {    		 index++;    fieldName=tableName+"["+index+"]"+columnName; 	 }
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='"+type+"'  id='"+tableName+"["+index+"]"+columnName+"' name='"+tableName+"["+index+"]"+columnName+"' readOnly tabindex='-1' size='100'/>";  
		
	}
		
}
function accountheadFormatter1(tableName,columnName,type){
    return function(el, oRecord, oColumn, oData) {
    	 var table_name=eval(tableName);  var index=table_name.getRecordIndex(oRecord);  	 var fieldName=tableName+"["+index+"]"+columnName;  	 while(document.getElementById(fieldName))    	 {    		 index++;    fieldName=tableName+"["+index+"]"+columnName; 	 }
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='"+type+"'  id='"+tableName+"["+index+"]"+columnName+"' name='"+tableName+"["+index+"]"+columnName+"' readOnly tabindex='-1' size='50'/>";  
		
	}
		
}

function amountFormatter(tableName,columnName,type){
    return function(el, oRecord, oColumn, oData) {
    	 var table_name=eval(tableName);  var index=table_name.getRecordIndex(oRecord);  	 var fieldName=tableName+"["+index+"]"+columnName;  	 while(document.getElementById(fieldName))    	 {    		 index++;    fieldName=tableName+"["+index+"]"+columnName; 	 }
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='"+type+"'  id='"+tableName+"["+index+"]"+columnName+"' name='"+tableName+"["+index+"]"+columnName+"'  onblur='validateDigitsAndDecimal(this);calculateNet(this)' style='text-align:right' maxlength='15' size='15'/>";
		
	}
		
}

function amountFormatterForGrid(tableName,columnName,type){
    return function(el, oRecord, oColumn, oData) {
    	 var table_name=eval(tableName);  var index=table_name.getRecordIndex(oRecord);  	 var fieldName=tableName+"["+index+"]"+columnName;  	 while(document.getElementById(fieldName))    	 {    		 index++;    fieldName=tableName+"["+index+"]"+columnName; 	 }
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='"+type+"'  id='"+tableName+"["+index+"]"+columnName+"' name='"+tableName+"["+index+"]"+columnName+"'  onblur='validateDigitsAndDecimal(this);calculateNetForGrid(this)' style='text-align:right' maxlength='15' size='15'/>";
		
	}
		
}

function detailnameFormatter(tableName,columnName,type)
{
	 return function(el, oRecord, oColumn, oData) {
    	var table_name=eval(tableName);  var index=table_name.getRecordIndex(oRecord);  	 var fieldName=tableName+"["+index+"]"+columnName;  	 while(document.getElementById(fieldName))    	 {    		 index++;    fieldName=tableName+"["+index+"]"+columnName; 	 }
    	var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='"+type+"'  id='"+tableName+"["+index+"]"+columnName+"' name='"+tableName+"["+index+"]"+columnName+"' size='33'/>";
		el.innerHTML = el.innerHTML+ "<input type='hidden'  id='"+tableName+"["+index+"].detailKey' name='"+tableName+"["+index+"].detailKey'/>";		
	}
}

function detailcodeFormatter(tableName,columnName,type)
{
	 return function(el, oRecord, oColumn, oData) {
    	 var table_name=eval(tableName);  var index=table_name.getRecordIndex(oRecord);  	 var fieldName=tableName+"["+index+"]"+columnName;  	 while(document.getElementById(fieldName))    	 {    		 index++;    fieldName=tableName+"["+index+"]"+columnName; 	 }
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='"+type+"'  id='"+tableName+"["+index+"]"+columnName+"' name='"+tableName+"["+index+"]"+columnName+"' onkeyup='autocompleteEntities(this,event)' autocomplete='off' onblur='splitEntitiesForGrid(this)' size='10'/>";
	 }
}


function updateGridCbill(tableName,field,index,value){
	document.getElementById(tableName+'['+index+'].'+field).value=value;
}

function fillNeibrAfterSplitFunctionHeader(obj)
{
	var temp = obj.value;
	temp = temp.split("`~`");
	if(temp.length>1)
	{ 
		obj.value=temp[0].split("`-`")[0]+'-'+temp[0].split("`-`")[1];
		document.getElementById("commonBean.functionId").value=temp[1];
	}
}


function fillNeibrAfterSplitGlcodeCommon(obj)
{
	var temp = obj.value;
	var key = obj.value;
//this is reslut for subledger entity to be selected 
//default is true for all non subledger
//every jsp or helper js should return true or false based on subleder data load is complete or error
if(temp.trim()!="")
{
	var sublegerLoaded=true;
	temp = temp.split("`-`");
	if(temp.length>1)
	{ 
		if(codesForAccountDetailType[key].split("`-`").length==2 && codesForAccountDetailType[key].split("`-`")[1]=="true")
		{
			sublegerLoaded=loadSubledgerGrids(temp);
		}
		if(sublegerLoaded){
			obj.value=temp[0];
			var currRow=getRowIndex(obj);
			var hiddenfieldname=obj.name;
			var accountHeadeName=obj.name;
			var isSubledger=obj.name;
			hiddenfieldname=hiddenfieldname.replace("glcodeDetail","glcodeIdDetail");
			accountHeadeName=accountHeadeName.replace("glcodeDetail","accounthead");
			isSubledger=isSubledger.replace("glcodeDetail","isSubledger");
//			bootbox.alert(codesForAccountDetailType[key]);
	//		bootbox.alert(key);			
			document.getElementById(hiddenfieldname).value=codesForAccountDetailType[key].split("`-`")[0];
			
			document.getElementById(accountHeadeName).value=key.split("`-`")[1];
			if(codesForAccountDetailType[key].split("`-`").length==2)
			{
				document.getElementById(isSubledger).value=codesForAccountDetailType[key].split("`-`")[1];
			}
			else
			{
				document.getElementById(isSubledger).value="false";
			}
		}else
		{
			obj.value="";
			var hiddenfieldname=obj.name;
			var accountHeadeName=obj.name;
			var isSubledger=obj.name;
			hiddenfieldname=hiddenfieldname.replace("glcodeDetail","glcodeIdDetail");
			accountHeadeName=accountHeadeName.replace("glcodeDetail","accounthead");
			isSubledger=isSubledger.replace("glcodeDetail","isSubledger");
			
			document.getElementById(hiddenfieldname).value="";
			document.getElementById(accountHeadeName).value="";
			document.getElementById(isSubledger).value="";
		}
		
		
	}else
	{
	
		//bootbox.alert(obj.value+":"+invalidAccountCode); this line will cause problem for mouse selection
		obj.value="";
		var hiddenfieldname=obj.name;
		var accountHeadeName=obj.name;
		var isSubledger=obj.name;
		hiddenfieldname=hiddenfieldname.replace("glcodeDetail","glcodeIdDetail");
		accountHeadeName=accountHeadeName.replace("glcodeDetail","accounthead");
		isSubledger=isSubledger.replace("glcodeDetail","isSubledger");
		
		document.getElementById(hiddenfieldname).value="";
		document.getElementById(accountHeadeName).value="";
		document.getElementById(isSubledger).value="";   
	}
}		
}

function fillNeibrAfterSplitGlcodeModify(obj)
{
	var temp = obj.value;
	var key = obj.value;
//this is reslut for subledger entity to be selected 
//default is true for all non subledger
//every jsp or helper js should return true or false based on subleder data load is complete or error
if(temp.trim()!="")
{
	var sublegerLoaded=true;
	temp = temp.split("`-`");
	if(temp.length>1)
	{ 
		/*if(temp.length==3)
		{
			sublegerLoaded=loadSubledgerGrids(temp);
		}*/
		if(sublegerLoaded){
			obj.value=temp[0];
			var currRow=getRowIndex(obj);
			var hiddenfieldname=obj.name;
			var accountHeadeName=obj.name;
			var isSubledger=obj.name;
			hiddenfieldname=hiddenfieldname.replace("glcodeDetail","glcodeIdDetail");
			accountHeadeName=accountHeadeName.replace("glcodeDetail","accounthead");
			isSubledger=isSubledger.replace("glcodeDetail","isSubledger");
			document.getElementById(hiddenfieldname).value=codesForAccountDetailType[key].split("`-`")[0];
			document.getElementById(accountHeadeName).value=key.split("`-`")[1];
			if(codesForAccountDetailType[key].split("`-`").length==2)
			{
				document.getElementById(isSubledger).value=codesForAccountDetailType[key].split("`-`")[1];
			}
			else
			{
				document.getElementById(isSubledger).value="false";
			}
		}else
		{
			obj.value="";
			var hiddenfieldname=obj.name;
			var accountHeadeName=obj.name;
			var isSubledger=obj.name;
			hiddenfieldname=hiddenfieldname.replace("glcodeDetail","glcodeIdDetail");
			accountHeadeName=accountHeadeName.replace("glcodeDetail","accounthead");
			isSubledger=isSubledger.replace("glcodeDetail","isSubledger");
			
			document.getElementById(hiddenfieldname).value="";
			document.getElementById(accountHeadeName).value="";
			document.getElementById(isSubledger).value="";
		}
		
		
	}
}		
}
function fillNeibrAfterSplitSubledgercodeCommon(obj,name)
{
	//var key = obj.value;
	var temp = obj.value;
	/*var value = allGlcodes[key];
	var secondPart = value.split("`-`");
	var id = secondPart[0];*/
//this is reslut for subledger entity to be selected 
//default is true for all non subledger
//every jsp or helper js should return true or false based on subleder data load is complete or error
if(temp.trim()!="")
{
	var sublegerLoaded=true;
	temp = temp.split("`-`");
	if(temp.length>1)
	{ 
		/*if(secondPart.length==2)
		{
			//sublegerLoaded=loadSubledgerGrids(temp);
		}*/
		if(sublegerLoaded)
		{
		obj.value=temp[0];
		var currRow=getRowIndex(obj);
		var hiddenfieldname=obj.name;
		var accountHeadeName=obj.name;
		var isSubledger=obj.name;
		hiddenfieldname=hiddenfieldname.replace(name,"glcodeIdDetail");
		accountHeadeName=accountHeadeName.replace(name,"accounthead");
		isSubledger=isSubledger.replace(name,"isSubledger");
		
		document.getElementById(hiddenfieldname).value=temp[1].split("~")[1].replace("`","");
		document.getElementById(accountHeadeName).value=temp[1].split("~")[0];
		if(temp.length==3)
			document.getElementById(isSubledger).value=temp[2].replace("`","");
		
		}else
		{
			obj.value="";
			var hiddenfieldname=obj.name;
			var accountHeadeName=obj.name;
			var isSubledger=obj.name;
			hiddenfieldname=hiddenfieldname.replace(name,"glcodeIdDetail");
			accountHeadeName=accountHeadeName.replace(name,"accounthead");
			isSubledger=isSubledger.replace(name,"isSubledger");
			
			document.getElementById(hiddenfieldname).value="";
			document.getElementById(accountHeadeName).value="";
			document.getElementById(isSubledger).value="";
		}
		
		
	}
}		
}

function validateDigits(obj)
{
	if(isNaN(obj.value.trim()))
	{
		bootbox.alert("Invalid Amount")
		obj.value=0;
		obj.focus();
	}
	else
	{
		if(parseFloat(obj.value.trim())<0)
		{
			bootbox.alert("Negetive Amount is not allowed");
			obj.value=0;
			obj.focus();
		}
		else
		{
			obj.value=obj.value.replace("+","");	
			obj.value=obj.value.trim();
			if(isNaN(parseFloat(obj.value)))
			{
				obj.value=0;	
			}
			else
			{
				obj.value = parseFloat(obj.value);
			}
		}
	}
}

function validateDigitsAndDecimal(obj)
{
	if(isNaN(obj.value.trim()))
	{
		bootbox.alert("Invalid Amount")
		obj.value=0;
		obj.focus();
	}
	else
	{
		if(parseFloat(obj.value.trim())<0)
		{
			bootbox.alert("Negetive Amount is not allowed");
			obj.value=0;
			obj.focus();
		}
		else
		{
			obj.value=obj.value.replace("+","");	
			obj.value=obj.value.trim();
			if(isNaN(parseFloat(obj.value)))
			{
				obj.value=0;	
			}
			else
			{
				var resultNum = parseFloat(obj.value);
				obj.value=amountConverter(resultNum);
			}
		}
	}
}
function createSLDropdownFormatterFuncJV(prefix){
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
            selectEl = el.appendChild(selectEl);
	    var selectedIndex = {value: slDetailTableIndex }; 

            YAHOO.util.Event.addListener(selectEl,"change",onSLFuncChange,selectedIndex,this);
			
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
var onSLFuncChange = function(obj,selectedIndex) { 
   
	var filterglcodeArray = new Array();
	var funcObj = document.getElementById('subLedgerlist['+selectedIndex.value+'].functionDetail'); 
	for(var i=0 ; i<funcIdfuncAccCodeArray.length;i++){
		var tokens = funcIdfuncAccCodeArray[i].split("~");
		
		if(funcObj.value == tokens[0]){
			
			if(filterglcodeArray.indexOf(tokens[2]+"~"+tokens[3]) == -1){
				filterglcodeArray.push(tokens[2]+"~"+tokens[3]);
			}
			
		}
	}
	slglcodeObj = document.getElementById('subLedgerlist['+selectedIndex.value+'].glcode.id');
	slglcodeObj.options.length=filterglcodeArray.length+1;
	for(var i=0 ; i<filterglcodeArray.length;i++){
		var tokens = filterglcodeArray[i].split("~");
		slglcodeObj.options[i+1].text=tokens[1];
		slglcodeObj.options[i+1].value=tokens[0];
	}
	
};
if(!Array.indexOf){
  Array.prototype.indexOf = function(obj){
   for(var i=0; i<this.length; i++){
    if(this[i]==obj){
     return i;
    }
   }
   return -1;
  }
}
/*
function check(){
	var accountCodes=new Array();
	
	for(var i=0;i<billDetailTableIndex+1;i++){

	if(null != document.getElementById('billDetailslist['+i+'].glcodeDetail')){
		accountCodes[i] = document.getElementById('billDetailslist['+i+'].glcodeDetail').value;
	}
	}
	var url = path+'/voucher/common!getDetailCode.action?accountCodes='+accountCodes;
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
				d=document.getElementById('subLedgerlist['+j+'].glcode.id');
				d.options.length=((test.length)/2)+1;
				for (var i=1; i<((test.length)/2)+1;i++ )
				{
					d.options[i].text=test[i*2-2];
					d.options[i].value=test[i*2 -1];
					document.getElementById('subLedgerlist['+j+'].subledgerCode').value = test[i*2-2] ;
				}
			} 
			if(test.length<2)
			{
				var d = document.getElementById('subLedgerlist['+j+'].glcode.id');
				if(d)
				{
				d.options.length=1;
				d.options[0].text='---Select---';
				d.options[0].value=0;
				document.getElementById('subLedgerlist['+j+'].subledgerCode').value = "";
				}
			}
		}
    },
    failure: function(o) {
    	bootbox.alert('failure');
    }
}

*/

function autocompleteSchemeBy20()
{
		path="../..";
	     oACDS = new YAHOO.widget.DS_XHR(path+"/voucher/common-ajaxLoadSchemeBy20.action", [ "~^"]);
	    // bootbox.alert("helllpo");
	   oACDS.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
	   oACDS.scriptQueryParam = "startsWith";
	  
	   var oAutoComp1 = new YAHOO.widget.AutoComplete('subScheme.scheme.name','codescontainer',oACDS);
	   oAutoComp1.doBeforeSendQuery = function(sQuery){
		   loadWaitingImage(); 
		   return sQuery+"&fundId="+document.getElementById("fundId").value;
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
	   };


	
}
function autocompleteSubSchemeBy20  ()
{
		path="../..";
	   oACDS = new YAHOO.widget.DS_XHR(path+"/voucher/common-ajaxLoadSubSchemeBy20.action", [ "~^"]);
	   oACDS.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
	   oACDS.scriptQueryParam = "startsWith";
	   var oAutoComp1 = new YAHOO.widget.AutoComplete('subScheme.name','codescontainer',oACDS);
	   oAutoComp1.doBeforeSendQuery = function(sQuery){
		   loadWaitingImage(); 
		   return sQuery+"&schemeId="+document.getElementById("schemeId").value;
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
	   };


	
}

function loadProjectCodes()
{
	var subSchemeId = document.getElementById('subSchemeId').value;
	if(subSchemeId!=null && subSchemeId!="")
	{
	var url = path+'/voucher/common-ajaxLoadProjectCodesForSubScheme.action?subSchemeId='+subSchemeId;
	var transaction = YAHOO.util.Connect.asyncRequest('POST', url, callbackProjectCodes, null);
	}
}

var callbackProjectCodes = {
	success: function(o) {
	document.getElementById('projectCodes').innerHTML=o.responseText;
    },
    failure: function(o) {
    	bootbox.alert('Failed to Load Project Codes');
    }
}
function disableForm() {
	var frmIndex = 0;
		for (var i = 0; i < document.forms[frmIndex].length; i++) {
			for (var i = 0; i < document.forms[0].length; i++) {
				if (document.forms[0].elements[i].name != 'button2')
					document.forms[frmIndex].elements[i].disabled = true;
			}
		}
} 


