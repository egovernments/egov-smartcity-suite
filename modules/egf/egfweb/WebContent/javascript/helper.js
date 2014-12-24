var dom=YAHOO.util.Dom;

function loadWaitingImage() {
	if(document.getElementById('loading')){
		document.getElementById('loading').style.display='block';
	}
}

function clearWaitingImage(){
	if(document.getElementById('loading')){
		document.getElementById('loading').style.display='none';
	}
}

function createTextFieldFormatterWithOnBlur(tableName,prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		table = eval(tableName);
		var value = (YAHOO.lang.isValue(oData))?oData:0.00;
		el.innerHTML = "<input type='text' id='"+prefix+"["+table.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+table.getRecordIndex(oRecord)+"]"+suffix+"' value='"+value+"' style='text-align:right;width:90px;' onBlur='computeAvailable(\""+prefix+"\","+table.getRecordIndex(oRecord)+");' maxlength='15'/>";
	}
}

function computeAvailable(prefix,index){
	available = document.getElementById(prefix+'['+index+'].availableAmount').innerHTML
	delta = document.getElementById(prefix+'['+index+'].deltaAmount').value
	if(available == undefined)
		available = "0.00";
	else if(isNaN(available)){
		alert("Please enter a valid number");
		return;
	}
	if(delta == undefined)
		delta = "0.00";
	else if(isNaN(delta)){
		alert("Please enter a valid number");
		return;
	}
	element = document.getElementById(prefix+'['+index+'].remainingAmount');
	changeRequestElement = document.getElementById(prefix+'['+index+'].changeRequestType');
	if(changeRequestElement == null){
		element.innerHTML = parseInt(available)+parseInt(delta);
	}else{
		if(changeRequestElement.options[changeRequestElement.selectedIndex].value == 'Addition')
			element.innerHTML = (parseInt(available)+parseInt(delta)).toFixed(2);
		else
			element.innerHTML = (parseInt(available)-parseInt(delta)).toFixed(2);
	}
}

function createDeleteImageFormatter(baseURL,delteRowFunction){
	var deleteImageFormatter = function(el, oRecord, oColumn, oData) {
	    var imageURL=baseURL+"/images/cancel.png";
	    markup='<img height="16" border="0" width="16" name="egov_yui_delete_image" id="egov_yui_delete_image" alt="Delete" onclick="'+delteRowFunction+'"  src="'+imageURL+'"/>';
	    el.innerHTML = markup;
	}
	return deleteImageFormatter;
}

function createAddImageFormatter(baseURL,addRowFunction){
	var addImageFormatter = function(el, oRecord, oColumn, oData) {
	    var imageURL=baseURL+"/images/add.png";
	    markup='<img height="16" border="0" width="16" alt="Add" name="egov_yui_add_image" id="egov_yui_add_image" onclick="'+addRowFunction+'" src="'+imageURL+'"/>'
	    el.innerHTML = markup;
	}
	return addImageFormatter;
}

function createDeleteImageFormatterEmpty(baseURL,delteRowFunction){
	var deleteImageFormatter = function(el, oRecord, oColumn, oData) {
	    var imageURL=baseURL+"/images/cancel.png";
	    markup='<img height="16" border="0" width="16" name="egov_yui_delete_image" id="egov_yui_delete_image" alt="Delete"   src="'+imageURL+'" disabled/>';
	    el.innerHTML = markup;
	}
	return deleteImageFormatter;
}

function createAddImageFormatterEmpty(baseURL,addRowFunction){
	var addImageFormatter = function(el, oRecord, oColumn, oData) {
	    var imageURL=baseURL+"/images/add.png";
	    markup='<img height="16" border="0" width="16" alt="Add" name="egov_yui_add_image" id="egov_yui_add_image" src="'+imageURL+'" disabled/>'
	    el.innerHTML = markup;
	}
	return addImageFormatter;
}

var lang=YAHOO.lang;

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
            selectEl.name = prefix+'['+budgetDetailsTable.getRecordIndex(oRecord)+'].'+oColumn.getKey();
			selectEl.id = prefix+'['+budgetDetailsTable.getRecordIndex(oRecord)+'].'+oColumn.getKey();
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

function processGrid(grid,processor,elementPattern){
	len = grid.getRecordSet().getLength()
	count=0;
	j=0;
	while(count < len){
		element=document.getElementById(elementPattern.replace(/\{index\}/,j))
		processor(element,grid);
		if(element){
			count++;
		}
		j++;
	}
}
function copyOptions(src,dest){
  while(dest.options.length>0) dest.options[0]=null
  for(i=0;i<src.options.length;i++){
	dest.options[i]=(new Option(src.options[i].text,src.options[i].value))
  }
}

