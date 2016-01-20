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
<%@page  import="com.exilant.eGov.src.reports.LongAmountWrapper,com.exilant.eGov.src.reports.*,java.io.*,java.util.*,java.sql.*,javax.sql.*,javax.naming.InitialContext,com.exilant.GLEngine.*,org.egov.infstr.utils.EGovConfig"%>
<%@ page import="org.apache.log4j.Logger"%>
<!--  -->

<html>
<head>
<title>Cheque In Hand Report</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<!-- <META http-equiv=pragma content=no-cache> -->
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

var fuId="";
var fuSrId="";
var sDate="";
var eDate="";
var gl1="";
var gl2="";
var forRE="";
var reportType="ChequeInHand";
var boundaryLevelId='';
var boundaryTypeId='';
function afterRefreshPage(dc)
{
	boundaryLevelId=dc.values['boundaryLevelId'];
	boundaryTypeId=dc.values['boundaryTypeId'];
	PageManager.DataService.setQueryField('boundaryLevelId',boundaryLevelId);
	PageManager.DataService.setQueryField('boundaryTypeId',boundaryTypeId);
	PageManager.ListService.callListService();	
}
function onLoad()
{
	PageValidator.addCalendars();
	//In Cheques in hand report--Account Code drop down list it is showing cash in
	//transit also,acyually it should be cheque in hand code only.
	// it should be based on boundary level.
	PageManager.DataService.callDataService('BoundaryLevelList');
		
	//var purposeId="5";	
	//PageManager.DataService.setQueryField('cashCode',purposeId);
	//PageManager.DataService.setQueryField('boundaryLevelId',boundaryLevelId);
	//PageManager.DataService.setQueryField('boundaryTypeId',boundaryTypeId);
	//PageManager.ListService.callListService();	
	var beanVal= <%=request.getParameter("fromBean")%>;
	if(beanVal==2)
	{
		document.getElementById("row3").style.display="block";
		document.getElementById("row2").style.display="none";		
	}
	document.getElementById("glCode1").focus();
}

function getAccCode()
{
	var obj=document.getElementById('glcode1');	
	var text=obj.options[obj.selectedIndex].innerHTML;		
    var temp=text.split(" -- ");    	   
	sIndex=obj.selectedIndex;
	sValue=obj.options[obj.selectedIndex].value;
	sText=text;
	if(!temp[1])
		temp[1]=" ";	
	
	if(temp[1]!=null && temp[1]!="" )
	{
		obj.options[obj.selectedIndex].text=temp[0];
	}	
}


function ButtonPress()
{
	
	if (!PageValidator.validateForm()){
		//document.getElementById('startDate').value='';
		//document.getElementById('endDate').value='';
		return;
	}
	document.getElementById('fromBean').value = 1;
	document.getElementById('glCode2').value=document.getElementById('glCode1').value;
	var fuObj=document.getElementById('fund_id');
	
	if(fuObj.value!='' && fuObj.selectedIndex!=-1)
		fuId=fuObj.options[fuObj.selectedIndex].value;
		
	var fuSrObj=document.getElementById('fundSource_id');
	if(fuSrObj.value!='' && fuSrObj.selectedIndex!=-1)
		fuSrId=fuSrObj.options[fuSrObj.selectedIndex].value;
	sDate=document.getElementById("startDate").value;
	eDate=document.getElementById("endDate").value;
	/*if( compareDate(formatDate6(sDate),formatDate6(eDate)) == -1 ){
		bootbox.alert('Start Date cannot be greater than End Date');
		document.getElementById('startDate').value='';
		document.getElementById('endDate').value='';
		document.getElementById('startDate').focus();
		return false;
	}*/
	
	/* checking Fiscal year start Date with End Date of Fiscal year */
	
	var tMonth=eDate.substr(eDate.length-7,2);
	if(tMonth<4)
	var fiscalYearStartDate="01/04/"+(eDate.substr(eDate.length-4,4)-1);
	else
	var fiscalYearStartDate="01/04/"+eDate.substr(eDate.length-4,4);
	
	if(compareDate(fiscalYearStartDate,sDate) == -1 )
	{ 
		bootbox.alert("Start Date should be in fiscal year");
	   document.getElementById('startDate').focus();
	   return;
	 }
	gl1=document.getElementById("glCode1").value;
	gl2=document.getElementById("glCode2").value;
	
//	forRE=document.getElementById("forRevEntry").value;
	document.forms[0].submit();
	
	
}
	
