var dom=YAHOO.util.Dom;

function roundTo(value,decimals,decimal_padding){
  if(!decimals) decimals=2;
  if(!decimal_padding) decimal_padding='0';
  if(isNaN(value)) value=0;
  value=Math.round(value*Math.pow(10,decimals));
  var stringValue= (value/Math.pow(10,decimals)).toString();
  var padding=0;
  var parts=stringValue.split(".")
  if(parts.length==1) {
  	padding=decimals;
  	stringValue+=".";
}
  else padding=decimals-parts[1].length
  for(i=0;i<padding;i++) stringValue+=decimal_padding
  return stringValue
}
function validateDecimalForOnePlace(val,errorId){
  if(validateNumberForDecimal(val))
 {
   var amount= val.value;
   var n=amount.split('.');
   if(n.length>1){
     if(n[1].length>1)
     {
       dom.get(errorId).style.display='';      
       dom.get(errorId).innerHTML='More than One decimal places are not allowed';
       return false;
      }
   }
      dom.get(errorId).style.display='none';
      dom.get(errorId).innerHTML='';	
 }
}

function createDeleteImageFormatter(baseURL){
	var deleteImageFormatter = function(el, oRecord, oColumn, oData) {
	    var imageURL=baseURL+"/image/cancel.png";
	    var id=oColumn.getKey()+oRecord.getId(); 
	    markup='<img height="16" border="0" width="16" id="'+id+'" alt="Delete" src="'+imageURL+'"/>';
	    el.innerHTML = markup;
	}
	return deleteImageFormatter;
}

function createAddImageFormatter(baseURL){
	var addImageFormatter = function(el, oRecord, oColumn, oData) {
	    var imageURL=baseURL+"/image/add.png";
	    markup='<img height="16" border="0" width="16" alt="Add" src="'+imageURL+'"/>'
	    el.innerHTML = markup;
	}
	return addImageFormatter;
}

function createSearchImageFormatter(baseURL){
	var searchImageFormatter = function(el, oRecord, oColumn, oData) {
	    var imageURL=baseURL+"/image/magnifier.png";
	    markup='<img height="16" border="0" width="16" alt="Search" src="'+imageURL+'"/>'
	    el.innerHTML = markup;
	}
	return searchImageFormatter;
}

function viewImageFormatter(baseURL){
	var searchImageFormatter = function(el, oRecord, oColumn, oData) {
	    var imageURL=baseURL+"/image/book_open.png";
	    markup='<img height="16" border="0" width="16" alt="Search" src="'+imageURL+'"/>'
	    el.innerHTML = markup;
	}
	return searchImageFormatter;
}


function createPageEditFormatter(baseURL){
	var searchImageFormatter = function(el, oRecord, oColumn, oData) {
	    var imageURL=baseURL+"/image/page_edit.png";
		markup='<img height="16" border="0" width="16" alt="Search" src="'+imageURL+'"/>'
	    el.innerHTML = markup;
	}
	return searchImageFormatter;
}


function makeJSONCall(fields,url,params,onSuccess,onFailure){
 dataSource=new YAHOO.util.DataSource(url);
 
            dataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
            dataSource.connXhrMode = "queueRequests";
            dataSource.responseSchema = {
                resultsList: "ResultSet.Result",
                fields: fields
            };
	        var callbackObj = {
            success : onSuccess,
            failure : onFailure
        };
        dataSource.sendRequest("?"+toQuery(params),callbackObj);
}

function toQuery(params){
   var query="";
   for(var f in params){
     query+=f+"="+params[f]+"&"
   }
   if(query.lastIndexOf('&')==query.length-1) query=query.substring(0,query.length-1);
   return query;
}

function getNumericValueFromInnerHTML(id){
    value=dom.get(id).innerHTML;
    return getNumber(value);
}

function getNumber(value){
    return isNaN(value)?0.0:parseFloat(value);
}

