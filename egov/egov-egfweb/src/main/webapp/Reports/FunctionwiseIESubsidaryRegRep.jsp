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
<%@page import="com.exilant.eGov.src.reports.FunctionwiseIESubsidaryReg,java.util.ArrayList,java.text.SimpleDateFormat,java.util.Date,java.util.HashMap,java.util.Iterator"%>
<%@page import="org.apache.log4j.Logger"%>
<html>
	<head>	
		<title>Function wise Income/Expenditure  Subsidary Register Report</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<meta http-equiv="KEYWORDS" content="">
		<meta http-equiv="DESCRIPTION" content="">
		<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen, print" />
		<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
		<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>
		<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
		<script language="javascript" src="../resources/javascript/jsCommonMethods.js"></script>
		<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js" type="text/javascript" ></SCRIPT>

		<SCRIPT LANGUAGE="javascript">
		<%
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date currentDate = new Date();
			String currDate = sdf.format(currentDate);
		%>
		function onBodyLoad()
		{
			PageValidator.addCalendars();
			PageManager.ListService.callListService();
			if(document.getElementById("startDate").value=="")
			{		
				PageManager.DataService.callDataService("finYearDate");
			}
			PageManager.DataService.callDataService('companyDetailData'); 
	
			var beanVal= <%=request.getParameter("fromBean")%>;
			if(beanVal==null)
			{
				document.getElementById('endDate').value = '<%=currDate%>';
			}
			else
			{
				document.getElementById('incExp').value = '<%=request.getParameter("incExp")%>';
				document.getElementById('fundId').value = '<%=request.getParameter("fundId")%>';
				document.getElementById('fundName').value = '<%=request.getParameter("fundName")%>';
				document.getElementById('fromBean').value = '<%=request.getParameter("fromBean")%>';
				document.getElementById('startDate').value = '<%=request.getParameter("startDate")%>';
				document.getElementById('endDate').value = '<%=request.getParameter("endDate")%>';
				document.getElementById('companyDetail_name').value='<%=request.getParameter("companyDetail_name")%>';
			}
			if(beanVal==2)
			{
				document.getElementById("row3").style.display="block";
				document.getElementById("row2").style.display="none";		
			}
			document.getElementById("incExp").focus();
		}
		function afterRefreshPage(dc)
		{
			if(dc.values['serviceID']=="finYearDate")
			{	
				var dt=dc.values['startFinDate'];
				dt=formatDate2(dt);
				document.getElementById("startDate").value=dt;
			}
		}
		function ButtonPress()
		{
			if (!PageValidator.validateForm())
			{
				//document.getElementById('startDate').value='';
				//document.getElementById('endDate').value='';
				//document.getElementById('fundLst').value='';
				return;
			}
			var fundObj=document.getElementById('fundLst');
			document.getElementById('fundName').value=fundObj.options[fundObj.selectedIndex].text;
			var strtDate = document.getElementById('startDate').value;
			rptStartDate = strtDate;
			var endDate = document.getElementById('endDate').value;
			if (endDate=="")
			{
				rptEndDate=strtDate;
				endDate=strtDate;
				document.getElementById('endDate').value=strtDate;
			}
			else
			{
				rptEndDate = endDate;
			}
			var rptfundId= document.getElementById('fundLst').value;
			if(rptfundId=='')
				document.getElementById('fundId').value="0";
			else
				document.getElementById('fundId').value=rptfundId;

			if (document.getElementById('fundLst').value=="")
			{
				bootbox.alert("Please select Fund ");
				return false;
			}
			 if(strtDate.length==0)
			{
				 bootbox.alert("please select start date ");
				return false;
			}
			if(compareDate(formatDate6(strtDate),formatDate6(endDate)) == -1 )
			{
				bootbox.alert('Start Date can not be greater than End Date');
				document.getElementById('startDate').focus();
				return false;
			}
			document.getElementById('fromBean').value = 1;
			document.forms[0].submit();	
		
		}
		function buttonFlush()
		{
			document.getElementById('startDate').value='';
			document.getElementById('endDate').value='';
			document.getElementById('fundLst').selectedIndex=-1;
		}
					
		function pageSetup()
		{
			document.body.leftMargin=0.75;
			document.body.rightMargin=0.75;
			document.body.topMargin=0.75;
			document.body.bottomMargin=0.75;
		}
		function buttonFlush()
		{
			window.location="FunctionwiseIESubsidaryRegRep.jsp";
		}
		function buttonPrintPreview()
		{
			document.getElementById('fromBean').value ="2";	   
			document.forms[0].submit();	
		}

		function buttonPrint()
		{
		    document.getElementById("tbl-header1").style.display = "none";
			document.forms[0].submit();	
			if(window.print)
		  		window.print();
		}
	</SCRIPT>
</head>

