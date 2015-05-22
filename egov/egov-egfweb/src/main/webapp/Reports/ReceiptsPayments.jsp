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
<%@page  import="com.exilant.eGov.src.reports.LongAmountWrapper,com.exilant.eGov.src.reports.*,java.io.*,java.util.*,java.sql.*,javax.sql.*,javax.naming.InitialContext,com.exilant.GLEngine.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<META http-equiv=pragma content=no-cache>

<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen, print" />

<!--
<link rel=stylesheet href="../exility/global.css" type="text/css">
-->
<%@ page import="org.egov.commons.service.CommonsManager,org.egov.commons.service.CommonsManagerHome" %>
<%@ page import="org.egov.commons.CFinancialYear"%>
<%@ page import="org.egov.infstr.utils.ServiceLocator"%>
<%@ page import="com.exilant.eGov.src.common.EGovernCommon"%>
<%@ page import="org.egov.infstr.utils.HibernateUtil"%>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
<script language="javascript" src="../script/jsCommonMethods.js"></script>
<SCRIPT type="text/javascript" src="../script/calendar.js" type="text/javascript" ></SCRIPT>
<link rel="stylesheet" href="../css/lockRowHead.css" type="text/css" />
<!--
<link rel="stylesheet" href="../exility/screen.css" type="text/css" media="screen, print" />
-->

<SCRIPT LANGUAGE="javascript">
var endDate="";
var fuid="";
function onLoad()
{


	}

function ButtonPress()
{
		document.getElementById('fromBean').value = 1;
		obj=document.getElementById('fin_Year');
		document.getElementById('finYear').value = obj.options[obj.selectedIndex].text;
		document.forms[0].submit();

	}

function buttonFlush()
{

	window.location="ReceiptsPayments.jsp";

}
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

//For print method
function buttonPrint()
{
	document.getElementById('fromBean').value ="2";
    var hide1,hide2;
    hide1 = document.getElementById("tbl-header1");
	hide1.style.display = "none";
	hide2 = document.getElementById("currentRowObject");
	document.forms[0].submit();
  	if(window.print)
  		{
  		window.print();
	  	}
}
</SCRIPT>
<!--
<%
if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("2"))
    {out.println("In If block...."+request.getParameter("fromBean"));

	  }
	  else{out.println("In else block...."+request.getParameter("fromBean"));}
%>
-->
</head>
<body onLoad="onLoad()">
<jsp:useBean id = "reportBean" scope ="session" class="com.exilant.eGov.src.reports.ReceiptPaymentBean"/>
<jsp:setProperty name = "reportBean" property="*"/>

