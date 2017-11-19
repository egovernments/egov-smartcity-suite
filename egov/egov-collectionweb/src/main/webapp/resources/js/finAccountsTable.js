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
var lang=YAHOO.lang;
var accountTableIndex = 0;
var subledgerTableIndex = 0;
var ACCOUNTDETAILSLIST = 'accountDetails';
var SUBLEDGERLIST = 'subledgerDetails';
var codeObj;
var acccodeArray;
var allGlcodes={}
var detailTypeId=0;
var codeTextMessage='Enter 3 letters';
var entities;
var firstthreechars;
var savedthreechars;
var oAutoCompEntity;
var yuiflag2 = new Array();
function updateGridAccounts(field,index,value){
	
	document.getElementById('accountDetails'+'['+index+'].'+field).value=value;
}

function updateAccountTableIndex(){
	
	accountTableIndex = accountTableIndex +1 ;
}

function updateSLTableIndex(){
	
	 subledgerTableIndex = subledgerTableIndex +1 ;
}

function createTextFieldFormatterForFunction(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text'  id='"+prefix+"["+accountTableIndex+"]"+suffix+"' name='"+prefix+"["+accountTableIndex+"]"+suffix+"' style='width:90px;' onkeyup='autocompletecodeFunction(this,event)' autocomplete='off' onblur='fillNeibrAfterSplitFunction(this)'/>";
		
	}
		
}

function createTextFieldFormatter(prefix,suffix,type){
	
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = " <input type='"+type+"' id='"+prefix+"["+accountTableIndex+"]"+suffix+"' name='"+prefix+"["+accountTableIndex+"]"+suffix+"' style='width:90px;' readOnly/>";
		
	}
}

function createLongTextFieldFormatter(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+accountTableIndex+"]"+suffix+"' name='"+prefix+"["+accountTableIndex+"]"+suffix+"' onfocus='autocompletecode(this,event)' onblur='fillNeibrAfterSplitGlcode(this)' style='width:250px;' tabindex='-1'/>";
	}
}

function createAmountFieldFormatter(prefix,suffix,onblurfunction){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+accountTableIndex+"]"+suffix+"' name='"+prefix+"["+accountTableIndex+"]"+suffix+"' value='0' style='text-align:right;width:90px;' maxlength='18' onblur='validateDigitsAndDecimal(this);"+onblurfunction+"'/>";
	}
}

function createSLTextFieldFormatter(prefix,suffix,onblurfunction){
    return function(el, oRecord, oColumn, oData) {
		el.innerHTML = "<input type='text' id='"+prefix+"["+subledgerTableIndex+"]"+suffix+"' name='"+prefix+"["+subledgerTableIndex+"]"+suffix+"' style='width:90px;' onblur='"+onblurfunction+"'/>";
	}
}

function createSLDetailCodeTextFieldFormatter(prefix,suffix,onblurfunction){
	 return function(el, oRecord, oColumn, oData) {
				el.innerHTML = "<input type='text' id='"+prefix+"["+subledgerTableIndex+"]"+suffix+"' name='"+prefix+"["+subledgerTableIndex+"]"+suffix+"' style='width:90px;' onkeyup='autocompleteForEntity(this,event)' autocomplete='off' onblur = 'waterMarkTextOut(\""+prefix+"["+subledgerTableIndex+"]"+suffix+"\",\""+codeTextMessage+"\");"+onblurfunction+"' onfocus='onFocusDetailCode(this);waterMarkTextIn(\""+prefix+"["+subledgerTableIndex+"]"+suffix+"\",\""+codeTextMessage+"\");' />";
				
			}
	}
function createSLHiddenFieldFormatter(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		el.innerHTML = "<input type='text' id='"+prefix+"["+subledgerTableIndex+"]"+suffix+"' name='"+prefix+"["+subledgerTableIndex+"]"+suffix+"'/>";
	}
}

function createSLLongTextFieldFormatter(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+subledgerTableIndex+"]"+suffix+"' name='"+prefix+"["+subledgerTableIndex+"]"+suffix+"' readOnly style='width:120px;'/>";
	}
}


function createSLAmountFieldFormatter(prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = "<input type='text' id='"+prefix+"["+subledgerTableIndex+"]"+suffix+"' name='"+prefix+"["+subledgerTableIndex+"]"+suffix+"' value='0' onblur='validateDigitsAndDecimal(this);' maxlength='18' style='text-align:right;width:90px;'/>";
	}
}

