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
<%@ page buffer = "30kb" %>
<%@page import="com.exilant.eGov.src.reports.LongAmountWrapper,com.exilant.eGov.src.reports.*,org.egov.infstr.utils.EGovConfig,java.io.*,java.io.*,java.util.*,java.sql.*,javax.sql.*,javax.naming.InitialContext,com.exilant.GLEngine.*"%>
<%@page import="org.apache.log4j.Logger"%>
<html>
<head>
<title>Day Book Report</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<!-- Inclusion of the CSS files that contains the styles -->
<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen" />
<link rel=stylesheet href="../css/print.css" type="text/css" media="print" />
<!--
<link rel=stylesheet href="../exility/global.css" type="text/css" />
-->
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
<script language="javascript" src="../resources/javascript/jsCommonMethods.js"></script>
<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js" type="text/javascript" ></SCRIPT>
<!--
<link rel="stylesheet" href="../exility/screen.css" type="text/css" media="screen, print" />
-->
<SCRIPT LANGUAGE="javascript">
var flag='0', tbNames, tbIds;
var rptType;
var rptTitle='', rptStartDate, rptEndDate, rptGLCode='', rptAccName='', detail='',accCode_1='',accCode_2='',snapShotDateTime,forRevEntry;
var rptfundId='';
onLoad