function getDropdownFormatter(tableName,prefix){
    return function(el, oRecord, oColumn, oData) {
		table = eval(tableName);
        var selectedValue = (lang.isValue(oData)) ? oData : oRecord.getData(oColumn.field),
            options = (lang.isArray(oColumn.dropdownOptions)) ?
                oColumn.dropdownOptions : null,
            selectEl,
            collection = el.getElementsByTagName("select");
        if(collection.length === 0) {
            selectEl = document.createElement("select");
            selectEl.className = YAHOO.widget.DataTable.CLASS_DROPDOWN;
            selectEl.name = prefix+'['+table.getRecordIndex(oRecord)+'].'+oColumn.getKey();
			selectEl.id = prefix+'['+table.getRecordIndex(oRecord)+'].'+oColumn.getKey();
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

function getTextFieldFormatter(tableName,prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:0;
		table = eval(tableName);
		el.innerHTML = "<input type='text' id='"+prefix+"["+table.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+table.getRecordIndex(oRecord)+"]"+suffix+"' value='"+value+"' style='text-align:right;width:90px;' />";
	}
}

function getLabelFormatter(tableName,prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:0;
		table = eval(tableName);
		el.innerHTML = "<label id='"+prefix+"["+table.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+table.getRecordIndex(oRecord)+"]"+suffix+"'  style='text-align:right;width:90px;'>"+value+"</label>";
	}
}

function getAmountFieldFormatter(tableName,values,prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		table = eval(tableName);
	    value = table.getRecordIndex(oRecord)>=values.length?0.0:values[table.getRecordIndex(oRecord)]
		el.innerHTML = "<label id='"+prefix+"["+table.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+table.getRecordIndex(oRecord)+"]"+suffix+"' style='text-align:right'>"+value+"</label>";
	}
}

function updateGridForTable(tableName,field,index,list){
	table = eval(tableName)
	if(table != null){
		len = table.getRecordSet().getLength()
		count=0;
		i=0;
		while(count < len){
			element=document.getElementById(list+'['+i+'].'+field)
			if(element){
				element.selectedIndex = index;
				count++;
			}
			i++;
		}
	}
}
function getReadOnlyTextFieldFormatter(tableName,prefix,suffix){
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:0;
		table = eval(tableName);
		el.innerHTML = "<input type='text' id='"+prefix+"["+table.getRecordIndex(oRecord)+"]"+suffix+"' name='"+prefix+"["+table.getRecordIndex(oRecord)+"]"+suffix+"' value='"+value+"' style='text-align:right;width:100px;' readOnly='true'/>";
	}
}

function textCounter(fieldId,limit) {
    var field = document.getElementById(fieldId);
	var maxlimit = limit?limit:100;
    if(field.value.length > maxlimit)
		field.value = field.value.substring(0, maxlimit);
}
function ltrim(str) {
  return str.replace(/^\s+/g, '');
}

function rtrim(str) {
  return str.replace(/\s+$/g, '');
}

function trimStr(str) {
  return str.replace(/^\s*|\s(?=\s)|\s*$/g, '');
}


function disableYUIAddDeleteButtons(value)
{
	var i=0;
	var allAddImages=document.getElementsByName('egov_yui_add_image');
	if(allAddImages)
	{
		for (i=0;i<allAddImages.length;i++)
		{
			allAddImages[i].disabled=value;
		}
	}
	else if(document.getElementById('egov_yui_add_image'))	
	{
		document.getElementById('egov_yui_add_image').disabled=value;
	}
	
	var allDeleteImages=document.getElementsByName('egov_yui_delete_image');
	if(allDeleteImages)
	{
		for (i=0;i<allDeleteImages.length;i++)
		{
			allDeleteImages[i].disabled=value;
		}
	}
	else if(document.getElementById('egov_yui_delete_image'))
	{
		document.getElementById('egov_yui_delete_image').disabled=value;
	}

}


//Document Upload Starts
function showDocumentManager(){
    return docManager(dom.get("docNumber").value);
}

function docManager(docNumber){
    var url;
    if(docNumber==null||docNumber==''||docNumber=='To be assigned'||docNumber==0){
      url="/egi/docmgmt/basicDocumentManager.action?moduleName=EGF";
    }else{
      url = "/egi/docmgmt/basicDocumentManager!editDocument.action?docNumber="+docNumber+"&moduleName=EGF";
    }
    var wdth = 1000;
    var hght = 400;
    window.open(url,'docupload','width='+wdth+',height='+hght);
}

function viewDocumentManager(){
   var v= dom.get("docNumber").value;
   var url= "/egi/docmgmt/basicDocumentManager!viewDocument.action?docNumber="+v+"&moduleName=EGF";
   var wdth = 1000;
    var hght = 400;
    window.open(url,'docupload','width='+wdth+',height='+hght);
}

function viewDocumentManager(docNumber){
   var url= "/egi/docmgmt/basicDocumentManager!viewDocument.action?docNumber="+docNumber+"&moduleName=EGF";
   var wdth = 1000;
   var hght = 400;
   window.open(url,'docupload','width='+wdth+',height='+hght);
}

function setupDocNumberBeforeSave(){
	   var v= dom.get("docNumber").value;
       if(v=='To be assigned'){
          dom.get("docNumber").value='';
       }
}
// Document Upload Ends

function createDocUploadFormatter(tableName,prefix,suffix){
	var deleteImageFormatter = function(el, oRecord, oColumn, oData) {
		table = eval(tableName);
		markup='<input type="submit" class="buttonsubmit" value="Upload Document" id="'+prefix+"["+table.getRecordIndex(oRecord)+"]"+suffix+'" onclick="showDocumentManager(this);return false;" />';
	    el.innerHTML = markup;
	}
	return deleteImageFormatter;
}
