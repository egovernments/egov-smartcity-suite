<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<!-- <%@ page buffer = "16kb" %> -->
<%@page  import="com.exilant.eGov.src.reports.*,org.egov.infstr.utils.EGovConfig,java.io.*,com.exilant.eGov.src.transactions.*,java.io.*,java.util.*,java.sql.*,javax.sql.*,javax.naming.InitialContext,com.exilant.GLEngine.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html>
<head>
<title>Journal Book Report</title>
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

//var cWind;         // temporary window reference
var count=1;
var forRE="";
var cWindows=new Object(); 
//var flag='0', tbNames, tbIds,dbTotal=0,crTotal=0;
var strtDate="",endDate="";

function onBodyLoad() 
{
	PageValidator.addCalendars();
	PageManager.ListService.callListService();
	//PageManager.DataService.callDataService('cgNumber');
	var beanVal= <%=request.getParameter("fromBean")%>;
	if(beanVal==2)
	{
		document.getElementById("row2").style.display="block";
		document.getElementById("row1").style.display="none";		
	}
	document.getElementById("startDate").focus();
}


function ButtonPress()
{
	if (!PageValidator.validateForm())
	{
		//document.getElementById('startDate').value='';
		//document.getElementById('endDate').value='';
		return;
	}
	strtDate = document.getElementById('startDate').value;
	endDate = document.getElementById('endDate').value;
	var dbDate=document.getElementById('databaseDate').value;
	
	/*if(compareDate(formatDateToDDMMYYYY1(endDate),formatDateToDDMMYYYY1(dbDate)) == -1 )
	{
		alert('End Date should be less than or equal to '+dbDate);
		document.getElementById('endDate').focus();
		return false;
	}*/
	if( compareDate(formatDate6(strtDate),formatDate6(endDate)) == -1 )
		{
			alert('Start Date cannot be greater than End Date');
			document.getElementById('startDate').value='';
			document.getElementById('endDate').value='';
			document.getElementById('startDate').focus();
			return false;
		}
	 document.getElementById('fromBean').value = 1;
	
	 var fObj = document.getElementById('fund_id');
	 fObj=fObj.options[fObj.selectedIndex].value;
	 var fsObj = document.getElementById('fundSource_id');
	//alert("fsObj....."+fsObj.selectedIndex);
	if(fsObj!='' && fsObj.selectedIndex!=-1)	 
		 fsObj=fsObj.options[fsObj.selectedIndex].value;
	 var fpObj = document.getElementById('voucher_name');
	if(fpObj!='' && fpObj.selectedIndex!=-1)
		 fpObj=fpObj.options[fpObj.selectedIndex].value;
	// cgn=document.getElementById("cgn").value;	 
		
	//forRE=document.getElementById("RevEntry").value; 	 
	 document.JournalBook.submit();
 
}


