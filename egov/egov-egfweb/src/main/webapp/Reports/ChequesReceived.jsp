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
<%@ page buffer = "16kb" %>
<%@page  import="java.text.SimpleDateFormat,com.exilant.eGov.src.reports.LongAmountWrapper,com.exilant.eGov.src.reports.*,java.io.*,
				java.util.HashMap,java.util.*,javax.naming.InitialContext,com.exilant.GLEngine.*"%>

<html>
<head>
<title>Cheques Received Report</title>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<META http-equiv=pragma content=no-cache>

<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen" />
<link rel=stylesheet href="../css/print.css" type="text/css" media="print" />

<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
<script language="javascript" src="../resources/javascript/jsCommonMethods.js"></script>
<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js" type="text/javascript" ></SCRIPT>

<SCRIPT LANGUAGE="javascript">
<%
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Date curntDate = new Date();
	String currDate = sdf.format(curntDate);
%>

var eDate="";
var sDate="";
var fId="";

function onLoad()
{
	PageValidator.addCalendars();
	if(document.getElementById("sDate").value=="")
	{		
		PageManager.DataService.callDataService("finYearDate");
	}
	PageManager.ListService.callListService();
	var beanVal= <%=request.getParameter("fromBean")%>;
	if(beanVal==2)
	{
		document.getElementById("row2").style.display="block";
		document.getElementById("row1").style.display="none";		
	}	
	document.getElementById("eDate").value="<%=currDate%>";
	document.getElementById("crFund_id").focus();
}

