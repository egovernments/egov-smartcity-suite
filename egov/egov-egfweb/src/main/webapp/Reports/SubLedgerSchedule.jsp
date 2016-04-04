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
<%@page  import="java.text.SimpleDateFormat,com.exilant.eGov.src.reports.*,com.exilant.eGov.src.transactions.*,java.io.*,java.util.*,javax.naming.InitialContext,com.exilant.GLEngine.*"%>
<%@ page import="org.apache.log4j.Logger"%>
<html>
<head>
<title>SubLedger Schedule Report</title>
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
<link type="text/css" rel="stylesheet" href="/commonyui/build/reset/reset.css">
<link type="text/css" rel="stylesheet" href="/commonyui/build/fonts/fonts.css">
<link type="text/css" rel="stylesheet" href="/commonyui/examples/autocomplete/css/examples.css">

<style type="text/css">
#codescontainer {position:absolute;left:11em;width:9%}
#codescontainer .yui-ac-content {position:absolute;width:100%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:60%;background:#a0a0a0;z-index:9049;}
#codescontainer ul {padding:5px 0;width:100%;}
#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
#codescontainer li.yui-ac-highlight {background:#ff0;}
#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
</style>

<%
 	SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
	Date crntDate = new Date();
	String currDate = sdf.format(crntDate);
%>

<SCRIPT LANGUAGE="javascript">
var fuId ="";
var deptId="";
var strtDate="", endDate="", rptGLCode="", rptAccName="", detail="";
var gl="";
var codeObj;
var accEntityId="";
var pressedKey;

document.onkeydown= handleKeyPress

function handleKeyPress(evt)
{
 pressedKey= (window.event)?event.keyCode:evt.which;
}
function onBodyLoad() 
{
	PageValidator.addCalendars();
	PageManager.ListService.callListService();
	PageManager.DataService.callDataService('cgNumber');
	var beanVal= <%=request.getParameter("fromBean")%>;
	if(beanVal==2)
	{
		document.getElementById("row2").style.display="block";
		document.getElementById("row1").style.display="none";		
	}
		/*
	 * The following code for account code implements Ajax autocomplete through YUI.
	 *
	 *
	 */
	   	  var type='getSubLedgerScheduleCodes';
	      var url = "../commons/Process.jsp?type="+type;
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
	document.getElementById("accCode").focus();
}
function ButtonPress()
{
	
	if (!PageValidator.validateForm())
	{
		return;
	}

	
	document.getElementById('fromBean').value = 1;
	document.getElementById('glcode').value=document.getElementById('accCode').value;
	var fObj=document.getElementById('fund_id');	
	fuId=fObj.options[fObj.selectedIndex].value;
	var deptObj=document.getElementById('deptId');
	if(deptObj.value!='' && deptObj.selectedIndex!=-1)
		deptId=deptObj.options[deptObj.selectedIndex].value
	strtDate = document.getElementById('startDate').value;
	endDate = document.getElementById('endDate').value;
	var dbDate=document.getElementById('databaseDate').value;

	 /* checking Fiscal year start Date with End Date of Fiscal year */	
		var tMonth=endDate.substr(endDate.length-7,2);
		if(tMonth<4)
		var fiscalYearStartDate="01/04/"+(endDate.substr(endDate.length-4,4)-1);
		else
		var fiscalYearStartDate="01/04/"+endDate.substr(endDate.length-4,4);
		if(compareDate(fiscalYearStartDate,strtDate) == -1 )
		{ 
			bootbox.alert("Start Date and End Date should be in same financial year");
		   document.getElementById('startDate').focus();
		   return;
		} 
	
	/*To check whether Start Date is Greater than End Date*/
	if( compareDate(formatDate6(strtDate),formatDate6(endDate)) == -1 )
	{
		bootbox.alert('Start Date cannot be greater than End Date');
		document.getElementById('startDate').value='';
		document.getElementById('endDate').value='';
		document.getElementById('startDate').focus();
		return false;
	}	
	   /*to check whether the End Date is greater than the Current Date*/
		if( compareDate(formatDate6("<%=currDate%>"),formatDate6(endDate)) == 1 )
		{
			bootbox.alert('End Date cannot be greater than Current Date');
			document.getElementById('endDate').value='';
			document.getElementById('endDate').focus();	
			return;	
		}		
		document.getElementById('accEntityText').value=document.getElementById('accEntityId').options[document.getElementById('accEntityId').selectedIndex].text;
		document.getElementById('fund_name').value=document.getElementById('fund_id').options[document.getElementById('fund_id').selectedIndex].text;
		
	gl=document.getElementById("glcode").value;
	//gl2=document.getElementById("glCode1").value;	
	
  	document.SubLedgerSchedule.submit();
}	 

