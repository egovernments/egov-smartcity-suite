<!--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  -->
<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<!-- <%@ page buffer = "16kb" %> --> 
<%@page  import="com.exilant.eGov.src.reports.LongAmountWrapper,com.exilant.eGov.src.reports.*,java.io.*,java.util.*,java.sql.*,javax.sql.*,javax.naming.InitialContext,com.exilant.GLEngine.*"%>
<%@page import="org.apache.log4j.Logger"%>
<html>
<head>
<title>General Ledger Report</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<!-- Inclusion of the CSS files that contains the styles -->

<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen, print" />

<!--
<link rel=stylesheet href="../exility/global.css" type="text/css">
-->
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js?rnd=${app_release_no}"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js?rnd=${app_release_no}"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js?rnd=${app_release_no}"></SCRIPT>
<script language="javascript" src="../resources/javascript/jsCommonMethods.js?rnd=${app_release_no}"></script>
<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js?rnd=${app_release_no}" type="text/javascript" ></SCRIPT>

<!--
<link rel="stylesheet" href="../exility/screen.css" type="text/css" media="screen, print" />
-->
<script type="text/javascript" src="../yui/build/yahoo/yahoo.js"></script>
<script type="text/javascript" src="../yui/build/dom/dom.js" ></script>
<script type="text/javascript" src="../yui/build/autocomplete/autocomplete-debug.js"></script>
<script type="text/javascript" src="../yui/build/event/event-debug.js"></script>
<script type="text/javascript" src="../yui/build/animation/animation.js"></script>

<link type="text/css" rel="stylesheet" href="../yui/build/reset/reset.css">
<link type="text/css" rel="stylesheet" href="../yui/build/fonts/fonts.css">
<link type="text/css" rel="stylesheet" href="../yui/examples/autocomplete/css/examples.css">


<style type="text/css">
	#codescontainer {position:absolute;left:11em;width:9%}
	#codescontainer .yui-ac-content {position:absolute;width:100%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
	#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:100%;background:#a0a0a0;z-index:9049;}
	#codescontainer ul {padding:5px 0;width:100%;}
	#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
	#codescontainer li.yui-ac-highlight {background:#ff0;}
	#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
</style>

<SCRIPT LANGUAGE="javascript">
var flag='0', tbNames, tbIds;
var rptType;
var rptTitle='', rptStartDate='', rptEndDate='', rptGLCode='', rptAccName='',fundNameQ='', detail='',accCode_1='',accCode_2='',snapShotDateTime,forRevEntry;
var acccodeArray;
var codeObj;

//onLoad

function onBodyLoad(){

PageValidator.addCalendars();
PageManager.ListService.callListService();
PageManager.DataService.callDataService('getTitle');

var beanVal= <%=request.getParameter("fromBean")%>;
if(beanVal==2)
{
	document.getElementById("row3").style.display="block";
	document.getElementById("row2").style.display="none";		
}

snapShotDateTime=PageManager.DataService.getQueryField('snapDT');
isDrillDown=PageManager.DataService.getQueryField('isDrillDown');
if(isDrillDown=="Y")

		 
if(PageManager.DataService.getQueryField('startDate') != null) 
{
	document.getElementById('startDate').value=PageManager.DataService.getQueryField('startDate');	
	//document.getElementById('endDate').value=PageManager.DataService.getQueryField('endDate');
}	
if(PageManager.DataService.getQueryField('endDate') != null)
{
	document.getElementById('endDate').value=PageManager.DataService.getQueryField('endDate');	
}
if(PageManager.DataService.getQueryField('glCode1') != null)
{
	document.getElementById('glCode1').value=PageManager.DataService.getQueryField('glCode1');	
}
if(PageManager.DataService.getQueryField('glCode2') != null)
{
	document.getElementById('glCode2').value=PageManager.DataService.getQueryField('glCode2');	
}
if(PageManager.DataService.getQueryField('fundName') != null)
{
	//bootbox.alert(PageManager.DataService.getQueryField('fundName'));	
}
//document.getElementById('titleUlb').value=PageManager.DataService.getQueryField('titleUlb');
//var x = document.getElementById('titleUlb').value;
//bootbox.alert(x);

var tempT=PageManager.DataService.getQueryField('endDate');	

if(isDrillDown=="Y")
{
document.getElementById('fromBean').value = 1;
document.getElementById('fund_id').style.display = 'none';
document.getElementById('mainTable').style.display = 'none';
}

if(document.getElementById('startDate').value && document.getElementById('endDate').value && document.getElementById('glCode1').value && document.getElementById('glCode2').value)
{
	if(isDrillDown!="Y")
		finHeader();
}

//document.getElementById('fundName').value=PageManager.DataService.getQueryField('fundNameQ');	

	/*
	 * The following code for account code implements Ajax autocomplete through YUI.
	 *
	 *
	 */
	       var type='getGLreportCodes';
            var url = "../commons/Process.jsp?type=" +type+ " ";
           	var req2 = initiateRequest();
            req2.onreadystatechange = function()
            {
	                  if (req2.readyState == 4)
	                  {
	                      if (req2.status == 200)
	                      {
	                      	var codes2=req2.responseText;
	                      	var a = codes2.split("^");
							var codes = a[0]; //bootbox.alert(acccodeArray);
							acccodeArray=codes.split("+");
							codeObj = new YAHOO.widget.DS_JSArray(acccodeArray);
	                      }
	                  }
	        };
	        req2.open("GET", url, true);
	        req2.send(null);

}