function createTextFieldFormatter(size, maxlength, columnName,onBlur){
    var textboxFormatter = function(el, oRecord, oColumn, oData) {
                            var value = (YAHOO.lang.isValue(oData))?oData:"";
                            var id=oColumn.getKey()+oRecord.getId();
                            var fieldName=oColumn.getKey()+'_'+oRecord.getData(columnName)
                            var recordId=oRecord.getId();
                            markupTemplate="<input type='text' id='@id@' name='@fieldName@' size='@size@' maxlength='@maxlength@' class='selectamountwk' @onblur@ /><span id='error@id@' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
                            var markup=markupTemplate.replace(/@id@/g,id).
                                   replace(/@fieldName@/g,fieldName).
                                   replace(/@size@/g,size).
                                   replace(/@maxlength@/g,maxlength);

                            var onblurAttrib=''
                             if(onBlur){
                              onblurAttrib="onblur='"+onBlur+"(this,\""+recordId+"\");'";
                             }
                             markup= markup.replace(/@onblur@/g,onblurAttrib);                                   

                             el.innerHTML = markup;
                            }
    return textboxFormatter;
}
function validateNumberInTableCell(table,elem,recordId){
     record=table.getRecord(recordId);
      dom.get('error'+elem.id).style.display='none';
      if(isNaN(elem.value) || getNumber(elem.value)<0){
      	dom.get('error'+elem.id).style.display='';
      	return false;
      }
      return true;
}

function addCell(tr,index,divId,initialValue){
	var cell = tr.insertCell(index);
	cell.setAttribute('className','selectamountwk whitebox4wknoalign');
	cell.setAttribute('class','selectamountwk whitebox4wknoalign');
	cell.innerHTML = '<div class="yui-dt-liner" id="'+divId+'">'+initialValue+'</div>';
}

function addCell(tr,index,divId,initialValue,colSpan){
	var cell = tr.insertCell(index);
	cell.setAttribute('className','selectamountwk whitebox4wknoalign');
	cell.setAttribute('class','selectamountwk whitebox4wknoalign');
	cell.setAttribute('colspan',colSpan);
	cell.innerHTML = '<div class="yui-dt-liner" id="'+divId+'">'+initialValue+'</div>';
}

function addCellRightAligned(tr,index,divId,initialValue){
	var cell = tr.insertCell(index);
	cell.setAttribute('className','selectamountwk whitebox4wknoalign');
	cell.setAttribute('class','selectamountwk whitebox4wknoalign');
	cell.innerHTML = '<div class="yui-dt-liner" style="text-align: right" id="'+divId+'">'+initialValue+'</div>';
}

function showMessage(id,msg){
    dom.get(id).style.display='';
    dom.get(id).innerHTML=msg;
}
function clearMessage(id){
    dom.get(id).style.display='none';
    dom.get(id).innerHTML='';
}


function toggle(div_id){
	var el = document.getElementById(div_id);
	if (el.style.display == 'none' ){
		el.style.display = 'block';
	}
	else{
		el.style.display = 'none';
	}
}

function blanket_size(popUpDivVar){
	if (typeof window.innerWidth != 'undefined'){
		viewportheight = window.innerHeight;
	} else {
		viewportheight = document.documentElement.clientHeight;
	}
	if ((viewportheight > document.body.parentNode.scrollHeight) && (viewportheight > document.body.parentNode.clientHeight)) {
		blanket_height = viewportheight;
	} else {
		if (document.body.parentNode.clientHeight > document.body.parentNode.scrollHeight) {
			blanket_height = document.body.parentNode.clientHeight;
		} else {
			blanket_height = document.body.parentNode.scrollHeight;
		}
	}
	var blanket = document.getElementById('blanket');
	blanket.style.height = blanket_height + 'px';
	var popUpDiv = document.getElementById(popUpDivVar);
	popUpDiv_height=blanket_height/2-150;//150 is half popup's height
	popUpDiv.style.top = popUpDiv_height + 'px';
}