function buttonFlush()
{
	
	window.location="chequeInHand.jsp";

}
/*
function openSearch(obj)
{
	 var a = new Array(2);
		var	screenName="5";
		var str= "../HTML/Search.html?cashCode="+screenName;
		var sRtn = showModalDialog(str,"","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
    	a = sRtn.split("`~`");
		var type = document.getElementById('glCode1');
		type.value = a[0];
}
*/
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
</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onLoad="onLoad()">
<jsp:useBean id = "glReportBean" scope ="request" class="com.exilant.eGov.src.reports.GeneralLedgerReportBean"/>
<jsp:setProperty name = "glReportBean" property="*"/>  
<form name="chequeInHandRpt" action = "chequeInHand.jsp?fund_id="+fuId+"&fundSource_id="+fuSrId+"&glCode1="+gl1+"&glCode2="+gl2+"&startDate="+sDate+"&endDate="+eDate+ "&reportType="+reportType method = "post">
<input type="hidden" name="fromBean" id="fromBean" value="0"> 
<input type="hidden" name="glCode2" id="glCode2" value=""> 
<input type="hidden" name="reportType" id="reportType" value="ChequeInHand"> 

<div class= "tbl-header1" id="tbl-header1">
<center>
<br>
<table align='center' id="table2">
<tr><td>
<div id="main"><div id="m2"><div id="m3" style="width:810px">

