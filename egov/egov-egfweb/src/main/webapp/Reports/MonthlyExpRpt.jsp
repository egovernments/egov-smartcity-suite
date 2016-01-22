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
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@page  import="com.exilant.eGov.src.reports.LongAmountWrapper,com.exilant.eGov.src.reports.*,org.apache.log4j.Logger,java.io.*,java.util.*,java.sql.*,javax.sql.*,javax.naming.InitialContext,com.exilant.GLEngine.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<!-- Inclusion of the CSS files that contains the styles -->
<link rel=stylesheet href="../exility/global.css" type="text/css">
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
<script language="javascript" src="../resources/javascript/jsCommonMethods.js"></script>
<link rel="stylesheet" href="../exility/screen.css" type="text/css" media="screen, print" />
<SCRIPT LANGUAGE="javascript">

var rptTitle='', rptMonthName='',  rptYearName='', rptZoneName='';

//onLoad

function onBodyLoad() 
{
PageManager.ListService.callListService();
	if(PageManager.DataService.getQueryField('monthName') != null && PageManager.DataService.getQueryField('yearName') != null)
		{
		document.getElementById('monthName').value=PageManager.DataService.getQueryField('monthName');
		document.getElementById('yearName').value=PageManager.DataService.getQueryField('yearName');
		document.getElementById('zoneName').value=PageManager.DataService.getQueryField('zoneName');
		}
	}

//buttonflush

function buttonFlush()
{
	document.getElementById('monthName').value='';
	document.getElementById('yearName').value='';
	document.getElementById('zoneName').value='';
	
}

//buttonpress

function ButtonPress(){
	
	
	if (!PageValidator.validateForm())
			return ;
	 		
	var mnName = document.getElementById('monthName');
	rptMonthName=mnName.options[mnName.selectedIndex].value;
	
	var yrName = document.getElementById('yearName');
	rptYearName=yrName;
	
	var znName = document.getElementById('zoneName');
	rptZoneName = znName;
		
	
	if(mnName.options[mnName.selectedIndex].text = ""){
		bootbox.alert('Select the Month');
	document.getElementById('monthName').value;
	return false;
	}
	
	if(mnName.options[mnName.selectedIndex].text != ""){
	PageManager.DataService.setQueryField('monthName', mnName.options[mnName.selectedIndex].value);
	}
	
	if(document.getElementById('yearName').text = ""){
		bootbox.alert('Select the Year');
	document.getElementById('yearName').value;
	return;
	}	
	
	if(document.getElementById('yearName').text != ""){
	PageManager.DataService.setQueryField('yearName', document.getElementById('yearName').value);
	}
	PageManager.DataService.setQueryField('zoneName',rptZoneName);	
}


</SCRIPT>
</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onload="onBodyLoad()">

 <jsp:useBean id = "reportBean" scope ="page" class="com.exilant.eGov.src.reports.MonthlyExpRptBean"/>
 <jsp:setProperty name = "reportBean" property="*"/>

<%
	Logger LOGGER = Logger.getLogger("MonthlyExpRpt.jsp");
    try
	
	{	
		MonthlyExpRptList monthlyExpList = new MonthlyExpRptList();
	 	request.setAttribute("links",monthlyExpList.getMonthlyExpRptList(reportBean));
	 	
   	}catch(Exception e){ LOGGER.info("Exception in Jsp Page:"+e); }
%>


<form name="monexpVoucher" action = "MonthlyExpRpt.jsp?yearName="+rptYearName+"&monthName="+rptMonthName+"&zoneName="+rptZoneName method = "post">

