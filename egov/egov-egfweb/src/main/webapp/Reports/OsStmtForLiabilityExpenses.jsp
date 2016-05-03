<%--
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
  --%>


<!--
	Report Name : STATEMENT OF OUTSTANDING LIABILITY FOR EXPENSES FOR CONTRACTORS/SUPPLIERS
	Author		: Ilayaraja P
	Created	on	: 06-May-2008
	Purpose 	: To get the outstanding for liability expenses for contractors/suppliers
 -->
<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page buffer = "16kb" %>
<%@page import="com.exilant.eGov.src.reports.OsStmtForLiabilityExpenses,org.apache.log4j.Logger,org.egov.infstr.utils.EGovConfig,java.text.SimpleDateFormat,java.util.ArrayList,java.util.Date,java.util.HashMap,java.util.List" %>

<html>
	<head>
		<title>Statement of Outstanding Liability For Expenses Report</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<meta http-equiv="KEYWORDS" content="">
		<meta http-equiv="DESCRIPTION" content="">
		<META http-equiv=pragma content=no-cache>
		<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen, print"/>

		<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js?rnd=${app_release_no}"></SCRIPT>
		<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js?rnd=${app_release_no}"></SCRIPT>
		<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js?rnd=${app_release_no}"></SCRIPT>
		<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js?rnd=${app_release_no}"></SCRIPT>
		<script LANGUAGE="javascript" src="../resources/javascript/jsCommonMethods.js?rnd=${app_release_no}"></script>
		<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js?rnd=${app_release_no}"></SCRIPT>
		<script type="text/javascript" src="../commonyui/build/yahoo/yahoo.js"></script>
		<script type="text/javascript" src="../commonyui/build/dom/dom.js" ></script>
		<script type="text/javascript" src="../commonyui/build/autocomplete/autocomplete-debug.js"></script>
		<script type="text/javascript" src="../commonyui/build/event/event-debug.js"></script>
		<script type="text/javascript" src="../commonyui/build/animation/animation.js"></script>
		<script language="javascript" src="../resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}"></script>
		
		<link type="text/css" rel="stylesheet" href="../commonyui/build/reset/reset.css">
		<link type="text/css" rel="stylesheet" href="../commonyui/build/fonts/fonts.css">
		<link type="text/css" rel="stylesheet" href="../commonyui/examples/autocomplete/css/examples.css">
		
		<style type="text/css">
			#codescontainer {position:absolute;left:11em;width:9%}
			#codescontainer .yui-ac-content {position:absolute;width:100%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
			#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:100%;background:#a0a0a0;z-index:9049;}
			#codescontainer ul {padding:5px 0;width:100%;}
			#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
			#codescontainer li.yui-ac-highlight {background:#ff0;}
			#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
		</style>

		<SCRIPT LANGUAGE="javascript">
		<%
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date currentDate = new Date();
			String currDate = sdf.format(currentDate);
		%>

			function onLoad()
			{
				PageValidator.addCalendars();
				loadSelectData('../commonyui/egov/loadComboAjax.jsp', "fund", "id", "name", " isactive=true and isnotleaf!=true", 'dummy', 'fund');
				var fromBean = '<%=request.getParameter("fromBean")%>';
				PageManager.DataService.callDataService('companyDetailData'); 
				if(fromBean==null || fromBean=='null')
				{
					//document.getElementById('asOnDate').value="<%=currDate%>";
					
				}
				else
				{
					if(fromBean=='print')
					{
						document.getElementById("tbl-header2").style.display="block";
						document.getElementById("row3").style.display="block";
						document.getElementById("row2").style.display="none";
					}
					//else
					{
						//document.getElementById('asOnDate').value="<%=request.getParameter("asOnDate")%>";
						document.getElementById('startDate').value="<%=request.getParameter("startDate")%>";
						document.getElementById('endDate').value="<%=request.getParameter("endDate")%>";
						var fundObj=document.getElementById('fund');
						for(var i=0;i<fundObj.options.length;i++){
							if(fundObj.options[i].value == "<%=request.getParameter("fund")%>"){
								fundObj.selectedIndex=i;
							}
						}
						
						
						document.getElementById('type').value = '<%=request.getParameter("type")%>'
						showCSName(document.getElementById('type'));
						if(document.getElementById('code'))
							document.getElementById('code').value = '<%=request.getParameter("code")%>';
						if(document.getElementById('divId'))
							document.getElementById('divId').value = '<%=request.getParameter("divId")%>';
						if(document.getElementById('conSupName'))
							document.getElementById('conSupName').value = '<%=request.getParameter("conSupName")%>';
					}
					document.getElementById('companyDetail_name').value='<%=request.getParameter("companyDetail_name")%>';
				}
				
				getData();
				document.getElementById('startDate').focus();
			}
			
			function getData()
			{
				/*
				 * The following code for supplier/contractor code implements Ajax autocomplete through YUI.
				 */
			       var type='';
			       if(document.getElementById('type').value==1)
			       		type='supplierCode';
			       else
			       		type='contractorCode';
			       var url = "../commons/Process.jsp?type=" +type+ " ";
			       var req2 = initiateRequest();
			       req2.onreadystatechange = function()
			       {
			              if (req2.readyState == 4)
			              {
			                  if (req2.status == 200)
			                  {
			                  	var codes2=req2.responseText;
			                  	var a = codes2.split("^");
								var codes = a[0]; 
								acccodeArray=codes.split("+");
								codeObj = new YAHOO.widget.DS_JSArray(acccodeArray);
			                  }
			              }
			        };
			        req2.open("GET", url, true);
			        req2.send(null);
			}
			
			function openSearch(obj)
			{
				var a = new Array(2);
				var sRtn='';
	 			var relationType=document.getElementById('type').value;
	 			if(relationType==1)
	 				sRtn = showModalDialog("/EGF/HTML/Search.html?tableNameForCode=supplier_os_liability","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
	 			else if(relationType==2)
	 				sRtn = showModalDialog("/EGF/HTML/Search.html?tableNameForCode=contractor_os_liability","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
	 			if ( sRtn != '' )
	   			{
		 			a = sRtn.split("`~`");
					document.getElementById('code').value = a[0];
					document.getElementById('conSupName').value = a[1];
					document.getElementById('divId').value = a[2];
	   			}
			}
			
			function showCSName(obj)
			{
				if(obj.value=='0')
					document.getElementById('conSupNameRow').style.display='none';
				else
				{
					document.getElementById('conSupNameRow').style.display='block';
					if(obj.value=='1')
					{
						document.getElementById('codeLabel').innerHTML ='Supplier Code';
						document.getElementById('conSupNameLabel').innerHTML ='Supplier Name';
						document.getElementById('code').value = "";
						document.getElementById('conSupName').value = "";
						document.getElementById('divId').value = "";
					}
					else
					{
						document.getElementById('codeLabel').innerHTML ='Contractor Code';
						document.getElementById('conSupNameLabel').innerHTML ='Contractor Name';
						document.getElementById('code').value ="";
						document.getElementById('conSupName').value = "";
						document.getElementById('divId').value = "";
					}
				}
				getData();
			}
			function setNameOnCodeChange()
			{
				var code1=document.getElementById('code').value;
				var type1=document.getElementById('type').value;

				if(code1!= '' && type1!=0)
				{
					document.getElementById('code').value="";
					document.getElementById('conSupName').value="";
			
					var rel='relation';
					PageManager.DataService.setQueryField('conSupCode1',code1);
					if(type1==1)
						PageManager.DataService.callDataService('getSuppliersForOsLiability_description');
					else if(type1==2)
						PageManager.DataService.callDataService('getContractorsForOsLiability_description');
				}
				else
				{
					document.getElementById('conSupName').value="";
					document.getElementById('divId').value="";
				}
			}
			
			function ButtonPress()
			{
				if (!PageValidator.validateForm())
					return;
				var strtDate = document.getElementById('startDate').value;
				var endDate = document.getElementById('endDate').value;
				if( compareDate(formatDate6(strtDate),formatDate6(endDate)) == -1 )
				{
					bootbox.alert('Start Date cannot be greater than End Date');
							document.getElementById('startDate').value='';
							document.getElementById('endDate').value='';
							document.getElementById('startDate').focus();
							return false;
				}
				document.getElementById("fromBean").value="submit";
				document.forms[0].submit();
			}
			function buttonFlush()
			{
				window.location="OsStmtForLiabilityExpenses.jsp";
			}
			//For Print Preview method
			function buttonPrintPreview()
			{
				document.getElementById('fromBean').value ="print";
				document.forms[0].submit();
			}
			function pageSetup()
			{
				document.body.leftMargin=0.75;
				document.body.rightMargin=0.75;
				document.body.topMargin=0.75;
				document.body.bottomMargin=0.75;
			}
			function buttonPrint()
			{
			    document.getElementById('fromBean').value ="print";
			    var hide1; 
   				hide1 = document.getElementById("tbl-header1");
				hide1.style.display = "none";	
				document.forms[0].submit();
			  	if(window.print)
			  	{
			  		window.print();
				}
			}
			
			//based on key input displays the matching list of glcodes
			function autocompletecode(obj)
			{
				// set position of dropdown
				var src = obj;
				var target = document.getElementById('codescontainer');
				var posSrc=findPos(src);
				target.style.left=posSrc[0];
				target.style.top=posSrc[1]+25;
				if(obj.name=='code') 
					target.style.left=posSrc[0]+100;
			
				target.style.width=500;
			
				var currRow=PageManager.DataService.getRow(obj);
				var coaCodeObj = obj;//PageManager.DataService.getControlInBranch(currRow,'chartOfAccounts_glCodeearning');
				//40  Down arrow, 38  Up arrow
				if(event.keyCode != 40 )
				{
					if(event.keyCode != 38 )
					{
						var oAutoComp = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', codeObj);
						oAutoComp.queryDelay = 0;
						oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
						oAutoComp.useShadow = true;
					}
				}
				
			}

			//fills the related neighboor object after splitting
			 function fillNeibrAfterSplit(obj,neibrObjName)
			 {
				if(obj.value=='')
				{
					document.getElementById('conSupName').value='';
					document.getElementById('divId').value='';
				}
				else
				{
					var currRow=PageManager.DataService.getRow(obj);
					neibrObj=PageManager.DataService.getControlInBranch(currRow,neibrObjName);
					var temp = obj.value; 
					temp = temp.split("`-`");
					obj.value=temp[0];
					if(temp.length>1)
					{
						document.getElementById('conSupName').value=temp[1];
						document.getElementById('divId').value=temp[2];
					}
					else
					{
						document.getElementById('conSupName').value='';
						document.getElementById('divId').value='';
					}	
				}
			 }
		</SCRIPT>
	</head>
	
	<body onload="onLoad()">
		<%
			Logger logger = Logger.getLogger("OsStmtForLiabilityExpenses.jsp");
		%>
		<form name="OsStmtForLiabilityExpensesReport" action="OsStmtForLiabilityExpenses.jsp" method="post">
			<input type="hidden" id="fromBean" name="fromBean" value=""/>
			<input type="hidden" name="companyDetail_name" id="companyDetail_name" value="">
			<input type="hidden" name="dummy" id="dummy" value="dummy">
			<center>
				<table align="center" name="table1" id="table1">
					<tr>
						<td>
							<div id="main">
								<div id="m2">
									<div id="m3" style="width:810px" >
										<div class="tbl-header1" id="tbl-header1" >
											<div class="tbl-header2" id="tbl-header2">
												<table width="100%" border=0 cellpadding="3" cellspacing="0">
													<tr>
														<td class="tableheader" colspan="7" valign="center" width="100%"><span id="screenName">STATEMENT OF OUTSTANDING LIABILITY FOR EXPENSES FOR CONTRACTORS/SUPPLIERS</span></td>
													</tr>
													<tr>
														<td align="right"><div  valign="center" class="labelcell">Starting Date</div></td>
														<td><span class="leadon">*</span></td>
														<td align="left" class="smallfieldcell">
															<input name="startDate" id="startDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" exilMustEnter="true">
														</td>
														<td align="right"><div  valign="center" class="labelcell" >End Date</div></td>
														<td><span class="leadon">*</span></td>
														<td class="smallfieldcell">
															<input name="endDate" id="endDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" exilMustEnter="true">
														</td>
													</tr>
													<tr>
														<td align="right"><div  valign="center" class="labelcell">Fund</div></td>
														<td><span class="leadon"></span></td>
														<td class="smallfieldcell" width="5%"><select name="fund" id="fund" class="fieldinput" ></td>
														<td align="right"><div  valign="center" class="labelcell">Type</div></td>
														<td><span class="leadon">*</span></td>
														<td class="smallfieldcell">
															<select class="fieldinput" name="type" id="type" onchange="showCSName(this)">        
        														<option value="0">Both</option>
        														<option value="1">Supplier</option>
        														<option value="2">Contractor</option>
        													</select>
														</td>
													</tr>
													<tr id="conSupNameRow" style="display:none">
														<td align="right"><div  valign="center" class="labelcell"><span id="codeLabel"  class="labelcell"></span></div> </td>
														<td><input type="hidden" id="divId" name="divId" value="" size=10/></td>
														<td class="smallfieldcell"><input class="fieldinput" name="code" id="code" autocomplete="off"   onkeyup="autocompletecode(this);" onblur="fillNeibrAfterSplit(this,'conSupName');setNameOnCodeChange()" ><IMG id=IMG1 onclick="openSearch(this);" height=22 src="/egi/resources/erp2/images/plus1.gif" width=25 align=top border=0></td>
														<div id="codescontainer"></div>
														<td align="right"><div  valign="center" class="labelcell"><span id="conSupNameLabel"  class="labelcell"></span></div> </td>
														<td/>
														<td class="smallfieldcell"><input class="fieldinputlarge" name="conSupName" id="conSupName" style="width=210"></td>
													</tr>
													
												</table>
											</div> <!-- end of tbl-header1 -->
											<div> 
												<table border="0" cellpadding="0" cellspacing="0" align="center"> 
												<br>
													<tr id="row2" name="row2">
														<td colspan="4" align="middle">
															<table border="0" cellpadding="0" cellspacing="0">
																<tr>
																	<td align="right"><input type=button class=button onclick="ButtonPress()" href="#" value="Search"></td>
																	<td align="right"><input type=button class=button onclick=buttonFlush(); href="#" value="Cancel"></td>
																	<td align="right"><input type=button class=button onclick=window.close() href="#" value="Close"></td>
																	<td align="right"><input type=button class=button onclick="buttonPrintPreview()" href="#" value="Print Preview"></td>
																</tr>
															</table>
													 	</td>
													</tr>
													<tr style="DISPLAY: none" id="row3" name="row3">  <!-- for what ? -->
														<td colspan="4" align="middle">
															<table border="0" cellpadding="0" cellspacing="0">
																<tr>
																	<td align="right"><input type=button class=button onclick="history.go(-1)" href="#" value="Back"></td>
																	<td align="right"><input type=button class=button onclick=buttonFlush(); href="#" value="Cancel"></td>
																	<td align="right"><input type=button class=button onclick=window.close() href="#" value="Close"></td>
																	<td align="right"><input type=button class=button onclick="pageSetup();buttonPrint()" href="#" value="Print"></td>
																</tr>
		 													</table>
		 												</td>
													</tr>
												</table>
											</div>
										</div>	<!-- end of tbl-header1 -->
									</div> <!-- end of m3 -->
								</div> <!-- end of m2 -->
							</div> <!-- end of main -->
						</td>
					</tr>
				</table>
				<!--  after submitting the form -->
				
				<%
					try
					{
						if(request.getParameter("fromBean")!=null)
						{
						
							OsStmtForLiabilityExpenses osStmt = new OsStmtForLiabilityExpenses();
							List osStmtList = new ArrayList(); 
							HashMap returnMap = osStmt.getReport(request.getParameter("asOnDate"),request.getParameter("type"),request.getParameter("divId"),request.getParameter("startDate"),request.getParameter("endDate"),request.getParameter("fund"));
							osStmtList = (List)returnMap.get("osStmtList");
							request.setAttribute("osStmtReport",osStmtList);
							String compName = request.getParameter("companyDetail_name");
							if(request.getParameter("fromBean").equals("submit"))
							{
								%>
									<u><b><div class = "normaltext" align="right" style="width:810px">Form GEN-28</div></b></u>
									<center><u><b><div class = "normaltext"><%=request.getParameter("companyDetail_name")%></div></b></u></center>
									<center><u><b><div class = "normaltext">STATEMENT OF OUTSTANDING LIABILITY FOR EXPENSES FOR CONTRACTORS/SUPPLIERS </div></b></u></center>
									<u><b><div class = "normaltext" align="left" style="width:760px">Date : <%=currDate%></div></b></u>
									<div class="tbl5-container" id="tbl-container" >
										<display:table cellspacing="0" name="osStmtReport" export="true"  sort="list" class="its" id="osStmtReport" style="width:850px">
										
										<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										Form GEN-28&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<%=compName%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										STATEMENT OF OUTSTANDING LIABILITY FOR EXPENSES
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										FOR CONTRACTORS/SUPPLIERS &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										Date : <%=currDate%>
										</display:caption>
										<display:caption media="excel">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										Form GEN-28&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<%=compName%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										STATEMENT OF OUTSTANDING LIABILITY FOR EXPENSES
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										FOR CONTRACTORS/SUPPLIERS &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										Date : <%=currDate%>
										</display:caption>
		
										<div STYLE="display:table-header-group;width:810px;">
											<display:column property="srlNo" title="Sr. No." style="width:40px" />
											<display:column property="csName" title="Name of the Supplier/ Contractor" style="width:150px" />
											<display:column property="natureOfPayable" title="Nature of Payable" style="width:130px" />
											<display:column property="billDate" title="Date of Bill" style="width:120px;white-space: nowrap;width:4%;" />
											<display:column  title="Bill No./Voucher No." style="width:120px" >
											<%
												HashMap rowMap = (HashMap)pageContext.getAttribute("osStmtReport");
												String cgn=rowMap.get("cgn").toString();
												String link="";
												String deployment=EGovConfig.getProperty("egf_config.xml", "DEPLOYMENT","", "general");
												
												if(cgn.length()>0)
									     		{
													if(deployment.equalsIgnoreCase("NN"))	
								     					link="javascript:callme('"+cgn+"','"+cgn.substring(0,3)+"')";
								     				else 
	     												link="javascript:callmeAP('"+cgn+"','"+cgn.substring(0,3)+"')";
									     		}
												//link="javascript:callmeAP('"+cgn+"','"+cgn.substring(0,3)+"')";
												String url="";
												try { url = rowMap.get("billNumber").toString(); }catch(Exception e){url="&nbsp;";}
											%>
											<a href="<%=link%>"><%=url%></a>
											</display:column>
											<display:column property="codeOfAccount" title="Account Code" style="width:150px" />
											<display:column property="billAmount" title="Outstanding Amount (Rs.)" style="width:140px;text-align:right" />
											<display:column property="fund" title="In respect of Grant/ Special Fund" style="width:200px" />
											<display:column property="remarks" title="Remarks" style="width:100px;border-right:2px solid #000000" />
											
											<display:setProperty name="export.pdf" value="true" />
											<display:setProperty name="export.pdf.filename" value="OsStmtForLiabilityExpReport.pdf" /> 
											<display:setProperty name="export.csv" value="false" />
										    <display:setProperty name="export.xml" value="false" />
										    <display:setProperty name="export.excel" value="true" />
										    <display:setProperty name="export.excel.filename" value="OsStmtForLiabilityExpReport.xls"/>
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
							else if(request.getParameter("fromBean").equals("print")) // print preview
							{
								%>
								<u><b><div class = "normaltext" align="right" style="width:810px" >Form GEN-28</div></b></u>
								<center><u><b><div class = "normaltext" ><%=request.getParameter("companyDetail_name")%></div></b></u></center>
								<center><u><b><div class = "normaltext" >STATEMENT OF OUTSTANDING LIABILITY FOR EXPENSES FOR CONTRACTORS/SUPPLIERS</div></b></u></center>
								<u><b><div class = "normaltext" align="left" style="width:760px;" >Date : <%=currDate%></div></b></u>
								<div class="tbl5-container" id="tbl-container">
									<display:table cellspacing="0" name="osStmtReport" uid="currentRowObject" export="false"  sort="list" class="its" id="osStmtReport" style="width:850px">
										<div STYLE="display:table-header-group;width:810px;" >
											<display:column property="srlNo" title="Sr. No." style="width:40px" />
											<display:column property="csName" title="Name of the Supplier/ Contractor" style="width:150px" />
											<display:column property="natureOfPayable" title="Nature of Payable" style="width:130px" />
											<display:column property="billDate" title="Date of Bill" style="width:120px" />
											<display:column property="billNumber" title="Bill No./Voucher No." style="width:120px;white-space: nowrap;width:4%;" />
											<display:column property="codeOfAccount" title="Account Code" style="width:150px" />							
											<display:column property="billAmount" title="Outstanding Amount (Rs.)" style="width:140px;text-align:right" />
											<display:column property="fund" title="In respect of Grant/ Special Fund" style="width:200px" />
											<display:column property="remarks" title="Remarks" style="width:100px;border-right:2px solid #000000" />
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
						} // main if
					}catch(Exception e)
					{
						logger.error("Exception while calling the bean="+e.getMessage());
						
						 %>
						 <script>
						 bootbox.alert("Error :<%=e.getMessage()%>");
						 </script>
						 <%
					}
				%>
			</center>
		</form>
	</body>
</html>
