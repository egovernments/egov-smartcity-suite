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
<%@page  import="com.exilant.eGov.src.reports.*,java.io.*,java.util.*,org.egov.infstr.utils.EGovConfig,java.io.*,java.sql.*,javax.sql.*,javax.naming.InitialContext,com.exilant.GLEngine.*"%>
<%@page import="org.apache.log4j.Logger"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java"%>


<html>
<head>
<title>General Ledger Report</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<!-- <META http-equiv=pragma content=no-cache> -->
<!-- Inclusion of the CSS files that contains the styles -->

<link rel=stylesheet href="../css/egov.css?rnd=${app_release_no}" type="text/css" media="screen" />
<link rel=stylesheet href="../css/print.css?rnd=${app_release_no}" type="text/css" media="print" />

<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js?rnd=${app_release_no}"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js?rnd=${app_release_no}"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js?rnd=${app_release_no}"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js?rnd=${app_release_no}"></SCRIPT>
<script language="javascript" src="../resources/javascript/jsCommonMethods.js?rnd=${app_release_no}"></script>
<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js?rnd=${app_release_no}" type="text/javascript" ></SCRIPT>
<script type="text/javascript" src="../commonyui/build/yahoo/yahoo.js"></script>
<script type="text/javascript" src="../commonyui/build/dom/dom.js" ></script>
<script type="text/javascript" src="../commonyui/build/autocomplete/autocomplete-debug.js"></script>
<script type="text/javascript" src="../commonyui/build/event/event-debug.js"></script>
<script type="text/javascript" src="../commonyui/build/animation/animation.js"></script>

<link type="text/css" rel="stylesheet" href="../commonyui/build/reset/reset.css">
<link type="text/css" rel="stylesheet" href="../commonyui/build/fonts/fonts.css">


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

//var cWind;         // temporary window reference
var count=1;
var forRE="";
var cWindows=new Object();
//var flag='0', tbNames, tbIds,dbTotal=0,crTotal=0;
var strtDate="",endDate="",isDrillDown="";
var rptType;
var fObj;
var rptTitle='', rptStartDate='', rptEndDate='', rptGLCode='', rptAccName='',fundNameQ='', detail='',accCode_1='',accCode_2='',snapShotDateTime,forRevEntry,revEntry;
var acccodeArray;
var codeObj;
var deptObj="",deptName="",functionCodeObj="",functionCodeIdObj="",functionName="",fieldObj="",fieldName="",functionaryObj="",functionaryName="",subtitle1="";

function onBodyLoad()
{
	PageValidator.addCalendars();
	PageManager.ListService.callListService();
	//PageManager.DataService.callDataService('cgNumber');
	var beanVal= <%=request.getParameter("fromBean")%>;
	if(beanVal==2)
	{
		document.getElementById("row3").style.display="block";
		document.getElementById("row2").style.display="none";
	}

	if(PageManager.DataService.getQueryField('glCode1') != null)
	{
		document.getElementById('glCode1').value=PageManager.DataService.getQueryField('glCode1');
	}
	isDrillDown=PageManager.DataService.getQueryField('isDrillDown');
	if(document.getElementById('glCode1').value)
	{
		if(isDrillDown!="Y")
			finHeader();
	}
	if(isDrillDown=="Y")
	{
	document.getElementById('fromBean').value = 1;
	document.getElementById('fund_id').style.display = 'none';
	document.getElementById('fundSource_id').style.display = 'none';

	document.getElementById('mainTable').style.display = 'none';
	document.getElementById('tbl-header1').style.display = 'none';
	}
	

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
	/*if(PageManager.DataService.getQueryField('glCode2') != null)
	{
		document.getElementById('glCode2').value=PageManager.DataService.getQueryField('glCode2');
	}*/
	if(PageManager.DataService.getQueryField('fundName') != null)
	{
		//bootbox.alert(PageManager.DataService.getQueryField('fundName'));
}
	if(document.getElementById('startDate').value && document.getElementById('endDate').value && document.getElementById('glCode1').value)
	{
		if(isDrillDown!="Y")
			finHeader();
}
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
	//40  Down arrow, 38  Up arrow
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
	if(gLCode1!="")
	{
		rptTitle = '<font size=3><b>General Ledger Report for ' +gLCode1;
	}
}

