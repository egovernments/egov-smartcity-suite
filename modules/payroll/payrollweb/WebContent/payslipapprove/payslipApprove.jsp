<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="org.egov.payroll.dao.*,java.util.*,org.egov.infstr.*,org.egov.infstr.commons.dao.*,org.egov.infstr.commons.* "%>

<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>


<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Payslip approve </title>
	<LINK REL=stylesheet HREF="/css/egov.css" TYPE="text/css">
	 <LINK REL=stylesheet HREF="/ccMenu.css" TYPE="text/css">
	<script type="text/JavaScript" src="/javascript/calender.js"></script>
	<script language="JavaScript" src="/dhtml.js" type="text/JavaScript"></script>
	<script language="JavaScript" src="/javascript/SASvalidation.js" type="text/JavaScript"></script>
	<script language="JavaScript" src="/javascript/dateValidation.js" type="text/JavaScript"></script>
	<script language="JavaScript"  type="text/JavaScript">

	function displayPayslips(){
  		ifrm = document.createElement("IFRAME");
		ifrm.setAttribute("src", "payslipApproveCriteria.jsp");
		ifrm.setAttribute("name", "displaytable");
		ifrm.setAttribute("id", "displaytable");
		ifrm.setAttribute("align", "center");		
		ifrm.frameBorder = 0;
		ifrm.style.width = 1002+"px";
		ifrm.style.height = 385+"px";
		ifrm.style.position = "absolute";
		ifrm.style.left = 0+"px";
		document.body.appendChild(ifrm);
  	}
	function displayBudget(){
  		ifrm = document.createElement("IFRAME");
		ifrm.setAttribute("src", "budget.jsp");
		ifrm.setAttribute("name", "displaytable");
		ifrm.setAttribute("id", "displaytable");
		ifrm.setAttribute("align", "bottom");
		ifrm.frameBorder = 0;
		ifrm.style.width = 1002+"px";
		ifrm.style.height = 520+"px";
		ifrm.style.position = "absolute";
		ifrm.style.left = 0+"px";
		document.body.appendChild(ifrm);
  	}

</script>
<body>
	<table style="width: 730;" align="center" colspan="6" cellpadding="0" cellspacing="0"	border="1" >		
		<tr>
		<td><a href="#" onclick="displayPayslips();">Show Payslips</a></td>
<!-- 	<td><a href="#" onclick="displayBudget()"> Abstract </a></td>
		<td><html:link action="/payslipapprove/approvePayslip.do">Suplimentary payslip</html:link></td>	
-->		
		</tr>
	</table>
</body>
</html>