<table width="100%" border=0 cellpadding="3" cellspacing="0" id="mainTable">
	<!-- row-0 -->
		<tr>
			<td class="rowheader" valign="center" width="100%" colspan="4"><span id="screenName" class="headerwhite2">Books of Accounts - Money Expenditure (Non-plan) Report</span><span id="partyName" class="headerwhite2"></span></td>
		</tr>
	
		<!-- row-1 -->
		<tr class="row2">
			
			<td><div align="right" valign="center" class="normaltext">Year&nbsp;<span class="leadon">*</span></div></td>
				<td >
					<SELECT class="fieldinput" id=yearName name="yearName" exilListSource="yearNameList" exilMustEnter="true"></SELECT>
				</td>
			<td><div align="right" valign="center" class="normaltext">Month&nbsp;<span class="leadon">*</span></div></td>
			
			<td >
				<SELECT class="fieldinput" id=monthName name="monthName" exilMustEnter="true">
				<option value = "" selected>&nbsp;</option>
				<option value = "1">January</option>
				<option value = "2">February</option>
				<option value = "3">March</option>
				<option value = "4">April</option>
				<option value = "5">May</option>
				<option value = "6">June</option>
				<option value = "7">July</option>
				<option value = "8">August</option>
				<option value = "9">September</option>
				<option value = "10">October</option>
				<option value = "11">November</option>
				<option value = "12">December</option>
				</SELECT>
			</td>
			
		
		</tr>
		
		<tr class="row1">
			<td colspan=2><div align="right" valign="center" class="normaltext">Zone&nbsp</div></td>
			<td >
				<SELECT class="fieldinput" id=zoneName name="zoneName" exilListSource="zoneList"></SELECT>
			</td>
		</tr>
			
		<tr class="row2">
			
			<td colspan="4" align="middle"><!-- Buttons Start Here -->
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td ></td><td></td><td></td><td ></td><td></td><td></td>
						<td ><div align="right" valign="center" class="normaltext" ><input type="submit" value = "Search" name ="submit" class="normaltext" onclick=ButtonPress()></td>
						<td ><div align="middle" valign="center" class="normaltext" ><CENTER><INPUT TYPE="submit" value = "Clear" name = "flush"class="normaltext" onclick=buttonFlush()></CENTER></td>
						<td ><div align="left" valign="center" class="normaltext" ><input type="submit" value = "Show All" name = "printView"class="normaltext" onclick=ButtonPress()></td>					
						<td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td ></td><td></td><td></td><td ></td><td></td><td></td>
					</tr>
				</table>
			</td>
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
		<!------------------ Monthly Expenditure Data ------------------>
	</table>
</form>

<%
	String submitted="";
	submitted = (String) request.getParameter("flush");
	if (submitted != null)
		{
		//out.println("HI");
		}
%>


<%
	submitted = (String) request.getParameter("submit");
	if (submitted != null && !request.getParameter("monthName").equals("")&& !request.getParameter("yearName").equals("")&& !request.getParameter("zoneName").equals(""))
	{

%>

<display:table name="links" export="true" id="currentRowObject"  class="its" pagesize="10">
		
		
	 <display:column property="deptCode"  title="Dept Code" group="1" />
		 <display:column property="accCode"  title="Acc Code"  group="2" class="textAlign" />
		 <display:column property="nomenName" title="Nomenclature" group="3"  />
		 <display:column property="budEstimate" title="Budget Estimate" group="4" class="textAlign" />
		 <display:column property="expPrev" title="Expenditure upto the end of previous month (including unpaid bills) " group="5" class="textAlign" />
		 <display:column property="payPrev" title="Paid upto the previous month" group="6" class="textAlign" />
		 <display:column property="expCurr" title="Expenditure during the month " group="7" class="textAlign" />
		 <display:column property="payCurr" title="Payment made during the month" group="8" class="textAlign" />
		 <display:column property="progExp" title="Progressive Expenditure upto end of month" group="9" class="textAlign" />
		 <display:column property="liaAccured" title="Liabilities" group="10" class="textAlign" />
		 <display:setProperty name="basic.empty.showtable" value="true" />
		<display:setProperty name="export.pdf" value="true" />

		
</display:table> 
<% } %> 
<%

