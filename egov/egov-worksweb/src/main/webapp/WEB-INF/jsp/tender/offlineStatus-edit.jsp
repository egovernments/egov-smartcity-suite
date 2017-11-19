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
	
	<s:text name="set.offline.status" />
	
	</title>
</head>
<body onload="hideDataBasedOnMode();">
<script src="<egov:url path='resources/js/works.js?${app_release_no}'/>"></script>
<script type="text/javascript">
jQuery("#loadingMask").remove();
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
				document.getElementsByName('statusDate')[i].setAttribute("id","statusDate["+i+"]");
	  			document.getElementsByName('dateHref')[i].setAttribute("id","dateHref["+i+"]");
	  			document.getElementsByName('dateHref')[i].setAttribute("href","javascript:show_calendar("+"\"getElementById('statusDate["+i+"]')"+"\")");
			}
	 	 }
	 	 else{
	 	 	document.forms[0].slNo.value=1;
	 	 	document.getElementsByName('statusDate')[rows].setAttribute("id","statusDate["+rows+"]");
	  		document.getElementsByName('dateHref')[rows].setAttribute("id","dateHref["+rows+"]");
	  		document.getElementsByName('dateHref')[rows].setAttribute("href","javascript:show_calendar("+"\"getElementById('statusDate["+rows+"]')"+"\")");
	 	 }
	  }
	  if(document.forms[0].statusName.length==len)
     	 dom.get('statusrow').style.display='none';
      else
  	  	  dom.get('statusrow').style.display='';
  	  
  	  validateSubmit(false);
   }
}
function addGrid(tableId,trId)
{
 if(validateSubmit(false)){
   var tbl = dom.get(tableId);
   var rowObj = dom.get(trId).cloneNode(true);
   addRow(tbl,rowObj);
   var lastRow = tbl.rows.length;
   var rows = parseInt(tbl.rows.length)-2;
   document.getElementsByName('statusDate')[rows].value='';
   document.getElementsByName('statusDate')[rows].disabled=false;
   document.getElementsByName('statusDate')[rows].setAttribute("id","statusDate["+rows+"]");
   document.getElementsByName('dateHref')[rows].setAttribute("id","dateHref["+rows+"]");
   document.getElementsByName('dateHref')[rows].setAttribute("href","javascript:show_calendar("+"\"getElementById('statusDate["+rows+"]')"+"\")");
   document.getElementsByName('dateHref')[rows].style.visibility='visible';
   document.getElementsByName('delHref')[rows].style.visibility='visible';
   document.forms[0].statusName[lastRow-2].value=-1;
   document.forms[0].statusName[lastRow-2].disabled=false;
   document.forms[0].slNo[lastRow-2].value=eval(lastRow-document.forms[0].slNo[lastRow-2].value);
   if(document.forms[0].statusName[lastRow-2].length==lastRow)
      dom.get('statusrow').style.display='none';
   }
  
}
 function addRow(tableObj,rowObj)
 {
  	var tbody=tableObj.tBodies[0];
	tbody.appendChild(rowObj);
 }
 function validateSubmit(flag)
 {
     var tbl = dom.get('statustable');
     var lastRow = tbl.rows.length;
     if(lastRow>2)
     {
        for(var i=1;i<lastRow;i++)
        {
        	if(getControlInBranch(tbl.rows[i],'statusName').value==-1)
        	{
        		 dom.get("ws_error").innerHTML='<s:text name="ws.status.is.null"/>'; 
        		 dom.get("ws_error").style.display='';
		 		 return false;
        	}
        	if(dom.get('statusDate['+eval(i-1)+']').value=="")
        	{
        		 dom.get("ws_error").innerHTML='<s:text name="ws.statusDate.is.null"/>'; 
         		 dom.get("ws_error").style.display='';
		 		 return false;
        	}else{
        	   if(!isvalidFormat(dom.get('statusDate['+eval(i-1)+']')))return false;
        	 }
        }
     }
     else{
        if(dom.get('status').value==-1)
       	{
       		 dom.get("ws_error").innerHTML='<s:text name="ws.status.is.null"/>';  
       		 dom.get("ws_error").style.display='';
	 		 return false;
       	}
       	if(dom.get('statusDate[0]').value=="")
       	{
       		 dom.get("ws_error").innerHTML='<s:text name="ws.statusDate.is.null"/>'; 
        	 dom.get("ws_error").style.display='';
	 		 return false;
       	}else {
       	  if(!isvalidFormat(dom.get('statusDate[0]')))return false;
       	}
     }
     for(var i=1;i<lastRow;i++){
      	for(var j=2;j<lastRow;j++){
      		if(i!=j && getControlInBranch(tbl.rows[j],'statusName')!=null && 
        		getControlInBranch(tbl.rows[i],'statusName').value==getControlInBranch(tbl.rows[j],'statusName').value){
        		dom.get("ws_error").innerHTML='The Status '+getControlInBranch(tbl.rows[i],'statusName').options[getControlInBranch(tbl.rows[i],'statusName').selectedIndex].text+' is set multiple times'; 
         		dom.get("ws_error").style.display='';
		 		return false;
        	}
      	}
      }
       dom.get("ws_error").style.display='none';
	   dom.get("ws_error").innerHTML='';
	   if(flag){
		   toggleTableDetail(tbl,lastRow,false,false);
		   jQuery(".commontopyellowbg").prepend('<div id="loadingMask" style="display:none;overflow:none;scroll:none;" ><img src="/egi/images/bar_loader.gif"> <span id="message">Please wait....</span></div>')
		   doLoadingMask();
	   }
	   return true;
 }
 function formCalendarRef(type,rowNo)
 {
	 show_calendar("getElementById('statusDate["+rowNo+"]')");
 }
 function hideDataBasedOnMode()
 {
 	var tbl = dom.get('statustable');
 	var rows = parseInt(tbl.rows.length)-1;
 	if(rows>0){
 	  for(var i=0;i<rows;i++){
 	    document.getElementsByName('statusDate')[i].setAttribute("id","statusDate["+i+"]");
	    document.getElementsByName('dateHref')[i].setAttribute("id","dateHref["+i+"]");
	    document.getElementsByName('dateHref')[i].setAttribute("href","javascript:show_calendar("+"\"getElementById('statusDate["+i+"]')"+"\")");
	   }
	}
	else{
		document.getElementsByName('statusDate')[0].setAttribute("id","statusDate["+0+"]");
	    document.getElementsByName('dateHref')[0].setAttribute("id","dateHref["+0+"]");
	    document.getElementsByName('dateHref')[0].setAttribute("href","javascript:show_calendar("+"\"getElementById('statusDate["+0+"]')"+"\")");	
	}
	if(getControlInBranch(tbl.rows[rows],'statusName').length==tbl.rows.length){
      dom.get('statusrow').style.display='none';
    }
	dom.get('delHref').style.visibility='hidden';
 	<s:if test="%{setStatusList!=null && !setStatusList.isEmpty()}">
 	 var lastRow = '<s:property value="%{setStatusList.size()}"/>';
 	 var statList = '<s:property value="%{dropdownData.statusList.size()}"/>';
 	 if(lastRow==statList)dom.get('buttonSubmit').style.display='none';
     if(getRequestParamsInURL('setStatus')!='view')toggleTableDetail(tbl,eval(parseInt(lastRow)+1),true,true);
 	</s:if>	
 	 if(getRequestParamsInURL('setStatus')=='view'){
 	    dom.get('statusrow').style.display='none';
 		dom.get('buttonSubmit').style.display='none';
 		toggleTableDetail(tbl,tbl.rows.length,true,true);
 		dom.get('heading').innerHTML = 'View Set Status'
 	 }
 	 <s:if test="%{objectType=='TenderResponseContractors'}">	 
 	 	dom.get('contName').innerHTML=unescape(getRequestParamsInURL('contractorName'));
 	 </s:if>
  }
 
 function toggleTableDetail(tbl,lastRow,flag,req)
 {
     if(lastRow>2){
        for(var i=1;i<lastRow;i++){
            getControlInBranch(tbl.rows[i],'statusName').disabled=flag;
        	dom.get('statusDate['+eval(i-1)+']').disabled=flag;
        	if(req){
        	  getControlInBranch(tbl.rows[i],'dateHref').style.visibility='hidden';
        	  getControlInBranch(tbl.rows[i],'delHref').style.visibility='hidden';
        	}
        }
     }
     else{
        dom.get('status').disabled=flag;
        dom.get('statusDate[0]').disabled=flag;
    	if(req){
    	 dom.get('dateHref[0]').style.visibility='hidden';
    	 dom.get('delHref').style.visibility='hidden';
        }
     }
 }
 function isvalidFormat(obj)
 {
 	if(!checkDateFormat(obj)){
 		dom.get("ws_error").innerHTML='<s:text name="invalid.fieldvalue.statusDate"/>'; 
        dom.get("ws_error").style.display='';
	 	return false;
 	}
 	dom.get("ws_error").style.display='none';
	dom.get("ws_error").innerHTML='';
	return true;
 }
