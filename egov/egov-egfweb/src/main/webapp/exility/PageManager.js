/*#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------*/
//<script language=javascript>
if (typeof(window.exilWindow) == 'undefined')window.exilWindow = window;
function PageManager(){}

//adding services with PageManager.
PageManager.DataService = new DataService();
PageManager.ListService = new ListService();
PageManager.UpdateService=new UpdateService();
PageManager.DescService= new DescService();
PageManager.TreeService= new TreeService();
PageManager.Helper = new Object();

PageManager.Helper.objectIsInGrid = function(obj){
	if (null == obj || !obj.tagName) return false;
	if (obj.getAttribute('exilInGrid')) return true;
	var tagName = obj.tagName.toUpperCase();
	if (tagName == "BODY") return false;
	if (tagName == "TABLE" && obj.getAttribute("exilDataSource")) return true;
	return (PageManager.Helper.objectIsInGrid(obj.parentNode));
}

PageManager.Helper.getDescFields = function(obj){
	var att = obj.getAttribute("exilDescField");
	var fields = att.split(",");
	var exilInGrid = PageManager.Helper.objectIsInGrid(obj);
	for( var i=0; i< fields.length; i++){
		if (exilInGrid)fields[i] = PageManager.DataService.getNaybur(obj, fields[i]);
		else fields[i] = exilWindow.document.getElementById(fields[i]);
	}
	return fields;
}

PageManager.Helper.dispatchService = function (src){
	if (typeof(exilParms) == 'undefined' || exilParms.verbose) {  
		bootbox.alert('Going to call Exility with ' + src );
	}
	var ifrdiv=exilWindow.document.createElement('div');
	ifrdiv.innerHTML='<iframe style="width:0px;height:0px" src="' +src +'">	</iframe>';
	exilWindow.document.body.appendChild(ifrdiv);
}
                            /*******Code for Data Service**********/
function DataService(){}

//Method Declarations for Data Service
DataService.prototype.callDataService =DataService_callDataService;
DataService.prototype.refreshPage =DataService_refreshPage;
DataService.prototype.changeNodeValue =DataService_changeNodeValue;
DataService.prototype.hideAndSeek =DataService_hideAndSeek;
DataService.prototype.selectListValue =DataService_selectListValue;
DataService.prototype.fillList =DataService_fillList;
DataService.prototype.appendList =DataService_appendList;

//Method Declarations to handle Grid(DataService)
DataService.prototype.addDataToGrid=dataService_addDataToGrid;
DataService.prototype.addRows=dataService_addRows;
DataService.prototype.fillRow=dataService_fillRow;
DataService.prototype.getControlInBranch=dataService_getControlInBranch;
DataService.prototype.addNewRow=dataService_addNewRow;
DataService.prototype.getRowKey=dataService_getRowKey;
DataService.prototype.getRow=dataService_getRow;
DataService.prototype.getRowUsingKey=dataService_getRowUsingKey;	
DataService.prototype.resetMustEnter=dataService_resetMustEnter;
DataService.prototype.resetValues =dataService_resetValues;
DataService.prototype.getQueryField =dataService_getQueryField;
DataService.prototype.setQueryField =dataService_setQueryField;
DataService.prototype.loadQueryFields =dataService_loadQueryFields;
DataService.prototype.dcToAlert =dataService_dcToAlert;
DataService.prototype.getQueryString =dataService_getQueryString;
DataService.prototype.getNaybur =dataService_getNaybur;
DataService.prototype.getControlInRow =dataService_getControlInRow;
DataService.prototype.removeListOptions =dataService_removeListOptions;
DataService.prototype.removeQueryField =dataService_removeQueryField;

//Method to raise a data request to server
// service ID is either the id for the common service on the server, or the URL of the service

function DataService_callDataService(serviceID){
	// get the URL with query string to be used for the request
	var src;
	if (exilWindow.location.host){//it is running from a server. request service from the host
		if( typeof(exilParms)!= 'undefined' && exilParms.dataServiceName){ //This is defined in ExilityParameters.js
			this.setQueryField('serviceID',serviceID);
			src = exilParms.dataServiceName;
		}
		else src = serviceID; // in this case, service id itself is the URL
	}else{ // this is a local file.. look for data in a local file
		src = '../data/' +serviceID +'.htm';
	}
	src = src + this.getQueryString();
	PageManager.Helper.dispatchService(src);
}

