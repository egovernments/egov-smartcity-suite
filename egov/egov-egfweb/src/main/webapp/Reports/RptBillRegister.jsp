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
<%@page  import="com.exilant.eGov.src.reports.LongAmountWrapper,com.exilant.eGov.src.reports.*,java.io.*,java.util.*,java.sql.*,javax.sql.*,javax.naming.InitialContext,com.exilant.GLEngine.*,java.util.Date,java.text.SimpleDateFormat"%>
<%@page import="org.apache.log4j.Logger"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<!-- Inclusion of the CSS files that contains the styles -->
<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen" />
<link rel=stylesheet href="../css/print.css" type="text/css" media="print" />
<!-- link rel="stylesheet" href="../exility/screen.css" type="text/css" media="screen, print" / -->

<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" src="../resources/javascript/jsCommonMethods.js"></script>
<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js" type="text/javascript" ></SCRIPT>

<SCRIPT LANGUAGE="javascript">
<%
 	SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
	Date crntDate = new Date();
	String currDate = sdf.format(crntDate);
%>

var financialYear_ID,flag='false',flag1;
var financialYear,detlTypeId,fin_year;
var flag = '0', ulb ='';
//var fundId = '' ,fieldId='';
function loadRpt(){

	PageValidator.addCalendars();
	if(PageManager.DataService.getQueryField('startDate') != null || PageManager.DataService.getQueryField('endDate') != null)
	{
		document.getElementById('startDate').value=PageManager.DataService.getQueryField('startDate');
		document.getElementById('endDate').value=PageManager.DataService.getQueryField('endDate');
	}
	if(PageManager.DataService.getQueryField('fundId') != null)
	{
		document.getElementById('fundId').value=PageManager.DataService.getQueryField('fundId');
	}
	if(PageManager.DataService.getQueryField('fieldId') != null)
	{
		document.getElementById('fieldId').value=PageManager.DataService.getQueryField('fieldId');
	}
	if(PageManager.DataService.getQueryField('functionaryId') != null)
	{
		document.getElementById('functionaryId').value=PageManager.DataService.getQueryField('functionaryId');
	}

	/*fin_year=PageManager.DataService.getQueryField('fin_year');
	if(fin_year){
		flag = '1';
		PageManager.DataService.setQueryField('fin_year',fin_year);
		PageManager.DataService.setQueryField('financialYear_ID',fin_year);
		PageManager.DataService.removeQueryField('financialYear_ID',fin_year);
		PageManager.DataService.callDataService('getFinancialYrData');
		//selectData();
	}*/
	
	PageManager.ListService.callListService();
	PageManager.DataService.callDataService('getSupConType');
	PageManager.DataService.callDataService('companyDetailData');
	
	//document.getElementById('financialYear_financialYear').focus();
	var beanVal= <%=request.getParameter("fromBean")%>;
	
	if(beanVal==2)
	{
		document.getElementById("row3").style.display="block";
	}		

	
	
				
}

function buttonFlush()
{
	
	window.location="RptBillRegister.jsp";

}

//For Print Preview method
function buttonPrintPreview()
{
	document.getElementById('fromBean').value ="2";	   
	document.forms[0].submit();	
}

function pageSetup()
{
	document.body.leftMargin=0.75;
	document.body.rightMargin=0.75;
	document.body.topMargin=0.75;
	document.body.bottomMargin=0.75;	
}
//For print method
function buttonPrint()
{
	document.getElementById('fromBean').value ="2";
    var hide1,hide2; 
    hide1 = document.getElementById("table2");
	hide1.style.display = "none";	
	document.forms[0].submit();
	document.getElementById("row3").style.display="none";
  	if(window.print)
  	{ 
  		window.print();
	} 
	

}

