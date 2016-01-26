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
<%@page  import="com.exilant.eGov.src.reports.*,java.io.*,java.util.*,javax.naming.InitialContext,org.apache.log4j.Logger"%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<META http-equiv=pragma content=no-cache>

<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen, print" />
<%@ page import="org.egov.infstr.utils.ServiceLocator"%>
<%@ page import="org.egov.infstr.utils.HibernateUtil"%>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
<script language="javascript" src="../resources/javascript/jsCommonMethods.js"></script>
<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js" type="text/javascript" ></SCRIPT>
<link rel="stylesheet" href="../css/lockRowHead.css" type="text/css" />

<SCRIPT LANGUAGE="javascript">

function onLoad(){
	PageManager.DataService.callDataService('companyDetailData');
	document.getElementById('period').value="<%=request.getParameter("period")%>";
	document.getElementById('rupees').value="<%=request.getParameter("rupees")%>";
	document.getElementById('finYear').value="<%=request.getParameter("finYear")%>";
	//document.getElementById('endDate').value="<%=request.getParameter("endDate")%>";
	document.getElementById('scheduleNo').value="<%=request.getParameter("scheduleNo")%>";
	document.getElementById('month').value="<%=request.getParameter("month")%>";
	document.getElementById('fundId').value="<%=request.getParameter("fundId")%>";
	var fromBean="<%=request.getParameter("fromBean")%>";
	//bootbox.alert(fromBean);
	if(fromBean!=null && fromBean==0){
		document.getElementById('fromBean').value=1;
		document.forms[0].action=action = "../Reports/RPSchedules.jsp?fromBean=1";
		bootbox.alert('inside submit');
		document.forms[0].submit();
	}
}

function buttonPrint()
{
   	document.getElementById("msgRow").style.display="none";
   	document.getElementById("backButton").style.display = "none";
	document.getElementById("printButton").style.display = "none";
		if(window.print)
  		{
  		window.print();
	  	}
	document.getElementById("backButton").style.display = "block";
	  	
}
function goBack()
{

if(window.opener)
	window.opener.focus();
window.close();
}
function afterRefreshPage(dc){
	document.getElementById('ulbName').innerHTML=dc.values['companyDetail_name'];
}


</SCRIPT>