/* Refresh the page with the data being received
 * This function is triggered as from the document that is loaded in iframe
 * Data Collection has four types of data
 */
function DataService_refreshPage(dc, isListService){
	if (typeof isListService == 'undefined' || isListService == null || isListService == false){ //it is data service
		if (typeof exilWindow.beforeRefreshPage != 'undefined'){ // user wants to do something before	
			if (exilWindow.beforeRefreshPage(dc) == false) return;
		}
		if(typeof exilWindow.exilOriginalData == 'undefined' ||exilWindow.exilOriginalData == null ){
			exilWindow.exilOriginalData  = dc; //saved for any reset()
		}
	}

	if (typeof(exilParms) == 'undefined' || exilParms.verbose) {  
		var str = this.dcTobootbox.alert(dc);
		alert (str);
	}

// Code to Handle the values
    this.fillList(dc.grids);
    var obj, val;
	for(var a in dc.values){ //a is the name, and val would be its value
		val = dc.values[a]; 
		//try a node with 'a' as id
		obj = exilWindow.document.getElementById(a);
		if(obj)this.changeNodeValue(obj,val);
		else this.hideAndSeek(a,val.toUpperCase());// see if this is used to hide/show elements
	}	//loop for elements in values
	//take care of entries in grids
	for(var a in dc.grids){  
		var obj = exilWindow.document.getElementById(a);
		if(obj && obj.nodeName.toUpperCase()=='TABLE')this.addDataToGrid(a, dc.grids[a]); 
	}
	// and any messages
    PageManager.UpdateService.fillMessage(dc.messages);

	if (!isListService){ //it is data service
		if (typeof exilWindow.afterRefreshPage != 'undefined'){ // user wants to do something after refresh()	
			exilWindow.afterRefreshPage(dc);
		}
	}

}
/* check the type of node and change its value acordingly */
function DataService_changeNodeValue(obj,val){
	if(!obj) return false; //playing it safe
	//exilTrimLength indicates the max length of the string ,if it exceeds that string should be shown in trimmed format
	var exilTrimLength=parseInt(obj.getAttribute("exilTrimLength"));
	if(!exilTrimLength || exilTrimLength<0 )exilTrimLength=9999;
	switch(obj.nodeName.toUpperCase()){
	case "TEXTAREA": obj.innerHTML=val;break;
	case "SPAN":obj.innerHTML=val;break;
	case "INPUT":
		switch(obj.type){
		case "checkbox":
			if(val == obj.value || val == exilParms.checkedValue || val.toUpperCase == "TRUE")obj.checked=true;
 			else obj.checked=false;
			break;
		case "radio":
			var eleName = obj.name;
			var ele;
			var eles = exilWindow.document.forms[0].elements;
			for(var i=0;  i< eles.length; i++){
				ele = eles[i];
				if (ele.name == eleName ){
					if(ele.value == val) ele.checked = true;
					else ele.checked = false;
				}
			}
			break;
		default :
			obj.value=val;
			break;
		}
		break;

	case "OPTION":			 
		obj=obj.parentNode;	
		//continue to next case to 
	case "SELECT": //drop down box
		this.selectListValue(obj,val);
		break;	   
	case "A":
	    obj.href = val;
		break; 

	default:		 
		if(typeof obj.value == 'undefined'){
			if(exilTrimLength<val.length){
				obj.title=val;
				val=val.substring(0,exilTrimLength-3) + '...';
			}else{
				obj.removeAttribute('title');
			}
			 obj.innerHTML = val.replace('&', '&amp;');
		}
		else obj.value = val;
		break; 
	}
	return true;
}

//show/hide an element	, if found, with the id name_True or name_False
function DataService_hideAndSeek(name, val){

	var trueObj = exilWindow.document.getElementById(name+'_True');
	var falseObj = exilWindow.document.getElementById(name+'_False');

	if (trueObj || falseObj){ 
		if( val=='TRUE'){
		// should display 'true' element and hide false element
			if(trueObj) {
				trueObj.style.display='block';
				this.resetMustEnter(trueObj, 'true');//enable fields if they were disabled ealier
			}
			if(falseObj){
				falseObj.style.display='none';
				this.resetMustEnter(falseObj, 'false'); 
			}
		}else{ //the other way around
			if(falseObj){
				falseObj.style.display='block';
				this.resetMustEnter(falseObj, 'true');
			}
			if(trueObj){
				trueObj.style.display='none';
				this.resetMustEnter(trueObj, 'false');
			}
		}
	}
}

