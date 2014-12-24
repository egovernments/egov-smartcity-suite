<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page import="org.egov.commons.service.CommonsServiceImpl"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page buffer = "16kb" %>
<%@page  import="com.exilant.eGov.src.reports.*,java.io.*,java.util.*,javax.naming.InitialContext"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html>
<head>
<title>Receipt/Payment Report</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<META http-equiv=pragma content=no-cache>
<link rel="stylesheet" href="../css/egov.css" type="text/css">
<%@ page import="org.egov.commons.CFinancialYear"%>
<%@ page import="com.exilant.eGov.src.common.EGovernCommon"%>
<%@ page import="org.egov.infstr.utils.HibernateUtil"%>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
<script language="javascript" src="../script/jsCommonMethods.js"></script>
<SCRIPT type="text/javascript" src="../script/calendar.js" type="text/javascript" ></SCRIPT>
<SCRIPT LANGUAGE="javascript">
var endDate="";
var fuid="";
var finYear1="";
var month="",period="",fin_Year="",rupees="", toDate="";
function onLoad()
{
	var m='<%=request.getParameter("month")%>';
	var p='<%=request.getParameter("period")%>';
	var y='<%=request.getParameter("fin_Year")%>';
	var rs='<%=request.getParameter("rupees")%>';
	var toDate='<%=request.getParameter("toDate")%>';
	var rptTitletemp='<%=request.getParameter("rptTittle")%>';

	document.getElementById('period').value=p;
	document.getElementById('fin_Year').value=y;
	document.getElementById('rupees').value=rs;
	obj=document.getElementById('period');
	fillMonths(obj);
	document.getElementById('month').value=m;
	if(toDate!=null)
		document.getElementById('toDate').value=toDate;
	document.getElementById('rptTittle').value=rptTitletemp;
	if('<%=request.getParameter("fromBean")%>'==1){
		document.getElementById('viewAllSchedules').style.display='block';
		}
}

