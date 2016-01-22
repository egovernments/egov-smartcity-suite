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
<%@page import="com.exilant.eGov.src.reports.SchemeUtilizationReport,java.util.ArrayList,java.text.SimpleDateFormat,java.util.Date,java.util.LinkedHashMap,java.util.Iterator"%>
<%@ page import="org.apache.log4j.Logger"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<META http-equiv=pragma content=no-cache>

<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen" />
<link rel=stylesheet href="../css/print.css" type="text/css" media="print" />


<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
<script language="javascript" src="../resources/javascript/jsCommonMethods.js"></script>
<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js" type="text/javascript" ></SCRIPT>
<link rel="stylesheet" href="../css/lockRowHead.css" type="text/css" />


<SCRIPT LANGUAGE="javascript">


var vstartDate="";
var vendDate="";
function onLoad()
{
	
	PageValidator.addCalendars();
	PageManager.ListService.callListService();
	
	
	var fromBean="<%=request.getParameter("fromBean")%>";
	

}
function ButtonPress()
{
	
	if (!PageValidator.validateForm())
	return;
			
		
		var pscheme_Id=document.getElementById("scheme_Id");
		if(pscheme_Id.value !=""){
			document.getElementById('pschemeid').value=pscheme_Id.value;
			document.getElementById('schemeName').value=pscheme_Id.options[pscheme_Id.selectedIndex].text;
		}
		vstartDate=validateDate(document.getElementById('voucherDateFrom').value);	
		vendDate=validateDate(document.getElementById('voucherDateTo').value);	
		
				
		if(vendDate!="")
		{
			if( compareDate(formatDate6(vstartDate),formatDate6(vendDate)) == -1 )
			{
				//bootbox.alert("Inside compare dt");
				bootbox.alert('From Date cannot be greater than To Date');
				//document.getElementById('voucherDateFrom').value='';
				//document.getElementById('voucherDateTo').value='';
				document.getElementById('voucherDateFrom').focus();
				return false;
			}
		}
		
		
	document.getElementById('fromBean').value = 1;
	document.forms[0].submit();
}

function buttonFlush()
{

	window.location="SchemeUtilizationReport.jsp";

}

function buttonPrint()
{ 
	document.getElementById("tbl-header1").style.display = "none";
	document.getElementById("msgRow").style.display = "none";
	document.getElementById("submitRow").style.display = "none";
  	if(window.print)
  	{
  		window.print();
	}
	document.getElementById("tbl-header1").style.display = "block";
}
function fillDate1(objName)
{
	PageValidator.showCalendar('selectedDate');
	document.getElementById(objName).value=document.getElementById('selectedDate').value;
	document.getElementById('selectedDate').value = "";
}


</SCRIPT>
</head>
<body onLoad="onLoad()">
<form name="SchemeReport" action="">

<input type="hidden" name="selectedDate" id="selectedDate">
<input type="hidden" name="fromBean" id="fromBean" value="0">
<input type=hidden name="finYear" id="finYear" value="">
<input type=hidden name="schemeName" id="schemeName" value="">

<input type=hidden name="pschemeid" id="pschemeid" value="">


<table align='center' id="table2">
<tr><td>
<div id="main"><div id="m2"><div id="m3" style="width:810px">