submitted = (String) request.getParameter("submit");
	if (submitted != null && !request.getParameter("monthName").equals("")&& !request.getParameter("yearName").equals("")&& request.getParameter("zoneName").equals(""))
	{

%>
<display:table name="links" export="true" id="currentRowObject"  class="its" pagesize="10">
		
		
	 <display:column property="deptCode"  title="Dept Code" group="1" />
		 <display:column property="accCode"  title="Acc Code"  group="2" class="textAlign" />
		 <display:column property="nomenName" title="Nomenclature" group="3"  />
		 <display:column property="budEstimate" title="Budget Estimate" group="4" class="textAlign" />
		 <display:column property="expPrev" title="Expenditure upto the end of previous month (including unpaid bills) " group="5" class="textAlign" />
		 <display:column property="payPrev" title="Paid upto the previous month" group="6" class="textAlign" />
		 <display:column property="expCurr" title="Expenditure during the month " group="7" class="textAlign" />
		 <display:column property="payCurr" title="Payment made during the month" group="8" class="textAlign" />
		 <display:column property="progExp" title="Progressive Expenditure upto end of month" group="9" class="textAlign" />
		 <display:column property="liaAccured" title="Liabilities" group="10" class="textAlign" />
		 <display:setProperty name="basic.empty.showtable" value="true" />
		<display:setProperty name="export.pdf" value="true" />
		
</display:table> 
<% } %> 

<%

	submitted = (String) request.getParameter("printView");
	if (submitted != null && !request.getParameter("monthName").equals("")&& !request.getParameter("yearName").equals("")&& request.getParameter("zoneName").equals(""))
		{ %>

<display:table name="links" export="true" id="currentRowObject"  class="its" pagesize="10">

	 <display:column property="deptCode"  title="Dept Code" group="1" />
		 <display:column property="accCode"  title="Acc Code"  group="2" class="textAlign" />
		 <display:column property="nomenName" title="Nomenclature" group="3" />
		 <display:column property="budEstimate" title="Budget Estimate" group="4" class="textAlign" />
		 <display:column property="expPrev" title="Expenditure upto the end of previous month (including unpaid bills) " group="5" class="textAlign" />
		 <display:column property="payPrev" title="Paid upto the previous month" group="6" class="textAlign" />
		 <display:column property="expCurr" title="Expenditure during the month " group="7" class="textAlign" />
		 <display:column property="payCurr" title="Payment made during the month" group="8" class="textAlign" />
		 <display:column property="progExp" title="Progressive Expenditure upto end of month" group="9" class="textAlign" />
		 <display:column property="liaAccured" title="Liabilities" group="10" class="textAlign" /> 
		 <display:setProperty name="basic.empty.showtable" value="true" />
		<display:setProperty name="export.pdf" value="true" />
		

</display:table> 
<% } %>

<%

	submitted = (String) request.getParameter("printView");
	if (submitted != null && !request.getParameter("monthName").equals("")&& !request.getParameter("yearName").equals("")&& !request.getParameter("zoneName").equals(""))
		{ %>

<display:table name="links" export="true" id="currentRowObject"  class="its" pagesize="10">

	 	<display:column property="deptCode"  title="Dept Code" group="1" />
		 <display:column property="accCode"  title="Acc Code"  group="2" class="textAlign" />
		 <display:column property="nomenName" title="Nomenclature" group="3" />
		 <display:column property="budEstimate" title="Budget Estimate" group="4" class="textAlign" />
		 <display:column property="expPrev" title="Expenditure upto the end of previous month (including unpaid bills) " group="5" class="textAlign" />
		 <display:column property="payPrev" title="Paid upto the previous month" group="6" class="textAlign" />
		 <display:column property="expCurr" title="Expenditure during the month " group="7" class="textAlign" />
		 <display:column property="payCurr" title="Payment made during the month" group="8" class="textAlign" />
		 <display:column property="progExp" title="Progressive Expenditure upto end of month" group="9" class="textAlign" />
		 <display:column property="liaAccured" title="Liabilities" group="10" class="textAlign" /> 
		 <display:setProperty name="basic.empty.showtable" value="true" />
		<display:setProperty name="export.pdf" value="true" />
		
</display:table> 
<% } %>


	
</body>
</html>

