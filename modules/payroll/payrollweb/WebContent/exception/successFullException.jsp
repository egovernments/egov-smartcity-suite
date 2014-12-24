<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="org.egov.payroll.dao.*,java.util.*,org.egov.infstr.*,org.egov.infstr.commons.dao.*,org.egov.infstr.commons.*,java.text.SimpleDateFormat "%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>eGov EIS Payroll</title>
	<script language="JavaScript"  type="text/JavaScript">
	
</script>

<script>
	function refreshInbox()
	{
	
		if(opener.top.document.getElementById('inboxframe')!=null)
		{    	
			if(opener.top.document.getElementById('inboxframe').contentWindow.name!=null && opener.top.document.getElementById('inboxframe').contentWindow.name=="inboxframe")
			{    		
			opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
			}
		
		}
	}

</script>
</head>
<%
java.util.Date date = new java.util.Date();
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
%>
<body onload="refreshInbox();">
		
<div class="navibarshadowwk"></div>
<div class="formmainbox"><div class="insidecontent">
<div class="rbroundbox2">
<div class="rbtop2"><div></div></div>
<div class="rbcontent2">
<div class="datewk"><span class="bold">Today </span><%=sdf.format(date)%></div>


<table width="100%" border="0" cellspacing="0" cellpadding="0">

<tr>
	<td colspan="6"  class="success">				
	Employee Exception created successfully..

	</td>
</tr>
 <tr>
  <td>
<table style="width:800px" align="center" colspan="6" cellpadding ="0" cellspacing ="0" border = "0" id="paytable">
	<tr>
    <td colspan="5" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
      <div class="headplacer">Employee  Exception</div></td>
    </tr>
	
	
	 <tr>
	      <td class="tablesubheadwk">Code</td>
	      <td class="tablesubheadwk">Name</td>
	      <td class="tablesubheadwk">Action</td>
	      <td class="tablesubheadwk">Reason</td>
	      <td class="tablesubheadwk">Comments</td>
	  </tr>
	  <c:forEach var="exceptionObj" items="${exceptions}">
	  <tr id="earnings">
	   	 <td class="whitebox5wk">
	   	 	<c:out value="${exceptionObj.employee.employeeCode}"/>
	     </td>
	   	 <td class="whitebox5wk">
	   	 	<c:out value="${exceptionObj.employee.employeeFirstName}"/>&nbsp; 
		  		<c:if test="${exceptionObj.employee.employeeMiddleName != null}">
		  			<c:out value="${exceptionObj.employee.employeeMiddleName}"/>&nbsp; 
		  		</c:if>	
	  		<c:out value="${exceptionObj.employee.employeeLastName}"/>
	  	 </td>
	   	 <td class="whitebox5wk">
	  		<c:out value="${exceptionObj.exceptionMstr.type}"/>
	   	 </td>
	   	 <td class="whitebox5wk">
	  		<c:out value="${exceptionObj.exceptionMstr.reason}"/>
	  	 </td>
	   	 <td class="whitebox5wk">
	      	<c:out value="${exceptionObj.comments}"/>&nbsp;
	     </td>
	  </tr>
	  </c:forEach>
	   

	  
 </table> 
  </td>
 </tr>
  <tr>
                 <td class="shadowwk"></td>
               </tr>

 </table>
 </div>
<div class="rbbot2"><div></div></div>
</div>
</div>
</div>

<div class="buttonholderwk"><html:button property="close"  styleId="button"  styleClass="buttonfinal"  onclick="window.close()"  ><bean:message key="close"/></html:button>
</div>
<div class="urlwk">City Administration System Designed and Implemented by <a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved </div>



</body>
</html>
