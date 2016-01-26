<!--  #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------  -->
<%@ page language="java"  import="org.egov.infstr.utils.EGovConfig"%>
<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<META http-equiv=pragma content=no-cache>

<!--<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen, print" />-->
<link rel="stylesheet" href="../css/egov.css" type="text/css">

<!--
<link rel=stylesheet href="../exility/global.css" type="text/css">
-->
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
<script language="javascript" src="../resources/javascript/jsCommonMethods.js"></script>
<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js" type="text/javascript" ></SCRIPT>
<script language="javascript" src="../resources/javascript/ajaxCommonFunctions.js"></script>
<!--
<link rel="stylesheet" href="../exility/screen.css" type="text/css" media="screen, print" />
-->

<%!  String B2ScheduleMajorCode=null; %>
<%!  String subGLcodes=null; %>
<% 	B2ScheduleMajorCode= EGovConfig.getProperty("egf_config.xml","majorcode","null","ScheduleB-02");
 	subGLcodes= EGovConfig.getProperty("egf_config.xml","subGLcodes","null","ScheduleB-02");
 %>


<SCRIPT LANGUAGE="javascript">
  var sameWindow=0;
var B2ScheduleMajorCode="<%=B2ScheduleMajorCode%>";
var subGLcodes="<%= subGLcodes %>";
  function loadData()
  {
       sm=PageManager.DataService.getQueryField("showMode");
       document.getElementById("lblsubType").style.display="none";
       subRep=document.getElementById("repSubType");
       subRep.removeAttribute('exilMustEnter');
	   subRep.style.display="none";
	   document.getElementById("description").readOnly=true;
       if(sm && (sm.toLowerCase()=='edit' || sm.toLowerCase()=='view') )
       {
       	/*       Hiding Cancel and Save&New Buttons     Starts Here        */
         document.getElementById("cancelI1").style.display="none";
		 //document.getElementById("cancelI2").style.display="none";
		// document.getElementById("cancelI3").style.display="none";
		// document.getElementById("cancelI4").style.display="none";
		 document.getElementById("savnewI1").style.display="none";
		 //document.getElementById("savnewI2").style.display="none";
		 //document.getElementById("savnewI3").style.display="none";
		 //document.getElementById("savnewI4").style.display="none";
		/*       Hiding Cancel and Save&New Buttons    Ends Here         */
		/*    Disabling Schedule Definition Starts Here    */
		 
		 document.getElementById("description").readOnly=true;
		 document.getElementById("repType").disabled=true;
		 document.getElementById("repSubType").disabled=true;
		 document.getElementById("isRemission").disabled=true;
		 document.getElementById("repLineItem").disabled=true;
		 /*    Disabling Schedule Definition Ends Here    */
		 
		 
         var schedId=PageManager.DataService.getQueryField("schId");
         document.getElementById("schId").value=schedId;
         var report=PageManager.DataService.getQueryField("rep");
        
	        if(sm && sm.toLowerCase()=='view')
		   {
		  	 document.getElementById("schNumber").readOnly=true;
		 	 document.getElementById("schName").readOnly=true;
		     document.getElementById("mode").value="view";
		     document.getElementById("view").style.display="block";
		     document.getElementById("editable").style.display="none";
		   }
		   else
		   {
	         document.getElementById("mode").value="edit";
	       }
	       PageManager.DataService.setQueryField("schId",schedId);
	       
	       var schMajorCode ='<%=request.getParameter("scheduleB2MajorCode")%>';
	     
	       if(sm && sm.toLowerCase()=='edit' && schMajorCode && B2ScheduleMajorCode !="null" && schMajorCode ==B2ScheduleMajorCode)
	       {
	        document.getElementById("repLineItem").options[0]=new Option(schMajorCode,schMajorCode);
	      	PageManager.DataService.setQueryField("glcode",schMajorCode);
	      	PageManager.DataService.callDataService("getAccountCodeDescription");
	      	
	      	PageManager.DataService.callDataService("editScheduleB2");
	      	loadSelectData('../commonyui/egov/loadComboAjax.jsp', "chartofaccounts", "id||'  ~  '||name", "glcode", " glcode in("+subGLcodes+")", 'dummy', 'accnt_Code');
	      	
	       } 
	       else
	       
	       PageManager.DataService.callDataService("editSchedule");
	       
       }
       
	   else
	   {
		   document.getElementById("lblsubType").style.display="none";
	       document.getElementById("repSubType").style.display="none";
	   }
   /* document.getElementById("userId").value=PageManager.DataService.getQueryField("current_UserID");
    bootbox.alert(document.getElementById("userId").value);
    */
  }
  function setLists()
  {
    var acctCode=document.getElementById("accnt_Code");
    acctCode.setAttribute('exilListSource','getAccountCode');
	PageManager.DataService.callDataService("getScheduleLineItem");
  }
  function onClickCancel()
  {
	window.location="ScheduleMaster.jsp?";
  }
  function getRepLineItem(obj)
  {
    //bootbox.alert(obj.options[obj.selectedIndex].value);
    if(!validateCombo("repType"))
    {
    	bootbox.alert("Select any one of Report Type Options");
       return;
    }
    //On change of report type Clening up all the fields
    clearCombo(document.getElementById("repLineItem"));
    document.getElementById("description").value="";
    removeGrid(document.getElementById('schLineItem'),0);
    clearCombo(document.getElementById("accnt_Code"));
    document.getElementById("itemDesc").value="";
     //------ cleaning ends here  ------ 
    var report=obj.options[obj.selectedIndex].value;
    var type="";
   
    if(report=='BS')
      type="('A','L')";
    else if(report=='IE' || report=='FIE')
       type="('I','E')";
    else if(report=='RP')
    {
       document.getElementById("lblsubType").style.display="block";
       subRep=document.getElementById("repSubType");
       subRep.setAttribute("exilMustEnter","true");
       subRep.style.display="block";
	   type="('I','E','A','L')";
	}
	var lineItem=document.getElementById("repLineItem");
	lineItem.setAttribute('exilListSource','getReportLineItem');
    PageManager.DataService.setQueryField('type',type);
    PageManager.DataService.callDataService('getReportLineItem');
  }
  function getAcctCode(obj)
  {
     if(!obj) return;
      //On change of report type Clening up all the fields
    document.getElementById("description").value="";
    removeGrid(document.getElementById('schLineItem'),0);
    clearCombo(document.getElementById("accnt_Code"));
    document.getElementById("itemDesc").value="";
     //------ cleaning ends here  ------ 

     var code=obj.options[obj.selectedIndex].value;
     var acctCode=document.getElementById("accnt_Code");
     acctCode.setAttribute('exilListSource','getAccountCode');
     PageManager.DataService.setQueryField('parent',code);
     var type=document.getElementById('repType');
     var report=type.options[type.selectedIndex].value;
     if(B2ScheduleMajorCode!="null" && report=='BS' && obj.options[obj.selectedIndex].text==B2ScheduleMajorCode)
     	{
     	
     	if(subGLcodes=="null") { bootbox.alert("property  subGLcodes doesn't exists in config file"); return;}
     	loadSelectData('../commonyui/egov/loadComboAjax.jsp', "chartofaccounts", "id||'  ~  '||name", "glcode", " glcode in("+subGLcodes+")", 'repLineItem', 'accnt_Code');
     	var repLineItemObjvalue=document.getElementById('repLineItem').options[document.getElementById('repLineItem').selectedIndex].text;
     	PageManager.DataService.setQueryField("glcode",repLineItemObjvalue);
	      	PageManager.DataService.callDataService("getAccountCodeDescription");
     	}
     else
     PageManager.DataService.callDataService('getAccountCode');
  }
  function validateCombo(name)
  {
 	 var obj=document.getElementById(name);
     var idx=obj.selectedIndex;
     var val=obj.options[idx].value;
     if(val.toLowerCase()=="choose")
       return false;
     return true;
  }
  function afterUpdateService()
  {
	if(sameWindow == 1)
	{
	  window.location="ScheduleMaster.jsp";
    }
    else
    {
	  window.close();
	}
	sameWindow=0;
	return true;

  }
  function getDesc(obj)
  {
	var Rtn=PageManager.DataService.getControlInBranch(obj.parentNode,'accnt_Code');
	var sRtn=Rtn.value;
	var row=PageManager.DataService.getRow(obj);
	var curRow=PageManager.DataService.getRow(obj);
	a = sRtn.split("  ~  ");
	var desc =PageManager.DataService.getControlInBranch(row,'itemDesc');
	
	desc.value = a[1];
  }
  function clearCombo(obj)
  {
	 var accCtrl=obj
	 for(var i=accCtrl.options.length-1;i>=0;i--){
		accCtrl.remove(i);}
  }

  /**
   idx=0 refers delete all rows except 1
   idx>0 refers delete last idx number of rows
  **/
  function removeGrid(tableObj,idx)
  {
	//var tableObj=document.getElementById('schLineItem');
	if(idx>0)
	{
	  if(idx>(tableObj.rows.length-1))//if ino. of rows specified to delete are greater than existing deletes all rows but 1
	    idx=tableObj.rows.length-1;
	  else
	    idx=tableObj.rows.length-idx-1;
	}
	else
	  idx=1;
	for(var i=tableObj.rows.length-1;i>idx;i--)
	{
		tableObj.deleteRow(i);
	}
  } 
  function addGrid(table)
  {
  		child=table.firstChild;
	    newRow=child.rows[1].cloneNode(true);
	    child.appendChild(newRow);
  }
  function alterGrid(TableId)
  {
     var e = window.event;
	if(e.keyCode == 113 ) 
	{
		var index, inIndex, rowLength;
		var table = document.getElementById(TableId);
		if(!table || !table.rows) return false;
		var accntLength=document.getElementById("accnt_Code").options.length;
		//bootbox.alert(table.rows.length);			
		if(table.rows.length>accntLength)
		{
			bootbox.alert("No more Account Codes to select!...");
		  return;
		}
	    //addGrid(table);
	    PageManager.DataService.addNewRow(TableId);
	    PageManager.DataService.getControlInBranch(table.rows[table.rows.length-1],'itemDesc').value="";
    }
    else if(e.keyCode == 46 ) 
	{
		var index, inIndex, rowLength;
		var table = document.getElementById(TableId);
		if(!table || !table.rows) return false;	
		var schLineItemGrid=document.getElementById('schLineItem');
		if(schLineItemGrid.rows.length<=2) { bootbox.alert('There should be atleast one line item'); return false;}
		removeGrid(document.getElementById(TableId),1);
	}
  }
  function afterRefreshPage(dc)
  {
   	if(dc.values['serviceID']=='editSchedule' || dc.values['serviceID']=='editScheduleB2')
  	{
  	  PageManager.DataService.setQueryField("parent",dc.values['parentId']);
  	  PageManager.DataService.setQueryField("schId",dc.values['schId']);
  	  var oper="operation";
  	  var report=dc.values['repType'];
  	  var isRemission=dc.values['isRemission'];
  	  if(report=='RP')
	  {
	       document.getElementById("lblsubType").style.display="block";
	       document.getElementById("repSubType").style.display="block";
	       if(dc.values['repSubType']=='ROP' || dc.values['repSubType']=='RNOP')
	         oper="receiptoperation";
	       else
	         oper="paymentoperation";       
	       
	       if(isRemission==1)
	       {
	       		document.getElementById("remissionRow").style.display="block";
	       		document.getElementById("isRemission").checked=true;
	       		document.getElementById("isRemission").value=1;
	       		getAllAcctCodes(document.getElementById("isRemission"));
	       }
	  }else if(report=='FIE')
	  {
	   oper="fieoperation";
	  }
	  
	  PageManager.DataService.setQueryField("operation1",oper);	
	  setLists();	
  	}
  	if(dc.values['serviceID']=='getScheduleLineItem')
  	{
  	  setCorrectData();
  	}
  }
  function setCorrectData()
  {
    
  }
  function beforeRefreshPage(dc)
  {
    if(dc.values['serviceID']=='getScheduleLineItem'){
    	if(dc.values['showMode']=='edit' && dc.values['scheduleB2MajorCode']==B2ScheduleMajorCode)
    		dc.grids['getAccountCode']=null;
    }
    
  }
  function ButtonPress(name)
  {
    if(!validateCombo("repType"))
    {
    	bootbox.alert("Select any one of Report Type Options");
      return;
    }
    var repType=document.getElementById("repType")
    if(repType.options[repType.selectedIndex].value=="RP")
    {
	    if(!validateCombo("repSubType"))
	    {
	    	bootbox.alert("Select any one of Sub-Report Type Options");
	      return;
	    }
	}
	if(!chkAccountCode())
	{
		bootbox.alert('Duplicate Account Codes are not allowed\n \t\tor \n Delete empty rows if exists by pressing "Delete" button');
		return false;
	}
	if(!chkAccountCodeRowEmpty())
	{
		bootbox.alert('Account Code can not be empty\n \t\tor \n Delete empty rows if exists by pressing "Delete" button');
		return false;
	}

    if(name.toLowerCase()=='savenew')
	   sameWindow=1;
	if(!PageValidator.validateForm())
	   return false;
	if(document.getElementById("mode").value=='edit')
	{
	   document.getElementById("repType").disabled=false;
	   document.getElementById("repSubType").disabled=false;
	   document.getElementById("repLineItem").disabled=false;
	   document.getElementById("isRemission").disabled=false;
	}
	//bootbox.alert("submit");
	PageManager.UpdateService.submitForm('scheduleMaster');
	//bootbox.alert("submited");
	
  }
  function chkAccountCode()
  {
	   table=document.getElementById('schLineItem');
	   for(var i=1;i<table.rows.length;i++)
	   {

	  	 // rowObj=PageManager.DataService.getControlInBranch(table.rows[i]);
	  	  accNo=PageManager.DataService.getControlInBranch(table.rows[i],'accnt_Code');
	  	 // bootbox.alert("Acc no:"+accNo+"**"+accNo.value);
	 	 accNo=accNo.value;
	  	 accNo=accNo.toLowerCase();
	  	  for(var j=i+1;j<table.rows.length;j++)
	  	  {
		 	 var accNo2=PageManager.DataService.getControlInBranch(table.rows[j],'accnt_Code');
			 if(accNo2.length>0)
			 {
			 	accNo2=accNo2.value;
			 	accNo2=accNo2.toLowerCase();
			 	if((accNo==accNo2) && (accNo!=null || accNo.value!=""))
		  	 	       return false;
		  	 }
	
	      }
  }
	  return true;

  }
 function chkAccountCodeRowEmpty()
 {
  	   table=document.getElementById('schLineItem');
  	   for(var i=1;i<table.rows.length;i++)
  	   {
    	  	  accNo=PageManager.DataService.getControlInBranch(table.rows[i],'accnt_Code');
  	  	  accNo=accNo.value;
    	  	//bootbox.alert("accNo----->"+accNo);
  	  	  if(!accNo!="")
  	  	  return false;
  		  
    	  }
  	  return true;
  
  }
  
