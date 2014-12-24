var dom=YAHOO.util.Dom;
var yuiflag=new Array();

// THESE METHODS FOR FORMATTING THE DATATABLE OF NOMINEE MASTER

function createNomineeTextBoxFormatter(size,maxlength)
{
			
 			var textboxFormatter = function(el, oRecord, oColumn, oData) {
   			var value = (YAHOO.lang.isValue(oData))?oData:"";
    		var id=oColumn.getKey()+oRecord.getId();
    		var fieldName="egpimsNomineeMaster["+oRecord.getCount()+"]."+ oColumn.getKey();
    		markup="<input type='text' class='selectwk' id='"+id+"' name='"+fieldName+"'  value='"+value+"' size='"+size+"'onkeyup='checkAlphaNumeric(this,\""+oColumn.getKey()+"\");checkAge(this,\""+oColumn.getKey()+"\")';/><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
   			el.innerHTML = markup;
  		}
		return textboxFormatter;
}

function createNomineeTextBoxForBankAutoComplete(size,maxlength)
{
			
 			var textboxFormatter = function(el, oRecord, oColumn, oData) {
   			var value = (YAHOO.lang.isValue(oData))?oData:"";
    		var id=oColumn.getKey()+oRecord.getId();
    		var fieldName="egpimsNomineeMaster["+oRecord.getCount()+"].bankBranch."+ oColumn.getKey()+".name";
    		markup="<input type='text'  id='"+id+"' name='"+fieldName+"' size='"+size+"'  value='"+value+"'onkeyup='bankautocompletecode(this,event)' autocomplete='off' onblur='fillNeibrAfterSplitForBank(this); '/>";
   			el.innerHTML = markup;
  		}
		return textboxFormatter;
}

function createNomineeTextBoxForForBranchAutoComplete(size,maxlength)
{
			
 			var textboxFormatter = function(el, oRecord, oColumn, oData) {
   			var value = (YAHOO.lang.isValue(oData))?oData:"";
    		var id=oColumn.getKey()+oRecord.getId();
    		var fieldName="egpimsNomineeMaster["+oRecord.getCount()+"].bankBranch."+ oColumn.getKey();
    		markup="<input type='text'  id='"+id+"' name='"+fieldName+"' size='"+size+"'  value='"+value+"'onkeyup='branchautocomplete(this,event)' autocomplete='off' onblur='fillNeibrAfterSplitForBranch(this); '/>";
   			el.innerHTML = markup;
  		}
		return textboxFormatter;
}

function createNomineeTextBoxFormatterNameValidate(size,maxlength)
{
			
 			var textboxFormatter = function(el, oRecord, oColumn, oData) {
   			var value = (YAHOO.lang.isValue(oData))?oData:"";
    		var id=oColumn.getKey()+oRecord.getId();
    		var fieldName="egpimsNomineeMaster["+oRecord.getCount()+"]."+ oColumn.getKey();
    		markup="<input type='text' class='selectwk' id='"+id+"' name='"+fieldName+"'  value='"+value+"' size='"+size+"' class='dataList' onblur='validateUniqueName(this);'/><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
   			el.innerHTML = markup;
  		}
		return textboxFormatter;
}

function createNomineeTextBoxForDateFormatter(size,maxlength)
{
			
 			var textboxFormatter = function(el, oRecord, oColumn, oData) {
   			var value = (YAHOO.lang.isValue(oData))?oData:"";
    		var id=oColumn.getKey()+oRecord.getId();
    		var fieldName="egpimsNomineeMaster["+oRecord.getCount()+"]."+ oColumn.getKey();
    		markup="<input type='text' class='selectwk' id='"+id+"' name='"+fieldName+"'  value='"+value+"' size='"+size+"' onblur='validateDateFormat(this),populateAge(this,\""+oRecord.getId()+"\");' onfocus='javascript:vDateType=3' onkeyup='DateFormat(this,this.value,event,false,3)' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
   			el.innerHTML = markup;
  		}
		return textboxFormatter;
}

