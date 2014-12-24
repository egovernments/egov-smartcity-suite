
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Master Setup</title>



<script language="JavaScript"  type="text/JavaScript">
function nextAction()
{
	var employeeId="<%=request.getAttribute("empId")%>";
	window.location ="${pageContext.request.contextPath}/empPension/AfterPensionSearchAction.do?submitType=beforeViewDetails&employeeId="+employeeId;
	
}
function timingex(){
setTimeout("alert('Three seconds has passed.');",3000);
}
 

</script>

</head>
<body onload="timingex();nextAction();" >
<center> <font color="green"><b>Successfully Created/Modified the Pension Details</center>
<html:form  action="/empPension/AfterPensionHeaderAction.do?submitType=beforeViewDetails">
<input type="button" VALUE="Click me!" OnClick="timingex( )">
</html:form>
</body>
</html>