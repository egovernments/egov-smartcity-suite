<%@ include file="/includes/taglibs.jsp" %>

<html>
<head>
	<title>View Exception</title>
<%
try{
String msg=(String)request.getAttribute("AlertMessage");

%>

<script language="JavaScript"  type="text/JavaScript">
		var empCodeArray;
		var selectedEmpCode;
		var acctCodeArray;
		var selectedAcctCode;
		var yuiflag = new Array();
		var yuiflag1 = new Array();
		var fromdateOnLoad;
<%
if(msg!=null)
{
%>

var msg="<%=msg%>";
alert(msg);
window.close();
<%
}
%>
  
	function populate()
	{	
		var mon="";
		if('${exception.month}'==1) mon="JAN"; else if('${exception.month}'==2) mon="FEB"; 
		else if('${exception.month}'==3) mon="MAR"; else if('${exception.month}'==4) mon="APR"; 
		else if('${exception.month}'==5) mon="MAY"; else if('${exception.month}'==6) mon="JUN"; 
		else if('${exception.month}'==7) mon="JUL"; else if('${exception.month}'==8) mon="AUG"; 
		else if('${exception.month}'==9) mon="SEP"; else if('${exception.month}'==10) mon="OCT"; 
		else if('${exception.month}'==11) mon="NOV"; else if('${exception.month}'==12) mon="DEC";
		
		document.getElementById("month").value=mon;
	} 
	
</script>
</head>


<body onLoad="populate();">

   <html:form method="POST" action="exception/beforeException" >
   <center>
	<div class="navibarshadowwk"></div>
				<div class="formmainbox">
					<div class="insidecontent">
				  		<div class="rbroundbox2">
							<div class="rbtop2">
								<div>
								</div>
							</div>
							<div class="rbcontent2">
								<div class="datewk" width="220px;">	
								    <span class="bold">Today:</span> <egovtags:now/>
								</div>
							  
								  <table width="100%" cellpadding ="0" cellspacing ="0" border = "0" id="viewExcHdr">
								    
								    <tr>
										<td colspan="4" class="headingwk">
											<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
							               	<div class="headplacer">View Exception</div>
							            </td>
							        </tr>
								    									
									<tr>
										<td class="greyboxwk" >Year</td>
										<td class="greybox2wk" >
											<html:text property="financialYear" readonly="true" value="${exception.financialyear.finYearRange}" />
										</td>
										<td class="greyboxwk" >Month</td>
										<td class="greybox2wk">
											<html:text property="month" styleId="month" disabled="true" value="" />
										</td>		
									</tr>
									<tr>
										<td class="whiteboxwk" >Created On</td>
										<td class="whitebox2wk" colspan="3">
										<input type="text" name="createdOn"  id="createdOn" readonly="true" class="datefieldinput" size="20"  value="<fmt:formatDate value="${exception.expDate}" pattern="dd/MM/yyyy" />" />
										</td>					
									</tr>
									<tr>
										<td class="greyboxwk" >From Date</td>
										<td class="greybox2wk" >
											<input type="text" name="fromDate"  id="fromDate" readonly="true" class="datefieldinput" size="20"  value="<fmt:formatDate value="${exception.fromDate}" pattern="dd/MM/yyyy" />" />
										</td>
										<td class="greyboxwk" >To Date</td>
										<td class="greybox2wk">
										    <input type="text"  name="toDate" id="toDate" readonly="true" class="datefieldinput" size="20"  value="<fmt:formatDate value="${exception.toDate}" pattern="dd/MM/yyyy" />" />
										</td>		
									</tr>
									<tr>
										<td class="labelcell" colspan="4">&nbsp;</td>
									</tr>
								 <table width="100%"  cellpadding ="0" cellspacing ="0" border = "0" id="viewExcDtl">
								  <tr>
										<td colspan="5" class="headingwk">
											<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
							               	<div class="headplacer">Detail</div>
							            </td>
							        </tr>
								 
								 <tr>
								      <td class="dtlTblHeader">Code</td>
								      <td class="dtlTblHeader">Name</td>
								      <td class="dtlTblHeader">Action</td>
								      <td class="dtlTblHeader">Reason</td>
								      <td class="dtlTblHeader">Comments</td>
								  </tr>
								  <tr id="viewexdtl">
								   	 <td class="greybox2wk">
								   	 	<c:out value="${exception.employee.employeeCode}"/>
								     </td>
								   	 <td class="greybox2wk">
								   	 	<c:out value="${exception.employee.employeeFirstName}"/>
								  	 </td>
								   	 <td class="greybox2wk">
								  		<c:out value="${exception.exceptionMstr.type}"/>
								   	 </td>
								   	 <td class="greybox2wk">
								  		<c:out value="${exception.exceptionMstr.reason}"/>
								  	 </td>
								   	 <td class="greybox2wk">
								      	<c:out value="${exception.comments}"/>
								     </td>
								  </tr>
								 </table>   
							 </div>
						</table>
					</div>
				</div>
			</div>
		</div>
		
		<div class="buttonholderwk">
			<html:button property="actionType" value="Close" onclick="window.close();"/>		    		
		</div>	
		<div class="urlwk">City Administration System Designed and Implemented by <a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved </div>
	</center>
</html:form>

<%
}catch(Exception e)
{
e.printStackTrace();
}
%>
</body>
</html>