<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onload="onBodyLoad()"><!------------------ Header Begins Begins--------------------->
<form name="FunctionwiseIESubsidaryRegRep" action = "FunctionwiseIESubsidaryRegRep.jsp">
<input type="hidden" name="fromBean" id="fromBean" value="0">
<input type="hidden" name="fundId" id="fundId" value="0">
<input type="hidden" id="fundName" name="fundName" value="">
<input type="hidden" name="companyDetail_name" id="companyDetail_name" value="">
<br>
<table align='center' id="table2">
	<tr>
		<td>
			<div id="main">
				<div id="m2">
					<div id="m3" style="width:810px">
						<div class="tbl-header1" id="tbl-header1"> 		<!--       Div for hiding purpose    -->
							<table width="100%" border=0 cellpadding="3" cellspacing="0" id="mainTable">
								<tr>
									<td class="tableheader" valign="center" width="100%" colspan="6"><span id="screenName">Function-wise Income/Expenditure Susidary Register</span><span id="partyName" class="headerwhite2"></span></td>
								</tr>
								<tr class="row1" >
									<td><div align="right" class="labelcell" >Income/Expenditure&nbsp;<font size="2" color="red">*</font></div></td>
									<td class="smallfieldcell">
									<SELECT CLASS="fieldinput" NAME="incExp" id="incExp">
										<option value='I'>Income</option>
										<option value='E'>Expenditure</option>
									</SELECT></td>
									<td><div align="right" valign="center" class="labelcell" >Fund&nbsp;<font size="2" color="red">*</font></div></td>
									<td class="smallfieldcell"><SELECT CLASS="fieldinput" NAME="fundLst" ID="fundLst" exilListSource="fundNameList" exilMustEnter="true" > </SELECT></td>  
								</tr>
								<tr class="row1">
									<td><div align="right" class="labelcell">Start Date<font size="2" color="red">*</font></div></td>
									<td align="left" class="smallfieldcell" style="width:190px">
										<input name="startDate" id="startDate" class="datefieldinput" style="width:90px" size="15" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilDataType="exilAnyDate" exilCalendar="true"  exilMustEnter="true">
									</td>
									<td ><div align="right" class="labelcell">End Date<font size="2" color="red">*</font></div></td>
									<td class="smallfieldcell" style="width:190px">
										<input name="endDate" id="endDate" class="datefieldinput" style="width:90px" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true"  exilMustEnter="true">
									</td>
								</tr>		
								<tr class="row2" id="row2" name="row2">
									<td colspan="6" align="middle"><!-- Buttons Start Here -->
										<table border="0" cellpadding="0" cellspacing="0">
											<tr>
												<td align="right"><input type=button class=button onclick=ButtonPress() href="#" value="Search"></td>
												<td align="right"><input type=button class=button onclick=buttonFlush(); href="#" value="Cancel"></td>
												<td align="right"><input type=button class=button onclick=window.close() href="#" value="Close"></td>
												<td align="right"><input type=button class=button onclick="buttonPrintPreview()" href="#" value="Print Preview"></td>
											</tr>
										</table>
									</td>
								</tr>
								<tr style="DISPLAY: none" class="row3" id="row3" name="row3">
									<td colspan="6" align="middle"><!-- Buttons Start Here -->
										<table border="0" cellpadding="0" cellspacing="0">
											<tr>
												<td align="right"><input type=button class=button onclick=ButtonPress() href="#" value="Search"></td>
												<td align="right"><input type=button class=button onclick=buttonFlush(); href="#" value="Cancel"></td>
												<td align="right"><input type=button class=button onclick=window.close() href="#" value="Close"></td>
												<td align="right"><input type=button class=button onclick="pageSetup();buttonPrint()" href="#" value="Print"></td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
	 					</div>	
					</div>
				</div>
			</div>
		</td>
	</tr>
