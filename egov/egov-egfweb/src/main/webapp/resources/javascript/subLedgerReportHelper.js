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
function openSearch(obj, index)
{	var a = new Array(2);
	var screenName="report";
	var str="";
	document.getElementById("glCode1").value="";
	if (obj.toLowerCase() == 'relation')
			str = "../HTML/Search.html?tableNameForCode="+obj;
		else if(obj.toLowerCase() == 'chartofaccounts')
		{			
			str = "../HTML/Search.html?tableNameForCode=chartofaccounts_controlcodes";					
		}
	
	var sRtn = showModalDialog(str,"","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=400pt;status=no;");
			PageManager.DataService.setQueryField('index', '0');
    if(index==1)
    {
    	a = sRtn.split("`~`");
		var type = document.getElementById('glCode1');
		type.value = a[0]; 	
		rptGLCode = a[0];
		rptAccName = a[1];
		
		document.getElementById('chartOfAccounts_id').value = a[2];		
	}
	
	coaId=document.getElementById('chartOfAccounts_id').value;
	if(sRtn!='')
		loadSubLedgerTypeList();	
	type.value = a[0];	
}
function openAccountCodeSearch()
{	
		var	url = "../voucher/common!searchAccountCodes.action";
		window.open(url, 'EntitySearch','resizable=no,scrollbars=yes,left=300,top=40, width=400, height=500');
}
function openEntitySearch()
{	
	var detailtypeid = document.getElementById("subLedgerList").value;
	if( detailtypeid != null && detailtypeid != 0) {
		var	url = "../voucher/common!searchEntites.action?accountDetailType="+detailtypeid;
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
			document.getElementById('accEntityList').value=entity_array[0];
			document.getElementById('accEntityKey').value=entity_array[2];
			document.getElementById('entityName').value=entity_array[1];
		}
		else
		{
			bootbox.alert("Invalid entity selected.");
			document.getElementById('accEntityList').value="";
			document.getElementById('accEntityKey').value="";
			document.getElementById('entityName').value="";
		}
	}
}
function popupCallbackForAccountCode(glcode,coaid) {
	var obj = document.getElementById('glCode1');
	var neibrObjName = 'chartOfAccounts_id';
	var currRow=PageManager.DataService.getRow (obj);
 	neibrObj=PageManager.DataService.getControlInBranch(currRow,neibrObjName);
 	if(coaid ==null && (neibrObj.value!='' || neibrObj.value!=null) ) {  return ;}
 	else {
 		document.getElementById('glCode1').value=glcode;
 		neibrObj.value = coaid;
 	}
 	
			loadSubLedgerTypeList();
	
}

function loadEntityList()
{			
	var glCodeId = document.getElementById('chartOfAccounts_id').value;	
	glcode=document.getElementById('glCode1').value;	
	if(glCodeId != '')
	{
		PageManager.DataService.setQueryField('glcode', glcode);		
		PageManager.DataService.setQueryField('glCodeId', glCodeId);				
		PageManager.DataService.callDataService('accEntityList');				
	}	
}

function loadSubLedgerTypeList()
{	
	var glCodeId = document.getElementById('chartOfAccounts_id').value;	
	glcode=document.getElementById('glCode1').value;	
	if(glCodeId != '')
	{
		PageManager.DataService.setQueryField('glCode', glcode);		
		PageManager.DataService.callDataService('subLedgerList');				
	}	
}


function callEntity() {
	var detailtypeid = document.getElementById("subLedgerList").value;
	if( detailtypeid != null && detailtypeid != 0) {
		var	url = "../voucher/common!searchEntites.action?accountDetailType="+detailtypeid;
		window.open(url, 'EntitySearch','resizable=no,scrollbars=yes,left=300,top=40, width=400, height=500');
	} else {
		bootbox.alert("Select the Type.");
	}
}    
function autocompletecode(obj,eventObj)
{
	// set position of dropdown
	var src = obj;
	var target = document.getElementById('codescontainer');
	var posSrc=findPos(src);
	target.style.left=posSrc[0]-30;
	target.style.top=posSrc[1]+15;
	target.style.width=600;
	if(obj.name=='accEntityList') target.style.left=posSrc[0]-200;	
	var currRow=PageManager.DataService.getRow(obj);
	var coaCodeObj = obj;
	if(eventObj.keyCode != 40 )
	{
		if(eventObj.keyCode != 38 )
		{
	
			var oAutoComp = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', codeObj);
			oAutoComp.queryDelay = 0;
			oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
			oAutoComp.useShadow = true;
		}
	}	
}

//fills the related neighboor object after splitting 
function fillNeibrAfterSplit(obj,neibrObjName)
{  
	var currRow=PageManager.DataService.getRow (obj);
 	neibrObj=PageManager.DataService.getControlInBranch(currRow,neibrObjName);
 	var temp = obj.value; 
 	temp = temp.split("`-`");
 	if(obj.value==null || obj.value=="") { neibrObj.value= ""; return; }
 	if(temp[1]==null && (neibrObj.value!='' || neibrObj.value!=null) ) {  return ;}
 	else {
   		var temp1=temp[1].split("-$-");
   		obj.value=temp[0];
   		neibrObj.value = temp1[1];
   	}
   	loadSubLedgerTypeList();
}
function beforeRefreshPage(dc)
{
	var accName = dc.values['accName'];
	if(accName != null && accName != '')
		rptAccName = dc.values['accName'];	
	if(dc.values['glCode1']!=dc.values['glCode'])
	{	
		dc.values['accEntityList']='';
		dc.values['accEntityKey']='';
		dc.values['entityName']='';
	}
	var comboSubledgerList = document.getElementById('subLedgerList'); 			
	clearCombo(comboSubledgerList);	
	if(dc.values['glCode'])	
		dc.values['glCode1']=dc.values['glCode'];	
	
}