function ButtonPress()
{
	if (!PageValidator.validateForm())
	{
		return;
	}
	if(document.getElementById('crFund_id').value=='')
	{
		bootbox.alert("Fund is Required!");
	   document.getElementById('crFund_id').focus();
	   return;
	}
	
	if(document.getElementById('sDate').value=='')
	{
		bootbox.alert("Start Date is Required!");
	   document.getElementById('sDate').focus();
	   return;
	}
	
	if(document.getElementById('eDate').value=='')
	{
		bootbox.alert("End Date is Required!");
	   document.getElementById('eDate').focus();
	   return;
	}	
	var fundObj=document.getElementById('crFund_id');
		document.getElementById('fundName').value=fundObj.options[fundObj.selectedIndex].text;			
		if(fundObj=='')
			document.getElementById('fundName').value="All Funds";
	document.getElementById('fromBean').value = 1;
	var fObj=document.getElementById('crFund_id');
	fId = fObj.options[fObj.selectedIndex].value;
	startDate = document.getElementById('sDate').value;
	endDate = document.getElementById('eDate').value;
	
	/* checking Fiscal year start Date with End Date of Fiscal year */	
		var tMonth=endDate .substr(endDate .length-7,2);
		if(tMonth<4)
		var fiscalYearStartDate="01/04/"+(endDate .substr(endDate .length-4,4)-1);
		else
		var fiscalYearStartDate="01/04/"+endDate .substr(endDate .length-4,4);
		if(compareDate(fiscalYearStartDate,startDate) == -1 )
		{ 
			bootbox.alert("Start Date and End Date should be in same financial year");
		   document.getElementById('sDate').focus();
		   return;
		} 
	
		/*To check whether Start Date is Greater than End Date*/
		if( compareDate(formatDate6(startDate),formatDate6(endDate)) == -1 )
		{
			bootbox.alert('Start Date cannot be greater than End Date');
			document.getElementById('sDate').value='';
			document.getElementById('eDate').value='';
			document.getElementById('sDate').focus();
			return false;
		}	
	   /*to check whether the End Date is greater than the Current Date*/
		if( compareDate(formatDate6("<%=currDate%>"),formatDate6(endDate)) == 1 )
		{
			bootbox.alert('End Date cannot be greater than Current Date');
			document.getElementById('eDate').value='';
			document.getElementById('eDate').focus();	
			return;	
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
	document.getElementById("sDate").value=dt;
}

function buttonFlush1()
{
	window.location="ChequesReceived.jsp";
} 

function pageSetup()
{
	document.body.leftMargin=0.75;
	document.body.rightMargin=0.75;
	document.body.topMargin=0.75;
	document.body.bottomMargin=0.75;
}

function buttonPrintPreview()
{
	document.getElementById('fromBean').value ="2";	   
	document.forms[0].submit();	
}
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
<body onLoad="onLoad()">
<form name="ChequesReceivedReport" action = "ChequesReceived.jsp?crFund_id="+fId+"&t_date="+eDate+"&f_date="+sDate method = "post">
 <jsp:useBean id = "chqRecvdBean" scope ="request" class="com.exilant.eGov.src.reports.ChequesReceivedBean"/>
<jsp:setProperty name = "chqRecvdBean" property="*"/> 
<input type="hidden" name="fromBean" id="fromBean" value="0">
<input type="hidden" id="fundName" name="fundName" value="">
<input type="hidden" name="databaseDate"  id="databaseDate">

<div class="tbl-header1" id="tbl-header1">
<center>
<br>
<table align='center' id="table2">
<tr><td>
<div id="main"><div id="m2"><div id="m3" style="width:810px">


<table width="100%" border=0 cellpadding="3" cellspacing="0">
<tr>
		<td colspan="4" class="tableheader" valign="center" colspan="4" width="100%"><span id="screenName">Cheques Received</span></td>
</tr>

<tr>
  <td align="right">
    <div  valign="center" class="labelcell">Fund<span class="leadon">* &nbsp;</span></div></td>
			<td align="left" class="smallfieldcell">
				<SELECT class="fieldinput" id="crFund_id" name="crFund_id" exilListSource="fundNameList" exilMustEnter="true"></SELECT><td></td><td></td>
            </td>
</tr>


<tr>
			<td align="right"><div  valign="center" class="labelcell">From Date<span class="leadon">* &nbsp;</span></div></td>
			<td align="left" class="smallfieldcell">
				<input name="sDate" id="sDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')" exilDataType="exilAnyDate" exilCalendar="true">
			</td>
			<td align="right" ><div valign="center" class="labelcell" >To Date<span class="leadon">* &nbsp;</span></div></td>
			<td class="smallfieldcell">
				<input name="eDate" id="eDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')" exilDataType="exilAnyDate" exilCalendar="true">
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
</div></div></div>
</td></tr>
</table>
</div>
<%
   
	if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
	 {
    	
	 System.out.println("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("sDate")+" enddate "+request.getParameter("eDate") );
		try{
		 ChequesReceived chqRecvd = new ChequesReceived();			   
		 request.setAttribute("links",chqRecvd.getChequesReceivedRep(request.getParameter("sDate"),request.getParameter("eDate"), request.getParameter("crFund_id") ));
		 }
		 catch(Exception e){
		 System.out.println("Exception in Jsp Page "+ e.getMessage());
		 %> 
		 <script>
		 bootbox.alert("Error :<%=e.getMessage()%>"); 
		 PageManager.DataService.setQueryField('endDate',null);
		 </script>
		 <%
		 }
%>		
		
		<center><b><div class = "normaltext"> Cheques Received for <%= request.getParameter("fundName") %> from <%= request.getParameter("sDate") %> to <%= request.getParameter("eDate") %></div></b> </center>   		
		<div class="tbl-container" id="tbl-container">
		<display:table cellspacing="0" name="links"  id="currentRowObject" export="true"  sort="list" class="its">
		<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		Cheques Received for <%= request.getParameter("fundName") %> from <%= request.getParameter("sDate") %> to <%= request.getParameter("eDate") %>
		</display:caption>
		<div STYLE="display:table-header-group">
		<display:column property="fundName" style="width:100px" title="<center>&nbsp;Fund&nbsp;</center>" class="textAlign"/> 	
		<display:column property="collName" style="width:300px" title="<center>Name&nbsp;of&nbsp;the&nbsp;Collector</center>"   /> 
		<display:column property="rcptNo" style="width:120px"  title="<center>Rcpt&nbsp;No/Date</center>"   class="textAlign"  /> 
		<display:column property="rcvdFrom"  style="width:200px" title="<center>Received&nbsp;From</center>"    /> 
		<display:column property="particulars"  style="width:100px" title="<center>Particulars&nbsp;</center>"    />	
		<display:column property="chqNoDate" style="width:120px" title="<center>Cheque&nbsp;No/Date/Name&nbsp;of&nbsp;Bank</center>"  class="textAlign" />	
		<display:column property="amount" style="width:120px" title="<center>Amount&nbsp;of Tax/Fees/Charges/Other&nbsp;Income</center>"  class="textAlign" />	
		<display:column property="bankDep" style="width:120px" title="<center>Bank&nbsp;Deposited</center>"   />
		<display:column property="depositDate" style="width:120px" title="<center>Date&nbsp;of&nbsp;Deposit</center>"  class="textAlign" />
		<display:column property="realizationDate" style="width:120px" title="<center>Date&nbsp;of&nbsp;Realization</center>"  class="textAlign" />		
		<display:column property="remarks" style="width:120px" title="<center>Remarks&nbsp;</center>"  class="textAlign6"  />
		<display:setProperty name="export.pdf" value="true" />
		<display:setProperty name="export.pdf.filename" value="ChequesReceived.pdf" /> 
	    <display:setProperty name="export.excel" value="true" />
	    <display:setProperty name="export.excel.filename" value="ChequesReceived.xls"/>
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
   
	if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("2"))
	 {
    	
	 System.out.println("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("sDate")+" enddate "+request.getParameter("eDate") );
		try{
		 ChequesReceived chqRecvd = new ChequesReceived();			   
		 request.setAttribute("links",chqRecvd.getChequesReceivedRep(request.getParameter("sDate"),request.getParameter("eDate"), request.getParameter("crFund_id") ));
		 }
		 catch(Exception e){
		 System.out.println("Exception in Jsp Page "+ e.getMessage());
		 %> 
		 <script>
		 bootbox.alert("Error :<%=e.getMessage()%>"); 
		 PageManager.DataService.setQueryField('endDate',null);
		 </script>
		 <%
		 }
%>	 
		
		
		<center><b><div class = "normaltext"> Cheques Received for <%= request.getParameter("fundName") %> from <%= request.getParameter("sDate") %> to <%= request.getParameter("eDate") %></div></b> </center>   		
		<div class="tbl5-container" id="tbl-container" style="width:900px">
		<display:table cellspacing="0" name="links"  id="currentRowObject" export="true"  sort="list" class="its" style="width:900px">
		<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		Cheques Received for <%= request.getParameter("fundName") %> from <%= request.getParameter("sDate") %> to <%= request.getParameter("eDate") %>
		</display:caption>
		<div STYLE="display:table-header-group">
		<display:column property="fundName" style="width:80px" title="<font size=1><center>Fund&nbsp;</center></font>" class="textAlign9"/> 	
		<display:column property="collName" style="width:60px" title="<font size=1><center>Name&nbsp;of&nbsp;the&nbsp;Collector</center></font>"  class="textAlign9" /> 
		<display:column property="rcptNo" style="width:50px"  title="<font size=1><center>Rcpt No/Date</center></font>"   class="textAlign8"  /> 
		<display:column property="rcvdFrom"  style="width:80px" title="<font size=1><center>Received From</center></font>"    class="textAlign9"/> 
		<display:column property="particulars"  style="width:150px" title="<font size=1><center>Particulars</center></font>"    class="textAlign9"/>	
		<display:column property="chqNoDate" style="width:80px" title="<font size=1><center>Cheque No/Date/Name of Bank</center></font>"  class="textAlign8" />	
		<display:column property="amount" style="width:50px" title="<font size=1><center>Amount of Tax/Fees/ Charges/Other Income</center></font>"  class="textAlign8" />	
		<display:column property="bankDep" style="width:100px" title="<font size=1><center>Bank Deposited</center></font>"  class="textAlign9" />
		<display:column property="depositDate" style="width:50px" title="<font size=1><center>Date of Deposit</center></font>"  class="textAlign9" />
		<display:column property="realizationDate" style="width:50px" title="<font size=1><center>Date of Realization</center></font>"  class="textAlign9" />
		<display:column property="remarks" style="width:150px" title="<font size=1><center>Remarks</center></font>"  class="textAlign6" />
		<display:setProperty name="export.pdf" value="true" />
		<display:setProperty name="export.pdf.filename" value="ChequesReceived.pdf" /> 
	    <display:setProperty name="export.excel" value="true" />
	    <display:setProperty name="export.excel.filename" value="ChequesReceived.xls"/>
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


		





