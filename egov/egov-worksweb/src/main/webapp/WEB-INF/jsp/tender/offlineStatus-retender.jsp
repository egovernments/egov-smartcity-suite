<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>

<%@ include file="/includes/taglibs.jsp" %>
<html>
<head>
	<title>
	<s:text name="set.status" />
	</title>
</head>
<body onload="enableOrDisableRetendering();addElementsInLastColumn();" >
<script src="<egov:url path='resources/js/works.js?${app_release_no}'/>"></script>

<script type="text/javascript">
jQuery("#loadingMask").remove();
var ITERATION_COUNT = <s:property value="%{iterationCount}" />;
var RETENDER_HISTORY_LIST_INDEX = 0;
var RETENDER_HISTORY_TABLE_INDEX = 0;
var RETENDER_LIST_INDEX = 0;
function addRetenderInfo()
{
	if(validateSubmitRT()){
		addRTColumn('statustable');
		addRTTable('retenderInfoTable');
		dom.get("retenderingIsSelected").value='true';
		dom.get("retenderButton").disabled='true';
		ITERATION_COUNT++;
	}
}
function addRTTable(tabid)
{
	tabObj=document.getElementById(tabid);
	tabObj.style.display="";
	noOfRows = tabObj.rows.length;
	var uobjNameDate =  'retenderInfoList['+RETENDER_LIST_INDEX+'].retenderDate';
	var uobjNameReason =  'retenderInfoList['+RETENDER_LIST_INDEX+'].reason';
	var uobjIdDate =  'retenderInfoList['+eval(noOfRows-1)+'].retenderDate';
	var uobjIdReason =  'retenderInfoList['+eval(noOfRows-1)+'].reason';

	// Increment the RETENDER_LIST_INDEX
    RETENDER_LIST_INDEX++;
	
	var newRow = document.createElement('tr');
    var tdElem = document.createElement('td');
    tdElem.setAttribute('width', '4');
    tdElem.setAttribute('class', 'whitebox3wka');
    tdElem.appendChild(document.createTextNode(eval(noOfRows)));

    newRow.appendChild(tdElem);
	
    var tdElem1 = document.createElement('td');
    tdElem1.setAttribute('width', '56');
    tdElem1.setAttribute('class', 'whitebox3wka');

    var textArea = document.createElement('textarea');
    textArea.setAttribute('name', uobjNameReason);
    textArea.setAttribute('id', uobjIdReason);
    textArea.setAttribute('rows', '3');
    textArea.setAttribute('cols', '60');

    tdElem1.appendChild(textArea);
    newRow.appendChild(tdElem1);

    var tdElem2 = document.createElement('td');
    tdElem2.setAttribute('width', '20');
    tdElem2.setAttribute('class', 'whitebox3wka');
    var newField = document.createElement('input');
    newField.setAttribute('type', 'text');
    newField.setAttribute('name', uobjNameDate);
    newField.setAttribute('onfocus', 'javascript:vDateType="3";');
    newField.setAttribute('onkeyup', "DateFormat(this,this.value,event,false,'3')");
    newField.setAttribute('onblur', "isvalidFormat(this)");
    newField.setAttribute('size', '25');
    newField.setAttribute('value',getCurrentDate());
    newField.setAttribute('id', uobjIdDate);
    newField.setAttribute('class', 'selectboldwk');
    var aElem = document.createElement('a');
    
    aElem.setAttribute('href', "javascript:formCalendarRefRT('"+uobjIdDate+"')");
    aElem.setAttribute('id', 'dateHref[0]');
    aElem.setAttribute('dateHref', 'dateHref');
    aElem.setAttribute('onmouseover', "window.status='Date Picker';return true;" );
    aElem.setAttribute('onmouseout', "window.status='';return true;");

    var imgElem = document.createElement('img');
    imgElem.setAttribute('src', "/resources/erp2/images/calendar.png");
    imgElem.setAttribute('id', "wsDateImg");
    imgElem.setAttribute('alt', "Calendar");
    imgElem.setAttribute('width', "16" );
    imgElem.setAttribute('height', "16" );
    imgElem.setAttribute('border', "0");
    imgElem.setAttribute('align',"absmiddle");
    aElem.appendChild(imgElem);

    tdElem2.appendChild(newField);
    tdElem2.appendChild(document.createTextNode(" "));
    tdElem2.appendChild(aElem);

    newRow.appendChild(tdElem2);
    
    var tdElem3 = document.createElement('td');
    tdElem3.setAttribute('width', '20');
    tdElem3.setAttribute('class', 'whitebox3wka');
    tdElem3.appendChild(document.createTextNode('Retender '+eval(noOfRows)));
    
    newRow.appendChild(tdElem3);
    tabObj.appendChild(newRow);
}
function addRTColumn(tableId)
{
	 var table = document.getElementById(tableId);
	 var deleteColumnItemsArray=new Array();
	 for (var i = 0, row; row = table.rows[i]; i++) {
		var leng=row.childNodes.length;
		deleteColumnItemsArray[i]=row.childNodes[leng-2];
		row.removeChild(row.childNodes[leng-2]);
	 }	 
	 for (var i = 0, row; row = table.rows[i]; i++) {
		if(i==0)
		{
			var newTd = document.createElement('td');
			newTd.setAttribute('class', 'tablesubheadwk');
			var aElemTN = document.createTextNode('Date Retendered '+eval(ITERATION_COUNT+1));
			newTd.appendChild(aElemTN);
		    row.appendChild(newTd);
		}
		else
		{
			var uobj =  'statusInfo['+eval(i-1)+']['+eval(ITERATION_COUNT+2)+']';
			var nuobjName =  'retenderHistoryList['+RETENDER_HISTORY_LIST_INDEX+'].statusDate';
			var nuobjNameStatus =  'retenderHistoryList['+RETENDER_HISTORY_LIST_INDEX+'].egwStatus.id';
			RETENDER_HISTORY_LIST_INDEX++;
		    var newField = document.createElement('input');
		    newField.setAttribute('type', 'text');
		    newField.setAttribute('name', nuobjName);
		    newField.setAttribute('onfocus', 'javascript:vDateType="3";');
		    newField.setAttribute('onkeyup', "DateFormat(this,this.value,event,false,'3')");
		    newField.setAttribute('onblur', "isvalidFormat(this)");
		    newField.setAttribute('size', '25');
		    newField.setAttribute('id', uobj);
		    newField.setAttribute('class', 'selectboldwk');

			var hiddenStatusId = 'hiddenStatusInfo['+eval(i-1)+']';
		    var newField1 = document.createElement('input');
		    newField1.setAttribute('type', 'hidden');
		    newField1.setAttribute('name', nuobjNameStatus);
		    newField1.setAttribute('id', hiddenStatusId);
		    
		    var aElem = document.createElement('a');
		    aElem.setAttribute('href', "javascript:formCalendarRefRT('"+uobj+"')");
		    aElem.setAttribute('id', 'dateHref[0]');
		    aElem.setAttribute('dateHref', 'dateHref');
		    aElem.setAttribute('onmouseover', "window.status='Date Picker';return true;" );
		    aElem.setAttribute('onmouseout', "window.status='';return true;");

		    var imgElem = document.createElement('img');
		    imgElem.setAttribute('src', "/resources/erp2/images/calendar.png");
		    imgElem.setAttribute('id', "wsDateImg");
		    imgElem.setAttribute('alt', "Calendar");
		    imgElem.setAttribute('width', "16" );
		    imgElem.setAttribute('height', "16" );
		    imgElem.setAttribute('border', "0");
		    imgElem.setAttribute('align',"absmiddle");
		    aElem.appendChild(imgElem);
		    
		    var newTd = document.createElement('td');
		    newTd.setAttribute("class","whitebox3wka");
		    newTd.appendChild(newField);
		    newTd.appendChild(newField1);
		    newTd.appendChild(document.createTextNode(" "));
		    newTd.appendChild(aElem);
		    row.appendChild(newTd);   
		}
		row.appendChild(deleteColumnItemsArray[i]);	 
	 }
}

