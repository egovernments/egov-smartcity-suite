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


function createDeleteImageFormatter(baseURL){
	var deleteImageFormatter = function(el, oRecord, oColumn, oData) {
	    var imageURL=baseURL+"/image/cancel.png";
	    markup='<img height="16" border="0" width="16" alt="Delete" src="'+imageURL+'"/>';
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

function showMessage(id,msg){
    dom.get(id).style.display='';
    dom.get(id).innerHTML=msg;
}
function clearMessage(id){
    dom.get(id).style.display='none';
    dom.get(id).innerHTML='';
}
function getControlInBranch(obj,controlName)
{
	if (!obj || !(obj.getAttribute)) return null;
	// check if the object itself has the name
	if (obj.getAttribute('id') == controlName) return obj;

	// try its children
	var children = obj.childNodes;
	var child;
	if (children && children.length > 0){
		for(var i=0; i<children.length; i++){
			child=this.getControlInBranch(children[i],controlName);
			if(child) return child;
		}
	}
	return null;
}

// this is to get the current row
function getRow(obj)    
{
 if(!obj)return null;
 tag = obj.nodeName.toUpperCase();
 while(tag != 'BODY'){
  if (tag == 'TR') return obj;
  obj=obj.parentNode ;
  tag = obj.nodeName.toUpperCase();
 }
 return null;
}

function validateDigitsAndDecimal(obj)
{
	if(isNaN(obj.value.trim()))
	{
		alert("Invalid Amount")
		obj.value=0;
		obj.focus();
	}
	else
	{
		if(parseFloat(obj.value.trim())<0)
		{
			alert("Negetive Amount is not allowed");
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
function amountConverter(amt) {
	var formattedAmt = amt.toFixed(2);
	return formattedAmt;
}