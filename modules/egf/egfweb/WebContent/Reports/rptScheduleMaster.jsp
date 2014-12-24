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
<%@ page import="com.exilant.eGov.src.common.EGovernCommon"%>
<%@ page import="org.egov.infstr.utils.HibernateUtil"%>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
<script language="javascript" src="../script/jsCommonMethods.js"></script>
<SCRIPT type="text/javascript" src="../script/calendar.js" type="text/javascript" ></SCRIPT>
<link rel="stylesheet" href="../css/lockRowHead.css" type="text/css" />

<SCRIPT LANGUAGE="javascript">
var endDate="";
var fuid="";
var finYear1="";
function onLoad()
{



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

</head>
<body onLoad="onLoad()">
<jsp:useBean id = "reportBean" scope ="session" class="com.exilant.eGov.src.reports.ReceiptPaymentBean"/>
<jsp:setProperty name = "reportBean" property="*"/>

<form name="ReceiptsPaymentsReport" action = "ReceiptPaymentReport.jsp? method = "post">
<input type="hidden" name="fromBean" id="fromBean" value="0">
<input type=hidden name="finYear" id="finYear" value="">
<input type=hidden name="rptTittle" id="rptTittle" value="">


<br><br>

<% // System.out.println("after submit "+request.getParameter("fromBean")+" year "+request.getParameter("finYear"));
HibernateUtil.getCurrentSession().doWork(new Work() {
			
	@Override
	public void execute(Connection con) throws SQLException {

	 try
	 {
		//System.out.println("after submit "+request.getParameter("fromBean")+" date "+request.getParameter("finYear"));
		System.out.println("before call");
		CommnFunctions cf=new CommnFunctions();
		HashMap schWiseHshMap=new HashMap();
		String finId=request.getParameter("finId");
		ReceiptPaymentReport receiptPayRpt= new ReceiptPaymentReport();
		String schName=request.getParameter("schName");
		String schId=request.getParameter("schId");
		String amountIn=request.getParameter("amountIn");
		String period=request.getParameter("period");
		String snapTime=request.getParameter("snapTime");
		System.out.println("schId"+request.getParameter("schId"));
		System.out.println("period"+request.getParameter("period"));
		int repSubType=Integer.parseInt(request.getParameter("repSubType"));
			//cf.getschedulewiseOB(HibernateUtil.getCurrentSession().connection(),"","","","3",request.getParameter("startDate"),request.getParameter("endDate"),request.getParameter("prevStartDate"),request.getParameter("prevEndDate"),1,"RP",schId,schWiseHshMap);
			System.out.println("before call");
			receiptPayRpt.getScheduleReport(con,"3",request.getParameter("startDate"),request.getParameter("endDate"),request.getParameter("prevStartDate"),request.getParameter("prevEndDate"),repSubType,"RP",schId,schWiseHshMap,amountIn,schName,finId,period,snapTime);
			System.out.println("after call");
			request.setAttribute("links",schWiseHshMap.get(schId));
			System.out.println("hashmap element"+schWiseHshMap.get(schId));
			cf.getFundList("",request.getParameter("startDate"),request.getParameter("endDate"));
			//String fundList[]=cf.reqFundName;
			//String fundId[]=cf.reqFundId;
			String fundList[]=receiptPayRpt.reqFundName;
			String fundId[]=receiptPayRpt.reqFundId;

			Set set=null;
			set=(Set)((HashMap)schWiseHshMap.get(schId)).keySet();
			Iterator iter=set.iterator();
			//while(iter.hasNext())

	%>
	<div class="tbl-container" id="tbl-container">
	<display:table name="links" uid="currentRowObject" export="true" sort="list" defaultsort="1" pagesize = "11" class="its" id="BSR" >
			 <display:caption><b><div ><font size=2>RECEIPTS AND PAYMENTS ACCOUNT FOR THE <%=request.getParameter("endDate")%> </font>  </div></b><BR><div class = "textAlign"><font size=2>Amount in <%=request.getParameter("amountIn")%></font></div></display:caption>
		<%
		String glcode="";
		 if(iter.hasNext()) glcode=iter.next().toString();
		 System.out.println("checking:");
		%>
			 <display:column class="textAlign" title="Account Code" >
			 					<%= glcode %>
			 </display:column>
			 <display:column property="coaName"  title="Head of Account" class="width" />

			<%


					//System.out.println(glcode);
					double val=0.0;
					String periodTittle="For the period Ended "+request.getParameter("prevEndDate");
				for(int i=0;i<fundList.length;i++)
						{val+=Double.parseDouble(((HashMap)pageContext.getAttribute("BSR")).get(fundId[i]).toString());

					%>
							<display:column class="textAlign" title="<%= fundList[i] %>">
							<%= ((HashMap)pageContext.getAttribute("BSR")).get(fundId[i])  %>
							</display:column>
					<%
						  }

					String prevTotal=((HashMap)pageContext.getAttribute("BSR")).get("0").toString();


					%>
					<display:column class="textAlign" title="Total">
										<%= val  %>
					</display:column>
					<display:column class="textAlign" title="<%= periodTittle  %>">
					<%= ((HashMap)pageContext.getAttribute("BSR")).get("0") %>
					</display:column>


			<display:setProperty name="export.pdf" value="true" />
		</display:table>
		</div>
<%}
	 	//}
		 catch(Exception e){
		 System.out.println("Exception in Jsp Page "+ e.getMessage());
		}
	}
		});
%>
<div class="tbl-header1" id="tbl-header1">
<table width="100%" name="RecPayTab" id="RecPayTab" border=0 cellpadding="3" cellspacing="0">



	<tr class="row2">
		<td colspan="4" align="middle">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>

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
</form>
</body>
</html>
