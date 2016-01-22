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
<%@page import="org.apache.log4j.Logger"%>
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
<script language="javascript" src="../resources/javascript/jsCommonMethods.js"></script>
<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js" type="text/javascript" ></SCRIPT>
<link rel="stylesheet" href="../css/lockRowHead.css" type="text/css" />
<!--
<link rel="stylesheet" href="../exility/screen.css" type="text/css" media="screen, print" />
-->

<SCRIPT LANGUAGE="javascript">
var endDate="";
var fuid="";
var finYear1="";
function onLoad()
{

	var m='<%=request.getParameter("month")%>';
	var p='<%=request.getParameter("period")%>';
	var y='<%=request.getParameter("fin_Year")%>';
	var rs='<%=request.getParameter("rupees")%>';

	document.getElementById('period').value=p;
	//else document.getElementById('period').value=0 ;
	document.getElementById('fin_Year').value=y;
	document.getElementById('rupees').value=rs;
	obj=document.getElementById('period');
	fillMonths(obj);
	document.getElementById('month').value=m;

}




function fillMonths(obj)
{
	var monthObj=document.getElementById('month');

	clearCombo('month');
	var table = document.getElementById('RecPayTab');
	//bootbox.alert(table.rows[2].childNodes[0].innerHTML);
	if(obj.value==3)
	{
		//bootbox.alert(table.rows[2].childNodes[1].innerHTML);

		table.rows[1].childNodes[4].innerHTML='';
		table.rows[1].childNodes[4].innerHTML='<div align="right" valign="center" class="normaltext">Month<span class="leadon">*</span></div>';
		//bootbox.alert(table.rows[2].childNodes[1].innerHTML);
		document.getElementById('month').style.display ="block";
		monthObj.options[0]=new Option('---Choose---');
		monthObj.options[1]=new Option('January','01');
		monthObj.options[2]=new Option('February','02');
		monthObj.options[3]=new Option('March','03');
		monthObj.options[4]=new Option('April','04');
		monthObj.options[5]=new Option('May','05');
		monthObj.options[6]=new Option('June','06');
		monthObj.options[7]=new Option('July','07');
		monthObj.options[8]=new Option('August','08');
		monthObj.options[9]=new Option('September','09');
		monthObj.options[10]=new Option('October','10');
		monthObj.options[11]=new Option('November','11');
		monthObj.options[12]=new Option('December','12');
	}
	/*else if(obj.value==2)

	{
		table.rows[2].childNodes[0].innerHTML='';
		table.rows[2].childNodes[0].innerHTML='<div align="right" valign="center" class="normaltext">Month<span class="leadon">*</span></div>';
		document.getElementById('month').style.display ="block";
		monthObj.options[1]=new Option('First Half(April-Sep30)','Apr-Sep');
		monthObj.options[2]=new Option('Second Half(Oct-March31)','Sep-March');
	}*/
	else
	{	document.getElementById('month').style.display ="none";
		table.rows[1].childNodes[4].innerHTML='';
	}

}

function clearCombo(cId)
{
   var bCtrl=document.getElementById(cId);
   for(var i=bCtrl.options.length-1;i>=0;i--)
   {
   	bCtrl.remove(i);
   }
 }

function ButtonPress()
{

		if(document.getElementById('period').value==0)
		{ 	bootbox.alert("Choose Period");
			return;
		}
		if(document.getElementById('period').value==3 && document.getElementById('month').value==0)
		{
			bootbox.alert("Choose Month");
			return;
		}
		document.getElementById('fromBean').value = 1;
		//finYear1=document.getElementById('fin_Year').value;
		document.getElementById('finYear').value=document.getElementById('fin_Year').value;
		yrObj=document.getElementById('fin_Year');
		document.getElementById('finYear').value = yrObj.options[yrObj.selectedIndex].value;
		mnObj=document.getElementById('month');
		var p=document.getElementById('period').value;
		if(p==1) document.getElementById('rptTittle').value=" YEAR ENDED "+ yrObj.options[yrObj.selectedIndex].text;
		if(p==2) document.getElementById('rptTittle').value=" HALF YEAR ENDED "+ yrObj.options[yrObj.selectedIndex].text;
		if(p==3) document.getElementById('rptTittle').value=" MONTH ENDED "+mnObj.options[mnObj.selectedIndex].text+"-"+ yrObj.options[yrObj.selectedIndex].text;

		document.forms[0].submit();

	}