<form name="ReceiptsPaymentsReport" action = "ReceiptsPayments.jsp? method = "post">
<input type="hidden" name="fromBean" id="fromBean" value="0">
<input type=hidden name="finYear" id="finYear" value="">
<div class="tbl-header1" id="tbl-header1">
<table width="100%" border=0 cellpadding="3" cellspacing="0">
<tr >
		<td colspan="6" class="rowheader" valign="center" colspan="4" width="100%"><span id="screenName" class="headerwhite2">Receipts and Payments </span></td>
	</tr>
	<tr  class="row1">
				<td width=25% colspan="2"><div align="right" valign="center" class="normaltext">
					Period<span class="leadon">*</span></div></td>
				<td width=25% colspan="2" >
					<SELECT class="fieldinput" id="period" name="period" exilMustEnter="true" >
					<option value=1 selected >Yearly</option>
					<option value=2 >Half Yearly</option>
					<option value=3 >Monthly</option>
					</SELECT>
				</td >
				<td width=25%>&nbsp;</td>
				<td width=25%>
					&nbsp;</td>

		</tr>
	<tr class="row1">
				<td width="13%"><div align="right" valign="center" class="normaltext">
					Month<span class="leadon">*</span></div></td>
				<td width="12%">
					<SELECT class="fieldinput" id="month" name="month" exilMustEnter="true" >
					<option value="Jan" selected >January</option>
					<option value="Feb" >February</option>
					<option value="Mar" >March</option>
					<option value="Apr" >April</option>
					<option value="May" >May</option>
					<option value="Jun" >June</option>
					<option value="Jul" >July</option>
					<option value="Aug" >August</option>
					<option value="Sep" >September</option>
					<option value="Oct" >Octobar</option>
					<option value="Nov" >November</option>
					<option value="Dec" >December</option>
				</SELECT></td>
				<td width="13%"><div align="right" valign="center" class="normaltext">
					Year<span class="leadon">*</span></div></td>
				<td width="12%">
					<SELECT class="fieldinput" id="fin_Year" name="fin_Year" exilMustEnter="true" >
					<%

									String fYear=request.getParameter("finYear");
									System.out.println("hi3"+fYear);
									ServiceLocator 	serviceloc  = ServiceLocator.getInstance();
									CommonsManagerHome	bhome	=	(CommonsManagerHome)serviceloc.getLocalHome("CommonsManagerHome");
									CommonsManager bservice	=	bhome.create();
									System.out.println("hi31");

									List finYearList =(List)bservice.getFinancialYearList();
									Iterator finYearIterator=finYearList.iterator();
									CFinancialYear f;

									EGovernCommon cm=new EGovernCommon();
									String today=cm.getCurrentDate(HibernateUtil.getCurrentSession().connection());
									String[] dtArr = today.split("/");
									String dateRange="",str="";
									int month;
									month=Integer.parseInt(dtArr[1]);
									if(month>=4)
										{dateRange=dtArr[2]+"-"+(Integer.parseInt(dtArr[2])+1);
										str=dateRange.substring(0,5)+dateRange.substring(7,9);
										System.out.println("str1:"+str);
										}
									else
										{ dateRange=(Integer.parseInt(dtArr[2])-1)+"-"+dtArr[2];
											System.out.println("dateRange:"+dateRange);

											str=dateRange.substring(0,4)+dateRange.substring(7,8);
											System.out.println("str:"+str);
										}



								while(finYearIterator.hasNext())
								{


										f=(CFinancialYear)finYearIterator.next();
										String yrRange=f.getFinYearRange();
										if(fYear!=null && fYear.equalsIgnoreCase(yrRange))
										{%>
											<option value='<%= f.getId() %>' selected><%= f.getFinYearRange() %></option>
										<%}
										else
										{
											if(yrRange.equalsIgnoreCase(dateRange)||yrRange.equalsIgnoreCase(str))
											{%>
												<option value='<%= f.getId() %>' selected ><%= f.getFinYearRange() %></option>

											<%}
											else
											{%>
												<option value='<%= f.getId() %>' ><%= f.getFinYearRange() %></option>
									   		<%}
									   	}
								}

							%>



					</SELECT></td>
				<td width="13%"><div align="right" valign="center" class="normaltext">
					Rupees<span class="leadon">*</span></div></td>
				<td width="12%">
					<SELECT class="fieldinput" id="rupees" name="rupees" exilMustEnter="true" >
						<option value="Rs" >Rupees</option>
						<option value="Th" >Thousands</option>
						<option value="Lk" >Lakhs</option>
					</SELECT></td>

		</tr>
	<tr class="row2" height="5"><td colspan="2">&nbsp;</td><td colspan="2">&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr class="row2" height="5"><td colspan="2">&nbsp;</td><td colspan="2">&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr class="row2">
		<td colspan="6" align="middle">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td align="right"><IMG height=18 src="../images/Button_leftside.gif" width=7></td>
			<td bgcolor="#fe0000" valign="center" nowrap><A class=buttonprimary onclick="ButtonPress()" href="#">Search</A></td>
			<td><IMG height=18 src="../images/Button_rightside.gif" width=7></td>
			<td><IMG src="../images/spacer.gif" width=8></td>

			<td align="right"><IMG height=18 src="../images/Button_second_leftside.gif" width=6></td>
			<td bgcolor="#ffffff" valign="center" nowrap background="../images/Button_second_middle.gif"><A class=buttonsecondary onclick=buttonFlush(); href="#">Cancel</A></td>
			<td><IMG height=18 src="../images/Button_second_rightside.gif" width=6></td>
			<td><IMG src="../images/spacer.gif" width=8></td>

			<td align="right"><IMG height=18 src="../images/Button_second_leftside.gif" width=6></td>
			<td bgcolor="#ffffff" valign="center" nowrap background="../images/Button_second_middle.gif"><A class=buttonsecondary onclick=window.close() href="#">Close</A></td>
			<td><IMG height=18 src="../images/Button_second_rightside.gif" width=6></td>
			<td><IMG src="../images/spacer.gif" width=8></td>

			<!-- Print Button start -->
			<td align="right"><IMG height=18 src="../images/Button_second_leftside.gif" width=8></td>
			<td bgcolor="#ffffff" valign="center" nowrap background="../images/Button_second_middle.gif"><A class=buttonsecondary onclick="pageSetup();buttonPrint()" href="#">Print</A></td>
			<td><IMG height=18 src="../images/Button_second_rightside.gif" width=8></td>
			<td><IMG src="../images/spacer.gif" width=8></td>
			<!-- Print end-->
		</tr>
		 </table>
		 </td>
	</tr>
