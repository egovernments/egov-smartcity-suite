<!--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  -->
<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page buffer = "64kb" %> 
<%@page  import="com.exilant.eGov.src.reports.DishonoredChequeReport,org.apache.log4j.Logger"%>
<html>
<head>
<title>Dishonored Cheque Report</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<!-- <META http-equiv=pragma content=no-cache> -->
<!-- Inclusion of the CSS files that contains the styles -->

<link rel=stylesheet href="../css/egov.css?rnd=${app_release_no}" type="text/css" media="screen" />
<link rel=stylesheet href="../css/print.css?rnd=${app_release_no}" type="text/css" media="print" />

<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js?rnd=${app_release_no}"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js?rnd=${app_release_no}"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js?rnd=${app_release_no}"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js?rnd=${app_release_no}"></SCRIPT>
<script language="javascript" src="../resources/javascript/jsCommonMethods.js?rnd=${app_release_no}"></script>
<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js?rnd=${app_release_no}" type="text/javascript" ></SCRIPT>


<SCRIPT LANGUAGE="javascript">


var strtDate="";
var endDate="";
var chqNo="";
var fundId="";
var mode="";

function onLoad()
{
	PageValidator.addCalendars();
	PageManager.ListService.callListService();
	
	var beanVal= <%=request.getParameter("fromBean")%>;
	document.getElementById('chequeNo').focus();
}
function ButtonPress()
{
	//bootbox.alert("search");
	
	if (!PageValidator.validateForm())
	return;
	
	strtDate = document.getElementById('startDate').value;
	endDate = document.getElementById('endDate').value;
	
	
	if(strtDate=="")
	{
	//bootbox.alert('Enter either Cheque From date or Cheque To Date');
	bootbox.alert('Enter Cheque From date');
	return false;
	}
	
	chqNo=document.getElementById("chequeNo").value;

	mode=document.getElementById("mode").value;
	//bootbox.alert(mode);
	if(mode==0 || mode==""){
		bootbox.alert('Please Select mode of Payment or Receipt');
		return false;
	}
	
	if(chqNo!="")
	chqNo=document.getElementById("chequeNo").value;
	
	fundId=document.getElementById("fundLst").value;
	//bootbox.alert(fundId);
		
	if(fundId!="")
	fundId=document.getElementById("fundLst").value;
	
		
	
	if(endDate!="")
	{
		if( compareDate(formatDate6(strtDate),formatDate6(endDate)) == -1 )
		{
			//bootbox.alert("Inside compare dt");
			bootbox.alert('From Date cannot be greater than To Date');
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

<form name="DishonoredChequeReport"  action = "DishonoredChequeReport.jsp?chqNo="+chqNo+"&fundId="+fundId+"&startDate="+strtDate+"&endDate="+endDate+"&mode="+mode  method = "post">
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
				Dishonored Cheque/DD Details</span>
			</td>
	</tr>
	<tr class ="row1">
		<td></td>
		<td></td> <td></td> <td></td>
	</tr>
	
	<tr>
		<td align="right"><div align="right" valign="center" class="labelcell" >Cheque/DD&nbsp;Number </div></td>
		<td class="smallfieldcell"><input CLASS="fieldinput" NAME="chequeNo" ID="chequeNo"> </td>
		
		
		<td align="right"><div align="right" valign="center" class="labelcell" >Fund&nbsp;</div></td>
		<td class="smallfieldcell"><SELECT CLASS="fieldinput" NAME="fundLst" ID="fundLst" exilListSource="fundNameList"> </SELECT></td>
	</tr>
	<tr>
	<td align="right"><div align="right" valign="center" class="labelcell" >Mode of Payment or Receipt<SPAN class="leadon">*</SPAN> </div></td>
	
	<td class="smallfieldcell" width="17%" >
					<SELECT class="fieldinput" id="mode" name="mode"  exilMustEnter="true" >
					<option value=0 selected >---Choose---</option>
					<option value=1>DD</option>
					<option value=2>Cheque</option>
					
					</SELECT>
				</td >
	</tr>
	<tr>
		<td align="right"><div align="right" valign="center" class="labelcell" >Cheque/DD Date From<SPAN class="leadon">*</SPAN></div></td>
		<td class="smallfieldcell" >
		<input name="startDate" id="startDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true">
		</td>
		<td align="right"><div align="right" valign="center" class="labelcell" >Cheque/DD Date To</div></td>
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
	Logger LOGGER = Logger.getLogger("DishonoredChequeReport.jsp");
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
    {
		try{
		 DishonoredChequeReport dcReport = new DishonoredChequeReport();
		 request.setAttribute("links",dcReport.getDishonoredChequeDetails(disChqReportBean));
		 String md=request.getParameter("mode");
		 		
			//System.out.println("mode==============================="+md);
		 }
		 catch(Exception e){
		 LOGGER.info("Exception in Jsp Page "+ e.getMessage());
		 %>
		 <script>
		 bootbox.alert("Error :<%=e.getMessage()%>");
		 </script>
		 <%
		  }                                                
		 %>	
		 <center><u><b><div class = "normaltext"> Dishonored Cheque/DD Report</div></b></u></center>   		
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
		<% if(request.getParameter("mode").equals("2") ) { %>                                                                    
		<display:column property="chequeNumber" style="width:80px" title="<center>Cheque&nbsp;Number</center>"  style="text-align:right"      />	
		<display:column property="chequeDate" style="width:80px" title="<center>Cheque&nbsp;&nbsp;&nbsp;Date</center>"  style="text-align:right"      />
		<% }else  { %>
			<display:column property="chequeNumber" style="width:80px" title="<center>DD&nbsp;Number</center>"  style="text-align:right"      />	
			<display:column property="chequeDate" style="width:80px" title="<center>DD&nbsp;&nbsp;&nbsp;Date</center>"  style="text-align:right"      />	
		<%} %>	
		<display:column property="payeeName"  style="width:100px" title="<center>Payee</center>"/>	
		<display:column property="bankName" style="width:80px" title="<center>Deposited&nbsp;in</center>"  style="text-align:right"      />
		<% if(request.getParameter("mode").equals("2") ) { %>   	
		<display:column property="recChequeDate" style="width:80px" title="<center>Cheque&nbsp;Dishonored&nbsp;on</center>"  style="text-align:right"    />
		<% }else  { %>
		<display:column property="recChequeDate" style="width:80px" title="<center>DD&nbsp;Dishonored&nbsp;on</center>"  style="text-align:right"    />
		<%} %>		
		<display:column property="bankRefNumber" style="width:80px" title="<center>Bank&nbsp;Ref&nbsp;Number</center>"  style="text-align:right"    />	
		<display:column property="bankChargeAmt" style="width:80px" title="<center>Bank&nbsp;Charge&nbsp;Amount&nbsp;(Rs.)</center>"  style="text-align:right"      />	
		<display:column property="amount" style="width:80px" title="<center>Cheque&nbsp;Amount&nbsp;(Rs.)</center>"  class="textAlign2" style="text-align:right"      />
		<% if(request.getParameter("mode").equals("2") ) { %>   	
		<display:column property="status" style="width:80px" title="<center>Cheque&nbsp;Status&nbsp;</center>"  style="text-align:right"    />
		<% }else  { %>
		<display:column property="status" style="width:80px" title="<center>DD&nbsp;Status&nbsp;</center>"  style="text-align:right"    />
		<%} %>		
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