function openSearch(obj,index){
	 var a = new Array(1);

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
		//bootbox.alert(rptGLCode+" code");
		rptAccName = a[1];
		//bootbox.alert(rptAccName);
		//bootbox.alert("value "+a[2]);
		document.getElementById('chartOfAccounts_id1').value = a[2];
		//document.getElementById('glCode1').value=a[0]+","+a[1];
		document.getElementById('glCode1').value=a[0];
		//getDetailData(a[0]);
	}


}

function ButtonPress()
{
	//bootbox.alert("search");
	if (!PageValidator.validateForm())
	{
		//document.getElementById('startDate').value='';
		//document.getElementById('endDate').value='';
		return;
	}
	strtDate = document.getElementById('startDate').value;
	endDate = document.getElementById('endDate').value;
	var dbDate=document.getElementById('databaseDate').value;

	var aCode1 = document.getElementById('glCode1').value;
	accCode_1=aCode1;
	if(aCode1.length==0)
	{
		bootbox.alert("please select  Account Code");
		return;
	}
	if( compareDate(formatDate6(strtDate),formatDate6(endDate)) == -1 )
		{
		bootbox.alert('Start Date cannot be greater than End Date');
			document.getElementById('startDate').value='';
			document.getElementById('endDate').value='';
			document.getElementById('startDate').focus();
			return false;
		}
	//bootbox.alert("acc code "+accCode_1);
	PageManager.DataService.setQueryField('glCode1',accCode_1);
	 document.getElementById('fromBean').value = 1;

	 fObj = document.getElementById('fund_id');
	 fObj=fObj.options[fObj.selectedIndex].value;
	// bootbox.alert("fobj "+fObj);
	 var fsObj = document.getElementById('fundSource_id');
	 if(fsObj.value!='')
		fsObj=fsObj.options[fsObj.selectedIndex].value;
		 
	 deptObj=document.getElementById('departmentId');
	 if(deptObj.value!='' && deptObj.selectedIndex!=-1){
			//deptName=deptObj.options[deptObj.selectedIndex].text;
			//bootbox.alert("DepartmentName="+deptName);
			 document.getElementById('dept_name_for').value=' under '+deptObj.options[deptObj.selectedIndex].text+ ' Department';
			deptObj=deptObj.options[deptObj.selectedIndex].value;
		}else{
		document.getElementById('dept_name_for').value='';
		}
		
		functionaryObj=document.getElementById('functionaryId');
		if(functionaryObj.value!='' && functionaryObj.selectedIndex!=-1){
			//functionaryName=functionaryObj.options[functionaryObj.selectedIndex].text;
			//bootbox.alert("functionaryName="+functionaryName);
			//subtitle=" in "+functionaryName+" Functionary";
			document.getElementById('functionary_name_in').value=' in '+functionaryObj.options[functionaryObj.selectedIndex].text+' functionary';
			functionaryObj=functionaryObj.options[functionaryObj.selectedIndex].value;
		}else{
		document.getElementById('functionary_name_in').value='';
		}
		fieldObj=document.getElementById('fieldId');
		if(fieldObj.value!='' && fieldObj.selectedIndex!=-1){
			//fieldObj=fieldObj.options[fieldObj.selectedIndex].value;
			document.getElementById('field_name_in').value=' in '+fieldObj.options[fieldObj.selectedIndex].text+' field';
			//fieldName=fieldObj.options[fieldObj.selectedIndex].text;
			//bootbox.alert("fieldName="+fieldName);
			//subtitle=" under "+fieldName+" Field";
		}else{
		document.getElementById('field_name_in').value='';
		}               
		//functionCodeObj=document.getElementById('functionCodeId');
		functionCodeIdObj=document.getElementById('functionCode');
		
		if(functionCodeIdObj.value!=''){
			document.getElementById('functionCode_name').value=' in '+functionCodeIdObj.value+' Function Code';
		}else{
			document.getElementById('functionCode_name').value='';
		}
		//bootbox.alert("Subtitle"+subtitle);
		//return subtitle;
	// cgn=document.getElementById("cgn").value;
		//gl1=document.getElementById("glCode1").value;
	//forRE=document.getElementById("forRevEntry").value;
	//bootbox.alert(document.getElementById("functionCodeId").value);
	
	       
	 document.GeneralLedger.submit();

}


function buttonFlush1()
{
	document.getElementById('glCode1').value='';
	window.location="GeneralLedger.jsp";

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
	document.forms[0].submit();
  	if(window.print)
  		{
  		window.print();
	  	}
}


