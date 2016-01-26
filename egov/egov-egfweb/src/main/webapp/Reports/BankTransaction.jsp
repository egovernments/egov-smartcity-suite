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
	<!--/**************************************************************
	* Name		    		: BankTransaction.jsp  
	* Type		    		: Java Server Page
	* Description			: Bank Transaction Summary
	* Author	    		: Nandini Ramesh
	**************************************************************/-->
<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page buffer = "16kb" %> 
<%@page  import="java.text.SimpleDateFormat,org.egov.infstr.utils.EGovConfig,java.io.*,com.exilant.eGov.src.reports.*,javax.naming.InitialContext,java.io.*,
			java.util.*"%>
<html>
<head>
<title>Bank Transaction Report</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<!-- <META http-equiv=pragma content=no-cache> -->
<!-- Inclusion of the CSS files that contains the styles -->
<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen" />
<link rel=stylesheet href="../css/egov.css" type="text/css" media="print" />
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
var bank_val="";
var accNum="";
var sDate="";
var eDate="";
var sIndex,sValue,sText;

function onLoad()
{
	PageValidator.addCalendars();
	if(document.getElementById("startDate").value=="")
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
	PageManager.DataService.callDataService('bankAndBranch');
	document.getElementById("endDate").value="<%=currDate%>";
	
}
function ButtonPress()
{
	if (!PageValidator.validateForm())
	{
		return;
	}
	
	document.getElementById('fromBean').value = 1;
	var bankId=document.getElementById('bank_id');
	document.getElementById('bankName').value=bankId.options[bankId.selectedIndex].text;

	var bankAccId=document.getElementById('bankAccount_id');
	document.getElementById('bankAccNo').value=bankAccId.options[bankAccId.selectedIndex].text;
	bank_val=bankId.value;
	accNum=document.getElementById('bankAccount_id').value;
	sDate=document.getElementById("startDate").value;
	eDate=document.getElementById("endDate").value;	
	
	/* checking Fiscal year start Date with End Date of Fiscal year */	
		var tMonth=eDate.substr(eDate.length-7,2);
		if(tMonth<4)
		var fiscalYearStartDate="01/04/"+(eDate.substr(eDate.length-4,4)-1);
		else
		var fiscalYearStartDate="01/04/"+eDate.substr(eDate.length-4,4);
		if(compareDate(fiscalYearStartDate,sDate) == -1 )
		{ 
			bootbox.alert("Start Date and End Date should be in same financial year");
		   document.getElementById('startDate').focus();
		   return;
		} 
	
	/*To check whether Start Date is Greater than End Date*/
	if( compareDate(formatDate6(sDate),formatDate6(eDate)) == -1 )
	{
		bootbox.alert('Start Date cannot be greater than End Date');
		document.getElementById('startDate').value='';
		document.getElementById('endDate').value='';
		document.getElementById('startDate').focus();
		return false;
	}
	
	/*to check whether the End Date is greater than the Current Date*/
		if( compareDate(formatDate6("<%=currDate%>"),formatDate6(eDate)) == 1 )
		{
			bootbox.alert('End Date cannot be greater than Current Date');
			document.getElementById('endDate').value='';
			document.getElementById('endDate').focus();	
			return;	
		}	

	document.forms[0].submit();
}

function getAccountNumbers()
{
	sIndex=null;
	var ctrl = document.getElementById('bank_id');
	var branchid = ctrl.options[ctrl.selectedIndex].value;
	var a = Array(2);
	a = branchid.split('-');
	branchid = a[1];
	var obj = document.getElementById('bankAccount_id');
	obj.setAttribute('exilListSource','accountList');
	PageManager.DataService.setQueryField('branchId',branchid);
	PageManager.DataService.callDataService('accNumber');

}
function getCode(obj1)
{
	var ctrl = document.getElementById('bankAccount_id');
	var accId = ctrl.options[ctrl.selectedIndex].value;
	PageManager.DataService.setQueryField('bankAccountID',accId);
	PageManager.DataService.callDataService('getBACode');
}
function buttonFlush()
{
	window.location="BankTransaction.jsp";
}

function afterRefreshPage(dc)
{ 
	if(dc.values['serviceID']=='getBACode')
	{
	   var ctrl = document.getElementById('bankAccount_id');
	   accntMod(ctrl);
	}
	var afterSub="<%=(request.getParameter("fromBean"))%>";
	try
	{
	if(dc.values['serviceID'] =='accNumber')
	{	
		var accnoObj=document.getElementById('bankAccount_id');
		accntMod(accnoObj);
	}
	}catch(err) {}
	if(afterSub != "null")
	{

		if(dc.values['serviceID'] == 'bankAndBranch')
		{
			getAccountNumbers();
		}
		PageManager.DataService.removeQueryField('bank_id');

	}
	if(afterSub != "null")
	{
		if(dc.values['serviceID'] == 'accNumber')
		{
			PageManager.DataService.removeQueryField('bankAccount_id');
			
		}
	}
}