function populateAge(obj,obj1)
{
  var today = new Date(); 
  var dob="";
  if(obj.value!="")
  {
  	 dob=obj.value;
  	 dob = dob.split("/");
     var nowyear = today.getFullYear();
  	 var byr = parseInt(dob[2]); 
  	 var age=nowyear-byr;
  	 if(age<0)
  	 {
  		alert("Age cannot be less than 0");
  		obj.value="";
  		obj.focus();
  	 }	
  	 else
  	 {	 
	  	  var recordAge = 'nomineeAge' + obj1;
	  	  var record = nomineeDataTable.getRecordSet().getRecord(obj1);
	  	  nomineeDataTable.updateCell(record,nomineeDataTable.getColumn('nomineeAge'),age);
  	 } 	  
  }
  
}

function checkAge(obj,column)
{
	if(column=='nomineeAge')
	{
		var currRow=getRowIndex(obj);
		var i,dob,byr,age;
		dob = document.getElementsByName('egpimsNomineeMaster['+currRow+'].nomineeDob')[0].value;
		var today = new Date(); 
		var nowyear = today.getFullYear();
		dob=dob.split("/");
		byr = parseInt(dob[2]);
		age=nowyear-byr;
		document.getElementsByName('egpimsNomineeMaster['+currRow+'].nomineeAge')[0].value=parseInt(age);
	}
}


function createAddImageFormatter(baseURL){
	var addImageFormatter = function(el, oRecord, oColumn, oData) {
	    var imageURL=baseURL+"/common/image/add.png";
	    markup='<img height="16" border="0" width="14" alt="Add" src="'+imageURL+'"/>';
	    el.innerHTML = markup;
		}
		return addImageFormatter;
	}
	
	function createDeleteImageFormatter(baseURL)
	{
		var deleteImageFormatter = function(el, oRecord, oColumn, oData) {
	    var imageURL=baseURL+"/common/image/cancel.png";
	    markup='<img height="14" border="0" width="14" alt="Delete" src="'+imageURL+'"/>';
	    el.innerHTML = markup;
		}
		return deleteImageFormatter;
	}
	
	function addAllOptions(selectbox,text,value)
{
	var optn = document.createElement("OPTION");
	optn.text = text;
	optn.value = value;
	selectbox.options.add(optn);
	return;
}

function removeAllOptions(selectbox)
{
	for(var i=selectbox.options.length-1;i>0;i--)
	{
		selectbox.remove(i);
	}
	return;
}

