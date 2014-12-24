<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page buffer = "64kb" %> 
<%@page  import="com.exilant.eGov.src.reports.LongAmountWrapper,java.text.SimpleDateFormat,com.exilant.eGov.src.reports.*,javax.naming.InitialContext,java.io.*,
			java.util.*"%>
<html>
<head>
<title>Dishonored Cheque Report</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<!-- <META http-equiv=pragma content=no-cache> -->
<!-- Inclusion of the CSS files that contains the styles -->

<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen" />
<link rel=stylesheet href="../css/print.css" type="text/css" media="print" />

<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
<script language="javascript" src="../script/jsCommonMethods.js"></script>
<SCRIPT type="text/javascript" src="../script/calendar.js" type="text/javascript" ></SCRIPT>


<SCRIPT LANGUAGE="javascript">


var strtDate="";
var endDate="";
var chqNo="";
var fundId="";

function onLoad()
{
	PageValidator.addCalendars();
	PageManager.ListService.callListService();
	
	var beanVal= <%=request.getParameter("fromBean")%>;
	document.getElementById('chequeNo').focus();
}
function ButtonPress()
{
	//alert("search");
	
	if (!PageValidator.validateForm())
	return;
	
	strtDate = document.getElementById('startDate').value;
	endDate = document.getElementById('endDate').value;
	
	
	if(strtDate=="")
	{
	//alert('Enter either Cheque From date or Cheque To Date');
	alert('Enter Cheque From date');
	return false;
	}
	
	chqNo=document.getElementById("chequeNo").value;
	//alert(chqNo);
	
	if(chqNo!="")
	chqNo=document.getElementById("chequeNo").value;
	
	fundId=document.getElementById("fundLst").value;
	//alert(fundId);
		
	if(fundId!="")
	fundId=document.getElementById("fundLst").value;
	
		
	
	if(endDate!="")
	{
		if( compareDate(formatDate6(strtDate),formatDate6(endDate)) == -1 )
		{
			//alert("Inside compare dt");
			alert('From Date cannot be greater than To Date');
			document.getElementById('startDate').value='';
			document.getElementById('endDate').value='';
			document.getElementById('startDate').focus();
			return false;
		}
	}
		
			
	
	document.getElementById('fromBean').value = 1;
 	document.DishonoredChequeReport.submit();

}


function afterRefreshPage(dc)
{
	/*
	if(dc.values['serviceID']=="finYearDate")
	{

	var dt=dc.values['startFinDate'];
	dt=formatDate2(dt);
	document.getElementById("startDate").value=dt;
	}
	*/
}

function beforeRefreshPage(dc){

	/*
	if(dc.values['serviceID']=="finYearDate")
	{

	var dt=dc.values['startFinDate'];
	dt=formatDate2(dt);
	document.getElementById("startDate").value=dt;
	}
	*/
}
function buttonFlush()
{
	window.location="DishonoredChequeReport.jsp";
}

      


 
 function buttonPrint()
 {
 	document.getElementById("tbl-header1").style.display = "none";
	document.getElementById("msgRow").style.display = "none";
	
	if(window.print)
	{
	  window.print();
	}
	document.getElementById("tbl-header1").style.display = "block";
}	
</SCRIPT>
</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onLoad="onLoad()">

<jsp:useBean id = "disChqReportBean" scope ="request" class="com.exilant.eGov.src.reports.DishonoredChequeBean"/> 
<jsp:setProperty name = "disChqReportBean" property="*"/>

<form name="DishonoredChequeReport"  action = "DishonoredChequeReport.jsp?chqNo="+chqNo+"&fundId="+fundId+"&startDate="+strtDate+"&endDate="+endDate  method = "post">
<input type="hidden" name="fromBean" id="fromBean" value="0">

<center>
<br>
<table align='center' id="table2">
<tr><td>
<div id="main"><div id="m2"><div id="m3" style="width:810px">
<div class="tbl-header1" id="tbl-header1">



