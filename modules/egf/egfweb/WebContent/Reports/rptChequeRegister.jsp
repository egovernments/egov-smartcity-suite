<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@page  import="com.exilant.eGov.src.reports.LongAmountWrapper,com.exilant.eGov.src.reports.*,java.io.*,java.util.*,java.sql.*,javax.sql.*,javax.naming.InitialContext,com.exilant.GLEngine.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<META http-equiv=pragma content=no-cache>
<!--<link rel=stylesheet href="../exility/global.css" type="text/css">-->
<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen" />
<link rel=stylesheet href="../css/print.css" type="text/css" media="print" />
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
<script language="javascript" src="../script/jsCommonMethods.js"></script>
<SCRIPT type="text/javascript" src="../script/calendar.js" type="text/javascript" ></SCRIPT>
<!--<link rel="stylesheet" href="../exility/screen.css" type="text/css" media="screen, print" />-->
<SCRIPT LANGUAGE="javascript">

var fromDate1;
var toDate1;
function onLoadofPage()
{		
	PageValidator.addCalendars();
    PageManager.DataService.callDataService('companyDetailData');    
	var beanVal= <%=request.getParameter("fromBean")%>;
	if(beanVal==2)
	{
		document.getElementById("row3").style.display="block";
		document.getElementById("row2").style.display="none";		
	}
	document.getElementById("fromDate").focus();
}

function display()
{
	if (!PageValidator.validateForm())
	{
		//document.getElementById('fromDate').value='';
		//document.getElementById('toDate').value='';
		return;
	}
	fromDate1=document.getElementById('fromDate').value;
	toDate1=document.getElementById('toDate').value;
	document.getElementById('fromBean').value = 1;
	document.conSupRpt.submit();
}

function buttonFlush1()
{
	window.location="rptChequeRegister.jsp";
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
	document.getElementById('fromBean').value =2;	   
	document.conSupRpt.submit();	
}


//For print method
function buttonPrint()
{
    var hide1= document.getElementById("tbl-header1").style.display = "none"; 
	document.conSupRpt.submit();	
	if(window.print)
	{
		window.print();
  	}
}

</SCRIPT>
<title>Cheque Issue Register</title>
</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onLoad="onLoadofPage()" >
<jsp:useBean id = "csReportBean" scope ="session" class="com.exilant.eGov.src.reports.ChequeRegisterBean"/>
<jsp:setProperty name ="csReportBean" property="fromDate"/>
<jsp:setProperty name ="csReportBean" property="toDate"/> 

<form name="conSupRpt" action ="rptChequeRegister.jsp?fromDate="+fromDate+"&toDate="+toDate method = "post">
<input type="hidden" name="fromBean" id="fromBean" value="0">
<input type="hidden" name="companyDetail_name" id="companyDetail_name" value="">

<br>
<table align='center' id="table2">
<tr><td>
<div id="main"><div id="m2"><div id="m3" style="width:810px">

<div class="tbl-header1" id="tbl-header1">
<table width="100%" border=0 cellpadding="3" cellspacing="0">
	<tr >
		<td colspan="6" class="tableheader" valign="center" colspan="6" width="100%"><span id="screenName">Cheque Issue Register</span></td>
	</tr>
	<tr class="row1">


		<td><div align="right" valign="center" class="labelcell" >From Date<span class="leadon">*</span></div></td>
		<td class="smallfieldcell">
			<input name="fromDate" id="fromDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" exilMustEnter="true">
		</td>

		<td><div align="right" valign="center" class="labelcell" >To date<span class="leadon">*</span></div></td>
		<td class="smallfieldcell">
			<input name="toDate" id="toDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" exilMustEnter="true">
		</td>


	</tr>


	<tr class="row2"><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr class="row2" id="row2" name="row2">
		<td colspan="6" align="middle">
		<table border="0" cellpadding="0" cellspacing="0" >
		<tr>
			<td align="right">
			<input type=button class=button onclick=display() href="#" value="Submit"></td>
			
			<td align="right">
			<input type=button class=button onclick=buttonFlush1(); href="#" value="Cancel"></td>
			
			<td align="right">
			<input type=button class=button onclick=window.close() href="#" value="Close"></td>
			
			<!-- Print Preview Button start -->
			<td align="right">
			<input type=button class=button onclick="buttonPrintPreview()" href="#" value="Print Preview"></td>
			<!-- Preview Print end-->
		</tr>
		</table>
		</td>
	</tr>
	
	<tr style="DISPLAY: none" class="row3" id="row3" name="row3">
		<td colspan="6" align="middle"><!-- Buttons Start Here -->
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td align="right">
					<input type=button class=button onclick=display() href="#" value="Submit"></td>

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
</div>
</div></div></div>
</td></tr>
</table>

