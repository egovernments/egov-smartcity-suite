<!--
	Report Name : Provident Fund Report
	Author		: Ilayaraja P
	Created	on	: 28-May-2008
	Purpose 	: 
 -->
<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@include file="/includes/taglibs.jsp" %>
<%@ page buffer = "16kb" %>
<%@page import="java.util.Date,java.util.List,java.util.ArrayList,java.text.SimpleDateFormat,org.apache.log4j.Logger,org.egov.payroll.reports.PFReport" %>

<html>
	<head>
		<title>Provident Fund Report</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<meta http-equiv="KEYWORDS" content="">
		<meta http-equiv="DESCRIPTION" content="">
		<META http-equiv="pragma" content="no-cache">

		<style type="text/css">
			.yui-ac-input {
			height:17px;
			position:absolute;
			width:50px;
			}
		</style>	

		<SCRIPT LANGUAGE="javascript">
		<%
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date currentDate = new Date();
			String currDate = sdf.format(currentDate);
			
		%>
			
			function onLoad()
			{
				//PageValidator.addCalendars();
				var fromBean = '<%=request.getParameter("fromBean")%>';

				if(fromBean=='submit' || fromBean=='print')
				{
					if(fromBean=='print')
					{
						document.getElementById("tbl-header2").style.display="block";
						document.getElementById("row3").style.display="block";
						
					}
					document.getElementById('empId').value='<%=request.getParameter("empId")%>'
					document.getElementById('code').value='<%=request.getParameter("code")%>'
					document.getElementById('name').value='<%=request.getParameter("name")%>'
					document.getElementById('fromDate').value='<%=request.getParameter("fromDate")%>'
					document.getElementById('toDate').value='<%=request.getParameter("toDate")%>'
					document.getElementById('fromBean').value='<%=request.getParameter("fromBean")%>'
				}
				else
				{
					if(document.getElementById("fromDate").value=="")
						getFinStartDate();
					if(document.getElementById("toDate").value=="")
						document.getElementById("toDate").value="<%=currDate%>";
				}					
			}
			function beforeRefreshPage(dc)
			{
				if(dc.values['serviceID']=="finYearDate")
				{
					var dt=dc.values['startFinDate'];
					dt=formatDate2(dt);
					document.getElementById("fromDate").value=dt;
				}
			}
			
			function getFinStartDate()
			{
				var url2 = "${pageContext.request.contextPath}/commons/process.jsp?type=getFinancialYearStartDate";
		        var req3 = initiateRequest();
		        req3.onreadystatechange = function()
		        {
		              if (req3.readyState == 4)
		              {
		                  if (req3.status == 200)
		                  {
		                  	var codes2=req3.responseText;
							var a = codes2.split("^");
							document.getElementById('fromDate').value=a[0].substring(8,10)+"/"+a[0].substring(5,7)+"/"+a[0].substring(0,4);
		                  }
		              }
		         };
		         req3.open("GET", url2, true);
		         req3.send(null);
			}
			
			function validateFields()
			{
				var prcolor = document.getElementById('code').style.backgroundColor;
				if(document.getElementById('code').value=='')
				{
					document.getElementById('code').style.backgroundColor = 'red';
					alert('Employee code cannot be empty');
					document.getElementById('code').style.backgroundColor = prcolor;
					document.getElementById('code').focus();
					return false;
				}
				if(document.getElementById('fromDate').value=='')
				{
					document.getElementById('fromDate').style.backgroundColor = 'red';
					alert('From Date cannot be empty');
					document.getElementById('fromDate').style.backgroundColor = prcolor;
					document.getElementById('fromDate').focus();
					return false;
				}
				if(document.getElementById('toDate').value=='')
				{
					document.getElementById('toDate').style.backgroundColor = 'red';
					alert('To Date cannot be empty');
					document.getElementById('toDate').style.backgroundColor = prcolor;
					document.getElementById('toDate').focus();
					return false;
				}
				var fromDate = document.getElementById('fromDate').value;
				var toDate = document.getElementById('toDate').value;
				if(compareDate(fromDate,toDate) == -1 )
				{
					var prevcolor = document.getElementById('toDate').style.backgroundColor;
					document.getElementById('toDate').style.backgroundColor = 'red';
					alert('From Date cannot be greater than To Date');
					document.getElementById('toDate').style.backgroundColor = prevcolor;
					document.getElementById('toDate').select();
					return false;
				}
				return true;
			}
			
			function ButtonPress()
			{
				if(validateFields())
				{
					document.getElementById("fromBean").value="submit";
					document.forms[0].submit();
				}
			}
			function buttonFlush()
			{
				window.location="PFReport.jsp";
			}
			//For Print Preview method
			function buttonPrintPreview()
			{
				if(validateFields())
				{
					document.getElementById('fromBean').value ="print";
					document.forms[0].submit();
				}
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

			function showCalendar(objId)
			{
				show_calendar("getElementById('"+objId+"')");
			}
			 
			var codeSelectionHandler = function(sType, arguments)
		    { 
		        var oData = arguments[2];
			 	var empDetails = oData[0];
			 	var empId = oData[1];
			 	var empCode = empDetails.split(EMPCODE_SEP)[0];
			 	var empName = empDetails.split(EMPCODE_SEP)[1];
			 	dom.get("empId").value = empId;
			 	dom.get("code").value = empCode;
			 	dom.get("name").value = empName;	 	
			 	name=empName;
			 	code=empCode;
		 	}
		    var codeSelectionEnforceHandler = function(sType, arguments) {
		      		warn('impropercodeSelection');
		  	}
		  	
		  	function getReport() {
		  		<c:choose>
		  		<c:when test="${ess  == 1 or (not empty param.ess)}" >
		  		document.form.action.value = "selfService";
		  		</c:when>
		  		<c:otherwise>
		  		document.form.action.value = "PFReport.jsp";
		  		</c:otherwise>
		  		</c:choose>
		  	}
			 
		</SCRIPT>
	</head>
	
	<body onload="onLoad()">
		<%
			Logger logger = Logger.getLogger("Provident Fund.jsp");
			java.util.Date date = new java.util.Date();

		%>
		<form name="PfReport"  onsubmit="return getReport();" method="post">
			
			
								
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td></td>
									</tr>
									
									<tr>
										<td>
											<input type="hidden" id="fromBean" name="fromBean" value=""/>
											<input type="hidden" id="empId" name="empId" value="${empId}"/>
										</td>
									</tr>

									<tr>
										<td>
											<div class="tbl-header1" id="tbl-header1" >
												<div class="tbl-header2" id="tbl-header2">
													<table width="100%" border="0" cellspacing="0" cellpadding="0">
														<tr>
															<td colspan="7" class="headingwk"><div class="arrowiconwk">
																<img src="${pageContext.request.contextPath}/common/image/arrow.gif" /></div>
																<div class="headplacer">Provident Fund Report</div>
																
															</td>
														</tr>
														
														<c:choose>
														<c:when test="${ess  == 1 or (not empty param.ess)}">
										
														<tr>
															<td class="whiteboxwk"><span class="mandatory">*</span>Employee Code</td>
															<td  class="whitebox2wk" width="20%" valign="top" >  	
													  		<c:out value="${empCode !=null ? empCode : param.code }" />	
															<input type="hidden" id="code" name="code" value="${empCode !=null ? empCode : param.code }"/>	
															<input type="hidden" id="ess" name="ess" value="${ess != null ? ess : param.ess}"/>	
															<input type="hidden" id="reportType" name="reportType" value="${reportType !=null ? reportType : param.reportType }"/>	
							
															</td>
															<td class="whiteboxwk">Name</td>
															<td class="whitebox2wk" >
																<c:out value="${employeeName != null ? employeeName : param.name}" />
																<input type="hidden" id="name" name="name" value="${employeeName != null ? employeeName : param.name}"/>
															</td>						
														</tr>
														
														
														
														</c:when>
														<c:otherwise>
										
														<tr>
															<td class="whiteboxwk"><span class="mandatory">*</span>Employee Code</td>
															<td  class="greybox2wk" width="20%" valign="top" >  	
													  			<div class="yui-skin-sam">
															    	<div id="empSearch_autocomplete" class="yui-ac" >
																    	<input type="text" id="code" name="code" onBlur="checkValidEmpCode(this);" size="10"   class="selectwk"/> 	    
																   	    <div id="codeSearchResults"></div> 
														    		</div>
																</div>
																<egovtags:autocomplete name="code"  field="code" 
															   	    	url="${pageContext.request.contextPath}/common/employeeSearch!getEmpListByEmpCodeLike.action" queryQuestionMark="true"  results="codeSearchResults" 
															   	    	handler="codeSelectionHandler" forceSelectionHandler="codeSelectionEnforceHandler"/>
															   	 <span class='warning' id="impropercodeSelectionWarning"></span>
															</td>
															<td class="whiteboxwk">Name</td>
															<td class="whitebox2wk" >
																<input name="name" id="name" 	class="selectwk"  readonly="true"></td>
															</td>						
														</tr>
														</c:otherwise>
														</c:choose>
														
														<tr>
															<td class="greyboxwk"><span class="mandatory">*</span>From Date</td>
															<td class="greybox2wk" >
																<input name="fromDate" id="fromDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur = "validateDateFormat(this);" >
																<a href="javascript:show_calendar('forms[0].fromDate');"
																	onmouseover="window.status='Date Picker';return true;" 
																	onmouseout="window.status='';return true;">
																	<img src="<%=request.getContextPath()%>/common/image/calendar.png"  border="0">
																</a>
															</td>
															<td class="greyboxwk"><span class="mandatory">*</span>To Date</td>
															<td class="greybox2wk">
																<input name="toDate" id="toDate" class="datefieldinput" 
																	onkeyup="DateFormat(this,this.value,event,false,'3')" onblur = "validateDateFormat(this);">
																<a href="javascript:show_calendar('forms[0].toDate');"
																	onmouseover="window.status='Date Picker';return true;" 
																	onmouseout="window.status='';return true;">
																	<img src="<%=request.getContextPath()%>/common/image/calendar.png"  border="0">
																</a>
															</td>						
														</tr>
													</table>
												</div> <!-- end of tbl-header1 -->
												<div> 
													<center>
														<table border="0" cellpadding="0" cellspacing="0" width=""> 
															<br>
															<tr id="row2" name="row2">
																<td colspan="4" align="middle">
																	<table border="0" cellpadding="0" cellspacing="0">
																		<tr>
																			<td colspan="4" >
				                                            					<div class="buttonholderwk">
																					<input type="button" class="buttonfinal" onclick="ButtonPress()" href="#" value="Search">
																				</div>
																			</td>
																			<td colspan="4" >
				                                            					<div class="buttonholderwk">
																					<input type="button" class="buttonfinal" onclick="buttonFlush();" href="#" value="Cancel">
																				</div>
																			</td>
																			<td colspan="4" >
				                                            					<div class="buttonholderwk">
																					<!-- <input type="button" class="buttonfinal" onclick="buttonPrintPreview()" href="#" value="Print Preview"> -->
																				</div>
																			</td>
																		</tr>
																	</table>
											 					</td>
															</tr>
															
															<tr style="DISPLAY: none" id="row3" name="row3">  
																<td colspan="4" align="middle">
																	<table border="0" cellpadding="0" cellspacing="0">
																		<tr>
																			<input type="button" class="buttonfinal" onclick="history.go(-1)" href="#" value="Back"></td>
																			<td align="right">
																			<input type="button" class="buttonfinal" onclick="buttonFlush();" href="#" value="Cancel"></td>
																			<td align="right">
																			<input type="button" class="buttonfinal" onclick="pageSetup();buttonPrint()" href="#" value="Print"></td>
																		</tr>
																	</table>
																</td>
															</tr>
														</table>
													</center>
												</div>
											</div>	<!-- end of tbl-header1 -->
										</td>
									</tr>
								</table>
								
								<c:set var="displayTagRequestURI" value="${pageContext.request.contextPath}/reports/PFReport.jsp" />
														
								<c:if test="${ess  == 1 or (not empty param.ess)}" >
								<c:set var="displayTagRequestURI" value="${pageContext.request.contextPath}/reports/selfService.do" />
								</c:if>
				
								<!--  after submitting the form -->
								<%
									
									if(request.getParameter("fromBean")!=null)
									{
										PFReport pfReport = new PFReport();
										List pfReportList = new ArrayList();  
										pfReportList = pfReport.getPFReport(request.getParameter("empId"),request.getParameter("code"),request.getParameter("fromDate"),request.getParameter("toDate"));
										request.setAttribute("pfReportObj",pfReportList);
										if(request.getParameter("fromBean").equals("submit"))
										{
											%>
										<div class="aligncenter">
										<display:table cellspacing="0" name="pfReportObj" id="currentRowObject" export="true"  sort="list"  class="its" 
											requestURI="${displayTagRequestURI}" style="background-color:#e8edf1;width:98%;height:40px;padding:0px;margin:10 0 0 5px;">
														
														<display:caption style="background-color:#fffff1;text-align:center;font-weight:bold">															
															PROVIDENT FUND REPORT - <%=request.getParameter("name")%> &nbsp;
															 Date Range: <%=request.getParameter("fromDate")%> to <%=request.getParameter("toDate")%>
														</display:caption>
														
														<div STYLE="display:table-header-group;">
															<display:column property="date" title="Date" style="width:70px" />
															<display:column property="type" title="Type" style="width:140px" />
															<display:column property="deductions" title="Deductions (Debit)" style="width:80px;text-align:right" />
															<display:column property="earned" title="Earned (Credit)" style="width:80px;text-align:right" />
															<display:column property="balance" title="Balance" style="width:90px;text-align:right;" />

															<display:setProperty name="export.pdf" value="true" />
															<display:setProperty name="export.pdf.filename" value="PFReport.pdf" /> 
															<display:setProperty name="export.csv" value="false" />
														    <display:setProperty name="export.xml" value="false" />
														    <display:setProperty name="export.excel" value="true" />
														    <display:setProperty name="export.excel.filename" value="PFReport.xls"/>
														</div>
														<display:footer>
															<tr>
																<td style="border-left: solid 0px #000000" colspan="10"><div style="border-top: solid 1px #000000 ">&nbsp;</div></td>
															</tr>
														</display:footer>
										 </display:table>
													
												
											<%
										}
										
																			
									} // main if
									
								%>
						</div>
						
			
		
		</form>
	</body>
</html>