function updateSLGrid(field,index,value){
	document.getElementById(SUBLEDGERLIST+'['+index+'].'+field).value=value;
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
	//Fix-Me
	var accCodeObj = document.getElementById('accountDetails[0].glCodeId.glcode');
	jQuery(accCodeObj).trigger('focus');
	jQuery(obj).trigger('focus');
	
	
	var src = obj;	
	var target = document.getElementById('codescontainer');	
	
	var posSrc=findPos(src); 
	target.style.left=posSrc[0]+"px";	
	target.style.top=posSrc[1]-40+"px";
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
				oAutoComp.formatResult = function(oResultData, sQuery, sResultMatch) {
					var data = oResultData.toString();
				    return data.split("`~`")[0];
				};
			}
		}
		//yuiflagFunc[currRow] = 1;
	//}	
}
function fillNeibrAfterSplitFunction(obj)
{
	var temp = obj.value;
	temp = temp.split("`~`");
	var currRow=getRowIndex(obj);
	if(temp.length>1)
	{ 
		obj.value=temp[0];
		document.getElementById('accountDetails['+currRow+'].function.id').value=temp[1];
	}else if(temp == ''){
		obj.value='';
		document.getElementById('accountDetails['+currRow+'].function.name').value='';
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


var yuiflag = new Array();
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

function fillNeibrAfterSplitGlcode(obj)
{
	var key = obj.value;
	var temp = obj.value;
	temp = temp.split("`-`");
	if(temp.length>1)
	{ 
		obj.value=temp[0];
		var currRow=getRowIndex(obj);
		document.getElementById('accountDetails['+currRow+'].glCodeId.id').value=allGlcodes[key];
		document.getElementById('accountDetails['+currRow+'].glCodeId.name').value=temp[1].split("`~`")[0];
		document.getElementById('accountDetails['+currRow+'].glCodeId.glcode').value=temp[0];
		var flag=false;
		for (var i=0; i<subledgerTableIndex;i++ )
		{
			for(var j=0; j<accountTableIndex;j++){
				if(null != document.getElementById(SUBLEDGERLIST+'['+i+'].serviceAccountDetail.glCodeId.id')){
					var subledgerSel = document.getElementById(SUBLEDGERLIST+'['+i+'].serviceAccountDetail.glCodeId.id').value;
					
				}
				if(null != document.getElementById(ACCOUNTDETAILSLIST+'['+j+'].glCodeId.id')){
					var accDetailSel = document.getElementById(ACCOUNTDETAILSLIST+'['+j+'].glCodeId.id').value;
				}
				
				if(subledgerSel == accDetailSel){
					
					flag = true;break;
				}
				
			}
			if(!flag){
				
					document.getElementById(SUBLEDGERLIST+'['+i+'].serviceAccountDetail.glCodeId.id').value=0;
					document.getElementById(SUBLEDGERLIST+'['+i+'].detailType.id').value=0;
					document.getElementById(SUBLEDGERLIST+'['+i+'].detailType.name').value="";
					document.getElementById(SUBLEDGERLIST+'['+i+'].detailCode').value="";
					document.getElementById(SUBLEDGERLIST+'['+i+'].detailKeyId').value="";
					document.getElementById(SUBLEDGERLIST+'['+i+'].detailKey').value="";
					document.getElementById(SUBLEDGERLIST+'['+i+'].amount').value="0";
			
				
			}
			
		}
		
		loadSLAccountCode();
	}

}


function createDropdownFormatter(prefix){
    return function(el, oRecord, oColumn, oData) {
        var selectedValue = (lang.isValue(oData)) ? oData : oRecord.getData(oColumn.field),
            options = (lang.isArray(oColumn.dropdownOptions)) ?
                oColumn.dropdownOptions : null,
            selectEl,
            collection = el.getElementsByTagName("select");
        if(collection.length === 0) {
            selectEl = document.createElement("select");
            selectEl.className = YAHOO.widget.DataTable.CLASS_DROPDOWN;
            selectEl.name = prefix+'['+subledgerTableIndex+'].'+oColumn.getKey();
			selectEl.id = prefix+'['+subledgerTableIndex+'].'+oColumn.getKey();
			//selectEl.onfocus=check;
            selectEl = el.appendChild(selectEl);
	    var selectedIndex = {value: subledgerTableIndex }; 

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

function createDropdownFormatter1(prefix){
    return function(el, oRecord, oColumn, oData) {
        var selectedValue = (lang.isValue(oData)) ? oData : oRecord.getData(oColumn.field),
            options = (lang.isArray(oColumn.dropdownOptions)) ?
                oColumn.dropdownOptions : null,
            selectEl,
            collection = el.getElementsByTagName("select");
        if(collection.length === 0) {
            selectEl = document.createElement("select");
            selectEl.className = YAHOO.widget.DataTable.CLASS_DROPDOWN;
            selectEl.name = prefix+'['+subledgerTableIndex+'].'+oColumn.getKey();
			selectEl.id = prefix+'['+subledgerTableIndex+'].'+oColumn.getKey();
            selectEl = el.appendChild(selectEl);
		var selectedIndex = {value: subledgerTableIndex }; 
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


var onDropdownChange = function(index,obj) { 
		
		var subledgerid=document.getElementById(SUBLEDGERLIST+'['+obj.value+'].serviceAccountDetail.glCodeId.id');
		var accountCode = subledgerid.options[subledgerid.selectedIndex].text;
		document.getElementById(SUBLEDGERLIST+'['+obj.value+'].serviceAccountDetail.glCodeId.glcode').value =accountCode;
		if(accountCode != '---Select---'){
			var url = path+'/receipts/ajaxReceiptCreate-getDetailTypeForService.action?accountCode='+accountCode+'&index='+obj.value;
			var transaction = YAHOO.util.Connect.asyncRequest('POST', url, postType, null);
		}else{
				var d = document.getElementById(SUBLEDGERLIST+'['+obj.value+'].detailType.id');
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
				obj = document.getElementById(SUBLEDGERLIST+'['+parseInt(eachItem[0])+']'+'.detailType.id');
				if(obj!=null)
					obj.options.length=detailRecord.length+1;
			}
			if(obj!=null)
			{
				obj.options[i+1].text=eachItem[1];
				obj.options[i+1].value=eachItem[2];
				document.getElementById(SUBLEDGERLIST+'['+parseInt(eachItem[0])+']'+'.detailType.name').value = eachItem[1];
			}
			
			if(eachItem.length==1) // for deselect the subledger code
			{
				var d = document.getElementById(SUBLEDGERLIST+'['+i+'].detailType.id');
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
function loadSLAccountCode(){
	var accountCodes=new Array();
	for(var i=0;i<accountTableIndex+1;i++){
	if(null != document.getElementById('accountDetails['+i+'].glCodeId.glcode')){
		accountCodes[i] = document.getElementById('accountDetails['+i+'].glCodeId.glcode').value;
	}
	}
	var url =  path+'/receipts/ajaxReceiptCreate-getDetailCode.action?accountCodes='+accountCodes;
	var transaction = YAHOO.util.Connect.asyncRequest('POST', url, callbackSLAccCode, null);
}
var callbackSLAccCode = {
success: function(o) {
		var test= o.responseText;
		test = test.split('~');
		for (var j=0; j<subledgerTableIndex;j++ )
		{
			
			if(null != document.getElementById(SUBLEDGERLIST+'['+j+'].serviceAccountDetail.glCodeId.id')&& test.length >1 )
			{
				d=document.getElementById(SUBLEDGERLIST+'['+j+'].serviceAccountDetail.glCodeId.id');
				d.options.length=((test.length)/2)+1;
				for (var i=1; i<((test.length)/2)+1;i++ )
				{
					d.options[i].text=test[i*2-2];
					d.options[i].value=test[i*2 -1];
					
				}
			} 
			if(test.length<2)
			{
				var d = document.getElementById(SUBLEDGERLIST+'['+j+'].serviceAccountDetail.glCodeId.id');
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



var onDropdownDetailTypeChange = function(index,obj) { 
	var detailtypeidObj=document.getElementById(SUBLEDGERLIST+'['+obj.value+'].detailType.id');
	if(detailTypeId != detailtypeidObj.value){ // checks if the subledgercodes already loaded for that detail type
		detailTypeId = detailtypeidObj.value;
		
	}
	
};

function onFocusDetailCode(obj){
	var currRow=getRowIndex(obj);
	var detailtypeidObj=document.getElementById(SUBLEDGERLIST+'['+currRow+'].detailType.id');
	if(detailTypeId != detailtypeidObj.value){
		detailTypeId = detailtypeidObj.value;
		//loadDropDownCodesForEntities(detailtypeidObj); 
	}
}


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
			var detailtypeidObj=document.getElementById(SUBLEDGERLIST+'['+currRow+'].detailType.id');
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
	
	if(entity.trim()!="")
	{
		
		var entity_array=entity.split("`-`");
		
		if(entity_array.length==3)
		{
			document.getElementById(SUBLEDGERLIST+'['+currRow+']'+'.detailCode').value=entity_array[0];
			document.getElementById(SUBLEDGERLIST+'['+currRow+']'+'.detailKeyId').value=entity_array[2];
			document.getElementById(SUBLEDGERLIST+'['+currRow+']'+'.detailKey').value=entity_array[1];
		}
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


function amountConverter(amt) {
	var formattedAmt = amt.toFixed(2);
	return formattedAmt;
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
function getRowIndex(obj)
{
	var temp =obj.name.split('[');
	var temp1 = temp[1].split(']');
	return temp1[0];
}

function onElementFocused(e)
{
    return document.activeElement ==e?true:false;
       
} 
String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
}