</head>
<jsp:useBean id = "rpBean" scope ="request" class="com.exilant.eGov.src.reports.ReceiptPaymentBean"/>
<jsp:setProperty name = "rpBean" property="*"/>
<body onLoad="onLoad()">
<form name="RPSchedules" method = "post">
 	<br>
 	<input type=hidden id=period name=period value=""/>
 	<input type=hidden id=fundId name=fundId value=""/>
	<input type=hidden id=scheduleNo name=scheduleNo value=""/>
	<input type=hidden id=rupees name=rupees value=""/>
	<input type=hidden id=finYear name=finYear value=""/>
	<input type=hidden id=month name=month value=""/>
	<input type=hidden name="fromBean" id="fromBean" value="1">
	<input type=hidden name="rptTitle" id=rptTitle value="">
	<input type=hidden name=companyDetail_name id=companyDetail_name value="">
	
 <%!
 List list;
 String[] fundNameList;
 String style="",prevEndDate,endDate="";
 %>
 <%
 		Logger LOGGER = Logger.getLogger("RPSchedules.jsp");
 		 try{
			 RPReport rpRpt = new RPReport();	
			 list=(ArrayList)rpRpt.getRPReport(rpBean);
			  fundNameList=rpRpt.reqFundName;
			 System.out.println("--------- -fundNameList=rpRpt.reqFundName"+fundNameList.length);
			 //System.out.println("rpRpt.prevEndDate:"+rpRpt.prevEndDate);
			 String[] fundIdList=rpRpt.reqFundId;
			 prevEndDate="For the Period Ended: ";
			 prevEndDate=prevEndDate+rpRpt.prevEndDate;
			 endDate=rpRpt.endDate;
			 request.setAttribute("links",list);
			 LOGGER.info("size:"+list.size());
 		 }catch(Exception e){
 		 	LOGGER.error("Exception in Jsp Page "+ e.getMessage());
	 %> 
	 		 <script>
	 		bootbox.alert("Error :<%=e.getMessage()%>");
	 		 </script>
	 <%
 		 }
 	 %>
			 <div class="tbl3-container"  id="tbl-container1" > 
			 <center><u><b><div class = "normaltext"><span id=ulbName></span></div></b></u></center>
			 <center><u><b><div class = "normaltext">Reciepts and Payments Account for the Period Ended: <%=request.getParameter("rptTitle")%> <br><bean:message key="RecPay.Name"/></br></div></b></u></center>
			 <display:table cellspacing="0"  name="links" id="currentRowObject"  export="true" class="its" > 
			 <display:caption media="html"><div class="normaltext"><u></u></div><div align="right"><span class="labelcell"><B>Amount in <%=rpBean.getRupees()%></B></span></div></display:caption>
			 <display:caption media="pdf">
			 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Amount in <%=rpBean.getRupees()%></display:caption>
	  			<display:caption media="excel">
	 			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Amount in <%=rpBean.getRupees()%></display:caption>
		<%
				String rowHeader=pageContext.getAttribute("currentRowObject")!=null?(String)((HashMap)pageContext.getAttribute("currentRowObject")).get("rowHeader"):null;
				String rowBold=pageContext.getAttribute("currentRowObject")!=null?(String)((HashMap)pageContext.getAttribute("currentRowObject")).get("rowBold"):null;
				if(rowHeader!=null){
					style="background-color:#69c;border-bottom:1x solid #000000";
				}else if(rowBold!=null){
					style="FONT-WEIGHT: bold;";
				}else{
					style="";
				}
		%>
			<display:column title="Account Code" property="accountcode" class="normaltext" style="<%=style%>">
			</display:column>
			<display:column title="Particulars" property="particulars" class="normaltext" style="<%=style%>"/>
		<%	for(int i=0;i<fundNameList.length;i++){
		%>		<display:column title="<%=fundNameList[i]%>" property="<%=fundNameList[i]%>" class="textAlign" style="<%=style%>"/>
		<%	}
		%>	<display:column title="Total" property="total" class="textAlign" style="<%=style%>"/>
			<display:column title="<%=prevEndDate%>" property="prevYearTotal" class="textAlign" style="<%=style%>"/>
 		 	<display:footer>
				<tr>
					<td style="border-left: solid 0px #000000" colspan="20"><div style="border-top: solid 1px #000000 ">&nbsp;</div></td>
				<tr>
			</display:footer>
 		 	<display:setProperty name="export.pdf" value="true" />
			<display:setProperty name="export.pdf.filename" value="RPSchedules.pdf" /> 
			<display:setProperty name="export.csv" value="false" />
		    <display:setProperty name="export.xml" value="false" />
		    <display:setProperty name="export.excel" value="true" />
		    <display:setProperty name="export.excel.filename" value="RPSchedules.xls"/> 			
		    <!-- display:setProperty name="basic.msg.empty_list" value=""/ -->
		 </display:table>
		 </div>
		 

 <table>
 	<tr id="msgRow" align="center">
 	<td align="left" class="tdStlyle" style="FONT-SIZE: 12px;" colspan=2>
 	To print the report, please ensure the following settings:<br>
 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1. Paper size: A4<br>
 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2. Paper Orientation: Landscape
 	</td>
 	</tr>

   <tr>
 	<td align="center" >
    <input type="button" class=button name="backButton"  id="backButton"onclick="goBack();" value="Back" />    
 	</td>
    <td align="center">
 	<input type="button" class=button  name="printButton" onclick="buttonPrint();" value="PRINT" />    
    </td>
    </tr>  
 </table>


</form>
</body>
</html>
