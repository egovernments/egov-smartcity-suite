<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="org.egov.payroll.dao.*,java.util.*,org.egov.infstr.*,org.egov.infstr.commons.dao.*,org.egov.infstr.commons.* "%>

<html>
<head>
	<title>Approval/Rejection Page</title>
<script language="JavaScript" type="text/JavaScript">
</script>
</head>

<body>	
	<table style="width: 750;" colspan="6" cellpadding="0" cellspacing="0"	border="1" id="paytable">
		<tr>
			<td colspan="6" height="20" bgcolor=#bbbbbb align=middle class="tablesubcaption">
			<B>Created bill(s)</B>
			</td>			
		</tr>
		<c:forEach var="billObj" items="${billRegisters}">
		<tr>
			<td>				
				<c:out value="${billObj.billnumber}"/>
			</td>			
		</tr>
		</c:forEach>
		
	</table>	

</body>
</html>
