<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="java.util.*,org.egov.lib.rjbac.dept.Department,org.egov.pims.commons.DesignationMaster,org.egov.pims.model.PersonalInformation"
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Employee Summary By Status Report</title>
	<link href="../common/css/eispayroll.css" rel="stylesheet" type="text/css" />
	<link href="../common/css/commonegov.css" rel="stylesheet" type="text/css" />


<script language="JavaScript"  type="text/JavaScript">
<%
 Department department=null;
DesignationMaster designation=null;
%>
function validation()
{
var statusLabel=document.getElementById('status').options[document.getElementById('status').selectedIndex].text;
 if(document.getElementById("status").value=="0")
	 {
	 	alert("Please Select Status ");
	 	return false;
	 }
	if(statusLabel=='Employed' || statusLabel=='Suspended')
	{
	document.searchForm.toDate.value=document.getElementById("asofDate").value;
		if(document.searchForm.toDate.value=="")
		{
			alert("Please Enter  To Date ");
		 	return false;
		}
		else
		{
		document.getElementById("fromDate").value=document.searchForm.toDate.value;
		}
	}
	else
	{
		 if(document.getElementById("fromDate").value==null || document.getElementById("toDate").value==null || document.getElementById("toDate").value=="" 	|| document.getElementById("fromDate").value=="")
		 {
		 	alert("Please Enter From and To Date ");
		 	return false;
		 }
	}
	
   
  	return true;
}

function loadField()
{
	<c:if test="${not empty alertMessage }">
	alert("${alertMessage}");
	
	</c:if>
	document.getElementById('status').value="${searchForm.status}";
	showDateOnStatus();
	document.getElementById('asofDate').value="${searchForm.toDate}";
	document.getElementById('fromDate').value="${searchForm.fromDate}";
	document.getElementById('toDate').value="${searchForm.toDate}";
}

function showDateOnStatus()
{
	document.getElementById('asofDate').value="";
	document.getElementById('fromDate').value="";
	document.getElementById('toDate').value="";
	var statusLabel=document.getElementById('status').options[document.getElementById('status').selectedIndex].text;
	if(statusLabel=='Employed' || statusLabel=='Suspended')
	{
	document.getElementById('dateRangeRow').style.visibility="hidden";
	document.getElementById('asofDateRow').style.visibility="visible";
	
	}
	else
	{
	document.getElementById('dateRangeRow').style.visibility="visible";
	document.getElementById('asofDateRow').style.visibility="hidden";
	}
}
														
</script>   
</head>

