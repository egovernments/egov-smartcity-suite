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
<title>Opening Balance Report</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">

<!-- Inclusion of the CSS files that contains the styles -->
<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen" />
<link rel=stylesheet href="../css/print.css" type="text/css" media="print" />

<!-- <link rel=stylesheet href="../exility/global.css" type="text/css">   -->
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
<script language="javascript" src="../resources/javascript/jsCommonMethods.js"></script>
<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js" type="text/javascript" ></SCRIPT>
<!-- <link rel="stylesheet" href="../css/lockRowHead.css" type="text/css" />  -->
<!--  <link rel="stylesheet" href="../exility/screen.css" type="text/css" media="screen, print" /> -->
<SCRIPT LANGUAGE="javascript">

var finYear="";
var fuid="";
var deptid="";
function onLoad()
{
	PageValidator.addCalendars();
	PageManager.ListService.callListService();
	var beanVal= <%=request.getParameter("fromBean")%>;
	if(beanVal==2)
	{
		document.getElementById("row3").style.display="block";
		document.getElementById("row2").style.display="none";		
	}
	document.getElementById("obFund_id").focus();
}

function ButtonPress()
{

	if (!PageValidator.validateForm())
		return;
	document.getElementById('fromBean').value = 1;
	obj=document.getElementById("finYear");
	finYear=obj.value;
	document.getElementById('yearText').value =obj.options[obj.selectedIndex].text;
	var fundObj=document.getElementById('obFund_id');
	if(fundObj.value!='' && fundObj.selectedIndex!=-1)
		fuid=fundObj.options[fundObj.selectedIndex].value
	var deptObj=document.getElementById('deptId');
	if(deptObj.value!='' && deptObj.selectedIndex!=-1)
		deptid=deptObj.options[deptObj.selectedIndex].value
	document.forms[0].submit();


}
function buttonFlush()
{


	window.location="OpeningBalance.jsp";

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
    var hide1; 
    hide1 = document.getElementById("tbl-header1");
	hide1.style.display = "none";	
	document.forms[0].submit();
  	if(window.print)
  		{
  		window.print();
	  	}
}	


</SCRIPT>
<title> Opening Balance Report</title>
</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onLoad="onLoad()">
<jsp:useBean id = "OBBean" scope ="request" class="com.exilant.eGov.src.reports.OpeningBalanceInputBean"/>
<jsp:setProperty name ="OBBean" property="obFund_id"/>
<jsp:setProperty name ="OBBean" property="deptId"/>
<jsp:setProperty name ="OBBean" property="finYear"/>
<form name="OpeningBalance" action = "OpeningBalance.jsp?obFund_id="+fuid+"&finYear="+finYear+"&deptId="+deptid method = "post">
<input type="hidden" name="fromBean" id="fromBean" value="0">
<input type="hidden" name="yearText" id="yearText" value="">

<div class= "tbl-header1" id="tbl-header1">
<center>
<br>
<table align='center' id="table2">
<tr><td>
<div id="main"><div id="m2"><div id="m3" style="width:810px">


<table width="100%" border=0 cellpadding="3" cellspacing="0">
<tr >
	<td colspan="4" class="tableheader" valign="center" colspan="4" width="100%"><span id="screenName">Opening Balance Report</span></td>
</tr>
	<tr>
				<td  align="right"><div valign="center" class="labelcell">Fund &nbsp;</div></td>
				<td width=25% class="smallfieldcell">
					<SELECT class="fieldinput" id="obFund_id" name="obFund_id" exilListSource="fundNameList" ></SELECT>
				</td >
				<td align="right"><div  valign="center" class="labelcell" >Financial Year<span class="leadon">* &nbsp;</span></div></td>
				<td width=25%>
				<div align="left" class="smallfieldcell">
					    <SELECT id="finYear"  name="finYear" class="fieldinput" exilListSource="financialYearList" exilMustEnter="true" >
					    </SELECT></div>
				</td>

		</tr>
		<tr>
				<td><div align="right" valign="center" class="labelcell">Department</div></td>
				<td align="left"><span class="smallfieldcell">
					<SELECT class="fieldinput" id="deptId" name="deptId" class="fieldinput" exilListSource="departmentName"></SELECT>
				</td>
		</tr>
		
	<tr height="5"><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
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
			<input type=button class=button onclick="ButtonPress()" href="#" value="Search"></td>
			
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