//based on key input displays the matching list of glcodes
		function autocompletecode(obj)
		{
			// set position of dropdown 
			var src = obj;	
			var target = document.getElementById('codescontainer');	
			var posSrc=findPos(src); 
			target.style.left=posSrc[0];	
			target.style.top=posSrc[1]+25;
			if(obj.name=='glCode1') target.style.left=posSrc[0]+150;

			target.style.width=500;	
				
			var currRow=PageManager.DataService.getRow(obj);
			var coaCodeObj = obj;//PageManager.DataService.getControlInBranch(currRow,'chartOfAccounts_glCodeearning');
			//40 --> Down arrow, 38 --> Up arrow
						if(event.keyCode != 40 )
						{
							if(event.keyCode != 38 )
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
			var currRow=PageManager.DataService.getRow(obj);
			neibrObj=PageManager.DataService.getControlInBranch(currRow,neibrObjName);
			var temp = obj.value; temp = temp.split("`-`");
			obj.value=temp[0]; 
			
			var s = obj.value.replace(/^(\s)*/, '');
		    obj.value = s.replace(/(\s)*$/, '');
		
			if(obj.value==null || obj.value=="") { neibrObj.value=""; return; }
			
			if(temp[1]==null && (neibrObj.value!='' || neibrObj.value!=null) ) {  return ;}
			else {
					temp=temp[1].split("-$-");
					neibrObj.value = temp[1];
				 }
		 }
		


function finHeader()
{
	var gLCode1 =document.getElementById('glCode1').value;
	//var x = document.getElementById('titleUlb').value;
	//bootbox.alert(x);
	var gLCode2 =document.getElementById('glCode2').value;
	//var title = document.getElementById('titleUlb').value;
	var title='';
	//var fObj = document.getElementById('fund_id');
	var fundHeader = PageManager.DataService.getQueryField('fundName');
	//bootbox.alert(title);
	rptAccName = 'FUND : '+ fundHeader;
	rptStartDate=document.getElementById('startDate').value;
	rptEndDate=document.getElementById('endDate').value;
	if(title != null) title='<font size=4><b>' + title;
	if(gLCode1==gLCode2)
	{
		rptTitle = '<font size=3><b>General Ledger Report for ' +gLCode1+ ' </b></font><br>' + rptAccName + '<br>' + rptStartDate + ' to ' + rptEndDate + detail;
	}
	else
	{
		rptTitle = '<font size=3><b>General Ledger Report from ' +gLCode1+ ' to ' +gLCode2+' </b></font><br>' + rptAccName + '<br>' + rptStartDate + ' to ' + rptEndDate + detail;
	}	
	document.getElementById('rptHeader').innerHTML = rptTitle;
	//document.getElementById('rptHeader').innerHTML = title + '</b></font><br>' + document.getElementById('rptHeader').innerHTML;
	document.getElementById('rptHeader').innerHTML = document.getElementById('rptHeader').innerHTML;
}
function buttonFlush()
{
	document.getElementById('glCode1').value='';
	document.getElementById('glCode2').value='';
	document.getElementById('startDate').value='';
	document.getElementById('endDate').value='';
	//bootbox.alert('XXXXX');
	PageManager.DataService.setQueryField('fundSource_id','');
	PageManager.DataService.setQueryField('fund_id','');
	//bootbox.alert(document.getElementById('reversedEntry').vlaue);
	//bootbox.alert(document.getElementById('glCode1').value);
}
function ButtonPress(){
	if (!PageValidator.validateForm()){
		document.getElementById('startDate').value='';
		document.getElementById('endDate').value='';
		return;
	}
	var strtDate = document.getElementById('startDate').value;
	rptStartDate = strtDate;
	//bootbox.alert(rptStartDate);
	var endDate = document.getElementById('endDate').value;
	rptEndDate = endDate;
	//bootbox.alert('HI');
	//accCode_1=document.getElementById('glCode1').value;
	//accCode_2=document.getElementById('glCode2').value;
	var strtDate = document.getElementById('startDate').value;
	rptStartDate = strtDate;
	//bootbox.alert(rptStartDate);
	var endDate = document.getElementById('endDate').value;
	rptEndDate = endDate;
	//bootbox.alert(rptEndDate);
	var aCode1 = document.getElementById('glCode1').value;
	accCode_1=aCode1;
	var aCode2 = document.getElementById('glCode2').value;
	accCode_2=aCode2;
	
	if(aCode1.length==0){
		bootbox.alert("please select  Account Code");
					
					return;
				 }
				 if(aCode2.length==0){
					 bootbox.alert("please select  Account Code");
				 					return;
				 }
	if(strtDate.length==0||endDate.length==0){
		bootbox.alert("please select start dates and end dates");
					return;
				 }

		if( compareDate(formatDate6(strtDate),formatDate6(endDate)) == -1 ){
			bootbox.alert('Start Date can not be greater than End Date');
		document.getElementById('startDate').value='';
		document.getElementById('endDate').value='';
		document.getElementById('startDate').focus();
		return false;
		}
	
	var fObj = document.getElementById('fund_id');
		var fsObj = document.getElementById('fundSource_id');
		if(fObj.selectedIndex<0)
		{
			bootbox.alert("please select fund");
		}
		if(fObj.selectedIndex>-1){
			PageManager.DataService.setQueryField('fund_id', fObj.options[fObj.selectedIndex].value);
			var fName=fObj.options[fObj.selectedIndex].text;
			
			//fundNameQ=fName;
			document.getElementById('fundName').value=fName;
			PageManager.DataService.setQueryField('fundName',fObj.options[fObj.selectedIndex].text);
			//bootbox.alert(fObj.options[fObj.selectedIndex].text);
		}

		if(fsObj.selectedIndex>-1){
			PageManager.DataService.setQueryField('fundSource_id', fsObj.options[fsObj.selectedIndex].value);
		}
	
		PageManager.DataService.setQueryField('glCode1',accCode_1);
		PageManager.DataService.setQueryField('glCode1',accCode_2);
		
		document.getElementById('fromBean').value = 1;
	  //document.getElementById('titleUlb').value=document.getElementById('title').value;
	   //bootbox.alert(document.getElementById('titleUlb').value);
	   //PageManager.DataService.setQueryField('titleUlb',document.getElementById('titleUlb').value);

	
}


function openSearch(obj,index){
	 var a = new Array(2);

		var screenName="report";
		var str="";

		if (obj.toLowerCase() == 'relation')
			str = "../HTML/Search.html?tableNameForCode="+obj;
		else if(obj.toLowerCase() == 'chartofaccounts'){
			if (rptType == "cb"){
				screenName="2";
				str = "Search.html?filterServiceID="+screenName;
			}
			else if(rptType == "bb"){
				//screenName="bankBook";
				screenName="13";
				str = "../HTML/Search.html?filterServiceID="+screenName;
			}else if(rptType == "sl"){
				str = "../HTML/Search.html?tableNameForCode=chartofaccounts_controlcodes";
			}else{
				str = "../HTML/Search.html?screenName="+screenName;
			}
		}
	 var sRtn = showModalDialog(str,"","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
     if (index==1){
		a = sRtn.split("`~`");
		var type = document.getElementById('glCode1');
		type.value = a[0];
		rptGLCode = a[0];
		//rptAccName = a[1];
		document.getElementById('chartOfAccounts_id1').value = a[2];
		//getDetailData(a[0]);
	}
	if (index==2){
			a = sRtn.split("`~`");
			var type = document.getElementById('glCode2');
			type.value = a[0];
			rptGLCode = a[0];
			//rptAccName = a[1];
			document.getElementById('chartOfAccounts_id2').value = a[2];
			//getDetailData(a[0]);
	}

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
	document.getElementById('fromBean').value ="2";	   
	document.forms[0].submit();	
}

//For print method
function buttonPrint()
{
	document.getElementById('fromBean').value ="2";
    var hide1,hide2; 
    hide1 = document.getElementById("tbl-header1");
	hide1.style.display = "none";     
	hide2 = document.getElementById("currentRowObject");
	//document.forms[0].submit();
  	if(window.print)
  		{
  		window.print();
	  	}
}

</SCRIPT>

</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onload="onBodyLoad()"><!------------------ Header Begins Begins--------------------->
 <jsp:useBean id = "glReportBean" scope ="request" class="com.exilant.eGov.src.reports.GeneralLedgerReportBean"/>
<jsp:setProperty name = "glReportBean" property="*"/>  


<form name="journalVoucher" action = "GeneralReport.jsp?startDate="+rptStartDate+"&endDate="+rptEndDate+"&glCode1="+glc+"&glCode2="+glc+"&fund_id="+fuid+"&snapShotDateTime="+snapShotDateTime method = "post">
<input type="hidden" name="fromBean" id="fromBean" value="0"> 
<input type="hidden" name="reportType" id="reportType" value="generalledger"> 
<div class="tbl-header1" style="width:1000px" id="tbl-header1">
<table width="100%" border=0 cellpadding="3" cellspacing="0" id="mainTable">
	<!-- row-0 -->
		<tr>
			<td class="rowheader" valign="center" width="100%" colspan="4"><span id="screenName" class="headerwhite2">Books of Accounts - General Ledger Report</span><span id="partyName" class="headerwhite2"></span></td>
		</tr>
		<tr class="row1" >		
		</tr>
		<tr class="row1">
					<td><div align="right" valign="center" class="normaltext" >From Account Code</div></td>
					<td>
						<input type="hidden" name="chartOfAccounts_id1" id="chartOfAccounts_id1">
						<input class="fieldinput" name="glCode1" id="glCode1"  autocomplete="off"   onkeyup="autocompletecode(this);" onblur="fillNeibrAfterSplit(this,'chartOfAccounts_id1');"><IMG id=IMG1 onclick="openSearch('chartofaccounts',1);" height=22 src="/egi/resources/erp2/images/plus1.gif" width=25 align=top border=0>
					</td>
					<td><div align="right" valign="center" class="normaltext" >To Account Code</div></td>
					<td>
						<input type="hidden" name="chartOfAccounts_id2" id="chartOfAccounts_id2">
						<input class="fieldinput" name="glCode2" id="glCode2"  onkeyup="autocompletecode(this);" onblur="fillNeibrAfterSplit(this,'chartOfAccounts_id2');"><IMG id=IMG1 onclick="openSearch('chartofaccounts',2);" height=22 src="/egi/resources/erp2/images/plus1.gif" width=25 align=top border=0>
					</td>

		</tr>
		<!-- row-3 -->
		<tr class="row2">
			<td><div align="right" valign="center" class="normaltext">Fund&nbsp;<span class="leadon">*</span></div></td>
			<td >
				<SELECT class="fieldinput" id=fund_id name="fund_id" exilListSource="fundNameList" exilMustEnter="true"></SELECT>
			</td>
			<td colspan=2>&nbsp;</td>
		</tr>
		<tr class="row2" style="DISPLAY: none">
		<td><div align="right" valign="center" class="normaltext">Financing Source</div></td>
			<td align="left">
				<SELECT class="fieldinput" id=fundSource_id name="fundSource_id" exilListSource="fundSourceNameList"></SELECT>
			</td>
			<td></td>
			<td></td>		
		</tr>
		<tr>
		      
		<td><INPUT TYPE="hidden" NAME="fundName"></td>
		<td><INPUT TYPE="hidden" NAME="title"></td>		
		<td><INPUT TYPE="hidden" NAME="titleUlb" value="<%= request.getParameter("title")%>" ></td>
		
		</tr>
		<tr class="row1" >
			<td><div align="right" valign="center" class="normaltext">Starting Date</div></td>
			<td align="left">
				<input name="startDate" id="startDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')" exilCalendar="true">
			</td>
			<td><div align="right" valign="center" class="normaltext" >End Date</div></td>
			<td>
				<input name="endDate" id="endDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')" exilCalendar="true">
			</td>

		</tr>
		<tr class="row2">
			<td><div align="right" valign="center" class="normaltext">Show Reversed Entry</div></td>
			<td ><input type="checkbox" name="forRevEntry" id="forRevEntry" ></td>
			<td><div align="right" valign="center" class="normaltext">Show Running Balance</div></td>
			<td ><input type="checkbox" name="forRunningBalance" id="forRunningBalance" ></td>
			
		</tr>
		<tr class="row2">
			<td colspan=4>
			</td>
		</tr>
		<tr class="row2" id="row2" name="row2">
			<td colspan="4" align="middle"><!-- Buttons Start Here -->
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td ></td><td></td><td></td><td ></td><td></td><td></td>
						<td ><div align="right" valign="center" class="normaltext" ><input type="submit" value = "Search" name ="submit" class="normaltext" onclick=ButtonPress()></td>
						<td ><div align="middle" valign="center" class="normaltext" ><CENTER><INPUT TYPE="submit" value = "Clear" name = "flush"class="normaltext" onclick=buttonFlush()></CENTER></td>
						<td ><div align="left" valign="center" class="normaltext" ><input type="submit" value = "Show All" name = "printView"class="normaltext" onclick=ButtonPress()></td>					
						<td ><div align="left" valign="center" class="normaltext" ><input type="submit" value = "Print Preview" name = "print" class="normaltext" onclick=buttonPrintPreview()></td>				
					
					<!-- Print Button start 
						<td align="right"><IMG height=16 src="/egi/resources/erp2/images/Button_second_leftside.gif" width=8></td>
						<td bgcolor="#ffffff" valign="center" nowrap background="/egi/resources/erp2/images/Button_second_middle.gif"><A class=buttonsecondary onclick="pageSetup();buttonPrint()" href="#">Print</A></td>
						<td><IMG height=16 src="/egi/resources/erp2/images/Button_second_rightside.gif" width=8></td>
						<td><IMG src="/egi/resources/erp2/images/spacer.gif" width=8></td>
					 Print end-->					
					
						<td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td ></td><td></td><td></td><td ></td><td></td><td></td>
					</tr>
				</table>
			</td><div id="codescontainer"></div>
		</tr>
		
		<tr style="DISPLAY: none" class="row3" id="row3" name="row3">
			<td colspan="4" align="middle"><!-- Buttons Start Here -->
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td ></td><td></td><td></td><td ></td><td></td><td></td>
						<td ><div align="right" valign="center" class="normaltext" ><input type="submit" value = "Search" name ="submit" class="normaltext" onclick=ButtonPress()></td>
						<td ><div align="middle" valign="center" class="normaltext" ><CENTER><INPUT TYPE="submit" value = "Clear" name = "flush"class="normaltext" onclick=buttonFlush()></CENTER></td>
						<td ><div align="left" valign="center" class="normaltext" ><input type="submit" value = "Show All" name = "printView"class="normaltext" onclick=ButtonPress()></td>					
						<td ><div align="left" valign="center" class="normaltext" ><input type="submit" value = "Print" name = "print"class="normaltext" onclick=pageSetup();buttonPrint()></td>				
					
					<!-- Print Button start 
						<td align="right"><IMG height=16 src="/egi/resources/erp2/images/Button_second_leftside.gif" width=8></td>
						<td bgcolor="#ffffff" valign="center" nowrap background="/egi/resources/erp2/images/Button_second_middle.gif"><A class=buttonsecondary onclick="pageSetup();buttonPrint()" href="#">Print</A></td>
						<td><IMG height=16 src="/egi/resources/erp2/images/Button_second_rightside.gif" width=8></td>
						<td><IMG src="/egi/resources/erp2/images/spacer.gif" width=8></td>
					 Print end-->					
					
						<td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td ></td><td></td><td></td><td ></td><td></td><td></td>
					</tr>
				</table>
			</td><div id="codescontainer"></div>
		</tr>

		<tr class="row1" >
			<td colspan=4>
				<table width="100%" align=center border="0" cellpadding="0" cellspacing="0" id='hdrRow' name="hdrRow">
				<tr class="row1" height="25">
					<TD width="95%" ?>
					<DIV class=displaydata id=rptHeader align=center
					name="rptHeader">&nbsp;
					</DIV>
					</TD>
				</tr>
				</table>
			</td>
		</tr>
		<!------------------ General Ledger Data ------------------>
	</table>
</div>
<br>
<%
	  Logger LOGGER = Logger.getLogger("GeneralReport.jsp");
	 LOGGER.info("after submit "+request.getParameter("fromBean"));
	 if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
	 {
		 LOGGER.info("after submit "+request.getParameter("fromBean"));
		 try{
		 GeneralLedgerReportList generalLedgerList = new GeneralLedgerReportList();			   
		 request.setAttribute("links",generalLedgerList.getGeneralLedgerList(glReportBean));
		 }
		 catch(Exception e){
		 LOGGER.error("Exception in Jsp Page "+ e.getMessage());
		 %> 
		 <script>
		 bootbox.alert("Error :<%=e.getMessage()%>");
		 PageManager.DataService.setQueryField('endDate',null)
		 </script>
		 <%
		 }
%>


<center><u><b><div class = "normaltext">General Ledger Report <br> <%= glReportBean.getStartDate() %> to <%= request.getParameter("endDate") %> </div></b></u></center>  
<div class = "alignleft"> <%= glReportBean.getIsConfirmedCount() %> in <%= glReportBean.getTotalCount() %> are unconfirmed   </div>
<div class="tbl3-container" style="width:1000px" id="tbl-container" > 
<display:table cellspacing="0" decorator="org.displaytag.decorator.TotalTableDecorator" name="links" id="currentRowObject"  export="false" sort="list" class="its" > 
				 
		 <div STYLE="display:table-header-group">
		 <display:column property="glcode"  title="Glcode" style="width:90px; " sortable="true"  headerClass="sortable"/>	
		 <display:column property="voucherdate" style="width:80px" title="VoucherDate" sortable="true"  headerClass="sortable"/>
		 <display:column media="pdf" property="vouchernumber" title="Voucher#" /> 	
		 <display:column media="excel" property="vouchernumber" title="Voucher#" />  
		 <display:column media="xml" property="vouchernumber" title="Voucher#" />  	
		 <display:column media="csv" property="vouchernumber" title="Voucher#" />  
 		<display:column media="html" title="Voucher#" sortable="true"  headerClass="sortable"> 
    		<% String cgn =((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getCGN();
     			String cgn1="",link="";
     			if(cgn.length()>0)
     			{
     			cgn1=cgn.substring(0,3);
     			}
     			if(cgn.length()>0)
     			{
     				link="javascript:callme('"+cgn+"','"+cgn1+"')";
     			}
				String url = ((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getVouchernumber();%>	
				
			<a href="<%=link%>"><%=url%></a>
		</display:column>
			
		 <display:column property="name" style="width:200px" title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Particulars&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" />
		 <display:column property="amount" title="Amount&nbsp;(Rs.)" />	
				
		 <display:column property="narration" style="width:80px" title="Narration"  />
		  <display:column property="type" style="width:90px" title="Transaction" />
		  <display:column property="debitamount" title="Debit&nbsp;(Rs.)" class="textAlign" />
		  <display:column property="creditamount" title="Credit&nbsp;(Rs.)" class="textAlign3" />
		  <% if(request.getParameter("forRunningBalance") !=null && request.getParameter("forRunningBalance").equalsIgnoreCase("on"))
		   {
		   %>
		 	<display:column property="runningDrCr" title="Running Balance&nbsp;(Rs.)" class="textAlign3" />
		  <% 
		  }
		  %>
		  <display:setProperty name="export.pdf" value="false" />
		  </div>
		 
		  <display:footer>
			<tr>
				<td style="border-left: solid 0px #000000" colspan="20"><div style="border-top: solid 1px #000000 ">&nbsp;</div></td>  		
			<tr>
  		</display:footer>	
</display:table>
 </div> 
<% 
} 
%>

				<!--  Added by Sathish for Print purpose -->
<%
	 LOGGER.info("after submit "+request.getParameter("fromBean"));
	 if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("2"))
	 {
		 LOGGER.info("after submit "+request.getParameter("fromBean"));
		 try{
		 GeneralLedgerReportList generalLedgerList = new GeneralLedgerReportList();			   
		 request.setAttribute("links",generalLedgerList.getGeneralLedgerList(glReportBean));
		 }
		 catch(Exception e){
		 LOGGER.error("Exception in Jsp Page "+ e.getMessage());
		 %> 
		 <script>
		 bootbox.alert("Error :<%=e.getMessage()%>");
		 PageManager.DataService.setQueryField('endDate',null)
		 </script>
		 <%
		 }
%>

  	

<center><u><b><div class = "normaltext">General Ledger Report <br> <%= glReportBean.getStartDate() %> to <%= request.getParameter("endDate") %> </div></b></u></center>  
<div class = "alignleft"> <%= glReportBean.getIsConfirmedCount() %> in <%= glReportBean.getTotalCount() %> are unconfirmed   </div>
<div class="tbl4-container" style="width:960px" id="tbl-container" >
<display:table cellspacing="0" decorator="org.displaytag.decorator.TotalTableDecorator" name="links" id="currentRowObject"  export="false" sort="list" class="its" > 
		
		 <div STYLE="display:table-header-group">
		 <display:column property="glcode" title="&nbsp;&nbsp;&nbsp;&nbsp;Glcode&nbsp;&nbsp;&nbsp;&nbsp;" style="width:90px" class="textAlign6" sortable="true"  headerClass="sortable"/>	
		 <display:column property="voucherdate" style="width:80px" title="VoucherDate" class="textAlign6" sortable="true"  headerClass="sortable"/>
		 <display:column media="pdf" property="vouchernumber" title="Voucher#" /> 	
		 <display:column media="excel" property="vouchernumber" title="Voucher#" />  
		 <display:column media="xml" property="vouchernumber" title="Voucher#" />  	
		 <display:column media="csv" property="vouchernumber" title="Voucher#" />  
 		<display:column media="html" title="Voucher#" style="width:50px" sortable="true" headerClass="sortable" > 
    		<% String cgn =((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getCGN();
     			String cgn1="",link="";
     			if(cgn.length()>0)
     			{
     			cgn1=cgn.substring(0,3);
     			}
     			if(cgn.length()>0)
     			{
     				link="javascript:callme('"+cgn+"','"+cgn1+"')";
     			}
				String url = ((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getVouchernumber();%>	
				
			<a href="<%=link%>"><font face="sans-serif" size="1"><%=url%></font></a>
		</display:column>
			
		 <display:column property="name" style="width:180px" title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Particulars&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" class="textAlign6"/>
		 <display:column property="amount" style="width:90px" title="Amount&nbsp;(Rs.)" class="textAlign6" />	
		 <display:column property="narration" style="width:80px" title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Narration&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" class="textAlign6" />
		  <display:column property="type" style="width:130px" title="&nbsp;&nbsp;&nbsp;&nbsp;Transaction&nbsp;&nbsp;&nbsp;&nbsp;" class="textAlign6" />
		  <display:column property="debitamount" title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Debit&nbsp;(Rs.)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" class="textAlign5" />
		  <display:column property="creditamount" title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Credit&nbsp;(Rs.)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" class="textAlign5" />
		   <% if(request.getParameter("forRunningBalance") !=null && request.getParameter("forRunningBalance").equalsIgnoreCase("on"))
		   {
		   %>
		 	<display:column property="runningDrCr" title="Running Balance&nbsp;(Rs.)" class="textAlign5" />
		  <% 
		  }
		  %>
		  <display:setProperty name="export.pdf" value="true" />
		  <display:setProperty name="paging.banner.placement" value="false" />
		</div>
		<display:footer>
			<tr>
				<td style="border-left: solid 0px #000000" colspan="20"><div style="border-top: solid 1px #000000 ">&nbsp;</div></td>  		
			<tr>
  		</display:footer>	
		
</display:table>
 </div>
<% 
}
%>

</form>
</body>
</html>