function getRemission(obj)
{
	if(obj.value=="POP")
	{		
		document.getElementById("remissionRow").style.display="block";
	}
	else
	{
		document.getElementById("remissionRow").style.display="none";
	}
}

function getAllAcctCodes(obj)
{
	if(obj.checked==true)
	{
		document.getElementById("isRemission").value=1;
		document.getElementById("repLineItem").value='';
		document.getElementById("description").value='';
		document.getElementById("repLineItem").disabled=true;
		document.getElementById("description").disabled=true;
		var acctCode=document.getElementById("accnt_Code");
	    acctCode.setAttribute('exilListSource','getAccountCode');
		PageManager.DataService.callDataService('getAllAccountCode');
	}
	else
	{
		document.getElementById("isRemission").value='';
		clearCombo(document.getElementById("accnt_Code"));
		document.getElementById("repLineItem").disabled=false;
		document.getElementById("description").disabled=false;
	}
}

</SCRIPT>
<title>ScheduleMaster</title>
</head>
<body onload="loadData()" >
<table><tr><td><div id="main"><div id="m2"><div id="m3">		 
<form name="ScheduleMaster"  method = "post">
<div>
<input type="hidden" id="mode" name="mode" value="insert">
<input type="hidden" id="schId" name="schId">
<input type="hidden" id="dummy" name="dummy" value="dummy">
<table width="100%" border=0 cellpadding="3" cellspacing="0">
<tr >

		<td colspan="6" class="tableheader" valign="center" colspan="4" width="100%"><span id="screenName" class="">Schedule Definition </span></td>
	</tr>
	<tr>
				<td width="19%" ><div align="right" valign="center" class="labelcell">
					Report Type<span class="leadon">*</span></div></td>
				<td width="19%" >
					<span class="smallfieldcell"><SELECT class="combowidth1" id="repType" name="repType" exilMustEnter="true"  onchange="getRepLineItem(this);">
						<option value="Choose">--Choose--</option>
						<option value="BS" >Balance Sheet</option>
						<option value="IE" >Income/Expenditure Statement</option>
						<option value="RP" >Receipts/Payment Statement</option>
						<option value="FIE" >Functionwise Income/Expenditure Statement</option>
					</SELECT></span>
				</td >
				<td width="18%"><div id="lblsubType" align="right" valign="center" class="labelcell">
					Sub-Report Type<span class="leadon">*</span></div></td>
				<td width="35%">
				   <span class="smallfieldcell"><SELECT class="combowidth1" id="repSubType" name="repSubType"  onchange="getRemission(this)">
				  		<option value="Choose">--Choose--</option>
						<option value="ROP" >Operating Receipt</option>
						<option value="RNOP" >Non-Operating Receipt</option>
						<option value="POP" >Operating Payment</option>
						<option value="PNOP" >Non-Operating Payment</option>
					</SELECT></span>					
				</td>	
		</tr>
	<tr  id="remissionRow"  style="display:none">
	<td></td>
	<td></td>
	<td></td>
	<td class="labelcell">Is Remission<input type="checkbox" id="isRemission" name="isRemission" value="1" onclick="getAllAcctCodes(this)"> </td>
	</tr>
		
	<tr>
		<td ><div align="right" valign="center" class="labelcell">
			Report Line Item</div></td>
		<td ><span class="smallfieldcell"><SELECT class="combowidth1" id="repLineItem" name="repLineItem"  onchange="getAcctCode(this)"></SELECT></span></td>
		<td ><div align="right" valign="center" class="labelcell">Description</div></td>
		<td ><span class="fieldcell">
		<input ID="description" NAME="description" class="fieldcellwithinputwide" style="width:280px"  size="34"></span></td>
	</tr>
	<tr height="5">
	    <td ><div align="right" valign="center" class="labelcell">
					Schedule No.<span class="leadon">*</span></div></td>
		<td ><span class="fieldcell"><input class="fieldcell" id="schNumber" name="schNumber" exilMustEnter="true" exilListSource="" ></span></SELECT></td>
		<td><div align="right" valign="center" class="labelcell">Schedule Name<span class="leadon">*</span></div></td>
		<td><span class="fieldcell"><input class="fieldcell" id="schName" name="schName" exilMustEnter="true" exilListSource="" ></span></SELECT></td>
	</tr>
	<tr  height="5">
	   <td ><div align="right" valign="center" class="normaltext">&nbsp;</div></td>
	   <td >&nbsp;</td>
	  <td>&nbsp;</td><td>&nbsp;</td></tr>
	  <tr >
		<td colspan="6" class="tableheader" valign="center" colspan="4" width="100%"><span id="screenName" class="">Schedule Line Items </span></td>
	</tr>
	<tr>
	   <td colspan="6">
	      <table width="100%" border="1" cellpadding="0" cellspacing="0" name="schLineItem" ID="schLineItem">
	       	<TBODY>
				<tr class="thStlyle">
					<td width="27%"><div align="center" class="">Account Code<span class="leadon">*</span></div></div></td>
					<td width="30%"><div align="center" class="">Description</div></div></td>
					<td width="12%"><div align="center" class="">Operation<span class="leadon">*</span></div></div></td>
				 </tr>
				 <tr class="smallfieldcell">
				   <td ><span>
						     <select ID="accnt_Code" NAME="accnt_Code" class="combowidth1" MaxLength="25" style="WIDTH: 251; height:21"  onchange="getDesc(this)"></select>
			 		   </span>
				   </td>
				   <td width="30%"><span>
						     <input size=25 ID="itemDesc" NAME="itemDesc" class="" MaxLength="15" style="WIDTH: 411; HEIGHT: 22" ></span> </td>
				   <td><span>
			 				<select ID="operation" NAME="operation" class="combowidth" MaxLength="25" style="WIDTH: 106; height:21" exilMustEnter="true" exilDataType="exilAlphaNumeric" onKeyDown="alterGrid('schLineItem')">
				 				<option value="A">Add</option>
				 				<option value="L">Less</option>
			 				</select>
			 		   </span>
			 	   </td>
			</tr>
          </TBODY>
         </table>
	   </td>
	</tr>
	<tr >
		<td colspan="6" align="middle">
		<!-- Button Starts-->
		<table border="0" cellpadding="0" cellspacing="0" name="editable" id="editable">
		<!--<tr>
			<td align="right" id="savnewI1"><IMG height=18 src="/egi/resources/erp2/images/Button_leftside.gif" width=7></td>
			<td bgcolor="#fe0000" valign="center" nowrap id="savnewI2"><A class=buttonprimary onclick="ButtonPress('savenew')" href="#">Save & New</A></td>
			<td id="savnewI3"><IMG height=18 src="/egi/resources/erp2/images/Button_rightside.gif" width=7></td>
			<td id="savnewI4"><IMG src="/egi/resources/erp2/images/spacer.gif" width=8></td>
			
			<td align="right"><IMG height=18 src="/egi/resources/erp2/images/Button_leftside.gif" width=6></td>
			<td bgcolor="#fe0000" valign="center" nowrap ><A class=buttonprimary onclick="ButtonPress('saveclose');" href="#">Save & Close</A></td>
			<td><IMG height=18 src="/egi/resources/erp2/images/Button_rightside.gif" width=6></td>						
			<td><IMG src="/egi/resources/erp2/images/spacer.gif" width=8></td>
			
			<td align="right" id="cancelI1"><IMG height=18 src="/egi/resources/erp2/images/Button_leftside.gif" width=6></td>
			<td bgcolor="#fe0000" valign="center" nowrap id="cancelI2" ><A class=buttonprimary onclick=onClickCancel() href="#">Cancel</A></td>
			<td id="cancelI3"><IMG height=18 src="/egi/resources/erp2/images/Button_rightside.gif" width=6></td>
			<td id="cancelI4"><IMG src="/egi/resources/erp2/images/spacer.gif" width=8></td>
			
			<td align="right"><IMG height=18 src="/egi/resources/erp2/images/Button_leftside.gif" width=8></td>
			<td bgcolor="#fe0000" valign="center" nowrap ><A class=buttonprimary onclick=window.close() href="#">Close</A></td>
			<td><IMG height=18 src="/egi/resources/erp2/images/Button_rightside.gif" width=8></td>
			<td><IMG src="/egi/resources/erp2/images/spacer.gif" width=8></td>
		</tr>-->
		<tr>
				<td width=100% align=center>
						<input type="button" class="button" id="savnewI1" value="Save & New" onclick="ButtonPress('savenew');" />
						<input type="button" class="button" value="Save & Close" onclick="ButtonPress('saveclose');" />
						<input id="cancelI1"type="button" class="button" value="Cancel" onclick="onClickCancel();" />
						<input type="button" class="button" value="Close" onclick="window.close()" />
				</td>
		</tr>

		 </table>  
		 <table border="0" cellpadding="0" cellspacing="0" name="view" id="view" style="Display:none">
		 <!-- <tr>
			<td align="right"><IMG height=18 src="/egi/resources/erp2/images/Button_leftside.gif" width=8></td>
			<td bgcolor="#fe0000" valign="center" nowrap ><A class=buttonprimary onclick=window.close() href="#">Close</A></td>
			<td><IMG height=18 src="/egi/resources/erp2/images/Button_rightside.gif" width=8></td>
			<td><IMG src="/egi/resources/erp2/images/spacer.gif" width=8></td>
		 </tr>-->
		 <tr>
				<td width=100% align=center>
						<input type="button" class="button"  value="Close" onclick=window.close() ;" />
				</td>
				</tr>		
		</table>
		 <!-- Button ends-->
		 </td>
	</tr>
</table>

</form>
</div></div></div></td></tr></table>
</body>
</html>