</table>
</div>
<br><br>

<%
    System.out.println("after submit "+request.getParameter("fromBean")+" year "+request.getParameter("finYear"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
    {
	 try
	 {
		System.out.println("after submit "+request.getParameter("fromBean")+" date "+request.getParameter("finYear"));
		System.out.println("before call");
		ReceiptPaymentReport receiptPayRpt= new ReceiptPaymentReport();
			request.setAttribute("links",receiptPayRpt.getReceiptPaymentReport(reportBean));
	%>
	<div class="tbl-container" id="tbl-container">
	<center><u><b><div class = "normaltext">Receipts and Payments Report </div></b></u></center>
	<display:table name="links" uid="currentRowObject"  export="true" sort="list" pagesize = "10" class="its" >

			 <display:caption><center><b><div class = "normaltext"><font size=2>Receipts and Payments </font>  </div></b></center></display:caption>
			 <display:column property="accCodeRec"  title="Code No " class="width1" />
			 <display:column property="headAccRec"  title="Head of Account" class="width" />
			 <display:column property="schNoRec"  title="Schedule No" class="width" />
			<display:column property="generalRec" title="General Fund (Rs)"  class="textAlign" />
			<display:column property="waterSupplyRec" title="Water Supply & Sewerage Fund  (Rs)"  class="textAlign" />
			<display:column property="enterpriseRec" title="Enterprise Fund (Rs)"  class="textAlign" />
			<display:column property="currPeriodRec" title="Total (Rs)"  class="textAlign" />
			<display:column property="prevPeriodRec" title="For the Period ended____"  class="textAlign" />
			<display:column property="accCodePay"  title="Code No " class="width1"/>
			<display:column property="headAccPay"  title="Head of Account" class="width" />
			<display:column property="schNoPay"  title="Schedule No" class="width" />
			<display:column property="generalPay" title="General Fund (Rs)"  class="textAlign" />
			<display:column property="waterSupplyPay" title="Water Supply & Sewerage Fund  (Rs)"  class="textAlign" />
			<display:column property="enterprisePay" title="Enterprise Fund (Rs)"  class="textAlign" />
			<display:column property="currPeriodRec" title="Total (Rs)"  class="textAlign" />
			<display:column property="prevPeriodPay" title="For the Period ended____ "  class="textAlign" />
			<display:setProperty name="export.pdf" value="true" />
		<display:footer>
		   <tr>
		     <td><div class = "normaltext">#</div></td>
		     <td><div class = "normaltext">Balances banks operate for</div></td>
		     <td><div class = "normaltext">Grants </div></td>
		     <td><div class = "normaltext">and</div></td>
		     <td><div class = "normaltext">special </div></td>
		     <td><div class = "normaltext">funds</div></td>
		    </tr>
		    <tr>
		     <td><div class = "normaltext">*</div></td>
		     <td><div class = "normaltext">Details in respect of these items </div></td>
		     <td><div class = "normaltext">will be </div></td>
		     <td><div class = "normaltext">available </div></td>
		     <td><div class = "normaltext">in the</div></td>
		     <td><div class = "normaltext">corres. asset ledger accounts</div></td>
		    </tr>
		    <tr>
		     <td><div class = "normaltext">**</div></td>
		     <td><div class = "normaltext">Details in respect of these items </div></td>
		     <td><div class = "normaltext">will be </div></td>
		     <td><div class = "normaltext">available </div></td>
		     <td><div class = "normaltext">in the</div></td>
		     <td><div class = "normaltext">corres. liability ledger accounts</div></td>
		    </tr>
  		 </display:footer>
		</display:table>
		</div>
<%
	 	}
		 catch(Exception e){
		 System.out.println("Exception in Jsp Page "+ e.getMessage());
		}
	}
%>

</form>
</body>
</html>
