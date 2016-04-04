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
  --><!--
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
<%@ page import="java.util.Date,java.util.ArrayList,com.exilant.eGov.src.reports.SummaryStatement,com.exilant.eGov.src.reports.SummaryStatementBean,java.text.SimpleDateFormat,org.apache.log4j.Logger;" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<META http-equiv=pragma content=no-cache>

<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen" />
<link rel=stylesheet href="../css/print.css" type="text/css" media="print" />

<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js?rnd=${app_release_no}"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js?rnd=${app_release_no}"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js?rnd=${app_release_no}"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js?rnd=${app_release_no}"></SCRIPT>
<script language="javascript" src="../resources/javascript/jsCommonMethods.js?rnd=${app_release_no}"></script>
<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js?rnd=${app_release_no}" type="text/javascript" ></SCRIPT>
<SCRIPT type="text/javascript" src="../resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}"></Script>

<SCRIPT LANGUAGE="javascript">

function onLoad()
{
	PageValidator.addCalendars(); 
	PageManager.ListService.callListService();
	
}

function ButtonPress()
{
	if (!PageValidator.validateForm())
		return;
	
	document.getElementById("fromBean").value=1;	
	var sDate=document.getElementById("wDate").value;
	var fObj=document.getElementById('FYear');
	document.getElementById("finyear").value=fObj.options[fObj.selectedIndex].text;
	var modeObj=document.getElementById('monthname');
	workNo=document.getElementById('wono');
	workNoid=workNo.value;
	workName1=document.getElementById('workname');
	workNameid=workName1;
	
	obj = document.getElementById('month');
	document.getElementById('monthName').value = obj.options[obj.selectedIndex].text;
	var moth = document.getElementById('month').value
	
	if(moth==0)
	{
		bootbox.alert('Select Month Name');

		document.getElementById('month').focus();
		return false;
	}
	document.forms[0].submit();	
}

function buttonFlush()
{
	window.location="SummaryStatement.jsp";

}

function buttonPrint()
{
      	document.getElementById("tbl-header1").style.display = "none";
	document.getElementById("submitRow").style.display = "none";
	 if(window.print)
	  window.print();
		
	document.getElementById("tbl-header1").style.display = "block";
  	
  	
}
</SCRIPT>
</head>

<body onLoad="onLoad()">
<form name="SummaryStatement" action="SummaryStatement.jsp?wono="+wono+"&workname="+workname+"&wDate="+wDate+"&monthname="+month+"& &finyear="+finyear method="post">

<input type="hidden" name="fromBean" id="fromBean" value="0"> 
<input type="hidden" name="monthName" id="monthName" value="0"> 
<input type="hidden" id="finyear" name="finyear" value="">

<table align='center' id="table2">
<tr><td>
<div id="main"><div id="m2"><div id="m3" style="width:810px">