function openSearch(obj)
{
 	var a = new Array(2);
	var screenName="subLedger_names";
	var str="";		
	var accCode=document.getElementById("accCode").value;
	str = "../HTML/Search.html?tableNameForCode=chartofaccounts_controlcodes";
	var sRtn = showModalDialog(str,"","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");    
	a = sRtn.split("`~`");
	var type = document.getElementById('accCode');
	type.value = a[0];
	rptGLCode = a[0];
	rptAccName = a[1];
	document.getElementById('chartOfAccounts_id').value = a[2];
	if(sRtn!='')
		loadSubLedgerTypeList();
}

function buttonFlush1()
{
	window.location="SubLedgerSchedule.jsp";
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
//compose url to open subledger report
function composeUrl(name,accEntId,accEntkey)
{
	var accCodeURL=document.getElementById("accCode").value;
	var chartOfAccounts_idURL=document.getElementById("chartOfAccounts_id").value;
	var fund_idURL=document.getElementById("fund_id").value;
	var dept_idURL=document.getElementById("deptId").value;
	var startDateURL=document.getElementById("startDate").value;
	var endDateURL=document.getElementById("endDate").value; 	
	window.open("SubLedgerReport.jsp?fromBean=1&reportType=sl&glCode2="+accCodeURL+"&chartOfAccounts_id="+chartOfAccounts_idURL+"&glCode1="+accCodeURL+"&fund_id="+fund_idURL+"&departmentId="+dept_idURL+"&startDate="+startDateURL+"&endDate="+endDateURL+"&accEntityId="+accEntId+"&entityName="+name+"&accEntityKey="+accEntkey+"&drillDownFromSchedule=true","","height=650,width=900,scrollbars=yes,left=0,top=0,status=yes");
}
function autocompletecode(obj)
{
	// set position of dropdown
	var src = obj;
	var target = document.getElementById('codescontainer');
	var posSrc=findPos(src);
	target.style.left=posSrc[0];
	target.style.top=posSrc[1]+25;
	target.style.width=500;
	if(obj.name=='accCode') target.style.left=posSrc[0]-200;	
	var currRow=PageManager.DataService.getRow(obj);
	var coaCodeObj = obj;
	if(pressedKey != 40 )
	{
		if(pressedKey != 38 )
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
	var temp = obj.value; 
	temp = temp.split("`-`");
	if(obj.value==null || obj.value=="") { neibrObj.value=""; return; }
	if(temp[1]==null && (neibrObj.value!='' || neibrObj.value!=null) ) {  return ;}
	else 
	{
		//bootbox.alert("Hi");
		temp=temp[0].split("-$-");
		obj.value=temp[0];
		neibrObj.value = temp[1];
	}
	 loadSubLedgerTypeList(); 
			 
 }

 function clearCombo(obj)
{
	try{
		for(var i=obj.length-1;i>=0;i--)				
			obj.remove(i);			
	}catch(err) {}
		
}
 function loadSubLedgerTypeList()
{	
	var glCodeId = document.getElementById('chartOfAccounts_id').value;	
	glcode=document.getElementById('accCode').value;	
	if(glCodeId != '')
	{
		PageManager.DataService.setQueryField('glCode', glcode);		
		PageManager.DataService.callDataService('subLedgerList');				
	}	
} 
 
 function beforeRefreshPage(dc)
{
	var accName = dc.values['accName'];
	if(accName != null && accName != '')
		rptAccName = dc.values['accName'];	
	if(dc.values['glCode'])
		dc.values['accCode']=dc.values['glCode'];	
}

function afterRefreshPage(dc)
{
	coaId=dc.values['glCodeId'];
	if(dc.values['serviceID']=='cgNumber')
	{ 	
		loadSubLedgerTypeList();
	}
	PageManager.DataService.removeQueryField('startDate');
	PageManager.DataService.removeQueryField('endDate');
	rptAccName = dc.values['accName'];
	
	/* to fill the SubLedgerList Combo*/
	if(dc.values['serviceID']=='subLedgerList')
	{
		if(coaId && coaId != '')
			document.getElementById('chartOfAccounts_id').value=coaId;
		var subLedgerList= dc.grids['subLedgerList'];
		if(subLedgerList!= null)
		{
			document.getElementById('accEntityId').value = dc.values['accountDetailTypeId'];
			var comboSubledgerList = document.getElementById('accEntityId'); 			
			clearCombo(comboSubledgerList);
			var option;
			for(var i=0; i<subLedgerList.length; i++)
			{			
				option = document.createElement('OPTION');
				option.value=subLedgerList[i][0];
				option.text = subLedgerList[i][1];				
				comboSubledgerList.add(option,undefined);			
			} 
		}
	}
}

</SCRIPT>
</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onload="onBodyLoad()">
 <jsp:useBean id = "slsReportBean" scope ="request" class="com.exilant.GLEngine.GeneralLedgerBean"/>
<jsp:setProperty name = "slsReportBean" property="*"/>  
<form name="SubLedgerSchedule" action = "SubLedgerSchedule.jsp?Fund_id="+fuId+"&glcode="+gl+"&deptId="+deptId+"&accEntityId="+accEntityId+"&startDate="+strtDate+"&endDate="+endDate method = "post">
<input type="hidden" name="fromBean" id="fromBean" value="0">
<input type="hidden" name="glcode" id="glcode" value="">  
<input type="hidden" name="databaseDate"  id="databaseDate">
<div class="tbl-header1" id="tbl-header1">
<center>
<br>
<table align='center' id="table2">
<tr><td>
<div id="main"><div id="m2"><div id="m3" style="width:810px">



<table width="100%" border=0 cellpadding="3" cellspacing="0">
<tr>
			<td class="tableheader" valign="center" width="100%" colspan="4"><span id="screenName" >SubLedger Schedule Report</span></td>
		</tr><!-- row-1 -->

	<tr>

   		</tr><!-- row-2 -->
		<tr>
			<td align="right"><div  class="labelcell" >Account Code<span class=leadon>* &nbsp;</span></div></td>
			<td class="smallfieldcell">
				<input type="hidden" name="chartOfAccounts_id" id="chartOfAccounts_id">
				<input class="fieldinput" type="hidden" name="accEntityText" id="accEntityText">
				<input class="fieldinput" name="accCode" id="accCode"  exilMustEnter="true" autocomplete="off" onkeyup="autocompletecode(this);" onblur="fillNeibrAfterSplit(this,'chartOfAccounts_id')"><IMG id=IMG1 onclick="openSearch(this);" height=22 src="/egi/resources/erp2/images/plus1.gif" width=25 align=top border=0>
			</td>
			<td align="right"><div  valign="center" class="labelcell">Fund<span class=leadon>* &nbsp;</span></div></td>
			<td align="left" class="smallfieldcell"><input type="hidden" name="fund_name" id="fund_name">
				<SELECT class="fieldinput" id=fund_id name="fund_id" exilListSource="fundNameList" exilMustEnter="true"></SELECT>
			</td>
		</tr><!-- row-3 -->

		<tr>
			<td align="right"><div  valign="center" class="labelcell">Starting Date<span class=leadon>* &nbsp;</span></div></td>
			<td align="left" class="smallfieldcell">
				<input name="startDate" id="startDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')" exilDataType="exilAnyDate" exilCalendar="true" exilMustEnter="true">
			</td>
			<td align="right"><div  valign="center" class="labelcell" >End Date<span class=leadon>* &nbsp;</span></div></td>
			<td class="smallfieldcell">
				<input name="endDate" id="endDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')" exilDataType="exilAnyDate" exilCalendar="true" exilMustEnter="true">
			</td>
			
			<tr>
			<td  align=right><div class="labelcell">Sub Ledger Type<span class="leadon">* &nbsp;</span></td></div>
			<td align="left" class="smallfieldcell"><input type="hidden" name="accEntityText" id="accEntityText">
			
			 <SELECT class="fieldinput" name="accEntityId" id="accEntityId" exilMustEnter="true" onChange="getDescription(this);callEntity()" ></SELECT>
		
			</td>
				<td  align=right><div class="labelcell">Department</td></div>
				<td align="left"><span class="smallfieldcell">
					<SELECT class="fieldinput" id="deptId" name="deptId" class="fieldinput" exilListSource="departmentName"></SELECT>
				</td>
			<td></td><td></td>

		</tr>
		<tr>
		<td>
		<div id="codescontainer"></div>
		</td>
		</tr>

		<tr>
			<td colspan=4>
			</td>
		</tr>
	
	<tr id="row1" name="row1">
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
	
	<tr style="DISPLAY: none" id="row2" name="row2">
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

<br><br>
</div></div></div>
</td></tr>
</table>
</div>
<%
	Logger LOGGER = Logger.getLogger("SubLedgerSchedule.jsp");
    LOGGER.info("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate")+" glcode "+request.getParameter("glcode"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
    {
    	
	 LOGGER.info("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate")+" glcode "+request.getParameter("glcode"));
		try{
		 RptSubLedgerSchedule slsReport = new RptSubLedgerSchedule();			   
		 request.setAttribute("links",slsReport.getSubLedgerTypeSchedule(slsReportBean));
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
			
		<%!
				String link="";
				String accEntityIdLink=""; 
				String accEntityKeyLink="";
		%>
		<center><b><div class = "normaltext"><!--<%=slsReportBean.getUlbName() %>--> SubLedger Schedule for  <%= request.getParameter("accCode") %> - <%= slsReportBean.getAccName() %></div></b> </center>   		
		<center><div class = "normaltext"> AND SubLedger Type:  <%= request.getParameter("accEntityText")  %> <br> <%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %> for <%= request.getParameter("fund_name") %></div><center>		
		<div class="tbl2-container" id="tbl-container" >
		<display:table cellspacing="0"  decorator="org.displaytag.decorator.TotalTableDecorator" name="links" id="currentRowObject"  export="true" sort="list" class="its" > 
		<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;
		SubLedger Schedule for  <%= request.getParameter("accCode") %> - <%= slsReportBean.getAccName() %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		AND SubLedger Type:  <%= request.getParameter("accEntityText")  %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %> for <%= request.getParameter("fund_name") %>
		</display:caption>
		<div STYLE="display:table-header-group">
			<display:column property="code" title="Code" />			
			<display:column title="Name">	
			
			<%	String name =((com.exilant.GLEngine.GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getName();
				accEntityKeyLink =((com.exilant.GLEngine.GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getAccEntityKey();
				LOGGER.info("accEtityKeyLink..."+accEntityKeyLink);
				accEntityIdLink=slsReportBean.getAccEntityId();
				LOGGER.info("accEntityIdLink..."+accEntityIdLink);

				link="javascript:composeUrl('"+name+"','"+accEntityIdLink+"','"+accEntityKeyLink+"')";
				if(accEntityKeyLink!=null){
			%>
						
			<a href="<%=link%>"><%=name%></a>
			<%
				}else{
			%>
					<%=name%>
			<% 		}
			%>
			</display:column>
		
			
			<display:column property="openingBal"   title="Opening Balance"   class="textAlign"  /> 
			<display:column property="debitamount" title="Debit&nbsp;(Rs.)" class="textAlign" />
			<display:column property="creditamount" title="Credit&nbsp;(Rs.)" class="textAlign" />
			<display:column property="closingBal"  title="Closing Balance"  class="textAlign3" />
			<display:setProperty name="export.pdf" value="true" />
			<display:setProperty name="export.pdf.filename" value="SubLedgerSchedule.pdf" /> 
		    <display:setProperty name="export.excel" value="true" />
		    <display:setProperty name="export.excel.filename" value="SubLedgerSchedule.xls"/>
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

	<!--  Added by Sathish for Print purpose -->
<%
    LOGGER.info("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate")+" glcode "+request.getParameter("glcode"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("2"))
    {
	 LOGGER.info("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate")+" glcode "+request.getParameter("glcode"));
		try{
		 RptSubLedgerSchedule slsReport = new RptSubLedgerSchedule();			   
		 request.setAttribute("links",slsReport.getSubLedgerTypeSchedule(slsReportBean));
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
	 		
		<center><b><div class = "normaltext"> <br> SubLedger Schedule for <%= request.getParameter("accCode") %> - <%= slsReportBean.getAccName() %> </div></b></center>   		
		<center><div class = "normaltext"> AND SubLedger Type:  <%= request.getParameter("accEntityText")  %> <br> <%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %> for <%= request.getParameter("fund_name") %> </div><center>		
		<div class="tbl5-container" id="tbl-container" >  
		<display:table cellspacing="0"  decorator="org.displaytag.decorator.TotalTableDecorator" name="links" id="currentRowObject"  export="true" sort="list" class="its" > 
		<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;
		SubLedger Schedule for  <%= request.getParameter("accCode") %> - <%= slsReportBean.getAccName() %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		AND SubLedger Type:  <%= request.getParameter("accEntityText")  %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %> for <%= request.getParameter("fund_name") %>
		</display:caption>
		<div STYLE="display:table-header-group">
			<display:column property="code" title="Code" />			
			<display:column property="name" title="Name" />				 
			
			<display:column property="openingBal"   title="Opening Balance"   class="textAlign"  /> 			
			<display:column property="debitamount" title="Debit&nbsp;(Rs.)" class="textAlign" />
			<display:column property="creditamount" title="Credit&nbsp;(Rs.)" class="textAlign" />			
			<display:setProperty name="paging.banner.placement" value="false" />
			<display:column property="closingBal"  title="Closing Balance"  class="textAlign3" />	
			<display:setProperty name="export.pdf" value="true" />
			<display:setProperty name="export.pdf.filename" value="SubLedgerSchedule.pdf" /> 
		    <display:setProperty name="export.excel" value="true" />
		    <display:setProperty name="export.excel.filename" value="SubLedgerSchedule.xls"/>
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