function getDataList(){
	document.getElementById("displayCondition").value=1;
	
	if (!PageValidator.validateForm()){
			return;
	}
   
	//var temp=document.getElementById("financialYear_financialYear");
	//document.getElementById("finYear").value=temp.options[temp.selectedIndex].text;
	
	var endDate=document.getElementById("endDate").value;
	var startDate=document.getElementById("startDate").value;
	
		 /* checking Fiscal year start Date with End Date of Fiscal year */	
		var tMonth=endDate.substr(endDate.length-7,2);
		if(tMonth<4)
		var fiscalYearStartDate="01/04/"+(endDate.substr(endDate.length-4,4)-1);
		else
		var fiscalYearStartDate="01/04/"+endDate.substr(endDate.length-4,4);
		//bootbox.alert("fiscalYearStartDate :" + fiscalYearStartDate);
		if(compareDate(fiscalYearStartDate,startDate) == -1 )
		{ 
			bootbox.alert("Start Date  should be in same financial year");
		   document.getElementById('startDate').focus();
		   return;
		}
		if(compareDate(fiscalYearStartDate,endDate) == -1 )
		{ 
			bootbox.alert(" End Date should be in same financial year");
		   document.getElementById('endDate').focus();
		   return;
		}
		 /*to check whether the Start Date is greater than the End Date*/
		if( compareDate(formatDate6(startDate),formatDate6(endDate)) == -1 )
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
			document.getElementById('endDate').value='';
		
		}



	
	//bootbox.alert(document.getElementById("displayCondition").value);
	if(document.getElementById('displayCondition').value!=0)
		document.rptBillReg.submit();
}


</SCRIPT>
</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onLoad="loadRpt()" onKeyDown ="CloseWindow(window.self);"><!------------------ Header Begins Begins--------------------->
<jsp:useBean id = "rptBRBean" scope ="request" class="com.exilant.eGov.src.reports.RptBillRegisterBean"/>
<jsp:setProperty name = "rptBRBean" property="finId"/> 
<jsp:setProperty name = "rptBRBean" property="conSupTypeId"/> 
<jsp:setProperty name = "rptBRBean" property="fundId" />
<jsp:setProperty name = "rptBRBean" property="fieldId" />
<jsp:setProperty name = "rptBRBean" property="functionaryId" />
<jsp:setProperty name = "rptBRBean" property="startDate" />
<jsp:setProperty name = "rptBRBean" property="endDate" />

<form name="rptBillReg" action="RptBillRegister.jsp"?fundId="+fundId+"&fieldId="+fieldId+"&functionaryId="+functionaryId+"&startDate="+startDate+"&endDate="+endDate method="post" >
<input type=hidden name="finId" id="finId" value="">
<input type=hidden name="conSupTypeId" id="conSupTypeId" value="">
<input type=hidden name="finYear" id="finYear" value="">
<input type=hidden name="fromBean" id="fromBean" value="0"> 