<table width="100%" border=0 cellpadding="3" cellspacing="0">
<tr >
		<td colspan="4" class="tableheader" valign="center" colspan="4" width="100%"><span id="screenName">Cheque In Hand</span></td>
	</tr>
	<tr>
		<td align="right"><div  valign="center" class="labelcell" >Account Code<span class="leadon">* &nbsp;</span></div></td>
		<td  class="smallfieldcell">
			<!-- <input class="fieldinput" name="glCode1" id="glCode1" exilMustEnter="true" ><IMG id=IMG1 onclick="openSearch('chartofaccounts');" height=22 src="/egi/resources/erp2/images/plus1.gif" width=25 align=top border=0> -->
			<SELECT class="combowidth1" id="glCode1" name="glCode1" class="combowidth1" exilListSource="mappedCodes2" onChange="getAccCode()" exilMustEnter="true"></SELECT>
		</td>
		<td align="right"><div  valign="center" class="labelcell">Fund<span class="leadon">* &nbsp; </span></div></td>
		<td align="left"  class="smallfieldcell">
			<SELECT class="fieldinput" id="fund_id" name="fund_id" exilMustEnter="true" exilListSource="fundNameList"></SELECT>
		</td>
	</tr>
	<tr>
	<td align="right"><div  valign="center" class="labelcell">Financing Source &nbsp;</div></td>
		<td align="left"  class="smallfieldcell">
			<SELECT class="fieldinput" id="fundSource_id" name="fundSource_id" exilListSource="fundSourceNameList"></SELECT>
		</td>
		<td></td><td></td>
		<!--<td><div align="right" valign="center" class="normaltext">Show Reversed Entry</div></td>
		<td >
			<input type="checkbox" name="forRevEntry" id="forRevEntry" ></td>
		</td>-->
	</tr>
	<tr>
		<td align="right"><div  valign="center" class="labelcell">Starting Date<span class="leadon">* &nbsp;</span></div></td>
		<td align="left"  class="smallfieldcell">
			<input name="startDate" id="startDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" exilMustEnter="true">
		</td>
		<td align="right"><div  valign="center" class="labelcell" >End Date<span class="leadon">* &nbsp;</span></div></td>
		<td  class="smallfieldcell">
			<input name="endDate" id="endDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" exilMustEnter="true">
		</td>
	</tr>
	<tr height="5"><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr name="row2" id="row2">
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
	Logger LOGGER = Logger.getLogger("chequeInHand.jsp");
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
    {
		 try{
		 GeneralLedgerReportList generalLedgerList = new GeneralLedgerReportList();			   
		 request.setAttribute("links",generalLedgerList.getGeneralLedgerList(glReportBean));
		 }
		 catch(Exception e){
			LOGGER.info("Exception in Jsp Page "+ e.getMessage());
		 %> 
		 <script>
		 bootbox.alert("Error :<%=e.getMessage()%>"); 
		 PageManager.DataService.setQueryField('endDate',null);
		 </script>
		 <%
		 }
%>		
		 <div class = "normaltext"><b>Cheque In Hand Report<br> <%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %>  </b></div>
		 <div class = "alignleft"> <%= glReportBean.getIsConfirmedCount() %> in <%= glReportBean.getTotalCount() %> are unconfirmed   </div>
		 <div class="tbl2-container" id="tbl-container"> 
		 <display:table cellspacing="0" decorator="org.displaytag.decorator.TotalTableDecorator" name="links" id="currentRowObject"  export="true" sort="list" class="its" >
		 <display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			Cheque In Hand Report &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %>
			</display:caption> 
		  <div STYLE="display:table-header-group">				 		
		 <display:column property="voucherdate" title="VoucherDate" />
		 <display:column media="html" title="Voucher#" > 
		 
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
				
				<display:column media="pdf" property="vouchernumber" title="Voucher#" /> 	
				 <display:column media="excel" property="vouchernumber" title="Voucher#" />  
				 <display:column media="xml" property="vouchernumber" title="Voucher#" />  	
		 		<display:column media="csv" property="vouchernumber" title="Voucher#" />  
		 		 <display:column property="name" title="Particulars" style="width:160px" media="html"/>
		 		 <display:column property="name" title="Particulars" media="pdf" />			 
		 		 <display:column property="name" title="Particulars" media="xml" />	
		 		<display:column property="name" title="Particulars" media="excel"  />	
		 		<display:column property="name" title="Particulars" media="csv"  />	
		 		<display:column property="amount" title="Amount&nbsp;(Rs.)" />	

		 		<display:column property="cheques" style="width:200px;word-wrap:break-word" title="Cheque-Payee"  />
		 		 <display:column property="narration" style="width:200px;word-wrap:break-word" title="Narration"  />
		 		  <display:column property="type" title="Transaction" />
		 		  <display:column property="debitamount" title="Debit&nbsp;(Rs.)" class="textAlign" />
		 		  <display:column property="creditamount" title="Credit&nbsp;(Rs.)" class="textAlign3" />	
		 		  <display:setProperty name="export.pdf" value="true" />
					<display:setProperty name="export.pdf.filename" value="chequeInHand.pdf" /> 
				    <display:setProperty name="export.excel" value="true" />
				    <display:setProperty name="export.excel.filename" value="chequeInHand.xls"/>
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
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("2"))
    {
		 try{
		 GeneralLedgerReportList generalLedgerList = new GeneralLedgerReportList();			   
		 request.setAttribute("links",generalLedgerList.getGeneralLedgerList(glReportBean));
		 }
		 catch(Exception e){
		 LOGGER.info("Exception in Jsp Page "+ e.getMessage());
		 %> 
		 <script>
		 bootbox.alert("Error :<%=e.getMessage()%>"); 
		 PageManager.DataService.setQueryField('endDate',null);
		 </script>
		 <%
		 }
%>	 
	 
		 <div class = "normaltext"><b>Cheque In Hand Report<br> <%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %>  </b></div>
		 <div class = "alignleft"> <%= glReportBean.getIsConfirmedCount() %> in <%= glReportBean.getTotalCount() %> are unconfirmed   </div>
		 <div class="tbl5-container" id="tbl-container">
		 <display:table cellspacing="0" decorator="org.displaytag.decorator.TotalTableDecorator" name="links" id="currentRowObject" export="true" sort="list" class="its" > 
		 	<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			Cheque In Hand Report &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %>
			</display:caption> 			 		
		 <div STYLE="display:table-header-group">
		 <display:column property="voucherdate" title="VoucherDate" />
		 <display:column media="html" title="Voucher#" > 
		 
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
				<display:column media="pdf" property="vouchernumber" title="Voucher#" /> 	
				 <display:column media="excel" property="vouchernumber" title="Voucher#" />  
				 <display:column media="xml" property="vouchernumber" title="Voucher#" />  	
		 		<display:column media="csv" property="vouchernumber" title="Voucher#" /> 		
		 		 <display:column property="name" style="width:160px" title="Particulars" />		 		 
		 		<display:column property="amount" title="Amount&nbsp;(Rs.)" />	
		 		
		 		<display:column property="cheques" style="width:200px;word-wrap:break-word" title="Cheque-Payee"  />
		 		 <display:column property="narration"  style="width:200px;word-wrap:break-word" title="Narration"  />
		 		  <display:column property="type" title="Transaction" />
		 		  <display:column property="debitamount" title="Debit&nbsp;(Rs.)" class="textAlign" />
		 		  <display:column property="creditamount" title="Credit&nbsp;(Rs.)" class="textAlign3" />			 	
		 		  <display:setProperty name="paging.banner.placement" value="false" />
		 		  <display:setProperty name="export.pdf" value="true" />
					<display:setProperty name="export.pdf.filename" value="chequeInHand.pdf" /> 
				    <display:setProperty name="export.excel" value="true" />
				    <display:setProperty name="export.excel.filename" value="chequeInHand.xls"/>
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