function fillMonths(obj)
{
	var monthObj=document.getElementById('month');
	clearCombo('month');
	var table = document.getElementById('RecPayTab');
	if(obj.value==3)
	{
		table.rows[1].childNodes[4].innerHTML='';
		table.rows[1].childNodes[4].innerHTML='<div align="right" valign="center" class="labelcell">Month<span class="leadon">*</span></div>';
		document.getElementById('month').style.display ="block";
		document.getElementById('toDate').style.display ="none";
		table.rows[1].childNodes[6].innerHTML='';
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
		
	else if(obj.value==1)
	{
		document.getElementById('month').style.display ="none";
		table.rows[1].childNodes[4].innerHTML='';
		document.getElementById('toDate').style.display ="block";
		document.getElementById('toDate').value="";
		table.rows[1].childNodes[6].innerHTML='<div align="right" valign="center" class="labelcell">To&nbsp;Date</div>';;
	}
	else
	{	document.getElementById('month').style.display ="none";
		table.rows[1].childNodes[4].innerHTML='';
		document.getElementById('toDate').style.display ="none";
		table.rows[1].childNodes[6].innerHTML='';
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
 
function validateBeforeSubmit(){
	
	if(document.getElementById('period').value==0)
	{ 	
		alert("Choose Period");
		return false;
	}
	if(document.getElementById('period').value==3 && document.getElementById('month').value==0)
	{
		alert("Choose Month");
		return false;
	}
	if(document.getElementById('rupees').value==0)
	{
		alert("Choose Rupees");
		return false;
	}
	document.getElementById('finYear').value=document.getElementById('fin_Year').value;
	var yrObj=document.getElementById('fin_Year');
	document.getElementById('finYear').value = yrObj.options[yrObj.selectedIndex].value;
	mnObj=document.getElementById('month');
	var p=document.getElementById('period').value;
	var toDate=document.getElementById('toDate').value;
	if(p==1) 
	{
		if(toDate=='')
			document.getElementById('rptTittle').value=" Year Ended "+ yrObj.options[yrObj.selectedIndex].text;
		else
			document.getElementById('rptTittle').value=" As On Date "+ toDate + " for the Year "+yrObj.options[yrObj.selectedIndex].text;
	}
	if(p==2) document.getElementById('rptTittle').value=" Half Year Ended "+ yrObj.options[yrObj.selectedIndex].text;
	if(p==3) document.getElementById('rptTittle').value=" Month Ended "+mnObj.options[mnObj.selectedIndex].text+"-"+ yrObj.options[yrObj.selectedIndex].text;
		
	return true;
} 

function ButtonPress()
{
		if(!validateBeforeSubmit()) return;
		document.getElementById('fromBean').value = 1;
		document.forms[0].submit();
	}

function buttonFlush()
{
	window.location="RPReport.jsp";
}

function buttonPrint()
{ 
	document.getElementById("tbl-header1").style.display = "none";
  	if(window.print)
  	{
  		window.print();
	}
	document.getElementById("tbl-header1").style.display = "block";
}

function setURL(scheduleNo){
	if(!validateBeforeSubmit()) return;	
	var period=document.getElementById('period').value;
	var rupees=document.getElementById('rupees').value;
	var finYear=document.getElementById('fin_Year').value;
	var month=document.getElementById('month').value;
	var rptTitle=document.getElementById('rptTittle').value;
	//window.open('../Reports/RPSchedules.jsp?period='+period+'&rupees='+rupees+'&finYear='+finYear+'&scheduleNo='+scheduleNo+'&month='+month+'&fromBean=0','','height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	if(scheduleNo=='viewAllSchedules'){
		window.open('../Reports/RPSchedules.jsp?rptTitle='+rptTitle+'&period='+period+'&rupees='+rupees+'&finYear='+finYear+'&scheduleNo=viewAllSchedules'+'&month='+month,'','height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	}else{
		window.open('../Reports/RPSchedules.jsp?rptTitle='+rptTitle+'&period='+period+'&rupees='+rupees+'&finYear='+finYear+'&scheduleNo='+scheduleNo+'&month='+month,'','height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	}
}
</SCRIPT>

</head>
<body onLoad="onLoad()">
<jsp:useBean id = "reportBean" scope ="request" class="com.exilant.eGov.src.reports.ReceiptPaymentBean"/>
<jsp:setProperty name = "reportBean" property="*"/>
<form name="ReceiptsPaymentsReport" action = "RPReport.jsp" method = "post">
<input type="hidden" name="fromBean" id="fromBean" value="0">
<input type=hidden name="finYear" id="finYear" value="">
<input type=hidden name="rptTittle" id="rptTittle" value="">
<table align='center' id="table2">
<tr><td>
<div id="main"><div id="m2"><div id="m3" style="width:810px">
<div class="tbl-header1" id="tbl-header1">
<table width="100%" name="RecPayTab" id="RecPayTab" border=0 cellpadding="3" cellspacing="0">
<tr>
		<td colspan="10" class="rowheader" valign="center" colspan="4" width="100%"><span id="screenName" class="headerwhite2">Receipts and Payments </span></td>
	</tr>
	<tr>
				<td class="labelcell"><div align="right" valign="center">
					Period<span class="leadon">*</span></div></td>
				<td class="smallfieldcell">
					<SELECT class="fieldinput" id="period" name="period" onchange="fillMonths(this)" tabindex=1 style="width:100px" exilMustEnter="true" >
					<option value=0 selected >---Choose---</option>
					<option value=1>Yearly</option>
					<option value=2 >Half Yearly</option>
					<option value=3 >Monthly</option>
					</SELECT>
				</td>
				<td class="labelcell"><div align="right" valign="center">Year<span class="leadon">*</span></div></td>
				<td class="smallfieldcell">
					<SELECT class="fieldinput" id="fin_Year" name="fin_Year" style="width:100px" tabindex=1  exilMustEnter="true" >
					<%

									String fYear=request.getParameter("fin_Year");
									System.out.println("hi3"+fYear);
									CommonsServiceImpl commonsServiceImpl= new CommonsServiceImpl();
									List finYearList =(List)commonsServiceImpl.getFinancialYearList();
									Iterator finYearIterator=finYearList.iterator();
									CFinancialYear f;

									EGovernCommon cm=new EGovernCommon();
									String today=cm.getCurrentDate();
									String[] dtArr = today.split("/");
									String dateRange="",str="";
									int month;
									month=Integer.parseInt(dtArr[1]);
									if(month>=4)
										{
											dateRange=dtArr[2]+"-"+(Integer.parseInt(dtArr[2])+1);
											str=dateRange.substring(0,5)+dateRange.substring(7,9);
										}
									else
										{ 
											dateRange=(Integer.parseInt(dtArr[2])-1)+"-"+dtArr[2];
											str=dateRange.substring(0,5)+dateRange.substring(7,9);
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
				
				<td class="labelcell">Month<span class="leadon">*</span></td>
				<td class="smallfieldcell">
					<SELECT class="fieldinput" id="month" name="month" style="width:100px" tabindex=1  exilMustEnter="true" ></SELECT>
				</td>
				
				<td align=right class="labelcell" style="display:none;">To&nbsp;Date</td>
				<td class="smallfieldcell"  style="display:none;" align=center><input name="toDate" id="toDate" class="datefieldinput" tabindex=1 onkeyup="DateFormat(this,this.value,event,false,'3')" ></td>
				
				<td class="labelcell">Amount&nbsp;In<span class="leadon">*</span></td>
				<td class="smallfieldcell">
					<SELECT class="fieldinput" id="rupees" name="rupees" style="width:90px" tabindex=1 exilMustEnter="true" >
						<option value="Rs" >Rupees</option>
						<option value="thousand" >Thousands</option>
						<option value="lakhs" >Lakhs</option>
					</SELECT>
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
	<tr class="row2">
		<td colspan="10" align="middle">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>	
		<td>		
			<input type=button class=button onclick="ButtonPress()" href="#" tabindex=1 value="Submit">
			<input type=button class=button onclick="buttonPrint()" href="#" tabindex=1 value="Print">
			<input type=button class=button onclick="buttonFlush()" href="#"  tabindex=1 value="Cancel">
			<input type=button class=button onclick="window.close();" href="#" tabindex=1 value="Close">
			<input type=button class=button id='viewAllSchedules' name=='viewAllSchedules'  onClick="setURL('viewAllSchedules')";tabindex=1 value="View All Schedules" style="display:none">
		</td>
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
    System.out.println("after submit "+request.getParameter("fromBean")+" year "+request.getParameter("finYear"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
    {
	 try
	 {		
		RPReport rpRpt= new RPReport();
		request.setAttribute("links",rpRpt.getRPReport(reportBean));
		String[] fundNameList=rpRpt.reqFundName;
		String[] fundIdList=rpRpt.reqFundId;
		String title=request.getParameter("rptTittle");
		String rupees=request.getParameter("rupees");
		String prvPeriod="For&nbsp;the Period&nbsp;ended: "+reportBean.getPrevPeriodEndDate();
	%>

<center><u><b><div class = "normaltext">Reciepts and Payments Account for the: <%= title %> <br><bean:message key="RecPay.Name"/></br></div></b></u></center>
<b><div class = "normaltext" align="right">Amount in:<%= rupees %></div></b></u>
<div class="tbl4-container" id="tbl-container" style="width:970px;">
		<display:table cellspacing="0" name="links" id="currentRowObject" class="its"  export="true" sort="list" >
		<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Reciepts and Payments Account for the: <%= title %>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Amount in:<%= rupees %>
		</display:caption>
		<display:caption media="excel">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Reciepts and Payments Account for the: <%= title %>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Amount in:<%= rupees %>
		</display:caption>
		<div STYLE="display:table-header-group">
		<display:column property="accountCode" title="Account Code"/>
		<display:column property="schedulename" title="Head&nbsp;Of&nbsp;Account"/>
		<display:column title="Schedule No">	
		<%
		Object scheduleNoRow=((HashMap)pageContext.getAttribute("currentRowObject")).get("scheduleNo");
		System.out.println("scheduleNoRow:"+scheduleNoRow);
		%>
		<span onMouseOver="this.style.cursor='hand'" onclick="setURL('<%=scheduleNoRow%>')"><%=scheduleNoRow%></span>
		</display:column>
		<%  
		for(int i=0;i<fundIdList.length;i++)
		{
			Object fund=((HashMap)pageContext.getAttribute("currentRowObject")).get(fundIdList[i]);	
		
			%>
			<display:column title="<%=fundNameList[i]%>" class="textAlign">	
			<%=fund %>
			</display:column>
			<%
		}
		%>	
		<display:column property="rowTotal" title="Total" class="textAlign"/>	
		<display:column property="prevYrAmt" title="<%= prvPeriod %>" class="textAlign3"/>	
		<display:setProperty name="export.pdf" value="true" />
		<display:setProperty name="export.pdf.filename" value="RPReport.pdf" /> 
		<display:setProperty name="export.csv" value="false" />
	    <display:setProperty name="export.xml" value="false" />
	    <display:setProperty name="export.excel" value="true" />
	    <display:setProperty name="export.excel.filename" value="RPReport.xls"/> 			
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
	 catch(Exception e)
	 {
	 	System.out.println("Exception in Jsp Page "+ e.getMessage());		 
	 }
}
%>
</form>
</body>
</html>