function buttonFlush1()
{
	window.location="JournalBook.jsp";

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
 <jsp:useBean id = "jbReportBean" scope ="request" class="com.exilant.GLEngine.GeneralLedgerBean"/>
<jsp:setProperty name = "jbReportBean" property="*"/>  
<form name="JournalBook" action = "JournalBook.jsp?fund_id="+fObj+"&fundSource_id="+fsObj+"&startDate="+strtDate+"&endDate="+endDate method = "post">
<input type="hidden" name="fromBean" id="fromBean" value="0"> 
<input type="hidden" name="databaseDate"  id="databaseDate">
<center>
<br>
<table align='center' id="table2">
<tr><td>
<div id="main"><div id="m2"><div id="m3" style="width:810px">


<div class="tbl-header1" id="tbl-header1">

<table width="100%" border=0 cellpadding="3" cellspacing="0">
<tr >
		<td class="tableheader" valign="center" colspan="4" width="100%"><span id="screenName">Journal Book</span></td>
	</tr>
	<tr>
		<td><div align="right" valign="center" class="labelcell">Starting Date<span class="leadon">*</span></div></td>
		<td align="left" width="260" class="smallfieldcell">
			<input name="startDate" id="startDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')" exilCalendar="true" exilMustEnter="true">
		</td>
		<td><div align="right" valign="center" class="labelcell" >End Date<span class="leadon">*</span></div></td>
		<td class="smallfieldcell">
			<input name="endDate" id="endDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')" exilCalendar="true" exilMustEnter="true">
		</td>

	</tr>

	<tr>
		<td><div align="right" valign="center" class="labelcell">Fund<span class="leadon">*</span></div></td>
		<td align="left" width="260" class="smallfieldcell">
			<SELECT class="fieldinput" id=fund_id name="fund_id" exilListSource="fundNameList" exilMustEnter="true"></SELECT>
		</td>

		<td><div align="right" valign="center" class="labelcell">Voucher name</div></td>
		<td align="left" width="260" class="smallfieldcell">
			<SELECT class="fieldinput" id="voucher_name" name="voucher_name"  exilListSource="voucherList"></SELECT>
		</td>
	</tr>

	<tr>
		<td><div align="right" valign="center" class="labelcell">Financing Source</div></td>
		<td align="left" class="smallfieldcell">
			<SELECT class="fieldinput" id="fundSource_id" name="fundSource_id"  exilListSource="fundSourceNameList"></SELECT>
		</td>
	</tr>
	
	<tr>
			<td></td>
		<td width="260"></td>
		<!--<td><div align="right" valign="center" class="normaltext">Show Reversed Entry</div></td>
		<td >
		<input type="checkbox" name="RevEntry" id="RevEntry" ></td>
		</td>-->
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
</div>
<br><br>
</div></div></div>
</td></tr>
</table>


<%
    System.out.println("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
    {
	 System.out.println("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate"));
		try{
		 JbReport jbReport = new JbReport();			   
		 request.setAttribute("links",jbReport.getJbReport(jbReportBean));
		 }
		 catch(Exception e){
		 System.out.println("Exception in Jsp Page "+ e.getMessage());
		 %> 
		 <script>
		 alert("Error :<%=e.getMessage()%>"); 
		 PageManager.DataService.setQueryField('endDate',null);
		 </script>
		 <%
		 }
%>	 
			
		<center><u><b><div class = "normaltext"> Journal Book<br><bean:message key="JourBook.Name"/><br><%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %>  </div></b></u></center>   		
		<div class = "alignleft"> <%= jbReportBean.getIsConfirmedCount() %> in <%= jbReportBean.getTotalCount() %> are unconfirmed   </div>		
		<div class="tbl2-container" id="tbl-container" >
		<display:table cellspacing="0"  decorator="org.displaytag.decorator.TotalTableDecorator" name="links" id="currentRowObject"  export="true" sort="list" class="its" > 
		<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		Journal Book
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %>
		</display:caption>
		<div STYLE="display:table-header-group">
		<display:column property="voucherdate" title="Voucher Date" style="width:70px" sortable="true"  headerClass="sortable"/>
		<display:column media="pdf" property="vouchernumber" title="Voucher#" /> 	
		<display:column media="excel" property="vouchernumber" title="Voucher#" />  
		<display:column media="xml" property="vouchernumber" title="Voucher#" />  	
		<display:column media="csv" property="vouchernumber" title="Voucher#" />  
		<display:column media="html" title="Voucher#" sortable="true" style="width:90px" headerClass="sortable">
		
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

			<display:column property="voucherName" title="Voucher Name" class="textAlign3" />			
			<display:column property="code" title="Account Code" style="width:45px"/>
			<display:column property="accName" title="Account Name" style="width:250px" />				 
			<display:column property="narration" title="Narration" style="width:100px" />			
			<display:column property="debitamount" title="Debit&nbsp(Rs.)" class="textAlign" />
			<display:column property="creditamount" title="Credit&nbsp(Rs.)" class="textAlign3" />
			<display:setProperty name="export.pdf" value="true" />
			<display:setProperty name="export.pdf.filename" value="JournalBook.pdf" /> 
		    <display:setProperty name="export.excel" value="true" />
		    <display:setProperty name="export.excel.filename" value="JournalBook.xls"/>
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
    System.out.println("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("2"))
    {
	 System.out.println("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate"));
		try{
		 JbReport jbReport = new JbReport();			   
		 request.setAttribute("links",jbReport.getJbReport(jbReportBean));
		 }
		 catch(Exception e){
		 System.out.println("Exception in Jsp Page "+ e.getMessage());
		 %> 
		 <script>
		 alert("Error :<%=e.getMessage()%>"); 
		 PageManager.DataService.setQueryField('endDate',null);
		 </script>
		 <%
		 }
%>	 
	 		
		<center><u><b><div class = "normaltext"> Journal Book <br><bean:message key="JourBook.Name"/><br><%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %>  </div></b></u></center>   		
		<div class = "alignleft"> <%= jbReportBean.getIsConfirmedCount() %> in <%= jbReportBean.getTotalCount() %> are unconfirmed   </div>		
		<div class="tbl5-container" id="tbl-container" > 
		<display:table cellspacing="0"  decorator="org.displaytag.decorator.TotalTableDecorator" name="links" id="currentRowObject"  export="true" sort="list" class="its" > 
		<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		Journal Book
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %>
		</display:caption>
		<div STYLE="display:table-header-group">
		<display:column property="voucherdate" title="Voucher Date" style="width:70px" sortable="true"  headerClass="sortable"/>
		<display:column media="pdf" property="vouchernumber" title="Voucher#" /> 	
		<display:column media="excel" property="vouchernumber" title="Voucher#" />  
		<display:column media="xml" property="vouchernumber" title="Voucher#" />  	
		<display:column media="csv" property="vouchernumber" title="Voucher#" />  
		<display:column media="html" title="Voucher#" sortable="true" style="width:90px" headerClass="sortable">

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

			<display:column property="voucherName" title="Voucher Name" class="textAlign3" />
			<display:column property="code" title="Account Code" style="width:45px"/>
			<display:column property="accName" title="Account Name" style="width:250px" />				 
			<display:column property="narration" title="Narration" style="width:100px" />			
			<display:column property="debitamount" title="Debit&nbsp(Rs.)" class="textAlign" />
			<display:column property="creditamount" title="Credit&nbsp(Rs.)" class="textAlign3" />
		
			<display:setProperty name="paging.banner.placement" value="false" />
			<display:setProperty name="export.pdf" value="true" />
			<display:setProperty name="export.pdf.filename" value="JournalBook.pdf" /> 
		    <display:setProperty name="export.excel" value="true" />
		    <display:setProperty name="export.excel.filename" value="JournalBook.xls"/>
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