<table align='center' class="tableStyle" id="table3"> 

	<tr >
			<td colspan="4" class="rowheader" valign="center"  width="100%"><span id="screenName" class="headerwhite2">
				Dishonored Cheque Details</span>
			</td>
	</tr>
	<tr class ="row1">
		<td></td>
		<td></td> <td></td> <td></td>
	</tr>
	
	<tr>
		<td align="right"><div align="right" valign="center" class="labelcell" >Cheque&nbsp;Number</div></td>
		<td class="smallfieldcell"><input CLASS="fieldinput" NAME="chequeNo" ID="chequeNo"> </td>
		
		
		<td align="right"><div align="right" valign="center" class="labelcell" >Fund&nbsp;</div></td>
		<td class="smallfieldcell"><SELECT CLASS="fieldinput" NAME="fundLst" ID="fundLst" exilListSource="fundNameList"> </SELECT></td>
	</tr>
	<tr>
		<td align="right"><div align="right" valign="center" class="labelcell" >Cheque Date From<SPAN class="leadon">*</SPAN></div></td>
		<td class="smallfieldcell" >
		<input name="startDate" id="startDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true">
		</td>
		<td align="right"><div align="right" valign="center" class="labelcell" >Cheque Date To</div></td>
		<td class="smallfieldcell">
		<input name="endDate" id="endDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true">
		</td>
	</tr>
	
	
	<tr><td>&nbsp;</td></tr>
		<tr id="msgRow">
		<td align="left" class="labelcellforsingletd" style="FONT-SIZE: 12px;" colspan="10">
		To&nbsp;print&nbsp;the&nbsp;report,&nbsp;please&nbsp;ensure&nbsp;the&nbsp;following&nbsp;settings:<br>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1. Paper size: A4<br>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2. Paper Orientation: Landscape
		</td>
	 </tr>
		
	<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	
	
	<tr>
			<td colspan="10" align="middle">
			<table border="0" cellpadding="0" cellspacing="0">
			<tr>	
			<td>		
				<input type=button class=button onclick="ButtonPress()" href="#" value="Search">
				<input type=button class=button onclick="buttonFlush()" href="#" value="Cancel">
				<input type=button class=button onclick="buttonPrint()" href="#" value="Print">
				<input type=button class=button onclick="window.close();" href="#" value="Close">
			</td>
			</tr>
			 </table>
			 </td>
	</tr>
	
</table>
  <br>
  
  </div>
</div></div></div>
  </td></tr>
  </table>
</center>

<!-- 		*************Search Results Start********************		-->
<%
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
    {
		try{
		 DishonoredChequeReport dcReport = new DishonoredChequeReport();
		 request.setAttribute("links",dcReport.getDishonoredChequeDetails(disChqReportBean));
		 }
		 catch(Exception e){
		 System.out.println("Exception in Jsp Page "+ e.getMessage());
		 %>
		 <script>
		 alert("Error :<%=e.getMessage()%>");
		 </script>
		 <%
		  }
		 %>	
		 
		 <center><u><b><div class = "normaltext"> Dishonored Cheque Report</div></b></u></center>   		
		<div class="tbl2-container" id="tbl-container">
		<display:table cellspacing="0" name="links"  id="currentRowObject" export="true"  sort="list" class="its">
		<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Dishonored Cheque Report
		</display:caption>
		<display:caption media="excel">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Dishonored Cheque Report
		</display:caption>

		<div STYLE="display:table-header-group">

		<display:column property="serialNo" style="width:30px" title="Sl No."  />
		<display:column property="voucherNumber"  style="width:80px" title="<center>Voucher&nbsp;Number</center>"          /> 
		<display:column property="voucherType"  style="width:100px" title="<center>Voucher&nbsp;Type</center>"         />	

		<display:column property="chequeNumber" style="width:80px" title="<center>Cheque&nbsp;Number</center>"  style="text-align:right"      />	
		<display:column property="chequeDate" style="width:80px" title="<center>Cheque&nbsp;&nbsp;&nbsp;Date</center>"  style="text-align:right"      />	
	
		<display:column property="payeeName"  style="width:100px" title="<center>Payee</center>"/>	
		<display:column property="bankName" style="width:80px" title="<center>Deposited&nbsp;in</center>"  style="text-align:right"      />	
		<display:column property="recChequeDate" style="width:80px" title="<center>Cheque&nbsp;Dishonored&nbsp;on</center>"  style="text-align:right"    />	
		<display:column property="bankRefNumber" style="width:80px" title="<center>Bank&nbsp;Ref&nbsp;Number</center>"  style="text-align:right"    />	
		<display:column property="bankChargeAmt" style="width:80px" title="<center>Bank&nbsp;Charge&nbsp;Amount&nbsp;(Rs.)</center>"  style="text-align:right"      />	
		<display:column property="amount" style="width:80px" title="<center>Cheque&nbsp;Amount&nbsp;(Rs.)</center>"  class="textAlign2" style="text-align:right"      />	
		

		<display:setProperty name="export.pdf" value="true" />
		<display:setProperty name="export.pdf.filename" value="DishonoredReport.pdf" /> 
		<display:setProperty name="export.csv" value="false" />
		<display:setProperty name="export.xml" value="false" />
		<display:setProperty name="export.excel" value="true" />
		<display:setProperty name="export.excel.filename" value="DishonoredReport.xls"/> 			
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
		 
<!-- 		**************Search Results End****************		-->
</form>
</body>
</html>
