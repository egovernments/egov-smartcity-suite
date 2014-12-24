<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>
<%@ include file="/staff/egovHeader.jsp" %>
<html>
<head>
<title>Error Page</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<title>WelCome to eGov PTIS</title>
		<script language="JavaScript" src="/dhtml.js" type="text/JavaScript"></script>
		<LINK REL=stylesheet HREF="/egov.css" TYPE="text/css">
		<LINK REL=stylesheet HREF="/ccMenu.css" TYPE="text/css">


</head>

<body>

<!-- Header Section Begins -->

<!-- Header Section Ends -->


<table align='center'>
<tr>
<td>
<!-- Tab Navigation Begins -->
<center>
<jsp:include page="/staff/tabmenu.html" />
</center>
<!-- Tab Navigation Ends -->

<!-- Body Begins -->
<div id="main"><div id="m2"><div id="m3">
<form name="signup" id="signup" method="post" action="#">
<!--img  align=absmiddle src="img/c_top.gif"-->
 <div align="center">
<table align='center' width="400" height="100" style="border: 1px solid #D7E5F2"> 
  
    <tr >
        <td class="labelcell" style="width:650" align="center">         
	<p><font size="2"><b>Password Changed Successfully!!!</b></font></p>
     	</td>
     	</tr>

     </table>
</div>
</form>
<!--img align=absmiddle src="img/c_bot.gif"-->
</div></div></div>
</td></tr>
</table>
</body>
</html>