<br>
</div></div></div>
</td></tr>
</table>
</div>
<%
	Logger LOGGER = Logger.getLogger("OpeningBalance.jsp");
    LOGGER.info("after submit "+request.getParameter("fromBean")+" finYear "+request.getParameter("finYear")+" fundid  "+request.getParameter("obFund_id")+" deptid  "+request.getParameter("deptId"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
    {
	 try
	 {
		LOGGER.info("after submit "+request.getParameter("fromBean")+" finYear "+request.getParameter("finYear")+" fundid  "+request.getParameter("obFund_id")+" deptid  "+request.getParameter("deptId"));
		OpeningBalance obReport = new OpeningBalance();
		request.setAttribute("obReport",obReport.getOBReport(OBBean));
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
%>
	 
	<u><b><div class = "normaltext">Opening Balance for the Year <%= request.getParameter("yearText") %></div></b></u>
	<div class="tbl2-container" id="tbl-container">
	<display:table cellspacing="0" decorator="org.displaytag.decorator.TotalTableDecorator"  name="obReport"  id="currentRowObject" export="true" sort="list" class="its" >
	<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	Opening Balance for the Year <%= request.getParameter("yearText") %>
	</display:caption>
	<div STYLE="display:table-header-group">
	<display:column  property="fund" title="Fund" />
	<display:column property="accCode" title="Glcode" />
	<display:column property="accName" title="<center>Account Head" />
	<display:column property="description" title="<center>Description" />
	<display:column property="debit"   title="<center>Debit&nbsp;(Rs.)" class="textAlign"/>
	<display:column property="credit"  title="<center>Credit&nbsp;(Rs.)" class="textAlign3"/>	
	<display:setProperty name="export.pdf" value="true" />
	<display:setProperty name="export.pdf.filename" value="OpeningBalance.pdf" /> 
    <display:setProperty name="export.excel" value="true" />
    <display:setProperty name="export.excel.filename" value="OpeningBalance.xls"/>
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
    LOGGER.info("after submit "+request.getParameter("fromBean")+" finYear "+request.getParameter("finYear")+" fundid  "+request.getParameter("obFund_id")+" deptid  "+request.getParameter("deptId"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("2"))
    {
	 try
	 {
		LOGGER.info("after submit "+request.getParameter("fromBean")+" finYear "+request.getParameter("finYear")+" fundid  "+request.getParameter("obFund_id")+" deptid  "+request.getParameter("deptId"));
		OpeningBalance obReport = new OpeningBalance();
		request.setAttribute("obReport",obReport.getOBReport(OBBean));
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
%>
    
	<u><b><div class = "normaltext">Opening Balance for the Year <%= request.getParameter("yearText") %></div></b></u>
	<div class="tbl5-container" id="tbl-container"> 
	<display:table cellspacing="0" decorator="org.displaytag.decorator.TotalTableDecorator" name="obReport"  id="currentRowObject" export="true" sort="list"  class="its" >
	<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	Opening Balance for the Year <%= request.getParameter("yearText") %>
	</display:caption>
	<div STYLE="display:table-header-group">

	<display:column  property="fund" title="Fund" />	
	<display:column property="accCode" title="Glcode" />
	<display:column property="accName" title="<center>Account Head" />
	<display:column property="description" title="<center>Description" />
	<display:column property="debit"   title="<center>Debit&nbsp;(Rs.)" class="textAlign"/>
	<display:column property="credit"  title="<center>Credit&nbsp;(Rs.)" class="textAlign3"/>	
	<display:setProperty name="paging.banner.placement" value="false" />
	<display:setProperty name="export.pdf" value="true" />
	<display:setProperty name="export.pdf.filename" value="OpeningBalance.pdf" /> 
    <display:setProperty name="export.excel" value="true" />
    <display:setProperty name="export.excel.filename" value="OpeningBalance.xls"/>
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