function window_pos(popUpDivVar) {
	if (typeof window.innerWidth != 'undefined') {
		viewportwidth = window.innerHeight;
	} else {
		viewportwidth = document.documentElement.clientHeight;
	}
	if ((viewportwidth > document.body.parentNode.scrollWidth) && (viewportwidth > document.body.parentNode.clientWidth)) {
		window_width = viewportwidth;
	} else {
		if (document.body.parentNode.clientWidth > document.body.parentNode.scrollWidth) {
			window_width = document.body.parentNode.clientWidth;
		} else {
			window_width = document.body.parentNode.scrollWidth;
		}
	}
	var popUpDiv = document.getElementById(popUpDivVar);
	window_width=window_width/2-150;//150 is half popup's width
	popUpDiv.style.left = window_width + 'px';
}

function popup(windowname) {
	blanket_size(windowname);
	window_pos(windowname);
	toggle('blanket');
	toggle(windowname);		
}


/**
* Check for the valid characters in the date fields 
**/
function checkDate(obj)
{

var validChars="0123456789/";
var dt=obj.value;
var len= 0;
var invalid=false;

if(obj.readOnly==false)
{
if(dt!="" && dt!=null)
{
	len= dt.length;
	for(var i=0;i<len && invalid==false;i++)
	{
		chars=dt.charAt(i);
		
		if(validChars.indexOf(chars)==-1)
		{			
			invalid=true;
		}
	}
	
	if(invalid==true)
	{
		alert("Please enter the valid characters");
		obj.value="";
		obj.focus();
	}
}
}
return;
}

/**
* Check whether the values entered in the date fields are in dd/MM/yyyy format 
* and whether the dd, MM and yyyy values are valid
**/
function validateDateFormat(obj)
{
 var dtStr=obj.value;
 var year;
 var day;
 var month;
 var leap=0;
 var valid=true;
 var oth_valid=true;
 var feb=false;
 var validDate=true;
 var Ret=true;

 dom.get('error'+obj.id).style.display='none';
 
  if(obj.readOnly==false)
  {
    if(dtStr!="" && dtStr!=null)
    { 
    	year=dtStr.substr(6,4);
    	month=dtStr.substr(3,2);
    	day=dtStr.substr(0,2);
    	if(dtStr.indexOf("/")=="2" && dtStr.lastIndexOf("/")=="5")    	
    	validDate=true;    	
    	else    	
    	validDate=false;
    	    	
    	//alert("day="+day+" month="+month);
    	if(year=="0000" || year<1900 || month=="00" || day=="00" || dtStr.length!=10)
    	{
    		validDate=false;
    	}
    		
    	if(validDate==true)
    	{
    		leap=year%4;
 		
 		 if(month=="02")
		 {
		 		feb=true;
		 }

 		
 		if(leap==0 && month=="02")
   		{
   			//alert("111111111111111111111");
   			if(day>29)
   			{
   				valid=false;
   				feb=true;
   			}
   		}
    	
   		else if(month=="02" && day>28)
   		{    
   			valid=false;
   			feb=true;
   		}
    		
   		if(feb==false)
   		{    	
   			if(month=="03" || month=="01" || month=="05" || month=="07" || month=="08" || month=="10" || month=="12"){
 				if(day>31){
 					oth_valid=false;
 				}
   			}
   		
   			else if(month=="04" || month==06 || month=="09" || month=="11") {
   				if(day>30){
   					oth_valid=false;
   				}
   			}
   		
   			else{
   				oth_valid=false;
   			}
   		}
    }
    }	
   
    if(valid==false || oth_valid==false || validDate==false)
    {
    	obj.focus();
    	Ret=false;
		dom.get('error'+obj.id).style.display='';
    }
    return Ret;
   }
}
/**
* Check whether the values entered in the date fields are in dd/MM/yyyy format 
* and whether the dd, MM and yyyy values are valid
* added with out error div id, it just return true or false;
**/
function checkDateFormat(obj)
{
 var dtStr=obj.value;
 var year;
 var day;
 var month;
 var leap=0;
 var valid=true;
 var oth_valid=true;
 var feb=false;
 var validDate=true;
 var Ret=true;
 
 
  if(obj.readOnly==false)
  {
    if(dtStr!="" && dtStr!=null)
    {
    	year=dtStr.substr(6,4);
    	month=dtStr.substr(3,2);
    	day=dtStr.substr(0,2);
    	if(dtStr.indexOf("/")=="2" && dtStr.lastIndexOf("/")=="5")    	
    	validDate=true;    	
    	else    	
    	validDate=false;
    	    	
    	//alert("day="+day+" month="+month);
    	if(year=="0000" || year<1900 || month=="00" || day=="00" || dtStr.length!=10)
    	{
    		validDate=false;
    	}
    		
    	if(validDate==true)
    	{
    		leap=year%4;
 		
 		 if(month=="02")
		 {
		 		feb=true;
		 }

 		
 		if(leap==0 && month=="02")
   		{
   			//alert("111111111111111111111");
   			if(day>29)
   			{
   				valid=false;
   				feb=true;
   			}
   		}
    	
   		else if(month=="02" && day>28)
   		{    
   			valid=false;
   			feb=true;
   		}
    		
   		if(feb==false)
   		{    	
   			if(month=="03" || month=="01" || month=="05" || month=="07" || month=="08" || month=="10" || month=="12"){
 				if(day>31){
 					oth_valid=false;
 				}
   			}
   		
   			else if(month=="04" || month==06 || month=="09" || month=="11") {
   				if(day>30){
   					oth_valid=false;
   				}
   			}
   		
   			else{
   				oth_valid=false;
   			}
   		}
    }
    }	
   
    if(valid==false || oth_valid==false || validDate==false)
    {
    	obj.focus();
    	Ret=false;
	}
    return Ret;
   }
}
function validateNumberInTableCell(table,elem,recordId){
     record=table.getRecord(recordId);
      dom.get('error'+elem.id).style.display='none';
      if(isNaN(elem.value) || getNumber(elem.value)<0){
      	dom.get('error'+elem.id).style.display='';
      	return false;
      }
      return true;
}