<div class="tbl-header1" id="tbl-header1">

	<table align='center' class="tableStyle" id="table3"> 
	<tr >
	<td colspan="6" class="rowheader" valign="center"  ><span id="screenName" class="headerwhite2">
	SCHEME UTILIZATION REPORT</span></td>
	</tr>


	<tr>
	<td align="right"><div align="right" class="labelcell" >Scheme<span class="leadon">*</span></div></td>
	<td class=smallfieldcell>
	<select  name="scheme_Id" id="scheme_Id" class="combowidth1"  exilListSource="schemeList" exilMustEnter="true">
	</select>
	</td>
	</tr>
	<tr >
	<td align="right" ><div align="right" class="labelCell">Date From<span class="leadon">*</span></div></td>
	<td class=smallfieldcell >
	<input class="datefieldinput"  name="voucherDateFrom"  id="voucherDateFrom" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilMustEnter="true">
	<A onclick="fillDate1('voucherDateFrom')" tabIndex=-1 href=#><IMG tabIndex=-1 src="/egi/resources/erp2/images/calendar.gif"></A>
	</td>
	<td align="right" ><div align="right" class="labelcell">To<span class="leadon">*</span></div></td>
	<td class=smallfieldcell>
	<input class="datefieldinput" name="voucherDateTo"  id="voucherDateTo" onkeyup="DateFormat(this,this.value,event,false,'3')" exilMustEnter="true">
	<A onclick="fillDate1('voucherDateTo')" tabIndex=-1 href=#><IMG tabIndex=-1 src="/egi/resources/erp2/images/calendar.gif"></A>
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
	    	<tr id="submitRow">
	    		<td colspan="10" align="middle">
	    		<table border="0" cellpadding="0" cellspacing="0"  >
	    		<tr>	
	    		<td>		
	    			<input type=button class=button onclick="ButtonPress()" href="#" value="Submit">
	    			<input type=button class=button onclick="buttonPrint()" href="#" value="Print">
	    			<input type=button class=button onclick="buttonFlush()" href="#" value="Cancel">
	    			<input type=button class=button onclick="window.close();" href="#" value="Close">
	    		    <input type="button" name="viewAllSchedulesButton" id="viewAllSchedulesButton" class="button" onclick="printAllSchedules();" value="View All Schedules" style="display:none" />    
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
		Logger LOGGER = Logger.getLogger("SchemeUtilizationReport.jsp");
	    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
	    {
		 	
	    	try
		 	{
		 		SchemeUtilizationReport schemeRpt = new SchemeUtilizationReport();
		 		ArrayList retList = schemeRpt.getSchemeUtilizationReport(Integer.parseInt(request.getParameter("pschemeid")),request.getParameter("voucherDateFrom"),request.getParameter("voucherDateTo"));
		 		request.setAttribute("schemeWiseReportObj",retList);
				%>
				
		
		<center><u><b><div class = "normaltext">SCHEME UTILIZATION REPORT for <%= request.getParameter("schemeName") %> : From <%= request.getParameter("voucherDateFrom") %>  To  <%= request.getParameter("voucherDateTo") %></div></b></u></center>   		
		<b><div class = "normaltext" align="right">Amount in:(Rs.)</div></b></u>
				<div class="tbl2-container" id="tbl-container">
				<display:table cellspacing="0" name="schemeWiseReportObj" id="currentRowObject" export="true"  sort="list" class="its">
				
				<display:caption media="pdf">&nbsp;&nbsp;			
				SCHEME UTILIZATION REPORT for &nbsp; <%= request.getParameter("schemeName") %> : From <%= request.getParameter("voucherDateFrom") %> &nbsp;&nbsp; To &nbsp;&nbsp; <%= request.getParameter("voucherDateTo") %>
				&nbsp;&nbsp;Amount in:(Rs.)
				</display:caption>
				
				<display:caption media="excel">
				&nbsp;&nbsp;SCHEME UTILIZATION REPORT for <%= request.getParameter("schemeName") %>: From <%= request.getParameter("voucherDateFrom") %>  To  <%= request.getParameter("voucherDateTo") %>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Amount in:(Rs.)																				
				</display:caption>

				<div STYLE="display:table-header-group;">
					
					<display:column property="particulars" title="<center>Particulars</center>"  />
					<%
						LinkedHashMap rowMap = (LinkedHashMap)pageContext.getAttribute("currentRowObject");
						
						Iterator it = rowMap.keySet().iterator();
						int count=0;
						while(it.hasNext())
						{
							Object key = it.next();
							
							if(key.toString().equals("particulars")||key.toString().equals("schemetotal"))
									continue;

							
							//System.out.println("key====================="+key.toString()+",value="+rowMap.get(key).toString());
							
							%>
							<display:column  title="<%=key.toString()%>" style="text-align:right" >	
							<%=rowMap.get(key).toString() %>
							</display:column>
							<%
						}
					%>
					
					<display:column property="schemetotal" title="<center>Scheme Total</center>" class="textAlign3" style="text-align:right" />
					
					<display:setProperty name="export.pdf" value="true" />
					<display:setProperty name="export.pdf.filename" value="SchemeUtilizationReport.pdf" /> 
					<display:setProperty name="export.csv" value="false" />
				    <display:setProperty name="export.xml" value="false" />
				    <display:setProperty name="export.excel" value="true" />
				    <display:setProperty name="export.excel.filename" value="SchemeUtilizationReport.xls"/>
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
			catch(Exception e)  
			{
				LOGGER.error("Exception in Jsp Page:"+e.getMessage());
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