<center>
<br>
<table align='center' id="table2">
<tr><td>
<div id="main"><div id="m2"><div id="m3" style="width:810px">
<center>

	<table width="100%"  align="center">
		<tr >
			<td  class="tableheader" valign="center"  ><span>Bill Register Report</span></td>
		</tr>
		<tr> 
			<td   align="right">&nbsp; Form GEN-13&nbsp; </td>
			
		</tr>
		<tr>
			
			<td   class="labelcell" align="right">&nbsp Name of the ULB :  </td>
			<td  class="headerblack2" id="companyDetail_name" name="companyDetail_name"  exilListSource="companyDetailData" exilMustEnter="true">
			</td>
		</tr>

		<tr>
					
			<!--<td align="right" valign="center" class="labelcell" ><div align="right">Financial Year<span class="leadon">*&nbsp</span></div>
			</td>
			<td align="left" valign="center" class="normaltext" >
				<div align="left"  class="smallfieldcell" >
				  <SELECT  id="financialYear_financialYear"  name="financialYear_financialYear" class="smallfieldinput" exilListSource="financialYearList"   exilMustEnter="true" onChange="finYearchange()" width="10%";>
				  </SELECT>
				  </div>
			</td>-->
			<td align="right"><div align="right" valign="center" class="labelcell">Fund<span class="leadon"> &nbsp;</span></div></td>
		<td align="left"  class="smallfieldcell">
			<SELECT class="fieldinput" id="fundId" name="fundId" exilListSource="fundNameList" ></SELECT>
		</td>
		</tr>
			<tr>
		<td align="right"><div align="right" valign="center" class="labelcell">Field<span class="leadon"> &nbsp;</span></div></td>
		<td align="left"  class="smallfieldcell">
			<SELECT class="fieldinput" id="fieldId" name="fieldId" exilListSource="field_name" ></SELECT>
		</td>

		<td align="right"><div  valign="center" class="labelcell">Functionary &nbsp;</div></td>
		<td align="left" class="smallfieldcell">
			<SELECT class="fieldinput" id="functionaryId" name="functionaryId"  exilListSource="functionaryName"></SELECT>
		</td>
	</tr>
	<tr>
		<td align="right"><div align="right" valign="center" class="labelcell">Starting Date<span class="leadon">* &nbsp;</span></div></td>
		<td align="left"  class="smallfieldcell">
			<input name="startDate" id="startDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')" exilCalendar="true" exilMustEnter="true">
		</td>
		
		<td align="right"><div align="right" valign="center" class="labelcell" >End Date<span class="leadon"> &nbsp;</span></div></td>
		<td class="smallfieldcell">
			<input name="endDate" id="endDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')" exilCalendar="true" >
		</td>

	</tr>
	</table>
 	
 			
	<table width=100% border="0" cellpadding="0" cellspacing="0" align="center">
		<tr>
			<td colspan=11> </td>
		</tr>
		<tr>
			<td colspan="4" align="middle" width="100%"><!-- Buttons Start Here -->
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
					<td align="right">
					<input type=button class=button href="#" onclick="getDataList()"; value="Search"></td>
					
					<td align="right">
					<input type=button class=button onclick=buttonFlush(); href="#" value="Cancel"></td>
					
					<td align="right">
					<input type=button class=button onclick=window.close() href="#" value="Close"></td>
					
					<!-- Print Button start -->
			<td align="right">
			<input type=button class=button onclick="buttonPrintPreview()" href="#" value="Print Preview"></td>
			<!-- Print end-->
			</tr>
				</table><!-- Buttons End Here -->
			</td>
		</tr>
	</table>
