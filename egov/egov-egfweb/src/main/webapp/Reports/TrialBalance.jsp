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
<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page buffer = "16kb" %>
<%@page  import="java.text.SimpleDateFormat,com.exilant.eGov.src.reports.LongAmountWrapper,com.exilant.eGov.src.reports.*,java.io.*,java.util.HashMap,java.util.*,javax.naming.InitialContext,com.exilant.GLEngine.*"%>
<%@ page import="org.apache.log4j.Logger"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java"%>
<html>
<head>
<title>Trial Balance Report</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<META http-equiv=pragma content=no-cache>

<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen, print" />

<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
<script language="javascript" src="../resources/javascript/jsCommonMethods.js"></script>
<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js" type="text/javascript" ></SCRIPT>


<SCRIPT LANGUAGE="javascript">
<%
 	SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
	Date crntDate = new Date();
	String currDate = sdf.format(crntDate);
%>
var endDate="";
var fuid="";
var startDate="";
var type="";
var deptObj="",deptName="",functionCodeObj="",functionCodeIdObj="",functionName="",fieldObj="",fieldName="",functionaryObj="",functionaryName="",subtitle1="";
function onLoad()
{
	PageManager.ListService.callListService();
	
   	PageValidator.addCalendars();
   	if(document.getElementById("startDate").value=="")
	{		
		PageManager.DataService.callDataService("finYearDate");
	}
	PageManager.ListService.callListService();
	var beanVal= <%=request.getParameter("fromBean")%>;
	if(beanVal==2 || beanVal==4)
	{
		var type1='<%=request.getParameter("type")%>'; 
		if(type1!='null')
  		document.getElementById("type").value=type1;  	
		getHeaderRow(document.getElementById("type"));
		document.getElementById("row3").style.display="block";
		document.getElementById("row2").style.display="none";
	}
	else
	{
		var type1='<%=request.getParameter("type")%>'; 
   		if(type1!='null')
  		document.getElementById("type").value=type1;  	
		getHeaderRow(document.getElementById("type"));
	}
	document.getElementById("toDate").value="<%=currDate%>";	
	document.getElementById("type").focus();	
}

function getHeaderRow(obj)
{	
	if(obj.value=="asOnDate")
	{	
		type="asOnDate";
		document.getElementById("row2").style.display="block";	
		document.getElementById('fund').innerHTML="Fund";
		document.getElementById("td1").style.display="none";
		document.getElementById("td2").style.display="none";
		document.getElementById("oldReportFundTd1").style.display="block";
		document.getElementById("oldReportFundTd2").style.display="block";
		document.getElementById("oldReportAsOnDateTd1").style.display="block";
		document.getElementById("oldReportAsOnDateTd2").style.display="block";
		document.getElementById("newReportDateRangeRow").style.display="none";
		document.getElementById("asOnDate").setAttribute('exilMustEnter');
		document.getElementById("trFund_id").removeAttribute('exilMustEnter');
		document.getElementById("startDate").removeAttribute('exilMustEnter');
		document.getElementById("toDate").removeAttribute('exilMustEnter');
	}
	else if(obj.value=="dateRange")
	{		
		type="dateRange";
		document.getElementById("row2").style.display="block";	
		document.getElementById('fund').innerHTML="Fund<span class='leadon'>*</span>";	
		document.getElementById("td1").style.display="block";
		document.getElementById("td2").style.display="block";
		document.getElementById("oldReportFundTd1").style.display="block";
		document.getElementById("oldReportFundTd2").style.display="block";
		document.getElementById("oldReportAsOnDateTd1").style.display="none";
		document.getElementById("oldReportAsOnDateTd2").style.display="none";
		document.getElementById("newReportDateRangeRow").style.display="block";		
		document.getElementById("asOnDate").removeAttribute('exilMustEnter');
		document.getElementById("trFund_id").setAttribute('exilMustEnter');
		document.getElementById("startDate").setAttribute('exilMustEnter');
		document.getElementById("toDate").setAttribute('exilMustEnter');
	}
}