<body onLoad="loadField();">

	<html:form   action ="/pims/AfterSearchAction.do?submitType=executeSearchByStatus" onsubmit="return validation();">
		<div class="formmainbox">
			<div class="insidecontent">
		  		<div class="rbroundbox2">
					<div class="rbtop2">
						<div></div>
					</div>
					<div class="rbcontent2">

						<!-- Header Section Begins -->
						<!-- Header Section Ends -->
						<table width="95%" cellpadding ="0" cellspacing ="0" border = "0" id="table2">
							<tr>
								<td>
									<!-- Tab Navigation Begins -->
									<!-- Tab Navigation Ends -->
									<!-- Body Begins -->
									<!-- Body Begins -->
									<div>
										<center>
											<table width="100%" cellpadding ="0" cellspacing ="0" border = "0"  >
												<tbody>
													<tr><td>&nbsp;</td></tr>
													<tr>
			  											<td colspan="8" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
															<p>
															<div class="headplacer">Search employee by Status and date range</div>
															</p>
														</td>
			  										</tr>
			  										<tr><td>&nbsp;</td></tr>
			  										<tr>
														<td  class="whiteboxwk"><span class="mandatory">*</span>Status</td>
														<td  class="whitebox2wk">
															<html:select tabindex="1" styleId="status" property="status" styleClass="dropdownsize" onchange="showDateOnStatus()">
																<html:option value="0" ><bean:message key="chooseType"/></html:option>
																<html:options collection="statusMasterList" property="id" labelProperty="description"/>
															</html:select>
														</td>
														
														<td class="whiteboxwk" colspan="2"/>
													</tr>
													<tr id="dateRangeRow">
								        				<td class="greyboxwk" id="fromDateLabel"><span class="mandatory">*</span>From Date (dd/MM/yyyy)</td>
														<td class="greybox2wk" id="fromDateTxt">
															<input type="text" id="fromDate" maxlength="10" name="fromDate" class="selectwk"   onblur = "validateDateFormat(this);" size="10" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" ></input>
															<a href="javascript:show_calendar('forms[0].fromDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border=0></a>
														</td>
												
								          				<td class="greyboxwk"><span class="mandatory">*</span>To Date (dd/MM/yyyy)</td>
														<td class="greybox2wk">
															<input type="text" id="toDate" maxlength="10" name="toDate" class="selectwk"   onblur = "validateDateFormat(this);" size="10" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" ></input>
															<a href="javascript:show_calendar('forms[0].toDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border=0></a>
														</td>
													</tr>
													
													<tr id="asofDateRow"  >
								        				
												
								          				<td class="greyboxwk"><span class="mandatory">*</span>To Date (dd/MM/yyyy)</td>
														<td class="greybox2wk" colspan="3">
															<input type="text" name="asofDate" maxlength="10" id="asofDate" class="selectwk"   onblur = "validateDateFormat(this);" size="10" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" ></input>
															<a href="javascript:show_calendar('forms[0].asofDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border=0></a>
														</td>
													</tr>
													

												
												</table>

											<br>
												
												<c:if test="${not empty employeeList }">
												
												<br>
										  			<table align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
														<tr>
															<td class="headingwk" colspan="5">
														  		<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
														  		<div align="left" style="margin-top:4px;">Employee Details</div>
														  	</td>
														</tr>
													</table>
													
										 			<display:table name="employeeList" id="currentRowObject" cellspacing="0" style="width: 750;" 
										 			requestURI="${pageContext.request.contextPath}/pims/AfterSearchAction.do?submitType=executeSearchByStatus"
										  				export="true" defaultsort="2" pagesize = "10" sort="list"  class="its">
													<br>
														<display:caption style="text-align: center">Employees by Status and Date</display:caption>
														
														
														<display:column style="tablesubheadwk:10%"   property="employeeCode" title="Employee Code" />
												 		<display:column style="tablesubheadwk:5%"   property="employeeName" title="Employee Name" />
												 		 
												 		
												 		<%
												 		try{
												 		 Date toDate= (Date)request.getAttribute("toDate") ;
												 		 department=((PersonalInformation)pageContext.getAttribute("currentRowObject")).getAssignment(toDate).getDeptId();
												 		 designation=((PersonalInformation)pageContext.getAttribute("currentRowObject")).getAssignment(toDate).getDesigId();
												 		}
												 		catch(Exception e)
												 		{
												 		System.out.println(e.getMessage());
												 	}	
												 		%>
												 		
												 		<display:column title="Employee Designation" >
												 		<%=designation==null?"N/A":designation.getDesignationName()%>
												 		</display:column>
												 		
												 		<display:column title="Employee Department" >
												 		<%=department==null?"":department.getDeptName()%>
												 		</display:column>
												 		
												 		
												 		
														<display:column style="width:5%" title="Reports" media="html">
															<%
															String iId =(String)((PersonalInformation)pageContext.findAttribute("currentRowObject")).getIdPersonalInformation().toString();
															%>
												 			<a  href="${pageContext.request.contextPath}/pims/AfterSearchAction.do?submitType=executeReportSearch&Id=<%=iId%>" target="_blank"><FONT class="labelcell">View Employee</FONT></a>
														</display:column>
														
														<div STYLE="display:table-header-group;">			      
													  		<display:setProperty name="paging.banner.placement" value="bottom" />
													  		<display:setProperty name="basic.show.header" value="true" />
															<display:setProperty name="export.pdf.filename" value="searchEmployeeByStatusRep.pdf" />
															<display:setProperty name="export.excel.filename" value="searchEmployeeByStatusRep.xls"/>
															<display:setProperty name="export.csv" value="false" />
															<display:setProperty name="export.xml" value="false" />
															
														</div>
														
													</display:table>
													
												</c:if>
											</table>
											<br>
										</center>
									</div>
								</td>
							</tr>
							<tr><td>&nbsp;</td></tr>
							<tr>
           						<td><div align="right" class="mandatory">* Mandatory Fields</div></td>
         					</tr>
						</table>
						
			<div class="rbbot2"><div></div></div>
		</div>
</div></div>

		
		
		
		<div><html:submit styleClass="buttonfinal" value="Submit" property="b4"  />
  		 <input type="button" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close();"/></div>
	</html:form>
	
</body>
</html>