var lang=YAHOO.lang;
function createDropdownFormatter(listObj,postfixFieldValue){
     return function(el, oRecord, oColumn, oData) {
        var selectedValue = (lang.isValue(oData)) ? oData : oRecord.getData(oColumn.field),
            options = (lang.isArray(oColumn.dropdownOptions)) ?
                oColumn.dropdownOptions : null,
            selectEl,
            collection = el.getElementsByTagName("select");
        if(collection.length === 0) {
            selectEl = document.createElement("select");
            selectEl.className = YAHOO.widget.DataTable.CLASS_DROPDOWN;
            if(postfixFieldValue!="")
            	selectEl.name = listObj+'['+oRecord.getCount()+'].'+oColumn.getKey()+postfixFieldValue;
            else
            	selectEl.name = listObj+'['+oRecord.getCount()+'].'+oColumn.getKey();
			selectEl.id = oColumn.getKey()+oRecord.getId();
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


var selectBankName;
var selectBankBranchName;
var yuiflag = new Array();
var yuiflag1 = new Array();
var oAutoCompName;
var bankId;

function loadBank()
{
	var url = "/eis/common/employeeSearch!getBankList.action";
	var req = initiateRequest();
	req.open("GET", url, false);
	req.send(null);
    if (req.status == 200)
    {
  	    var codes=req.responseText;
  	    var a = codes.split("^");
		var codes = a[0];
		codesArray=codes.split("+");
		selectBankName = new YAHOO.widget.DS_JSArray(codesArray);
    }
}

function loadBankBranch(bankId)
{
	var url = "/eis/common/employeeSearch!getBranchByBankId.action?branchId="+bankId;
	var req = initiateRequest();
	req.open("GET", url, false);
	req.send(null);
    if (req.status == 200)
    {
  	    var codes=req.responseText;
  	    var a = codes.split("^");
		var codes = a[0];
		codesArray=codes.split("+");
		selectBranchName = new YAHOO.widget.DS_JSArray(codesArray);
    }
}

function branchautocomplete(obj,event)
{
	var id = "autocom"+obj.id;
    var target = document.getElementById('codescontainer');
    var autocomp = document.createElement("div");
    autocomp.setAttribute("id", id);
    target.appendChild(autocomp);
	// set position of dropdown
	var src = obj;
	var posSrc=findPos(src);
	target.style.left=posSrc[0];
	target.style.top=posSrc[1]+25;
	target.style.width=300;
	var coaCodeObj = obj;
	
	var key = window.event ? window.event.keyCode : event.charCode;  
		//40 --> Down arrow, 38 --> Up arrow
		if(event.keyCode != 40 )
		{
			if(event.keyCode != 38 )
			{
				oAutoCompName = new YAHOO.widget.AutoComplete(coaCodeObj,id, selectBranchName);
				oAutoCompName.queryDelay = 0;
				oAutoCompName.prehighlightClassName = "yui-ac-prehighlight";
				oAutoCompName.useShadow = true;
				oAutoCompName.maxResultsDisplayed = 30;
				oAutoCompName.minQueryLength = 0;
				oAutoCompName.useIFrame = true;
			}
		}
}


function fillNeibrAfterSplitForBank(obj)
{
	var currRow=getRow(obj);
	var temp = obj.value;
	temp = temp.split('`~`');
	if(temp[1]!='' && temp[1]!=undefined)
	{
		loadBankBranch(temp[1]);
	}
	obj.value=temp[0];
	
}


function fillNeibrAfterSplitForBranch(obj)
{
	var currRow=getRowIndex(obj);
	var record = nomineeDataTable.getRecordSet().getRecord(obj);
	var temp = obj.value;
	temp = temp.split('`~`');
	if(temp[1]!='' && temp[1]!=undefined)
	{
		if(temp.length>1)
		{ 
			obj.value=temp[0];
			document.getElementsByName('egpimsNomineeMaster['+currRow+'].bankBranch.id')[0].value=temp[1];
			
		}else if(temp == ''){
			obj.value='';
			document.getElementsByName('egpimsNomineeMaster['+currRow+'].bankBranch.id')[0].value='';
		}
	}	
	
	
}


function bankautocompletecode(obj,event)
{
	
	var id = "autocom"+obj.id;
    var target = document.getElementById('codescontainer');
    var autocomp = document.createElement("div");
    autocomp.setAttribute("id", id);
    target.appendChild(autocomp);
	// set position of dropdown
	var src = obj;
	var posSrc=findPos(src);
	target.style.left=posSrc[0];
	target.style.top=posSrc[1]+25;
	target.style.width=300;
	var coaCodeObj = obj;
	var key = window.event ? window.event.keyCode : event.charCode;  
		//40 --> Down arrow, 38 --> Up arrow
		if(key != 40 )
		{
			if(key != 38 )
			{
				oAutoCompName = new YAHOO.widget.AutoComplete(coaCodeObj,id, selectBankName);
				oAutoCompName.queryDelay = 0;
				oAutoCompName.prehighlightClassName = "yui-ac-prehighlight";
				oAutoCompName.useShadow = true;
				oAutoCompName.maxResultsDisplayed = 30;
				oAutoCompName.minQueryLength = 0;
			}
		}
}



function getRow(obj)
{
if(!obj)return null;
tag = obj.nodeName.toUpperCase();
while(tag != 'BODY'){
if (tag == 'TR') return obj;
obj=obj.parentNode;
tag = obj.nodeName.toUpperCase();
}
return null;
}


function findPos(obj)
{
	var curleft = curtop = 0;
	if (obj.offsetParent)
	{
		curleft = obj.offsetLeft;
		curtop = obj.offsetTop;
		while (obj = obj.offsetParent)
		{
			curleft =curleft + obj.offsetLeft;
			curtop =curtop + obj.offsetTop; 
		}
	}
	return [curleft,curtop];
}

function getRowIndex(obj)
{
	var temp =obj.name.split('[');
	var temp1 = temp[1].split(']');
	return temp1[0];
}

function checkAlphaNumeric(obj,column)
{
	if(column=='nomineeAddress' && obj.value!="")
	{
		var num=obj.value;
		var objRegExp  = /^[a-zA-Z0-9- () _ , #]+$/;
		if(!objRegExp.test(num))
		{
			alert('Special characters are not allowed');
			obj.focus();
		}
	}
	if(column=='accountNumber' && obj.value!="")
	{
		var num=obj.value;
		var objRegExp = /^[a-zA-Z0-9]+$/;
		if(!objRegExp.test(num))
		{
			alert('Special characters are not allowed');
			obj.focus();
		}
	}
}	