</SCRIPT>


</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onload="onBodyLoad()" onbeforeunload="closeChilds(cWindows,count)">
 <jsp:useBean id = "glReportBean" scope ="request" class="com.exilant.eGov.src.reports.GeneralLedgerReportBean"/>
<jsp:setProperty name = "glReportBean" property="*"/>
<form name="GeneralLedger" action = "GeneralLedger.jsp?fund_id="+fObj+"&fundSource_id="+fsObj+"&accountCodes="+accountCodes+"&startDate="+strtDate+"&endDate="+endDate method "post">
<input type="hidden" name="fromBean" id="fromBean" value="0">
<input type="hidden" name="databaseDate"  id="databaseDate">
<input type="hidden" id="functionCode_name" name="functionCode_name" value="">

<input type="hidden" id="field_name_in" name="field_name_in" value="">
<input type="hidden" id="functionary_name_in" name="functionary_name_in" value="">
<input type="hidden" id="dept_name_for" name="dept_name_for" value="">

<div class="tbl-header1" id="tbl-header1">
<center>
<br>
<table align='center' id="table2">
<tr><td>
<div id="main"><div id="m2"><div id="m3" style="width:810px">



<table width="100%" border=0 cellpadding="3" cellspacing="0" id="mainTable">
<tr >
		<td class="tableheader" valign="center" colspan="4" width="100%"><span id="screenName">GeneralLedger Report</span></td>
	</tr>
	<tr>
		<td align="right"><div  valign="center" class="labelcell" >Account Code<span class="leadon">* &nbsp;</span></div></td>

		<td class="smallfieldcell">
						<input type="hidden" name="chartOfAccounts_id1" id="chartOfAccounts_id1">
						<input class="fieldinput" name="glCode1" id="glCode1" autocomplete="off"   onkeyup="autocompletecode(this);" onblur="fillNeibrAfterSplit(this,'chartOfAccounts_id1');" ><IMG id=IMG1 onclick="openSearch('chartofaccounts',1);" height=22 src="/egi/resources/erp2/images/plus1.gif" width=25 align=top border=0>
					</td>
		<td colspan="2">&nbsp;</td>
		<div id="codescontainer"></div>
	</tr>


	<tr>
		<td align="right"><div align="right" valign="center" class="labelcell">Fund<span class="leadon">* &nbsp;</span></div></td>
		<td align="left" width="260" class="smallfieldcell">
			<SELECT class="fieldinput" id="fund_id" name="fund_id" exilListSource="fundNameList" exilMustEnter="true"></SELECT>
		</td>

		<td align="right"><div  valign="center" class="labelcell">Financing Source &nbsp;</div></td>
		<td align="left" class="smallfieldcell">
			<SELECT class="fieldinput" id="fundSource_id" name="fundSource_id"  exilListSource="fundSourceNameList"></SELECT>
		</td>
	</tr>
	<tr>
		<td align="right"><div align="right" valign="center" class="labelcell">Starting Date<span class="leadon">* &nbsp;</span></div></td>
		<td align="left" width="260" class="smallfieldcell">
			<input name="startDate" id="startDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')" exilCalendar="true" exilMustEnter="true">
		</td>
		<td align="right"><div align="right" valign="center" class="labelcell" >End Date<span class="leadon">* &nbsp;</span></div></td>
		<td class="smallfieldcell">
			<input name="endDate" id="endDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')" exilCalendar="true" exilMustEnter="true">
		</td>

	</tr>
	<%@ include file="reportParameter.jsp" %>
	<tr>
			<td></td>
		<td width="260"></td>
		<!--	<td><div align="right" valign="center" class="normaltext">Show Reversed Entry</div></td>
		<td >
		<input type="checkbox" name="forRevEntry" id="forRevEntry"></td>
		</td>-->
	</tr>

	<tr id="row2" name="row2">
		<td colspan="4" align="middle"><!-- Buttons Start Here -->
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td align="right">
					<input type=button class=button onclick=ButtonPress() href="#" value="Search"></td>

					<td align="right">
					<input type=button class=button onclick=buttonFlush1(); href="#" value="Cancel"></td>

					<td align="right">
					<input type=button class=button onclick=window.close() href="#" value="Close"></td>

					<!-- Print Button start -->
					<td align="right">
					<input type=button class=button onclick="buttonPrintPreview()" href="#" value="Print Preview"></td>
					<!-- Print end-->


				</tr>
			</table>
		</td>
	</tr>

	<tr style="DISPLAY: none" id="row3" name="row3">
		<td colspan="4" align="middle"><!-- Buttons Start Here -->
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td align="right">
					<input type=button class=button onclick=ButtonPress() href="#" value="Search"></td>

					<td align="right">
					<input type=button class=button onclick=buttonFlush1(); href="#" value="Cancel"></td>

					<td align="right">
					<input type=button class=button onclick=window.close() href="#" value="Close"></td>

					<!-- Print Button start -->
					<td align="right">
					<input type=button class=button onclick="pageSetup();buttonPrint()" href="#" value="Print"></td>
					<!-- Print end-->


				</tr>
			</table>
		</td>
	</tr>


