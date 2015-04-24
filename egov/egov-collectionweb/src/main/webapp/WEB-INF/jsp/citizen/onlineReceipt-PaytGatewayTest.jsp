<%@ include file="/includes/taglibs.jsp" %> 
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><s:text name="coc.title"/></title>
<script type="text/javascript">

</script>
</head>
<body>
<s:form name="onlinePaytDetails" action="/citizen/onlineReceipt.action">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" border="1">
	<tr>
		<td>Receipt Id : </td>
		<td><s:textfield name="testReceiptId" id="testReceiptId" value="" /></td>
	</tr>
	<tr>
		<td>Authorisation Status Code : </td>
		<td><s:textfield name="testAuthStatusCode" id="testAuthStatusCode" value="" /></td>
	</tr>
	</table>

<br/>
<div class="buttonbottom">
	<s:submit type="button" value="Test Online Payment Message" method="acceptMessageFromPaymentGateway" align="left"/>
	<input name="button2" type="button" class="button" id="button" onclick="window.close()" value="Close"/>
</div>
</s:form>
</body>
</html>
