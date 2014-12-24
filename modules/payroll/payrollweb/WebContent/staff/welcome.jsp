<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>

<%@ page import="javax.servlet.http.HttpServletRequest,
				javax.servlet.http.HttpSession"
%>

<html>
<head>
<title>Admin Form</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<title>WelCome to eGov PTIS</title>
		<LINK REL=stylesheet HREF="/ptisnn/egov.css" TYPE="text/css">
		<LINK REL=stylesheet HREF="/ptisnn/ccMenu.css" TYPE="text/css">
</head>
<html:form action="/admin/CreateExemption">
<body>

<%
	String headerstr = (String)session.getAttribute("headerlabel");
	System.out.println(">>>>>>>>>>>headerstr = "+headerstr);

	String success_str = (String)session.getAttribute("successtr");
	System.out.println(">>>>>>>>>>> success_str ="+ success_str);


%>

<!-- Header Section Begins -->
<%@ include file="/staff/egovHeader.jsp" %>
<!-- Header Section Ends -->


<table align='center' id="signup">

<tr>
<td>



<div id="main"><div id="m2"><div id="m3">
<div align="center">

<table align='center' width="600" height="83" border="1">
<tr>
 <td class="tableheader" colspan="2" align='center' height="20">
 <b><font face="Verdana" color="#000080" size="4">Welcome to Admins</td>
</tr>

</table>

</div>
</div></div></div>
</td></tr>
</table>
</body>
</html:form>
</html>