function ButtonPress()
{	
	if (!PageValidator.validateForm())
		return;

	if(type=="asOnDate")
	{	
		if(document.getElementById('asOnDate').value=='')
		{
			bootbox.alert("AsOn date is Required!");
		   document.getElementById('asOnDate').focus();
		   return;
		}
		document.getElementById('fromBean').value = 1;
		var fundObj=document.getElementById('trFund_id');
		if(fundObj.value!='' && fundObj.selectedIndex!=-1)
			document.getElementById('fundName').value=fundObj.options[fundObj.selectedIndex].text;	
		else
			document.getElementById('fundName').value='';		
		if(fundObj.value=='')
			document.getElementById('fundName').value="All Funds";
		endDate=document.getElementById("asOnDate").value;
		if(fundObj.value!='' && fundObj.selectedIndex!=-1)
			fuid=fundObj.options[fundObj.selectedIndex].value;		
	}
	else if(type=="dateRange")
	{
		if(document.getElementById('trFund_id').value=='')
		{
			bootbox.alert("Fund is Required!");
		   document.getElementById('trFund_id').focus();
		   return;
		}
		if(document.getElementById('startDate').value=='')
		{
			bootbox.alert("Start Date is Required!");
		   document.getElementById('startDate').focus();
		   return;
		}
		if(document.getElementById('toDate').value=='')
		{
			bootbox.alert("To Date is Required!");
		   document.getElementById('toDate').focus();
		   return;
		}
	
		document.getElementById('fromBean').value = 3;
		var fundObj=document.getElementById('trFund_id');
		document.getElementById('fundName').value=fundObj.options[fundObj.selectedIndex].text;
		endDate=document.getElementById("toDate").value;
		startDate=document.getElementById("startDate").value;
		fuid=fundObj.options[fundObj.selectedIndex].value
		
		 /* checking Fiscal year start Date with End Date of Fiscal year */	
		var tMonth=endDate.substr(endDate.length-7,2);
		if(tMonth<4)
		var fiscalYearStartDate="01/04/"+(endDate.substr(endDate.length-4,4)-1);
		else
		var fiscalYearStartDate="01/04/"+endDate.substr(endDate.length-4,4);
		if(compareDate(fiscalYearStartDate,startDate) == -1 )
		{ 
			bootbox.alert("Start Date and End Date should be in same financial year");
		   document.getElementById('startDate').focus();
		   return;
		}
		 /*to check whether the Start Date is greater than the End Date*/
		if( compareDate(formatDate6(startDate),formatDate6(endDate)) == -1 )
			{
			bootbox.alert('Start Date cannot be greater than End Date');
				document.getElementById('startDate').value='';
				document.getElementById('toDate').value='';
				document.getElementById('startDate').focus();
				return false;
			}
	
		   /*to check whether the End Date is greater than the Current Date*/
		if( compareDate(formatDate6("<%=currDate%>"),formatDate6(endDate)) == 1 )
		{
			bootbox.alert('End Date cannot be greater than Current Date');
			document.getElementById('toDate').value='';
			document.getElementById('toDate').value='';
		
		}
	}
		 
	 deptObj=document.getElementById('departmentId');
	 if(deptObj.value!='' && deptObj.selectedIndex!=-1){
			//deptName=deptObj.options[deptObj.selectedIndex].text;
			//bootbox.alert("DepartmentName="+deptName);
			 document.getElementById('dept_name_for').value=' in '+deptObj.options[deptObj.selectedIndex].text+ ' Department';
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
			document.getElementById('field_name_in').value=' in '+fieldObj.options[fieldObj.selectedIndex].text+' ';
			//fieldName=fieldObj.options[fieldObj.selectedIndex].text;
			//bootbox.alert("fieldName="+fieldName);
			//subtitle=" under "+fieldName+" Field";
		}else{
		document.getElementById('field_name_in').value='';
		}
		//functionCodeObj=document.getElementById('functionCodeId');
		functionCodeIdObj=document.getElementById('functionCode');
		
		if(functionCodeIdObj.value!=''){
			document.getElementById('functionCode_name').value=' in '+functionCodeIdObj.value;
		}else{
		document.getElementById('functionCode_name').value='';
		}
	document.forms[0].submit();
}
	
function afterRefreshPage(dc)
{
	if(dc.values['serviceID']=="finYearDate")
	{	
		var dt=dc.values['startFinDate'];
		dt=formatDate2(dt);
	}
	document.getElementById("startDate").value=dt;
}

function buttonFlush()
{

	window.location="TrialBalance.jsp";

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
	if(type=="asOnDate")
	{	
		document.getElementById('fromBean').value ="2";
	}
	else if(type=="dateRange")
		document.getElementById('fromBean').value ="4";

	document.forms[0].submit();
}
function buttonPrint()
{
	if(type=="asOnDate")
	{	
		document.getElementById('fromBean').value ="2";
	}
	else if(type=="dateRange")
		document.getElementById('fromBean').value ="4";
    var hide1,hide2hide3;
    hide1 = document.getElementById("tbl-header1");
	hide1.style.display = "none";
	hide2 = document.getElementById("TBR");
	hide3=document.getElementById("row3");
	hide3.style.display = "none";
	document.forms[0].submit();
  	if(window.print)
  	{
  		window.print();
	 }
}