//to select one option in the list 
function DataService_selectListValue(obj,val){

	for(var i=0; i<obj.options.length; i++){
		if(obj.options[i].value==val){ 
			obj.options[i].setAttribute('selected',true);
			obj.selectedIndex = i;
			//obj.value = obj.options[i];
			return true;
		}
	}// value not found. add it  
	var option = exilWindow.document.createElement('option');
	obj.appendChild(option);
	option.innerHTML = new String(val).replace("&", "&amp;");
	option.value =val;
	option.selected =true;
	obj.selectedIndex = obj.options.length - 1;
}

//To find the lists in form and fill with the grid if available
function DataService_fillList(grids){
	var frm = exilWindow.document.forms[0];
	var obj, att;
	for(var i=0; i<frm.length; i++){
		obj = frm.elements[i];
		if(obj.nodeName.toUpperCase()=='SELECT'){
			att = obj.getAttribute("exilListSource"); 
			if(att && grids[att])this.appendList(obj,grids[att]);
		}		
	}
}

//Method to append the List  after deleting existing list
function DataService_appendList(obj,list){
	var oldval; //existing selection
	var option, mustEnter, codeAndDesc;
	var ind = obj.selectedIndex;
	if (ind >=0 ) oldval = obj.options[ind].value;
	
	for(var i=obj.childNodes.length-1; i>=0; i--) obj.removeChild(obj.childNodes[i]);
	var selectedIndex = -1;
	mustEnter = obj.getAttribute('exilMustEnter');
	if (mustEnter ==null || mustEnter.toUpperCase() != 'TRUE'){ //this is optional, we should add a blank option
		option = exilWindow.document.createElement('option');   
		option.value = '';
		obj.appendChild(option);
		option.setAttribute('selected', true);
		obj.selectedIndex = 0;
	}
	codeAndDesc = obj.getAttribute('exilCodeAndDesc');
	if (codeAndDesc != null && codeAndDesc.toUpperCase() == "TRUE") codeAndDesc = true;
	else codeAndDesc = false;
	for(var i=0; i<list.length; i++){  
		option = exilWindow.document.createElement('option');
		option.innerHTML = (codeAndDesc ? (list[i][0] + ' - ' + list[i][1]) : list[i][1]).replace("&","&nbsp;");
		option.value = list[i][0];
		obj.appendChild(option);
		if(oldval && oldval == list[i][0]){
			selectedIndex = i;
			option.setAttribute('selected', true);
		}else{
			option.setAttribute('selected', false);
		}
	}
	if (mustEnter && mustEnter.toUpperCase() == 'TRUE'){ //nothing should be selected
		if (oldval != null && obj.options[obj.selectedIndex].value == oldval); //ok. that is the right selection.
		else obj.selectedIndex = -1;
	}
}    

/********* Functions Handle Grid  **********************/

//fill the table with the data in the grid
function dataService_addDataToGrid(tblId,grid){
	// does this table exist?
	var tbl = exilWindow.document.getElementById(tblId);
	if (tbl && tbl.tagName.toUpperCase() == "TABLE"); //we are ok to continue 
	else return false;
	var nbrdatarows = grid.length-1;
	var rowToStart = 1; //first row of grid is col names. second row is optionalally header labels
  	//if runtimeHeader attribute is set, we have to use the first row as header
	var runtimeHeader=tbl.getAttribute("exilRuntimeHeader");
   	if(runtimeHeader){
		//second row in grid contains values for header row
		nbrdatarows = grid.length-2;
		rowToStart = 2;
		this.fillRow(tbl.rows[0], grid[0], grid[1]);
	}
	//Add/remove rows to tbl to match the grid size
	if (!this.addRows(tbl,nbrdatarows)) return false; //unable to add rows
	
	//fill the grid with data array passed
	var rowKey=tbl.getAttribute("exilRowKey");
	var j = 1; //this is the index for rows
	
	for (var i=rowToStart; i<grid.length; i++){ // for each data grid row
		if(rowKey)tbl.rows[j].setAttribute("exilRowKey",grid[i][0]);
		this.fillRow(tbl.rows[j],grid[0],grid[i]);
		j++;
	}
	
}

