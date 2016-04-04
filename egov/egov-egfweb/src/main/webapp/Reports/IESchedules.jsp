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
<%@ page buffer = "16kb" %>
<%@page  import="com.exilant.eGov.src.reports.*,java.io.*,java.util.*"%>
<%@page import="org.apache.log4j.Logger"%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<META http-equiv=pragma content=no-cache>

<link rel=stylesheet href="../css/egov.css?rnd=${app_release_no}" type="text/css" media="screen, print" />
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js?rnd=${app_release_no}"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js?rnd=${app_release_no}"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js?rnd=${app_release_no}"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js?rnd=${app_release_no}"></SCRIPT>
<script language="javascript" src="../resources/javascript/jsCommonMethods.js?rnd=${app_release_no}"></script>
<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js?rnd=${app_release_no}" type="text/javascript" ></SCRIPT>
<link rel="stylesheet" href="../css/lockRowHead.css?rnd=${app_release_no}" type="text/css" />

<SCRIPT LANGUAGE="javascript">
var endDate="";
var fuid="";
var finYear1="";
//var schId="",endDate="",snapTime="",period="",year="",prevEndDate="",amountIn="",rptHead=""
function onLoad(){
	document.getElementById('amountIn').value="<%=request.getParameter("amountIn")%>";
	document.getElementById('scheduleId').value="<%=request.getParameter("scheduleId")%>";
	document.getElementById('startDate').value="<%=request.getParameter("startDate")%>";
	document.getElementById('endDate').value="<%=request.getParameter("endDate")%>";
	document.getElementById('departmentId').value="<%=request.getParameter("departmentId")%>";
	document.getElementById('functionaryId').value="<%=request.getParameter("functionaryId")%>";
	document.getElementById('functionCode').value="<%=request.getParameter("functionCode")%>";
	document.getElementById('fieldId').value="<%=request.getParameter("fieldId")%>";
	
	//var queryString="<%=request.getQueryString()%>"
	var fromBean="<%=request.getParameter("fromBean")%>";
	if(fromBean!=null && fromBean==0){
		document.getElementById('fromBean').value=1;
		document.forms[0].action=action = "../Reports/IESchedules.jsp?fromBean=1";
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


</SCRIPT>

</head>
<body onLoad="onLoad()">

<jsp:useBean id = "ieBean" scope ="request" class="com.exilant.eGov.src.reports.IEBean"/>
<jsp:setProperty name = "ieBean" property="*"/>

<form name="IESchedules" action = "IESchedules.jsp" method = "post">
 	
 	<input type=hidden id=period name=period value=""/>
	<input type=hidden id=scheduleId name=scheduleId value=""/>
	<input type=hidden id=amountIn name=amountIn value=""/>
	<input type=hidden id=fin_Year name=fin_Year value=""/>
	<input type=hidden id=date name=date value=""/>
	<input type=hidden id=startDate name=startDate value=""/>
	<input type=hidden id=endDate name=endDate value=""/>
	<input type=hidden id=departmentId name=departmentId value=""/>
	<input type=hidden id=functionaryId name=functionaryId value=""/>
	<input type=hidden id=functionCode name=functionCode value=""/>
	<input type=hidden id=fieldId name=fieldId value=""/>
	<input type=hidden name="fromBean" id="fromBean" value="0">
	
 <%!
 	HashMap hashMap=new HashMap();
 	HashMap hmcolname=null;
 	HashMap colTitle = null;
 	String data=null;
 	String scheduleName=null;
 	ArrayList al=new ArrayList();
 	String colData=null;
 	String colDataTitle=null;
 %>
 <%
 		Logger LOGGER = Logger.getLogger("IESchedules.jsp");
 		 try{
 		 IncomeExpenseReport IERpt = new IncomeExpenseReport ();			   
 		 al=(ArrayList)IERpt.getIEReport(ieBean);
 		 hashMap=(HashMap)al.get(0);
 		 request.setAttribute("links",(ArrayList)hashMap.get("data"));
 		 
 		 //System.out.println("list:"+ieBean.getTitle());
 		 }
 		 catch(Exception e){
 		 LOGGER.info("Exception in Jsp Page "+ e.getMessage());
 		 %> 
 		 <script>
 		bootbox.alert("Error :<%=e.getMessage()%>");
 		 </script>
 		 <%
 		 }
 		 String compName = request.getParameter("compName");
 %>

  <div class="tbl4-container"  id="tbl-container" > 
  <center><u><b><div class = "normaltext"><%=compName%></div></b></u></center>
  <display:table cellspacing="0"  name="links" id="currentRowObject"  export="true" class="its" > 
 	<display:caption media="html"><div class="normaltext"><u><%=ieBean.getTitle()%></u></div><div align="right"><span class="labelcell"><B>Amount in <%=ieBean.getAmountIn()%></B></span></div></display:caption>
 	<display:caption media="pdf">&nbsp;<%=compName%>&nbsp;-&nbsp;<%=ieBean.getTitle()%>
 			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Amount in <%=ieBean.getAmountIn()%></display:caption>
  	<display:caption media="excel"><%=ieBean.getTitle()%>
 			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Amount in <%=ieBean.getAmountIn()%></display:caption>
  	

  	 		<%
  	 		String tempStyle="";
  	 		boolean applystyle=true;
  	 		//scheduleName=(String)((HashMap)pageContext.getAttribute("currentRowObject")).get("scheduleName");
 			for(int i=0;i<((ArrayList)hashMap.get("columnNamesListwithoutSpace")).size();i++)
 			{
 				hmcolname=(HashMap)((ArrayList)hashMap.get("columnNamesListwithoutSpace")).get(i);
 				//colTitle=(HashMap)((ArrayList)hashMap.get("columnNamesList")).get(i);
 				//System.out.println("colName:"+i+"::"+hmcolname.get("colName").toString());
 				
 				colData=(String)((HashMap)pageContext.getAttribute("currentRowObject")).get(hmcolname.get("colName"));
 				colDataTitle=(String)((HashMap)pageContext.getAttribute("currentRowObject")).get("rowHeader");
 				String colDataScheduleName=(String)((HashMap)pageContext.getAttribute("currentRowObject")).get("scheduleNameasData");
 				
 
 				//System.out.println("hmcolname="+hmcolname+",colData=="+colData+",colDataTitle==="+colDataTitle+",colDataScheduleName="+colDataScheduleName);
 				//System.out.println("colDataScheduleName:"+(colDataTitle!=null && colDataTitle.equals("rowHeader")));
 				 					
				if(colDataScheduleName!=null && colDataScheduleName.equals("scheduleNameasData"))
				{
					if(colDataScheduleName.equals("scheduleNameasData") && colDataTitle==null && !colData.equals(""))
					{
						if(applystyle)  // only applicable for printing schedule no
							tempStyle="width:110px;";
						else
							tempStyle="border-left:0px";
						%>
							<display:column  class="normaltext" title=""  property="<%=hmcolname.get("colName").toString()%>" style="<%=tempStyle%>">
							</display:column>
						<%	
						applystyle=false;
						tempStyle="";
					}
					else
					{
						%>
						<display:column  class="normaltext" title=""  property="<%=hmcolname.get("colName").toString()%>" style="">
						</display:column>
						<%	
					}
			}
			else if((colDataTitle!=null && colDataTitle.equals("rowHeader")))
			{ 
				%>
				<display:column  class="normaltext" title=""  property="<%=hmcolname.get("colName").toString()%>" style="background-color:#69c;text-align:center;border-top:solid 1px #000000">
				<%=((HashMap)pageContext.getAttribute("currentRowObject")).get(hmcolname.get("colName"))%>
				</display:column>
				<%
 			}
 			else if(i>=2){ 
 		%>
 				<display:column class="textAlign"  title=""  property="<%=hmcolname.get("colName").toString()%>" style="">
 				<%=((HashMap)pageContext.getAttribute("currentRowObject")).get(hmcolname.get("colName"))%>
 				</display:column>
 
  		 <%		} else { 
 		%>
 				<display:column class=""  title=""  property="<%=hmcolname.get("colName").toString()%>" style="">
 				<%=((HashMap)pageContext.getAttribute("currentRowObject")).get(hmcolname.get("colName"))%>
 				</display:column>
 
  		 <%		}

  		 	}
  		 %>


				<display:setProperty name="export.pdf" value="true" />
				<display:setProperty name="export.pdf.filename" value="IESchedules.pdf" /> 
				<display:setProperty name="export.csv" value="false" />
			    <display:setProperty name="export.xml" value="false" />
			    <display:setProperty name="export.excel" value="true" />
			    <display:setProperty name="export.excel.filename" value="IESchedules.xls"/> 			

 			<display:footer>
 				<tr>
 					<td style="border-left: solid 0px #000000" colspan="20"><div style="border-top: solid 1px #000000 ">&nbsp;</div></td>
 				<tr>
			</display:footer>
 
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