</SCRIPT>
</head>
<body onLoad="onLoad()">
<form name="TrialBalanceReport" action = "TrialBalance.jsp?trFund_id="+fuid+"&asOnDate="+endDate method = "post">
<input type="hidden" name="fromBean" id="fromBean" value="0">
<input type="hidden" id="fundName" name="fundName" value="">
<input type="hidden" id="functionCode_name" name="functionCode_name" value="">
<input type="hidden" id="field_name_in" name="field_name_in" value="">
<input type="hidden" id="functionary_name_in" name="functionary_name_in" value="">
<input type="hidden" id="dept_name_for" name="dept_name_for" value="">

<center>
<br>
<table align='center' id="table2">
<tr><td>
<div id="main">
	<div id="m2">
		<div id="m3" style="width:810px">
		<div class="tbl-header1" id="tbl-header1">
			<table width="100%" border=0 cellpadding="3" cellspacing="0">
				<tr >
					<td colspan="4" class="tableheader" valign="center" colspan="4" width="100%"><span id="screenName">Trial Balance </span></td>
				</tr>
				<tr id="reportType"  style="display:block">
					<td align="right" ><div  valign="center" class="labelcell">Report Type<span class="leadon">* &nbsp;</span></div></td>
							<td width=25% colspan=3 class="smallfieldcell">
							<select class="fieldinput" name="type" id="type" onchange="getHeaderRow(this);">        
			        <option value="asOnDate">AsOn Date</option>
			        <option value="dateRange">Date Range</option></select>
					</td>
				</tr>
				<tr>
					<td align="right" id="oldReportFundTd1" style="display:none"><div  id="fund" valign="center" class="labelcell"></div></td>
					<td width=25% class="smallfieldcell" id="oldReportFundTd2" style="display:none" >
						<SELECT class="fieldinput" id="trFund_id" name="trFund_id" exilListSource="fundNameList" ></SELECT>
					</td >				
					<td align="right"  id="oldReportAsOnDateTd1"  style="display:none"><div valign="center" class="labelcell" >As On Date<span class="leadon">* &nbsp;</span></div></td>
					<td width=25% id="oldReportAsOnDateTd2"  style="display:none" class="smallfieldcell">
						<input name="asOnDate" id="asOnDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" >
					</td>
					<td id="td1" style="display: none"></td>
					<td id="td2" style="display: none"></td>
				</tr>
				<%@ include file="reportParameter.jsp" %>
				<tr id="newReportDateRangeRow" style="display:none">
					<td align="right"><div  valign="center" class="labelcell" >From Date<span class="leadon">* &nbsp;</span></div></td>
							<td width=25% class="smallfieldcell">
								<input name="startDate" id="startDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" exilMustEnter="true">
							</td>					
					<td align="right"><div  valign="center" class="labelcell" >To Date<span class="leadon">* &nbsp;</span></div></td>
							<td width=25% class="smallfieldcell">
								<input name="toDate" id="toDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" exilMustEnter="true">
							</td>				
				</tr>
				
			 	<tr id="row2" name="row2">
					<td colspan="4" align="middle">
						<table border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td align="right">
							<input type=button class=button onclick="ButtonPress()" href="#" value="Search"></td>
				
							<td align="right">
							<input type=button class=button onclick=buttonFlush(); href="#" value="Cancel"></td>
				
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
				<td colspan="4" align="middle">
					<table border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td align="right">
							<input type=button class=button onclick="history.go(-1)" href="#" value="Back"></td>
				
							<td align="right">
							<input type=button class=button onclick=buttonFlush(); href="#" value="Cancel"></td>
				
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
</div>
</div>
</div>
</div>
</td></tr>
</table>
<%
	Logger LOGGER = Logger.getLogger("TrialBalance.jsp");
    LOGGER.info("after submit "+request.getParameter("fromBean")+" date "+request.getParameter("asOnDate")+" fundid  "+request.getParameter("trFund_id")+" fundname  "+request.getParameter("fundName"));
	LOGGER.info("Depaartment ID >>>>>>>>>>>>>>>>>>>>> := "+ request.getParameter("departmentId"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
    {
	 try
	 {
		LOGGER.info("after submit "+request.getParameter("fromBean")+" date "+request.getParameter("asOnDate")+" fundid  "+request.getParameter("trFund_id")+" fundname  "+request.getParameter("fundName"));
		TrialBalance tbReport = new TrialBalance();
		request.setAttribute("tbreport",tbReport.getTBReport(request.getParameter("asOnDate"),request.getParameter("trFund_id"),request.getParameter("departmentId"),request.getParameter("functionaryId"),request.getParameter("functionCodeId"),request.getParameter("fieldId")));
		String fundList[]=tbReport.reqFundName;
		String fundIdList[]=tbReport.reqFundId;
		//System.out.println("tbreport::"+request.getAttribute("tbreport"));
		if((request.getAttribute("tbreport").toString()).length()<=2)
			 fundList=new String[0];

%>
	<center><u><b><div class = "normaltext">Trial Balance for <%= request.getParameter("fundName") %> as on <%= request.getParameter("asOnDate") %><%= request.getParameter("dept_name_for") %> 
			<%= request.getParameter("functionCode_name") %><%= request.getParameter("functionary_name_in") %> <%= request.getParameter("field_name_in") %> <br></br></div></b></u></center>
	<div class="normaltext" align="Left" >Report Run Date:<%= currDate %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Amount in Rupees </div>
	<div class="tbl-container" id="tbl-container">
	<display:table cellspacing="0" name="tbreport" uid="currentRowObject" export="true"  sort="list" class="its" id="TBR">
	 <div STYLE="display:table-header-group">
	 <display:caption  media="pdf">
		Trial Balance Report for <%= request.getParameter("fundName") %> as on <%= request.getParameter("asOnDate") %> <%= request.getParameter("dept_name_for") %> 
		<%= request.getParameter("functionCode_name") %> <%= request.getParameter("functionary_name_in") %> <%= request.getParameter("field_name_in") %>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		Report Run Date:<%= currDate %> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Amount in Rupees
	</display:caption>
	<display:column media="pdf" property ="accCode" title="Account Code" />
	<display:column media="excel" property="accCode" title="Account Code" />
	
	<%
		int k=1;
		if(k==fundList.length)
		{
	%>
	<display:column media="html" title="Account&nbsp;Code" style="width:90px">
	 <%
	 	String snapShotDateTime=tbReport.getDateTime();
	 	String code=(((HashMap)pageContext.getAttribute("TBR")).get("accCode").toString());
	 	String functionaryStr="";
	 	String functionCodeStr="";
	 	String fieldStr="";
	 	if(request.getParameter("functionaryId")!=null || !request.getParameter("functionaryId").equals(""))
	 	{
	 	functionaryStr="&functionaryId="+request.getParameter("functionaryId");
	 	}
	 	if(request.getParameter("functionCodeId")!=null || !request.getParameter("functionCodeId").equals(""))
	 	{
	 	functionCodeStr="&functionCodeId="+request.getParameter("functionCodeId");
	 	}
	 	if(request.getParameter("fieldId")!=null || !request.getParameter("fieldId").equals(""))
	 	{
	 	fieldStr="&fieldId="+request.getParameter("fieldId");
	 	}
	 	//System.out.println("code::"+code);
	    String link = "javascript:"+"void(window.open('../Reports/GeneralLedger.jsp?reportType=generalledger&forRevEntry=on&isDrillDown=Y&glCode1="+code+"&glCode2="+code+"&fund_id="+request.getParameter("trFund_id")+"&departmentId="+request.getParameter("departmentId")+
	    functionaryStr+functionCodeStr+fieldStr+"&startDate=null&endDate="+request.getParameter("asOnDate")+"&fromBean=1&snapShotDateTime="+snapShotDateTime+"&submit=submit"+"'"+",'','height=650,width=1000,scrollbars=yes,left=20,top=20,status=yes'"+"))";
	    String url = ((HashMap)pageContext.getAttribute("TBR")).get("accCode").toString();
		%>
	 <a href="<%=link%>"><%=url%></a>
	</display:column>
	<%}
	else
	{
	%>
	<display:column media="html" property="accCode" title="Account&nbsp;Code" style="width:90px" />
	<%}%>
	
	
	<display:column property="accName" title="<center>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Account&nbsp;Head&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</center>"/>
	<%
	for(int i=0;i<fundList.length;i++)
	{
		int j=1;
		if(j==fundList.length)
		{
		Object drAmt=((HashMap)pageContext.getAttribute("TBR")).get("debitAmount"+fundIdList[i]);
		if(drAmt==null) drAmt="<hr noshade color=black size=1><b>0.00</b><hr noshade color=black size=1>";

		Object crAmt=((HashMap)pageContext.getAttribute("TBR")).get("creditAmount"+fundIdList[i]);
		if(crAmt==null) crAmt="<hr noshade color=black size=1><b>0.00</b><hr noshade color=black size=1>";

			{
				%>

				<display:column class="textAlign3" title="<table ><tr><td class=textAlign4><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Debit&nbsp;(Rs.)<b></td><td class=textAlign4><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Credit&nbsp;(Rs.)<b></td></tr></table>">
					<table ><tr><td class="textAlign4">
						<%=drAmt %>
					</td>
					<td class="textAlign4">
						<%=crAmt %>
					</td></tr></table>
				</display:column>
				<%

			}
		}
		else
		{
		Object drAmt=((HashMap)pageContext.getAttribute("TBR")).get("debitAmount"+fundIdList[i]);
		if(drAmt==null) drAmt="<hr noshade color=black size=1><b>0.00</b><hr noshade color=black size=1>";

		Object crAmt=((HashMap)pageContext.getAttribute("TBR")).get("creditAmount"+fundIdList[i]);
		if(crAmt==null) crAmt="<hr noshade color=black size=1><b>0.00</b><hr noshade color=black size=1>";

			{
				%>

				<display:column class="textAlign" title="<%= "<center><b>"+fundList[i] +"</center></b><br><table ><tr><td class=textAlign4><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Debit&nbsp;(Rs.)<b></td><td class=textAlign4><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Credit&nbsp;(Rs.)<b></td></tr></table>"%>">
					<table ><tr><td class="textAlign4">
						<%=drAmt %>
					</td>
					<td class="textAlign4">
						<%=crAmt %>
					</td></tr></table>
				</display:column>
				<%

			}
		}
	}
	%>

	<% if(fundList.length!=1)
	{
	%>
	 <display:column class="textAlign" title="<b><br><br><center>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Total&nbsp;Debit&nbsp;(Rs.)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</center></b>">
	<%= ((HashMap)pageContext.getAttribute("TBR")).get("drTotal")  %>
	</display:column>
	<display:column class="textAlign" title="<b><br><br><center>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Total&nbsp;Credit&nbsp;(Rs.)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</center></b>">
	<%= ((HashMap)pageContext.getAttribute("TBR")).get("crTotal")  %>
	</display:column>
	<%
			 }
	%>
	<display:setProperty name="basic.empty.showtable" value="true" />
	<display:caption  media="pdf">
		Trial Balance Report for <%= request.getParameter("fundName") %> as on <%= request.getParameter("asOnDate") %> <%= request.getParameter("dept_name_for") %> 
		<%= request.getParameter("functionCode_name") %><%= request.getParameter("functionary_name_in") %> <%= request.getParameter("field_name_in") %>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
		Report Run Date <%= currDate %> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Amount in Rupees
	</display:caption>
	<display:setProperty name="export.pdf" value="true" />
	<display:setProperty name="export.pdf.filename" value="TrialBalance.pdf" /> 
    <display:setProperty name="export.excel" value="true" />
    <display:setProperty name="export.excel.filename" value="TrialBalance.xls"/>
    

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
		 catch(Exception e){
		 LOGGER.error("Exception in Jsp Page "+ e.getMessage());
		 %>
		 <script>
		 bootbox.alert("Error :<%=e.getMessage()%>");
		 PageManager.DataService.setQueryField('asOnDate',null);
		 </script>
		 <%
		 }
	}
%>

<%
    LOGGER.info("after submit "+request.getParameter("fromBean")+" date "+request.getParameter("asOnDate")+" fundid  "+request.getParameter("trFund_id")+" fundname  "+request.getParameter("fundName"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("2"))
    {
	 try
	 {
		LOGGER.info("after submit "+request.getParameter("fromBean")+" date "+request.getParameter("asOnDate")+" fundid  "+request.getParameter("trFund_id")+" fundname  "+request.getParameter("fundName"));
		TrialBalance tbReport = new TrialBalance();
		request.setAttribute("tbreport",tbReport.getTBReport(request.getParameter("asOnDate"),request.getParameter("trFund_id"),request.getParameter("departmentId"),request.getParameter("functionaryId"),request.getParameter("functionCodeId"),request.getParameter("fieldId")));
		String fundList[]=tbReport.reqFundName;
		String fundIdList[]=tbReport.reqFundId;
		LOGGER.info("tbreport::"+request.getAttribute("tbreport"));
		if((request.getAttribute("tbreport").toString()).length()<=2)
			 fundList=new String[0];
%>
	<center><u><b><div class = "normaltext">Trial Balance for <%= request.getParameter("fundName") %> as on <%= request.getParameter("asOnDate") %><br><bean:message key="TrialBal.Name"/><%= request.getParameter("dept_name_for") %> 
			<%= request.getParameter("functionCode_name") %>	<%= request.getParameter("functionary_name_in") %> <%= request.getParameter("field_name_in") %> </br></div></b></u></center>
	<div class = "normaltext" align="Left">Report Run Date<%= currDate %> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Amount in Rupees </div>   
	<div class="tbl5-container" id="tbl-container">
	<display:table cellspacing="0"  name="tbreport"  decorator="org.displaytag.decorator.TotalTableDecorator" uid="currentRowObject" export="false" class="its" id="TBR" >
	<div STYLE="display:table-header-group">
	<display:caption  media="pdf">
		Trial Balance Report for <%= request.getParameter("fundName") %> from <%= request.getParameter("startDate") %> to <%= request.getParameter("toDate") %> <%= request.getParameter("dept_name_for") %> 
		<%= request.getParameter("functionCode_name") %><%= request.getParameter("functionary_name_in") %> <%= request.getParameter("field_name_in") %>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;shamili  
		Report Run Date <%= currDate %> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Amount in Rupees
	</display:caption>
	
	
	<display:column  property ="accCode" title="Account&nbsp;Code" style="width:90px"/>	
	<display:column property="accName" style="border-right:0px;FONT-SIZE: 9px" title="<center>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Account&nbsp;Head&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</center>"/>
		<%
		for(int i=0;i<fundList.length;i++)
		{
			int j=1;
			if(j==fundList.length)
			{
			Object drAmt=((HashMap)pageContext.getAttribute("TBR")).get("debitAmount"+fundIdList[i]);
			if(drAmt==null) drAmt="<hr noshade color=black size=1><b>0.00</b><hr noshade color=black size=1>";

			Object crAmt=((HashMap)pageContext.getAttribute("TBR")).get("creditAmount"+fundIdList[i]);
			if(crAmt==null) crAmt="<hr noshade color=black size=1><b>0.00</b><hr noshade color=black size=1>";

				{
					%>

					<display:column class="textAlign3" title="<table ><tr><td class=textAlign4><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Debit&nbsp;(Rs.)<b></td><td class=textAlign4><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Credit&nbsp;(Rs.)<b></td></tr></table>">
						<table ><tr><td class="textAlign7">
							<%=drAmt %>
						</td>
						<td class="textAlign7">
							<%=crAmt %>
						</td></tr></table>
					</display:column>
					<%

				}
			}


			else
			{
			Object drAmt=((HashMap)pageContext.getAttribute("TBR")).get("debitAmount"+fundIdList[i]);
			if(drAmt==null) drAmt="<hr noshade color=black size=1><b>0.00</b><hr noshade color=black size=1>";

			Object crAmt=((HashMap)pageContext.getAttribute("TBR")).get("creditAmount"+fundIdList[i]);
			if(crAmt==null) crAmt="<hr noshade color=black size=1><b>0.00</b><hr noshade color=black size=1>";

				{
					%>

					<display:column class="textAlign" title="<%= "<center><b>"+fundList[i] +"</center></b><br><table ><tr><td class=textAlign4><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Debit&nbsp;(Rs.)<b></td><td class=textAlign4><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Credit&nbsp;(Rs.)<b></td></tr></table>"%>">
						<table ><tr><td class="textAlign7">
							<%=drAmt %>
						</td>
						<td class="textAlign7">
							<%=crAmt %>
						</td></tr></table>
					</display:column>
					<%

				}
			}
		}
		%>

		<% if(fundList.length!=1)
		{
		%>
		 <display:column class="textAlign8" title="<b><br><br><center>&nbsp;&nbsp;&nbsp;&nbsp;Total&nbsp;Debit&nbsp;(Rs.)&nbsp;&nbsp;&nbsp;</center></b>">
		<%= ((HashMap)pageContext.getAttribute("TBR")).get("drTotal")  %>
		</display:column>
		<display:column class="textAlign5" title="<b><br><br><center>&nbsp;&nbsp;&nbsp;&nbsp;Total&nbsp;Credit&nbsp;(Rs.)&nbsp;&nbsp;&nbsp;</center></b>">
		<%= ((HashMap)pageContext.getAttribute("TBR")).get("crTotal")  %>
	</display:column>
	<%
			 }
	%>
	 <display:setProperty name="basic.empty.showtable" value="true" /> 
	<display:caption  media="pdf">
		Trial Balance Report for <%= request.getParameter("fundName") %> from <%= request.getParameter("startDate") %> to <%= request.getParameter("toDate") %> <%= request.getParameter("dept_name_for") %> 
			<%= request.getParameter("functionCode_name") %><%= request.getParameter("functionary_name_in") %> <%= request.getParameter("field_name_in") %>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
		Report Run Date <%= currDate %> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Amount in Rupees
	</display:caption>
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
		 catch(Exception e){
		 LOGGER.error("Exception in Jsp Page "+ e.getMessage());
		 %>
		 <script>
		 bootbox.alert("Error :<%=e.getMessage()%>");
		 PageManager.DataService.setQueryField('asOnDate',null);
		 </script>
		 <%
		 }
	}
%>
<% 
	LOGGER.info("after submit "+request.getParameter("fromBean")+" fundid "+request.getParameter("trFund_id")+" fundname  "+request.getParameter("fundName"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("3"))
    {
	 try
	 {	TrialBalance tbReport = new TrialBalance();
		request.setAttribute("tbreport",tbReport.getTBReportForDateRange(request.getParameter("startDate"),request.getParameter("toDate"),request.getParameter("trFund_id"),request.getParameter("departmentId"),request.getParameter("functionaryId"),request.getParameter("functionCodeId"),request.getParameter("fieldId")));
%>
	<!-- JSP Header for date range --> 
	<center><u><b><div class = "normaltext">Trial Balance For <%= request.getParameter("fundName") %> from <%= request.getParameter("startDate") %> to <%= request.getParameter("toDate") %><%= request.getParameter("dept_name_for") %> 
	<%= request.getParameter("functionCode_name") %><%= request.getParameter("functionary_name_in") %> <%= request.getParameter("field_name_in") %> </div></b></u></center>
	<div class = "normaltext" align="Left">Report Run Date:<%=currDate%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Amount in Rupees </div>
	<div class="tbl-container" id="tbl-container">
	<display:table cellspacing="0" name="tbreport"  decorator="org.displaytag.decorator.TotalTableDecorator" id="currentRowObject" export="true"  sort="list" class="its">
	<display:caption media="pdf">Trial Balance Report for <%= request.getParameter("fundName") %> from <%= request.getParameter("startDate") %> to <%= request.getParameter("toDate") %> <%= request.getParameter("dept_name_for") %> 
		<%= request.getParameter("functionCode_name") %><%= request.getParameter("functionary_name_in") %> <%= request.getParameter("field_name_in") %>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
		Report Run Date <%= currDate %> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Amount in Rupees 
	</display:caption>
	<div STYLE="display:table-header-group">
	<display:column media="pdf" title="Account&nbsp;Code" property="accCode"/> 
	<display:column media="excel" title="Account&nbsp;Code"  property="accCode"/> 
	<display:column media="html" title="Account&nbsp;Code"> 
	
	 <% 
	    String snapShotDateTime=tbReport.getDateTime();
	    String functionaryStr="";
	 	String functionCodeStr="";
	 	String fieldStr="";
	 	if(request.getParameter("functionaryId")!=null || !request.getParameter("functionaryId").equals(""))
	 	{
	 	functionaryStr="&functionaryId="+request.getParameter("functionaryId");
	 	}
	 	if(request.getParameter("functionCodeId")!=null || !request.getParameter("functionCodeId").equals(""))
	 	{
	 	functionCodeStr="&functionCodeId="+request.getParameter("functionCodeId");
	 	}
	 	if(request.getParameter("fieldId")!=null || !request.getParameter("fieldId").equals(""))
	 	{
	 	fieldStr="&fieldId="+request.getParameter("fieldId");
	 	}
	    String code=((TrialBalanceBean)pageContext.getAttribute("currentRowObject")).getAccCode();
	    String link = "javascript:"+"void(window.open('../Reports/GeneralLedger.jsp?reportType=generalledger&forRevEntry=on&isDrillDown=Y&glCode1="+code+"&glCode2="+code+"&fund_id="+request.getParameter("trFund_id")+"&departmentId="+request.getParameter("departmentId")+
	    functionaryStr+functionCodeStr+fieldStr+"&startDate="+request.getParameter("startDate")+"&endDate="+request.getParameter("toDate")+"&fromBean=1&snapShotDateTime="+snapShotDateTime+"&submit=submit"+"'"+",'','height=650,width=900,scrollbars=yes,left=20,top=20,status=yes'"+"))";
	    String url = ((TrialBalanceBean)pageContext.getAttribute("currentRowObject")).getAccCode();
		%>
	<a href="<%=link%>"><%=url%></a>
	</display:column>
	
	<display:column property="accName" title="Account Name"  /> 
	<display:column property="openingBal"   title="Opening Balance"   class="textAlign"  /> 
	<display:column property="debit"   title="Debit"    class="textAlign"/> 
	<display:column property="credit"  title="Credit"    class="textAlign"/> 
	<display:setProperty name="basic.empty.showtable" value="true" />
	<display:column property="closingBal"  title="Closing Balance"  class="textAlign3" />	
	<display:caption  media="pdf">  <!-- Report As on Date PDF header-->
		Trial Balance Report for <%= request.getParameter("fundName") %> from <%= request.getParameter("startDate") %> to <%= request.getParameter("toDate") %> <%= request.getParameter("dept_name_for") %> 
		<%= request.getParameter("functionCode_name") %><%= request.getParameter("functionary_name_in") %> <%= request.getParameter("field_name_in") %>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		Report Run Date <%= currDate %> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Amount in Rupees 
	</display:caption>
	<display:setProperty name="export.pdf" value="true" />
	<display:setProperty name="export.pdf.filename" value="TrialBalance.pdf" /> 
    <display:setProperty name="export.excel" value="true" />
    <display:setProperty name="export.excel.filename" value="TrialBalance.xls"/>
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
		 catch(Exception e){
		 LOGGER.error("Exception in Jsp Page "+ e.getMessage());
		 %> 
		
		 <%
		 }
	}  
%>

<% 
	LOGGER.info("after submit "+request.getParameter("fromBean")+" fundid "+request.getParameter("trFund_id")+" fundname  "+request.getParameter("fundName"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("4"))
    {
	 try
	 {	TrialBalance tbReport = new TrialBalance();
		request.setAttribute("tbreport",tbReport.getTBReportForDateRange(request.getParameter("startDate"),request.getParameter("toDate"),request.getParameter("trFund_id"),request.getParameter("departmentId"),request.getParameter("functionaryId"),request.getParameter("functionCodeId"),request.getParameter("fieldId")));
%>		
	<center><u><b><div class = "normaltext">Trial Balance For <%= request.getParameter("fundName") %> from <%= request.getParameter("startDate") %> to <%= request.getParameter("toDate") %> <%= request.getParameter("dept_name_for") %> 
			<%= request.getParameter("functionCode_name") %><%= request.getParameter("functionary_name_in") %> <%= request.getParameter("field_name_in") %></div></b></u></center>
	<div class = "normaltext" align="Left">&nbsp;&nbsp;&nbsp;Report Run Date:&nbsp;<%=currDate %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Amount in Rupees &nbsp;&nbsp;&nbsp;</div>
	<div class="tbl5-container" id="tbl-container" style="width:600px">
	<display:table cellspacing="0" name="tbreport"  id="currentRowObject" export="false"  sort="list" class="its">
	<div STYLE="display:table-header-group">
	<display:column media="html" property="accCode" style="width:80px" title="<font size=1><center>Account&nbsp;Code</center></font>" class="textAlign9"/> 	
	<display:column property="accName" style="width:300px" title="<font size=1><center>Account Name</center></font>"  class="textAlign9" /> 
	<display:column property="openingBal" style="width:120px"  title="<font size=1><center>Opening Balance</center></font>"   class="textAlign8"  /> 
	<display:column property="debit"  style="width:100px" title="<font size=1><center>Debit</center></font>"    class="textAlign8"/> 
	<display:column property="credit"  style="width:100px" title="<font size=1><center>Credit</center></font>"    class="textAlign8"/>	
	<display:column property="closingBal" style="width:120px" title="<font size=1><center>Closing Balance</center></font>"  class="textAlign5" />	
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
		 catch(Exception e){
		 LOGGER.error("Exception in Jsp Page "+ e.getMessage());
		 %> 
		
		 <%
		 }
	}  
%>
</form>
</body>
</html>