// ensure that the table tbl has len rows (other than the header)
function dataService_addRows(tbl, len){

	var tbody=tbl.tBodies[0];
	var rows=tbl.rows;
	var existingRows=rows.length -1; //header is excluded
	
	var rowsToClone ; //
	rowsToClone = tbl.getAttribute("rowsToClone");
	if (!rowsToClone){ // not yet copied..
		if (existingRows < 1){
			alert ("Table " + tbl.id + " should have at least one row for Exility to load data");
			return false;
		}
		rowsToClone = new Array();
		rowsToClone[0] = rows[1].cloneNode(true);
		if (existingRows > 1)rowsToClone[1] = rows[2].cloneNode(true);
		else rowsToClone[1] = rows[1].cloneNode(true);
		tbl.setAttribute("rowsToClone", rowsToClone);
	}	

	if (existingRows == len)return true; //we have the right nbr of rows.

	if (existingRows > len){ // we have problem of plenty !! remove some rows
		for (var i=existingRows; i>len; i--)tbl.deleteRow(i);
		return true;
	}
	//add more rows
	var rowstoAdd = len -existingRows;
	var rowToClone=0;
	var newNode;
	for(var i=existingRows; i<len; i++){
		if(i%2==0 || existingRows < 2) rowToClone=0;//For alternate cloning of records
		else rowToClone=1;
		newNode=rowsToClone[rowToClone].cloneNode(true);
		tbody.appendChild(newNode);		  
	}
	return true;
}

//fill a row of a table with the name/value pairs in names and data.
// setRowKey=true implies that the first data is to be set as the key of the row
function dataService_fillRow(row,names,data){ 

	var control;
	//to save first column data as exilRowKey for all columns of that row.
	for(var i=0; i<names.length; i++){
		control = this.getControlInBranch(row, names[i]);
		if (control)this.changeNodeValue(control,data[i]);
	}	
}