</script>
<div class="errorstyle" id="ws_error" style="display: none;"></div>
<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
<s:form name="setStatusForm" theme="simple" onsubmit="return validateSubmit(true);">
<s:token/>
	<div class="formmainbox"></div>
	<div class="insidecontent">
	<div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	<div class="rbcontent2">
	<s:hidden name="objectType" id="objectType" value="%{objectType}"></s:hidden>
	<s:hidden name="objId" id="objectId" value="%{objId}"></s:hidden>
	<s:hidden name="appDate" id="appDate" value="%{appDate}"></s:hidden>
	<s:hidden name="objNo" id="objNo" value="%{objNo}"></s:hidden>
	<s:hidden name="contractorName" id="contractorName" /> 
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
			<s:if test="%{objectType=='TenderResponseContractors'}">
			 <div>
			 	<s:date name="appDate" var="appDateFormat" format="dd/MM/yyyy"/>
	  			<b><s:property value="%{getText('objectType.tenderNegotiation')}"/>&nbsp;Approved On :&nbsp;<s:property value="%{appDateFormat}"/></b>
	  			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<b><s:property value="%{getText('objectType.tenderNegotiation')}"/>&nbsp;Number :&nbsp;<s:property value="%{objNo}"/></b>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<b><s:property value="%{getText('setStatus.contractorName')}"/>&nbsp;<span name="contName" id="contName"></span></b>
			</div>
			</s:if>
			<s:else>
				<b><s:property value="%{objectType}"/>&nbsp;&nbsp;Approved On :&nbsp;&nbsp;<s:property value="%{appDate}"/></b>
	  			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<b><s:property value="%{objectType}"/>&nbsp;&nbsp;Number :&nbsp;&nbsp;<s:property value="%{objNo}"/></b>
			</s:else> 
			</td>
			</tr>
			</table>
			</td>
		</tr>	
        <tr>
            <td colspan="4">
            <table  width="100%" border="0" cellspacing="0" cellpadding="0" id="statustable">
              <tr>
		   <td width="4%" class="tablesubheadwk">
			<s:text name="column.title.SLNo" />
		   </td>
		   <td width="20%" class="tablesubheadwk">
			 <span class="mandatory">*</span><s:text name="works.status"/>
			</td>
			<td width="20%" class="tablesubheadwk">
				<span class="mandatory">*</span><s:text name="status.date"/>
			</td>
			<td width="7%" class="tablesubheadwk">
				 <a id="statusrow" href="#" onclick="addGrid('statustable','detailsRow');return false;">
   		   						 <img border="0" alt="Add Status" src="/egworks/resources/erp2/images/add.png" /></a>
   		   			</td>
			</tr>
			<s:if test="%{!statusNameDetails.isEmpty()}">
			<s:iterator status="status" value="statusNameDetails">
			<tr id="detailsRow">
			<td width="4%" class="whitebox3wka">
				<input type="text" name="slNo" id="slNo" disabled="true" size="1" value='<s:property value="%{#status.count}"/>'/>
			</td>
			<td width="60%" class="whitebox3wka">
	  			<s:select name="statusName" id="status" list="dropdownData.statusList" listKey="code" listValue="description" headerKey="-1"  
				headerValue="%{getText('estimate.default.select')}" value='%{statusNameDetails[#status.index]}'/>
			</td>
			<td width="30%" class="whitebox3wka">
			<s:date name="statusDateDetails[#status.index]" var="statusDateFormat" format="dd/MM/yyyy" />
			<s:textfield name="statusDate" value="%{statusDateFormat}" size="25" id='statusDate[0]' cssClass="selectboldwk" 
        		 onfocus="javascript:vDateType='3';" 
        		 onkeyup="DateFormat(this,this.value,event,false,'3')"   onblur="isvalidFormat(this)"/>
        		 <a href="javascript:formCalendarRef('statusDate','0');" name="dateHref" id='dateHref[0]'
        		 onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;">
        		 <img src="/egworks/resources/erp2/images/calendar.png" id="wsDateImg" alt="Calendar" width="16" height="16" 
       			  border="0" align="absmiddle" /></a>
       		</td>
			<td width="7%" align="right" class="whitebox3wka" style="border-left-width: 0px">
       		<a id="delHref" name="delHref" href="#" onclick="deleterow(this)">
       		<img border="0" alt="Delete Estimates" src="/egworks/resources/erp2/images/cancel.png" /></a>
       		</td>
			</tr>
			</s:iterator>
			</s:if>
			<s:else>
			<tr id="detailsRow">
			<td width="4%" class="whitebox3wka">
				<input type="text" name="slNo" id="slNo" disabled="true" size="1" value="1"/>
			</td>
			<td width="60%" class="whitebox3wka">
	  			<s:select name="statusName" id="status" list="dropdownData.statusList" headerKey="-1"  listKey="code" listValue="description"
				headerValue="%{getText('estimate.default.select')}"/>
			</td>
			<td width="30%" class="whitebox3wka">
				<s:date name="model.statusDate" var="statusDateFormat" format="dd/MM/yyyy"/>
			<s:textfield name="statusDate" value="%{statusDateFormat}" size="25" id="statusDate[0]"  cssClass="selectboldwk" 
        		 onfocus="javascript:vDateType='3';" 
        		 onkeyup="DateFormat(this,this.value,event,false,'3')"  onblur="isvalidFormat(this)"/>
        		 <a href="javascript:formCalendarRef('statusDate','0');" id="dateHref[0]" name="dateHref"
        		 onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;">
        		 <img src="/egworks/resources/erp2/images/calendar.png" id="wsDateImg" alt="Calendar" width="16" height="16" 
       			  border="0" align="absmiddle" /></a>
			</td>
			<td width="7%" align="right" class="whitebox3wka" style="border-left-width: 0px">
       		<a id="delHref" name="delHref" href="#" onclick="deleterow(this)">
       		<img border="0" alt="Delete Estimates" src="/egworks/resources/erp2/images/cancel.png" /></a>
       		</td>
			</tr>
			</s:else>
			</table>
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
   <s:submit type="submit" cssClass="buttonfinal" value="SUBMIT" id="buttonSubmit" name="buttonSubmit" method="save"/>
   <input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" 
   onclick="confirmClose('<s:text name='setStatus.close.confirm'/>');"/>
</div>
</s:form>
</body>
</html>