<%
    System.out.println("before submit "+request.getParameter("fromBean")+" from Date "+request.getParameter("fromDate")+" To Date "+request.getParameter("toDate"));
       if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
       {
   	 System.out.println("after submit "+request.getParameter("fromBean")+" from Date "+request.getParameter("fromDate")+" To Date "+request.getParameter("toDate"));
   	 ChequeRegister chequeReg = new ChequeRegister();
   	 try{
   	   	request.setAttribute("links",chequeReg.getChequeRegister(csReportBean));
	    } catch(Exception e) { System.out.println("Error:"+"connecting to databse failed");}

%>
<!--<div class="tbl-bill" id="tbl-container" >-->
<div class = "alignright"><b><font size=2>Form GEN-15 <br> <u><%=request.getParameter("companyDetail_name")%></u></font>   </b></div>
<div  class = "normaltext"> CHEQUE ISSUE REGISTER   </div>
<div class="tbl2-container" id="tbl-container" >
 <display:table  name="links" id="currentRowObject" cellspacing="0" export="true" sort="list"  class="its" >
	<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Form GEN-15
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 <%=request.getParameter("companyDetail_name")%>
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 CHEQUE ISSUE REGISTER
		</display:caption>
	<display:column style="width:1%"  property="totalCount"  title="Sl.No" />
	 <display:column  property="pymtDate"  title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Date&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" decorator="com.exilant.eGov.src.reports.ReplaceWrapper" />
	 <display:column style="width:1%"   property="vchrNo" title="Bank&nbsp;Payment Voucher Number & Date" />
	 <display:column style="width:5%" title="Payment&nbsp;Order Number & Date" >
	 <% 
	 	String vhId =((ChequeRegisterBean)pageContext.findAttribute("currentRowObject")).getVhId();    		
	 	String chequeNo =((ChequeRegisterBean)pageContext.findAttribute("currentRowObject")).getChequeNo();  
	 	String pymtNature=((ChequeRegisterBean)pageContext.findAttribute("currentRowObject")).getPymtNature();   			 	     		
	 	     		
		String link = "javascript:"+"void(window.open('../Reports/ChequeDetail.jsp?vhId="+vhId+"&chequeNo="+chequeNo+"&pymtNature="+pymtNature+"'"+",'','height=500,width=680,scrollbars=no,left=20,top=20,status=yes'"+"))";
		String url = ((ChequeRegisterBean)pageContext.findAttribute("currentRowObject")).getOrderNo();%>	

		<a href="<%=link%>"><%=url%></a>
	</display:column>
	 <display:column style="width:15%"   property="nameOfPayee" title="Name of the Payee" />
	 <display:column style="width:5%" property="pymtNature"  title="Nature of Payement" />
	 <display:column style="width:2%"  property="chequeNo"  title="Cheque/ Draft No." />
	 <display:column style="width:1%"  property="chequeDate"  title="Date of Cheque/Draft" />
	 <display:column style="width:1%" property="chequeAmount" class="textAlign" value="&nbsp;" title="Amount <center>( Rs )</center>" />
	 <display:column   style="width:1%"  property="enteredBy" title="Entered By" />
	 <display:column   style="width:1%" value="&nbsp;" title="Signature of the first Signatory" />
	 <display:column  style="width:1%"  value="&nbsp;" title="Signature of the Second Signatory" />
	 <display:column style="width:1%"  property="issueDate"  title="Date of issue of&nbsp;Cheque/Draft" />
	 <display:column  style="width:1%"  value="&nbsp;" title="Signature of the&nbsp;Receipient of&nbsp;Cheque/Draft" />
	 <display:column style="width:1%"  property="clrDate"  title="Date of Clearence&nbsp;&nbsp;" />
	 <display:column style="width:1%"  property="remarks"  class="textAlign3" title="Remarks" />

	 <display:setProperty name="paging.banner.placement" value="false" />
	 <display:setProperty name="export.pdf" value="true" />
	 <display:setProperty name="export.pdf.filename" value="rptChequeRegister.pdf" /> 
	 <display:setProperty name="export.excel" value="true" />
	 <display:setProperty name="export.excel.filename" value="rptChequeRegister.xls"/>
	 <display:setProperty name="export.csv" value="false" />
	 <display:setProperty name="export.xml" value="false" />	
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
    System.out.println("before submit "+request.getParameter("fromBean")+" from Date "+request.getParameter("fromDate")+" To Date "+request.getParameter("toDate"));
       if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("2"))
       {
	   	 System.out.println("after submit "+request.getParameter("fromBean")+" from Date "+request.getParameter("fromDate")+" To Date "+request.getParameter("toDate"));
	   	 ChequeRegister chequeReg = new ChequeRegister();
	   	 try{
	   	   		request.setAttribute("links",chequeReg.getChequeRegister(csReportBean));
		    } catch(Exception e) { System.out.println("Error:"+"connecting to databse failed");}

%>

<div class = "alignright"><b><font size=2>Form GEN-15 <br> <u><%=request.getParameter("companyDetail_name")%></u></font>   </b></div>
<div  class = "normaltext"> CHEQUE ISSUE REGISTER   </div>
<div class="tbl5-container" id="tbl-container" >
 <display:table  name="links" id="currentRowObject" cellspacing="0" export="true" sort="list"  class="its" >
	<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Form GEN-15
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 <%=request.getParameter("companyDetail_name")%>
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 CHEQUE ISSUE REGISTER
		</display:caption>
	<display:column style="width:1%"  property="totalCount"  title="<font size=1 face=sans-serif>Sl.No</font>" class="textAlign9"/>
	 <display:column  property="pymtDate" class="textAlign9" title="<font size=1 face=sans-serif>Date</font>" decorator="com.exilant.eGov.src.reports.ReplaceWrapper" />
	 <display:column style="width:1%"   property="vchrNo" class="textAlign9" title="<font size=1 face=sans-serif>Bank&nbsp;Payment Voucher Number & Date</font>" />
	 <display:column style="width:5%" title="<font size=1 face=sans-serif>Payment&nbsp;Order Number & Date</font>" >
	 <% 
	 	String vhId =((ChequeRegisterBean)pageContext.findAttribute("currentRowObject")).getVhId();    		
	 	String chequeNo =((ChequeRegisterBean)pageContext.findAttribute("currentRowObject")).getChequeNo();  
	 	String pymtNature=((ChequeRegisterBean)pageContext.findAttribute("currentRowObject")).getPymtNature();   			 	     		
	 	     		
		String link = "javascript:"+"void(window.open('../Reports/ChequeDetail.jsp?vhId="+vhId+"&chequeNo="+chequeNo+"&pymtNature="+pymtNature+"'"+",'','height=500,width=680,scrollbars=no,left=20,top=20,status=yes'"+"))";
		String url = ((ChequeRegisterBean)pageContext.findAttribute("currentRowObject")).getOrderNo();%>	

		<a href="<%=link%>"><font size=1 face=sans-serif><%=url%></font></a>
	</display:column>
	 <display:column style="width:15%"   property="nameOfPayee" title="<font size=1 face=sans-serif>Name of the Payee</font>" class="textAlign9" />
	 <display:column style="width:5%" property="pymtNature"  title="<font size=1 face=sans-serif>Nature of Payement</font>" class="textAlign9"/>
	 <display:column style="width:2%"  property="chequeNo"  title="<font size=1 face=sans-serif>Cheque/ Draft No.</font>" class="textAlign9" />
	 <display:column style="width:1%"  property="chequeDate"  title="<font size=1 face=sans-serif>Date of Cheque/Draft</font>" class="textAlign9"/>
	 <display:column style="width:1%" property="chequeAmount" class="textAlign8" value="&nbsp;" title="<font size=1 face=sans-serif>Amount <center>( Rs )</center></font>" />
	 <display:column   style="width:1%"  property="enteredBy" title="<font size=1 face=sans-serif>Entered By</font>" class="textAlign9" />
	 <display:column   style="width:1%" value="&nbsp;" title="<font size=1 face=sans-serif>Signature of the first Signatory</font>" class="textAlign9" />
	 <display:column  style="width:1%"  value="&nbsp;" title="<font size=1 face=sans-serif>Signature of the Second Signatory</font>" class="textAlign9" />
	 <display:column style="width:1%"  property="issueDate"  title="<font size=1 face=sans-serif>Date of issue of&nbsp;Cheque/Draft</font>" class="textAlign9"/>
	 <display:column  style="width:1%"  value="&nbsp;" title="<font size=1 face=sans-serif>Signature of the&nbsp;Receipient of&nbsp;Cheque/Draft</font>" class="textAlign9" />
	 <display:column style="width:1%"  property="clrDate"  title="<font size=1 face=sans-serif>Date of Clearence&nbsp;&nbsp;</font>" class="textAlign9"/>
	 <display:column style="width:1%"  property="remarks"  class="textAlign3" title="<font size=1 face=sans-serif>Remarks</font>" class="textAlign9" style="border-right: solid 1px #000000" />

	 <display:setProperty name="paging.banner.placement" value="false" />
	 <display:setProperty name="export.pdf" value="true" />
	 <display:setProperty name="export.pdf.filename" value="rptChequeRegister.pdf" /> 
	 <display:setProperty name="export.excel" value="true" />
	 <display:setProperty name="export.excel.filename" value="rptChequeRegister.xls"/>
	 <display:setProperty name="export.csv" value="false" />
	 <display:setProperty name="export.xml" value="false" />
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