// returns a DOM element within the TR for the name
function dataService_getControlInBranch(obj,controlName){
	
	if (!obj || !(obj.getAttribute)) return null;
	// check if the object itself has the name
	if (obj.getAttribute('name') == controlName) return obj;

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
function dataService_getControlInRow(tableName, dataRowNumber, controlName){
	// dataRowNumbr would start from 1, but excude header row.
	//Only one header row is assumed
	
	var tbl = exilWindow.document.getElementById(tableName);
	if (!tbl){
		alert (' No table with name ' + tableName  + ' exists');
		return null;
	}
	if (tbl.rows.length <= dataRowNumber){
		alert (' Table has only ' + (tbl.rows.length - 1) +' data rows. Can not get row number ' + dataRowNumber);
		return null;
	}

	return getControlInBranch(tbl.rows[dataRowNumber], controlname);
}

// find a neighbor in the same TR
function dataService_getNaybur(obj,nameOfNaybur){
	
	var row = this.getRow(obj);
	if(row) return this.getControlInBranch(row,nameOfNaybur);
	return null;
}
   
//Returns the row key of the row based on any object in the row.
function dataService_getRowKey(obj){
	if(!obj)return '';
	while(obj && obj.nodeName && obj.nodeName.toUpperCase()!='TR')  obj=obj.parentNode;
	if (obj)key=obj.getAttribute('exilRowKey');  
	if(!key)key='';
	return key;
}

// returns an HTML element with name=targetName within the same row as that of obj
function dataService_getRow(obj){
	if(!obj)return null;
	tag = obj.nodeName.toUpperCase();
	while(tag != 'BODY'){
		if (tag == 'TR') return obj;
		obj=obj.parentNode;
		tag = obj.nodeName.toUpperCase();
	}
	return null;
}

// searches for an object with targetName in the row identified by rowKey in table
function dataService_getRowUsingKey(tableId,rowKey){
	var table=exilWindow.document.getElementById(tableId);
	if (!table) return null;
	for(var i=1; i<table.rows.length; i++){
		if (rowKey == table.rows[i].getAttribute('exilRowKey')) return table.rows[i];
	}
	return null;
}

//clear any must-enter constraints within the branch
function dataService_resetMustEnter(obj, mustEnter){
	var att = null;
	if(obj && obj.getAttribute){
		att = obj.getAttribute("exilMustEnter");
		if (att != null && att != "")obj.setAttribute("exilMustEnter",mustEnter);
		else for(var i=0; i<obj.childNodes.length; i++)this.resetMustEnter(obj.childNodes[i],mustEnter);
	}
}

//add a new row with the contents of the previous row
function dataService_addNewRow(tableId){
	var table = exilWindow.document.getElementById(tableId);
	if(!table || !table.rows) return false;
	//last record number
	var len = table.rows.length;
	this.addRows(table, len); // len is the actual length. addRow assumes it to be datarows.
}

//To Clear values in an object
function dataService_resetValues(obj){
	if (!obj) return;
	if (typeof obj.checked != 'undefined')obj.checked = false; //check box or radio
	else if (typeof obj.selectedIndex != 'undefined' ){ //option
		//if (obj.options) obj.options[0].selected = true;
		obj.selectedIndex = -1;
	}else if (obj.value) obj.value="";
	else if (obj.childNodes){
		for(var i=0; i<obj.childNodes.length; i++) this.resetValues(obj.childNodes[i]);
	}
}
// when this page is called, caller might have passed a query string in the form
// thisPage.htm?field1=value1&field2=value2 etc..
// if you want use them in any of your code, here is the method to get it
function dataService_getQueryField(name){
	if (typeof exilWindow.exilQueryFields == 'undefined' ) this.loadQueryFields();
	if (exilWindow.exilQueryFields[name] == 'undefined') return "";
	return exilWindow.exilQueryFields[name];
}

function dataService_setQueryField(name, value){
	if (typeof exilWindow.exilQueryFields == 'undefined' ) this.loadQueryFields();
	exilWindow.exilQueryFields[name] = value;
}

function dataService_removeQueryField(name){
	if (typeof exilWindow.exilQueryFields == 'undefined' ) this.loadQueryFields();
	else	exilWindow.exilQueryFields[name] = null;
	
}

//this function should not be called by the programmers. it is called only once
// to split the query string into a neat object. This object will be used by get/set
// methods as well as other service calls to the server 
function dataService_loadQueryFields(){
	if (typeof exilWindow.exilQueryFields != 'undefined' ) return; // it is already built..
	exilWindow.exilQueryFields = new Object();
	var search = new String(unescape(exilWindow.location.search));
	search = search.substring(1);
	var params = search.split("&");

	for (var i=0; i<params.length; i++){
		var vals = params[i].split("=");
		if (vals.length != 2) continue; // invalid parameter
		exilWindow.exilQueryFields[vals[0]] = vals[1];
	}
}

function dataService_getQueryString(){
	if (typeof exilWindow.exilQueryFields == 'undefined' ) return exilWindow.location.search;
	var str = "";
	var prefix = "?";
	for (var a in exilWindow.exilQueryFields){
		var val = exilWindow.exilQueryFields[a];
		if (val == null) continue;
		str += prefix + a + "=" +val;
		prefix='&';
	}
	return str;
	//if (typeof encodeURI == 'undefined') return "?" + escape(str);
	//return "?" + encodeURIComponent(str);
}


function dataService_dcTobootbox.alert(dc){  
	var str = "";
	str += "There are " + dc.messages.length + " Messages";
	for (var i=0; i<dc.messages.length; i++) str+= "\n" + dc.messages[i];
	str += "\nValues are :";
	for (var a in dc.values) str+= "\n" + a + "=" + dc.values[a];
	str += "\nGrids are:";
	for (var a in dc.grids) {
		str+= "\n" + a + " has " + dc.grids[a].length + " rows and " + dc.grids[a][0].length + " columns";
		for(var i=0; i<dc.grids[a].length; i++) str += "\n" + dc.grids[a][i];
	}

	return str;
}


function dataService_removeListOptions(obj){
	if(obj.childNodes){	
		for(var i=obj.childNodes.length-1; i>=0; i--) obj.removeChild(obj.childNodes[i]);
	}
}

                        /************Code For List Service***************/
						
//List Service to Fill in the Form select controls with dynamic options if exilGridSource 
//attribute is set
function ListService(){}
//Method declarations for ListService
ListService.prototype.callListService =listService_callListService;
ListService.prototype.fetchList =listService_fetchList;

//To find the dynamic lists in form and form the queryString
function listService_callListService(){
	var frm = exilWindow.document.forms[0];
	var sources = new Object();
	var obj;
	var listFound = false;
	for(var i=0; i<frm.length; i++){
		obj = frm.elements[i];
		if(obj.tagName.toUpperCase()=='SELECT' && obj.getAttribute('exilListSource')){
			if(obj.getAttribute('exilAutoLoad') == "false"); // feature to disable auto load
			else sources[obj.getAttribute('exilListSource')] = "yes"; //accumulate with no duplicates
			listFound = true;
		}
	}
	if(listFound){
		this.fetchList(sources);
	}
}

//Method to raise a request to server for List
function listService_fetchList(sources){
	var src = '';
	if (exilWindow.location.host){//it is running from a server. request service from teh host
		if ( typeof(exilParms) != 'undefined' && exilParms.listServiceName){ 
			src = exilParms.listServiceName;
		}
		else alert ('Dear Programmer.. You have forgotten to include ExilityParameters.js file');
	}else{ //local disk. running in test or prototype mode.
		src = '../data/ListService.htm';
	}
	var qs = PageManager.DataService.getQueryString();
	var prefix;
	if (!qs) prefix = "?";
	else prefix = "&"

	for (var a in sources){
		qs += prefix + "serviceID=" +a;
		prefix = "&";
	}
	src += qs;

	PageManager.Helper.dispatchService(src);
}

                            /**************Code for Update Service******************/
							
// Service to Handle the Submition
function  UpdateService(){
}

//Method declartions for UpdateService
UpdateService.prototype.submitForm=updateService_submitForm;
UpdateService.prototype.fillMessage=updateService_fillMessage;
UpdateService.prototype.returnedFromServer=updateService_returnedFromServer;
UpdateService.prototype.resetCheckBoxes=updateService_resetCheckBoxes;
UpdateService.prototype.setCheckBoxes=updateService_setCheckBoxes;

//raise a request to the server to update 
function updateService_submitForm(serviceID){
	// if validation is included, validate the form
	if (typeof(PageValidator) != 'undefined'){
		if (!PageValidator.validateForm()) return false;
	}
	//is the Iframe already there..
	var iframeObj = exilWindow.document.getElementById('exilUpdateFrame');
	if (!iframeObj) iframeObj = exilWindow.frames['exilUpdateFrame'];
	if (!iframeObj){ // create it
		var ifrdiv=exilWindow.document.createElement('div');
		ifrdiv.style.display = 'none';
		ifrdiv.innerHTML="<iframe id=exilUpdateFrame name=exilUpdateFrame >	</iframe>";
		exilWindow.document.body.appendChild(ifrdiv);
		iframeObj=ifrdiv.childNodes[0];
	}
	
	var frm = exilWindow.document.forms[0];
	if (exilWindow.location.host){//it is running from a server. request service from teh host
		frm.method ="post";
		if (typeof(exilParms) != 'undefined' && exilParms.updateServiceName) frm.action = exilParms.updateServiceName + "?serviceID=" + serviceID; //common request
		else afrm.action = serviceID; //
	}else{ // this is a local file.. Call our own updateStub
		frm.action="../data/UpdateService.htm";
		frm.method="get";
	}
	frm.target=iframeObj.id;
	this.setCheckBoxes(frm);
	if(frm.disabled==true) frm.disabled=false;
	frm.submit();
	this.resetCheckBoxes(frm);
	frm.disabled=true; //so that user can not press submit again, or change any values.
						// there is a small issue with this. drop-downs are not disabled. It is OK
	this.fillMessage(null); //it hides message area if "" is passed						
}

function updateService_setCheckBoxes(frm){
	var len = frm.elements.length;
	var a;
	for (var i=0; i<len; i++){
		a = frm.elements[i];	
		if (a.type !="checkbox") continue;
		if (a.getAttribute('exilDontTouch')) continue;
		var checked = a.checked;
		a.value = (checked)? exilParms.checkedValue : exilParms.uncheckedValue;
		a.setAttribute('exilChecked', checked);
		a.checked = true;
	}
}

function updateService_resetCheckBoxes(frm){
	var len = frm.elements.length;
	var a;
	for (var i=0; i<len; i++){
		a = frm.elements[i];	
		if (a.type !="checkbox") continue;
		if (a.getAttribute('exilDontTouch')) continue;
		a.checked = a.getAttribute('exilChecked');
	}
}

//Method go get object and handle messages
function updateService_returnedFromServer(dc){
	if (typeof(exilParms) == 'undefined' || exilParms.verbose) {  
		var str = 'returned from update service with dc as follows:';
		str += PageManager.DataService.dcTobootbox.alert(dc);
		alert (str);
	}
	
	if (dc.success){ //update successful, redirect to next page
		var msg = "";
		for (var i=0; i<dc.messages.length; i++) msg+= dc.messages[i] + "\n";
		if (msg != "") alert (msg);
		if (typeof exilWindow.afterUpdateService !='undefined'){// programmer has supplied the function
			exilWindow.afterUpdateService(dc);
		}else{
			alert ('Update successful.\n A required function by name "afterUpdateService(dataCollection)" is not defined for this page. \nExility does cannot determine where to redirect'); 
		}
	}else{    
		this.fillMessage(dc.messages); //failure throw an error message back
		exilWindow.document.forms[0].disabled=false;
		if (typeof exilWindow.afterUpdateServiceFailed !='undefined'){// programmer has supplied the function
			exilWindow.afterUpdateServiceFailed(dc);
		}
	}		
}

function updateService_fillMessage(messages){
	var msgArea=exilWindow.document.getElementById('exilErrorArea');
	var msg = "";
	if (messages && messages.length && messages.length > 0){
		var separator = (msgArea)? "<br>" : "\n";
		for (var i=0; i<messages.length; i++) msg+= messages[i] + separator;
	}
	if(msgArea){
		msgArea.innerHTML=msg.replace("&","&amp;");
		msgArea.style.display = (msg == "")? 'none' : 'block';
	}else{
		if (msg != "")bootbox.alert(msg); 
	}
}
         /*******************Code for Description Service***********************/
		 
//Get the descriptions from server dynamically	 
function DescService(){
 	 //store all descriptions as property/value pairs. desc[descid][code] = desc 
 	 this.desc = new Object(); 
 	 this.requests =  new Object(); //keeps track of pending requests
}
//method declaration for Description Service
DescService.prototype.onblur=descService_onblur;
DescService.prototype.getDesc=descService_getDesc;
DescService.prototype.setDesc=descService_setDesc;
DescService.prototype.requestDesc=descService_requestDesc;
DescService.prototype.setDescToObjects=descService_setDescToObjects;

function descService_onblur(e){
	var evt  =  (exilWindow.event)? exilWindow.event : e ;// because NS passes evt while IE defines event as global
	var ele = (evt.srcElement) ? evt.srcElement : evt.target;
	if (ele.getAttribute('exilDataSource')){
		this.getDesc(ele);
		return;
	}
	if (ele.getAttribute('exilImmidiateValidation')){
	
		//if ( typeof(PageValidator)!= 'undefined') return(PageValidator.vaidateElemet(ele));
			/* changed The method Name by iliyaraja   */
		
		if ( typeof(PageValidator)!= 'undefined') return(PageValidator.validateElement(ele));
	}
}

function descService_getDesc(obj){
	var descID, code, desc, descObj;
	descID = obj.getAttribute('exilDataSource');
	if(!descID) return null;
	code = obj.value;
	if (null == code || code == "") return null;
	
	if (this.desc[descID])desc = this.desc[descID][code]; 
	if (desc)this.setDescToObjects(obj, code, desc, true);
	else {
		this.setDescToObjects(obj, code, null, "unknown");
		this.requestDesc(descID, code, obj);
	}
}

function descService_setDescToObjects(obj, code, desc, validity){
	//bootbox.alert(obj);//object
	//bootbox.alert( code);//input text value
	//bootbox.alert(desc);//null(wrong means)
	//bootbox.alert(validity);//unknown,false

	if (obj.value != code) return; 
	obj.setAttribute('exilValidity', validity);
	var exilInGrid = PageManager.Helper.objectIsInGrid(obj);
	var descFields = PageManager.Helper.getDescFields(obj);
	if (!descFields) return;
	
	if (desc == null) {
		var text;
		if (validity == "unknown") text = "getting value from server....";
		
		else {
					//Added by  Iliyaraja----- Alert for Function Name Invalid input 
		      		if(obj.getAttribute('exilDataSource')=="functionIdKey")
				{
					bootbox.alert("Invalid Function Name.");
					text="";
					obj.value='';
					obj.focus();
				}	
					// Alert for Account code Invalid input 
				else
				{
					bootbox.alert("Invalid Account code or Not Active for Posting\n or Not a Detailed Code.");
					text="";
					obj.value='';
					obj.focus();
				}
				
		       } //main else
		PageManager.DataService.changeNodeValue(descFields[0], text);
		for(var i=1; i<descFields.length; i++){
			if(descFields[i] != null) PageManager.DataService.changeNodeValue(descFields[i], "");
		}  
	} //main if
	else {
		var len = descFields.length;
		if (len > desc.length){
			bootbox.alert("Server returned only " + desc.length + " fields while description service for " +obj.id + " expected " + len + " fields. ");
			return;
		}  
		for(var i=0; i<len; i++){
			if(descFields[i] != null) PageManager.DataService.changeNodeValue(descFields[i], desc[i]);
		} 
	}  //else
}  //method

function descService_requestDesc(descID, code, obj){
	if (this.requests[descID] && this.requests[descID][code]) return;
	if (!this.requests[descID]) this.requests[descID] = new Object();
	this.requests[descID][code] = obj;
	var src;
	if (exilWindow.location.host){
		if(typeof(exilParms) != 'undefined' && exilParms.descServiceName)src = exilParms.descServiceName;
		else src = serviceID;
	}else{ 
		src = '../data/DescriptionService.htm';
	}

	src += '?serviceID=' + descID +'&keyValue=' +code;	
	PageManager.Helper.dispatchService(src);
}

function descService_setDesc(descID, code, desc){
	var obj = this.requests[descID][code];

	if (!obj){
		alert ('Mismatch in description service call. descID=' + descID + " and code=" + code);
		return;
	}
	if(this.requests[descID] && this.requests[descID][code])this.requests[descID][code]=null;
	var validity = "false";
	if (desc){
		validity = "true";
		if (!this.desc[descID]) this.desc[descID] = new Object();
		this.desc[descID][code] = desc;
	}
	
	this.setDescToObjects(obj, code, desc, validity);
}

 /*******************Code for Java Script Object(DataCollection)************************/

//Data structure equalent to java object in  server as an Object
function DataCollection(){
    this.values = new Object(); //name value pairs(value is simple String);
    this.lists = new Object();//name value pairs(value is an array)
    this.grids = new Object();//name value pairs(value is an array of array)
    this.messages = new Array();//Messaages in displayable form
    this.success; // boolean, representing the status of the call made.
}

function TreeService(){

this.loadTree = function (treeName, divName){
	PageManager.DataService.setQueryField('treeName', treeName);
	PageManager.DataService.setQueryField('divName', divName);

	var src
	if (exilWindow.location.host){
		if( typeof(exilParms)!= 'undefined' && exilParms.treeServiceName){
			src = exilParms.treeServiceName;
		}
		else src = treeName; // in this case, service id itself is the URL
	}else{ // this is a local file.. look for data in a local file
		src = '../data/' +treeName +'.js';
	}
	src = src + PageManager.DataService.getQueryString();
	PageManager.Helper.dispatchService(src);
}

this.displayTree = function (dc){
	if (!dc.success){
		PageManager.UpdateService.fillMessage(dc.messages);
		return;
	}
	
	var treeName = dc.values['treeName'];
	var divName = dc.values['divName'];
	
	var divObj = exilWindow.document.getElementById(divName);
	if (!divObj){
		bootbox.alert('div name ' + divName + ' is not an id of a div element');
		return;
	}
	
	if (typeof(exilParms) == 'undefined' || exilParms.verbose) 
		alert ('Dc returned from Tree Service' + PageManager.DataService.dcTobootbox.alert(dc));

	var grid = dc.grids[treeName];
	if (!grid) {
		bootbox.alert('Tree service did not return data in grid with name' + divName);
		return; 
	}

	var nbrCols = grid[0].length;
	var nbrRows = grid.length;
	if (nbrRows < 2 || nbrCols < 4 ){
		alert ('Tree service returned an invalid grid with only ' + nbrRows + ' rows and ' + nbrCols + ' columns.');
		return;
		
	} 
	
	var tree = new ExilTree(divObj);
	var node;
	for (var i=1; i<nbrRows; i++) {
		node = tree.createNode(grid[i][0],grid[i][1],grid[i][2],grid[i][3],grid[i][4]);
		for (var j=4; j<nbrCols; j++)node.setAttribute(grid[0][j], grid[i][j]);
	}
	
	if (typeof (exilWindow.beforeTreeRefresh) != 'undefined'){
		if (!exilWindow.beforeTreeRefresh(tree)) return;
	}
	
	tree.display();
	
	if (typeof (exilWindow.afterTreeRefresh) != 'undefined') exilWindow.afterTreeRefresh(tree);
}
}
//</script>