</div></div></div>
</td></tr>
</table>	
	</center>
	<input type="hidden" name="displayCondition" id="displayCondition" value="0"> 
	<%
		Logger LOGGER = Logger.getLogger("RptBillRegister.jsp");
	    LOGGER.info("Before submit "+request.getParameter("displayCondition"));
	    if(request.getParameter("displayCondition") !=null && request.getParameter("displayCondition").equals("1"))
	    {
		 LOGGER.info("after submit "+request.getParameter("displayCondition"));
			 try{
			 RptBillRegisterList BillRegister = new RptBillRegisterList();			   
			 request.setAttribute("links",BillRegister.getRptBillRegisterList(rptBRBean));
			 if(rptBRBean.getFundName() != null)
				request.setAttribute("fundName",rptBRBean.getFundName());
			 else
				 request.setAttribute("fundName","");
			 if(rptBRBean.getFieldName() != null)
				request.setAttribute("fieldName",rptBRBean.getFieldName());
			 else
				 request.setAttribute("fieldName","");	
			  if(rptBRBean.getFunctionaryName() != null)
				request.setAttribute("functionaryName",rptBRBean.getFunctionaryName());
			 else
				 request.setAttribute("functionaryName","");
			 if(rptBRBean.getUlbname() != null)
			 request.setAttribute("ulbname",rptBRBean.getUlbname());
			 else
				request.setAttribute("ulbname","");

			 }
			 catch(Exception e){
			 LOGGER.error("Exception in Jsp Page "+ e.getMessage());
			 %> 
			 <script>
			 bootbox.alert("Error :<%=e.getMessage()%>"); 
			 //PageManager.DataService.setQueryField('endDate',null);
			 </script>
			 <%  }  %>	 

	
	
	
	
	<div class = "normaltext"><b><font size=2> GEN-13: Register of the Bills for <%= request.getAttribute("ulbname") %> </b> From <%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %> </b> For <%= request.getAttribute("fundName") %>,<%= request.getAttribute("functionaryName") %>,<%= request.getAttribute("fieldName") %> </font>   </b></div>
		
	<div  id="tbl-container" class="tbl4-container">	
	 <display:table name="links" id="links"  export="true" sort="list" class="its" cellspacing="0">
	 <display:caption media="pdf">
		GEN-13: Register of the Bills for <%= request.getAttribute("ulbname") %> From <%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %>
For <%= request.getAttribute("fundName") %>,<%= request.getAttribute("functionaryName") %>,<%= request.getAttribute("fieldName") %>		
		</display:caption>
		 
		<display:column property="slno" title="Sl No" style="width:6px; align=center"/>			
		<display:column property="billDate" title="<center>Date of presentation by the Supplier/Dept</center>" style="width:90px; align=center"  />
		<display:column property="conSupName" title="<center>Name of Party/Dept</center>" />	
		<display:column property="particulars" title="<center>Particulars</center>" />
		  <display:column property="billAmount" title="<center>Amount of Bill (Rs)</center>"  class="textAlign" />
		  <display:column property="approvedBy" title="<center>Initials of Authorised Officer</center>" />
		  <display:column property="sanctionedDate" title="<center>Date of &nbsp;&nbsp;Sanction&nbsp;&nbsp;&nbsp;&nbsp;</center>"   style="width:90px; align=center" />	
		   <display:column property="voucherNo" title="<center>Voucher Number</center>" class="textAlign" />
		 <display:column property="sanctionedAmount" title="<center>Amount Sanctioned</center>" class="textAlign" />
		  <display:column property="disallowedAmount" title="<center>Amount Disallowed (Rs)</center>" class="textAlign" />
		   <display:column property="paidAmt" title="<center>Paid Amount (Rs)</center>" class="textAlign" />
		    <display:column property="paymentDate" title="<center>&nbsp;&nbsp;Date&nbsp;of&nbsp; &nbsp;Payment&nbsp; &nbsp;or &nbsp;issue &nbsp;of&nbsp;cheque &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</center>"  style="width:90px; align=center"/>
		  <display:column property="balanceAmount" title="<center>Balance outstanding at the end of the year (Rs)</center>" class="textAlign" />	
		  <display:column property="remarks" title="<center>Remarks </center>" class="textAlign" />
		 
		  <display:setProperty name="export.pdf" value="true" />
		  <display:setProperty name="export.pdf.filename" value="RptBillRegister.pdf" /> 
	      <display:setProperty name="export.excel" value="true" />
	      <display:setProperty name="export.excel.filename" value="RptBillRegister.xls"/>
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
   // System.out.println("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("2"))
    {
	// System.out.println("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate"));
		try{
		 RptBillRegisterList BillRegister = new RptBillRegisterList();			   
			 request.setAttribute("links",BillRegister.getRptBillRegisterList(rptBRBean));
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
	}

%>
<table border="0" cellpadding="0" cellspacing="0">
		<tr  style="DISPLAY: none" id="row3" name="row3">
			
			<td align="right">
			<input type=button class=button onclick=window.close() href="#" value="Close"></td>
			
			<!-- Print Button start -->
			<td align="right">
			<input type=button class=button onclick="pageSetup(); buttonPrint()" href="#" value="Print"></td>
			<!-- Print end-->

		</tr>
		</table>	
	

</form>


</body>
</html>