</table>

</div></div></div>
</td></tr>
</table>
</div>

<%
	Logger LOGGER = Logger.getLogger("GeneralLedger.jsp");
  //  System.out.println("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
    {
	// System.out.println("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate"));
		try{
		 GeneralLedgerReport glReport = new GeneralLedgerReport ();
		 request.setAttribute("links",glReport.getGeneralLedgerList(glReportBean));
		 }
		 catch(Exception e){
		 LOGGER.error("Exception in Jsp Page "+ e.getMessage());
		 %>
		 <script>
		 bootbox.alert("Error :<%=e.getMessage()%>");
		 PageManager.DataService.setQueryField('endDate',null);
		 </script>
		 <%
		 }
%>
	<center><u><b><div class = "normaltext"> Ledger Report<br><bean:message key="GL.Name"/><br>
	For <%= glReportBean.getAccountCode() %> for <%= glReportBean.getFundName()==null?"":glReportBean.getFundName() %>
	from <%= glReportBean.getStartDate()==null?"":glReportBean.getStartDate() %> to 
	<%= request.getParameter("endDate")==null?"":request.getParameter("endDate") %> <%= request.getParameter("dept_name_for")==null?"":request.getParameter("dept_name_for") %> 
	<%= request.getParameter("functionCode_name")==null?"":request.getParameter("functionCode_name") %>	<%= request.getParameter("functionary_name_in")==null?"":request.getParameter("functionary_name_in") %> 
	<%= request.getParameter("field_name_in")==null?"":request.getParameter("field_name_in") %> </div></b></u></center>
	<div class = "alignleft"> <%= glReportBean.getIsConfirmedCount() %> in <%= glReportBean.getTotalCount() %> are unconfirmed   </div>

	<table border="1" style="border: solid 1px #000000" style="width:970px;">
		<tr colspan=2 >
			<td width=50% class="normaltext" style="border: solid 1px #000000" align="center"><B><center>Debit</center></B></td>
			<td width=50% class="normaltext" style="border: solid 1px #000000" align="center"><B><center>Credit</center></B></td>
		</tr>
	</table>

	<div class="tbl2-container" id="tbl-container" style="width:970px;" >
		<display:table cellspacing="0" decorator="org.displaytag.decorator.TotalTableDecorator" name="links" id="currentRowObject" class="its"  export="true" sort="list" >
		<display:caption media="pdf ">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		Ledger Report&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		For <%= glReportBean.getAccountCode() %> for <%= glReportBean.getFundName()==null?"":glReportBean.getFundName() %> from <%= glReportBean.getStartDate() %> to <%= request.getParameter("endDate") %>
		<%= request.getParameter("dept_name_for")==null?"":request.getParameter("dept_name_for") %> <%= request.getParameter("functionCode_name")==null?"":request.getParameter("functionCode_name") %>
			<%= request.getParameter("functionary_name_in")==null?"":request.getParameter("functionary_name_in") %> <%= request.getParameter("field_name_in")==null?"":request.getParameter("field_name_in") %>   
		</display:caption>
		<div STYLE="display:table-header-group">

		<display:column property="voucherdate" title="Voucher&nbsp;Date" sortable="true" style="width:95px;"  headerClass="sortable" />
		<display:column media="pdf" property="vouchernumber" title="Voucher No." />
		<display:column media="excel" property="vouchernumber" title="Voucher No." />
		<display:column media="xml" property="vouchernumber" title="Voucher No." />
		<display:column media="csv" property="vouchernumber" title="Voucher No." />
		<display:column media="html" title="Voucher No." sortable="true"  headerClass="sortable" style="width:100px;">

	  		<% String cgn =((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getCGN();
	  			String deployment=EGovConfig.getProperty("egf_config.xml", "DEPLOYMENT","", "general");
	     		String cgn1="",link="";
	     		if(cgn.length()>0)
	     		{
	     			cgn1=cgn.substring(0,3);
	     		}
	     		if(cgn.length()>0)
	     		{
	     			if(deployment.equalsIgnoreCase("NN"))	
		     			link="javascript:callme('"+cgn+"','"+cgn1+"')";
		     		else 
	     					link="javascript:callmeAP('"+cgn+"','"+cgn1+"')";		
	     		}
			String url = ((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getVouchernumber();%>

			<a href="<%=link%>"><%=url%></a>
			</display:column>
			<display:column property="debitVoucherTypeName" title="Voucher Type- Name"/>
			<display:column property="debitamount" title="&nbsp;&nbsp;&nbsp;Amount&nbsp;(Rs.)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" class="textAlign" />
			<display:column property="creditdate" title="Voucher&nbsp;Date" sortable="true"  headerClass="sortable" style="width:90px;" />
			<display:column media="pdf" property="creditvouchernumber" title="Voucher No." />
			<display:column media="excel" property="creditvouchernumber" title="Voucher No." />
			<display:column media="xml" property="creditvouchernumber" title="Voucher No." />
			<display:column media="csv" property="creditvouchernumber" title="Voucher No." />
			<display:column media="html" title="Voucher No." sortable="true"  headerClass="sortable" style="width:100px;" >

				<% String cgn =((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getCGN();
					String deployment=EGovConfig.getProperty("egf_config.xml", "DEPLOYMENT","", "general");
					String cgn1="",link="";
					if(cgn.length()>0)
					{
						cgn1=cgn.substring(0,3);
					}
					if(cgn.length()>0)
					{
						if(deployment.equalsIgnoreCase("NN"))
							link="javascript:callme('"+cgn+"','"+cgn1+"')";
						else 
	     					link="javascript:callmeAP('"+cgn+"','"+cgn1+"')";	
					}
				String url = ((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getCreditvouchernumber();%>

				<a href="<%=link%>"><%=url%></a>
				</display:column>
				<display:column property="creditVoucherTypeName" title="Voucher Type- Name"/>
				<display:column property="creditamount" title="&nbsp;&nbsp;&nbsp;Amount&nbsp;(Rs.)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" class="textAlign3" />
				<display:setProperty name="export.pdf" value="true" />
				<display:setProperty name="export.pdf.filename" value="GeneralLedger.pdf" /> 
			    <display:setProperty name="export.excel" value="true" />
			    <display:setProperty name="export.excel.filename" value="GeneralLedger.xls"/>
			    <display:setProperty name="export.csv" value="false" />
				<display:setProperty name="export.xml" value="false" />

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

	<%
   // System.out.println("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("2"))
    {
	// System.out.println("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate"));
		try{
		 GeneralLedgerReport glReport = new GeneralLedgerReport ();
		 request.setAttribute("links",glReport.getGeneralLedgerList(glReportBean));
		 }
		 catch(Exception e){
		 LOGGER.error("Exception in Jsp Page "+ e.getMessage());
		 %>
		 <script>
		 bootbox.alert("Error :<%=e.getMessage()%>");
		 PageManager.DataService.setQueryField('endDate',null);
		 </script>
		 <%
		 }
%>


	<center><u><b><div class = "normaltext"> Ledger Report<br><bean:message key="GL.Name"/><br>
	For <%= glReportBean.getAccountCode() %> for <%= glReportBean.getFundName()==null?"":glReportBean.getFundName() %>
			from <%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %>  
			<%= request.getParameter("dept_name_for")==null?"":request.getParameter("dept_name_for") %><%= request.getParameter("functionCode_name")==null?"":request.getParameter("functionCode_name")%>
			<%= request.getParameter("functionary_name_in")==null?"":request.getParameter("functionary_name_in") %> <%= request.getParameter("field_name_in")==null?"":request.getParameter("field_name_in") %> </div></b></u></center>
			<div class = "alignleft"> <%= glReportBean.getIsConfirmedCount() %> in <%= glReportBean.getTotalCount() %> are unconfirmed   </div>

	 <table border="1"  class="normaltext" cellspacing="0"  style="width:970px;">
		<tr colspan=2 >
			<td width=49%  align="center"><B><center>Debit</center></B></td>
			<td width=49%  align="center"><B><center>Credit</center></B></td>
		</tr></center>
	</table>

	 <div class="tbl4-container" id="tbl-container" style="width:970px;">

		<display:table cellspacing="0" decorator="org.displaytag.decorator.TotalTableDecorator" name="links" id="currentRowObject" class="its"  export="true" sort="list" >
		<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		Ledger Report&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		For <%= glReportBean.getAccountCode() %> for <%= glReportBean.getFundName()==null?"":glReportBean.getFundName() %> from <%= glReportBean.getStartDate() %> to <%= request.getParameter("endDate") %> <%= request.getParameter("dept_name_for")==null?"":request.getParameter("dept_name_for") %> 
		<%= request.getParameter("functionCode_name")==null?"":request.getParameter("functionCode_name") %>	<%= request.getParameter("functionary_name_in")==null?"":request.getParameter("functionary_name_in")%> <%= request.getParameter("field_name_in")==null?"":request.getParameter("field_name_in") %>  
		</display:caption>
		<div STYLE="display:table-header-group">

		<display:column property="voucherdate" title="Voucher&nbsp;Date" sortable="true" style="width:95px;"  headerClass="sortable" />
		<display:column media="pdf" property="vouchernumber" title="Voucher No." />
		<display:column media="excel" property="vouchernumber" title="Voucher No." />
		<display:column media="xml" property="vouchernumber" title="Voucher No." />
		<display:column media="csv" property="vouchernumber" title="Voucher No." />
		<display:column media="html" title="Voucher No." sortable="true"  headerClass="sortable" style="width:100px;">

	  		<% String cgn =((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getCGN();
	     		String deployment=EGovConfig.getProperty("egf_config.xml", "DEPLOYMENT","", "general");
	     		String cgn1="",link="";
	     		if(cgn.length()>0)
	     		{
	     			cgn1=cgn.substring(0,3);
	     		}
	     		if(cgn.length()>0)
	     		{
	     			if(deployment.equalsIgnoreCase("NN"))
		     			link="javascript:callme('"+cgn+"','"+cgn1+"')";
		     		else 
	     					link="javascript:callmeAP('"+cgn+"','"+cgn1+"')";		
	     		}
			String url = ((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getVouchernumber();%>

			<a href="<%=link%>"><%=url%></a>
			</display:column>
			<display:column property="debitVoucherTypeName" title="Voucher Type- Name"/>
			<display:column property="debitamount" title="&nbsp;&nbsp;&nbsp;Amount&nbsp;(Rs.)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" class="textAlign" />
			<display:column property="creditdate" title="Voucher&nbsp;Date" sortable="true"  headerClass="sortable" style="width:90px;" />
			<display:column media="pdf" property="creditvouchernumber" title="Voucher No." />
			<display:column media="excel" property="creditvouchernumber" title="Voucher No." />
			<display:column media="xml" property="creditvouchernumber" title="Voucher No." />
			<display:column media="csv" property="creditvouchernumber" title="Voucher No." />
			<display:column media="html" title="Voucher No." sortable="true"  headerClass="sortable" style="width:100px;" >

				<% String cgn =((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getCGN();
					String deployment=EGovConfig.getProperty("egf_config.xml", "DEPLOYMENT","", "general");
					String cgn1="",link="";
					if(cgn.length()>0)
					{
						cgn1=cgn.substring(0,3);
					}
					if(cgn.length()>0)
					{
						if(deployment.equalsIgnoreCase("NN"))
							link="javascript:callme('"+cgn+"','"+cgn1+"')";
						else 
	     					link="javascript:callmeAP('"+cgn+"','"+cgn1+"')";	
					}
				String url = ((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getCreditvouchernumber();%>

				<a href="<%=link%>"><%=url%></a>
				</display:column>
				<display:column property="creditVoucherTypeName" title="Voucher Type- Name"/>
				<display:column property="creditamount" title="&nbsp;&nbsp;&nbsp;Amount&nbsp;(Rs.)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" class="textAlign3" />
				<display:setProperty name="export.pdf" value="true" />
				<display:setProperty name="export.pdf.filename" value="GeneralLedger.pdf" /> 
			    <display:setProperty name="export.excel" value="true" />
			    <display:setProperty name="export.excel.filename" value="GeneralLedger.xls"/>
			    <display:setProperty name="export.csv" value="false" />
				<display:setProperty name="export.xml" value="false" />

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