function isvalidFormat(obj)
{
	if(!checkDateFormat(obj)){
		dom.get("ws_error").innerHTML='<s:text name="invalid.fieldvalue.statusDate"/>'; 
		dom.get("ws_error").style.display='';
		window.scroll(0,0);
	 	return false;
	}
	dom.get("ws_error").style.display='none';
	dom.get("ws_error").innerHTML='';
	return true;
}
function isvalidFormatValue(val)
{
	if(!validateDate(val)){
		dom.get("ws_error").innerHTML='<s:text name="invalid.fieldvalue.statusDate"/>'; 
		dom.get("ws_error").style.display='';
		window.scroll(0,0);
	 	return false;
	}
	dom.get("ws_error").style.display='none';
	dom.get("ws_error").innerHTML='';
	return true;
}
function validateSubmitRT()
{
     var tbl = dom.get('statustable');
     //Dont include header
     var lastRow = RETENDER_HISTORY_TABLE_INDEX;
     var lastColumnIndex = dom.get('statustable').rows[1].cells.length-3;
     var obj;
     for(var i=0;i<lastRow;i++)
     {
    	 obj  =dom.get('statusInfo['+eval(i)+'][0]');  
       	if(obj && obj.value==-1 && obj.disabled==false)
       	{
       		 dom.get("ws_error").innerHTML='<s:text name="ws.status.is.null"/>'; 
       		 dom.get("ws_error").style.display='';
       		 window.scroll(0,0);
	 		 return false;
       	}
       	obj  = dom.get('statusInfo['+eval(i)+'][1]');
       	if(obj && obj.disabled==false)
        {
       		if(obj.value=="" )
           	{
           		 dom.get("ws_error").innerHTML='<s:text name="ws.statusDate.is.null"/>'; 
            	 dom.get("ws_error").style.display='';
            	 window.scroll(0,0);
    	 		 return false;
           	}else{
           	   if(!isvalidFormat(obj))return false;
           	}
        }   	
     }
     var selectObj;
     if(dom.get("retenderingIsSelected").value=='' || dom.get("retenderingIsSelected").value=='false')
     {
    	 for(var j=2;j<=ITERATION_COUNT+1;j++)
    		{
    			for(var i=0;i<lastRow;i++)
    	         {
    		         obj = dom.get('statusInfo['+eval(i)+']['+eval(j)+']');
    		         selectObj = dom.get('statusInfo['+eval(i)+'][0]');
    		        if(obj && obj.disabled==false && selectObj && selectObj.disabled==false)
    			    {
    		        	if(obj.value=="")
    		         	{
    		         		 dom.get("ws_error").innerHTML='<s:text name="ws.statusDate.is.null"/>'; 
    		          		 dom.get("ws_error").style.display='';
    		          		 window.scroll(0,0);
    		 		 		 return false;
    		         	}else{
    		         	   if(!isvalidFormat(obj))return false;
    		         	}
    				}     
    			}	
    		}
     }
        //Traverse in reverse and check for gaps
        var textPresent = false;
	   	 for(var i=lastRow-1;i>=0;i--)
	        {
	         obj = dom.get('statusInfo['+eval(i)+']['+eval(lastColumnIndex)+']');
	        if(obj)
		    {
	        	if(obj.value=="")
	         	{
		         	if(textPresent)
	        		{
		         		dom.get("ws_error").innerHTML='<s:text name="ws.statusDate.is.null"/>'; 
		          		dom.get("ws_error").style.display='';
		          		window.scroll(0,0);
		 		 		return false;	
		        	}
	         	}
	        	else
		        {
	        		textPresent = true;
	        		if(!isvalidFormat(obj))return false;
			    }	    	
			}     
		}
		if(!textPresent)
	 	{
			dom.get("ws_error").innerHTML='<s:text name="wp.retender.set.status.no.info"/>'; 
     		dom.get("ws_error").style.display='';
     		window.scroll(0,0);
	 		return false;
	 	}	
     //For checking if any new row does not have date entered
	 for(var i=lastRow-1;i>=0;i--)
     {
        obj = dom.get('statusInfo['+eval(i)+']['+eval(lastColumnIndex)+']');
        if(obj)
	    {
        	if(obj.value=="")
         	{
        		selectObj = dom.get('statusInfo['+i+'][0]'); 
	         	if(selectObj && selectObj.value!=-1 && selectObj.disabled==false)
        		{
	         		dom.get("ws_error").innerHTML='<s:text name="ws.statusDate.is.null"/>'; 
	          		dom.get("ws_error").style.display='';
	          		window.scroll(0,0);
	 		 		return false;	
	        	}
         	}
		}     
	}
		         
    for(var i=0;i<lastRow;i++){
      	for(var j=1;j<lastRow;j++){
      		if(i!=j &&  dom.get('statusInfo['+eval(i)+'][0]')!=null && dom.get('statusInfo['+eval(j)+'][0]')!=null && 
      			dom.get('statusInfo['+eval(i)+'][0]').value==dom.get('statusInfo['+eval(j)+'][0]').value){
        		dom.get("ws_error").innerHTML='The Status '+dom.get('statusInfo['+eval(i)+'][0]').options[dom.get('statusInfo['+eval(i)+'][0]').selectedIndex].text+' is set multiple times'; 
         		dom.get("ws_error").style.display='';
         		window.scroll(0,0);
		 		return false;
        	}
      	}
    }
    dom.get("ws_error").style.display='none';
	dom.get("ws_error").innerHTML='';
	return true;
}
function formCalendarRefST(i,j)
{
	var statusTable = "statusInfo["+i+"]["+j+"]";
	show_calendar("getElementById('"+statusTable+"')");
}
function formCalendarRefRT(objId)
{
	 show_calendar("getElementById('"+objId+"')");
}
function addNewRow(tableId)
{
	if(validateSubmitRT()){
		//Disable the retender button
		dom.get("retenderButton").disabled='true';
		
		var tbl = dom.get(tableId);
		var rowEle = document.createElement("tr");
		 
		var lastRowIndex = RETENDER_HISTORY_TABLE_INDEX-1;
		var lastColumnIndex = parseInt(tbl.rows[lastRowIndex].cells.length)-3;
		var objName;
		var hiddenObjId;
		var selectField,option,newField,selectFieldTd ;
		var slNoTd = document.createElement('td');
		slNoTd.setAttribute("class","whitebox3wka");
		var slNo = document.createElement('input');
		slNo.setAttribute("type","text");
		slNo.setAttribute("value",eval(lastRowIndex+2)+'');
		slNo.setAttribute("name","slNo");
		slNo.setAttribute("id","slNo");
		slNo.setAttribute("size",'1');
		slNo.setAttribute("disabled","true");
		slNoTd.appendChild(slNo);
		rowEle.appendChild(slNoTd);
		for(var j=0;j<=(lastColumnIndex);j++)
		{
			objId="statusInfo["+eval(lastRowIndex+1)+"]["+j+"]";
			if(j==0)
			{
				selectFieldTd = document.createElement('td');
				selectFieldTd.setAttribute("class","whitebox3wka");
				selectField = document.createElement('select');
				selectField.setAttribute('id', objId);
				selectField.setAttribute('name', objId);
				option= document.createElement('option');
				option.setAttribute('value', -1);
				option.appendChild(document.createTextNode('<s:text name="estimate.default.select" />'));
				selectField.appendChild(option);
			    <s:iterator value="allStatus" >
					option= document.createElement('option');
					option.setAttribute('value', '<s:property value="%{id}" />');
					option.appendChild(document.createTextNode('<s:property value="%{description}" />'));
					selectField.appendChild(option);	    
			    </s:iterator>
			    selectFieldTd.appendChild(selectField);
			    rowEle.appendChild(selectFieldTd);
			}
			else if(j==lastColumnIndex)
			{
				var uobj =  'statusInfo['+eval(lastRowIndex+1)+']['+eval(j)+']';
				var nuobjName =  'retenderHistoryList['+RETENDER_HISTORY_LIST_INDEX+'].statusDate';
				var nuobjNameStatus =  'retenderHistoryList['+RETENDER_HISTORY_LIST_INDEX+'].egwStatus.id';
				RETENDER_HISTORY_LIST_INDEX++;
			    var newField = document.createElement('input');
			    newField.setAttribute('type', 'text');
			    newField.setAttribute('name', nuobjName);
			    newField.setAttribute('onfocus', 'javascript:vDateType="3";');
			    newField.setAttribute('onkeyup', "DateFormat(this,this.value,event,false,'3')");
			    newField.setAttribute('onblur', "isvalidFormat(this)");
			    newField.setAttribute('size', '25');
			    newField.setAttribute('id', uobj);
			    newField.setAttribute('class', 'selectboldwk');

				var hiddenStatusId = 'hiddenStatusInfo['+eval(lastRowIndex+1)+']';
			    var newField1 = document.createElement('input');
			    newField1.setAttribute('type', 'hidden');
			    newField1.setAttribute('name', nuobjNameStatus);
			    newField1.setAttribute('id', hiddenStatusId);
			    
			    var aElem = document.createElement('a');
			    aElem.setAttribute('href', "javascript:formCalendarRefRT('"+uobj+"')");
			    aElem.setAttribute('id', 'dateHref[0]');
			    aElem.setAttribute('dateHref', 'dateHref');
			    aElem.setAttribute('onmouseover', "window.status='Date Picker';return true;" );
			    aElem.setAttribute('onmouseout', "window.status='';return true;");

			    var imgElem = document.createElement('img');
			    imgElem.setAttribute('src', "/resources/erp2/images/calendar.png");
			    imgElem.setAttribute('id', "wsDateImg");
			    imgElem.setAttribute('alt', "Calendar");
			    imgElem.setAttribute('width', "16" );
			    imgElem.setAttribute('height', "16" );
			    imgElem.setAttribute('border', "0");
			    imgElem.setAttribute('align',"absmiddle");
			    aElem.appendChild(imgElem);
			    
			    var newTd = document.createElement('td');
			    newTd.setAttribute("class","whitebox3wka");
			    newTd.appendChild(newField);
			    newTd.appendChild(newField1);
			    newTd.appendChild(document.createTextNode(" "));
			    newTd.appendChild(aElem);
			    rowEle.appendChild(newTd);   
			}
			else
			{
				var uobj =  'statusInfo['+eval(lastRowIndex+1)+']['+eval(j)+']';
			    var newField = document.createElement('input');
			    newField.setAttribute('type', 'text');
			    newField.setAttribute('name', uobj);
			    newField.setAttribute('onfocus', 'javascript:vDateType="3";');
			    newField.setAttribute('onkeyup', "DateFormat(this,this.value,event,false,'3')");
			    newField.setAttribute('onblur', "isvalidFormat(this)");
			    newField.setAttribute('size', '25');
			    newField.setAttribute('id', uobj);
			    newField.setAttribute('disabled', "true");
			    newField.setAttribute('class', 'selectboldwk');

			    var imgElem = document.createElement('img');
			    imgElem.setAttribute('src', "/resources/erp2/images/calendar.png");
			    imgElem.setAttribute('id', "wsDateImg");
			    imgElem.setAttribute('alt', "Calendar");
			    imgElem.setAttribute('width', "16" );
			    imgElem.setAttribute('height', "16" );
			    imgElem.setAttribute('border', "0");
			    imgElem.setAttribute('align',"absmiddle");
			    
			    var newTd = document.createElement('td');
			    newTd.setAttribute("class","whitebox3wka");
			    newTd.appendChild(newField);
			    newTd.appendChild(document.createTextNode(" "));
			    newTd.appendChild(imgElem);
			    rowEle.appendChild(newTd); 
			}			
		}
		var delIndex = lastRowIndex-1;
		if(delIndex<=0)
			delIndex=1; // Doing this, we get the first red icon 
		var deleteNode=tbl.rows[delIndex].cells[lastColumnIndex+2].cloneNode(true);
		rowEle.appendChild(deleteNode);
		//LastCharacter is needed
		rowEle.appendChild(document.createTextNode(""));
		tbl.appendChild(rowEle);
		// Add back the href for this delete icon
		document.getElementsByName("delHref")[lastRowIndex+1].onclick=function(){deleterow(document.getElementsByName("delHref")[lastRowIndex+1]);};
		RETENDER_HISTORY_TABLE_INDEX++;	
	}  
}
function enableOrDisableRetendering()
{
	<s:if test="%{retenderingIsAllowed}" >
		document.getElementById("retenderButton").disabled = false;
	</s:if>
	<s:if test="%{viewMode}" >
		toggleFields(true,['closeButton']);
	</s:if>
}
function deleterow(obj)
{
	var tbl = dom.get('statustable');
	var rowNumber=getRow(obj).rowIndex;
	var currRow1=getRow(obj);
	if(tbl.rows.length>2){
		tbl.deleteRow(rowNumber);
	}
	else{
		dom.get("ws_error").innerHTML='Last Row Cannot be Deleted';
        dom.get("ws_error").style.display='';
        window.scroll(0,0);
		return false;
	}
	if(dom.get("statustable")!=null){
		if(dom.get("statustable").rows.length>0){
		var len =dom.get("statustable").rows.length;
		var rows = parseInt(tbl.rows.length)-2;
		if(len>2){
			for(var i=0;i<len-1;i++)
			{
				document.forms[0].slNo[i].value=eval(i+1);
			}
	 	 }
	 	 else{
	 	 	document.forms[0].slNo.value=1;
	 	 }
	  }
  	  dom.get('statusrow').style.display='';
   }
	RETENDER_HISTORY_TABLE_INDEX--;   
}
function validateDataBeforeSubmit()
{
	var tbl = dom.get("statustable");
	var lastRowIndex = RETENDER_HISTORY_TABLE_INDEX-1;
	var lastColumnIndex = parseInt(tbl.rows[lastRowIndex].cells.length)-3;
	var objId,obj;
	var properStatusOrderArray=new Array();
	var properStatusNameOrderArray=new Array();
	var i=0,correctOrderOfStatus = "";

	if(!validateSubmitRT())
		return false;
	
	//Validate order of status
	<s:iterator value="allStatus" >
		properStatusOrderArray[i]= <s:property value="%{id}" />;
		properStatusNameOrderArray[i]='<s:property value="%{description}" />';
		if(i==0)
			correctOrderOfStatus =  '<s:property value="%{description}" />';
		else
			correctOrderOfStatus =  correctOrderOfStatus + " , " +'<s:property value="%{description}" />';		
		i++;
	</s:iterator>
	
	var orderStatusIndex = 0;
	for(i=0;i<=lastRowIndex;i++)
	{
		objId = "statusInfo["+i+"][0]";
		obj = document.getElementById(objId);
		if(obj)
		{
			if(properStatusOrderArray[orderStatusIndex]==obj.value)
			{
				orderStatusIndex++;		
			}
			else
			{
				dom.get("ws_error").innerHTML='<s:text name="wp.retender.correct.order"/>'+correctOrderOfStatus; 
			    dom.get("ws_error").style.display='';
			    window.scroll(0,0);
				return false;
			}		
		}
	}
	// Check for dates
	var dateIndex = 0;
	var dateArray=new Array();
	//Dates should be gathered columnwise not in the conventional rowwise manner
	for(var j=1;j<=lastColumnIndex;j++)
	{
		for(i=0;i<=lastRowIndex;i++)
		{
			objId = "statusInfo["+i+"]["+j+"]";
			obj = document.getElementById(objId);
			if(obj)
			{
				if(obj.value!='')
				{
					dateArray[dateIndex] = obj.value;
					dateIndex++;
				}	
			}
		}
	}
	var date1,date2;
	var appDateProperFormat = dom.get("appDateProperFormat").value;
	for(i=0;i<dateIndex-1;i++)
	{
		date1 = dateArray[i];
		date2 = dateArray[i+1];
		if(!compareDate(date1,date2))
		{
			dom.get("ws_error").innerHTML='<s:text name="wp.retender.incorrect.date.order"/>'+date1+' , '+date2; 
		    dom.get("ws_error").style.display='';
		    window.scroll(0,0);
			return false;
		}
		if(!compareDate(date1,getCurrentDate()))
		{
			dom.get("ws_error").innerHTML='<s:text name="wp.retender.no.futuredate"/>'+date1; 
		    dom.get("ws_error").style.display='';
		    window.scroll(0,0);
			return false;
		}
		if(!compareDate(appDateProperFormat, date1))
		{
			dom.get("ws_error").innerHTML='<s:text name="wp.retender.greater.than.approved.date"/>'+appDateProperFormat; 
		    dom.get("ws_error").style.display='';
		    window.scroll(0,0);
			return false;
		}
		if(!isvalidFormatValue(date1))return false;		
	}
	//Validate last date
	if(!compareDate(dateArray[dateIndex-1],getCurrentDate()))
	{
		dom.get("ws_error").innerHTML='<s:text name="wp.retender.no.futuredate"/>'+dateArray[dateIndex-1]; 
	    dom.get("ws_error").style.display='';
	    window.scroll(0,0);
		return false;
	}
	if(!compareDate(appDateProperFormat,dateArray[dateIndex-1]))
	{
		dom.get("ws_error").innerHTML='<s:text name="wp.retender.greater.than.approved.date"/>'+appDateProperFormat;
	    dom.get("ws_error").style.display='';
	    window.scroll(0,0);
		return false;
	}
	if(!isvalidFormatValue(dateArray[dateIndex-1]))return false;			
		
	//Validate retenderTable
	var retTabObj=document.getElementById("retenderInfoTable");
	noOfRows = retTabObj.rows.length;	
	for(i=0;i<noOfRows-1;i++)
	{
		obj = document.getElementById("retenderInfoList["+i+"].reason");
		if(obj)
		{
			if(obj.value=='')
			{
				dom.get("ws_error").innerHTML='<s:text name ="wp.retender.enter.reason" />'; 
			    dom.get("ws_error").style.display='';
			    window.scroll(0,0);
				return false;
			}
		}
		obj = document.getElementById("retenderInfoList["+i+"].retenderDate");
		if(obj)
		{
			if(obj.value=='')
			{
				dom.get("ws_error").innerHTML='<s:text name ="wp.retender.enter.date" />'; 
			    dom.get("ws_error").style.display='';
			    window.scroll(0,0);
				return false;
			}
			else if(!isvalidFormat(obj))return false;
			//Check only last retendered date with status date
			if(i==noOfRows-2)
			{
				if(!compareDate(dateArray[dateIndex-1],obj.value))
				{
					dom.get("ws_error").innerHTML='<s:text name ="wp.retender.date.less.than.statusdate" />'; 
				    dom.get("ws_error").style.display='';
				    window.scroll(0,0);
					return false;	
				}				
			}	
		}
	}
	var obj2;
	for(i=0;i<noOfRows-2;i++)
	{
		obj = document.getElementById("retenderInfoList["+i+"].retenderDate");
		obj2 = document.getElementById("retenderInfoList["+eval(i+1)+"].retenderDate");
		if(obj && obj2 &&  !compareDate(obj.value,obj2.value))
		{
			dom.get("ws_error").innerHTML='<s:text name ="wp.retender.increasing.date" />'; 
		    dom.get("ws_error").style.display='';
		    window.scroll(0,0);
			return false;
		}		
	}
	setHiddenValues();	
	dom.get("ws_error").innerHTML=''; 
    dom.get("ws_error").style.display='none';
    disableNoDataFields();
    jQuery(".commontopyellowbg").prepend('<div id="loadingMask" style="display:none;overflow:none;scroll:none;" ><img src="/egi/images/bar_loader.gif"> <span id="message">Please wait....</span></div>')
    doLoadingMask();
	return true;
}
function disableNoDataFields()
{
	var lastColumnIndex = dom.get('statustable').rows[1].cells.length-3;
   	 for(var i=RETENDER_HISTORY_LIST_INDEX-1;i>=0;i--)
     {
        obj = document.getElementsByName('retenderHistoryList['+eval(i)+'].statusDate');
        if(obj)
	    {
        	if(obj[0].value=="")
         	{
        		obj[0].disabled=true;
        		obj = document.getElementsByName('retenderHistoryList['+eval(i)+'].egwStatus.id');
    	        if(obj)
    		    {
    	        	obj[0].disabled=true;    
    		    }    
         	}
		}     
	}
}	
function setHiddenValues()
{
	// For setting the hidden list elements with the respective egwStatus values 
	var obj, objName, objId, objArray, rowIndex;
	for(var i=0;i<RETENDER_HISTORY_LIST_INDEX;i++)
	{
		objName ="retenderHistoryList["+i+"].egwStatus.id";
		objArray = document.getElementsByName(objName);
		if(objArray.length==1 ) 
		{
			//Date is present, status should be set
			rowIndex = extractRowFromId(objArray[0].id);
			document.getElementsByName("retenderHistoryList["+i+"].egwStatus.id")[0].value = document.getElementById("statusInfo["+rowIndex+"][0]").value;
		}			
	}	
}
function extractRowFromId(rowId)
{
	var retVal = "", objArr;
	if(rowId && rowId!='')
	{
		objArr = rowId.split("hiddenStatusInfo[");
		if(objArr.length==2)
		{
			objArr = objArr[1].split("]");
			retVal = objArr[0]; 
		}	
	}
	return retVal;	
}
function compareDate(obj1,obj2){
	if(obj1=='' || obj2==''){
		return false;
	}
	var dt1  = parseInt(obj1.substring(0,2),10);
	var mon1 = parseInt(obj1.substring(3,5),10);
	var yr1  = parseInt(obj1.substring(6,10),10);
	var date1 = new Date(eval(yr1), eval(mon1)-1,eval(dt1));
	var dt2  = parseInt(obj2.substring(0,2),10);
	var mon2 = parseInt(obj2.substring(3,5),10);
	var yr2  = parseInt(obj2.substring(6,10),10);
	var date2 = new Date(eval(yr2),eval(mon2)-1,eval(dt2)); 
	if(date2 < date1){
     	return false;
	}else{
		return true;
	} 
}
function addElementsInLastColumn()
{
	 var table = document.getElementById('statustable');
	 var leng,obj,oldTd;
	 if(ITERATION_COUNT>=1)
	 {
		 //i=0 is the header row
		 for (var i = 1, row; row = table.rows[i]; i++) {
			leng=row.childNodes.length;
			obj = document.getElementById('statusInfo['+eval(i-1)+']['+eval(ITERATION_COUNT+1)+']');
			if(obj && obj.value=='')
			{
				oldTd = row.childNodes[leng-4];
				
				//Create new TD and elements
				var uobj =  'statusInfo['+eval(i-1)+']['+eval(ITERATION_COUNT+1)+']';
				var nuobjName =  'retenderHistoryList['+RETENDER_HISTORY_LIST_INDEX+'].statusDate';
				var nuobjNameStatus =  'retenderHistoryList['+RETENDER_HISTORY_LIST_INDEX+'].egwStatus.id';
				RETENDER_HISTORY_LIST_INDEX++;
			    var newField = document.createElement('input');
			    newField.setAttribute('type', 'text');
			    newField.setAttribute('name', nuobjName);
			    newField.setAttribute('onfocus', 'javascript:vDateType="3";');
			    newField.setAttribute('onkeyup', "DateFormat(this,this.value,event,false,'3')");
			    newField.setAttribute('onblur', "isvalidFormat(this)");
			    newField.setAttribute('size', '25');
			    newField.setAttribute('id', uobj);
			    newField.setAttribute('class', 'selectboldwk');

				var hiddenStatusId = 'hiddenStatusInfo['+eval(i-1)+']';
			    var newField1 = document.createElement('input');
			    newField1.setAttribute('type', 'hidden');
			    newField1.setAttribute('name', nuobjNameStatus);
			    newField1.setAttribute('id', hiddenStatusId);
			    
			    var aElem = document.createElement('a');
			    aElem.setAttribute('href', "javascript:formCalendarRefRT('"+uobj+"')");
			    aElem.setAttribute('id', 'dateHref[0]');
			    aElem.setAttribute('dateHref', 'dateHref');
			    aElem.setAttribute('onmouseover', "window.status='Date Picker';return true;" );
			    aElem.setAttribute('onmouseout', "window.status='';return true;");

			    var imgElem = document.createElement('img');
			    imgElem.setAttribute('src', "/resources/erp2/images/calendar.png");
			    imgElem.setAttribute('id', "wsDateImg");
			    imgElem.setAttribute('alt', "Calendar");
			    imgElem.setAttribute('width', "16" );
			    imgElem.setAttribute('height', "16" );
			    imgElem.setAttribute('border', "0");
			    imgElem.setAttribute('align',"absmiddle");
			    aElem.appendChild(imgElem);
			    
			    var newTd = document.createElement('td');
			    newTd.setAttribute("class","whitebox3wka");
			    newTd.appendChild(newField);
			    newTd.appendChild(newField1);
			    newTd.appendChild(document.createTextNode(" "));
			    newTd.appendChild(aElem);
				//Replace old td with new td
			    oldTd.parentNode.replaceChild(newTd, oldTd);
			}	
		 }	 
	 }	 
}
</script>
<div class="errorstyle" id="ws_error" style="display: none;"></div>
<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
<s:form name="setStatusForm" theme="simple" onsubmit="return validateDataBeforeSubmit();" >
<s:token/>
	<div class="formmainbox"></div>
	<div class="insidecontent">
	<div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	<div class="rbcontent2">
	<s:hidden name="objectType" id="objectType" value="%{objectType}"></s:hidden>
	<s:hidden name="objId" id="objectId" value="%{objId}"></s:hidden>
	<s:hidden name="appDate" id="appDate" value="%{appDate}"></s:hidden>
	<s:hidden name="appDateProperFormat" id="appDateProperFormat" value="%{appDateProperFormat}"></s:hidden>
	
	<s:hidden name="retenderingIsAllowed" id="retenderingIsAllowed" value="%{retenderingIsAllowed}" />
	<s:hidden name="retenderingIsSelected" id="retenderingIsSelected" value="%{retenderingIsSelected}" />

		<table width="100%" border="0" cellspacing="0" cellpadding="0">          
			<tr>
			  <td>&nbsp;</td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr> 
				<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="/egworks/resources/erp2/images/arrow.gif" /></div>
				<div class="headplacer" id="heading"><s:text name="set.status" />:</div></td>
			</tr>
			<tr>
				<td colspan="4">
			         <table  width="100%" border="0" cellspacing="0" cellpadding="0" id="statustable1">
						<tr>
							<td width="30%" class="whitebox3wka">
									<s:if test="%{viewMode==false}" >
										<input type="button" disabled="true" class="buttonadd" value="Retender" name="retenderButton" id="retenderButton" onclick="addRetenderInfo()" />
									</s:if>	
									<br />
									<b><s:property value="%{objectType}"/>&nbsp;&nbsp;Approved On :&nbsp;&nbsp;<s:property value="%{appDate}"/></b>
							 			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<b><s:property value="%{objectType}"/>&nbsp;&nbsp;Number :&nbsp;&nbsp;<s:property value="%{objNo}"/></b>
							</td>
						</tr>
					</table>
				</td>
			</tr>	
			<tr>
				<td colspan="4">
					<table  width="100%" border="0" cellspacing="0" cellpadding="0" id="statustable">
						<tr>
							<td width="7%" class="tablesubheadwk">
								<s:text name="column.title.SLNo" />
		  					</td>
							<td  class="tablesubheadwk">
		 						<span class="mandatory">*</span><s:text name="works.status"/>
							</td>
							<td  class="tablesubheadwk">
								<span class="mandatory">*</span><s:text name="status.date"/>
							</td>
		  					<s:iterator status="innerloop" value="columnloop">
								<s:if test="%{#innerloop.index > 1}">
									<td  class="tablesubheadwk">
										<span class="mandatory">*</span><s:text name="status.date"/>&nbsp;Retendered&nbsp;<s:property value="%{#innerloop.index-1}"/>
									</td>
								</s:if>									
							</s:iterator>
							<td width="7%" class="tablesubheadwk">
								<a id="statusrow" href="#" onclick="addNewRow('statustable');return false;">
								<img border="0" alt="Add Status" src="/egworks/resources/erp2/images/add.png" /></a>
							</td>
						</tr>
						<s:if test="%{!rowloop.isEmpty()}">
							<s:iterator value="statusInfo" var="mySched" status="iterStatus">
								<tr >
									<td  class="whitebox3wka">
										<input type="text" name="slNo" id="slNo" disabled="true" size="1" value='<s:property value="%{#iterStatus.count}"/>'/>
									</td> 
									<s:iterator value="mySched" status="innerIterStatus">
										<s:if test="%{#innerIterStatus.index == 0}">
											<td  class="whitebox3wka">
												<s:select name="statusInfo[%{#iterStatus.index}][%{#innerIterStatus.index}]" id="statusInfo[%{#iterStatus.index}][%{#innerIterStatus.index}]" list="allStatus" 
													listKey="id" listValue="description" headerKey="-1"  
													headerValue="%{getText('estimate.default.select')}" disabled="true" />
												<script>
													document.getElementById('statusInfo[<s:property value="%{#iterStatus.index}" />][<s:property value="%{#innerIterStatus.index}" />]').value="<s:property />";
												</script>
											</td>
										</s:if>
										<s:else>
											<td  class="whitebox3wka">
												<input name='statusInfo[<s:property value="%{#iterStatus.index}" />][<s:property value="%{#innerIterStatus.index}" />]' value="<s:property />" size="25" id='statusInfo[<s:property value="%{#iterStatus.index}" />][<s:property value="%{#innerIterStatus.index}" />]' class="selectboldwk" 
									        		 onfocus="javascript:vDateType='3';" disabled="true" 
									        		 onkeyup="DateFormat(this,this.value,event,false,'3')"   onblur="isvalidFormat(this)"/>
									        		 <a href="#" name="dateHref" id='dateHref[0]'
									        		 onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;">
									        		 <img src="/egworks/resources/erp2/images/calendar.png" id="wsDateImg" alt="Calendar" width="16" height="16" 
									       			  border="0" align="absmiddle" /></a>
									       	</td>		  
										</s:else>
								   </s:iterator>
						   			<td width="7%" align="right" class="whitebox3wka" style="border-left-width: 0px">
							       		<a id="delHref" name="delHref" href="#" >
							       		<img border="0" alt="Delete Estimates" src="/egworks/resources/erp2/images/cancel.png" /></a>
						       		</td>
								</tr>
								<script>RETENDER_HISTORY_TABLE_INDEX++;</script>							   
							</s:iterator> 
						</s:if>
						<s:else>
							<tr id="detailsRow">
								<td width="4%" class="whitebox3wka">
									<input type="text" name="slNo" id="slNo" disabled="true" size="1" value="1"/>
								</td>
								<td width="60%" class="whitebox3wka">
									<s:select name="statusInfo[0][0]" id="statusInfo[0][0]" list="allStatus" headerKey="-1"  listKey="id" listValue="description" 
										headerValue="%{getText('estimate.default.select')}"/>
								</td>
								<td width="30%" class="whitebox3wka">
									<s:date name="model.statusDate" var="statusDateFormat" format="dd/MM/yyyy"/>
									<s:textfield name="retenderHistoryList[0].statusDate" value="%{statusDateFormat}" size="25" id="statusInfo[0][1]"  cssClass="selectboldwk" 
										onfocus="javascript:vDateType='3';" 
										onkeyup="DateFormat(this,this.value,event,false,'3')"  onblur="isvalidFormat(this)"/>
										<a href="javascript:formCalendarRefST(0,1);" id="dateHref[0]" name="dateHref"
											onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;">
											<img src="/egworks/resources/erp2/images/calendar.png" id="wsDateImg" alt="Calendar" width="16" height="16" 
							    				border="0" align="absmiddle" /></a>
							    	<input type="hidden" name="retenderHistoryList[0].egwStatus.id" id="hiddenStatusInfo[0]" />
							    	<script>RETENDER_HISTORY_LIST_INDEX++;</script>			
								</td>
								<td width="7%" align="right" class="whitebox3wka" style="border-left-width: 0px">
									<a id="delHref" name="delHref" href="#" onclick="deleterow(this)">
									<img border="0" alt="Delete Estimates" src="/egworks/resources/erp2/images/cancel.png" /></a>
								</td>
								<script>RETENDER_HISTORY_TABLE_INDEX++;</script>
							</tr>
						</s:else>
					</table>
				</td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td colspan="4">
					<table  width="70%" border="0" cellspacing="0" cellpadding="0" id="retenderInfoTable" style="display: none;">
						<tr>
							<td width="4%" class="tablesubheadwk">
								<s:text name="column.title.SLNo" />
		  					</td>
		  					<td width="20%" class="tablesubheadwk">
		 						<span class="mandatory">*</span><s:text name="wp.retender.reason"/>
							</td>
							<td width="20%" class="tablesubheadwk">
								<span class="mandatory">*</span><s:text name="wp.retender.date"/>
							</td>
							<td width="7%" class="tablesubheadwk">
								<s:text name="wp.retender.remarks"/>
							</td>
						</tr>
						<s:iterator value="worksPackage.retenderDetails" var="mySched" status="iterStatus"> 
							<tr >
								<td  class="whitebox3wka" width="4%">
									<s:property value="%{#iterStatus.count}"/>
								</td> 
								<td  class="whitebox3wka" width="20%">
									<textarea cols="60" rows="3" id='retenderInfoList[<s:property value="%{#iterStatus.index}" />].reason' class="selectboldwk" disabled="true" ><s:property value="%{reason}"/></textarea>
						       	</td>
						       	<td  class="whitebox3wka" width="20%">
						       		<s:date name="retenderDate" var="statusDateFormat" format="dd/MM/yyyy"/>
									<s:textfield value="%{statusDateFormat}" size="25" id='retenderInfoList[%{#iterStatus.index}].retenderDate' cssClass="selectboldwk" 
						        		 disabled="true" />
						        		 <a href="#" name="dateHref" id='dateHref[0]'>
						        		 <img src="/egworks/resources/erp2/images/calendar.png" id="wsDateImg" alt="Calendar" width="16" height="16" 
						       			  border="0" align="absmiddle" /></a>
						       	</td>	
						       	<td  class="whitebox3wka" width="7%">
						       		Retender&nbsp;<s:property value="%{iterationNumber}" />
						       	</td>		  
							</tr>							   
						</s:iterator> 
					</table>
					<script>
					<s:if  test="%{worksPackage.retenderDetails.size()>0}" >
						document.getElementById("retenderInfoTable").style.display="";
					</s:if>
					</script>
				</td>
			</tr>
			<tr><td>&nbsp;</td></tr>
		    <tr>
		    	<td><div align="right" class="mandatory" style="font-size:11px;padding-right:20px;">* <s:text name="message.mandatory" /></div></td>
		    </tr>     
		</table>
	</div><!-- end of rbroundbox2 -->
	<div class="rbbot2"><div></div></div>
	</div><!-- end of insidecontent -->
	</div><!-- end of formmainbox -->
	<div class="buttonholderwk" id="buttons">
	<s:if test="%{viewMode}" >
		<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" 
			onclick="window.close();"/>
	</s:if>
	<s:else>
		<s:submit type="submit" cssClass="buttonfinal" value="SUBMIT" id="buttonSubmit" name="buttonSubmit" method="retenderSave"/>
		<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" 
			onclick="confirmClose('<s:text name='setStatus.close.confirm'/>');"/>
	</s:else>
</div>
</s:form>
</body>
</html>