function afterRefreshPage(dc)
{
	coaId=dc.values['glCodeId'];
	if(dc.values['serviceID']=='cgNumber')
	{ 	
		loadSubLedgerTypeList();
	}

	if(dc.values['serviceID']=='getAccCOAID')
	{			
			var glCodeId = document.getElementById('chartOfAccounts_id').value;
			var glCode= document.getElementById('glCode1').value;
			if(glCodeId != '')
			{
				clearCombo(document.getElementById('subLedgerList'));
				PageManager.DataService.setQueryField('glCode', glCode);
				PageManager.DataService.callDataService('subLedgerList')
			}
	}
	if(dc.values['reportCodeFailed'])
	{
		bootbox.alert(dc.values['reportCodeFailed']);
	}
	PageManager.DataService.removeQueryField('startDate');
	PageManager.DataService.removeQueryField('endDate');
	rptAccName = dc.values['accName'];
	
	/* to fill the SubLedgerList Combo*/
	if(dc.values['serviceID']=='subLedgerList')
	{
		if(coaId && coaId != '')
			document.getElementById('chartOfAccounts_id').value=coaId;
		var subLedgerList = dc.grids['subLedgerList'];
		if(subLedgerList != null)
		{
			document.getElementById('accEntityId').value = dc.values['accountDetailTypeId'];
			var comboSubledgerList = document.getElementById('subLedgerList'); 			
			clearCombo(comboSubledgerList);
			var option;
			for(var i=0; i<subLedgerList.length; i++)
			{			
				option = document.createElement('OPTION');
				option.value=subLedgerList [i][0];
				option.text = subLedgerList [i][1];
				
				comboSubledgerList.add(option);			
			} 
			//callEntity();
			
		 }
	}
}
 
function clearCombo(obj)
{
	try{
		for(var i=obj.length-1;i>=0;i--)				
			obj.remove(i);			
	}catch(err) {}
		
}
function getAccountName(obj)
{
	var subLedgerType = document.getElementById('subLedgerList');
	clearCombo(subLedgerType);
	if(obj.value != '')
	{
		PageManager.DataService.setQueryField('glCode', obj.value);
		PageManager.DataService.callDataService('getAccName');
		PageManager.DataService.callDataService('getAccCOAID');
		loadSubLedgerTypeList();
	}
	else
		document.getElementById("chartOfAccounts_id").value='';
}
function getDescription(obj)
{
	coaId=document.getElementById('chartOfAccounts_id').value;

	document.getElementById("index").value=obj.selectedIndex;
	if(document.getElementById('accEntityId').value!='')
	{
		document.forms[0].accEntityKey.value=obj.options[obj.selectedIndex].value;
	}
}

function buttonFlush()
{	
	window.location="SubLedgerReport.jsp?reportType=sl";
}

function pageSetup()
{
	document.body.leftMargin=0.75;
	document.body.rightMargin=0.75;
	document.body.topMargin=0.75;
	document.body.bottomMargin=0.75;	
}


//For Print Preview method
function buttonPrintPreview()
{
	document.getElementById('accEntityId').value=document.getElementById('subLedgerList').value;	
	document.getElementById('fromBean').value ="2";	   
	document.forms[0].submit();	
}

//For print method
function buttonPrint()
{
	document.getElementById('accEntityId').value=document.getElementById('subLedgerList').value;
	document.getElementById('fromBean').value ="2";
    var hide1,hide2; 
    hide1 = document.getElementById("tbl-header1");
	hide1.style.display = "none";	
	document.forms[0].submit();
  	if(window.print)
  	{ 
  		window.print();
	} 
}
var detailTypeId=0;
function onFocusDetailCode(){
	var detailtypeidObj=document.getElementById("subLedgerList");
	if(detailTypeId != detailtypeidObj.value){
		detailTypeId = detailtypeidObj.value;
		loadDropDownCodesForEntities(); 
	}
}

var path="../..";

function loadDropDownCodesForEntities()
{
  	   oACDS = new YAHOO.widget.DS_XHR(path+"/voucher/common!ajaxLoadEntitesBy20.action", [ "~^"]);
	   oACDS.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
	   oACDS.scriptQueryParam = "startsWith";
	   var oAutoComp1 = new YAHOO.widget.AutoComplete("accEntityList",'codescontainer',oACDS);
	   oAutoComp1.doBeforeSendQuery = function(sQuery){
		   loadWaitingImage(); 
		   return sQuery+"&accountDetailType="+document.getElementById("subLedgerList").value;
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

function splitEntitiesDetailCode(obj)
{
	var entity=obj.value;
	if(entity.trim()!="")
	{
		var entity_array=entity.split("`~`");
		if(entity_array.length==2)
		{
			document.getElementById('accEntityList').value=entity_array[0].split("`-`")[0];
			document.getElementById('accEntityKey').value=entity_array[1];
			document.getElementById('entityName').value=entity_array[0].split("`-`")[1];
		}
	}
}
String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
};

function autocompleteEntities1(obj,myEvent)
{
	//bootbox.alert('autocomplete');
	var src = obj;	
	var target = document.getElementById('codescontainer');	
	var posSrc=findPos(src); 
	
	target.style.left=posSrc[0]-220;	
	target.style.top=posSrc[1];
	target.style.width=470;	
	      		
	
	var coaCodeObj=obj;
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
}