function callme(val,val1)
{
	var mode="view";
	switch(val1)
	{

		case 'JVG':
		window.open("../HTML/JV_General.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'SJV' :
		window.open("../HTML/SupplierJournal.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'CJV' :
		window.open("../HTML/ContractorJournal.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'SAL' :
		window.open("../HTML/JV_Salary.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'PYS':
		window.open("../HTML/PayInSlip.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'BTB':
		window.open("../HTML/JV_Contra_BToB.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'BTC':
		window.open("../HTML/JV_Contra_BToC.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'CTB':
		window.open("../HTML/JV_Contra_CToB.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'FTF':
		window.open("../HTML/JV_Contra_FToF.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'DBP':
		window.open("../HTML/DirectBankPayment.htm?cgNumber="+val+"&showMode=viewBank","","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'DCP' :
		window.open("../HTML/DirectCashPayment.htm?cgNumber="+val+"&showMode=viewCash","","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'SPH':
		window.open("../HTML/SubLedgerPayment.htm?drillDownCgn="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'SSP' :
		window.open("../HTML/SubledgerSalaryPayment.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'ASP' :
		window.open("../HTML/AdvanceJournal.htm?drillDownCgn="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'MSR':
		window.open("../HTML/MiscReceipt.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'PRB' :
		case 'PRO' :
		case 'PRF' :
		window.open("../HTML/PT_Field.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'OTF':
		window.open("../HTML/OT_Field.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
		case 'OTO':
		window.open("../HTML/OT_Office.htm?cgNumber="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		break;
	}

}
function onBodyLoad()
{
	
	PageValidator.addCalendars();
	if(PageManager.DataService.getQueryField('startDate') != null || PageManager.DataService.getQueryField('endDate') != null)
	{
		document.getElementById('startDate').value=PageManager.DataService.getQueryField('startDate');
		document.getElementById('endDate').value=PageManager.DataService.getQueryField('endDate');
	}
	PageManager.ListService.callListService();
	var beanVal= <%=request.getParameter("fromBean")%>;
	if(beanVal==2)
	{
		document.getElementById("row3").style.display="block";
		document.getElementById("row2").style.display="none";		
	}
	document.getElementById('startDate').focus();
}
function ButtonPress(){

	if (!PageValidator.validateForm()){ 
		//document.getElementById('startDate').value='';
		//document.getElementById('endDate').value='';
		return;
	}
	
	var strtDate = document.getElementById('startDate').value;
	rptStartDate = strtDate;
	//bootbox.alert(rptStartDate);
	var endDate = document.getElementById('endDate').value;
	rptEndDate = endDate;
	var rptfundId= document.getElementById('fundLst').value;
	if(rptfundId=='')
		document.getElementById('fundId').value="0";
	else
		document.getElementById('fundId').value=rptfundId;
		
	
	 if(strtDate.length==0||endDate.length==0)
	{
		 bootbox.alert("please select start dates and end dates");
		return;
	}
	/*if( compareDate(formatDateToDDMMYYYY(strtDate),formatDateToDDMMYYYY(endDate)) == -1 ){
		bootbox.alert('Start Date can not be greater than End Date');
		document.getElementById('startDate').focus();
		return false;
	}*/
	if( compareDate(formatDate6(strtDate),formatDate6(endDate)) == -1 ){
		bootbox.alert('Start Date can not be greater than End Date');
		document.getElementById('startDate').focus();
		return false;
	}
	document.getElementById('fromBean').value = "1";
	document.forms[0].submit();	

	//var aCode1 = document.getElementById('glCode1').value;
	//var aCode2 = document.getElementById('glCode2').value;

}
function buttonFlush()
{
	document.getElementById('startDate').value='';
	document.getElementById('endDate').value='';
	document.getElementById('fundLst').selectedIndex=-1;

}
					/*  For Print Report*/
					
function pageSetup()
{

//document.body.header = "&w&bPage";
//document.body.footer = "&b&d";
//document.body.header = "&w&bPage &p of &P";
//document.body.footer = "&u&b&d";

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
</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onload="onBodyLoad()"><!------------------ Header Begins Begins--------------------->
 <jsp:useBean id = "dbReportBean" scope ="session" class ="com.exilant.eGov.src.reports.DayBookReportBean"/>
<jsp:setProperty name = "dbReportBean" property="*"/>
<form name="dayBook" action = "DayBook.jsp?startDate="+rptStartDate+"&endDate="+rptEndDate+"&fundLst="+rptfundId method = "post">
<input type="hidden" name="fromBean" id="fromBean" value="0">
<input type="hidden" name="fundId" id="fundId" value="0">

<div class="tbl-header1" id="tbl-header1"> 		<!--       Div for hiding purpose    -->
<center>
<br>
<table align='center' id="table2">
<tr><td>
<div id="main"><div id="m2"><div id="m3" style="width:810px">


<table width="100%" border=0 cellpadding="3" cellspacing="0" id="mainTable">
	<!-- row-0 -->
		<tr>
			<td class="tableheader" valign="center" width="100%" colspan="6"><span id="screenName">Books of Accounts - Day Book</span><span id="partyName" class="headerwhite2"></span></td>
		</tr>

		<tr>
			<td><div align="right" valign="center" class="labelcell">Starting Date<span class="leadon">*</span></div></td>
			<td align="left" width="260" class="smallfieldcell">
				<input name="startDate" id="startDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" exilMustEnter="true">
			</td>
			<td><div align="right" valign="center" class="labelcell" >End Date<span class="leadon">*</span></div></td>
			<td width="260" class="smallfieldcell">
				<input name="endDate" id="endDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" exilMustEnter="true">
			</td>
			<td class="labelcell"><div align="right" valign="center"  >Fund</div></td>
			<td class="smallfieldcell"><SELECT CLASS="fieldinput" NAME="fundLst" ID="fundLst" exilListSource="fundNameList"  > </SELECT></td>

		</tr>
		<tr>
			<td colspan=6>
			</td>
		</tr>
		
		<!------------------ Detail Table for GL ------------------
		<tr>
			<td colspan="6" align="middle">
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td ></td><td></td><td></td><td ></td><td></td><td></td>
						<td ><div align="right" valign="center" class="normaltext" ><input type="submit" value = "Search" name ="submit" class="normaltext" onclick=ButtonPress()></td>
						<td ><div align="middle" valign="center" class="normaltext" ><CENTER><INPUT TYPE="submit" value = "Clear" name = "flush"class="normaltext" onclick=buttonFlush()></CENTER></td>
						<td ><div align="left" valign="center" class="normaltext" ><input type="submit" value = "Show All" name = "printView1"class="normaltext" onclick=ButtonPress()></td>
						
						<td ><div align="left" valign="center" class="normaltext" ><input type="submit" value = "Print" name = "printView2" class="normaltext" onclick="pageSetup();buttonPrint()"></td>
						<td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td ></td><td></td><td></td><td ></td><td></td><td></td>
					</tr>
				</table>
			</td>
		</tr> -->
		
		<tr id="row2" name="row2">
		<td colspan="6" align="middle"><!-- Buttons Start Here -->
		<br/><br/>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td align="right">
					<input type=button class=button onclick=ButtonPress() href="#" value="Search"></td>

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
		<td colspan="6" align="middle"><!-- Buttons Start Here -->
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td align="right">
					<input type=button class=button onclick=ButtonPress() href="#" value="Search"></td>

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
</form>
</div></div></div>
</td></tr>
</table>
 </div>					<!--               Div for hiding purpose    -->


<%
	Logger LOGGER = Logger.getLogger("DayBook.jsp");
	LOGGER.info("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate")+" fundname  "+request.getParameter("fundName"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
    {
	 LOGGER.info("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate")+" fundname  "+request.getParameter("fundName"));	
	 try{
	 
		DayBookList dayBookList = new DayBookList ();
		request.setAttribute("links",dayBookList.getDayBookList(dbReportBean));
	
		}
		catch(Exception e)
		{
		LOGGER.error("Exception in Jsp Page:"+e.getMessage());
		%>
		 <script>
		 bootbox.alert("Error :<%=e.getMessage()%>");
		 PageManager.DataService.setQueryField('endDate','');
		 </script>
		 <%
	}
	
%>


<center><u><b><div class = "normaltext">
	DAY BOOK REPORT <br>  <%=dbReportBean.getStartDate()%> to <%=dbReportBean.getEndDate()%>
</div></b></u></center>
<b> <div class = "alignleft"> <%= dbReportBean.getIsConfirmedCount() %> in <%= dbReportBean.getTotalCount() %> are unconfirmed  </div></b>
<div class="tbl-container" id="tbl-container">
		<display:table cellspacing="0" name="links" id="currentRowObject" export="true" sort="list" class="its" >
			<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		DAY BOOK REPORT
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<%=dbReportBean.getStartDate()%> to <%=dbReportBean.getEndDate()%>
		</display:caption>
					
		  <div STYLE="display:table-header-group">
		  <display:column property="voucherdate" title="Date" style="white-space: nowrap;width:8%;" />
		  <display:column media="pdf" property ="voucher" title="Voucher#" />
		 <display:column media="excel" property="voucher" title="Voucher#" />
		 <display:column media="xml" property="voucher" title="Voucher#" />
		 <display:column media="csv" property="voucher" title="Voucher#" />
		<display:column media="html" title="Voucher#" style="white-space: nowrap;width:8%;">
    		<% String cgn = ((DayBook)pageContext.findAttribute("currentRowObject")).getCgn();
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

			String url = ((DayBook)pageContext.findAttribute("currentRowObject")).getVoucher();%>
			<a href="<%=link%>"><%=url%></a>
			</display:column>
		 <display:column property="type" title="Type" />
		 <display:column property="narration" title="Narration" />
		<display:column property="status" title="Status" />
		<display:column property="glcode" title="Glcode"  />
		  <display:column property="particulars" title="Particulars" />
		  <display:column property="debitamount"  title="DrAmt&nbsp;(Rs.)"  class="textAlign" />
		  <display:column property="creditamount" title="CrAmt&nbsp;(Rs.)"  class="textAlign3" />
		<display:setProperty name="export.pdf" value="true" />
		<display:setProperty name="export.pdf.filename" value="DayBook.pdf" /> 
	    <display:setProperty name="export.excel" value="true" />
	    <display:setProperty name="export.excel.filename" value="DayBook.xls"/>
	    <display:setProperty name="export.csv" value="false" />
		<display:setProperty name="export.xml" value="false" />
		    
		     </div>          <!-- For Header group  -->
		 <display:footer>
			<tr>
				<td style="border-left: solid 0px #000000" colspan="20"><div style="border-top: solid 1px #000000 ">&nbsp;</div></td>
			<tr>
  		</display:footer>

		  </display:table>
		  </div>
		

		<%}

%>


<%
	LOGGER.info("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate")+" fundname  "+request.getParameter("fundName"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("2"))
    {
		LOGGER.info("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate")+" fundname  "+request.getParameter("fundName"));	
	 try{
	 
		DayBookList dayBookList = new DayBookList ();
		request.setAttribute("links",dayBookList.getDayBookList(dbReportBean));
	
		}
		catch(Exception e)
		{
		LOGGER.error("Exception in Jsp Page:"+e.getMessage());
		%>
		 <script>
		 bootbox.alert("Error :<%=e.getMessage()%>");
		 PageManager.DataService.setQueryField('endDate','');
		 </script>
		 <%
	}
	
%>


<center><u><b><div class = "normaltext">
	DAY BOOK REPORT <br>  <%=dbReportBean.getStartDate()%> to <%=dbReportBean.getEndDate()%>
</div></b></u></center>
<b> <div class = "alignleft"> <%= dbReportBean.getIsConfirmedCount() %> in <%= dbReportBean.getTotalCount() %> are unconfirmed  </div></b>
<div class="tbl5-container" id="tbl-container">
<display:table cellspacing="0" name="links" id="currentRowObject" export="true" sort="list" class="its" >
			<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			DAY BOOK REPORT
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<%=dbReportBean.getStartDate()%> to <%=dbReportBean.getEndDate()%>
			</display:caption>
		  <div STYLE="display:table-header-group">
		  <display:column property="voucherdate" title="Date" style="white-space: nowrap;width:8%;" />
		  <display:column media="pdf" property ="voucher" title="Voucher#" />
		 <display:column media="excel" property="voucher" title="Voucher#" />
		 <display:column media="xml" property="voucher" title="Voucher#" />
		 <display:column media="csv" property="voucher" title="Voucher#" />
		<display:column media="html" title="Voucher#"  style="white-space: nowrap;width:8%;">
    		<% String cgn = ((DayBook)pageContext.findAttribute("currentRowObject")).getCgn();
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

			String url = ((DayBook)pageContext.findAttribute("currentRowObject")).getVoucher();%>
			<a href="<%=link%>"><%=url%></a>
			</display:column>
		 <display:column property="type" title="Type" />
		 <display:column property="narration" title="Narration" />
		<display:column property="status" title="Status" />
		<display:column property="glcode" title="Glcode"  />
		  <display:column property="particulars" title="Particulars" />
		  <display:column property="debitamount"  title="DrAmt&nbsp;(Rs.)"  class="textAlign" />
		  <display:column property="creditamount" title="CrAmt&nbsp;(Rs.)"  class="textAlign3" />
		    <display:setProperty name="export.pdf" value="true" />
			<display:setProperty name="export.pdf.filename" value="DayBook.pdf" /> 
		    <display:setProperty name="export.excel" value="true" />
		    <display:setProperty name="export.excel.filename" value="DayBook.xls"/>
		    <display:setProperty name="export.csv" value="false" />
			<display:setProperty name="export.xml" value="false" />
		     </div>          <!-- For Header group  -->
		 <display:footer>
			<tr>
				<td style="border-left: solid 0px #000000" colspan="20"><div style="border-top: solid 1px #000000 ">&nbsp;</div></td>
			<tr>
  		</display:footer>

		  </display:table>
		  </div>		 

		<%}

%>

       
</body>
</html>