function validateNumber(elem){
      dom.get('error'+elem.id).style.display='none';
      if(isNaN(elem.value) || getNumber(elem.value)<0){
      	dom.get('error'+elem.id).style.display='';
      	return false;
      }
      return true;
}
function validateNumberForDecimal(elem){
      dom.get('error'+elem.id).style.display='none';
      if(isNaN(elem.value) || getNumber(elem.value)<0 || elem.value.indexOf('+')!=-1){
      	dom.get('error'+elem.id).style.display='';
      	return false;
      }
      return true;
}
function validateNegativeNumber(elem){
      dom.get('error'+elem.id).style.display='none';
      if(isNaN(elem.value)){
      	dom.get('error'+elem.id).style.display='';
      	return false;
      }
      return true;
}


function validateDecimal(elem){
	var reg = /^[0-9]+(\.[0-9]+)?$/;
	var val=elem.value;
	var char=val.charAt(val.length-1)
	var validate=true;
	//dont validate for first occurance of '.'
	if(char=='.'){
    		var n=val.split('.');
    		if(n.length<3){
        		validate=false;
		}
	}
	if(validate && !reg.test(val)){
    	elem.value=elem.value.substring(0, elem.value.length - 1)
	}
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
            	selectEl.name = listObj+'['+oRecord.getCount()+'].'+oColumn.getKey()+'.'+postfixFieldValue;
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
function enabledivChilderns(divId)
{
	tagElements=document.getElementById(divId);
	var childtags = tagElements.childNodes;
	for(i=0;i<childtags.length;i++){
		if(childtags[i].id!=undefined){
			childtags[i].disabled=false;
		}
	}
}

function enableElements(elementNames)
{
	len=elementNames.length;
	for(i=0;i<len;i++){
		dom.get(elementNames[i]).disabled=false;
	}
}
function hideElements(elementNames)
{
	len=elementNames.length;
	for(i=0;i<len;i++){
		dom.get(elementNames[i]).style.display='none';
	}
}
function showElements(elementNames)
{
	len=elementNames.length;
	for(i=0;i<len;i++){
		dom.get(elementNames[i]).style.display='';
	}
}
function toggleFields(flag,excludedFields)
{
	for(var i=0;i<document.forms[0].length;i++) {
		document.forms[0].elements[i].disabled =flag;
	}
	for(var j=0;j<excludedFields.length;j++) {
		if(dom.get(excludedFields[j])!=null)
			dom.get(excludedFields[j]).disabled =false;
   }
}
function toggleForSelectedFields(flag,selectedFields)
{
   for(var j=0;j<selectedFields.length;j++) {
		dom.get(selectedFields[j]).disabled =flag;
   }
}
function disableLinks(links,excludedLinks)
{
	for(i=0;i<links.length;i++){
		if(excludedLinks.length>0){
			for(j=0;j<excludedLinks.length;j++){
				if(links[i].id.indexOf("header_")!=0 && excludedLinks[j]!=links[i].id)
				{
					links[i].onclick=function(){return false;};
				}
				
			}
		}
		else{
			links[i].onclick=function(){return false;};
		}
	}
}
function getCurrentDate(){
	var thetime=new Date();
	var month = eval(thetime.getMonth()+1);
	var day =  thetime.getDate();
	if(day.toString().length==1)
	    day =  "0"+thetime.getDate();
	if(month.toString().length==1)
	    month = "0"+month;
	var date = day + "/" + month + "/" + thetime.getFullYear();	
	return date;
}
function getRequestParamsInURL(name)
{
  name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
  var regexS = "[\\?&]"+name+"=([^&#]*)";
  var regex = new RegExp( regexS );
  var results = regex.exec( window.location.href );
  if( results == null )
    return "";
  else
    return results[1];
}

function validateNumberForDecimalPlace(val,errorId){
	 if(validateNumberForDecimal(val)){
	   var amount= val.value;
	   var n=amount.split('.');
	   if(n.length>1){
		 if(n[1].length>=0){
		   dom.get(errorId).style.display='';      
	       return false;
	      }
	   }
	   dom.get(errorId).style.display='none';
	 }
	 return true;
}

var Drag = {  

		obj : null,

		init : function(o, oRoot, minX, maxX, minY, maxY, bSwapHorzRef, bSwapVertRef, fXMapper, fYMapper)
		{
			o.onmousedown	= Drag.start;

			o.hmode			= bSwapHorzRef ? false : true ;
			o.vmode			= bSwapVertRef ? false : true ;

			o.root = oRoot && oRoot != null ? oRoot : o ;

			if (o.hmode  && isNaN(parseInt(o.root.style.left  ))) o.root.style.left   = "0px";
			if (o.vmode  && isNaN(parseInt(o.root.style.top   ))) o.root.style.top    = "0px";
			if (!o.hmode && isNaN(parseInt(o.root.style.right ))) o.root.style.right  = "0px";
			if (!o.vmode && isNaN(parseInt(o.root.style.bottom))) o.root.style.bottom = "0px";

			o.minX	= typeof minX != 'undefined' ? minX : null;
			o.minY	= typeof minY != 'undefined' ? minY : null;
			o.maxX	= typeof maxX != 'undefined' ? maxX : null;
			o.maxY	= typeof maxY != 'undefined' ? maxY : null;

			o.xMapper = fXMapper ? fXMapper : null;
			o.yMapper = fYMapper ? fYMapper : null;

			o.root.onDragStart	= new Function();
			o.root.onDragEnd	= new Function();
			o.root.onDrag		= new Function();
		},

		start : function(e)
		{
			var o = Drag.obj = this;
			e = Drag.fixE(e);
			var y = parseInt(o.vmode ? o.root.style.top  : o.root.style.bottom);
			var x = parseInt(o.hmode ? o.root.style.left : o.root.style.right );
			o.root.onDragStart(x, y);

			o.lastMouseX	= e.clientX;
			o.lastMouseY	= e.clientY;

			if (o.hmode) {
				if (o.minX != null)	o.minMouseX	= e.clientX - x + o.minX;
				if (o.maxX != null)	o.maxMouseX	= o.minMouseX + o.maxX - o.minX;
			} else {
				if (o.minX != null) o.maxMouseX = -o.minX + e.clientX + x;
				if (o.maxX != null) o.minMouseX = -o.maxX + e.clientX + x;
			}

			if (o.vmode) {
				if (o.minY != null)	o.minMouseY	= e.clientY - y + o.minY;
				if (o.maxY != null)	o.maxMouseY	= o.minMouseY + o.maxY - o.minY;
			} else {
				if (o.minY != null) o.maxMouseY = -o.minY + e.clientY + y;
				if (o.maxY != null) o.minMouseY = -o.maxY + e.clientY + y;
			}

			document.onmousemove	= Drag.drag;
			document.onmouseup		= Drag.end;

			return false;
		},

		drag : function(e)
		{
			e = Drag.fixE(e);
			var o = Drag.obj;

			var ey	= e.clientY;
			var ex	= e.clientX;
			var y = parseInt(o.vmode ? o.root.style.top  : o.root.style.bottom);
			var x = parseInt(o.hmode ? o.root.style.left : o.root.style.right );
			var nx, ny;

			if (o.minX != null) ex = o.hmode ? Math.max(ex, o.minMouseX) : Math.min(ex, o.maxMouseX);
			if (o.maxX != null) ex = o.hmode ? Math.min(ex, o.maxMouseX) : Math.max(ex, o.minMouseX);
			if (o.minY != null) ey = o.vmode ? Math.max(ey, o.minMouseY) : Math.min(ey, o.maxMouseY);
			if (o.maxY != null) ey = o.vmode ? Math.min(ey, o.maxMouseY) : Math.max(ey, o.minMouseY);

			nx = x + ((ex - o.lastMouseX) * (o.hmode ? 1 : -1));
			ny = y + ((ey - o.lastMouseY) * (o.vmode ? 1 : -1));

			if (o.xMapper)		nx = o.xMapper(y)
			else if (o.yMapper)	ny = o.yMapper(x)

			Drag.obj.root.style[o.hmode ? "left" : "right"] = nx + "px";
			Drag.obj.root.style[o.vmode ? "top" : "bottom"] = ny + "px";
			Drag.obj.lastMouseX	= ex;
			Drag.obj.lastMouseY	= ey;

			Drag.obj.root.onDrag(nx, ny);
			return false;
		},

		end : function()
		{
			document.onmousemove = null;
			document.onmouseup   = null;
			Drag.obj.root.onDragEnd(	parseInt(Drag.obj.root.style[Drag.obj.hmode ? "left" : "right"]), 
										parseInt(Drag.obj.root.style[Drag.obj.vmode ? "top" : "bottom"]));
			Drag.obj = null;
		},

		fixE : function(e)
		{
			if (typeof e == 'undefined') e = window.event;
			if (typeof e.layerX == 'undefined') e.layerX = e.offsetX;
			if (typeof e.layerY == 'undefined') e.layerY = e.offsetY;
			return e;
		}
	};

function preparedByDesignation(elem){
	var temp=elem.options[elem.selectedIndex].innerHTML; 
	var result=temp.split('-');
	if(result.length>1 && result[1]!="" && result[1]!=null && result[1]!=undefined)
		return result[1];
	else
		return "";
}


function checkUptoFourDecimalPlace(obj,errorId,labelname)
{
	if (dom.get(errorId) != null) 
		dom.get(errorId).style.display = 'none'; 
	if(obj.value!=""){
			if (isNaN(obj.value)) { 
				obj.value = "";
				dom.get(errorId).style.display='';      
			    dom.get(errorId).innerHTML=labelname+' should be Numeric';
			return false;
		}
	
		else if (obj.value < 0) { 
			obj.value = "";
			dom.get(errorId).style.display='';      
		    dom.get(errorId).innerHTML=labelname+' can not be Negative';
			return false;
		}
		
		else if(trimAll(obj.value)!="" && String(obj.value).indexOf(".") !=-1 && (String(obj.value).indexOf(".") < String(obj.value).length - 6)) {
			obj.value="";
		 	dom.get(errorId).style.display='';      
		    dom.get(errorId).innerHTML=labelname+' can only be 5 decimal places at most.';
			return false;
		}
	} 
	return true;
}

function showWaiting() {
	document.getElementById('loading').style.display = 'block'; 
}
function getRowIndexWithRowName(name)
{
    var temp = name.split("[");
    var rowIndex = temp[1].split("]")[0];
	return rowIndex;
}