function buttonFlush()
{

	window.location="ReceiptPaymentReport.jsp";

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
	Logger LOGGER = Logger.getLogger("ReceiptPaymentReport.jsp");
if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("2"))
    {out.println("In If block...."+request.getParameter("fromBean"));

	  }
	  else{out.println("In else block...."+request.getParameter("fromBean"));}
%>
-->
</head>
<body onLoad="onLoad()">
<jsp:useBean id = "reportBean" scope ="page" class="com.exilant.eGov.src.reports.ReceiptPaymentBean"/>
<jsp:setProperty name = "reportBean" property="*"/>

<form name="ReceiptsPaymentsReport" action = "ReceiptPaymentReport.jsp? method = "post">
<input type="hidden" name="fromBean" id="fromBean" value="0">
<input type=hidden name="finYear" id="finYear" value="">
<input type=hidden name="rptTittle" id="rptTittle" value="">

<div class="tbl-header1" id="tbl-header1">
<table width="100%" name="RecPayTab" id="RecPayTab" border=0 cellpadding="3" cellspacing="0">
<tr >
		<td colspan="8" class="rowheader" valign="center" colspan="4" width="100%"><span id="screenName" class="headerwhite2">Receipts and Payments </span></td>
	</tr>
	<tr  class="row1">
				<td width="12%"><div align="right" valign="center" class="normaltext">
					Period<span class="leadon">*</span></div></td>
				<td width="17%" >
					<SELECT class="fieldinput" id="period" name="period" onchange="fillMonths(this)" exilMustEnter="true" >
					<option value=0 selected >---Choose---</option>
					<option value=1>Yearly</option>
					<option value=2 >Half Yearly</option>
					<option value=3 >Monthly</option>
					</SELECT>
				</td >
				<td width="8%"><div align="right" valign="center" class="normaltext">
					Year<span class="leadon">*</span></div></td>
				<td width="11%">
					<SELECT class="fieldinput" id="fin_Year" name="fin_Year" exilMustEnter="true" >
					<%

									String fYear=request.getParameter("fin_Year");
									LOGGER.info("hi3"+fYear);
									ServiceLocator 	serviceloc  = ServiceLocator.getInstance();
									CommonsManagerHome	bhome	=	(CommonsManagerHome)serviceloc.getLocalHome("CommonsManagerHome");
									CommonsManager bservice	=	bhome.create();
									LOGGER.info("hi31");

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
										LOGGER.info("str1:"+str);
										}
									else
										{ dateRange=(Integer.parseInt(dtArr[2])-1)+"-"+dtArr[2];
											LOGGER.info("dateRange1:"+dateRange);

											str=dateRange.substring(0,5)+dateRange.substring(7,9);
											LOGGER.info("str:"+str);
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

				<td width="11%" class="normaltext">
					Month<span class="leadon">*</span></td>

				<td width="10%">
					<SELECT class="fieldinput" id="month" name="month" exilMustEnter="true" >

				</SELECT></td>

				<td width="10%" class="normaltext">
					Rupees<span class="leadon">*</span></td>

				<td width="20%">
					<SELECT class="fieldinput" id="rupees" name="rupees" exilMustEnter="true" >
						<option value="Rs" >Rupees</option>
						<option value="thousand" >Thousands</option>
						<option value="lakhs" >Lakhs</option>
					</SELECT></td>

		</tr>

	<tr class="row2">
		<td colspan="8" align="middle">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td align="right"><IMG height=18 src="/egi/resources/erp2/images/Button_leftside.gif" width=7></td>
			<td bgcolor="#fe0000" valign="center" nowrap><A class=buttonprimary onclick="ButtonPress()" href="#">Submit</A></td>
			<td><IMG height=18 src="/egi/resources/erp2/images/Button_rightside.gif" width=7></td>
			<td><IMG src="/egi/resources/erp2/images/spacer.gif" width=8></td>

			<td align="right"><IMG height=18 src="/egi/resources/erp2/images/Button_second_leftside.gif" width=6></td>
			<td bgcolor="#ffffff" valign="center" nowrap background="/egi/resources/erp2/images/Button_second_middle.gif"><A class=buttonsecondary onclick=buttonFlush(); href="#">Cancel</A></td>
			<td><IMG height=18 src="/egi/resources/erp2/images/Button_second_rightside.gif" width=6></td>
			<td><IMG src="/egi/resources/erp2/images/spacer.gif" width=8></td>

			<td align="right"><IMG height=18 src="/egi/resources/erp2/images/Button_second_leftside.gif" width=6></td>
			<td bgcolor="#ffffff" valign="center" nowrap background="/egi/resources/erp2/images/Button_second_middle.gif"><A class=buttonsecondary onclick=window.close() href="#">Close</A></td>
			<td><IMG height=18 src="/egi/resources/erp2/images/Button_second_rightside.gif" width=6></td>
			<td><IMG src="/egi/resources/erp2/images/spacer.gif" width=8></td>

			<!-- Print Button start -->
			<td align="right"><IMG height=18 src="/egi/resources/erp2/images/Button_second_leftside.gif" width=8></td>
			<td bgcolor="#ffffff" valign="center" nowrap background="/egi/resources/erp2/images/Button_second_middle.gif"><A class=buttonsecondary onclick="pageSetup();buttonPrint()" href="#">Print</A></td>
			<td><IMG height=18 src="/egi/resources/erp2/images/Button_second_rightside.gif" width=8></td>
			<td><IMG src="/egi/resources/erp2/images/spacer.gif" width=8></td>
			<!-- Print end-->
		</tr>
		 </table>
		 </td>
	</tr>
</table>
</div>
<%
    LOGGER.info("after submit "+request.getParameter("fromBean")+" year "+request.getParameter("finYear"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
    {
	 try
	 {
		String url="",link="",schId="",startDate="",endDate="",prevStartDate="",prevEndDate="",amountIn="",schName="",finId="",period="",snapTime="";
		amountIn=request.getParameter("rupees");
		LOGGER.info("after submit "+request.getParameter("fromBean")+" date "+request.getParameter("finYear"));
		LOGGER.info("before call");
		ReceiptPaymentReport receiptPayRpt= new ReceiptPaymentReport();
			request.setAttribute("links",receiptPayRpt.getReceiptPaymentReport(reportBean));
			LOGGER.info("after call");
			String fundList[]=receiptPayRpt.reqFundName;
			finId=request.getParameter("fin_Year");
			period=request.getParameter("period");
			snapTime=reportBean.getEffFilter();;

	%>
	<div class="tbl-container" id="tbl-container">
	<display:table name="links" uid="currentRowObject"   export="false" sort="list" class="its" id="BSR" >
			 <display:caption><b><div ><font size=1>RECEIPTS AND PAYMENTS ACCOUNT FOR THE <%=request.getParameter("rptTittle")%> </font>  </div></b></display:caption>
			 <display:column property="accCodeRec"  title="Code No " style="FONT-FAMILY: Verdana; FONT-SIZE: 20px" class="width1" />
			 <display:column property="headAccRec"  title="Head of Account" style="FONT-FAMILY: Verdana" class="width" />
			 <display:column title="Schedule No" style="FONT-FAMILY: Verdana" class="width"  >
			  <%

			  	schId =((HashMap)pageContext.findAttribute("BSR")).get("recSchId").toString();
			  	schName=((HashMap)pageContext.findAttribute("BSR")).get("schNoRec").toString();
			  	startDate =((HashMap)pageContext.findAttribute("BSR")).get("startDate").toString();
			  	endDate =((HashMap)pageContext.findAttribute("BSR")).get("endDate").toString();
			  	prevStartDate =((HashMap)pageContext.findAttribute("BSR")).get("prevStartDate").toString();
			  	prevEndDate =((HashMap)pageContext.findAttribute("BSR")).get("prevEndDate").toString();
			  	//System.out.println("schId"+schId+startDate+endDate+":"+prevStartDate+":"+prevEndDate);
			  	//String code2="";

			 	 	      		//String effFilter=reportBean.getPrevPeriodEndDate();
			 	 	      		String link1="";
			 	 	      		//String url="";
			 	 	      		url=((HashMap)pageContext.findAttribute("BSR")).get("schNoRec").toString();
			 	 	      		//String link="";
			 	 	      		link="javascript:"+"void(window.open('rptScheduleMaster.jsp?schId="+schId+"&startDate="+startDate+"&endDate="+endDate+"&prevStartDate="+prevStartDate+"&prevEndDate="+prevEndDate+"&amountIn="+amountIn+"&schName="+url+"&repSubType=1&finId="+finId+"&period="+period+"&snapTime="+snapTime+"'"+",'','height=650,width=900,scrollbars=yes,left=30,top=30,status=no'"+"))";
	 	      //System.out.println("after link");
	 	      %>
			<a href="<%=link%>"><FONT face="Verdana"><%=url%></font></a>
			</display:column>
			<% url="";schId ="";schName="";
			for(int i=0;i<fundList.length;i++)
			{	Object amt=((HashMap)pageContext.getAttribute("BSR")).get("currRecFund"+i);
				if(amt==null) amt="";

					%>

							<display:column class="textAlign" title="<%= fundList[i] %>">
							<%= amt  %>
							</display:column>



					<%
						  }
			 %>

			<display:column property="currPeriodRec" title="Total"   class="textAlign" />
			<display:column property="prevPeriodRec" title='<%=reportBean.getPrevPeriodEndDate()%>'  class="textAlign" />
			<display:column property="accCodePay"  title="Code No " style="FONT-FAMILY: Verdana" class="width1"/>
			<display:column property="headAccPay"  title="Head of Account" style="FONT-FAMILY: Verdana" class="width" />
			<display:column  title="Schedule No" class="width" >
			<%


				schId =((HashMap)pageContext.findAttribute("BSR")).get("paySchId").toString();

				url=((HashMap)pageContext.findAttribute("BSR")).get("schNoPay").toString();
				link="javascript:"+"void(window.open('rptScheduleMaster.jsp?schId="+schId+"&startDate="+startDate+"&endDate="+endDate+"&prevStartDate="+prevStartDate+"&prevEndDate="+prevEndDate+"&amountIn="+amountIn+"&schName="+url+"&repSubType=2&finId="+finId+"&period="+period+"&snapTime="+snapTime+"'"+",'','height=650,width=900,scrollbars=yes,left=30,top=30,status=no'"+"))";
			%>
			<a href="<%=link%>"><FONT face="Verdana"><%=url%></font></a>
			</display:column>
			<% url="";
			for(int i=0;i<fundList.length;i++)
			{
			Object amt=((HashMap)pageContext.getAttribute("BSR")).get("currPayFund"+i);
			if(amt==null) amt="";
			%>

					<display:column class="textAlign" title="<%= fundList[i] %>">
					<%=amt%>
					</display:column>



			<%
						 }
			 %>

			<display:column property="currPeriodPay" title="Total" class="textAlign" />
			<display:column property="prevPeriodPay" title='<%=reportBean.getPrevPeriodEndDate()%>'  class="textAlign" />

			<display:setProperty name="export.pdf" value="false" />


		</display:table>

		</div>

<%
	 	}
		 catch(Exception e){
		 LOGGER.error("Exception in Jsp Page "+ e);
		}
	}
%>

</form>
</body>
</html>