</table>
</form>
<%
	Logger LOGGER = Logger.getLogger("FunctionwiseIESubsidaryRegRep.jsp");
    if(request.getParameter("fromBean") !=null)
    {
	 	String incExp=request.getParameter("incExp");
	 	String inEx="",heading="";
	 	if(incExp.equals("I"))
	 	{
	 		incExp = "INCOME";
	 		inEx = "Income";
	 		heading = "Total Income (Rs.)";
	 	}
	 	else
	 	{
	 		incExp = "EXPENDITURE";
	 		inEx = "Expenditure";
	 		heading = "Total Expenditure (Rs.)";
	 	}
    	try
	 	{
	 		FunctionwiseIESubsidaryReg reg = new FunctionwiseIESubsidaryReg();
	 		ArrayList retList = reg.getFunctionwiseReport(request.getParameter("incExp"),Integer.parseInt(request.getParameter("fundId")),request.getParameter("startDate"),request.getParameter("endDate"));
	 		ArrayList tempList = new ArrayList();
	 		tempList = (ArrayList)retList.get(0);
	 		HashMap titleMap = new HashMap();
	 		titleMap = (HashMap)tempList.get(0);
	 		request.setAttribute("functionwiseReportObj",retList.get(1));
	 		String compName = request.getParameter("companyDetail_name");
			if(request.getParameter("fromBean").equals("1"))
			{
			%>
				<u><b><div class = "normaltext" align="right" ></div></b></u>
				<center><u><b><div class = "normaltext"><%=request.getParameter("companyDetail_name")%></div></b></u></center>
				<center><u><b><div class = "normaltext">FUNCTION-WISE <%=incExp%> SUBSIDARY REGISTER</div></b></u></center>

				<div class="tbl5-container" id="tbl-container">
					<display:table cellspacing="0" name="functionwiseReportObj" id="currentRowObject" export="true"  sort="list" class="its">
					<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<%=compName%>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FUNCTION-WISE <%=incExp%> SUBSIDARY REGISTER
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</display:caption>

					<div STYLE="display:table-header-group;">
						<display:column property="srlNo" title="Sl.No." />
						<display:column property="functionCode" title="Function Code" />
						<display:column property="functionName" title="Functions Head" />
						<display:column property="totalIncome" title="<%=heading%>" style="text-align:right" />
						<%
							HashMap rowMap = (HashMap)pageContext.getAttribute("currentRowObject");
							int mapsize = rowMap.size()-4;
							Iterator it = rowMap.keySet().iterator();
							int count=0;
							String style="";
							while(it.hasNext())
							{
								Object key = it.next();

								if(key.toString().equals("srlNo")||key.toString().equals("functionCode")||key.toString().equals("functionName")||key.toString().equals("totalIncome"))
									continue;
								count++;
								if(count==mapsize)
									style = "whitespace: wrap;border-right:1px solid #000000;text-align:right";
								else
									style="whitespace: wrap;text-align:right";
								%>
								<display:column  property="<%=key.toString()%>" title="<%=key.toString()+"-"+titleMap.get(key.toString()).toString()%>" style="<%=style%>"/>	
								<%
							}
						%>
						<display:setProperty name="export.pdf" value="true" />
						<display:setProperty name="export.pdf.filename" value="FunctionwiseIESubsidaryRegRep.pdf" /> 
						<display:setProperty name="export.csv" value="false" />
					    <display:setProperty name="export.xml" value="false" />
					    <display:setProperty name="export.excel" value="true" />
					    <display:setProperty name="export.excel.filename" value="FunctionwiseIESubsidaryRegRep.xls"/>
					</div>
					<display:footer>
						<tr>
							<td style="border-left: solid 0px #000000" colspan="50"><div style="border-top: solid 1px #000000 ">&nbsp;</div></td>
						</tr>
			  		</display:footer>
					</display:table>
				</div>
			<%
			}
			else if(request.getParameter("fromBean").equals("2"))
			{
				%>
				<u><b><div class = "normaltext" align="right" ></div></b></u>
				<center><u><b><div class = "normaltext"><%=request.getParameter("companyDetail_name")%></div></b></u></center>
				<center><u><b><div class = "normaltext">FUNCTION-WISE <%=incExp%> SUBSIDARY REGISTER</div></b></u></center>
				<div class="tbl5-container" id="tbl-container" >
					<display:table cellspacing="0" name="functionwiseReportObj" id="currentRowObject" export="false"  sort="list" class="its">
					<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<%=compName%>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FUNCTION-WISE <%=incExp%> SUBSIDARY REGISTER
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</display:caption>

					<div STYLE="display:table-header-group;">
						<display:column property="srlNo" title="Sl.No." />
						<display:column property="functionCode" title="Function Code" />
						<display:column property="functionName" title="Functions Head" />
						<display:column property="totalIncome" title="<%=heading%>" style="text-align:right" />
						<%
							HashMap rowMap = (HashMap)pageContext.getAttribute("currentRowObject");
							int mapsize = rowMap.size()-4;
							Iterator it = rowMap.keySet().iterator();
							int count=0;
							String style="";
							while(it.hasNext())
							{
								Object key = it.next();

								if(key.toString().equals("srlNo")||key.toString().equals("functionCode")||key.toString().equals("functionName")||key.toString().equals("totalIncome"))
									continue;
								count++;
								if(count==mapsize)
									style = "whitespace: nowrap;border-right:1px solid #000000;text-align:right";
								else
									style="whitespace: nowrap;text-align:right";
								%>
								<display:column  property="<%=key.toString()%>" title="<%=key.toString()+"-"+titleMap.get(key.toString()).toString()%>" style="<%=style%>"/>
								<%
							}
						%>
					</div>
					<display:footer>
						<tr>
							<td style="border-left: solid 0px #000000" colspan="50"><div style="border-top: solid 1px #000000 ">&nbsp;</div></td>
						</tr>
			  		</display:footer>
					</display:table>
				</div>
			<%
			}
		}
		catch(Exception e)  
		{
			LOGGER.info("Exception in Jsp Page:"+e.getMessage());
			%>
		 	<script>
		 	bootbox.alert("Error :<%=e.getMessage()%>");
		 	</script>
		 	<%
		}
	}
%>
</body>
</html>