<div class="tbl-header1" id="tbl-header1">

            <table align='center' width="150" class="tableStyle" cellpadding="3" cellspacing="0">
             
               <tr >
                <td colspan="6" class="tableheader" valign="center"  ><span id="screenName">Summary Statement For Capital Work-In-Progress/Deposit Works</span><span class="headerwhite2"></span></td>
               </tr>
               <tr>
                 	<td><div align="right" valign="center" class="labelcell">Work Order No.<span class="leadon">&nbsp;&nbsp;&nbsp;</span></div></td>
                	<td class=smallfieldcell><input name="wono" id="wono"></td>
			<td><div align="right" valign="center" class="labelcell">Work Order Name<span class="leadon"></span></div></td>
			<td class=smallfieldcell align="left"><input name="workname" id="workname"></td>
               </tr>
               <tr>
			<td><div align="right" valign="center" class="labelcell">Work Order Date<span class="leadon"></span></div></td>
			<td align="left" class=smallfieldcell><input name="wDate" id="wDate" class="datefieldinput" onKeyUp="DateFormat(this,this.value,event,false,'3')"  exilcalendar="true" ></td>
			<td width="172"><div align="right" valign="center" class="labelcell">&nbsp;</div></td>
			<td width="172"><div align="right" valign="center" class="labelcell">&nbsp;</div></td>
		</tr>
		<tr>
			<td><div align="center" class="labelcell" >Financial Year<span class="leadon">*</span></div></td>
			<td class=smallfieldcell align="center" valign="center"  width="200"><div align="left"><SELECT class="combowidth" id="FYear" name="FYear" exilListSource="yearNameList" exilMustEnter="true" >	</SELECT></div></td>	
			<td width="172"><div align="right" valign="center" class="labelcell">Month Name <span class="leadon">*</span></div></td>
			<td class=smallfieldcell align="left" width="34%"><SELECT class="combowidth" id="month" name="month" exilMustEnter="true"> <option value="0"></option><option value="1">January</option> <option value="2">February</option> <option value="3">March</option> <option value="4">April</option> <option value="5">May</option> <option value="6">June</option> <option value="7">July</option> <option value="8">August</option>	<option value="9">September</option> <option value="10">October</option> <option value="11">November</option> <option value="12">December</option></SELECT></td>
		</tr>
			 
               <tr class="row2" height="5">
                 <td>&nbsp;</td>
                 <td>&nbsp;</td>
                 <td>&nbsp;</td>
                 <td>&nbsp;</td>
               </tr>
               <tr class="row2" id="submitRow">
                 <td colspan="4" align="middle">
                   <table border="0" cellpadding="0" cellspacing="0">
                     <tr>
                      <td>		
			<input type=button class=button onclick="ButtonPress()" href="#" value="Search">
			<input type=button class=button onclick="buttonFlush()" href="#" value="Cancel">
			<input type=button class=button onclick="window.close();" href="#" value="Close">
			<input type=button class=button onclick="buttonPrint()" href="#" value="Print">
		   	</td>
                     </tr>
                 </table></td>
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
	Logger LOGGER = Logger.getLogger("SummaryStatement.jsp");
    if(request.getParameter("fromBean")!=null && request.getParameter("fromBean").equals("1"))
    {
	 try
	 {
		
		SummaryStatement tbReport1 = new SummaryStatement();
        ArrayList list=tbReport1.getSummaryStatementReport(request.getParameter("wono"),request.getParameter("workname"),request.getParameter("month"),request.getParameter("wDate"),request.getParameter("finyear"));
		//System.out.println("List Is :"+list);
		if(list!=null)
		{
		  request.setAttribute("tbreportList",list);
%>	
	<center><u><b><div class = "normaltext">Summary Statement For Capital Work-In-Progress/Deposit Works</div></b></u></center>   		
	<center><u><b><div class = "normaltext"><%= request.getParameter("monthName") %> : <%= request.getParameter("finyear") %></div></b></u></center>   		
	<div class="tbl2-container" id="tbl-container">

	<display:table cellspacing="0" name="tbreportList" id="currentRowObject" export="true"  sort="list" class="its">

	<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;Summary Statement For Capital Work-In-Progress/Deposit Works - (<%= request.getParameter("monthName") %> : <%= request.getParameter("finyear") %>)
	</display:caption>

	<display:caption media="excel">
	Summary Statement For Capital Work-In-Progress/Deposit Works - (<%= request.getParameter("monthName") %> : <%= request.getParameter("finyear")%>)
	</display:caption>

	<div STYLE="display:table-header-group;">
 	<display:column property="headOfAccount" title="Head Of Account" class="textAlign3" /> 
	<display:column property="workOrderNo" title="Work Order/Agreement No." class="textAlign3" /> 
	<display:column property="nameOfProject" title="Name Of Project" class="textAlign3" /> 
	<display:column property="valueOfWorkAmount"  title="Value Of Work/Contract Amount (Rs.)" class="textAlign3" />
	<display:column property="expenditureAmount"  title="Expenditure Incurred at Beginning of The Month (Rs.)" class="textAlign3" />
	<display:column property="expenditureBillAdmittedAmount"  title="Expenditure Incurred(bills admitted) During the month (Rs.)" class="textAlign3" />
	<display:column property="totalExpenditure"  title="Total Expenditure Incurred at end of the month (Rs.)" class="textAlign3" />
	<display:column property="amountOfContractUnexecute"  title="Amount of Contract Remaining Unexecuted (Rs.)" class="textAlign3" />
	<display:column property="projectCompleted"  title="Whether Project Completed(Y/N)" class="textAlign3" />
  
	<display:setProperty name="export.pdf" value="true" />
	<display:setProperty name="export.pdf.filename" value="SummaryStatement.pdf"/> 
	<display:setProperty name="export.csv" value="false" />
	<display:setProperty name="export.xml" value="false" />
	<display:setProperty name="export.excel" value="true" />
	<display:setProperty name="export.excel.filename" value="SummaryStatement.xls"/>
	</div>
	<display:footer>
		<tr>
			<td style="border-left: solid 0px #000000" colspan="10"><div style="border-top: solid 1px #000000 ">&nbsp;</div></td>
		</tr>
	</display:footer>
	</display:table>
	</div>
		

	<% 	
		}
		else
		{
				
	%>
	<center>
	<display:table export="false" sort="list" pagesize = "15" class="view" cellspacing="0" cellpadding="10">
	<display:caption style="align:center;">Nothing Found to Display</display:caption>
	</display:table>
	</center>
	<% 
		}
 	}
		 catch(Exception e){
		 LOGGER.error(e.getMessage());
		// System.out.println("Exception in Jsp Page"+ e.getMessage());
		 %> 
		 <script>
		 bootbox.alert("Error :<%=e.getMessage()%>"); 
		 </script>
		 <%
		 }
	}  
%>
	
</form>
</body>
</html>