function accntMod(obj)
{
	if(obj.selectedIndex==-1)	 return;
	 if(sIndex!=null)
	{
			obj.options[sIndex]= new Option(sText,sValue);
	}

	var text=obj.options[obj.selectedIndex].innerHTML;
        var temp=text.split(" -- ");
	sIndex=obj.selectedIndex;
	sValue=obj.options[obj.selectedIndex].value;
	sText=text;
	if(!temp[1])
		 temp[1]=" ";

	document.getElementById("acc_Desc").value=temp[1];
	try{
		if(temp[1]!=null && temp[1]!="" )
	   {
	     obj.options[obj.selectedIndex].text=temp[0];
	   }
	}catch(err){}
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
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onLoad="onLoad()">
<jsp:useBean id = "btReportBean" scope ="request" class="com.exilant.eGov.src.reports.BankTransactionReportBean"/>
<jsp:setProperty name = "btReportBean" property="*"/>
<form name="BankTransaction"  action = "BankTransaction.jsp?bankAccount_id=" +accNum+ "&startDate="+sDate+ "&endDate="+eDate method = "post">
<input type="hidden" name="fromBean" id="fromBean" value="0">
<input type="hidden" name="bankName" id="bankName" value="">
<input type="hidden" name="bankAccNo" id="bankAccNo" value="">

<div class="tbl-header1" id="tbl-header1">
<table align='center' id="table2">
<tr><td>
<div id="main"><div id="m2"><div id="m3" style="width:810px">

<table width="100%" border=0 cellpadding="3" cellspacing="0">
	<tr >
			<td colspan="4" class="tableheader" valign="center" colspan="4" width="100%"><span id="screenName">
				Bank Transaction</span>
			</td>
	</tr>
	<tr>
		<td align="right">
			<div  valign="center" class="labelcell"> Bank<span class="leadon">* &nbsp;</span>
			</div>
		</td>
		
		<td class="smallfieldcell">
			<select  name="bank_id" id="bank_id" class="smalltext" style="width:300px; background-color: #F2F7FB;" onChange="getAccountNumbers()"  exilListSource="bankAndBranch" exilMustEnter="true"></select>
		</td>
		
		<td align="right"><div align="right" valign="center" class="labelcell">
					Transaction Type</div></td>
				<td class="smallfieldcell"><SELECT class="fieldinput" id="transactioType" name="transactioType" >
						<option value="">--Choose--</option>
						<option value="0" >Receipt</option>
						<option value="1" >Payments </option>
					</SELECT>
				</td >
	</tr>
	<tr>
		<td></td>
		<td></td> <td></td> <td></td>
	</tr>
	<tr>
	 <td align="right"> 
			<div   valign="center" class="labelcell">Account Number<span class="leadon">* &nbsp;</span>
			</div>
		</td> 
		<td class="smallfieldcell">
			<SELECT name="bankAccount_id" id="bankAccount_id" onChange="getCode(this)" class="fieldinput" exilMustEnter="true"></SELECT>&nbsp;<input type="text" id="acc_Desc" name="acc_Desc" class="fieldinput"  readonly tabindex=-1>
		</td>
	</tr>
	<tr>
		<td align="right"><div  valign="center" class="labelcell">Starting Date<span class="leadon">* &nbsp;</span></div></td>
		<td align="left" class="smallfieldcell">
			<input name="startDate" id="startDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" exilMustEnter="true">
		</td>
		<td align="right"><div  valign="center" class="labelcell" >End Date<span class="leadon">* &nbsp;</span></div></td>
		<td class="smallfieldcell">
			<input name="endDate" id="endDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" exilMustEnter="true">
		</td>
	</tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr id="row1">
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
	
<tr style="DISPLAY: none"  id="row2" name="row2">
		<td colspan="4" align="middle"><!-- Buttons Start Here -->
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
  <br> <br>
</div></div></div>
</td></tr>
</table>
</div> 

<%
   
	if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
	 {
    	
	 System.out.println("after submit "+request.getParameter("fromBean")+" startDate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate") );
		try{
		 BankTransaction bankTran = new BankTransaction();			   
		 request.setAttribute("links",bankTran.getbankTranReport(btReportBean));
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

<center><b><div class = "normaltext"> Bank Transaction for <%= request.getParameter("bankName") %> from <%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %></div></b> </center>   		
		<div class="tbl-container" id="tbl-container">
		<display:table cellspacing="0" name="links"  id="currentRowObject" export="true"  sort="list" class="its">
		<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 Bank Transaction for <%= request.getParameter("bankName") %> from <%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %>
		</display:caption>
		<div STYLE="display:table-header-group">
		
		<display:column property="serialNo" style="width:30px" title="Sl No"  />
		<display:column style="width:80px" title="<center>Voucher&nbsp;Number</center>"   >
		<% String cgn =((BankTransactionReportBean)pageContext.findAttribute("currentRowObject")).getCgn();
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
			String url = ((BankTransactionReportBean)pageContext.findAttribute("currentRowObject")).getVhNo();%>

			<a href="<%=link%>"><%=url%></a>
		</display:column>
		
		<display:column property="chqNo" style="width:50px" title="<center>Cheque&nbsp;Number</center>"   />
		<display:column property="type" style="width:80px" title="<center>Type</center>" sortable="true"  headerClass="sortable"  /> 
		<display:column property="vhDate"  style="width:80px" title="<center>Voucher&nbsp;Date</center>"  sortable="true"  headerClass="sortable"   /> 
		<display:column property="chqDate"  style="width:100px" title="<center>Cheque&nbsp;Date</center>"  sortable="true"  headerClass="sortable"  />	
		<display:column property="paymentAmt" style="width:40px" title="<center>Payment&nbsp;Amount</center>"  style="text-align:right" sortable="true"  headerClass="sortable"/>
		<display:column property="receiptAmt" style="width:80px" title="<center>Receipt&nbsp;Amount</center>" class="textAlign2" style="text-align:right" sortable="true"  headerClass="sortable"/>	
		<display:column property="payinslipNo" style="width:80px" title="<center>Payinslip&nbsp;Number</center>" class="textAlign2" style="text-align:right" sortable="true"  headerClass="sortable"/>	
		<display:column property="payinslipDate" style="width:80px" title="<center>Payinslip&nbsp;Date</center>" class="textAlign2" style="text-align:right" sortable="true"  headerClass="sortable"/>
		<display:setProperty name="export.pdf" value="true" />
		<display:setProperty name="export.pdf.filename" value="BankTransaction.pdf" /> 
	    <display:setProperty name="export.excel" value="true" />
	    <display:setProperty name="export.excel.filename" value="BankTransaction.xls"/>
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
    	
	 System.out.println("after submit "+request.getParameter("fromBean")+" startDate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate") );
		try{
		 BankTransaction bankTran = new BankTransaction();			   
		 request.setAttribute("links",bankTran.getbankTranReport(btReportBean));
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

<center><b><div class = "normaltext"> Bank Transaction for <%= request.getParameter("bankName") %> from <%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %></div></b> </center>   		
		<div class="tbl4-container" id="tbl-container"  style="width:700px">
		<display:table cellspacing="0" name="links"  id="currentRowObject" export="true"  sort="list" class="its">
		<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 Bank Transaction for <%= request.getParameter("bankName") %> from <%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %>
		</display:caption>
		<div STYLE="display:table-header-group">
		
		<display:column property="serialNo" style="width:30px" title="Sl No"  />
		<display:column property="vhNo" style="width:30px" title="<center>Voucher&nbsp;Number</center>"  class="textAlign9"  />
		<display:column property="chqNo" style="width:20px" title="<center>Cheque&nbsp;Number</center>"  class="textAlign9" />
		<display:column property="type" style="width:80px" title="<center>Type</center>"  class="textAlign9"/> 
		<display:column property="vhDate"  style="width:50px" title="<center>Voucher&nbsp;Date</center>"    class="textAlign9"/> 
		<display:column property="chqDate"  style="width:50px" title="<center>Cheque&nbsp;Date</center>"    class="textAlign9" />	
		<display:column property="paymentAmt" style="width:80px" title="<center>Payment&nbsp;Amount</center>"  style="text-align:right"  class="textAlign9"/>
		<display:column property="receiptAmt" style="width:80px" title="<center>Receipt&nbsp;Amount</center>"  style="text-align:right"  class="textAlign5" />
		<display:column property="payinslipNo" style="width:80px" title="<center>Payinslip&nbsp;Number</center>" class="textAlign2" style="text-align:right" sortable="true"  headerClass="sortable"/>	
		<display:column property="payinslipDate" style="width:80px" title="<center>Payinslip&nbsp;Date</center>" class="textAlign2" style="text-align:right" sortable="true"  headerClass="sortable"/>
		<display:setProperty name="export.pdf" value="true" />
		<display:setProperty name="export.pdf.filename" value="BankTransaction.pdf" /> 
	    <display:setProperty name="export.excel" value="true" />
	    <display:setProperty name="export.excel.filename" value="BankTransaction.xls"/>
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
